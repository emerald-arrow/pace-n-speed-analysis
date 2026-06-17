package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.filter.EFilteringType;
import io.pacenspeedanalysis.feature.filter.FilteringResult;

public abstract class FilterTab extends VerticalLayout {

    protected final SessionState state;
    protected final Button filterButton = new Button();

    public FilterTab(SessionState state) {
        this.state = state;

        setWidthFull();
        setHeightFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);
        setPadding(true);
    }

    protected void buildLayout() {
        filterButton.setText(getTranslation("tab.shared.button.filter"));
        filterButton.setEnabled(false);
    }

    private void notifyLapsAlreadyHidden() {
        Notification.show(getTranslation("tab.shared.notification.lapsAlreadyHidden"));
    }

    private void notifyNoLapsToHide() {
        Notification.show(getTranslation("tab.shared.notification.noLaps"));
    }

    private void notifyHiddenLaps(int numberOfLaps) {
        Notification.show(getTranslation("tab.shared.notification.hiddenLaps", numberOfLaps));
    }

    private void notifySomeHiddenLaps(int newlyHiddenLaps, int alreadyHiddenLaps) {
        Notification.show(
                getTranslation(
                        "tab.shared.notification.newlyAndAlreadyHidden",
                        newlyHiddenLaps,
                        alreadyHiddenLaps
                )
        );
    }

    protected void notifyFilteringResult(FilteringResult result) {
        if (result.type() == EFilteringType.ALREADY_DELETED) {
            notifyLapsAlreadyHidden();
        } else if (result.type() == EFilteringType.NOTHING_TO_DELETE) {
            notifyNoLapsToHide();
        } else {
            state.addHiddenLaps(result.hiddenLaps());

            if (result.alreadyHidden() > 0) {
                notifySomeHiddenLaps(result.newlyHidden(), result.alreadyHidden());
            } else {
                notifyHiddenLaps(result.newlyHidden());
            }
        }
    }
}
