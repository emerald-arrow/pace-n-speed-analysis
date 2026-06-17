package io.pacenspeedanalysis.base.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.VaadinSession;

import java.util.Locale;

@Layout
public final class MainLayout extends AppLayout {

    private Select<Locale> langSelect;
    private boolean ignoreLocaleChange = false;

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        H1 title = new H1(getTranslation("layout.title"));

        langSelect = new Select<>();
        langSelect.setItems(new Locale("pl"), Locale.ENGLISH);
        langSelect.setItemLabelGenerator(locale -> switch (locale.getLanguage()) {
            case "pl" -> "Polski";
            default -> "English";
        });

        langSelect.addValueChangeListener(e -> {
            if (!e.isFromClient()) return;
            if (ignoreLocaleChange) return;

            VaadinSession.getCurrent().setLocale(e.getValue());
            UI.getCurrent().getPage().reload();
        });

        HorizontalLayout header = new HorizontalLayout(title, langSelect);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(title);

        addToNavbar(header);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        super.onAttach(event);

        ignoreLocaleChange = true;
        try {
            langSelect.setValue(VaadinSession.getCurrent().getLocale());
        } finally {
            ignoreLocaleChange = false;
        }
    }
}
