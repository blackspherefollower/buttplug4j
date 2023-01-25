package org.metafetish.buttplug.core.Messages.Parts;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class DeviceMessagesDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = p.getCodec();
        JsonNode node = oc.readTree(p);

        ArrayList<DeviceMessage> ret = new ArrayList<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> msg = it.next();
            DeviceMessage dmsg = new DeviceMessage();
            dmsg.message = msg.getKey();

            switch(dmsg.message) {
                case "StopDeviceCmd":
                    dmsg.attributes = new NullMessageAttributes();
                    break;
                case "RawReadCmd":
                case "RawWriteCmd":
                case "RawSubscribeCmd":
                    dmsg.attributes = new ObjectMapper().treeToValue(msg.getValue(), RawMessageAttributes.class);
                    break;
                case "SensorReadCmd":
                case "SensorSubscribeCmd":
                    SensorMessageAttributes sattrs = new SensorMessageAttributes();
                    for (Iterator<JsonNode> it2 = msg.getValue().elements(); it2.hasNext(); ) {
                        sattrs.features.add(new ObjectMapper().treeToValue(it2.next(), SensorFeatureAttributes.class));
                    }
                    dmsg.attributes = sattrs;
                    break;
                case "ScalarCmd":
                case "LinearCmd":
                case "RotateCmd":
                    GenericMessageAttributes gattrs = new GenericMessageAttributes();
                    for (Iterator<JsonNode> it2 = msg.getValue().elements(); it2.hasNext(); ) {
                        gattrs.features.add(new ObjectMapper().treeToValue(it2.next(), GenericFeatureAttributes.class));
                    }
                    dmsg.attributes = gattrs;
                    break;
                default:
                    throw new JsonParseException(p, "Unknown Buttplug Device Message type: " + dmsg.message);
            }

            ret.add(dmsg);
        }
        return ret;
    }
}
