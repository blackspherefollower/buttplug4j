package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;

public final class Error extends ButtplugMessage {

    @JsonProperty(value = "ErrorCode", required = true)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private ErrorClass errorCode;
    @JsonProperty(value = "ErrorMessage", required = true)
    private String errorMessage;
    @JsonIgnore
    private Throwable exception = null;

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

    public Error(Throwable e) {
        super(ButtplugConsts.SYSTEM_MSG_ID);
        this.setErrorMessage(e.getMessage());
        this.setErrorCode(ErrorClass.ERROR_UNKNOWN);
        this.exception = e;
    }
    public Error(Throwable e, final long id) {
        super(id);
        this.setErrorMessage(e.getMessage());
        this.setErrorCode(ErrorClass.ERROR_UNKNOWN);
        this.exception = e;
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

    public Throwable getException() {
        return exception;
    }

    public enum ErrorClass {
        ERROR_UNKNOWN,
        ERROR_INIT,
        ERROR_PING,
        ERROR_MSG,
        ERROR_DEVICE,
    }
}
