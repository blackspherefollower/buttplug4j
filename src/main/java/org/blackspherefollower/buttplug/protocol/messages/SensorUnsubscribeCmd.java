package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public class SensorUnsubscribeCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "SensorIndex", required = true)
    private int sensorIndex;
    @JsonProperty(value = "SensorType", required = true)
    private String sensorType;

    public SensorUnsubscribeCmd(long deviceIndex, long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private SensorUnsubscribeCmd() {
        super(ButtplugConsts.DefaultMsgId, -1);
    }
}
