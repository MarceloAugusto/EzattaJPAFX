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
@Table(name = "ezatta_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EzattaUsuario.findAll", query = "SELECT e FROM EzattaUsuario e"),
    @NamedQuery(name = "EzattaUsuario.findById", query = "SELECT e FROM EzattaUsuario e WHERE e.id = :id"),
    @NamedQuery(name = "EzattaUsuario.findByNome", query = "SELECT e FROM EzattaUsuario e WHERE e.nome = :nome"),
    @NamedQuery(name = "EzattaUsuario.findByLogin", query = "SELECT e FROM EzattaUsuario e WHERE e.login = :login"),
    @NamedQuery(name = "EzattaUsuario.findByLoginSenha", query = "SELECT e FROM EzattaUsuario e WHERE e.login = :login AND e.senha = :senha"),
    @NamedQuery(name = "EzattaUsuario.findBySenha", query = "SELECT e FROM EzattaUsuario e WHERE e.senha = :senha")})

public class EzattaUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    //Colunas(nome = "Código", size = 100)
    @Colunas(nome="Código", size =100)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Colunas(nome="Nome", size =100)
    @Column(name = "NOME", length = 40)
    private String nome;
    @Colunas(nome="Login", size =100)
    @Column(name = "login", length = 255)
    private String login;
    @Basic(optional = false)
    
    @Column(name = "SENHA", nullable = false, length = 40)
    private String senha;
    
    
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne
    @Colunas(nome="Empresa", size =100)
    private EzattaEmpresa empresa;
    
    
    @OneToMany(mappedBy = "usuario")
    private List<EzattaMovimentacoes> ezattaEstoqueList;

    public EzattaUsuario() {
    }

    public EzattaUsuario(Integer id, String nome, String login, String senha, EzattaEmpresa empresa) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.empresa = empresa;
    }

    public EzattaUsuario(String nome, String login, String senha, EzattaEmpresa empresa) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.empresa = empresa;
    }
    
    

    public EzattaUsuario(Integer id) {
        this.id = id;
    }

    public EzattaUsuario(Integer id, String senha) {
        this.id = id;
        this.senha = senha;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EzattaUsuario)) {
            return false;
        }
        EzattaUsuario other = (EzattaUsuario) object;
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
