package org.blacksphere.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.Ok;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class OkTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"Ok\":{\"Id\":3}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(Ok.class, msgs.get(0).getClass());
        assertEquals(3, msgs.get(0).id);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
