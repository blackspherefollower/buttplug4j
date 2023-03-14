package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

public final class DeviceMessageInfo {

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

    public DeviceMessageInfo(long deviceIndex, String deviceName, ArrayList<DeviceMessage> deviceMessages, int deviceMessageTimingGap, String deviceDisplayName) {
        this.deviceName = deviceName;
        this.deviceIndex = deviceIndex;
        this.deviceMessages = deviceMessages;
        this.deviceMessageTimingGap = deviceMessageTimingGap;
        this.deviceDisplayName = deviceDisplayName;
    }

    @SuppressWarnings("unused")
    private DeviceMessageInfo() {
        this.deviceName = "";
        this.deviceIndex = -1;
        this.deviceMessages = new ArrayList<>();
        this.deviceMessageTimingGap = null;
        this.deviceDisplayName = "";
    }
}
