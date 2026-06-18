package io.pacenspeedanalysis.feature.filter;

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
    public FilteringResult apply(List<DataRecord> records, Set<UUID> hiddenLaps) {
        final Set<UUID> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            final List<? extends Lap> laps = record.laps().stream()
                                                            .sorted(Comparator.comparing(Lap::getElapsed))
                                                            .toList();

            for (final Lap lap : laps) {
                final boolean duringPeriod = DurationUtils.isBetweenDurations(
                        periodStart,
                        periodEnd,
                        lap.getElapsed()
                );

                if (!duringPeriod) {
                    final UUID id = lap.getId();
                    final boolean hidden = hiddenLaps.contains(id);

                    if (hidden) {
                        alreadyHidden++;
                    } else {
                        newlyHidden++;
                        lapsToHide.add(id);
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
