package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public final class DeviceFeature {

    @JsonProperty(value = "FeatureIndex", required = true)
    private int featureIndex;
    @JsonProperty(value = "FeatureDescription", required = true)
    private String featureDescription;
    @JsonProperty(value = "Output", required = false)
    @JsonDeserialize(using = OutputDescriptorSetDeserialiser.class)
    @JsonSerialize(using = OutputDescriptorSetSerialiser.class, include = JsonSerialize.Inclusion.NON_NULL)
    private ArrayList<OutputDescriptor> output;
    @JsonProperty(value = "Input", required = false)
    @JsonDeserialize(using = InputDescriptorSetDeserialiser.class)
    @JsonSerialize(using = InputDescriptorSetSerialiser.class, include = JsonSerialize.Inclusion.NON_NULL)
    private ArrayList<InputDescriptor> input;

    public DeviceFeature() {
    }

    public String getFeatureDescription() {
        return featureDescription;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public void setFeatureIndex(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    public ArrayList<OutputDescriptor> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<OutputDescriptor> output) {
        this.output = output;
    }

    public ArrayList<InputDescriptor> getInput() {
        return input;
    }

    public void setInput(ArrayList<InputDescriptor> input) {
        this.input = input;
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DeviceFeature.Vibrate.class, name = "Vibrate"),
            @JsonSubTypes.Type(value = DeviceFeature.Rotate.class, name = "Rotate"),
            @JsonSubTypes.Type(value = DeviceFeature.Spray.class, name = "Spray"),
            @JsonSubTypes.Type(value = DeviceFeature.Oscillate.class, name = "Oscillate"),
            @JsonSubTypes.Type(value = DeviceFeature.Position.class, name = "Position"),
            @JsonSubTypes.Type(value = DeviceFeature.Temperature.class, name = "Temperature"),
            @JsonSubTypes.Type(value = DeviceFeature.Constrict.class, name = "Constrict"),
            @JsonSubTypes.Type(value = DeviceFeature.PositionWithDuration.class, name = "PositionWithDuration"),
            @JsonSubTypes.Type(value = DeviceFeature.Led.class, name = "Led")
    })
    public interface OutputDescriptor {
    }

    public static class SteppedOutputDescriptor implements OutputDescriptor {
        @JsonProperty(value = "Value", required = true)
        private int[] value;

        public SteppedOutputDescriptor(int[] value) {
            this.value = value;
        }

        public int[] getValue() {
            return value;
        }

        public void setStepCount(int[] value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SteppedOutputDescriptor that = (SteppedOutputDescriptor) o;
            return java.util.Arrays.equals(value, that.value);
        }
    }

    public static class Vibrate extends SteppedOutputDescriptor {
        public Vibrate(int[] value) {
            super(value);
        }

        public Vibrate() {
            super(new int[]{0, 0});
        }
    }

    public static class Rotate extends SteppedOutputDescriptor {
        public Rotate(int[] value) {
            super(value);
        }

        public Rotate() {
            super(new int[]{0, 0});
        }
    }

    public static class Oscillate extends SteppedOutputDescriptor {
        public Oscillate(int[] value) {
            super(value);
        }

        public Oscillate() {
            super(new int[]{0, 0});
        }
    }

    public static class Constrict extends SteppedOutputDescriptor {
        public Constrict(int[] value) {
            super(value);
        }

        public Constrict() {
            super(new int[]{0, 0});
        }
    }

    public static class Spray extends SteppedOutputDescriptor {
        public Spray(int[] value) {
            super(value);
        }

        public Spray() {
            super(new int[]{0, 0});
        }
    }

    public static class Temperature extends SteppedOutputDescriptor {
        public Temperature(int[] value) {
            super(value);
        }

        public Temperature() {
            super(new int[]{0, 0});
        }
    }

    public static class Led extends SteppedOutputDescriptor {
        public Led(int[] value) {
            super(value);
        }

        public Led() {
            super(new int[]{0, 0});
        }
    }

    public static class Position extends SteppedOutputDescriptor {
        public Position(int[] value) {
            super(value);
        }

        public Position() {
            super(new int[]{0, 0});
        }
    }

    public static class PositionWithDuration extends SteppedOutputDescriptor {
        @JsonProperty(value = "Duration", required = true)
        private int[] duration;

        public PositionWithDuration(int[] value, int[] duration) {
            super(value);
            this.duration = duration;
        }

        public PositionWithDuration() {
            super(new int[]{0, 0});
            this.duration = new int[]{0, 0};
        }

        public int[] getDuration() {
            return duration;
        }

        public void setDuration(int[] duration) {
            this.duration = duration;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PositionWithDuration that = (PositionWithDuration) o;
            return java.util.Arrays.equals(getValue(), that.getValue()) && java.util.Arrays.equals(duration, that.duration);
        }
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DeviceFeature.Battery.class, name = "Battery"),
            @JsonSubTypes.Type(value = DeviceFeature.Rssi.class, name = "RSSI"),
            @JsonSubTypes.Type(value = DeviceFeature.Button.class, name = "Button"),
            @JsonSubTypes.Type(value = DeviceFeature.Pressure.class, name = "Pressure"),
            @JsonSubTypes.Type(value = DeviceFeature.PositionInput.class, name = "Position")
    })
    public static class InputDescriptor {
        @JsonProperty(value = "Command", required = true)
        private ArrayList<InputCommandType> input;

        public InputDescriptor(ArrayList<InputCommandType> input) {
            this.input = input;
        }

        public ArrayList<InputCommandType> getInput() {
            return input;
        }

        public void setInput(ArrayList<InputCommandType> input) {
            this.input = input;
        }
    }

    public static class RangedInputDescriptor extends InputDescriptor {
        @JsonProperty(value = "Value", required = true)
        private int[][] valueRange;

        public RangedInputDescriptor(ArrayList<InputCommandType> input, int[][] valueRange) {
            super(input);
            this.valueRange = valueRange;
        }

        public RangedInputDescriptor() {
            super(new ArrayList<>());
            this.valueRange = new int[][]{{0, 0}, {0, 0}};
        }

        public int[][] getValueRange() {
            return valueRange;
        }

        public void setValueRange(int[][] valueRange) {
            this.valueRange = valueRange;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RangedInputDescriptor that = (RangedInputDescriptor) o;
            if (valueRange.length != that.valueRange.length) {
                return false;
            }
            for (int i = 0; i < valueRange.length; i++) {
                if (!java.util.Arrays.equals(valueRange[i], that.valueRange[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Battery extends RangedInputDescriptor {

        public Battery(ArrayList<InputCommandType> input, int[][] valueRange) {
            super(input, valueRange);
        }

        public Battery() {
            super();
        }
    }

    public static class Rssi extends RangedInputDescriptor {
        public Rssi(ArrayList<InputCommandType> input, int[][] valueRange) {
            super(input, valueRange);
        }

        public Rssi() {
            super();
        }
    }

    public static class Button extends RangedInputDescriptor {
        public Button(ArrayList<InputCommandType> input, int[][] valueRange) {
            super(input, valueRange);
        }

        public Button() {
            super();
        }
    }

    public static class Pressure extends RangedInputDescriptor {
        public Pressure(ArrayList<InputCommandType> input, int[][] valueRange) {
            super(input, valueRange);
        }

        public Pressure() {
            super();
        }
    }

    public static class PositionInput extends RangedInputDescriptor {
        public PositionInput(ArrayList<InputCommandType> input, int[][] valueRange) {
            super(input, valueRange);
        }

        public PositionInput() {
            super();
        }
    }

    static class OutputDescriptorSetDeserialiser extends JsonDeserializer<ArrayList<OutputDescriptor>> {

        @Override
        public ArrayList<OutputDescriptor> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            ObjectMapper mapper = ((ObjectMapper) jsonParser.getCodec());
            ArrayList<OutputDescriptor> ret = new ArrayList<OutputDescriptor>();
            try {
                TreeNode tree = jsonParser.readValueAsTree();
                for (Iterator<String> it = tree.fieldNames(); it.hasNext(); ) {
                    String f = it.next();
                    ObjectNode node = mapper.createObjectNode();
                    node.set(f, mapper.readTree(tree.get(f).traverse(mapper)));
                    ret.add(node.traverse(mapper).readValueAs(OutputDescriptor.class));
                }
            } catch (Exception e) {
                System.out.println("Unknown OutputDescriptor: " + jsonParser.readValueAsTree());
                // unknown type
            }
            return ret;
        }
    }

    static class OutputDescriptorSetSerialiser extends JsonSerializer<ArrayList<OutputDescriptor>> {

        @Override
        public void serialize(ArrayList<OutputDescriptor> outputDescriptors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            ObjectMapper mapper = ((ObjectMapper) jsonGenerator.getCodec());
            ObjectNode node = null;
            for (OutputDescriptor outputDescriptor : outputDescriptors) {
                if (node == null) {
                    node = mapper.createObjectNode();
                }

                TreeNode n = mapper.readValue(mapper.writeValueAsString(outputDescriptor), JsonNode.class).traverse(mapper).readValueAsTree();
                for (Iterator<String> it = n.fieldNames(); it.hasNext(); ) {
                    String f = it.next();
                    ObjectNode on = mapper.createObjectNode();
                    node.set(f, mapper.readTree(n.get(f).traverse(mapper)));
                }
            }
            jsonGenerator.writeObject(node);
        }
    }

    static class InputDescriptorSetDeserialiser extends JsonDeserializer<ArrayList<InputDescriptor>> {

        @Override
        public ArrayList<InputDescriptor> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            ObjectMapper mapper = ((ObjectMapper) jsonParser.getCodec());
            ArrayList<InputDescriptor> ret = new ArrayList<InputDescriptor>();
            try {
                TreeNode tree = jsonParser.readValueAsTree();
                for (Iterator<String> it = tree.fieldNames(); it.hasNext(); ) {
                    String f = it.next();
                    ObjectNode node = mapper.createObjectNode();
                    node.set(f, mapper.readTree(tree.get(f).traverse(mapper)));
                    ret.add(node.traverse(mapper).readValueAs(InputDescriptor.class));
                }
            } catch (Exception e) {
                System.out.println("Unknown InputDescriptor: " + jsonParser.readValueAsTree());
                // unknown type
            }
            return ret;
        }
    }

    static class InputDescriptorSetSerialiser extends JsonSerializer<ArrayList<InputDescriptor>> {

        @Override
        public void serialize(ArrayList<InputDescriptor> inputDescriptors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            ObjectMapper mapper = ((ObjectMapper) jsonGenerator.getCodec());
            ObjectNode node = null;
            for (InputDescriptor inputDescriptor : inputDescriptors) {
                if (node == null) {
                    node = mapper.createObjectNode();
                }

                TreeNode n = mapper.readValue(mapper.writeValueAsString(inputDescriptor), JsonNode.class).traverse(mapper).readValueAsTree();
                for (Iterator<String> it = n.fieldNames(); it.hasNext(); ) {
                    String f = it.next();
                    ObjectNode on = mapper.createObjectNode();
                    node.set(f, mapper.readTree(n.get(f).traverse(mapper)));
                }
            }
            jsonGenerator.writeObject(node);
        }
    }
}
