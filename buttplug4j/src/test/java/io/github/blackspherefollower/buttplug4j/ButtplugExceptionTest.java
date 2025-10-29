
package io.github.blackspherefollower.buttplug4j;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ButtplugExceptionTest {

    @Test
    public void testExceptionWithMessage() {
        String errorMessage = "Test error message";
        ButtplugException exception = new ButtplugException();
        exception.setMessage(errorMessage);
        
        assertEquals(errorMessage, exception.getMessage());
    }
}
