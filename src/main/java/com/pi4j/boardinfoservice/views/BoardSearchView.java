package com.pi4j.boardinfoservice.views;

import com.pi4j.boardinfo.definition.BoardModel;
import com.pi4j.boardinfoservice.views.header.HeaderLegend;
import com.pi4j.boardinfoservice.views.header.HeaderPinView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@PageTitle("Search Raspberry Pi Board")
@Route(value = "board-search", layout = BaseLayout.class)
public class BoardSearchView extends VerticalLayout {

    private static final Logger logger = LogManager.getLogger(BoardSearchView.class);

    private final VerticalLayout holder = new VerticalLayout();
    private final SideNav items = new SideNav();

    public BoardSearchView() {
        setSpacing(false);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);

        add(new Html("<span><b>Not sure which Raspberry Pi board you have? Search here to find out!</b><br></span>"),
                new Html("<span>Execute this command in the terminal and fill in the value in the search box:<br></span>"),
                new Pre("cat /proc/cpuinfo | grep 'Revision' | awk '{print $3}'"));

        // Add search input
        var searchField = new TextField("Search by board code");
        searchField.setWidth(300, Unit.PIXELS);
        searchField.setPlaceholder("Enter board code (e.g. c03111)");

        var searchButton = new Button("Search");
        searchButton.addClickListener(event -> {
            String searchTerm = searchField.getValue();
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                showBoard(searchTerm, findBoardByCode(searchTerm));
            }
        });

        // Add Enter key support to the search field
        searchField.addKeyPressListener(Key.ENTER, event -> {
            String searchTerm = searchField.getValue();
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                showBoard(searchTerm, findBoardByCode(searchTerm));
            }
        });

        var searchLayout = new HorizontalLayout(searchField, searchButton);
        searchLayout.setAlignItems(Alignment.BASELINE);

        add(searchLayout);


        holder.setPadding(true);
        holder.setMargin(false);
        holder.setSpacing(true);
        add(holder);
    }

    /**
     * Finds a board model by board code.
     *
     * @param searchTerm the search value
     * @return the matching BoardModel or BoardModel.UNKNOWN if not found
     */
    private BoardModel findBoardByCode(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return BoardModel.UNKNOWN;
        }

        // First, try to find by board model name (existing functionality)
        Optional<BoardModel> boardByName = Arrays.stream(BoardModel.values())
                .filter(bm -> bm.getBoardCodes().contains(searchTerm.trim()))
                .findFirst();

        return boardByName.orElse(BoardModel.UNKNOWN);
    }

    private void showBoard(String searchValue, BoardModel boardModel) {
        holder.removeAll();

        if (boardModel == null || boardModel == BoardModel.UNKNOWN) {
            holder.add(new H2("Board not found"));
            holder.add(new Paragraph("The specified board code '" + searchValue + "' could not be found. " +
                    "Please check the spelling or try a different search term."));
            return;
        }

        logger.info("Board selected: {}", boardModel.name());

        // Use access to prevent long-running load board on top of the screen
        UI.getCurrent().access(() -> {
            holder.add(new H2(boardModel.getLabel()));

            var img = new Image("/boards/" + boardModel.name() + ".jpg", boardModel.getLabel());
            img.setHeight(200, Unit.PIXELS);
            img.setMaxHeight(350, Unit.PIXELS);
            holder.add(img);

            var picturesNote = new Paragraph("Pictures only as illustration, still searching for a complete list of good pictures...");
            picturesNote.getStyle().set("font-style", "italic");
            picturesNote.getStyle().set("font-size", "0.8em");
            holder.add(picturesNote);

            holder.add(new H3("Board info"));

            holder.add(getLabelValue("Board type", boardModel.getBoardType().name()));
            var formattedDate = boardModel.getReleaseDate().getMonth().getDisplayName(TextStyle.FULL, Locale.UK)
                    + " " + boardModel.getReleaseDate().getYear();
            holder.add(getLabelValue("Released", formattedDate));
            holder.add(getLabelValue("Model", boardModel.getModel().name()));
            holder.add(getLabelValue("Header version", boardModel.getHeaderVersion().getLabel()));
            holder.add(getLabelValue("Release date", boardModel.getReleaseDate().toString()));
            holder.add(getLabelValue("Board code(s)", String.join(", ", boardModel.getBoardCodes())));
            holder.add(getLabelValue("SOC", boardModel.getSoc().name()
                    + " / " + boardModel.getSoc().getInstructionSet().getLabel()));
            holder.add(getLabelValue("CPU", boardModel.getNumberOfCpu()
                    + "x " + boardModel.getCpu().getLabel()
                    + " @ " + (boardModel.getVersionsProcessorSpeedInMhz().isEmpty() ? "" :
                    boardModel.getVersionsProcessorSpeedInMhz().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(", "))) + "Mhz"));
            holder.add(getLabelValue("Memory in GB", boardModel.getVersionsMemoryInGb().isEmpty() ? "" :
                    boardModel.getVersionsMemoryInGb().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(", "))));
            if (boardModel.usesRP1()) {
                holder.add(getLabelValue("Uses RP1", new Anchor("https://datasheets.raspberrypi.com/rp1/rp1-peripherals.pdf", "Yes, click here for datasheet", AnchorTarget.BLANK)));
            } else {
                holder.add(getLabelValue("Uses RP1", "No"));
            }
            if (!boardModel.getRemarks().isEmpty()) {
                var remarks = new VerticalLayout();
                holder.add(remarks);
                boardModel.getRemarks().forEach(r -> remarks.add(new Div(r)));
            }

            holder.add(new H3("Header(s)"));

            if (boardModel.getHeaderVersion().getHeaderPins() != null
                    && !boardModel.getHeaderVersion().getHeaderPins().isEmpty()) {
                boardModel.getHeaderVersion().getHeaderPins().forEach(hp -> {
                    holder.add(new H4(hp.getLabel()));
                    holder.add(new HeaderPinView(hp));
                });
                holder.add(new HeaderLegend());
            }
        });
    }

    private HorizontalLayout getLabelValue(String label, String value) {
        var lbl = new Span(label);
        lbl.setWidth(250, Unit.PIXELS);
        var labelValueHolder = new HorizontalLayout(lbl, new Span(value));
        labelValueHolder.setMargin(false);
        labelValueHolder.setPadding(false);
        return labelValueHolder;
    }

    private HorizontalLayout getLabelValue(String label, HtmlContainer html) {
        var lbl = new Span(label);
        lbl.setWidth(250, Unit.PIXELS);
        var labelValueHolder = new HorizontalLayout(lbl, html);
        labelValueHolder.setMargin(false);
        labelValueHolder.setPadding(false);
        return labelValueHolder;
    }
}