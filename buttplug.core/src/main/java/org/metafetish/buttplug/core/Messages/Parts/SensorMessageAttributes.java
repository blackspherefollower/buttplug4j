package org.metafetish.buttplug.core.Messages.Parts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

@JsonSerialize(using = SensorFeaturesSerializer.class)
public class SensorMessageAttributes extends MessageAttributes {

    ArrayList<SensorFeatureAttributes> features;

    SensorMessageAttributes() {
        features = new ArrayList<>();
    }

}
