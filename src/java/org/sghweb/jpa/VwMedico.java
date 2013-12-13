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
 * @author essalud
 */
@Entity
@Table(name = "vw_medico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwMedico.findAll", query = "SELECT v FROM VwMedico v"),
    @NamedQuery(name = "VwMedico.findByCmp", query = "SELECT v FROM VwMedico v WHERE v.cmp = :cmp"),
    @NamedQuery(name = "VwMedico.findByDni", query = "SELECT v FROM VwMedico v WHERE v.dni = :dni"),
    @NamedQuery(name = "VwMedico.findByNombreCompleto", query = "SELECT v FROM VwMedico v WHERE v.nombreCompleto = :nombreCompleto"),
    @NamedQuery(name = "VwMedico.findByTelefono", query = "SELECT v FROM VwMedico v WHERE v.telefono = :telefono"),
    @NamedQuery(name = "VwMedico.findByEstado", query = "SELECT v FROM VwMedico v WHERE v.estado = :estado"),
    @NamedQuery(name = "VwMedico.findByServicio", query = "SELECT v FROM VwMedico v WHERE v.servicio = :servicio")})
public class VwMedico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cmp")
    @Id
    private String cmp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "dni")
    private String dni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nombreCompleto")
    private String nombreCompleto;
    @Size(max = 10)
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "estado")
    private Character estado;
    @Size(max = 60)
    @Column(name = "servicio")
    private String servicio;

    public VwMedico() {
    }

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
    
}
