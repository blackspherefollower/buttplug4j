package org.metafetish.buttplug.client;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.metafetish.buttplug.core.Messages.ScalarCmd;
import org.metafetish.buttplug.core.Messages.StopAllDevices;

import java.net.URI;

public class ButtplugWSClientMockTest {

    @Ignore
    @Test
    public void TestConnect() throws Exception {
        ButtplugWSClient client = new ButtplugWSClient("Java Test");
        client.Connect(new URI("ws://localhost:12345/buttplug"));
        client.startScanning();

        Thread.sleep(5000);
        client.requestDeviceList();
        for (ButtplugClientDevice dev : client.getDevices()) {
            if (dev.GetScalarVibrateCount() > 0) {
                dev.SendScalarVibrateCmd(0.5);
            }
        }

        Thread.sleep(1000);
        Assert.assertTrue(client.sendMessageExpectOk(new StopAllDevices(client.getNextMsgId())));

        client.Disconnect();
    }
}