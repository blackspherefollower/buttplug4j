package io.github.blackspherefollower.buttplug4j.protocol;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

import java.util.Arrays;
import java.util.List;

public final class ButtplugJsonMessageParser {

    private final ObjectMapper mapper;

    public ButtplugJsonMessageParser() {
        mapper = JsonMapper.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
        TypeResolverBuilder<?> typer = DefaultTypeResolverBuilder.construct(DefaultTyping.JAVA_LANG_OBJECT,
                this.mapper.getPolymorphicTypeValidator());
        typer = typer.init(JsonTypeInfo.Id.NAME, null);
        typer = typer.inclusion(As.WRAPPER_OBJECT);
        mapper.setDefaultTyping(typer);
    }

    public List<ButtplugMessage> parseJson(final String json)
            throws ButtplugProtocolException {
        try {
            return Arrays.asList(mapper.readValue(json, ButtplugMessage[].class));
        } catch (JsonProcessingException e) {
            throw new ButtplugProtocolException(e);
        }
    }

    public String formatJson(final List<ButtplugMessage> msgs)
            throws ButtplugProtocolException {
        try {
            return mapper.writeValueAsString(msgs);
        } catch (JsonProcessingException e) {
            throw new ButtplugProtocolException(e);
        }
    }

    public String formatJson(final ButtplugMessage msgs)
            throws ButtplugProtocolException {
        try {
            return mapper.writeValueAsString(new ButtplugMessage[]{msgs});
        } catch (JsonProcessingException e) {
            throw new ButtplugProtocolException(e);
        }
    }
}
