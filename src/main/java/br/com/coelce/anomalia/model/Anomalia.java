/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author dunkelheit
 */
@Entity
public class Anomalia extends Identified {
     private String nome;
    @ManyToOne
    private Acao acao;
    private boolean presidente;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Acao getPartido() {
        return acao;
    }

    public void setPartido(Acao acao) {
        this.acao = acao;
    }

    public boolean isPresidente() {
        return presidente;
    }

    public void setPresidente(boolean presidente) {
        this.presidente = presidente;
    }
    
}
