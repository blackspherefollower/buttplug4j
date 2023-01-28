package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public class Ok extends ButtplugMessage {

    public Ok(long id) {
        super(id);
    }

    @SuppressWarnings("unused")
    private Ok() {
        super(ButtplugConsts.DefaultMsgId);
    }
}
