package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public final class StopDeviceCmd extends ButtplugDeviceMessage {

    public StopDeviceCmd(final long deviceIndex, final long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private StopDeviceCmd() {
        super(ButtplugConsts.DEFAULT_MSG_ID, -1);
    }
}
