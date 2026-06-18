package io.pacenspeedanalysis.model.analysis.outliers;

import io.pacenspeedanalysis.model.data.DataRecord;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public sealed interface OutlierFilterStrategy permits PaceOutlierFilter, SpeedOutlierFilter {

    Set<UUID> filter(List<DataRecord> data);
}
