package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.base.ui.MainLayout;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.analysis.ui.PaceAnalysisView;
import io.pacenspeedanalysis.feature.start.ui.StartView;
import io.pacenspeedanalysis.model.EAnalysisType;

@UIScope
@Route(value = "filter-pace", layout = MainLayout.class)
public final class FilterPaceView extends FilterView implements BeforeEnterObserver {

    public FilterPaceView(SessionState state) {
        super(state);
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
                event.forwardTo(FilterSpeedView.class);
            } else {
                event.forwardTo(StartView.class);
            }
            return;
        }

        buildLayout();
    }

    @Override
    protected void buildLayout() {
        tabSheet.add(
                getTranslation("filtering.tab.analyse"),
                new AnalysisTab(PaceAnalysisView.class)
        );

        tabSheet.add(
                getTranslation("filtering.tab.firstLap"),
                new FilterFirstLapTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.pit"),
                new FilterPitLapsTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.nonGreenFinish"),
                new FilterNonGreenFinishLapsTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.slowLaps"),
                new FilterSlowPaceLapsTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.pickBest"),
                new FilterPickBestPaceLapsTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.pickBestPercentage"),
                new FilterPickBestPaceLapsPercentageTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.excludedPeriods"),
                new FilterUnwantedPeriodsTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.pickPeriod"),
                new FilterPickPeriodTab(state)
        );

        showView();
    }
}
