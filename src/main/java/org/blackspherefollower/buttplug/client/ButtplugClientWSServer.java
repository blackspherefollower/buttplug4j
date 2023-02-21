package org.blackspherefollower.buttplug.client;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.Error;
import org.blackspherefollower.buttplug.protocol.messages.RequestServerInfo;
import org.blackspherefollower.buttplug.protocol.messages.ServerInfo;
import org.eclipse.jetty.util.component.LifeCycle;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@ServerEndpoint("/")
public final class ButtplugClientWSServer extends ButtplugClientWSEndpoint {

    public ButtplugClientWSServer(final String clientName) {
        super(clientName);
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
    }
}
