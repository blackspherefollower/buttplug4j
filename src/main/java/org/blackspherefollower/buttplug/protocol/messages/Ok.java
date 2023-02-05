package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public final class Ok extends ButtplugMessage {

    public Ok(final long id) {
        super(id);
    }

    @SuppressWarnings("unused")
    private Ok() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }
}
