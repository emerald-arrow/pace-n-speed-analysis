package io.pacenspeedanalysis.model.analysis.export;

import java.util.List;

public abstract class ExportCsv<T> extends Export<T> {

    protected static final String COLUMN_SEPARATOR = ";";
    protected static final String DATA_QUOTE = "\"";
    protected static final String END_LINE = "\n";

    protected ExportCsv(String nameColumn, List<String> names, String averageColumn, List<T> averages,
                     String medianColumn, List<T> medians, String stDevColumn, List<T> stDevs, String lapsColumn,
                     List<Short> laps, String totalLapsColumn, List<Short> totalLaps) {
        super(
                nameColumn, names,
                averageColumn, averages,
                medianColumn, medians,
                stDevColumn, stDevs,
                lapsColumn, laps,
                totalLapsColumn, totalLaps
        );
    }

    @Override
    public String exportAnalysis() {
        StringBuilder sb = new StringBuilder();

        sb.append(createHeader());

        for (int i = 0; i < names.size(); i++) {
            sb.append(
                createRow(
                        names.get(i),
                        averages.get(i),
                        medians.get(i),
                        stDevs.get(i),
                        laps.get(i),
                        totalLaps.get(i)
                )
            );
        }

        return sb.toString();
    }

    protected String createData(Object data) {
        final String escaped = data.toString().replace("\"", "\"\"");
        return DATA_QUOTE + escaped + DATA_QUOTE;
    }

    protected String createRow(String name, T average, T median, T stDev, short laps, short totalLaps) {
        return createData(name) + COLUMN_SEPARATOR +
                createData(formatValue(average)) + COLUMN_SEPARATOR +
                createData(formatValue(median)) + COLUMN_SEPARATOR +
                createData(formatValue(stDev)) + COLUMN_SEPARATOR +
                createData(laps) + COLUMN_SEPARATOR +
                createData(totalLaps) + END_LINE;
    }

    protected abstract String formatValue(T value);

    private String createHeader() {
        return createData(nameColumn) + COLUMN_SEPARATOR +
                createData(averageColumn) + COLUMN_SEPARATOR +
                createData(medianColumn) + COLUMN_SEPARATOR +
                createData(stDevColumn) + COLUMN_SEPARATOR +
                createData(lapsColumn) + COLUMN_SEPARATOR +
                createData(totalLapsColumn) + END_LINE;
    }
}
