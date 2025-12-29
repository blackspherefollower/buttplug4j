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

import static org.junit.jupiter.api.Assertions.*;

class OutputCmdTest {

    static String schema = null;

    @BeforeAll
    public static void setup() throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(TestConstants.SCHEMA_URL).openStream());
        schema = new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));
        in.close();
    }

    @Test
    public void testVibrate() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Vibrate\":{\"Value\":5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Vibrate.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.Vibrate) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testRotate() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Rotate\":{\"Value\":5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Rotate.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.Rotate) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testRotateBackwards() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Rotate\":{\"Value\":-5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Rotate.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(-5, ((OutputCmd.Rotate) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testPosition() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Position\":{\"Value\":5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Position.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.Position) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testSpray() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Spray\":{\"Value\":5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Spray.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.Spray) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testConstrict() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Constrict\":{\"Value\":5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Constrict.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.Constrict) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testOscillate() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Oscillate\":{\"Value\":5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Oscillate.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.Oscillate) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testTemperature() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Temperature\":{\"Value\":5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Temperature.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.Temperature) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testLed() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"Led\":{\"Value\":5}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.Led.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.Led) ((OutputCmd) msgs.get(0)).getCommand()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testPositionWithDuration() throws IOException, ButtplugProtocolException {
        String testStr = "[{\"OutputCmd\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":0,\"Command\":{\"PositionWithDuration\":{\"Value\":5,\"Duration\":10}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(Error::getError).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(OutputCmd.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getDeviceIndex());
        assertEquals(0, ((OutputCmd) msgs.get(0)).getFeatureIndex());
        assertInstanceOf(OutputCmd.PositionWithDuration.class, ((OutputCmd) msgs.get(0)).getCommand());
        assertEquals(5, ((OutputCmd.PositionWithDuration) ((OutputCmd) msgs.get(0)).getCommand()).getValue());
        assertEquals(10, ((OutputCmd.PositionWithDuration) ((OutputCmd) msgs.get(0)).getCommand()).getDuration());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }
}