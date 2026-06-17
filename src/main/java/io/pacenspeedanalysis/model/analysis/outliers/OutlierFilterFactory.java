package io.pacenspeedanalysis.model.analysis.outliers;

import io.pacenspeedanalysis.model.EAnalysisType;

public final class OutlierFilterFactory {

    public static OutlierFilterStrategy forType(EAnalysisType type) {
        return switch (type) {
            case PACE -> new PaceOutlierFilter();
            case AVG_SPEED -> new SpeedOutlierFilter();
        };
    }
}
