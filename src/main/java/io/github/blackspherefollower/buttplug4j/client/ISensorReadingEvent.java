package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.messages.SensorReading;

public interface ISensorReadingEvent {
    void sensorReadingReceived(SensorReading msg);
}
