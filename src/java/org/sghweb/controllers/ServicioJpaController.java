// src/java/org/sghweb/controllers/ServicioJpaController.java

package org.sghweb.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.sghweb.jpa.Detalleserviciomedico;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Servicio;

/**
 *
 * @author essalud
 */
public class ServicioJpaController implements Serializable {

    public ServicioJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Servicio servicio) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (servicio.getDetalleserviciomedicoList() == null) {
            servicio.setDetalleserviciomedicoList(new ArrayList<Detalleserviciomedico>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Detalleserviciomedico> attachedDetalleserviciomedicoList = new ArrayList<Detalleserviciomedico>();
            for (Detalleserviciomedico detalleserviciomedicoListDetalleserviciomedicoToAttach : servicio.getDetalleserviciomedicoList()) {
                detalleserviciomedicoListDetalleserviciomedicoToAttach = em.getReference(detalleserviciomedicoListDetalleserviciomedicoToAttach.getClass(), detalleserviciomedicoListDetalleserviciomedicoToAttach.getDetalleserviciomedicoPK());
                attachedDetalleserviciomedicoList.add(detalleserviciomedicoListDetalleserviciomedicoToAttach);
            }
            servicio.setDetalleserviciomedicoList(attachedDetalleserviciomedicoList);
            em.persist(servicio);
            for (Detalleserviciomedico detalleserviciomedicoListDetalleserviciomedico : servicio.getDetalleserviciomedicoList()) {
                Servicio oldServicioOfDetalleserviciomedicoListDetalleserviciomedico = detalleserviciomedicoListDetalleserviciomedico.getServicio();
                detalleserviciomedicoListDetalleserviciomedico.setServicio(servicio);
                detalleserviciomedicoListDetalleserviciomedico = em.merge(detalleserviciomedicoListDetalleserviciomedico);
                if (oldServicioOfDetalleserviciomedicoListDetalleserviciomedico != null) {
                    oldServicioOfDetalleserviciomedicoListDetalleserviciomedico.getDetalleserviciomedicoList().remove(detalleserviciomedicoListDetalleserviciomedico);
                    oldServicioOfDetalleserviciomedicoListDetalleserviciomedico = em.merge(oldServicioOfDetalleserviciomedicoListDetalleserviciomedico);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findServicio(servicio.getCodigo()) != null) {
                throw new PreexistingEntityException("Servicio " + servicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Servicio servicio) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Servicio persistentServicio = em.find(Servicio.class, servicio.getCodigo());
            List<Detalleserviciomedico> detalleserviciomedicoListOld = persistentServicio.getDetalleserviciomedicoList();
            List<Detalleserviciomedico> detalleserviciomedicoListNew = servicio.getDetalleserviciomedicoList();
            List<String> illegalOrphanMessages = null;
            for (Detalleserviciomedico detalleserviciomedicoListOldDetalleserviciomedico : detalleserviciomedicoListOld) {
                if (!detalleserviciomedicoListNew.contains(detalleserviciomedicoListOldDetalleserviciomedico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleserviciomedico " + detalleserviciomedicoListOldDetalleserviciomedico + " since its servicio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Detalleserviciomedico> attachedDetalleserviciomedicoListNew = new ArrayList<Detalleserviciomedico>();
            for (Detalleserviciomedico detalleserviciomedicoListNewDetalleserviciomedicoToAttach : detalleserviciomedicoListNew) {
                detalleserviciomedicoListNewDetalleserviciomedicoToAttach = em.getReference(detalleserviciomedicoListNewDetalleserviciomedicoToAttach.getClass(), detalleserviciomedicoListNewDetalleserviciomedicoToAttach.getDetalleserviciomedicoPK());
                attachedDetalleserviciomedicoListNew.add(detalleserviciomedicoListNewDetalleserviciomedicoToAttach);
            }
            detalleserviciomedicoListNew = attachedDetalleserviciomedicoListNew;
            servicio.setDetalleserviciomedicoList(detalleserviciomedicoListNew);
            servicio = em.merge(servicio);
            for (Detalleserviciomedico detalleserviciomedicoListNewDetalleserviciomedico : detalleserviciomedicoListNew) {
                if (!detalleserviciomedicoListOld.contains(detalleserviciomedicoListNewDetalleserviciomedico)) {
                    Servicio oldServicioOfDetalleserviciomedicoListNewDetalleserviciomedico = detalleserviciomedicoListNewDetalleserviciomedico.getServicio();
                    detalleserviciomedicoListNewDetalleserviciomedico.setServicio(servicio);
                    detalleserviciomedicoListNewDetalleserviciomedico = em.merge(detalleserviciomedicoListNewDetalleserviciomedico);
                    if (oldServicioOfDetalleserviciomedicoListNewDetalleserviciomedico != null && !oldServicioOfDetalleserviciomedicoListNewDetalleserviciomedico.equals(servicio)) {
                        oldServicioOfDetalleserviciomedicoListNewDetalleserviciomedico.getDetalleserviciomedicoList().remove(detalleserviciomedicoListNewDetalleserviciomedico);
                        oldServicioOfDetalleserviciomedicoListNewDetalleserviciomedico = em.merge(oldServicioOfDetalleserviciomedicoListNewDetalleserviciomedico);
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
                String id = servicio.getCodigo();
                if (findServicio(id) == null) {
                    throw new NonexistentEntityException("The servicio with id " + id + " no longer exists.");
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
            Servicio servicio;
            try {
                servicio = em.getReference(Servicio.class, id);
                servicio.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleserviciomedico> detalleserviciomedicoListOrphanCheck = servicio.getDetalleserviciomedicoList();
            for (Detalleserviciomedico detalleserviciomedicoListOrphanCheckDetalleserviciomedico : detalleserviciomedicoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Servicio (" + servicio + ") cannot be destroyed since the Detalleserviciomedico " + detalleserviciomedicoListOrphanCheckDetalleserviciomedico + " in its detalleserviciomedicoList field has a non-nullable servicio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(servicio);
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

    public List<Servicio> findServicioEntities() {
        return findServicioEntities(true, -1, -1);
    }

    public List<Servicio> findServicioEntities(int maxResults, int firstResult) {
        return findServicioEntities(false, maxResults, firstResult);
    }

    private List<Servicio> findServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicio.class));
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

    public Servicio findServicio(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servicio> rt = cq.from(Servicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
