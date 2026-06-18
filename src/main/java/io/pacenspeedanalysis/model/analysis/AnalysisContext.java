package io.pacenspeedanalysis.model.analysis;

import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.data.PaceDataRecord;
import io.pacenspeedanalysis.model.data.SpeedDataRecord;
import io.pacenspeedanalysis.model.lap.PaceLap;
import io.pacenspeedanalysis.model.lap.SpeedLap;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

public record AnalysisContext(
        Map<String, Duration> fastestLapPerEntity,
        Map<String, BestSectors> fastestSectorsPerEntity,
        Map<String, BigDecimal> maxSpeedPerEntity
) {
    private static final Duration NEUTRAL_DURATION = Duration.ofHours(9999);
    private static final BigDecimal NEUTRAL_SPEED = BigDecimal.ZERO;

    public static AnalysisContext of(List<DataRecord> records, Set<UUID> hiddenLaps) {
        final Map<String, Duration> fastestLaps = new HashMap<>();
        final Map<String, BestSectors> fastestSectorTimes = new HashMap<>();
        final Map<String, BigDecimal> maxSpeeds = new HashMap<>();

        for (DataRecord record : records) {
            switch (record) {
                case PaceDataRecord p -> {
                    final Duration fastestLap = p.laps().stream()
                                                        .filter(l -> !hiddenLaps.contains(l.getId()))
                                                        .map(PaceLap::getLapTime)
                                                        .min(Duration::compareTo)
                                                        .orElse(NEUTRAL_DURATION);

                    fastestLaps.merge(
                            p.name(),
                            fastestLap,
                            DurationUtils::min
                    );

                    final Duration bestS1 = bestSectorTime(
                            p.laps().stream().map(PaceLap::getSector1).filter(Objects::nonNull)
                    );

                    final Duration bestS2 = bestSectorTime(
                            p.laps().stream().map(PaceLap::getSector2).filter(Objects::nonNull)
                    );

                    final Duration bestS3 = bestSectorTime(
                            p.laps().stream().map(PaceLap::getSector3).filter(Objects::nonNull)
                    );

                    fastestSectorTimes.merge(
                            p.name(),
                            new BestSectors(bestS1, bestS2, bestS3),
                            BestSectors::merge
                    );
                }
                case SpeedDataRecord s -> {
                    final BigDecimal maxSpeed = s.laps().stream()
                                                        .filter(l -> !hiddenLaps.contains(l.getId()))
                                                        .map(SpeedLap::getTopSpeed)
                                                        .max(BigDecimal::compareTo)
                                                        .orElse(NEUTRAL_SPEED);

                    maxSpeeds.merge(
                            s.name(),
                            maxSpeed,
                            BigDecimal::max
                    );
                }
            }
        }
        return new AnalysisContext(fastestLaps, fastestSectorTimes, maxSpeeds);
    }

    private static Duration bestSectorTime(Stream<Duration> times) {
        return times.min(Duration::compareTo)
                    .orElse(NEUTRAL_DURATION);
    }
}
