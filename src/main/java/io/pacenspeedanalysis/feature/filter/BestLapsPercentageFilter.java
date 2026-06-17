package io.pacenspeedanalysis.feature.filter;

import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class BestLapsPercentageFilter extends LapsFilter {

    private static final int MIN_THRESHOLD = 1;
    private static final int MAX_THRESHOLD = 99;

    protected final BigDecimal threshold;

    public BestLapsPercentageFilter(int threshold) {
        if (threshold < MIN_THRESHOLD || threshold > MAX_THRESHOLD) {
            throw new IllegalArgumentException("threshold must be between " + MIN_THRESHOLD + " and " + MAX_THRESHOLD);
        }

        this.threshold = BigDecimalUtils.divide(BigDecimal.valueOf(threshold), BigDecimal.valueOf(100));
    }

    protected short calculateIndividualThreshold(int totalLaps) {
        return BigDecimalUtils.multiply(threshold, BigDecimal.valueOf(totalLaps))
                                .setScale(0, RoundingMode.CEILING)
                                .shortValue();
    }
}
