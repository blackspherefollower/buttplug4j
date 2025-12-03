package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

import java.util.HashMap;

public final class DeviceList extends ButtplugMessage {

    @JsonProperty(value = "Devices", required = true)
    private HashMap<Integer, Device> devices;

    public DeviceList(final HashMap<Integer, Device> devices, final int id) {
        super(id);
        this.devices = devices;
    }

    @SuppressWarnings("unused")
    private DeviceList() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
        this.setDevices(new HashMap<>());
    }

    public HashMap<Integer, Device> getDevices() {
        return devices;
    }

    public void setDevices(final HashMap<Integer, Device> devices) {
        this.devices = devices;
    }
}
