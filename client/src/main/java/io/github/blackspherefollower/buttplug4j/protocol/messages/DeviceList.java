package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Parts.DeviceMessageInfo;

import java.util.ArrayList;

public final class DeviceList extends ButtplugMessage {

    @JsonProperty(value = "Devices", required = true)
    private ArrayList<DeviceMessageInfo> devices;

    public DeviceList(final ArrayList<DeviceMessageInfo> devices, final long id) {
        super(id);
        this.setDevices(devices);
    }

    @SuppressWarnings("unused")
    private DeviceList() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
        this.setDevices(new ArrayList<>());
    }

    public ArrayList<DeviceMessageInfo> getDevices() {
        return devices;
    }

    public void setDevices(final ArrayList<DeviceMessageInfo> devices) {
        this.devices = devices;
    }
}
