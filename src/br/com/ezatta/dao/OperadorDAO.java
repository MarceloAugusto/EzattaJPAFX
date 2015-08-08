package br.com.ezatta.dao;

import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaOperador;
import br.com.ezatta.util.JPAUtil;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class OperadorDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public OperadorDAO() {
    }

    public void addOperador(EzattaOperador operador) {
        em.getTransaction().begin();
        em.persist(operador);
        em.getTransaction().commit();
    }

    public void removeOperador(EzattaOperador operador) {
        em.getTransaction().begin();
        EzattaOperador empresa = em.find(EzattaOperador.class, operador.getId());
        em.remove(empresa);
        em.getTransaction().commit();
    }

    public void updateOperador(EzattaOperador operador) {
        em.getTransaction().begin();
        em.merge(operador);
        em.getTransaction().commit();
    }

    public EzattaOperador getOperador(int id){
        EzattaOperador operador = em.find(EzattaOperador.class, id);
        return operador;
    }

    public List<EzattaOperador> getAllOperador() {
        TypedQuery<EzattaOperador> query = em.createNamedQuery("EzattaOperador.findAll", EzattaOperador.class);
        List<EzattaOperador> result = query.getResultList();
        return result; 
    }
    
    public List<EzattaOperador> getAllOperadorbyEmpresa(EzattaEmpresa empresa){
       TypedQuery<EzattaOperador> query = em.createNamedQuery("EzattaOperador.findByEmpresa",EzattaOperador.class);
       query.setParameter("empresa", empresa);
       List<EzattaOperador> operador = query.getResultList();
       return operador;
    }
    
    public List<EzattaOperador> findAll(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, EzattaOperador.class).getResultList();
    }

   
}