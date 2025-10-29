package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class StopScanning extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StopScanning() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public StopScanning(final int id) {
        super(id);
    }
}
