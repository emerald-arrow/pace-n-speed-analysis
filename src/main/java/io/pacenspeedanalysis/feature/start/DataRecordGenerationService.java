package io.pacenspeedanalysis.feature.start;

import io.pacenspeedanalysis.model.*;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.data.PaceDataRecord;
import io.pacenspeedanalysis.model.data.SpeedDataRecord;
import io.pacenspeedanalysis.model.lap.PaceLap;
import io.pacenspeedanalysis.model.lap.SpeedLap;
import io.pacenspeedanalysis.model.row.Row;
import io.pacenspeedanalysis.model.row.PaceRow;
import io.pacenspeedanalysis.model.row.SpeedRow;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataRecordGenerationService {

    public List<DataRecord> generatePaceData(List<PaceRow> rows, EAggregationType aggregationType) {
        final Map<Details, List<PaceLap>> dataLaps = new HashMap<>();

        for (PaceRow row : rows) {
            final Details key = new Details(
                    processName(row, aggregationType),
                    row.getCategory()
            );

            final PaceLap lap = new PaceLap.Builder().withLapNumber(row.getLapNumber())
                                                        .withWentThroughPitLane(row.getWentThroughPitLane())
                                                        .withFlagAtFinishLine(row.getFlagAtFinishLine())
                                                        .withElapsed(row.getElapsed())
                                                        .withLapTime(row.getLapTime())
                                                        .withSector1(row.getSectorOne())
                                                        .withSector2(row.getSectorTwo())
                                                        .withSector3(row.getSectorThree())
                                                        .build();

            dataLaps.computeIfAbsent(key, k -> new ArrayList<>()).add(lap);
        }

        return dataLaps.entrySet()
                        .stream()
                        .<DataRecord>map(d -> new PaceDataRecord(
                                d.getKey().name(),
                                d.getKey().category(),
                                d.getValue(),
                                null
                        ))
                        .toList();
    }

    public List<DataRecord> generateSpeedData(List<SpeedRow> rows, EAggregationType aggregationType) {
        final Map<Details, List<SpeedLap>> dataLaps = new HashMap<>();

        for (SpeedRow row : rows) {
            final Details key = new Details(
                    processName(row, aggregationType),
                    row.getCategory()
            );

            final SpeedLap lap = new SpeedLap.Builder().withLapNumber(row.getLapNumber())
                                                        .withWentThroughPitLane(row.getWentThroughPitLane())
                                                        .withFlagAtFinishLine(row.getFlagAtFinishLine())
                                                        .withElapsed(row.getElapsed())
                                                        .withTopSpeed(row.getTopSpeed())
                                                        .build();

            dataLaps.computeIfAbsent(key, k -> new ArrayList<>()).add(lap);
        }

        return dataLaps.entrySet()
                        .stream()
                        .<DataRecord>map(d -> new SpeedDataRecord(
                                d.getKey().name(),
                                d.getKey().category(),
                                d.getValue(),
                                null
                        ))
                        .toList();
    }

    private String processName(Row row, EAggregationType aggregationType) {
        return switch (aggregationType) {
            case TEAM -> row.getFullTeamName();
            case DRIVER -> row.getDriver();
            case MANUFACTURER -> row.getManufacturer();
        };
    }

    private record Details(String name, String category) { }
}
