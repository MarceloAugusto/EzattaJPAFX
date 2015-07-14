package br.com.ezatta.dao;

import br.com.ezatta.model.ProdutoEstoque_ezatta;
import br.com.ezatta.util.JPAUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;

public class ProdutoEstoqueDAO extends GenericDAO {

    private static final long serialVersionUID = 1L;
    EntityManager em = JPAUtil.getEntityManager();
    
    public ProdutoEstoqueDAO() {
    }

    public void addPEstoque(ProdutoEstoque_ezatta estoque) throws SQLException {
       em.getTransaction().begin();
       em.persist(estoque);
       em.getTransaction().commit();
    }

    public void removePEstoque(Long id) throws SQLException {
        
    }

    public void updatePEstoque(ProdutoEstoque_ezatta estoque) throws SQLException {
        em.getTransaction().begin();
        em.merge(estoque);
        em.getTransaction().commit();
    }

    public ProdutoEstoque_ezatta getPEstoque(int id) throws SQLException {
        return null;
        
    }

    public List<ProdutoEstoque_ezatta> getAllPEstoque() throws SQLException {
        ResultSet rss = executeQuery("Select * from ezatta_produtoestoque");
        List<ProdutoEstoque_ezatta> toReturn = new LinkedList<ProdutoEstoque_ezatta>();
        while (rss.next()) {
            toReturn.add(populatePEstoqueInfo(rss));
        }
        rss.close();
        return toReturn;
    }

    private ProdutoEstoque_ezatta populatePEstoqueInfo(ResultSet rs) throws SQLException {
        ProdutoEstoque_ezatta toReturn = new ProdutoEstoque_ezatta();
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
}
