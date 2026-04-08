package com.example.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout implements AfterNavigationObserver {

    private H1 viewTitle;

    public MainLayout() {
        
        addHeaderContent();
    }

    private void addHeaderContent() {

        viewTitle = new H1();
        viewTitle.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.NONE
        );

        addToNavbar(true, viewTitle);
    }

    // 🔥 Métodos eliminados (ya no hay menú lateral)
    /*
    private void addDrawerContent() { }

    private SideNav createNavigation() { }

    private Footer createFooter() { }
    */

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }
}