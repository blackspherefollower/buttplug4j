package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

@JsonSerialize(using = SensorFeaturesSerializer.class)
public final class SensorMessageAttributes extends MessageAttributes {

    private ArrayList<SensorFeatureAttributes> features;

    SensorMessageAttributes() {
        setFeatures(new ArrayList<>());
    }

    public ArrayList<SensorFeatureAttributes> getFeatures() {
        return features;
    }

    public void setFeatures(final ArrayList<SensorFeatureAttributes> features) {
        this.features = features;
    }
}
