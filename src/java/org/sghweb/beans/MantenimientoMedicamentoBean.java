
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
import org.sghweb.controllers.MedicamentoJpaController;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Medicamento;

/**
 *
 * @author Roberto
 */
@ManagedBean
@ViewScoped
public class MantenimientoMedicamentoBean implements Serializable {
   
    private List<Medicamento> listaMedicamentos;
    private Medicamento selectedMedicamento;
    private Medicamento medicamento;
    private MedicamentoJpaController mjc;
    private SelectItem[] presentacionOptions;
    private int cantidad;
    
    public MantenimientoMedicamentoBean() {
        mjc = new MedicamentoJpaController(null, null);
        listaMedicamentos = mjc.findMedicamentoEntities();
        medicamento = new Medicamento();
        presentacionOptions = crearOpciones();
        cantidad = 0;
    }
      
    public void crear() {
        try {
            medicamento.setEstado('1');
            mjc.create(medicamento);
            listaMedicamentos = mjc.findMedicamentoEntities();
            medicamento = new Medicamento();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Medicamento Registrado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe el Medicamento", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar Medicamento", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void editar() {
        try {
            mjc.edit(selectedMedicamento);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Medicamento Modificado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar Medicamento", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminar() {
        try {
            if(selectedMedicamento != null) {
                selectedMedicamento.setEstado('2');
                mjc.edit(selectedMedicamento);
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Medicamento Eliminado correctamente", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar Medicamento", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ingresar() {
        try {
            selectedMedicamento.setStock(selectedMedicamento.getStock() + cantidad);
            mjc.edit(selectedMedicamento);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingreso registrado correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            cantidad = 0;
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar Medicamento", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void salir() {
        try {
            if(selectedMedicamento.getStock() < cantidad) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "No se puede registrar orrectamente", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
            else {
                selectedMedicamento.setStock(selectedMedicamento.getStock() - cantidad);
                mjc.edit(selectedMedicamento);
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Salida registrada correctamente", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                cantidad = 0;
            }
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar Medicamento", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoActividadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private SelectItem[] crearOpciones()  {  
        SelectItem[] options = new SelectItem[23];  
  
        options[0] = new SelectItem("", "Elegir");
        options[1] = new SelectItem("TB" , "TB");
        options[2] = new SelectItem("AM" , "AM");
        options[3] = new SelectItem("FR" , "FR");
        options[4] = new SelectItem("PT" , "PT");
        options[5] = new SelectItem("CAD", "CAD");
        options[6] = new SelectItem("TU" , "TU");
        options[7] = new SelectItem("BL" , "BL");
        options[8] = new SelectItem("CP" , "CP");
        options[9] = new SelectItem("OV" , "OV");
        options[10] = new SelectItem("UN" , "UN");
        options[11] = new SelectItem("G"  , "G");
        options[12] = new SelectItem("SOB", "SOB");
        options[13] = new SelectItem("SU" , "SU");
        options[14] = new SelectItem("POT", "POT");
        options[15] = new SelectItem("BOL", "BOL");
        options[16] = new SelectItem("CM3", "CM3");
        options[17] = new SelectItem("SB" , "SB");
        options[18] = new SelectItem("PQ" , "PQ");
        options[19] = new SelectItem("ROL", "ROL");
        options[20] = new SelectItem("HJ" , "HJ");
        options[21] = new SelectItem("PBA", "PBA");
        options[22] = new SelectItem("M3" , "M3");
        
        return options;  
    }  
    
    public List<Medicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<Medicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public Medicamento getSelectedMedicamento() {
        return selectedMedicamento;
    }

    public void setSelectedMedicamento(Medicamento selectedMedicamento) {
        this.selectedMedicamento = selectedMedicamento;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public MedicamentoJpaController getMjc() {
        return mjc;
    }

    public void setMjc(MedicamentoJpaController mjc) {
        this.mjc = mjc;
    }

    public SelectItem[] getPresentacionOptions() {
        return presentacionOptions;
    }

    public void setPresentacionOptions(SelectItem[] presentacionOptions) {
        this.presentacionOptions = presentacionOptions;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
