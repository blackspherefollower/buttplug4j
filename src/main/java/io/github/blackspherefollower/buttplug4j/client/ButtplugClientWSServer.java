package io.github.blackspherefollower.buttplug4j.client;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/")
public final class ButtplugClientWSServer extends ButtplugClientWSEndpoint {

    public ButtplugClientWSServer(final String clientName) {
        super(clientName);
        setConnectionState(ConnectionState.CONNECTING);
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
    }

}
