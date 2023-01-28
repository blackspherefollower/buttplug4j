package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

import java.util.Map;

public class RotateCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "Rotations", required = true)
    private RotateSubCmd[] rotations;

    public RotateCmd(long deviceIndex, RotateSubCmd[] rotations, long id) {
        super(id, deviceIndex);
        this.rotations = rotations;
    }

    @SuppressWarnings("unused")
    private RotateCmd() {
        super(ButtplugConsts.DefaultMsgId, -1);
    }

    public static class RotateSubCmd {
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

        public RotateSubCmd(long index, double speed, boolean clockwise)
        {
            this.index = index;
            this.clockwise = clockwise;
            SetSpeed(speed);
        }

        public RotateSubCmd()
        {
            this.index = -1;
            this.clockwise = true;
            this.speed = 0;
        }
    }
}
