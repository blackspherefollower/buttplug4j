package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.ButtplugException;

public class ButtplugClientException extends ButtplugException {
    public ButtplugClientException(final String errorMessage) {
        setMessage(errorMessage);
    }
}
