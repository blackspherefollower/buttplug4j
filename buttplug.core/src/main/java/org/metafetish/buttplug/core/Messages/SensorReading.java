package org.metafetish.buttplug.core.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metafetish.buttplug.core.ButtplugConsts;
import org.metafetish.buttplug.core.ButtplugDeviceMessage;

public class SensorReading extends ButtplugDeviceMessage {

    @JsonProperty(value = "SensorIndex", required = true)
    private int sensorIndex;

    @JsonProperty(value = "SensorType", required = true)
    private String sensorType;

    @JsonProperty(value = "Data", required = true)
    private byte[] data;

    public SensorReading(long deviceIndex, long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private SensorReading() {
        super(ButtplugConsts.DefaultMsgId, -1);
    }
}
