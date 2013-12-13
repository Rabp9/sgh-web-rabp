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
    @Size(min = 1, max = 7)
    @Column(name = "actoMedico")
    private String actoMedico;
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

    public CitaPK(String actoMedico, String medicocmp, String medicodni) {
        this.actoMedico = actoMedico;
        this.medicocmp = medicocmp;
        this.medicodni = medicodni;
    }

    public String getActoMedico() {
        return actoMedico;
    }

    public void setActoMedico(String actoMedico) {
        this.actoMedico = actoMedico;
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
        hash += (actoMedico != null ? actoMedico.hashCode() : 0);
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
        if ((this.actoMedico == null && other.actoMedico != null) || (this.actoMedico != null && !this.actoMedico.equals(other.actoMedico))) {
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
        return "org.sghweb.jpa.CitaPK[ actoMedico=" + actoMedico + ", medicocmp=" + medicocmp + ", medicodni=" + medicodni + " ]";
    }
    
}
