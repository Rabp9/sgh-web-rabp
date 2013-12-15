/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author essalud
 */
@Entity
@Table(name = "vw_orden")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwOrden.findAll", query = "SELECT v FROM VwOrden v"),
    @NamedQuery(name = "VwOrden.findByNroOrden", query = "SELECT v FROM VwOrden v WHERE v.nroOrden = :nroOrden"),
    @NamedQuery(name = "VwOrden.findByFechaHora", query = "SELECT v FROM VwOrden v WHERE v.fechaHora = :fechaHora"),
    @NamedQuery(name = "VwOrden.findByEstado", query = "SELECT v FROM VwOrden v WHERE v.estado = :estado"),
    @NamedQuery(name = "VwOrden.findByDni", query = "SELECT v FROM VwOrden v WHERE v.dni = :dni"),
    @NamedQuery(name = "VwOrden.findByNumeroRegistro", query = "SELECT v FROM VwOrden v WHERE v.numeroRegistro = :numeroRegistro"),
    @NamedQuery(name = "VwOrden.findByPaciente", query = "SELECT v FROM VwOrden v WHERE v.paciente = :paciente"),
    @NamedQuery(name = "VwOrden.findByActoMedico", query = "SELECT v FROM VwOrden v WHERE v.actoMedico = :actoMedico"),
    @NamedQuery(name = "VwOrden.findByMedico", query = "SELECT v FROM VwOrden v WHERE v.medico = :medico"),
    @NamedQuery(name = "VwOrden.findByServicio", query = "SELECT v FROM VwOrden v WHERE v.servicio = :servicio"),
    @NamedQuery(name = "VwOrden.findByDniByFechaHora", query = "SELECT v FROM VwOrden v WHERE v.dni = :dni AND v.fechaHora BETWEEN :fechaInicio AND :fechaFin")})
public class VwOrden implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "nroOrden")
    @Id
    private String nroOrden;
    @Column(name = "fechaHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Column(name = "estado")
    private Character estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "dni")
    private String dni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "numeroRegistro")
    private String numeroRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "paciente")
    private String paciente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "actoMedico")
    private String actoMedico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "medico")
    private String medico;
    @Size(max = 60)
    @Column(name = "servicio")
    private String servicio;

    public VwOrden() {
    }

    public String getNroOrden() {
        return nroOrden;
    }

    public void setNroOrden(String nroOrden) {
        this.nroOrden = nroOrden;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getActoMedico() {
        return actoMedico;
    }

    public void setActoMedico(String actoMedico) {
        this.actoMedico = actoMedico;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
    
}
