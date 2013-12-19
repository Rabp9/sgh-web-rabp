/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Roberto
 */
@Entity
@Table(name = "medico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medico.findAll", query = "SELECT m FROM Medico m"),
    @NamedQuery(name = "Medico.findByCmp", query = "SELECT m FROM Medico m WHERE m.medicoPK.cmp = :cmp"),
    @NamedQuery(name = "Medico.findByDni", query = "SELECT m FROM Medico m WHERE m.medicoPK.dni = :dni"),
    @NamedQuery(name = "Medico.findByNombreCompleto", query = "SELECT m FROM Medico m WHERE m.nombreCompleto = :nombreCompleto"),
    @NamedQuery(name = "Medico.findByTelefono", query = "SELECT m FROM Medico m WHERE m.telefono = :telefono"),
    @NamedQuery(name = "Medico.findByEstado", query = "SELECT m FROM Medico m WHERE m.estado = :estado"),
    @NamedQuery(name = "Medico.findByUsername", query = "SELECT m FROM Medico m WHERE m.username = :username"),
    @NamedQuery(name = "Medico.findByPassword", query = "SELECT m FROM Medico m WHERE m.password = :password"),
    @NamedQuery(name = "Medico.findByUsernameYPassword", query = "SELECT m FROM Medico m WHERE m.username = :username AND m.password = :password")})
public class Medico implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MedicoPK medicoPK;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "password")
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medico")
    private List<Detalleserviciomedico> detalleserviciomedicoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medico")
    private List<Cita> citaList;

    public Medico() {
    }

    public Medico(MedicoPK medicoPK) {
        this.medicoPK = medicoPK;
    }

    public Medico(MedicoPK medicoPK, String nombreCompleto, String username, String password) {
        this.medicoPK = medicoPK;
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.password = password;
    }

    public Medico(String cmp, String dni) {
        this.medicoPK = new MedicoPK(cmp, dni);
    }

    public MedicoPK getMedicoPK() {
        return medicoPK;
    }

    public void setMedicoPK(MedicoPK medicoPK) {
        this.medicoPK = medicoPK;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public List<Detalleserviciomedico> getDetalleserviciomedicoList() {
        return detalleserviciomedicoList;
    }

    public void setDetalleserviciomedicoList(List<Detalleserviciomedico> detalleserviciomedicoList) {
        this.detalleserviciomedicoList = detalleserviciomedicoList;
    }

    @XmlTransient
    public List<Cita> getCitaList() {
        return citaList;
    }

    public void setCitaList(List<Cita> citaList) {
        this.citaList = citaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medicoPK != null ? medicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medico)) {
            return false;
        }
        Medico other = (Medico) object;
        if ((this.medicoPK == null && other.medicoPK != null) || (this.medicoPK != null && !this.medicoPK.equals(other.medicoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sghweb.jpa.Medico[ medicoPK=" + medicoPK + " ]";
    }
    
}
