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
 * @author Roberto
 */
@Embeddable
public class DetalleserviciomedicoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idDetalleServicioMedico")
    private int idDetalleServicioMedico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "Servicio_codigo")
    private String serviciocodigo;
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

    public DetalleserviciomedicoPK() {
    }

    public DetalleserviciomedicoPK(int idDetalleServicioMedico, String serviciocodigo, String medicocmp, String medicodni) {
        this.idDetalleServicioMedico = idDetalleServicioMedico;
        this.serviciocodigo = serviciocodigo;
        this.medicocmp = medicocmp;
        this.medicodni = medicodni;
    }

    public int getIdDetalleServicioMedico() {
        return idDetalleServicioMedico;
    }

    public void setIdDetalleServicioMedico(int idDetalleServicioMedico) {
        this.idDetalleServicioMedico = idDetalleServicioMedico;
    }

    public String getServiciocodigo() {
        return serviciocodigo;
    }

    public void setServiciocodigo(String serviciocodigo) {
        this.serviciocodigo = serviciocodigo;
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
        hash += (int) idDetalleServicioMedico;
        hash += (serviciocodigo != null ? serviciocodigo.hashCode() : 0);
        hash += (medicocmp != null ? medicocmp.hashCode() : 0);
        hash += (medicodni != null ? medicodni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleserviciomedicoPK)) {
            return false;
        }
        DetalleserviciomedicoPK other = (DetalleserviciomedicoPK) object;
        if (this.idDetalleServicioMedico != other.idDetalleServicioMedico) {
            return false;
        }
        if ((this.serviciocodigo == null && other.serviciocodigo != null) || (this.serviciocodigo != null && !this.serviciocodigo.equals(other.serviciocodigo))) {
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
        return "org.sghweb.jpa.DetalleserviciomedicoPK[ idDetalleServicioMedico=" + idDetalleServicioMedico + ", serviciocodigo=" + serviciocodigo + ", medicocmp=" + medicocmp + ", medicodni=" + medicodni + " ]";
    }
    
}
