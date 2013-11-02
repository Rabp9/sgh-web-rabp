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
public class ReferenciaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "numeroRegistro")
    private String numeroRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "dni")
    private String dni;

    public ReferenciaPK() {
    }

    public ReferenciaPK(String numeroRegistro, String dni) {
        this.numeroRegistro = numeroRegistro;
        this.dni = dni;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroRegistro != null ? numeroRegistro.hashCode() : 0);
        hash += (dni != null ? dni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReferenciaPK)) {
            return false;
        }
        ReferenciaPK other = (ReferenciaPK) object;
        if ((this.numeroRegistro == null && other.numeroRegistro != null) || (this.numeroRegistro != null && !this.numeroRegistro.equals(other.numeroRegistro))) {
            return false;
        }
        if ((this.dni == null && other.dni != null) || (this.dni != null && !this.dni.equals(other.dni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.ReferenciaPK[ numeroRegistro=" + numeroRegistro + ", dni=" + dni + " ]";
    }
    
}
