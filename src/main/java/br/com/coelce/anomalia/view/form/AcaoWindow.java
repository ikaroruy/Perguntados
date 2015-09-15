/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view.form;

import br.com.coelce.anomalia.business.ImageUploader;
import br.com.coelce.anomalia.model.Acao;
import br.com.coelce.anomalia.persistence.AcaoContainer;
import br.com.coelce.anomalia.util.StringUtils;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
public class AcaoWindow extends Window implements Button.ClickListener {

    @PropertyId("sigla")
    private TextField siglaField;
    @PropertyId("nome")
    private TextField nomeField;
    private Upload upload;
    private ProgressBar progressBar;
    private Embedded image;
    private ImageUploader imageUploader;

    private FormLayout layout;
    private BeanFieldGroup<Acao> binder;
    private HorizontalLayout buttons;
    private Button bSalvar;
    private Button bCancelar;
    private Button bExcluir;
    @Inject
    private AcaoContainer container;

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
        siglaField = new TextField("Sigla");
        layout.addComponent(siglaField);
        nomeField = new TextField("Nome");
        layout.addComponent(nomeField);
        image = new Embedded("Logotipo", new ThemeResource("img/profile-pic-300px.jpg"));
        layout.addComponent(image);
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        layout.addComponent(progressBar);
        imageUploader = new ImageUploader(image, progressBar);
        upload = new Upload("Envie o logotipo", imageUploader);
        upload.setButtonCaption("Enviar");
        upload.addStartedListener(imageUploader);
        upload.addProgressListener(imageUploader);
        upload.addFailedListener(imageUploader);
        upload.addSucceededListener(imageUploader);
        upload.addFinishedListener(imageUploader);
        layout.addComponent(upload);
        
        layout.addComponent(buttons);
        binder = new BeanFieldGroup<>(Acao.class);
        binder.bindMemberFields(this);
        setHeight("400px");
        setWidth("300px");
    }

    public void create() {
        setCaption("Novo Partido");
        bindingFields(new Acao());
        UI.getCurrent().addWindow(this);
    }

    public void edit(String id) {
        try {
            setCaption("Editar Partido");
            Acao m = container.getItem(id).getEntity();
            bindingFields(m);
            bExcluir.setVisible(true);
            UI.getCurrent().addWindow(this);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Notification.show("Não consegui abrir o equipamento para edição!\n" + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void bindingFields(Acao m) {
        binder.setItemDataSource(m);
        if (!StringUtils.INSTANCE.isNullOrBlank(m.getLogotipo())) {
            image.setSource(new FileResource(new File(m.getLogotipo())));
        }
//        Field<?> field = null;
//        field = binder.buildAndBind("Nome", "nome");
//        field.setWidth("100%");
//        layout.addComponent(field);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == bSalvar) {
            try {
                if (image.getSource() instanceof FileResource) {
                    FileResource fr = (FileResource) image.getSource();
                    binder.getItemDataSource().getBean().setLogotipo(fr.getFilename());
                }
                binder.commit();
            } catch (FieldGroup.CommitException e) {
                Notification.show("Preencha o formulário corretamente");
                return;
            }
            try {
                container.addEntity(binder.getItemDataSource().getBean());
                //log.debug("Mercadoria persistida!");
                Notification.show("Partido cadastrado!", Notification.Type.HUMANIZED_MESSAGE);
            } catch (UnsupportedOperationException | IllegalStateException e) {
                Logger.getLogger(AcaoWindow.class.getSimpleName()).log(Level.SEVERE, "", e);
                Notification.show("Houve um problema durante o salvamento!\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                return;
            }
        } else if (event.getButton() == bExcluir) {
            container.removeItem(binder.getItemDataSource().getBean().getId());
        } else if (event.getButton() == bCancelar) {
            if (image.getSource() instanceof FileResource) {
                FileResource fr = (FileResource) image.getSource();
                File sourceFile = fr.getSourceFile();
                sourceFile.delete();
            }
            binder.discard();
        }
        close();
    }
}
