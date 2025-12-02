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

    public InputData getData() {
        return data;
    }

    public void setData(InputData data) {
        this.data = data;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public void setFeatureIndex(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    public interface InputData {
    }

    static public class InputIntegerData {
        @JsonProperty(value = "Data", required = true)
        int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    static public class BatteryData extends InputIntegerData {
    }

    static public class RssiData extends InputIntegerData {
    }

    static public class ButtonData extends InputIntegerData {
    }

    static public class PresureData extends InputIntegerData {
    }
}
