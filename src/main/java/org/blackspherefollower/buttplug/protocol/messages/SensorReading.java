package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

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
