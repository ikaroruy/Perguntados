/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.view;

import br.com.coelce.perguntados.event.NavigationEvent;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
public class ErrorView extends CustomComponent implements View {

    @Inject
    private AccessControl accessControl;

    @Inject
    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(new Label(
                "Unfortunately, the page you've requested does not exists."));
        if (accessControl.isUserSignedIn()) {
            layout.addComponent(createChatButton());
        } else {
            layout.addComponent(createLoginButton());
        }
        setCompositionRoot(layout);
    }

    private Button createLoginButton() {
        Button button = new Button("To login page");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigationEvent.fire(new NavigationEvent("login"));
            }
        });
        return button;
    }

    private Button createChatButton() {
        Button button = new Button("Back to the main page");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigationEvent.fire(new NavigationEvent("anomalia"));
            }
        });
        return button;
    }

}
