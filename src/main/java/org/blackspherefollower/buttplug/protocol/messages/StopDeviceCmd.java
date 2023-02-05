package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public final class StopDeviceCmd extends ButtplugDeviceMessage {

    public StopDeviceCmd(final long deviceIndex, final long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private StopDeviceCmd() {
        super(ButtplugConsts.DEFAULT_MSG_ID, -1);
    }
}
