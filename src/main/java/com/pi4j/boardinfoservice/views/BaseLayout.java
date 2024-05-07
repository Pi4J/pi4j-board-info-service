package com.pi4j.boardinfoservice.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

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

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Overflow.AUTO, LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Padding.Vertical.XSMALL);

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        //list.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.SMALL, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);
        list.addClassNames(LumoUtility.Gap.SMALL, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);
        nav.add(list);

        for (ListItem menuItem : createMenuItems()) {
            list.add(menuItem);
        }

        return nav;
    }

    private ListItem[] createMenuItems() {
        return new ListItem[]{
                new MenuItemComponent("Board Information", VaadinIcon.DATABASE.create(), BoardInfoView.class),
                new MenuItemComponent("System Information", VaadinIcon.INFO.create(), SystemInfoView.class),
                new MenuItemComponent("Thanks to...", VaadinIcon.HANDS_UP.create(), ThanksView.class),
                new MenuItemExternalLink("Open API", VaadinIcon.GLOBE.create(), "https://api.pi4j.com/api/docs/pi4j"),
                new MenuItemExternalLink("Swagger UI - API", VaadinIcon.GLOBE.create(), "https://api.pi4j.com/swagger-ui/index.html"),
                new MenuItemExternalLink("Pi4J Docs", VaadinIcon.GLOBE.create(), "https://pi4j.com")
        };
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

    public static class MenuItemComponent extends ListItem {
        private final Class<? extends Component> view;

        public MenuItemComponent(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.XSMALL, LumoUtility.Height.MEDIUM, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.SMALL,
                    LumoUtility.TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(LumoUtility.FontWeight.MEDIUM, LumoUtility.FontSize.MEDIUM, LumoUtility.Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }
    }

    public static class MenuItemExternalLink extends ListItem {
        public MenuItemExternalLink(String menuTitle, Component icon, String url) {
            Anchor link = new Anchor(url, "", AnchorTarget.BLANK);
            link.addClassNames(LumoUtility.Display.FLEX,
                    LumoUtility.Gap.XSMALL,
                    LumoUtility.Height.MEDIUM,
                    LumoUtility.AlignItems.CENTER,
                    LumoUtility.Padding.Horizontal.SMALL,
                    LumoUtility.TextColor.BODY);

            if (icon != null) {
                link.add(icon);
            }

            Span text = new Span(menuTitle);
            text.addClassNames(LumoUtility.FontWeight.MEDIUM,
                    LumoUtility.FontSize.MEDIUM,
                    LumoUtility.Whitespace.NOWRAP);
            link.add(text);

            add(link);
        }
    }
}
