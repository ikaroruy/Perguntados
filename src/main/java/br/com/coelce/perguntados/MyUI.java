package br.com.coelce.perguntados;

import br.com.coelce.perguntados.event.NavigationEvent;
import com.vaadin.annotations.Push;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import javax.inject.Inject;

/**
 *
 */
@Theme("mytheme")
@CDIUI("")
@Push
@Widgetset("br.com.coelce.perguntados.MyAppWidgetset")
public class MyUI extends UI {

    @Inject
    private AccessControl accessControl;

    @Inject
    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;

    @Override
    protected void init(VaadinRequest request) {
        addStyleName(ValoTheme.UI_WITH_MENU);
        navigationEvent.fire(new NavigationEvent("login"));
    }
}