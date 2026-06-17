package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import io.pacenspeedanalysis.config.SessionState;

import java.time.Duration;
import java.time.LocalTime;

public abstract class FilterPeriodTab extends FilterTab {

    private static final LocalTime DEFAULT_START = LocalTime.of(0, 0, 0);
    private static final LocalTime DEFAULT_END = LocalTime.of(1, 0, 0);

    protected final TimePicker startTimePicker = createTimePicker(DEFAULT_START);
    protected final TimePicker endTimePicker = createTimePicker(DEFAULT_END);
    protected final HorizontalLayout wrapper = new HorizontalLayout();
    protected final Text text = new Text(null);

    public FilterPeriodTab(SessionState state) {
        super(state);
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        startTimePicker.setLabel(getTranslation("tab.period.startPicker.label"));
        endTimePicker.setLabel(getTranslation("tab.period.endPicker.label"));

        wrapper.setAlignItems(Alignment.CENTER);
        wrapper.setSpacing(true);
        wrapper.setPadding(true);

        startTimePicker.addValueChangeListener(e -> {
            if (e.getValue().isAfter(endTimePicker.getValue())) {
                endTimePicker.setValue(e.getValue().plusHours(1));
            }
        });

        endTimePicker.addValueChangeListener(e -> {
            if (e.getValue().isBefore(startTimePicker.getValue())) {
                startTimePicker.setValue(e.getValue().minusHours(1));
            }
        });

        filterButton.setEnabled(true);

        wrapper.add(startTimePicker, endTimePicker);

        add(text, wrapper, filterButton);
    }

    private TimePicker createTimePicker(LocalTime defaultValue) {
        TimePicker timePicker = new TimePicker();
        timePicker.setStep(Duration.ofSeconds(1));
        timePicker.setValue(defaultValue);

        return timePicker;
    }
}
