/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.chamado.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "cha_interacoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChaInteracoes.findAll", query = "SELECT c FROM ChaInteracoes c"),
    @NamedQuery(name = "ChaInteracoes.findById", query = "SELECT c FROM ChaInteracoes c WHERE c.id = :id"),
    @NamedQuery(name = "ChaInteracoes.findByData", query = "SELECT c FROM ChaInteracoes c WHERE c.data = :data"),
    @NamedQuery(name = "ChaInteracoes.findByChamada", query = "SELECT c FROM ChaInteracoes c WHERE c.idChamado = :idChamado"),
    @NamedQuery(name = "ChaInteracoes.findByAdm", query = "SELECT c FROM ChaInteracoes c WHERE c.adm = :adm")})
public class ChaInteracoes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(nullable = false, length = 65535)
    private String texto;
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    private Integer adm;
    @JoinColumn(name = "id_chamado", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ChaChamado idChamado;

    public ChaInteracoes() {
    }

    public ChaInteracoes(Integer id) {
        this.id = id;
    }

    public ChaInteracoes(Integer id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getAdm() {
        return adm;
    }

    public void setAdm(Integer adm) {
        this.adm = adm;
    }

    public ChaChamado getIdChamado() {
        return idChamado;
    }

    public void setIdChamado(ChaChamado idChamado) {
        this.idChamado = idChamado;
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
        if (!(object instanceof ChaInteracoes)) {
            return false;
        }
        ChaInteracoes other = (ChaInteracoes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChaInteracoes{" + "id=" + id + ", texto=" + texto + ", data=" + data + ", adm=" + adm + ", idChamado=" + idChamado + '}';
    }

   
    
}
