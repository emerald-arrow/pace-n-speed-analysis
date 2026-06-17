package io.pacenspeedanalysis.model.analysis;

import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.time.Duration;

public record BestSectors(
        Duration bestS1,
        Duration bestS2,
        Duration bestS3
) {
    public static BestSectors merge(BestSectors a, BestSectors b) {
        return new BestSectors(
                DurationUtils.min(a.bestS1(), b.bestS1()),
                DurationUtils.min(a.bestS2(), b.bestS2()),
                DurationUtils.min(a.bestS3(), b.bestS3())
        );
    }
}