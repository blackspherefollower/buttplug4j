package io.github.blackspherefollower.buttplug4j.protocol;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class ButtplugJsonMessageParser {

    private final ObjectMapper mapper;

    public ButtplugJsonMessageParser() {
        mapper = new ObjectMapper();
        TypeResolverBuilder<?> typer = DefaultTypeResolverBuilder.construct(DefaultTyping.JAVA_LANG_OBJECT,
                this.mapper.getPolymorphicTypeValidator());
        typer = typer.init(JsonTypeInfo.Id.NAME, null);
        typer = typer.inclusion(As.WRAPPER_OBJECT);
        mapper.setDefaultTyping(typer);
    }

    public List<ButtplugMessage> parseJson(final String json)
            throws IOException {
        return Arrays.asList(mapper.readValue(json, ButtplugMessage[].class));
    }

    public String formatJson(final List<ButtplugMessage> msgs)
            throws IOException {
        return mapper.writeValueAsString(msgs);
    }

    public String formatJson(final ButtplugMessage msgs)
            throws IOException {
        return mapper.writeValueAsString(new ButtplugMessage[]{msgs});
    }
}
