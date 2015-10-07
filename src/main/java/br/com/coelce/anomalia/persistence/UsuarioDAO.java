/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.anomalia.persistence;

import br.com.coelce.anomalia.model.Permissoes;
import br.com.coelce.anomalia.model.TipoPermissao;
import br.com.coelce.anomalia.model.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author dunkelheit
 */
@Dependent
public class UsuarioDAO extends AbstractDAO<Usuario> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getSimpleName());
    @PersistenceContext
    private EntityManager em;

    public UsuarioDAO() {
        super(Usuario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Usuario findByLogin(String userLogin) {
        try {
            Object singleResult = em.createQuery("select a from Usuario a where a.login = :userAccount")
                    .setParameter("userAccount", userLogin)
                    .getSingleResult();
            return (Usuario) singleResult;
        } catch (NoResultException nre) {
            LOGGER.log(Level.INFO, nre.getMessage(), nre);
            if ("admin".equals(userLogin)) {
                Usuario u = new Usuario();
                u.setLogin("admin");
                u.setSenha("123456");
                try {
//                    save(u);
                    Usuario save = save(u);
                     save.setPermissoes(new ArrayList<Permissoes>());
                     Permissoes permissoes = new Permissoes();
                     permissoes.setTipoPermissao(TipoPermissao.ADMIN);
                     permissoes.setUsuarios(new ArrayList<Usuario>());
                     permissoes.getUsuarios().add(save);
                     em.persist(permissoes);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, nre.getMessage(), nre);
                }
            }
            return null;
        }
    }

    public Boolean passwordMatches(Usuario userAccount, String passwordToCheck) {
        try {
            Usuario authentication = new Usuario();
            authentication = (Usuario) em.createQuery("select a from Usuario a where a.login = :userAccount and a.senha = :password")
                    .setParameter("userAccount", userAccount.getLogin())
                    .setParameter("password", authentication.hashPassword(passwordToCheck))
                    .getSingleResult();
            if (authentication != null) {
                return Boolean.TRUE;
            }
        } catch (NoResultException nre) {
            LOGGER.log(Level.INFO, nre.getMessage(), nre);
            return Boolean.FALSE;
        }

        return Boolean.FALSE;
    }
}
