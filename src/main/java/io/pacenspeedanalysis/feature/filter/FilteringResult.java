package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.analysis.LapKey;

import java.util.Set;

public record FilteringResult(
        EFilteringType type,
        int newlyHidden,
        int alreadyHidden,
        Set<LapKey> hiddenLaps
) {
}
