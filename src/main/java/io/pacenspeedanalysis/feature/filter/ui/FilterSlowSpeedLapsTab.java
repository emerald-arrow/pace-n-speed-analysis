package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.FilteringResult;
import io.pacenspeedanalysis.feature.filter.SlowSpeedLapsFilter;

@UIScope
public final class FilterSlowSpeedLapsTab extends FilterSlowTab {

    private static final int MIN_FILTERING_VALUE = 1;
    private static final int MAX_FILTERING_VALUE = 99;

    private final Checkbox checkbox = new Checkbox();

    public FilterSlowSpeedLapsTab(SessionState state) {
        super(state);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        checkbox.setLabel(getTranslation("tab.slowSpeed.checkbox.label"));

        buildIntegerField();
        integerField.setEnabled(false);

        checkbox.addValueChangeListener(e -> {
            integerField.setEnabled(e.getValue());
        });

        integerField.addValueChangeListener(_ -> {
            filterButton.setEnabled(!integerField.isInvalid() && integerField.getValue() != null);
        });

        filterButton.addClickListener(_ -> {
            final SlowSpeedLapsFilter filter = new SlowSpeedLapsFilter(
                    integerField.getValue().shortValue(),
                    state.getAnalysisContext()
            );
            final FilteringResult result = filter.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);

            checkbox.setEnabled(false);
            integerField.setEnabled(false);
            filterButton.setEnabled(false);
        });

        stretchContainer.add(checkbox, integerField, filterButton);
        add(stretchContainer);
    }

    private void buildIntegerField() {
        integerField.setLabel(getTranslation("tab.slowSpeed.integerField.label"));
        integerField.setHelperText(getTranslation("tab.slowSpeed.integerField.helper"));
        integerField.setValueChangeMode(ValueChangeMode.EAGER);
        integerField.setMin(MIN_FILTERING_VALUE);
        integerField.setMax(MAX_FILTERING_VALUE);
        integerField.setClearButtonVisible(true);
        integerField.setAutoselect(true);
        integerField.setRequired(true);
        integerField.setRequiredIndicatorVisible(true);

        integerField.setI18n(
                new IntegerField.IntegerFieldI18n()
                        .setRequiredErrorMessage(
                                getTranslation("common.integerField.error.required")
                        )
                        .setBadInputErrorMessage(getTranslation("common.integerField.error.badFormat"))
                        .setMinErrorMessage(
                                getTranslation("common.integerField.error.min", MIN_FILTERING_VALUE)
                        )
                        .setMaxErrorMessage(
                                getTranslation("common.integerField.error.max", MAX_FILTERING_VALUE)
                        )
        );
    }
}
