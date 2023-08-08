package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class StopAllDevices extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StopAllDevices() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public StopAllDevices(final long id) {
        super(id);
    }
}
