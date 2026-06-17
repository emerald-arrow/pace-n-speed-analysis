package io.pacenspeedanalysis.util.string;

public class StringUtils {

    public static void requireNotBlank(String s, String name) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException(name + " must be neither null nor blank");
        }
    }

    public static void requireNotNull(String s, String name) {
        if (s == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
    }
}
