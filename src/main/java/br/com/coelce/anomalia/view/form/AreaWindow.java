/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view.form;

import br.com.coelce.anomalia.model.Area;
import br.com.coelce.anomalia.persistence.AreaContainer;
import br.com.coelce.anomalia.persistence.DiretoriaContainer;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
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
public class AreaWindow extends Window implements Button.ClickListener{

    @PropertyId("nome")
    private TextField nome;
    @PropertyId("descricao")
    private TextField descricao;
    
    private ComboBox diretoria;
    private Embedded image;
    
    private FormLayout layout;
    private BeanFieldGroup<Area> binder;
    private HorizontalLayout buttons;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private AreaContainer container;
    @Inject
    private DiretoriaContainer diretoriaContainer;

//    public ParlamentarWindow(ParlamentarContainer container) {
//        this.container = container;
////        init();
//        setModal(true);
//    }
    @PostConstruct
    public void init() {
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
        buttons.addComponent(bSalvar);
        buttons.addComponent(bCancelar);
        buttons.addComponent(bExcluir);

        setContent(layout);
        nome = new TextField("Nome");
        nome.setNullRepresentation("");
        layout.addComponent(nome);
        descricao = new TextField("Descrição");
        descricao.setNullRepresentation("");
        layout.addComponent(descricao);
        diretoria = new ComboBox("Diretoria", diretoriaContainer);
        layout.addComponent(diretoria);
        
        
        layout.addComponent(buttons);
        binder = new BeanFieldGroup<>(Area.class);
        binder.bindMemberFields(this);
    }

    public void create() {
        setCaption("Nova área");
        bindingFields(new Area());
        UI.getCurrent().addWindow(this);
    }

    public void edit(String id) {
        try {
            setCaption("Editar área");
            Area m = container.getItem(id).getEntity();
            bindingFields(m);
            bExcluir.setVisible(true);
            UI.getCurrent().addWindow(this);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Notification.show("Não consegui abrir área para edição!\n" + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void bindingFields(Area m) {
        binder.setItemDataSource(m);
//        if (!StringUtils.INSTANCE.isNullOrBlank(m.getDescricao())) {
//            image.setSource(new FileResource(new File(m.getDescricao())));
//        }
//        Field<?> field = null;
//        field = binder.buildAndBind("Nome", "tipo");
//        field.setWidth("100%");
//        layout.addComponent(field);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == bSalvar) {
            try {
                binder.commit();
            } catch (FieldGroup.CommitException ex) {
                Logger.getLogger(AreaWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                container.addEntity(binder.getItemDataSource().getBean());
                //log.debug("Mercadoria persistida!");
                Notification.show("Nova área cadastrada!", Notification.Type.HUMANIZED_MESSAGE);
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
