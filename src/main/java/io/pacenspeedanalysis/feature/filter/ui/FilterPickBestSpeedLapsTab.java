package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.BestSpeedLapsFilter;
import io.pacenspeedanalysis.feature.filter.FilteringResult;

@UIScope
public final class FilterPickBestSpeedLapsTab extends FilterPickBestTab {

    private static final int MAX_FILTERING_VALUE = Short.MAX_VALUE;

    public FilterPickBestSpeedLapsTab(SessionState state) {
        super(state, MAX_FILTERING_VALUE);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        checkbox.setLabel(getTranslation("tab.pickBestSpeed.checkbox.label"));

        buildIntegerField();

        filterButton.addClickListener(_ -> {
            final BestSpeedLapsFilter filter = new BestSpeedLapsFilter(integerField.getValue());
            final FilteringResult result = filter.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);
        });
    }

    @Override
    protected void buildIntegerField() {
        super.buildIntegerField();
        integerField.setLabel(getTranslation("tab.pickBestSpeed.integerField.label"));
        integerField.setHelperText(getTranslation("tab.pickBestSpeed.integerField.helper"));
    }
}
