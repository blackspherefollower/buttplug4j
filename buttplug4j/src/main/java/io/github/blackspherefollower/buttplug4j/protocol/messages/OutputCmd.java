package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public class OutputCmd extends ButtplugDeviceMessage {


    @JsonProperty(value = "FeatureIndex", required = true)
    private long featureIndex;
    @JsonProperty(value = "Command", required = true)
    private IOutputCommand command;

    public OutputCmd(int id, final long deviceIndex, final long featureIndex) {
        super(id, deviceIndex);
        this.featureIndex = featureIndex;
    }

    public OutputCmd() {
        super(-1, -1);
        this.featureIndex = -1;
    }

    public final long getFeatureIndex() {
        return featureIndex;
    }

    public final void setFeatureIndex(final long deviceIndex) {
        this.featureIndex = deviceIndex;
    }

    public final IOutputCommand getCommand() {
        return command;
    }

    public final void setCommand(final IOutputCommand command) {
        this.command = command;
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Vibrate.class, name = "Vibrate"),
            @JsonSubTypes.Type(value = Rotate.class, name = "Rotate"),
            @JsonSubTypes.Type(value = Spray.class, name = "Spray"),
            @JsonSubTypes.Type(value = Oscillate.class, name = "Oscillate"),
            @JsonSubTypes.Type(value = Position.class, name = "Position"),
            @JsonSubTypes.Type(value = Temperature.class, name = "Temperature"),
            @JsonSubTypes.Type(value = Constrict.class, name = "Constrict"),
            @JsonSubTypes.Type(value = PositionWithDuration.class, name = "PositionWithDuration"),
            @JsonSubTypes.Type(value = Led.class, name = "Led")
    })
    public interface IOutputCommand {
    }

    public abstract static class ValueCommand implements IOutputCommand {
        @JsonProperty(value = "Value", required = true)
        private int value;

        protected ValueCommand(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class Vibrate extends ValueCommand {
        public Vibrate(int value) {
            super(value);
        }

        public Vibrate() {
            super(0);
        }
    }

    public static class Rotate extends ValueCommand {
        public Rotate(int value) {
            super(value);
        }

        public Rotate() {
            super(0);
        }
    }

    public static class Oscillate extends ValueCommand {
        public Oscillate(int value) {
            super(value);
        }

        public Oscillate() {
            super(0);
        }
    }

    public static class Constrict extends ValueCommand {
        public Constrict(int value) {
            super(value);
        }

        public Constrict() {
            super(0);
        }
    }

    public static class Spray extends ValueCommand {
        public Spray(int value) {
            super(value);
        }

        public Spray() {
            super(0);
        }
    }

    public static class Temperature extends ValueCommand {
        public Temperature(int value) {
            super(value);
        }

        public Temperature() {
            super(0);
        }
    }

    public static class Led extends ValueCommand {
        public Led(int value) {
            super(value);
        }

        public Led() {
            super(0);
        }
    }

    public static class Position extends ValueCommand {
        public Position(int value) {
            super(value);
        }

        public Position() {
            super(0);
        }
    }

    public static class PositionWithDuration implements IOutputCommand {
        @JsonProperty(value = "Position", required = true)
        private int position;
        @JsonProperty(value = "Duration", required = true)
        private int duration;

        public PositionWithDuration(int position, int duration) {
            this.position = position;
            this.duration = duration;
        }

        public PositionWithDuration() {
            this.position = 0;
            this.duration = 0;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }

}
