package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.start.ui.StartView;

public abstract class FilterView extends VerticalLayout implements BeforeEnterObserver {

    protected final TabSheet tabSheet = new TabSheet();
    protected final SessionState state;

    public FilterView(SessionState state) {
        this.state = state;

        setWidthFull();
        setHeightFull();
        setAlignItems(Alignment.STRETCH);
        setSpacing(true);
        tabSheet.setHeightFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!state.hasData()) {
            Notification.show(getTranslation("filtering.missingData"));
            event.forwardTo(StartView.class);
        }
    }

    protected abstract void buildLayout();

    protected void showView() {
        tabSheet.setSelectedIndex(0);
        add(tabSheet);
    }
}
