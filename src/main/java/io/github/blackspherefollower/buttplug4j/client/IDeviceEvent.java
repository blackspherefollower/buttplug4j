package io.github.blackspherefollower.buttplug4j.client;

public interface IDeviceEvent {
    void deviceAdded(ButtplugClientDevice dev);

    void deviceRemoved(long index);
}
