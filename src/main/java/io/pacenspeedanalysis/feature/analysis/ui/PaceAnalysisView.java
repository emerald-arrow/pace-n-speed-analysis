package io.pacenspeedanalysis.feature.analysis.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.base.ui.MainLayout;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.analysis.AnalysisComparator;
import io.pacenspeedanalysis.feature.analysis.AnalysisService;
import io.pacenspeedanalysis.feature.start.ui.StartView;
import io.pacenspeedanalysis.model.EAnalysisType;
import io.pacenspeedanalysis.model.analysis.PaceAnalysis;
import io.pacenspeedanalysis.util.duration.DurationUtils;

import java.util.ArrayList;
import java.util.Locale;

@UIScope
@Route(value = "pace-analysis", layout = MainLayout.class)
public final class PaceAnalysisView extends AnalysisView<PaceAnalysis> {

    public PaceAnalysisView(SessionState state, AnalysisService analysisService) {
        super(state, analysisService, new ArrayList<>(), new Grid<>(PaceAnalysis.class, false));
    }

    @Override
    protected void loadDataAndBuildGrid() {
        final Locale locale = VaadinSession.getCurrent().getLocale();

        onLoadInit();

        analysis.addAll(analysisService.generatePaceAnalysis(state.getData(), state.getAllHiddenLaps()));

        grid.addColumn(PaceAnalysis::name)
                .setHeader(createNameHeader(state.getAggregationType()))
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(AnalysisComparator.BY_NAME::compare);

        grid.addColumn(a -> DurationUtils.formatAsLapTime(a.average(), locale))
                .setHeader(getTranslation("analysis.table.header.average"))
                .setSortable(true)
                .setComparator(PaceAnalysis::average);

        grid.addColumn(a -> DurationUtils.formatAsLapTime(a.median(), locale))
                .setHeader(getTranslation("analysis.table.header.median"))
                .setSortable(true)
                .setComparator(PaceAnalysis::median);

        grid.addColumn(a -> DurationUtils.formatAsLapTime(a.standardDeviation(), locale))
                .setHeader(getTranslation("analysis.table.header.stdDev"))
                .setSortable(true)
                .setComparator(PaceAnalysis::standardDeviation);

        grid.addColumn(PaceAnalysis::laps)
                .setHeader(getTranslation("analysis.table.header.laps"));

        grid.addColumn(PaceAnalysis::totalLaps)
                .setHeader(getTranslation("analysis.table.header.totalLaps"));

        grid.setItems(analysis);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);

        final EAnalysisType type = state.getAnalysisType();

        if (type == null) {
            event.forwardTo(StartView.class);
            return;
        }

        if (type != EAnalysisType.PACE) {
            if (type == EAnalysisType.AVG_SPEED) {
                event.forwardTo(SpeedAnalysisView.class);
            } else {
                event.forwardTo(StartView.class);
            }
            return;
        }

        loadDataAndBuildGrid();
    }
}
