/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.jpa.VwReportepaciente;

/**
 *
 * @author Roberto
 */
@ManagedBean
@ViewScoped
public class ReporteHistoriaClinicaBean implements Serializable {

    private List<VwReportepaciente> listaReportePacientes;
    private VwReportepaciente selectedReportePaciente;
    private VwReportepacienteJpaController vrjc;
    private String numeroRegistro;
    private StreamedContent reporte;
    
    public ReporteHistoriaClinicaBean() {    
        vrjc = new VwReportepacienteJpaController(null, null);
        listaReportePacientes = vrjc.findVwReportepacienteEntities();
        numeroRegistro = "";
    }      
    
    public List<String> listaNumeroRegistro(String query) {  
        List<String> results = new ArrayList();  
          
        for(VwReportepaciente vwReportepaciente : getListaReportePacientes()) {
            if(vwReportepaciente.getNumeroRegistro().startsWith(query))
                results.add(vwReportepaciente.getNumeroRegistro());
        }
          
        return results;  
    }  
    
    public void onRowSelect(SelectEvent event) {
        VwReportepaciente vwReportepaciente = (VwReportepaciente) event.getObject();
        setNumeroRegistro(vwReportepaciente.getNumeroRegistro());
    }
    
    public List<VwReportepaciente> getListaReportePacientes() {
        return listaReportePacientes;
    }

    public void setListaReportePacientes(List<VwReportepaciente> listaReportePacientes) {
        this.listaReportePacientes = listaReportePacientes;
    }

    public VwReportepaciente getSelectedReportePaciente() {
        return selectedReportePaciente;
    }

    public void setSelectedReportePaciente(VwReportepaciente selectedReportePaciente) {
        this.selectedReportePaciente = selectedReportePaciente;
    }

    public VwReportepacienteJpaController getVrjc() {
        return vrjc;
    }

    public void setVrjc(VwReportepacienteJpaController vrjc) {
        this.vrjc = vrjc;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }
    
} 