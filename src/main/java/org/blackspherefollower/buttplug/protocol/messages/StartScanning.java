package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public final class StartScanning extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StartScanning() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public StartScanning(final long id) {
        super(id);
    }
}
