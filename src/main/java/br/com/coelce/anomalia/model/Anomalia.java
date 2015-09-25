/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author dunkelheit
 */
@Entity
public class Anomalia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataOcorrencia;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCorrecao;
    
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaOcorrencia;

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

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    
    public Date getHoraOcorrencia() {
        return horaOcorrencia;
    }

    public void setHoraOcorrencia(Date horaOcorrencia) {
        this.horaOcorrencia = horaOcorrencia;
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
}
