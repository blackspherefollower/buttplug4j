package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public class Ping extends ButtplugMessage {

    @SuppressWarnings("unused")
    private Ping() {
        super(ButtplugConsts.DefaultMsgId);
    }

    public Ping(long id) {
        super(id);
    }
}
