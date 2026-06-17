package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.FilteringResult;
import io.pacenspeedanalysis.feature.filter.LapsInsidePeriodFilter;

import java.time.Duration;
import java.time.LocalTime;

@UIScope
public final class FilterUnwantedPeriodsTab extends FilterPeriodTab {

    public FilterUnwantedPeriodsTab(SessionState state) {
        super(state);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        text.setText(getTranslation("tab.unwantedPeriods.text"));

        filterButton.addClickListener(_ -> {
            final LocalTime start = startTimePicker.getValue();
            final Duration periodStart = Duration.ofHours(start.getHour())
                                                    .plusMinutes(start.getMinute())
                                                    .plusSeconds(start.getSecond())
                                                    .plusNanos(start.getNano());

            final LocalTime end = endTimePicker.getValue();
            final Duration periodEnd = Duration.ofHours(end.getHour())
                                                .plusMinutes(end.getMinute())
                                                .plusSeconds(end.getSecond())
                                                .plusNanos(end.getNano());

            final LapsInsidePeriodFilter filterNew = new LapsInsidePeriodFilter(periodStart, periodEnd);
            final FilteringResult result = filterNew.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);
        });
    }
}
