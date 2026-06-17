package io.pacenspeedanalysis.model.analysis.export;

import io.pacenspeedanalysis.util.string.StringUtils;

public record ExportColumns(
        String name,
        String average,
        String median,
        String stDev,
        String laps,
        String totalLaps
) {
    public ExportColumns {
        StringUtils.requireNotBlank(name, "name");
        StringUtils.requireNotBlank(average, "average");
        StringUtils.requireNotBlank(median, "median");
        StringUtils.requireNotBlank(stDev, "stDev");
        StringUtils.requireNotBlank(laps, "laps");
        StringUtils.requireNotBlank(totalLaps, "totalLaps");
    }
}
