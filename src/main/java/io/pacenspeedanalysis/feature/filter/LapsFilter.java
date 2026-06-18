package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.data.DataRecord;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class LapsFilter {

    public abstract FilteringResult apply(List<DataRecord> records, Set<UUID> hiddenLaps);

    protected EFilteringType obtainEFilteringType(int alreadyHidden, int newlyHidden) {
        final int total = alreadyHidden + newlyHidden;

        if (total == 0) {
            return EFilteringType.NOTHING_TO_DELETE;
        }

        if (newlyHidden == 0) {
            return EFilteringType.ALREADY_DELETED;
        }

        if (newlyHidden == total) {
            return EFilteringType.FULL;
        }

        return EFilteringType.PARTIAL;
    }
}
