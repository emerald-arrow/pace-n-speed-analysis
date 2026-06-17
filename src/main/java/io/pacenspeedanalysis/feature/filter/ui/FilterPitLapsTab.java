package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.FilteringResult;
import io.pacenspeedanalysis.feature.filter.PitLapsFilter;

@UIScope
public final class FilterPitLapsTab extends FilterTab {

    private final Checkbox checkboxPitIn = new Checkbox();
    private final Checkbox checkboxPitOut = new Checkbox();

    public FilterPitLapsTab(SessionState state) {
        super(state);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        checkboxPitIn.setLabel(getTranslation("tab.pitLaps.checkbox.pitIn.label"));
        checkboxPitIn.setValue(false);

        checkboxPitOut.setLabel(getTranslation("tab.pitLaps.checkbox.pitOut.label"));
        checkboxPitOut.setValue(false);

        final Runnable updateButton = () -> filterButton.setEnabled(
                checkboxPitIn.getValue() ||
                checkboxPitOut.getValue()
        );

        checkboxPitIn.addValueChangeListener(_ -> updateButton.run());

        checkboxPitOut.addValueChangeListener(_ -> updateButton.run());

        filterButton.addClickListener(_ -> {
            final PitLapsFilter filter = new PitLapsFilter(
                    checkboxPitIn.getValue() && checkboxPitIn.isEnabled(),
                    checkboxPitOut.getValue() && checkboxPitOut.isEnabled()
            );
            final FilteringResult result = filter.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);

            if (checkboxPitIn.getValue()) {
                checkboxPitIn.setEnabled(false);
            }
            if (checkboxPitOut.getValue()) {
                checkboxPitOut.setEnabled(false);
            }
            if (checkboxPitIn.getValue() && checkboxPitOut.getValue()) {
                filterButton.setEnabled(false);
            }
        });

        add(checkboxPitIn, checkboxPitOut, filterButton);
    }
}
