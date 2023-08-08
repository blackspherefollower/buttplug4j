package io.github.blackspherefollower.buttplug4j.protocol.messages.Parts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EmptySerializer.class)
public final class NullMessageAttributes extends MessageAttributes {

}
