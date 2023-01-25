package org.metafetish.buttplug.core.Messages;

import org.junit.Assert;
import org.junit.Test;
import org.metafetish.buttplug.core.ButtplugJsonMessageParser;
import org.metafetish.buttplug.core.ButtplugMessage;

import java.io.IOException;
import java.util.List;

public class DeviceListTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"DeviceList\":{\"Id\":5,\"Devices\":[{\"DeviceIndex\":2,\"DeviceName\":\"foo\",\"DeviceMessages\":{\"ScalarCmd\":[{\"StepCount\":20,\"FeatureDescriptor\":\"Clitoral Stimulator\",\"ActuatorType\":\"Vibrate\"},{\"StepCount\":20,\"FeatureDescriptor\":\"Insertable Vibrator\",\"ActuatorType\":\"Vibrate\"}],\"StopDeviceCmd\":{}}},{\"DeviceIndex\":4,\"DeviceName\":\"bar\",\"DeviceMessages\":{\"ScalarCmd\":[{\"StepCount\":20,\"FeatureDescriptor\":\"Clitoral Stimulator\",\"ActuatorType\":\"Vibrate\"},{\"StepCount\":20,\"FeatureDescriptor\":\"Insertable Vibrator\",\"ActuatorType\":\"Vibrate\"}],\"StopDeviceCmd\":{}}}]}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        Assert.assertEquals(1, msgs.size());
        Assert.assertEquals(DeviceList.class, msgs.get(0).getClass());
        Assert.assertEquals(5, msgs.get(0).id);
        Assert.assertEquals(2, ((DeviceList) msgs.get(0)).devices.size());

        //DeviceMessageInfo[] devs = ((DeviceList) msgs.get(0)).devices;
        //Assert.assertEquals(2, devs[0].deviceIndex);
        //Assert.assertEquals("foo", devs[0].deviceName);
        //Assert.assertArrayEquals(new String[]{"foo-cmd-1", "foo-cmd-2"}, devs[0].deviceMessages);

        //Assert.assertEquals(4, devs[1].deviceIndex);
        //Assert.assertEquals("bar", devs[1].deviceName);
        //Assert.assertArrayEquals(new String[]{"bar-cmd-1", "bar-cmd-2"}, devs[1].deviceMessages);

        String jsonOut = parser.formatJson(msgs);
        Assert.assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        Assert.assertEquals(testStr, jsonOut);
    }

}
