package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.ButtplugException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ButtplugClientExceptionTest {

    @Test
    public void testExceptionWithMessage() {
        String errorMessage = "Client error occurred";
        ButtplugClientException exception = new ButtplugClientException(errorMessage);
        
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        ButtplugClientException exception = new ButtplugClientException("test");
        assertInstanceOf(ButtplugException.class, exception);
    }
}
