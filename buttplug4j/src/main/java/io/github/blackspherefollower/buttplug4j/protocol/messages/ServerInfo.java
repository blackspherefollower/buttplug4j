package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class ServerInfo extends ButtplugMessage {

    @JsonProperty(value = "ProtocolMajorVersion", required = true)
    private int protocolMajorVersion;

    @JsonProperty(value = "ProtocolMinorVersion", required = true)
    private int protocolMinorVersion;

    @JsonProperty(value = "MaxPingTime", required = true)
    private long maxPingTime;

    @JsonProperty(value = "ServerName", required = true)
    private String serverName;

    public ServerInfo(final String serverName, final int protocolMajorVersion, final int protocolMinorVersion, final long maxPingTime, final int id) {
        super(id);

        this.serverName = serverName;
        this.protocolMajorVersion = protocolMajorVersion;
        this.protocolMinorVersion = protocolMinorVersion;
        this.maxPingTime = maxPingTime;
    }

    @SuppressWarnings("unused")
    private ServerInfo() {
        super(ButtplugConsts.DEFAULT_MSG_ID);

        this.serverName = "";
        this.protocolMajorVersion = 4;
        this.protocolMinorVersion = 0;
        this.maxPingTime = 0;
    }

    public int getProtocolMajorVersion() {
        return protocolMajorVersion;
    }

    public void setProtocolMajorVersion(final int protocolMajorVersion) {
        this.protocolMajorVersion = protocolMajorVersion;
    }

    public int getProtocolMinorVersion() {
        return protocolMinorVersion;
    }

    public void setProtocolMinorVersion(final int protocolMinorVersion) {
        this.protocolMinorVersion = protocolMinorVersion;
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
