package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.SpeedLap;
import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BestSpeedLapsPercentageFilter extends BestLapsPercentageFilter {

    public BestSpeedLapsPercentageFilter(int threshold) {
        super(threshold);
    }

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<LapKey> hiddenLaps) {
        final Set<LapKey> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            final String name = record.name();
            final List<SpeedLap> laps = record.laps().stream().map(l -> (SpeedLap) l)
                                                                .sorted((a, b) ->
                                                                        BigDecimalUtils.compare(
                                                                                a.getTopSpeed(),
                                                                                b.getTopSpeed()
                                                                        )
                                                                )
                                                                .toList()
                                                                .reversed();

            final short individualThreshold = calculateIndividualThreshold(laps.size());

            for (short i = individualThreshold; i < laps.size(); i++) {
                final LapKey lapToHide = new LapKey(name, laps.get(i).getLapNumber());
                final boolean hidden = hiddenLaps.contains(lapToHide);

                if (hidden) {
                    alreadyHidden++;
                } else {
                    newlyHidden++;
                    lapsToHide.add(lapToHide);
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
