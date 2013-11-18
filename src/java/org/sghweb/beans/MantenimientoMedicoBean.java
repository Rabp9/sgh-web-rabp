/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.sghweb.controllers.MedicoJpaController;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Actividad;
import org.sghweb.jpa.Medico;

/**
 *
 * @author Roberto
 */
@ManagedBean
@ViewScoped
public class MantenimientoMedicoBean implements Serializable {

    private List<Medico> listaMedicos;
    private Medico selectedMedico;
    private Medico medico;
    private MedicoJpaController mjc;
    
    public MantenimientoMedicoBean() {   
        mjc = new MedicoJpaController(null, null);
        listaMedicos = mjc.findMedicoEntities();
        medico = new Medico();
    }
    
    public void crear() {
        try {
            medico.setEstado('1');
            mjc.create(medico);
            listaMedicos = mjc.findMedicoEntities();
            medico = new Medico();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Médico Registrado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe el Médico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar Médico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void editar() {
        try {
            mjc.edit(selectedMedico);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Médico Modificado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar Médico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminar() {
        try {
            if(selectedMedico != null) {
                selectedMedico.setEstado('2');
                mjc.edit(selectedMedico);
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Médico Eliminado correctamente", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar Médico", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoMedicoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public List<Medico> getListaMedicos() {
        return listaMedicos;
    }

    public void setListaMedicos(List<Medico> listaMedicos) {
        this.listaMedicos = listaMedicos;
    }

    public Medico getSelectedMedico() {
        return selectedMedico;
    }

    public void setSelectedMedico(Medico selectedMedico) {
        this.selectedMedico = selectedMedico;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public MedicoJpaController getMjc() {
        return mjc;
    }

    public void setMjc(MedicoJpaController mjc) {
        this.mjc = mjc;
    }
}
