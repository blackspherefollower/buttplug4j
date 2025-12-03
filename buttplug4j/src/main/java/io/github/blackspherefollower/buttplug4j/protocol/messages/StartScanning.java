package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class StartScanning extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StartScanning() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public StartScanning(final int id) {
        super(id);
    }
}
