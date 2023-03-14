package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public final class DeviceMessagesSerializer extends JsonSerializer {
    @Override
    public void serialize(final Object value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        if (value instanceof List<?>) {
            List<?> data = (List<?>) value;
            for (Object obj : data) {
                if (obj instanceof DeviceMessage) {
                    DeviceMessage dmsg = (DeviceMessage) obj;
                    gen.writeObjectField(dmsg.message, dmsg.attributes);
                }
            }
        }
        gen.writeEndObject();
    }
}
