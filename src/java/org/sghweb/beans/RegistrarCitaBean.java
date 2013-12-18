
package org.sghweb.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.sghweb.controllers.ServicioJpaController;
import org.sghweb.controllers.VwCitaJpaController;
import org.sghweb.controllers.VwMedicoJpaController;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.jpa.Servicio;
import org.sghweb.jpa.VwCita;
import org.sghweb.jpa.VwMedico;
import org.sghweb.jpa.VwReportepaciente;

/**
 *
 * @author essalud
 */
@ManagedBean
@ViewScoped
public class RegistrarCitaBean implements Serializable  {

    // Paciente
    private List<VwReportepaciente> listaReportePacientes;
    private VwReportepacienteJpaController vrjc;
    private VwReportepaciente selectedReportePaciente;
    private VwReportepaciente vwReportepaciente;
    private String dni;
    // Servicio
    private List<Servicio> listaServicios;
    private ServicioJpaController vsjc;
    private Servicio selectedServicio;
    private Servicio servicio;
    private String codigoServicio;    
    // Médico
    private List<VwMedico> listaMedicos;
    private VwMedicoJpaController vmjc;
    private VwMedico selectedMedico;
    private VwMedico vwMedico;
    private String cmp;
    // Cita
    private List<VwCita> listaCitas;
    private VwCitaJpaController vcjc;
    private VwCita selectedCita;
            
    public RegistrarCitaBean() {
        // Paciente
        vrjc = new VwReportepacienteJpaController(null, null);
        listaReportePacientes = vrjc.findVwReportepacienteEntities();
        vwReportepaciente = new VwReportepaciente();     
        // Servicio
        vsjc = new ServicioJpaController(null, null);
        listaServicios = vsjc.findServicioEntities();
        servicio = new Servicio();
        // Médico
        vmjc = new VwMedicoJpaController(null, null);
        listaMedicos = vmjc.findVwMedicoEntities();
        vwMedico = new VwMedico();
        // Cita
        vcjc = new VwCitaJpaController(null, null);
        listaCitas = vcjc.findVwCitaDisponibles();
    }
    
    public void onRowSelectPaciente(SelectEvent event) {
        setVwReportepaciente((VwReportepaciente) event.getObject());
        RequestContext.getCurrentInstance().reset("frmRegistrarCita_atcDni");  
        dni = vwReportepaciente.getDni();
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
        VwReportepaciente auxVwReportepaciente = vrjc.findVwReportepaciente(dni);
        if(auxVwReportepaciente != null)
            setVwReportepaciente(auxVwReportepaciente);
    }
    
    public List<String> listaCodigoServicio(String query) {  
        List<String> results = new ArrayList();  
          
        for(Servicio servicio : getListaServicios()) {
            if(servicio.getCodigo().startsWith(query))
                results.add(servicio.getCodigo());
        }
          
        return results;  
    }
    
    public void onRowSelectServicio(SelectEvent event) {
        setServicio((Servicio) event.getObject());
        RequestContext.getCurrentInstance().reset("frmRegistrarCita_atcCodigoServicio");  
        codigoServicio = servicio.getCodigo();
        listaMedicos = vmjc.findVwMedicoByServicio(servicio.getDescripcion());
    }
    
    public void verificarServicio() {
        Servicio auxServicio = vsjc.findServicio(codigoServicio);
        if(auxServicio != null) {
            servicio = auxServicio;
            listaMedicos = vmjc.findVwMedicoByServicio(servicio.getDescripcion());
        }
    }
    
    public void onRowSelectMedico(SelectEvent event) {
        setVwMedico((VwMedico) event.getObject());
        RequestContext.getCurrentInstance().reset("frmRegistrarCita_atcCMP");  
        cmp = vwMedico.getCmp();
        listaCitas = vcjc.findVwCitaDisponibles(cmp);
    }
    
    public List<String> listaCmp(String query) {  
        List<String> results = new ArrayList();  
          
        for(VwMedico vwMedico : getListaMedicos()) {
            if(vwMedico.getCmp().startsWith(query))
                results.add(vwMedico.getCmp());
        }
          
        return results;  
    }
      
    public void verificarMedico() {
        VwMedico auxVwMedico = vmjc.findVwMedico(cmp);
        if(auxVwMedico != null) {
            setVwMedico(auxVwMedico);
            listaCitas = vcjc.findVwCitaDisponibles(cmp);
        }
    }
     
    public void registrar() {
        if(dni == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un paciente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<Servicio> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<Servicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public ServicioJpaController getVsjc() {
        return vsjc;
    }

    public void setVsjc(ServicioJpaController vsjc) {
        this.vsjc = vsjc;
    }

    public Servicio getSelectedServicio() {
        return selectedServicio;
    }

    public void setSelectedServicio(Servicio selectedServicio) {
        this.selectedServicio = selectedServicio;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(String codigoServicio) {
        this.codigoServicio = codigoServicio;
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

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
    }

    public List<VwCita> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(List<VwCita> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public VwCitaJpaController getVcjc() {
        return vcjc;
    }

    public void setVcjc(VwCitaJpaController vcjc) {
        this.vcjc = vcjc;
    }

    public VwCita getSelectedCita() {
        return selectedCita;
    }

    public void setSelectedCita(VwCita selectedCita) {
        this.selectedCita = selectedCita;
    }
    
}
