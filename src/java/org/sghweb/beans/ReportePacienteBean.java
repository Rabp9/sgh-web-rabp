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
import com.lowagie.text.pdf.BaseFont;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.SelectEvent;
import org.sghweb.controllers.PacienteJpaController;
import org.sghweb.jpa.Paciente;

/**
 *
 * @author Roberto
 */
@ManagedBean
@RequestScoped
public class ReportePacienteBean implements Serializable {
   
    private List<Paciente> listaPacientes;
    private Paciente selectedPaciente;
    private PacienteJpaController pjc;
    private String dni;

    public ReportePacienteBean() {
        pjc = new PacienteJpaController(null, null);
        listaPacientes = pjc.findPacienteEntities();
        dni = "";
    }

    public List<String> listaDni(String query) {  
        List<String> results = new ArrayList();  
          
        for(Paciente paciente : listaPacientes) {
            if(paciente.getDni().startsWith(query))
                results.add(paciente.getDni());
        }
          
        return results;  
    }  
         
    public void onRowSelect(SelectEvent event) {
        Paciente paciente = (Paciente) event.getObject();
        dni = paciente.getDni();
    }
    
    public void preProcessPDF(Object document) throws IOException,
        BadElementException, DocumentException {
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
        
        pdf.add(new Paragraph("Número total de Pacientes: " + listaPacientes.size()));
        pdf.add(Chunk.NEWLINE);
    }
    
    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(List<Paciente> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public Paciente getSelectedPaciente() {
        return selectedPaciente;
    }

    public void setSelectedPaciente(Paciente selectedPaciente) {
        this.selectedPaciente = selectedPaciente;
    }

    public PacienteJpaController getPjc() {
        return pjc;
    }

    public void setPjc(PacienteJpaController pjc) {
        this.pjc = pjc;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

}
