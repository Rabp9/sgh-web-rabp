/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "detallereferenciaservicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallereferenciaservicio.findAll", query = "SELECT d FROM Detallereferenciaservicio d"),
    @NamedQuery(name = "Detallereferenciaservicio.findByIdDetalleReferenciaServicio", query = "SELECT d FROM Detallereferenciaservicio d WHERE d.detallereferenciaservicioPK.idDetalleReferenciaServicio = :idDetalleReferenciaServicio"),
    @NamedQuery(name = "Detallereferenciaservicio.findByReferencianumeroRegistro", query = "SELECT d FROM Detallereferenciaservicio d WHERE d.detallereferenciaservicioPK.referencianumeroRegistro = :referencianumeroRegistro"),
    @NamedQuery(name = "Detallereferenciaservicio.findByReferenciaPacientedni", query = "SELECT d FROM Detallereferenciaservicio d WHERE d.detallereferenciaservicioPK.referenciaPacientedni = :referenciaPacientedni"),
    @NamedQuery(name = "Detallereferenciaservicio.findByServiciocodigo", query = "SELECT d FROM Detallereferenciaservicio d WHERE d.detallereferenciaservicioPK.serviciocodigo = :serviciocodigo"),
    @NamedQuery(name = "Detallereferenciaservicio.findByCondicion", query = "SELECT d FROM Detallereferenciaservicio d WHERE d.condicion = :condicion")})
public class Detallereferenciaservicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallereferenciaservicioPK detallereferenciaservicioPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "condicion")
    private String condicion;
    @JoinColumn(name = "Servicio_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Servicio servicio;
    @JoinColumns({
        @JoinColumn(name = "Referencia_numeroRegistro", referencedColumnName = "numeroRegistro", insertable = false, updatable = false),
        @JoinColumn(name = "Referencia_Paciente_dni", referencedColumnName = "Paciente_dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Referencia referencia;

    public Detallereferenciaservicio() {
    }

    public Detallereferenciaservicio(DetallereferenciaservicioPK detallereferenciaservicioPK) {
        this.detallereferenciaservicioPK = detallereferenciaservicioPK;
    }

    public Detallereferenciaservicio(DetallereferenciaservicioPK detallereferenciaservicioPK, String condicion) {
        this.detallereferenciaservicioPK = detallereferenciaservicioPK;
        this.condicion = condicion;
    }

    public Detallereferenciaservicio(int idDetalleReferenciaServicio, String referencianumeroRegistro, String referenciaPacientedni, String serviciocodigo) {
        this.detallereferenciaservicioPK = new DetallereferenciaservicioPK(idDetalleReferenciaServicio, referencianumeroRegistro, referenciaPacientedni, serviciocodigo);
    }

    public DetallereferenciaservicioPK getDetallereferenciaservicioPK() {
        return detallereferenciaservicioPK;
    }

    public void setDetallereferenciaservicioPK(DetallereferenciaservicioPK detallereferenciaservicioPK) {
        this.detallereferenciaservicioPK = detallereferenciaservicioPK;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Referencia getReferencia() {
        return referencia;
    }

    public void setReferencia(Referencia referencia) {
        this.referencia = referencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallereferenciaservicioPK != null ? detallereferenciaservicioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallereferenciaservicio)) {
            return false;
        }
        Detallereferenciaservicio other = (Detallereferenciaservicio) object;
        if ((this.detallereferenciaservicioPK == null && other.detallereferenciaservicioPK != null) || (this.detallereferenciaservicioPK != null && !this.detallereferenciaservicioPK.equals(other.detallereferenciaservicioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Detallereferenciaservicio[ detallereferenciaservicioPK=" + detallereferenciaservicioPK + " ]";
    }
    
}
