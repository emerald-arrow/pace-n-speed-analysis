package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.time.Duration;
import java.util.*;

public final class LapsOutsidePeriodFilter extends LapsFilter {

    private final Duration periodStart;
    private final Duration periodEnd;

    public LapsOutsidePeriodFilter(Duration periodStart, Duration periodEnd) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<LapKey> hiddenLaps) {
        final Set<LapKey> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            final List<? extends Lap> laps = record.laps().stream()
                                                            .sorted(Comparator.comparing(Lap::getElapsed))
                                                            .toList();

            for (final Lap currentLap : laps) {
                final boolean duringPeriod = DurationUtils.isBetweenDurations(
                        periodStart,
                        periodEnd,
                        currentLap.getElapsed()
                );

                if (!duringPeriod) {
                    final LapKey currentLapKey = new LapKey(record.name(), currentLap.getLapNumber());
                    final boolean currentLapHidden = hiddenLaps.contains(currentLapKey);

                    if (currentLapHidden) {
                        alreadyHidden++;
                    } else {
                        newlyHidden++;
                        lapsToHide.add(currentLapKey);
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
