package io.pacenspeedanalysis.model.analysis.export;

import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public final class PaceExportCsv extends ExportCsv<Duration> {

    private final Locale locale;

    private PaceExportCsv(String nameColumn, List<String> names, String averageColumn, List<Duration> averages,
                         String medianColumn, List<Duration> medians, String stDevColumn, List<Duration> stDevs,
                         String lapsColumn, List<Short> laps, String totalLapsColumn, List<Short> totalLaps,
                         Locale locale) {
        super(
                nameColumn, names,
                averageColumn, averages,
                medianColumn, medians,
                stDevColumn, stDevs,
                lapsColumn, laps,
                totalLapsColumn, totalLaps
        );

        if (locale == null) {
            throw new IllegalArgumentException("locale must not be null");
        }

        this.locale = locale;
    }

    private PaceExportCsv(Builder builder) {
        this(
                builder.nameColumn, builder.names,
                builder.averageColumn, builder.averages,
                builder.medianColumn, builder.medians,
                builder.stDevColumn, builder.stDevs,
                builder.lapsColumn, builder.laps,
                builder.totalLapsColumn, builder.totalLaps,
                builder.locale
        );
    }

    @Override
    protected String formatValue(Duration value) {
        return DurationUtils.formatAsLapTime(value, locale);
    }

    public static final class Builder extends ExportBuilder<Duration, Builder, PaceExportCsv> {

        private Locale locale;

        public Builder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        @Override
        protected void validate() {
            super.validate();

            if (locale == null) {
                throw new IllegalStateException("All values must be set");
            }
        }

        @Override
        public PaceExportCsv build() {
            validate();

            return new PaceExportCsv(this);
        }
    }
}
