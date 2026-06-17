package io.pacenspeedanalysis.model.analysis.outliers;

import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.analysis.Quartiles;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.data.SpeedDataRecord;
import io.pacenspeedanalysis.model.lap.SpeedLap;
import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.*;

public final class SpeedOutlierFilter implements OutlierFilterStrategy {

    @Override
    public Set<LapKey> filter(List<DataRecord> data) {
        final Set<LapKey> keys = new HashSet<>();

        for (DataRecord record : data) {
            final SpeedDataRecord speedRecord = (SpeedDataRecord) record;

            final List<BigDecimal> validSpeeds = new ArrayList<>();

            for (SpeedLap lap : speedRecord.laps()) {
                if (lap.getTopSpeed().compareTo(BigDecimal.ZERO) == 0) {
                    keys.add(new LapKey(record.name(), lap.getLapNumber()));
                    continue;
                }

                validSpeeds.add(lap.getTopSpeed());
            }

            if (validSpeeds.isEmpty()) {
                continue;
            }

            validSpeeds.sort(Comparator.naturalOrder());

            final Quartiles<BigDecimal> quartiles = BigDecimalUtils.inclusiveQuartilesSorted(validSpeeds);

            if (quartiles == null) {
                for (SpeedLap lap : speedRecord.laps()) {
                    keys.add(new LapKey(record.name(), lap.getLapNumber()));
                }

                continue;
            }

            for (SpeedLap lap : speedRecord.laps()) {
                if (lap.getTopSpeed().compareTo(quartiles.lowerFence()) < 0 ||
                    lap.getTopSpeed().compareTo(quartiles.upperFence()) > 0) {
                    keys.add(new LapKey(record.name(), lap.getLapNumber()));
                }
            }
        }

        return Collections.unmodifiableSet(keys);
    }
}
