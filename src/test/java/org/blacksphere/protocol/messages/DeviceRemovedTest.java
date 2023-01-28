package org.blacksphere.protocol.messages;

import org.blackspherefollower.buttplug.protocol.messages.DeviceRemoved;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

import java.io.IOException;
import java.util.List;

public class DeviceRemovedTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"DeviceRemoved\":{\"Id\":3,\"DeviceIndex\":2}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(DeviceRemoved.class, msgs.get(0).getClass());
        assertEquals(3, msgs.get(0).id);
        assertEquals(2, ((DeviceRemoved) msgs.get(0)).deviceIndex);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
