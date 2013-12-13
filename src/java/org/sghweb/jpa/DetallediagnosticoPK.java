/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author essalud
 */
@Embeddable
public class DetallediagnosticoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idDetalleDiagnostico")
    private int idDetalleDiagnostico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "Diagnostico_codigo")
    private String diagnosticocodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DetalleHistoriaClinica_idDetalleHistoriaClinica")
    private int detalleHistoriaClinicaidDetalleHistoriaClinica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "DetalleHistoriaClinica_Cita_actoMedico")
    private String detalleHistoriaClinicaCitaactoMedico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "DetalleHistoriaClinica_Cita_Medico_cmp")
    private String detalleHistoriaClinicaCitaMedicocmp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "DetalleHistoriaClinica_Cita_Medico_dni")
    private String detalleHistoriaClinicaCitaMedicodni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "DetalleHistoriaClinica_HistoriaClinica_numeroRegistro")
    private String detalleHistoriaClinicaHistoriaClinicanumeroRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "DetalleHistoriaClinica_HistoriaClinica_autogenerado")
    private String detalleHistoriaClinicaHistoriaClinicaautogenerado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "DetalleHistoriaClinica_HistoriaClinica_Paciente_dni")
    private String detalleHistoriaClinicaHistoriaClinicaPacientedni;

    public DetallediagnosticoPK() {
    }

    public DetallediagnosticoPK(int idDetalleDiagnostico, String diagnosticocodigo, int detalleHistoriaClinicaidDetalleHistoriaClinica, String detalleHistoriaClinicaCitaactoMedico, String detalleHistoriaClinicaCitaMedicocmp, String detalleHistoriaClinicaCitaMedicodni, String detalleHistoriaClinicaHistoriaClinicanumeroRegistro, String detalleHistoriaClinicaHistoriaClinicaautogenerado, String detalleHistoriaClinicaHistoriaClinicaPacientedni) {
        this.idDetalleDiagnostico = idDetalleDiagnostico;
        this.diagnosticocodigo = diagnosticocodigo;
        this.detalleHistoriaClinicaidDetalleHistoriaClinica = detalleHistoriaClinicaidDetalleHistoriaClinica;
        this.detalleHistoriaClinicaCitaactoMedico = detalleHistoriaClinicaCitaactoMedico;
        this.detalleHistoriaClinicaCitaMedicocmp = detalleHistoriaClinicaCitaMedicocmp;
        this.detalleHistoriaClinicaCitaMedicodni = detalleHistoriaClinicaCitaMedicodni;
        this.detalleHistoriaClinicaHistoriaClinicanumeroRegistro = detalleHistoriaClinicaHistoriaClinicanumeroRegistro;
        this.detalleHistoriaClinicaHistoriaClinicaautogenerado = detalleHistoriaClinicaHistoriaClinicaautogenerado;
        this.detalleHistoriaClinicaHistoriaClinicaPacientedni = detalleHistoriaClinicaHistoriaClinicaPacientedni;
    }

    public int getIdDetalleDiagnostico() {
        return idDetalleDiagnostico;
    }

    public void setIdDetalleDiagnostico(int idDetalleDiagnostico) {
        this.idDetalleDiagnostico = idDetalleDiagnostico;
    }

    public String getDiagnosticocodigo() {
        return diagnosticocodigo;
    }

    public void setDiagnosticocodigo(String diagnosticocodigo) {
        this.diagnosticocodigo = diagnosticocodigo;
    }

    public int getDetalleHistoriaClinicaidDetalleHistoriaClinica() {
        return detalleHistoriaClinicaidDetalleHistoriaClinica;
    }

    public void setDetalleHistoriaClinicaidDetalleHistoriaClinica(int detalleHistoriaClinicaidDetalleHistoriaClinica) {
        this.detalleHistoriaClinicaidDetalleHistoriaClinica = detalleHistoriaClinicaidDetalleHistoriaClinica;
    }

    public String getDetalleHistoriaClinicaCitaactoMedico() {
        return detalleHistoriaClinicaCitaactoMedico;
    }

    public void setDetalleHistoriaClinicaCitaactoMedico(String detalleHistoriaClinicaCitaactoMedico) {
        this.detalleHistoriaClinicaCitaactoMedico = detalleHistoriaClinicaCitaactoMedico;
    }

    public String getDetalleHistoriaClinicaCitaMedicocmp() {
        return detalleHistoriaClinicaCitaMedicocmp;
    }

    public void setDetalleHistoriaClinicaCitaMedicocmp(String detalleHistoriaClinicaCitaMedicocmp) {
        this.detalleHistoriaClinicaCitaMedicocmp = detalleHistoriaClinicaCitaMedicocmp;
    }

    public String getDetalleHistoriaClinicaCitaMedicodni() {
        return detalleHistoriaClinicaCitaMedicodni;
    }

    public void setDetalleHistoriaClinicaCitaMedicodni(String detalleHistoriaClinicaCitaMedicodni) {
        this.detalleHistoriaClinicaCitaMedicodni = detalleHistoriaClinicaCitaMedicodni;
    }

    public String getDetalleHistoriaClinicaHistoriaClinicanumeroRegistro() {
        return detalleHistoriaClinicaHistoriaClinicanumeroRegistro;
    }

    public void setDetalleHistoriaClinicaHistoriaClinicanumeroRegistro(String detalleHistoriaClinicaHistoriaClinicanumeroRegistro) {
        this.detalleHistoriaClinicaHistoriaClinicanumeroRegistro = detalleHistoriaClinicaHistoriaClinicanumeroRegistro;
    }

    public String getDetalleHistoriaClinicaHistoriaClinicaautogenerado() {
        return detalleHistoriaClinicaHistoriaClinicaautogenerado;
    }

    public void setDetalleHistoriaClinicaHistoriaClinicaautogenerado(String detalleHistoriaClinicaHistoriaClinicaautogenerado) {
        this.detalleHistoriaClinicaHistoriaClinicaautogenerado = detalleHistoriaClinicaHistoriaClinicaautogenerado;
    }

    public String getDetalleHistoriaClinicaHistoriaClinicaPacientedni() {
        return detalleHistoriaClinicaHistoriaClinicaPacientedni;
    }

    public void setDetalleHistoriaClinicaHistoriaClinicaPacientedni(String detalleHistoriaClinicaHistoriaClinicaPacientedni) {
        this.detalleHistoriaClinicaHistoriaClinicaPacientedni = detalleHistoriaClinicaHistoriaClinicaPacientedni;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDetalleDiagnostico;
        hash += (diagnosticocodigo != null ? diagnosticocodigo.hashCode() : 0);
        hash += (int) detalleHistoriaClinicaidDetalleHistoriaClinica;
        hash += (detalleHistoriaClinicaCitaactoMedico != null ? detalleHistoriaClinicaCitaactoMedico.hashCode() : 0);
        hash += (detalleHistoriaClinicaCitaMedicocmp != null ? detalleHistoriaClinicaCitaMedicocmp.hashCode() : 0);
        hash += (detalleHistoriaClinicaCitaMedicodni != null ? detalleHistoriaClinicaCitaMedicodni.hashCode() : 0);
        hash += (detalleHistoriaClinicaHistoriaClinicanumeroRegistro != null ? detalleHistoriaClinicaHistoriaClinicanumeroRegistro.hashCode() : 0);
        hash += (detalleHistoriaClinicaHistoriaClinicaautogenerado != null ? detalleHistoriaClinicaHistoriaClinicaautogenerado.hashCode() : 0);
        hash += (detalleHistoriaClinicaHistoriaClinicaPacientedni != null ? detalleHistoriaClinicaHistoriaClinicaPacientedni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallediagnosticoPK)) {
            return false;
        }
        DetallediagnosticoPK other = (DetallediagnosticoPK) object;
        if (this.idDetalleDiagnostico != other.idDetalleDiagnostico) {
            return false;
        }
        if ((this.diagnosticocodigo == null && other.diagnosticocodigo != null) || (this.diagnosticocodigo != null && !this.diagnosticocodigo.equals(other.diagnosticocodigo))) {
            return false;
        }
        if (this.detalleHistoriaClinicaidDetalleHistoriaClinica != other.detalleHistoriaClinicaidDetalleHistoriaClinica) {
            return false;
        }
        if ((this.detalleHistoriaClinicaCitaactoMedico == null && other.detalleHistoriaClinicaCitaactoMedico != null) || (this.detalleHistoriaClinicaCitaactoMedico != null && !this.detalleHistoriaClinicaCitaactoMedico.equals(other.detalleHistoriaClinicaCitaactoMedico))) {
            return false;
        }
        if ((this.detalleHistoriaClinicaCitaMedicocmp == null && other.detalleHistoriaClinicaCitaMedicocmp != null) || (this.detalleHistoriaClinicaCitaMedicocmp != null && !this.detalleHistoriaClinicaCitaMedicocmp.equals(other.detalleHistoriaClinicaCitaMedicocmp))) {
            return false;
        }
        if ((this.detalleHistoriaClinicaCitaMedicodni == null && other.detalleHistoriaClinicaCitaMedicodni != null) || (this.detalleHistoriaClinicaCitaMedicodni != null && !this.detalleHistoriaClinicaCitaMedicodni.equals(other.detalleHistoriaClinicaCitaMedicodni))) {
            return false;
        }
        if ((this.detalleHistoriaClinicaHistoriaClinicanumeroRegistro == null && other.detalleHistoriaClinicaHistoriaClinicanumeroRegistro != null) || (this.detalleHistoriaClinicaHistoriaClinicanumeroRegistro != null && !this.detalleHistoriaClinicaHistoriaClinicanumeroRegistro.equals(other.detalleHistoriaClinicaHistoriaClinicanumeroRegistro))) {
            return false;
        }
        if ((this.detalleHistoriaClinicaHistoriaClinicaautogenerado == null && other.detalleHistoriaClinicaHistoriaClinicaautogenerado != null) || (this.detalleHistoriaClinicaHistoriaClinicaautogenerado != null && !this.detalleHistoriaClinicaHistoriaClinicaautogenerado.equals(other.detalleHistoriaClinicaHistoriaClinicaautogenerado))) {
            return false;
        }
        if ((this.detalleHistoriaClinicaHistoriaClinicaPacientedni == null && other.detalleHistoriaClinicaHistoriaClinicaPacientedni != null) || (this.detalleHistoriaClinicaHistoriaClinicaPacientedni != null && !this.detalleHistoriaClinicaHistoriaClinicaPacientedni.equals(other.detalleHistoriaClinicaHistoriaClinicaPacientedni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.DetallediagnosticoPK[ idDetalleDiagnostico=" + idDetalleDiagnostico + ", diagnosticocodigo=" + diagnosticocodigo + ", detalleHistoriaClinicaidDetalleHistoriaClinica=" + detalleHistoriaClinicaidDetalleHistoriaClinica + ", detalleHistoriaClinicaCitaactoMedico=" + detalleHistoriaClinicaCitaactoMedico + ", detalleHistoriaClinicaCitaMedicocmp=" + detalleHistoriaClinicaCitaMedicocmp + ", detalleHistoriaClinicaCitaMedicodni=" + detalleHistoriaClinicaCitaMedicodni + ", detalleHistoriaClinicaHistoriaClinicanumeroRegistro=" + detalleHistoriaClinicaHistoriaClinicanumeroRegistro + ", detalleHistoriaClinicaHistoriaClinicaautogenerado=" + detalleHistoriaClinicaHistoriaClinicaautogenerado + ", detalleHistoriaClinicaHistoriaClinicaPacientedni=" + detalleHistoriaClinicaHistoriaClinicaPacientedni + " ]";
    }
    
}
