package io.github.blackspherefollower.buttplug4j.protocol.messages;

import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"Error\":{\"Id\":7,\"ErrorCode\":4,\"ErrorMessage\":\"TestError\"}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(msgs.size(), 1);
        assertEquals(msgs.get(0).getClass(),
                Error.class);
        assertEquals(msgs.get(0).getId(), 7);
        assertEquals(
                ((Error) msgs.get(0)).getErrorMessage(),
                "TestError");
        assertEquals(
                ((Error) msgs.get(0)).getErrorCode(),
                Error.ErrorClass.ERROR_DEVICE);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
