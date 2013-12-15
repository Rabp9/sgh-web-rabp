/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "detallereferenciadiagnostico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallereferenciadiagnostico.findAll", query = "SELECT d FROM Detallereferenciadiagnostico d"),
    @NamedQuery(name = "Detallereferenciadiagnostico.findByIdDetalleReferenciaDiagnostico", query = "SELECT d FROM Detallereferenciadiagnostico d WHERE d.detallereferenciadiagnosticoPK.idDetalleReferenciaDiagnostico = :idDetalleReferenciaDiagnostico"),
    @NamedQuery(name = "Detallereferenciadiagnostico.findByReferencianumeroRegistro", query = "SELECT d FROM Detallereferenciadiagnostico d WHERE d.detallereferenciadiagnosticoPK.referencianumeroRegistro = :referencianumeroRegistro"),
    @NamedQuery(name = "Detallereferenciadiagnostico.findByReferenciaPacientedni", query = "SELECT d FROM Detallereferenciadiagnostico d WHERE d.detallereferenciadiagnosticoPK.referenciaPacientedni = :referenciaPacientedni"),
    @NamedQuery(name = "Detallereferenciadiagnostico.findByDiagnosticocodigo", query = "SELECT d FROM Detallereferenciadiagnostico d WHERE d.detallereferenciadiagnosticoPK.diagnosticocodigo = :diagnosticocodigo")})
public class Detallereferenciadiagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallereferenciadiagnosticoPK detallereferenciadiagnosticoPK;
    @JoinColumn(name = "Diagnostico_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Diagnostico diagnostico;
    @JoinColumns({
        @JoinColumn(name = "Referencia_numeroRegistro", referencedColumnName = "numeroRegistro", insertable = false, updatable = false),
        @JoinColumn(name = "Referencia_Paciente_dni", referencedColumnName = "Paciente_dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Referencia referencia;

    public Detallereferenciadiagnostico() {
    }

    public Detallereferenciadiagnostico(DetallereferenciadiagnosticoPK detallereferenciadiagnosticoPK) {
        this.detallereferenciadiagnosticoPK = detallereferenciadiagnosticoPK;
    }

    public Detallereferenciadiagnostico(int idDetalleReferenciaDiagnostico, String referencianumeroRegistro, String referenciaPacientedni, String diagnosticocodigo) {
        this.detallereferenciadiagnosticoPK = new DetallereferenciadiagnosticoPK(idDetalleReferenciaDiagnostico, referencianumeroRegistro, referenciaPacientedni, diagnosticocodigo);
    }

    public DetallereferenciadiagnosticoPK getDetallereferenciadiagnosticoPK() {
        return detallereferenciadiagnosticoPK;
    }

    public void setDetallereferenciadiagnosticoPK(DetallereferenciadiagnosticoPK detallereferenciadiagnosticoPK) {
        this.detallereferenciadiagnosticoPK = detallereferenciadiagnosticoPK;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
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
        hash += (detallereferenciadiagnosticoPK != null ? detallereferenciadiagnosticoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallereferenciadiagnostico)) {
            return false;
        }
        Detallereferenciadiagnostico other = (Detallereferenciadiagnostico) object;
        if ((this.detallereferenciadiagnosticoPK == null && other.detallereferenciadiagnosticoPK != null) || (this.detallereferenciadiagnosticoPK != null && !this.detallereferenciadiagnosticoPK.equals(other.detallereferenciadiagnosticoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Detallereferenciadiagnostico[ detallereferenciadiagnosticoPK=" + detallereferenciadiagnosticoPK + " ]";
    }
    
}
