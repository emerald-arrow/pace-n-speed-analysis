package io.pacenspeedanalysis.feature.filter.ui;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.base.ui.MainLayout;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.analysis.ui.SpeedAnalysisView;
import io.pacenspeedanalysis.feature.start.ui.StartView;
import io.pacenspeedanalysis.model.EAnalysisType;

@UIScope
@Route(value = "filter-speed", layout = MainLayout.class)
public final class FilterSpeedView extends FilterView implements BeforeEnterObserver {

    public FilterSpeedView(SessionState state) {
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

        if (type != EAnalysisType.AVG_SPEED) {
            if (type == EAnalysisType.PACE) {
                event.forwardTo(FilterPaceView.class);
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
                new AnalysisTab(SpeedAnalysisView.class)
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
                new FilterSlowSpeedLapsTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.pickBest"),
                new FilterPickBestSpeedLapsTab(state)
        );

        tabSheet.add(
                getTranslation("filtering.tab.pickBestPercentage"),
                new FilterPickBestSpeedLapsPercentageTab(state)
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
