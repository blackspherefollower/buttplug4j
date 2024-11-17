package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.*;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.*;
import io.github.blackspherefollower.buttplug4j.util.Pair;

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

    /**
     * Sends a command to read a sensor value from the device.
     * <p>
     * This method constructs and sends a sensor read command to the device for the specified sensor type and index.
     * It checks if the device supports the "SensorReadCmd" attribute before attempting to send the command. 
     * If the device does not support this attribute, or if the command cannot be sent, a {@link ButtplugDeviceException} 
     * is thrown.
     * </p>
     * 
     * @param sensorIndex The index of the sensor feature to read. This value specifies which sensor data 
     *              to retrieve from the device.
     * @param sensorType The type of sensor to read (e.g., "Battery"). This value indicates the specific 
     *                   type of sensor data requested.
     * 
     * @return A {@link Future} representing the pending result of the sensor read command.
     *         Once completed, the {@link ButtplugMessage} returned by the Future will contain 
     *         the sensor data if the command succeeds.
     * 
     * @throws ButtplugDeviceException if the device does not support "SensorReadCmd" or if 
     *                                 the sensor read command could not be created or sent.
     */
    public final Future<ButtplugMessage> sendSensorReadCmd(final int sensorIndex, final String sensorType)
            throws ButtplugDeviceException {

        MessageAttributes attrs = getDeviceMessages().get("SensorReadCmd");
        if (!(attrs instanceof SensorMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support SensorReadCmd!");
        }

        final SensorReadCmd cmd = new SensorReadCmd(this.deviceIndex, ButtplugConsts.DEFAULT_MSG_ID);
        cmd.setSensorType(sensorType);
        cmd.setSensorIndex(sensorIndex);

        return client.sendDeviceMessage(this, cmd);
    }

    /**
     * Checks if the device has a battery sensor feature.
     * <p>
     * This method verifies if the device's message attributes include a "Battery" sensor feature
     * by looking for the presence of "SensorReadCmd" in the device's messages. If found,
     * it then checks if one of the sensor features corresponds to "Battery".
     * </p>
     * 
     * @return {@code true} if the device has a "Battery" sensor feature, {@code false} otherwise.
     */
    public final boolean hasBatterySensor() {
        MessageAttributes attrs = getDeviceMessages().get("SensorReadCmd");
        if (!(attrs instanceof SensorMessageAttributes)) {
            return false;
        }

        SensorMessageAttributes sensorAttrs = (SensorMessageAttributes) attrs;
        
        boolean hasBatteryLevel = sensorAttrs.getFeatures().stream().anyMatch(
            featureAttributes -> "Battery".equals(featureAttributes.getSensorType())
        );

        return hasBatteryLevel;
    }

    /**
     * Reads the battery level of the device.
     * <p>
     * This method queries the device to retrieve its battery level as a percentage from 0 to 100.
     * Before calling this method, use {@link #hasBatterySensor()} to verify that the device supports
     * a "Battery" sensor feature. If the device lacks this feature or returns an unexpected message type,
     * an exception is thrown.
     * </p>
     * 
     * @return The battery level of the device, in the range of 0 to 100.
     * 
     * @throws ButtplugDeviceException if the device does not support "SensorReadCmd",
     *                                 does not have a "Battery" feature, or returns an invalid response.
     * @throws InterruptedException if the thread is interrupted while waiting for a response from the device.
     * @throws ExecutionException if an exception occurred during the execution of the sensor read command.
     */
    public final long readBatteryLevel() throws ButtplugDeviceException, InterruptedException, ExecutionException {
        MessageAttributes attrs = getDeviceMessages().get("SensorReadCmd");
        if (!(attrs instanceof SensorMessageAttributes)) {
            throw new ButtplugDeviceException("Device doesn't support SensorReadCmd!");
        }

        SensorMessageAttributes sensorAttrs = (SensorMessageAttributes) attrs;
        int index = -1;
        boolean found = false;
        for (SensorFeatureAttributes featureAttributes : sensorAttrs.getFeatures()) {
            index++;
            if ("Battery".equals(featureAttributes.getSensorType())) {
               found = true;
               break; 
            }
        }

        if (!found) {
            throw new ButtplugDeviceException("Device doesn't have Battery feature!");
        }

        Future<ButtplugMessage> sensorReadFuture = sendSensorReadCmd(index, "Battery");
        ButtplugMessage message = sensorReadFuture.get();
        if (!(message instanceof SensorReading)) {
            throw new ButtplugDeviceException("Invalid ButtplugMessage returned. Expecting SensorReading and got " + message.getClass());
        }

        SensorReading sensorReading = (SensorReading) message;
        byte singleByte = sensorReading.getData()[0];
        long result = (singleByte & 0xFF);
        return result;
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
