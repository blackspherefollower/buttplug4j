package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public class InputCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "FeatureIndex", required = true)
    private long featureIndex;

    @JsonProperty(value = "InputType", required = true)
    private String inputType;

    @JsonProperty(value = "InputCommand", required = true)
    private InputCommandType inputCommand;

    public InputCmd(int id, final long deviceIndex, final long featureIndex, final String inputType, final InputCommandType inputCommand) {
        super(id, deviceIndex);
        this.featureIndex = featureIndex;
        this.inputType = inputType;
        this.inputCommand = inputCommand;
    }

    public InputCmd() {
        super(-1, -1);
        this.featureIndex = -1;
        this.inputType = "None";
        this.inputCommand = InputCommandType.READ;
    }


    public long getFeatureIndex() {
        return featureIndex;
    }

    public void setFeatureIndex(long featureIndex) {
        this.featureIndex = featureIndex;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public InputCommandType getInputCommand() {
        return inputCommand;
    }

    public void setInputCommand(InputCommandType inputCommand) {
        this.inputCommand = inputCommand;
    }
}
