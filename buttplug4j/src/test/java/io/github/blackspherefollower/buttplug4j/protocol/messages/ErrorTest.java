package io.github.blackspherefollower.buttplug4j.protocol.messages;

import dev.harrel.jsonschema.Validator;
import dev.harrel.jsonschema.ValidatorFactory;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorTest {

    static String schema = null;

    @BeforeAll
    public static void setup() throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(TestConstants.SCHEMA_URL).openStream());
        schema = new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));
        in.close();
    }

    @Test
    public void test() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"Error\":{\"Id\":7,\"ErrorCode\":4,\"ErrorMessage\":\"TestError\"}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(dev.harrel.jsonschema.Error::getError).collect(Collectors.joining("\n")));

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

    @Test
    public void testExceptionConstructors() {
        Throwable ex = new RuntimeException("Test message");
        Error err1 = new Error(ex);
        assertEquals("Test message", err1.getErrorMessage());
        assertEquals(Error.ErrorClass.ERROR_UNKNOWN, err1.getErrorCode());
        assertEquals(ex, err1.getException());

        Error err2 = new Error(ex, 123);
        assertEquals("Test message", err2.getErrorMessage());
        assertEquals(Error.ErrorClass.ERROR_UNKNOWN, err2.getErrorCode());
        assertEquals(ex, err2.getException());
        assertEquals(123, err2.getId());
    }

    @Test
    public void testSettersAndGetters() {
        Error err = new Error("msg", Error.ErrorClass.ERROR_INIT, 1);
        err.setErrorMessage("new msg");
        err.setErrorCode(Error.ErrorClass.ERROR_PING);
        err.setId(456);

        assertEquals("new msg", err.getErrorMessage());
        assertEquals(Error.ErrorClass.ERROR_PING, err.getErrorCode());
        assertEquals(456, err.getId());
        assertNull(err.getException());
    }
}
