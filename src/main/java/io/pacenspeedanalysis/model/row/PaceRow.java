package io.pacenspeedanalysis.model.row;

import io.pacenspeedanalysis.util.duration.DurationUtils;
import io.pacenspeedanalysis.util.string.StringUtils;

import java.time.Duration;
import java.util.Objects;

public final class PaceRow extends Row {
    private final Duration lapTime;
    private final Duration sectorOne;
    private final Duration sectorTwo;
    private final Duration sectorThree;

    private PaceRow(Builder builder) {
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
        this.lapTime = builder.lapTime;
        this.sectorOne = builder.sector1;
        this.sectorTwo = builder.sector2;
        this.sectorThree = builder.sector3;
    }

    public Duration getLapTime() {
        return lapTime;
    }

    public Duration getSectorOne() {
        return sectorOne;
    }

    public Duration getSectorTwo() {
        return sectorTwo;
    }

    public Duration getSectorThree() {
        return sectorThree;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaceRow that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(lapTime, that.getLapTime()) &&
                Objects.equals(sectorOne, that.getSectorOne()) &&
                Objects.equals(sectorTwo, that.getSectorTwo()) &&
                Objects.equals(sectorThree, that.getSectorThree());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lapTime, sectorOne, sectorTwo, sectorThree);
    }

    public static final class Builder extends RowBuilder<Duration, Builder, PaceRow> {
        private Duration lapTime;
        private Duration sector1;
        private boolean sector1Processed = false;
        private Duration sector2;
        private boolean sector2Processed = false;
        private Duration sector3;
        private boolean sector3Processed = false;

        public Builder withLapTime(String lapTime) {
            StringUtils.requireNotBlank(lapTime, "lapTime");
            this.lapTime = parseValue(lapTime);
            return self();
        }

        public Builder withSector1(String sector1) {
            requireNotNull(sector1, "sector1");
            this.sector1 = parseValue(sector1);
            this.sector1Processed = true;
            return self();
        }

        public Builder withSector2(String sector2) {
            requireNotNull(sector2, "sector2");
            this.sector2 = parseValue(sector2);
            this.sector2Processed = true;
            return self();
        }

        public Builder withSector3(String sector3) {
            requireNotNull(sector3, "sector3");
            this.sector3 = parseValue(sector3);
            this.sector3Processed = true;
            return self();
        }

        @Override
        protected void validate() {
            super.validate();
            if (lapTime == null ||
                !sector1Processed ||
                !sector2Processed ||
                !sector3Processed
            ) {
                throw new IllegalStateException("All values must be set");
            }
        }

        @Override
        protected Duration parseValue(String s) {
            if (!s.isBlank()) {
                return DurationUtils.parseTime(s);
            } else {
                return null;
            }
        }

        @Override
        public PaceRow build() {
            return new PaceRow(this);
        }
    }
}
