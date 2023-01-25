package org.metafetish.buttplug.core.Messages;

import org.junit.Assert;
import org.junit.Test;
import org.metafetish.buttplug.core.ButtplugJsonMessageParser;
import org.metafetish.buttplug.core.ButtplugMessage;

import java.io.IOException;
import java.util.List;

public class ServerInfoTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"ServerInfo\":{\"Id\":1,\"MessageVersion\":3,\"MaxPingTime\":500,\"ServerName\":\"Websocket Server\"}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        Assert.assertEquals(1, msgs.size());
        Assert.assertEquals(ServerInfo.class, msgs.get(0).getClass());
        Assert.assertEquals(1, msgs.get(0).id, 1);
        Assert.assertEquals(3, ((ServerInfo) msgs.get(0)).messageVersion);
        Assert.assertEquals(500, ((ServerInfo) msgs.get(0)).maxPingTime);
        Assert.assertEquals("Websocket Server", ((ServerInfo) msgs.get(0)).serverName);

        String jsonOut = parser.formatJson(msgs);
        Assert.assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        Assert.assertEquals(testStr, jsonOut);
    }
}