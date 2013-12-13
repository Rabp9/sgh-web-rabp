/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author essalud
 */
@Entity
@Table(name = "diagnostico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Diagnostico.findAll", query = "SELECT d FROM Diagnostico d"),
    @NamedQuery(name = "Diagnostico.findByCodigo", query = "SELECT d FROM Diagnostico d WHERE d.codigo = :codigo"),
    @NamedQuery(name = "Diagnostico.findByDescripcion", query = "SELECT d FROM Diagnostico d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "Diagnostico.findBySexoAfectado", query = "SELECT d FROM Diagnostico d WHERE d.sexoAfectado = :sexoAfectado"),
    @NamedQuery(name = "Diagnostico.findByEstado", query = "SELECT d FROM Diagnostico d WHERE d.estado = :estado")})
public class Diagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "sexoAfectado")
    private String sexoAfectado;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "diagnostico")
    private List<Detallereferenciadiagnostico> detallereferenciadiagnosticoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "diagnostico")
    private List<Detallediagnostico> detallediagnosticoList;

    public Diagnostico() {
    }

    public Diagnostico(String codigo) {
        this.codigo = codigo;
    }

    public Diagnostico(String codigo, String sexoAfectado) {
        this.codigo = codigo;
        this.sexoAfectado = sexoAfectado;
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

    public String getSexoAfectado() {
        return sexoAfectado;
    }

    public void setSexoAfectado(String sexoAfectado) {
        this.sexoAfectado = sexoAfectado;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Detallereferenciadiagnostico> getDetallereferenciadiagnosticoList() {
        return detallereferenciadiagnosticoList;
    }

    public void setDetallereferenciadiagnosticoList(List<Detallereferenciadiagnostico> detallereferenciadiagnosticoList) {
        this.detallereferenciadiagnosticoList = detallereferenciadiagnosticoList;
    }

    @XmlTransient
    public List<Detallediagnostico> getDetallediagnosticoList() {
        return detallediagnosticoList;
    }

    public void setDetallediagnosticoList(List<Detallediagnostico> detallediagnosticoList) {
        this.detallediagnosticoList = detallediagnosticoList;
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
        if (!(object instanceof Diagnostico)) {
            return false;
        }
        Diagnostico other = (Diagnostico) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Diagnostico[ codigo=" + codigo + " ]";
    }
    
}
