package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.model.analysis.AnalysisContext;
import io.pacenspeedanalysis.model.analysis.BestSectors;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.lap.Lap;
import io.pacenspeedanalysis.model.lap.PaceLap;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.time.Duration;
import java.util.*;

public final class SlowPaceLapsFilter extends LapsFilter {

    private static final short BASE_THRESHOLD = 100;

    private final short threshold;
    private final boolean filterOutSectors;
    private final AnalysisContext analysisContext;

    public SlowPaceLapsFilter(short threshold, boolean filterOutSectors, AnalysisContext analysisContext) {
        if (threshold <= 0 || threshold >= BASE_THRESHOLD) {
            throw new IllegalArgumentException("threshold must be between 1 and " + (BASE_THRESHOLD - 1));
        }
        if (analysisContext == null) {
            throw new IllegalArgumentException("analysisContext must not be null");
        }
        this.threshold = threshold;
        this.filterOutSectors = filterOutSectors;
        this.analysisContext = analysisContext;
    }

    @Override
    public FilteringResult apply(List<DataRecord> records, Set<UUID> hiddenLaps) {
        final short limit = (short) (BASE_THRESHOLD + threshold);

        if (filterOutSectors) {
            return filterLapsAndSectors(records, hiddenLaps, limit);
        } else {
            return filterLaps(records, hiddenLaps, limit);
        }
    }

    private FilteringResult filterLaps(List<DataRecord> records, Set<UUID> hiddenLaps, short filter) {
        final Set<UUID> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            final Duration fastestLap = analysisContext.fastestLapPerEntity().get(record.name());

            final Duration limit = fastestLap.multipliedBy(filter).dividedBy(100);

            for (Lap lap : record.laps()) {
                final PaceLap paceLap = (PaceLap) lap;

                if (compare(paceLap.getLapTime(), limit) > 0) {
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

    private FilteringResult filterLapsAndSectors(List<DataRecord> records, Set<UUID> hiddenLaps, short filter) {
        final Set<UUID> lapsToHide = new HashSet<>();

        int newlyHidden = 0;
        int alreadyHidden = 0;

        for (DataRecord record : records) {
            final Duration fastestLap = analysisContext.fastestLapPerEntity().get(record.name());
            final BestSectors fastestSectors = analysisContext.fastestSectorsPerEntity().get(record.name());

            final Duration lapLimit = fastestLap.multipliedBy(filter).dividedBy(100);
            final Duration s1Limit = fastestSectors.bestS1().multipliedBy(filter).dividedBy(100);
            final Duration s2Limit = fastestSectors.bestS2().multipliedBy(filter).dividedBy(100);
            final Duration s3Limit = fastestSectors.bestS3().multipliedBy(filter).dividedBy(100);

            for (Lap lap : record.laps()) {
                final PaceLap paceLap = (PaceLap) lap;

                if (compare(paceLap.getLapTime(), lapLimit) > 0 ||
                        compare(paceLap.getSector1(), s1Limit) > 0 ||
                        compare(paceLap.getSector2(), s2Limit) > 0 ||
                        compare(paceLap.getSector3(), s3Limit) > 0
                ) {
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

    private int compare(Duration d1, Duration d2) {
        return DurationUtils.compare(d1, d2);
    }
}
