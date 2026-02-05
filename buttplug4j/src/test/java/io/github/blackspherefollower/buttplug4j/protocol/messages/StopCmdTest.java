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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StopCmdTest {

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
        String testStr = "[{\"StopCmd\":{\"Id\":7}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(msgs.size(), 1);
        assertEquals(msgs.get(0).getClass(), StopCmd.class);
        assertEquals(msgs.get(0).getId(), 7);

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testInputsOutputs() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"StopCmd\":{\"Id\":7,\"Inputs\":true,\"Outputs\":false}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(StopCmd.class, msgs.get(0).getClass());

        StopCmd msg = (StopCmd) msgs.get(0);
        assertEquals(7, msg.getId());
        assertEquals(true, msg.getInputs());
        assertEquals(false, msg.getOutputs());
        assertNull(msg.getDeviceIndex());
        assertNull(msg.getFeatureIndex());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testDeviceIndex() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"StopCmd\":{\"Id\":7,\"DeviceIndex\":4}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(StopCmd.class, msgs.get(0).getClass());

        StopCmd msg = (StopCmd) msgs.get(0);
        assertEquals(7, msg.getId());
        assertNull(msg.getInputs());
        assertNull(msg.getOutputs());
        assertEquals(4, msg.getDeviceIndex());
        assertNull(msg.getFeatureIndex());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testDeviceIndexInputsOutputs() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"StopCmd\":{\"Id\":7,\"Inputs\":true,\"Outputs\":false,\"DeviceIndex\":4}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(StopCmd.class, msgs.get(0).getClass());

        StopCmd msg = (StopCmd) msgs.get(0);
        assertEquals(7, msg.getId());
        assertEquals(true, msg.getInputs());
        assertEquals(false, msg.getOutputs());
        assertEquals(4, msg.getDeviceIndex());
        assertNull(msg.getFeatureIndex());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testDeviceIndexFeatureIndex() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"StopCmd\":{\"Id\":7,\"DeviceIndex\":4,\"FeatureIndex\":2}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(StopCmd.class, msgs.get(0).getClass());

        StopCmd msg = (StopCmd) msgs.get(0);
        assertEquals(7, msg.getId());
        assertNull(msg.getInputs());
        assertNull(msg.getOutputs());
        assertEquals(4, msg.getDeviceIndex());
        assertEquals(2, msg.getFeatureIndex());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testDeviceIndexFeatureIndexInputsOutputs() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"StopCmd\":{\"Id\":7,\"Inputs\":false,\"Outputs\":true,\"DeviceIndex\":4,\"FeatureIndex\":2}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(StopCmd.class, msgs.get(0).getClass());

        StopCmd msg = (StopCmd) msgs.get(0);
        assertEquals(7, msg.getId());
        assertEquals(false, msg.getInputs());
        assertEquals(true, msg.getOutputs());
        assertEquals(4, msg.getDeviceIndex());
        assertEquals(2, msg.getFeatureIndex());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testSettersAndGetters() {
        StopCmd msg = new StopCmd(1);
        msg.setInputs(true);
        msg.setOutputs(false);
        msg.setDeviceIndex(5);
        msg.setFeatureIndex(3);

        assertEquals(1, msg.getId());
        assertEquals(true, msg.getInputs());
        assertEquals(false, msg.getOutputs());
        assertEquals(5, msg.getDeviceIndex());
        assertEquals(3, msg.getFeatureIndex());
    }

    @Test
    public void testConstructors() {
        StopCmd msg1 = new StopCmd(2, true, false);
        assertEquals(2, msg1.getId());
        assertEquals(true, msg1.getInputs());
        assertEquals(false, msg1.getOutputs());

        StopCmd msg2 = new StopCmd(3, 10);
        assertEquals(3, msg2.getId());
        assertEquals(10, msg2.getDeviceIndex());

        StopCmd msg3 = new StopCmd(4, 11, false, true);
        assertEquals(4, msg3.getId());
        assertEquals(11, msg3.getDeviceIndex());
        assertEquals(false, msg3.getInputs());
        assertEquals(true, msg3.getOutputs());

        StopCmd msg4 = new StopCmd(5, 12, 6);
        assertEquals(5, msg4.getId());
        assertEquals(12, msg4.getDeviceIndex());
        assertEquals(6, msg4.getFeatureIndex());

        StopCmd msg5 = new StopCmd(6, 13, 7, true, true);
        assertEquals(6, msg5.getId());
        assertEquals(13, msg5.getDeviceIndex());
        assertEquals(7, msg5.getFeatureIndex());
        assertEquals(true, msg5.getInputs());
        assertEquals(true, msg5.getOutputs());
    }
}
