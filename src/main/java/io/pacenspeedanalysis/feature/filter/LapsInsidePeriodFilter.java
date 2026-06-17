package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.time.Duration;
import java.util.*;

public final class LapsInsidePeriodFilter extends LapsFilter {

    private final Duration periodStart;
    private final Duration periodEnd;

    public LapsInsidePeriodFilter(Duration periodStart, Duration periodEnd) {
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

            final ListIterator<? extends Lap> iterator = laps.listIterator();

            while (iterator.hasNext()) {
                final Lap currentLap = iterator.next();
                final Duration currentElapsed = currentLap.getElapsed();

                final boolean duringPeriod = DurationUtils.isBetweenDurations(
                        periodStart,
                        periodEnd,
                        currentElapsed
                );

                if (duringPeriod) {
                    final LapKey currentLapKey = new LapKey(record.name(), currentLap.getLapNumber());
                    final boolean currentLapHidden = hiddenLaps.contains(currentLapKey);

                    if (currentLapHidden) {
                        alreadyHidden++;
                    } else {
                        newlyHidden++;
                        lapsToHide.add(currentLapKey);
                    }
                    continue;
                }

                if (currentElapsed.compareTo(periodEnd) > 0) {
                    if (!iterator.hasPrevious()) {
                        break;
                    }

                    final int previousIndex = iterator.previousIndex();

                    final Lap previousLap = laps.get(previousIndex);

                    final boolean affectedByPeriod = DurationUtils.isBetweenDurations(
                            previousLap.getElapsed(),
                            currentElapsed,
                            periodEnd
                    );

                    if (affectedByPeriod) {
                        final LapKey previousLapKey = new LapKey(record.name(), previousLap.getLapNumber());
                        final boolean previousLapHidden = hiddenLaps.contains(previousLapKey);

                        if (previousLapHidden) {
                            alreadyHidden++;
                        } else {
                            newlyHidden++;
                            lapsToHide.add(previousLapKey);
                        }
                    }

                    break;
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
