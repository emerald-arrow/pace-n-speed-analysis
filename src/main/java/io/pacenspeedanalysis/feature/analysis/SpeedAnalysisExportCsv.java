package io.pacenspeedanalysis.feature.analysis;

import io.pacenspeedanalysis.model.analysis.export.ExportColumns;
import io.pacenspeedanalysis.model.analysis.export.ExportData;
import io.pacenspeedanalysis.model.analysis.export.SpeedExportCsv;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class SpeedAnalysisExportCsv implements AnalysisExport<BigDecimal> {

    private final SpeedExportCsv.Builder builder;

    public SpeedAnalysisExportCsv(Locale locale) {
        this.builder = new SpeedExportCsv.Builder().withLocale(locale);
    }

    @Override
    public AnalysisExport<BigDecimal> withColumns(ExportColumns c) {
        builder.withNameColumn(c.name())
                .withAverageColumn(c.average())
                .withMedianColumn(c.median())
                .withStDevColumn(c.stDev())
                .withLapsColumn(c.laps())
                .withTotalLapsColumn(c.totalLaps());

        return this;
    }

    @Override
    public AnalysisExport<BigDecimal> withData(ExportData data) {
        builder.withNames(data.names())
                .withAverages((List<BigDecimal>) data.averages())
                .withMedians((List<BigDecimal>) data.medians())
                .withStDevs((List<BigDecimal>) data.stDevs())
                .withLaps(data.laps())
                .withTotalLaps(data.totalLaps());

        return this;
    }

    @Override
    public String export() {
        return builder.build().exportAnalysis();
    }
}
