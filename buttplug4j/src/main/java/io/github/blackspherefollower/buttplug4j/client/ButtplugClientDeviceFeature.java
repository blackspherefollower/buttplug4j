package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.ButtplugException;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceFeature;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Error;
import io.github.blackspherefollower.buttplug4j.protocol.messages.InputReading;
import io.github.blackspherefollower.buttplug4j.protocol.messages.OutputCmd;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ButtplugClientDeviceFeature.
 */
public final class ButtplugClientDeviceFeature {

    /**
     * Parent device.
     */
    private final ButtplugClientDevice device;
    /**
     * Feature description.
     */
    private final String description;
    /**
     * Supported outputs.
     */
    private final HashMap<ButtplugOutput, DeviceFeature.OutputDescriptor> output;
    /**
     * Supported inputs.
     */
    private final HashMap<ButtplugInput, DeviceFeature.InputDescriptor> input;
    /**
     * Feature index.
     */
    private final int featureIndex;

    /**
     * Constructor.
     *
     * @param device  parent device
     * @param feature protocol feature
     */
    public ButtplugClientDeviceFeature(final ButtplugClientDevice device, final DeviceFeature feature) {
        this.device = device;
        this.featureIndex = feature.getFeatureIndex();
        this.description = feature.getFeatureDescription();
        this.output = new HashMap<>();
        if (feature.getOutput() != null) {
            feature.getOutput().forEach(outputDescriptor -> {
                try {
                    this.output.put(ButtplugOutput.fromString(outputDescriptor.getClass().getSimpleName()),
                            outputDescriptor);
                } catch (IllegalArgumentException e) {
                    // not a supported output type
                }
            });
        }
        this.input = new HashMap<>();
        if (feature.getInput() != null) {
            feature.getInput().forEach(inputDescriptor -> {
                try {
                    this.input.put(ButtplugInput.fromString(inputDescriptor.getClass().getSimpleName()),
                            inputDescriptor);
                } catch (IllegalArgumentException e) {
                    // not a supported output type
                }
            });
        }
    }

    /**
     * Get outputs.
     *
     * @return outputs
     */
    public HashMap<ButtplugOutput, DeviceFeature.OutputDescriptor> getOutput() {
        return output;
    }

    /**
     * Get inputs.
     *
     * @return inputs
     */
    public HashMap<ButtplugInput, DeviceFeature.InputDescriptor> getInput() {
        return input;
    }

    /**
     * Get feature index.
     *
     * @return feature index
     */
    public int getFeatureIndex() {
        return featureIndex;
    }

    /**
     * Get feature description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    private int getStepFromFloat(final ButtplugOutput type, final float value) throws ButtplugDeviceFeatureException {
        DeviceFeature.OutputDescriptor desc = output.get(type);
        if (desc == null) {
            throw new ButtplugDeviceFeatureException(type);
        }
        if (desc instanceof DeviceFeature.SteppedOutputDescriptor) {
            double steps = ((DeviceFeature.SteppedOutputDescriptor) desc).getValue()[1];
            steps *= value;
            
            int result;
            if (steps >= 0) {
                result = (int) Math.ceil(steps);
            } else {
                result = (int) Math.floor(steps);
            }
            return result;
        } else {
            throw new ButtplugDeviceFeatureException(type);
        }
    }

    private void checkStepRange(final ButtplugOutput type, final int value) throws ButtplugDeviceFeatureException {
        DeviceFeature.OutputDescriptor desc = output.get(type);
        if (desc == null) {
            throw new ButtplugDeviceFeatureException(type);
        }
        if (desc instanceof DeviceFeature.SteppedOutputDescriptor) {
            if (value < 0) {
                int steps = ((DeviceFeature.SteppedOutputDescriptor) desc).getValue()[0];
                if (value < steps) {
                    throw new ButtplugDeviceFeatureException(value, steps);
                }
            } else {
                int steps = ((DeviceFeature.SteppedOutputDescriptor) desc).getValue()[1];
                if (value > steps) {
                    throw new ButtplugDeviceFeatureException(value, steps);
                }
            }
        } else {
            throw new ButtplugDeviceFeatureException(type);
        }
    }

    /**
     * Check if output is supported.
     *
     * @param type output type
     * @return true if supported
     */
    public boolean hasOutput(final ButtplugOutput type) {
        return output.get(type) != null;
    }

    /**
     * Check if vibrate is supported.
     *
     * @return true if supported
     */
    public boolean hasVibrate() {
        return output.get(ButtplugOutput.VIBRATE) != null;
    }

    /**
     * Run vibrate.
     *
     * @param vibrate step count
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runVibrate(final int vibrate) throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.VIBRATE, vibrate);
        return device.runOutput(featureIndex, new OutputCmd.Vibrate(vibrate));
    }

    /**
     * Run vibrate.
     *
     * @param vibrate float value (0.0 to 1.0)
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runVibrateFloat(final float vibrate) throws ButtplugDeviceFeatureException {
        int steps = getStepFromFloat(ButtplugOutput.VIBRATE, vibrate);
        checkStepRange(ButtplugOutput.VIBRATE, steps);
        return device.runOutput(featureIndex, new OutputCmd.Vibrate(steps));
    }

    /**
     * Check if rotate is supported.
     *
     * @return true if supported
     */
    public boolean hasRotate() {
        return output.get(ButtplugOutput.ROTATE) != null;
    }

    /**
     * Run rotate.
     *
     * @param rotate step count
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runRotate(final int rotate) throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.ROTATE, rotate);
        return device.runOutput(featureIndex, new OutputCmd.Rotate(rotate));
    }

    /**
     * Run rotate.
     *
     * @param rotate float value (0.0 to 1.0)
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runRotateFloat(final float rotate) throws ButtplugDeviceFeatureException {
        int steps = getStepFromFloat(ButtplugOutput.ROTATE, rotate);
        checkStepRange(ButtplugOutput.ROTATE, steps);
        return device.runOutput(featureIndex, new OutputCmd.Rotate(steps));
    }

    /**
     * Check if constrict is supported.
     *
     * @return true if supported
     */
    public boolean hasConstrict() {
        return output.get(ButtplugOutput.CONSTRICT) != null;
    }

    /**
     * Run constrict.
     *
     * @param constrict step count
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runConstrict(final int constrict) throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.CONSTRICT, constrict);
        return device.runOutput(featureIndex, new OutputCmd.Constrict(constrict));
    }

    /**
     * Run constrict.
     *
     * @param constrict float value (0.0 to 1.0)
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runConstrictFloat(final float constrict) throws ButtplugDeviceFeatureException {
        int steps = getStepFromFloat(ButtplugOutput.CONSTRICT, constrict);
        checkStepRange(ButtplugOutput.CONSTRICT, steps);
        return device.runOutput(featureIndex, new OutputCmd.Constrict(steps));
    }

    /**
     * Check if spray is supported.
     *
     * @return true if supported
     */
    public boolean hasSpray() {
        return output.get(ButtplugOutput.SPRAY) != null;
    }

    /**
     * Run spray.
     *
     * @param spray step count
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runSpray(final int spray) throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.SPRAY, spray);
        return device.runOutput(featureIndex, new OutputCmd.Spray(spray));
    }

    /**
     * Run spray.
     *
     * @param spray float value (0.0 to 1.0)
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runSprayFloat(final float spray) throws ButtplugDeviceFeatureException {
        int steps = getStepFromFloat(ButtplugOutput.SPRAY, spray);
        checkStepRange(ButtplugOutput.SPRAY, steps);
        return device.runOutput(featureIndex, new OutputCmd.Spray(steps));
    }

    /**
     * Check if position is supported.
     *
     * @return true if supported
     */
    public boolean hasPosition() {
        return output.get(ButtplugOutput.POSITION) != null;
    }

    /**
     * Run position.
     *
     * @param position step count
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runPosition(final int position) throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.POSITION, position);
        return device.runOutput(featureIndex, new OutputCmd.Position(position));
    }

    /**
     * Run position.
     *
     * @param position float value (0.0 to 1.0)
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runPositionFloat(final float position) throws ButtplugDeviceFeatureException {
        int steps = getStepFromFloat(ButtplugOutput.POSITION, position);
        checkStepRange(ButtplugOutput.POSITION, steps);
        return device.runOutput(featureIndex, new OutputCmd.Position(steps));
    }

    /**
     * Check if HW position with duration is supported.
     *
     * @return true if supported
     */
    public boolean hasHwPositionWithDuration() {
        return output.get(ButtplugOutput.HW_POSITION_WITH_DURATION) != null;
    }

    /**
     * Run HW position with duration.
     *
     * @param position step count
     * @param duration duration in ms
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runHwPositionWithDuration(final int position, final int duration)
            throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.HW_POSITION_WITH_DURATION, position);

        // Validate duration
        checkDuration(duration);

        return device.runOutput(featureIndex, new OutputCmd.HwPositionWithDuration(position, duration));
    }

    private void checkDuration(final int duration) throws ButtplugDeviceFeatureException {
        DeviceFeature.OutputDescriptor desc = output.get(ButtplugOutput.HW_POSITION_WITH_DURATION);
        if (desc == null) {
            throw new ButtplugDeviceFeatureException(ButtplugOutput.HW_POSITION_WITH_DURATION);
        }
        if (desc instanceof DeviceFeature.HwPositionWithDuration) {
            int steps = ((DeviceFeature.HwPositionWithDuration) desc).getDuration()[1];
            if (duration > steps || duration < 0) {
                throw new ButtplugDeviceFeatureException(duration, steps);
            }
        } else {
            throw new ButtplugDeviceFeatureException(ButtplugOutput.HW_POSITION_WITH_DURATION);
        }
    }

    /**
     * Run HW position with duration.
     *
     * @param position float value (0.0 to 1.0)
     * @param duration duration in ms
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runHwPositionWithDurationFloat(final float position, final int duration)
            throws ButtplugDeviceFeatureException {
        int step = getStepFromFloat(ButtplugOutput.HW_POSITION_WITH_DURATION, position);
        checkStepRange(ButtplugOutput.HW_POSITION_WITH_DURATION, step);
        // Validate duration
        checkDuration(duration);

        checkStepRange(ButtplugOutput.HW_POSITION_WITH_DURATION, step);
        return device.runOutput(featureIndex, new OutputCmd.HwPositionWithDuration(step, duration));
    }

    /**
     * Check if LED is supported.
     *
     * @return true if supported
     */
    public boolean hasLed() {
        return output.get(ButtplugOutput.LED) != null;
    }

    /**
     * Run LED.
     *
     * @param led step count
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runLed(final int led) throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.LED, led);
        return device.runOutput(featureIndex, new OutputCmd.Led(led));
    }

    /**
     * Run LED.
     *
     * @param led float value (0.0 to 1.0)
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runLedFloat(final float led) throws ButtplugDeviceFeatureException {
        int steps = getStepFromFloat(ButtplugOutput.LED, led);
        checkStepRange(ButtplugOutput.LED, steps);
        return device.runOutput(featureIndex, new OutputCmd.Led(steps));
    }

    /**
     * Check if oscillate is supported.
     *
     * @return true if supported
     */
    public boolean hasOscillate() {
        return output.get(ButtplugOutput.OSCILLATE) != null;
    }

    /**
     * Run oscillate.
     *
     * @param oscillate step count
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runOscillate(final int oscillate) throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.OSCILLATE, oscillate);
        return device.runOutput(featureIndex, new OutputCmd.Oscillate(oscillate));
    }

    /**
     * Run oscillate.
     *
     * @param oscillate float value (0.0 to 1.0)
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runOscillateFloat(final float oscillate) throws ButtplugDeviceFeatureException {
        int steps = getStepFromFloat(ButtplugOutput.OSCILLATE, oscillate);
        checkStepRange(ButtplugOutput.OSCILLATE, steps);
        return device.runOutput(featureIndex, new OutputCmd.Oscillate(steps));
    }

    /**
     * Check if temperature is supported.
     *
     * @return true if supported
     */
    public boolean hasTemperature() {
        return output.get(ButtplugOutput.TEMPERATURE) != null;
    }

    /**
     * Run temperature.
     *
     * @param temperature step count
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runTemperature(final int temperature) throws ButtplugDeviceFeatureException {
        checkStepRange(ButtplugOutput.TEMPERATURE, temperature);
        return device.runOutput(featureIndex, new OutputCmd.Temperature(temperature));
    }

    /**
     * Run temperature.
     *
     * @param temperature float value (0.0 to 1.0)
     * @return future
     * @throws ButtplugDeviceFeatureException if error occurs
     */
    public Future<ButtplugMessage> runTemperatureFloat(final float temperature) throws ButtplugDeviceFeatureException {
        int steps = getStepFromFloat(ButtplugOutput.TEMPERATURE, temperature);
        checkStepRange(ButtplugOutput.TEMPERATURE, steps);
        return device.runOutput(featureIndex, new OutputCmd.Temperature(steps));
    }

    private void checkInput(final ButtplugInput type) throws ButtplugDeviceFeatureException {
        if (input.get(type) == null) {
            throw new ButtplugDeviceFeatureException(type);
        }
    }

    /**
     * Check if battery is supported.
     *
     * @return true if supported
     */
    public boolean hasBattery() {
        return input.get(ButtplugInput.BATTERY) != null;
    }

    /**
     * Read battery level.
     *
     * @return battery level
     * @throws ButtplugException    if error occurs
     * @throws ExecutionException   if error occurs
     * @throws InterruptedException if error occurs
     * @throws TimeoutException     if error occurs
     */
    public int readBattery() throws ButtplugException, ExecutionException, InterruptedException, TimeoutException {
        checkInput(ButtplugInput.BATTERY);
        ButtplugMessage msg = device.runInputRead(featureIndex, ButtplugInput.BATTERY).get(2, TimeUnit.SECONDS);
        return extractIntegerReading(msg);
    }

    /**
     * Extract integer reading from message.
     *
     * @param msg message
     * @return reading
     * @throws ButtplugException if error occurs
     */
    private int extractIntegerReading(final ButtplugMessage msg) throws ButtplugException {
        if (msg instanceof InputReading && ((InputReading) msg).getData() instanceof InputReading.InputIntegerData) {
            return ((InputReading.InputIntegerData) ((InputReading) msg).getData()).getValue();
        } else if (msg instanceof Error) {
            throw ((Error) msg).getException();
        } else {
            throw new ButtplugDeviceException("Unexpected message type: " + msg.getClass().getName());
        }
    }

    /**
     * Check if RSSI is supported.
     *
     * @return true if supported
     */
    public boolean hasRSSI() {
        return input.get(ButtplugInput.RSSI) != null;
    }

    /**
     * Read RSSI level.
     *
     * @return RSSI level
     * @throws ButtplugException    if error occurs
     * @throws ExecutionException   if error occurs
     * @throws InterruptedException if error occurs
     * @throws TimeoutException     if error occurs
     */
    public int readRSSI() throws ButtplugException, ExecutionException, InterruptedException, TimeoutException {
        checkInput(ButtplugInput.RSSI);
        ButtplugMessage msg = device.runInputRead(featureIndex, ButtplugInput.RSSI).get(2, TimeUnit.SECONDS);
        return extractIntegerReading(msg);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ButtplugClientDeviceFeature that = (ButtplugClientDeviceFeature) o;
        if (featureIndex != that.featureIndex
                || (description == null ? that.description != null : !description.equals(that.description))
                || (output == null) != (that.output == null)
                || (input == null) != (that.input == null)) {
            return false;
        }
        if (output != null) {
            if (output.size() != that.output.size() || !output.keySet().equals(that.output.keySet())) {
                return false;
            }
            for (ButtplugOutput type : output.keySet()) {
                if (!output.get(type).equals(that.output.get(type))) {
                    return false;
                }
            }
        }
        if (input != null) {
            if (input.size() != that.input.size() || !input.keySet().equals(that.input.keySet())) {
                return false;
            }
            for (ButtplugInput type : input.keySet()) {
                if (!input.get(type).equals(that.input.get(type))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int magic = 31;
        int result = Objects.hash(featureIndex, description);
        result = magic * result + (output != null ? output.hashCode() : 0);
        result = magic * result + (input != null ? input.hashCode() : 0);
        return result;
    }

    /**
     * Check if input is supported.
     *
     * @param inputType input type
     * @return true if supported
     */
    public boolean hasInput(final ButtplugInput inputType) {
        return input.containsKey(inputType);
    }
}
