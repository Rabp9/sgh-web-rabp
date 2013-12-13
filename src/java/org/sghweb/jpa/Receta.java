/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author essalud
 */
@Entity
@Table(name = "receta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Receta.findAll", query = "SELECT r FROM Receta r"),
    @NamedQuery(name = "Receta.findByNroReceta", query = "SELECT r FROM Receta r WHERE r.recetaPK.nroReceta = :nroReceta"),
    @NamedQuery(name = "Receta.findByMedicamentocodigo", query = "SELECT r FROM Receta r WHERE r.recetaPK.medicamentocodigo = :medicamentocodigo"),
    @NamedQuery(name = "Receta.findByOrdennroOrden", query = "SELECT r FROM Receta r WHERE r.recetaPK.ordennroOrden = :ordennroOrden"),
    @NamedQuery(name = "Receta.findBySolicita", query = "SELECT r FROM Receta r WHERE r.solicita = :solicita"),
    @NamedQuery(name = "Receta.findByAtiende", query = "SELECT r FROM Receta r WHERE r.atiende = :atiende"),
    @NamedQuery(name = "Receta.findByPendiente", query = "SELECT r FROM Receta r WHERE r.pendiente = :pendiente"),
    @NamedQuery(name = "Receta.findByIndicacion", query = "SELECT r FROM Receta r WHERE r.indicacion = :indicacion"),
    @NamedQuery(name = "Receta.findByEstado", query = "SELECT r FROM Receta r WHERE r.estado = :estado")})
public class Receta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecetaPK recetaPK;
    @Column(name = "solicita")
    private Short solicita;
    @Column(name = "atiende")
    private Short atiende;
    @Column(name = "pendiente")
    private Short pendiente;
    @Size(max = 60)
    @Column(name = "indicacion")
    private String indicacion;
    @Column(name = "estado")
    private Character estado;
    @JoinColumn(name = "Orden_nroOrden", referencedColumnName = "nroOrden", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orden orden;
    @JoinColumn(name = "Medicamento_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Medicamento medicamento;

    public Receta() {
    }

    public Receta(RecetaPK recetaPK) {
        this.recetaPK = recetaPK;
    }

    public Receta(String nroReceta, String medicamentocodigo, String ordennroOrden) {
        this.recetaPK = new RecetaPK(nroReceta, medicamentocodigo, ordennroOrden);
    }

    public RecetaPK getRecetaPK() {
        return recetaPK;
    }

    public void setRecetaPK(RecetaPK recetaPK) {
        this.recetaPK = recetaPK;
    }

    public Short getSolicita() {
        return solicita;
    }

    public void setSolicita(Short solicita) {
        this.solicita = solicita;
    }

    public Short getAtiende() {
        return atiende;
    }

    public void setAtiende(Short atiende) {
        this.atiende = atiende;
    }

    public Short getPendiente() {
        return pendiente;
    }

    public void setPendiente(Short pendiente) {
        this.pendiente = pendiente;
    }

    public String getIndicacion() {
        return indicacion;
    }

    public void setIndicacion(String indicacion) {
        this.indicacion = indicacion;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recetaPK != null ? recetaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Receta)) {
            return false;
        }
        Receta other = (Receta) object;
        if ((this.recetaPK == null && other.recetaPK != null) || (this.recetaPK != null && !this.recetaPK.equals(other.recetaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Receta[ recetaPK=" + recetaPK + " ]";
    }
    
}
