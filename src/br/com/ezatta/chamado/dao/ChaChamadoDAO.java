/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.chamado.dao;

import br.com.ezatta.chamado.model.ChaChamado;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.util.JPAUtilChamado;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Marcelo
 */
public class ChaChamadoDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtilChamado.getEntityManager();

    public ChaChamadoDAO() {
    }

    public void addChaChamado(ChaChamado chamado) throws SQLException {
        em.getTransaction().begin();
        em.persist(chamado);
        em.getTransaction().commit();
        //JPAUtilChamado.closeManager(em);
    }

    public void removeChaChamado(ChaChamado emp) throws SQLException {
        em.getTransaction().begin();
        ChaChamado chamado = em.find(ChaChamado.class, emp.getId());
        em.remove(chamado);
        em.getTransaction().commit();
        //JPAUtilChamado.closeManager(em);
    }

    public void updateChaChamado(ChaChamado chamado) throws SQLException {
        em.getTransaction().begin();
        em.merge(chamado);
        em.getTransaction().commit();
        //JPAUtilChamado.closeManager(em);
    }

    public ChaChamado getChaChamado(int id) throws SQLException {
        ChaChamado empersa = em.find(ChaChamado.class, id);
        return empersa;
    }

    public List<ChaChamado> getAllChaChamado() {
        TypedQuery<ChaChamado> query = em.createNamedQuery("ChaChamado.findAll", ChaChamado.class);
        List<ChaChamado> results = query.getResultList();
        return results;
    }

    public List<ChaChamado> findAllChaChamado(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, ChaChamado.class).getResultList();
    }
    
    public List<ChaChamado> getAllByEmpresa(EzattaEmpresa empresa) {
        TypedQuery<ChaChamado> query = em.createNamedQuery("ChaChamado.findByIdCliente", ChaChamado.class);
        query.setParameter("idCliente", empresa.getId());
        List<ChaChamado> resultado = query.getResultList();
        return resultado;
    }
    

}
