package com.pi4j.raspberrypiinfoservice.views;

import com.pi4j.raspberrypiinfo.definition.BoardModel;
import com.pi4j.raspberrypiinfoservice.service.SystemInfoService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@PageTitle("Raspberry Pi Board Information")
@Route(value = "board-information", layout = BaseLayout.class)
@RouteAlias(value = "", layout = BaseLayout.class)
public class BoardInfoView extends VerticalLayout {

    private final VerticalLayout holder = new VerticalLayout();

    public BoardInfoView(@Autowired SystemInfoService systemInfoService) {
        setSpacing(false);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        //getStyle().set("text-align", "center");

        ListBox<BoardModel> listBox = new ListBox<>();
        listBox.setItems(Arrays.stream(BoardModel.values())
                .sorted(Comparator.comparing(BoardModel::getLabel))
                .toList());
        listBox.addValueChangeListener(e -> showBoard(e.getValue()));
        listBox.setMinWidth(250, Unit.PIXELS);
        listBox.setHeightFull();
        //listBox.getStyle().set("text-alignment", "left");
        listBox.setRenderer(new ComponentRenderer<>(board -> {
            var lbl = new Label(board.getLabel());
            lbl.setWidthFull();
            return lbl;
        }));

        var split = new SplitLayout(listBox, holder);
        split.setHeightFull();
        split.setWidthFull();

        add(split);
    }

    private void showBoard(BoardModel boardModel) {
        holder.removeAll();

        holder.add(new H2(boardModel.getLabel()));

        var img = new Image("/boards/" + boardModel.name() + ".jpg", boardModel.getLabel());
        img.setHeight(200, Unit.PIXELS);
        holder.add(img);

        holder.add(getLabelValue("Board type", boardModel.getBoardType().name()));
        holder.add(getLabelValue("Model", boardModel.getModel().name()));
        holder.add(getLabelValue("Header version", boardModel.getVersion().getLabel()));
        holder.add(getLabelValue("Release date", boardModel.getReleaseDate().toString()));
        holder.add(getLabelValue("Processor", boardModel.getProcessor().getLabel()));
        holder.add(getLabelValue("Memory in GB", boardModel.getMemoryInGb().isEmpty() ? "" :
                boardModel.getMemoryInGb().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))));
    }

    private HorizontalLayout getLabelValue(String label, String value) {
        var lbl = new Label(label);
        lbl.setWidth(250, Unit.PIXELS);
        var labelValueHolder = new HorizontalLayout(lbl, new Label(value));
        labelValueHolder.setMargin(false);
        labelValueHolder.setPadding(false);
        return labelValueHolder;
    }
}
