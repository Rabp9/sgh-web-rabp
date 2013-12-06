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
import org.sghweb.jpa.Orden;
import org.sghweb.jpa.Medicamento;
import org.sghweb.jpa.Receta;
import org.sghweb.jpa.RecetaPK;

/**
 *
 * @author Roberto
 */
public class RecetaJpaController implements Serializable {

    public RecetaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Receta receta) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (receta.getRecetaPK() == null) {
            receta.setRecetaPK(new RecetaPK());
        }
        receta.getRecetaPK().setMedicamentocodigo(receta.getMedicamento().getCodigo());
        receta.getRecetaPK().setOrdennroOrden(receta.getOrden().getNroOrden());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orden orden = receta.getOrden();
            if (orden != null) {
                orden = em.getReference(orden.getClass(), orden.getNroOrden());
                receta.setOrden(orden);
            }
            Medicamento medicamento = receta.getMedicamento();
            if (medicamento != null) {
                medicamento = em.getReference(medicamento.getClass(), medicamento.getCodigo());
                receta.setMedicamento(medicamento);
            }
            em.persist(receta);
            if (orden != null) {
                orden.getRecetaList().add(receta);
                orden = em.merge(orden);
            }
            if (medicamento != null) {
                medicamento.getRecetaList().add(receta);
                medicamento = em.merge(medicamento);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReceta(receta.getRecetaPK()) != null) {
                throw new PreexistingEntityException("Receta " + receta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Receta receta) throws NonexistentEntityException, RollbackFailureException, Exception {
        receta.getRecetaPK().setMedicamentocodigo(receta.getMedicamento().getCodigo());
        receta.getRecetaPK().setOrdennroOrden(receta.getOrden().getNroOrden());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Receta persistentReceta = em.find(Receta.class, receta.getRecetaPK());
            Orden ordenOld = persistentReceta.getOrden();
            Orden ordenNew = receta.getOrden();
            Medicamento medicamentoOld = persistentReceta.getMedicamento();
            Medicamento medicamentoNew = receta.getMedicamento();
            if (ordenNew != null) {
                ordenNew = em.getReference(ordenNew.getClass(), ordenNew.getNroOrden());
                receta.setOrden(ordenNew);
            }
            if (medicamentoNew != null) {
                medicamentoNew = em.getReference(medicamentoNew.getClass(), medicamentoNew.getCodigo());
                receta.setMedicamento(medicamentoNew);
            }
            receta = em.merge(receta);
            if (ordenOld != null && !ordenOld.equals(ordenNew)) {
                ordenOld.getRecetaList().remove(receta);
                ordenOld = em.merge(ordenOld);
            }
            if (ordenNew != null && !ordenNew.equals(ordenOld)) {
                ordenNew.getRecetaList().add(receta);
                ordenNew = em.merge(ordenNew);
            }
            if (medicamentoOld != null && !medicamentoOld.equals(medicamentoNew)) {
                medicamentoOld.getRecetaList().remove(receta);
                medicamentoOld = em.merge(medicamentoOld);
            }
            if (medicamentoNew != null && !medicamentoNew.equals(medicamentoOld)) {
                medicamentoNew.getRecetaList().add(receta);
                medicamentoNew = em.merge(medicamentoNew);
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
                RecetaPK id = receta.getRecetaPK();
                if (findReceta(id) == null) {
                    throw new NonexistentEntityException("The receta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(RecetaPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Receta receta;
            try {
                receta = em.getReference(Receta.class, id);
                receta.getRecetaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The receta with id " + id + " no longer exists.", enfe);
            }
            Orden orden = receta.getOrden();
            if (orden != null) {
                orden.getRecetaList().remove(receta);
                orden = em.merge(orden);
            }
            Medicamento medicamento = receta.getMedicamento();
            if (medicamento != null) {
                medicamento.getRecetaList().remove(receta);
                medicamento = em.merge(medicamento);
            }
            em.remove(receta);
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

    public List<Receta> findRecetaEntities() {
        return findRecetaEntities(true, -1, -1);
    }

    public List<Receta> findRecetaEntities(int maxResults, int firstResult) {
        return findRecetaEntities(false, maxResults, firstResult);
    }

    private List<Receta> findRecetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Receta.class));
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

    public Receta findReceta(RecetaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Receta.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Receta> rt = cq.from(Receta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
