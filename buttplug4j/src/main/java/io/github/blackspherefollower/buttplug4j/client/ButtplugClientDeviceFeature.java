package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceFeature;
import io.github.blackspherefollower.buttplug4j.protocol.messages.OutputCmd;

import java.util.HashMap;
import java.util.concurrent.Future;

public class ButtplugClientDeviceFeature {

    private final ButtplugClientDevice device;
    private final String description;
    private final HashMap<String, DeviceFeature.OutputDescriptor> output;
    private final HashMap<String, DeviceFeature.InputDescriptor> input;
    private final int featureIndex;

    public ButtplugClientDeviceFeature(final ButtplugClientDevice device, final DeviceFeature feature) {
        this.device = device;
        this.featureIndex = feature.getFeatureIndex();
        this.description = feature.getFeatureDescription();
        this.output = new HashMap<>();
        if (feature.getOutput() != null) {
            feature.getOutput().forEach(outputDescriptor -> this.output.put(outputDescriptor.getClass().getSimpleName(), outputDescriptor));
        }
        this.input = new HashMap<>();
        if (feature.getInput() != null) {
            feature.getInput().forEach(inputDescriptor -> this.input.put(inputDescriptor.getClass().getSimpleName(), inputDescriptor));
        }
    }

    private int GetStepFromFloat(final String type, final float value) throws ButtplugDeviceFeatureException {
        if (value < 0.0f || value > 1.0f) {
            throw new ButtplugDeviceFeatureException("Range error");
        }
        DeviceFeature.OutputDescriptor desc = output.get(type);
        if (desc == null) {
            throw new ButtplugDeviceFeatureException(type);
        }
        if (desc instanceof DeviceFeature.SteppedOutputDescriptor) {
            double steps = ((DeviceFeature.SteppedOutputDescriptor) desc).getValue()[1];
            steps *= value;
            return (int) Math.floor(steps);
        } else if (desc instanceof DeviceFeature.PositionWithDuration) {
            double steps = ((DeviceFeature.PositionWithDuration) desc).getPosition()[1];
            steps *= value;
            return (int) Math.floor(steps);
        } else {
            throw new ButtplugDeviceFeatureException(type);
        }
    }

    private void CheckStepRange(final String type, final float value) throws ButtplugDeviceFeatureException {
        DeviceFeature.OutputDescriptor desc = output.get(type);
        if (desc == null) {
            throw new ButtplugDeviceFeatureException(type);
        }
        if (desc instanceof DeviceFeature.SteppedOutputDescriptor) {
            int steps = ((DeviceFeature.SteppedOutputDescriptor) desc).getValue()[1];
            if (value > steps || value < 0) {
                throw new ButtplugDeviceFeatureException("Range error");
            }
        } else if (desc instanceof DeviceFeature.PositionWithDuration) {
            int steps = ((DeviceFeature.PositionWithDuration) desc).getPosition()[1];
            if (value > steps || value < 0) {
                throw new ButtplugDeviceFeatureException("Range error");
            }
        } else {
            throw new ButtplugDeviceFeatureException(type);
        }
    }

    public Future<ButtplugMessage> Vibrate(final int vibrate) throws ButtplugDeviceFeatureException {
        CheckStepRange("Vibrate", vibrate);
        return device.sendOutputCommand(featureIndex, new OutputCmd.Vibrate(vibrate));
    }

    public Future<ButtplugMessage> VibrateFloat(final float vibrate) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.Vibrate(GetStepFromFloat("Vibrate", vibrate)));
    }

    public Future<ButtplugMessage> Rotate(final int rotate) throws ButtplugDeviceFeatureException {
        CheckStepRange("Rotate", rotate);
        return device.sendOutputCommand(featureIndex, new OutputCmd.Rotate(rotate));
    }

    public Future<ButtplugMessage> RotateFloat(final float rotate) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.Rotate(GetStepFromFloat("Rotate", rotate)));
    }

    public Future<ButtplugMessage> Constrict(final int constrict) throws ButtplugDeviceFeatureException {
        CheckStepRange("Constrict", constrict);
        return device.sendOutputCommand(featureIndex, new OutputCmd.Constrict(constrict));
    }

    public Future<ButtplugMessage> ConstrictFloat(final float constrict) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.Constrict(GetStepFromFloat("Constrict", constrict)));
    }

    public Future<ButtplugMessage> Spray(final int spray) throws ButtplugDeviceFeatureException {
        CheckStepRange("Spray", spray);
        return device.sendOutputCommand(featureIndex, new OutputCmd.Spray(spray));
    }

    public Future<ButtplugMessage> SprayFloat(final float spray) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.Spray(GetStepFromFloat("Spray", spray)));
    }

    public Future<ButtplugMessage> Position(final int position) throws ButtplugDeviceFeatureException {
        CheckStepRange("Position", position);
        return device.sendOutputCommand(featureIndex, new OutputCmd.Position(position));
    }

    public Future<ButtplugMessage> PositionFloat(final float position) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.Position(GetStepFromFloat("Position", position)));
    }

    public Future<ButtplugMessage> PositionWithDuration(final int position, final int duration) throws ButtplugDeviceFeatureException {
        CheckStepRange("PositionWithDuration", position);
        return device.sendOutputCommand(featureIndex, new OutputCmd.PositionWithDuration(position, duration));
    }

    public Future<ButtplugMessage> PositionWithDurationFloat(final float position, final int duration) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.PositionWithDuration(GetStepFromFloat("PositionWithDuration", position), duration));
    }

    public Future<ButtplugMessage> Led(final int led) throws ButtplugDeviceFeatureException {
        CheckStepRange("Led", led);
        return device.sendOutputCommand(featureIndex, new OutputCmd.Led(led));
    }

    public Future<ButtplugMessage> LedFloat(final float led) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.Led(GetStepFromFloat("Led", led)));
    }

    public Future<ButtplugMessage> Oscillate(final int oscillate) throws ButtplugDeviceFeatureException {
        CheckStepRange("Oscillate", oscillate);
        return device.sendOutputCommand(featureIndex, new OutputCmd.Oscillate(oscillate));
    }

    public Future<ButtplugMessage> OscillateFloat(final float oscillate) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.Oscillate(GetStepFromFloat("Oscillate", oscillate)));
    }

    public Future<ButtplugMessage> Temperature(final int temperature) throws ButtplugDeviceFeatureException {
        CheckStepRange("Temperature", temperature);
        return device.sendOutputCommand(featureIndex, new OutputCmd.Temperature(temperature));
    }

    public Future<ButtplugMessage> TemperatureFloat(final float temperature) throws ButtplugDeviceFeatureException {
        return device.sendOutputCommand(featureIndex, new OutputCmd.Temperature(GetStepFromFloat("Temperature", temperature)));
    }

    public String getDescription() {
        return description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ButtplugClientDeviceFeature that = (ButtplugClientDeviceFeature) o;
        if (featureIndex != that.featureIndex ||
                !description.equals(that.description) ||
                (output == null) != (that.output == null) ||
                (input == null) != (that.input == null)) {
            return false;
        }
        if (output != null) {
            if (output.size() != that.output.size() || output.keySet().containsAll(that.output.keySet())) {
                return false;
            }
            for (String type : output.keySet()) {
                if (!output.get(type).equals(that.output.get(type))) {
                    return false;
                }
            }
        }
        if (input != null) {
            if (input.size() != that.input.size() || input.keySet().containsAll(that.input.keySet())) {
                return false;
            }
            for (String type : input.keySet()) {
                if (!input.get(type).equals(that.input.get(type))) {
                    return false;
                }
            }
        }
        return true;
    }
}
