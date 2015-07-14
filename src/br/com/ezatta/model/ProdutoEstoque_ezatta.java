package br.com.ezatta.model;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ProdutoEstoque_ezatta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private EzattaProduto produto;
    private Float quantidade;
    private String operacao;
    private Date dataoperacao;
    @OneToOne
    private EzattaUsuario usuario;

    public ProdutoEstoque_ezatta() {
    }

    public ProdutoEstoque_ezatta(EzattaProduto produto) {
        this.produto = produto;
    }

    public ProdutoEstoque_ezatta(Integer id, EzattaProduto produto, Float quantidade, String operacao, Date dataoperacao, EzattaUsuario usuario) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.operacao = operacao;
        this.dataoperacao = dataoperacao;
        this.usuario = usuario;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProdutoEstoque_ezatta)) {
            return false;
        }
        ProdutoEstoque_ezatta other = (ProdutoEstoque_ezatta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProdutoEstoque_ezatta{" + "id=" + id + ", produto=" + produto + ", quantidade=" + quantidade + ", operacao=" + operacao + ", dataoperacao=" + dataoperacao + ", usuario=" + usuario + '}';
    }

    
    
    
}
