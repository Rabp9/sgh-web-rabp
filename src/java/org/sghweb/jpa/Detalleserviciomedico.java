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
import javax.persistence.JoinColumns;
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
 * @author Roberto
 */
@Entity
@Table(name = "detalleserviciomedico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleserviciomedico.findAll", query = "SELECT d FROM Detalleserviciomedico d"),
    @NamedQuery(name = "Detalleserviciomedico.findByServiciocodigo", query = "SELECT d FROM Detalleserviciomedico d WHERE d.detalleserviciomedicoPK.serviciocodigo = :serviciocodigo"),
    @NamedQuery(name = "Detalleserviciomedico.findByMedicocmp", query = "SELECT d FROM Detalleserviciomedico d WHERE d.detalleserviciomedicoPK.medicocmp = :medicocmp"),
    @NamedQuery(name = "Detalleserviciomedico.findByMedicodni", query = "SELECT d FROM Detalleserviciomedico d WHERE d.detalleserviciomedicoPK.medicodni = :medicodni"),
    @NamedQuery(name = "Detalleserviciomedico.findByFechaInicio", query = "SELECT d FROM Detalleserviciomedico d WHERE d.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Detalleserviciomedico.findByFechaFin", query = "SELECT d FROM Detalleserviciomedico d WHERE d.fechaFin = :fechaFin")})
public class Detalleserviciomedico implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleserviciomedicoPK detalleserviciomedicoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fechaFin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @JoinColumns({
        @JoinColumn(name = "Medico_cmp", referencedColumnName = "cmp", insertable = false, updatable = false),
        @JoinColumn(name = "Medico_dni", referencedColumnName = "dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Medico medico;
    @JoinColumn(name = "Servicio_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Servicio servicio;

    public Detalleserviciomedico() {
    }

    public Detalleserviciomedico(DetalleserviciomedicoPK detalleserviciomedicoPK) {
        this.detalleserviciomedicoPK = detalleserviciomedicoPK;
    }

    public Detalleserviciomedico(DetalleserviciomedicoPK detalleserviciomedicoPK, Date fechaInicio) {
        this.detalleserviciomedicoPK = detalleserviciomedicoPK;
        this.fechaInicio = fechaInicio;
    }

    public Detalleserviciomedico(String serviciocodigo, String medicocmp, String medicodni) {
        this.detalleserviciomedicoPK = new DetalleserviciomedicoPK(serviciocodigo, medicocmp, medicodni);
    }

    public DetalleserviciomedicoPK getDetalleserviciomedicoPK() {
        return detalleserviciomedicoPK;
    }

    public void setDetalleserviciomedicoPK(DetalleserviciomedicoPK detalleserviciomedicoPK) {
        this.detalleserviciomedicoPK = detalleserviciomedicoPK;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleserviciomedicoPK != null ? detalleserviciomedicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleserviciomedico)) {
            return false;
        }
        Detalleserviciomedico other = (Detalleserviciomedico) object;
        if ((this.detalleserviciomedicoPK == null && other.detalleserviciomedicoPK != null) || (this.detalleserviciomedicoPK != null && !this.detalleserviciomedicoPK.equals(other.detalleserviciomedicoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Detalleserviciomedico[ detalleserviciomedicoPK=" + detalleserviciomedicoPK + " ]";
    }
    
}
