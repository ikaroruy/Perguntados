/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package br.com.coelce.anomalia.view.form;
//
//import br.com.coelce.anomalia.model.Permissao;
//import br.com.coelce.anomalia.model.Permissoes;
//import br.com.coelce.anomalia.persistence.PermissaoContainer;
//import com.vaadin.data.fieldgroup.BeanFieldGroup;
//import com.vaadin.data.fieldgroup.FieldGroup;
//import com.vaadin.data.fieldgroup.PropertyId;
//import com.vaadin.event.ShortcutAction;
//import com.vaadin.server.Page;
//import com.vaadin.shared.ui.window.WindowMode;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.FormLayout;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Notification;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.UI;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.annotation.PostConstruct;
//import javax.inject.Inject;
//
///**
// *
// * @author dunkelheit
// */
//public class PermissoesWindow extends Window implements Button.ClickListener {
//
//    @PropertyId("tipoDePermissao")
//    private TextField tipoPermissaoField;
//    private VerticalLayout layout;
//    private BeanFieldGroup<Permissao> binder;
//    private HorizontalLayout buttons;
//    private Button bSalvar;
//    private Button bCancelar;
//    private Button bExcluir;
//    @Inject 
//    PermissaoContainer container;
//
//    @PostConstruct
//    public void init() {
//        Page.getCurrent().setTitle("Permissões | Gestão da Rotina");
//        addStyleName("profile-window");
//        setModal(true);
//        setSizeFull();
//        
//        setResizable(false);
//        layout = new VerticalLayout();
//        layout.setSizeUndefined();
//        layout.setMargin(true);
//        layout.setSpacing(true);
//
//        bSalvar = new Button("Salvar");
//        bSalvar.addClickListener(this);
//        bSalvar.setClickShortcut(ShortcutAction.KeyCode.ENTER);
//
//        bCancelar = new Button("Cancelar");
//        bCancelar.addClickListener(this);
//        bCancelar.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
//
//        bExcluir = new Button("Excluir");
//        bExcluir.addClickListener(this);
//        bExcluir.setVisible(false);
//
//        buttons = new HorizontalLayout();
//        buttons.setSpacing(true);
//        buttons.addComponent(bSalvar);
//        buttons.addComponent(bCancelar);
//        buttons.addComponent(bExcluir);
//
//        setContent(layout);
//        tipoPermissaoField = new TextField("Tipo de Permissão");
//        tipoPermissaoField.setNullRepresentation("");
//        layout.addComponent(tipoPermissaoField);
//        
//        layout.addComponent(buttons);
//        binder = new BeanFieldGroup<>(Permissao.class);
//        binder.bindMemberFields(this);
//    }
//
//    public void create() {
//        setCaption("Nova permissão");
//        bindingFields(new Permissao());
//        UI.getCurrent().addWindow(this);
//    }
//
//    public void edit(String id) {
//        try {
//            setCaption("Editar permissão");
//            Permissao m = container.getItem(id).getEntity();
//            bindingFields(m);
//            bExcluir.setVisible(true);
//            UI.getCurrent().addWindow(this);
//        } catch (IllegalArgumentException | NullPointerException ex) {
//            Notification.show("Não consegui abrir permissão para edição!\n", Notification.Type.ERROR_MESSAGE);
//        }
//    }
//
//    private void bindingFields(Permissao m) {
//        binder.setItemDataSource(m);
//    }
//
//    @Override
//    public void buttonClick(Button.ClickEvent event) {
//        if (event.getButton() == bSalvar) {
//            try {
//                binder.commit();
//            } catch (FieldGroup.CommitException ex) {
//                Logger.getLogger(PermissoesWindow.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                container.addEntity(binder.getItemDataSource().getBean());
//                Notification.show("Nova permissão cadastrada!", Notification.Type.HUMANIZED_MESSAGE);
//            } catch (UnsupportedOperationException | IllegalStateException e) {
//                Logger.getLogger(UsuarioWindow.class.getSimpleName()).log(Level.SEVERE, "", e);
//                Notification.show("Houve um problema durante o salvamento!\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
//                return;
//            }
//        } else if (event.getButton() == bExcluir) {
//            container.removeItem(binder.getItemDataSource().getBean().getId());
//        } else if (event.getButton() == bCancelar) {
//            binder.discard();
//        }
//        close();
//    }
//}
