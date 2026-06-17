package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.BestSpeedLapsPercentageFilter;
import io.pacenspeedanalysis.feature.filter.FilteringResult;

@UIScope
public final class FilterPickBestSpeedLapsPercentageTab extends FilterPickBestPercentageTab {

    public FilterPickBestSpeedLapsPercentageTab(SessionState state) {
        super(state);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        checkbox.setLabel(getTranslation("tab.pickBestSpeedPercentage.checkbox.label"));

        filterButton.addClickListener(_ -> {
            final BestSpeedLapsPercentageFilter filter = new BestSpeedLapsPercentageFilter(integerField.getValue());
            final FilteringResult result = filter.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);
        });
    }
}
