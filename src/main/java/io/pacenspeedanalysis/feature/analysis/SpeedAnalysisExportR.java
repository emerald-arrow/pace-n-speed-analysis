package io.pacenspeedanalysis.feature.analysis;

import io.pacenspeedanalysis.model.analysis.export.ExportColumns;
import io.pacenspeedanalysis.model.analysis.export.ExportData;
import io.pacenspeedanalysis.model.analysis.export.SpeedExportR;

import java.math.BigDecimal;
import java.util.List;

public class SpeedAnalysisExportR implements AnalysisExport<BigDecimal> {

    private SpeedExportR.Builder builder;

    public SpeedAnalysisExportR() {
        this.builder = new SpeedExportR.Builder();
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
        builder = builder.withNames(data.names())
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
