/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view.form;

import br.com.coelce.anomalia.model.Acao;
import br.com.coelce.anomalia.model.Anomalia;
import br.com.coelce.anomalia.persistence.AcaoContainer;
import br.com.coelce.anomalia.persistence.AnomaliaContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
public class AnomaliaWindow extends Window implements Button.ClickListener {

    @PropertyId("nome")
    private TextField firstNameField;
    @PropertyId("partido")
    private ComboBox partidoField;

    private FormLayout layout;
    private BeanFieldGroup<Anomalia> binder;
    private HorizontalLayout buttons;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private AnomaliaContainer container;
    @Inject
    private AcaoContainer acaoContainer;

//    public ParlamentarWindow(ParlamentarContainer container) {
//        this.container = container;
////        init();
//        setModal(true);
//    }
    @PostConstruct
    public void init() {
        addStyleName("profile-window");
        setModal(true);
        layout = new FormLayout();
        layout.setSizeFull();
        layout.setSpacing(true);

        bSalvar = new Button("Salvar");
        bSalvar.addClickListener(this);
        bSalvar.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        bCancelar = new Button("Cancelar");
        bCancelar.addClickListener(this);
        bCancelar.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        bExcluir = new Button("Excluir");
        bExcluir.addClickListener(this);
        bExcluir.setVisible(false);

        buttons = new HorizontalLayout();
        buttons.addComponent(bSalvar);
        buttons.addComponent(bCancelar);
        buttons.addComponent(bExcluir);

        setContent(layout);
        firstNameField = new TextField("Nome");
        layout.addComponent(firstNameField);
        partidoField = new ComboBox("Acao", acaoContainer);
        partidoField.setInputPrompt("Escolha uma Acao");
        partidoField.setItemCaptionPropertyId("sigla");
        partidoField.setConverter(new SingleSelectConverter<Acao>(partidoField));
//        partidoField.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        partidoField.setNewItemsAllowed(true);
        partidoField.setNewItemHandler(new MyNewItemHandler());
        layout.addComponent(partidoField);

        layout.addComponent(buttons);
        binder = new BeanFieldGroup<>(Anomalia.class);
        binder.bindMemberFields(this);
        setHeight("400px");
        setWidth("300px");
    }

    public void create() {
        setCaption("Novo Vereador");
        bindingFields(new Anomalia());
        UI.getCurrent().addWindow(this);
    }

    public void edit(String id) {
        try {
            setCaption("Editar Vereador");
            Anomalia m = container.getItem(id).getEntity();
            bindingFields(m);
            bExcluir.setVisible(true);
            UI.getCurrent().addWindow(this);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Notification.show("Não há Vereador a editar!\n" + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void bindingFields(Anomalia m) {
        binder.setItemDataSource(m);
//        Field<?> field = null;
//        field = binder.buildAndBind("Nome", "nome");
//        field.setWidth("100%");
//        layout.addComponent(field);
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
                //log.debug("Mercadoria persistida!");
                Notification.show("Vereador cadastrado!", Notification.Type.HUMANIZED_MESSAGE);
            } catch (UnsupportedOperationException | IllegalStateException e) {
                Logger.getLogger(AnomaliaWindow.class.getSimpleName()).log(Level.SEVERE, "", e);
                Notification.show("Nao consegui salvar o equipamento!\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                return;
            }
        } else if (event.getButton() == bExcluir) {
//            container.removeItem(binder.getItemDataSource().getBean().getId());            
        } else if (event.getButton() == bCancelar) {
            binder.discard();
        }
        close();
    }

    private class MyNewItemHandler implements AbstractSelect.NewItemHandler {

        @Override
        public void addNewItem(String newItemCaption) {
            List<Object> allEntityIdentifiers = acaoContainer.getEntityProvider().getAllEntityIdentifiers(acaoContainer, new Compare.Equal("sigle", newItemCaption), null);
            if (allEntityIdentifiers.isEmpty()) {
                Acao p = new Acao();
                p.setSigla(newItemCaption.toUpperCase());
                Object addEntity = acaoContainer.addEntity(p);
                partidoField.addItem(addEntity);
                partidoField.setValue(addEntity);
            }
        }

    }
}
