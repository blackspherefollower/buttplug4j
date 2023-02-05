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

public final class DeviceAdded extends ButtplugDeviceMessage {
    @JsonProperty(value = "DeviceName", required = true)
    private String deviceName;

    @JsonProperty(value = "DeviceMessageTimingGap")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer deviceMessageTimingGap = null;

    @JsonProperty(value = "DeviceDisplayName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String deviceDisplayName;

    @JsonProperty(value = "DeviceMessages", required = true)
    @JsonDeserialize(using = DeviceMessagesDeserializer.class)
    @JsonSerialize(using = DeviceMessagesSerializer.class)
    private ArrayList<DeviceMessage> deviceMessages;

    public DeviceAdded(final long deviceIndex, final String deviceName, final ArrayList<DeviceMessage> deviceMessages) {
        super(ButtplugConsts.SYSTEM_MSG_ID, deviceIndex);

        this.setDeviceName(deviceName);
        this.setDeviceMessages(deviceMessages);
    }

    @SuppressWarnings("unused")
    private DeviceAdded() {
        super(ButtplugConsts.SYSTEM_MSG_ID, 0);
        this.setDeviceName("");
        this.setDeviceMessages(new ArrayList<>());
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
}
