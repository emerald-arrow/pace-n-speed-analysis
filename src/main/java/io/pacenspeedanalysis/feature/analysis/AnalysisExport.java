package io.pacenspeedanalysis.feature.analysis;

import io.pacenspeedanalysis.model.analysis.export.ExportColumns;
import io.pacenspeedanalysis.model.analysis.export.ExportData;

public interface AnalysisExport<T> {
    AnalysisExport<T> withColumns(ExportColumns c);
    AnalysisExport<T> withData(ExportData data);
    String export();
}

