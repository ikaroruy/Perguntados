/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.security;

import br.com.coelce.perguntados.model.Perfil;
import com.vaadin.cdi.access.AccessControl;
import java.io.Serializable;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

/**
 *
 * @author dunkelheit
 */
@Alternative
public class CustomAccessControl extends AccessControl implements Serializable {

    @Inject
    private UserInfo userInfo;

    @Override
    public boolean isUserSignedIn() {
        return userInfo.getUsuario() != null;
    }
    
    @Override
    public boolean isUserInRole(String string) {
        if (isUserSignedIn()) {
            Perfil perfil = userInfo.getUsuario().getPerfil();
           
                if (perfil.name().equals(string)) {
                    return true;
                }
           
        }
        return false;
    }


    @Override
    public String getPrincipalName() {
        if (isUserSignedIn()) {
            return userInfo.getUsuario().getLogin();
        }
        return null;
    }

}
