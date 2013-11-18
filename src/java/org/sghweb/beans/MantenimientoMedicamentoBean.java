
package org.sghweb.beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.sghweb.controllers.MedicamentoJpaController;
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
    
    public MantenimientoMedicamentoBean() {
        mjc = new MedicamentoJpaController(null, null);
        listaMedicamentos = mjc.findMedicamentoEntities();
        medicamento = new Medicamento();
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
}
