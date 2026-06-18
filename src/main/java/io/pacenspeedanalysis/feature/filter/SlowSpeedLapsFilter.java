package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.analysis.AnalysisContext;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;
import io.pacenspeedanalysis.model.lap.SpeedLap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class SlowSpeedLapsFilter extends LapsFilter {

    private static final short BASE_THRESHOLD = 100;

    private final short threshold;
    private final AnalysisContext analysisContext;

    public SlowSpeedLapsFilter(short threshold, AnalysisContext analysisContext) {
        if (threshold <= 0 || threshold >= BASE_THRESHOLD) {
            throw new IllegalArgumentException("threshold must be between 1 and " + (BASE_THRESHOLD - 1));
        }
        if (analysisContext == null) {
            throw new IllegalArgumentException("analysisContext must not be null");
        }
        this.threshold = threshold;
        this.analysisContext = analysisContext;
    }

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<UUID> hiddenLaps) {
        final BigDecimal filter = BigDecimal.valueOf(BASE_THRESHOLD)
                                            .subtract(BigDecimal.valueOf(threshold))
                                            .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

        final Set<UUID> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            final BigDecimal maxSpeed = analysisContext.maxSpeedPerEntity().get(record.name());

            for (Lap lap : record.laps()) {
                final SpeedLap speedLap = (SpeedLap) lap;

                if (speedLap.getTopSpeed().compareTo(maxSpeed.multiply(filter)) < 0) {
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
