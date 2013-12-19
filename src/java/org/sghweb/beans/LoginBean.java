/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.sghweb.controllers.MedicoJpaController;
import org.sghweb.controllers.OperadorJpaController;
import org.sghweb.controllers.PacienteJpaController;
import org.sghweb.jpa.Medico;
import org.sghweb.jpa.Operador;
import org.sghweb.jpa.Paciente;

/**
 *
 * @author Roberto
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private String usuario;
    private String clave;
    private String tipo;
    private MedicoJpaController mjc;
    private PacienteJpaController pjc;
    private OperadorJpaController ojc;
    private Medico loginMedico;
    private Paciente loginPaciente;
    private Operador loginOperador;
    
    public LoginBean() {
        mjc = new MedicoJpaController(null, null);
        pjc = new PacienteJpaController(null, null);
        ojc = new OperadorJpaController(null, null);
    }
    
    public String ingresar() {
        List<Medico> medicos = getMjc().findMedicoByUsuarioYClave(usuario, clave);
        if(!medicos.isEmpty()) {
            setTipo("medico");
            setLoginMedico(medicos.get(0));
            return "Home?faces-redirect=true";
        }
        List<Paciente> pacientes = getPjc().findPacienteByUsuarioYClave(usuario, clave);
        if(!pacientes.isEmpty()) {
            setTipo("paciente");
            setLoginPaciente(pacientes.get(0));
            return "Home?faces-redirect=true";
        }       
        List<Operador> operadores = getOjc().findOperadorByUsuarioYClave(usuario, clave);
        if(!operadores.isEmpty()) {
            setTipo("operador");
            setLoginOperador(operadores.get(0));
            return "Home?faces-redirect=true";
        }
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario no está registrado", null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        return "";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public MedicoJpaController getMjc() {
        return mjc;
    }

    public void setMjc(MedicoJpaController mjc) {
        this.mjc = mjc;
    }

    public PacienteJpaController getPjc() {
        return pjc;
    }

    public void setPjc(PacienteJpaController pjc) {
        this.pjc = pjc;
    }

    public OperadorJpaController getOjc() {
        return ojc;
    }

    public void setOjc(OperadorJpaController ojc) {
        this.ojc = ojc;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Medico getLoginMedico() {
        return loginMedico;
    }

    public void setLoginMedico(Medico loginMedico) {
        this.loginMedico = loginMedico;
    }

    public Paciente getLoginPaciente() {
        return loginPaciente;
    }

    public void setLoginPaciente(Paciente loginPaciente) {
        this.loginPaciente = loginPaciente;
    }

    public Operador getLoginOperador() {
        return loginOperador;
    }

    public void setLoginOperador(Operador loginOperador) {
        this.loginOperador = loginOperador;
    }
}
