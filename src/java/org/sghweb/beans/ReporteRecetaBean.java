
package org.sghweb.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.jpa.VwReportepaciente;

/**
 *
 * @author essalud
 */
@ManagedBean
@RequestScoped
public class ReporteRecetaBean implements Serializable  {

    private List<VwReportepaciente> listaReportePacientes;
    private VwReportepacienteJpaController vrjc;
    private VwReportepaciente vwReportepaciente;

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
}
