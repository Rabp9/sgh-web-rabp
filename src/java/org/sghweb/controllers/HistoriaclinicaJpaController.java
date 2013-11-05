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
import org.sghweb.jpa.Historiaclinica;
import org.sghweb.jpa.HistoriaclinicaPK;
import org.sghweb.jpa.Paciente;

/**
 *
 * @author Roberto
 */
public class HistoriaclinicaJpaController implements Serializable {

    public HistoriaclinicaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historiaclinica historiaclinica) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (historiaclinica.getHistoriaclinicaPK() == null) {
            historiaclinica.setHistoriaclinicaPK(new HistoriaclinicaPK());
        }
        historiaclinica.getHistoriaclinicaPK().setPacientedni(historiaclinica.getPaciente().getDni());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente paciente = historiaclinica.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getDni());
                historiaclinica.setPaciente(paciente);
            }
            em.persist(historiaclinica);
            if (paciente != null) {
                paciente.getHistoriaclinicaList().add(historiaclinica);
                paciente = em.merge(paciente);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findHistoriaclinica(historiaclinica.getHistoriaclinicaPK()) != null) {
                throw new PreexistingEntityException("Historiaclinica " + historiaclinica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Historiaclinica historiaclinica) throws NonexistentEntityException, RollbackFailureException, Exception {
        historiaclinica.getHistoriaclinicaPK().setPacientedni(historiaclinica.getPaciente().getDni());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Historiaclinica persistentHistoriaclinica = em.find(Historiaclinica.class, historiaclinica.getHistoriaclinicaPK());
            Paciente pacienteOld = persistentHistoriaclinica.getPaciente();
            Paciente pacienteNew = historiaclinica.getPaciente();
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getDni());
                historiaclinica.setPaciente(pacienteNew);
            }
            historiaclinica = em.merge(historiaclinica);
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.getHistoriaclinicaList().remove(historiaclinica);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.getHistoriaclinicaList().add(historiaclinica);
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
                HistoriaclinicaPK id = historiaclinica.getHistoriaclinicaPK();
                if (findHistoriaclinica(id) == null) {
                    throw new NonexistentEntityException("The historiaclinica with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(HistoriaclinicaPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Historiaclinica historiaclinica;
            try {
                historiaclinica = em.getReference(Historiaclinica.class, id);
                historiaclinica.getHistoriaclinicaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historiaclinica with id " + id + " no longer exists.", enfe);
            }
            Paciente paciente = historiaclinica.getPaciente();
            if (paciente != null) {
                paciente.getHistoriaclinicaList().remove(historiaclinica);
                paciente = em.merge(paciente);
            }
            em.remove(historiaclinica);
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

    public List<Historiaclinica> findHistoriaclinicaEntities() {
        return findHistoriaclinicaEntities(true, -1, -1);
    }

    public List<Historiaclinica> findHistoriaclinicaEntities(int maxResults, int firstResult) {
        return findHistoriaclinicaEntities(false, maxResults, firstResult);
    }

    private List<Historiaclinica> findHistoriaclinicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historiaclinica.class));
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

    public Historiaclinica findHistoriaclinica(HistoriaclinicaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historiaclinica.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoriaclinicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historiaclinica> rt = cq.from(Historiaclinica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
