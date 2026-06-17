package io.pacenspeedanalysis.feature.analysis.ui;

import com.vaadin.flow.component.ModalityMode;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoIcon;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.analysis.AnalysisService;
import io.pacenspeedanalysis.feature.start.ui.StartView;
import io.pacenspeedanalysis.model.EExportFormat;
import io.pacenspeedanalysis.model.analysis.Analysis;
import io.pacenspeedanalysis.model.EAggregationType;
import io.pacenspeedanalysis.model.analysis.export.ExportColumns;

import java.util.List;

public abstract class AnalysisView<T extends Analysis<?>> extends VerticalLayout implements BeforeEnterObserver {

    protected final SessionState state;
    protected final AnalysisService analysisService;
    protected final List<T> analysis;
    protected final MenuBar menuBar = createMenuBar();
    protected final Grid<T> grid;

    public AnalysisView(SessionState state, AnalysisService analysisService, List<T> analysis, Grid<T> grid) {
        this.analysis = analysis;
        this.state = state;
        this.analysisService = analysisService;
        this.grid = grid;

        setWidthFull();
        setAlignItems(Alignment.STRETCH);
        setSpacing(true);

        grid.setEmptyStateText(getTranslation("analysis.table.noData"));

        UI.getCurrent().setPollInterval(-1);

        add(menuBar, grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!state.hasData()) {
            Notification.show(getTranslation("analysis.table.noData"));
            event.forwardTo(StartView.class);
        }
    }

    protected abstract void loadDataAndBuildGrid();

    protected String createNameHeader(EAggregationType type) {
        return switch (type) {
            case DRIVER -> getTranslation("common.aggregation.driver");
            case MANUFACTURER -> getTranslation("common.aggregation.manufacturer");
            case TEAM -> getTranslation("common.aggregation.team");
        };
    }

    protected void onLoadInit() {
        analysis.clear();
        grid.removeAllColumns();
    }

    private MenuBar createMenuBar() {
        final MenuBar bar = new MenuBar();

        bar.setOpenOnHover(true);
        bar.addThemeVariants(MenuBarVariant.LUMO_DROPDOWN_INDICATORS);

        MenuItem export = bar.addItem(getTranslation("analysis.bar.btn.export"));
        SubMenu exportSubMenu = export.getSubMenu();

        exportSubMenu.addItem("CSV", _ -> exportCsv());
        exportSubMenu.addItem("R", _ -> exportR());

        bar.addItem(getTranslation("analysis.bar.btn.startOver"), _ -> startOver());

        return bar;
    }

    private void exportCsv() {
        final ExportColumns columns = new ExportColumns(
                csvExportNameColumn(state.getAggregationType()),
                getTranslation("analysis.export.csv.average"),
                getTranslation("analysis.export.csv.median"),
                getTranslation("analysis.export.csv.stdDev"),
                getTranslation("analysis.export.csv.laps"),
                getTranslation("analysis.export.csv.totalLaps")
        );

        final String text = analysisService.exportAnalysis(
                analysis,
                state.getAnalysisType(),
                columns,
                EExportFormat.CSV,
                VaadinSession.getCurrent().getLocale()
        );

        showDialog(text);
    }

    private void exportR() {
        final ExportColumns columns = new ExportColumns(
                rExportNameColumn(state.getAggregationType()),
                getTranslation("analysis.export.r.average"),
                getTranslation("analysis.export.r.median"),
                getTranslation("analysis.export.r.stdDev"),
                getTranslation("analysis.export.r.laps"),
                getTranslation("analysis.export.r.totalLaps")
        );

        final String text = analysisService.exportAnalysis(
                analysis,
                state.getAnalysisType(),
                columns,
                EExportFormat.R,
                VaadinSession.getCurrent().getLocale()
        );

        showDialog(text);
    }

    private String csvExportNameColumn(EAggregationType type) {
        return switch (type) {
            case DRIVER -> getTranslation("analysis.export.csv.driver");
            case MANUFACTURER -> getTranslation("analysis.export.csv.manufacturer");
            case TEAM -> getTranslation("analysis.export.csv.team");
        };
    }

    private String rExportNameColumn(EAggregationType type) {
        return switch (type) {
            case DRIVER -> getTranslation("analysis.export.r.driver");
            case MANUFACTURER -> getTranslation("analysis.export.r.manufacturer");
            case TEAM -> getTranslation("analysis.export.r.team");
        };
    }

    private void showDialog(String export) {
        final Dialog dialog = new Dialog();

        dialog.setHeaderTitle(getTranslation("analysis.dialog.title"));
        dialog.setModality(ModalityMode.STRICT);
        dialog.setDraggable(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setWidth("800px");

        final Button closeButton = new Button(LumoIcon.CROSS.create(), (_) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        final Button copyButton = new Button(getTranslation("analysis.dialog.copy"));
        copyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        copyButton.addClickListener(_ -> {
            UI.getCurrent().getPage().executeJs(
                    "navigator.clipboard.writeText($0);",
                    export
            );
            Notification.show(getTranslation("analysis.dialog.copied"));
        });

        dialog.getHeader().add(copyButton, closeButton);

        final TextArea readonlyArea = new TextArea();
        readonlyArea.setReadOnly(true);
        readonlyArea.setValue(export);
        readonlyArea.setWidthFull();

        dialog.add(readonlyArea);

        dialog.open();
    }

    private void startOver() {
        state.clearState();
        UI.getCurrent().navigate(StartView.class);
    }
}
