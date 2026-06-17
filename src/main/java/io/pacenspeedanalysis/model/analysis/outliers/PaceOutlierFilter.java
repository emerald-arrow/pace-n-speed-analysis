package io.pacenspeedanalysis.model.analysis.outliers;

import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.analysis.Quartiles;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.data.PaceDataRecord;
import io.pacenspeedanalysis.model.lap.PaceLap;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PaceOutlierFilter implements OutlierFilterStrategy {

    @Override
    public Set<LapKey> filter(List<DataRecord> data) {
        final Set<LapKey> keys = new HashSet<>();

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
                    keys.add(new LapKey(record.name(), lap.getLapNumber()));
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
                    keys.add(new LapKey(record.name(), lap.getLapNumber()));
                }
            }
        }

        return Collections.unmodifiableSet(keys);
    }
}
