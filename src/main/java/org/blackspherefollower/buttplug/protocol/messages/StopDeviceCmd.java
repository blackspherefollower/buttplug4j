package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public class StopDeviceCmd extends ButtplugDeviceMessage {

    public StopDeviceCmd(long deviceIndex, long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private StopDeviceCmd() {
        super(ButtplugConsts.DefaultMsgId, -1);
    }
}
