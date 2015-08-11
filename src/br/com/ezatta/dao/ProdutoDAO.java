package br.com.ezatta.dao;

import static br.com.ezatta.controller.LoginController.ezattaEmpresaStatic;
import static br.com.ezatta.controller.LoginController.ezattaUsuarioStatic;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaLog;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.model.EzattaProdutoVo;
import br.com.ezatta.util.JPAUtil;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ProdutoDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public ProdutoDAO() {
    }

    public void addProduto(EzattaProduto produto) throws SQLException {
        em.getTransaction().begin();
        em.persist(produto);
        em.getTransaction().commit();

        //log-------------------------------------------------------------------
        String acao = "Adicionar Produto: " + produto;
        EzattaLog log = new EzattaLog();
        Timestamp data = new Timestamp(System.currentTimeMillis());
        log.setData(data);
        log.setAcao(acao);
        log.setEmpresa(ezattaEmpresaStatic);
        LogDAO dao = new LogDAO();

        System.out.println("log: " + log);
        dao.addLog(log);
        //----------------------------------------------------------------------
    }

    public void removeProduto(EzattaProduto produto) throws SQLException {
        em.getTransaction().begin();
        EzattaProduto prod = em.find(EzattaProduto.class, produto.getId());
        em.remove(prod);
        em.getTransaction().commit();

        //log-------------------------------------------------------------------
        String acao = "Remover Produto: " + produto;
        EzattaLog log = new EzattaLog();
        Timestamp data = new Timestamp(System.currentTimeMillis());
        log.setData(data);
        log.setAcao(acao);
        log.setEmpresa(ezattaEmpresaStatic);
        LogDAO dao = new LogDAO();

        System.out.println("log: " + log);
        dao.addLog(log);
        //----------------------------------------------------------------------
    }

    public void updateProduto(EzattaProduto produto) throws SQLException {
        em.getTransaction().begin();
        em.merge(produto);
        em.getTransaction().commit();

        //log-------------------------------------------------------------------
        String acao = "Alterar Produto: " + produto;
        EzattaLog log = new EzattaLog();

        if (ezattaEmpresaStatic instanceof EzattaEmpresa) {
            log.setEmpresa(ezattaEmpresaStatic);
            System.out.println("ezattaEmpresaStatic instanceof EzattaEmpresa");
        }else{
            System.out.println("ezattaUsuarioStatic.getEmpresa(): " + ezattaUsuarioStatic.getEmpresa());
            log.setEmpresa(ezattaUsuarioStatic.getEmpresa());
        }

        Timestamp data = new Timestamp(System.currentTimeMillis());
        log.setData(data);
        log.setAcao(acao);

        LogDAO dao = new LogDAO();
        System.out.println("log: " + log);
        dao.addLog(log);
        //----------------------------------------------------------------------
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
