package io.github.blackspherefollower.buttplug4j.connectors.javax.websocket.client;

import io.github.blackspherefollower.buttplug4j.client.ButtplugClient;
import io.github.blackspherefollower.buttplug4j.connectors.javax.websocket.common.ButtplugClientWSEndpoint;
import org.eclipse.jetty.util.component.LifeCycle;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ClientEndpoint
public final class ButtplugClientWSClient extends ButtplugClientWSEndpoint {

    private WebSocketContainer client;

    public ButtplugClientWSClient(final String clientName) {
        super(clientName);
    }

    public void connect(final URI url) throws IllegalStateException, DeploymentException, IOException,
            ExecutionException, InterruptedException {

        if (client != null && getSession() != null && getSession().isOpen()) {
            throw new IllegalStateException("WS is already open");
        }
        setConnectionState(ButtplugClient.ConnectionState.CONNECTING);

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
