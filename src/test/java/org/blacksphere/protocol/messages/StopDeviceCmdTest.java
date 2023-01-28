package org.blacksphere.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.StopDeviceCmd;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StopDeviceCmdTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"StopDeviceCmd\":{\"Id\":7,\"DeviceIndex\":3}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(msgs.size(), 1);
        assertEquals(msgs.get(0).getClass(), StopDeviceCmd.class);
        assertEquals(msgs.get(0).id, 7);
        assertEquals(((StopDeviceCmd) msgs.get(0)).deviceIndex, 3);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
