package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;

import java.util.*;
import java.util.stream.Collectors;

public final class FirstLapFilter extends LapsFilter {

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<UUID> hiddenLaps) {
        final Set<UUID> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            final Set<UUID> firstLaps = getFirstLaps(record.laps());

            for (UUID id : firstLaps) {
                final boolean hidden = hiddenLaps.contains(id);

                if (hidden) {
                    alreadyHidden++;
                } else {
                    newlyHidden++;
                    lapsToHide.add(id);
                }
            }
        }

        final EFilteringType filteringType = obtainEFilteringType(alreadyHidden, newlyHidden);

        return new FilteringResult(
                filteringType,
                newlyHidden,
                alreadyHidden,
                Collections.unmodifiableSet(lapsToHide)
        );
    }

    private Set<UUID> getFirstLaps(List<? extends Lap> laps) {
        return laps.stream().filter(l -> l.getLapNumber() == 1)
                            .map(Lap::getId)
                            .collect(Collectors.toUnmodifiableSet());
    }
}
