/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author essalud
 */
@Entity
@Table(name = "detallehistoriaclinica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallehistoriaclinica.findAll", query = "SELECT d FROM Detallehistoriaclinica d"),
    @NamedQuery(name = "Detallehistoriaclinica.findByIdDetalleHistoriaClinica", query = "SELECT d FROM Detallehistoriaclinica d WHERE d.detallehistoriaclinicaPK.idDetalleHistoriaClinica = :idDetalleHistoriaClinica"),
    @NamedQuery(name = "Detallehistoriaclinica.findByCitaactoMedico", query = "SELECT d FROM Detallehistoriaclinica d WHERE d.detallehistoriaclinicaPK.citaactoMedico = :citaactoMedico"),
    @NamedQuery(name = "Detallehistoriaclinica.findByCitaMedicocmp", query = "SELECT d FROM Detallehistoriaclinica d WHERE d.detallehistoriaclinicaPK.citaMedicocmp = :citaMedicocmp"),
    @NamedQuery(name = "Detallehistoriaclinica.findByCitaMedicodni", query = "SELECT d FROM Detallehistoriaclinica d WHERE d.detallehistoriaclinicaPK.citaMedicodni = :citaMedicodni"),
    @NamedQuery(name = "Detallehistoriaclinica.findByHistoriaClinicanumeroRegistro", query = "SELECT d FROM Detallehistoriaclinica d WHERE d.detallehistoriaclinicaPK.historiaClinicanumeroRegistro = :historiaClinicanumeroRegistro"),
    @NamedQuery(name = "Detallehistoriaclinica.findByHistoriaClinicaautogenerado", query = "SELECT d FROM Detallehistoriaclinica d WHERE d.detallehistoriaclinicaPK.historiaClinicaautogenerado = :historiaClinicaautogenerado"),
    @NamedQuery(name = "Detallehistoriaclinica.findByHistoriaClinicaPacientedni", query = "SELECT d FROM Detallehistoriaclinica d WHERE d.detallehistoriaclinicaPK.historiaClinicaPacientedni = :historiaClinicaPacientedni"),
    @NamedQuery(name = "Detallehistoriaclinica.findByFecha", query = "SELECT d FROM Detallehistoriaclinica d WHERE d.fecha = :fecha")})
public class Detallehistoriaclinica implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallehistoriaclinicaPK detallehistoriaclinicaPK;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detallehistoriaclinica")
    private List<Citt> cittList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detallehistoriaclinica")
    private List<Detalleorden> detalleordenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detallehistoriaclinica")
    private List<Detallediagnostico> detallediagnosticoList;
    @JoinColumns({
        @JoinColumn(name = "Cita_actoMedico", referencedColumnName = "actoMedico", insertable = false, updatable = false),
        @JoinColumn(name = "Cita_Medico_cmp", referencedColumnName = "Medico_cmp", insertable = false, updatable = false),
        @JoinColumn(name = "Cita_Medico_dni", referencedColumnName = "Medico_dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Cita cita;
    @JoinColumns({
        @JoinColumn(name = "HistoriaClinica_numeroRegistro", referencedColumnName = "numeroRegistro", insertable = false, updatable = false),
        @JoinColumn(name = "HistoriaClinica_autogenerado", referencedColumnName = "autogenerado", insertable = false, updatable = false),
        @JoinColumn(name = "HistoriaClinica_Paciente_dni", referencedColumnName = "Paciente_dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Historiaclinica historiaclinica;

    public Detallehistoriaclinica() {
    }

    public Detallehistoriaclinica(DetallehistoriaclinicaPK detallehistoriaclinicaPK) {
        this.detallehistoriaclinicaPK = detallehistoriaclinicaPK;
    }

    public Detallehistoriaclinica(int idDetalleHistoriaClinica, String citaactoMedico, String citaMedicocmp, String citaMedicodni, String historiaClinicanumeroRegistro, String historiaClinicaautogenerado, String historiaClinicaPacientedni) {
        this.detallehistoriaclinicaPK = new DetallehistoriaclinicaPK(idDetalleHistoriaClinica, citaactoMedico, citaMedicocmp, citaMedicodni, historiaClinicanumeroRegistro, historiaClinicaautogenerado, historiaClinicaPacientedni);
    }

    public DetallehistoriaclinicaPK getDetallehistoriaclinicaPK() {
        return detallehistoriaclinicaPK;
    }

    public void setDetallehistoriaclinicaPK(DetallehistoriaclinicaPK detallehistoriaclinicaPK) {
        this.detallehistoriaclinicaPK = detallehistoriaclinicaPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @XmlTransient
    public List<Citt> getCittList() {
        return cittList;
    }

    public void setCittList(List<Citt> cittList) {
        this.cittList = cittList;
    }

    @XmlTransient
    public List<Detalleorden> getDetalleordenList() {
        return detalleordenList;
    }

    public void setDetalleordenList(List<Detalleorden> detalleordenList) {
        this.detalleordenList = detalleordenList;
    }

    @XmlTransient
    public List<Detallediagnostico> getDetallediagnosticoList() {
        return detallediagnosticoList;
    }

    public void setDetallediagnosticoList(List<Detallediagnostico> detallediagnosticoList) {
        this.detallediagnosticoList = detallediagnosticoList;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Historiaclinica getHistoriaclinica() {
        return historiaclinica;
    }

    public void setHistoriaclinica(Historiaclinica historiaclinica) {
        this.historiaclinica = historiaclinica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallehistoriaclinicaPK != null ? detallehistoriaclinicaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallehistoriaclinica)) {
            return false;
        }
        Detallehistoriaclinica other = (Detallehistoriaclinica) object;
        if ((this.detallehistoriaclinicaPK == null && other.detallehistoriaclinicaPK != null) || (this.detallehistoriaclinicaPK != null && !this.detallehistoriaclinicaPK.equals(other.detallehistoriaclinicaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Detallehistoriaclinica[ detallehistoriaclinicaPK=" + detallehistoriaclinicaPK + " ]";
    }
    
}
