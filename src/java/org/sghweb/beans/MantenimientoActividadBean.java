
package org.sghweb.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.sghweb.controllers.ActividadJpaController;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Actividad;

/**
 *
 * @author Roberto
 */
@ManagedBean
@ViewScoped
public class MantenimientoActividadBean implements Serializable {

    private List<Actividad> listaActividades;
    private Actividad selectedActividad;
    private Actividad actividad;
    private ActividadJpaController ajc;
    
    public MantenimientoActividadBean() {
        ajc = new ActividadJpaController(null, null);
        listaActividades = ajc.findActividadEntities();
        actividad = new Actividad();
    }
    
    public void crear() {
        try {
            actividad.setEstado('1');
            ajc.create(actividad);
            listaActividades = ajc.findActividadEntities();
            actividad = new Actividad();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Actividad Registrada correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar Actividad", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void editar() {
        try {
            ajc.edit(selectedActividad);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Actividad Modificada correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar Actividad", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminar() {
        try {
            if(selectedActividad != null) {
                selectedActividad.setEstado('2');
                ajc.edit(selectedActividad);
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Actividad Eliminada correctamente", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar Actividad", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public List<Actividad> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<Actividad> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public Actividad getSelectedActividad() {
        return selectedActividad;
    }

    public void setSelectedActividad(Actividad selectedActividad) {
        this.selectedActividad = selectedActividad;
    }

    public ActividadJpaController getAjc() {
        return ajc;
    }

    public void setAjc(ActividadJpaController ajc) {
        this.ajc = ajc;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
}
