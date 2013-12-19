
package org.sghweb.beans;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.*;
import java.text.SimpleDateFormat;
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
import org.sghweb.controllers.VwReportepacienteJpaController;
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
    
   /* public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
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
        
    }*/
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
        Paragraph titulo = new Paragraph("Lista de pacientes", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0)));
        titulo.setAlignment(Element.ALIGN_CENTER);
        pdf.add(titulo);
        
        //Mostrar información de Pacientes
        List<VwReportepaciente> vwReportepacientes = vrjc.findVwReportepacienteEntities();  
        pdf.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(6);
        table.setWidths(new int[]{15, 20, 30, 25, 25, 20});
        String[] campos = new String[]{"DNI", "Número de Registro", "Nombre Completo", "Autogenerado", "Titular", "Tipo Asegurado"};

        for(String campo : campos) {
            PdfPCell cell = new PdfPCell(new Phrase(campo, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(255, 255, 255))));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(0, 130, 197));
            table.addCell(cell);
        }
        
        for (VwReportepaciente vwReportepaciente : vwReportepacientes) {
            table.addCell(new Phrase(vwReportepaciente.getDni(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwReportepaciente.getNumeroRegistro(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwReportepaciente.getNombreCompleto(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwReportepaciente.getAutogenerado(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwReportepaciente.getTitular(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
            table.addCell(new Phrase(vwReportepaciente.getTipoAsegurado(), FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0))));
        }
        pdf.add(table);
        pdf.close(); // no need to close PDFwriter?
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        setReporte(new DefaultStreamedContent(is, "application/pdf", "Reporte de Pacientes.pdf"));
    }
    
    public void reportarPaciente() throws IOException, BadElementException, DocumentException {
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
        Paragraph titulo = new Paragraph("Reporte de Paciente", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0)));
        titulo.setAlignment(Element.ALIGN_CENTER);
        pdf.add(titulo);
        
        //Mostrar información de Paciente
        
        VwReportepaciente vwReportepaciente = vrjc.findVwReportepaciente(dni);
        if(vwReportepaciente == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe Paciente con DNI: " + dni, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
        pdf.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(2);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        PdfPCell cell1 = new PdfPCell(new Paragraph("Informacion Personal", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0))));
        cell1.setColspan(2);
        table.addCell(cell1);
        
        table.addCell("Nombre: ");
        table.addCell(vwReportepaciente.getNombreCompleto());
        
        table.addCell("DNI: ");
        table.addCell(vwReportepaciente.getDni());        

        table.addCell("Genero: ");
        table.addCell(vwReportepaciente.getGenero());
                
        table.addCell("Fecha de Nacimiento: ");
        table.addCell(sdf.format(vwReportepaciente.getFechaNacimiento()));
    
        PdfPCell cell2 = new PdfPCell(new Paragraph("Informacion General", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0))));
        cell2.setColspan(2);
        table.addCell(cell2);
    
        table.addCell("Ubicación de Nacimiento: ");
        table.addCell(vwReportepaciente.getUbicacionNacimiento());
        
        table.addCell("Ubicación Actual: ");
        table.addCell(vwReportepaciente.getUbicacionActual());
        
        table.addCell("Dirección: ");
        table.addCell(vwReportepaciente.getDireccion());
        
        table.addCell("Estado Civil: ");
        table.addCell(vwReportepaciente.getEstadoCivil());
        
        PdfPCell cell3 = new PdfPCell(new Paragraph("Informacion de Historia Clínica", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0))));
        cell3.setColspan(2);
        table.addCell(cell3);
        
        table.addCell("Numero de Registro: ");
        table.addCell(vwReportepaciente.getNumeroRegistro());
        
        table.addCell("Autogenerado: ");
        table.addCell(vwReportepaciente.getAutogenerado());
        
        table.addCell("Tipo de Asegurado: ");
        table.addCell(vwReportepaciente.getTipoAsegurado());
        
        table.addCell("Titular: ");
        table.addCell(vwReportepaciente.getTitular());
                
        table.addCell("Fecha afiliación: ");
        table.addCell(sdf.format(vwReportepaciente.getFechaInicio()));
        
        table.addCell("Fecha de ultima cita: ");
        table.addCell(sdf.format(vwReportepaciente.getFechaUltimaCita()));
        
        table.addCell("Citas del ultimo mes: ");
        table.addCell(String.valueOf(vwReportepaciente.getNumCitasUltimoMes()));
        
        table.addCell("Total de citas: ");
        table.addCell(String.valueOf(vwReportepaciente.getNumCitasTotal()));
        
        pdf.add(table);
        //Fin Escritura de Reprote
        
        pdf.close(); // no need to close PDFwriter?
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        setReporte(new DefaultStreamedContent(is, "application/pdf", "Paciente.pdf"));
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
