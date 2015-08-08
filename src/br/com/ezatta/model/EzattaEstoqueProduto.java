package br.com.ezatta.model;

import br.com.ezatta.util.Colunas;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ezatta_estoque_produto")


@NamedQueries({
    @NamedQuery(name = "EzattaEstoqueProduto.findAll", query = "SELECT e FROM EzattaEstoqueProduto e"),
    @NamedQuery(name = "EzattaEstoqueProduto.findById", query = "SELECT e FROM EzattaEstoqueProduto e WHERE e.id = :id")})



    
public class EzattaEstoqueProduto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Colunas(nome = "Produto", size = 100)
    @JoinColumn(name = "produto", referencedColumnName = "id")
    @ManyToOne
    private EzattaProduto produto;
    
    @Colunas(nome = "Quantidade", size = 100)
    private Float quantidade;
    @Colunas(nome = "Operação", size = 100)
    private String operacao;
    @Colunas(nome = "Data", size = 100)
    private Date dataoperacao;
    
    @Colunas(nome = "Usuário", size = 100)
    @OneToOne
    private EzattaUsuario usuario;
    
    @Colunas(nome = "Empresa", size = 100)
    @OneToOne
    private EzattaEmpresa empresa;

    public EzattaEstoqueProduto() {
    }

    public EzattaEstoqueProduto(Integer id, EzattaProduto produto, Float quantidade, String operacao, Date dataoperacao, EzattaUsuario usuario, EzattaEmpresa empresa) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.operacao = operacao;
        this.dataoperacao = dataoperacao;
        this.usuario = usuario;
        this.empresa = empresa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EzattaProduto getProduto() {
        return produto;
    }

    public void setProduto(EzattaProduto produto) {
        this.produto = produto;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public Date getDataoperacao() {
        return dataoperacao;
    }

    public void setDataoperacao(Date dataoperacao) {
        this.dataoperacao = dataoperacao;
    }

    public EzattaUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(EzattaUsuario usuario) {
        this.usuario = usuario;
    }

    public EzattaEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EzattaEmpresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EzattaEstoqueProduto other = (EzattaEstoqueProduto) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EzattaEstoqueProduto{" + "id=" + id + ", produto=" + produto + ", quantidade=" + quantidade + ", operacao=" + operacao + ", dataoperacao=" + dataoperacao + ", usuario=" + usuario + ", empresa=" + empresa + '}';
    }

    
    
    
}
