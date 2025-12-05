package io.github.blackspherefollower.buttplug4j.protocol;

import com.fasterxml.jackson.core.JsonParseException;
import io.github.blackspherefollower.buttplug4j.ButtplugException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ButtplugProtocolExceptionTest {

    @Test
    public void testExceptionWithMessage() {
        String errorMessage = "Invalid protocol message";
        JsonParseException cause = new JsonParseException(errorMessage);
        ButtplugProtocolException exception = new ButtplugProtocolException(cause);

        assertEquals("Buttplug JSON message exception", exception.getMessage());
        assertEquals(errorMessage, exception.getCause().getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        String errorMessage = "Invalid protocol message";
        JsonParseException cause = new JsonParseException(errorMessage);
        ButtplugProtocolException exception = new ButtplugProtocolException(cause);
        assertInstanceOf(ButtplugException.class, exception);
    }
}
