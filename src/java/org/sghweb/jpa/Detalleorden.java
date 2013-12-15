/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "detalleorden")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleorden.findAll", query = "SELECT d FROM Detalleorden d"),
    @NamedQuery(name = "Detalleorden.findByIdDetalleOrden", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.idDetalleOrden = :idDetalleOrden"),
    @NamedQuery(name = "Detalleorden.findByDetalleHistoriaClinicaidDetalleHistoriaClinica", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.detalleHistoriaClinicaidDetalleHistoriaClinica = :detalleHistoriaClinicaidDetalleHistoriaClinica"),
    @NamedQuery(name = "Detalleorden.findByDetalleHistoriaClinicaCitaactoMedico", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.detalleHistoriaClinicaCitaactoMedico = :detalleHistoriaClinicaCitaactoMedico"),
    @NamedQuery(name = "Detalleorden.findByDetalleHistoriaClinicaCitaMedicocmp", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.detalleHistoriaClinicaCitaMedicocmp = :detalleHistoriaClinicaCitaMedicocmp"),
    @NamedQuery(name = "Detalleorden.findByDetalleHistoriaClinicaCitaMedicodni", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.detalleHistoriaClinicaCitaMedicodni = :detalleHistoriaClinicaCitaMedicodni"),
    @NamedQuery(name = "Detalleorden.findByDetalleHistoriaClinicaHistoriaClinicanumeroRegistro", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.detalleHistoriaClinicaHistoriaClinicanumeroRegistro = :detalleHistoriaClinicaHistoriaClinicanumeroRegistro"),
    @NamedQuery(name = "Detalleorden.findByDetalleHistoriaClinicaHistoriaClinicaautogenerado", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.detalleHistoriaClinicaHistoriaClinicaautogenerado = :detalleHistoriaClinicaHistoriaClinicaautogenerado"),
    @NamedQuery(name = "Detalleorden.findByDetalleHistoriaClinicaHistoriaClinicaPacientedni", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.detalleHistoriaClinicaHistoriaClinicaPacientedni = :detalleHistoriaClinicaHistoriaClinicaPacientedni"),
    @NamedQuery(name = "Detalleorden.findByOrdennroOrden", query = "SELECT d FROM Detalleorden d WHERE d.detalleordenPK.ordennroOrden = :ordennroOrden")})
public class Detalleorden implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleordenPK detalleordenPK;
    @JoinColumn(name = "Orden_nroOrden", referencedColumnName = "nroOrden", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orden orden;
    @JoinColumns({
        @JoinColumn(name = "DetalleHistoriaClinica_idDetalleHistoriaClinica", referencedColumnName = "idDetalleHistoriaClinica", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_Cita_actoMedico", referencedColumnName = "Cita_actoMedico", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_Cita_Medico_cmp", referencedColumnName = "Cita_Medico_cmp", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_Cita_Medico_dni", referencedColumnName = "Cita_Medico_dni", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_HistoriaClinica_numeroRegistro", referencedColumnName = "HistoriaClinica_numeroRegistro", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_HistoriaClinica_autogenerado", referencedColumnName = "HistoriaClinica_autogenerado", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_HistoriaClinica_Paciente_dni", referencedColumnName = "HistoriaClinica_Paciente_dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Detallehistoriaclinica detallehistoriaclinica;

    public Detalleorden() {
    }

    public Detalleorden(DetalleordenPK detalleordenPK) {
        this.detalleordenPK = detalleordenPK;
    }

    public Detalleorden(int idDetalleOrden, int detalleHistoriaClinicaidDetalleHistoriaClinica, String detalleHistoriaClinicaCitaactoMedico, String detalleHistoriaClinicaCitaMedicocmp, String detalleHistoriaClinicaCitaMedicodni, String detalleHistoriaClinicaHistoriaClinicanumeroRegistro, String detalleHistoriaClinicaHistoriaClinicaautogenerado, String detalleHistoriaClinicaHistoriaClinicaPacientedni, String ordennroOrden) {
        this.detalleordenPK = new DetalleordenPK(idDetalleOrden, detalleHistoriaClinicaidDetalleHistoriaClinica, detalleHistoriaClinicaCitaactoMedico, detalleHistoriaClinicaCitaMedicocmp, detalleHistoriaClinicaCitaMedicodni, detalleHistoriaClinicaHistoriaClinicanumeroRegistro, detalleHistoriaClinicaHistoriaClinicaautogenerado, detalleHistoriaClinicaHistoriaClinicaPacientedni, ordennroOrden);
    }

    public DetalleordenPK getDetalleordenPK() {
        return detalleordenPK;
    }

    public void setDetalleordenPK(DetalleordenPK detalleordenPK) {
        this.detalleordenPK = detalleordenPK;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Detallehistoriaclinica getDetallehistoriaclinica() {
        return detallehistoriaclinica;
    }

    public void setDetallehistoriaclinica(Detallehistoriaclinica detallehistoriaclinica) {
        this.detallehistoriaclinica = detallehistoriaclinica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleordenPK != null ? detalleordenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleorden)) {
            return false;
        }
        Detalleorden other = (Detalleorden) object;
        if ((this.detalleordenPK == null && other.detalleordenPK != null) || (this.detalleordenPK != null && !this.detalleordenPK.equals(other.detalleordenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Detalleorden[ detalleordenPK=" + detalleordenPK + " ]";
    }
    
}
