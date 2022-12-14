package com.pi4j.boardinfoservice.views;

import com.pi4j.boardinfoservice.views.header.HeaderPinView;
import com.pi4j.raspberrypiinfo.definition.HeaderPins;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@PageTitle("Raspberry Pi Header")
@Route(value = "header")
public class HeaderView extends VerticalLayout implements HasUrlParameter<String> {

    private Optional<HeaderPins> headerPins = Optional.empty();

    public HeaderView() {
        setSpacing(false);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        if (parametersMap.isEmpty()) {
            return;
        }
        headerPins = Arrays.stream(HeaderPins.values())
                .filter(b -> b.name().equalsIgnoreCase(parametersMap.get("name").get(0)))
                .findFirst();
        headerPins.ifPresent(pins -> this.add(new HeaderPinView(pins)));
    }
}
