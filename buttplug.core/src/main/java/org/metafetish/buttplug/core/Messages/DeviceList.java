package org.metafetish.buttplug.core.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metafetish.buttplug.core.ButtplugConsts;
import org.metafetish.buttplug.core.ButtplugMessage;
import org.metafetish.buttplug.core.Messages.Parts.DeviceMessageInfo;

import java.util.ArrayList;

public class DeviceList extends ButtplugMessage {

    @JsonProperty(value = "Devices", required = true)
    public ArrayList<DeviceMessageInfo> devices;

    public DeviceList(ArrayList<DeviceMessageInfo> devices, long id) {
        super(id);
        this.devices = devices;
    }

    @SuppressWarnings("unused")
    private DeviceList() {
        super(ButtplugConsts.DefaultMsgId);
        this.devices = new ArrayList<>();
    }
}