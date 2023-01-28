package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public class ScanningFinished extends ButtplugMessage {

    public ScanningFinished() {
        super(ButtplugConsts.SystemMsgId);
    }
}
