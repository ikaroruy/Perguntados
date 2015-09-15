/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 *
 * @author dunkelheit
 */
@Entity
public class Permissoes extends Identified {

    @ManyToMany(mappedBy = "permissoes")
    private List<Usuario> usuarios;
    private TipoPermissao tipoPermissao;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public TipoPermissao getTipoPermissao() {
        return tipoPermissao;
    }

    public void setTipoPermissao(TipoPermissao tipoPermissao) {
        this.tipoPermissao = tipoPermissao;
    }

}
