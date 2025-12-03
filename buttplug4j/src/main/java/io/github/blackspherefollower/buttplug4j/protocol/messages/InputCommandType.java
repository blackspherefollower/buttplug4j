package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InputCommandType {
    READ("Read"),
    SUBSCRIBE("Subscribe"),
    UNSUBSCRIBE("Unsubscribe");

    private final String specName;

    InputCommandType(String specName) {
        this.specName = specName;
    }

    @JsonValue
    public String getSpecName() {
        return specName;
    }
}
