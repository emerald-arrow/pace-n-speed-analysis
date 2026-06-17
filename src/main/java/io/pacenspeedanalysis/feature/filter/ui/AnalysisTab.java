package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public final class AnalysisTab extends VerticalLayout {

    private final Text text = new Text(null);
    private final Button analyseButton = new Button();

    private boolean analyseButtonEnabled = true;

    public AnalysisTab(Class<? extends Component> targetView) {
        if (targetView == null) {
            Notification.show(getTranslation("tab.analysis.wrongButtonPath"));
            analyseButtonEnabled = false;
        }

        buildLayout(targetView);
    }

    private void buildLayout(Class<? extends Component> targetView) {
        setWidthFull();
        setHeightFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);
        setPadding(true);

        text.setText(getTranslation("tab.analysis.tooltip"));

        analyseButton.setText(getTranslation("tab.analysis.button"));
        analyseButton.setEnabled(analyseButtonEnabled);
        analyseButton.setWidth(null);

        analyseButton.addClickListener(_ -> UI.getCurrent().navigate(targetView));

        add(text, analyseButton);
    }
}
