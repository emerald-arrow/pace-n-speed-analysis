package io.pacenspeedanalysis.model.analysis.export;

import java.util.List;

public record ExportData(
        List<String> names,
        List<?> averages,
        List<?> medians,
        List<?> stDevs,
        List<Short> laps,
        List<Short> totalLaps
) {
    public ExportData {
        if (names == null ||
            averages == null ||
            medians == null ||
            stDevs == null ||
            laps == null ||
            totalLaps == null) {
            throw new IllegalArgumentException("null are not allowed values");
        }

        if (names.isEmpty() ||
            averages.isEmpty() ||
            medians.isEmpty() ||
            stDevs.isEmpty() ||
            laps.isEmpty() ||
            totalLaps.isEmpty()) {
            throw new IllegalArgumentException("empty lists are not allowed values");
        }
    }
}
