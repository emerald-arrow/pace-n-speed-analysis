package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.FilteringResult;
import io.pacenspeedanalysis.feature.filter.SlowPaceLapsFilter;

@UIScope
public final class FilterSlowPaceLapsTab extends FilterSlowTab {

    private static final int MIN_FILTERING_VALUE = 1;
    private static final int MAX_FILTERING_VALUE = 300;

    private final Checkbox lapsCheckbox = new Checkbox();
    private final Checkbox sectorsCheckbox = new Checkbox();

    public FilterSlowPaceLapsTab(SessionState state) {
        super(state);

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        lapsCheckbox.setLabel(getTranslation("tab.slowPace.checkbox.laps.label"));

        buildIntegerField();

        sectorsCheckbox.setLabel(getTranslation("tab.slowPace.checkbox.sectors.label"));
        sectorsCheckbox.setEnabled(false);

        filterButton.getStyle().set("align-self", "center");
        filterButton.setWidth(null);

        final Runnable updateButton = () -> filterButton.setEnabled(
                ((lapsCheckbox.getValue() && lapsCheckbox.isEnabled()) || sectorsCheckbox.getValue()) &&
                (integerField.getValue() != null && !integerField.isInvalid())
        );

        lapsCheckbox.addValueChangeListener(e -> {
            final boolean enabled = e.getValue();
            integerField.setEnabled(enabled);
            sectorsCheckbox.setEnabled(enabled);
        });

        integerField.addValueChangeListener(_ -> {
            updateButton.run();
        });

        sectorsCheckbox.addValueChangeListener(_ -> {
            updateButton.run();
        });

        filterButton.addClickListener(_ -> {
            final SlowPaceLapsFilter filterV2 = new SlowPaceLapsFilter(
                    integerField.getValue().shortValue(),
                    sectorsCheckbox.getValue(),
                    state.getAnalysisContext()
            );
            final FilteringResult result = filterV2.apply(state.getData(), state.getAllHiddenLaps());

            notifyFilteringResult(result);

            lapsCheckbox.setEnabled(false);
            if (sectorsCheckbox.getValue()) {
                sectorsCheckbox.setEnabled(false);
            }

            filterButton.setEnabled(false);

            if (!lapsCheckbox.isEnabled() && !sectorsCheckbox.isEnabled()) {
                integerField.setEnabled(false);
            }
        });

        stretchContainer.add(lapsCheckbox, integerField, sectorsCheckbox, filterButton);
        add(stretchContainer);
    }

    private void buildIntegerField() {
        integerField.setLabel(getTranslation("tab.slowPace.integerField.label"));
        integerField.setHelperText(getTranslation("tab.slowPace.integerField.helper"));
        integerField.setValueChangeMode(ValueChangeMode.EAGER);
        integerField.setMin(MIN_FILTERING_VALUE);
        integerField.setMax(MAX_FILTERING_VALUE);
        integerField.setClearButtonVisible(true);
        integerField.setAutoselect(true);
        integerField.setRequired(true);
        integerField.setRequiredIndicatorVisible(true);
        integerField.setEnabled(false);

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
