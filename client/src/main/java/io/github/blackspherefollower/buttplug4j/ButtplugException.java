package io.github.blackspherefollower.buttplug4j;

public class ButtplugException extends Exception {

    private String errorMessage = "";

    protected final void setMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public final String getMessage() {
        return errorMessage;
    }
}
