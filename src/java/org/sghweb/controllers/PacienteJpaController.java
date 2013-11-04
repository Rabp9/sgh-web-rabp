/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.sghweb.jpa.Cita;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Historiaclinica;
import org.sghweb.jpa.Paciente;
import org.sghweb.jpa.Referencia;

/**
 *
 * @author essalud
 */
public class PacienteJpaController implements Serializable {

    public PacienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "sgh-webPU") 
    private EntityManagerFactory emf = null;
    
    public EntityManager getEntityManager() {
        if (emf == null) { 
            emf = Persistence.createEntityManagerFactory("sgh-webPU"); 
        }
        return emf.createEntityManager();
    }

    public void create(Paciente paciente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (paciente.getCitaList() == null) {
            paciente.setCitaList(new ArrayList<Cita>());
        }
        if (paciente.getHistoriaclinicaList() == null) {
            paciente.setHistoriaclinicaList(new ArrayList<Historiaclinica>());
        }
        if (paciente.getReferenciaList() == null) {
            paciente.setReferenciaList(new ArrayList<Referencia>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cita> attachedCitaList = new ArrayList<Cita>();
            for (Cita citaListCitaToAttach : paciente.getCitaList()) {
                citaListCitaToAttach = em.getReference(citaListCitaToAttach.getClass(), citaListCitaToAttach.getCitaPK());
                attachedCitaList.add(citaListCitaToAttach);
            }
            paciente.setCitaList(attachedCitaList);
            List<Historiaclinica> attachedHistoriaclinicaList = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListHistoriaclinicaToAttach : paciente.getHistoriaclinicaList()) {
                historiaclinicaListHistoriaclinicaToAttach = em.getReference(historiaclinicaListHistoriaclinicaToAttach.getClass(), historiaclinicaListHistoriaclinicaToAttach.getHistoriaclinicaPK());
                attachedHistoriaclinicaList.add(historiaclinicaListHistoriaclinicaToAttach);
            }
            paciente.setHistoriaclinicaList(attachedHistoriaclinicaList);
            List<Referencia> attachedReferenciaList = new ArrayList<Referencia>();
            for (Referencia referenciaListReferenciaToAttach : paciente.getReferenciaList()) {
                referenciaListReferenciaToAttach = em.getReference(referenciaListReferenciaToAttach.getClass(), referenciaListReferenciaToAttach.getReferenciaPK());
                attachedReferenciaList.add(referenciaListReferenciaToAttach);
            }
            paciente.setReferenciaList(attachedReferenciaList);
            em.persist(paciente);
            for (Cita citaListCita : paciente.getCitaList()) {
                Paciente oldPacienteOfCitaListCita = citaListCita.getPaciente();
                citaListCita.setPaciente(paciente);
                citaListCita = em.merge(citaListCita);
                if (oldPacienteOfCitaListCita != null) {
                    oldPacienteOfCitaListCita.getCitaList().remove(citaListCita);
                    oldPacienteOfCitaListCita = em.merge(oldPacienteOfCitaListCita);
                }
            }
            for (Historiaclinica historiaclinicaListHistoriaclinica : paciente.getHistoriaclinicaList()) {
                Paciente oldPacienteOfHistoriaclinicaListHistoriaclinica = historiaclinicaListHistoriaclinica.getPaciente();
                historiaclinicaListHistoriaclinica.setPaciente(paciente);
                historiaclinicaListHistoriaclinica = em.merge(historiaclinicaListHistoriaclinica);
                if (oldPacienteOfHistoriaclinicaListHistoriaclinica != null) {
                    oldPacienteOfHistoriaclinicaListHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListHistoriaclinica);
                    oldPacienteOfHistoriaclinicaListHistoriaclinica = em.merge(oldPacienteOfHistoriaclinicaListHistoriaclinica);
                }
            }
            for (Referencia referenciaListReferencia : paciente.getReferenciaList()) {
                Paciente oldPacienteOfReferenciaListReferencia = referenciaListReferencia.getPaciente();
                referenciaListReferencia.setPaciente(paciente);
                referenciaListReferencia = em.merge(referenciaListReferencia);
                if (oldPacienteOfReferenciaListReferencia != null) {
                    oldPacienteOfReferenciaListReferencia.getReferenciaList().remove(referenciaListReferencia);
                    oldPacienteOfReferenciaListReferencia = em.merge(oldPacienteOfReferenciaListReferencia);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPaciente(paciente.getDni()) != null) {
                throw new PreexistingEntityException("Paciente " + paciente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paciente paciente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente persistentPaciente = em.find(Paciente.class, paciente.getDni());
            List<Cita> citaListOld = persistentPaciente.getCitaList();
            List<Cita> citaListNew = paciente.getCitaList();
            List<Historiaclinica> historiaclinicaListOld = persistentPaciente.getHistoriaclinicaList();
            List<Historiaclinica> historiaclinicaListNew = paciente.getHistoriaclinicaList();
            List<Referencia> referenciaListOld = persistentPaciente.getReferenciaList();
            List<Referencia> referenciaListNew = paciente.getReferenciaList();
            List<String> illegalOrphanMessages = null;
            for (Cita citaListOldCita : citaListOld) {
                if (!citaListNew.contains(citaListOldCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cita " + citaListOldCita + " since its paciente field is not nullable.");
                }
            }
            for (Historiaclinica historiaclinicaListOldHistoriaclinica : historiaclinicaListOld) {
                if (!historiaclinicaListNew.contains(historiaclinicaListOldHistoriaclinica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historiaclinica " + historiaclinicaListOldHistoriaclinica + " since its paciente field is not nullable.");
                }
            }
            for (Referencia referenciaListOldReferencia : referenciaListOld) {
                if (!referenciaListNew.contains(referenciaListOldReferencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Referencia " + referenciaListOldReferencia + " since its paciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cita> attachedCitaListNew = new ArrayList<Cita>();
            for (Cita citaListNewCitaToAttach : citaListNew) {
                citaListNewCitaToAttach = em.getReference(citaListNewCitaToAttach.getClass(), citaListNewCitaToAttach.getCitaPK());
                attachedCitaListNew.add(citaListNewCitaToAttach);
            }
            citaListNew = attachedCitaListNew;
            paciente.setCitaList(citaListNew);
            List<Historiaclinica> attachedHistoriaclinicaListNew = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListNewHistoriaclinicaToAttach : historiaclinicaListNew) {
                historiaclinicaListNewHistoriaclinicaToAttach = em.getReference(historiaclinicaListNewHistoriaclinicaToAttach.getClass(), historiaclinicaListNewHistoriaclinicaToAttach.getHistoriaclinicaPK());
                attachedHistoriaclinicaListNew.add(historiaclinicaListNewHistoriaclinicaToAttach);
            }
            historiaclinicaListNew = attachedHistoriaclinicaListNew;
            paciente.setHistoriaclinicaList(historiaclinicaListNew);
            List<Referencia> attachedReferenciaListNew = new ArrayList<Referencia>();
            for (Referencia referenciaListNewReferenciaToAttach : referenciaListNew) {
                referenciaListNewReferenciaToAttach = em.getReference(referenciaListNewReferenciaToAttach.getClass(), referenciaListNewReferenciaToAttach.getReferenciaPK());
                attachedReferenciaListNew.add(referenciaListNewReferenciaToAttach);
            }
            referenciaListNew = attachedReferenciaListNew;
            paciente.setReferenciaList(referenciaListNew);
            paciente = em.merge(paciente);
            for (Cita citaListNewCita : citaListNew) {
                if (!citaListOld.contains(citaListNewCita)) {
                    Paciente oldPacienteOfCitaListNewCita = citaListNewCita.getPaciente();
                    citaListNewCita.setPaciente(paciente);
                    citaListNewCita = em.merge(citaListNewCita);
                    if (oldPacienteOfCitaListNewCita != null && !oldPacienteOfCitaListNewCita.equals(paciente)) {
                        oldPacienteOfCitaListNewCita.getCitaList().remove(citaListNewCita);
                        oldPacienteOfCitaListNewCita = em.merge(oldPacienteOfCitaListNewCita);
                    }
                }
            }
            for (Historiaclinica historiaclinicaListNewHistoriaclinica : historiaclinicaListNew) {
                if (!historiaclinicaListOld.contains(historiaclinicaListNewHistoriaclinica)) {
                    Paciente oldPacienteOfHistoriaclinicaListNewHistoriaclinica = historiaclinicaListNewHistoriaclinica.getPaciente();
                    historiaclinicaListNewHistoriaclinica.setPaciente(paciente);
                    historiaclinicaListNewHistoriaclinica = em.merge(historiaclinicaListNewHistoriaclinica);
                    if (oldPacienteOfHistoriaclinicaListNewHistoriaclinica != null && !oldPacienteOfHistoriaclinicaListNewHistoriaclinica.equals(paciente)) {
                        oldPacienteOfHistoriaclinicaListNewHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListNewHistoriaclinica);
                        oldPacienteOfHistoriaclinicaListNewHistoriaclinica = em.merge(oldPacienteOfHistoriaclinicaListNewHistoriaclinica);
                    }
                }
            }
            for (Referencia referenciaListNewReferencia : referenciaListNew) {
                if (!referenciaListOld.contains(referenciaListNewReferencia)) {
                    Paciente oldPacienteOfReferenciaListNewReferencia = referenciaListNewReferencia.getPaciente();
                    referenciaListNewReferencia.setPaciente(paciente);
                    referenciaListNewReferencia = em.merge(referenciaListNewReferencia);
                    if (oldPacienteOfReferenciaListNewReferencia != null && !oldPacienteOfReferenciaListNewReferencia.equals(paciente)) {
                        oldPacienteOfReferenciaListNewReferencia.getReferenciaList().remove(referenciaListNewReferencia);
                        oldPacienteOfReferenciaListNewReferencia = em.merge(oldPacienteOfReferenciaListNewReferencia);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = paciente.getDni();
                if (findPaciente(id) == null) {
                    throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente paciente;
            try {
                paciente = em.getReference(Paciente.class, id);
                paciente.getDni();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cita> citaListOrphanCheck = paciente.getCitaList();
            for (Cita citaListOrphanCheckCita : citaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Cita " + citaListOrphanCheckCita + " in its citaList field has a non-nullable paciente field.");
            }
            List<Historiaclinica> historiaclinicaListOrphanCheck = paciente.getHistoriaclinicaList();
            for (Historiaclinica historiaclinicaListOrphanCheckHistoriaclinica : historiaclinicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Historiaclinica " + historiaclinicaListOrphanCheckHistoriaclinica + " in its historiaclinicaList field has a non-nullable paciente field.");
            }
            List<Referencia> referenciaListOrphanCheck = paciente.getReferenciaList();
            for (Referencia referenciaListOrphanCheckReferencia : referenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Referencia " + referenciaListOrphanCheckReferencia + " in its referenciaList field has a non-nullable paciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(paciente);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paciente> findPacienteEntities() {
        return findPacienteEntities(true, -1, -1);
    }

    public List<Paciente> findPacienteEntities(int maxResults, int firstResult) {
        return findPacienteEntities(false, maxResults, firstResult);
    }

    private List<Paciente> findPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paciente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Paciente findPaciente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paciente> rt = cq.from(Paciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
