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
 * @author essalud
 */
@Entity
@Table(name = "turno")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Turno.findAll", query = "SELECT t FROM Turno t"),
    @NamedQuery(name = "Turno.findByIdTurno", query = "SELECT t FROM Turno t WHERE t.turnoPK.idTurno = :idTurno"),
    @NamedQuery(name = "Turno.findByActividadcodigo", query = "SELECT t FROM Turno t WHERE t.turnoPK.actividadcodigo = :actividadcodigo"),
    @NamedQuery(name = "Turno.findByDetalleServicioMedicoidDetalleServicioMedico", query = "SELECT t FROM Turno t WHERE t.turnoPK.detalleServicioMedicoidDetalleServicioMedico = :detalleServicioMedicoidDetalleServicioMedico"),
    @NamedQuery(name = "Turno.findByDetalleServicioMedicoServiciocodigo", query = "SELECT t FROM Turno t WHERE t.turnoPK.detalleServicioMedicoServiciocodigo = :detalleServicioMedicoServiciocodigo"),
    @NamedQuery(name = "Turno.findByDetalleServicioMedicoMedicocmp", query = "SELECT t FROM Turno t WHERE t.turnoPK.detalleServicioMedicoMedicocmp = :detalleServicioMedicoMedicocmp"),
    @NamedQuery(name = "Turno.findByDetalleServicioMedicoMedicodni", query = "SELECT t FROM Turno t WHERE t.turnoPK.detalleServicioMedicoMedicodni = :detalleServicioMedicoMedicodni"),
    @NamedQuery(name = "Turno.findByFecha", query = "SELECT t FROM Turno t WHERE t.fecha = :fecha")})
public class Turno implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TurnoPK turnoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumns({
        @JoinColumn(name = "DetalleServicioMedico_idDetalleServicioMedico", referencedColumnName = "idDetalleServicioMedico", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleServicioMedico_Servicio_codigo", referencedColumnName = "Servicio_codigo", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleServicioMedico_Medico_cmp", referencedColumnName = "Medico_cmp", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleServicioMedico_Medico_dni", referencedColumnName = "Medico_dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Detalleserviciomedico detalleserviciomedico;
    @JoinColumn(name = "Actividad_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Actividad actividad;

    public Turno() {
    }

    public Turno(TurnoPK turnoPK) {
        this.turnoPK = turnoPK;
    }

    public Turno(TurnoPK turnoPK, Date fecha) {
        this.turnoPK = turnoPK;
        this.fecha = fecha;
    }

    public Turno(int idTurno, String actividadcodigo, int detalleServicioMedicoidDetalleServicioMedico, String detalleServicioMedicoServiciocodigo, String detalleServicioMedicoMedicocmp, String detalleServicioMedicoMedicodni) {
        this.turnoPK = new TurnoPK(idTurno, actividadcodigo, detalleServicioMedicoidDetalleServicioMedico, detalleServicioMedicoServiciocodigo, detalleServicioMedicoMedicocmp, detalleServicioMedicoMedicodni);
    }

    public TurnoPK getTurnoPK() {
        return turnoPK;
    }

    public void setTurnoPK(TurnoPK turnoPK) {
        this.turnoPK = turnoPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Detalleserviciomedico getDetalleserviciomedico() {
        return detalleserviciomedico;
    }

    public void setDetalleserviciomedico(Detalleserviciomedico detalleserviciomedico) {
        this.detalleserviciomedico = detalleserviciomedico;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (turnoPK != null ? turnoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Turno)) {
            return false;
        }
        Turno other = (Turno) object;
        if ((this.turnoPK == null && other.turnoPK != null) || (this.turnoPK != null && !this.turnoPK.equals(other.turnoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Turno[ turnoPK=" + turnoPK + " ]";
    }
    
}
