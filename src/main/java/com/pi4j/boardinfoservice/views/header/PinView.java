package com.pi4j.boardinfoservice.views.header;

import com.pi4j.boardinfo.model.HeaderPin;
import com.pi4j.boardinfoservice.util.Converter;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Visualizes the {@link HeaderPin}.
 */
class PinView extends HorizontalLayout {

    /**
     * Constructor to create the visualization of a {@link HeaderPin}.
     *
     * @param pin         The {@link HeaderPin} to be visualized.
     * @param rightToLeft True/False to switch between RL and LR visualization.
     */
    PinView(final HeaderPin pin, final boolean rightToLeft) {
        this.getStyle().set("border-color", "black")
                .set("border-width", "2px")
                .set("border-style", "solid");
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);
        this.setHeight(35, Unit.PIXELS);

        // BCM
        var bcmHolder = new VerticalLayout();
        bcmHolder.setPadding(false);
        bcmHolder.setMargin(false);
        bcmHolder.setSpacing(false);

        var bcmLabel = new Span();
        bcmLabel.setWidth(20, Unit.PIXELS);
        bcmLabel.getStyle().set("font", "9px Tahoma")
                .set("text-align", "center");
        bcmHolder.add(bcmLabel);

        var bcmNumber = new Span();
        bcmNumber.setWidth(20, Unit.PIXELS);
        bcmNumber.getStyle().set("font", "16px Tahoma")
                .set("text-align", "center");
        bcmHolder.add(bcmNumber);

        if (pin.getBcmNumber() != null) {
            bcmLabel.setText("BCM");
            bcmNumber.setText(String.valueOf(pin.getBcmNumber()));
        }

        // WiringPi number
        var wiringHolder = new VerticalLayout();
        wiringHolder.setPadding(false);
        wiringHolder.setMargin(false);
        wiringHolder.setSpacing(false);

        var wiringPiLabel = new Span();
        wiringPiLabel.setWidth(20, Unit.PIXELS);
        wiringPiLabel.getStyle().set("font", "9px Tahoma")
                .set("text-align", "center");
        wiringHolder.add(wiringPiLabel);

        var wiringPiNumber = new Span();
        wiringPiNumber.setWidth(20, Unit.PIXELS);
        wiringPiNumber.getStyle().set("font", "16px Tahoma")
                .set("text-align", "center");
        wiringHolder.add(wiringPiNumber);

        if (pin.getWiringPiNumber() != null) {
            wiringPiLabel.setText("WPI");
            wiringPiNumber.setText(String.valueOf(pin.getWiringPiNumber()));
        }

        // Name and info
        var name = new Span(pin.getName());
        name.getStyle().set("font", "12px Tahoma")
                .set("text-align", "center");
        name.setMinWidth(180, Unit.PIXELS);
        name.setMaxWidth(180, Unit.PIXELS);

        // Pin number
        var pinNumberHolder = new VerticalLayout();
        pinNumberHolder.setPadding(false);
        pinNumberHolder.setMargin(false);
        pinNumberHolder.setSpacing(false);

        var pinNumberLabel = new Span();
        pinNumberLabel.setWidth(20, Unit.PIXELS);
        pinNumberLabel.getStyle().set("font", "9px Tahoma")
                .set("text-align", "center");
        pinNumberLabel.setText("PIN");
        pinNumberHolder.add(pinNumberLabel);

        var pinNumber = new Span();
        pinNumber.setWidth(20, Unit.PIXELS);
        pinNumber.getStyle().set("font", "16px Tahoma")
                .set("text-align", "center");
        pinNumber.setText(String.valueOf(pin.getPinNumber()));
        pinNumberHolder.add(pinNumber);

        // Color pin
        var color = new VerticalLayout();
        color.setWidth(25, Unit.PIXELS);
        color.setHeight(25, Unit.PIXELS);
        color.getStyle().set("background-color", Converter.intToHexColor(pin.getPinType().getColor()));

        // Add in correct order to the screen
        if (rightToLeft) {
            this.add(color, pinNumberHolder, name, wiringHolder, bcmHolder);
        } else {
            this.add(bcmHolder, wiringHolder, name, pinNumberHolder, color);
        }
    }
}
