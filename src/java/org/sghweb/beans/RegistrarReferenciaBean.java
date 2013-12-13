/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.event.SelectEvent;
import org.sghweb.controllers.VwMedicoJpaController;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.jpa.VwMedico;
import org.sghweb.jpa.VwReportepaciente;

/**
 *
 * @author essalud
 */
@ManagedBean
@RequestScoped
public class RegistrarReferenciaBean implements Serializable  {

    // Paciente
    private List<VwReportepaciente> listaReportePacientes;
    private VwReportepacienteJpaController vrjc;
    private VwReportepaciente selectedReportePaciente;
    private VwReportepaciente vwReportepaciente;
    // Médico
    private List<VwMedico> listaMedicos;
    private VwMedicoJpaController vmjc;
    private VwMedico selectedMedico;
    private VwMedico vwMedico;
    
    public RegistrarReferenciaBean() {
        vrjc = new VwReportepacienteJpaController(null, null);
        listaReportePacientes = vrjc.findVwReportepacienteEntities();
        vwReportepaciente = new VwReportepaciente();
    }
  
    public void onRowSelectPaciente(SelectEvent event) {
        setVwReportepaciente((VwReportepaciente) event.getObject());
    }
    
    public List<String> listaDni(String query) {  
        List<String> results = new ArrayList();  
          
        for(VwReportepaciente vwReportepaciente : getListaReportePacientes()) {
            if(vwReportepaciente.getDni().startsWith(query))
                results.add(vwReportepaciente.getDni());
        }
          
        return results;  
    }
      
    public void verificarPaciente() {
        VwReportepaciente auxVwReportepaciente = vrjc.findVwReportepaciente(vwReportepaciente.getDni());
        if(auxVwReportepaciente != null)
            vwReportepaciente = auxVwReportepaciente;
    }
    
    public List<VwReportepaciente> getListaReportePacientes() {
        return listaReportePacientes;
    }

    public void setListaReportePacientes(List<VwReportepaciente> listaReportePacientes) {
        this.listaReportePacientes = listaReportePacientes;
    }

    public VwReportepacienteJpaController getVrjc() {
        return vrjc;
    }

    public void setVrjc(VwReportepacienteJpaController vrjc) {
        this.vrjc = vrjc;
    }

    public VwReportepaciente getSelectedReportePaciente() {
        return selectedReportePaciente;
    }

    public void setSelectedReportePaciente(VwReportepaciente selectedReportePaciente) {
        this.selectedReportePaciente = selectedReportePaciente;
    }

    public VwReportepaciente getVwReportepaciente() {
        return vwReportepaciente;
    }

    public void setVwReportepaciente(VwReportepaciente vwReportepaciente) {
        this.vwReportepaciente = vwReportepaciente;
    }

    public List<VwMedico> getListaMedicos() {
        return listaMedicos;
    }

    public void setListaMedicos(List<VwMedico> listaMedicos) {
        this.listaMedicos = listaMedicos;
    }

    public VwMedicoJpaController getVmjc() {
        return vmjc;
    }

    public void setVmjc(VwMedicoJpaController vmjc) {
        this.vmjc = vmjc;
    }

    public VwMedico getSelectedMedico() {
        return selectedMedico;
    }

    public void setSelectedMedico(VwMedico selectedMedico) {
        this.selectedMedico = selectedMedico;
    }

    public VwMedico getVwMedico() {
        return vwMedico;
    }

    public void setVwMedico(VwMedico vwMedico) {
        this.vwMedico = vwMedico;
    }
}
