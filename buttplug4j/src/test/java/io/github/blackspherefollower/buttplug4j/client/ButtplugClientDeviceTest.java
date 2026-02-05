package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Device;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceFeature;
import io.github.blackspherefollower.buttplug4j.protocol.messages.InputCommandType;
import io.github.blackspherefollower.buttplug4j.protocol.messages.OutputCmd;
import io.github.blackspherefollower.buttplug4j.protocol.messages.StopCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ButtplugClientDeviceTest {

    private ButtplugClient mockClient;
    private Device testDevice;
    private ButtplugClientDevice clientDevice;

    @BeforeEach
    void setup() {
        mockClient = mock(ButtplugClient.class);

        HashMap<Integer, DeviceFeature> features = new HashMap<>();

        // Feature 0: Vibrator
        DeviceFeature vibratorFeature = new DeviceFeature();
        vibratorFeature.setFeatureIndex(0);
        vibratorFeature.setFeatureDescription("Vibrator");
        ArrayList<DeviceFeature.OutputDescriptor> vibratorOutputs = new ArrayList<>();
        vibratorOutputs.add(new DeviceFeature.Vibrate(new int[]{0, 100}));
        vibratorFeature.setOutput(vibratorOutputs);
        features.put(0, vibratorFeature);

        // Feature 1: Battery sensor
        DeviceFeature batteryFeature = new DeviceFeature();
        batteryFeature.setFeatureIndex(1);
        batteryFeature.setFeatureDescription("Battery");
        ArrayList<DeviceFeature.InputDescriptor> batteryInputs = new ArrayList<>();
        ArrayList<InputCommandType> batteryCommands = new ArrayList<>();
        batteryCommands.add(InputCommandType.READ);
        batteryInputs.add(new DeviceFeature.Battery(batteryCommands, new int[][]{{0, 0}, {0, 100}}));
        batteryFeature.setInput(batteryInputs);
        features.put(1, batteryFeature);

        // Create a test device with various features
        testDevice = new Device(5, "Test Device", features, 100, "Display Name");

        testDevice.setDeviceFeatures(features);

        when(mockClient.getNextMsgId()).thenReturn(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        when(mockClient.sendMessage(any(ButtplugMessage.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(ButtplugMessage.class)));

        clientDevice = new ButtplugClientDevice(mockClient, testDevice);
    }

    @Test
    void testConstructorInitializesProperties() {
        assertEquals(5, clientDevice.getDeviceIndex());
        assertEquals("Test Device", clientDevice.getName());
        assertEquals("Display Name", clientDevice.getDisplayName());
        assertEquals(Integer.valueOf(100), clientDevice.getMessageTimingGap());
    }

    @Test
    void testConstructorWithNullDisplayName() {
        testDevice.setDeviceDisplayName(null);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        assertEquals("Test Device", device.getDisplayName());
    }

    @Test
    void testConstructorWithEmptyDisplayName() {
        testDevice.setDeviceDisplayName("");
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        assertEquals("Test Device", device.getDisplayName());
    }

    @Test
    void testGetDeviceFeatures() {
        Map<Integer, ButtplugClientDeviceFeature> features = clientDevice.getDeviceFeatures();

        assertNotNull(features);
        assertEquals(2, features.size());
        assertTrue(features.containsKey(0));
        assertTrue(features.containsKey(1));
        assertEquals("Vibrator", features.get(0).getDescription());
        assertEquals("Battery", features.get(1).getDescription());
    }

    @Test
    void testSendStopDeviceCmd() {
        Future<ButtplugMessage> result = clientDevice.sendStopDeviceCmd();

        assertNotNull(result);
        verify(mockClient).getNextMsgId();
        verify(mockClient).sendMessage(any(ButtplugMessage.class));
    }

    @Test
    void testSendOutputCommand() {
        OutputCmd.Vibrate vibrateCommand = new OutputCmd.Vibrate(50);

        Future<ButtplugMessage> result = clientDevice.sendOutputCommand(0, vibrateCommand);

        assertNotNull(result);
        verify(mockClient).getNextMsgId();
        verify(mockClient).sendMessage(any(OutputCmd.class));
    }

    @Test
    void testSendOutputCommandWithDifferentFeatureIndex() {
        OutputCmd.Vibrate vibrateCommand = new OutputCmd.Vibrate(75);

        Future<ButtplugMessage> result = clientDevice.sendOutputCommand(1, vibrateCommand);

        assertNotNull(result);
        verify(mockClient).getNextMsgId();
        verify(mockClient).sendMessage(any(OutputCmd.class));
    }

    @Test
    void testDeviceWithNoMessageTimingGap() {
        testDevice.setDeviceMessageTimingGap(null);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        assertNull(device.getMessageTimingGap());
    }

    @Test
    void testDeviceWithZeroMessageTimingGap() {
        testDevice.setDeviceMessageTimingGap(0);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        assertEquals(Integer.valueOf(0), device.getMessageTimingGap());
    }

    @Test
    void testDeviceWithNoFeatures() {
        testDevice.setDeviceFeatures(new HashMap<>());
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        Map<Integer, ButtplugClientDeviceFeature> features = device.getDeviceFeatures();
        assertNotNull(features);
        assertTrue(features.isEmpty());
    }

    @Test
    void testDeviceWithNullFeatures() {
        testDevice.setDeviceFeatures(null);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        Map<Integer, ButtplugClientDeviceFeature> features = device.getDeviceFeatures();
        assertNotNull(features);
        assertTrue(features.isEmpty());
    }

    @Test
    void testDeviceWithMultipleOutputFeatures() {
        HashMap<Integer, DeviceFeature> multiFeatures = new HashMap<>();

        // Add multiple output features
        for (int i = 0; i < 5; i++) {
            DeviceFeature feature = new DeviceFeature();
            feature.setFeatureIndex(i);
            feature.setFeatureDescription("Feature " + i);
            ArrayList<DeviceFeature.OutputDescriptor> outputs = new ArrayList<>();
            outputs.add(new DeviceFeature.Vibrate(new int[]{0, 100}));
            feature.setOutput(outputs);
            multiFeatures.put(i, feature);
        }

        testDevice.setDeviceFeatures(multiFeatures);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        Map<Integer, ButtplugClientDeviceFeature> features = device.getDeviceFeatures();
        assertEquals(5, features.size());

        for (int i = 0; i < 5; i++) {
            assertTrue(features.containsKey(i));
            assertEquals("Feature " + i, features.get(i).getDescription());
        }
    }

    @Test
    void testDeviceWithMixedInputOutputFeatures() {
        HashMap<Integer, DeviceFeature> mixedFeatures = new HashMap<>();

        // Output feature
        DeviceFeature outputFeature = new DeviceFeature();
        outputFeature.setFeatureIndex(0);
        outputFeature.setFeatureDescription("Output Feature");
        ArrayList<DeviceFeature.OutputDescriptor> outputs = new ArrayList<>();
        outputs.add(new DeviceFeature.Rotate(new int[]{0, 50}));
        outputFeature.setOutput(outputs);
        mixedFeatures.put(0, outputFeature);

        // Input feature
        DeviceFeature inputFeature = new DeviceFeature();
        inputFeature.setFeatureIndex(1);
        inputFeature.setFeatureDescription("Input Feature");
        ArrayList<DeviceFeature.InputDescriptor> inputs = new ArrayList<>();
        ArrayList<InputCommandType> commands = new ArrayList<>();
        commands.add(InputCommandType.READ);
        inputs.add(new DeviceFeature.Pressure(commands, new int[][]{{0, 0}, {0, 100}}));
        inputFeature.setInput(inputs);
        mixedFeatures.put(1, inputFeature);

        // Both input and output feature
        DeviceFeature mixedFeature = new DeviceFeature();
        mixedFeature.setFeatureIndex(2);
        mixedFeature.setFeatureDescription("Mixed Feature");
        mixedFeature.setOutput(outputs);
        mixedFeature.setInput(inputs);
        mixedFeatures.put(2, mixedFeature);

        testDevice.setDeviceFeatures(mixedFeatures);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        Map<Integer, ButtplugClientDeviceFeature> features = device.getDeviceFeatures();
        assertEquals(3, features.size());
        assertEquals("Output Feature", features.get(0).getDescription());
        assertEquals("Input Feature", features.get(1).getDescription());
        assertEquals("Mixed Feature", features.get(2).getDescription());
    }

    @Test
    void testGetDeviceIndex() {
        assertEquals(5, clientDevice.getDeviceIndex());
    }

    @Test
    void testGetName() {
        assertEquals("Test Device", clientDevice.getName());
    }

    @Test
    void testGetDisplayName() {
        assertEquals("Display Name", clientDevice.getDisplayName());
    }

    @Test
    void testGetMessageTimingGap() {
        assertEquals(Integer.valueOf(100), clientDevice.getMessageTimingGap());
    }

    @Test
    void testDeviceWithSpecialCharactersInName() {
        String specialName = "Test!@#$%^&*()_+-=[]{}|;':\",./<>?";
        testDevice.setDeviceName(specialName);
        testDevice.setDeviceDisplayName(specialName);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        assertEquals(specialName, device.getName());
        assertEquals(specialName, device.getDisplayName());
    }

    @Test
    void testDeviceWithNegativeIndex() {
        testDevice.setDeviceIndex(-1);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        assertEquals(-1, device.getDeviceIndex());
    }

    @Test
    void testDeviceWithLargeIndex() {
        testDevice.setDeviceIndex(Integer.MAX_VALUE);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        assertEquals(Integer.MAX_VALUE, device.getDeviceIndex());
    }

    @Test
    void testDeviceWithLargeMessageTimingGap() {
        testDevice.setDeviceMessageTimingGap(Integer.MAX_VALUE);
        ButtplugClientDevice device = new ButtplugClientDevice(mockClient, testDevice);

        assertEquals(Integer.valueOf(Integer.MAX_VALUE), device.getMessageTimingGap());
    }

    @Test
    void testSendStopDeviceCmdVariants() {
        assertNotNull(clientDevice.sendStopDeviceCmd(true, false));
        verify(mockClient).sendMessage(argThat(msg -> msg instanceof StopCmd && Boolean.TRUE.equals(((StopCmd)msg).getInputs()) && Boolean.FALSE.equals(((StopCmd)msg).getOutputs())));

        assertNotNull(clientDevice.sendStopDeviceCmd(2));
        verify(mockClient).sendMessage(argThat(msg -> msg instanceof StopCmd && Integer.valueOf(2).equals(((StopCmd)msg).getFeatureIndex())));

        assertNotNull(clientDevice.sendStopDeviceCmd(3, false, true));
        verify(mockClient).sendMessage(argThat(msg -> msg instanceof StopCmd && Integer.valueOf(3).equals(((StopCmd)msg).getFeatureIndex()) && Boolean.FALSE.equals(((StopCmd)msg).getInputs()) && Boolean.TRUE.equals(((StopCmd)msg).getOutputs())));
    }

    @Test
    void testEquals() {
        ButtplugClientDevice same = new ButtplugClientDevice(mockClient, testDevice);
        assertEquals(clientDevice, clientDevice);
        assertEquals(clientDevice, same);
        assertNotEquals(clientDevice, null);
        assertNotEquals(clientDevice, new Object());

        Device differentDevice = new Device(6, "Different", new HashMap<>(), 0, "Different");
        assertNotEquals(clientDevice, new ButtplugClientDevice(mockClient, differentDevice));
    }
}
