/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "medicamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicamento.findAll", query = "SELECT m FROM Medicamento m"),
    @NamedQuery(name = "Medicamento.findByCodigo", query = "SELECT m FROM Medicamento m WHERE m.codigo = :codigo"),
    @NamedQuery(name = "Medicamento.findByDescripcion", query = "SELECT m FROM Medicamento m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Medicamento.findByPresentacion", query = "SELECT m FROM Medicamento m WHERE m.presentacion = :presentacion"),
    @NamedQuery(name = "Medicamento.findByQsmin", query = "SELECT m FROM Medicamento m WHERE m.qsmin = :qsmin"),
    @NamedQuery(name = "Medicamento.findByQsmax", query = "SELECT m FROM Medicamento m WHERE m.qsmax = :qsmax"),
    @NamedQuery(name = "Medicamento.findBySecuencia", query = "SELECT m FROM Medicamento m WHERE m.secuencia = :secuencia"),
    @NamedQuery(name = "Medicamento.findByFechaIngreso", query = "SELECT m FROM Medicamento m WHERE m.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "Medicamento.findByFechaVencimiento", query = "SELECT m FROM Medicamento m WHERE m.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "Medicamento.findByEstado", query = "SELECT m FROM Medicamento m WHERE m.estado = :estado")})
public class Medicamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 60)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 2)
    @Column(name = "presentacion")
    private String presentacion;
    @Column(name = "qsmin")
    private Integer qsmin;
    @Column(name = "qsmax")
    private Integer qsmax;
    @Column(name = "secuencia")
    private Integer secuencia;
    @Column(name = "fechaIngreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "fechaVencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "estado")
    private Short estado;

    public Medicamento() {
    }

    public Medicamento(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Integer getQsmin() {
        return qsmin;
    }

    public void setQsmin(Integer qsmin) {
        this.qsmin = qsmin;
    }

    public Integer getQsmax() {
        return qsmax;
    }

    public void setQsmax(Integer qsmax) {
        this.qsmax = qsmax;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicamento)) {
            return false;
        }
        Medicamento other = (Medicamento) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Medicamento[ codigo=" + codigo + " ]";
    }
    
}
