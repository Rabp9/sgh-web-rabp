
package org.sghweb.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.jpa.VwReportepaciente;

/**
 *
 * @author essalud
 */
@ManagedBean
@ViewScoped

public class ReporteRecetaBean implements Serializable  {

    private List<VwReportepaciente> listaReportePacientes;
    private VwReportepacienteJpaController vrjc;
    private VwReportepaciente vwReportepaciente;
    private Date fechaInicio;
    private Date fechaFin;

    public ReporteRecetaBean() {
        vrjc = new VwReportepacienteJpaController(null, null);
        listaReportePacientes = vrjc.findVwReportepacienteEntities();
        vwReportepaciente = new VwReportepaciente();
    }
    
    public List<String> listaDni(String query) {  
        List<String> results = new ArrayList();  
          
        for(VwReportepaciente vwReportepaciente : getListaReportePacientes()) {
            if(vwReportepaciente.getDni().startsWith(query))
                results.add(vwReportepaciente.getDni());
        }
          
        return results;  
    }
    
    public void verificar() {
        vwReportepaciente = vrjc.findVwReportepaciente(vwReportepaciente.getDni());
    }

    public List<VwReportepaciente> getListaReportePacientes() {
        return listaReportePacientes;
    }
         
    public void onRowSelect(SelectEvent event) {
        vwReportepaciente = (VwReportepaciente) event.getObject();
    }
    
    public void mostrarRecetas() {
        if(fechaInicio.after(fechaFin)) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La fecha de Inicio debe ser antes de la Fecha de Fin", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
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

    public VwReportepaciente getVwReportepaciente() {
        return vwReportepaciente;
    }

    public void setVwReportepaciente(VwReportepaciente vwReportepaciente) {
        this.vwReportepaciente = vwReportepaciente;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
