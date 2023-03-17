package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public final class GenericFeaturesSerializer extends JsonSerializer {
    @Override
    public void serialize(final Object value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        GenericMessageAttributes data = (GenericMessageAttributes) value;
        gen.writeStartArray();
        for (GenericFeatureAttributes feat : data.getFeatures()) {
            gen.writeObject(feat);
        }
        gen.writeEndArray();
    }
}
