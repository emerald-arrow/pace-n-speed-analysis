package io.pacenspeedanalysis.feature.filter;

import java.util.Set;
import java.util.UUID;

public record FilteringResult(
        EFilteringType type,
        int newlyHidden,
        int alreadyHidden,
        Set<UUID> hiddenLaps
) {
}
