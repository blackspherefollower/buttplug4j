package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public final class StopAllDevices extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StopAllDevices() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public StopAllDevices(final long id) {
        super(id);
    }
}
