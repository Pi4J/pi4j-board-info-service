package com.pi4j.boardinfoservice.views;

import com.pi4j.boardinfo.definition.BoardModel;
import com.pi4j.boardinfoservice.views.header.HeaderLegend;
import com.pi4j.boardinfoservice.views.header.HeaderPinView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

@PageTitle("Raspberry Pi Board Information")
@Route(value = "board-information", layout = BaseLayout.class)
@RouteAlias(value = "", layout = BaseLayout.class)
public class BoardInfoView extends VerticalLayout implements HasUrlParameter<String> {

    private static final Logger logger = LogManager.getLogger(BoardInfoView.class);

    private final VerticalLayout holder = new VerticalLayout();
    private final SideNav items = new SideNav();
    private BoardModel selectedBoard;

    public BoardInfoView() {
        setSpacing(false);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);

        add(new Paragraph(
                new Span("Information about all the Raspberry Pi boards. This info is based on the "),
                new Anchor("https://pi4j.com/documentation/board-info", "board info", AnchorTarget.BLANK),
                new Span(" provided by the Pi4J library.")));

        holder.setPadding(true);
        holder.setMargin(false);
        holder.setSpacing(true);

        items.setMinWidth(250, Unit.PIXELS);

        var split = new SplitLayout(items, holder);
        split.setHeightFull();
        split.setWidthFull();

        add(split);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        selectedBoard = Arrays.stream(BoardModel.values())
                .filter(bm -> bm.name().equalsIgnoreCase(parameter == null ? "" : parameter))
                .findFirst()
                .orElse(BoardModel.UNKNOWN);
        UI.getCurrent().access(() -> showBoard(selectedBoard));
    }

    @Override
    public void onAttach(AttachEvent event) {
        var listWithoutUnknown = Arrays.stream(BoardModel.values())
                .filter(bm -> bm != BoardModel.UNKNOWN)
                .sorted(Comparator.comparing(BoardModel::getLabel))
                .toList();
        UI.getCurrent().access(() -> {
            for (BoardModel boardModel : listWithoutUnknown) {
                var item = new SideNavItem(boardModel.getLabel(), "/board-information/" + boardModel.getName());
                items.addItem(item);
                if (boardModel == selectedBoard) {
                    showBoard(boardModel);
                }
            }
        });
    }

    private void showBoard(BoardModel boardModel) {
        holder.removeAll();

        if (boardModel == null || boardModel == BoardModel.UNKNOWN) {
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
