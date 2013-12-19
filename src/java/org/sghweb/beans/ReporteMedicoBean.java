/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.sghweb.controllers.VwMedicoJpaController;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.jpa.VwMedico;
import org.sghweb.jpa.VwReportepaciente;

/**
 *
 * @author Roberto
 */
@ManagedBean
@RequestScoped
public class ReporteMedicoBean implements Serializable {

    private List<VwMedico> listaReporteMedicos;
    private VwMedico selectedReporteMedico;
    private VwMedicoJpaController vmjc;
    private String cmp;
    private StreamedContent reporte;
    
    public ReporteMedicoBean() {
        vmjc = new VwMedicoJpaController(null, null);
        listaReporteMedicos = vmjc.findVwMedicoEntities();
        cmp = "";
    }
    
    public List<String> listaCmp(String query) {  
        List<String> results = new ArrayList();  
          
        for(VwMedico vwMedico : getListaReporteMedicos()) {
            if(vwMedico.getCmp().startsWith(query))
                results.add(vwMedico.getCmp());
        }
          
        return results;  
    }  
    
    public void onRowSelect(SelectEvent event) {
        VwMedico vwMedico = (VwMedico) event.getObject();
        setCmp(vwMedico.getCmp());
    }
      
    public void reportarGeneral() throws IOException, BadElementException, DocumentException {
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
        Paragraph titulo = new Paragraph("Lista de médicos", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0)));
        titulo.setAlignment(Element.ALIGN_CENTER);
        pdf.add(titulo);
        
        //Mostrar información de Médicos
        List<VwMedico> vwMedicos = vmjc.findVwMedicoEntities();  
        pdf.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new int[]{15, 20, 30, 25, 25});
        String[] campos = new String[]{"CMP", "DNI", "Nombre Completo", "Teléfono", "Servicio"};

        for(String campo : campos) {
            PdfPCell cell = new PdfPCell(new Phrase(campo, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(255, 255, 255))));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(0, 130, 197));
            table.addCell(cell);
        }
        
        for (VwMedico vwMedico : vwMedicos) {
            table.addCell(new Phrase(vwMedico.getCmp(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwMedico.getDni(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwMedico.getNombreCompleto(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwMedico.getTelefono(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwMedico.getServicio(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
        }
        pdf.add(table);
        pdf.close(); // no need to close PDFwriter?
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        setReporte(new DefaultStreamedContent(is, "application/pdf", "Reporte de Médicos.pdf"));
    }
    
    public void reportarMedico() throws IOException, BadElementException, DocumentException {
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
        Paragraph titulo = new Paragraph("Reporte de Médico", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0)));
        titulo.setAlignment(Element.ALIGN_CENTER);
        pdf.add(titulo);
        
        //Mostrar información de Paciente
        
        VwMedico vwMedico = vmjc.findVwMedico(cmp);
        if(vwMedico == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe Médico con CMP: " + cmp, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
        pdf.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(2);
        
        PdfPCell cell1 = new PdfPCell(new Paragraph("Informacion Personal", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0))));
        cell1.setColspan(2);
        table.addCell(cell1);
        
        table.addCell("CMP: ");
        table.addCell(vwMedico.getCmp());
        
        table.addCell("DNI: ");
        table.addCell(vwMedico.getDni());        

        table.addCell("Nombre Completo: ");
        table.addCell(vwMedico.getNombreCompleto());
                
        table.addCell("Teléfono: ");
        table.addCell(vwMedico.getTelefono());
    
        table.addCell("Servicio: ");
        table.addCell(vwMedico.getServicio());
        
        pdf.add(table);
        //Fin Escritura de Reprote
        
        pdf.close(); // no need to close PDFwriter?
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        setReporte(new DefaultStreamedContent(is, "application/pdf", "Médico.pdf"));
    }
    
    public List<VwMedico> getListaReporteMedicos() {
        return listaReporteMedicos;
    }

    public void setListaReporteMedicos(List<VwMedico> listaReporteMedicos) {
        this.listaReporteMedicos = listaReporteMedicos;
    }

    public VwMedico getSelectedReporteMedico() {
        return selectedReporteMedico;
    }

    public void setSelectedReporteMedico(VwMedico selectedReporteMedico) {
        this.selectedReporteMedico = selectedReporteMedico;
    }

    public VwMedicoJpaController getVmjc() {
        return vmjc;
    }

    public void setVmjc(VwMedicoJpaController vmjc) {
        this.vmjc = vmjc;
    }

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }
}
