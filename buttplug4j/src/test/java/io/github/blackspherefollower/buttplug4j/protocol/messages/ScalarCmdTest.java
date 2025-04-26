package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScalarCmdTest {

    @Test
    public void test() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"ScalarCmd\":{\"Id\":1,\"DeviceIndex\":0,\"Scalars\":[{\"Index\":0,\"Scalar\":0.3,\"ActuatorType\":\"Vibrate\"},{\"Index\":1,\"Scalar\":0.8,\"ActuatorType\":\"Inflate\"}]}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(ScalarCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((ScalarCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(2, ((ScalarCmd) msgs.get(0)).getScalars().length);
        assertEquals(0, ((ScalarCmd) msgs.get(0)).getScalars()[0].getIndex());
        assertEquals("Vibrate", ((ScalarCmd) msgs.get(0)).getScalars()[0].getActuatorType());
        assertEquals(0.3, ((ScalarCmd) msgs.get(0)).getScalars()[0].getScalar());
        assertEquals(1, ((ScalarCmd) msgs.get(0)).getScalars()[1].getIndex());
        assertEquals("Inflate", ((ScalarCmd) msgs.get(0)).getScalars()[1].getActuatorType());
        assertEquals(0.8, ((ScalarCmd) msgs.get(0)).getScalars()[1].getScalar());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }
}