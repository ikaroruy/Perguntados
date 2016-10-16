/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dunkelheit
 */
package br.com.coelce.perguntados.view;

import br.com.coelce.perguntados.event.NavigationEvent;
import br.com.coelce.perguntados.persistence.ProvaContainer;
import br.com.coelce.perguntados.persistence.QuestaoContainer;
import br.com.coelce.perguntados.view.form.ProvaWindow;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import com.vaadin.ui.themes.ValoTheme;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
@CDIView(ProvaView.VIEW_NAME)
public class ProvaView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "provas";
    private Table table;
    private ExcelExport excelExport;
    @Inject
    private ProvaContainer container;
    @Inject
    private ProvaWindow provaWindow;
    private Button editButton;
    @Inject
    private AccessControl accessControl;
    @Inject
    private QuestaoContainer questaoContainer;
    public String finalID;
    private List list;
    private int randomNum;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (table == null) {
            addComponent(buildToolbar());
            table = buildTable();
            addComponent(table);
            setExpandRatio(table, 1);
        }
    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Provas");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

//           createReport = buildCreateReport();
        Component buildFilter = buildFilter();
        HorizontalLayout buildBarButtons = buildBarButtons(container);
        buildBarButtons.setSpacing(true);

        HorizontalLayout tools;
        if (table == null && accessControl.isUserInRole("USUARIO")) {
            tools = new HorizontalLayout(buildFilter, buildBarButtons);
        } else {
            tools = new HorizontalLayout(buildFilter);
        }
//        HorizontalLayout tools = new HorizontalLayout(buildFilter, buildBarButtons);
        tools.setComponentAlignment(buildFilter, Alignment.BOTTOM_CENTER);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Table buildTable() {
        final Table tableReturn = new Table();
        tableReturn.setSelectable(true);
        tableReturn.setSizeFull();
        tableReturn.addStyleName(ValoTheme.TABLE_BORDERLESS);
        tableReturn.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableReturn.addStyleName(ValoTheme.TABLE_COMPACT);
        tableReturn.setColumnCollapsingAllowed(true);
        tableReturn.setColumnReorderingAllowed(true);
        tableReturn.setContainerDataSource(container);
        tableReturn.setVisibleColumns(new Object[]{"aluno", "pontos"});
        tableReturn.setColumnHeaders(new String[]{"Aluno", "Pontos"});
        tableReturn.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                editButton.setEnabled(event.getProperty().getValue() != null);
            }
        });
        return tableReturn;
    }

    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Container.Filterable data = (Container.Filterable) table.getContainerDataSource();
                data.removeAllContainerFilters();
                Or or = new Or(new Like("aluno", event.getText() + "%"),
                        new Like("pontos", event.getText() + "%"));
                data.addContainerFilter(or);
            }
        });

        filter.setInputPrompt("Buscar");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(new ShortcutListener("Limpar",
                ShortcutAction.KeyCode.ESCAPE, null) {
                    @Override
                    public void handleAction(Object sender, Object target) {
                        filter.setValue("");
                        ((com.vaadin.data.Container.Filterable) table.getContainerDataSource())
                        .removeAllContainerFilters();
                    }
                });
        return filter;
    }

    private HorizontalLayout buildBarButtons(final JPAContainer datasource) {
        Button bIncluir = new Button("Incluir");
        bIncluir.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
               
                provaWindow.create();
            }
        });

        Button bAtualizar = new Button("Atualizar");
        bAtualizar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                datasource.refresh();
            }
        });

        Button bExportar = new Button("Exportar");
        bExportar.setIcon(FontAwesome.FILE_EXCEL_O);
        bExportar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                SimpleDateFormat formatador = new SimpleDateFormat("dd_MM_yyyy");
                Date minhaDate = new Date();
                String novoFormato = formatador.format(minhaDate);
                excelExport = new ExcelExport(table, VIEW_NAME);
                excelExport.excludeCollapsedColumns();
                excelExport.setReportTitle(VIEW_NAME.toUpperCase());
                excelExport.setExportFileName(VIEW_NAME + "_"
                        + novoFormato + ".xls");
                excelExport.export();
            }
        });

        editButton = new Button("Excluir");
        editButton.setEnabled(false);
        editButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
//                provaWindow.edit(table.getValue().toString());
                provaWindow.excluir(table.getValue().toString());
                datasource.refresh();
            }
        });
        Button[] buttons = {bIncluir, editButton, bAtualizar, bExportar};
        HorizontalLayout barButton = new HorizontalLayout();
        barButton.setHeight("50");

        for (Button b : buttons) {
            b.setStyleName(Runo.BUTTON_BIG);
            barButton.addComponent(b);
            barButton.setComponentAlignment(b, Alignment.BOTTOM_CENTER);
        }

        return barButton;
    }
}
