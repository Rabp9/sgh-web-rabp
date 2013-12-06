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
import org.sghweb.jpa.Turno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Actividad;

/**
 *
 * @author Roberto
 */
public class ActividadJpaController implements Serializable {

    public ActividadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividad actividad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (actividad.getTurnoList() == null) {
            actividad.setTurnoList(new ArrayList<Turno>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Turno> attachedTurnoList = new ArrayList<Turno>();
            for (Turno turnoListTurnoToAttach : actividad.getTurnoList()) {
                turnoListTurnoToAttach = em.getReference(turnoListTurnoToAttach.getClass(), turnoListTurnoToAttach.getTurnoPK());
                attachedTurnoList.add(turnoListTurnoToAttach);
            }
            actividad.setTurnoList(attachedTurnoList);
            em.persist(actividad);
            for (Turno turnoListTurno : actividad.getTurnoList()) {
                Actividad oldActividadOfTurnoListTurno = turnoListTurno.getActividad();
                turnoListTurno.setActividad(actividad);
                turnoListTurno = em.merge(turnoListTurno);
                if (oldActividadOfTurnoListTurno != null) {
                    oldActividadOfTurnoListTurno.getTurnoList().remove(turnoListTurno);
                    oldActividadOfTurnoListTurno = em.merge(oldActividadOfTurnoListTurno);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findActividad(actividad.getCodigo()) != null) {
                throw new PreexistingEntityException("Actividad " + actividad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividad actividad) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Actividad persistentActividad = em.find(Actividad.class, actividad.getCodigo());
            List<Turno> turnoListOld = persistentActividad.getTurnoList();
            List<Turno> turnoListNew = actividad.getTurnoList();
            List<String> illegalOrphanMessages = null;
            for (Turno turnoListOldTurno : turnoListOld) {
                if (!turnoListNew.contains(turnoListOldTurno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Turno " + turnoListOldTurno + " since its actividad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Turno> attachedTurnoListNew = new ArrayList<Turno>();
            for (Turno turnoListNewTurnoToAttach : turnoListNew) {
                turnoListNewTurnoToAttach = em.getReference(turnoListNewTurnoToAttach.getClass(), turnoListNewTurnoToAttach.getTurnoPK());
                attachedTurnoListNew.add(turnoListNewTurnoToAttach);
            }
            turnoListNew = attachedTurnoListNew;
            actividad.setTurnoList(turnoListNew);
            actividad = em.merge(actividad);
            for (Turno turnoListNewTurno : turnoListNew) {
                if (!turnoListOld.contains(turnoListNewTurno)) {
                    Actividad oldActividadOfTurnoListNewTurno = turnoListNewTurno.getActividad();
                    turnoListNewTurno.setActividad(actividad);
                    turnoListNewTurno = em.merge(turnoListNewTurno);
                    if (oldActividadOfTurnoListNewTurno != null && !oldActividadOfTurnoListNewTurno.equals(actividad)) {
                        oldActividadOfTurnoListNewTurno.getTurnoList().remove(turnoListNewTurno);
                        oldActividadOfTurnoListNewTurno = em.merge(oldActividadOfTurnoListNewTurno);
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
                String id = actividad.getCodigo();
                if (findActividad(id) == null) {
                    throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.");
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
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Turno> turnoListOrphanCheck = actividad.getTurnoList();
            for (Turno turnoListOrphanCheckTurno : turnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Actividad (" + actividad + ") cannot be destroyed since the Turno " + turnoListOrphanCheckTurno + " in its turnoList field has a non-nullable actividad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(actividad);
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

    public List<Actividad> findActividadEntities() {
        return findActividadEntities(true, -1, -1);
    }

    public List<Actividad> findActividadEntities(int maxResults, int firstResult) {
        return findActividadEntities(false, maxResults, firstResult);
    }

    private List<Actividad> findActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividad.class));
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

    public Actividad findActividad(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividad> rt = cq.from(Actividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
