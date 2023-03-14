package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public final class SensorReading extends ButtplugDeviceMessage {

    @JsonProperty(value = "SensorIndex", required = true)
    private int sensorIndex;

    @JsonProperty(value = "SensorType", required = true)
    private String sensorType;

    @JsonProperty(value = "Data", required = true)
    private byte[] data;

    public SensorReading(final long deviceIndex, final long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private SensorReading() {
        super(ButtplugConsts.DEFAULT_MSG_ID, -1);
    }

    public int getSensorIndex() {
        return sensorIndex;
    }

    public void setSensorIndex(final int sensorIndex) {
        this.sensorIndex = sensorIndex;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(final String sensorType) {
        this.sensorType = sensorType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(final byte[] data) {
        this.data = data;
    }
}
