package org.metafetish.buttplug.client;

import org.metafetish.buttplug.core.ButtplugMessage;
import org.metafetish.buttplug.core.Messages.DeviceAdded;
import org.metafetish.buttplug.core.Messages.Parts.*;
import org.metafetish.buttplug.core.Messages.DeviceRemoved;
import org.metafetish.buttplug.core.Messages.ScalarCmd;
import org.metafetish.buttplug.core.Messages.StopDeviceCmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ButtplugClientDevice {
    public long index;

    public String name;

    public String displayName;

    public Integer messageTimingGap;

    public Map<String, MessageAttributes> deviceMessages;

    public ButtplugWSClient client;

    public ButtplugClientDevice(ButtplugWSClient client, DeviceMessageInfo aDevInfo) {
        this.client = client;
        index = aDevInfo.deviceIndex;
        name = aDevInfo.deviceName;
        displayName = aDevInfo.deviceDisplayName != null && !aDevInfo.deviceDisplayName.isEmpty() ? aDevInfo.deviceDisplayName : aDevInfo.deviceName;
        messageTimingGap = aDevInfo.deviceMessageTimingGap;
        deviceMessages = new HashMap<>();
        for( DeviceMessage deviceMessage : aDevInfo.deviceMessages) {
            deviceMessages.put(deviceMessage.message, deviceMessage.attributes);
        }
    }

    public ButtplugClientDevice(ButtplugWSClient client, DeviceAdded aDevInfo) {
        this.client = client;
        index = aDevInfo.deviceIndex;
        name = aDevInfo.deviceName;
        displayName = aDevInfo.deviceDisplayName != null && !aDevInfo.deviceDisplayName.isEmpty() ? aDevInfo.deviceDisplayName : aDevInfo.deviceName;
        messageTimingGap = aDevInfo.deviceMessageTimingGap;
        deviceMessages = new HashMap<>();
        for( DeviceMessage deviceMessage : aDevInfo.deviceMessages) {
            deviceMessages.put(deviceMessage.message, deviceMessage.attributes);
        }
    }

    public ButtplugClientDevice(ButtplugWSClient client, DeviceRemoved aDevInfo) {
        this.client = client;
        index = aDevInfo.deviceIndex;
        name = "";
        displayName = "";
        messageTimingGap = null;
        deviceMessages = new HashMap<>();
    }

    public Future<ButtplugMessage> SendStopDeviceCmd() throws IOException, ExecutionException, InterruptedException {
        return client.sendDeviceMessage(this, new StopDeviceCmd(index, client.getNextMsgId()));
    }

    public long GetScalarCount(String actuatorType) {
        MessageAttributes attrs = deviceMessages.get("ScalarCmd");
        if(attrs == null || !(attrs instanceof GenericMessageAttributes)) {
            return 0;
        }
        GenericMessageAttributes gattrs = (GenericMessageAttributes)attrs;
        return gattrs.features.stream().filter(genericFeatureAttributes -> genericFeatureAttributes.actuatorType.contentEquals(actuatorType)).count();
    }

    public Future<ButtplugMessage> SendScalarCmd(String actuatorType, double scalar) throws IOException, ExecutionException, InterruptedException {

        MessageAttributes attrs = deviceMessages.get("ScalarCmd");
        if(attrs == null || !(attrs instanceof GenericMessageAttributes)) {
            throw new IOException("Device doesn't support ScalarCmd!");
        }

        long count = 0;

        ArrayList<Double> values = new ArrayList<>();
        GenericMessageAttributes gattrs = (GenericMessageAttributes)attrs;
        for(GenericFeatureAttributes attr : gattrs.features) {
            if(attr.actuatorType.contentEquals(actuatorType))
            {
                values.add(scalar);
                count++;
            } else {
                values.add(null);
            }
        }
        if(count == 0) {
            throw new IOException("Device doesn't have any ScalarCmd features that support Rotate!");
        }

        return client.sendDeviceMessage(this, new ScalarCmd(index, values.toArray(new Double[]{}), actuatorType, client.getNextMsgId()));
    }

    public Future<ButtplugMessage> SendScalarVibrateCmd(double scalar) throws IOException, ExecutionException, InterruptedException {
        return SendScalarCmd("Vibrate", scalar);
    }

    public long GetScalarVibrateCount() {
        return GetScalarCount("Vibrate");
    }

    public Future<ButtplugMessage> SendScalarRotateCmd(double scalar) throws IOException, ExecutionException, InterruptedException {
        return SendScalarCmd("Rotate", scalar);
    }

    public long GetScalarRotateCount() {
        return GetScalarCount("Rotate");
    }

    public Future<ButtplugMessage> SendScalarOscillateCmd(double scalar) throws IOException, ExecutionException, InterruptedException {
        return SendScalarCmd("Oscillate", scalar);
    }

    public long GetScalarOscillateCount() {
        return GetScalarCount("Oscillate");
    }
}
