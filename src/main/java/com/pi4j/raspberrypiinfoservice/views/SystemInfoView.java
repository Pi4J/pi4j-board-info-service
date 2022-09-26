package com.pi4j.raspberrypiinfoservice.views;

import com.pi4j.raspberrypiinfoservice.service.SystemInfoService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@PageTitle("System Information")
@Route(value = "system-information", layout = BaseLayout.class)
public class SystemInfoView extends VerticalLayout {

    public SystemInfoView(@Autowired SystemInfoService systemInfoService) {
        setSpacing(false);

        Grid<InfoLine> grid = new Grid<>(InfoLine.class, false);
        grid.addColumn(InfoLine::type).setHeader("Type");
        grid.addColumn(InfoLine::label).setHeader("Label");
        grid.addColumn(InfoLine::info).setHeader("Info");

        List<InfoLine> infoList = new ArrayList<>();
        systemInfoService.getJavaVersion()
                .forEach((key, value) -> infoList.add(new InfoLine("Java", key, value)));
        systemInfoService.getOsVersion()
                .forEach((key, value) -> infoList.add(new InfoLine("Operating System", key, value)));
        systemInfoService.getMemory()
                .forEach((key, value) -> infoList.add(new InfoLine("Memory", key, value)));
        grid.setItems(infoList);
        add(grid);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private record InfoLine(String type, String label, Object info) {
    }
}
