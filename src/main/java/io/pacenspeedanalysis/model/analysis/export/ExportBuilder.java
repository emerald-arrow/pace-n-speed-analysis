package io.pacenspeedanalysis.model.analysis.export;

import java.util.List;

public abstract class ExportBuilder<T, SELF extends ExportBuilder<T, SELF, PRODUCT>, PRODUCT> {

    protected String nameColumn;
    protected List<String> names;
    protected String averageColumn;
    protected List<T> averages;
    protected String medianColumn;
    protected List<T> medians;
    protected String stDevColumn;
    protected List<T> stDevs;
    protected String lapsColumn;
    protected List<Short> laps;
    protected String totalLapsColumn;
    protected List<Short> totalLaps;

    @SuppressWarnings("unchecked")
    protected SELF self() {
        return (SELF) this;
    }

    public SELF withNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
        return self();
    }

    public SELF withNames(List<String> names) {
        this.names = names;
        return self();
    }

    public SELF withAverageColumn(String averageColumn) {
        this.averageColumn = averageColumn;
        return self();
    }

    public SELF withAverages(List<T> averages) {
        this.averages = averages;
        return self();
    }

    public SELF withMedianColumn(String medianColumn) {
        this.medianColumn = medianColumn;
        return self();
    }

    public SELF withMedians(List<T> medians) {
        this.medians = medians;
        return self();
    }

    public SELF withStDevColumn(String stDevColumn) {
        this.stDevColumn = stDevColumn;
        return self();
    }

    public SELF withStDevs(List<T> stDevs) {
        this.stDevs = stDevs;
        return self();
    }

    public SELF withLapsColumn(String lapsColumn) {
        this.lapsColumn = lapsColumn;
        return self();
    }

    public SELF withLaps(List<Short> laps) {
        this.laps = laps;
        return self();
    }

    public SELF withTotalLapsColumn(String totalLapsColumn) {
        this.totalLapsColumn = totalLapsColumn;
        return self();
    }

    public SELF withTotalLaps(List<Short> totalLaps) {
        this.totalLaps = totalLaps;
        return self();
    }

    protected void validate() {
        if (nameColumn == null ||
            names == null ||
            averageColumn == null ||
            averages == null ||
            medianColumn == null ||
            medians == null ||
            stDevColumn == null ||
            stDevs == null ||
            lapsColumn == null ||
            laps == null ||
            totalLapsColumn == null ||
            totalLaps == null) {
            throw new IllegalStateException("All values must be set");
        }
    }

    public abstract PRODUCT build();
}
