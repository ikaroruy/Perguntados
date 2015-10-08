/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.service;

import br.com.coelce.anomalia.event.NavigationEvent;
import br.com.coelce.anomalia.view.ErrorView;
import br.com.coelce.anomalia.view.MainView;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.NormalUIScoped;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 *
 * @author pr02nl
 */
@NormalUIScoped
public class NavigationService {

    @Inject
    private AccessControl accessControl;

    @Inject
    private CDIViewProvider viewProvider;

    @Inject
    private ErrorView errorView;

    @Inject
    private MainView mainView;

    @Inject
    private UI ui;

    @PostConstruct
    public void initialize() {
        if (ui.getNavigator() == null) {
            Navigator navigator = new Navigator(ui, ui);
            navigator.addProvider(viewProvider);
            navigator.setErrorView(errorView);
        }
    }

    public void onNavigationEvent(@Observes NavigationEvent event) {
        try {
            if (accessControl.isUserSignedIn() && ui.getContent() != mainView) {
                mainView.rebuildMenu();
                ui.setContent((Component) mainView);
                Navigator navigator = new Navigator(ui, mainView.getContent());
                navigator.addProvider(viewProvider);
                navigator.setErrorView(errorView);
            }
            ui.getNavigator().navigateTo(event.getNavigateTo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
