/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.model;

import br.com.ezatta.util.Colunas;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcelo
 */
@Entity
@Table(name = "EZATTA_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EzattaLog.findAll", query = "SELECT e FROM EzattaLog e"),
    @NamedQuery(name = "EzattaLog.findByData", query = "SELECT e FROM EzattaLog e WHERE e.data = :data"),
    @NamedQuery(name = "EzattaLog.findByAcao", query = "SELECT e FROM EzattaLog e WHERE e.acao = :acao"),
    @NamedQuery(name = "EzattaLog.findById", query = "SELECT e FROM EzattaLog e WHERE e.id = :id"),
    @NamedQuery(name = "EzattaLog.findByEmpresa", query = "SELECT e FROM EzattaLog e WHERE e.empresa = :empresa")})
public class EzattaLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    //@Colunas(nome = "Código", size = 100)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Colunas(nome = "Data", size = 100)
    private Date data;
    @Column(length = 250)
    @Colunas(nome = "Ação", size = 100)
    private String acao;

//    @Colunas(nome = "Usuário", size = 100)
//    @Column(length = 250)
//    private EzattaEmpresa empresa;
    
    @Colunas(nome = "Usuário", size = 100)
    @JoinColumn(name = "EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private EzattaEmpresa empresa;

    public EzattaLog() {
    }

    public EzattaLog(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        final EzattaLog other = (EzattaLog) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EzattaLog{" + "data=" + data + ", acao=" + acao + ", id=" + id + ", empresa=" + empresa + '}';
    }

}
