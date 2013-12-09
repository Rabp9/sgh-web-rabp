
package org.sghweb.beans;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.sghweb.controllers.VwOrdenJpaController;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.controllers.VwReporterecetaJpaController;
import org.sghweb.jpa.VwOrden;
import org.sghweb.jpa.VwReportepaciente;
import org.sghweb.jpa.VwReportereceta;

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
    private List<VwOrden> listaOrdenes;
    private VwOrden selectedVwOrden;
    private VwOrdenJpaController vojc;
    private VwReportepaciente selectedReportePaciente;
    private StreamedContent reporte;
    private VwReporterecetaJpaController vrrjc;
    
    public ReporteRecetaBean() {
        vrjc = new VwReportepacienteJpaController(null, null);
        vojc = new VwOrdenJpaController(null, null);
        vrrjc = new VwReporterecetaJpaController(null, null);
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
        VwReportepaciente auxVwReportepaciente = vrjc.findVwReportepaciente(vwReportepaciente.getDni());
        if(auxVwReportepaciente != null)
            vwReportepaciente = auxVwReportepaciente;
    }

    public List<VwReportepaciente> getListaReportePacientes() {
        return listaReportePacientes;
    }
         
    public void onRowSelect(SelectEvent event) {
        vwReportepaciente = (VwReportepaciente) event.getObject();
    }
    
    public void mostrarRecetas() {
        if(fechaInicio.after(getFechaFin())) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La fecha de Inicio debe ser antes de la Fecha de Fin", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
        listaOrdenes = vojc.findVwOrdenByDniByHora(vwReportepaciente.getDni(), fechaInicio, fechaFin);
    }
    
    public void reportarReceta() throws IOException, BadElementException, DocumentException {
        if(selectedVwOrden == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No seleccionó ninguna receta", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
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
        Paragraph titulo = new Paragraph("Reporte de Receta", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0)));
        titulo.setAlignment(Element.ALIGN_CENTER);
        pdf.add(titulo);
        
        //Mostrar información de la Receta
        List<VwReportereceta> vwReporterecetas = vrrjc.findVwReporteRecetaByNroOrden(selectedVwOrden.getNroOrden());
        System.out.println(vwReporterecetas);
        pdf.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(0);
        
        table.addCell("No. Orden:");
        table.addCell(selectedVwOrden.getNroOrden());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
        table.addCell("Fecha:");
        table.addCell(sdf.format(selectedVwOrden.getFechaHora()));
        
        table.addCell("Servicio:");
        table.addCell(selectedVwOrden.getServicio());
        
        table.addCell("Paciente:");
        table.addCell(selectedVwOrden.getPaciente());

        table.addCell("Acto Médico:");
        table.addCell(selectedVwOrden.getActoMedico());
        
        table.addCell("Médico:");
        table.addCell(selectedVwOrden.getMedico());
        
        pdf.add(table);
        pdf.add(Chunk.NEWLINE);
     
        table = new PdfPTable(7);
                
        table.setWidths(new int[]{15, 35, 15, 20, 20, 20, 40});
        String[] campos = new String[]{"No. Receta", "Medicamento", "UM", "Solicita", "Atendido", "Pendiente", "Indicación"};

        for(String campo : campos) {
            PdfPCell cell = new PdfPCell(new Phrase(campo, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(255, 255, 255))));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(0, 130, 197));
            table.addCell(cell);
        }
                
        for (VwReportereceta vwReportereceta : vwReporterecetas) {
            table.addCell(new Phrase(vwReportereceta.getNroReceta(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwReportereceta.getMedicamento(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwReportereceta.getPresentacion(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(String.valueOf(vwReportereceta.getSolicita()), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(String.valueOf(vwReportereceta.getAtendido()), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(String.valueOf(vwReportereceta.getPendiente()), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwReportereceta.getIndicacion(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
        }
        
        pdf.add(table);
        
        pdf.close(); // no need to close PDFwriter?
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        setReporte(new DefaultStreamedContent(is, "application/pdf", "Reporte Receta.pdf"));
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

    public List<VwOrden> getListaOrdenes() {
        return listaOrdenes;
    }

    public void setListaOrdenes(List<VwOrden> listaOrdenes) {
        this.listaOrdenes = listaOrdenes;
    }

    public VwOrden getSelectedVwOrden() {
        return selectedVwOrden;
    }

    public void setSelectedVwOrden(VwOrden selectedVwOrden) {
        this.selectedVwOrden = selectedVwOrden;
    }

    public VwOrdenJpaController getVojc() {
        return vojc;
    }

    public void setVojc(VwOrdenJpaController vojc) {
        this.vojc = vojc;
    }

    public VwReportepaciente getSelectedReportePaciente() {
        return selectedReportePaciente;
    }

    public void setSelectedReportePaciente(VwReportepaciente selectedReportePaciente) {
        this.selectedReportePaciente = selectedReportePaciente;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }

    public VwReporterecetaJpaController getVrrjc() {
        return vrrjc;
    }

    public void setVrrjc(VwReporterecetaJpaController vrrjc) {
        this.vrrjc = vrrjc;
    }
}
