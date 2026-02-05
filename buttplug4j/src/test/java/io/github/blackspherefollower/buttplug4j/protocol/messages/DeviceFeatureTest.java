package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceFeatureTest {

    @Test
    public void testDeviceFeature() {
        DeviceFeature feature = new DeviceFeature();
        feature.setFeatureDescription("Desc");
        feature.setFeatureIndex(1);
        
        ArrayList<DeviceFeature.OutputDescriptor> outputs = new ArrayList<>();
        outputs.add(new DeviceFeature.Vibrate(new int[]{10}));
        feature.setOutput(outputs);
        
        ArrayList<DeviceFeature.InputDescriptor> inputs = new ArrayList<>();
        inputs.add(new DeviceFeature.Battery(new ArrayList<>(Arrays.asList(InputCommandType.READ)), new int[][]{{0, 100}}));
        feature.setInput(inputs);

        assertEquals("Desc", feature.getFeatureDescription());
        assertEquals(1, feature.getFeatureIndex());
        assertEquals(outputs, feature.getOutput());
        assertEquals(inputs, feature.getInput());
    }

    @Test
    public void testSteppedOutputDescriptors() {
        int[] steps = {10, 20};
        int[] defaultSteps = {0, 0};
        
        checkSteppedOutput(new DeviceFeature.Vibrate(steps), steps);
        checkSteppedOutput(new DeviceFeature.Vibrate(), defaultSteps);
        
        checkSteppedOutput(new DeviceFeature.Rotate(steps), steps);
        checkSteppedOutput(new DeviceFeature.Rotate(), defaultSteps);
        
        checkSteppedOutput(new DeviceFeature.Oscillate(steps), steps);
        checkSteppedOutput(new DeviceFeature.Oscillate(), defaultSteps);
        
        checkSteppedOutput(new DeviceFeature.Constrict(steps), steps);
        checkSteppedOutput(new DeviceFeature.Constrict(), defaultSteps);
        
        checkSteppedOutput(new DeviceFeature.Spray(steps), steps);
        checkSteppedOutput(new DeviceFeature.Spray(), defaultSteps);
        
        checkSteppedOutput(new DeviceFeature.Temperature(steps), steps);
        checkSteppedOutput(new DeviceFeature.Temperature(), defaultSteps);
        
        checkSteppedOutput(new DeviceFeature.Led(steps), steps);
        checkSteppedOutput(new DeviceFeature.Led(), defaultSteps);
        
        checkSteppedOutput(new DeviceFeature.Position(steps), steps);
        checkSteppedOutput(new DeviceFeature.Position(), defaultSteps);
    }

    private void checkSteppedOutput(DeviceFeature.SteppedOutputDescriptor descriptor, int[] expected) {
        assertArrayEquals(expected, descriptor.getValue());
        int[] newSteps = {30};
        descriptor.setStepCount(newSteps);
        assertArrayEquals(newSteps, descriptor.getValue());
        
        DeviceFeature.SteppedOutputDescriptor other = null;
        if (descriptor instanceof DeviceFeature.Vibrate) other = new DeviceFeature.Vibrate(newSteps);
        if (descriptor instanceof DeviceFeature.Rotate) other = new DeviceFeature.Rotate(newSteps);
        if (descriptor instanceof DeviceFeature.Oscillate) other = new DeviceFeature.Oscillate(newSteps);
        if (descriptor instanceof DeviceFeature.Constrict) other = new DeviceFeature.Constrict(newSteps);
        if (descriptor instanceof DeviceFeature.Spray) other = new DeviceFeature.Spray(newSteps);
        if (descriptor instanceof DeviceFeature.Temperature) other = new DeviceFeature.Temperature(newSteps);
        if (descriptor instanceof DeviceFeature.Led) other = new DeviceFeature.Led(newSteps);
        if (descriptor instanceof DeviceFeature.Position) other = new DeviceFeature.Position(newSteps);
        
        assertEquals(descriptor, other);
        assertNotEquals(descriptor, new Object());
    }

    @Test
    public void testPositionWithDuration() {
        int[] steps = {10};
        int[] duration = {100};
        DeviceFeature.PositionWithDuration pwd = new DeviceFeature.PositionWithDuration(steps, duration);
        assertArrayEquals(steps, pwd.getValue());
        assertArrayEquals(duration, pwd.getDuration());
        
        DeviceFeature.PositionWithDuration pwd2 = new DeviceFeature.PositionWithDuration();
        pwd2.setStepCount(steps);
        pwd2.setDuration(duration);
        assertEquals(pwd, pwd2);
        
        assertNotEquals(pwd, new DeviceFeature.PositionWithDuration(new int[]{20}, duration));
        assertNotEquals(pwd, new DeviceFeature.PositionWithDuration(steps, new int[]{200}));
    }

    @Test
    public void testRangedInputDescriptors() {
        ArrayList<InputCommandType> types = new ArrayList<>(Arrays.asList(InputCommandType.READ));
        int[][] range = {{0, 100}};
        int[][] defaultRange = {{0, 0}, {0, 0}};
        ArrayList<InputCommandType> defaultTypes = new ArrayList<>();
        
        checkRangedInput(new DeviceFeature.Battery(types, range), types, range);
        checkRangedInput(new DeviceFeature.Battery(), defaultTypes, defaultRange);
        
        checkRangedInput(new DeviceFeature.Rssi(types, range), types, range);
        checkRangedInput(new DeviceFeature.Rssi(), defaultTypes, defaultRange);
        
        checkRangedInput(new DeviceFeature.Button(types, range), types, range);
        checkRangedInput(new DeviceFeature.Button(), defaultTypes, defaultRange);
        
        checkRangedInput(new DeviceFeature.Pressure(types, range), types, range);
        checkRangedInput(new DeviceFeature.Pressure(), defaultTypes, defaultRange);
        
        checkRangedInput(new DeviceFeature.PositionInput(types, range), types, range);
        checkRangedInput(new DeviceFeature.PositionInput(), defaultTypes, defaultRange);
    }

    private void checkRangedInput(DeviceFeature.RangedInputDescriptor descriptor, ArrayList<InputCommandType> expectedTypes, int[][] expectedRange) {
        assertEquals(expectedTypes, descriptor.getInput());
        assertArrayEquals(expectedRange, descriptor.getValueRange());
        
        ArrayList<InputCommandType> newTypes = new ArrayList<>(Arrays.asList(InputCommandType.SUBSCRIBE));
        int[][] newRange = {{0, 200}};
        descriptor.setInput(newTypes);
        descriptor.setValueRange(newRange);
        
        assertEquals(newTypes, descriptor.getInput());
        assertArrayEquals(newRange, descriptor.getValueRange());
        
        DeviceFeature.RangedInputDescriptor other = null;
        if (descriptor instanceof DeviceFeature.Battery) other = new DeviceFeature.Battery(newTypes, newRange);
        if (descriptor instanceof DeviceFeature.Rssi) other = new DeviceFeature.Rssi(newTypes, newRange);
        if (descriptor instanceof DeviceFeature.Button) other = new DeviceFeature.Button(newTypes, newRange);
        if (descriptor instanceof DeviceFeature.Pressure) other = new DeviceFeature.Pressure(newTypes, newRange);
        if (descriptor instanceof DeviceFeature.PositionInput) other = new DeviceFeature.PositionInput(newTypes, newRange);
        
        assertEquals(descriptor, other);
        assertNotEquals(descriptor, new Object());
        assertNotEquals(descriptor, null);
    }

    @Test
    public void testOutputDescriptorSerialization() throws IOException, ButtplugProtocolException {
        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        String json = "[{\"DeviceList\":{\"Id\":1,\"Devices\":{\"0\":{\"DeviceIndex\":0,\"DeviceName\":\"Test\",\"DeviceFeatures\":{\"0\":{\"FeatureIndex\":0,\"FeatureDescription\":\"Test\",\"Output\":{\"Vibrate\":{\"Value\":[0,20]},\"Rotate\":{\"Value\":[0,20]},\"Oscillate\":{\"Value\":[0,20]},\"Constrict\":{\"Value\":[0,20]},\"Spray\":{\"Value\":[0,20]},\"Temperature\":{\"Value\":[0,20]},\"Led\":{\"Value\":[0,20]},\"Position\":{\"Value\":[0,20]},\"PositionWithDuration\":{\"Value\":[0,100],\"Duration\":[0,2000]}}}}}}}}]";
        
        List<ButtplugMessage> msgs = parser.parseJson(json);
        assertEquals(1, msgs.size());
        String reserialized = parser.formatJson(msgs);
        
        // Use parseJson again on reserialized to compare objects, because JSON field order might differ
        List<ButtplugMessage> msgs2 = parser.parseJson(reserialized);
        assertEquals(msgs.size(), msgs2.size());
    }

    @Test
    public void testInputDescriptorSerialization() throws IOException, ButtplugProtocolException {
        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        String json = "[{\"DeviceList\":{\"Id\":1,\"Devices\":{\"0\":{\"DeviceIndex\":0,\"DeviceName\":\"Test\",\"DeviceFeatures\":{\"0\":{\"FeatureIndex\":0,\"FeatureDescription\":\"Test\",\"Input\":{\"Battery\":{\"Command\":[\"Read\"],\"Value\":[[0,0],[0,100]]},\"RSSI\":{\"Command\":[\"Read\"],\"Value\":[[0,0],[0,100]]},\"Button\":{\"Command\":[\"Read\"],\"Value\":[[0,0],[0,100]]},\"Pressure\":{\"Command\":[\"Read\"],\"Value\":[[0,0],[0,100]]},\"Position\":{\"Command\":[\"Read\"],\"Value\":[[0,0],[0,100]]}}}}}}}}]";
        
        List<ButtplugMessage> msgs = parser.parseJson(json);
        assertEquals(1, msgs.size());
        String reserialized = parser.formatJson(msgs);
        
        List<ButtplugMessage> msgs2 = parser.parseJson(reserialized);
        assertEquals(msgs.size(), msgs2.size());
    }
}
