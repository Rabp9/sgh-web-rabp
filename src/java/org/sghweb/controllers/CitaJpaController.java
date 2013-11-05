/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Cita;
import org.sghweb.jpa.CitaPK;
import org.sghweb.jpa.Medico;
import org.sghweb.jpa.Paciente;

/**
 *
 * @author Roberto
 */
public class CitaJpaController implements Serializable {

    public CitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cita cita) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cita.getCitaPK() == null) {
            cita.setCitaPK(new CitaPK());
        }
        cita.getCitaPK().setMedicodni(cita.getMedico().getMedicoPK().getDni());
        cita.getCitaPK().setPacientedni(cita.getPaciente().getDni());
        cita.getCitaPK().setMedicocmp(cita.getMedico().getMedicoPK().getCmp());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medico medico = cita.getMedico();
            if (medico != null) {
                medico = em.getReference(medico.getClass(), medico.getMedicoPK());
                cita.setMedico(medico);
            }
            Paciente paciente = cita.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getDni());
                cita.setPaciente(paciente);
            }
            em.persist(cita);
            if (medico != null) {
                medico.getCitaList().add(cita);
                medico = em.merge(medico);
            }
            if (paciente != null) {
                paciente.getCitaList().add(cita);
                paciente = em.merge(paciente);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCita(cita.getCitaPK()) != null) {
                throw new PreexistingEntityException("Cita " + cita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cita cita) throws NonexistentEntityException, RollbackFailureException, Exception {
        cita.getCitaPK().setMedicodni(cita.getMedico().getMedicoPK().getDni());
        cita.getCitaPK().setPacientedni(cita.getPaciente().getDni());
        cita.getCitaPK().setMedicocmp(cita.getMedico().getMedicoPK().getCmp());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita persistentCita = em.find(Cita.class, cita.getCitaPK());
            Medico medicoOld = persistentCita.getMedico();
            Medico medicoNew = cita.getMedico();
            Paciente pacienteOld = persistentCita.getPaciente();
            Paciente pacienteNew = cita.getPaciente();
            if (medicoNew != null) {
                medicoNew = em.getReference(medicoNew.getClass(), medicoNew.getMedicoPK());
                cita.setMedico(medicoNew);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getDni());
                cita.setPaciente(pacienteNew);
            }
            cita = em.merge(cita);
            if (medicoOld != null && !medicoOld.equals(medicoNew)) {
                medicoOld.getCitaList().remove(cita);
                medicoOld = em.merge(medicoOld);
            }
            if (medicoNew != null && !medicoNew.equals(medicoOld)) {
                medicoNew.getCitaList().add(cita);
                medicoNew = em.merge(medicoNew);
            }
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.getCitaList().remove(cita);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.getCitaList().add(cita);
                pacienteNew = em.merge(pacienteNew);
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
                CitaPK id = cita.getCitaPK();
                if (findCita(id) == null) {
                    throw new NonexistentEntityException("The cita with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CitaPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita cita;
            try {
                cita = em.getReference(Cita.class, id);
                cita.getCitaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cita with id " + id + " no longer exists.", enfe);
            }
            Medico medico = cita.getMedico();
            if (medico != null) {
                medico.getCitaList().remove(cita);
                medico = em.merge(medico);
            }
            Paciente paciente = cita.getPaciente();
            if (paciente != null) {
                paciente.getCitaList().remove(cita);
                paciente = em.merge(paciente);
            }
            em.remove(cita);
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

    public List<Cita> findCitaEntities() {
        return findCitaEntities(true, -1, -1);
    }

    public List<Cita> findCitaEntities(int maxResults, int firstResult) {
        return findCitaEntities(false, maxResults, firstResult);
    }

    private List<Cita> findCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cita.class));
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

    public Cita findCita(CitaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cita> rt = cq.from(Cita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
