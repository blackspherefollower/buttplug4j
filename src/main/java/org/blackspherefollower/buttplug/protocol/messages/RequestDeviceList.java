package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public class RequestDeviceList extends ButtplugMessage {

    @SuppressWarnings("unused")
    private RequestDeviceList() {
        super(ButtplugConsts.DefaultMsgId);
    }

    public RequestDeviceList(long id) {
        super(id);
    }
}
