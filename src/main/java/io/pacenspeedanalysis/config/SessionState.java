package io.pacenspeedanalysis.config;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import io.pacenspeedanalysis.model.*;
import io.pacenspeedanalysis.model.analysis.AnalysisContext;
import io.pacenspeedanalysis.model.analysis.LapKey;
import io.pacenspeedanalysis.model.analysis.outliers.OutlierFilterFactory;
import io.pacenspeedanalysis.model.data.DataRecord;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
@VaadinSessionScope
public final class SessionState {

    private Path uploadedFile;
    private List<DataRecord> data;
    private EAnalysisType analysisType;
    private EAggregationType aggregationType;
    private AnalysisContext analysisContext;
    private Set<LapKey> hiddenLaps;
    private Set<LapKey> outlierLaps;

    public SessionState() {
        this.uploadedFile = null;
        this.data = new ArrayList<>();
        this.analysisType = null;
        this.aggregationType = null;
        this.analysisContext = null;
        this.hiddenLaps = new HashSet<>();
        this.outlierLaps = new HashSet<>();
    }

    public Path getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Path uploadedFile) {
        if (uploadedFile == null) {
            throw new IllegalArgumentException("uploadedFile must not be null");
        }

        this.uploadedFile = uploadedFile;
    }

    public boolean hasData() {
        return data != null && !data.isEmpty();
    }

    public List<DataRecord> getData() {
        return Collections.unmodifiableList(data);
    }

    public void setData(List<DataRecord> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data must be neither null or empty");
        }

        this.data = data;

        this.outlierLaps.clear();
        this.hiddenLaps.clear();
        this.analysisContext = null;
    }

    public void updateData(List<DataRecord> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data must be neither null or empty");
        }

        this.data = data;
        this.outlierLaps.addAll(OutlierFilterFactory.forType(analysisType).filter(data));
        this.analysisContext = AnalysisContext.of(data, outlierLaps);
    }

    public EAnalysisType getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(EAnalysisType analysisType) {
        if (analysisType == null) {
            throw new IllegalArgumentException("analysisType must not be null");
        }

        this.analysisType = analysisType;
    }

    public EAggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(EAggregationType aggregationType) {
        if (aggregationType == null) {
            throw new IllegalArgumentException("aggregationType must not be null");
        }

        this.aggregationType = aggregationType;
    }

    public AnalysisContext getAnalysisContext() {
        return analysisContext;
    }

    public Set<LapKey> getAllHiddenLaps() {
        return Stream.of(hiddenLaps, outlierLaps)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toUnmodifiableSet());
    }

    public void addHiddenLaps(Set<LapKey> lapsToHide) {
        if (lapsToHide == null) {
            throw new IllegalArgumentException("lapsToHide must not be null");
        }

        this.hiddenLaps.addAll(lapsToHide);
    }

    public void clearSelectedTypes() {
        analysisType = null;
        aggregationType = null;
    }

    public void clearState() {
        uploadedFile = null;
        data = new ArrayList<>();
        analysisType = null;
        aggregationType = null;
        analysisContext = null;
        hiddenLaps = new HashSet<>();
        outlierLaps = new HashSet<>();
    }

    public void clearUploadedFile() {
        this.uploadedFile = null;
    }
}
