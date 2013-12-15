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
@Table(name = "detallediagnostico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallediagnostico.findAll", query = "SELECT d FROM Detallediagnostico d"),
    @NamedQuery(name = "Detallediagnostico.findByIdDetalleDiagnostico", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.idDetalleDiagnostico = :idDetalleDiagnostico"),
    @NamedQuery(name = "Detallediagnostico.findByDiagnosticocodigo", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.diagnosticocodigo = :diagnosticocodigo"),
    @NamedQuery(name = "Detallediagnostico.findByDetalleHistoriaClinicaidDetalleHistoriaClinica", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.detalleHistoriaClinicaidDetalleHistoriaClinica = :detalleHistoriaClinicaidDetalleHistoriaClinica"),
    @NamedQuery(name = "Detallediagnostico.findByDetalleHistoriaClinicaCitaactoMedico", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.detalleHistoriaClinicaCitaactoMedico = :detalleHistoriaClinicaCitaactoMedico"),
    @NamedQuery(name = "Detallediagnostico.findByDetalleHistoriaClinicaCitaMedicocmp", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.detalleHistoriaClinicaCitaMedicocmp = :detalleHistoriaClinicaCitaMedicocmp"),
    @NamedQuery(name = "Detallediagnostico.findByDetalleHistoriaClinicaCitaMedicodni", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.detalleHistoriaClinicaCitaMedicodni = :detalleHistoriaClinicaCitaMedicodni"),
    @NamedQuery(name = "Detallediagnostico.findByDetalleHistoriaClinicaHistoriaClinicanumeroRegistro", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.detalleHistoriaClinicaHistoriaClinicanumeroRegistro = :detalleHistoriaClinicaHistoriaClinicanumeroRegistro"),
    @NamedQuery(name = "Detallediagnostico.findByDetalleHistoriaClinicaHistoriaClinicaautogenerado", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.detalleHistoriaClinicaHistoriaClinicaautogenerado = :detalleHistoriaClinicaHistoriaClinicaautogenerado"),
    @NamedQuery(name = "Detallediagnostico.findByDetalleHistoriaClinicaHistoriaClinicaPacientedni", query = "SELECT d FROM Detallediagnostico d WHERE d.detallediagnosticoPK.detalleHistoriaClinicaHistoriaClinicaPacientedni = :detalleHistoriaClinicaHistoriaClinicaPacientedni")})
public class Detallediagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallediagnosticoPK detallediagnosticoPK;
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
    @JoinColumn(name = "Diagnostico_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Diagnostico diagnostico;

    public Detallediagnostico() {
    }

    public Detallediagnostico(DetallediagnosticoPK detallediagnosticoPK) {
        this.detallediagnosticoPK = detallediagnosticoPK;
    }

    public Detallediagnostico(int idDetalleDiagnostico, String diagnosticocodigo, int detalleHistoriaClinicaidDetalleHistoriaClinica, String detalleHistoriaClinicaCitaactoMedico, String detalleHistoriaClinicaCitaMedicocmp, String detalleHistoriaClinicaCitaMedicodni, String detalleHistoriaClinicaHistoriaClinicanumeroRegistro, String detalleHistoriaClinicaHistoriaClinicaautogenerado, String detalleHistoriaClinicaHistoriaClinicaPacientedni) {
        this.detallediagnosticoPK = new DetallediagnosticoPK(idDetalleDiagnostico, diagnosticocodigo, detalleHistoriaClinicaidDetalleHistoriaClinica, detalleHistoriaClinicaCitaactoMedico, detalleHistoriaClinicaCitaMedicocmp, detalleHistoriaClinicaCitaMedicodni, detalleHistoriaClinicaHistoriaClinicanumeroRegistro, detalleHistoriaClinicaHistoriaClinicaautogenerado, detalleHistoriaClinicaHistoriaClinicaPacientedni);
    }

    public DetallediagnosticoPK getDetallediagnosticoPK() {
        return detallediagnosticoPK;
    }

    public void setDetallediagnosticoPK(DetallediagnosticoPK detallediagnosticoPK) {
        this.detallediagnosticoPK = detallediagnosticoPK;
    }

    public Detallehistoriaclinica getDetallehistoriaclinica() {
        return detallehistoriaclinica;
    }

    public void setDetallehistoriaclinica(Detallehistoriaclinica detallehistoriaclinica) {
        this.detallehistoriaclinica = detallehistoriaclinica;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallediagnosticoPK != null ? detallediagnosticoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallediagnostico)) {
            return false;
        }
        Detallediagnostico other = (Detallediagnostico) object;
        if ((this.detallediagnosticoPK == null && other.detallediagnosticoPK != null) || (this.detallediagnosticoPK != null && !this.detallediagnosticoPK.equals(other.detallediagnosticoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Detallediagnostico[ detallediagnosticoPK=" + detallediagnosticoPK + " ]";
    }
    
}
