package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;

import static org.blackspherefollower.buttplug.protocol.ButtplugConsts.MessageVersion;

public class RequestServerInfo extends ButtplugMessage {
    @JsonProperty(value = "ClientName", required = true)
    public String clientName;

    @JsonProperty(value = "MessageVersion", required = true)
    public final long messageVersion = MessageVersion;

    public RequestServerInfo(String clientName, long id) {
        super(id);
        this.clientName = clientName;
    }

    @SuppressWarnings("unused")
    private RequestServerInfo() {
        super(ButtplugConsts.DefaultMsgId);
        this.clientName = "";
    }
}