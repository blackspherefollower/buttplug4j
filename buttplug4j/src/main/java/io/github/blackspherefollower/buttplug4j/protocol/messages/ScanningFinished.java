package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class ScanningFinished extends ButtplugMessage {

    public ScanningFinished() {
        super(ButtplugConsts.SYSTEM_MSG_ID);
    }
}
