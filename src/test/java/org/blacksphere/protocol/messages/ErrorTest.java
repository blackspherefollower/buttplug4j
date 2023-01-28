package org.blacksphere.protocol.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.blackspherefollower.buttplug.protocol.ButtplugJsonMessageParser;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;
import org.blackspherefollower.buttplug.protocol.messages.Error;

import java.io.IOException;
import java.util.List;

public class ErrorTest {

    @Test
    public void test() throws IOException {
        String testStr = "[{\"Error\":{\"Id\":7,\"ErrorCode\":4,\"ErrorMessage\":\"TestError\"}}]";

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(msgs.size(), 1);
        assertEquals(msgs.get(0).getClass(),
                Error.class);
        assertEquals(msgs.get(0).id, 7);
        assertEquals(
                ((Error) msgs.get(0)).errorMessage,
                "TestError");
        assertEquals(
                ((Error) msgs.get(0)).errorCode,
                Error.ErrorClass.ERROR_DEVICE);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

}
