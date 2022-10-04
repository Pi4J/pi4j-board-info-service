package com.pi4j.raspberrypiinfoservice.views.header;

import com.pi4j.raspberrypiinfo.definition.PinType;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HeaderLegend extends VerticalLayout {

    /**
     * Visualizes the legend for a header.
     */
    public HeaderLegend() {
        this.setWidth(200, Unit.PIXELS);
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(true);
        this.add(new H4("Legend"));
        for (PinType pinType : PinType.values()) {
            this.add(new PinTypeView(pinType));
        }
    }
}
