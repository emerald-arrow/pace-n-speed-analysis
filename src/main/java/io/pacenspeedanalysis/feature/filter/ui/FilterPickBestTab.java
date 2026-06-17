package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.value.ValueChangeMode;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.FilteringResult;

public abstract class FilterPickBestTab extends FilterTab {

    private static final int MIN_FILTERING_VALUE = 1;

    protected final VerticalLayout stretchContainer = new VerticalLayout();
    protected final Checkbox checkbox = new Checkbox();
    protected final IntegerField integerField = new IntegerField();
    private final int maxFilteringValue;

    public FilterPickBestTab(SessionState state, int maxFilteringValue) {
        if (maxFilteringValue <= MIN_FILTERING_VALUE) {
            throw new IllegalArgumentException("maxFilteringValue must be greater than " + MIN_FILTERING_VALUE);
        }

        super(state);
        this.maxFilteringValue = maxFilteringValue;
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        stretchContainer.setWidth("600px");
        stretchContainer.setAlignItems(Alignment.STRETCH);
        stretchContainer.setSpacing(true);
        stretchContainer.setPadding(true);

        filterButton.getStyle().set("align-self", "center");
        filterButton.setWidth(null);

        checkbox.addValueChangeListener(e -> {
            integerField.setEnabled(e.getValue());
        });

        integerField.addValueChangeListener(e -> {
            filterButton.setEnabled(!integerField.isInvalid() && e.getValue() != null);
        });

        stretchContainer.add(checkbox, integerField, filterButton);
        add(stretchContainer);
    }

    protected void buildIntegerField() {
        integerField.setValueChangeMode(ValueChangeMode.EAGER);
        integerField.setMin(MIN_FILTERING_VALUE);
        integerField.setMax(maxFilteringValue);
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
                                getTranslation("common.integerField.error.max", maxFilteringValue)
                        )
        );
    }

    protected void notifyFilteringResult(FilteringResult result) {
        super.notifyFilteringResult(result);

        checkbox.setEnabled(false);
        integerField.setEnabled(false);
        filterButton.setEnabled(false);
    }
}
