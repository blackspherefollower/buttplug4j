package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public final class Ping extends ButtplugMessage {

    @SuppressWarnings("unused")
    private Ping() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public Ping(final long id) {
        super(id);
    }
}
