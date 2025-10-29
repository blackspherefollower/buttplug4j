package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public class InputReading extends ButtplugDeviceMessage {

    @JsonProperty(value = "FeatureIndex", required = true)
    private int featureIndex;
    @JsonProperty(value = "Data", required = true)
    private InputData data;

    public InputReading(int id, long deviceIndex, int featureIndex) {
        super(id, deviceIndex);
        this.featureIndex = featureIndex;
    }

    public interface InputData {
    }

    static public class InputIntegerData {
        @JsonProperty(value = "Data", required = true)
        int value;
    }

    static public class BatteryData implements InputData {
    }

    static public class RssiData implements InputData {
    }

    static public class ButtonData implements InputData {
    }

    static public class PresureData implements InputData {
    }
}
