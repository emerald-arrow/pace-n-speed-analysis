package io.pacenspeedanalysis.util.duration;

import io.pacenspeedanalysis.model.analysis.Median;
import io.pacenspeedanalysis.model.analysis.Quartiles;
import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DurationUtils {
    private static final Pattern HOURS_TIME = Pattern.compile("(\\d{1,2}):([0-5][0-9]):([0-5][0-9]).(\\d{3})");
    private static final Pattern MINUTES_TIME = Pattern.compile("([0-5]?[0-9]):([0-5][0-9]).(\\d{3})");
    private static final Pattern SECONDS_TIME = Pattern.compile("([0-5]?[0-9]).(\\d{3})");
    private static final int FENCE_MULTIPLICAND = 2;

    public static Duration average(List<Duration> durations) {
        Duration totalTime = Duration.ZERO;

        for (Duration duration : durations) {
            totalTime = totalTime.plus(duration);
        }

        return Duration.ofMillis(totalTime.dividedBy(durations.size()).toMillis());
    }

    public static int compare(Duration d1, Duration d2) {
        return ObjectUtils.compare(d1, d2);
    }

    public static String formatAsLapTime(Duration d, Locale locale) {
        final int fractionDigits = 3;

        final long minutes = d.toMinutes();
        final long seconds = d.toSecondsPart();
        final long millis = d.toMillisPart();

        final StringBuilder builder = new StringBuilder();

        final int minimumSecondsDigits = minutes > 0 ? 2 : 1;

        if (minutes > 0) {
            builder.append(minutes).append(":");
        }

        final BigDecimal rest = BigDecimal.valueOf(seconds * 1000L + millis);

        builder.append(
                BigDecimalUtils.format(
                        rest.divide(BigDecimal.valueOf(1000), fractionDigits, RoundingMode.HALF_UP),
                        locale,
                        minimumSecondsDigits,
                        fractionDigits
                )
        );

        return builder.toString();
    }

    public static Quartiles<Duration> inclusiveQuartilesSorted(List<Duration> sortedDurations) {
        if (sortedDurations.size() < 2) {
            return null;
        }

        final Median<Duration> q2 = medianSorted(sortedDurations);
        assert q2 != null;

        final List<Duration> lessThanQ2 = new ArrayList<>();
        final List<Duration> greaterThanQ2 = new ArrayList<>();

        if (q2.index() != null) {
            lessThanQ2.addAll(sortedDurations.subList(0, q2.index() + 1));
            greaterThanQ2.addAll(sortedDurations.subList(q2.index(), sortedDurations.size()));
        } else {
            lessThanQ2.addAll(
                    sortedDurations.stream().filter(d -> d.compareTo(q2.value()) < 0)
                                            .collect(Collectors.toCollection(ArrayList::new))
            );
            lessThanQ2.add(q2.value());
            greaterThanQ2.addAll(
                    sortedDurations.stream().filter(d -> d.compareTo(q2.value()) > 0)
                                            .collect(Collectors.toCollection(ArrayList::new))
            );
            greaterThanQ2.addFirst(q2.value());
        }

        final Median<Duration> q1 = medianSorted(lessThanQ2);
        assert q1 != null;

        final Median<Duration> q3 = medianSorted(greaterThanQ2);
        assert q3 != null;

        final Duration iqr = q3.value().minus(q1.value());
        final Duration multipliedIQR = iqr.multipliedBy(FENCE_MULTIPLICAND);

        final Duration lowerFence = q1.value().minus(multipliedIQR);
        final Duration upperFence = q3.value().plus(multipliedIQR);

        return new Quartiles<>(
                q1.value(),
                q2.value(),
                q3.value(),
                iqr,
                lowerFence,
                upperFence
        );
    }

    public static boolean isBetweenDurations(Duration start, Duration end, Duration d) {
        return d.compareTo(start) >= 0 && d.compareTo(end) <= 0;
    }

    public static Duration median(List<Duration> durations) {
        if (durations.isEmpty()) {
            return null;
        }

        final List<Duration> sortedDurations = durations.stream().sorted().toList();

        final int totalCount = sortedDurations.size();
        final int middle = totalCount / 2;

        if (totalCount % 2 == 1) {
            return sortedDurations.get(middle);
        } else {
            return sortedDurations.get(middle - 1).plus(sortedDurations.get(middle))
                                                    .dividedBy(2);
        }
    }

    public static Duration min(Duration d1, Duration d2) {
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return d1.compareTo(d2) < 0 ? d1 : d2;
    }

    public static Duration parseTime(String s) {
        Matcher m = HOURS_TIME.matcher(s.trim());
        if (m.matches()) {
            final String hours = m.group(1);
            final String minutes = m.group(2);
            final String seconds = m.group(3);
            final String milliseconds = m.group(4);
            return Duration.ofMillis(
                    Short.parseShort(milliseconds) +
                    (1_000 * Long.parseLong(seconds)) +
                    (60_000 * Long.parseLong(minutes)) +
                    (3_600_000 * Long.parseLong(hours))
            );
        }
        m = MINUTES_TIME.matcher(s);
        if (m.matches()) {
            final String minutes = m.group(1);
            final String seconds = m.group(2);
            final String milliseconds = m.group(3);
            return Duration.ofMillis(
                    Short.parseShort(milliseconds) +
                    (1_000 * Long.parseLong(seconds)) +
                    (60_000 * Long.parseLong(minutes))
            );
        }
        m = SECONDS_TIME.matcher(s);
        if (m.matches()) {
            final String seconds = m.group(1);
            final String milliseconds = m.group(2);
            return Duration.ofMillis(
                    Short.parseShort(milliseconds) +
                    (1_000 * Long.parseLong(seconds))
            );
        }
        throw new IllegalArgumentException("Unsupported time format: " + s);
    }

    private static Median<Duration> medianSorted(List<Duration> sortedDurations) {
        if (sortedDurations.isEmpty()) {
            return null;
        }

        final int totalCount = sortedDurations.size();
        final int middle = totalCount / 2;

        if (totalCount % 2 == 1) {
            return new Median<>(middle, sortedDurations.get(middle));
        } else {
            final Duration first = sortedDurations.get(middle - 1);
            final Duration second = sortedDurations.get(middle);
            final Duration median = first.plus(second).dividedBy(2);
            return new Median<>(null, median);
        }
    }
}
