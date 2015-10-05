/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view.form;

import br.com.coelce.anomalia.model.Diretoria;
import br.com.coelce.anomalia.persistence.DiretoriaContainer;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
public class DiretoriaWindow extends Window implements Button.ClickListener {

    @PropertyId("nome")
    private TextField nomeField;
    @PropertyId("descricao")
    private TextArea descricaoField;
    private FormLayout layout;
    private BeanFieldGroup<Diretoria> binder;
    private HorizontalLayout buttons;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private DiretoriaContainer container;

    @PostConstruct
    public void init() {
        Page.getCurrent().setTitle("Diretoria | Gestão da Rotina");
        addStyleName("profile-window");
        setModal(true);
        setWindowMode(WindowMode.MAXIMIZED);
        layout = new FormLayout();
        layout.setSizeUndefined();
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
        buttons.setSpacing(true);
        buttons.addComponent(bSalvar);
        buttons.addComponent(bCancelar);
        buttons.addComponent(bExcluir);

        setContent(layout);
        nomeField = new TextField("Nome");
        nomeField.setNullRepresentation("");
        layout.addComponent(nomeField);

        descricaoField = new TextArea("Descrição");
        descricaoField.setWidth(775, Unit.PIXELS);
        descricaoField.setNullRepresentation("");
        layout.addComponent(descricaoField);

        layout.addComponent(buttons);
        binder = new BeanFieldGroup<>(Diretoria.class);
        binder.bindMemberFields(this);
    }

    public void create() {
        setCaption("Nova diretoria");
        bindingFields(new Diretoria());
        UI.getCurrent().addWindow(this);
    }

    public void edit(String id) {
        try {
            setCaption("Editar diretoria");
            Diretoria m = container.getItem(id).getEntity();
            bindingFields(m);
            bExcluir.setVisible(true);
            UI.getCurrent().addWindow(this);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Notification.show("Não consegui abrir diretoria para edição!\n", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void bindingFields(Diretoria m) {
        binder.setItemDataSource(m);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == bSalvar) {
            try {
                binder.commit();
            } catch (FieldGroup.CommitException ex) {
                Logger.getLogger(DiretoriaWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                container.addEntity(binder.getItemDataSource().getBean());
                Notification.show("Nova diretoria cadastrada!", Notification.Type.HUMANIZED_MESSAGE);
            } catch (UnsupportedOperationException | IllegalStateException e) {
                Logger.getLogger(DiretoriaWindow.class.getSimpleName()).log(Level.SEVERE, "", e);
                Notification.show("Houve um problema durante o salvamento!\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                return;
            }
        } else if (event.getButton() == bExcluir) {
            container.removeItem(binder.getItemDataSource().getBean().getId());
        } else if (event.getButton() == bCancelar) {
            binder.discard();
        }
        close();
    }
}
