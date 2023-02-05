package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public final class ServerInfo extends ButtplugMessage {

    @JsonProperty(value = "MessageVersion", required = true)
    private int messageVersion;

    @JsonProperty(value = "MaxPingTime", required = true)
    private long maxPingTime;

    @JsonProperty(value = "ServerName", required = true)
    private String serverName;

    public ServerInfo(final String serverName, final int messageVersion, final long maxPingTime, final long id) {
        super(id);

        this.setServerName(serverName);
        this.setMessageVersion(messageVersion);
        this.setMaxPingTime(maxPingTime);
    }

    @SuppressWarnings("unused")
    private ServerInfo() {
        super(ButtplugConsts.DEFAULT_MSG_ID);

        this.setServerName("");
        this.setMessageVersion(1);
        this.setMaxPingTime(0);
    }

    public int getMessageVersion() {
        return messageVersion;
    }

    public void setMessageVersion(final int messageVersion) {
        this.messageVersion = messageVersion;
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