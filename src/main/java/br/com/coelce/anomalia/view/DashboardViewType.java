/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Tree;

/**
 *
 * @author pr02nl
 */
public enum DashboardViewType {

    DASHBOARD("dashboard", AnomaliaView.class, FontAwesome.HOME, true),
    ANOMALIAS(AnomaliaView.VIEW_NAME, AnomaliaView.class, FontAwesome.EXCLAMATION_CIRCLE, false),
    CLASANOMALIAS(TipoAnomaliaView.VIEW_NAME, TipoAnomaliaView.class, FontAwesome.ALIGN_CENTER, false),
    ACOES(AcaoView.VIEW_NAME, AcaoView.class, FontAwesome.CHECK, false),
    PROCESSOS(ProcessoView.VIEW_NAME, ProcessoView.class, FontAwesome.SITEMAP, false),
    DIRETORIA(DiretoriaView.VIEW_NAME, DiretoriaView.class, FontAwesome.GROUP, false),
    AREAS(AreaView.VIEW_NAME, AreaView.class, FontAwesome.BUILDING, false),
    REPORTS("relatorios", AreaView.class, FontAwesome.FILE_TEXT_O, true),
    SCHEDULE("agenda", AreaView.class, FontAwesome.CALENDAR_O, false);
    
    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
            final Class<? extends View> viewClass,
            final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
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
