package com.pi4j.boardinfoservice.views.header;


import com.pi4j.raspberrypiinfo.definition.HeaderPins;
import com.pi4j.raspberrypiinfo.pin.HeaderPin;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

/**
 * Visualizes the header in two columns, as it looks on the board.
 */
public class HeaderPinView extends VerticalLayout {

    /**
     * Constructor to create the pin visualization of the header.
     *
     * @param headerPins {@link HeaderPins} to be visualized.
     */
    public HeaderPinView(HeaderPins headerPins) {
        //this.spacing(25);
        var rows = new HorizontalLayout();
        rows.setPadding(false);
        rows.setMargin(false);
        rows.setSpacing(true);
        rows.add(this.getRow(headerPins.getPins(), true));
        rows.add(this.getRow(headerPins.getPins(), false));
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);
        this.add(rows);
    }

    /**
     * Visualizes one side of the header.
     *
     * @param pins     {@link List} of {@link HeaderPin} for which the header must be visualized.
     * @param firstRow True for first row, false for second.
     * @return {@link VerticalLayout} with the pins of the header row.
     */
    private VerticalLayout getRow(List<HeaderPin> pins, boolean firstRow) {
        var row = new VerticalLayout();
        row.setPadding(false);
        row.setMargin(false);
        row.setSpacing(true);

        for (int i = (firstRow ? 0 : 1); i < pins.size(); i += 2) {
            row.add(new PinView(pins.get(i), !firstRow));
        }

        return row;
    }
}
