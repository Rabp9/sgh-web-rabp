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
public class DetallereferenciadiagnosticoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idDetalleReferenciaDiagnostico")
    private int idDetalleReferenciaDiagnostico;
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
    @Size(min = 1, max = 5)
    @Column(name = "Diagnostico_codigo")
    private String diagnosticocodigo;

    public DetallereferenciadiagnosticoPK() {
    }

    public DetallereferenciadiagnosticoPK(int idDetalleReferenciaDiagnostico, String referencianumeroRegistro, String referenciaPacientedni, String diagnosticocodigo) {
        this.idDetalleReferenciaDiagnostico = idDetalleReferenciaDiagnostico;
        this.referencianumeroRegistro = referencianumeroRegistro;
        this.referenciaPacientedni = referenciaPacientedni;
        this.diagnosticocodigo = diagnosticocodigo;
    }

    public int getIdDetalleReferenciaDiagnostico() {
        return idDetalleReferenciaDiagnostico;
    }

    public void setIdDetalleReferenciaDiagnostico(int idDetalleReferenciaDiagnostico) {
        this.idDetalleReferenciaDiagnostico = idDetalleReferenciaDiagnostico;
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

    public String getDiagnosticocodigo() {
        return diagnosticocodigo;
    }

    public void setDiagnosticocodigo(String diagnosticocodigo) {
        this.diagnosticocodigo = diagnosticocodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDetalleReferenciaDiagnostico;
        hash += (referencianumeroRegistro != null ? referencianumeroRegistro.hashCode() : 0);
        hash += (referenciaPacientedni != null ? referenciaPacientedni.hashCode() : 0);
        hash += (diagnosticocodigo != null ? diagnosticocodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallereferenciadiagnosticoPK)) {
            return false;
        }
        DetallereferenciadiagnosticoPK other = (DetallereferenciadiagnosticoPK) object;
        if (this.idDetalleReferenciaDiagnostico != other.idDetalleReferenciaDiagnostico) {
            return false;
        }
        if ((this.referencianumeroRegistro == null && other.referencianumeroRegistro != null) || (this.referencianumeroRegistro != null && !this.referencianumeroRegistro.equals(other.referencianumeroRegistro))) {
            return false;
        }
        if ((this.referenciaPacientedni == null && other.referenciaPacientedni != null) || (this.referenciaPacientedni != null && !this.referenciaPacientedni.equals(other.referenciaPacientedni))) {
            return false;
        }
        if ((this.diagnosticocodigo == null && other.diagnosticocodigo != null) || (this.diagnosticocodigo != null && !this.diagnosticocodigo.equals(other.diagnosticocodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.DetallereferenciadiagnosticoPK[ idDetalleReferenciaDiagnostico=" + idDetalleReferenciaDiagnostico + ", referencianumeroRegistro=" + referencianumeroRegistro + ", referenciaPacientedni=" + referenciaPacientedni + ", diagnosticocodigo=" + diagnosticocodigo + " ]";
    }
    
}
