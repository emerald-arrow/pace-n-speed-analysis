package io.pacenspeedanalysis.model.row;

import io.pacenspeedanalysis.model.EFlag;

import java.time.Duration;
import java.util.Objects;

public abstract class Row {

    protected final String carNumber;
    protected final short lapNumber;
    protected final boolean wentThroughPitLane;
    protected final String driver;
    protected final String category;
    protected final String team;
    protected final String manufacturer;
    protected final EFlag flagAtFinishLine;
    protected final Duration elapsed;

    public Row(String carNumber, short lapNumber, boolean wentThroughPitLane, String driver, String category,
               String team, String manufacturer, EFlag flagAtFinishLine, Duration elapsed) {
        this.carNumber = carNumber;
        this.lapNumber = lapNumber;
        this.wentThroughPitLane = wentThroughPitLane;
        this.driver = driver;
        this.category = category;
        this.team = team;
        this.manufacturer = manufacturer;
        this.flagAtFinishLine = flagAtFinishLine;
        this.elapsed = elapsed;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public short getLapNumber() {
        return lapNumber;
    }

    public boolean getWentThroughPitLane() {
        return wentThroughPitLane;
    }

    public String getDriver() {
        return driver;
    }

    public String getCategory() {
        return category;
    }

    public String getTeam() {
        return team;
    }

    public String getFullTeamName() {
        return String.format("#%s %s", carNumber, team);
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public EFlag getFlagAtFinishLine() {
        return flagAtFinishLine;
    }

    public Duration getElapsed() {
        return elapsed;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Row that)) return false;
        return lapNumber == that.getLapNumber() &&
                wentThroughPitLane == that.getWentThroughPitLane() &&
                Objects.equals(carNumber, that.getCarNumber()) &&
                Objects.equals(driver, that.getDriver()) &&
                Objects.equals(category, that.getCategory()) &&
                Objects.equals(team, that.getTeam()) &&
                Objects.equals(manufacturer, that.getManufacturer()) &&
                flagAtFinishLine == that.getFlagAtFinishLine() &&
                Objects.equals(elapsed, that.getElapsed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                carNumber,
                lapNumber,
                wentThroughPitLane,
                driver,
                category,
                team,
                manufacturer,
                flagAtFinishLine,
                elapsed
        );
    }
}
