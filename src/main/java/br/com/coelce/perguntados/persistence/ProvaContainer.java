/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.persistence;

import br.com.coelce.perguntados.model.Prova;
import br.com.coelce.perguntados.model.Questao;
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
@PersistenceContext(name = "myFooEntityManagerFactory", unitName = "perguntadosPU")
public class ProvaContainer extends JPAContainer<Prova> {

//    @PersistenceContext
//    EntityManager em;
    public ProvaContainer() {
        super(Prova.class);
    }

    @PostConstruct
    void init() {
        setEntityProvider(
                new MutableEntityProvider<>(Prova.class,
                        new JndiAddressesImpl(
                                "java:comp/UserTransaction", "java:comp/env/myFooEntityManagerFactory"
                        )));
    }
}
