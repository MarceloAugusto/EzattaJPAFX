/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.model;

import br.com.ezatta.util.Colunas;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "ezatta_empresa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EzattaEmpresa.findAll", query = "SELECT e FROM EzattaEmpresa e"),
    @NamedQuery(name = "EzattaEmpresa.findById", query = "SELECT e FROM EzattaEmpresa e WHERE e.id = :id"),
    @NamedQuery(name = "EzattaEmpresa.findByNome", query = "SELECT e FROM EzattaEmpresa e WHERE e.nome = :nome"),
    @NamedQuery(name = "EzattaEmpresa.findByLogin", query = "SELECT e FROM EzattaEmpresa e WHERE e.login = :login"),
    @NamedQuery(name = "EzattaEmpresa.findBySenha", query = "SELECT e FROM EzattaEmpresa e WHERE e.senha = :senha"),
    @NamedQuery(name = "EzattaEmpresa.findUserSenha", query = "SELECT e FROM EzattaEmpresa e WHERE e.senha = :senha AND e.login = :login"),
    @NamedQuery(name = "EzattaEmpresa.findByEmail", query = "SELECT e FROM EzattaEmpresa e WHERE e.email = :email")})
public class EzattaEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Colunas(nome = "Código", size = 10)
    private Integer id;
    @Column(name = "nome", length = 255)
    @Colunas(nome = "Nome", size = 100)
    private String nome;
    @Column(name = "login", length = 255)
    @Colunas(nome = "Login", size = 100)
    private String login;
    //@Colunas(nome = "Senha", size = 100)
    @Column(name = "senha", length = 255)
    private String senha;
    @Column(name = "email", length = 255)
    @Colunas(nome = "E-mail", size = 100)
    private String email;
        
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<EzattaOperador> ezattaOperadorList;
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<EzattaUsuario> ezattaUsuarioList;
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<EzattaProduto> ezattaProdutoList;
    
    public EzattaEmpresa() {
    }

    public EzattaEmpresa(Integer id, String nome, String login, String senha, String email, List<EzattaOperador> ezattaOperadorList, List<EzattaUsuario> ezattaUsuarioList, List<EzattaProduto> ezattaProdutoList) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.ezattaOperadorList = ezattaOperadorList;
        this.ezattaUsuarioList = ezattaUsuarioList;
        this.ezattaProdutoList = ezattaProdutoList;
    }

    public EzattaEmpresa(Integer id, String nome, String login, String senha, String email) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
    }

   

    public EzattaEmpresa(String nome, String login, String senha, String email) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public List<EzattaOperador> getEzattaOperadorList() {
        return ezattaOperadorList;
    }

    @XmlTransient
    public void setEzattaOperadorList(List<EzattaOperador> ezattaOperadorList) {
        this.ezattaOperadorList = ezattaOperadorList;
    }

    @XmlTransient
    public List<EzattaUsuario> getEzattaUsuarioList() {
        return ezattaUsuarioList;
    }

    @XmlTransient
    public void setEzattaUsuarioList(List<EzattaUsuario> ezattaUsuarioList) {
        this.ezattaUsuarioList = ezattaUsuarioList;
    }

    @XmlTransient
    public List<EzattaProduto> getEzattaProdutoList() {
        return ezattaProdutoList;
    }

    @XmlTransient
    public void setEzattaProdutoList(List<EzattaProduto> ezattaProdutoList) {
        this.ezattaProdutoList = ezattaProdutoList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final EzattaEmpresa other = (EzattaEmpresa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNome();
    }

}
