package io.pacenspeedanalysis.model.analysis;

import io.pacenspeedanalysis.model.data.PaceDataRecord;
import io.pacenspeedanalysis.model.lap.PaceLap;
import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;

public record PaceAnalysis(
        String name,
        short laps,
        short totalLaps,
        Duration average,
        Duration median,
        Duration variance,
        Duration standardDeviation
) implements Analysis<Duration> {
    public PaceAnalysis(PaceDataRecord record, List<Short> hiddenLaps) {
        final List<Duration> validLaps = record.laps().stream()
                                                        .filter(l -> !hiddenLaps.contains(l.getLapNumber()))
                                                        .map(PaceLap::getLapTime)
                                                        .toList();

        if (validLaps.isEmpty()) {
            throw new IllegalArgumentException("No valid laps found for given record.");
        }

        final BigDecimal varianceMillis = calculateVarianceMillis(validLaps);

        this(
                record.name(),
                (short) validLaps.size(),
                (short) record.laps().size(),
                DurationUtils.average(validLaps),
                DurationUtils.median(validLaps),
                calculateVariance(varianceMillis),
                calculateStandardDeviation(varianceMillis)
        );
    }

    private static Duration calculateVariance(BigDecimal varianceMillis) {
        return Duration.ofMillis(varianceMillis.setScale(0, RoundingMode.HALF_UP).longValue());
    }

    private static BigDecimal calculateVarianceMillis(List<Duration> lapTimes) {
        final List<BigDecimal> bigDecimals = lapTimes.stream()
                                                        .map(l -> BigDecimal.valueOf(l.toMillis()))
                                                        .toList();

        return BigDecimalUtils.populationVariance(bigDecimals);
    }

    private static Duration calculateStandardDeviation(BigDecimal varianceMillis) {
        final BigDecimal sqrt = BigDecimalUtils.sqrt(varianceMillis);
        return Duration.ofMillis(sqrt.setScale(0, RoundingMode.HALF_UP).longValue());
    }
}
