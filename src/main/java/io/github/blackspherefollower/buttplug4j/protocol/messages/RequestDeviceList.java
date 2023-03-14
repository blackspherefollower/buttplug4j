package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class RequestDeviceList extends ButtplugMessage {

    @SuppressWarnings("unused")
    private RequestDeviceList() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public RequestDeviceList(final long id) {
        super(id);
    }
}
