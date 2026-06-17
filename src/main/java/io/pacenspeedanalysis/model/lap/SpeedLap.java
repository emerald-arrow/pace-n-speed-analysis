package io.pacenspeedanalysis.model.lap;

import java.math.BigDecimal;
import java.util.Objects;

public final class SpeedLap extends Lap {

    private final BigDecimal topSpeed;

    private SpeedLap(Builder builder) {
        super(
                builder.lapNumber,
                builder.wentThroughPitLane,
                builder.flagAtFinishLine,
                builder.elapsed
        );

        this.topSpeed = builder.topSpeed;
    }

    public BigDecimal getTopSpeed() {
        return topSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SpeedLap that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(topSpeed, that.getTopSpeed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), topSpeed);
    }

    public static final class Builder extends LapBuilder<Builder, SpeedLap> {

        private BigDecimal topSpeed;

        public Builder withTopSpeed(BigDecimal topSpeed) {
            requireNotNull(topSpeed, "topSpeed");
            this.topSpeed = topSpeed;
            return self();
        }

        @Override
        protected void validate() {
            super.validate();
            if (topSpeed == null) {
                throw new IllegalStateException("All values must be set");
            }
        }

        @Override
        public SpeedLap build() {
            validate();
            return new SpeedLap(this);
        }
    }
}
