package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public class StopAllDevices extends ButtplugMessage {

    @SuppressWarnings("unused")
    private StopAllDevices() {
        super(ButtplugConsts.DefaultMsgId);
    }

    public StopAllDevices(long id) {
        super(id);
    }
}
