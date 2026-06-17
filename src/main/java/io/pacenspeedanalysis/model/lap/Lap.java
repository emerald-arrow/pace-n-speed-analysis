package io.pacenspeedanalysis.model.lap;

import io.pacenspeedanalysis.model.EFlag;

import java.time.Duration;
import java.util.Objects;

public abstract class Lap {
    protected final short lapNumber;
    protected final boolean wentThroughPitLane;
    protected final EFlag flagAtFinishLine;
    protected final Duration elapsed;

    public Lap(short lapNumber, boolean wentThroughPitLane, EFlag flagAtFinishLine, Duration elapsed) {
        this.lapNumber = lapNumber;
        this.wentThroughPitLane = wentThroughPitLane;
        this.flagAtFinishLine = flagAtFinishLine;
        this.elapsed = elapsed;
    }

    public short getLapNumber() {
        return lapNumber;
    }

    public boolean getWentThroughPitLane() {
        return wentThroughPitLane;
    }

    public EFlag getFlagAtFinishLine() {
        return flagAtFinishLine;
    }

    public Duration getElapsed() {
        return elapsed;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Lap that)) return false;
        return lapNumber == that.getLapNumber() &&
                wentThroughPitLane == that.getWentThroughPitLane() &&
                flagAtFinishLine == that.getFlagAtFinishLine() &&
                Objects.equals(elapsed, that.getElapsed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(lapNumber, wentThroughPitLane, flagAtFinishLine, elapsed);
    }
}
