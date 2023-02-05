package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public final class SensorReadCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "SensorIndex", required = true)
    private int sensorIndex;
    @JsonProperty(value = "SensorType", required = true)
    private String sensorType;

    public SensorReadCmd(final long deviceIndex, final long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private SensorReadCmd() {
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
}
