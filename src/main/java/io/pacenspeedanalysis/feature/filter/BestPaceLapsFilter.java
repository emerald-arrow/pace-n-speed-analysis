package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;
import io.pacenspeedanalysis.model.lap.PaceLap;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.util.*;

public final class BestPaceLapsFilter extends BestLapsFilter {

    public BestPaceLapsFilter(int threshold) {
        super(threshold);
    }

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<UUID> hiddenLaps) {
        final Set<UUID> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            if (threshold > record.laps().size()) {
                for (Lap lap : record.laps()) {
                    final UUID id = lap.getId();
                    final boolean hidden = hiddenLaps.contains(id);

                    if (hidden) {
                        alreadyHidden++;
                    } else {
                        newlyHidden++;
                        lapsToHide.add(id);
                    }
                }
                continue;
            }

            final List<PaceLap> laps = record.laps().stream().map(l -> (PaceLap) l)
                                                                .sorted((a, b) -> (
                                                                        DurationUtils.compare(
                                                                            a.getLapTime(),
                                                                            b.getLapTime()
                                                                        )
                                                                ))
                                                                .toList();

            for (int i = threshold; i < laps.size(); i++) {
                final UUID id = laps.get(i).getId();
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
}
