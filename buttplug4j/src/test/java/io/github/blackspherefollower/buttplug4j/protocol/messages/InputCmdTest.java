package io.github.blackspherefollower.buttplug4j.protocol.messages;

import dev.harrel.jsonschema.Error;
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

public class InputCmdTest {

    static String schema = null;

    @BeforeAll
    public static void setup() throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(TestConstants.SCHEMA_URL).openStream());
        schema = new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));
        in.close();
    }

    @Test
    public void testInputCmdSubscribe() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"InputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Type\":\"Battery\",\"Command\":\"Subscribe\"}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((InputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((InputCmd) msgs.get(0)).getFeatureIndex());
        assertEquals("Battery", ((InputCmd) msgs.get(0)).getInputType());
        assertEquals(InputCommandType.SUBSCRIBE, ((InputCmd) msgs.get(0)).getInputCommand());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testInputCmdUnsubscribe() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"InputCmd\":{\"Id\":2,\"DeviceIndex\":1,\"FeatureIndex\":1,\"Type\":\"RSSI\",\"Command\":\"Unsubscribe\"}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputCmd.class, msgs.get(0).getClass());
        assertEquals(2, msgs.get(0).getId());
        assertEquals(1, ((InputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(1, ((InputCmd) msgs.get(0)).getFeatureIndex());
        assertEquals("RSSI", ((InputCmd) msgs.get(0)).getInputType());
        assertEquals(InputCommandType.UNSUBSCRIBE, ((InputCmd) msgs.get(0)).getInputCommand());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testInputCmdRead() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"InputCmd\":{\"Id\":3,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Type\":\"Button\",\"Command\":\"Read\"}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputCmd.class, msgs.get(0).getClass());
        assertEquals(InputCommandType.READ, ((InputCmd) msgs.get(0)).getInputCommand());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testInputCmdReadInternal() throws IOException, ButtplugProtocolException {
        InputCmd cmd = new InputCmd(1, 0, 0, "Battery", InputCommandType.READ);

        assertEquals(InputCommandType.READ, cmd.getInputCommand());
        assertEquals("Battery", cmd.getInputType());
    }
}
