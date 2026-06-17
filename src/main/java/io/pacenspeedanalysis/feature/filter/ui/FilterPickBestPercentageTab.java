package io.pacenspeedanalysis.feature.filter.ui;

import io.pacenspeedanalysis.config.SessionState;

public abstract class FilterPickBestPercentageTab extends FilterPickBestTab {

    private static final int MAX_FILTERING_VALUE = 99;

    public FilterPickBestPercentageTab(SessionState state) {
        super(state, MAX_FILTERING_VALUE);
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();
        buildIntegerField();
    }

    @Override
    protected void buildIntegerField() {
        super.buildIntegerField();
        integerField.setLabel(getTranslation("tab.pickBestPercentage.integerField.label"));
        integerField.setHelperText(getTranslation("tab.pickBestPercentage.integerField.helper"));
    }
}
