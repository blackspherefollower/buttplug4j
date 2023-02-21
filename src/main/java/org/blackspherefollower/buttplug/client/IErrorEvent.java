package org.blackspherefollower.buttplug.client;

import org.blackspherefollower.buttplug.protocol.messages.Error;

public interface IErrorEvent {
    void errorReceived(Error err);
}
