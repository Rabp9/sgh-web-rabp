/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.sghweb.controllers.PacienteJpaController;
import org.sghweb.jpa.Paciente;

/**
 *
 * @author Roberto
 */
@ManagedBean
@RequestScoped
public class ReportePacienteBean implements Serializable {
   
    private List<Paciente> listaPacientes;
    private Paciente selectedPaciente;
    private PacienteJpaController pjc;

    public ReportePacienteBean() {
        pjc = new PacienteJpaController(null, null);
        listaPacientes = pjc.findPacienteEntities();
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

}
