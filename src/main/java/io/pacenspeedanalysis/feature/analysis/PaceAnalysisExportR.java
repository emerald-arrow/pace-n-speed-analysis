package io.pacenspeedanalysis.feature.analysis;

import io.pacenspeedanalysis.model.analysis.export.ExportColumns;
import io.pacenspeedanalysis.model.analysis.export.ExportData;
import io.pacenspeedanalysis.model.analysis.export.PaceExportR;

import java.time.Duration;
import java.util.List;

public class PaceAnalysisExportR implements AnalysisExport<Duration> {

    private final PaceExportR.Builder builder;

    public PaceAnalysisExportR() {
        this.builder = new PaceExportR.Builder();
    }

    @Override
    public AnalysisExport<Duration> withColumns(ExportColumns c) {
        builder.withNameColumn(c.name())
                .withAverageColumn(c.average())
                .withMedianColumn(c.median())
                .withStDevColumn(c.stDev())
                .withLapsColumn(c.laps())
                .withTotalLapsColumn(c.totalLaps());

        return this;
    }

    @Override
    public AnalysisExport<Duration> withData(ExportData data) {
        builder.withNames(data.names())
                .withAverages((List<Duration>) data.averages())
                .withMedians((List<Duration>) data.medians())
                .withStDevs((List<Duration>) data.stDevs())
                .withLaps(data.laps())
                .withTotalLaps(data.totalLaps());

        return this;
    }

    @Override
    public String export() {
        return builder.build().exportAnalysis();
    }
}
