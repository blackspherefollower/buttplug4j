package org.blackspherefollower.buttplug.client.client;

import org.blackspherefollower.buttplug.protocol.messages.SensorReading;

public interface ISensorReadingEvent {
    void sensorReadingReceived(SensorReading msg);
}
