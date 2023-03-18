package io.github.blackspherefollower.buttplug4j.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.blackspherefollower.buttplug4j.ButtplugException;

public class ButtplugProtocolException extends ButtplugException {
    public ButtplugProtocolException(final JsonProcessingException e) {
        setMessage("Buttplug JSON message exception");
        this.initCause(e);
    }
}
