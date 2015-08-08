package br.com.ezatta.dao;



import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.util.JPAUtil;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class BicosDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();
    
    public BicosDAO() {
    }

    public void addBico(EzattaBico bico){
        em.getTransaction().begin();
        em.persist(bico);
        em.getTransaction().commit();
    }

    public void removeBico(EzattaBico bico){
        em.getTransaction().begin();
        EzattaBico bicos = em.find(EzattaBico.class, bico.getId());
        em.remove(bicos);
        em.getTransaction().commit();
    }

    public void updateBico(EzattaBico bico){
       em.getTransaction().begin();
       em.merge(bico);
       em.getTransaction().commit();
    }

    public EzattaBico getBico(int id) {
        TypedQuery<EzattaBico> query = em.createNamedQuery("EzattaBico.findById",EzattaBico.class);
        query.setParameter("id", id);
        EzattaBico b = query.getSingleResult();
        return b;
    }

    public List<EzattaBico> getAllBico() {
        TypedQuery<EzattaBico> query = em.createNamedQuery("EzattaBico.findAll", EzattaBico.class);
        List<EzattaBico> bico = query.getResultList();
        return bico;
    }
    
    public List<EzattaBico> getAllBicoByProd(EzattaProduto produto) {
        TypedQuery<EzattaBico> query = em.createNamedQuery("EzattaBico.findByProduto",EzattaBico.class);
        query.setParameter("produto", produto);
        List<EzattaBico> retorno = query.getResultList();
        return retorno;
    }

    public List<EzattaBico> findAll(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, EzattaBico.class).getResultList();
    }
    
}