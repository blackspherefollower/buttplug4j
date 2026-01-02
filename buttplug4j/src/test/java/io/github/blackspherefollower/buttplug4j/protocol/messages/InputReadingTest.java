package io.github.blackspherefollower.buttplug4j.protocol.messages;

import dev.harrel.jsonschema.Validator;
import dev.harrel.jsonschema.ValidatorFactory;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugJsonMessageParser;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class InputReadingTest {

    static String schema = null;

    @BeforeAll
    public static void setup() throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(TestConstants.SCHEMA_URL).openStream());
        schema = new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));
        in.close();
    }

    @Test
    public void testInputReadingCreation() {
        InputReading reading = new InputReading(1, 0, 0);

        assertEquals(1, reading.getId());
        assertEquals(0, reading.getDeviceIndex());
        assertNotNull(reading);
    }

    @Test
    public void testInputReadingInheritance() {
        InputReading reading = new InputReading(1, 0, 0);

        assertInstanceOf(ButtplugDeviceMessage.class, reading);
    }

    @Test
    public void testInputDataInterfaces() {
        // Test that InputData interface classes exist
        InputReading.Battery batteryData = new InputReading.Battery();
        InputReading.Rssi rssiData = new InputReading.Rssi();
        InputReading.Button buttonData = new InputReading.Button();
        InputReading.Presure presureData = new InputReading.Presure();
        InputReading.Position positionData = new InputReading.Position();

        assertInstanceOf(InputReading.Battery.class, batteryData);
        assertInstanceOf(InputReading.Rssi.class, rssiData);
        assertInstanceOf(InputReading.Button.class, buttonData);
        assertInstanceOf(InputReading.Presure.class, presureData);
        assertInstanceOf(InputReading.Position.class, positionData);
    }

    @Test
    public void testBatteryReading() throws ButtplugProtocolException {
        String testStr = "[{\"InputReading\":{\"Id\":10,\"DeviceIndex\":0,\"FeatureIndex\":1,\"Reading\":{\"Battery\":{\"Value\":100}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(error -> error.getError() + " - " + error.getInstanceLocation()).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputReading.class, msgs.get(0).getClass());
        assertEquals(10, msgs.get(0).getId());
        assertEquals(0, ((InputReading) msgs.get(0)).getDeviceIndex());
        assertEquals(1, ((InputReading) msgs.get(0)).getFeatureIndex());
        assertEquals(InputReading.Battery.class, ((InputReading) msgs.get(0)).getData().getClass());
        assertEquals(100, ((InputReading.Battery)((InputReading) msgs.get(0)).getData()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testRSSIReading() throws ButtplugProtocolException {
        String testStr = "[{\"InputReading\":{\"Id\":10,\"DeviceIndex\":0,\"FeatureIndex\":1,\"Reading\":{\"Rssi\":{\"Value\":-10}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(error -> error.getError() + " - " + error.getInstanceLocation()).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputReading.class, msgs.get(0).getClass());
        assertEquals(10, msgs.get(0).getId());
        assertEquals(0, ((InputReading) msgs.get(0)).getDeviceIndex());
        assertEquals(1, ((InputReading) msgs.get(0)).getFeatureIndex());
        assertEquals(InputReading.Rssi.class, ((InputReading) msgs.get(0)).getData().getClass());
        assertEquals(-10, ((InputReading.Rssi)((InputReading) msgs.get(0)).getData()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testPressureReading() throws ButtplugProtocolException {
        String testStr = "[{\"InputReading\":{\"Id\":10,\"DeviceIndex\":0,\"FeatureIndex\":1,\"Reading\":{\"Pressure\":{\"Value\":50}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(error -> error.getError() + " - " + error.getInstanceLocation()).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputReading.class, msgs.get(0).getClass());
        assertEquals(10, msgs.get(0).getId());
        assertEquals(0, ((InputReading) msgs.get(0)).getDeviceIndex());
        assertEquals(1, ((InputReading) msgs.get(0)).getFeatureIndex());
        assertEquals(InputReading.Presure.class, ((InputReading) msgs.get(0)).getData().getClass());
        assertEquals(50, ((InputReading.Presure)((InputReading) msgs.get(0)).getData()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testButtonReading() throws ButtplugProtocolException {
        String testStr = "[{\"InputReading\":{\"Id\":10,\"DeviceIndex\":0,\"FeatureIndex\":1,\"Reading\":{\"Button\":{\"Value\":1}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(error -> error.getError() + " - " + error.getInstanceLocation()).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputReading.class, msgs.get(0).getClass());
        assertEquals(10, msgs.get(0).getId());
        assertEquals(0, ((InputReading) msgs.get(0)).getDeviceIndex());
        assertEquals(1, ((InputReading) msgs.get(0)).getFeatureIndex());
        assertEquals(InputReading.Button.class, ((InputReading) msgs.get(0)).getData().getClass());
        assertEquals(1, ((InputReading.Button)((InputReading) msgs.get(0)).getData()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }

    @Test
    public void testPositionReading() throws ButtplugProtocolException {
        String testStr = "[{\"InputReading\":{\"Id\":10,\"DeviceIndex\":0,\"FeatureIndex\":1,\"Reading\":{\"Position\":{\"Value\":25}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(error -> error.getError() + " - " + error.getInstanceLocation()).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputReading.class, msgs.get(0).getClass());
        assertEquals(10, msgs.get(0).getId());
        assertEquals(0, ((InputReading) msgs.get(0)).getDeviceIndex());
        assertEquals(1, ((InputReading) msgs.get(0)).getFeatureIndex());
        assertEquals(InputReading.Position.class, ((InputReading) msgs.get(0)).getData().getClass());
        assertEquals(25, ((InputReading.Position)((InputReading) msgs.get(0)).getData()).getValue());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }
}
