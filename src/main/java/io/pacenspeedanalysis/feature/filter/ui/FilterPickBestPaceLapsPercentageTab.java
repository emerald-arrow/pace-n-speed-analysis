package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.BestPaceLapsPercentageFilter;
import io.pacenspeedanalysis.feature.filter.FilteringResult;

@UIScope
public final class FilterPickBestPaceLapsPercentageTab extends FilterPickBestPercentageTab {

    public FilterPickBestPaceLapsPercentageTab(SessionState state) {
        super(state);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        checkbox.setLabel(getTranslation("tab.pickBestPacePercentage.checkbox.label"));

        filterButton.addClickListener(_ -> {
            final BestPaceLapsPercentageFilter filter = new BestPaceLapsPercentageFilter(integerField.getValue());
            final FilteringResult result = filter.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);
        });
    }
}
