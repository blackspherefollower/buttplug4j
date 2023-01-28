package org.blackspherefollower.buttplug.client.client;

import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.DeviceAdded;
import org.blackspherefollower.buttplug.protocol.messages.DeviceRemoved;
import org.blackspherefollower.buttplug.protocol.messages.LinearCmd;
import org.blackspherefollower.buttplug.protocol.messages.Parts.DeviceMessage;
import org.blackspherefollower.buttplug.protocol.messages.Parts.DeviceMessageInfo;
import org.blackspherefollower.buttplug.protocol.messages.Parts.GenericFeatureAttributes;
import org.blackspherefollower.buttplug.protocol.messages.Parts.GenericMessageAttributes;
import org.blackspherefollower.buttplug.protocol.messages.Parts.MessageAttributes;
import org.blackspherefollower.buttplug.protocol.messages.RotateCmd;
import org.blackspherefollower.buttplug.protocol.messages.ScalarCmd;
import org.blackspherefollower.buttplug.protocol.messages.StopDeviceCmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ButtplugClientDevice {
    private final long deviceIndex;

    private final String name;

    private final String displayName;

    private final Integer messageTimingGap;

    private final Map<String, MessageAttributes> deviceMessages;

    private final ButtplugWSClient client;

    public ButtplugClientDevice(final ButtplugWSClient bpClient, final DeviceMessageInfo deviceMessageInfo) {
        this.client = bpClient;
        this.deviceIndex = deviceMessageInfo.deviceIndex;
        this.name = deviceMessageInfo.deviceName;
        this.displayName = deviceMessageInfo.deviceDisplayName != null
                && !deviceMessageInfo.deviceDisplayName.isEmpty()
                ? deviceMessageInfo.deviceDisplayName : deviceMessageInfo.deviceName;
        this.messageTimingGap = deviceMessageInfo.deviceMessageTimingGap;
        this.deviceMessages = new HashMap<>();
        for (DeviceMessage deviceMessage : deviceMessageInfo.deviceMessages) {
            this.deviceMessages.put(deviceMessage.message, deviceMessage.attributes);
        }
    }

    public ButtplugClientDevice(final ButtplugWSClient bpClient, final DeviceAdded deviceAdded) {
        this.client = bpClient;
        this.deviceIndex = deviceAdded.deviceIndex;
        this.name = deviceAdded.deviceName;
        this.displayName = deviceAdded.deviceDisplayName != null
                && !deviceAdded.deviceDisplayName.isEmpty()
                ? deviceAdded.deviceDisplayName : deviceAdded.deviceName;
        this.messageTimingGap = deviceAdded.deviceMessageTimingGap;
        this.deviceMessages = new HashMap<>();
        for (DeviceMessage deviceMessage : deviceAdded.deviceMessages) {
            this.deviceMessages.put(deviceMessage.message, deviceMessage.attributes);
        }
    }

    public ButtplugClientDevice(final ButtplugWSClient bpClient, final DeviceRemoved deviceRemoved) {
        this.client = bpClient;
        this.deviceIndex = deviceRemoved.deviceIndex;
        this.name = "";
        this.displayName = "";
        this.messageTimingGap = null;
        this.deviceMessages = new HashMap<>();
    }

    public final Future<ButtplugMessage> sendStopDeviceCmd()
            throws IOException, ExecutionException, InterruptedException {
        return client.sendDeviceMessage(this, new StopDeviceCmd(getDeviceIndex(),
                client.getNextMsgId()));
    }

    public final long getScalarCount(final String actuatorType) {
        MessageAttributes attrs = getDeviceMessages().get("ScalarCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            return 0;
        }
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        return gattrs.features.stream().filter(genericFeatureAttributes ->
                genericFeatureAttributes.actuatorType.contentEquals(actuatorType)).count();
    }

    public final Future<ButtplugMessage> sendScalarCmd(final String actuatorType, final double scalar)
            throws IOException, ExecutionException, InterruptedException {

        MessageAttributes attrs = getDeviceMessages().get("ScalarCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new IOException("Device doesn't support ScalarCmd!");
        }

        long count = 0;

        ArrayList<Double> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.features) {
            if (attr.actuatorType.contentEquals(actuatorType)) {
                values.add(scalar);
                count++;
            } else {
                values.add(null);
            }
        }
        if (count == 0) {
            throw new IOException("Device doesn't have any ScalarCmd features that support Rotate!");
        }

        return client.sendDeviceMessage(this, new ScalarCmd(getDeviceIndex(),
                values.toArray(new Double[]{}), actuatorType, client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendScalarVibrateCmd(final double scalar)
            throws IOException, ExecutionException, InterruptedException {
        return sendScalarCmd("Vibrate", scalar);
    }

    public final long getScalarVibrateCount() {
        return getScalarCount("Vibrate");
    }

    public final Future<ButtplugMessage> sendScalarRotateCmd(final double scalar)
            throws IOException, ExecutionException, InterruptedException {
        return sendScalarCmd("Rotate", scalar);
    }

    public final long getScalarRotateCount() {
        return getScalarCount("Rotate");
    }

    public final Future<ButtplugMessage> sendScalarOscillateCmd(final double scalar)
            throws IOException, ExecutionException, InterruptedException {
        return sendScalarCmd("Oscillate", scalar);
    }

    public final long getScalarOscillateCount() {
        return getScalarCount("Oscillate");
    }

    public final Future<ButtplugMessage> sendRotateCmd(final double speed, final boolean clockwise)
            throws IOException, ExecutionException, InterruptedException {

        MessageAttributes attrs = getDeviceMessages().get("RotateCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new IOException("Device doesn't support RotateCmd!");
        }

        long count = 0;

        ArrayList<RotateCmd.RotateSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.features) {
            values.add(new RotateCmd.RotateSubCmd(count++, speed, clockwise));
        }
        if (count == 0) {
            throw new IOException("Device doesn't have any ScalarCmd features that support Rotate!");
        }

        return client.sendDeviceMessage(this, new RotateCmd(getDeviceIndex(),
                values.toArray(new RotateCmd.RotateSubCmd[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendRotateCmd(final long index, final double speed,
                                                       final boolean clockwise)
            throws IOException, ExecutionException, InterruptedException {

        MessageAttributes attrs = getDeviceMessages().get("RotateCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new IOException("Device doesn't support RotateCmd!");
        }

        long count = 0;

        ArrayList<RotateCmd.RotateSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        if (index < 0 || index >= gattrs.features.size()) {
            throw new IOException("Device doesn't have a RotateCmd feature at index " + index + "!");
        }

        for (GenericFeatureAttributes attr : gattrs.features) {
            if (count == index) {
                values.add(new RotateCmd.RotateSubCmd(count++, speed, clockwise));
            } else {
                values.add(null);
                count++;
            }
        }

        return client.sendDeviceMessage(this, new RotateCmd(index,
                values.toArray(new RotateCmd.RotateSubCmd[]{}), client.getNextMsgId()));
    }

    public final long getRotateCount() {
        MessageAttributes attrs = getDeviceMessages().get("RotateCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            return 0;
        }
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        return gattrs.features.size();
    }


    public final Future<ButtplugMessage> sendLinearCmd(final double position, final long duration)
            throws IOException, ExecutionException, InterruptedException {

        MessageAttributes attrs = getDeviceMessages().get("LinearCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new IOException("Device doesn't support LinearCmd!");
        }

        long count = 0;

        ArrayList<LinearCmd.LinearSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.features) {
            values.add(new LinearCmd.LinearSubCmd(count++, position, duration));
        }
        if (count == 0) {
            throw new IOException("Device doesn't have any ScalarCmd features that support Rotate!");
        }

        return client.sendDeviceMessage(this, new LinearCmd(getDeviceIndex(),
                values.toArray(new LinearCmd.LinearSubCmd[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendLinearCmd(final long index, final double position,
                                                       final long duration)
            throws IOException, ExecutionException, InterruptedException {

        MessageAttributes attrs = getDeviceMessages().get("LinearCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new IOException("Device doesn't support LinearCmd!");
        }

        long count = 0;

        ArrayList<LinearCmd.LinearSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        if (index < 0 || index >= gattrs.features.size()) {
            throw new IOException("Device doesn't have a LinearCmd feature at index " + index + "!");
        }

        for (GenericFeatureAttributes attr : gattrs.features) {
            if (count == index) {
                values.add(new LinearCmd.LinearSubCmd(count++, position, duration));
            } else {
                values.add(null);
                count++;
            }
        }

        return client.sendDeviceMessage(this, new LinearCmd(index,
                values.toArray(new LinearCmd.LinearSubCmd[]{}), client.getNextMsgId()));
    }

    public final long getLinearCount() {
        MessageAttributes attrs = getDeviceMessages().get("LinearCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            return 0;
        }
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        return gattrs.features.size();
    }

    public final long getDeviceIndex() {
        return deviceIndex;
    }

    public final String getName() {
        return name;
    }

    public final String getDisplayName() {
        return displayName;
    }

    public final Integer getMessageTimingGap() {
        return messageTimingGap;
    }

    public final Map<String, MessageAttributes> getDeviceMessages() {
        return deviceMessages;
    }
}
