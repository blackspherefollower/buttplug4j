package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Device;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceFeature;
import io.github.blackspherefollower.buttplug4j.protocol.messages.OutputCmd;
import io.github.blackspherefollower.buttplug4j.protocol.messages.StopDeviceCmd;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;


public class ButtplugClientDevice {

    private final ButtplugClient client;
    private final int deviceIndex;
    private final String deviceName;
    private final String deviceDisplayName;
    private final HashMap<Integer, ButtplugClientDeviceFeature> deviceFeatures = new HashMap<>();
    private Integer deviceMessageTimingGap = null;

    public ButtplugClientDevice(final ButtplugClient bpClient, final Device device) {
        this.client = bpClient;
        this.deviceIndex = device.getDeviceIndex();
        this.deviceName = device.getDeviceName();
        this.deviceDisplayName = device.getDeviceDisplayName() != null
                && !device.getDeviceDisplayName().isEmpty()
                ? device.getDeviceDisplayName() : device.getDeviceName();
        this.deviceMessageTimingGap = device.getDeviceMessageTimingGap();
        if (device.getDeviceFeatures() != null) {
            for (Map.Entry<Integer, DeviceFeature> feature : device.getDeviceFeatures().entrySet()) {
                this.deviceFeatures.put(feature.getKey(), new ButtplugClientDeviceFeature(this, feature.getValue()));
            }
        }
    }

    public final Future<ButtplugMessage> sendStopDeviceCmd() {
        return client.sendMessage(new StopDeviceCmd(client.getNextMsgId(), getDeviceIndex()));
    }

    public final int getDeviceIndex() {
        return deviceIndex;
    }

    public final String getName() {
        return deviceName;
    }

    public final String getDisplayName() {
        return deviceDisplayName;
    }

    public final Integer getMessageTimingGap() {
        return deviceMessageTimingGap;
    }

    public final Map<Integer, ButtplugClientDeviceFeature> getDeviceFeatures() {
        return deviceFeatures;
    }

    public Future<ButtplugMessage> sendOutputCommand(int featureIndex, OutputCmd.IOutputCommand outputCommand) {
        OutputCmd cmd = new OutputCmd(client.getNextMsgId(), deviceIndex, featureIndex);
        cmd.setCommand(outputCommand);
        return client.sendMessage(cmd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ButtplugClientDevice that = (ButtplugClientDevice) o;
        if (deviceIndex != that.deviceIndex ||
                !deviceName.equals(that.deviceName) ||
                !deviceDisplayName.equals(that.deviceDisplayName) ||
                !Objects.equals(deviceMessageTimingGap, that.deviceMessageTimingGap) ||
                (deviceFeatures == null) != (that.deviceFeatures == null)) {
            return false;
        }
        if (deviceFeatures == null) {
            return true;
        }
        if (deviceFeatures.size() != that.deviceFeatures.size() ||
                !deviceFeatures.keySet().containsAll(that.deviceFeatures.keySet())) {
            return false;
        }
        for (Integer feat : deviceFeatures.keySet()) {
            if (!deviceFeatures.get(feat).equals(that.deviceFeatures.get(feat))) {
                return false;
            }
        }
        return true;
    }
}
