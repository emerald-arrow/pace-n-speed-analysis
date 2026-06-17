package io.pacenspeedanalysis.model.analysis.export;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public abstract class Export<T> {

    protected final String nameColumn;
    protected final List<String> names;
    protected final String averageColumn;
    protected final List<T> averages;
    protected final String medianColumn;
    protected final List<T> medians;
    protected final String stDevColumn;
    protected final List<T> stDevs;
    protected final String lapsColumn;
    protected final List<Short> laps;
    protected final String totalLapsColumn;
    protected final List<Short> totalLaps;

    public Export(String nameColumn, List<String> names, String averageColumn, List<T> averages, String medianColumn,
                  List<T> medians, String stDevColumn, List<T> stDevs, String lapsColumn, List<Short> laps,
                  String totalLapsColumn, List<Short> totalLaps) {
        requireNonBlank(nameColumn, "nameColumn");
        requireNonBlank(averageColumn, "averageColumn");
        requireNonBlank(medianColumn, "medianColumn");
        requireNonBlank(stDevColumn, "stDevColumn");
        requireNonBlank(lapsColumn, "lapsColumn");
        requireNonBlank(totalLapsColumn, "totalLapsColumn");

        requireNonEmpty(names, "names");
        requireNonEmpty(averages, "averages");
        requireNonEmpty(medians, "medians");
        requireNonEmpty(stDevs, "stDevs");
        requireNonEmpty(laps, "laps");
        requireNonEmpty(totalLaps, "totalLaps");

        requireEqualListsSize(names, averages, medians, stDevs, laps, totalLaps);

        this.averageColumn = averageColumn;
        this.nameColumn = nameColumn;
        this.names = names;
        this.averages = averages;
        this.medianColumn = medianColumn;
        this.medians = medians;
        this.stDevColumn = stDevColumn;
        this.stDevs = stDevs;
        this.lapsColumn = lapsColumn;
        this.laps = laps;
        this.totalLapsColumn = totalLapsColumn;
        this.totalLaps = totalLaps;
    }

    public abstract String exportAnalysis();

    private static void requireEqualListsSize(List<?> ...lists) {
        if (!Arrays.stream(lists).map(List::size).allMatch(l -> l == lists[0].size())) {
            throw new IllegalArgumentException("Provided lists are not equal sized");
        }
    }

    private static void requireNonBlank(String s, String name) {
        if (StringUtils.isBlank(s)) {
            throw new IllegalArgumentException(name + " must be neither null nor blank");
        }
    }

    private static void requireNonEmpty(List<?> list, String name) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException(name + " must be neither null nor empty");
        }
    }
}
