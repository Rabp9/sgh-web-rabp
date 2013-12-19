/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.VwReportereceta;

/**
 *
 * @author Roberto
 */
public class VwReporterecetaJpaController implements Serializable {

    public VwReporterecetaJpaController(UserTransaction utx, EntityManagerFactory emf) {
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

    public void create(VwReportereceta vwReportereceta) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(vwReportereceta);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVwReportereceta(vwReportereceta.getNroReceta()) != null) {
                throw new PreexistingEntityException("VwReportereceta " + vwReportereceta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwReportereceta vwReportereceta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            vwReportereceta = em.merge(vwReportereceta);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = vwReportereceta.getNroReceta();
                if (findVwReportereceta(id) == null) {
                    throw new NonexistentEntityException("The vwReportereceta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            VwReportereceta vwReportereceta;
            try {
                vwReportereceta = em.getReference(VwReportereceta.class, id);
                vwReportereceta.getNroReceta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwReportereceta with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwReportereceta);
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

    public List<VwReportereceta> findVwReporterecetaEntities() {
        return findVwReporterecetaEntities(true, -1, -1);
    }

    public List<VwReportereceta> findVwReporterecetaEntities(int maxResults, int firstResult) {
        return findVwReporterecetaEntities(false, maxResults, firstResult);
    }

    private List<VwReportereceta> findVwReporterecetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwReportereceta.class));
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

    public VwReportereceta findVwReportereceta(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwReportereceta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwReporterecetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwReportereceta> rt = cq.from(VwReportereceta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     
    public List<VwReportereceta> findVwReporteRecetaByNroOrden(String nroOrden) {
        EntityManager em = getEntityManager();
        return em.createNamedQuery("VwReportereceta.findByNroOrden").setParameter("nroOrden", nroOrden).getResultList();
    }   
}
