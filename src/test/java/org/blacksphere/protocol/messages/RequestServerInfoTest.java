package org.blacksphere.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.RequestServerInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestServerInfoTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"RequestServerInfo\":{\"Id\":7,\"MessageVersion\":3,\"ClientName\":\"UnitTest\"}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(msgs.size(), 1);
        assertEquals(msgs.get(0).getClass(), RequestServerInfo.class);
        assertEquals(msgs.get(0).id, 7);
        assertEquals(((RequestServerInfo) msgs.get(0)).clientName, "UnitTest");

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
