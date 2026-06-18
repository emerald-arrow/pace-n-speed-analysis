package io.pacenspeedanalysis.model.analysis.outliers;

import io.pacenspeedanalysis.model.analysis.Quartiles;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.data.SpeedDataRecord;
import io.pacenspeedanalysis.model.lap.SpeedLap;
import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.*;

public final class SpeedOutlierFilter implements OutlierFilterStrategy {

    @Override
    public Set<UUID> filter(List<DataRecord> data) {
        final Set<UUID> laps = new HashSet<>();

        for (DataRecord record : data) {
            final SpeedDataRecord speedRecord = (SpeedDataRecord) record;

            final List<BigDecimal> validSpeeds = new ArrayList<>();

            for (SpeedLap lap : speedRecord.laps()) {
                if (lap.getTopSpeed().compareTo(BigDecimal.ZERO) == 0) {
                    laps.add(lap.getId());
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
                    laps.add(lap.getId());
                }

                continue;
            }

            for (SpeedLap lap : speedRecord.laps()) {
                if (lap.getTopSpeed().compareTo(quartiles.lowerFence()) < 0 ||
                    lap.getTopSpeed().compareTo(quartiles.upperFence()) > 0) {
                    laps.add(lap.getId());
                }
            }
        }

        return Collections.unmodifiableSet(laps);
    }
}
