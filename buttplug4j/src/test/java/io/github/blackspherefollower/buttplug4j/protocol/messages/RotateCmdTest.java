package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RotateCmdTest {

    @Test
    public void test() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"RotateCmd\":{\"Id\":6,\"DeviceIndex\":1,\"Rotations\":[{\"Index\":0,\"Speed\":0.5,\"Clockwise\":true},{\"Index\":1,\"Speed\":1.0,\"Clockwise\":false}]}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(RotateCmd.class, msgs.get(0).getClass());
        assertEquals(6, msgs.get(0).getId());
        assertEquals(1, ((RotateCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(2, ((RotateCmd) msgs.get(0)).getRotations().length);
        assertEquals(0, ((RotateCmd) msgs.get(0)).getRotations()[0].getIndex());
        assertEquals(0.5, ((RotateCmd) msgs.get(0)).getRotations()[0].getSpeed());
        assertEquals(true, ((RotateCmd) msgs.get(0)).getRotations()[0].isClockwise());
        assertEquals(1, ((RotateCmd) msgs.get(0)).getRotations()[1].getIndex());
        assertEquals(1.0, ((RotateCmd) msgs.get(0)).getRotations()[1].getSpeed());
        assertEquals(false, ((RotateCmd) msgs.get(0)).getRotations()[1].isClockwise());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }
}