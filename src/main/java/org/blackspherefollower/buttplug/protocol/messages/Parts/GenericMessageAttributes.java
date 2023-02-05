package org.blackspherefollower.buttplug.protocol.messages.Parts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

@JsonSerialize(using = GenericFeaturesSerializer.class)
public final class GenericMessageAttributes extends MessageAttributes {

    private ArrayList<GenericFeatureAttributes> features;

    public GenericMessageAttributes() {
        setFeatures(new ArrayList<>());
    }

    public ArrayList<GenericFeatureAttributes> getFeatures() {
        return features;
    }

    public void setFeatures(final ArrayList<GenericFeatureAttributes> features) {
        this.features = features;
    }
}

