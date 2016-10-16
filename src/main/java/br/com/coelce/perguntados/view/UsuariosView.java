/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.view;

import br.com.coelce.perguntados.persistence.QuestaoContainer;
import br.com.coelce.perguntados.persistence.UsuarioContainer;
import br.com.coelce.perguntados.view.form.QuestaoWindow;
import br.com.coelce.perguntados.view.form.UsuarioWindow;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.tableexport.CustomTableHolder;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.CDIView;
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
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
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
import java.util.Date;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.tepi.filtertable.FilterTable;

/**
 *
 * @author dunkelheit
 */
@CDIView(UsuariosView.VIEW_NAME)
public class UsuariosView extends VerticalLayout implements View {

    private static final Logger LOGGER = Logger.getLogger(QuestaoView.class.getSimpleName());
    public static final String VIEW_NAME = "usuários";
    private FilterTable table;
    private ExcelExport excelExport;
    @Inject
    private UsuarioContainer container;
    @Inject
    private UsuarioWindow usuarioWindow;
    private Button editButton;

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

        Label title = new Label("Cadastro de usuários");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

//           createReport = buildCreateReport();
        Component buildFilter = buildFilter();
        HorizontalLayout buildBarButtons = buildBarButtons(container);
        buildBarButtons.setSpacing(true);
        HorizontalLayout tools = new HorizontalLayout(buildFilter, buildBarButtons);
        tools.setComponentAlignment(buildFilter, Alignment.BOTTOM_CENTER);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private FilterTable buildTable() {
        final FilterTable tableReturn = new FilterTable();
//        tableReturn.setFilterBarVisible(true);
        tableReturn.setSelectable(true);
        tableReturn.setSizeFull();
        tableReturn.addStyleName(ValoTheme.TABLE_BORDERLESS);
        tableReturn.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableReturn.addStyleName(ValoTheme.TABLE_COMPACT);
        tableReturn.setColumnCollapsingAllowed(true);
        tableReturn.setColumnReorderingAllowed(true);

        tableReturn.setContainerDataSource(container);
        tableReturn.setVisibleColumns(new Object[]{"login", "perfil"});
        tableReturn.setColumnHeaders(new String[]{"Login", "Perfil"});
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
                Or or = new Or(new Like("login", event.getText() + "%"),
                               new Like("perfil", event.getText() + "%"));
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
                usuarioWindow.create();

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
                excelExport = new ExcelExport(new CustomTableHolder(table), VIEW_NAME);
                excelExport.excludeCollapsedColumns();
                excelExport.setReportTitle(VIEW_NAME.toUpperCase());
                excelExport.setExportFileName(VIEW_NAME + "_" 
                        + novoFormato + ".xls");
                excelExport.export();
            }
        });


        editButton = new Button("Editar");
        editButton.setEnabled(false);
        editButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                usuarioWindow.edit(table.getValue().toString());
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
