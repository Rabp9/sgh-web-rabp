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
import org.sghweb.jpa.Actividad;
import org.sghweb.jpa.Medicamento;

/**
 *
 * @author Roberto
 */
public class MedicamentoJpaController implements Serializable {

    public MedicamentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
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

    public void create(Medicamento medicamento) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (medicamento.getRecetaList() == null) {
            medicamento.setRecetaList(new ArrayList<Receta>());
        }
        EntityManager em = null;  
        Context initCtx = new InitialContext();   
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            List<Receta> attachedRecetaList = new ArrayList<Receta>();
            for (Receta recetaListRecetaToAttach : medicamento.getRecetaList()) {
                recetaListRecetaToAttach = em.getReference(recetaListRecetaToAttach.getClass(), recetaListRecetaToAttach.getRecetaPK());
                attachedRecetaList.add(recetaListRecetaToAttach);
            }
            medicamento.setRecetaList(attachedRecetaList);
            em.persist(medicamento);
            for (Receta recetaListReceta : medicamento.getRecetaList()) {
                Medicamento oldMedicamentoOfRecetaListReceta = recetaListReceta.getMedicamento();
                recetaListReceta.setMedicamento(medicamento);
                recetaListReceta = em.merge(recetaListReceta);
                if (oldMedicamentoOfRecetaListReceta != null) {
                    oldMedicamentoOfRecetaListReceta.getRecetaList().remove(recetaListReceta);
                    oldMedicamentoOfRecetaListReceta = em.merge(oldMedicamentoOfRecetaListReceta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                if (findMedicamento(medicamento.getCodigo()) != null) {
                    throw new PreexistingEntityException("Medicamento " + medicamento + " already exists.", ex);
                }
                if(!findMedicamentoByDescripcion(medicamento.getDescripcion()).isEmpty())
                    throw new PreexistingEntityException("Medicamento " + medicamento + " already exists.", ex);
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medicamento medicamento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;  
        Context initCtx = new InitialContext();   
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento persistentMedicamento = em.find(Medicamento.class, medicamento.getCodigo());
            List<Receta> recetaListOld = persistentMedicamento.getRecetaList();
            List<Receta> recetaListNew = medicamento.getRecetaList();
            List<String> illegalOrphanMessages = null;
            for (Receta recetaListOldReceta : recetaListOld) {
                if (!recetaListNew.contains(recetaListOldReceta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Receta " + recetaListOldReceta + " since its medicamento field is not nullable.");
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
            medicamento.setRecetaList(recetaListNew);
            medicamento = em.merge(medicamento);
            for (Receta recetaListNewReceta : recetaListNew) {
                if (!recetaListOld.contains(recetaListNewReceta)) {
                    Medicamento oldMedicamentoOfRecetaListNewReceta = recetaListNewReceta.getMedicamento();
                    recetaListNewReceta.setMedicamento(medicamento);
                    recetaListNewReceta = em.merge(recetaListNewReceta);
                    if (oldMedicamentoOfRecetaListNewReceta != null && !oldMedicamentoOfRecetaListNewReceta.equals(medicamento)) {
                        oldMedicamentoOfRecetaListNewReceta.getRecetaList().remove(recetaListNewReceta);
                        oldMedicamentoOfRecetaListNewReceta = em.merge(oldMedicamentoOfRecetaListNewReceta);
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
                String id = medicamento.getCodigo();
                if (findMedicamento(id) == null) {
                    throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.");
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
        Context initCtx = new InitialContext();   
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento medicamento;
            try {
                medicamento = em.getReference(Medicamento.class, id);
                medicamento.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Receta> recetaListOrphanCheck = medicamento.getRecetaList();
            for (Receta recetaListOrphanCheckReceta : recetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicamento (" + medicamento + ") cannot be destroyed since the Receta " + recetaListOrphanCheckReceta + " in its recetaList field has a non-nullable medicamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(medicamento);
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

    public List<Medicamento> findMedicamentoEntities() {
        return findMedicamentoEntities(true, -1, -1);
    }

    public List<Medicamento> findMedicamentoEntities(int maxResults, int firstResult) {
        return findMedicamentoEntities(false, maxResults, firstResult);
    }

    private List<Medicamento> findMedicamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicamento.class));
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

    public Medicamento findMedicamento(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicamento> rt = cq.from(Medicamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     
    public List<Medicamento> findMedicamentoByDescripcion(String descripcion) {
        EntityManager em = getEntityManager();
        return em.createNamedQuery("Medicamento.findByDescripcion").setParameter("descripcion", descripcion).getResultList();
    }   
}
