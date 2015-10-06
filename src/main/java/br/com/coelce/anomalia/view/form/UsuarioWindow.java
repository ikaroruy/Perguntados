/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view.form;

import br.com.coelce.anomalia.model.Diretoria;
import br.com.coelce.anomalia.model.Permissoes;
import br.com.coelce.anomalia.model.Usuario;
import br.com.coelce.anomalia.persistence.DiretoriaContainer;
import br.com.coelce.anomalia.persistence.PermissaoContainer;
import br.com.coelce.anomalia.persistence.UsuarioContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
public class UsuarioWindow extends Window implements Button.ClickListener {

    @PropertyId("nome")
    private TextField nomeField;
    @PropertyId("senha")
    private PasswordField passwordField;
    @PropertyId("confirmPassword")
    private PasswordField conPasswordField;
    @PropertyId("permissoes")
    private ComboBox permissaoCombo;
    private VerticalLayout layout;
    private BeanFieldGroup<Usuario> binder;
    private HorizontalLayout buttons;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private UsuarioContainer container;
    @Inject 
    PermissaoContainer permissaoContainer;

    @PostConstruct
    public void init() {
        Page.getCurrent().setTitle("Usuários | Gestão da Rotina");
        addStyleName("profile-window");
        setModal(true);
        setSizeFull();
        
        setResizable(false);
        layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setMargin(true);
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
        nomeField = new TextField("Login");
        nomeField.setNullRepresentation("");
        layout.addComponent(nomeField);
        
        passwordField = new PasswordField("Senha");
        passwordField.setNullRepresentation("");
        layout.addComponent(passwordField);
        
        conPasswordField = new PasswordField("Confirmar senha");
        passwordField.setNullRepresentation("");
        layout.addComponent(conPasswordField);
        
        permissaoCombo = new ComboBox("Permissão", permissaoContainer);
//        area.setRequired(true);
//        area.setRequiredError("campo obrigatório");
        permissaoCombo.setItemCaptionPropertyId("tipoPermissao");
        permissaoCombo.setNullSelectionAllowed(false);
        permissaoCombo.setConverter(new SingleSelectConverter<Permissoes>(permissaoCombo));
        layout.addComponent(permissaoCombo);
        
       
        layout.addComponent(buttons);
        binder = new BeanFieldGroup<>(Usuario.class);
        binder.bindMemberFields(this);
    }

    public void create() {
        setCaption("Novo usuário");
        bindingFields(new Usuario());
        UI.getCurrent().addWindow(this);
    }

    public void edit(String id) {
        try {
            setCaption("Editar usuário");
            Usuario m = container.getItem(id).getEntity();
            bindingFields(m);
            bExcluir.setVisible(true);
            UI.getCurrent().addWindow(this);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Notification.show("Não consegui abrir usuário para edição!\n", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void bindingFields(Usuario m) {
        binder.setItemDataSource(m);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == bSalvar) {
            try {
                binder.commit();
            } catch (FieldGroup.CommitException ex) {
                Logger.getLogger(UsuarioWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                container.addEntity(binder.getItemDataSource().getBean());
                Notification.show("Novo usuario cadastrada!", Notification.Type.HUMANIZED_MESSAGE);
            } catch (UnsupportedOperationException | IllegalStateException e) {
                Logger.getLogger(UsuarioWindow.class.getSimpleName()).log(Level.SEVERE, "", e);
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