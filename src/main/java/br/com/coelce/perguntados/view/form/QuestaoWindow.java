/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.view.form;

import br.com.coelce.perguntados.model.Questao;
import br.com.coelce.perguntados.model.Resposta;
import br.com.coelce.perguntados.persistence.QuestaoContainer;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
public class QuestaoWindow extends Window implements Button.ClickListener {

    @PropertyId("pergunta")
    private TextArea perguntaArea;
    @PropertyId("resposta")
    private ComboBox respostaArea;

    private VerticalLayout layout;
    private BeanFieldGroup<Questao> binder;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private QuestaoContainer container;

    @PostConstruct
    public void init() {
        Page.getCurrent().setTitle("Questão | Perguntados");
        addStyleName("profile-window");
        setModal(true);

        setResizable(false);
        setHeight(90.0f, Sizeable.Unit.PERCENTAGE);

        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(new MarginInfo(true, false, false, false));
        setContent(layout);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        layout.addComponent(detailsWrapper);
        layout.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildPergunta());
        detailsWrapper.addComponent(buildRespostaCorreta());

        layout.addComponent(buildFooter());

        binder = new BeanFieldGroup<>(Questao.class);
        binder.bindMemberFields(this);

    }

    public void create() {
        setCaption("Nova questão");
        bindingFields(new Questao());
        UI.getCurrent().addWindow(this);
    }

    public void edit(String id) {
        try {
            setCaption("Editar questão");
            Questao m = container.getItem(id).getEntity();
            bindingFields(m);
            bExcluir.setVisible(true);
            UI.getCurrent().addWindow(this);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Notification.show("Não há questões para editar!\n", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void bindingFields(Questao m) {
        binder.setItemDataSource(m);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == bSalvar) {
            try {
                binder.commit();
            } catch (FieldGroup.CommitException e) {
                Notification.show("Preencha o formulário corretamente");
                return;
            }
            try {
                container.addEntity(binder.getItemDataSource().getBean());
                Notification.show("Questão cadastrada!", Notification.Type.HUMANIZED_MESSAGE);
            } catch (UnsupportedOperationException | IllegalStateException e) {
                Logger.getLogger(QuestaoWindow.class.getSimpleName()).log(Level.SEVERE, "", e);
                Notification.show("Nao consegui salvar a questão!\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                return;
            }
        } else if (event.getButton() == bExcluir) {
            container.removeItem(binder.getItemDataSource().getBean().getId());
        } else if (event.getButton() == bCancelar) {
            binder.discard();
        }
        close();
    }

    private class MyNewItemHandler implements AbstractSelect.NewItemHandler {

        @Override
        public void addNewItem(String newItemCaption) {
            List<Object> allEntityIdentifiers = container.getEntityProvider().getAllEntityIdentifiers(container, new Compare.Equal("id", newItemCaption), null);

            if (allEntityIdentifiers.isEmpty()) {
                Questao p = new Questao();
                p.setPergunta(newItemCaption.toUpperCase());
                Object addEntity = container.addEntity(p);

            }
        }

    }

    private Component buildPergunta() {
        FormLayout dadosLayout = new FormLayout();
        dadosLayout.setCaption("Pergunta");
        dadosLayout.setIcon(FontAwesome.QUESTION);
        dadosLayout.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        dadosLayout.setMargin(true);
        dadosLayout.setSpacing(true);
        dadosLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        Label text = new Label("Texto");
        text.addStyleName(ValoTheme.LABEL_LIGHT);
        perguntaArea = new TextArea();
        perguntaArea.setWidth(640, Sizeable.Unit.PIXELS);
        perguntaArea.setNullRepresentation("");
        dadosLayout.addComponent(text);
        dadosLayout.addComponent(perguntaArea);

        return dadosLayout;

    }

    private Component buildRespostaCorreta() {

        FormLayout detailsLayout = new FormLayout();
        detailsLayout.setCaption("Resposta");
        detailsLayout.setIcon(FontAwesome.THUMBS_O_UP);
        detailsLayout.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        detailsLayout.setMargin(true);
        detailsLayout.setSpacing(true);
        detailsLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        respostaArea = new ComboBox("Resposta");
        respostaArea.addItem(Resposta.CERTO);
        respostaArea.addItem(Resposta.ERRADO);
        respostaArea.setNullSelectionAllowed(false);
        detailsLayout.addComponent(respostaArea);

        return detailsLayout;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        footer.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        bSalvar = new Button("Salvar");
        bSalvar.addClickListener(this);
        bSalvar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bSalvar.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        bCancelar = new Button("Cancelar");
        bCancelar.addClickListener(this);
        bCancelar.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        bExcluir = new Button("Excluir");
        bExcluir.addClickListener(this);
        bExcluir.setVisible(false);

        Button[] buttons = {bSalvar, bCancelar, bExcluir};
        HorizontalLayout barButton = new HorizontalLayout();
        barButton.setSpacing(true);
        footer.addComponent(barButton);
        for (Button b : buttons) {
            barButton.addComponent(b);
            barButton.setComponentAlignment(b, Alignment.TOP_RIGHT);
        }

        return footer;
    }
}
