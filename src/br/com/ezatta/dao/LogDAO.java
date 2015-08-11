/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.dao;

import br.com.ezatta.model.EzattaLog;
import br.com.ezatta.util.JPAUtil;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Marcelo
 */
public class LogDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public LogDAO() {
    }

    public void addLog(EzattaLog log) throws SQLException {
        em.getTransaction().begin();
        em.persist(log);
        em.getTransaction().commit();
    }

    public void removeLog(EzattaLog emp) throws SQLException {
        em.getTransaction().begin();
        EzattaLog log = em.find(EzattaLog.class, emp.getId());
        em.remove(log);
        em.getTransaction().commit();
    }

    public void updateLog(EzattaLog log) throws SQLException {
        em.getTransaction().begin();
        em.merge(log);
        em.getTransaction().commit();
    }

    public EzattaLog getLog(int id) throws SQLException {
        EzattaLog empersa = em.find(EzattaLog.class, id);
        return empersa;
    }

    public List<EzattaLog> getAllLog() {
        TypedQuery<EzattaLog> query = em.createNamedQuery("EzattaLog.findAll", EzattaLog.class);
        List<EzattaLog> results = query.getResultList();
        return results;
    }

    public List<EzattaLog> findAll(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, EzattaLog.class).getResultList();
    }

}
