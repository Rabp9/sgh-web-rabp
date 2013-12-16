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
import org.sghweb.jpa.VwCita;

/**
 *
 * @author Roberto
 */
public class VwCitaJpaController implements Serializable {

    public VwCitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
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

    public void create(VwCita vwCita) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(vwCita);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVwCita(vwCita.getActoMedico()) != null) {
                throw new PreexistingEntityException("VwCita " + vwCita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwCita vwCita) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            vwCita = em.merge(vwCita);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = vwCita.getActoMedico();
                if (findVwCita(id) == null) {
                    throw new NonexistentEntityException("The vwCita with id " + id + " no longer exists.");
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
            VwCita vwCita;
            try {
                vwCita = em.getReference(VwCita.class, id);
                vwCita.getActoMedico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwCita with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwCita);
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

    public List<VwCita> findVwCitaEntities() {
        return findVwCitaEntities(true, -1, -1);
    }

    public List<VwCita> findVwCitaEntities(int maxResults, int firstResult) {
        return findVwCitaEntities(false, maxResults, firstResult);
    }

    private List<VwCita> findVwCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwCita.class));
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

    public VwCita findVwCita(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwCita.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwCita> rt = cq.from(VwCita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<VwCita> findVwCitaDisponibles() {
        EntityManager em = getEntityManager();
        return em.createNamedQuery("VwCita.findDisponibles").setParameter("fechaHora", new Date()).getResultList();
    } 
     
    public List<VwCita> findVwCitaDisponibles(String cmp) {
        if (cmp == null)
            return findVwCitaDisponibles();
        EntityManager em = getEntityManager();
        return em.createNamedQuery("VwCita.findDisponiblesbyCmp").setParameter("fechaHora", new Date()).setParameter("cmp", cmp).getResultList();
    } 
}
