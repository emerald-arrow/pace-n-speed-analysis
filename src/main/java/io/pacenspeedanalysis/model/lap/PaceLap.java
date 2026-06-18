package io.pacenspeedanalysis.model.lap;

import java.time.Duration;
import java.util.Objects;

public final class PaceLap extends Lap {

    private final Duration lapTime;
    private final Duration sector1;
    private final Duration sector2;
    private final Duration sector3;

    private PaceLap(Builder builder) {
        super(
                builder.lapNumber,
                builder.wentThroughPitLane,
                builder.flagAtFinishLine,
                builder.elapsed,
                builder.id
        );

        this.lapTime = builder.lapTime;
        this.sector1 = builder.sector1;
        this.sector2 = builder.sector2;
        this.sector3 = builder.sector3;
    }

    public Duration getLapTime() {
        return lapTime;
    }

    public Duration getSector1() {
        return sector1;
    }

    public Duration getSector2() {
        return sector2;
    }

    public Duration getSector3() {
        return sector3;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaceLap that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(lapTime, that.getLapTime()) &&
                Objects.equals(sector1, that.getSector1()) &&
                Objects.equals(sector2, that.getSector2()) &&
                Objects.equals(sector3, that.getSector3());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lapTime, sector1, sector2, sector3);
    }

    public static final class Builder extends LapBuilder<Builder, PaceLap> {

        private Duration lapTime;
        private Duration sector1;
        private boolean sector1Set = false;
        private Duration sector2;
        private boolean sector2Set = false;
        private Duration sector3;
        private boolean sector3Set = false;

        public Builder withLapTime(Duration lapTime) {
            requireNotNull(lapTime, "lapTime");
            this.lapTime = lapTime;
            return self();
        }

        public Builder withSector1(Duration sector1) {
            this.sector1 = sector1;
            sector1Set = true;
            return self();
        }

        public Builder withSector2(Duration sector2) {
            this.sector2 = sector2;
            sector2Set = true;
            return self();
        }

        public Builder withSector3(Duration sector3) {
            this.sector3 = sector3;
            sector3Set = true;
            return self();
        }

        @Override
        protected void validate() {
            super.validate();
            if (lapTime == null ||
                !sector1Set ||
                !sector2Set ||
                !sector3Set) {
                throw new IllegalStateException("All values must be set");
            }
        }

        @Override
        public PaceLap build() {
            validate();
            return new PaceLap(this);
        }
    }
}
