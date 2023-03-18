package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.ButtplugException;

public class ButtplugDeviceException extends ButtplugException {
    public ButtplugDeviceException(final String errorMessage) {
        setMessage(errorMessage);
    }
}
