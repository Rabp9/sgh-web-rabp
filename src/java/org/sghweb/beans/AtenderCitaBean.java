/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import org.sghweb.controllers.CitaJpaController;
import org.sghweb.controllers.ServicioJpaController;
import org.sghweb.controllers.VwCitaAtenderJpaController;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Cita;
import org.sghweb.jpa.CitaPK;
import org.sghweb.jpa.Medico;
import org.sghweb.jpa.Servicio;
import org.sghweb.jpa.VwCitaAtender;
import org.sghweb.jpa.VwReportepaciente;

/**
 *
 * @author Roberto
 */
@ManagedBean
@SessionScoped
public class AtenderCitaBean implements Serializable {

    private Date fecha;
    private VwCitaAtender selectedCita;
    private VwCitaAtenderJpaController vcajc;
    private Medico medico;
    private List<VwCitaAtender> listaCitas;
    private String cmp;
    private VwReportepaciente vwReportepaciente;
    private VwReportepacienteJpaController vrjc;
    private Servicio servicio;
    private ServicioJpaController sjc;
    
    public AtenderCitaBean() {
        fecha = new Date();
        vcajc = new VwCitaAtenderJpaController(null, null);
        vrjc = new VwReportepacienteJpaController(null, null);
        sjc = new ServicioJpaController(null, null);
        
        // Logueado como médico
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
        cmp = loginBean.getLoginMedico().getMedicoPK().getCmp();
        medico = loginBean.getLoginMedico();
        listaCitas = vcajc.findVwCitaAtenderbyCmp(cmp, fecha);
    }

    public void seleccionarFecha(SelectEvent event) {
        listaCitas = vcajc.findVwCitaAtenderbyCmp(getCmp(), fecha);
    }
    
    public void onRowSelectCita() throws IOException {
        vwReportepaciente = vrjc.findVwReportepaciente(selectedCita.getDniPaciente());
        servicio = sjc.findServicio(selectedCita.getCodigoServicio());
        FacesContext.getCurrentInstance().getExternalContext().redirect("AtencionPacientes/AtenderCita2.xhtml");
    }
    
    public String finalizarCita() throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        // Cita
        CitaJpaController cjc = new CitaJpaController(null, null);
        CitaPK cpk = new CitaPK(getSelectedCita().getActoMedico(), getSelectedCita().getCmp(), getSelectedCita().getMedicoDni());
        Cita cita = cjc.findCita(cpk);
        cita.setEstado('2');
        cjc.edit(cita);   
        
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "La cita fue atendida correctamente", null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        fecha = new Date();
        // Logueado como médico
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
        cmp = loginBean.getLoginMedico().getMedicoPK().getCmp();
        medico = loginBean.getLoginMedico();
        listaCitas = vcajc.findVwCitaAtenderbyCmp(cmp, fecha);
        
        return "/AtencionPacientes/AtenderCita";
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public VwCitaAtender getSelectedCita() {
        return selectedCita;
    }

    public void setSelectedCita(VwCitaAtender selectedCita) {
        this.selectedCita = selectedCita;
    }

    public VwCitaAtenderJpaController getVcajc() {
        return vcajc;
    }

    public void setVcajc(VwCitaAtenderJpaController vcajc) {
        this.vcajc = vcajc;
    }

    public List<VwCitaAtender> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(List<VwCitaAtender> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
    }

    public VwReportepaciente getVwReportepaciente() {
        return vwReportepaciente;
    }

    public void setVwReportepaciente(VwReportepaciente vwReportepaciente) {
        this.vwReportepaciente = vwReportepaciente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public ServicioJpaController getSjc() {
        return sjc;
    }

    public void setSjc(ServicioJpaController sjc) {
        this.sjc = sjc;
    }
}
