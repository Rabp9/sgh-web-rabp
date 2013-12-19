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
@Table(name = "vw_cita_atender")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwCitaAtender.findAll", query = "SELECT v FROM VwCitaAtender v"),
    @NamedQuery(name = "VwCitaAtender.findByActoMedico", query = "SELECT v FROM VwCitaAtender v WHERE v.actoMedico = :actoMedico"),
    @NamedQuery(name = "VwCitaAtender.findByCmp", query = "SELECT v FROM VwCitaAtender v WHERE v.cmp = :cmp"),
    @NamedQuery(name = "VwCitaAtender.findByMedicoDni", query = "SELECT v FROM VwCitaAtender v WHERE v.medicoDni = :medicoDni"),
    @NamedQuery(name = "VwCitaAtender.findByMedico", query = "SELECT v FROM VwCitaAtender v WHERE v.medico = :medico"),
    @NamedQuery(name = "VwCitaAtender.findByCodigoServicio", query = "SELECT v FROM VwCitaAtender v WHERE v.codigoServicio = :codigoServicio"),
    @NamedQuery(name = "VwCitaAtender.findByFechaHora", query = "SELECT v FROM VwCitaAtender v WHERE v.fechaHora = :fechaHora"),
    @NamedQuery(name = "VwCitaAtender.findByDniPaciente", query = "SELECT v FROM VwCitaAtender v WHERE v.dniPaciente = :dniPaciente"),
    @NamedQuery(name = "VwCitaAtender.findByCmpYFecha", query = "SELECT v FROM VwCitaAtender v WHERE v.cmp = :cmp AND v.fechaHora LIKE :fechaHora")})
public class VwCitaAtender implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "actoMedico")
    @Id
    private String actoMedico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cmp")
    private String cmp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "medicoDni")
    private String medicoDni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "medico")
    private String medico;
    @Size(max = 3)
    @Column(name = "codigoServicio")
    private String codigoServicio;
    @Column(name = "fechaHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "dniPaciente")
    private String dniPaciente;

    public VwCitaAtender() {
    }

    public String getActoMedico() {
        return actoMedico;
    }

    public void setActoMedico(String actoMedico) {
        this.actoMedico = actoMedico;
    }

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
    }

    public String getMedicoDni() {
        return medicoDni;
    }

    public void setMedicoDni(String medicoDni) {
        this.medicoDni = medicoDni;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(String codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }
    
}
