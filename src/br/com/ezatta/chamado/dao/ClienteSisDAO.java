/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.chamado.dao;

//import br.com.ezatta.chamado.model.SisCliente;
import br.com.ezatta.util.JPAUtilChamado;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Marcelo
 */
public class ClienteSisDAO {

//    private static final long serialVersionUID = 1L;
//
//    EntityManager em = JPAUtilChamado.getEntityManager();
//
//    public ClienteSisDAO() {
//    }
//
//    public void addClienteSis(SisCliente cliente) throws SQLException {
//        em.getTransaction().begin();
//        em.persist(cliente);
//        em.getTransaction().commit();
//    }
//
//    public void removeClienteSis(SisCliente emp) throws SQLException {
//        em.getTransaction().begin();
//        SisCliente cliente = em.find(SisCliente.class, emp.getId());
//        em.remove(cliente);
//        em.getTransaction().commit();
//    }
//
//    public void updateClienteSis(SisCliente cliente) throws SQLException {
//        em.getTransaction().begin();
//        em.merge(cliente);
//        em.getTransaction().commit();
//    }
//
//    public SisCliente getClienteSis(int id) throws SQLException {
//        SisCliente empersa = em.find(SisCliente.class, id);
//        return empersa;
//    }
//
//    public List<SisCliente> getAllClienteSis() {
//        TypedQuery<SisCliente> query = em.createNamedQuery("SisCliente.findAll", SisCliente.class);
//        List<SisCliente> results = query.getResultList();
//        return results;
//    }
//
//    public List<SisCliente> findAllClienteSis(String qry, String parametros)
//            throws SQLException {
//        if (!parametros.isEmpty()) {
//            qry = qry.concat(parametros);
//        }
//        return em.createNativeQuery(qry, SisCliente.class).getResultList();
//    }

}
