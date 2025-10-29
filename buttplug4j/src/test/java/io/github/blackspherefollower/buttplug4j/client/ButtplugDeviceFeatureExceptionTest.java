package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.ButtplugException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ButtplugDeviceFeatureExceptionTest {

    @Test
    public void testExceptionWithMessage() {
        ButtplugDeviceFeatureException exception = new ButtplugDeviceFeatureException("slap");
        
        assertEquals("Buttplug Device Feature does not support slap", exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        ButtplugDeviceFeatureException exception = new ButtplugDeviceFeatureException("test");
        assertInstanceOf(ButtplugException.class, exception);
    }
}
