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
public class HistoriaclinicaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "numeroRegistro")
    private String numeroRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "autogenerado")
    private String autogenerado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "dni")
    private String dni;

    public HistoriaclinicaPK() {
    }

    public HistoriaclinicaPK(String numeroRegistro, String autogenerado, String dni) {
        this.numeroRegistro = numeroRegistro;
        this.autogenerado = autogenerado;
        this.dni = dni;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getAutogenerado() {
        return autogenerado;
    }

    public void setAutogenerado(String autogenerado) {
        this.autogenerado = autogenerado;
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
        hash += (autogenerado != null ? autogenerado.hashCode() : 0);
        hash += (dni != null ? dni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoriaclinicaPK)) {
            return false;
        }
        HistoriaclinicaPK other = (HistoriaclinicaPK) object;
        if ((this.numeroRegistro == null && other.numeroRegistro != null) || (this.numeroRegistro != null && !this.numeroRegistro.equals(other.numeroRegistro))) {
            return false;
        }
        if ((this.autogenerado == null && other.autogenerado != null) || (this.autogenerado != null && !this.autogenerado.equals(other.autogenerado))) {
            return false;
        }
        if ((this.dni == null && other.dni != null) || (this.dni != null && !this.dni.equals(other.dni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.HistoriaclinicaPK[ numeroRegistro=" + numeroRegistro + ", autogenerado=" + autogenerado + ", dni=" + dni + " ]";
    }
    
}
