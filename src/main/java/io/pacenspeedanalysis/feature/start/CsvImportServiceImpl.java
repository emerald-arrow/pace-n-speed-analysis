package io.pacenspeedanalysis.feature.start;

import io.pacenspeedanalysis.model.*;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.row.PaceRow;
import io.pacenspeedanalysis.model.row.SpeedRow;
import io.pacenspeedanalysis.util.file.csv.PaceReader;
import io.pacenspeedanalysis.util.file.csv.SpeedReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class CsvImportServiceImpl implements CsvImportService {

    private final PaceReader paceReader;
    private final SpeedReader speedReader;
    private final DataRecordGenerationService generationService;

    public CsvImportServiceImpl(PaceReader paceReader, SpeedReader speedReader,
                                DataRecordGenerationService generationService) {
        this.paceReader = paceReader;
        this.speedReader = speedReader;
        this.generationService = generationService;
    }

    @Override
    public List<DataRecord> importCsv(InputStream is, EAnalysisType type, EAggregationType aggregationType)
            throws IOException {
        return switch (type) {
            case PACE -> {
                final List<PaceRow> rows = readPaceRows(is);
                yield generationService.generatePaceData(rows, aggregationType);
            }
            case AVG_SPEED -> {
                final List<SpeedRow> rows = readSpeedRows(is);
                yield generationService.generateSpeedData(rows, aggregationType);
            }
        };
    }

    private List<PaceRow> readPaceRows(InputStream is) throws IOException {
        return paceReader.read(is);
    }

    private List<SpeedRow> readSpeedRows(InputStream is) throws IOException {
        return speedReader.read(is);
    }
}
