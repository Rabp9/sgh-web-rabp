
package org.sghweb.beans;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.sghweb.controllers.CitaJpaController;
import org.sghweb.controllers.DetallehistoriaclinicaJpaController;
import org.sghweb.controllers.HistoriaclinicaJpaController;
import org.sghweb.controllers.ReferenciaJpaController;
import org.sghweb.controllers.ServicioJpaController;
import org.sghweb.controllers.VwCitaJpaController;
import org.sghweb.controllers.VwMedicoJpaController;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Cita;
import org.sghweb.jpa.CitaPK;
import org.sghweb.jpa.Detallehistoriaclinica;
import org.sghweb.jpa.DetallehistoriaclinicaPK;
import org.sghweb.jpa.Historiaclinica;
import org.sghweb.jpa.HistoriaclinicaPK;
import org.sghweb.jpa.Referencia;
import org.sghweb.jpa.Servicio;
import org.sghweb.jpa.VwCita;
import org.sghweb.jpa.VwMedico;
import org.sghweb.jpa.VwReportepaciente;
import org.sghweb.jpa.VwReportereceta;

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
    private StreamedContent reporteCita;
            
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
        
        // Logueado como paciente
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
        dni = loginBean.getLoginPaciente().getDni();
        vwReportepaciente = vrjc.findVwReportepaciente(loginBean.getLoginPaciente().getDni());
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
     
    public void registrar() throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        if(dni == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un paciente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
        if(getSelectedCita() == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar una Cita", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
        ReferenciaJpaController rjc = new ReferenciaJpaController(null, null);
        List<Referencia> referencias = rjc.findReferenciabyDni(dni);
        
        if(referencias.isEmpty()) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El paciente tiene una referencia vencida", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
        
        // Cita
        CitaJpaController cjc = new CitaJpaController(null, null);
        CitaPK cpk = new CitaPK(getSelectedCita().getActoMedico(), getSelectedCita().getCmp(), getSelectedCita().getMedicoDni());
        Cita cita = cjc.findCita(cpk);
        cita.setEstado('3');
        cjc.edit(cita);
        
        // Detalle Historia Clínica
        DetallehistoriaclinicaJpaController djc = new DetallehistoriaclinicaJpaController(null, null);
        Detallehistoriaclinica d = new Detallehistoriaclinica();
        DetallehistoriaclinicaPK dpk = new DetallehistoriaclinicaPK();
        dpk.setCitaMedicocmp(getSelectedCita().getCmp());
        dpk.setCitaMedicodni(getSelectedCita().getMedicoDni());
        dpk.setCitaactoMedico(getSelectedCita().getActoMedico());
        dpk.setHistoriaClinicaPacientedni(vwReportepaciente.getDni());
        dpk.setHistoriaClinicaautogenerado(vwReportepaciente.getAutogenerado());
        dpk.setHistoriaClinicanumeroRegistro(vwReportepaciente.getNumeroRegistro());
        d.setDetallehistoriaclinicaPK(dpk);
        d.setFecha(cita.getFecha());
        
        HistoriaclinicaJpaController hjc = new HistoriaclinicaJpaController(null, null);
        HistoriaclinicaPK hcpk = new HistoriaclinicaPK();
        hcpk.setAutogenerado(vwReportepaciente.getAutogenerado());
        hcpk.setNumeroRegistro(vwReportepaciente.getNumeroRegistro());
        hcpk.setPacientedni(vwReportepaciente.getDni());
        Historiaclinica h = hjc.findHistoriaclinica(hcpk);
        
        d.setHistoriaclinica(h);
        d.setCita(cita);
        
        djc.create(d);
        
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "La cita fue registrada correctamente", null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        Document pdf = new Document();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(pdf, os);   
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        pdf.open();
       
        // INICIO Escritura de Reporte
        
        // Cabecera
        Paragraph cabecera = new Paragraph();
        String logo = servletContext.getRealPath("") + File.separator + "resources"+ File.separator + "img" + File.separator + "logo - header.png";
        cabecera.add(Image.getInstance(logo));
        pdf.setHeader(new HeaderFooter(cabecera, false));    
        
        // Título
        pdf.add(cabecera);
        Paragraph titulo = new Paragraph("Cita Registrada", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0)));
        titulo.setAlignment(Element.ALIGN_CENTER);
        pdf.add(titulo);
        
        //Mostrar información de la Receta
        pdf.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(0);
        
        table.addCell("Acto Médico:");
        table.addCell(getSelectedCita().getActoMedico());
        
        table.addCell("Médico:");
        table.addCell(selectedMedico.getNombreCompleto());
        
        table.addCell("CMP Médico:");
        table.addCell(selectedMedico.getCmp());
        
        table.addCell("Servicio:");
        table.addCell(selectedServicio.getDescripcion());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
        table.addCell("Fecha y Hora:");
        table.addCell(sdf.format(getSelectedCita().getFechaHora()));

        table.addCell("Paciente:");
        table.addCell(selectedReportePaciente.getNombreCompleto());
        
        table.addCell("DNI Paciente:");
        table.addCell(selectedReportePaciente.getDni());
        
        table.addCell("H/C:");
        table.addCell(selectedReportePaciente.getNumeroRegistro());
        
        pdf.add(table);
        
        pdf.close(); // no need to close PDFwriter?
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        setReporteCita(new DefaultStreamedContent(is, "application/pdf", "Reporte Cita.pdf"));
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

    public StreamedContent getReporteCita() {
        return reporteCita;
    }

    public void setReporteCita(StreamedContent reporteCita) {
        this.reporteCita = reporteCita;
    }
    
}
