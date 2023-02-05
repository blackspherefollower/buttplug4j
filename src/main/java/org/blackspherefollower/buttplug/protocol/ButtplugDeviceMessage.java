package org.blackspherefollower.buttplug.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ButtplugDeviceMessage extends ButtplugMessage {

    @JsonProperty(value = "DeviceIndex", required = true)
    private long deviceIndex;

    public ButtplugDeviceMessage(final long id, final long deviceIndex) {
        super(id);
        this.setDeviceIndex(deviceIndex);
    }

    public final long getDeviceIndex() {
        return deviceIndex;
    }

    public final void setDeviceIndex(final long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }
}
