/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
public class MainView extends HorizontalLayout {

    @Inject
    private DashboardMenu dashboardMenu;
    private ComponentContainer content;

    @PostConstruct
    public void init() {
        setSizeFull();
        addStyleName("mainview");

        addComponent(dashboardMenu);

        content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }

    public ComponentContainer getContent() {
        return content;
    }

}
