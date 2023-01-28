package org.blackspherefollower.buttplug.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ButtplugDeviceMessage extends ButtplugMessage {

    @JsonProperty(value = "DeviceIndex", required = true)
    public long deviceIndex;

    public ButtplugDeviceMessage(long id, long deviceIndex) {
        super(id);
        this.deviceIndex = deviceIndex;
    }

}
