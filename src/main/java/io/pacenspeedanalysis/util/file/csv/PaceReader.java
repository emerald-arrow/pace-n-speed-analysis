package io.pacenspeedanalysis.util.file.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.pacenspeedanalysis.exception.CsvFormatException;
import io.pacenspeedanalysis.model.row.PaceRow;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public final class PaceReader extends CsvReader<PaceRow> {

    private static final String[] REQUIRED_HEADERS = {
            "NUMBER",
            "LAP_NUMBER",
            "CROSSING_FINISH_LINE_IN_PIT",
            "DRIVER_NAME",
            "CLASS",
            "TEAM",
            "MANUFACTURER",
            "FLAG_AT_FL",
            "ELAPSED",
            "LAP_TIME",
            "S1",
            "S2",
            "S3"
    };

    @Override
    public List<PaceRow> read(InputStream stream) throws IOException {
        final List<PaceRow> paceData = new ArrayList<>();

        try (CSVReader reader = getReader(stream)) {
            String[] line = reader.readNext();

            List<String> headersLine = Arrays.stream(line)
                                                .map(h -> h.trim().toUpperCase())
                                                .toList();

            if (!hasColumns(headersLine, REQUIRED_HEADERS)) {
                throw new IllegalArgumentException(
                        "Provided file does not have some of required columns which are: " +
                                Arrays.toString(REQUIRED_HEADERS)
                );
            }

            final int carNumberPos = headersLine.indexOf(REQUIRED_HEADERS[0]);
            final int lapNumberPos = headersLine.indexOf(REQUIRED_HEADERS[1]);
            final int pitLanePos = headersLine.indexOf(REQUIRED_HEADERS[2]);
            final int driverPos = headersLine.indexOf(REQUIRED_HEADERS[3]);
            final int categoryPos = headersLine.indexOf(REQUIRED_HEADERS[4]);
            final int teamPos = headersLine.indexOf(REQUIRED_HEADERS[5]);
            final int manufacturerPos = headersLine.indexOf(REQUIRED_HEADERS[6]);
            final int flagAtFinishPos = headersLine.indexOf(REQUIRED_HEADERS[7]);
            final int elapsedPos = headersLine.indexOf(REQUIRED_HEADERS[8]);
            final int lapTimePos = headersLine.indexOf(REQUIRED_HEADERS[9]);
            final int sectorOnePos = headersLine.indexOf(REQUIRED_HEADERS[10]);
            final int sectorTwoPos = headersLine.indexOf(REQUIRED_HEADERS[11]);
            final int sectorThreePos = headersLine.indexOf(REQUIRED_HEADERS[12]);

            while((line = reader.readNext()) != null) {
                final String carNumber = line[carNumberPos];
                final String lapNumber = line[lapNumberPos];
                final String pitLane = line[pitLanePos];
                final String driver = line[driverPos];
                final String category = line[categoryPos];
                final String team = line[teamPos];
                final String manufacturer = line[manufacturerPos];
                final String flagAtFinish = line[flagAtFinishPos];
                final String elapsed = line[elapsedPos];
                final String lapTime = line[lapTimePos];
                final String sectorOne = line[sectorOnePos];
                final String sectorTwo = line[sectorTwoPos];
                final String sectorThree = line[sectorThreePos];

                paceData.add(
                        new PaceRow.Builder().withCarNumber(carNumber)
                                                .withLapNumber(lapNumber)
                                                .withWentThroughPitLane(pitLane)
                                                .withDriver(driver)
                                                .withCategory(category)
                                                .withTeam(team)
                                                .withManufacturer(manufacturer)
                                                .withFlagAtFinishLine(flagAtFinish)
                                                .withElapsed(elapsed)
                                                .withLapTime(lapTime)
                                                .withSector1(sectorOne)
                                                .withSector2(sectorTwo)
                                                .withSector3(sectorThree)
                                                .build()
                );
            }
        } catch (CsvValidationException e) {
            throw new CsvFormatException("Invalid CSV format", e);
        }

        return Collections.unmodifiableList(paceData);
    }
}
