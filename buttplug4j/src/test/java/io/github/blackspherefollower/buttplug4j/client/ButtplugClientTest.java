package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.*;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ButtplugClientTest {

    private TestButtplugClient client;
    private AtomicReference<ButtplugClientDevice> addedDevice;
    private AtomicReference<ButtplugClientDevice> updatedDevice;
    private AtomicReference<Integer> removedDevice;
    private AtomicBoolean scanningFinishedCalled;
    private AtomicReference<Error> errorReceived;
    private AtomicReference<InputReading> sensorReadingReceived;
    private AtomicBoolean connectedCalled;

    @BeforeEach
    void setup() {
        client = new TestButtplugClient("Test Client");
        addedDevice = new AtomicReference<>();
        updatedDevice = new AtomicReference<>();
        removedDevice = new AtomicReference<>();
        scanningFinishedCalled = new AtomicBoolean(false);
        errorReceived = new AtomicReference<>();
        sensorReadingReceived = new AtomicReference<>();
        connectedCalled = new AtomicBoolean(false);
    }

    @Test
    void testConstructor() {
        assertNotNull(client);
        assertEquals(ButtplugClient.ConnectionState.DISCONNECTED, client.getConnectionState());
        assertFalse(client.isConnected());
    }

    @Test
    void testGetNextMsgId() {
        assertEquals(1, client.getNextMsgId());
        assertEquals(2, client.getNextMsgId());
        assertEquals(3, client.getNextMsgId());
    }

    @Test
    void testConnectionStates() {
        assertEquals(ButtplugClient.ConnectionState.DISCONNECTED, client.getConnectionState());
        assertFalse(client.isConnected());

        client.setConnectionState(ButtplugClient.ConnectionState.CONNECTING);
        assertEquals(ButtplugClient.ConnectionState.CONNECTING, client.getConnectionState());
        assertFalse(client.isConnected());

        client.setConnectionState(ButtplugClient.ConnectionState.CONNECTED);
        assertEquals(ButtplugClient.ConnectionState.CONNECTED, client.getConnectionState());
        assertTrue(client.isConnected());
    }

    @Test
    void testSetAndGetDeviceAddedHandler() {
        IDeviceAddedEvent handler = device -> addedDevice.set(device);
        client.setDeviceAdded(handler);
        assertEquals(handler, client.getDeviceAdded());
    }

    @Test
    void testSetAndGetDeviceRemovedHandler() {
        IDeviceRemovedEvent handler = device -> removedDevice.set(device);
        client.setDeviceRemoved(handler);
        assertEquals(handler, client.getDeviceRemoved());
    }

    @Test
    void testSetAndGetScanningFinishedHandler() {
        IScanningEvent handler = () -> scanningFinishedCalled.set(true);
        client.setScanningFinished(handler);
        assertEquals(handler, client.getScanningFinished());
    }

    @Test
    void testSetAndGetErrorReceivedHandler() {
        IErrorEvent handler = error -> errorReceived.set(error);
        client.setErrorReceived(handler);
        assertEquals(handler, client.getErrorReceived());
    }

    @Test
    void testSetAndGetSensorReadingReceivedHandler() {
        ISensorReadingEvent handler = reading -> sensorReadingReceived.set(reading);
        client.setSensorReadingReceived(handler);
        assertEquals(handler, client.getSensorReadingReceived());
    }

    @Test
    void testSetAndGetOnConnectedHandler() {
        IConnectedEvent handler = c -> connectedCalled.set(true);
        client.setOnConnected(handler);
        assertEquals(handler, client.getOnConnected());
    }

    @Test
    void testOnMessageWithOk() {
        AtomicInteger completedId = new AtomicInteger(-1);
        CompletableFuture<ButtplugMessage> future = new CompletableFuture<>();
        future.thenAccept(msg -> {
            if (msg instanceof Ok) {
                completedId.set(msg.getId());
            }
        });

        client.scheduleWait(5, future);

        List<ButtplugMessage> messages = new ArrayList<>();
        messages.add(new Ok(5));
        client.onMessage(messages);

        assertTrue(future.isDone());
        assertEquals(5, completedId.get());
    }

    @Test
    void testOnMessageWithError() {
        client.setErrorReceived(error -> errorReceived.set(error));

        List<ButtplugMessage> messages = new ArrayList<>();
        Error error = new Error("Test error", Error.ErrorClass.ERROR_DEVICE, 0);
        messages.add(error);
        client.onMessage(messages);

        assertNotNull(errorReceived.get());
        assertEquals("Test error", errorReceived.get().getErrorMessage());
    }

    @Test
    void testOnMessageWithScanningFinished() {
        client.setScanningFinished(() -> scanningFinishedCalled.set(true));

        List<ButtplugMessage> messages = new ArrayList<>();
        messages.add(new ScanningFinished());
        client.onMessage(messages);

        assertTrue(scanningFinishedCalled.get());
    }

    @Test
    void testOnMessageWithInputReading() {
        client.setSensorReadingReceived(reading -> sensorReadingReceived.set(reading));

        List<ButtplugMessage> messages = new ArrayList<>();
        InputReading reading = new InputReading(0, 1, 1);
        messages.add(reading);
        client.onMessage(messages);

        assertNotNull(sensorReadingReceived.get());
    }

    @Test
    void testStartScanningAsync() {
        CompletableFuture<ButtplugMessage> future = (CompletableFuture<ButtplugMessage>) client.startScanningAsync();

        assertNotNull(future);
        assertInstanceOf(StartScanning.class, client.lastSentMessage);
    }

    @Test
    void testStopScanningAsync() {
        CompletableFuture<ButtplugMessage> future = (CompletableFuture<ButtplugMessage>) client.stopScanningAsync();

        assertNotNull(future);
        assertInstanceOf(StopScanning.class, client.lastSentMessage);
    }

    @Test
    void testStopAllDevicesAsync() {
        CompletableFuture<ButtplugMessage> future = (CompletableFuture<ButtplugMessage>) client.stopAllDevicesAsync();

        assertNotNull(future);
        assertInstanceOf(StopCmd.class, client.lastSentMessage);
    }

    @Test
    void testStartScanning() throws ExecutionException, InterruptedException, IOException, TimeoutException {
        client.setNextResponse(new Ok(1));
        boolean result = client.startScanning();

        assertTrue(result);
        assertInstanceOf(StartScanning.class, client.lastSentMessage);
    }

    @Test
    void testStopScanning() throws ExecutionException, InterruptedException, TimeoutException {
        client.setNextResponse(new Ok(1));
        boolean result = client.stopScanning();

        assertTrue(result);
        assertInstanceOf(StopScanning.class, client.lastSentMessage);
    }

    @Test
    void testStopAllDevices() throws ExecutionException, InterruptedException, IOException, TimeoutException {
        client.setNextResponse(new Ok(1));
        boolean result = client.stopAllDevices();

        assertTrue(result);
        assertInstanceOf(StopCmd.class, client.lastSentMessage);
    }

    @Test
    void testGetDevicesReturnsEmptyListInitially() {
        List<ButtplugClientDevice> devices = client.getDevices();

        assertNotNull(devices);
        assertTrue(devices.isEmpty());
    }

    @Test
    void testRequestDeviceList() throws ButtplugClientException, ExecutionException, InterruptedException, TimeoutException {

        // Create test device
        Device device = new Device(0, "Test Device", new HashMap<>(), 100, "Display Name");
        HashMap<Integer, Device> devices = new HashMap<>();
        devices.put(0, device);

        DeviceList deviceList = new DeviceList(devices, 1);
        client.setNextResponse(deviceList);

        client.setDeviceAdded(dev -> addedDevice.set(dev));
        client.setDeviceRemoved(dev -> removedDevice.set(dev));
        client.setDeviceChanged(dev -> updatedDevice.set(dev));
        client.requestDeviceList();
        ButtplugMessage msg = client.sentMessages.remove(0);
        assertInstanceOf(RequestDeviceList.class, msg);

        // Since we don't exactly mock commands exactly, we need to throw the response back to the client
        client.onMessage(Collections.singletonList(deviceList));

        assertNotNull(addedDevice.get());
        assertNull(removedDevice.get());
        assertNull(updatedDevice.get());
        assertEquals("Test Device", addedDevice.get().getName());
        assertEquals(1, client.getDevices().size());
        addedDevice.set(null);

        Device device2 = new Device(1, "Test Device 2", new HashMap<>(), 100, "Other");
        devices.put(1, device2);

        client.onMessage(Collections.singletonList(deviceList));

        assertNotNull(addedDevice.get());
        assertNull(removedDevice.get());
        assertEquals("Test Device 2", addedDevice.get().getName());
        assertEquals(2, client.getDevices().size());
        addedDevice.set(null);

        devices.remove(0);
        device2.setDeviceName("Bob");
        client.onMessage(Collections.singletonList(deviceList));

        assertNull(addedDevice.get());
        assertNotNull(removedDevice.get());
        assertEquals(0, removedDevice.get());
        assertNotNull(updatedDevice.get());
        assertEquals("Bob", updatedDevice.get().getName());
        assertEquals(1, client.getDevices().size());
    }

    @Test
    void testRequestDeviceListWithError() {
        client.setNextResponse(new Error("Test error", Error.ErrorClass.ERROR_DEVICE, 1));

        assertThrows(ButtplugClientException.class, () -> client.requestDeviceList());
    }

    @Test
    void testSendDeviceMessageWithValidDevice() throws Exception {
        // Add a device first
        Device device = new Device(5, "Test Device", new HashMap<>(), 100, "Display Name");

        HashMap<Integer, Device> devices = new HashMap<>();
        devices.put(5, device);

        DeviceList deviceList = new DeviceList(devices, 1);
        client.setNextResponse(deviceList);
        client.requestDeviceList();

        // Now send a message to the device
        ButtplugClientDevice clientDevice = client.getDevices().get(0);
        StopCmd stopCmd = new StopCmd(2, 5);
        client.setNextResponse(new Ok(2));

        CompletableFuture<ButtplugMessage> result = client.sendMessage(stopCmd);

        assertNotNull(result);
        ButtplugMessage response = result.get();
        assertInstanceOf(Ok.class, response);
    }

    @Test
    void testSendDeviceMessageWithInvalidDevice() {
        Device device = new Device(999, "Invalid Device", new HashMap<>(), 100, "");


        ButtplugClientDevice clientDevice = new ButtplugClientDevice(client, device);
        OutputCmd outCmd = new OutputCmd(1, 999, 0);

        CompletableFuture<ButtplugMessage> result = client.sendDeviceMessage(clientDevice, outCmd);

        assertTrue(result.isDone());
        assertInstanceOf(Error.class, result.getNow(null));
        Error error = (Error) result.getNow(null);
        assertEquals("Device not available.", error.getErrorMessage());
    }

    @Test
    void testDisconnect() {
        client.setConnectionState(ButtplugClient.ConnectionState.CONNECTED);

        CompletableFuture<ButtplugMessage> future = new CompletableFuture<>();
        client.scheduleWait(10, future);

        client.disconnect();

        assertTrue(future.isDone());
        assertInstanceOf(Error.class, future.getNow(null));
        Error error = (Error) future.getNow(null);
        assertEquals("Connection closed!", error.getErrorMessage());
        assertTrue(client.cleanupCalled);
    }

    @Test
    void testDoHandshakeSuccess() {
        ServerInfo serverInfo = new ServerInfo("Test Server", 4, 0, 0, 1);
        client.setNextResponse(serverInfo);
        client.setNextResponse(new DeviceList(new HashMap<>(), 2));
        client.setOnConnected(c -> connectedCalled.set(true));

        client.doHandshake();

        assertEquals(ButtplugClient.ConnectionState.CONNECTED, client.getConnectionState());
        assertTrue(connectedCalled.get());
    }

    @Test
    void testDoHandshakeWithPing() throws InterruptedException {
        ServerInfo serverInfo = new ServerInfo("Test Server", 4, 0, 500, 1);
        client.setNextResponse(serverInfo);
        client.setNextResponse(new DeviceList(new HashMap<>(), 2));
        client.setNextResponse(new Ok(3)); // For ping

        client.doHandshake();

        assertEquals(ButtplugClient.ConnectionState.CONNECTED, client.getConnectionState());

        // Wait a bit to see if ping timer fires
        Thread.sleep(300);

        // Verify ping was sent
        boolean foundPing = false;
        for (ButtplugMessage msg : client.sentMessages) {
            if (msg instanceof Ping) {
                foundPing = true;
                break;
            }
        }
        assertTrue(foundPing);
    }

    @Test
    void testDoHandshakeWithError() {
        client.setNextResponse(new Error("Handshake failed", Error.ErrorClass.ERROR_UNKNOWN, 1));
        client.setErrorReceived(error -> errorReceived.set(error));

        client.doHandshake();

        assertNotNull(errorReceived.get());
    }

    @Test
    void testMultipleDevicesInDeviceList() throws Exception {
        Device device1 = new Device(0, "Device 1", new HashMap<>(), 100, "");
        Device device2 = new Device(1, "Device 2", new HashMap<>(), 100, "");

        HashMap<Integer, Device> devices = new HashMap<>();
        devices.put(0, device1);
        devices.put(1, device2);

        DeviceList deviceList = new DeviceList(devices, 1);
        client.setNextResponse(deviceList);

        AtomicInteger deviceCount = new AtomicInteger(0);
        client.setDeviceAdded(dev -> deviceCount.incrementAndGet());
        client.requestDeviceList();

        assertEquals(2, deviceCount.get());
        assertEquals(2, client.getDevices().size());
    }

    @Test
    void testScheduleWait() {
        CompletableFuture<ButtplugMessage> future = new CompletableFuture<>();
        CompletableFuture<ButtplugMessage> result = client.scheduleWait(42, future);

        assertSame(future, result);
        assertFalse(result.isDone());

        // Simulate receiving a message
        List<ButtplugMessage> messages = new ArrayList<>();
        messages.add(new Ok(42));
        client.onMessage(messages);

        assertTrue(result.isDone());
        assertInstanceOf(Ok.class, result.getNow(null));
    }

    @Test
    void testGetParser() {
        assertNotNull(client.getParser());
    }

    @Test
    void testWaitForOk() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<ButtplugMessage> okFuture = CompletableFuture.completedFuture(new Ok(1));
        assertTrue(client.waitForOk(okFuture));

        CompletableFuture<ButtplugMessage> errorFuture = CompletableFuture.completedFuture(
                new Error("Error", Error.ErrorClass.ERROR_UNKNOWN, 1)
        );
        assertFalse(client.waitForOk(errorFuture));
    }

    /**
     * Concrete implementation of ButtplugClient for testing purposes
     */
    private static class TestButtplugClient extends ButtplugClient {
        private final List<ButtplugMessage> messageQueue = new ArrayList<>();
        ButtplugMessage lastSentMessage;
        List<ButtplugMessage> sentMessages = new ArrayList<>();
        boolean cleanupCalled = false;
        private int queueIndex = 0;

        public TestButtplugClient(String clientName) {
            super(clientName);
        }

        public void setNextResponse(ButtplugMessage message) {
            messageQueue.add(message);
        }

        @Override
        protected CompletableFuture<ButtplugMessage> sendMessage(ButtplugMessage msg) {
            lastSentMessage = msg;
            sentMessages.add(msg);

            CompletableFuture<ButtplugMessage> future = new CompletableFuture<>();
            scheduleWait(msg.getId(), future);

            if (queueIndex < messageQueue.size()) {
                ButtplugMessage response = messageQueue.get(queueIndex++);
                response.setId(msg.getId());
                onMessage(Collections.singletonList(response));
            } else {
                onMessage(Collections.singletonList(new Ok(msg.getId())));
            }

            return future;
        }

        @Override
        protected void cleanup() {
            cleanupCalled = true;
        }
    }

    @Test
    void testStopAllDevicesAsyncWithInputsOutputs() {
        CompletableFuture<ButtplugMessage> future =
                (CompletableFuture<ButtplugMessage>) client.stopAllDevicesAsync(true, false);

        assertNotNull(future);
        assertInstanceOf(StopCmd.class, client.lastSentMessage);

        StopCmd stopCmd = (StopCmd) client.lastSentMessage;
        assertEquals(Boolean.TRUE, stopCmd.getInputs());
        assertEquals(Boolean.FALSE, stopCmd.getOutputs());
    }

    @Test
    void testStopAllDevicesWithInputsOutputs() throws ExecutionException, InterruptedException, IOException, TimeoutException {
        client.setNextResponse(new Ok(1));
        boolean result = client.stopAllDevices(true, false);

        assertTrue(result);
        assertInstanceOf(StopCmd.class, client.lastSentMessage);

        StopCmd stopCmd = (StopCmd) client.lastSentMessage;
        assertEquals(Boolean.TRUE, stopCmd.getInputs());
        assertEquals(Boolean.FALSE, stopCmd.getOutputs());
    }
}
