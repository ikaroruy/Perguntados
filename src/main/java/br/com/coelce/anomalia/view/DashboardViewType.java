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
    
//    DASHBOARD("dashboard", FontAwesome.HOME, true),
    ANOMALIAS(AnomaliaView.VIEW_NAME, FontAwesome.EXCLAMATION_CIRCLE, false),
    CLASANOMALIAS(TipoAnomaliaView.VIEW_NAME, FontAwesome.ALIGN_CENTER, true),
    ACOES(AcaoView.VIEW_NAME, FontAwesome.CHECK, false),
    PROCESSOS(ProcessoView.VIEW_NAME, FontAwesome.SITEMAP, false),
    ROTINAS(RotinaView.VIEW_NAME, FontAwesome.CLIPBOARD, false),
    DIRETORIA(DiretoriaView.VIEW_NAME, FontAwesome.GROUP, true),
    AREAS(AreaView.VIEW_NAME, FontAwesome.BUILDING, true),
    OPERADORES(OperadorView.VIEW_NAME, FontAwesome.MALE, true),
    USUARIOS(UsuariosView.VIEW_NAME, FontAwesome.USER, true),
//    PERMISSOES(PermissoesView.VIEW_NAME, FontAwesome.LOCK, true),
    LOCAIS(LocaisView.VIEW_NAME, FontAwesome.MAP_MARKER, true),
    REPORTS("relatorios", FontAwesome.FILE_TEXT_O, false);
//    SCHEDULE("agenda", FontAwesome.CALENDAR_O, false);

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
