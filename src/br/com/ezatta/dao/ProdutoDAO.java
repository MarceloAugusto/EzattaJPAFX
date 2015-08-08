package br.com.ezatta.dao;

import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.model.EzattaProdutoVo;
import br.com.ezatta.util.JPAUtil;
import java.sql.SQLException;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ProdutoDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public ProdutoDAO() {
    }

    public void addProduto(EzattaProduto produto) {
        em.getTransaction().begin();
        em.persist(produto);
        em.getTransaction().commit();
    }

    public void removeProduto(EzattaProduto produto) {
        em.getTransaction().begin();
        EzattaProduto prod = em.find(EzattaProduto.class, produto.getId());
        em.remove(prod);
        em.getTransaction().commit();
    }

    public void updateProduto(EzattaProduto produto) {
        em.getTransaction().begin();
        em.merge(produto);
        em.getTransaction().commit();
    }

    public EzattaProduto getProduto(int id) {
        TypedQuery<EzattaProduto> query = em.createNamedQuery("EzattaProduto.findById", EzattaProduto.class);
        query.setParameter("id", id);
        EzattaProduto p = query.getSingleResult();
        return p;
    }

    public List<EzattaProduto> getAllProduto() {
        TypedQuery<EzattaProduto> query = em.createNamedQuery("EzattaProduto.findAll", EzattaProduto.class);
        List<EzattaProduto> listaProduto = query.getResultList();
        return listaProduto;
    }

    public List<EzattaProduto> getAllProdutoByEmpresa(EzattaEmpresa empresa) {
        TypedQuery<EzattaProduto> query = em.createNamedQuery("EzattaProduto.findByEmpresa", EzattaProduto.class);
        query.setParameter("empresa", empresa);
        List<EzattaProduto> produtoEmpresa = query.getResultList();
        return produtoEmpresa;
    }

    public EzattaProduto getProdutoByNome(String nome) {
        TypedQuery<EzattaProduto> query = em.createNamedQuery("EzattaProduto.findByNome", EzattaProduto.class);
        query.setParameter("nome", nome);
        EzattaProduto p = query.getSingleResult();
        return p;
    }

    public List<EzattaProduto> findAll(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, EzattaProduto.class).getResultList();
    }
    
//    public long getValueProduto(){
//        Query query = em.createQuery("Select count(p) from EzattaProduto p");
//        long count = (long) query.getSingleResult(); 
//        return count;
//    }
}
