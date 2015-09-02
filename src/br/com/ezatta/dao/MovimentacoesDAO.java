package br.com.ezatta.dao;



import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaMovimentacoes;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.util.JPAUtil;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class MovimentacoesDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public MovimentacoesDAO() {
    }

    public void addEstoque(EzattaMovimentacoes movimentacao) {
        em.getTransaction().begin();
        em.persist(movimentacao);
        em.getTransaction().commit();
    }

    public void removeEstoque(EzattaMovimentacoes movimentacao) {
        em.getTransaction().begin();
        EzattaMovimentacoes est = em.find(EzattaMovimentacoes.class, movimentacao.getId());
        em.persist(est);
        em.getTransaction().commit();
    }

    public void updateEstoque(EzattaMovimentacoes movimentacao) {
        em.getTransaction().begin();
        em.merge(movimentacao);
        em.getTransaction().commit();
    }

    public EzattaMovimentacoes getEstoque(int id) {
        TypedQuery<EzattaMovimentacoes> query = em.createNamedQuery("EzattaMovimentacoes.findById", EzattaMovimentacoes.class);
        query.setParameter("id", id);
        EzattaMovimentacoes movimentacao = query.getSingleResult();
        return movimentacao;
    }
    
    public List getUltimoEstoque() {
        List resultList = em.createQuery("SELECT e FROM EzattaMovimentacoes e ORDER BY e.id desc").setMaxResults(1).getResultList();
        return resultList;
    }
    
    public List<EzattaMovimentacoes> getEstoqueByStatus(int status) {
        TypedQuery<EzattaMovimentacoes> query = em.createNamedQuery("EzattaMovimentacoes.findByStatus", EzattaMovimentacoes.class);
        query.setParameter("status", status);
        List<EzattaMovimentacoes> listaEstoque = query.getResultList();
        return listaEstoque;
    }

    public List<EzattaMovimentacoes> getAllEstoque() {
        TypedQuery<EzattaMovimentacoes> query = em.createNamedQuery("EzattaMovimentacoes.findAll", EzattaMovimentacoes.class);
        List<EzattaMovimentacoes> listaEstoque = query.getResultList();
        return listaEstoque;
    }
    
    public List<EzattaMovimentacoes> getAllEstoqueLimitOrderBy() {
        TypedQuery<EzattaMovimentacoes> query = em.createNamedQuery("EzattaMovimentacoes.findAllLimit", EzattaMovimentacoes.class);
        query.setMaxResults(30);
        
        List<EzattaMovimentacoes> listaEstoque = query.getResultList();
        return listaEstoque;
    }

    public List<EzattaMovimentacoes> getAllByUsuario(EzattaUsuario usuario) {
        TypedQuery<EzattaMovimentacoes> query = em.createNamedQuery("EzattaMovimentacoes.findByUsuario", EzattaMovimentacoes.class);
        query.setParameter("usuario", usuario.getId());
        List<EzattaMovimentacoes> movimentacao = query.getResultList();
        return movimentacao;
    }

    public EzattaMovimentacoes getEstoqueById(Integer id) {
        TypedQuery<EzattaMovimentacoes> query = em.createNamedQuery("EzattaMovimentacoes.findById", EzattaMovimentacoes.class);
        query.setParameter("id", id);
        EzattaMovimentacoes movimentacao = query.getSingleResult();
        return movimentacao;
    }
    
    public List<EzattaMovimentacoes> findAll(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, EzattaMovimentacoes.class).getResultList();
    }

}
