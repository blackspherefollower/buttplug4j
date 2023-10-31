package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Error;
import io.github.blackspherefollower.buttplug4j.protocol.messages.*;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.DeviceMessageInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ButtplugClient is the abstract class containing the bulk of the logic for communicating with the Buttplug.io sever
 * Intiface Central.
 * <br/>
 * <br/>
 * The transport logic is not in this package, as the various websocket libraries have differing
 * requirements that will not fit all use-cases. In general, the Java8 compatible Jetty Websocket Client connector will
 * meet the needs of most users.
 *
 * @see io.github.blackspherefollower.buttplug4j.connectors.jetty.websocket.client.ButtplugClientWSClient
 */
public abstract class ButtplugClient {
    static final int MAX_DISCONNECT_MESSAGE_TRYS = 3;
    private final ButtplugJsonMessageParser parser;
    private final String clientName;
    private final ConcurrentHashMap<Long, CompletableFuture<ButtplugMessage>> waitingMsgs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, ButtplugClientDevice> devices = new ConcurrentHashMap<>();
    private final AtomicLong msgId = new AtomicLong(1);
    private final Object sendLock = new Object();
    private ConnectionState connectionState = ConnectionState.DISCONNECTED;
    private Timer pingTimer;
    private IDeviceEvent deviceAdded;
    private IDeviceEvent deviceRemoved;
    private IScanningEvent scanningFinished;
    private IErrorEvent errorReceived;
    private ISensorReadingEvent sensorReadingReceived;
    private IConnectedEvent onConnected;

    public ButtplugClient(final String aClientName) {
        parser = new ButtplugJsonMessageParser();
        clientName = aClientName;
    }

    protected final ButtplugJsonMessageParser getParser() {
        return parser;
    }

    public final ConnectionState getConnectionState() {
        return connectionState;
    }

    protected final void setConnectionState(final ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public final boolean isConnected() {
        return connectionState == ConnectionState.CONNECTED;
    }

    public final long getNextMsgId() {
        return msgId.getAndIncrement();
    }

    public final void onMessage(final List<ButtplugMessage> msgs) {
        for (ButtplugMessage msg : msgs) {
            if (msg.getId() > 0) {
                CompletableFuture<ButtplugMessage> val = waitingMsgs.remove(msg.getId());
                if (val != null) {
                    val.complete(msg);
                    continue;
                }
            }

            if (msg instanceof DeviceAdded) {
                ButtplugClientDevice device = new ButtplugClientDevice(this, (DeviceAdded) msg);
                devices.put(((DeviceAdded) msg).getDeviceIndex(), device);
                if (getDeviceAdded() != null) {
                    getDeviceAdded().deviceAdded(device);
                }
            } else if (msg instanceof DeviceRemoved) {
                if (devices.remove(((DeviceRemoved) msg).getDeviceIndex()) != null) {
                    if (getDeviceRemoved() != null) {
                        getDeviceRemoved().deviceRemoved(((DeviceRemoved) msg).getDeviceIndex());
                    }
                }
            } else if (msg instanceof ScanningFinished) {
                if (getScanningFinished() != null) {
                    getScanningFinished().scanningFinished();
                }
            } else if (msg instanceof Error) {
                if (getErrorReceived() != null) {
                    getErrorReceived().errorReceived((Error) msg);
                }
            } else if (msg instanceof SensorReading) {
                if (getSensorReadingReceived() != null) {
                    getSensorReadingReceived().sensorReadingReceived((SensorReading) msg);
                }
            }
        }
    }

    protected final void doHandshake() {
        waitingMsgs.clear();
        devices.clear();
        msgId.set(1);

        try {
            ButtplugMessage res = sendMessage(new RequestServerInfo(clientName, getNextMsgId())).get();
            if (res instanceof ServerInfo) {
                if (((ServerInfo) res).getMaxPingTime() > 0) {
                    pingTimer = new Timer("pingTimer", true);
                    pingTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                onPingTimer();
                            } catch (Exception e) {
                                if (errorReceived != null) {
                                    errorReceived.errorReceived(new Error(e));
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 0, Math.round(((double) ((ServerInfo) res).getMaxPingTime()) / 2));
                }

                // Populate already connected devices
                requestDeviceList();

            } else if (res instanceof Error) {
                throw new ButtplugClientException(((Error) res).getErrorMessage());
            } else {
                throw new ButtplugClientException("Unexpected message returned: " + res.getClass().getName());
            }
        } catch (ButtplugClientException | InterruptedException | ExecutionException e) {
            if (getErrorReceived() != null) {
                getErrorReceived().errorReceived(new Error(e));
            } else {
                e.printStackTrace();
            }
        }

        connectionState = ConnectionState.CONNECTED;

        if (getOnConnected() != null) {
            getOnConnected().onConnected(this);
        }

    }

    private void onPingTimer() throws ButtplugClientException, ExecutionException, InterruptedException {
        try {
            ButtplugMessage msg = sendMessage(new Ping(msgId.incrementAndGet())).get();
            if (msg instanceof Error) {
                throw new ButtplugClientException(((Error) msg).getErrorMessage());
            }
        } catch (ButtplugClientException | InterruptedException | ExecutionException e) {
            disconnect();
            throw e;
        }
    }

    public final void requestDeviceList() throws ButtplugClientException, ExecutionException, InterruptedException {
        ButtplugMessage res = sendMessage(new RequestDeviceList(msgId.incrementAndGet())).get();
        if (!(res instanceof DeviceList) || ((DeviceList) res).getDevices() == null) {
            if (res instanceof Error) {
                throw new ButtplugClientException(((Error) res).getErrorMessage());
            }
            return;
        }

        for (DeviceMessageInfo d : ((DeviceList) res).getDevices()) {
            if (!devices.containsKey(d.getDeviceIndex())) {
                ButtplugClientDevice device = new ButtplugClientDevice(this, d);
                if (devices.put(d.getDeviceIndex(), device) == null) {
                    if (getDeviceAdded() != null) {
                        getDeviceAdded().deviceAdded(device);
                    }
                }
            }
        }
    }

    public final List<ButtplugClientDevice> getDevices() {
        List<ButtplugClientDevice> devicesCopy = new ArrayList<>();
        devicesCopy.addAll(this.devices.values());
        return devicesCopy;
    }

    public final boolean startScanning() throws ExecutionException, InterruptedException, IOException {
        return waitForOk(startScanningAsync());
    }

    public final Future<ButtplugMessage> startScanningAsync() {
        return sendMessage(new StartScanning(msgId.incrementAndGet()));
    }

    public final boolean stopScanning() throws ExecutionException, InterruptedException {
        return waitForOk(stopScanningAsync());
    }

    public final Future<ButtplugMessage> stopScanningAsync() {
        return sendMessage(new StopScanning(msgId.incrementAndGet()));
    }

    public final boolean stopAllDevices() throws ExecutionException, InterruptedException, IOException {
        return waitForOk(stopAllDevicesAsync());
    }

    public final Future<ButtplugMessage> stopAllDevicesAsync() {
        return sendMessage(new StopAllDevices(getNextMsgId()));
    }

    public final CompletableFuture<ButtplugMessage> sendDeviceMessage(
            final ButtplugClientDevice device, final ButtplugDeviceMessage deviceMsg) {
        ButtplugClientDevice dev = devices.get(device.getDeviceIndex());
        if (dev != null) {
            if (!dev.getDeviceMessages().containsKey(deviceMsg.getClass().getSimpleName())) {
                return CompletableFuture.completedFuture(new Error(
                        "Device does not accept message type: "
                                + deviceMsg.getClass().getSimpleName(),
                        Error.ErrorClass.ERROR_DEVICE, ButtplugConsts.SYSTEM_MSG_ID));
            }

            deviceMsg.setDeviceIndex(device.getDeviceIndex());
            deviceMsg.setId(msgId.incrementAndGet());
            return sendMessage(deviceMsg);
        } else {
            return CompletableFuture.completedFuture(new Error("Device not available.",
                    Error.ErrorClass.ERROR_DEVICE, ButtplugConsts.SYSTEM_MSG_ID));
        }
    }

    protected final boolean waitForOk(final Future<ButtplugMessage> msg)
            throws ExecutionException, InterruptedException {
        return msg.get() instanceof Ok;
    }

    protected abstract CompletableFuture<ButtplugMessage> sendMessage(ButtplugMessage msg);

    public final IDeviceEvent getDeviceAdded() {
        return deviceAdded;
    }

    public final void setDeviceAdded(final IDeviceEvent deviceAddedHandler) {
        this.deviceAdded = deviceAddedHandler;
    }

    public final IDeviceEvent getDeviceRemoved() {
        return deviceRemoved;
    }

    public final void setDeviceRemoved(final IDeviceEvent deviceRemovedHandler) {
        this.deviceRemoved = deviceRemovedHandler;
    }

    public final IScanningEvent getScanningFinished() {
        return scanningFinished;
    }

    public final void setScanningFinished(final IScanningEvent scanningFinishedHandler) {
        this.scanningFinished = scanningFinishedHandler;
    }

    public final IErrorEvent getErrorReceived() {
        return errorReceived;
    }

    public final void setErrorReceived(final IErrorEvent errorReceivedHandler) {
        this.errorReceived = errorReceivedHandler;
    }

    public final ISensorReadingEvent getSensorReadingReceived() {
        return sensorReadingReceived;
    }

    public final void setSensorReadingReceived(final ISensorReadingEvent sensorReadingReceivedHandler) {
        this.sensorReadingReceived = sensorReadingReceivedHandler;
    }

    public final IConnectedEvent getOnConnected() {
        return onConnected;
    }

    public final void setOnConnected(final IConnectedEvent onConnected) {
        this.onConnected = onConnected;
    }

    protected abstract void cleanup();

    public final void disconnect() {
        if (pingTimer != null) {
            pingTimer.cancel();
            pingTimer = null;
        }

        cleanup();

        int max = MAX_DISCONNECT_MESSAGE_TRYS;
        while (max-- > 0 && waitingMsgs.size() != 0) {
            for (long waitMmsgId : waitingMsgs.keySet()) {
                CompletableFuture<ButtplugMessage> val = waitingMsgs.remove(waitMmsgId);
                if (val != null) {
                    val.complete(new Error("Connection closed!",
                            Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SYSTEM_MSG_ID));
                }
            }
        }

        msgId.set(1);
    }

    protected final CompletableFuture<ButtplugMessage> scheduleWait(final long id,
                                                                    final CompletableFuture<ButtplugMessage> promise) {
        waitingMsgs.put(id, promise);
        return promise;
    }

    public enum ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }
}
