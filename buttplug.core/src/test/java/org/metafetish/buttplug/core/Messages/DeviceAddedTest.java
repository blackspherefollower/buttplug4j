package org.metafetish.buttplug.core.Messages;

import org.junit.Assert;
import org.junit.Test;
import org.metafetish.buttplug.core.ButtplugJsonMessageParser;
import org.metafetish.buttplug.core.ButtplugMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceAddedTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"DeviceAdded\":{\"Id\":3,\"DeviceIndex\":2,\"DeviceName\":\"foo\",\"DeviceMessages\":{\"ScalarCmd\":[{\"StepCount\":20,\"FeatureDescriptor\":\"Clitoral Stimulator\",\"ActuatorType\":\"Vibrate\"},{\"StepCount\":20,\"FeatureDescriptor\":\"Insertable Vibrator\",\"ActuatorType\":\"Vibrate\"}],\"StopDeviceCmd\":{}}}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        Assert.assertEquals(1, msgs.size());
        Assert.assertEquals(DeviceAdded.class, msgs.get(0).getClass());
        Assert.assertEquals(3, msgs.get(0).id);
        Assert.assertEquals(2, ((DeviceAdded) msgs.get(0)).deviceIndex);
        Assert.assertEquals("foo", ((DeviceAdded) msgs.get(0)).deviceName);
        Assert.assertEquals("ScalarCmd", ((DeviceAdded) msgs.get(0)).deviceMessages.get(0).message);
        Assert.assertEquals("StopDeviceCmd", ((DeviceAdded) msgs.get(0)).deviceMessages.get(1).message);

        String jsonOut = parser.formatJson(msgs);
        Assert.assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        Assert.assertEquals(testStr, jsonOut);
    }

}
