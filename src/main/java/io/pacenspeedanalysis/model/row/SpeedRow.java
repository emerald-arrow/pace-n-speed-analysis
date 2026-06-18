package io.pacenspeedanalysis.model.row;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class SpeedRow extends Row {

    private final BigDecimal topSpeed;

    private SpeedRow(Builder builder) {
        super(
                builder.carNumber,
                builder.lapNumber,
                builder.wentThroughPitLane,
                builder.driver,
                builder.category,
                builder.team,
                builder.manufacturer,
                builder.flagAtFinishLine,
                builder.elapsed,
                builder.id
        );

        this.topSpeed = builder.topSpeed;
    }

    public BigDecimal getTopSpeed() {
        return topSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SpeedRow that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(topSpeed, that.getTopSpeed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), topSpeed);
    }

    public static final class Builder extends RowBuilder<BigDecimal, Builder, SpeedRow> {

        private BigDecimal topSpeed;

        public Builder withTopSpeed(String topSpeed) {
            requireNotNull(topSpeed, "topSpeed");
            this.topSpeed = parseValue(topSpeed);
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
        protected BigDecimal parseValue(String s) {
            BigDecimal val = BigDecimal.ZERO;

            if (!s.isBlank()) {
                try {
                    val = new BigDecimal(s.trim()).setScale(1, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("topSpeed is invalid", e);
                }

                if (val.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("topSpeed must not be negative");
                }
            }

            return val;
        }

        @Override
        public SpeedRow build() {
            return new SpeedRow(this);
        }
    }
}
