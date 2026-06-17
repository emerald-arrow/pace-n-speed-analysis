package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.EAggregationType;
import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;

import java.util.*;

public final class FirstLapFilter extends LapsFilter {

    private final EAggregationType aggregationType;

    public FirstLapFilter(EAggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<LapKey> hiddenLaps) {
        final Set<LapKey> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            final short firstLapNumber = getFirstLaps(record.laps());

            final LapKey lap = new LapKey(record.name(), firstLapNumber);
            final boolean hidden = hiddenLaps.contains(lap);

            if (hidden) {
                alreadyHidden++;
            } else {
                newlyHidden++;
                lapsToHide.add(lap);
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

    private short getFirstLaps(List<? extends Lap> laps) {
        return switch (aggregationType) {
            case DRIVER -> getDriverFirstLap(laps);
            case MANUFACTURER, TEAM -> getCommonFirstLap();
        };
    }

    private short getDriverFirstLap(List<? extends Lap> laps) {
        return laps.stream().min(Comparator.comparing(Lap::getLapNumber))
                            .get()
                            .getLapNumber();
    }

    private short getCommonFirstLap() {
        return (short) 1;
    }
}
