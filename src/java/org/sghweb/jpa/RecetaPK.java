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
public class RecetaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "nroReceta")
    private String nroReceta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "Medicamento_codigo")
    private String medicamentocodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "Orden_nroOrden")
    private String ordennroOrden;

    public RecetaPK() {
    }

    public RecetaPK(String nroReceta, String medicamentocodigo, String ordennroOrden) {
        this.nroReceta = nroReceta;
        this.medicamentocodigo = medicamentocodigo;
        this.ordennroOrden = ordennroOrden;
    }

    public String getNroReceta() {
        return nroReceta;
    }

    public void setNroReceta(String nroReceta) {
        this.nroReceta = nroReceta;
    }

    public String getMedicamentocodigo() {
        return medicamentocodigo;
    }

    public void setMedicamentocodigo(String medicamentocodigo) {
        this.medicamentocodigo = medicamentocodigo;
    }

    public String getOrdennroOrden() {
        return ordennroOrden;
    }

    public void setOrdennroOrden(String ordennroOrden) {
        this.ordennroOrden = ordennroOrden;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroReceta != null ? nroReceta.hashCode() : 0);
        hash += (medicamentocodigo != null ? medicamentocodigo.hashCode() : 0);
        hash += (ordennroOrden != null ? ordennroOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecetaPK)) {
            return false;
        }
        RecetaPK other = (RecetaPK) object;
        if ((this.nroReceta == null && other.nroReceta != null) || (this.nroReceta != null && !this.nroReceta.equals(other.nroReceta))) {
            return false;
        }
        if ((this.medicamentocodigo == null && other.medicamentocodigo != null) || (this.medicamentocodigo != null && !this.medicamentocodigo.equals(other.medicamentocodigo))) {
            return false;
        }
        if ((this.ordennroOrden == null && other.ordennroOrden != null) || (this.ordennroOrden != null && !this.ordennroOrden.equals(other.ordennroOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.RecetaPK[ nroReceta=" + nroReceta + ", medicamentocodigo=" + medicamentocodigo + ", ordennroOrden=" + ordennroOrden + " ]";
    }
    
}
