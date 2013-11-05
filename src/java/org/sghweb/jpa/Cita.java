/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @NamedQuery(name = "Cita.findByActoMedico", query = "SELECT c FROM Cita c WHERE c.citaPK.actoMedico = :actoMedico"),
    @NamedQuery(name = "Cita.findByFecha", query = "SELECT c FROM Cita c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Cita.findByPacientedni", query = "SELECT c FROM Cita c WHERE c.citaPK.pacientedni = :pacientedni"),
    @NamedQuery(name = "Cita.findByMedicocmp", query = "SELECT c FROM Cita c WHERE c.citaPK.medicocmp = :medicocmp"),
    @NamedQuery(name = "Cita.findByMedicodni", query = "SELECT c FROM Cita c WHERE c.citaPK.medicodni = :medicodni")})
public class Cita implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CitaPK citaPK;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumns({
        @JoinColumn(name = "Medico_cmp", referencedColumnName = "cmp", insertable = false, updatable = false),
        @JoinColumn(name = "Medico_dni", referencedColumnName = "dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Medico medico;
    @JoinColumn(name = "Paciente_dni", referencedColumnName = "dni", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Paciente paciente;

    public Cita() {
    }

    public Cita(CitaPK citaPK) {
        this.citaPK = citaPK;
    }

    public Cita(int actoMedico, String pacientedni, String medicocmp, String medicodni) {
        this.citaPK = new CitaPK(actoMedico, pacientedni, medicocmp, medicodni);
    }

    public CitaPK getCitaPK() {
        return citaPK;
    }

    public void setCitaPK(CitaPK citaPK) {
        this.citaPK = citaPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
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
        hash += (citaPK != null ? citaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cita)) {
            return false;
        }
        Cita other = (Cita) object;
        if ((this.citaPK == null && other.citaPK != null) || (this.citaPK != null && !this.citaPK.equals(other.citaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Cita[ citaPK=" + citaPK + " ]";
    }
    
}
