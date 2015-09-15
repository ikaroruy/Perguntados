/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.persistence;

import br.com.coelce.anomalia.model.Identified;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;


/**
 *
 * @author dunkelheit
 * @param <T>
 */
public abstract class AbstractDAO<T extends Identified> {

    private final Class<T> entityClass;
    @Resource
    UserTransaction utx;

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public T save(T entity) throws Exception {
        T persistedEntity;
        utx.begin();
        if (entity.getId() == null || entity.getId().trim().isEmpty()) {
            getEntityManager().persist(entity);
            persistedEntity = entity;
        } else {
            persistedEntity = getEntityManager().merge(entity);
        }
        utx.commit();
        return persistedEntity;
    }

    public void remove(String id) throws Exception {
        utx.begin();
        getEntityManager().remove(find(id));
        utx.commit();
    }

    public T find(String id) {
        return getEntityManager().find(this.entityClass, id);
    }
}
