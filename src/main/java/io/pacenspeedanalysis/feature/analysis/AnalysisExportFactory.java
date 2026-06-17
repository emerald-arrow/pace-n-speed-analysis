package io.pacenspeedanalysis.feature.analysis;

import io.pacenspeedanalysis.model.EAnalysisType;
import io.pacenspeedanalysis.model.EExportFormat;

import java.util.Locale;

public final class AnalysisExportFactory {

    public static AnalysisExport<?> create(EAnalysisType type, EExportFormat format, Locale locale) {
        return switch (type) {
            case PACE -> switch (format) {
                case CSV -> new PaceAnalysisExportCsv(locale);
                case R   -> new PaceAnalysisExportR();
            };
            case AVG_SPEED -> switch (format) {
                case CSV -> new SpeedAnalysisExportCsv(locale);
                case R   -> new SpeedAnalysisExportR();
            };
        };
    }
}
