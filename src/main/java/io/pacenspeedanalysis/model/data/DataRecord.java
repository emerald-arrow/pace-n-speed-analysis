package io.pacenspeedanalysis.model.data;

import io.pacenspeedanalysis.model.lap.Lap;

import java.util.List;

public sealed interface DataRecord permits PaceDataRecord, SpeedDataRecord {

    String name();

    String category();

    List<? extends Lap> laps();
}
