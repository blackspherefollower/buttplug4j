package io.github.blackspherefollower.buttplug4j.client;

import io.github.blackspherefollower.buttplug4j.protocol.messages.Error;

public interface IErrorEvent {
    void errorReceived(Error err);
}
