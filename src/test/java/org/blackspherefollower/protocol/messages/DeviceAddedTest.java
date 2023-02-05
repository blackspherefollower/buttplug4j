package org.blackspherefollower.protocol.messages;

import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.DeviceAdded;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviceAddedTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"DeviceAdded\":{\"Id\":3,\"DeviceIndex\":2,\"DeviceName\":\"foo\",\"DeviceMessages\":{\"ScalarCmd\":[{\"StepCount\":20,\"FeatureDescriptor\":\"Clitoral Stimulator\",\"ActuatorType\":\"Vibrate\"},{\"StepCount\":20,\"FeatureDescriptor\":\"Insertable Vibrator\",\"ActuatorType\":\"Vibrate\"}],\"StopDeviceCmd\":{}}}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(DeviceAdded.class, msgs.get(0).getClass());
        assertEquals(3, msgs.get(0).getId());
        assertEquals(2, ((DeviceAdded) msgs.get(0)).getDeviceIndex());
        assertEquals("foo", ((DeviceAdded) msgs.get(0)).getDeviceName());
        assertEquals("ScalarCmd", ((DeviceAdded) msgs.get(0)).getDeviceMessages().get(0).message);
        assertEquals("StopDeviceCmd", ((DeviceAdded) msgs.get(0)).getDeviceMessages().get(1).message);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
