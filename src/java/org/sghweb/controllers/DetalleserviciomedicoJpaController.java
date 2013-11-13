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
import org.sghweb.jpa.DetalleserviciomedicoPK;
import org.sghweb.jpa.Medico;
import org.sghweb.jpa.Servicio;

/**
 *
 * @author Roberto
 */
public class DetalleserviciomedicoJpaController implements Serializable {

    public DetalleserviciomedicoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleserviciomedico detalleserviciomedico) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (detalleserviciomedico.getDetalleserviciomedicoPK() == null) {
            detalleserviciomedico.setDetalleserviciomedicoPK(new DetalleserviciomedicoPK());
        }
        detalleserviciomedico.getDetalleserviciomedicoPK().setMedicocmp(detalleserviciomedico.getMedico().getMedicoPK().getCmp());
        detalleserviciomedico.getDetalleserviciomedicoPK().setMedicodni(detalleserviciomedico.getMedico().getMedicoPK().getDni());
        detalleserviciomedico.getDetalleserviciomedicoPK().setServiciocodigo(detalleserviciomedico.getServicio().getCodigo());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medico medico = detalleserviciomedico.getMedico();
            if (medico != null) {
                medico = em.getReference(medico.getClass(), medico.getMedicoPK());
                detalleserviciomedico.setMedico(medico);
            }
            Servicio servicio = detalleserviciomedico.getServicio();
            if (servicio != null) {
                servicio = em.getReference(servicio.getClass(), servicio.getCodigo());
                detalleserviciomedico.setServicio(servicio);
            }
            em.persist(detalleserviciomedico);
            if (medico != null) {
                medico.getDetalleserviciomedicoList().add(detalleserviciomedico);
                medico = em.merge(medico);
            }
            if (servicio != null) {
                servicio.getDetalleserviciomedicoList().add(detalleserviciomedico);
                servicio = em.merge(servicio);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleserviciomedico(detalleserviciomedico.getDetalleserviciomedicoPK()) != null) {
                throw new PreexistingEntityException("Detalleserviciomedico " + detalleserviciomedico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleserviciomedico detalleserviciomedico) throws NonexistentEntityException, RollbackFailureException, Exception {
        detalleserviciomedico.getDetalleserviciomedicoPK().setMedicocmp(detalleserviciomedico.getMedico().getMedicoPK().getCmp());
        detalleserviciomedico.getDetalleserviciomedicoPK().setMedicodni(detalleserviciomedico.getMedico().getMedicoPK().getDni());
        detalleserviciomedico.getDetalleserviciomedicoPK().setServiciocodigo(detalleserviciomedico.getServicio().getCodigo());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleserviciomedico persistentDetalleserviciomedico = em.find(Detalleserviciomedico.class, detalleserviciomedico.getDetalleserviciomedicoPK());
            Medico medicoOld = persistentDetalleserviciomedico.getMedico();
            Medico medicoNew = detalleserviciomedico.getMedico();
            Servicio servicioOld = persistentDetalleserviciomedico.getServicio();
            Servicio servicioNew = detalleserviciomedico.getServicio();
            if (medicoNew != null) {
                medicoNew = em.getReference(medicoNew.getClass(), medicoNew.getMedicoPK());
                detalleserviciomedico.setMedico(medicoNew);
            }
            if (servicioNew != null) {
                servicioNew = em.getReference(servicioNew.getClass(), servicioNew.getCodigo());
                detalleserviciomedico.setServicio(servicioNew);
            }
            detalleserviciomedico = em.merge(detalleserviciomedico);
            if (medicoOld != null && !medicoOld.equals(medicoNew)) {
                medicoOld.getDetalleserviciomedicoList().remove(detalleserviciomedico);
                medicoOld = em.merge(medicoOld);
            }
            if (medicoNew != null && !medicoNew.equals(medicoOld)) {
                medicoNew.getDetalleserviciomedicoList().add(detalleserviciomedico);
                medicoNew = em.merge(medicoNew);
            }
            if (servicioOld != null && !servicioOld.equals(servicioNew)) {
                servicioOld.getDetalleserviciomedicoList().remove(detalleserviciomedico);
                servicioOld = em.merge(servicioOld);
            }
            if (servicioNew != null && !servicioNew.equals(servicioOld)) {
                servicioNew.getDetalleserviciomedicoList().add(detalleserviciomedico);
                servicioNew = em.merge(servicioNew);
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
                DetalleserviciomedicoPK id = detalleserviciomedico.getDetalleserviciomedicoPK();
                if (findDetalleserviciomedico(id) == null) {
                    throw new NonexistentEntityException("The detalleserviciomedico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetalleserviciomedicoPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleserviciomedico detalleserviciomedico;
            try {
                detalleserviciomedico = em.getReference(Detalleserviciomedico.class, id);
                detalleserviciomedico.getDetalleserviciomedicoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleserviciomedico with id " + id + " no longer exists.", enfe);
            }
            Medico medico = detalleserviciomedico.getMedico();
            if (medico != null) {
                medico.getDetalleserviciomedicoList().remove(detalleserviciomedico);
                medico = em.merge(medico);
            }
            Servicio servicio = detalleserviciomedico.getServicio();
            if (servicio != null) {
                servicio.getDetalleserviciomedicoList().remove(detalleserviciomedico);
                servicio = em.merge(servicio);
            }
            em.remove(detalleserviciomedico);
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

    public List<Detalleserviciomedico> findDetalleserviciomedicoEntities() {
        return findDetalleserviciomedicoEntities(true, -1, -1);
    }

    public List<Detalleserviciomedico> findDetalleserviciomedicoEntities(int maxResults, int firstResult) {
        return findDetalleserviciomedicoEntities(false, maxResults, firstResult);
    }

    private List<Detalleserviciomedico> findDetalleserviciomedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleserviciomedico.class));
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

    public Detalleserviciomedico findDetalleserviciomedico(DetalleserviciomedicoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleserviciomedico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleserviciomedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleserviciomedico> rt = cq.from(Detalleserviciomedico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
