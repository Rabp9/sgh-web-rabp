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
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.sghweb.controllers.VwReportepacienteJpaController;
import org.sghweb.jpa.Paciente;
import org.sghweb.jpa.VwReportepaciente;

/**
 *
 * @author Roberto
 */
@ManagedBean
@RequestScoped
public class ReportePacienteBean implements Serializable {
   
    private List<VwReportepaciente> listaReportePacientes;
    private VwReportepaciente selectedReportePaciente;
    private VwReportepacienteJpaController vrjc;
    private String dni;
    private StreamedContent reporte;  

    public ReportePacienteBean() {
        vrjc = new VwReportepacienteJpaController(null, null);
        listaReportePacientes = vrjc.findVwReportepacienteEntities();
        dni = "";
    }

    public List<String> listaDni(String query) {  
        List<String> results = new ArrayList();  
          
        for(VwReportepaciente vwReportepaciente : getListaReportePacientes()) {
            if(vwReportepaciente.getDni().startsWith(query))
                results.add(vwReportepaciente.getDni());
        }
          
        return results;  
    }  
         
    public void onRowSelect(SelectEvent event) {
        VwReportepaciente vwReportepaciente = (VwReportepaciente) event.getObject();
        setDni(vwReportepaciente.getDni());
    }
    
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        pdf.open();           
             
        // Cabecera
        Paragraph cabecera = new Paragraph();
        String logo = servletContext.getRealPath("") + File.separator + "resources"+ File.separator + "img" + File.separator + "logo - header.png";
        cabecera.add(Image.getInstance(logo));
        pdf.setHeader(new HeaderFooter(cabecera, false));
              
        // Pie de Página
        HeaderFooter hf = new HeaderFooter(new Paragraph(""), true);
        hf.setAlignment(Element.ALIGN_CENTER);
        pdf.setFooter(hf);
        
        // Título
        pdf.add(cabecera);
        Paragraph titulo = new Paragraph("Lista de pacientes", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0)));
        titulo.setAlignment(Element.ALIGN_CENTER);
        pdf.add(titulo);
        
        pdf.add(new Paragraph("Número total de Pacientes: " + getListaReportePacientes().size()));
        pdf.add(Chunk.NEWLINE);
    }

    public void reportarPaciente() throws IOException, BadElementException, DocumentException {
        Document document = new Document();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);
        document.open();
        
        // INICIO Escritura de Reporte
        document.add(new Paragraph("A Hello World PDF document."));

        //Fin Escritura de Reprote
        
        document.close(); // no need to close PDFwriter?
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        setReporte(new DefaultStreamedContent(is, "application/pdf", "ohyeah.pdf"));
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }

}
