package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.ButtplugException;

public class ButtplugDeviceFeatureException extends ButtplugException {
    public ButtplugDeviceFeatureException(final String cmd) {
        super();
        setMessage("Buttplug Device Feature does not support " + cmd);
    }
}
