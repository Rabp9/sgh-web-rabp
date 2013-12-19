/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import org.sghweb.jpa.VwCitaAtender;

/**
 *
 * @author Roberto
 */
public class VwCitaAtenderJpaController implements Serializable {

    public VwCitaAtenderJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    
    public void create(VwCitaAtender vwCitaAtender) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(vwCitaAtender);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVwCitaAtender(vwCitaAtender.getActoMedico()) != null) {
                throw new PreexistingEntityException("VwCitaAtender " + vwCitaAtender + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwCitaAtender vwCitaAtender) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            vwCitaAtender = em.merge(vwCitaAtender);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = vwCitaAtender.getActoMedico();
                if (findVwCitaAtender(id) == null) {
                    throw new NonexistentEntityException("The vwCitaAtender with id " + id + " no longer exists.");
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
            VwCitaAtender vwCitaAtender;
            try {
                vwCitaAtender = em.getReference(VwCitaAtender.class, id);
                vwCitaAtender.getActoMedico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwCitaAtender with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwCitaAtender);
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

    public List<VwCitaAtender> findVwCitaAtenderEntities() {
        return findVwCitaAtenderEntities(true, -1, -1);
    }

    public List<VwCitaAtender> findVwCitaAtenderEntities(int maxResults, int firstResult) {
        return findVwCitaAtenderEntities(false, maxResults, firstResult);
    }

    private List<VwCitaAtender> findVwCitaAtenderEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwCitaAtender.class));
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

    public VwCitaAtender findVwCitaAtender(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwCitaAtender.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwCitaAtenderCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwCitaAtender> rt = cq.from(VwCitaAtender.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
      
    public List<VwCitaAtender> findVwCitaAtenderbyCmp(String cmp, Date fecha) {
        EntityManager em = getEntityManager();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return em.createNamedQuery("VwCitaAtender.findByCmpYFecha").setParameter("cmp", cmp).setParameter("fechaHora", sdf.format(fecha) + "%").getResultList();
    }  
}
