package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinearCmdTest {

    @Test
    public void test() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"LinearCmd\":{\"Id\":1,\"DeviceIndex\":0,\"Vectors\":[{\"Index\":0,\"Position\":0.3,\"Duration\":500},{\"Index\":1,\"Position\":0.8,\"Duration\":1000}]}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(LinearCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((LinearCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(2, ((LinearCmd) msgs.get(0)).getVectors().length);
        assertEquals(0, ((LinearCmd) msgs.get(0)).getVectors()[0].getIndex());
        assertEquals(500, ((LinearCmd) msgs.get(0)).getVectors()[0].getDuration());
        assertEquals(0.3, ((LinearCmd) msgs.get(0)).getVectors()[0].getPosition());
        assertEquals(1, ((LinearCmd) msgs.get(0)).getVectors()[1].getIndex());
        assertEquals(1000, ((LinearCmd) msgs.get(0)).getVectors()[1].getDuration());
        assertEquals(0.8, ((LinearCmd) msgs.get(0)).getVectors()[1].getPosition());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }
}