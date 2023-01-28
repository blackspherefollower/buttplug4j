package org.blackspherefollower.buttplug.protocol.messages.Parts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EmptySerializer.class)
public class NullMessageAttributes extends MessageAttributes {

}
