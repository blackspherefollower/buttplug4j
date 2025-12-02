package io.github.blackspherefollower.buttplug4j.connectors.jetty.websocket.client;

import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import io.github.blackspherefollower.buttplug4j.client.ButtplugDeviceException;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.utils.test.IntifaceEngineWrapper;
import io.github.blackspherefollower.buttplug4j.utils.test.WSDMClient;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ButtplugClientWSJettyClientTest {


    @Test
    public void TestConnect() throws Exception {
        try (IntifaceEngineWrapper wrapper = new IntifaceEngineWrapper()) {
            Thread.sleep(500);
            WSDMClient wsdev = new WSDMClient(new URI("ws://localhost:" + wrapper.dport), "LVS-Fake", "A9816725B");
            Thread.sleep(500);

            ButtplugClientWSClient client = new ButtplugClientWSClient("Java Test");
            client.connect(new URI("ws://localhost:" + wrapper.cport + "/buttplug"));
            client.startScanning();

            Thread.sleep(500);
            client.requestDeviceList();

            assertEquals(1, client.getDevices().size());
            for (ButtplugClientDevice dev : client.getDevices()) {
                if (dev.getScalarVibrateCount() > 0) {
                    dev.sendScalarVibrateCmd(0.5F).get();
                }
            }

            Thread.sleep(500);
            assertEquals("Vibrate:10;", wsdev.messages.poll());

            assertTrue(client.stopAllDevices());
            Thread.sleep(500);
            assertEquals("Vibrate:0;", wsdev.messages.poll());

            client.disconnect();
        }
    }

    @Test
    public void TestBattery() throws Exception {
        try (IntifaceEngineWrapper wrapper = new IntifaceEngineWrapper()) {
            Thread.sleep(500);
            WSDMClient wsdev = new WSDMClient(new URI("ws://localhost:" + wrapper.dport), "LVS-Fake", "A9816725B");
            Thread.sleep(500);

            ButtplugClientWSClient client = new ButtplugClientWSClient("Java Test");
            client.connect(new URI("ws://localhost:" + wrapper.cport + "/buttplug"));
            client.startScanning();

            Thread.sleep(500);
            client.requestDeviceList();
            for (ButtplugClientDevice dev : client.getDevices()) {
                if (dev.hasBatterySensor()) {
                    long battery = dev.readBatteryLevel();
                    System.out.println("Battery is " + battery);
                    assertTrue(battery >= 0);
                    assertTrue(battery <= 100);
                } else {
                    assertThrows(ButtplugDeviceException.class, dev::readBatteryLevel);
                }
            }

            client.disconnect();
        }
    }
}