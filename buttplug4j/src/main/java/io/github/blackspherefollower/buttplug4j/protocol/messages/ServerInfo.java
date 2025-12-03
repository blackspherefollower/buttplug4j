package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class ServerInfo extends ButtplugMessage {

    @JsonProperty(value = "ProtocolVersionMajor", required = true)
    private int protocolVersionMajor;

    @JsonProperty(value = "ProtocolVersionMinor", required = true)
    private int protocolVersionMinor;

    @JsonProperty(value = "MaxPingTime", required = true)
    private long maxPingTime;

    @JsonProperty(value = "ServerName", required = true)
    private String serverName;

    public ServerInfo(final String serverName, final int protocolVersionMajor, final int protocolVersionMinor, final long maxPingTime, final int id) {
        super(id);

        this.serverName = serverName;
        this.protocolVersionMajor = protocolVersionMajor;
        this.protocolVersionMinor = protocolVersionMinor;
        this.maxPingTime = maxPingTime;
    }

    @SuppressWarnings("unused")
    private ServerInfo() {
        super(ButtplugConsts.DEFAULT_MSG_ID);

        this.serverName = "";
        this.protocolVersionMajor = 4;
        this.protocolVersionMinor = 0;
        this.maxPingTime = 0;
    }

    public int getProtocolVersionMajor() {
        return protocolVersionMajor;
    }

    public void setProtocolVersionMajor(final int protocolVersionMajor) {
        this.protocolVersionMajor = protocolVersionMajor;
    }

    public int getProtocolVersionMinor() {
        return protocolVersionMinor;
    }

    public void setProtocolVersionMinor(final int protocolVersionMinor) {
        this.protocolVersionMinor = protocolVersionMinor;
    }

    public long getMaxPingTime() {
        return maxPingTime;
    }

    public void setMaxPingTime(final long maxPingTime) {
        this.maxPingTime = maxPingTime;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(final String serverName) {
        this.serverName = serverName;
    }
}
