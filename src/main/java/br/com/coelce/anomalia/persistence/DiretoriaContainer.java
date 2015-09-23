/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.persistence;

import br.com.coelce.anomalia.model.Area;
import br.com.coelce.anomalia.model.Diretoria;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.jndijta.JndiAddressesImpl;
import com.vaadin.addon.jpacontainer.provider.jndijta.MutableEntityProvider;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.persistence.PersistenceContext;

/**
 *
 * @author dunkelheit
 */
@Dependent
@PersistenceContext(name = "myFooEntityManagerFactory", unitName = "anomaliaPU")
public class DiretoriaContainer extends JPAContainer<Diretoria> {

//    @PersistenceContext
//    EntityManager em;
    public DiretoriaContainer() {
        super(Diretoria.class);
    }

    @PostConstruct
    void init() {
        setEntityProvider(
                new MutableEntityProvider<>(Diretoria.class,
                        new JndiAddressesImpl(
                                "java:comp/UserTransaction", "java:comp/env/myFooEntityManagerFactory"
                        )));
    }
}

