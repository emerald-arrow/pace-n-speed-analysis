package io.pacenspeedanalysis.model.lap;

import io.pacenspeedanalysis.model.EFlag;

import java.time.Duration;

public abstract class LapBuilder<SELF extends LapBuilder<SELF, PRODUCT>, PRODUCT> {

    protected Short lapNumber;
    protected Boolean wentThroughPitLane;
    protected EFlag flagAtFinishLine;
    protected Duration elapsed;

    @SuppressWarnings("unchecked")
    protected SELF self() {
        return (SELF) this;
    }

    public SELF withLapNumber(short lapNumber) {
        this.lapNumber = lapNumber;
        return self();
    }

    public SELF withWentThroughPitLane(boolean wentThroughPitLane) {
        this.wentThroughPitLane = wentThroughPitLane;
        return self();
    }

    public SELF withFlagAtFinishLine(EFlag flagAtFinishLine) {
        requireNotNull(flagAtFinishLine, "flagAtFinishLine");
        this.flagAtFinishLine = flagAtFinishLine;
        return self();
    }

    public SELF withElapsed(Duration elapsed) {
        requireNotNull(elapsed, "elapsed");
        this.elapsed = elapsed;
        return self();
    }

    protected void validate() {
        if (lapNumber == null ||
            wentThroughPitLane == null ||
            flagAtFinishLine == null ||
            elapsed == null
        ) {
            throw new IllegalStateException("All values must be set");
        }
    }

    protected void requireNotNull(Object o, String name) {
        if (o == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
    }

    public abstract PRODUCT build();
}
