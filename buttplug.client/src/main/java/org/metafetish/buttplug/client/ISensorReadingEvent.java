package org.metafetish.buttplug.client;

import org.metafetish.buttplug.core.Messages.SensorReading;

public interface ISensorReadingEvent {
    void sensorReadingReceived(SensorReading msg);
}
