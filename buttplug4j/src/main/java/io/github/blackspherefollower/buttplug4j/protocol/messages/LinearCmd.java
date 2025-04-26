package io.github.blackspherefollower.buttplug4j.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugDeviceMessage;

public final class LinearCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "Vectors", required = true)
    private LinearSubCmd[] vectors;

    public LinearCmd(final long deviceIndex, final LinearSubCmd[] vectors, final long id) {
        super(id, deviceIndex);
        this.setVectors(vectors);
    }

    @SuppressWarnings("unused")
    private LinearCmd() {
        super(ButtplugConsts.DEFAULT_MSG_ID, -1);
    }

    public LinearSubCmd[] getVectors() {
        return vectors;
    }

    public void setVectors(final LinearSubCmd[] vectors) {
        this.vectors = vectors;
    }

    public static final class LinearSubCmd {
        @JsonProperty(value = "Index", required = true)
        private long index;

        @JsonProperty(value = "Position", required = true)
        private double position;

        @JsonProperty(value = "Duration", required = true)
        private long duration;

        public LinearSubCmd(final long index, final double position, final long duration) {
            this.index = index;
            this.duration = duration;
            setPosition(position);
        }

        public LinearSubCmd() {
            this.index = -1;
            this.duration = 0;
            setPosition(0);
        }

        public double getPosition() {
            if (position > 1 || position < 0) {
                return 0;
            }
            return position;
        }

        public void setPosition(final double position) {
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

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public long getIndex() {
            return index;
        }

        public void setIndex(long index) {
            this.index = index;
        }
    }
}
