package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class Device {

    @JsonProperty(value = "DeviceIndex", required = true)
    private int deviceIndex;

    @JsonProperty(value = "DeviceName", required = true)
    private String deviceName;

    @JsonProperty(value = "DeviceMessageTimingGap")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer deviceMessageTimingGap = null;

    @JsonProperty(value = "DeviceDisplayName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String deviceDisplayName;

    @JsonProperty(value = "DeviceFeatures", required = true)
    private HashMap<Integer, DeviceFeature> deviceFeatures;

    public Device(final int deviceIndex, final String deviceName,
                  final HashMap<Integer, DeviceFeature> deviceFeatures, final int deviceMessageTimingGap,
                  final String deviceDisplayName) {
        this.deviceName = deviceName;
        this.deviceIndex = deviceIndex;
        this.deviceFeatures = deviceFeatures;
        this.deviceMessageTimingGap = deviceMessageTimingGap;
        this.deviceDisplayName = deviceDisplayName;
    }

    @SuppressWarnings("unused")
    private Device() {
        this.deviceName = "";
        this.deviceIndex = -1;
        this.deviceFeatures = new HashMap<>();
        this.deviceMessageTimingGap = null;
        this.deviceDisplayName = "";
    }

    public int getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(final int deviceIndex) {
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

    public HashMap<Integer, DeviceFeature> getDeviceFeatures() {
        return deviceFeatures;
    }

    public void setDeviceFeatures(final HashMap<Integer, DeviceFeature> deviceFeatures) {
        this.deviceFeatures = deviceFeatures;
    }
}
