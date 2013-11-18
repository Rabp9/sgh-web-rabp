/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @NamedQuery(name = "Cita.findByActoMedico", query = "SELECT c FROM Cita c WHERE c.citaPK.actoMedico = :actoMedico"),
    @NamedQuery(name = "Cita.findByMedicocmp", query = "SELECT c FROM Cita c WHERE c.citaPK.medicocmp = :medicocmp"),
    @NamedQuery(name = "Cita.findByMedicodni", query = "SELECT c FROM Cita c WHERE c.citaPK.medicodni = :medicodni"),
    @NamedQuery(name = "Cita.findByFecha", query = "SELECT c FROM Cita c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Cita.findByEstado", query = "SELECT c FROM Cita c WHERE c.estado = :estado")})
public class Cita implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CitaPK citaPK;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "estado")
    private Character estado;
    @JoinColumns({
        @JoinColumn(name = "Medico_cmp", referencedColumnName = "cmp", insertable = false, updatable = false),
        @JoinColumn(name = "Medico_dni", referencedColumnName = "dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Medico medico;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cita")
    private List<Detallehistoriaclinica> detallehistoriaclinicaList;

    public Cita() {
    }

    public Cita(CitaPK citaPK) {
        this.citaPK = citaPK;
    }

    public Cita(String actoMedico, String medicocmp, String medicodni) {
        this.citaPK = new CitaPK(actoMedico, medicocmp, medicodni);
    }

    public CitaPK getCitaPK() {
        return citaPK;
    }

    public void setCitaPK(CitaPK citaPK) {
        this.citaPK = citaPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @XmlTransient
    public List<Detallehistoriaclinica> getDetallehistoriaclinicaList() {
        return detallehistoriaclinicaList;
    }

    public void setDetallehistoriaclinicaList(List<Detallehistoriaclinica> detallehistoriaclinicaList) {
        this.detallehistoriaclinicaList = detallehistoriaclinicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (citaPK != null ? citaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cita)) {
            return false;
        }
        Cita other = (Cita) object;
        if ((this.citaPK == null && other.citaPK != null) || (this.citaPK != null && !this.citaPK.equals(other.citaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Cita[ citaPK=" + citaPK + " ]";
    }
    
}
