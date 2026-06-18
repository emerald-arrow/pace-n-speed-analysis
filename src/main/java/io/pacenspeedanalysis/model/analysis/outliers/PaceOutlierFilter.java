package io.pacenspeedanalysis.model.analysis.outliers;

import io.pacenspeedanalysis.model.analysis.Quartiles;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.data.PaceDataRecord;
import io.pacenspeedanalysis.model.lap.PaceLap;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.time.Duration;
import java.util.*;

public final class PaceOutlierFilter implements OutlierFilterStrategy {

    @Override
    public Set<UUID> filter(List<DataRecord> data) {
        final Set<UUID> laps = new HashSet<>();

        for (DataRecord record : data) {
            final PaceDataRecord paceRecord = (PaceDataRecord) record;

            final List<Duration> sortedLapTimes = paceRecord.laps().stream()
                                                                    .map(PaceLap::getLapTime)
                                                                    .sorted()
                                                                    .toList();

            if (sortedLapTimes.isEmpty()) {
                continue;
            }

            final Quartiles<Duration> quartiles = DurationUtils.inclusiveQuartilesSorted(sortedLapTimes);

            if (quartiles == null) {
                for (PaceLap lap : paceRecord.laps()) {
                    laps.add(lap.getId());
                }

                continue;
            }

            for (PaceLap lap : paceRecord.laps()) {
                final boolean isNotOutlier = DurationUtils.isBetweenDurations(
                        quartiles.lowerFence(),
                        quartiles.upperFence(),
                        lap.getLapTime()
                );

                if (!isNotOutlier) {
                    laps.add(lap.getId());
                }
            }
        }

        return Collections.unmodifiableSet(laps);
    }
}
