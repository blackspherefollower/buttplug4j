package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.ButtplugException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ButtplugDeviceExceptionTest {

    @Test
    public void testExceptionWithMessage() {
        String errorMessage = "Device not found";
        ButtplugDeviceException exception = new ButtplugDeviceException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        ButtplugDeviceException exception = new ButtplugDeviceException("test");
        assertInstanceOf(ButtplugException.class, exception);
    }
}
