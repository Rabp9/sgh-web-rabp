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
import org.sghweb.jpa.Citt;
import org.sghweb.jpa.CittPK;
import org.sghweb.jpa.Detallehistoriaclinica;

/**
 *
 * @author Roberto
 */
public class CittJpaController implements Serializable {

    public CittJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Citt citt) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (citt.getCittPK() == null) {
            citt.setCittPK(new CittPK());
        }
        citt.getCittPK().setDetalleHistoriaClinicaCitaactoMedico(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaactoMedico());
        citt.getCittPK().setDetalleHistoriaClinicaHistoriaClinicanumeroRegistro(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicanumeroRegistro());
        citt.getCittPK().setDetalleHistoriaClinicaCitaMedicocmp(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicocmp());
        citt.getCittPK().setDetalleHistoriaClinicaCitaMedicodni(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicodni());
        citt.getCittPK().setDetalleHistoriaClinicaidDetalleHistoriaClinica(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getIdDetalleHistoriaClinica());
        citt.getCittPK().setDetalleHistoriaClinicaHistoriaClinicaPacientedni(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaPacientedni());
        citt.getCittPK().setDetalleHistoriaClinicaHistoriaClinicaautogenerado(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaautogenerado());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallehistoriaclinica detallehistoriaclinica = citt.getDetallehistoriaclinica();
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica = em.getReference(detallehistoriaclinica.getClass(), detallehistoriaclinica.getDetallehistoriaclinicaPK());
                citt.setDetallehistoriaclinica(detallehistoriaclinica);
            }
            em.persist(citt);
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica.getCittList().add(citt);
                detallehistoriaclinica = em.merge(detallehistoriaclinica);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCitt(citt.getCittPK()) != null) {
                throw new PreexistingEntityException("Citt " + citt + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Citt citt) throws NonexistentEntityException, RollbackFailureException, Exception {
        citt.getCittPK().setDetalleHistoriaClinicaCitaactoMedico(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaactoMedico());
        citt.getCittPK().setDetalleHistoriaClinicaHistoriaClinicanumeroRegistro(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicanumeroRegistro());
        citt.getCittPK().setDetalleHistoriaClinicaCitaMedicocmp(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicocmp());
        citt.getCittPK().setDetalleHistoriaClinicaCitaMedicodni(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicodni());
        citt.getCittPK().setDetalleHistoriaClinicaidDetalleHistoriaClinica(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getIdDetalleHistoriaClinica());
        citt.getCittPK().setDetalleHistoriaClinicaHistoriaClinicaPacientedni(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaPacientedni());
        citt.getCittPK().setDetalleHistoriaClinicaHistoriaClinicaautogenerado(citt.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaautogenerado());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Citt persistentCitt = em.find(Citt.class, citt.getCittPK());
            Detallehistoriaclinica detallehistoriaclinicaOld = persistentCitt.getDetallehistoriaclinica();
            Detallehistoriaclinica detallehistoriaclinicaNew = citt.getDetallehistoriaclinica();
            if (detallehistoriaclinicaNew != null) {
                detallehistoriaclinicaNew = em.getReference(detallehistoriaclinicaNew.getClass(), detallehistoriaclinicaNew.getDetallehistoriaclinicaPK());
                citt.setDetallehistoriaclinica(detallehistoriaclinicaNew);
            }
            citt = em.merge(citt);
            if (detallehistoriaclinicaOld != null && !detallehistoriaclinicaOld.equals(detallehistoriaclinicaNew)) {
                detallehistoriaclinicaOld.getCittList().remove(citt);
                detallehistoriaclinicaOld = em.merge(detallehistoriaclinicaOld);
            }
            if (detallehistoriaclinicaNew != null && !detallehistoriaclinicaNew.equals(detallehistoriaclinicaOld)) {
                detallehistoriaclinicaNew.getCittList().add(citt);
                detallehistoriaclinicaNew = em.merge(detallehistoriaclinicaNew);
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
                CittPK id = citt.getCittPK();
                if (findCitt(id) == null) {
                    throw new NonexistentEntityException("The citt with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CittPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Citt citt;
            try {
                citt = em.getReference(Citt.class, id);
                citt.getCittPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The citt with id " + id + " no longer exists.", enfe);
            }
            Detallehistoriaclinica detallehistoriaclinica = citt.getDetallehistoriaclinica();
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica.getCittList().remove(citt);
                detallehistoriaclinica = em.merge(detallehistoriaclinica);
            }
            em.remove(citt);
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

    public List<Citt> findCittEntities() {
        return findCittEntities(true, -1, -1);
    }

    public List<Citt> findCittEntities(int maxResults, int firstResult) {
        return findCittEntities(false, maxResults, firstResult);
    }

    private List<Citt> findCittEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Citt.class));
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

    public Citt findCitt(CittPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Citt.class, id);
        } finally {
            em.close();
        }
    }

    public int getCittCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Citt> rt = cq.from(Citt.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
