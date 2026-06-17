package io.pacenspeedanalysis.model.row;

import io.pacenspeedanalysis.model.EFlag;
import io.pacenspeedanalysis.util.duration.DurationUtils;
import io.pacenspeedanalysis.util.numeric.ShortUtils;
import io.pacenspeedanalysis.util.string.StringUtils;

import java.time.Duration;

public abstract class RowBuilder<T, SELF extends RowBuilder<T, SELF, PRODUCT>, PRODUCT> {

    private static final String PIT_STOP_MARK = "B";

    protected String carNumber;
    protected Short lapNumber;
    protected Boolean wentThroughPitLane;
    protected String driver;
    protected String category;
    protected String team;
    protected String manufacturer;
    protected EFlag flagAtFinishLine;
    protected Duration elapsed;

    @SuppressWarnings("unchecked")
    protected SELF self() {
        return (SELF) this;
    }

    public SELF withCarNumber(String carNumber) {
        StringUtils.requireNotBlank(carNumber, "carNumber");
        this.carNumber = carNumber.trim();
        return self();
    }

    public SELF withLapNumber(String lapNumber) {
        ShortUtils.requireParsable(lapNumber, "lapNumber");
        this.lapNumber = Short.parseShort(lapNumber.trim());
        return self();
    }

    public SELF withWentThroughPitLane(String wentThroughPitLane) {
        requireNotNull(wentThroughPitLane, "wentThroughPitLane");
        this.wentThroughPitLane = wentThroughPitLane.equalsIgnoreCase(PIT_STOP_MARK);
        return self();
    }

    public SELF withDriver(String driver) {
        StringUtils.requireNotBlank(driver, "driver");
        this.driver = formatString(driver);
        return self();
    }

    public SELF withCategory(String category) {
        requireNotNull(category, "category");
        this.category = formatString(category);
        return self();
    }

    public SELF withTeam(String team) {
        StringUtils.requireNotBlank(team, "team");
        this.team = formatString(team);
        return self();
    }

    public SELF withManufacturer(String manufacturer) {
        StringUtils.requireNotBlank(manufacturer, "manufacturer");
        this.manufacturer = formatString(manufacturer);
        return self();
    }

    public SELF withFlagAtFinishLine(String flagAtFinishLine) {
        requireNotNull(flagAtFinishLine, "flagAtFinishLine");
        this.flagAtFinishLine = EFlag.of(flagAtFinishLine);
        return self();
    }

    public SELF withElapsed(String elapsed) {
        StringUtils.requireNotBlank(elapsed, "elapsed");
        this.elapsed = DurationUtils.parseTime(elapsed);
        return self();
    }

    protected void validate() {
        if (carNumber == null ||
            lapNumber == null ||
            wentThroughPitLane == null ||
            driver == null ||
            category == null ||
            team == null ||
            manufacturer == null ||
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

    protected abstract T parseValue(String s);

    private String formatString(String s) {
        return s.trim().toUpperCase();
    }

    public abstract PRODUCT build();
}
