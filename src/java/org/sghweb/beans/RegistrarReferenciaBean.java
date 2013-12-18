/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.sghweb.controllers.DetallereferenciadiagnosticoJpaController;
import org.sghweb.controllers.DiagnosticoJpaController;
import org.sghweb.controllers.PacienteJpaController;
import org.sghweb.controllers.ReferenciaJpaController;
import org.sghweb.controllers.ServicioJpaController;
import org.sghweb.controllers.VwMedicoJpaController;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Detallereferenciadiagnostico;
import org.sghweb.jpa.DetallereferenciadiagnosticoPK;
import org.sghweb.jpa.Diagnostico;
import org.sghweb.jpa.Paciente;
import org.sghweb.jpa.Referencia;
import org.sghweb.jpa.ReferenciaPK;
import org.sghweb.jpa.Servicio;
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
    private String dni;
    // Médico
    private List<VwMedico> listaMedicos;
    private VwMedicoJpaController vmjc;
    private VwMedico selectedMedico;
    private VwMedico vwMedico;
    private String cmp;
    // Diagnóstico
    private List<Diagnostico> listaDiagnosticos;
    private DiagnosticoJpaController vdjc;
    private Diagnostico selectedDiagnostico1;
    private Diagnostico diagnostico1;
    private Diagnostico selectedDiagnostico2;
    private Diagnostico diagnostico2;   
    private String codigoDiagnostico1;  
    private String codigoDiagnostico2;
    // Servicio
    private List<Servicio> listaServicios;
    private ServicioJpaController vsjc;
    private Servicio selectedServicioOrigen;
    private Servicio servicioOrigen;
    private Servicio selectedServicioDestino;
    private Servicio servicioDestino;
    private String codigoServicioOrigen;
    private String codigoServicioDestino;
    // Información
    private String numeroRegistro;
    private Date fechaEmision;
    private Date fechaRecibida;
    private Date fechaTermino;
    private String motivo;
    
    public RegistrarReferenciaBean() {
        // Paciente
        vrjc = new VwReportepacienteJpaController(null, null);
        listaReportePacientes = vrjc.findVwReportepacienteEntities();
        vwReportepaciente = new VwReportepaciente();
        // Médico
        vmjc = new VwMedicoJpaController(null, null);
        listaMedicos = vmjc.findVwMedicoEntities();
        vwMedico = new VwMedico();
        // Diagnóstico
        vdjc = new DiagnosticoJpaController(null, null);
        listaDiagnosticos = vdjc.findDiagnosticoEntities();
        diagnostico1 = new Diagnostico();
        diagnostico2 = new Diagnostico();
        // Servicio
        vsjc = new ServicioJpaController(null, null);
        listaServicios = vsjc.findServicioEntities();
        servicioOrigen = new Servicio();
        servicioDestino = new Servicio();
        // Información
        fechaEmision = new Date();
        fechaRecibida = new Date();
        fechaTermino = new Date();
    }
  
    public void onRowSelectPaciente(SelectEvent event) {
        vwReportepaciente = (VwReportepaciente) event.getObject();
        RequestContext.getCurrentInstance().reset("frmRegistrarReferencia_atcDni");  
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
    
    public void onRowSelectMedico(SelectEvent event) {
        setVwMedico((VwMedico) event.getObject());
        RequestContext.getCurrentInstance().reset("frmRegistrarReferencia_atcCMP");  
        cmp = vwMedico.getCmp();
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
        if(auxVwMedico != null)
            setVwMedico(auxVwMedico);
    }
     
    public List<String> listaCodigoDiagnostico(String query) {  
        List<String> results = new ArrayList();  
          
        for(Diagnostico diagnostico : getListaDiagnosticos()) {
            if(diagnostico.getCodigo().startsWith(query))
                results.add(diagnostico.getCodigo());
        }
          
        return results;  
    }
    
    public void onRowSelectDiagnostico1(SelectEvent event) {
        setDiagnostico1((Diagnostico) event.getObject());
        RequestContext.getCurrentInstance().reset("frmRegistrarReferencia_atcCodigoDiagnostico1");  
        codigoDiagnostico1 = diagnostico1.getCodigo();
    }
    
    public void verificarDiagnostico1() {
        Diagnostico auxDiagnostico = vdjc.findDiagnostico(codigoDiagnostico1);
        if(auxDiagnostico != null)
            diagnostico1 = auxDiagnostico;
    }
      
    public void onRowSelectDiagnostico2(SelectEvent event) {
        setDiagnostico2((Diagnostico) event.getObject());
        RequestContext.getCurrentInstance().reset("frmRegistrarReferencia_atcCodigoDiagnostico2");  
        codigoDiagnostico2 = diagnostico2.getCodigo();
    }
    
    public void verificarDiagnostico2() {
        Diagnostico auxDiagnostico = vdjc.findDiagnostico(codigoDiagnostico2);
        if(auxDiagnostico != null)
            diagnostico2 = auxDiagnostico;
    }
     
    public List<String> listaCodigoServicio(String query) {  
        List<String> results = new ArrayList();  
          
        for(Servicio servicio : getListaServicios()) {
            if(servicio.getCodigo().startsWith(query))
                results.add(servicio.getCodigo());
        }
          
        return results;  
    }
    
    public void onRowSelectServicioOrigen(SelectEvent event) {
        setServicioOrigen((Servicio) event.getObject());
        RequestContext.getCurrentInstance().reset("frmRegistrarReferencia_atcCodigoServicioOrigen");  
        codigoServicioOrigen = servicioOrigen.getCodigo();
    }
    
    public void verificarServicioOrigen() {
        Servicio auxServicio = vsjc.findServicio(codigoServicioOrigen);
        if(auxServicio != null)
            servicioOrigen = auxServicio;
    }
      
    public void onRowSelectServicioDestino(SelectEvent event) {
        setServicioDestino((Servicio) event.getObject());
        RequestContext.getCurrentInstance().reset("frmRegistrarReferencia_atcCodigoServicioDestino");  
        codigoServicioDestino = servicioDestino.getCodigo();
    }
    
    public void verificarServicioDestino() {
        Servicio auxServicio = vsjc.findServicio(codigoServicioDestino);
        if(auxServicio != null)
            setServicioDestino(auxServicio);
    }
    
    public void registrar() throws PreexistingEntityException, RollbackFailureException, Exception {
        if(dni == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un Paciente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        if(cmp == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un Médico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        if(codigoDiagnostico1 == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un Diagnóstico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        if(codigoDiagnostico2 == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un Diagnóstico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        if(codigoServicioOrigen == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un Servicio de Origen", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        if(codigoServicioDestino == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un Servicio de Destino", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }      
        if(numeroRegistro == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un Servicio de Destino", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        // Referencia
        Referencia referencia = new Referencia();
        referencia.setReferenciaPK(new ReferenciaPK(numeroRegistro, dni));
        PacienteJpaController pjc = new PacienteJpaController(null, null);
        Paciente paciente = pjc.findPaciente(dni);
        referencia.setPaciente(paciente);
        referencia.setFechaEmision(fechaEmision);
        referencia.setFechaRecibida(fechaRecibida);
        referencia.setFechaTermino(fechaTermino);
        referencia.setMotivo(motivo);
        ReferenciaJpaController rjc = new ReferenciaJpaController(null, null);
        rjc.create(referencia);
        
        // DetalleReferenciaDiagnostico1
        Detallereferenciadiagnostico drd1 = new Detallereferenciadiagnostico();
        DetallereferenciadiagnosticoPK drdpk = new DetallereferenciadiagnosticoPK();
        drdpk.setDiagnosticocodigo(codigoDiagnostico1);
        drdpk.setReferenciaPacientedni(dni);
        drdpk.setReferencianumeroRegistro(numeroRegistro);
        drd1.setDetallereferenciadiagnosticoPK(drdpk);
        diagnostico1 = vdjc.findDiagnostico(codigoDiagnostico1);
        drd1.setDiagnostico(diagnostico1);
        drd1.setReferencia(referencia);
        DetallereferenciadiagnosticoJpaController drdjc = new DetallereferenciadiagnosticoJpaController(null, null);
        drdjc.create(drd1);
        
        // DetalleReferenciaDiagnostico2
        Detallereferenciadiagnostico drd2 = new Detallereferenciadiagnostico();
        drdpk = new DetallereferenciadiagnosticoPK();
        drdpk.setDiagnosticocodigo(codigoDiagnostico2);
        drdpk.setReferenciaPacientedni(dni);
        drdpk.setReferencianumeroRegistro(numeroRegistro);
        drd2.setDetallereferenciadiagnosticoPK(drdpk);
        diagnostico2 = vdjc.findDiagnostico(codigoDiagnostico2);
        drd2.setDiagnostico(diagnostico2);
        drd2.setReferencia(referencia);
        drdjc.create(drd2);
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

    public List<Diagnostico> getListaDiagnosticos() {
        return listaDiagnosticos;
    }

    public void setListaDiagnosticos(List<Diagnostico> listaDiagnosticos) {
        this.listaDiagnosticos = listaDiagnosticos;
    }

    public DiagnosticoJpaController getVdjc() {
        return vdjc;
    }

    public void setVdjc(DiagnosticoJpaController vdjc) {
        this.vdjc = vdjc;
    }

    public Diagnostico getSelectedDiagnostico1() {
        return selectedDiagnostico1;
    }

    public void setSelectedDiagnostico1(Diagnostico selectedDiagnostico1) {
        this.selectedDiagnostico1 = selectedDiagnostico1;
    }

    public Diagnostico getDiagnostico1() {
        return diagnostico1;
    }

    public void setDiagnostico1(Diagnostico diagnostico1) {
        this.diagnostico1 = diagnostico1;
    }

    public Diagnostico getSelectedDiagnostico2() {
        return selectedDiagnostico2;
    }

    public void setSelectedDiagnostico2(Diagnostico selectedDiagnostico2) {
        this.selectedDiagnostico2 = selectedDiagnostico2;
    }

    public Diagnostico getDiagnostico2() {
        return diagnostico2;
    }

    public void setDiagnostico2(Diagnostico diagnostico2) {
        this.diagnostico2 = diagnostico2;
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

    public Servicio getSelectedServicioOrigen() {
        return selectedServicioOrigen;
    }

    public void setSelectedServicioOrigen(Servicio selectedServicioOrigen) {
        this.selectedServicioOrigen = selectedServicioOrigen;
    }

    public Servicio getServicioOrigen() {
        return servicioOrigen;
    }

    public void setServicioOrigen(Servicio servicioOrigen) {
        this.servicioOrigen = servicioOrigen;
    }

    public Servicio getSelectedServicioDestino() {
        return selectedServicioDestino;
    }

    public void setSelectedServicioDestino(Servicio selectedServicioDestino) {
        this.selectedServicioDestino = selectedServicioDestino;
    }

    public Servicio getServicioDestino() {
        return servicioDestino;
    }

    public void setServicioDestino(Servicio ServicioDestino) {
        this.servicioDestino = ServicioDestino;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaRecibida() {
        return fechaRecibida;
    }

    public void setFechaRecibida(Date fechaRecibida) {
        this.fechaRecibida = fechaRecibida;
    }

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getCodigoServicioOrigen() {
        return codigoServicioOrigen;
    }

    public void setCodigoServicioOrigen(String codigoServicioOrigen) {
        this.codigoServicioOrigen = codigoServicioOrigen;
    }

    public String getCodigoServicioDestino() {
        return codigoServicioDestino;
    }

    public void setCodigoServicioDestino(String codigoServicioDestino) {
        this.codigoServicioDestino = codigoServicioDestino;
    }

    public String getCodigoDiagnostico1() {
        return codigoDiagnostico1;
    }

    public void setCodigoDiagnostico1(String codigoDiagnostico1) {
        this.codigoDiagnostico1 = codigoDiagnostico1;
    }

    public String getCodigoDiagnostico2() {
        return codigoDiagnostico2;
    }

    public void setCodigoDiagnostico2(String codigoDiagnostico2) {
        this.codigoDiagnostico2 = codigoDiagnostico2;
    }

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
