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
import org.sghweb.jpa.Receta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Orden;

/**
 *
 * @author Roberto
 */
public class OrdenJpaController implements Serializable {

    public OrdenJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orden orden) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (orden.getRecetaList() == null) {
            orden.setRecetaList(new ArrayList<Receta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Receta> attachedRecetaList = new ArrayList<Receta>();
            for (Receta recetaListRecetaToAttach : orden.getRecetaList()) {
                recetaListRecetaToAttach = em.getReference(recetaListRecetaToAttach.getClass(), recetaListRecetaToAttach.getRecetaPK());
                attachedRecetaList.add(recetaListRecetaToAttach);
            }
            orden.setRecetaList(attachedRecetaList);
            em.persist(orden);
            for (Receta recetaListReceta : orden.getRecetaList()) {
                Orden oldOrdenOfRecetaListReceta = recetaListReceta.getOrden();
                recetaListReceta.setOrden(orden);
                recetaListReceta = em.merge(recetaListReceta);
                if (oldOrdenOfRecetaListReceta != null) {
                    oldOrdenOfRecetaListReceta.getRecetaList().remove(recetaListReceta);
                    oldOrdenOfRecetaListReceta = em.merge(oldOrdenOfRecetaListReceta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findOrden(orden.getNroOrden()) != null) {
                throw new PreexistingEntityException("Orden " + orden + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orden orden) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orden persistentOrden = em.find(Orden.class, orden.getNroOrden());
            List<Receta> recetaListOld = persistentOrden.getRecetaList();
            List<Receta> recetaListNew = orden.getRecetaList();
            List<String> illegalOrphanMessages = null;
            for (Receta recetaListOldReceta : recetaListOld) {
                if (!recetaListNew.contains(recetaListOldReceta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Receta " + recetaListOldReceta + " since its orden field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Receta> attachedRecetaListNew = new ArrayList<Receta>();
            for (Receta recetaListNewRecetaToAttach : recetaListNew) {
                recetaListNewRecetaToAttach = em.getReference(recetaListNewRecetaToAttach.getClass(), recetaListNewRecetaToAttach.getRecetaPK());
                attachedRecetaListNew.add(recetaListNewRecetaToAttach);
            }
            recetaListNew = attachedRecetaListNew;
            orden.setRecetaList(recetaListNew);
            orden = em.merge(orden);
            for (Receta recetaListNewReceta : recetaListNew) {
                if (!recetaListOld.contains(recetaListNewReceta)) {
                    Orden oldOrdenOfRecetaListNewReceta = recetaListNewReceta.getOrden();
                    recetaListNewReceta.setOrden(orden);
                    recetaListNewReceta = em.merge(recetaListNewReceta);
                    if (oldOrdenOfRecetaListNewReceta != null && !oldOrdenOfRecetaListNewReceta.equals(orden)) {
                        oldOrdenOfRecetaListNewReceta.getRecetaList().remove(recetaListNewReceta);
                        oldOrdenOfRecetaListNewReceta = em.merge(oldOrdenOfRecetaListNewReceta);
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
                String id = orden.getNroOrden();
                if (findOrden(id) == null) {
                    throw new NonexistentEntityException("The orden with id " + id + " no longer exists.");
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
            Orden orden;
            try {
                orden = em.getReference(Orden.class, id);
                orden.getNroOrden();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orden with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Receta> recetaListOrphanCheck = orden.getRecetaList();
            for (Receta recetaListOrphanCheckReceta : recetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orden (" + orden + ") cannot be destroyed since the Receta " + recetaListOrphanCheckReceta + " in its recetaList field has a non-nullable orden field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(orden);
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

    public List<Orden> findOrdenEntities() {
        return findOrdenEntities(true, -1, -1);
    }

    public List<Orden> findOrdenEntities(int maxResults, int firstResult) {
        return findOrdenEntities(false, maxResults, firstResult);
    }

    private List<Orden> findOrdenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orden.class));
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

    public Orden findOrden(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orden.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orden> rt = cq.from(Orden.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
