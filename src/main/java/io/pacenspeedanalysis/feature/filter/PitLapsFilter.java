package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;

import java.util.*;

public final class PitLapsFilter extends LapsFilter {

    private final boolean filterInLaps;
    private final boolean filterOutLaps;

    public PitLapsFilter(boolean filterInLaps, boolean filterOutLaps) {
        this.filterInLaps = filterInLaps;
        this.filterOutLaps = filterOutLaps;
    }

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<UUID> hiddenLaps) {
        final Set<UUID> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            for (int i = 0; i < record.laps().size(); i++) {
                final Lap lap = record.laps().get(i);

                if (lap.getWentThroughPitLane()) {
                    final UUID inLapId = lap.getId();
                    final boolean inLapHidden = hiddenLaps.contains(inLapId);

                    if (filterInLaps) {
                        if (inLapHidden) {
                            alreadyHidden++;
                        } else {
                            newlyHidden++;
                            lapsToHide.add(inLapId);
                        }
                    }

                    if (filterOutLaps && i + 1 < record.laps().size()) {
                        final UUID outLapId = record.laps().get(i + 1).getId();
                        final boolean outLapHidden = hiddenLaps.contains(outLapId);

                        if (outLapHidden) {
                            alreadyHidden++;
                        } else {
                            newlyHidden++;
                            lapsToHide.add(outLapId);
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
