/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.chamado.model;

import br.com.ezatta.util.Colunas;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcelo
 */
@Entity
@Table(name = "cha_chamado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChaChamado.findAll", query = "SELECT c FROM ChaChamado c"),
    @NamedQuery(name = "ChaChamado.findById", query = "SELECT c FROM ChaChamado c WHERE c.id = :id"),
    @NamedQuery(name = "ChaChamado.findByIdCliente", query = "SELECT c FROM ChaChamado c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "ChaChamado.findByTitulo", query = "SELECT c FROM ChaChamado c WHERE c.titulo = :titulo"),
    @NamedQuery(name = "ChaChamado.findByStatus", query = "SELECT c FROM ChaChamado c WHERE c.status = :status"),
    @NamedQuery(name = "ChaChamado.findByData", query = "SELECT c FROM ChaChamado c WHERE c.data = :data")})
public class ChaChamado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "id_cliente", nullable = false)
    private int idCliente;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
     @Colunas(nome = "Titulo", size = 100)
    private String titulo;
    @Enumerated(EnumType.STRING)
    @Colunas(nome = "Status", size = 100)
    private Status status;
    @Temporal(TemporalType.TIMESTAMP)
    @Colunas(nome = "Data", size = 100)
    private Date data;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idChamado")
    private List<ChaInteracoes> chaInteracoesList;

    public ChaChamado() {
    }

    public ChaChamado(Integer id) {
        this.id = id;
    }

    public ChaChamado(Integer id, int idCliente, String titulo, Status status) {
        this.id = id;
        this.idCliente = idCliente;
        this.titulo = titulo;
        this.status = status;
    }
    
    public ChaChamado(String titulo, Status status, int idCliente, Date data){
        this.titulo = titulo;
        this.status = status;
        this.idCliente = idCliente;
        this.data = data;
    }  

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @XmlTransient
    public List<ChaInteracoes> getChaInteracoesList() {
        return chaInteracoesList;
    }

    public void setChaInteracoesList(List<ChaInteracoes> chaInteracoesList) {
        this.chaInteracoesList = chaInteracoesList;
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
        if (!(object instanceof ChaChamado)) {
            return false;
        }
        ChaChamado other = (ChaChamado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.ezatta.chamado.model.ChaChamado[ id=" + id + " ]";
    }
    
}
