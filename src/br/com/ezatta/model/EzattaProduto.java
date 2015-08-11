/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.model;

import br.com.ezatta.chamado.model.Status;
import br.com.ezatta.util.Colunas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Marcelo
 */
@Entity
@Table(name = "ezatta_produto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EzattaProduto.findAll", query = "SELECT e FROM EzattaProduto e"),
    @NamedQuery(name = "EzattaProduto.findById", query = "SELECT e FROM EzattaProduto e WHERE e.id = :id"),
    @NamedQuery(name = "EzattaProduto.findByNome", query = "SELECT e FROM EzattaProduto e WHERE e.nome = :nome"),
    @NamedQuery(name = "EzattaProduto.findByQuantidade", query = "SELECT e FROM EzattaProduto e WHERE e.quantidade = :quantidade"),
    @NamedQuery(name = "EzattaProduto.findByEstoqueMaximo", query = "SELECT e FROM EzattaProduto e WHERE e.estoqueMaximo = :estoqueMaximo"),
    @NamedQuery(name = "EzattaProduto.findByEstoqueMinimo", query = "SELECT e FROM EzattaProduto e WHERE e.estoqueMinimo = :estoqueMinimo"),
    @NamedQuery(name = "EzattaProduto.findByCor", query = "SELECT e FROM EzattaProduto e WHERE e.cor = :cor")})

public class EzattaProduto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Colunas(nome = "Código", size = 100)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Colunas(nome = "Nome", size = 100)
    @Column(name = "nome", length = 50)
    private String nome;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantidade", precision = 15, scale = 2)
    @Colunas(nome = "Quantidade", size = 100)
    private BigDecimal quantidade;
    @Column(name = "ESTOQUE_MAXIMO", precision = 15, scale = 2)
    @Colunas(nome = "Reservatório", size = 100)
    private BigDecimal estoqueMaximo;
    @Column(name = "ESTOQUE_MINIMO", precision = 15, scale = 2)
    @Colunas(nome = "Mínimo", size = 100)
    private BigDecimal estoqueMinimo;
    @Column(name = "cor", length = 255)
    @Colunas(nome = "Cor", size = 100)
    private String cor;

    @Column(name = "notificacao", length = 50)
    private boolean notificacaoEstoqueEmail;

    @Colunas(nome = "Email", size = 100)
    @Column(name = "email", length = 255)
    private String email;

    @Colunas(nome = "Empresa", size = 100)
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne
    private EzattaEmpresa empresa;
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<EzattaMovimentacoes> ezattaEstoqueList;
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<EzattaEstoqueProduto> ezattaProdutoList;
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<EzattaBico> ezattaBicoList;

    public EzattaProduto() {
    }

    public EzattaProduto(String nome, BigDecimal quantidade, BigDecimal estoqueMaximo, BigDecimal estoqueMinimo, String cor, EzattaEmpresa empresa) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.estoqueMaximo = estoqueMaximo;
        this.estoqueMinimo = estoqueMinimo;
        this.cor = cor;
        this.empresa = empresa;
    }

    public EzattaProduto(Integer id, String nome, BigDecimal quantidade, BigDecimal estoqueMaximo, BigDecimal estoqueMinimo, String cor, EzattaEmpresa empresa) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.estoqueMaximo = estoqueMaximo;
        this.estoqueMinimo = estoqueMinimo;
        this.cor = cor;
        this.empresa = empresa;
    }

    public EzattaProduto(Integer id) {
        this.id = id;
    }

    public EzattaProduto(Integer id, String nome, BigDecimal estoqueMaximo, BigDecimal estoqueMinimo, String cor, EzattaEmpresa empresa) {
        this.id = id;
        this.nome = nome;
        this.estoqueMaximo = estoqueMaximo;
        this.estoqueMinimo = estoqueMinimo;
        this.cor = cor;
        this.empresa = empresa;
    }

    public List<EzattaEstoqueProduto> getEzattaProdutoList() {
        return ezattaProdutoList;
    }

    public void setEzattaProdutoList(List<EzattaEstoqueProduto> ezattaProdutoList) {
        this.ezattaProdutoList = ezattaProdutoList;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNotificacaoEstoqueEmail() {
        return notificacaoEstoqueEmail;
    }

    public void setNotificacaoEstoqueEmail(boolean notificacaoEstoqueEmail) {
        this.notificacaoEstoqueEmail = notificacaoEstoqueEmail;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public void addQuantidade(BigDecimal quantidade) {
        this.quantidade.add(quantidade);
    }

    public void removQuantidade(BigDecimal quantidade) {
        this.quantidade.subtract(quantidade);
    }

    public BigDecimal getEstoqueMaximo() {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(BigDecimal estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public EzattaEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EzattaEmpresa empresa) {
        this.empresa = empresa;
    }

    @XmlTransient
    public List<EzattaMovimentacoes> getEzattaEstoqueList() {
        return ezattaEstoqueList;
    }

    public void setEzattaEstoqueList(List<EzattaMovimentacoes> ezattaEstoqueList) {
        this.ezattaEstoqueList = ezattaEstoqueList;
    }

    @XmlTransient
    public List<EzattaBico> getEzattaBicoList() {
        return ezattaBicoList;
    }

    public void setEzattaBicoList(List<EzattaBico> ezattaBicoList) {
        this.ezattaBicoList = ezattaBicoList;
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
        if (!(object instanceof EzattaProduto)) {
            return false;
        }
        EzattaProduto other = (EzattaProduto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNome();
    }

}
