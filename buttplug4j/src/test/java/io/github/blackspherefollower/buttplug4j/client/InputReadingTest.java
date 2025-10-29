package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InputReadingTest {

    @Test
    public void testInputReadingCreation() {
        InputReading reading = new InputReading(1, 0, 0);
        
        assertEquals(1, reading.getId());
        assertEquals(0, reading.getDeviceIndex());
        assertNotNull(reading);
    }

    @Test
    public void testInputReadingInheritance() {
        InputReading reading = new InputReading(1, 0, 0);

        assertInstanceOf(ButtplugDeviceMessage.class, reading);
    }

    @Test
    public void testInputDataInterfaces() {
        // Test that InputData interface classes exist
        InputReading.BatteryData batteryData = new InputReading.BatteryData();
        InputReading.RssiData rssiData = new InputReading.RssiData();
        InputReading.ButtonData buttonData = new InputReading.ButtonData();
        InputReading.PresureData presureData = new InputReading.PresureData();

        assertInstanceOf(InputReading.InputData.class, batteryData);
        assertInstanceOf(InputReading.InputData.class, rssiData);
        assertInstanceOf(InputReading.InputData.class, buttonData);
        assertInstanceOf(InputReading.InputData.class, presureData);
    }
}
