package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public class StartScanning extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StartScanning() {
        super(ButtplugConsts.DefaultMsgId);
    }

    public StartScanning(long id) {
        super(id);
    }
}
