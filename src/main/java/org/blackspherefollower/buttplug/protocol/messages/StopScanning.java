package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public final class StopScanning extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StopScanning() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public StopScanning(final long id) {
        super(id);
    }
}
