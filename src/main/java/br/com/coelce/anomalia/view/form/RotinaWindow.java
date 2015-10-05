/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view.form;

import br.com.coelce.anomalia.model.Operador;
import br.com.coelce.anomalia.model.Processo;
import br.com.coelce.anomalia.model.Rotina;
import br.com.coelce.anomalia.persistence.OperadorContainer;
import br.com.coelce.anomalia.persistence.ProcessoContainer;
import br.com.coelce.anomalia.persistence.RotinaContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
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
public class RotinaWindow extends Window implements Button.ClickListener {

    @PropertyId("nome")
    private TextField nomeField;
    @PropertyId("descricao")
    private TextArea descricaoField;
    @PropertyId("operador")
    private ComboBox operadorCombo;
    @PropertyId("processo")
    private ComboBox processoCombo;

    private FormLayout layout;
    private BeanFieldGroup<Rotina> binder;
    private HorizontalLayout buttons;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private RotinaContainer container;
    @Inject
    private OperadorContainer operadorContainer;
    @Inject
    private ProcessoContainer processoContainer;

    @PostConstruct
    public void init() {
        Page.getCurrent().setTitle("Rotinas | Gestão da Rotina");
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

        processoCombo = new ComboBox("Processo", processoContainer);
        processoCombo.setItemCaptionPropertyId("nome");
        processoCombo.setNullSelectionAllowed(false);
        processoCombo.setConverter(new SingleSelectConverter<Processo>(processoCombo));
        layout.addComponent(processoCombo);

        operadorCombo = new ComboBox("Operador", operadorContainer);
        operadorCombo.setItemCaptionPropertyId("nome");
        operadorCombo.setNullSelectionAllowed(false);
        operadorCombo.setConverter(new SingleSelectConverter<Operador>(operadorCombo));
        layout.addComponent(operadorCombo);

        layout.addComponent(buttons);
        binder = new BeanFieldGroup<>(Rotina.class);
        binder.bindMemberFields(this);

    }

    public void create() {
        setCaption("Nova rotina");
        bindingFields(new Rotina());
        UI.getCurrent().addWindow(this);
    }

    public void edit(String id) {
        try {
            setCaption("Editar rotina");
            Rotina m = container.getItem(id).getEntity();
            bindingFields(m);
            bExcluir.setVisible(true);
            UI.getCurrent().addWindow(this);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Notification.show("Não consegui abrir a rotina para edição!\n", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void bindingFields(Rotina m) {
        binder.setItemDataSource(m);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == bSalvar) {
            try {
                binder.commit();
            } catch (FieldGroup.CommitException ex) {
                Logger.getLogger(RotinaWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                container.addEntity(binder.getItemDataSource().getBean());
                Notification.show("Nova rotina cadastrada!", Notification.Type.HUMANIZED_MESSAGE);
            } catch (UnsupportedOperationException | IllegalStateException e) {
                Logger.getLogger(AreaWindow.class.getSimpleName()).log(Level.SEVERE, "", e);
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
