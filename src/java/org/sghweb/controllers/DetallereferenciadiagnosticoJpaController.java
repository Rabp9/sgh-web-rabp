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
import org.sghweb.jpa.Detallereferenciadiagnostico;
import org.sghweb.jpa.DetallereferenciadiagnosticoPK;
import org.sghweb.jpa.Diagnostico;
import org.sghweb.jpa.Referencia;

/**
 *
 * @author Roberto
 */
public class DetallereferenciadiagnosticoJpaController implements Serializable {

    public DetallereferenciadiagnosticoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallereferenciadiagnostico detallereferenciadiagnostico) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK() == null) {
            detallereferenciadiagnostico.setDetallereferenciadiagnosticoPK(new DetallereferenciadiagnosticoPK());
        }
        detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK().setDiagnosticocodigo(detallereferenciadiagnostico.getDiagnostico().getCodigo());
        detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK().setReferenciaPacientedni(detallereferenciadiagnostico.getReferencia().getReferenciaPK().getPacientedni());
        detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK().setReferencianumeroRegistro(detallereferenciadiagnostico.getReferencia().getReferenciaPK().getNumeroRegistro());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Diagnostico diagnostico = detallereferenciadiagnostico.getDiagnostico();
            if (diagnostico != null) {
                diagnostico = em.getReference(diagnostico.getClass(), diagnostico.getCodigo());
                detallereferenciadiagnostico.setDiagnostico(diagnostico);
            }
            Referencia referencia = detallereferenciadiagnostico.getReferencia();
            if (referencia != null) {
                referencia = em.getReference(referencia.getClass(), referencia.getReferenciaPK());
                detallereferenciadiagnostico.setReferencia(referencia);
            }
            em.persist(detallereferenciadiagnostico);
            if (diagnostico != null) {
                diagnostico.getDetallereferenciadiagnosticoList().add(detallereferenciadiagnostico);
                diagnostico = em.merge(diagnostico);
            }
            if (referencia != null) {
                referencia.getDetallereferenciadiagnosticoList().add(detallereferenciadiagnostico);
                referencia = em.merge(referencia);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallereferenciadiagnostico(detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK()) != null) {
                throw new PreexistingEntityException("Detallereferenciadiagnostico " + detallereferenciadiagnostico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallereferenciadiagnostico detallereferenciadiagnostico) throws NonexistentEntityException, RollbackFailureException, Exception {
        detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK().setDiagnosticocodigo(detallereferenciadiagnostico.getDiagnostico().getCodigo());
        detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK().setReferenciaPacientedni(detallereferenciadiagnostico.getReferencia().getReferenciaPK().getPacientedni());
        detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK().setReferencianumeroRegistro(detallereferenciadiagnostico.getReferencia().getReferenciaPK().getNumeroRegistro());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallereferenciadiagnostico persistentDetallereferenciadiagnostico = em.find(Detallereferenciadiagnostico.class, detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK());
            Diagnostico diagnosticoOld = persistentDetallereferenciadiagnostico.getDiagnostico();
            Diagnostico diagnosticoNew = detallereferenciadiagnostico.getDiagnostico();
            Referencia referenciaOld = persistentDetallereferenciadiagnostico.getReferencia();
            Referencia referenciaNew = detallereferenciadiagnostico.getReferencia();
            if (diagnosticoNew != null) {
                diagnosticoNew = em.getReference(diagnosticoNew.getClass(), diagnosticoNew.getCodigo());
                detallereferenciadiagnostico.setDiagnostico(diagnosticoNew);
            }
            if (referenciaNew != null) {
                referenciaNew = em.getReference(referenciaNew.getClass(), referenciaNew.getReferenciaPK());
                detallereferenciadiagnostico.setReferencia(referenciaNew);
            }
            detallereferenciadiagnostico = em.merge(detallereferenciadiagnostico);
            if (diagnosticoOld != null && !diagnosticoOld.equals(diagnosticoNew)) {
                diagnosticoOld.getDetallereferenciadiagnosticoList().remove(detallereferenciadiagnostico);
                diagnosticoOld = em.merge(diagnosticoOld);
            }
            if (diagnosticoNew != null && !diagnosticoNew.equals(diagnosticoOld)) {
                diagnosticoNew.getDetallereferenciadiagnosticoList().add(detallereferenciadiagnostico);
                diagnosticoNew = em.merge(diagnosticoNew);
            }
            if (referenciaOld != null && !referenciaOld.equals(referenciaNew)) {
                referenciaOld.getDetallereferenciadiagnosticoList().remove(detallereferenciadiagnostico);
                referenciaOld = em.merge(referenciaOld);
            }
            if (referenciaNew != null && !referenciaNew.equals(referenciaOld)) {
                referenciaNew.getDetallereferenciadiagnosticoList().add(detallereferenciadiagnostico);
                referenciaNew = em.merge(referenciaNew);
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
                DetallereferenciadiagnosticoPK id = detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK();
                if (findDetallereferenciadiagnostico(id) == null) {
                    throw new NonexistentEntityException("The detallereferenciadiagnostico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetallereferenciadiagnosticoPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallereferenciadiagnostico detallereferenciadiagnostico;
            try {
                detallereferenciadiagnostico = em.getReference(Detallereferenciadiagnostico.class, id);
                detallereferenciadiagnostico.getDetallereferenciadiagnosticoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallereferenciadiagnostico with id " + id + " no longer exists.", enfe);
            }
            Diagnostico diagnostico = detallereferenciadiagnostico.getDiagnostico();
            if (diagnostico != null) {
                diagnostico.getDetallereferenciadiagnosticoList().remove(detallereferenciadiagnostico);
                diagnostico = em.merge(diagnostico);
            }
            Referencia referencia = detallereferenciadiagnostico.getReferencia();
            if (referencia != null) {
                referencia.getDetallereferenciadiagnosticoList().remove(detallereferenciadiagnostico);
                referencia = em.merge(referencia);
            }
            em.remove(detallereferenciadiagnostico);
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

    public List<Detallereferenciadiagnostico> findDetallereferenciadiagnosticoEntities() {
        return findDetallereferenciadiagnosticoEntities(true, -1, -1);
    }

    public List<Detallereferenciadiagnostico> findDetallereferenciadiagnosticoEntities(int maxResults, int firstResult) {
        return findDetallereferenciadiagnosticoEntities(false, maxResults, firstResult);
    }

    private List<Detallereferenciadiagnostico> findDetallereferenciadiagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallereferenciadiagnostico.class));
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

    public Detallereferenciadiagnostico findDetallereferenciadiagnostico(DetallereferenciadiagnosticoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallereferenciadiagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallereferenciadiagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallereferenciadiagnostico> rt = cq.from(Detallereferenciadiagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
