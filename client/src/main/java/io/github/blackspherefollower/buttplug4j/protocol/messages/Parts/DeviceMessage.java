package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

public final class DeviceMessage {
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public MessageAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(final MessageAttributes attributes) {
        this.attributes = attributes;
    }

    private String message;
    private MessageAttributes attributes;
}
