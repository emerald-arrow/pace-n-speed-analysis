package io.pacenspeedanalysis.feature.analysis;

import io.pacenspeedanalysis.model.EAnalysisType;
import io.pacenspeedanalysis.model.EExportFormat;
import io.pacenspeedanalysis.model.analysis.Analysis;
import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.analysis.PaceAnalysis;
import io.pacenspeedanalysis.model.analysis.SpeedAnalysis;
import io.pacenspeedanalysis.model.analysis.export.*;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.data.PaceDataRecord;
import io.pacenspeedanalysis.model.data.SpeedDataRecord;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalysisService {

    public List<PaceAnalysis> generatePaceAnalysis(List<DataRecord> records, Set<LapKey> hiddenLaps) {
        final List<PaceAnalysis> analysis = new ArrayList<>();

        for (DataRecord record : records) {
            final PaceDataRecord paceRecord = (PaceDataRecord) record;

            final List<Short> filtered = hiddenLaps.stream().filter(
                                        e -> e.entityName().equalsIgnoreCase(record.name())
                                        )
                                        .map(LapKey::lapNumber)
                                        .toList();

            if (filtered.size() == paceRecord.laps().size()) {
                continue;
            }

            analysis.add(new PaceAnalysis(paceRecord, filtered));
        }

        return Collections.unmodifiableList(analysis);
    }

    public List<SpeedAnalysis> generateSpeedAnalysis(List<DataRecord> records, Set<LapKey> hiddenLaps) {
        final List<SpeedAnalysis> analysis = new ArrayList<>();

        for (DataRecord record : records) {
            final SpeedDataRecord speedRecord = (SpeedDataRecord) record;

            final List<Short> filtered = hiddenLaps.stream().filter(
                                    e -> e.entityName().equalsIgnoreCase(record.name())
                                        )
                                        .map(LapKey::lapNumber)
                                        .toList();

            if (filtered.size() == speedRecord.laps().size()) {
                continue;
            }

            analysis.add(new SpeedAnalysis(speedRecord, filtered));
        }

        return Collections.unmodifiableList(analysis);
    }

    public String exportAnalysis(List<? extends Analysis<?>> list, EAnalysisType type, ExportColumns columns,
                                 EExportFormat format, Locale locale) {
        if (list.isEmpty()) {
            return "";
        }

        final ExportData data = new ExportData(
                list.stream().map(Analysis::name).toList(),
                list.stream().map(Analysis::average).toList(),
                list.stream().map(Analysis::median).toList(),
                list.stream().map(Analysis::standardDeviation).toList(),
                list.stream().map(Analysis::laps).toList(),
                list.stream().map(Analysis::totalLaps).toList()
        );

        AnalysisExport<?> export = AnalysisExportFactory.create(type, format, locale)
                                                        .withColumns(columns)
                                                        .withData(data);

        return export.export();
    }
}
