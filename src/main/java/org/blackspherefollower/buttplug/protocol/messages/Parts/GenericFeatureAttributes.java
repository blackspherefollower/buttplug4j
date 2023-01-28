package org.blackspherefollower.buttplug.protocol.messages.Parts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericFeatureAttributes {
    @JsonProperty(value = "StepCount", required = true)
    public long stepCount;

    @JsonProperty(value = "FeatureDescriptor")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String featureDescriptor;

    @JsonProperty(value = "ActuatorType")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String actuatorType;

    public GenericFeatureAttributes() {
    }
}
