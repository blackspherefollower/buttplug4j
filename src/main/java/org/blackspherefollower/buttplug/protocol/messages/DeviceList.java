package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.Parts.DeviceMessageInfo;

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