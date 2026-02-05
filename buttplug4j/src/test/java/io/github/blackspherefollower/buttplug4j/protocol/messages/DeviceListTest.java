package io.github.blackspherefollower.buttplug4j.protocol.messages;

import dev.harrel.jsonschema.Validator;
import dev.harrel.jsonschema.ValidatorFactory;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceListTest {

    static String schema = null;

    @BeforeAll
    public static void setup() throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(TestConstants.SCHEMA_URL).openStream());
        schema = new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));
        in.close();
    }

    @Test
    public void test() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"DeviceList\":{\"Id\":5,\"Devices\":{\"0\":{\"DeviceIndex\":0,\"DeviceName\":\"Test Vibrator\",\"DeviceMessageTimingGap\":100,\"DeviceFeatures\":{\"0\":{\"FeatureIndex\":0,\"FeatureDescription\":\"Clitoral Stimulator\",\"Output\":{\"Vibrate\":{\"Value\":[0,20]}}},\"1\":{\"FeatureIndex\":1,\"FeatureDescription\":\"Insertable Stimulator\",\"Output\":{\"Vibrate\":{\"Value\":[0,20]}}},\"2\":{\"FeatureIndex\":2,\"FeatureDescription\":\"Battery\",\"Input\":{\"Battery\":{\"Command\":[\"Read\"],\"Value\":[[0,0],[0,100]]}}}}},\"2\":{\"DeviceIndex\":2,\"DeviceName\":\"Test Stroker\",\"DeviceMessageTimingGap\":100,\"DeviceDisplayName\":\"User set name\",\"DeviceFeatures\":{\"0\":{\"FeatureIndex\":0,\"FeatureDescription\":\"Stroker\",\"Output\":{\"Oscillate\":{\"Value\":[0,20]},\"PositionWithDuration\":{\"Value\":[0,100],\"Duration\":[0,2000]}},\"Input\":{\"Position\":{\"Command\":[\"Subscribe\",\"Read\"],\"Value\":[[0,0],[0,100]]}}},\"1\":{\"FeatureIndex\":1,\"FeatureDescription\":\"Bluetooth Radio RSSI\",\"Input\":{\"RSSI\":{\"Command\":[\"Read\"],\"Value\":[[-10,0],[-100,0]]}}}}}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(error -> error.getError() + " - " + error.getInstanceLocation()).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(DeviceList.class, msgs.get(0).getClass());
        assertEquals(5, msgs.get(0).getId());
        assertEquals(2, ((DeviceList) msgs.get(0)).getDevices().size());

        HashMap<Integer, Device> devs = ((DeviceList) msgs.get(0)).getDevices();
        assertNotNull(devs.get(0));
        assertEquals(0, devs.get(0).getDeviceIndex());
        assertEquals("Test Vibrator", devs.get(0).getDeviceName());
        assertEquals("", devs.get(0).getDeviceDisplayName());
        assertEquals(100, devs.get(0).getDeviceMessageTimingGap());
        HashMap<Integer, DeviceFeature> dev0Features = devs.get(0).getDeviceFeatures();
        assertEquals(3, dev0Features.size());
        assertEquals(0, dev0Features.get(0).getFeatureIndex());
        assertEquals("Clitoral Stimulator", dev0Features.get(0).getFeatureDescription());
        assertEquals(1, dev0Features.get(0).getOutput().size());
        assertArrayEquals(new String[]{"Vibrate"}, dev0Features.get(0).getOutput().stream().map(outputDescriptor -> outputDescriptor.getClass().getSimpleName()).toArray());
        assertNull(dev0Features.get(0).getInput());
        assertEquals(1, dev0Features.get(1).getFeatureIndex());
        assertEquals("Insertable Stimulator", dev0Features.get(1).getFeatureDescription());
        assertEquals(1, dev0Features.get(1).getOutput().size());
        assertArrayEquals(new String[]{"Vibrate"}, dev0Features.get(1).getOutput().stream().map(outputDescriptor -> outputDescriptor.getClass().getSimpleName()).toArray());
        assertNull(dev0Features.get(1).getInput());
        assertEquals(2, dev0Features.get(2).getFeatureIndex());
        assertEquals("Battery", dev0Features.get(2).getFeatureDescription());
        assertNull(dev0Features.get(2).getOutput());
        assertEquals(1, dev0Features.get(2).getInput().size());
        assertArrayEquals(new String[]{"Battery"}, dev0Features.get(2).getInput().stream().map(inputDescriptor -> inputDescriptor.getClass().getSimpleName()).toArray());

        assertNull(devs.get(1));

        assertNotNull(devs.get(2));
        assertEquals(2, devs.get(2).getDeviceIndex());
        assertEquals("Test Stroker", devs.get(2).getDeviceName());
        assertEquals("User set name", devs.get(2).getDeviceDisplayName());
        assertEquals(100, devs.get(2).getDeviceMessageTimingGap());
        HashMap<Integer, DeviceFeature> dev2Features = devs.get(2).getDeviceFeatures();
        assertEquals(2, dev2Features.size());
        assertEquals(0, dev2Features.get(0).getFeatureIndex());
        assertEquals("Stroker", dev2Features.get(0).getFeatureDescription());
        assertEquals(2, dev2Features.get(0).getOutput().size());
        assertArrayEquals(new String[]{"Oscillate", "PositionWithDuration"}, dev2Features.get(0).getOutput().stream().map(outputDescriptor -> outputDescriptor.getClass().getSimpleName()).toArray());
        assertEquals(1, dev2Features.get(0).getInput().size());
        assertEquals(1, dev2Features.get(1).getFeatureIndex());
        assertArrayEquals(new String[]{"PositionInput"}, dev2Features.get(0).getInput().stream().map(inputDescriptor -> inputDescriptor.getClass().getSimpleName()).toArray());
        assertEquals("Bluetooth Radio RSSI", dev2Features.get(1).getFeatureDescription());
        assertNull(dev2Features.get(1).getOutput());
        assertEquals(1, dev2Features.get(1).getInput().size());
        assertArrayEquals(new String[]{"Rssi"}, dev2Features.get(1).getInput().stream().map(inputDescriptor -> inputDescriptor.getClass().getSimpleName()).toArray());


        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testSettersAndGetters() {
        DeviceList deviceList = new DeviceList(new HashMap<>(), 1);
        HashMap<Integer, Device> devices = new HashMap<>();
        Device device = new Device(0, "name", new HashMap<>(), 100, "display");
        devices.put(0, device);
        deviceList.setDevices(devices);
        assertEquals(devices, deviceList.getDevices());

        device.setDeviceIndex(1);
        device.setDeviceName("newName");
        device.setDeviceMessageTimingGap(200);
        device.setDeviceDisplayName("newDisplay");
        HashMap<Integer, DeviceFeature> features = new HashMap<>();
        device.setDeviceFeatures(features);

        assertEquals(1, device.getDeviceIndex());
        assertEquals("newName", device.getDeviceName());
        assertEquals(200, device.getDeviceMessageTimingGap());
        assertEquals("newDisplay", device.getDeviceDisplayName());
        assertEquals(features, device.getDeviceFeatures());
    }
}
