package io.github.blackspherefollower.buttplug4j.client;

import org.eclipse.jetty.util.component.LifeCycle;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@ClientEndpoint
public final class ButtplugClientWSClient extends ButtplugClientWSEndpoint {

    private WebSocketContainer client;

    public ButtplugClientWSClient(final String clientName) {
        super(clientName);
    }

    public void connect(final URI url) throws Exception {

        if (client != null && getSession() != null && getSession().isOpen()) {
            throw new IllegalStateException("WS is already open");
        }
        setConnectionState(ConnectionState.CONNECTING);

        CompletableFuture<Boolean> promise = new CompletableFuture<>();
        setOnConnected(client -> promise.complete(true));

        client = ContainerProvider.getWebSocketContainer();
        client.connectToServer(this, url);
        promise.get();
    }

    protected void cleanup() {

        if (getSession() != null) {
            try {
                getSession().close();
            } catch (IOException e) {
                // noop - something when wrong closing the socket, but we're
                // about to dispose of it anyway.
            }
        }

        LifeCycle.stop(client);
        client = null;
    }


}
