package io.pacenspeedanalysis.model.analysis.outliers;

import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.analysis.LapKey;

import java.util.List;
import java.util.Set;

public sealed interface OutlierFilterStrategy permits PaceOutlierFilter, SpeedOutlierFilter {

    Set<LapKey> filter(List<DataRecord> data);
}
