package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.FilteringResult;
import io.pacenspeedanalysis.feature.filter.LapsOutsidePeriodFilter;

import java.time.Duration;
import java.time.LocalTime;

@UIScope
public final class FilterPickPeriodTab extends FilterPeriodTab {

    public FilterPickPeriodTab(SessionState state) {
        super(state);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        text.setText(getTranslation("tab.pickPeriod.text"));

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

            final LapsOutsidePeriodFilter filter = new LapsOutsidePeriodFilter(periodStart, periodEnd);
            final FilteringResult result = filter.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);

            filterButton.setEnabled(false);
        });
    }
}
