package com.pi4j.boardinfoservice.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;

/**
 * The main view is a top-level placeholder for other views.
 */
public class BaseLayout extends AppLayout {

    private H1 viewTitle;

    public BaseLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("view-header");
        return header;
    }

    private Component createDrawerContent() {
        var logo = new Image("/logo/pi4j-logo.png", "Pi4J logo");
        logo.setWidth(150, Unit.PIXELS);
        logo.getStyle().set("padding", "25px 0 0 50px");

        H2 appName = new H2("Pi4J API");
        appName.addClassNames("app-name");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(logo,
                appName, createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(
                new SideNavItem("Board Information", BoardInfoView.class, VaadinIcon.DATABASE.create()),
                new SideNavItem("System Information", SystemInfoView.class, VaadinIcon.INFO.create()),
                new SideNavItem("Open API", "https://api.pi4j.com/api/docs/pi4j", VaadinIcon.GLOBE.create()),
                new SideNavItem("Swagger UI - API", "https://api.pi4j.com/swagger-ui/index.html", VaadinIcon.GLOBE.create()),
                new SideNavItem("Pi4J Docs", "https://pi4j.com", VaadinIcon.GLOBE.create())
        );

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("app-nav-footer");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
