package org.blackspherefollower.buttplug.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.blackspherefollower.buttplug.protocol.messages.DeviceAdded;
import org.blackspherefollower.buttplug.protocol.messages.DeviceList;
import org.blackspherefollower.buttplug.protocol.messages.DeviceRemoved;
import org.blackspherefollower.buttplug.protocol.messages.Error;
import org.blackspherefollower.buttplug.protocol.messages.LinearCmd;
import org.blackspherefollower.buttplug.protocol.messages.Ok;
import org.blackspherefollower.buttplug.protocol.messages.Ping;
import org.blackspherefollower.buttplug.protocol.messages.RequestDeviceList;
import org.blackspherefollower.buttplug.protocol.messages.RequestServerInfo;
import org.blackspherefollower.buttplug.protocol.messages.RotateCmd;
import org.blackspherefollower.buttplug.protocol.messages.ScalarCmd;
import org.blackspherefollower.buttplug.protocol.messages.ScanningFinished;
import org.blackspherefollower.buttplug.protocol.messages.SensorReadCmd;
import org.blackspherefollower.buttplug.protocol.messages.SensorReading;
import org.blackspherefollower.buttplug.protocol.messages.SensorSubscribeCmd;
import org.blackspherefollower.buttplug.protocol.messages.SensorUnsubscribeCmd;
import org.blackspherefollower.buttplug.protocol.messages.ServerInfo;
import org.blackspherefollower.buttplug.protocol.messages.StartScanning;
import org.blackspherefollower.buttplug.protocol.messages.StopAllDevices;
import org.blackspherefollower.buttplug.protocol.messages.StopDeviceCmd;
import org.blackspherefollower.buttplug.protocol.messages.StopScanning;

@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAdded.class, name = "DeviceAdded"),
        @JsonSubTypes.Type(value = DeviceList.class, name = "DeviceList"),
        @JsonSubTypes.Type(value = DeviceRemoved.class, name = "DeviceRemoved"),
        @JsonSubTypes.Type(value = Error.class, name = "Error"),
        @JsonSubTypes.Type(value = LinearCmd.class, name = "LinearCmd"),
        @JsonSubTypes.Type(value = Ok.class, name = "Ok"),
        @JsonSubTypes.Type(value = Ping.class, name = "Ping"),
        @JsonSubTypes.Type(value = RequestDeviceList.class, name = "RequestDeviceList"),
        @JsonSubTypes.Type(value = RequestServerInfo.class, name = "RequestServerInfo"),
        @JsonSubTypes.Type(value = RotateCmd.class, name = "RotateCmd"),
        @JsonSubTypes.Type(value = ScalarCmd.class, name = "ScalarCmd"),
        @JsonSubTypes.Type(value = ScanningFinished.class, name = "ScanningFinished"),
        @JsonSubTypes.Type(value = SensorReadCmd.class, name = "SensorReadCmd"),
        @JsonSubTypes.Type(value = SensorReading.class, name = "SensorReading"),
        @JsonSubTypes.Type(value = SensorSubscribeCmd.class, name = "SensorSubscribeCmd"),
        @JsonSubTypes.Type(value = SensorUnsubscribeCmd.class, name = "SensorUnsubscribeCmd"),
        @JsonSubTypes.Type(value = ServerInfo.class, name = "ServerInfo"),
        @JsonSubTypes.Type(value = StartScanning.class, name = "StartScanning"),
        @JsonSubTypes.Type(value = StopAllDevices.class, name = "StopAllDevices"),
        @JsonSubTypes.Type(value = StopDeviceCmd.class, name = "StopDeviceCmd"),
        @JsonSubTypes.Type(value = StopScanning.class, name = "StopScanning")
})
public abstract class ButtplugMessage {

    @JsonProperty(value = "Id", required = true)
    private long id;

    public ButtplugMessage(final long id) {
        this.setId(id);
    }

    public final long getId() {
        return id;
    }

    public final void setId(final long id) {
        this.id = id;
    }
}
