/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "citt")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Citt.findAll", query = "SELECT c FROM Citt c"),
    @NamedQuery(name = "Citt.findByNumeroCitt", query = "SELECT c FROM Citt c WHERE c.cittPK.numeroCitt = :numeroCitt"),
    @NamedQuery(name = "Citt.findByDetalleHistoriaClinicaidDetalleHistoriaClinica", query = "SELECT c FROM Citt c WHERE c.cittPK.detalleHistoriaClinicaidDetalleHistoriaClinica = :detalleHistoriaClinicaidDetalleHistoriaClinica"),
    @NamedQuery(name = "Citt.findByDetalleHistoriaClinicaCitaactoMedico", query = "SELECT c FROM Citt c WHERE c.cittPK.detalleHistoriaClinicaCitaactoMedico = :detalleHistoriaClinicaCitaactoMedico"),
    @NamedQuery(name = "Citt.findByDetalleHistoriaClinicaCitaMedicocmp", query = "SELECT c FROM Citt c WHERE c.cittPK.detalleHistoriaClinicaCitaMedicocmp = :detalleHistoriaClinicaCitaMedicocmp"),
    @NamedQuery(name = "Citt.findByDetalleHistoriaClinicaCitaMedicodni", query = "SELECT c FROM Citt c WHERE c.cittPK.detalleHistoriaClinicaCitaMedicodni = :detalleHistoriaClinicaCitaMedicodni"),
    @NamedQuery(name = "Citt.findByDetalleHistoriaClinicaHistoriaClinicanumeroRegistro", query = "SELECT c FROM Citt c WHERE c.cittPK.detalleHistoriaClinicaHistoriaClinicanumeroRegistro = :detalleHistoriaClinicaHistoriaClinicanumeroRegistro"),
    @NamedQuery(name = "Citt.findByDetalleHistoriaClinicaHistoriaClinicaautogenerado", query = "SELECT c FROM Citt c WHERE c.cittPK.detalleHistoriaClinicaHistoriaClinicaautogenerado = :detalleHistoriaClinicaHistoriaClinicaautogenerado"),
    @NamedQuery(name = "Citt.findByDetalleHistoriaClinicaHistoriaClinicaPacientedni", query = "SELECT c FROM Citt c WHERE c.cittPK.detalleHistoriaClinicaHistoriaClinicaPacientedni = :detalleHistoriaClinicaHistoriaClinicaPacientedni"),
    @NamedQuery(name = "Citt.findByFechaInicio", query = "SELECT c FROM Citt c WHERE c.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Citt.findByFechaTermino", query = "SELECT c FROM Citt c WHERE c.fechaTermino = :fechaTermino"),
    @NamedQuery(name = "Citt.findByEstado", query = "SELECT c FROM Citt c WHERE c.estado = :estado")})
public class Citt implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CittPK cittPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaTermino")
    @Temporal(TemporalType.DATE)
    private Date fechaTermino;
    @Column(name = "estado")
    private Character estado;
    @JoinColumns({
        @JoinColumn(name = "DetalleHistoriaClinica_idDetalleHistoriaClinica", referencedColumnName = "idDetalleHistoriaClinica", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_Cita_actoMedico", referencedColumnName = "Cita_actoMedico", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_Cita_Medico_cmp", referencedColumnName = "Cita_Medico_cmp", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_Cita_Medico_dni", referencedColumnName = "Cita_Medico_dni", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_HistoriaClinica_numeroRegistro", referencedColumnName = "HistoriaClinica_numeroRegistro", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_HistoriaClinica_autogenerado", referencedColumnName = "HistoriaClinica_autogenerado", insertable = false, updatable = false),
        @JoinColumn(name = "DetalleHistoriaClinica_HistoriaClinica_Paciente_dni", referencedColumnName = "HistoriaClinica_Paciente_dni", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Detallehistoriaclinica detallehistoriaclinica;

    public Citt() {
    }

    public Citt(CittPK cittPK) {
        this.cittPK = cittPK;
    }

    public Citt(CittPK cittPK, Date fechaInicio, Date fechaTermino) {
        this.cittPK = cittPK;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
    }

    public Citt(String numeroCitt, int detalleHistoriaClinicaidDetalleHistoriaClinica, String detalleHistoriaClinicaCitaactoMedico, String detalleHistoriaClinicaCitaMedicocmp, String detalleHistoriaClinicaCitaMedicodni, String detalleHistoriaClinicaHistoriaClinicanumeroRegistro, String detalleHistoriaClinicaHistoriaClinicaautogenerado, String detalleHistoriaClinicaHistoriaClinicaPacientedni) {
        this.cittPK = new CittPK(numeroCitt, detalleHistoriaClinicaidDetalleHistoriaClinica, detalleHistoriaClinicaCitaactoMedico, detalleHistoriaClinicaCitaMedicocmp, detalleHistoriaClinicaCitaMedicodni, detalleHistoriaClinicaHistoriaClinicanumeroRegistro, detalleHistoriaClinicaHistoriaClinicaautogenerado, detalleHistoriaClinicaHistoriaClinicaPacientedni);
    }

    public CittPK getCittPK() {
        return cittPK;
    }

    public void setCittPK(CittPK cittPK) {
        this.cittPK = cittPK;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Detallehistoriaclinica getDetallehistoriaclinica() {
        return detallehistoriaclinica;
    }

    public void setDetallehistoriaclinica(Detallehistoriaclinica detallehistoriaclinica) {
        this.detallehistoriaclinica = detallehistoriaclinica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cittPK != null ? cittPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Citt)) {
            return false;
        }
        Citt other = (Citt) object;
        if ((this.cittPK == null && other.cittPK != null) || (this.cittPK != null && !this.cittPK.equals(other.cittPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Citt[ cittPK=" + cittPK + " ]";
    }
    
}
