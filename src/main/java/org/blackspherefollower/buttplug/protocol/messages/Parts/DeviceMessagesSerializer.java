package org.blackspherefollower.buttplug.protocol.messages.Parts;


import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.List;

public class DeviceMessagesSerializer extends JsonSerializer {
     @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        if(value instanceof List<?>) {
            List<?> data = (List<?>)value;
            for (Object obj : data) {
                if(obj instanceof DeviceMessage) {
                    DeviceMessage dmsg = (DeviceMessage) obj;
                    gen.writeObjectField(dmsg.message, dmsg.attributes);
                }
            }
        }
        gen.writeEndObject();
    }
}
