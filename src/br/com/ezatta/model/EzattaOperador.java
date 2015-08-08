/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.model;

//import br.com.devmedia.frame.SwingColumn;
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
@Table(name = "ezatta_operador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EzattaOperador.findAll", query = "SELECT e FROM EzattaOperador e"),
    @NamedQuery(name = "EzattaOperador.findById", query = "SELECT e FROM EzattaOperador e WHERE e.id = :id"),
    @NamedQuery(name = "EzattaOperador.findByNome", query = "SELECT e FROM EzattaOperador e WHERE e.nome = :nome"),
    @NamedQuery(name = "EzattaOperador.findByMatricula", query = "SELECT e FROM EzattaOperador e WHERE e.matricula = :matricula")})
public class EzattaOperador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @Colunas(nome = "Código", size = 100)
    private Integer id;
    @Column(name = "NOME", length = 50)
    @Colunas(nome = "Nome", size = 100)
    private String nome;
    @Colunas(nome = "Matrícula", size = 100)
    @Column(name = "matricula", length = 255)
    private String matricula;
    @Colunas(nome = "Empresa", size = 100)
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne
    private EzattaEmpresa empresa;
    @OneToMany(mappedBy = "operador")
    private List<EzattaMovimentacoes> ezattaEstoqueList;

    public EzattaOperador() {
    }

    public EzattaOperador(Integer id, String nome, String matricula, EzattaEmpresa empresa) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.empresa = empresa;
    }

    public EzattaOperador(String nome, String matricula, EzattaEmpresa empresa) {
        this.nome = nome;
        this.matricula = matricula;
        this.empresa = empresa;
    }

    
    
    public EzattaOperador(Integer id) {
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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
        if (!(object instanceof EzattaOperador)) {
            return false;
        }
        EzattaOperador other = (EzattaOperador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
    
}
