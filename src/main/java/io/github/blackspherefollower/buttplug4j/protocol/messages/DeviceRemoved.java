package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public final class DeviceRemoved extends ButtplugDeviceMessage {
    public DeviceRemoved(final long deviceMessage) {
        super(ButtplugConsts.SYSTEM_MSG_ID, deviceMessage);
    }

    @SuppressWarnings("unused")
    private DeviceRemoved() {
        super(ButtplugConsts.SYSTEM_MSG_ID, -1);
    }
}
