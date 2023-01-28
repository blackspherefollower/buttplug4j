package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public class StopScanning extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StopScanning() {
        super(ButtplugConsts.DefaultMsgId);
    }

    public StopScanning(long id) {
        super(id);
    }
}
