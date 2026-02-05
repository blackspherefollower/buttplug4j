package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class StopCmd extends ButtplugMessage {

    @JsonProperty(value = "Inputs", required = false)
    private Boolean inputs = null;
    @JsonProperty(value = "Outputs", required = false)
    private Boolean outputs = null;
    @JsonProperty(value = "DeviceIndex", required = false)
    private Integer deviceIndex = null;
    @JsonProperty(value = "FeatureIndex", required = false)
    private Integer featureIndex = null;

    @SuppressWarnings("unused")
    private StopCmd() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
    }

    public StopCmd(final int id) {
        super(id);
    }

    public StopCmd(final int id, final boolean inputs, final boolean outputs) {
        super(id);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public StopCmd(final int id, final int deviceIndex) {
        super(id);
        this.deviceIndex = deviceIndex;
    }

    public StopCmd(final int id, final int deviceIndex, final boolean inputs, final boolean outputs) {
        super(id);
        this.deviceIndex = deviceIndex;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public StopCmd(final int id, final int deviceIndex, final int featureIndex) {
        super(id);
        this.deviceIndex = deviceIndex;
        this.featureIndex = featureIndex;
    }

    public StopCmd(final int id, final int deviceIndex, final int featureIndex, final boolean inputs, final boolean outputs) {
        super(id);
        this.deviceIndex = deviceIndex;
        this.featureIndex = featureIndex;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Boolean getInputs() {
        return inputs;
    }

    public void setInputs(Boolean inputs) {
        this.inputs = inputs;
    }

    public Boolean getOutputs() {
        return outputs;
    }

    public void setOutputs(Boolean outputs) {
        this.outputs = outputs;
    }

    public Integer getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Integer deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Integer getFeatureIndex() {
        return featureIndex;
    }

    public void setFeatureIndex(Integer featureIndex) {
        this.featureIndex = featureIndex;
    }
}
