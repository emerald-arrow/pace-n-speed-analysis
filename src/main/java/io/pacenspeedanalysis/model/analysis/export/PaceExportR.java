package io.pacenspeedanalysis.model.analysis.export;

import java.time.Duration;
import java.util.List;

public final class PaceExportR extends ExportR<Duration> {

    private PaceExportR(String nameColumn, List<String> names, String averageColumn, List<Duration> averages,
                       String medianColumn, List<Duration> medians, String stDevColumn, List<Duration> stDevs,
                       String lapsColumn, List<Short> laps, String totalLapsColumn, List<Short> totalLaps) {
        super(
                nameColumn, names,
                averageColumn, averages,
                medianColumn, medians,
                stDevColumn, stDevs,
                lapsColumn, laps,
                totalLapsColumn, totalLaps
        );
    }

    private PaceExportR(Builder builder) {
        this(
                builder.nameColumn, builder.names,
                builder.averageColumn, builder.averages,
                builder.medianColumn, builder.medians,
                builder.stDevColumn, builder.stDevs,
                builder.lapsColumn, builder.laps,
                builder.totalLapsColumn, builder.totalLaps
        );
    }

    @Override
    protected String disclaimer() {
        return "# Time values are in milliseconds\n";
    }

    @Override
    protected String averagesData() {
        return extractElements(averageColumn, averages, d -> String.valueOf(d.toMillis()));
    }

    @Override
    protected String mediansData() {
        return extractElements(medianColumn, medians, d -> String.valueOf(d.toMillis()));
    }

    @Override
    protected String stDevsData() {
        return extractElements(stDevColumn, stDevs, d -> String.valueOf(d.toMillis()));
    }

    public static final class Builder extends ExportBuilder<Duration, Builder, PaceExportR> {

        @Override
        public PaceExportR build() {
            validate();

            return new PaceExportR(this);
        }
    }
}
