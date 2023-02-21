package org.blackspherefollower.buttplug.client;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.RequestServerInfo;
import org.blackspherefollower.buttplug.protocol.messages.ServerInfo;
import org.blackspherefollower.buttplug.protocol.messages.Error;
import org.eclipse.jetty.util.component.LifeCycle;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@ClientEndpoint
public final class ButtplugClientWSClient extends ButtplugClientWSEndpoint {

    private WebSocketContainer client;

    public ButtplugClientWSClient(final String clientName) {
        super(clientName);
    }

    public void connect(final URI url) throws Exception {

        if (client != null && session != null && session.isOpen()) {
            throw new IllegalStateException("WS is already open");
        }

        CompletableFuture<Boolean> promise = new CompletableFuture<>();
        setOnConnected(new IConnectedEvent() {
            @Override
            public void onConnected(final ButtplugClientWSEndpoint client) {
                promise.complete(true);
            }
        });

        client = ContainerProvider.getWebSocketContainer();
        client.connectToServer(this, url);
        promise.get();
    }

    public void disconnect() {
        if (pingTimer != null) {
            pingTimer.cancel();
            pingTimer = null;
        }

        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                // noop - something when wrong closing the socket, but we're
                // about to dispose of it anyway.
            }
        }

        LifeCycle.stop(client);
        client = null;

        int max = MAX_DISCONNECT_MESSAGE_TRYS;
        while (max-- > 0 && waitingMsgs.size() != 0) {
            for (long waitMmsgId : waitingMsgs.keySet()) {
                CompletableFuture<ButtplugMessage> val = waitingMsgs.remove(waitMmsgId);
                if (val != null) {
                    val.complete(new Error("Connection closed!",
                            Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SYSTEM_MSG_ID));
                }
            }
        }

        msgId.set(1);
    }
}
