package br.com.ezatta.dao;

import br.com.ezatta.model.EzattaEstoqueProduto;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.util.JPAUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class EstoqueProdutoDAO {

    private static final long serialVersionUID = 1L;
    EntityManager em = JPAUtil.getEntityManager();
    
    public EstoqueProdutoDAO() {
    }

    public void addPEstoque(EzattaEstoqueProduto estoque) {
       em.getTransaction().begin();
       em.persist(estoque);
       em.getTransaction().commit();
    }

    public void removePEstoque(Long id) throws SQLException {
        
    }

    public void updatePEstoque(EzattaEstoqueProduto estoque) throws SQLException {
        em.getTransaction().begin();
        em.merge(estoque);
        em.getTransaction().commit();
    }

    public EzattaEstoqueProduto getPEstoque(int id) throws SQLException {
        return null;
        
    }

//    public List<EzattaEstoqueProduto> getAllPEstoque() throws SQLException {
//        ResultSet rss = executeQuery("Select * from ezatta_produtoestoque");
//        List<EzattaEstoqueProduto> toReturn = new LinkedList<EzattaEstoqueProduto>();
//        while (rss.next()) {
//            toReturn.add(populatePEstoqueInfo(rss));
//        }
//        rss.close();
//        return toReturn;
//    }
    
    public List<EzattaEstoqueProduto> getAllMovimentacoes() {
        TypedQuery<EzattaEstoqueProduto> query = em.createNamedQuery("EzattaEstoqueProduto.findAll", EzattaEstoqueProduto.class);
        List<EzattaEstoqueProduto> listaProduto = query.getResultList();
        return listaProduto;
    }

    private EzattaEstoqueProduto populatePEstoqueInfo(ResultSet rs) throws SQLException {
        EzattaEstoqueProduto toReturn = new EzattaEstoqueProduto();
        OperadorDAO operadordao = new OperadorDAO();
        UsuarioDAO userdao = new UsuarioDAO();
        ProdutoDAO produtodao = new ProdutoDAO();
        
        toReturn.setId(rs.getInt("ID"));
        toReturn.setDataoperacao(rs.getDate("data"));
        toReturn.setQuantidade(rs.getFloat("quantidade"));
        toReturn.setOperacao(rs.getString("Operacao"));
//        toReturn.setUsuario(userdao.getUser(rs.getInt("usuario")));
//        toReturn.setProduto(produtodao.getProduto(rs.getInt("Produto")));     
        return toReturn;
    }
    
    public List<EzattaEstoqueProduto> findAll(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, EzattaEstoqueProduto.class).getResultList();
    }
}
