package io.pacenspeedanalysis.feature.start;

import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.EAggregationType;
import io.pacenspeedanalysis.model.EAnalysisType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface CsvImportService {
    List<DataRecord> importCsv(InputStream is,
                               EAnalysisType type,
                               EAggregationType aggregationType) throws IOException;
}
