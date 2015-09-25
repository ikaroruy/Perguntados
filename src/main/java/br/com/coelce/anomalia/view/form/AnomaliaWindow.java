/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view.form;

import br.com.coelce.anomalia.model.Anomalia;
import br.com.coelce.anomalia.model.TipoAnomalia;
import br.com.coelce.anomalia.persistence.AcaoContainer;
import br.com.coelce.anomalia.persistence.AnomaliaContainer;
import br.com.coelce.anomalia.persistence.AreaContainer;
import br.com.coelce.anomalia.persistence.DiretoriaContainer;
import br.com.coelce.anomalia.persistence.OperadorContainer;
import br.com.coelce.anomalia.persistence.RotinaContainer;
import br.com.coelce.anomalia.persistence.TipoAnomaliaContainer;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.thomas.timefield.TimeField;

/**
 *
 * @author dunkelheit
 */
public class AnomaliaWindow extends Window implements Button.ClickListener {

    private ComboBox acaoField;
    private ComboBox clasAnomaliaCombo;
    private ComboBox rotinaCombo;
    private ComboBox operadorCombo;
    private TimeField horaField;
    private ComboBox diretoriaCombo;
    private ComboBox areaCombo;
    private DateField dataField;
    private FormLayout layout;
    private GridLayout gridLayout;
    private BeanFieldGroup<Anomalia> binder;
    private HorizontalLayout buttons;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private AnomaliaContainer container;
    @Inject
    private AcaoContainer acaoContainer;
    @Inject
    private TipoAnomaliaContainer tipoAnomaliaContainer;
    @Inject
    private DiretoriaContainer diretoriaContainer;
    @Inject
    private AreaContainer areaContainer;
    @Inject
    private OperadorContainer operadorContainer;
    @Inject
    private RotinaContainer rotinaContainer;

    @PostConstruct
    public void init() {
        addStyleName("profile-window");
        setModal(true);
        setWindowMode(WindowMode.MAXIMIZED);
        gridLayout = new GridLayout(4, 2);
        gridLayout.setSpacing(true);
        gridLayout.setSizeUndefined();
        gridLayout.setMargin(true);
        layout = new FormLayout();
        layout.setSizeUndefined();
        layout.setSpacing(true);
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        layout.addComponent(gridLayout);

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
        buttons.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        buttons.addComponent(bSalvar);
        buttons.addComponent(bCancelar);
        buttons.addComponent(bExcluir);

        setContent(layout);
        clasAnomaliaCombo = new ComboBox("Classificação da Anomalia", tipoAnomaliaContainer);
//        clasAnomalia.setImmediate(true);
//        clasAnomalia.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        clasAnomaliaCombo.setItemCaptionPropertyId("tipo");
//        clasAnomalia.setRequired(true);
//        clasAnomalia.setRequiredError("campo obrigatório");
        gridLayout.addComponent(clasAnomaliaCombo);

        diretoriaCombo = new ComboBox("Diretoria", diretoriaContainer);
//        diretoria.setRequired(true);
//        diretoria.setRequiredError("campo obrigatório");
        diretoriaCombo.setItemCaptionPropertyId("nome");
        gridLayout.addComponent(diretoriaCombo);

        areaCombo = new ComboBox("Area", areaContainer);
//        area.setRequired(true);
//        area.setRequiredError("campo obrigatório");
        areaCombo.setItemCaptionPropertyId("nome");
        gridLayout.addComponent(areaCombo);

        rotinaCombo = new ComboBox("Rotina", rotinaContainer);
//        rotina.setRequired(true);
//        rotina.setRequiredError("campo obrigatório");
        rotinaCombo.setItemCaptionPropertyId("nome");
        gridLayout.addComponent(rotinaCombo);

        operadorCombo = new ComboBox("Operador", operadorContainer);
//        operador.setRequired(true);
//        operador.setRequiredError("campo obrigatório");
        operadorCombo.setItemCaptionPropertyId("nome");
        gridLayout.addComponent(operadorCombo);

        dataField = new DateField("Data da Ocorrência");
//        data.setRequired(true);
//        data.setRequiredError("campo obrigatório");
        dataField.setValue(new java.util.Date());
        gridLayout.addComponent(dataField);

        horaField = new TimeField("Hora da Ocorrência");
        horaField.set24HourClock(true);
//        hora.setRequired(true);
//        hora.setRequiredError("campo obrigatório");
        gridLayout.addComponent(horaField);

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
            Notification.show("Não há anomalia a editar!\n" + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
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
            List<Object> allEntityIdentifiers = tipoAnomaliaContainer.getEntityProvider().getAllEntityIdentifiers(tipoAnomaliaContainer, new Compare.Equal("id", newItemCaption), null);

            if (allEntityIdentifiers.isEmpty()) {
                TipoAnomalia p = new TipoAnomalia();
                p.setTipo(newItemCaption.toUpperCase());
                Object addEntity = tipoAnomaliaContainer.addEntity(p);
                clasAnomaliaCombo.addItem(addEntity);
                clasAnomaliaCombo.setValue(addEntity);
            }
        }

    }
}
