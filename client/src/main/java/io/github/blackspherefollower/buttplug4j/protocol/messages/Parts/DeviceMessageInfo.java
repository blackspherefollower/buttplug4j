package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

public final class DeviceMessageInfo {

    @JsonProperty(value = "DeviceIndex", required = true)
    private long deviceIndex;

    @JsonProperty(value = "DeviceName", required = true)
    private String deviceName;

    @JsonProperty(value = "DeviceMessageTimingGap")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer deviceMessageTimingGap = null;

    @JsonProperty(value = "DeviceDisplayName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String deviceDisplayName;

    public long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(final long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(final String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDeviceMessageTimingGap() {
        return deviceMessageTimingGap;
    }

    public void setDeviceMessageTimingGap(final Integer deviceMessageTimingGap) {
        this.deviceMessageTimingGap = deviceMessageTimingGap;
    }

    public String getDeviceDisplayName() {
        return deviceDisplayName;
    }

    public void setDeviceDisplayName(final String deviceDisplayName) {
        this.deviceDisplayName = deviceDisplayName;
    }

    public ArrayList<DeviceMessage> getDeviceMessages() {
        return deviceMessages;
    }

    public void setDeviceMessages(final ArrayList<DeviceMessage> deviceMessages) {
        this.deviceMessages = deviceMessages;
    }

    @JsonProperty(value = "DeviceMessages", required = true)
    @JsonDeserialize(using = DeviceMessagesDeserializer.class)
    @JsonSerialize(using = DeviceMessagesSerializer.class)
    private ArrayList<DeviceMessage> deviceMessages;

    public DeviceMessageInfo(final long deviceIndex, final String deviceName,
                             final ArrayList<DeviceMessage> deviceMessages, final int deviceMessageTimingGap,
                             final String deviceDisplayName) {
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
