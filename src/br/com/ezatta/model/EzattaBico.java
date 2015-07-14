/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.model;

import br.com.ezatta.util.Colunas;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 *
 * @author Marcelo
 */
@Entity
@Table(name = "ezatta_bico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EzattaBico.findAll", query = "SELECT e FROM EzattaBico e"),
    @NamedQuery(name = "EzattaBico.findById", query = "SELECT e FROM EzattaBico e WHERE e.id = :id"),
    @NamedQuery(name = "EzattaBico.findByNome", query = "SELECT e FROM EzattaBico e WHERE e.nome = :nome"),
    @NamedQuery(name = "EzattaBico.findByStatus", query = "SELECT e FROM EzattaBico e WHERE e.status = :status"),
    @NamedQuery(name = "EzattaBico.findByEndereco", query = "SELECT e FROM EzattaBico e WHERE e.endereco = :endereco"),
    @NamedQuery(name = "EzattaBico.findByFatorescala", query = "SELECT e FROM EzattaBico e WHERE e.fatorescala = :fatorescala"),
    @NamedQuery(name = "EzattaBico.findByProduto", query = "SELECT e FROM EzattaBico e WHERE e.produto = :produto")})
public class EzattaBico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @Colunas(nome = "Código", size = 100)
    private Integer id;
    @Column(name = "NOME", length = 255)
    @Colunas(nome = "Nome", size = 100)
    private String nome;
    @Column(name = "STATUS", length = 40)
    private String status;
    @Column(name = "ENDERECO", length = 255)
    @Colunas(nome = "Endereço", size = 100)
    private String endereco;
    @Column(name = "FATORESCALA", length = 255)
    private String fatorescala;
    @OneToMany(mappedBy = "bico")
    private List<EzattaEstoque> ezattaEstoqueList;
    @JoinColumn(name = "PRODUTO", referencedColumnName = "id")
    @ManyToOne
    @Colunas(nome = "Produto", size = 100)
    private EzattaProduto produto;

    public EzattaBico(Integer id, String nome, String endereco, EzattaProduto produto) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.produto = produto;
    }

//    public EzattaBico(String nome, String endereco, EzattaProduto produto) {
//        this.nome = nome;
//        this.endereco = endereco;
//        this.produto = produto;
//    }

    public EzattaBico(String nome, String endereco, String fatorescala, EzattaProduto produto) {
        this.nome = nome;
        this.endereco = endereco;
        this.fatorescala = fatorescala;
        this.produto = produto;
    }
    
    

    public EzattaBico() {
    }

    public EzattaBico(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFatorescala() {
        return fatorescala;
    }

    public void setFatorescala(String fatorescala) {
        this.fatorescala = fatorescala;
    }

    @XmlTransient
    public List<EzattaEstoque> getEzattaEstoqueList() {
        return ezattaEstoqueList;
    }

    public void setEzattaEstoqueList(List<EzattaEstoque> ezattaEstoqueList) {
        this.ezattaEstoqueList = ezattaEstoqueList;
    }

    public EzattaProduto getProduto() {
        return produto;
    }

    public void setProduto(EzattaProduto produto) {
        this.produto = produto;
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
        if (!(object instanceof EzattaBico)) {
            return false;
        }
        EzattaBico other = (EzattaBico) object;
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
