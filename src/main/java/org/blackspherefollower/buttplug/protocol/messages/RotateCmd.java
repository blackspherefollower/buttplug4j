package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public class RotateCmd extends ButtplugDeviceMessage {

    public class RotateSubCmd {
        @JsonProperty(value = "Index", required = true)
        private long index;

        @JsonProperty(value = "Speed", required = true)
        private double speed;

        @JsonProperty(value = "Clockwise", required = true)
        private boolean clockwise;

        public double GetPosition() {
            if (speed > 1 || speed < 0) {
                return 0;
            }
            return speed;
        }

        public void SetSpeed(double speed) {
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

    @JsonProperty(value = "Rotations", required = true)
    private RotateSubCmd[] rotations;

    public RotateCmd(long deviceIndex, long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private RotateCmd() {
        super(ButtplugConsts.DefaultMsgId, -1);
    }
}
