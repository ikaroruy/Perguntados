/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.view;

import br.com.coelce.anomalia.event.NavigationEvent;
import br.com.coelce.anomalia.model.Usuario;
import br.com.coelce.anomalia.persistence.UsuarioDAO;
import br.com.coelce.anomalia.security.UserInfo;
import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author pr02nl
 */
@CDIView("login")
public class LoginView extends VerticalLayout implements View, ClickListener {

    private static final Logger LOGGER = Logger.getLogger(LoginView.class.getSimpleName());
    @Inject
    private UserInfo user;
    private TextField usernameField;
    private PasswordField passwordField;
    @Inject
    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;
    @Inject
    private UsuarioDAO usuarioDAO;
    private Component loginForm;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        getUI().addStyleName("loginview");
        setSizeFull();
//        VerticalLayout layout = new VerticalLayout();
//        setCompositionRoot(layout);
//        layout.setSizeFull();
//        layout.setMargin(true);
//        layout.setSpacing(true);

        if (loginForm == null) {
            loginForm = buildLoginForm();
            addComponent(loginForm);
            setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        }
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        return loginPanel;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Bem Vindo");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("Sistema Gestão da Rotina");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        usernameField = new TextField("Login");
        usernameField.setIcon(FontAwesome.USER);
        usernameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        usernameField.focus();

        passwordField = new PasswordField("Senha");
        passwordField.setIcon(FontAwesome.LOCK);
        passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Entrar");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);

        fields.addComponents(usernameField, passwordField, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(this);
        return fields;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        Usuario findByLogin;
//        teste();
        try {
            findByLogin = usuarioDAO.findByLogin(username);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro com o banco de dados", e);
            Notification.show("Houve um problema com o banco de dados, por favor contate o administrador!", Notification.Type.ERROR_MESSAGE);
            return;
        }
        if (findByLogin == null) {
            LOGGER.log(Level.WARNING, "Tentativa de login com usu\u00e1rio inexistente: {0}", username);
            Notification.show("Esse nome de login não existe!", Notification.Type.ERROR_MESSAGE);
            return;
        }
        if (!usuarioDAO.passwordMatches(findByLogin, password)) {
            LOGGER.log(Level.WARNING, "Login: {0} com senha errada", username);
            Notification.show("Desculpe-me, mas a senha está incorreta.", Notification.Type.ERROR_MESSAGE);
            return;
        }
        user.setUsuario(findByLogin);
        navigationEvent.fire(new NavigationEvent("parlamentar"));
    }

    private void teste() {
        Notification notification = new Notification(
                "Welcome to Dashboard Demo");
        notification
                .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
        notification.show(Page.getCurrent());
    }
}
