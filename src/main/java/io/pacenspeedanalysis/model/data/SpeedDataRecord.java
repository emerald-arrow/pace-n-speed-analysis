package io.pacenspeedanalysis.model.data;

import io.pacenspeedanalysis.model.lap.SpeedLap;

import java.math.BigDecimal;
import java.util.List;

public record SpeedDataRecord(
        String name,
        String category,
        List<SpeedLap> laps,
        BigDecimal maximumSpeed
) implements DataRecord {
    public SpeedDataRecord {
        if (maximumSpeed == null) {
            maximumSpeed = laps.stream()
                                .map(SpeedLap::getTopSpeed)
                                .max(BigDecimal::compareTo)
                                .orElse(null);
        }
    }
}
