package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public final class SensorFeaturesSerializer extends JsonSerializer {
    @Override
    public void serialize(final Object value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        SensorMessageAttributes data = (SensorMessageAttributes) value;
        gen.writeStartArray();
        for (SensorFeatureAttributes feat : data.getFeatures()) {
            gen.writeObject(feat);
        }
        gen.writeEndArray();
    }
}
