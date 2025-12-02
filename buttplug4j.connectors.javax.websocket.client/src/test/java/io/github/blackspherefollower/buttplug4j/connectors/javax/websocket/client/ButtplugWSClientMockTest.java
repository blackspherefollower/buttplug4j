package io.github.blackspherefollower.buttplug4j.connectors.javax.websocket.client;

import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDeviceFeature;
import io.github.blackspherefollower.buttplug4j.client.ButtplugDeviceFeatureException;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.InputReading;
import io.github.blackspherefollower.buttplug4j.utils.test.WSDMClient;
import io.github.blackspherefollower.buttplug4j.utils.test.IntifaceEngineWrapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class ButtplugWSClientMockTest {

    @Test
    public void TestConnect() throws Exception {
        try(IntifaceEngineWrapper wrapper = new IntifaceEngineWrapper() ) {
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
                for (ButtplugClientDeviceFeature feat : dev.getDeviceFeatures().values()) {
                    if (feat.HasVibrate()) {
                        feat.VibrateFloat(0.5F).get();
                    }
                }
            }
            assertEquals(wsdev.messages.poll(), "Vibrate:10;");

            Thread.sleep(500);
            assertTrue(client.stopAllDevices());
            assertEquals(wsdev.messages.poll(), "Vibrate:0;");

            client.disconnect();
        }
    }

    @Test
    @Disabled("See https://github.com/buttplugio/buttplug/issues/801")
    public void TestBattery() throws Exception {
        try(IntifaceEngineWrapper wrapper = new IntifaceEngineWrapper() ) {
            Thread.sleep(500);
            WSDMClient wsdev = new WSDMClient(new URI("ws://localhost:" + wrapper.dport), "LVS-Fake", "A9816725B");
            Thread.sleep(500);

            ButtplugClientWSClient client = new ButtplugClientWSClient("Java Test");
            client.connect(new URI("ws://localhost:" + wrapper.cport + "/buttplug"));
            client.startScanning();

            Thread.sleep(500);
            client.requestDeviceList();
            for (ButtplugClientDevice dev : client.getDevices()) {
                for (ButtplugClientDeviceFeature feat : dev.getDeviceFeatures().values()) {
                    if (feat.HasBattery()) {
                        ButtplugMessage res = feat.ReadBattery().get();
                        if (res instanceof InputReading && ((InputReading) res).getData() instanceof InputReading.BatteryData) {
                            InputReading.BatteryData reading = (InputReading.BatteryData) ((InputReading) res).getData();
                            int battery = reading.getValue();
                            System.out.println("Battery is " + battery);
                            assertTrue(battery >= 0);
                            assertTrue(battery <= 100);
                        }
                    } else {
                        assertThrows(ButtplugDeviceFeatureException.class, () -> {
                            feat.ReadBattery().get();
                        });
                    }
                }
            }

            client.disconnect();
        }
    }
}