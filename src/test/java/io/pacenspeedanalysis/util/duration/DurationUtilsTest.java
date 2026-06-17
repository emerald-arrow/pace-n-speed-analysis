package io.pacenspeedanalysis.util.duration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DurationUtilsTest {
    private final Duration START = Duration.ofHours(1);
    private final Duration END = Duration.ofHours(2);

    @ParameterizedTest
    @ValueSource(strings = {"28.675", "1:33.684", "1:00:39.365", "24:01:14.397"})
    void parseTimeSuccess(String s) {
        assertInstanceOf(Duration.class, DurationUtils.parseTime(s));
    }

    @ParameterizedTest
    @ValueSource(strings = {"78.675", "1:63.684", "1:60:39.365", "24:01:74.397"})
    void parseTimeFail(String s) {
        assertThrows(IllegalArgumentException.class, () -> DurationUtils.parseTime(s));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1:00:39.365", "1:02:14.122", "1:05:25.726", "1:07:01.048", "1:08:34.986"})
    void isBetweenDurationsTrue(String s) {
        assertTrue(DurationUtils.isBetweenDurations(START, END, DurationUtils.parseTime(s)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2:00:39.365", "2:02:14.122", "2:05:25.726", "2:07:01.048", "2:08:34.986"})
    void isBetweenDurationsFalse(String s) {
        assertFalse(DurationUtils.isBetweenDurations(START, END, DurationUtils.parseTime(s)));
    }
}