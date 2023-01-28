package org.blacksphere.protocol.messages;

import org.blackspherefollower.buttplug.protocol.messages.StopScanning;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

import java.io.IOException;
import java.util.List;

public class StopScanningTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"StopScanning\":{\"Id\":7}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(msgs.size(), 1);
        assertEquals(msgs.get(0).getClass(), StopScanning.class);
        assertEquals(msgs.get(0).id, 7);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
