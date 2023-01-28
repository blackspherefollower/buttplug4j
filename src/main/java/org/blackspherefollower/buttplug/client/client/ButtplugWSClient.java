package org.blackspherefollower.buttplug.client.client;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;
import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.DeviceAdded;
import org.blackspherefollower.buttplug.protocol.messages.DeviceList;
import org.blackspherefollower.buttplug.protocol.messages.DeviceRemoved;
import org.blackspherefollower.buttplug.protocol.messages.Error;
import org.blackspherefollower.buttplug.protocol.messages.Ok;
import org.blackspherefollower.buttplug.protocol.messages.Parts.DeviceMessageInfo;
import org.blackspherefollower.buttplug.protocol.messages.Ping;
import org.blackspherefollower.buttplug.protocol.messages.RequestDeviceList;
import org.blackspherefollower.buttplug.protocol.messages.RequestServerInfo;
import org.blackspherefollower.buttplug.protocol.messages.ScanningFinished;
import org.blackspherefollower.buttplug.protocol.messages.SensorReading;
import org.blackspherefollower.buttplug.protocol.messages.ServerInfo;
import org.blackspherefollower.buttplug.protocol.messages.StartScanning;
import org.blackspherefollower.buttplug.protocol.messages.StopAllDevices;
import org.blackspherefollower.buttplug.protocol.messages.StopScanning;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class ButtplugWSClient {

    private final ButtplugJsonMessageParser parser;
    private final Object sendLock = new Object();
    private final String clientName;
    private final ConcurrentHashMap<Long, CompletableFuture<ButtplugMessage>> waitingMsgs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, ButtplugClientDevice> devices = new ConcurrentHashMap<>();
    private final AtomicLong msgId = new AtomicLong(1);
    public IDeviceEvent deviceAdded;
    public IDeviceEvent deviceRemoved;
    public IScanningEvent scanningFinished;
    public IErrorEvent errorReceived;
    public ISensorReadingEvent sensorReadingReceived;
    private WebSocketClient client;
    private Session session;
    private Timer pingTimer;

    public ButtplugWSClient(final String aClientName) {
        clientName = aClientName;
        parser = new ButtplugJsonMessageParser();
    }

    public final long getNextMsgId() {
        return msgId.getAndIncrement();
    }

    public final void connect(final URI url) throws Exception {

        if (client != null && session != null && session.isOpen()) {
            throw new IllegalStateException("WS is already open");
        }

        client = new WebSocketClient();

        waitingMsgs.clear();
        devices.clear();
        msgId.set(1);

        client.start();
        client.connect(this, url, new ClientUpgradeRequest()).get();

        ButtplugMessage res = sendMessage(new RequestServerInfo(clientName, getNextMsgId())).get();
        if (res instanceof ServerInfo) {
            if (((ServerInfo) res).maxPingTime > 0) {
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
                }, 0, Math.round(((double) ((ServerInfo) res).maxPingTime) / 2));
            }

        } else if (res instanceof Error) {
            throw new Exception(((Error) res).errorMessage);
        } else {
            throw new Exception("Unexpected message returned: " + res.getClass().getName());
        }
    }

    @OnWebSocketClose
    @SuppressWarnings("unused")
    public final void onClose(final int statusCode, final String reason) {
        this.session = null;
    }

    @OnWebSocketConnect
    @SuppressWarnings("unused")
    public final void onConnect(final Session newSession) {
        this.session = newSession;
    }

    public final void disconnect() {
        if (pingTimer != null) {
            pingTimer.cancel();
            pingTimer = null;
        }

        if (session != null) {
            try {
                session.disconnect();
            } catch (IOException e) {
                // noop - something when wrong closing the socket, but we're
                // about to dispose of it anyway.
            }
        }
        client = null;

        int max = 3;
        while (max-- > 0 && waitingMsgs.size() != 0) {
            for (long waitMmsgId : waitingMsgs.keySet()) {
                CompletableFuture<ButtplugMessage> val = waitingMsgs.remove(waitMmsgId);
                if (val != null) {
                    val.complete(new Error("Connection closed!",
                            Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SystemMsgId));
                }
            }
        }

        msgId.set(1);
    }

    @OnWebSocketMessage
    public final void onMessage(final String buf) {
        try {
            List<ButtplugMessage> msgs = parser.parseJson(buf);

            for (ButtplugMessage msg : msgs) {
                if (msg.id > 0) {
                    CompletableFuture<ButtplugMessage> val = waitingMsgs.remove(msg.id);
                    if (val != null) {
                        val.complete(msg);
                        continue;
                    }
                }

                if (msg instanceof DeviceAdded) {
                    ButtplugClientDevice device = new ButtplugClientDevice(this, (DeviceAdded) msg);
                    devices.put(((DeviceAdded) msg).deviceIndex, device);
                    if (deviceAdded != null) {
                        deviceAdded.deviceAdded(device);
                    }
                } else if (msg instanceof DeviceRemoved) {
                    if (devices.remove(((DeviceRemoved) msg).deviceIndex) != null) {
                        if (deviceRemoved != null) {
                            deviceRemoved.deviceRemoved(((DeviceRemoved) msg).deviceIndex);
                        }
                    }
                } else if (msg instanceof ScanningFinished) {
                    if (scanningFinished != null) {
                        scanningFinished.scanningFinished();
                    }
                } else if (msg instanceof Error) {
                    if (errorReceived != null) {
                        errorReceived.errorReceived((Error) msg);
                    }
                } else if (msg instanceof SensorReading) {
                    if (sensorReadingReceived != null) {
                        sensorReadingReceived.sensorReadingReceived((SensorReading) msg);
                    }
                }
            }
        } catch (IOException e) {
            if (errorReceived != null) {
                errorReceived.errorReceived(new Error(e.getMessage(),
                        Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SystemMsgId));
            } else {
                e.printStackTrace();
            }
        }
    }

    private void onPingTimer() throws Exception {
        try {
            ButtplugMessage msg = sendMessage(new Ping(msgId.incrementAndGet())).get();
            if (msg instanceof Error) {
                throw new Exception(((Error) msg).errorMessage);
            }
        } catch (Throwable e) {
            if (client != null) {
                disconnect();
            }
            throw e;
        }
    }

    public final void requestDeviceList() throws Exception {
        ButtplugMessage res = sendMessage(new RequestDeviceList(msgId.incrementAndGet())).get();
        if (!(res instanceof DeviceList) || ((DeviceList) res).devices == null) {
            if (res instanceof Error) {
                throw new Exception(((Error) res).errorMessage);
            }
            return;
        }

        for (DeviceMessageInfo d : ((DeviceList) res).devices) {
            if (!devices.containsKey(d.deviceIndex)) {
                ButtplugClientDevice device = new ButtplugClientDevice(this, d);
                if (devices.put(d.deviceIndex, device) == null) {
                    if (deviceAdded != null) {
                        deviceAdded.deviceAdded(device);
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
                        Error.ErrorClass.ERROR_DEVICE, ButtplugConsts.SystemMsgId));
            }

            deviceMsg.deviceIndex = device.getDeviceIndex();
            deviceMsg.id = msgId.incrementAndGet();
            return sendMessage(deviceMsg);
        } else {
            return CompletableFuture.completedFuture(new Error("Device not available.",
                    Error.ErrorClass.ERROR_DEVICE, ButtplugConsts.SystemMsgId));
        }
    }

    protected final boolean waitForOk(final Future<ButtplugMessage> msg)
            throws ExecutionException, InterruptedException, IOException {
        return msg.get() instanceof Ok;
    }


    protected final CompletableFuture<ButtplugMessage> sendMessage(final ButtplugMessage msg)
            throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<ButtplugMessage> promise = new CompletableFuture<>();

        waitingMsgs.put(msg.id, promise);
        if (session == null) {
            return CompletableFuture.completedFuture(new Error("Bad WS state!",
                    Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SystemMsgId));
        }

        try {
            Future<Void> fut = session.getRemote().sendStringByFuture(parser.formatJson(msg));
            fut.get();
        } catch (WebSocketException e) {
            return CompletableFuture.completedFuture(new Error(e.getMessage(),
                    Error.ErrorClass.ERROR_UNKNOWN, msg.id));
        }

        return promise;
    }
}
