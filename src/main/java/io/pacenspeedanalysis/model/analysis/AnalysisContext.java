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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record AnalysisContext(
        Map<String, Duration> fastestLapPerEntity,
        Map<String, BestSectors> fastestSectorsPerEntity,
        Map<String, BigDecimal> maxSpeedPerEntity
) {
    private static final Duration NEUTRAL_DURATION = Duration.ofHours(9999);
    private static final BigDecimal NEUTRAL_SPEED = BigDecimal.ZERO;

    public static AnalysisContext of(List<DataRecord> records, Set<LapKey> hiddenLaps) {
        final Map<String, Duration> fastestLaps = new HashMap<>();
        final Map<String, BestSectors> fastestSectorTimes = new HashMap<>();
        final Map<String, BigDecimal> maxSpeeds = new HashMap<>();

        final Map<String, Set<Short>> groupedExcluded = hiddenLaps.stream().collect(
                Collectors.groupingBy(
                        LapKey::entityName,
                        Collectors.mapping(LapKey::lapNumber, Collectors.toSet())
                )
        );

        for (DataRecord record : records) {
            final Set<Short> excludedLaps = groupedExcluded.getOrDefault(record.name(), Set.of());

            switch (record) {
                case PaceDataRecord p -> {
                    final Duration fastestLap = p.laps().stream()
                                                        .filter(l -> !excludedLaps.contains(l.getLapNumber()))
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
                                                        .filter(l -> !excludedLaps.contains(l.getLapNumber()))
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
