package io.pacenspeedanalysis.model.analysis.export;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class SpeedExportR extends ExportR<BigDecimal> {

    private SpeedExportR(String nameColumn, List<String> names, String averageColumn, List<BigDecimal> averages,
                        String medianColumn, List<BigDecimal> medians, String stDevColumn, List<BigDecimal> stDevs,
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

    private SpeedExportR(Builder builder) {
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
        return "";
    }

    @Override
    protected String averagesData() {
        return extractElements(
                averageColumn,
                averages,
                s -> scale(s).toString()
        );
    }

    @Override
    protected String mediansData() {
        return extractElements(
                medianColumn,
                medians,
                s -> scale(s).toString()
        );
    }

    @Override
    protected String stDevsData() {
        return extractElements(
                stDevColumn,
                stDevs,
                s -> scale(s).toString()
        );
    }

    private BigDecimal scale(BigDecimal d) {
        return d.setScale(1, RoundingMode.HALF_UP);
    }

    public static final class Builder extends ExportBuilder<BigDecimal, Builder, SpeedExportR> {

        @Override
        public SpeedExportR build() {
            validate();

            return new SpeedExportR(this);
        }
    }
}
