package io.pacenspeedanalysis.feature.analysis;

import io.pacenspeedanalysis.model.analysis.Analysis;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AnalysisComparator {

    private static final Pattern TEAM_NUMBER = Pattern.compile("#(\\d{1,3})");

    public static final Comparator<Analysis> BY_NAME = (a1, a2) -> {
        if (a1 == null && a2 == null) {
            return 0;
        }
        if (a1 == null) {
            return -1;
        }
        if (a2 == null) {
            return 1;
        }

        final String firstName = a1.name();
        final String secondName = a2.name();

        if (firstName.startsWith("#") && secondName.startsWith("#")) {
            final Matcher m1 = TEAM_NUMBER.matcher(firstName);
            final Matcher m2 = TEAM_NUMBER.matcher(secondName);
            if (m1.find() && m2.find()) {
                final String n1 = m1.group(1);
                final String n2 = m2.group(1);
                return Integer.compare(Integer.parseInt(n1), Integer.parseInt(n2));
            }
        }

        return String.CASE_INSENSITIVE_ORDER.compare(a1.name(), a2.name());
    };
}
