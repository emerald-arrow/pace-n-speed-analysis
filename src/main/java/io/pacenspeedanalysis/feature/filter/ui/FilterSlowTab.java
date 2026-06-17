package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import io.pacenspeedanalysis.config.SessionState;

public abstract class FilterSlowTab extends FilterTab {

    protected final VerticalLayout stretchContainer = new VerticalLayout();
    protected final IntegerField integerField = new IntegerField();

    public FilterSlowTab(SessionState state) {
        super(state);
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        stretchContainer.setWidth("600px");
        stretchContainer.setAlignItems(Alignment.STRETCH);
        stretchContainer.setSpacing(true);
        stretchContainer.setPadding(true);
    }
}
