package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

public final class DeviceMessage {
    private String message;
    private MessageAttributes attributes;

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
}
