
package org.sghweb.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.sghweb.controllers.DiagnosticoJpaController;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Diagnostico;

/**
 *
 * @author Roberto
 */
@ManagedBean
@ViewScoped
public class MantenimientoDiagnosticoBean implements Serializable {

    private List<Diagnostico> listaDiagnosticos;
    private Diagnostico selectedDiagnostico;
    private Diagnostico diagnostico;
    private DiagnosticoJpaController djc;
    private SelectItem[] sexoAfectadoOptions;
  
    public MantenimientoDiagnosticoBean() {  
        djc = new DiagnosticoJpaController(null, null);
        listaDiagnosticos = djc.findDiagnosticoEntities();
        diagnostico = new Diagnostico();
        sexoAfectadoOptions = crearOpciones();
    }
    
    public void crear() {
        try {
            if(diagnostico.getSexoAfectado().equals("")) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se requiere seleccionar un sexo afectado", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
            diagnostico.setEstado('1');
            djc.create(diagnostico);
            listaDiagnosticos = djc.findDiagnosticoEntities();
            diagnostico = new Diagnostico();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Diagnóstico Registrado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoDiagnosticoBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe el Diagnóstico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoDiagnosticoBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar Diagnóstico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoDiagnosticoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    public void editar() {
        try {
            djc.edit(selectedDiagnostico);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Diagnóstico Modificado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar Diagnóstico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminar() {
        try {
            if(selectedDiagnostico != null) {
                selectedDiagnostico.setEstado('2');
                djc.edit(selectedDiagnostico);
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Diagnóstico Eliminado correctamente", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar Diagnóstico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private SelectItem[] crearOpciones()  {  
        SelectItem[] options = new SelectItem[4];  
  
        options[0] = new SelectItem("", "Elegir");
        options[1] = new SelectItem("FEMENINO", "FEMENINO");  
        options[2] = new SelectItem("MASCULINO", "MASCULINO");  
        options[3] = new SelectItem("AMBOS", "AMBOS");  
        
        return options;  
    }  
    
    public List<Diagnostico> getListaDiagnosticos() {
        return listaDiagnosticos;
    }

    public void setListaDiagnosticos(List<Diagnostico> listaDiagnosticos) {
        this.listaDiagnosticos = listaDiagnosticos;
    }

    public Diagnostico getSelectedDiagnostico() {
        return selectedDiagnostico;
    }

    public void setSelectedDiagnostico(Diagnostico selectedDiagnostico) {
        this.selectedDiagnostico = selectedDiagnostico;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public DiagnosticoJpaController getDjc() {
        return djc;
    }

    public void setDjc(DiagnosticoJpaController djc) {
        this.djc = djc;
    }

    public SelectItem[] getSexoAfectadoOptions() {
        return sexoAfectadoOptions;
    }

    public void setSexoAfectadoOptions(SelectItem[] sexoAfectadoOptions) {
        this.setSexoAfectadoOptions(sexoAfectadoOptions);
    }
}
