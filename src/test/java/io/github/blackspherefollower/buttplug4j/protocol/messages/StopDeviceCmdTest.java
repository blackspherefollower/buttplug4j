package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
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
        assertEquals(msgs.get(0).getId(), 7);
        assertEquals(((StopDeviceCmd) msgs.get(0)).getDeviceIndex(), 3);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
