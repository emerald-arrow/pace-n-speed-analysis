package io.pacenspeedanalysis.util.numeric;

import io.pacenspeedanalysis.model.analysis.Median;
import io.pacenspeedanalysis.model.analysis.Quartiles;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BigDecimalUtils {

    private static final int PRECISION = 16;
    private static final BigDecimal FENCE_MULTIPLICAND = BigDecimal.TWO;
    private static final MathContext MATH_CONTEXT = new MathContext(PRECISION, RoundingMode.HALF_UP);
    private static final ConcurrentHashMap<String, NumberFormat> NF_CACHE = new ConcurrentHashMap<>();

    public static BigDecimal average(List<BigDecimal> values) {
        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal value : values) {
            sum = sum.add(value);
        }

        return sum.divide(BigDecimal.valueOf(values.size()), MATH_CONTEXT);
    }

    public static int compare(BigDecimal d1, BigDecimal d2) {
        return ObjectUtils.compare(d1, d2);
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return dividend.divide(divisor, MATH_CONTEXT);
    }

    public static String format(BigDecimal value, Locale locale, int minIntegerDigits, int fractionDigits) {
        final NumberFormat nf = getNumberFormatter(locale, minIntegerDigits, fractionDigits);
        nf.setRoundingMode(MATH_CONTEXT.getRoundingMode());
        nf.setMinimumIntegerDigits(minIntegerDigits);
        nf.setMinimumFractionDigits(fractionDigits);
        nf.setMaximumFractionDigits(fractionDigits);

        return nf.format(value);
    }

    public static Quartiles<BigDecimal> inclusiveQuartilesSorted(List<BigDecimal> sortedValues) {
        if (sortedValues.size() < 2) {
            return null;
        }

        final Median<BigDecimal> q2 = medianSorted(sortedValues);
        assert q2 != null;

        final List<BigDecimal> lessThanQ2 = new ArrayList<>();
        final List<BigDecimal> greaterThanQ2 = new ArrayList<>();

        if (q2.index() != null) {
            lessThanQ2.addAll(sortedValues.subList(0, q2.index() + 1));
            greaterThanQ2.addAll(sortedValues.subList(q2.index(), sortedValues.size()));
        } else {
            lessThanQ2.addAll(
                    sortedValues.stream().filter(v -> v.compareTo(q2.value()) < 0)
                                            .collect(Collectors.toCollection(ArrayList::new))
            );
            lessThanQ2.add(q2.value());
            greaterThanQ2.addAll(
                    sortedValues.stream().filter(v -> v.compareTo(q2.value()) > 0)
                                            .collect(Collectors.toCollection(ArrayList::new))
            );
            greaterThanQ2.addFirst(q2.value());
        }

        final Median<BigDecimal> q1 = medianSorted(lessThanQ2);
        assert q1 != null;

        final Median<BigDecimal> q3 = medianSorted(greaterThanQ2);
        assert q3 != null;

        final BigDecimal iqr = q3.value().subtract(q1.value(), MATH_CONTEXT);
        final BigDecimal multipliedIQR = iqr.multiply(FENCE_MULTIPLICAND, MATH_CONTEXT);

        final BigDecimal lowerFence = q1.value().subtract(multipliedIQR, MATH_CONTEXT);
        final BigDecimal upperFence = q3.value().add(multipliedIQR, MATH_CONTEXT);

        return new Quartiles<>(
                q1.value(),
                q2.value(),
                q3.value(),
                iqr,
                lowerFence,
                upperFence
        );
    }

    public static BigDecimal median(List<BigDecimal> values) {
        if (values.isEmpty()) {
            return null;
        }

        final List<BigDecimal> sortedValues = values.stream().sorted().toList();

        final int totalCount = sortedValues.size();
        final int middle = totalCount / 2;

        if (totalCount % 2 == 1) {
            return sortedValues.get(middle);
        } else {
            return sortedValues.get(middle - 1)
                                .add(sortedValues.get(middle))
                                .divide(BigDecimal.TWO, MATH_CONTEXT);
        }
    }

    public static BigDecimal multiply(BigDecimal multiplicand, BigDecimal multiplier) {
        return multiplier.multiply(multiplicand, MATH_CONTEXT);
    }

    public static BigDecimal populationVariance(List<BigDecimal> values) {
        BigDecimal mean = BigDecimal.ZERO;
        BigDecimal m2 = BigDecimal.ZERO;
        int n = 0;

        for (BigDecimal value : values) {
            n++;
            final BigDecimal delta = value.subtract(mean);
            mean = mean.add(delta.divide(BigDecimal.valueOf(n), MATH_CONTEXT));
            final BigDecimal delta2 = value.subtract(mean);
            m2 = m2.add(delta.multiply(delta2));
        }

        return m2.divide(BigDecimal.valueOf(n), MATH_CONTEXT);
    }

    public static BigDecimal sqrt(BigDecimal value) {
        return value.sqrt(MATH_CONTEXT);
    }

    private static NumberFormat getNumberFormatter(Locale locale, int minIntegerDigits, int fractionDigits) {
        final String key = locale.toLanguageTag() + "|" + minIntegerDigits + "|" + fractionDigits;

        return NF_CACHE.computeIfAbsent(key, _ -> {
            NumberFormat nf = NumberFormat.getNumberInstance(locale);
            nf.setRoundingMode(MATH_CONTEXT.getRoundingMode());
            nf.setMinimumIntegerDigits(minIntegerDigits);
            nf.setMinimumFractionDigits(fractionDigits);
            nf.setMaximumFractionDigits(fractionDigits);
            return nf;
        });
    }

    private static Median<BigDecimal> medianSorted(List<BigDecimal> sortedValues) {
        if (sortedValues.isEmpty()) {
            return null;
        }

        final int totalCount = sortedValues.size();
        final int middle = totalCount / 2;

        if (totalCount % 2 == 1) {
            return new Median<>(middle, sortedValues.get(middle));
        } else {
            final BigDecimal first = sortedValues.get(middle - 1);
            final BigDecimal second = sortedValues.get(middle);
            final BigDecimal median = first.add(second).divide(BigDecimal.TWO, MATH_CONTEXT);
            return new Median<>(null, median);
        }
    }
}