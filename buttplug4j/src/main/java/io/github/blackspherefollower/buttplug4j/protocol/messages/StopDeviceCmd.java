package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public final class StopDeviceCmd extends ButtplugDeviceMessage {

    public StopDeviceCmd(final int id, final long deviceIndex) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private StopDeviceCmd() {
        super(ButtplugConsts.DEFAULT_MSG_ID, -1);
    }
}
