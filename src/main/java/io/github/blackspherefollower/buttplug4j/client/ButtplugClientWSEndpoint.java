package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.DeviceMessageInfo;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceAdded;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceList;
import io.github.blackspherefollower.buttplug4j.protocol.messages.DeviceRemoved;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Error;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Ok;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Ping;
import io.github.blackspherefollower.buttplug4j.protocol.messages.RequestDeviceList;
import io.github.blackspherefollower.buttplug4j.protocol.messages.RequestServerInfo;
import io.github.blackspherefollower.buttplug4j.protocol.messages.ScanningFinished;
import io.github.blackspherefollower.buttplug4j.protocol.messages.SensorReading;
import io.github.blackspherefollower.buttplug4j.protocol.messages.ServerInfo;
import io.github.blackspherefollower.buttplug4j.protocol.messages.StartScanning;
import io.github.blackspherefollower.buttplug4j.protocol.messages.StopAllDevices;
import io.github.blackspherefollower.buttplug4j.protocol.messages.StopScanning;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@ClientEndpoint
@ServerEndpoint("/")
public abstract class ButtplugClientWSEndpoint {
    private final ButtplugJsonMessageParser parser;
    private final Object sendLock = new Object();
    final String clientName;
    final ConcurrentHashMap<Long, CompletableFuture<ButtplugMessage>> waitingMsgs = new ConcurrentHashMap<>();
    final ConcurrentHashMap<Long, ButtplugClientDevice> devices = new ConcurrentHashMap<>();
    final AtomicLong msgId = new AtomicLong(1);
    private IDeviceEvent deviceAdded;
    private IDeviceEvent deviceRemoved;
    private IScanningEvent scanningFinished;
    private IErrorEvent errorReceived;
    private ISensorReadingEvent sensorReadingReceived;
    private IConnectedEvent onConnected;
    Session session;
    Timer pingTimer;
    Timer wsPingTimer;

    public final ConnectionState getConnectionState() {
        return connectionState;
    }
    public final boolean isConnected() {
        return connectionState == ConnectionState.CONNECTED;
    }


    public enum ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }
    protected ConnectionState connectionState = ConnectionState.DISCONNECTED;

    public ButtplugClientWSEndpoint(final String aClientName) {
        clientName = aClientName;
        parser = new ButtplugJsonMessageParser();
    }

    public final long getNextMsgId() {
        return msgId.getAndIncrement();
    }

    @OnClose
    @SuppressWarnings("unused")
    public final void onClose(final CloseReason reason) {
        this.session = null;
        connectionState = ConnectionState.DISCONNECTED;
    }

    @OnOpen
    @SuppressWarnings("unused")
    public final void onConnect(final Session newSession) {
        this.session = newSession;

        // Setup websocket ping
        wsPingTimer = new Timer("wsPingTimer", true);
        wsPingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (session != null) {
                        session.getAsyncRemote().sendPing(ByteBuffer.wrap("ping".getBytes()));
                    }
                } catch (IOException e) {
                    wsPingTimer.cancel();
                    wsPingTimer = null;
                    throw new RuntimeException(e);
                }
            }
        },0, 10000);

        // Don't block the WS thread
        new Thread(() -> {
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
                                    e.printStackTrace();
                                }
                            }
                        }, 0, Math.round(((double) ((ServerInfo) res).getMaxPingTime()) / 2));
                    }

                } else if (res instanceof Error) {
                    throw new Exception(((Error) res).getErrorMessage());
                } else {
                    throw new Exception("Unexpected message returned: " + res.getClass().getName());
                }
            } catch (Exception e) {
                if (getErrorReceived() != null) {
                    getErrorReceived().errorReceived(new Error(e.getMessage(), Error.ErrorClass.ERROR_UNKNOWN, -1));
                }
            }

            connectionState = ConnectionState.CONNECTED;

            if (getOnConnected() != null) {
                getOnConnected().onConnected(this);
            }
        }).start();
    }

    @OnMessage
    public final void onMessage(final Session sess, final String message) {
        try {
            List<ButtplugMessage> msgs = parser.parseJson(message);

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
        } catch (IOException e) {
            if (getErrorReceived() != null) {
                getErrorReceived().errorReceived(new Error(e.getMessage(),
                        Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SYSTEM_MSG_ID));
            } else {
                e.printStackTrace();
            }
        }
    }

    void onPingTimer() throws Exception {
        try {
            ButtplugMessage msg = sendMessage(new Ping(msgId.incrementAndGet())).get();
            if (msg instanceof Error) {
                throw new Exception(((Error) msg).getErrorMessage());
            }
        } catch (Throwable e) {
            if (session != null) {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, e.getMessage()));
            }
            throw e;
        }
    }

    public final void requestDeviceList() throws Exception {
        ButtplugMessage res = sendMessage(new RequestDeviceList(msgId.incrementAndGet())).get();
        if (!(res instanceof DeviceList) || ((DeviceList) res).getDevices() == null) {
            if (res instanceof Error) {
                throw new Exception(((Error) res).getErrorMessage());
            }
            return;
        }

        for (DeviceMessageInfo d : ((DeviceList) res).getDevices()) {
            if (!devices.containsKey(d.deviceIndex)) {
                ButtplugClientDevice device = new ButtplugClientDevice(this, d);
                if (devices.put(d.deviceIndex, device) == null) {
                    if (getDeviceAdded() != null) {
                        getDeviceAdded().deviceAdded(device);
                    }
                }
            }
        }
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }

    public final List<ButtplugClientDevice> getDevices() {
        List<ButtplugClientDevice> devicesCopy = new ArrayList<>();
        devicesCopy.addAll(this.devices.values());
        return devicesCopy;
    }

    public final boolean startScanning() throws ExecutionException, InterruptedException, IOException {
        return waitForOk(startScanningAsync());
    }

    public final Future<ButtplugMessage> startScanningAsync()
            throws ExecutionException, InterruptedException, IOException {
        return sendMessage(new StartScanning(msgId.incrementAndGet()));
    }

    public final boolean stopScanning() throws ExecutionException, InterruptedException, IOException {
        return waitForOk(stopScanningAsync());
    }

    public final Future<ButtplugMessage> stopScanningAsync()
            throws ExecutionException, InterruptedException, IOException {
        return sendMessage(new StopScanning(msgId.incrementAndGet()));
    }

    public final boolean stopAllDevices() throws ExecutionException, InterruptedException, IOException {
        return waitForOk(stopAllDevicesAsync());
    }

    public final Future<ButtplugMessage> stopAllDevicesAsync()
            throws IOException, ExecutionException, InterruptedException {
        return sendMessage(new StopAllDevices(getNextMsgId()));
    }

    public final CompletableFuture<ButtplugMessage> sendDeviceMessage(
            final ButtplugClientDevice device, final ButtplugDeviceMessage deviceMsg)
            throws ExecutionException, InterruptedException, IOException {
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
            throws ExecutionException, InterruptedException, IOException {
        return msg.get() instanceof Ok;
    }


    protected final CompletableFuture<ButtplugMessage> sendMessage(final ButtplugMessage msg) {
        CompletableFuture<ButtplugMessage> promise = new CompletableFuture<>();

        waitingMsgs.put(msg.getId(), promise);
        if (session == null) {
            return CompletableFuture.completedFuture(new Error("Bad WS state!",
                    Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SYSTEM_MSG_ID));
        }

        try {
            session.getAsyncRemote().sendText(parser.formatJson(msg)).get();
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new Error(e.getMessage(),
                    Error.ErrorClass.ERROR_UNKNOWN, msg.getId()));
        }
        return promise;
    }

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

    static final int MAX_DISCONNECT_MESSAGE_TRYS = 3;

    public IConnectedEvent getOnConnected() {
        return onConnected;
    }

    public void setOnConnected(IConnectedEvent onConnected) {
        this.onConnected = onConnected;
    }

    public abstract void disconnect();
}
