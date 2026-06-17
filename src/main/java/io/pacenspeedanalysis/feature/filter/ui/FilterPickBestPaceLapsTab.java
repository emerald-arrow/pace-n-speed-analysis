package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.BestPaceLapsFilter;
import io.pacenspeedanalysis.feature.filter.FilteringResult;

@UIScope
public final class FilterPickBestPaceLapsTab extends FilterPickBestTab {

    private static final int MAX_FILTERING_VALUE = Short.MAX_VALUE;

    public FilterPickBestPaceLapsTab(SessionState state) {
        super(state, MAX_FILTERING_VALUE);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        checkbox.setLabel(getTranslation("tab.pickBestPace.checkbox.label"));

        buildIntegerField();

        filterButton.addClickListener(_ -> {
            final BestPaceLapsFilter filter = new BestPaceLapsFilter(integerField.getValue());
            final FilteringResult result = filter.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);
        });
    }

    @Override
    protected void buildIntegerField() {
        super.buildIntegerField();
        integerField.setLabel(getTranslation("tab.pickBestPace.integerField.label"));
        integerField.setHelperText(getTranslation("tab.pickBestPace.integerField.helper"));
    }
}
