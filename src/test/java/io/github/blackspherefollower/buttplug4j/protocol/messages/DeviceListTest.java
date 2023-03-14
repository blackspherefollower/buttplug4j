package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviceListTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"DeviceList\":{\"Id\":5,\"Devices\":[{\"DeviceIndex\":2,\"DeviceName\":\"foo\",\"DeviceMessages\":{\"ScalarCmd\":[{\"StepCount\":20,\"FeatureDescriptor\":\"Clitoral Stimulator\",\"ActuatorType\":\"Vibrate\"},{\"StepCount\":20,\"FeatureDescriptor\":\"Insertable Vibrator\",\"ActuatorType\":\"Vibrate\"}],\"StopDeviceCmd\":{}}},{\"DeviceIndex\":4,\"DeviceName\":\"bar\",\"DeviceMessages\":{\"ScalarCmd\":[{\"StepCount\":20,\"FeatureDescriptor\":\"Clitoral Stimulator\",\"ActuatorType\":\"Vibrate\"},{\"StepCount\":20,\"FeatureDescriptor\":\"Insertable Vibrator\",\"ActuatorType\":\"Vibrate\"}],\"StopDeviceCmd\":{}}}]}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(DeviceList.class, msgs.get(0).getClass());
        assertEquals(5, msgs.get(0).getId());
        assertEquals(2, ((DeviceList) msgs.get(0)).getDevices().size());

        //DeviceMessageInfo[] devs = ((DeviceList) msgs.get(0)).devices;
        //assertEquals(2, devs[0].deviceIndex);
        //assertEquals("foo", devs[0].deviceName);
        //assertArrayEquals(new String[]{"foo-cmd-1", "foo-cmd-2"}, devs[0].deviceMessages);

        //assertEquals(4, devs[1].deviceIndex);
        //assertEquals("bar", devs[1].deviceName);
        //assertArrayEquals(new String[]{"bar-cmd-1", "bar-cmd-2"}, devs[1].deviceMessages);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
