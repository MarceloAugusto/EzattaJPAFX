/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.model;

import br.com.ezatta.util.Colunas;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
@Table(name = "ezatta_movimentacoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EzattaMovimentacoes.findAll", query = "SELECT e FROM EzattaMovimentacoes e"),
    @NamedQuery(name = "EzattaMovimentacoes.findAllLimit", query = "SELECT e FROM EzattaMovimentacoes e ORDER BY e.id DESC"),
    @NamedQuery(name = "EzattaMovimentacoes.findById", query = "SELECT e FROM EzattaMovimentacoes e WHERE e.id = :id"),
    @NamedQuery(name = "EzattaMovimentacoes.findByQtdEstoque", query = "SELECT e FROM EzattaMovimentacoes e WHERE e.qtdEstoque = :qtdEstoque"),
    @NamedQuery(name = "EzattaMovimentacoes.findByDataEstoque", query = "SELECT e FROM EzattaMovimentacoes e WHERE e.dataEstoque = :dataEstoque"),
    @NamedQuery(name = "EzattaMovimentacoes.findByOs", query = "SELECT e FROM EzattaMovimentacoes e WHERE e.os = :os"),
    @NamedQuery(name = "EzattaMovimentacoes.findByPlaca", query = "SELECT e FROM EzattaMovimentacoes e WHERE e.placa = :placa"),
    @NamedQuery(name = "EzattaMovimentacoes.findByKm", query = "SELECT e FROM EzattaMovimentacoes e WHERE e.km = :km"),
    @NamedQuery(name = "EzattaMovimentacoes.findMaxIdEstoque", query = "SELECT MAX(e.id) FROM EzattaMovimentacoes e"),
    @NamedQuery(name = "EzattaMovimentacoes.findByStatus", query = "SELECT e FROM EzattaMovimentacoes e WHERE e.status = :status")})
public class EzattaMovimentacoes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @Colunas(nome = "Código", size = 100)
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qtd_estoque", precision = 12)
    @Colunas(nome = "Qtd.", size = 100)
    private Float qtdEstoque;
    @Column(name = "data_estoque")
    @Temporal(TemporalType.TIMESTAMP)
    @Colunas(nome = "Data", size = 100)
    private Date dataEstoque;
    @Column(name = "os", length = 255)
    @Colunas(nome = "O.S", size = 100)
    private String os;
    @Column(name = "placa", length = 255)
    @Colunas(nome = "Placa", size = 100)
    private String placa;
    @Column(name = "km", length = 255)
    @Colunas(nome = "KM", size = 100)
    private String km;
    @Column(name = "STATUS")
   // @Colunas(nome = "Status", size = 100)
    private Integer status;
    @JoinColumn(name = "operador", referencedColumnName = "id")
    @ManyToOne
    @Colunas(nome = "Operador", size = 100)
    private EzattaOperador operador;
    @JoinColumn(name = "bico", referencedColumnName = "ID")
    @ManyToOne
    @Colunas(nome = "Bico", size = 100)
    private EzattaBico bico;
    @JoinColumn(name = "produto", referencedColumnName = "id")
    @ManyToOne
    @Colunas(nome = "Produto", size = 100)
    private EzattaProduto produto;
    @JoinColumn(name = "usuario", referencedColumnName = "ID")
    @ManyToOne
    @Colunas(nome = "Usuário", size = 100)
    private EzattaUsuario usuario;

    public EzattaMovimentacoes() {
    }

    public EzattaMovimentacoes(Integer id) {
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
        if (!(object instanceof EzattaMovimentacoes)) {
            return false;
        }
        EzattaMovimentacoes other = (EzattaMovimentacoes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "EzattaEstoque{" + "id=" + id + ", qtdEstoque=" + qtdEstoque + ", dataEstoque=" + dataEstoque + ", os=" + os + ", placa=" + placa + ", km=" + km + ", status=" + status + ", operador=" + operador + ", bico=" + bico + ", produto=" + produto + ", usuario=" + usuario + '}';

        //DATE
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String dat = dt.format(dataEstoque);
        
        return "Data: "+dat+" - Qtd.: "+ qtdEstoque +" - O.S: " + os + " - Placa: " + placa + " - Bico: " + bico + " - Produto: " + produto + " - Operador: " + operador ;
    }

    
    
}
