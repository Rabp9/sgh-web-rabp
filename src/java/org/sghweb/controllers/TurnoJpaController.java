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
import org.sghweb.jpa.Detalleserviciomedico;
import org.sghweb.jpa.Actividad;
import org.sghweb.jpa.Turno;
import org.sghweb.jpa.TurnoPK;

/**
 *
 * @author Roberto
 */
public class TurnoJpaController implements Serializable {

    public TurnoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Turno turno) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (turno.getTurnoPK() == null) {
            turno.setTurnoPK(new TurnoPK());
        }
        turno.getTurnoPK().setDetalleServicioMedicoidDetalleServicioMedico(turno.getDetalleserviciomedico().getDetalleserviciomedicoPK().getIdDetalleServicioMedico());
        turno.getTurnoPK().setDetalleServicioMedicoServiciocodigo(turno.getDetalleserviciomedico().getDetalleserviciomedicoPK().getServiciocodigo());
        turno.getTurnoPK().setDetalleServicioMedicoMedicocmp(turno.getDetalleserviciomedico().getDetalleserviciomedicoPK().getMedicocmp());
        turno.getTurnoPK().setActividadcodigo(turno.getActividad().getCodigo());
        turno.getTurnoPK().setDetalleServicioMedicoMedicodni(turno.getDetalleserviciomedico().getDetalleserviciomedicoPK().getMedicodni());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleserviciomedico detalleserviciomedico = turno.getDetalleserviciomedico();
            if (detalleserviciomedico != null) {
                detalleserviciomedico = em.getReference(detalleserviciomedico.getClass(), detalleserviciomedico.getDetalleserviciomedicoPK());
                turno.setDetalleserviciomedico(detalleserviciomedico);
            }
            Actividad actividad = turno.getActividad();
            if (actividad != null) {
                actividad = em.getReference(actividad.getClass(), actividad.getCodigo());
                turno.setActividad(actividad);
            }
            em.persist(turno);
            if (detalleserviciomedico != null) {
                detalleserviciomedico.getTurnoList().add(turno);
                detalleserviciomedico = em.merge(detalleserviciomedico);
            }
            if (actividad != null) {
                actividad.getTurnoList().add(turno);
                actividad = em.merge(actividad);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTurno(turno.getTurnoPK()) != null) {
                throw new PreexistingEntityException("Turno " + turno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Turno turno) throws NonexistentEntityException, RollbackFailureException, Exception {
        turno.getTurnoPK().setDetalleServicioMedicoidDetalleServicioMedico(turno.getDetalleserviciomedico().getDetalleserviciomedicoPK().getIdDetalleServicioMedico());
        turno.getTurnoPK().setDetalleServicioMedicoServiciocodigo(turno.getDetalleserviciomedico().getDetalleserviciomedicoPK().getServiciocodigo());
        turno.getTurnoPK().setDetalleServicioMedicoMedicocmp(turno.getDetalleserviciomedico().getDetalleserviciomedicoPK().getMedicocmp());
        turno.getTurnoPK().setActividadcodigo(turno.getActividad().getCodigo());
        turno.getTurnoPK().setDetalleServicioMedicoMedicodni(turno.getDetalleserviciomedico().getDetalleserviciomedicoPK().getMedicodni());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Turno persistentTurno = em.find(Turno.class, turno.getTurnoPK());
            Detalleserviciomedico detalleserviciomedicoOld = persistentTurno.getDetalleserviciomedico();
            Detalleserviciomedico detalleserviciomedicoNew = turno.getDetalleserviciomedico();
            Actividad actividadOld = persistentTurno.getActividad();
            Actividad actividadNew = turno.getActividad();
            if (detalleserviciomedicoNew != null) {
                detalleserviciomedicoNew = em.getReference(detalleserviciomedicoNew.getClass(), detalleserviciomedicoNew.getDetalleserviciomedicoPK());
                turno.setDetalleserviciomedico(detalleserviciomedicoNew);
            }
            if (actividadNew != null) {
                actividadNew = em.getReference(actividadNew.getClass(), actividadNew.getCodigo());
                turno.setActividad(actividadNew);
            }
            turno = em.merge(turno);
            if (detalleserviciomedicoOld != null && !detalleserviciomedicoOld.equals(detalleserviciomedicoNew)) {
                detalleserviciomedicoOld.getTurnoList().remove(turno);
                detalleserviciomedicoOld = em.merge(detalleserviciomedicoOld);
            }
            if (detalleserviciomedicoNew != null && !detalleserviciomedicoNew.equals(detalleserviciomedicoOld)) {
                detalleserviciomedicoNew.getTurnoList().add(turno);
                detalleserviciomedicoNew = em.merge(detalleserviciomedicoNew);
            }
            if (actividadOld != null && !actividadOld.equals(actividadNew)) {
                actividadOld.getTurnoList().remove(turno);
                actividadOld = em.merge(actividadOld);
            }
            if (actividadNew != null && !actividadNew.equals(actividadOld)) {
                actividadNew.getTurnoList().add(turno);
                actividadNew = em.merge(actividadNew);
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
                TurnoPK id = turno.getTurnoPK();
                if (findTurno(id) == null) {
                    throw new NonexistentEntityException("The turno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TurnoPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Turno turno;
            try {
                turno = em.getReference(Turno.class, id);
                turno.getTurnoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The turno with id " + id + " no longer exists.", enfe);
            }
            Detalleserviciomedico detalleserviciomedico = turno.getDetalleserviciomedico();
            if (detalleserviciomedico != null) {
                detalleserviciomedico.getTurnoList().remove(turno);
                detalleserviciomedico = em.merge(detalleserviciomedico);
            }
            Actividad actividad = turno.getActividad();
            if (actividad != null) {
                actividad.getTurnoList().remove(turno);
                actividad = em.merge(actividad);
            }
            em.remove(turno);
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

    public List<Turno> findTurnoEntities() {
        return findTurnoEntities(true, -1, -1);
    }

    public List<Turno> findTurnoEntities(int maxResults, int firstResult) {
        return findTurnoEntities(false, maxResults, firstResult);
    }

    private List<Turno> findTurnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Turno.class));
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

    public Turno findTurno(TurnoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Turno.class, id);
        } finally {
            em.close();
        }
    }

    public int getTurnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Turno> rt = cq.from(Turno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
