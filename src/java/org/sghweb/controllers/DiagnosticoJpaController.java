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
import org.sghweb.jpa.Detallediagnostico;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Diagnostico;

/**
 *
 * @author Roberto
 */
public class DiagnosticoJpaController implements Serializable {

    public DiagnosticoJpaController(UserTransaction utx, EntityManagerFactory emf) {
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

    public void create(Diagnostico diagnostico) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (diagnostico.getDetallediagnosticoList() == null) {
            diagnostico.setDetallediagnosticoList(new ArrayList<Detallediagnostico>());
        }
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            List<Detallediagnostico> attachedDetallediagnosticoList = new ArrayList<Detallediagnostico>();
            for (Detallediagnostico detallediagnosticoListDetallediagnosticoToAttach : diagnostico.getDetallediagnosticoList()) {
                detallediagnosticoListDetallediagnosticoToAttach = em.getReference(detallediagnosticoListDetallediagnosticoToAttach.getClass(), detallediagnosticoListDetallediagnosticoToAttach.getDetallediagnosticoPK());
                attachedDetallediagnosticoList.add(detallediagnosticoListDetallediagnosticoToAttach);
            }
            diagnostico.setDetallediagnosticoList(attachedDetallediagnosticoList);
            em.persist(diagnostico);
            for (Detallediagnostico detallediagnosticoListDetallediagnostico : diagnostico.getDetallediagnosticoList()) {
                Diagnostico oldDiagnosticoOfDetallediagnosticoListDetallediagnostico = detallediagnosticoListDetallediagnostico.getDiagnostico();
                detallediagnosticoListDetallediagnostico.setDiagnostico(diagnostico);
                detallediagnosticoListDetallediagnostico = em.merge(detallediagnosticoListDetallediagnostico);
                if (oldDiagnosticoOfDetallediagnosticoListDetallediagnostico != null) {
                    oldDiagnosticoOfDetallediagnosticoListDetallediagnostico.getDetallediagnosticoList().remove(detallediagnosticoListDetallediagnostico);
                    oldDiagnosticoOfDetallediagnosticoListDetallediagnostico = em.merge(oldDiagnosticoOfDetallediagnosticoListDetallediagnostico);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                if (findDiagnostico(diagnostico.getCodigo()) != null) {
                    throw new PreexistingEntityException("Diagnostico " + diagnostico + " already exists.", ex);
                }
                if(!findDiagnosticoByDescripcion(diagnostico.getDescripcion()).isEmpty())
                    throw new PreexistingEntityException("Diagnostico " + diagnostico + " already exists.", ex);
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
               throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Diagnostico diagnostico) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Diagnostico persistentDiagnostico = em.find(Diagnostico.class, diagnostico.getCodigo());
            List<Detallediagnostico> detallediagnosticoListOld = persistentDiagnostico.getDetallediagnosticoList();
            List<Detallediagnostico> detallediagnosticoListNew = diagnostico.getDetallediagnosticoList();
            List<String> illegalOrphanMessages = null;
            for (Detallediagnostico detallediagnosticoListOldDetallediagnostico : detallediagnosticoListOld) {
                if (!detallediagnosticoListNew.contains(detallediagnosticoListOldDetallediagnostico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallediagnostico " + detallediagnosticoListOldDetallediagnostico + " since its diagnostico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Detallediagnostico> attachedDetallediagnosticoListNew = new ArrayList<Detallediagnostico>();
            for (Detallediagnostico detallediagnosticoListNewDetallediagnosticoToAttach : detallediagnosticoListNew) {
                detallediagnosticoListNewDetallediagnosticoToAttach = em.getReference(detallediagnosticoListNewDetallediagnosticoToAttach.getClass(), detallediagnosticoListNewDetallediagnosticoToAttach.getDetallediagnosticoPK());
                attachedDetallediagnosticoListNew.add(detallediagnosticoListNewDetallediagnosticoToAttach);
            }
            detallediagnosticoListNew = attachedDetallediagnosticoListNew;
            diagnostico.setDetallediagnosticoList(detallediagnosticoListNew);
            diagnostico = em.merge(diagnostico);
            for (Detallediagnostico detallediagnosticoListNewDetallediagnostico : detallediagnosticoListNew) {
                if (!detallediagnosticoListOld.contains(detallediagnosticoListNewDetallediagnostico)) {
                    Diagnostico oldDiagnosticoOfDetallediagnosticoListNewDetallediagnostico = detallediagnosticoListNewDetallediagnostico.getDiagnostico();
                    detallediagnosticoListNewDetallediagnostico.setDiagnostico(diagnostico);
                    detallediagnosticoListNewDetallediagnostico = em.merge(detallediagnosticoListNewDetallediagnostico);
                    if (oldDiagnosticoOfDetallediagnosticoListNewDetallediagnostico != null && !oldDiagnosticoOfDetallediagnosticoListNewDetallediagnostico.equals(diagnostico)) {
                        oldDiagnosticoOfDetallediagnosticoListNewDetallediagnostico.getDetallediagnosticoList().remove(detallediagnosticoListNewDetallediagnostico);
                        oldDiagnosticoOfDetallediagnosticoListNewDetallediagnostico = em.merge(oldDiagnosticoOfDetallediagnosticoListNewDetallediagnostico);
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
                String id = diagnostico.getCodigo();
                if (findDiagnostico(id) == null) {
                    throw new NonexistentEntityException("The diagnostico with id " + id + " no longer exists.");
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
            Diagnostico diagnostico;
            try {
                diagnostico = em.getReference(Diagnostico.class, id);
                diagnostico.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diagnostico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallediagnostico> detallediagnosticoListOrphanCheck = diagnostico.getDetallediagnosticoList();
            for (Detallediagnostico detallediagnosticoListOrphanCheckDetallediagnostico : detallediagnosticoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Diagnostico (" + diagnostico + ") cannot be destroyed since the Detallediagnostico " + detallediagnosticoListOrphanCheckDetallediagnostico + " in its detallediagnosticoList field has a non-nullable diagnostico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(diagnostico);
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

    public List<Diagnostico> findDiagnosticoEntities() {
        return findDiagnosticoEntities(true, -1, -1);
    }

    public List<Diagnostico> findDiagnosticoEntities(int maxResults, int firstResult) {
        return findDiagnosticoEntities(false, maxResults, firstResult);
    }

    private List<Diagnostico> findDiagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Diagnostico.class));
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

    public Diagnostico findDiagnostico(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Diagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Diagnostico> rt = cq.from(Diagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
        
    public List<Diagnostico> findDiagnosticoByDescripcion(String descripcion) {
        EntityManager em = getEntityManager();
        return em.createNamedQuery("Diagnostico.findByDescripcion").setParameter("descripcion", descripcion).getResultList();
    }   
}
