package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class Ping extends ButtplugMessage {

    @SuppressWarnings("unused")
    private Ping() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public Ping(final int id) {
        super(id);
    }
}
