package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;
import org.blackspherefollower.buttplug.protocol.messages.Parts.DeviceMessage;
import org.blackspherefollower.buttplug.protocol.messages.Parts.DeviceMessagesDeserializer;
import org.blackspherefollower.buttplug.protocol.messages.Parts.DeviceMessagesSerializer;

import java.util.ArrayList;

public class DeviceAdded extends ButtplugDeviceMessage {

    @JsonProperty(value = "DeviceIndex", required = true)
    public long deviceIndex;

    @JsonProperty(value = "DeviceName", required = true)
    public String deviceName;

    @JsonProperty(value = "DeviceMessageTimingGap")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Integer deviceMessageTimingGap = null;

    @JsonProperty(value = "DeviceDisplayName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String deviceDisplayName;

    @JsonProperty(value = "DeviceMessages", required = true)
    @JsonDeserialize(using = DeviceMessagesDeserializer.class)
    @JsonSerialize(using = DeviceMessagesSerializer.class)
    public ArrayList<DeviceMessage> deviceMessages;

    public DeviceAdded(long deviceIndex, String deviceName, ArrayList<DeviceMessage> deviceMessages) {
        super(ButtplugConsts.SystemMsgId, deviceIndex);

        this.deviceName = deviceName;
        this.deviceMessages = deviceMessages;
    }

    @SuppressWarnings("unused")
    private DeviceAdded() {
        super(ButtplugConsts.SystemMsgId, 0);
        this.deviceName = "";
        this.deviceMessages = new ArrayList<>();
    }
}
