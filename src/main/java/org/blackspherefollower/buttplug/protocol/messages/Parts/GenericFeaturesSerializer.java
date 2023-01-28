package org.blackspherefollower.buttplug.protocol.messages.Parts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GenericFeaturesSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        GenericMessageAttributes data = (GenericMessageAttributes) value;
        gen.writeStartArray();
        for (GenericFeatureAttributes feat : data.features) {
            gen.writeObject(feat);
        }
        gen.writeEndArray();
    }
}
