/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.model;

import java.io.Serializable;
import java.util.Date;
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
 * @author Marcelo
 */
@Entity
@Table(name = "ezatta_estoque")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EzattaEstoque.findAll", query = "SELECT e FROM EzattaEstoque e"),
    @NamedQuery(name = "EzattaEstoque.findById", query = "SELECT e FROM EzattaEstoque e WHERE e.id = :id"),
    @NamedQuery(name = "EzattaEstoque.findByQtdEstoque", query = "SELECT e FROM EzattaEstoque e WHERE e.qtdEstoque = :qtdEstoque"),
    @NamedQuery(name = "EzattaEstoque.findByDataEstoque", query = "SELECT e FROM EzattaEstoque e WHERE e.dataEstoque = :dataEstoque"),
    @NamedQuery(name = "EzattaEstoque.findByOs", query = "SELECT e FROM EzattaEstoque e WHERE e.os = :os"),
    @NamedQuery(name = "EzattaEstoque.findByPlaca", query = "SELECT e FROM EzattaEstoque e WHERE e.placa = :placa"),
    @NamedQuery(name = "EzattaEstoque.findByKm", query = "SELECT e FROM EzattaEstoque e WHERE e.km = :km"),
    @NamedQuery(name = "EzattaEstoque.findMaxIdEstoque", query = "SELECT MAX(e.id) FROM EzattaEstoque e"),
    @NamedQuery(name = "EzattaEstoque.findByStatus", query = "SELECT e FROM EzattaEstoque e WHERE e.status = :status")})
public class EzattaEstoque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qtd_estoque", precision = 12)
    private Float qtdEstoque;
    @Column(name = "data_estoque")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEstoque;
    @Column(name = "os", length = 255)
    private String os;
    @Column(name = "placa", length = 255)
    private String placa;
    @Column(name = "km", length = 255)
    private String km;
    @Column(name = "STATUS")
    private Integer status;
    @JoinColumn(name = "operador", referencedColumnName = "id")
    @ManyToOne
    private EzattaOperador operador;
    @JoinColumn(name = "bico", referencedColumnName = "ID")
    @ManyToOne
    private EzattaBico bico;
    @JoinColumn(name = "produto", referencedColumnName = "id")
    @ManyToOne
    private EzattaProduto produto;
    @JoinColumn(name = "usuario", referencedColumnName = "ID")
    @ManyToOne
    private EzattaUsuario usuario;

    public EzattaEstoque() {
    }

    public EzattaEstoque(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(Float qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public Date getDataEstoque() {
        return dataEstoque;
    }

    public void setDataEstoque(Date dataEstoque) {
        this.dataEstoque = dataEstoque;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public EzattaOperador getOperador() {
        return operador;
    }

    public void setOperador(EzattaOperador operador) {
        this.operador = operador;
    }

    public EzattaBico getBico() {
        return bico;
    }

    public void setBico(EzattaBico bico) {
        this.bico = bico;
    }

    public EzattaProduto getProduto() {
        return produto;
    }

    public void setProduto(EzattaProduto produto) {
        this.produto = produto;
    }

    public EzattaUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(EzattaUsuario usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof EzattaEstoque)) {
            return false;
        }
        EzattaEstoque other = (EzattaEstoque) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EzattaEstoque{" + "id=" + id + ", qtdEstoque=" + qtdEstoque + ", dataEstoque=" + dataEstoque + ", os=" + os + ", placa=" + placa + ", km=" + km + ", status=" + status + ", operador=" + operador + ", bico=" + bico + ", produto=" + produto + ", usuario=" + usuario + '}';
    }

    
    
}
