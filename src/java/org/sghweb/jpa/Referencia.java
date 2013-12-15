/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "referencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Referencia.findAll", query = "SELECT r FROM Referencia r"),
    @NamedQuery(name = "Referencia.findByNumeroRegistro", query = "SELECT r FROM Referencia r WHERE r.referenciaPK.numeroRegistro = :numeroRegistro"),
    @NamedQuery(name = "Referencia.findByPacientedni", query = "SELECT r FROM Referencia r WHERE r.referenciaPK.pacientedni = :pacientedni"),
    @NamedQuery(name = "Referencia.findByFechaEmision", query = "SELECT r FROM Referencia r WHERE r.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "Referencia.findByFechaRecibida", query = "SELECT r FROM Referencia r WHERE r.fechaRecibida = :fechaRecibida"),
    @NamedQuery(name = "Referencia.findByFechaTermino", query = "SELECT r FROM Referencia r WHERE r.fechaTermino = :fechaTermino"),
    @NamedQuery(name = "Referencia.findByMotivo", query = "SELECT r FROM Referencia r WHERE r.motivo = :motivo"),
    @NamedQuery(name = "Referencia.findByEstado", query = "SELECT r FROM Referencia r WHERE r.estado = :estado")})
public class Referencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReferenciaPK referenciaPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaEmision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaRecibida")
    @Temporal(TemporalType.DATE)
    private Date fechaRecibida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaTermino")
    @Temporal(TemporalType.DATE)
    private Date fechaTermino;
    @Size(max = 300)
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "referencia")
    private List<Detallereferenciadiagnostico> detallereferenciadiagnosticoList;
    @JoinColumn(name = "Paciente_dni", referencedColumnName = "dni", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Paciente paciente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "referencia")
    private List<Detallereferenciaservicio> detallereferenciaservicioList;

    public Referencia() {
    }

    public Referencia(ReferenciaPK referenciaPK) {
        this.referenciaPK = referenciaPK;
    }

    public Referencia(ReferenciaPK referenciaPK, Date fechaEmision, Date fechaRecibida, Date fechaTermino) {
        this.referenciaPK = referenciaPK;
        this.fechaEmision = fechaEmision;
        this.fechaRecibida = fechaRecibida;
        this.fechaTermino = fechaTermino;
    }

    public Referencia(String numeroRegistro, String pacientedni) {
        this.referenciaPK = new ReferenciaPK(numeroRegistro, pacientedni);
    }

    public ReferenciaPK getReferenciaPK() {
        return referenciaPK;
    }

    public void setReferenciaPK(ReferenciaPK referenciaPK) {
        this.referenciaPK = referenciaPK;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaRecibida() {
        return fechaRecibida;
    }

    public void setFechaRecibida(Date fechaRecibida) {
        this.fechaRecibida = fechaRecibida;
    }

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @XmlTransient
    public List<Detallereferenciaservicio> getDetallereferenciaservicioList() {
        return detallereferenciaservicioList;
    }

    public void setDetallereferenciaservicioList(List<Detallereferenciaservicio> detallereferenciaservicioList) {
        this.detallereferenciaservicioList = detallereferenciaservicioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (referenciaPK != null ? referenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Referencia)) {
            return false;
        }
        Referencia other = (Referencia) object;
        if ((this.referenciaPK == null && other.referenciaPK != null) || (this.referenciaPK != null && !this.referenciaPK.equals(other.referenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Referencia[ referenciaPK=" + referenciaPK + " ]";
    }
    
}
