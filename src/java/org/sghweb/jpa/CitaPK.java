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
public class CitaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "actoMedico")
    private int actoMedico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "Paciente_dni")
    private String pacientedni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "Medico_cmp")
    private String medicocmp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "Medico_dni")
    private String medicodni;

    public CitaPK() {
    }

    public CitaPK(int actoMedico, String pacientedni, String medicocmp, String medicodni) {
        this.actoMedico = actoMedico;
        this.pacientedni = pacientedni;
        this.medicocmp = medicocmp;
        this.medicodni = medicodni;
    }

    public int getActoMedico() {
        return actoMedico;
    }

    public void setActoMedico(int actoMedico) {
        this.actoMedico = actoMedico;
    }

    public String getPacientedni() {
        return pacientedni;
    }

    public void setPacientedni(String pacientedni) {
        this.pacientedni = pacientedni;
    }

    public String getMedicocmp() {
        return medicocmp;
    }

    public void setMedicocmp(String medicocmp) {
        this.medicocmp = medicocmp;
    }

    public String getMedicodni() {
        return medicodni;
    }

    public void setMedicodni(String medicodni) {
        this.medicodni = medicodni;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) actoMedico;
        hash += (pacientedni != null ? pacientedni.hashCode() : 0);
        hash += (medicocmp != null ? medicocmp.hashCode() : 0);
        hash += (medicodni != null ? medicodni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitaPK)) {
            return false;
        }
        CitaPK other = (CitaPK) object;
        if (this.actoMedico != other.actoMedico) {
            return false;
        }
        if ((this.pacientedni == null && other.pacientedni != null) || (this.pacientedni != null && !this.pacientedni.equals(other.pacientedni))) {
            return false;
        }
        if ((this.medicocmp == null && other.medicocmp != null) || (this.medicocmp != null && !this.medicocmp.equals(other.medicocmp))) {
            return false;
        }
        if ((this.medicodni == null && other.medicodni != null) || (this.medicodni != null && !this.medicodni.equals(other.medicodni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.CitaPK[ actoMedico=" + actoMedico + ", pacientedni=" + pacientedni + ", medicocmp=" + medicocmp + ", medicodni=" + medicodni + " ]";
    }
    
}
