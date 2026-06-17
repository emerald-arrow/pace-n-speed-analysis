package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PitLapsFilter extends LapsFilter {

    private final boolean filterInLaps;
    private final boolean filterOutLaps;

    public PitLapsFilter(boolean filterInLaps, boolean filterOutLaps) {
        this.filterInLaps = filterInLaps;
        this.filterOutLaps = filterOutLaps;
    }

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<LapKey> hiddenLaps) {
        final Set<LapKey> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            for (int i = 0; i < record.laps().size(); i++) {
                final Lap lap = record.laps().get(i);

                if (lap.getWentThroughPitLane()) {
                    final LapKey inLapKey = new LapKey(record.name(), lap.getLapNumber());
                    final boolean inLapHidden = hiddenLaps.contains(inLapKey);

                    if (filterInLaps) {
                        if (inLapHidden) {
                            alreadyHidden++;
                        } else {
                            newlyHidden++;
                            lapsToHide.add(inLapKey);
                        }
                    }

                    if (filterOutLaps && i + 1 < record.laps().size()) {
                        final LapKey outLapKey = new LapKey(record.name(), record.laps().get(i + 1).getLapNumber());
                        final boolean outLapHidden = hiddenLaps.contains(outLapKey);

                        if (outLapHidden) {
                            alreadyHidden++;
                        } else {
                            newlyHidden++;
                            lapsToHide.add(outLapKey);
                        }
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
