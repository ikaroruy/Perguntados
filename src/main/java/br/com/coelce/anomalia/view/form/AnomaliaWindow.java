/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view.form;

import br.com.coelce.anomalia.model.Anomalia;
import br.com.coelce.anomalia.model.Area;
import br.com.coelce.anomalia.model.Locais;
import br.com.coelce.anomalia.model.Operador;
import br.com.coelce.anomalia.persistence.AnomaliaContainer;
import br.com.coelce.anomalia.persistence.AreaContainer;
import br.com.coelce.anomalia.persistence.LocaisContainer;
import br.com.coelce.anomalia.persistence.OperadorContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Date;
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

    @PropertyId("operador")
    private ComboBox operadorCombo;
    @PropertyId("area")
    private ComboBox areaCombo;
    @PropertyId("dataOcorrencia")
    private DateField dataField;
    @PropertyId("local")
    private ComboBox localCombo;
    @PropertyId("texto1")
    private TextArea text1Area;
    @PropertyId("texto2")
    private TextArea text2Area;
    @PropertyId("texto3")
    private TextArea text3Area;
    @PropertyId("texto4")
    private TextArea text4Area;

    private VerticalLayout layout;
    private VerticalLayout verticalLayout;
    private HorizontalLayout comboLayout;
    private BeanFieldGroup<Anomalia> binder;
    private HorizontalLayout buttons;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private AnomaliaContainer container;
    @Inject
    private AreaContainer areaContainer;
    @Inject
    private OperadorContainer operadorContainer;
    @Inject
    private LocaisContainer locaisContainer;

    @PostConstruct
    public void init() {
        Page.getCurrent().setTitle("Anomalias | Gestão da Rotina");
        addStyleName("profile-window");
        setModal(true);
        setSizeFull();
        
        
        setResizable(false);
        comboLayout = new HorizontalLayout();
        comboLayout.setSpacing(true);
        comboLayout.setMargin(true);
        comboLayout.setSizeFull();
        
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeUndefined();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        
        layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setMargin(true);
        layout.addComponent(comboLayout);
        layout.addComponent(verticalLayout);
        setContent(layout);
        

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
        buttons.setMargin(true);
        buttons.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        buttons.addComponent(bSalvar);
        buttons.addComponent(bCancelar);
        buttons.addComponent(bExcluir);

        areaCombo = new ComboBox("Area", areaContainer);
//        area.setRequired(true);
//        area.setRequiredError("campo obrigatório");
        areaCombo.setItemCaptionPropertyId("nome");
        areaCombo.setNullSelectionAllowed(false);
        areaCombo.setConverter(new SingleSelectConverter<Area>(areaCombo));
        comboLayout.addComponent(areaCombo);
       

        operadorCombo = new ComboBox("Operador", operadorContainer);
//        operador.setRequired(true);
//        operador.setRequiredError("campo obrigatório");
        operadorCombo.setItemCaptionPropertyId("nome");
        operadorCombo.setNullSelectionAllowed(false);
        operadorCombo.setConverter(new SingleSelectConverter<Operador>(operadorCombo));
        comboLayout.addComponent(operadorCombo);

        dataField = new DateField("Data da Ocorrência");
//        data.setRequired(true);
//        data.setRequiredError("campo obrigatório");
        dataField.setDateFormat("dd/MM/yyyy");
        dataField.setRangeEnd(new Date());
        comboLayout.addComponent(dataField);
        
        localCombo = new ComboBox("Local", locaisContainer);
//        localCombo.setRequired(true);
//        localCombo.setRequiredError("campo obrigatório");
        localCombo.setItemCaptionPropertyId("nome");
        localCombo.setNullSelectionAllowed(false);
        localCombo.setConverter(new SingleSelectConverter<Locais>(localCombo));
        comboLayout.addComponent(localCombo);
        
        text1Area = new TextArea("O que aconteceu de diferente no serviço?");
        text1Area.setWidth(775, Unit.PIXELS);
        text1Area.setNullRepresentation("");
        verticalLayout.addComponent(text1Area);
        
        text2Area = new TextArea("O que pode ter gerado o problema?");
        text2Area.setWidth(775, Unit.PIXELS);
        text2Area.setNullRepresentation("");
        verticalLayout.addComponent(text2Area);
        
        text3Area = new TextArea("Foi feito alguma coisa para corrigir o problema?");
        text3Area.setWidth(775, Unit.PIXELS);
        text3Area.setNullRepresentation("");
        verticalLayout.addComponent(text3Area);
        
        text4Area = new TextArea("Se não, o que você sugere que seja feito?");
        text4Area.setWidth(775, Unit.PIXELS);
        text4Area.setNullRepresentation("");
        verticalLayout.addComponent(text4Area);
        
        
        layout.addComponent(buttons);
        binder = new BeanFieldGroup<>(Anomalia.class);
        binder.bindMemberFields(this);

    }

    public void create() {
        setCaption("Nova anomalia");
        bindingFields(new Anomalia());
        UI.getCurrent().addWindow(this);
    }

    public void edit(String id) {
        try {
            setCaption("Editar anomalia");
            Anomalia m = container.getItem(id).getEntity();
            bindingFields(m);
            bExcluir.setVisible(true);
            UI.getCurrent().addWindow(this);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Notification.show("Não há anomalia a editar!\n", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void bindingFields(Anomalia m) {
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
                Notification.show("Anomalia cadastrada!", Notification.Type.HUMANIZED_MESSAGE);
            } catch (UnsupportedOperationException | IllegalStateException e) {
                Logger.getLogger(AnomaliaWindow.class.getSimpleName()).log(Level.SEVERE, "", e);
                Notification.show("Nao consegui salvar a anomalia!\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
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
            List<Object> allEntityIdentifiers = areaContainer.getEntityProvider().getAllEntityIdentifiers(areaContainer, new Compare.Equal("id", newItemCaption), null);

            if (allEntityIdentifiers.isEmpty()) {
                Area p = new Area();
                p.setNome(newItemCaption.toUpperCase());
                Object addEntity = areaContainer.addEntity(p);
                areaCombo.addItem(addEntity);
                areaCombo.setValue(addEntity);
            }
        }

    }
}
