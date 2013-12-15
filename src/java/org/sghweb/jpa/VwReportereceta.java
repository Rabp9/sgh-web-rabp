/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "vw_reportereceta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwReportereceta.findAll", query = "SELECT v FROM VwReportereceta v"),
    @NamedQuery(name = "VwReportereceta.findByNroReceta", query = "SELECT v FROM VwReportereceta v WHERE v.nroReceta = :nroReceta"),
    @NamedQuery(name = "VwReportereceta.findByNroOrden", query = "SELECT v FROM VwReportereceta v WHERE v.nroOrden = :nroOrden"),
    @NamedQuery(name = "VwReportereceta.findByMedicamento", query = "SELECT v FROM VwReportereceta v WHERE v.medicamento = :medicamento"),
    @NamedQuery(name = "VwReportereceta.findByPresentacion", query = "SELECT v FROM VwReportereceta v WHERE v.presentacion = :presentacion"),
    @NamedQuery(name = "VwReportereceta.findBySolicita", query = "SELECT v FROM VwReportereceta v WHERE v.solicita = :solicita"),
    @NamedQuery(name = "VwReportereceta.findByAtendido", query = "SELECT v FROM VwReportereceta v WHERE v.atendido = :atendido"),
    @NamedQuery(name = "VwReportereceta.findByPendiente", query = "SELECT v FROM VwReportereceta v WHERE v.pendiente = :pendiente"),
    @NamedQuery(name = "VwReportereceta.findByIndicacion", query = "SELECT v FROM VwReportereceta v WHERE v.indicacion = :indicacion")})
public class VwReportereceta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "NroReceta")
    @Id
    private String nroReceta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "NroOrden")
    private String nroOrden;
    @Size(max = 60)
    @Column(name = "Medicamento")
    private String medicamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "Presentacion")
    private String presentacion;
    @Column(name = "Solicita")
    private Short solicita;
    @Column(name = "Atendido")
    private Short atendido;
    @Column(name = "Pendiente")
    private Short pendiente;
    @Size(max = 60)
    @Column(name = "Indicacion")
    private String indicacion;

    public VwReportereceta() {
    }

    public String getNroReceta() {
        return nroReceta;
    }

    public void setNroReceta(String nroReceta) {
        this.nroReceta = nroReceta;
    }

    public String getNroOrden() {
        return nroOrden;
    }

    public void setNroOrden(String nroOrden) {
        this.nroOrden = nroOrden;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Short getSolicita() {
        return solicita;
    }

    public void setSolicita(Short solicita) {
        this.solicita = solicita;
    }

    public Short getAtendido() {
        return atendido;
    }

    public void setAtendido(Short atendido) {
        this.atendido = atendido;
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
    
}
