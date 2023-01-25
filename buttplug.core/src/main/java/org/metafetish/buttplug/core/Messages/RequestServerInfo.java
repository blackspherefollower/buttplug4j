package org.metafetish.buttplug.core.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metafetish.buttplug.core.ButtplugConsts;
import org.metafetish.buttplug.core.ButtplugMessage;

import static org.metafetish.buttplug.core.ButtplugConsts.MessageVersion;

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