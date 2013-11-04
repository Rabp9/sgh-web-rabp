/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author essalud
 */
@Entity
@Table(name = "historiaclinica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historiaclinica.findAll", query = "SELECT h FROM Historiaclinica h"),
    @NamedQuery(name = "Historiaclinica.findByNumeroRegistro", query = "SELECT h FROM Historiaclinica h WHERE h.historiaclinicaPK.numeroRegistro = :numeroRegistro"),
    @NamedQuery(name = "Historiaclinica.findByAutogenerado", query = "SELECT h FROM Historiaclinica h WHERE h.historiaclinicaPK.autogenerado = :autogenerado"),
    @NamedQuery(name = "Historiaclinica.findByPacientedni", query = "SELECT h FROM Historiaclinica h WHERE h.historiaclinicaPK.pacientedni = :pacientedni"),
    @NamedQuery(name = "Historiaclinica.findByFechaInicio", query = "SELECT h FROM Historiaclinica h WHERE h.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Historiaclinica.findByFechaUltimaCita", query = "SELECT h FROM Historiaclinica h WHERE h.fechaUltimaCita = :fechaUltimaCita"),
    @NamedQuery(name = "Historiaclinica.findByNumCitasUltimoMes", query = "SELECT h FROM Historiaclinica h WHERE h.numCitasUltimoMes = :numCitasUltimoMes"),
    @NamedQuery(name = "Historiaclinica.findByNumCitasTotal", query = "SELECT h FROM Historiaclinica h WHERE h.numCitasTotal = :numCitasTotal"),
    @NamedQuery(name = "Historiaclinica.findByEstado", query = "SELECT h FROM Historiaclinica h WHERE h.estado = :estado")})
public class Historiaclinica implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HistoriaclinicaPK historiaclinicaPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaUltimaCita")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaCita;
    @Column(name = "numCitasUltimoMes")
    private Integer numCitasUltimoMes;
    @Column(name = "numCitasTotal")
    private Integer numCitasTotal;
    @Column(name = "estado")
    private Short estado;
    @JoinColumn(name = "Paciente_dni", referencedColumnName = "dni", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Paciente paciente;

    public Historiaclinica() {
    }

    public Historiaclinica(HistoriaclinicaPK historiaclinicaPK) {
        this.historiaclinicaPK = historiaclinicaPK;
    }

    public Historiaclinica(HistoriaclinicaPK historiaclinicaPK, Date fechaInicio, Date fechaUltimaCita) {
        this.historiaclinicaPK = historiaclinicaPK;
        this.fechaInicio = fechaInicio;
        this.fechaUltimaCita = fechaUltimaCita;
    }

    public Historiaclinica(String numeroRegistro, String autogenerado, String pacientedni) {
        this.historiaclinicaPK = new HistoriaclinicaPK(numeroRegistro, autogenerado, pacientedni);
    }

    public HistoriaclinicaPK getHistoriaclinicaPK() {
        return historiaclinicaPK;
    }

    public void setHistoriaclinicaPK(HistoriaclinicaPK historiaclinicaPK) {
        this.historiaclinicaPK = historiaclinicaPK;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaUltimaCita() {
        return fechaUltimaCita;
    }

    public void setFechaUltimaCita(Date fechaUltimaCita) {
        this.fechaUltimaCita = fechaUltimaCita;
    }

    public Integer getNumCitasUltimoMes() {
        return numCitasUltimoMes;
    }

    public void setNumCitasUltimoMes(Integer numCitasUltimoMes) {
        this.numCitasUltimoMes = numCitasUltimoMes;
    }

    public Integer getNumCitasTotal() {
        return numCitasTotal;
    }

    public void setNumCitasTotal(Integer numCitasTotal) {
        this.numCitasTotal = numCitasTotal;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historiaclinicaPK != null ? historiaclinicaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historiaclinica)) {
            return false;
        }
        Historiaclinica other = (Historiaclinica) object;
        if ((this.historiaclinicaPK == null && other.historiaclinicaPK != null) || (this.historiaclinicaPK != null && !this.historiaclinicaPK.equals(other.historiaclinicaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Historiaclinica[ historiaclinicaPK=" + historiaclinicaPK + " ]";
    }
    
}
