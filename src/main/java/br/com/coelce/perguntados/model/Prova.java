/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author dunkelheit
 */
@Entity
public class Prova extends Identified {

    
   
    
  
    private String pontos;

    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
    }
    
    
    private String aluno;

    public String getAluno() {
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

   
}
