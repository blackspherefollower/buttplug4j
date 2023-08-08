package io.github.blackspherefollower.buttplug4j.connectors.jetty.websocket.client;

import io.github.blackspherefollower.buttplug4j.client.ButtplugClient;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Error;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@WebSocket(maxTextMessageSize = 64 * 1024)
public final class ButtplugClientWSClient extends ButtplugClient {

    private WebSocketClient client;

    private Session session;

    public ButtplugClientWSClient(final String clientName) {
        super(clientName);
    }

    public void connect(final URI url) throws Exception {

        if (client != null && session != null && session.isOpen()) {
            throw new IllegalStateException("WS is already open");
        }
        setConnectionState(ButtplugClient.ConnectionState.CONNECTING);

        CompletableFuture<Boolean> promise = new CompletableFuture<>();
        setOnConnected(client -> promise.complete(true));

        client = new WebSocketClient();
        client.start();
        client.connect(this, url, new ClientUpgradeRequest()).get();
        promise.get();
    }

    protected void cleanup() {

        if (session != null) {
            session.close();
        }

        LifeCycle.stop(client);
        client = null;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        this.session = null;
        setConnectionState(ConnectionState.DISCONNECTED);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;

        // Setup websocket ping
        wsPingTimer = new Timer("wsPingTimer", true);
        wsPingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (session != null) {
                        session.getRemote().sendPing(ByteBuffer.wrap("ping".getBytes()));
                    }
                } catch (IOException e) {
                    wsPingTimer.cancel();
                    wsPingTimer = null;
                    throw new RuntimeException(e);
                }
            }
        }, 0, TENSEC);

        // Don't block the WS thread
        new Thread(() -> doHandshake()).start();
    }

    private Timer wsPingTimer;


    private static final int TENSEC = 10000;

    @OnWebSocketMessage
    public final void onMessage(final Session sess, final String message) {
        try {
            List<ButtplugMessage> msgs = getParser().parseJson(message);
            onMessage(msgs);
        } catch (ButtplugProtocolException e) {
            if (getErrorReceived() != null) {
                getErrorReceived().errorReceived(new Error(e.getMessage(),
                        Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SYSTEM_MSG_ID));
            } else {
                e.printStackTrace();
            }
        }
    }

    @OnWebSocketError
    public void onWebSocketError(final Throwable cause) {
        if (getErrorReceived() != null) {
            getErrorReceived().errorReceived(new Error(cause.getMessage(), Error.ErrorClass.ERROR_UNKNOWN,
                    ButtplugConsts.SYSTEM_MSG_ID));
        }
        disconnect();
    }

    @Override
    protected CompletableFuture<ButtplugMessage> sendMessage(final ButtplugMessage msg) {
        CompletableFuture<ButtplugMessage> promise = scheduleWait(msg.getId(), new CompletableFuture<>());
        if (session == null) {
            return CompletableFuture.completedFuture(new Error("Bad WS state!",
                    Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SYSTEM_MSG_ID));
        }

        try {
            session.getRemote().sendStringByFuture(getParser().formatJson(msg)).get();
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new Error(e.getMessage(),
                    Error.ErrorClass.ERROR_UNKNOWN, msg.getId()));
        }
        return promise;
    }
}
