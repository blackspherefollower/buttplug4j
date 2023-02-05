package org.blackspherefollower.buttplug.protocol.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.blackspherefollower.buttplug.protocol.ButtplugConsts;
import org.blackspherefollower.buttplug.protocol.ButtplugDeviceMessage;

import java.util.ArrayList;

public final class ScalarCmd extends ButtplugDeviceMessage {

    @JsonProperty(value = "Scalars", required = true)
    private ScalarSubCmd[] scalars;

    public ScalarCmd(final long deviceIndex, final Double[] scalars, final String actuatorType, final long id) {
        super(id, deviceIndex);
        long i = 0;
        ArrayList<ScalarSubCmd> scalarsubs = new ArrayList<>();
        for (Double scalar : scalars) {
            if (scalar != null) {
                scalarsubs.add(new ScalarSubCmd(i, scalar, actuatorType));
            }
            i++;
        }
        this.setScalars(scalarsubs.toArray(new ScalarSubCmd[]{}));
    }

    @SuppressWarnings("unused")
    private ScalarCmd() {
        super(ButtplugConsts.DEFAULT_MSG_ID, -1);
    }

    public ScalarSubCmd[] getScalars() {
        return scalars;
    }

    public void setScalars(final ScalarSubCmd[] scalars) {
        this.scalars = scalars;
    }

    public static final class ScalarSubCmd {
        @JsonProperty(value = "Index", required = true)
        private long index;

        @JsonProperty(value = "Scalar", required = true)
        private double scalar;

        @JsonProperty(value = "ActuatorType", required = true)
        private String actuatorType;

        public ScalarSubCmd(final long index, final double scalar, final String actuatorType) {
            this.setIndex(index);
            this.setActuatorType(actuatorType);
            setScalar(scalar);
        }

        public double getScalar() {
            if (scalar > 1 || scalar < 0) {
                return 0;
            }
            return scalar;
        }

        public void setScalar(final double scalar) {
            if (scalar > 1) {
                throw new IllegalArgumentException(
                        "Scalar values cannot be greater than 1!");
            }

            if (scalar < 0) {
                throw new IllegalArgumentException(
                        "Scalar values cannot be lower than 0!");
            }

            this.scalar = scalar;
        }

        public long getIndex() {
            return index;
        }

        public void setIndex(final long index) {
            this.index = index;
        }

        public String getActuatorType() {
            return actuatorType;
        }

        public void setActuatorType(final String actuatorType) {
            this.actuatorType = actuatorType;
        }
    }
}
