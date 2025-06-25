package com.pi4j.boardinfoservice.views;

import com.pi4j.boardinfo.definition.BoardModel;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Compare Raspberry Pi Boards")
@Route(value = "board-compare", layout = BaseLayout.class)
public class BoardCompareView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {

    private static final Logger logger = LogManager.getLogger(BoardCompareView.class);

    private final HorizontalLayout selectorLayout = new HorizontalLayout();
    private Grid<ComparisonRow> comparisonGrid;

    private ComboBox<BoardModel> board1Selector;
    private ComboBox<BoardModel> board2Selector;

    private List<BoardModel> availableBoards;
    private boolean isUpdatingFromUrl = false;

    public BoardCompareView() {
        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);

        // Updated description for comparison functionality
        add(new Paragraph(
                new Span("Compare Raspberry Pi boards side by side to easily find differences. This comparison is based on the "),
                new Anchor("https://pi4j.com/documentation/board-info", "board info", AnchorTarget.BLANK),
                new Span(" provided by the Pi4J library.")));

        setupLayout();
    }

    private void setupLayout() {
        // Configure selector layout
        selectorLayout.setWidthFull();
        selectorLayout.setSpacing(true);
        selectorLayout.setPadding(false);

        // Create board selectors
        setupBoardSelectors();

        // Setup comparison grid
        setupComparisonGrid();

        add(selectorLayout, comparisonGrid);
    }

    private void setupBoardSelectors() {
        // Board 1 selector
        board1Selector = new ComboBox<>("Select First Board");
        board1Selector.setWidth("300px");
        board1Selector.setItemLabelGenerator(BoardModel::getLabel);
        board1Selector.addValueChangeListener(event -> {
            if (!isUpdatingFromUrl) {
                updateUrlParameters();
            }
            updateComparison();
        });

        // Board 2 selector
        board2Selector = new ComboBox<>("Select Second Board");
        board2Selector.setWidth("300px");
        board2Selector.setItemLabelGenerator(BoardModel::getLabel);
        board2Selector.addValueChangeListener(event -> {
            if (!isUpdatingFromUrl) {
                updateUrlParameters();
            }
            updateComparison();
        });

        // Add selectors to layout with spacing
        selectorLayout.add(board1Selector, board2Selector);
        selectorLayout.setFlexGrow(1, board1Selector);
        selectorLayout.setFlexGrow(1, board2Selector);
    }

    private void setupComparisonGrid() {
        comparisonGrid = new Grid<>(ComparisonRow.class, false);
        comparisonGrid.setSizeFull();

        // Property column
        comparisonGrid.addColumn(new ComponentRenderer<>(this::createPropertyComponent))
                .setHeader("Property")
                .setWidth("250px")
                .setFlexGrow(0)
                .setTextAlign(ColumnTextAlign.START);

        // Board 1 column
        comparisonGrid.addColumn(new ComponentRenderer<>(row -> createValueComponent(row, row.value1())))
                .setHeader("Board 1")
                .setFlexGrow(1)
                .setTextAlign(ColumnTextAlign.START);

        // Board 2 column
        comparisonGrid.addColumn(new ComponentRenderer<>(row -> createValueComponent(row, row.value2())))
                .setHeader("Board 2")
                .setFlexGrow(1)
                .setTextAlign(ColumnTextAlign.START);

        // Apply custom CSS for top alignment
        comparisonGrid.getElement().getThemeList().add("top-aligned");

        // Enable text wrapping
        comparisonGrid.getElement().getStyle().set("white-space", "normal");
    }

    private Span createValueComponent(ComparisonRow row, String value) {
        if ("Board Picture".equals(row.property()) && !"-".equals(value)) {
            // Create image component for board pictures
            Image img = new Image("/boards/" + value + ".jpg", "Board Image");
            img.setHeight("150px");
            img.setMaxWidth("200px");
            img.getStyle().set("object-fit", "contain");

            Span container = new Span();
            container.add(img);
            return container;
        }

        Span span = new Span(value);
        span.getStyle()
                .set("white-space", "normal")
                .set("word-wrap", "break-word")
                .set("overflow-wrap", "break-word")
                .set("display", "block")
                .set("max-width", "100%")
                .set("vertical-align", "top")
                .set("align-self", "flex-start");

        if (row.isDifferent()) {
            span.getStyle().set("background-color", "#fff3cd");
            span.getStyle().set("padding", "4px 8px");
            span.getStyle().set("border-radius", "4px");
        }

        return span;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        // This method is called for the main route parameter, but we'll use query parameters instead
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Parse query parameters for board selection
        Map<String, List<String>> parameters = event.getLocation().getQueryParameters().getParameters();

        if (availableBoards != null) {
            updateSelectorsFromUrl(parameters);
        }
    }

    private void updateSelectorsFromUrl(Map<String, List<String>> parameters) {
        isUpdatingFromUrl = true;

        try {
            // Get board1 parameter
            if (parameters.containsKey("board1") && !parameters.get("board1").isEmpty()) {
                String board1Name = parameters.get("board1").getFirst();
                BoardModel board1 = findBoardByName(board1Name);
                if (board1 != null) {
                    board1Selector.setValue(board1);
                }
            }

            // Get board2 parameter
            if (parameters.containsKey("board2") && !parameters.get("board2").isEmpty()) {
                String board2Name = parameters.get("board2").getFirst();
                BoardModel board2 = findBoardByName(board2Name);
                if (board2 != null) {
                    board2Selector.setValue(board2);
                }
            }
        } finally {
            isUpdatingFromUrl = false;
        }

        updateComparison();
    }

    private BoardModel findBoardByName(String name) {
        return availableBoards.stream()
                .filter(board -> board.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private void updateUrlParameters() {
        if (isUpdatingFromUrl) {
            return;
        }

        Map<String, String> params = new HashMap<>();

        if (board1Selector.getValue() != null) {
            params.put("board1", board1Selector.getValue().name());
        }

        if (board2Selector.getValue() != null) {
            params.put("board2", board2Selector.getValue().name());
        }

        // Build query string
        String queryString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // Update URL without triggering navigation
        String newUrl = "board-compare" + (queryString.isEmpty() ? "" : "?" + queryString);
        UI.getCurrent().getPage().getHistory().replaceState(null, newUrl);

        logger.info("Updated URL parameters: board1={}, board2={}",
                board1Selector.getValue() != null ? board1Selector.getValue().name() : "none",
                board2Selector.getValue() != null ? board2Selector.getValue().name() : "none");
    }

    @Override
    public void onAttach(AttachEvent event) {
        availableBoards = Arrays.stream(BoardModel.values())
                .filter(bm -> bm != BoardModel.UNKNOWN)
                .sorted(Comparator.comparing(BoardModel::getLabel))
                .toList();

        UI.getCurrent().access(() -> {
            board1Selector.setItems(availableBoards);
            board2Selector.setItems(availableBoards);

            // Update grid headers with board names
            updateGridHeaders();

            // Check if we have URL parameters to process
            Map<String, List<String>> parameters = UI.getCurrent().getInternals()
                    .getActiveViewLocation().getQueryParameters().getParameters();

            if (!parameters.isEmpty()) {
                updateSelectorsFromUrl(parameters);
            }
        });
    }

    private void updateGridHeaders() {
        // Update column headers based on selected boards
        String board1Header = board1Selector.getValue() != null ?
                board1Selector.getValue().getLabel() : "Select First Board";
        String board2Header = board2Selector.getValue() != null ?
                board2Selector.getValue().getLabel() : "Select Second Board";

        comparisonGrid.getColumns().get(1).setHeader(board1Header);
        comparisonGrid.getColumns().get(2).setHeader(board2Header);
    }

    private void updateComparison() {
        BoardModel board1 = board1Selector.getValue();
        BoardModel board2 = board2Selector.getValue();

        // Update grid headers
        updateGridHeaders();

        if (board1 == null && board2 == null) {
            comparisonGrid.setItems(Collections.emptyList());
            return;
        }

        List<ComparisonRow> rows = new ArrayList<>();

        // Add comparison rows
        rows.add(createComparisonRow("Board Type",
                board1 != null ? board1.getBoardType().name() : "-",
                board2 != null ? board2.getBoardType().name() : "-"));

        rows.add(createComparisonRow("Released",
                board1 != null ? formatReleaseDate(board1) : "-",
                board2 != null ? formatReleaseDate(board2) : "-"));

        rows.add(createComparisonRow("Model",
                board1 != null ? board1.getModel().name() : "-",
                board2 != null ? board2.getModel().name() : "-"));

        rows.add(createComparisonRow("Header Version",
                board1 != null ? board1.getHeaderVersion().getLabel() : "-",
                board2 != null ? board2.getHeaderVersion().getLabel() : "-"));

        rows.add(createComparisonRow("Release Date",
                board1 != null ? board1.getReleaseDate().toString() : "-",
                board2 != null ? board2.getReleaseDate().toString() : "-"));

        rows.add(createComparisonRow("Board Code(s)",
                board1 != null ? String.join(", ", board1.getBoardCodes()) : "-",
                board2 != null ? String.join(", ", board2.getBoardCodes()) : "-"));

        rows.add(createComparisonRow("SOC",
                board1 != null ? board1.getSoc().name() + " / " + board1.getSoc().getInstructionSet().getLabel() : "-",
                board2 != null ? board2.getSoc().name() + " / " + board2.getSoc().getInstructionSet().getLabel() : "-"));

        rows.add(createComparisonRow("CPU",
                board1 != null ? formatCpuInfo(board1) : "-",
                board2 != null ? formatCpuInfo(board2) : "-"));

        rows.add(createComparisonRow("Memory (GB)",
                board1 != null ? formatMemory(board1) : "-",
                board2 != null ? formatMemory(board2) : "-"));

        rows.add(createComparisonRow("Uses RP1",
                board1 != null ? (board1.usesRP1() ? "Yes" : "No") : "-",
                board2 != null ? (board2.usesRP1() ? "Yes" : "No") : "-"));

        // Add remarks if present
        if ((board1 != null && !board1.getRemarks().isEmpty()) ||
                (board2 != null && !board2.getRemarks().isEmpty())) {

            String remarks1 = board1 != null ? String.join("; ", board1.getRemarks()) : "-";
            String remarks2 = board2 != null ? String.join("; ", board2.getRemarks()) : "-";

            rows.add(createComparisonRow("Remarks", remarks1, remarks2));
        }

        // Add image
        if (board1 != null || board2 != null) {
            rows.add(createImageComparisonRow("Board Picture", board1, board2));
        }

        comparisonGrid.setItems(rows);
    }

    private Span createPropertyComponent(ComparisonRow row) {
        Span span = new Span(row.property());
        span.getStyle()
                .set("font-weight", "bold")
                .set("white-space", "normal")
                .set("word-wrap", "break-word")
                .set("overflow-wrap", "break-word")
                .set("display", "block")
                .set("max-width", "100%")
                .set("vertical-align", "top")
                .set("align-self", "flex-start");
        return span;
    }

    private ComparisonRow createComparisonRow(String property, String value1, String value2) {
        boolean isDifferent = !value1.equals(value2) && !"-".equals(value1) && !"-".equals(value2);
        return new ComparisonRow(property, value1, value2, isDifferent);
    }

    private ComparisonRow createImageComparisonRow(String property, BoardModel board1, BoardModel board2) {
        return new ComparisonRow(property,
                board1 != null ? board1.name() : "-",
                board2 != null ? board2.name() : "-",
                false); // Images are not compared for differences
    }

    private String formatReleaseDate(BoardModel board) {
        return board.getReleaseDate().getMonth().getDisplayName(TextStyle.FULL, Locale.UK)
                + " " + board.getReleaseDate().getYear();
    }

    private String formatCpuInfo(BoardModel board) {
        String speedInfo = board.getVersionsProcessorSpeedInMhz().isEmpty() ? "" :
                board.getVersionsProcessorSpeedInMhz().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")) + "MHz";

        return board.getNumberOfCpu() + "x " + board.getCpu().getLabel() +
                (speedInfo.isEmpty() ? "" : " @ " + speedInfo);
    }

    private String formatMemory(BoardModel board) {
        return board.getVersionsMemoryInGb().isEmpty() ? "" :
                board.getVersionsMemoryInGb().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
    }

    // Record to represent a comparison row
    private record ComparisonRow(String property, String value1, String value2, boolean isDifferent) {
    }
}