package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.util.Pair;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceAdded;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceRemoved;
import io.github.blackspherefollower.buttplug4j.protocol.messages.LinearCmd;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.DeviceMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.DeviceMessageInfo;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.GenericFeatureAttributes;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.GenericMessageAttributes;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.MessageAttributes;
import io.github.blackspherefollower.buttplug4j.protocol.messages.RotateCmd;
import io.github.blackspherefollower.buttplug4j.protocol.messages.ScalarCmd;
import io.github.blackspherefollower.buttplug4j.protocol.messages.StopDeviceCmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;


public class ButtplugClientDevice {
    private final long deviceIndex;

    private final String name;

    private final String displayName;

    private final Integer messageTimingGap;

    private final Map<String, MessageAttributes> deviceMessages;

    private final ButtplugClient client;

    public ButtplugClientDevice(final ButtplugClient bpClient, final DeviceMessageInfo deviceMessageInfo) {
        this.client = bpClient;
        this.deviceIndex = deviceMessageInfo.getDeviceIndex();
        this.name = deviceMessageInfo.getDeviceName();
        this.displayName = deviceMessageInfo.getDeviceDisplayName() != null
                && !deviceMessageInfo.getDeviceDisplayName().isEmpty()
                ? deviceMessageInfo.getDeviceDisplayName() : deviceMessageInfo.getDeviceName();
        this.messageTimingGap = deviceMessageInfo.getDeviceMessageTimingGap();
        this.deviceMessages = new HashMap<>();
        for (DeviceMessage deviceMessage : deviceMessageInfo.getDeviceMessages()) {
            this.deviceMessages.put(deviceMessage.getMessage(), deviceMessage.getAttributes());
        }
    }

    public ButtplugClientDevice(final ButtplugClient bpClient, final DeviceAdded deviceAdded) {
        this.client = bpClient;
        this.deviceIndex = deviceAdded.getDeviceIndex();
        this.name = deviceAdded.getDeviceName();
        this.displayName = deviceAdded.getDeviceDisplayName() != null
                && !deviceAdded.getDeviceDisplayName().isEmpty()
                ? deviceAdded.getDeviceDisplayName() : deviceAdded.getDeviceName();
        this.messageTimingGap = deviceAdded.getDeviceMessageTimingGap();
        this.deviceMessages = new HashMap<>();
        for (DeviceMessage deviceMessage : deviceAdded.getDeviceMessages()) {
            this.deviceMessages.put(deviceMessage.getMessage(), deviceMessage.getAttributes());
        }
    }

    public ButtplugClientDevice(final ButtplugClient bpClient, final DeviceRemoved deviceRemoved) {
        this.client = bpClient;
        this.deviceIndex = deviceRemoved.getDeviceIndex();
        this.name = "";
        this.displayName = "";
        this.messageTimingGap = null;
        this.deviceMessages = new HashMap<>();
    }

    public final Future<ButtplugMessage> sendStopDeviceCmd() {
        return client.sendDeviceMessage(this, new StopDeviceCmd(getDeviceIndex(),
                client.getNextMsgId()));
    }

    public final long getScalarCount(final String actuatorType) {
        MessageAttributes attrs = getDeviceMessages().get("ScalarCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            return 0;
        }
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        return gattrs.getFeatures().stream().filter(genericFeatureAttributes ->
                genericFeatureAttributes.getActuatorType().contentEquals(actuatorType)).count();
    }

    public final Future<ButtplugMessage> sendScalarCmd(final String actuatorType, final double scalar)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("ScalarCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support ScalarCmd!");
        }

        long count = 0;

        ArrayList<Pair<Double, String>> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
            if (attr.getActuatorType().contentEquals(actuatorType)) {
                values.add(new Pair(scalar, actuatorType));
                count++;
            } else {
                values.add(null);
            }
        }
        if (count == 0) {
            throw new ButtplugDeviceException("Device doesn't have any ScalarCmd features that support "
                    + actuatorType + "!");
        }

        return client.sendDeviceMessage(this, new ScalarCmd(getDeviceIndex(),
                values.toArray(new Pair[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendScalarCmd(final String actuatorType, final long index, final double scalar)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("ScalarCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support ScalarCmd!");
        }

        long count = 0;

        ArrayList<Pair<Double, String>> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
            if (attr.getActuatorType().contentEquals(actuatorType) && count++ == index) {
                values.add(new Pair(scalar, actuatorType));
            } else {
                values.add(null);
            }
        }
        if (count == 0) {
            throw new ButtplugDeviceException("Device doesn't have any ScalarCmd features that support "
                    + actuatorType + " at index " + index + "!");
        }

        return client.sendDeviceMessage(this, new ScalarCmd(getDeviceIndex(),
                values.toArray(new Pair[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendScalarCmd(final String actuatorType, final Double[] scalars)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("ScalarCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support ScalarCmd!");
        }

        int count = 0;
        int index = 0;
        HashMap<Integer, Integer> indexMap = new HashMap<>();

        ArrayList<Pair<Double, String>> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
            if (attr.getActuatorType().contentEquals(actuatorType)) {
                indexMap.put(count++, index);
            }
            values.add(null);
            index++;
        }
        if (count == 0) {
            throw new ButtplugDeviceException("Device doesn't have any ScalarCmd features that support "
                    + actuatorType + "!");
        }

        for (index = 0; index < scalars.length; index++) {
            if (scalars[index] == null) {
                continue;
            }
            if (index >= count) {
                throw new ButtplugDeviceException("Device doesn't have any ScalarCmd features that support "
                        + actuatorType + " at index " + index + "!");
            }
            values.set(indexMap.get(index), new Pair<>(scalars[index], actuatorType));
        }


        return client.sendDeviceMessage(this, new ScalarCmd(getDeviceIndex(),
                values.toArray(new Pair[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendScalarCmd(final Pair<Double, String>[] scalars)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("ScalarCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support ScalarCmd!");
        }

        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (int index = 0; index < scalars.length; index++) {
            if (index >= gattrs.getFeatures().size()) {
                throw new ButtplugDeviceException("Device doesn't have any ScalarCmd features at index " + index + "!");
            }
            if (!scalars[index].getRight().contentEquals(gattrs.getFeatures().get(index).getActuatorType())) {
                throw new ButtplugDeviceException("Device doesn't have a ScalarCmd feature of type "
                        + scalars[index].getRight() + " at index " + index + " (found "
                        + gattrs.getFeatures().get(index).getActuatorType() + " instead)!");
            }
        }

        return client.sendDeviceMessage(this, new ScalarCmd(getDeviceIndex(),
                scalars, client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendScalarVibrateCmd(final double scalar)
            throws ButtplugDeviceException {
        return sendScalarCmd("Vibrate", scalar);
    }

    public final long getScalarVibrateCount() {
        return getScalarCount("Vibrate");
    }

    public final Future<ButtplugMessage> sendScalarRotateCmd(final double scalar)
            throws ButtplugDeviceException {
        return sendScalarCmd("Rotate", scalar);
    }

    public final long getScalarRotateCount() {
        return getScalarCount("Rotate");
    }

    public final Future<ButtplugMessage> sendScalarOscillateCmd(final double scalar)
            throws ButtplugDeviceException {
        return sendScalarCmd("Oscillate", scalar);
    }

    public final long getScalarOscillateCount() {
        return getScalarCount("Oscillate");
    }

    public final Future<ButtplugMessage> sendRotateCmd(final double speed, final boolean clockwise)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("RotateCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support RotateCmd!");
        }

        long count = 0;

        ArrayList<RotateCmd.RotateSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
            values.add(new RotateCmd.RotateSubCmd(count++, speed, clockwise));
        }
        if (count == 0) {
            throw new ButtplugDeviceException("Device doesn't have any Rotate features!");
        }

        return client.sendDeviceMessage(this, new RotateCmd(getDeviceIndex(),
                values.toArray(new RotateCmd.RotateSubCmd[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendRotateCmd(final long index, final double speed,
                                                       final boolean clockwise)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("RotateCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support RotateCmd!");
        }

        long count = 0;

        ArrayList<RotateCmd.RotateSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        if (index < 0 || index >= gattrs.getFeatures().size()) {
            throw new ButtplugDeviceException("Device doesn't have a RotateCmd feature at index " + index + "!");
        }

        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
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

    public final Future<ButtplugMessage> sendRotateCmd(final Pair<Double, Boolean>[] vectors)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("Rotate");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support RotateCmd!");
        }

        ArrayList<RotateCmd.RotateSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
            values.add(null);
        }

        for (int index = 0; index < vectors.length; index++) {
            if (index >= gattrs.getFeatures().size()) {
                throw new ButtplugDeviceException("Device doesn't have a RotateCmd feature at index " + index + "!");
            }
            values.set(index, new RotateCmd.RotateSubCmd(index, vectors[index].getLeft(), vectors[index].getRight()));
        }

        return client.sendDeviceMessage(this, new RotateCmd(getDeviceIndex(),
                values.toArray(new RotateCmd.RotateSubCmd[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendLinearCmd(final double position, final long duration)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("LinearCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support LinearCmd!");
        }

        long count = 0;

        ArrayList<LinearCmd.LinearSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
            values.add(new LinearCmd.LinearSubCmd(count++, position, duration));
        }
        if (count == 0) {
            throw new ButtplugDeviceException("Device doesn't have any LinearCmd features!");
        }

        return client.sendDeviceMessage(this, new LinearCmd(getDeviceIndex(),
                values.toArray(new LinearCmd.LinearSubCmd[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendLinearCmd(final long index, final double position,
                                                       final long duration)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("LinearCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support LinearCmd!");
        }

        long count = 0;

        ArrayList<LinearCmd.LinearSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        if (index < 0 || index >= gattrs.getFeatures().size()) {
            throw new ButtplugDeviceException("Device doesn't have a LinearCmd feature at index " + index + "!");
        }

        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
            if (count == index) {
                values.add(new LinearCmd.LinearSubCmd(count++, position, duration));
            } else {
                values.add(null);
                count++;
            }
        }

        return client.sendDeviceMessage(this, new LinearCmd(getDeviceIndex(),
                values.toArray(new LinearCmd.LinearSubCmd[]{}), client.getNextMsgId()));
    }

    public final Future<ButtplugMessage> sendLinearCmd(final Pair<Double, Long>[] vectors)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("LinearCmd");
        if (!(attrs instanceof GenericMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support LinearCmd!");
        }

        ArrayList<LinearCmd.LinearSubCmd> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        for (GenericFeatureAttributes attr : gattrs.getFeatures()) {
            values.add(null);
        }

        for (int index = 0; index < vectors.length; index++) {
            if (index >= gattrs.getFeatures().size()) {
                throw new ButtplugDeviceException("Device doesn't have a LinearCmd feature at index " + index + "!");
            }
            values.set(index, new LinearCmd.LinearSubCmd(index, vectors[index].getLeft(), vectors[index].getRight()));
        }

        return client.sendDeviceMessage(this, new LinearCmd(getDeviceIndex(),
                values.toArray(new LinearCmd.LinearSubCmd[]{}), client.getNextMsgId()));
    }

    public final long getRotateCount() {
        return getFeatureCount("RotateCmd");
    }

    public final long getLinearCount() {
        return getFeatureCount("LinearCmd");
    }

    private long getFeatureCount(final String command) {
        MessageAttributes attrs = getDeviceMessages().get(command);
        if (!(attrs instanceof GenericMessageAttributes)) {
            return 0;
        }
        GenericMessageAttributes gattrs = (GenericMessageAttributes) attrs;
        return gattrs.getFeatures().size();
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
