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
        InputReading.BatteryData batteryData = new InputReading.BatteryData();
        InputReading.RssiData rssiData = new InputReading.RssiData();
        InputReading.ButtonData buttonData = new InputReading.ButtonData();
        InputReading.PresureData presureData = new InputReading.PresureData();

        assertInstanceOf(InputReading.BatteryData.class, batteryData);
        assertInstanceOf(InputReading.RssiData.class, rssiData);
        assertInstanceOf(InputReading.ButtonData.class, buttonData);
        assertInstanceOf(InputReading.PresureData.class, presureData);
    }

    @Test
    @Disabled("See https://github.com/buttplugio/buttplug/issues/801")
    public void testBatteryReading() throws ButtplugProtocolException {
        String testStr = "[{\"InputReading\":{\"Id\":1,\"DeviceIndex\":0,\"FeatureIndex\":1,\"Data\":{\"Battery\":{\"Data\":100}}}}]";

        Validator.Result result = new ValidatorFactory().validate(schema, testStr);
        assertTrue(result.isValid(), result.getErrors().stream().map(error -> error.getError() + " - " + error.getInstanceLocation()).collect(Collectors.joining("\n")));

        ButtplugJsonMessageParser parser = new ButtplugJsonMessageParser();
        List<ButtplugMessage> msgs = parser.parseJson(testStr);

        assertEquals(1, msgs.size());
        assertEquals(InputReading.BatteryData.class, msgs.get(0).getClass());
        assertEquals(1, msgs.get(0).getId(), 1);
        assertEquals(4, ((ServerInfo) msgs.get(0)).getProtocolVersionMajor());
        assertEquals(0, ((ServerInfo) msgs.get(0)).getProtocolVersionMinor());
        assertEquals(500, ((ServerInfo) msgs.get(0)).getMaxPingTime());
        assertEquals("Websocket Server", ((ServerInfo) msgs.get(0)).getServerName());

        String jsonOut = parser.formatJson(msgs);
        assertEquals(testStr, jsonOut);

        jsonOut = parser.formatJson(msgs.get(0));
        assertEquals(testStr, jsonOut);
    }
}
