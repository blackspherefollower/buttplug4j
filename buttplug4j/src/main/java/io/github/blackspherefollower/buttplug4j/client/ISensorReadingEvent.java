package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.messages.InputReading;

public interface ISensorReadingEvent {
    void sensorReadingReceived(InputReading msg);
}
