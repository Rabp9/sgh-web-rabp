/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Roberto
 */
@Embeddable
public class TurnoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idTurno")
    private int idTurno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "Actividad_codigo")
    private String actividadcodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DetalleServicioMedico_idDetalleServicioMedico")
    private int detalleServicioMedicoidDetalleServicioMedico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "DetalleServicioMedico_Servicio_codigo")
    private String detalleServicioMedicoServiciocodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "DetalleServicioMedico_Medico_cmp")
    private String detalleServicioMedicoMedicocmp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "DetalleServicioMedico_Medico_dni")
    private String detalleServicioMedicoMedicodni;

    public TurnoPK() {
    }

    public TurnoPK(int idTurno, String actividadcodigo, int detalleServicioMedicoidDetalleServicioMedico, String detalleServicioMedicoServiciocodigo, String detalleServicioMedicoMedicocmp, String detalleServicioMedicoMedicodni) {
        this.idTurno = idTurno;
        this.actividadcodigo = actividadcodigo;
        this.detalleServicioMedicoidDetalleServicioMedico = detalleServicioMedicoidDetalleServicioMedico;
        this.detalleServicioMedicoServiciocodigo = detalleServicioMedicoServiciocodigo;
        this.detalleServicioMedicoMedicocmp = detalleServicioMedicoMedicocmp;
        this.detalleServicioMedicoMedicodni = detalleServicioMedicoMedicodni;
    }

    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public String getActividadcodigo() {
        return actividadcodigo;
    }

    public void setActividadcodigo(String actividadcodigo) {
        this.actividadcodigo = actividadcodigo;
    }

    public int getDetalleServicioMedicoidDetalleServicioMedico() {
        return detalleServicioMedicoidDetalleServicioMedico;
    }

    public void setDetalleServicioMedicoidDetalleServicioMedico(int detalleServicioMedicoidDetalleServicioMedico) {
        this.detalleServicioMedicoidDetalleServicioMedico = detalleServicioMedicoidDetalleServicioMedico;
    }

    public String getDetalleServicioMedicoServiciocodigo() {
        return detalleServicioMedicoServiciocodigo;
    }

    public void setDetalleServicioMedicoServiciocodigo(String detalleServicioMedicoServiciocodigo) {
        this.detalleServicioMedicoServiciocodigo = detalleServicioMedicoServiciocodigo;
    }

    public String getDetalleServicioMedicoMedicocmp() {
        return detalleServicioMedicoMedicocmp;
    }

    public void setDetalleServicioMedicoMedicocmp(String detalleServicioMedicoMedicocmp) {
        this.detalleServicioMedicoMedicocmp = detalleServicioMedicoMedicocmp;
    }

    public String getDetalleServicioMedicoMedicodni() {
        return detalleServicioMedicoMedicodni;
    }

    public void setDetalleServicioMedicoMedicodni(String detalleServicioMedicoMedicodni) {
        this.detalleServicioMedicoMedicodni = detalleServicioMedicoMedicodni;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idTurno;
        hash += (actividadcodigo != null ? actividadcodigo.hashCode() : 0);
        hash += (int) detalleServicioMedicoidDetalleServicioMedico;
        hash += (detalleServicioMedicoServiciocodigo != null ? detalleServicioMedicoServiciocodigo.hashCode() : 0);
        hash += (detalleServicioMedicoMedicocmp != null ? detalleServicioMedicoMedicocmp.hashCode() : 0);
        hash += (detalleServicioMedicoMedicodni != null ? detalleServicioMedicoMedicodni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurnoPK)) {
            return false;
        }
        TurnoPK other = (TurnoPK) object;
        if (this.idTurno != other.idTurno) {
            return false;
        }
        if ((this.actividadcodigo == null && other.actividadcodigo != null) || (this.actividadcodigo != null && !this.actividadcodigo.equals(other.actividadcodigo))) {
            return false;
        }
        if (this.detalleServicioMedicoidDetalleServicioMedico != other.detalleServicioMedicoidDetalleServicioMedico) {
            return false;
        }
        if ((this.detalleServicioMedicoServiciocodigo == null && other.detalleServicioMedicoServiciocodigo != null) || (this.detalleServicioMedicoServiciocodigo != null && !this.detalleServicioMedicoServiciocodigo.equals(other.detalleServicioMedicoServiciocodigo))) {
            return false;
        }
        if ((this.detalleServicioMedicoMedicocmp == null && other.detalleServicioMedicoMedicocmp != null) || (this.detalleServicioMedicoMedicocmp != null && !this.detalleServicioMedicoMedicocmp.equals(other.detalleServicioMedicoMedicocmp))) {
            return false;
        }
        if ((this.detalleServicioMedicoMedicodni == null && other.detalleServicioMedicoMedicodni != null) || (this.detalleServicioMedicoMedicodni != null && !this.detalleServicioMedicoMedicodni.equals(other.detalleServicioMedicoMedicodni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.TurnoPK[ idTurno=" + idTurno + ", actividadcodigo=" + actividadcodigo + ", detalleServicioMedicoidDetalleServicioMedico=" + detalleServicioMedicoidDetalleServicioMedico + ", detalleServicioMedicoServiciocodigo=" + detalleServicioMedicoServiciocodigo + ", detalleServicioMedicoMedicocmp=" + detalleServicioMedicoMedicocmp + ", detalleServicioMedicoMedicodni=" + detalleServicioMedicoMedicodni + " ]";
    }
    
}
