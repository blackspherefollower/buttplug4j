package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugMessage;

public final class Error extends ButtplugMessage {

    @JsonProperty(value = "ErrorCode", required = true)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private ErrorClass errorCode;
    @JsonProperty(value = "ErrorMessage", required = true)
    private String errorMessage;

    public Error(final String errorMessage, final ErrorClass errorCode, final long id) {
        super(id);
        this.setErrorMessage(errorMessage);
        this.setErrorCode(errorCode);
    }

    @SuppressWarnings("unused")
    private Error() {
        super(ButtplugConsts.DEFAULT_MSG_ID);
        this.setErrorMessage("");
        this.setErrorCode(ErrorClass.ERROR_UNKNOWN);
    }

    public ErrorClass getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final ErrorClass errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public enum ErrorClass {
        ERROR_UNKNOWN,
        ERROR_INIT,
        ERROR_PING,
        ERROR_MSG,
        ERROR_DEVICE,
    }
}
