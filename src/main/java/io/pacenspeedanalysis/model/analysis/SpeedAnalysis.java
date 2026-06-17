package io.pacenspeedanalysis.model.analysis;

import io.pacenspeedanalysis.model.data.SpeedDataRecord;
import io.pacenspeedanalysis.model.lap.SpeedLap;
import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.List;

public record SpeedAnalysis(
        String name,
        short laps,
        short totalLaps,
        BigDecimal average,
        BigDecimal median,
        BigDecimal variance,
        BigDecimal standardDeviation
) implements Analysis<BigDecimal> {
    public SpeedAnalysis(SpeedDataRecord record, List<Short> hiddenLaps) {
        final List<BigDecimal> validLaps = record.laps().stream()
                                                        .filter(l -> !hiddenLaps.contains(l.getLapNumber()))
                                                        .map(SpeedLap::getTopSpeed)
                                                        .toList();

        if (validLaps.isEmpty()) {
            throw new IllegalArgumentException("No valid laps were found for given record.");
        }

        final BigDecimal variance = BigDecimalUtils.populationVariance(validLaps);

        this(
                record.name(),
                (short) validLaps.size(),
                (short) record.laps().size(),
                BigDecimalUtils.average(validLaps),
                BigDecimalUtils.median(validLaps),
                variance,
                calculateStandardDeviation(variance)
        );
    }

    private static BigDecimal calculateStandardDeviation(BigDecimal variance) {
        return BigDecimalUtils.sqrt(variance);
    }
}
