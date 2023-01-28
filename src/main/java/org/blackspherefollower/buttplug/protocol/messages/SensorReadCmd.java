package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public class SensorReadCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "SensorIndex", required = true)
    private int sensorIndex;
    @JsonProperty(value = "SensorType", required = true)
    private String sensorType;

    public SensorReadCmd(long deviceIndex, long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private SensorReadCmd() {
        super(ButtplugConsts.DefaultMsgId, -1);
    }
}
