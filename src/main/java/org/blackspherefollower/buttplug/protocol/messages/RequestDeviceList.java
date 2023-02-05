package org.blackspherefollower.buttplug.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public final class RequestDeviceList extends ButtplugMessage {

    @SuppressWarnings("unused")
    private RequestDeviceList() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public RequestDeviceList(final long id) {
        super(id);
    }
}
