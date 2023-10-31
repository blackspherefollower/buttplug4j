package io.github.blackspherefollower.buttplug4j.connectors.javax.websocket.common;

import io.github.blackspherefollower.buttplug4j.client.ButtplugClient;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Error;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@ClientEndpoint
@ServerEndpoint("/")
public abstract class ButtplugClientWSEndpoint extends ButtplugClient {
    private static final int TENSEC = 10000;
    private Session session;
    private Timer wsPingTimer;

    public ButtplugClientWSEndpoint(final String aClientName) {
        super(aClientName);
    }

    public final Session getSession() {
        return session;
    }

    @OnMessage
    public final void onMessage(final Session sess, final String message) {
        try {
            List<ButtplugMessage> msgs = getParser().parseJson(message);
            onMessage(msgs);
        } catch (ButtplugProtocolException e) {
            if (getErrorReceived() != null) {
                getErrorReceived().errorReceived(new Error(e));
            } else {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    @SuppressWarnings("unused")
    public final void onClose(final CloseReason reason) {
        this.session = null;
        setConnectionState(ConnectionState.DISCONNECTED);
    }

    @OnOpen
    @SuppressWarnings("unused")
    public final void onConnect(final Session newSession) {
        this.session = newSession;

        // Setup websocket ping
        wsPingTimer = new Timer("wsPingTimer", true);
        wsPingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (session != null) {
                        session.getAsyncRemote().sendPing(ByteBuffer.wrap("ping".getBytes()));
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

    @OnError
    public final void onWebSocketError(final Throwable cause) {
        if (getErrorReceived() != null) {
            getErrorReceived().errorReceived(new Error(cause));
        } else {
            cause.printStackTrace();
        }
        disconnect();
    }

    @Override
    protected final CompletableFuture<ButtplugMessage> sendMessage(final ButtplugMessage msg) {
        CompletableFuture<ButtplugMessage> promise = scheduleWait(msg.getId(), new CompletableFuture<>());
        if (session == null) {
            Error err = new Error("Bad WS state!",
                    Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SYSTEM_MSG_ID);
            if( getErrorReceived() != null) {
                getErrorReceived().errorReceived(err);
            }
            return CompletableFuture.completedFuture(err);
        }

        try {
            session.getAsyncRemote().sendText(getParser().formatJson(msg)).get();
        } catch (Exception e) {
            Error err = new Error(e, msg.getId());
            if( getErrorReceived() != null) {
                getErrorReceived().errorReceived(err);
            }
            return CompletableFuture.completedFuture(err);
        }
        return promise;
    }

}
