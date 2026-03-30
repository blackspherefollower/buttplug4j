package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceFeature;
import io.github.blackspherefollower.buttplug4j.protocol.messages.InputReading;
import io.github.blackspherefollower.buttplug4j.protocol.messages.OutputCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ButtplugClientDeviceFeatureTest {

    private ButtplugClientDevice mockDevice;
    private DeviceFeature testFeature;
    private ButtplugClientDeviceFeature clientFeature;

    @BeforeEach
    void setup() {
        mockDevice = mock(ButtplugClientDevice.class);

        // Create a test feature with various output types
        testFeature = new DeviceFeature();
        testFeature.setFeatureIndex(0);
        testFeature.setFeatureDescription("Test Feature");

        ArrayList<DeviceFeature.OutputDescriptor> outputs = new ArrayList<>();
        outputs.add(new DeviceFeature.Vibrate(new int[]{0, 100}));
        outputs.add(new DeviceFeature.Rotate(new int[]{-50, 50}));
        outputs.add(new DeviceFeature.Oscillate(new int[]{0, 20}));
        outputs.add(new DeviceFeature.Constrict(new int[]{0, 10}));
        outputs.add(new DeviceFeature.Spray(new int[]{0, 5}));
        outputs.add(new DeviceFeature.Position(new int[]{0, 25}));
        outputs.add(new DeviceFeature.Led(new int[]{0, 255}));
        outputs.add(new DeviceFeature.Temperature(new int[]{0, 30}));
        testFeature.setOutput(outputs);

        ArrayList<DeviceFeature.InputDescriptor> inputs = new ArrayList<>();
        testFeature.setInput(inputs);

        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);
    }

    @Test
    void testConstructor() {
        assertEquals("Test Feature", clientFeature.getDescription());
    }

    @Test
    void testVibrateWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runVibrate(50);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Vibrate.class, captor.getValue());
        assertEquals(50, ((OutputCmd.Vibrate) captor.getValue()).getValue());
    }

    @Test
    void testNegativeVibrateSteps() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        try {
            Future<ButtplugMessage> result = clientFeature.runVibrateFloat(-1.0f);
            fail("Shouldn't get here");
        } catch (ButtplugDeviceFeatureException e) {
            assertEquals("Buttplug Device Feature value out of range", e.getMessage());
        }
    }

    @Test
    void testNegativeVibrateFloat() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        try {
            Future<ButtplugMessage> result = clientFeature.runVibrateFloat(-1.0f);
            fail("Shouldn't get here");
        } catch (ButtplugDeviceFeatureException e) {
            assertEquals("Buttplug Device Feature value out of range", e.getMessage());
        }
    }


    @Test
    void testNegativeRotateSteps() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runRotate(-50);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Rotate.class, captor.getValue());
        assertEquals(-50, ((OutputCmd.Rotate) captor.getValue()).getValue());
    }

    @Test
    void testNegativeRotateFloat() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runRotateFloat(-1.0f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Rotate.class, captor.getValue());
        assertEquals(-50, ((OutputCmd.Rotate) captor.getValue()).getValue());
    }

    @Test
    void testNegativeRotateStepsOOR() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        try {
            Future<ButtplugMessage> result = clientFeature.runVibrateFloat(-1.0f);
            fail("Shouldn't get here");
        } catch (ButtplugDeviceFeatureException e) {
            assertEquals("Buttplug Device Feature value out of range", e.getMessage());
        }
    }

    @Test
    void testNegativeRotateFloatOOR() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        try {
            Future<ButtplugMessage> result = clientFeature.runVibrateFloat(-1.0f);
            fail("Shouldn't get here");
        } catch (ButtplugDeviceFeatureException e) {
            assertEquals("Buttplug Device Feature value out of range", e.getMessage());
        }
    }


    @Test
    void testVibrateWithInvalidStepThrowsException() {
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.runVibrate(150));
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.runVibrate(-1));
    }

    @Test
    void testVibrateFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runVibrateFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Vibrate.class, captor.getValue());
        assertEquals(50, ((OutputCmd.Vibrate) captor.getValue()).getValue());
    }

    @Test
    void testVibrateFloatWithBoundaryValues() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        // Test minimum value
        clientFeature.runVibrateFloat(0.0f);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertEquals(0, ((OutputCmd.Vibrate) captor.getValue()).getValue());

        // Test maximum value
        reset(mockDevice);
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);
        clientFeature.runVibrateFloat(1.0f);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertEquals(100, ((OutputCmd.Vibrate) captor.getValue()).getValue());
    }

    @Test
    void testVibrateFloatWithInvalidValueThrowsException() {
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.runVibrateFloat(1.5f));
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.runVibrateFloat(-0.1f));
    }

    @Test
    void testRotateWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runRotate(25);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Rotate.class, captor.getValue());
        assertEquals(25, ((OutputCmd.Rotate) captor.getValue()).getValue());
    }

    @Test
    void testRotateFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runRotateFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Rotate.class, captor.getValue());
        assertEquals(25, ((OutputCmd.Rotate) captor.getValue()).getValue());
    }

    @Test
    void testConstrictWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runConstrict(5);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Constrict.class, captor.getValue());
        assertEquals(5, ((OutputCmd.Constrict) captor.getValue()).getValue());
    }

    @Test
    void testConstrictFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runConstrictFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Constrict.class, captor.getValue());
        assertEquals(5, ((OutputCmd.Constrict) captor.getValue()).getValue());
    }

    @Test
    void testSprayWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runSpray(3);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Spray.class, captor.getValue());
        assertEquals(3, ((OutputCmd.Spray) captor.getValue()).getValue());
    }

    @Test
    void testSprayFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runSprayFloat(0.6f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Spray.class, captor.getValue());
        assertEquals(4, ((OutputCmd.Spray) captor.getValue()).getValue());
    }

    @Test
    void testPositionWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runPosition(15);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Position.class, captor.getValue());
        assertEquals(15, ((OutputCmd.Position) captor.getValue()).getValue());
    }

    @Test
    void testPositionFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runPositionFloat(0.8f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Position.class, captor.getValue());
        assertEquals(21, ((OutputCmd.Position) captor.getValue()).getValue());
    }

    @Test
    void testPositionWithDurationWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        // Add PositionWithDuration to the feature
        testFeature.getOutput().add(new DeviceFeature.HwPositionWithDuration(new int[]{0, 25}, new int[]{0, 1000}));
        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);

        Future<ButtplugMessage> result = clientFeature.runHwPositionWithDuration(15, 500);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.HwPositionWithDuration.class, captor.getValue());
        assertEquals(15, ((OutputCmd.HwPositionWithDuration) captor.getValue()).getValue());
        assertEquals(500, ((OutputCmd.HwPositionWithDuration) captor.getValue()).getDuration());
    }

    @Test
    void testPositionWithDurationFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        // Add PositionWithDuration to the feature
        testFeature.getOutput().add(new DeviceFeature.HwPositionWithDuration(new int[]{0, 25}, new int[]{0, 1000}));
        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);

        Future<ButtplugMessage> result = clientFeature.runHwPositionWithDurationFloat(0.8f, 500);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.HwPositionWithDuration.class, captor.getValue());
        assertEquals(21, ((OutputCmd.HwPositionWithDuration) captor.getValue()).getValue());
        assertEquals(500, ((OutputCmd.HwPositionWithDuration) captor.getValue()).getDuration());
    }

    @Test
    void testLedWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runLed(128);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Led.class, captor.getValue());
        assertEquals(128, ((OutputCmd.Led) captor.getValue()).getValue());
    }

    @Test
    void testLedFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runLedFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Led.class, captor.getValue());
        assertEquals(128, ((OutputCmd.Led) captor.getValue()).getValue());
    }

    @Test
    void testOscillateWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runOscillate(10);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Oscillate.class, captor.getValue());
        assertEquals(10, ((OutputCmd.Oscillate) captor.getValue()).getValue());
    }

    @Test
    void testOscillateFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runOscillateFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Oscillate.class, captor.getValue());
        assertEquals(10, ((OutputCmd.Oscillate) captor.getValue()).getValue());
    }

    @Test
    void testTemperatureWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runTemperature(15);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Temperature.class, captor.getValue());
        assertEquals(15, ((OutputCmd.Temperature) captor.getValue()).getValue());
    }

    @Test
    void testTemperatureFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.runOutput(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.runTemperatureFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).runOutput(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Temperature.class, captor.getValue());
        assertEquals(15, ((OutputCmd.Temperature) captor.getValue()).getValue());
    }

    @Test
    void testUnsupportedOutputTypeThrowsException() {
        // Create a feature without Vibrate output
        DeviceFeature limitedFeature = new DeviceFeature();
        limitedFeature.setFeatureIndex(0);
        limitedFeature.setFeatureDescription("Limited Feature");
        limitedFeature.setOutput(new ArrayList<>());
        limitedFeature.setInput(new ArrayList<>());

        ButtplugClientDeviceFeature limited = new ButtplugClientDeviceFeature(mockDevice, limitedFeature);

        assertThrows(ButtplugDeviceFeatureException.class, () -> limited.runVibrate(50));
    }

    @Test
    void testGetDescription() {
        assertEquals("Test Feature", clientFeature.getDescription());
    }

    @Test
    void testHasMethods() {
        assertTrue(clientFeature.hasVibrate());
        assertTrue(clientFeature.hasRotate());
        assertTrue(clientFeature.hasOscillate());
        assertTrue(clientFeature.hasConstrict());
        assertTrue(clientFeature.hasSpray());
        assertTrue(clientFeature.hasPosition());
        assertTrue(clientFeature.hasLed());
        assertTrue(clientFeature.hasTemperature());
        assertFalse(clientFeature.hasBattery());
        assertFalse(clientFeature.hasRSSI());

        // Add HwPositionWithDuration
        testFeature.getOutput().add(new DeviceFeature.HwPositionWithDuration(new int[]{0, 25}, new int[]{0, 1000}));
        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);
        assertTrue(clientFeature.hasHwPositionWithDuration());

        // Add Battery
        testFeature.getInput().add(new DeviceFeature.Battery(new ArrayList<>(), new int[][]{{0, 100}}));
        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);
        assertTrue(clientFeature.hasBattery());

        // Add RSSI
        testFeature.getInput().add(new DeviceFeature.Rssi(new ArrayList<>(), new int[][]{{0, 100}}));
        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);
        assertTrue(clientFeature.hasRSSI());
    }

    @Test
    void testReadMethods() throws Exception {
        testFeature.getInput().add(new DeviceFeature.Battery(new ArrayList<>(), new int[][]{{0, 100}}));
        testFeature.getInput().add(new DeviceFeature.Rssi(new ArrayList<>(), new int[][]{{0, 100}}));
        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);

        InputReading batteryReading = new InputReading(0, 0, 0);
        InputReading.BatteryData batteryData = new InputReading.BatteryData();
        batteryData.setValue(80);
        batteryReading.setData(batteryData);

        InputReading rssiReading = new InputReading(0, 0, 0);
        InputReading.RssiData rssiData = new InputReading.RssiData();
        rssiData.setValue(50);
        rssiReading.setData(rssiData);

        when(mockDevice.runInputRead(eq(0), eq(ButtplugInput.BATTERY))).thenReturn(CompletableFuture.completedFuture(batteryReading));
        when(mockDevice.runInputRead(eq(0), eq(ButtplugInput.RSSI))).thenReturn(CompletableFuture.completedFuture(rssiReading));

        assertEquals(80, clientFeature.readBattery());
        verify(mockDevice).runInputRead(eq(0), eq(ButtplugInput.BATTERY));

        assertEquals(50, clientFeature.readRSSI());
        verify(mockDevice).runInputRead(eq(0), eq(ButtplugInput.RSSI));
    }

    @Test
    void testReadUnsupportedThrowsException() {
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.readBattery());
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.readRSSI());
    }

    @Test
    void testEqualsAndHashCode() {
        ButtplugClientDeviceFeature same = new ButtplugClientDeviceFeature(mockDevice, testFeature);

        DeviceFeature differentFeature = new DeviceFeature();
        differentFeature.setFeatureIndex(1);
        differentFeature.setFeatureDescription("Test Feature");
        ButtplugClientDeviceFeature differentFeatureObj = new ButtplugClientDeviceFeature(mockDevice, differentFeature);

        assertEquals(clientFeature, clientFeature);
        assertEquals(clientFeature, same);
        assertNotEquals(clientFeature, differentFeatureObj);
        assertNotEquals(clientFeature, null);
        assertNotEquals(clientFeature, new Object());

        // Test with different outputs
        DeviceFeature featureWithDifferentOutput = new DeviceFeature();
        featureWithDifferentOutput.setFeatureIndex(0);
        featureWithDifferentOutput.setFeatureDescription("Test Feature");
        featureWithDifferentOutput.setOutput(new ArrayList<>());
        ButtplugClientDeviceFeature featureWithDifferentOutputObj = new ButtplugClientDeviceFeature(mockDevice, featureWithDifferentOutput);
        assertNotEquals(clientFeature, featureWithDifferentOutputObj);

        // Test with different inputs
        DeviceFeature featureWithDifferentInput = new DeviceFeature();
        featureWithDifferentInput.setFeatureIndex(0);
        featureWithDifferentInput.setFeatureDescription("Test Feature");
        featureWithDifferentInput.setOutput(testFeature.getOutput());
        ArrayList<DeviceFeature.InputDescriptor> differentInputs = new ArrayList<>();
        differentInputs.add(new DeviceFeature.Battery());
        featureWithDifferentInput.setInput(differentInputs);
        ButtplugClientDeviceFeature featureWithDifferentInputObj = new ButtplugClientDeviceFeature(mockDevice, featureWithDifferentInput);
        assertNotEquals(clientFeature, featureWithDifferentInputObj);
    }
}
