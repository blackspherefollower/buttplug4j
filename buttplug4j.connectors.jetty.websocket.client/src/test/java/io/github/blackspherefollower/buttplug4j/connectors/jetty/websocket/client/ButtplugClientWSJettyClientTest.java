package io.github.blackspherefollower.buttplug4j.connectors.jetty.websocket.client;

import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ButtplugClientWSJettyClientTest {

    @Disabled
    @Test
    public void TestConnect() throws Exception {
        ButtplugClientWSClient client = new ButtplugClientWSClient("Java Test");
        client.connect(new URI("ws://localhost:12345/buttplug"));
        client.startScanning();

        Thread.sleep(5000);
        client.requestDeviceList();
        for (ButtplugClientDevice dev : client.getDevices()) {
            if (dev.getScalarVibrateCount() > 0) {
                dev.sendScalarVibrateCmd(0.5).get();
            }
        }

        Thread.sleep(1000);
        assertTrue(client.stopAllDevices());

        Thread.sleep(60000);
        for (ButtplugClientDevice dev : client.getDevices()) {
            if (dev.getScalarVibrateCount() > 0) {
                dev.sendScalarVibrateCmd(0.5).get();
            }
        }

        Thread.sleep(1000);
        assertTrue(client.stopAllDevices());

        Thread.sleep(60000);
        for (ButtplugClientDevice dev : client.getDevices()) {
            if (dev.getScalarVibrateCount() > 0) {
                dev.sendScalarVibrateCmd(0.5).get();
            }
        }

        Thread.sleep(1000);
        assertTrue(client.stopAllDevices());

        client.disconnect();
    }
}