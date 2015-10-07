/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dunkelheit
 */
@Entity
public class Anomalia extends Identified {

    @Temporal(TemporalType.DATE)
    private Date dataOcorrencia;

    @Temporal(TemporalType.DATE)
    private Date dataCorrecao;
   
    private String status;
    
    @ManyToOne
    private Acao acao;

    @ManyToOne
    private TipoAnomalia tipoAnomalia;
    
    @ManyToOne
    private Rotina rotina;
    
    @ManyToOne
    private Area area;

    @ManyToOne
    private Diretoria diretoria;
    
    @ManyToOne
    private Operador operador;
    
    @ManyToOne
    private Locais local;

    private String texto1;
    
    private String texto2;
    
    private String texto3;
    
    private String texto4;
    
    public Date getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(Date dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public Date getDataCorrecao() {
        return dataCorrecao;
    }

    public void setDataCorrecao(Date dataCorrecao) {
        this.dataCorrecao = dataCorrecao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }
    
    public TipoAnomalia getTipoAnomalia() {
        return tipoAnomalia;
    }

    public void setTipoAnomalia(TipoAnomalia tipoAnomalia) {
        this.tipoAnomalia = tipoAnomalia;
    }
    
    
     public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }
    
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Diretoria getDiretoria() {
        return diretoria;
    }

    public void setDiretoria(Diretoria diretoria) {
        this.diretoria = diretoria;
    }
    
    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public String getTexto1() {
        return texto1;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTexto2() {
        return texto2;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public String getTexto3() {
        return texto3;
    }

    public void setTexto3(String texto3) {
        this.texto3 = texto3;
    }

    public String getTexto4() {
        return texto4;
    }

    public void setTexto4(String texto4) {
        this.texto4 = texto4;
    }
    
    public Locais getLocal() {
        return local;
    }

    public void setLocal(Locais local) {
        this.local = local;
    }
}
