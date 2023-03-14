package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public final class SensorUnsubscribeCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "SensorIndex", required = true)
    private int sensorIndex;
    @JsonProperty(value = "SensorType", required = true)
    private String sensorType;

    public SensorUnsubscribeCmd(final long deviceIndex, final long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private SensorUnsubscribeCmd() {
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

    public  void setSensorType(final String sensorType) {
        this.sensorType = sensorType;
    }
}
