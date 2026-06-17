package io.pacenspeedanalysis.feature.start.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import io.pacenspeedanalysis.base.ui.MainLayout;
import io.pacenspeedanalysis.config.SessionState;
import io.pacenspeedanalysis.feature.category.ui.CategoryView;
import io.pacenspeedanalysis.feature.start.CsvImportServiceImpl;
import io.pacenspeedanalysis.model.data.DataRecord;
import io.pacenspeedanalysis.model.EAggregationType;
import io.pacenspeedanalysis.model.EAnalysisType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@UIScope
@Route(value = "", layout = MainLayout.class)
public final class StartView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionState state;
    private final CsvImportServiceImpl csvImportService;
    private final VerticalLayout elementsLayout = new VerticalLayout();
    private final Text text = new Text(null);
    private final Upload upload = new Upload();
    private final RadioButtonGroup<EAnalysisType> analysisGroup = new RadioButtonGroup<>();
    private final RadioButtonGroup<EAggregationType> aggregationGroup = new RadioButtonGroup<>();
    private final Button nextButton = new Button();
    private boolean builtLayout = false;
    private boolean resetting = false;

    public StartView(SessionState state, CsvImportServiceImpl csvImportService) {
        this.state = state;
        this.csvImportService = csvImportService;

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);

        elementsLayout.setWidth("600px");
        elementsLayout.setPadding(true);
        elementsLayout.setSpacing(true);
        elementsLayout.setAlignItems(Alignment.STRETCH);
        elementsLayout.getStyle()
                        .set("border-radius", "1em")
                        .set("padding", "1em")
                        .set("box-shadow", "0 1em 2em rgba(0,0,0,0.12)")
                        .set("border", "0.5em solid var(--lumo-contrast-10pct)");

        add(elementsLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!builtLayout) {
            buildLayout();
            builtLayout = true;
        }

        UI.getCurrent().setPollInterval(300);

        resetting = true;
        state.clearState();

        upload.clearFileList();

        analysisGroup.setEnabled(false);
        analysisGroup.clear();

        aggregationGroup.setEnabled(false);
        aggregationGroup.clear();

        nextButton.setEnabled(false);
        resetting = false;
    }

    private void buildLayout() {
        final UI ui = UI.getCurrent();

        setupPollListener(ui);

        text.setText(getTranslation("start.text"));

        buildUpload();

        buildAnalysisRadioButtonGroup();

        buildAggregationRadioButtonGroup();

        nextButton.setText(getTranslation("common.next"));
        nextButton.getStyle().set("align-self", "center");
        nextButton.setWidth(null);

        upload.setUploadHandler(uploadFile -> {
            try {
                Path temp = Files.createTempFile("analysis-pace-n-speed-", ".csv");
                Files.copy(uploadFile.getInputStream(), temp, StandardCopyOption.REPLACE_EXISTING);

                state.setUploadedFile(temp);
            } catch (IOException ex) {
                Notification.show(getTranslation("common.error", ex.getMessage()));
            }
        });

        upload.addFileRemovedListener(_ -> {
            resetting = true;

            state.clearUploadedFile();
            state.clearSelectedTypes();
            analysisGroup.clear();
            analysisGroup.setEnabled(false);
            aggregationGroup.clear();
            aggregationGroup.setEnabled(false);
            updateNextButtonState();

            resetting = false;
        });

        nextButton.addClickListener(_ -> {
            final Path file = state.getUploadedFile();

            if (file == null) {
                Notification.show(getTranslation("start.noFileError"));
                return;
            }
            if (state.getAnalysisType() == null || state.getAggregationType() == null) {
                Notification.show(getTranslation("start.noSelectionError"));
                return;
            }

            try (InputStream is = Files.newInputStream(file)) {
                final List<DataRecord> data = csvImportService.importCsv(
                        is, state.getAnalysisType(), state.getAggregationType()
                );

                state.setData(data);

                Files.deleteIfExists(file);

                ui.navigate(CategoryView.class);
            } catch (Exception ex) {
                Notification.show(getTranslation("common.error", ex.getMessage()));
            }
        });

        analysisGroup.addValueChangeListener(e -> {
            if (resetting || !e.isFromClient() || e.getValue() == null) {
                return;
            }
            state.setAnalysisType(e.getValue());
            updateNextButtonState();
        });

        aggregationGroup.addValueChangeListener(e -> {
            if (resetting || !e.isFromClient() || e.getValue() == null) {
                return;
            }
            state.setAggregationType(e.getValue());
            updateNextButtonState();
        });

        elementsLayout.add(text, upload, analysisGroup, aggregationGroup, nextButton);
    }

    private void buildAggregationRadioButtonGroup() {
        aggregationGroup.setLabel(getTranslation("start.aggregation"));
        aggregationGroup.setRequired(true);
        aggregationGroup.setItems(EAggregationType.DRIVER, EAggregationType.TEAM, EAggregationType.MANUFACTURER);
        aggregationGroup.setItemLabelGenerator(
                item -> switch (item) {
                    case DRIVER -> getTranslation("common.aggregation.driver");
                    case MANUFACTURER -> getTranslation("common.aggregation.manufacturer");
                    case TEAM -> getTranslation("common.aggregation.team");
                }
        );
    }

    private void buildAnalysisRadioButtonGroup() {
        analysisGroup.setLabel(getTranslation("start.analysisType"));
        analysisGroup.setRequired(true);
        analysisGroup.setItems(EAnalysisType.PACE, EAnalysisType.AVG_SPEED);
        analysisGroup.setItemLabelGenerator(
                item -> switch (item) {
                    case PACE -> getTranslation("start.analysisType.pace");
                    case AVG_SPEED -> getTranslation("start.analysisType.speed");
                }
        );
    }

    private void buildUpload() {
        upload.setAcceptedFileTypes(".csv");
        upload.setMaxFiles(1);
        upload.setDropAllowed(true);
        upload.setI18n(createUploadI18n());
        upload.setAutoUpload(true);
    }

    private UploadI18N createUploadI18n() {
        UploadI18N i18n = new UploadI18N();
        i18n.setDropFiles(new UploadI18N.DropFiles()
                .setOne(getTranslation("upload.drop.one")));
        i18n.setAddFiles(new UploadI18N.AddFiles()
                .setOne(getTranslation("upload.add.one")));
        return i18n;
    }

    private void setupPollListener(UI ui) {
        ui.addPollListener(_ -> {
            if (state.getUploadedFile() != null) {
                analysisGroup.setEnabled(true);
                aggregationGroup.setEnabled(true);
                updateNextButtonState();
            }
        });
    }

    private void updateNextButtonState() {
        nextButton.setEnabled(
                state.getUploadedFile() != null &&
                state.getAnalysisType() != null &&
                state.getAggregationType() != null
        );
    }
}
