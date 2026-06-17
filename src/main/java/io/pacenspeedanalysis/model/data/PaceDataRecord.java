package io.pacenspeedanalysis.model.data;

import io.pacenspeedanalysis.model.lap.PaceLap;

import java.time.Duration;
import java.util.List;

public record PaceDataRecord(
        String name,
        String category,
        List<PaceLap> laps,
        Duration fastestLap
) implements DataRecord {

    public PaceDataRecord {
        if (fastestLap == null) {
            fastestLap = laps.stream()
                                .map(PaceLap::getLapTime)
                                .min(Duration::compareTo)
                                .orElse(null);
        }
    }
}
