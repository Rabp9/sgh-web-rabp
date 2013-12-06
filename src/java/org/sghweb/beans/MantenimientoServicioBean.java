
package org.sghweb.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.sghweb.controllers.ServicioJpaController;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Servicio;

/**
 *
 * @author Roberto
 */
@ManagedBean
@ViewScoped
public class MantenimientoServicioBean implements Serializable {

    private List<Servicio> listaServicios;
    private Servicio selectedServicio; 
    private Servicio servicio;
    private ServicioJpaController sjc;
    
    public MantenimientoServicioBean() {
        sjc = new ServicioJpaController(null, null);
        listaServicios = sjc.findServicioEntities();
        servicio = new Servicio();
    }
  
    public void crear() {
        try {
            servicio.setEstado('1');
            sjc.create(servicio);
            listaServicios = sjc.findServicioEntities();
            servicio = new Servicio();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Servicio Registrado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe el Servicio", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar Servicio", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editar() {
        try {
            sjc.edit(selectedServicio);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Servicio Modificado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoServicioBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoServicioBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar Servicio", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoServicioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public void eliminar() {
        try {
            if(selectedServicio != null) {
                selectedServicio.setEstado('2');
                sjc.edit(selectedServicio);
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Servicio Eliminado correctamente", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoServicioBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoServicioBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar Servicio", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoServicioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public ServicioJpaController getSjc() {
        return sjc;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public List<Servicio> getListaServicios() {
        return listaServicios;
    }

    public Servicio getSelectedServicio() {
        return selectedServicio;
    }

    public void setSelectedServicio(Servicio selectedServicio) {
        this.selectedServicio = selectedServicio;
    }
}
