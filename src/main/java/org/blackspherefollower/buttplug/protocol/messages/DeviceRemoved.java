package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public class DeviceRemoved extends ButtplugDeviceMessage {
    public DeviceRemoved(long deviceMessage) {
        super(ButtplugConsts.SystemMsgId, deviceMessage);
    }

    @SuppressWarnings("unused")
    private DeviceRemoved() {
        super(ButtplugConsts.SystemMsgId, -1);
    }
}
