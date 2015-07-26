/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.chamado.dao;

import br.com.ezatta.chamado.model.ChaChamado;
import br.com.ezatta.chamado.model.ChaInteracoes;
import br.com.ezatta.util.JPAUtilChamado;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Marcelo
 */
public class ChaInteracoesDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtilChamado.getEntityManager();

    public ChaInteracoesDAO() {
    }

    public void addChaInteracoes(ChaInteracoes interacao) throws SQLException {
        em.getTransaction().begin();
        em.persist(interacao);
        em.getTransaction().commit();
        //JPAUtilChamado.closeManager(em);
    }

    public void removeChaInteracoes(ChaInteracoes emp) throws SQLException {
        em.getTransaction().begin();
        ChaInteracoes interacao = em.find(ChaInteracoes.class, emp.getId());
        em.remove(interacao);
        em.getTransaction().commit();
        //JPAUtilChamado.closeManager(em);
    }

    public void updateChaInteracoes(ChaInteracoes interacao) throws SQLException {
        em.getTransaction().begin();
        em.merge(interacao);
        em.getTransaction().commit();
        //JPAUtilChamado.closeManager(em);
    }

    public ChaInteracoes getChaInteracoes(int id) throws SQLException {
        ChaInteracoes empersa = em.find(ChaInteracoes.class, id);
        return empersa;
    }

    public List<ChaInteracoes> getAllChaInteracoes() {
        TypedQuery<ChaInteracoes> query = em.createNamedQuery("ChaInteracoes.findAll", ChaInteracoes.class);
        List<ChaInteracoes> results = query.getResultList();
        return results;
    }

    public List<ChaInteracoes> findAllChaInteracoes(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, ChaInteracoes.class).getResultList();
    }
    
    public List<ChaInteracoes> getChaInteracoesByChamada(ChaChamado idChamada) {
        TypedQuery<ChaInteracoes> query = em.createNamedQuery("ChaInteracoes.findByChamada", ChaInteracoes.class);
        query.setParameter("idChamado", idChamada);
        List<ChaInteracoes> results = query.getResultList();
        return results;
    }
    
     public List<ChaInteracoes> getAllByChamada(int idChamada) {
        TypedQuery<ChaInteracoes> query = em.createNamedQuery("ChaInteracoes.findByChamada", ChaInteracoes.class);
        query.setParameter("idChamada", idChamada);
        List<ChaInteracoes> resultado = query.getResultList();
        return resultado;
    }
    
    /*
    public List<EzattaUsuario> getAllUsersPerEmpresa(EzattaEmpresa empresa) {
        TypedQuery<EzattaUsuario> query = em.createNamedQuery("EzattaUsuario.findByEmpresa", EzattaUsuario.class);
        query.setParameter("empresa", empresa);
        List<EzattaUsuario> usuarios = query.getResultList();
        return usuarios;
    }
    */
    

}
