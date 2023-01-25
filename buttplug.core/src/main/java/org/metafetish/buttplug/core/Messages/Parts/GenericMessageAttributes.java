package org.metafetish.buttplug.core.Messages.Parts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

@JsonSerialize(using = GenericFeaturesSerializer.class)
public class GenericMessageAttributes extends MessageAttributes {

    public ArrayList<GenericFeatureAttributes> features;

    public GenericMessageAttributes() {
        features = new ArrayList<>();
    }
}

