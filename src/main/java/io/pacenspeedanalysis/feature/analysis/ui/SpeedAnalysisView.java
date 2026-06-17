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
import io.pacenspeedanalysis.model.analysis.SpeedAnalysis;
import io.pacenspeedanalysis.util.numeric.BigDecimalUtils;

import java.util.ArrayList;
import java.util.Locale;

@UIScope
@Route(value = "speed-analysis", layout = MainLayout.class)
public final class SpeedAnalysisView extends AnalysisView<SpeedAnalysis> {

    private static final int MINIMUM_INTEGER_DIGITS = 1;
    private static final int FRACTION_DIGITS = 1;

    public SpeedAnalysisView(SessionState state, AnalysisService analysisService) {
        super(state, analysisService, new ArrayList<>(), new Grid<>(SpeedAnalysis.class, false));
    }

    @Override
    protected void loadDataAndBuildGrid() {
        final Locale locale = VaadinSession.getCurrent().getLocale();

        onLoadInit();

        analysis.addAll(analysisService.generateSpeedAnalysis(state.getData(), state.getAllHiddenLaps()));

        grid.addColumn(SpeedAnalysis::name)
                .setHeader(createNameHeader(state.getAggregationType()))
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(AnalysisComparator.BY_NAME::compare);

        grid.addColumn(
                        a -> BigDecimalUtils.format(
                                a.average(),
                                locale,
                                MINIMUM_INTEGER_DIGITS,
                                FRACTION_DIGITS
                        )
                )
                .setHeader(getTranslation("analysis.table.header.average"))
                .setSortable(true)
                .setComparator(SpeedAnalysis::average);

        grid.addColumn(
                        a -> BigDecimalUtils.format(
                                a.median(),
                                locale,
                                MINIMUM_INTEGER_DIGITS,
                                FRACTION_DIGITS
                        )
                )
                .setHeader(getTranslation("analysis.table.header.median"))
                .setSortable(true)
                .setComparator(SpeedAnalysis::median);

        grid.addColumn(
                        a -> BigDecimalUtils.format(
                                a.standardDeviation(),
                                locale,
                                MINIMUM_INTEGER_DIGITS,
                                FRACTION_DIGITS
                        )
                )
                .setHeader(getTranslation("analysis.table.header.stdDev"))
                .setSortable(true)
                .setComparator(SpeedAnalysis::standardDeviation);

        grid.addColumn(SpeedAnalysis::laps)
                .setHeader(getTranslation("analysis.table.header.laps"));

        grid.addColumn(SpeedAnalysis::totalLaps)
                .setHeader(getTranslation("analysis.table.header.totalLaps"));

        grid.setEmptyStateText(getTranslation("analysis.table.noData"));

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

        if (type != EAnalysisType.AVG_SPEED) {
            if (type == EAnalysisType.PACE) {
                event.forwardTo(PaceAnalysisView.class);
            } else {
                event.forwardTo(StartView.class);
            }
            return;
        }

        loadDataAndBuildGrid();
    }
}
