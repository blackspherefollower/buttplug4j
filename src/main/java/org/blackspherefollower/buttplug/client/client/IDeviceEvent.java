package org.blackspherefollower.buttplug.client.client;

public interface IDeviceEvent {
    void deviceAdded(ButtplugClientDevice dev);

    void deviceRemoved(long index);
}
