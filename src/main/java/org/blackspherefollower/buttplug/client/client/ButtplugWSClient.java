package org.blackspherefollower.buttplug.client.client;

import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;
import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.Error;
import org.blackspherefollower.buttplug.protocol.messages.*;
import org.blackspherefollower.buttplug.protocol.messages.Parts.DeviceMessageInfo;
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

    private final ButtplugJsonMessageParser _parser;
    private final Object sendLock = new Object();
    private final String _clientName;
    private final ConcurrentHashMap<Long, CompletableFuture<ButtplugMessage>> _waitingMsgs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, ButtplugClientDevice> _devices = new ConcurrentHashMap<>();
    private final AtomicLong msgId = new AtomicLong(1);
    public IDeviceEvent deviceAdded;
    public IDeviceEvent deviceRemoved;
    public IScanningEvent scanningFinished;
    public IErrorEvent errorReceived;
    public ISensorReadingEvent sensorReadingReceived;
    private int _messageSchemaVersion;
    private WebSocketClient client;
    private Session session;
    private Timer _pingTimer;

    public ButtplugWSClient(String aClientName) {
        _clientName = aClientName;
        _parser = new ButtplugJsonMessageParser();
    }

    public long getNextMsgId() {
        return msgId.getAndIncrement();
    }

    public void Connect(URI url) throws Exception {

        if (client != null && session != null && session.isOpen()) {
            throw new IllegalStateException("WS is already open");
        }

        client = new WebSocketClient();

        _waitingMsgs.clear();
        _devices.clear();
        msgId.set(1);

        client.start();
        client.connect(this, url, new ClientUpgradeRequest()).get();

        ButtplugMessage res = sendMessage(new RequestServerInfo(_clientName, getNextMsgId())).get();
        if (res instanceof ServerInfo) {
            if (((ServerInfo) res).maxPingTime > 0) {
                _pingTimer = new Timer("pingTimer", true);
                _pingTimer.scheduleAtFixedRate(new TimerTask() {
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
    public void onClose(int statusCode, String reason) {
        this.session = null;
    }

    @OnWebSocketConnect
    @SuppressWarnings("unused")
    public void onConnect(Session session) {
        this.session = session;
    }

    public void Disconnect() {
        if (_pingTimer != null) {
            _pingTimer.cancel();
            _pingTimer = null;
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
        while (max-- > 0 && _waitingMsgs.size() != 0) {
            for (long msgId : _waitingMsgs.keySet()) {
                CompletableFuture<ButtplugMessage> val = _waitingMsgs.remove(msgId);
                if (val != null) {
                    val.complete(new Error("Connection closed!", Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SystemMsgId));
                }
            }
        }

        msgId.set(1);
    }

    @OnWebSocketMessage
    @SuppressWarnings("unused")
    public void onMessage(String buf) {
        try {
            List<ButtplugMessage> msgs = _parser.parseJson(buf);

            for (ButtplugMessage msg : msgs) {
                if (msg.id > 0) {
                    CompletableFuture<ButtplugMessage> val = _waitingMsgs.remove(msg.id);
                    if (val != null) {
                        val.complete(msg);
                        continue;
                    }
                }

                if (msg instanceof DeviceAdded) {
                    ButtplugClientDevice device = new ButtplugClientDevice(this, (DeviceAdded) msg);
                    _devices.put(((DeviceAdded) msg).deviceIndex, device);
                    if (deviceAdded != null) {
                        deviceAdded.deviceAdded(device);
                    }
                } else if (msg instanceof DeviceRemoved) {
                    if (_devices.remove(((DeviceRemoved) msg).deviceIndex) != null) {
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
                Disconnect();
            }
            throw e;
        }
    }

    public void requestDeviceList() throws Exception {
        ButtplugMessage res = sendMessage(new RequestDeviceList(msgId.incrementAndGet())).get();
        if (!(res instanceof DeviceList) || ((DeviceList) res).devices == null) {
            if (res instanceof Error) {
                throw new Exception(((Error) res).errorMessage);
            }
            return;
        }

        for (DeviceMessageInfo d : ((DeviceList) res).devices) {
            if (!_devices.containsKey(d.deviceIndex)) {
                ButtplugClientDevice device = new ButtplugClientDevice(this, d);
                if (_devices.put(d.deviceIndex, device) == null) {
                    if (deviceAdded != null) {
                        deviceAdded.deviceAdded(device);
                    }
                }
            }
        }
    }

    public List<ButtplugClientDevice> getDevices() {
        List<ButtplugClientDevice> devices = new ArrayList<>();
        devices.addAll(_devices.values());
        return devices;
    }

    public boolean startScanning() throws ExecutionException, InterruptedException, IOException {
        return sendMessageExpectOk(new StartScanning(msgId.incrementAndGet()));
    }

    public boolean stopScanning() throws ExecutionException, InterruptedException, IOException {
        return sendMessageExpectOk(new StopScanning(msgId.incrementAndGet()));
    }

    public boolean stopAllDevices() throws ExecutionException, InterruptedException, IOException {
        return sendMessageExpectOk(new StopAllDevices(msgId.incrementAndGet()));
    }

    public CompletableFuture<ButtplugMessage> sendDeviceMessage(ButtplugClientDevice device, ButtplugDeviceMessage deviceMsg) throws ExecutionException, InterruptedException, IOException {
        ButtplugClientDevice dev = _devices.get(device.index);
        if (dev != null) {
            if (!dev.deviceMessages.containsKey(deviceMsg.getClass().getSimpleName())) {
                return CompletableFuture.completedFuture(new Error("Device does not accept message type: " + deviceMsg.getClass().getSimpleName(), Error.ErrorClass.ERROR_DEVICE, ButtplugConsts.SystemMsgId));
            }

            deviceMsg.deviceIndex = device.index;
            deviceMsg.id = msgId.incrementAndGet();
            return sendMessage(deviceMsg);
        } else {
            return CompletableFuture.completedFuture(new Error("Device not available.", Error.ErrorClass.ERROR_DEVICE, ButtplugConsts.SystemMsgId));
        }
    }

    protected boolean sendMessageExpectOk(ButtplugMessage msg) throws ExecutionException, InterruptedException, IOException {
        return sendMessage(msg).get() instanceof Ok;
    }


    protected CompletableFuture<ButtplugMessage> sendMessage(ButtplugMessage msg) throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<ButtplugMessage> promise = new CompletableFuture<>();

        _waitingMsgs.put(msg.id, promise);
        if (session == null) {
            return CompletableFuture.completedFuture(new Error("Bad WS state!", Error.ErrorClass.ERROR_UNKNOWN, ButtplugConsts.SystemMsgId));
        }

        try {
            Future<Void> fut = session.getRemote().sendStringByFuture(_parser.formatJson(msg));
            fut.get();
        } catch (WebSocketException e) {
            return CompletableFuture.completedFuture(new Error(e.getMessage(), Error.ErrorClass.ERROR_UNKNOWN, msg.id));
        }

        return promise;
    }

    public CompletableFuture<ButtplugMessage> sendStopAllDevicesCmd() throws IOException, ExecutionException, InterruptedException {
        return sendMessage(new StopAllDevices(getNextMsgId()));
    }
}