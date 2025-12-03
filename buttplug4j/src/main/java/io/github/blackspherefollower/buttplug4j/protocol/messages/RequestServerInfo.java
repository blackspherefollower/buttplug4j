package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

import static io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts.PROTOCOL_VERSION_MAJOR;
import static io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts.PROTOCOL_VERSION_MINOR;

public final class RequestServerInfo extends ButtplugMessage {
    @JsonProperty(value = "ProtocolVersionMajor", required = true)
    private final long protocolVersionMajor = PROTOCOL_VERSION_MAJOR;

    @JsonProperty(value = "ProtocolVersionMinor", required = true)
    private final long protocolVersionMinor = PROTOCOL_VERSION_MINOR;

    @JsonProperty(value = "ClientName", required = true)
    private String clientName;

    public RequestServerInfo(final String clientName, final int id) {
        super(id);
        this.setClientName(clientName);
    }

    @SuppressWarnings("unused")
    private RequestServerInfo() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
        this.setClientName("");
    }

    public long getProtocolVersionMajor() {
        return protocolVersionMajor;
    }

    public long getProtocolVersionMinor() {
        return protocolVersionMinor;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }
}
