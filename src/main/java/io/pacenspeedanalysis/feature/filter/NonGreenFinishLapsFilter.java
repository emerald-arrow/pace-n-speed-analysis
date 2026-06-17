package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.EFlag;
import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NonGreenFinishLapsFilter extends LapsFilter {

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<LapKey> hiddenLaps) {
        final Set<LapKey> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            for (Lap lap : record.laps()) {
                if (lap.getFlagAtFinishLine() != EFlag.GF) {
                    final LapKey lapKey = new LapKey(record.name(), lap.getLapNumber());
                    final boolean hidden = hiddenLaps.contains(lapKey);

                    if (hidden) {
                        alreadyHidden++;
                    } else {
                        newlyHidden++;
                        lapsToHide.add(lapKey);
                    }
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
}
