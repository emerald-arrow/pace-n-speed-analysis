package io.pacenspeedanalysis.util.numeric;

import io.pacenspeedanalysis.util.string.StringUtils;

public class ShortUtils {

    public static void requireParsable(String s, String name) {
        StringUtils.requireNotBlank(s, "name");

        try {
            Short.parseShort(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " must be a number");
        }
    }
}
