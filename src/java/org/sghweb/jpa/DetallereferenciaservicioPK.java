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
public class DetallereferenciaservicioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idDetalleReferenciaServicio")
    private int idDetalleReferenciaServicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "Referencia_numeroRegistro")
    private String referencianumeroRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "Referencia_Paciente_dni")
    private String referenciaPacientedni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "Servicio_codigo")
    private String serviciocodigo;

    public DetallereferenciaservicioPK() {
    }

    public DetallereferenciaservicioPK(int idDetalleReferenciaServicio, String referencianumeroRegistro, String referenciaPacientedni, String serviciocodigo) {
        this.idDetalleReferenciaServicio = idDetalleReferenciaServicio;
        this.referencianumeroRegistro = referencianumeroRegistro;
        this.referenciaPacientedni = referenciaPacientedni;
        this.serviciocodigo = serviciocodigo;
    }

    public int getIdDetalleReferenciaServicio() {
        return idDetalleReferenciaServicio;
    }

    public void setIdDetalleReferenciaServicio(int idDetalleReferenciaServicio) {
        this.idDetalleReferenciaServicio = idDetalleReferenciaServicio;
    }

    public String getReferencianumeroRegistro() {
        return referencianumeroRegistro;
    }

    public void setReferencianumeroRegistro(String referencianumeroRegistro) {
        this.referencianumeroRegistro = referencianumeroRegistro;
    }

    public String getReferenciaPacientedni() {
        return referenciaPacientedni;
    }

    public void setReferenciaPacientedni(String referenciaPacientedni) {
        this.referenciaPacientedni = referenciaPacientedni;
    }

    public String getServiciocodigo() {
        return serviciocodigo;
    }

    public void setServiciocodigo(String serviciocodigo) {
        this.serviciocodigo = serviciocodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDetalleReferenciaServicio;
        hash += (referencianumeroRegistro != null ? referencianumeroRegistro.hashCode() : 0);
        hash += (referenciaPacientedni != null ? referenciaPacientedni.hashCode() : 0);
        hash += (serviciocodigo != null ? serviciocodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallereferenciaservicioPK)) {
            return false;
        }
        DetallereferenciaservicioPK other = (DetallereferenciaservicioPK) object;
        if (this.idDetalleReferenciaServicio != other.idDetalleReferenciaServicio) {
            return false;
        }
        if ((this.referencianumeroRegistro == null && other.referencianumeroRegistro != null) || (this.referencianumeroRegistro != null && !this.referencianumeroRegistro.equals(other.referencianumeroRegistro))) {
            return false;
        }
        if ((this.referenciaPacientedni == null && other.referenciaPacientedni != null) || (this.referenciaPacientedni != null && !this.referenciaPacientedni.equals(other.referenciaPacientedni))) {
            return false;
        }
        if ((this.serviciocodigo == null && other.serviciocodigo != null) || (this.serviciocodigo != null && !this.serviciocodigo.equals(other.serviciocodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.DetallereferenciaservicioPK[ idDetalleReferenciaServicio=" + idDetalleReferenciaServicio + ", referencianumeroRegistro=" + referencianumeroRegistro + ", referenciaPacientedni=" + referenciaPacientedni + ", serviciocodigo=" + serviciocodigo + " ]";
    }
    
}
