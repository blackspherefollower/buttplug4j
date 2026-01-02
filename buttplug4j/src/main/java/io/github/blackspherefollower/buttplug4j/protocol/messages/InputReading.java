package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public class InputReading extends ButtplugDeviceMessage {

    @JsonProperty(value = "FeatureIndex", required = true)
    private int featureIndex;
    @JsonProperty(value = "Reading", required = true)
    private InputData data;

    public InputReading(int id, long deviceIndex, int featureIndex) {
        super(id, deviceIndex);
        this.featureIndex = featureIndex;
    }
    public InputReading() {
        super(-1, -1);
        this.featureIndex = -1;
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

    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Battery.class, name = "Battery"),
            @JsonSubTypes.Type(value = Rssi.class, name = "Rssi"),
            @JsonSubTypes.Type(value = Button.class, name = "Button"),
            @JsonSubTypes.Type(value = Presure.class, name = "Pressure"),
            @JsonSubTypes.Type(value = Position.class, name = "Position"),
    })
    public static class InputData {
    }

    static public class InputIntegerData extends InputData {
        @JsonProperty(value = "Value", required = true)
        int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    static public class Battery extends InputIntegerData {
    }

    static public class Rssi extends InputIntegerData {
    }

    static public class Button extends InputIntegerData {
    }

    static public class Presure extends InputIntegerData {
    }

    static public class Position extends InputIntegerData {
    }
}
