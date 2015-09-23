/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view;

import br.com.coelce.anomalia.persistence.AcaoContainer;
import br.com.coelce.anomalia.view.form.AcaoWindow;
import com.vaadin.addon.jpacontainer.JPAContainer;
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
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
@CDIView(AcaoView.VIEW_NAME)
public class AcaoView extends VerticalLayout implements View {

    private static final Logger LOGGER = Logger.getLogger(AcaoView.class.getSimpleName());
    public static final String VIEW_NAME = "ação";
    private Table table;
    @Inject
    private AcaoContainer container;
    @Inject
    private AcaoWindow acaoWindow;
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

        Label title = new Label("Cadastro de ações");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

//        createReport = buildCreateReport();
        Component buildFilter = buildFilter();
        HorizontalLayout buildBarButtons = buildBarButtons(container);
        HorizontalLayout tools = new HorizontalLayout(buildFilter, buildBarButtons);
        tools.setComponentAlignment(buildFilter, Alignment.MIDDLE_CENTER);
        tools.setComponentAlignment(buildBarButtons, Alignment.MIDDLE_CENTER);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Table buildTable() {
        final Table tableReturn = new Table();
        tableReturn.setSizeFull();
        tableReturn.addStyleName(ValoTheme.TABLE_BORDERLESS);
        tableReturn.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableReturn.addStyleName(ValoTheme.TABLE_COMPACT);
        tableReturn.setSelectable(true);
        tableReturn.setColumnCollapsingAllowed(true);
        tableReturn.setColumnReorderingAllowed(true);

        tableReturn.setContainerDataSource(container);
//        container.addNestedContainerProperty("anomalia.id");
        tableReturn.setVisibleColumns(new Object[]{"id", "nome", "descricao", "dataInicio", "dataTermino"});
        tableReturn.setColumnHeaders(new String[]{"Código", "Nome", "Descrição", "Data de Início", "Data de Término"});
//        tableReturn.addGeneratedColumn("logotipo", new Table.ColumnGenerator() {
//
//            @Override
//            public Object generateCell(Table source, Object itemId, Object columnId) {
//                Property itemProperty = source.getItem(itemId).getItemProperty(columnId);
//                if (itemProperty.getValue()==null){
//                    return new Label("Sem imagem");
//                }
//                return new Embedded("", new FileResource(new File(itemProperty.getValue().toString())));
////                return new Image("", new FileResource(new File(itemProperty.getValue().toString())));
//            }
//        });
//        tableReturn.setItemIcon(this, null);
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
                Or or = new Or(new Like("sigla", event.getText() + "%", false),
                        new Like("nome", event.getText() + "%", false));
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
                acaoWindow.create();

            }
        });

        Button bAtualizar = new Button("Atualizar");
        bAtualizar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                datasource.refresh();
            }
        });

        editButton = new Button("Editar");
        editButton.setEnabled(false);
        editButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                acaoWindow.edit(table.getValue().toString());
//                ParlamentarWindow window = new ParlamentarWindow(container);
//                window.edit(table.getValue().toString());
            }
        });
        Button[] buttons = {bIncluir, editButton, bAtualizar};
        HorizontalLayout barButton = new HorizontalLayout();
        barButton.setHeight("50");

        for (Button b : buttons) {
            b.setStyleName(Runo.BUTTON_BIG);
            barButton.addComponent(b);
            barButton.setComponentAlignment(b, Alignment.MIDDLE_CENTER);
        }

        return barButton;
    }
}

