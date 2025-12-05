package io.github.blackspherefollower.buttplug4j.protocol;

import io.github.blackspherefollower.buttplug4j.protocol.messages.Ok;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Ping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ButtplugMessageTest {

    @Test
    public void testMessageId() {
        ButtplugMessage msg = new Ok(5);
        assertEquals(5, msg.getId());
    }

    @Test
    public void testMessageIdSetter() {
        ButtplugMessage msg = new Ok(1);
        msg.setId(10);
        assertEquals(10, msg.getId());
    }

    @Test
    public void testSystemMessageId() {
        ButtplugMessage msg = new Ok(ButtplugConsts.SYSTEM_MSG_ID);
        assertEquals(0, msg.getId());
    }

    @Test
    public void testDifferentMessageTypes() {
        ButtplugMessage okMsg = new Ok(1);
        ButtplugMessage pingMsg = new Ping(2);

        assertNotEquals(okMsg.getClass(), pingMsg.getClass());
        assertNotEquals(okMsg.getId(), pingMsg.getId());
    }
}
