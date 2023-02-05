package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

import static org.blackspherefollower.buttplug.protocol.ButtplugConsts.MESSAGE_VERSION;

public final class RequestServerInfo extends ButtplugMessage {
    @JsonProperty(value = "MessageVersion", required = true)
    private final long messageVersion = MESSAGE_VERSION;

    @JsonProperty(value = "ClientName", required = true)
    private String clientName;

    public RequestServerInfo(final String clientName, final long id) {
        super(id);
        this.setClientName(clientName);
    }

    @SuppressWarnings("unused")
    private RequestServerInfo() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
        this.setClientName("");
    }

    public long getMessageVersion() {
        return messageVersion;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }
}