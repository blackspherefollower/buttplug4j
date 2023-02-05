package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public final class RotateCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "Rotations", required = true)
    private RotateSubCmd[] rotations;

    public RotateCmd(final long deviceIndex, final RotateSubCmd[] rotations, final long id) {
        super(id, deviceIndex);
        this.setRotations(rotations);
    }

    @SuppressWarnings("unused")
    private RotateCmd() {
        super(ButtplugConsts.DEFAULT_MSG_ID, -1);
    }

    public RotateSubCmd[] getRotations() {
        return rotations;
    }

    public void setRotations(final RotateSubCmd[] rotations) {
        this.rotations = rotations;
    }

    public static final class RotateSubCmd {
        @JsonProperty(value = "Index", required = true)
        private long index;

        @JsonProperty(value = "Speed", required = true)
        private double speed;

        @JsonProperty(value = "Clockwise", required = true)
        private boolean clockwise;

        public RotateSubCmd(final long index, final double speed, final boolean clockwise) {
            this.index = index;
            this.clockwise = clockwise;
            setSpeed(speed);
        }

        public RotateSubCmd() {
            this.index = -1;
            this.clockwise = true;
            this.speed = 0;
        }

        public double getPosition() {
            if (speed > 1 || speed < 0) {
                return 0;
            }
            return speed;
        }

        public void setSpeed(final double speed) {
            if (speed > 1) {
                throw new IllegalArgumentException(
                        "Rotation speed cannot be greater than 1!");
            }

            if (speed < 0) {
                throw new IllegalArgumentException(
                        "Rotation speed cannot be lower than 0!");
            }

            this.speed = speed;
        }
    }
}
