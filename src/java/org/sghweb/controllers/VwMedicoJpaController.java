/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.controllers;

import java.io.Serializable;
import java.util.Date;
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
import org.sghweb.jpa.VwMedico;
import org.sghweb.jpa.VwOrden;

/**
 *
 * @author essalud
 */
public class VwMedicoJpaController implements Serializable {

    public VwMedicoJpaController(UserTransaction utx, EntityManagerFactory emf) {
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

    public void create(VwMedico vwMedico) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(vwMedico);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVwMedico(vwMedico.getCmp()) != null) {
                throw new PreexistingEntityException("VwMedico " + vwMedico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwMedico vwMedico) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            vwMedico = em.merge(vwMedico);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = vwMedico.getCmp();
                if (findVwMedico(id) == null) {
                    throw new NonexistentEntityException("The vwMedico with id " + id + " no longer exists.");
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
            VwMedico vwMedico;
            try {
                vwMedico = em.getReference(VwMedico.class, id);
                vwMedico.getCmp();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwMedico with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwMedico);
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

    public List<VwMedico> findVwMedicoEntities() {
        return findVwMedicoEntities(true, -1, -1);
    }

    public List<VwMedico> findVwMedicoEntities(int maxResults, int firstResult) {
        return findVwMedicoEntities(false, maxResults, firstResult);
    }

    private List<VwMedico> findVwMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwMedico.class));
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

    public VwMedico findVwMedico(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwMedico.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwMedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwMedico> rt = cq.from(VwMedico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
   
    public List<VwMedico> findVwMedicoByServicio(String servicio) {
        EntityManager em = getEntityManager();
        return em.createNamedQuery("VwMedico.findByServicio").setParameter("servicio", servicio).getResultList();
    }   
}
