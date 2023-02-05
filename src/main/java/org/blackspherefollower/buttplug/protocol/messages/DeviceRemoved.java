package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public final class DeviceRemoved extends ButtplugDeviceMessage {
    public DeviceRemoved(final long deviceMessage) {
        super(ButtplugConsts.SYSTEM_MSG_ID, deviceMessage);
    }

    @SuppressWarnings("unused")
    private DeviceRemoved() {
        super(ButtplugConsts.SYSTEM_MSG_ID, -1);
    }
}
