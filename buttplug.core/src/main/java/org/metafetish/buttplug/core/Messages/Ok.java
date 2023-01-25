package org.metafetish.buttplug.core.Messages;

import org.metafetish.buttplug.core.ButtplugConsts;
import org.metafetish.buttplug.core.ButtplugMessage;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class Ok extends ButtplugMessage {

    public Ok(long id) {
        super(id);
    }

    @SuppressWarnings("unused")
    private Ok() {
        super(ButtplugConsts.DefaultMsgId);
    }
}
