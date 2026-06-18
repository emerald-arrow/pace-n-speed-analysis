package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.FilteringResult;
import io.pacenspeedanalysis.feature.filter.FirstLapFilter;

@UIScope
public final class FilterFirstLapTab extends FilterTab {

    private final Checkbox checkbox = new Checkbox();

    public FilterFirstLapTab(SessionState state) {
        super(state);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        checkbox.setLabel(getTranslation("tab.firstLap.checkbox.label"));

        checkbox.addValueChangeListener(e -> {
            filterButton.setEnabled(e.getValue());
        });

        filterButton.addClickListener(_ -> {
            final FirstLapFilter filter = new FirstLapFilter();
            final FilteringResult result = filter.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);

            checkbox.setEnabled(false);
            filterButton.setEnabled(false);
        });

        add(checkbox, filterButton);
    }
}
