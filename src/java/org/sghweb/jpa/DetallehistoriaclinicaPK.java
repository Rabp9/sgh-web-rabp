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
public class DetallehistoriaclinicaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idDetalleHistoriaClinica")
    private int idDetalleHistoriaClinica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "Cita_actoMedico")
    private String citaactoMedico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "Cita_Medico_cmp")
    private String citaMedicocmp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "Cita_Medico_dni")
    private String citaMedicodni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "HistoriaClinica_numeroRegistro")
    private String historiaClinicanumeroRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "HistoriaClinica_autogenerado")
    private String historiaClinicaautogenerado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "HistoriaClinica_Paciente_dni")
    private String historiaClinicaPacientedni;

    public DetallehistoriaclinicaPK() {
    }

    public DetallehistoriaclinicaPK(int idDetalleHistoriaClinica, String citaactoMedico, String citaMedicocmp, String citaMedicodni, String historiaClinicanumeroRegistro, String historiaClinicaautogenerado, String historiaClinicaPacientedni) {
        this.idDetalleHistoriaClinica = idDetalleHistoriaClinica;
        this.citaactoMedico = citaactoMedico;
        this.citaMedicocmp = citaMedicocmp;
        this.citaMedicodni = citaMedicodni;
        this.historiaClinicanumeroRegistro = historiaClinicanumeroRegistro;
        this.historiaClinicaautogenerado = historiaClinicaautogenerado;
        this.historiaClinicaPacientedni = historiaClinicaPacientedni;
    }

    public int getIdDetalleHistoriaClinica() {
        return idDetalleHistoriaClinica;
    }

    public void setIdDetalleHistoriaClinica(int idDetalleHistoriaClinica) {
        this.idDetalleHistoriaClinica = idDetalleHistoriaClinica;
    }

    public String getCitaactoMedico() {
        return citaactoMedico;
    }

    public void setCitaactoMedico(String citaactoMedico) {
        this.citaactoMedico = citaactoMedico;
    }

    public String getCitaMedicocmp() {
        return citaMedicocmp;
    }

    public void setCitaMedicocmp(String citaMedicocmp) {
        this.citaMedicocmp = citaMedicocmp;
    }

    public String getCitaMedicodni() {
        return citaMedicodni;
    }

    public void setCitaMedicodni(String citaMedicodni) {
        this.citaMedicodni = citaMedicodni;
    }

    public String getHistoriaClinicanumeroRegistro() {
        return historiaClinicanumeroRegistro;
    }

    public void setHistoriaClinicanumeroRegistro(String historiaClinicanumeroRegistro) {
        this.historiaClinicanumeroRegistro = historiaClinicanumeroRegistro;
    }

    public String getHistoriaClinicaautogenerado() {
        return historiaClinicaautogenerado;
    }

    public void setHistoriaClinicaautogenerado(String historiaClinicaautogenerado) {
        this.historiaClinicaautogenerado = historiaClinicaautogenerado;
    }

    public String getHistoriaClinicaPacientedni() {
        return historiaClinicaPacientedni;
    }

    public void setHistoriaClinicaPacientedni(String historiaClinicaPacientedni) {
        this.historiaClinicaPacientedni = historiaClinicaPacientedni;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDetalleHistoriaClinica;
        hash += (citaactoMedico != null ? citaactoMedico.hashCode() : 0);
        hash += (citaMedicocmp != null ? citaMedicocmp.hashCode() : 0);
        hash += (citaMedicodni != null ? citaMedicodni.hashCode() : 0);
        hash += (historiaClinicanumeroRegistro != null ? historiaClinicanumeroRegistro.hashCode() : 0);
        hash += (historiaClinicaautogenerado != null ? historiaClinicaautogenerado.hashCode() : 0);
        hash += (historiaClinicaPacientedni != null ? historiaClinicaPacientedni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallehistoriaclinicaPK)) {
            return false;
        }
        DetallehistoriaclinicaPK other = (DetallehistoriaclinicaPK) object;
        if (this.idDetalleHistoriaClinica != other.idDetalleHistoriaClinica) {
            return false;
        }
        if ((this.citaactoMedico == null && other.citaactoMedico != null) || (this.citaactoMedico != null && !this.citaactoMedico.equals(other.citaactoMedico))) {
            return false;
        }
        if ((this.citaMedicocmp == null && other.citaMedicocmp != null) || (this.citaMedicocmp != null && !this.citaMedicocmp.equals(other.citaMedicocmp))) {
            return false;
        }
        if ((this.citaMedicodni == null && other.citaMedicodni != null) || (this.citaMedicodni != null && !this.citaMedicodni.equals(other.citaMedicodni))) {
            return false;
        }
        if ((this.historiaClinicanumeroRegistro == null && other.historiaClinicanumeroRegistro != null) || (this.historiaClinicanumeroRegistro != null && !this.historiaClinicanumeroRegistro.equals(other.historiaClinicanumeroRegistro))) {
            return false;
        }
        if ((this.historiaClinicaautogenerado == null && other.historiaClinicaautogenerado != null) || (this.historiaClinicaautogenerado != null && !this.historiaClinicaautogenerado.equals(other.historiaClinicaautogenerado))) {
            return false;
        }
        if ((this.historiaClinicaPacientedni == null && other.historiaClinicaPacientedni != null) || (this.historiaClinicaPacientedni != null && !this.historiaClinicaPacientedni.equals(other.historiaClinicaPacientedni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.DetallehistoriaclinicaPK[ idDetalleHistoriaClinica=" + idDetalleHistoriaClinica + ", citaactoMedico=" + citaactoMedico + ", citaMedicocmp=" + citaMedicocmp + ", citaMedicodni=" + citaMedicodni + ", historiaClinicanumeroRegistro=" + historiaClinicanumeroRegistro + ", historiaClinicaautogenerado=" + historiaClinicaautogenerado + ", historiaClinicaPacientedni=" + historiaClinicaPacientedni + " ]";
    }
    
}
