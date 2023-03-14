package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RawMessageAttributes extends MessageAttributes {

    @JsonProperty(value = "Endpoints", required = true)
    private String[] endpoints;

    public String[] getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(final String[] endpoints) {
        this.endpoints = endpoints;
    }
}

