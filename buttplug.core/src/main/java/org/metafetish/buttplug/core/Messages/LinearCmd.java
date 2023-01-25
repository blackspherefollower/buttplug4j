package org.metafetish.buttplug.core.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metafetish.buttplug.core.ButtplugConsts;
import org.metafetish.buttplug.core.ButtplugDeviceMessage;

public class LinearCmd extends ButtplugDeviceMessage {

    public class LinearSubCmd {
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
    }

    @JsonProperty(value = "Vectors", required = true)
    private LinearSubCmd[] vectors;

    public LinearCmd(long deviceIndex, long id) {
        super(id, deviceIndex);
    }

    @SuppressWarnings("unused")
    private LinearCmd() {
        super(ButtplugConsts.DefaultMsgId, -1);
    }
}
