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

        assertEquals(1, msgs.size());
        assertEquals(Error.class,
                msgs.get(0).getClass());
        assertEquals(7, msgs.get(0).getId());
        assertEquals(
                "TestError",
                ((Error) msgs.get(0)).getErrorMessage());
        assertEquals(
                Error.ErrorClass.ERROR_DEVICE,
                ((Error) msgs.get(0)).getErrorCode());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testException() throws IOException, ButtplugProtocolException {
        Error e = new Error(new Exception("TestError"), 7);

        assertEquals(7, e.getId());
        assertEquals(
                "TestError",
                e.getException().getMessage());
        assertEquals(
                Error.ErrorClass.ERROR_UNKNOWN,
                e.getErrorCode());
    }

}
