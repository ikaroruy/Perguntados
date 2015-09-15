/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.security;

import br.com.coelce.anomalia.model.Usuario;
import com.vaadin.cdi.UIScoped;
import java.io.Serializable;

/**
 *
 * @author dunkelheit
 */
@UIScoped
public class UserInfo implements Serializable {

    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}