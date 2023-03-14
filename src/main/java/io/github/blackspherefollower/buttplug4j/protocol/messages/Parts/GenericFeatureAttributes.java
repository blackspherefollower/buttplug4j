package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class GenericFeatureAttributes {
    @JsonProperty(value = "StepCount", required = true)
    private long stepCount;

    @JsonProperty(value = "FeatureDescriptor")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String featureDescriptor;

    @JsonProperty(value = "ActuatorType")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String actuatorType;

    public GenericFeatureAttributes() {
    }

    public long getStepCount() {
        return stepCount;
    }

    public void setStepCount(final long stepCount) {
        this.stepCount = stepCount;
    }

    public String getFeatureDescriptor() {
        return featureDescriptor;
    }

    public void setFeatureDescriptor(final String featureDescriptor) {
        this.featureDescriptor = featureDescriptor;
    }

    public String getActuatorType() {
        return actuatorType;
    }

    public void setActuatorType(final String actuatorType) {
        this.actuatorType = actuatorType;
    }
}
