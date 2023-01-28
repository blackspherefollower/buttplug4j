package org.blackspherefollower.buttplug.client;

import org.blackspherefollower.buttplug.client.client.ButtplugClientDevice;
import org.blackspherefollower.buttplug.client.client.ButtplugWSClient;
import org.blackspherefollower.buttplug.protocol.messages.Ok;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ButtplugWSClientMockTest {

    @Disabled
    @Test
    public void TestConnect() throws Exception {
        ButtplugWSClient client = new ButtplugWSClient("Java Test");
        client.Connect(new URI("ws://localhost:12345/buttplug"));
        client.startScanning();

        Thread.sleep(5000);
        client.requestDeviceList();
        for (ButtplugClientDevice dev : client.getDevices()) {
            if (dev.GetScalarVibrateCount() > 0) {
                dev.SendScalarVibrateCmd(0.5).get();
            }
        }

        Thread.sleep(1000);
        assertTrue(client.sendStopAllDevicesCmd().get() instanceof Ok);

        client.Disconnect();
    }
}