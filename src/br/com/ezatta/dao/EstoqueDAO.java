package br.com.ezatta.dao;



import br.com.ezatta.model.EzattaEstoque;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.util.JPAUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class EstoqueDAO extends GenericDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public EstoqueDAO() {
    }

    public void addEstoque(EzattaEstoque estoque) {
        em.getTransaction().begin();
        em.persist(estoque);
        em.getTransaction().commit();
    }

    public void removeEstoque(EzattaEstoque estoque) {
        em.getTransaction().begin();
        EzattaEstoque est = em.find(EzattaEstoque.class, estoque.getId());
        em.persist(est);
        em.getTransaction().commit();
    }

    public void updateEstoque(EzattaEstoque estoque) {
        em.getTransaction().begin();
        em.merge(estoque);
        em.getTransaction().commit();
    }

    public EzattaEstoque getEstoque(int id) {
        TypedQuery<EzattaEstoque> query = em.createNamedQuery("EzattaEstoque.findById", EzattaEstoque.class);
        query.setParameter("id", id);
        EzattaEstoque estoque = query.getSingleResult();
        return estoque;
    }
    
    public List getIdEstoque() {
        List resultList = em.createQuery("SELECT e FROM EzattaEstoque e ORDER BY e.id").setMaxResults(1).getResultList();
        return resultList;
    }
    
    public List<EzattaEstoque> getEstoqueByStatus(int status) {
        TypedQuery<EzattaEstoque> query = em.createNamedQuery("EzattaEstoque.findByStatus", EzattaEstoque.class);
        query.setParameter("status", status);
        List<EzattaEstoque> listaEstoque = query.getResultList();
        return listaEstoque;
    }

    public List<EzattaEstoque> getAllEstoque() {
        TypedQuery<EzattaEstoque> query = em.createNamedQuery("EzattaEstoque.findAll", EzattaEstoque.class);
        List<EzattaEstoque> listaEstoque = query.getResultList();
        return listaEstoque;
    }

    public List<EzattaEstoque> getAllByUsuario(EzattaUsuario usuario) {
        TypedQuery<EzattaEstoque> query = em.createNamedQuery("EzattaEstoque.findByUsuario", EzattaEstoque.class);
        query.setParameter("usuario", usuario.getId());
        List<EzattaEstoque> estoque = query.getResultList();
        return estoque;
    }

    public EzattaEstoque getEstoqueById(Integer id) {
        TypedQuery<EzattaEstoque> query = em.createNamedQuery("EzattaEstoque.findById", EzattaEstoque.class);
        query.setParameter("id", id);
        EzattaEstoque estoque = query.getSingleResult();
        return estoque;
    }

}
