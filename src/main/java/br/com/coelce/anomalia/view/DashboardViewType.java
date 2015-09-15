/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

/**
 *
 * @author pr02nl
 */
public enum DashboardViewType {

    DASHBOARD("dashboard", FontAwesome.HOME, true),
    ANOMALIAS(AnomaliaView.VIEW_NAME, FontAwesome.BAR_CHART_O, false),
    ACOES(AcaoView.VIEW_NAME, FontAwesome.BAR_CHART_O, false),
    REPORTS("relatorios", FontAwesome.FILE_TEXT_O, true),
    SCHEDULE("agenda", FontAwesome.CALENDAR_O, false);

    private final String viewName;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
            final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
