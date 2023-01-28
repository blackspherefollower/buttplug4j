package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public class ServerInfo extends ButtplugMessage {

    @JsonProperty(value = "MessageVersion", required = true)
    public int messageVersion;

    @JsonProperty(value = "MaxPingTime", required = true)
    public long maxPingTime;

    @JsonProperty(value = "ServerName", required = true)
    public String serverName;

    public ServerInfo(String serverName, int messageVersion, long maxPingTime, long id) {
        super(id);

        this.serverName = serverName;
        this.messageVersion = messageVersion;
        this.maxPingTime = maxPingTime;
    }

    @SuppressWarnings("unused")
    private ServerInfo() {
        super(ButtplugConsts.DefaultMsgId);

        this.serverName = "";
        this.messageVersion = 1;
        this.maxPingTime = 0;
    }
}