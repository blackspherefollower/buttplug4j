package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

public class LinearCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "Vectors", required = true)
    private LinearSubCmd[] vectors;

    public LinearCmd(long deviceIndex, LinearSubCmd[] vectors, long id) {
        super(id, deviceIndex);
        this.vectors = vectors;
    }

    @SuppressWarnings("unused")
    private LinearCmd() {
        super(ButtplugConsts.DefaultMsgId, -1);
    }

    public static class LinearSubCmd {
        @JsonProperty(value = "Index", required = true)
        private long index;

        @JsonProperty(value = "Position", required = true)
        private double position;

        @JsonProperty(value = "Duration", required = true)
        private long duration;

        public double GetPosition() {
            if (position > 1 || position < 0) {
                return 0;
            }
            return position;
        }

        public void SetPosition(double position) {
            if (position > 1) {
                throw new IllegalArgumentException(
                        "Linear position cannot be greater than 1!");
            }

            if (position < 0) {
                throw new IllegalArgumentException(
                        "Linear position cannot be lower than 0!");
            }

            this.position = position;
        }

        public LinearSubCmd(long index, double position, long duration )
        {
            this.index = index;
            this.duration = duration;
            SetPosition(position);
        }
        public LinearSubCmd()
        {
            this.index = -1;
            this.duration = 0;
            SetPosition(0);
        }
    }
}
