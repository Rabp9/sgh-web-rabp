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
 * @author Roberto
 */
@Entity
@Table(name = "vw_reportepaciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwReportepaciente.findAll", query = "SELECT v FROM VwReportepaciente v"),
    @NamedQuery(name = "VwReportepaciente.findByDni", query = "SELECT v FROM VwReportepaciente v WHERE v.dni = :dni"),
    @NamedQuery(name = "VwReportepaciente.findByNombreCompleto", query = "SELECT v FROM VwReportepaciente v WHERE v.nombreCompleto = :nombreCompleto"),
    @NamedQuery(name = "VwReportepaciente.findByUbicacionNacimiento", query = "SELECT v FROM VwReportepaciente v WHERE v.ubicacionNacimiento = :ubicacionNacimiento"),
    @NamedQuery(name = "VwReportepaciente.findByUbicacionActual", query = "SELECT v FROM VwReportepaciente v WHERE v.ubicacionActual = :ubicacionActual"),
    @NamedQuery(name = "VwReportepaciente.findByDireccion", query = "SELECT v FROM VwReportepaciente v WHERE v.direccion = :direccion"),
    @NamedQuery(name = "VwReportepaciente.findByGenero", query = "SELECT v FROM VwReportepaciente v WHERE v.genero = :genero"),
    @NamedQuery(name = "VwReportepaciente.findByFechaNacimiento", query = "SELECT v FROM VwReportepaciente v WHERE v.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "VwReportepaciente.findByTipoAsegurado", query = "SELECT v FROM VwReportepaciente v WHERE v.tipoAsegurado = :tipoAsegurado"),
    @NamedQuery(name = "VwReportepaciente.findByEstadoCivil", query = "SELECT v FROM VwReportepaciente v WHERE v.estadoCivil = :estadoCivil"),
    @NamedQuery(name = "VwReportepaciente.findByTitular", query = "SELECT v FROM VwReportepaciente v WHERE v.titular = :titular"),
    @NamedQuery(name = "VwReportepaciente.findByNumeroRegistro", query = "SELECT v FROM VwReportepaciente v WHERE v.numeroRegistro = :numeroRegistro"),
    @NamedQuery(name = "VwReportepaciente.findByAutogenerado", query = "SELECT v FROM VwReportepaciente v WHERE v.autogenerado = :autogenerado"),
    @NamedQuery(name = "VwReportepaciente.findByFechaInicio", query = "SELECT v FROM VwReportepaciente v WHERE v.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "VwReportepaciente.findByFechaUltimaCita", query = "SELECT v FROM VwReportepaciente v WHERE v.fechaUltimaCita = :fechaUltimaCita"),
    @NamedQuery(name = "VwReportepaciente.findByNumCitasUltimoMes", query = "SELECT v FROM VwReportepaciente v WHERE v.numCitasUltimoMes = :numCitasUltimoMes"),
    @NamedQuery(name = "VwReportepaciente.findByNumCitasTotal", query = "SELECT v FROM VwReportepaciente v WHERE v.numCitasTotal = :numCitasTotal")})
public class VwReportepaciente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "dni")
    @Id
    private String dni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nombreCompleto")
    private String nombreCompleto;
    @Size(max = 45)
    @Column(name = "ubicacionNacimiento")
    private String ubicacionNacimiento;
    @Size(max = 45)
    @Column(name = "ubicacionActual")
    private String ubicacionActual;
    @Size(max = 100)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 9)
    @Column(name = "genero")
    private String genero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaNacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Size(max = 45)
    @Column(name = "tipoAsegurado")
    private String tipoAsegurado;
    @Size(max = 7)
    @Column(name = "estadoCivil")
    private String estadoCivil;
    @Size(max = 15)
    @Column(name = "titular")
    private String titular;
    @Size(max = 7)
    @Column(name = "numeroRegistro")
    private String numeroRegistro;
    @Size(max = 15)
    @Column(name = "autogenerado")
    private String autogenerado;
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fechaUltimaCita")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaCita;
    @Column(name = "numCitasUltimoMes")
    private Integer numCitasUltimoMes;
    @Column(name = "numCitasTotal")
    private Integer numCitasTotal;

    public VwReportepaciente() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUbicacionNacimiento() {
        return ubicacionNacimiento;
    }

    public void setUbicacionNacimiento(String ubicacionNacimiento) {
        this.ubicacionNacimiento = ubicacionNacimiento;
    }

    public String getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipoAsegurado() {
        return tipoAsegurado;
    }

    public void setTipoAsegurado(String tipoAsegurado) {
        this.tipoAsegurado = tipoAsegurado;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaUltimaCita() {
        return fechaUltimaCita;
    }

    public void setFechaUltimaCita(Date fechaUltimaCita) {
        this.fechaUltimaCita = fechaUltimaCita;
    }

    public Integer getNumCitasUltimoMes() {
        return numCitasUltimoMes;
    }

    public void setNumCitasUltimoMes(Integer numCitasUltimoMes) {
        this.numCitasUltimoMes = numCitasUltimoMes;
    }

    public Integer getNumCitasTotal() {
        return numCitasTotal;
    }

    public void setNumCitasTotal(Integer numCitasTotal) {
        this.numCitasTotal = numCitasTotal;
    }
    
}
