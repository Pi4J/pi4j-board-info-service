package com.pi4j.boardinfoservice.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Thanks to...")
@Route(value = "thanks", layout = BaseLayout.class)
public class ThanksView extends VerticalLayout {

    public ThanksView() {
        setSpacing(false);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);

        add(new H2("Pi4J"));
        add(new Paragraph(
                new Span("This website is part of the "),
                new Anchor("http://pi4j.com/", "Pi4J project", AnchorTarget.BLANK),
                new Span("""
                        , a friendly object-oriented I/O API and implementation libraries for Java Programmers 
                        to access the full I/O capabilities of the Raspberry Pi platform. This project abstracts 
                        the low-level native integration and interrupt monitoring to enable Java programmers 
                        to focus on implementing their application business logic.
                        """)));

        add(new H2("Thanks to..."));
        add(new Paragraph(
                new Anchor("http://finaltek.com/", "FinalTek", AnchorTarget.BLANK),
                new Span(", for hosting this website on a Raspberry Pi in their data center!")));
        add(new Paragraph(
                new Anchor("https://vaadin.com/", "Vaadin", AnchorTarget.BLANK),
                new Span(", for this amazing Java Web framework with which this website is made.")));
        add(new Paragraph(
                new Anchor("https://foojay.io/", "Foojay and the whole Java community", AnchorTarget.BLANK),
                new Span(", for sharing a lot of knowledge about the complete Java and JDK ecosystem")));
    }
}
