package io.pacenspeedanalysis.model.analysis.export;

import java.util.List;
import java.util.function.Function;

public abstract class ExportR<T> extends Export<T> {

    protected ExportR(String nameColumn, List<String> names, String averageColumn, List<T> averages, String medianColumn,
                   List<T> medians, String stDevColumn, List<T> stDevs, String lapsColumn, List<Short> laps,
                   String totalLapsColumn, List<Short> totalLaps) {
        super(
                nameColumn,
                names,
                averageColumn,
                averages,
                medianColumn,
                medians,
                stDevColumn,
                stDevs,
                lapsColumn,
                laps,
                totalLapsColumn,
                totalLaps
        );
    }

    @Override
    public String exportAnalysis() {
        StringBuilder sb = new StringBuilder();

        sb.append(disclaimer());

        sb.append("analysis <- data.frame(\n");

        sb.append(extract(nameColumn, names, s -> String.format("\"%s\"", s)));
        sb.append(",\n");

        sb.append(averagesData());
        sb.append(",\n");

        sb.append(mediansData());
        sb.append(",\n");

        sb.append(stDevsData());
        sb.append(",\n");

        sb.append(extract(lapsColumn, laps, Object::toString));
        sb.append(",\n");

        sb.append(extract(totalLapsColumn, totalLaps, Object::toString));
        sb.append("\n)");

        return sb.toString();
    }

    protected <X> String extract(String columnName, List<X> values, Function<X, String> mapper) {
        StringBuilder sb = new StringBuilder();

        sb.append("\"").append(columnName).append("\"").append(" = c(");
        for (int i = 0; i < values.size(); i++) {
            sb.append(mapper.apply(values.get(i)));
            if (i + 1 < values.size()) {
                sb.append(",");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    protected String extractElements(String columnName, List<T> values, Function<T, String> mapper) {
        return extract(columnName, values, mapper);
    }

    protected abstract String disclaimer();

    protected abstract String averagesData();

    protected abstract String mediansData();

    protected abstract String stDevsData();
}
