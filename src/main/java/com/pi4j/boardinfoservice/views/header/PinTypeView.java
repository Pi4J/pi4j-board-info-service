package com.pi4j.boardinfoservice.views.header;

import com.pi4j.boardinfo.definition.PinType;
import com.pi4j.boardinfoservice.util.Converter;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Visualizes the {@link PinType}.
 */
class PinTypeView extends HorizontalLayout {
    /**
     * Constructor to create a legend field for a {@link PinType}.
     *
     * @param pinType The {@link PinType} to be visualized.
     */
    PinTypeView(final PinType pinType) {
        this.getStyle().set("border", "black 2px solid");
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(true);
        this.setHeight(35, Unit.PIXELS);
        this.setWidth(300, Unit.PIXELS);

        var color = new VerticalLayout();
        color.setWidth(25, Unit.PIXELS);
        color.setHeight(25, Unit.PIXELS);
        color.getStyle().set("background-color", Converter.intToHexColor(pinType.getColor()));
        this.add(color);

        var name = new Label(pinType.getLabel());
        name.getStyle().set("font", "18px Tahoma")
                .set("font-weight", "bold");
        this.add(name);
    }
}
