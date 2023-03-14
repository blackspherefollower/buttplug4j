package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerInfoTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"ServerInfo\":{\"Id\":1,\"MessageVersion\":3,\"MaxPingTime\":500,\"ServerName\":\"Websocket Server\"}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(ServerInfo.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId(), 1);
        assertEquals(3, ((ServerInfo) msgs.get(0)).getMessageVersion());
        assertEquals(500, ((ServerInfo) msgs.get(0)).getMaxPingTime());
        assertEquals("Websocket Server", ((ServerInfo) msgs.get(0)).getServerName());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }
}