package org.metafetish.buttplug.core.Messages;

import org.junit.Assert;
import org.junit.Test;
import org.metafetish.buttplug.core.ButtplugJsonMessageParser;
import org.metafetish.buttplug.core.ButtplugMessage;

import java.io.IOException;
import java.util.List;

public class RequestServerInfoTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"RequestServerInfo\":{\"Id\":7,\"ClientName\":\"UnitTest\",\"MessageVersion\":3}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        Assert.assertEquals(msgs.size(), 1);
        Assert.assertEquals(msgs.get(0).getClass(), RequestServerInfo.class);
        Assert.assertEquals(msgs.get(0).id, 7);
        Assert.assertEquals(((RequestServerInfo) msgs.get(0)).clientName, "UnitTest");

        String jsonOut = parser.formatJson(msgs);
        Assert.assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        Assert.assertEquals(testStr, jsonOut);
    }

}
