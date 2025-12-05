package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceFeature;
import io.github.blackspherefollower.buttplug4j.protocol.messages.OutputCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
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
        outputs.add(new DeviceFeature.Rotate(new int[]{0, 50}));
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
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.Vibrate(50);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Vibrate.class, captor.getValue());
        assertEquals(50, ((OutputCmd.Vibrate) captor.getValue()).getValue());
    }

    @Test
    void testVibrateWithInvalidStepThrowsException() {
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.Vibrate(150));
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.Vibrate(-1));
    }

    @Test
    void testVibrateFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.VibrateFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Vibrate.class, captor.getValue());
        assertEquals(50, ((OutputCmd.Vibrate) captor.getValue()).getValue());
    }

    @Test
    void testVibrateFloatWithBoundaryValues() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        // Test minimum value
        clientFeature.VibrateFloat(0.0f);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertEquals(0, ((OutputCmd.Vibrate) captor.getValue()).getValue());

        // Test maximum value
        reset(mockDevice);
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);
        clientFeature.VibrateFloat(1.0f);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertEquals(100, ((OutputCmd.Vibrate) captor.getValue()).getValue());
    }

    @Test
    void testVibrateFloatWithInvalidValueThrowsException() {
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.VibrateFloat(1.5f));
        assertThrows(ButtplugDeviceFeatureException.class, () -> clientFeature.VibrateFloat(-0.1f));
    }

    @Test
    void testRotateWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.Rotate(25);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Rotate.class, captor.getValue());
        assertEquals(25, ((OutputCmd.Rotate) captor.getValue()).getValue());
    }

    @Test
    void testRotateFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.RotateFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Rotate.class, captor.getValue());
        assertEquals(25, ((OutputCmd.Rotate) captor.getValue()).getValue());
    }

    @Test
    void testConstrictWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.Constrict(5);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Constrict.class, captor.getValue());
        assertEquals(5, ((OutputCmd.Constrict) captor.getValue()).getValue());
    }

    @Test
    void testConstrictFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.ConstrictFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Constrict.class, captor.getValue());
        assertEquals(5, ((OutputCmd.Constrict) captor.getValue()).getValue());
    }

    @Test
    void testSprayWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.Spray(3);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Spray.class, captor.getValue());
        assertEquals(3, ((OutputCmd.Spray) captor.getValue()).getValue());
    }

    @Test
    void testSprayFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.SprayFloat(0.6f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Spray.class, captor.getValue());
        assertEquals(3, ((OutputCmd.Spray) captor.getValue()).getValue());
    }

    @Test
    void testPositionWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.Position(15);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Position.class, captor.getValue());
        assertEquals(15, ((OutputCmd.Position) captor.getValue()).getValue());
    }

    @Test
    void testPositionFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.PositionFloat(0.8f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Position.class, captor.getValue());
        assertEquals(20, ((OutputCmd.Position) captor.getValue()).getValue());
    }

    @Test
    void testPositionWithDurationWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        // Add PositionWithDuration to the feature
        testFeature.getOutput().add(new DeviceFeature.PositionWithDuration(new int[]{0, 25}, new int[]{0, 1000}));
        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);

        Future<ButtplugMessage> result = clientFeature.PositionWithDuration(15, 500);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.PositionWithDuration.class, captor.getValue());
        assertEquals(15, ((OutputCmd.PositionWithDuration) captor.getValue()).getPosition());
        assertEquals(500, ((OutputCmd.PositionWithDuration) captor.getValue()).getDuration());
    }

    @Test
    void testPositionWithDurationFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        // Add PositionWithDuration to the feature
        testFeature.getOutput().add(new DeviceFeature.PositionWithDuration(new int[]{0, 25}, new int[]{0, 1000}));
        clientFeature = new ButtplugClientDeviceFeature(mockDevice, testFeature);

        Future<ButtplugMessage> result = clientFeature.PositionWithDurationFloat(0.8f, 500);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.PositionWithDuration.class, captor.getValue());
        assertEquals(20, ((OutputCmd.PositionWithDuration) captor.getValue()).getPosition());
        assertEquals(500, ((OutputCmd.PositionWithDuration) captor.getValue()).getDuration());
    }

    @Test
    void testLedWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.Led(128);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Led.class, captor.getValue());
        assertEquals(128, ((OutputCmd.Led) captor.getValue()).getValue());
    }

    @Test
    void testLedFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.LedFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Led.class, captor.getValue());
        assertEquals(127, ((OutputCmd.Led) captor.getValue()).getValue());
    }

    @Test
    void testOscillateWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.Oscillate(10);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Oscillate.class, captor.getValue());
        assertEquals(10, ((OutputCmd.Oscillate) captor.getValue()).getValue());
    }

    @Test
    void testOscillateFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.OscillateFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Oscillate.class, captor.getValue());
        assertEquals(10, ((OutputCmd.Oscillate) captor.getValue()).getValue());
    }

    @Test
    void testTemperatureWithValidStep() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.Temperature(15);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
        assertInstanceOf(OutputCmd.Temperature.class, captor.getValue());
        assertEquals(15, ((OutputCmd.Temperature) captor.getValue()).getValue());
    }

    @Test
    void testTemperatureFloatWithValidValue() throws Exception {
        CompletableFuture<ButtplugMessage> future = CompletableFuture.completedFuture(mock(ButtplugMessage.class));
        when(mockDevice.sendOutputCommand(anyInt(), any(OutputCmd.IOutputCommand.class))).thenReturn(future);

        Future<ButtplugMessage> result = clientFeature.TemperatureFloat(0.5f);

        assertNotNull(result);
        ArgumentCaptor<OutputCmd.IOutputCommand> captor = ArgumentCaptor.forClass(OutputCmd.IOutputCommand.class);
        verify(mockDevice).sendOutputCommand(eq(0), captor.capture());
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

        assertThrows(ButtplugDeviceFeatureException.class, () -> limited.Vibrate(50));
    }

    @Test
    void testGetDescription() {
        assertEquals("Test Feature", clientFeature.getDescription());
    }
}
