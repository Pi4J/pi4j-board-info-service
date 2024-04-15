package com.pi4j.boardinfoservice.views;

import com.pi4j.boardinfoservice.service.SystemInfoService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
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

    private final SystemInfoService systemInfoService;
    private final List<InfoLine> infoList = new ArrayList<>();

    public SystemInfoView(@Autowired SystemInfoService systemInfoService) {
        this.systemInfoService = systemInfoService;

        setSpacing(false);

        Grid<InfoLine> grid = new Grid<>(InfoLine.class, false);
        grid.addColumn(InfoLine::type)
                .setHeader("Type")
                .setTextAlign(ColumnTextAlign.START);
        grid.addColumn(InfoLine::label)
                .setHeader("Label")
                .setTextAlign(ColumnTextAlign.START);
        grid.addColumn(InfoLine::info)
                .setHeader("Info")
                .setTextAlign(ColumnTextAlign.START);
        grid.setItems(infoList);
        add(grid);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    @Override
    public void onAttach(AttachEvent event) {
        UI.getCurrent().access(() -> {
            var board = systemInfoService.getDetectedBoard();
            infoList.add(new InfoLine("Board", "Name", board.getBoardModel().getName()));
            infoList.add(new InfoLine("Board", "Type", board.getBoardModel().getBoardType()));
            infoList.add(new InfoLine("Board", "Model", board.getBoardModel().getModel().getLabel()));
            infoList.add(new InfoLine("Operating System", "Name", board.getOperatingSystem().getName()));
            infoList.add(new InfoLine("Operating System", "Architecture", board.getOperatingSystem().getArchitecture()));
            infoList.add(new InfoLine("Operating System", "Version", board.getOperatingSystem().getVersion()));
            infoList.add(new InfoLine("Java", "Version", board.getJavaInfo().getVersion()));
            infoList.add(new InfoLine("Java", "Runtime", board.getJavaInfo().getRuntime()));
            infoList.add(new InfoLine("Java", "Vendor", board.getJavaInfo().getVendor()));
            infoList.add(new InfoLine("Java", "Vendor Version", board.getJavaInfo().getVendorVersion()));

            var memory = systemInfoService.getJvmMemory();
            infoList.add(new InfoLine("JVM Memory", "Free (MB)", memory.getFreeInMb()));
            infoList.add(new InfoLine("JVM Memory", "Max (MB)", memory.getMaxInMb()));
            infoList.add(new InfoLine("JVM Memory", "Total (MB)", memory.getTotalInMb()));
            infoList.add(new InfoLine("JVM Memory", "Used (MB)", memory.getUsedInMb()));

            var reading = systemInfoService.getBoardReading();
            infoList.add(new InfoLine("Board reading", "Board code", reading.getBoardCode()));
            infoList.add(new InfoLine("Board reading", "Board version code", reading.getBoardVersionCode()));
            infoList.add(new InfoLine("Board reading", "Uptime", reading.getUptimeInfo()));
            infoList.add(new InfoLine("Board reading", "Memory", reading.getMemory()));
            infoList.add(new InfoLine("Board reading", "Temperature", reading.getTemperature()));
            infoList.add(new InfoLine("Board reading", "Temperature (°C)", reading.getTemperatureInCelsius()));
            infoList.add(new InfoLine("Board reading", "Temperature (°F)", reading.getTemperatureInFahrenheit()));
            infoList.add(new InfoLine("Board reading", "Volt", reading.getVolt()));
            infoList.add(new InfoLine("Board reading", "Volt (value)", reading.getVoltValue()));
        });
    }

    private record InfoLine(String type, String label, Object info) {
    }
}
