package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SensorFeatureAttributes {
    @JsonProperty(value = "SensorType", required = true)
    private String sensorType;

    @JsonProperty(value = "FeatureDescriptor")
    private String featureDescriptor;

    @JsonProperty(value = "SensorRange", required = true)
    private int[][] sensorRange;

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(final String sensorType) {
        this.sensorType = sensorType;
    }

    public String getFeatureDescriptor() {
        return featureDescriptor;
    }

    public void setFeatureDescriptor(final String featureDescriptor) {
        this.featureDescriptor = featureDescriptor;
    }

    public int[][] getSensorRange() {
        return sensorRange;
    }

    public void setSensorRange(final int[][] sensorRange) {
        this.sensorRange = sensorRange;
    }
}
