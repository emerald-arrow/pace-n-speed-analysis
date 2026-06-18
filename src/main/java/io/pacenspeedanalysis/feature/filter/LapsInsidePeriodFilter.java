package io.pacenspeedanalysis.feature.filter;

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
    public FilteringResult apply(List<DataRecord> records, Set<UUID> hiddenLaps) {
        final Set<UUID> lapsToHide = new HashSet<>();

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
                    final UUID currentId = currentLap.getId();
                    final boolean currentLapHidden = hiddenLaps.contains(currentId);

                    if (currentLapHidden) {
                        alreadyHidden++;
                    } else {
                        newlyHidden++;
                        lapsToHide.add(currentId);
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
                        final UUID previousId = previousLap.getId();
                        final boolean previousLapHidden = hiddenLaps.contains(previousId);

                        if (previousLapHidden) {
                            alreadyHidden++;
                        } else {
                            newlyHidden++;
                            lapsToHide.add(previousId);
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
