/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.sghweb.controllers.MedicoJpaController;
import org.sghweb.controllers.PacienteJpaController;
import org.sghweb.jpa.Medico;
import org.sghweb.jpa.MedicoPK;
import org.sghweb.jpa.Paciente;

/**
 *
 * @author Roberto
 */
@ManagedBean
@ViewScoped
public class MantenimientoPacienteBean implements Serializable {

    private List<Paciente> listaPacientes;
    private Paciente selectedPaciente;
    private Paciente paciente;
    private PacienteJpaController pjc;
    
    public MantenimientoPacienteBean() {
        pjc = new PacienteJpaController(null, null);
        listaPacientes = pjc.findPacienteEntities();
        paciente = new Paciente();
    }

    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(List<Paciente> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public Paciente getSelectedPaciente() {
        return selectedPaciente;
    }

    public void setSelectedPaciente(Paciente selectedPaciente) {
        this.selectedPaciente = selectedPaciente;
    }

    public PacienteJpaController getPjc() {
        return pjc;
    }

    public void setPjc(PacienteJpaController pjc) {
        this.pjc = pjc;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
