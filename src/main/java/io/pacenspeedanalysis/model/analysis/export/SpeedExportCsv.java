package io.pacenspeedanalysis.model.analysis.export;

import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public final class SpeedExportCsv extends ExportCsv<BigDecimal> {

    private static final int INTEGER_DIGITS = 1;
    private static final int FRACTION_DIGITS = 1;

    private final Locale locale;

    private SpeedExportCsv(String nameColumn, List<String> names, String averageColumn, List<BigDecimal> averages,
                          String medianColumn, List<BigDecimal> medians, String stDevColumn, List<BigDecimal> stDevs,
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

    private SpeedExportCsv(Builder builder) {
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
    protected String formatValue(BigDecimal value) {
        return BigDecimalUtils.format(value, locale, INTEGER_DIGITS, FRACTION_DIGITS);
    }

    public static final class Builder extends ExportBuilder<BigDecimal, Builder, SpeedExportCsv> {

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
        public SpeedExportCsv build() {
            validate();

            return new SpeedExportCsv(this);
        }
    }
}
