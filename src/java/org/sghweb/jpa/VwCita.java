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
@Table(name = "vw_cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwCita.findAll", query = "SELECT v FROM VwCita v"),
    @NamedQuery(name = "VwCita.findByActoMedico", query = "SELECT v FROM VwCita v WHERE v.actoMedico = :actoMedico"),
    @NamedQuery(name = "VwCita.findByCmp", query = "SELECT v FROM VwCita v WHERE v.cmp = :cmp"),
    @NamedQuery(name = "VwCita.findByMedicoDni", query = "SELECT v FROM VwCita v WHERE v.medicoDni = :medicoDni"),
    @NamedQuery(name = "VwCita.findByMedico", query = "SELECT v FROM VwCita v WHERE v.medico = :medico"),
    @NamedQuery(name = "VwCita.findByFechaHora", query = "SELECT v FROM VwCita v WHERE v.fechaHora = :fechaHora"),
    @NamedQuery(name = "VwCita.findDisponibles", query = "SELECT v FROM VwCita v WHERE v.fechaHora >= :fechaHora"),
    @NamedQuery(name = "VwCita.findDisponiblesbyCmp", query = "SELECT v FROM VwCita v WHERE v.fechaHora >= :fechaHora AND v.cmp = :cmp")})
public class VwCita implements Serializable {
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
    @Column(name = "fechaHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;

    public VwCita() {
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

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
    
}
