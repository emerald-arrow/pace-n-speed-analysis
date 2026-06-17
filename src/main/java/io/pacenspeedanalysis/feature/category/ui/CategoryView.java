package io.pacenspeedanalysis.feature.category.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.base.ui.MainLayout;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.category.CategoryService;
import io.pacenspeedanalysis.feature.filter.ui.FilterPaceView;
import io.pacenspeedanalysis.feature.filter.ui.FilterSpeedView;
import io.pacenspeedanalysis.feature.start.ui.StartView;
import io.pacenspeedanalysis.model.EAnalysisType;

import java.util.ArrayList;
import java.util.List;

@UIScope
@Route(value = "category", layout = MainLayout.class)
public final class CategoryView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionState state;
    private final CategoryService categoryService;
    private final List<String> availableCategories = new ArrayList<>();
    private final VerticalLayout elementsLayout = new VerticalLayout();
    private final RadioButtonGroup<String> categoryGroup = new RadioButtonGroup<>();
    private final Button nextButton = new Button();
    private boolean resetting = false;

    public CategoryView(SessionState state, CategoryService categoryService) {
        this.state = state;
        this.categoryService = categoryService;

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);

        elementsLayout.setWidth("600px");
        elementsLayout.setPadding(true);
        elementsLayout.setSpacing(true);
        elementsLayout.setAlignItems(Alignment.STRETCH);
        elementsLayout.getStyle()
                        .set("border-radius", "1em")
                        .set("padding", "1em")
                        .set("box-shadow", "0 1em 2em rgba(0,0,0,0.12)")
                        .set("border", "0.5em solid var(--lumo-contrast-10pct)");

        add(elementsLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!state.hasData() || state.getAnalysisType() == null) {
            Notification.show(getTranslation("category.nullState"));
            event.forwardTo(StartView.class);
            return;
        }

        availableCategories.clear();
        availableCategories.addAll(categoryService.getCategories(state.getData()));

        if (availableCategories.isEmpty()) {
            Notification.show(getTranslation("category.noCategories"));
            event.forwardTo(StartView.class);
            return;
        }

        buildLayout();

        resetting = true;
        categoryGroup.setItems(availableCategories);
        categoryGroup.clear();
        nextButton.setEnabled(false);
        resetting = false;
    }

    private void buildLayout() {
        categoryGroup.setLabel(getTranslation("category.info"));
        categoryGroup.setRequired(true);

        nextButton.setText(getTranslation("common.next"));
        nextButton.getStyle().set("align-self", "center");
        nextButton.setWidth(null);

        categoryGroup.addValueChangeListener(e -> {
            if (resetting) {
                return;
            }
            
            nextButton.setEnabled(e.getValue() != null);
        });

        nextButton.addClickListener(_ -> {
            final String selected = categoryGroup.getValue();
            if (selected == null) {
                Notification.show(getTranslation("category.noSelection"));
                return;
            }

            state.updateData(categoryService.filterOutCategories(state.getData(), selected));
            moveToFiltering(state.getAnalysisType());
        });

        elementsLayout.add(categoryGroup, nextButton);
    }

    private void moveToFiltering(EAnalysisType type) {
        UI ui = UI.getCurrent();

        switch (type) {
            case AVG_SPEED -> ui.navigate(FilterSpeedView.class);
            case PACE -> ui.navigate(FilterPaceView.class);
        }
    }
}
