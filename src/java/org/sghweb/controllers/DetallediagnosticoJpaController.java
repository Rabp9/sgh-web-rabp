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
import org.sghweb.jpa.Detallediagnostico;
import org.sghweb.jpa.DetallediagnosticoPK;
import org.sghweb.jpa.Detallehistoriaclinica;
import org.sghweb.jpa.Diagnostico;

/**
 *
 * @author Roberto
 */
public class DetallediagnosticoJpaController implements Serializable {

    public DetallediagnosticoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallediagnostico detallediagnostico) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (detallediagnostico.getDetallediagnosticoPK() == null) {
            detallediagnostico.setDetallediagnosticoPK(new DetallediagnosticoPK());
        }
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaCitaMedicodni(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicodni());
        detallediagnostico.getDetallediagnosticoPK().setDiagnosticocodigo(detallediagnostico.getDiagnostico().getCodigo());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaHistoriaClinicanumeroRegistro(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicanumeroRegistro());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaHistoriaClinicaPacientedni(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaPacientedni());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaHistoriaClinicaautogenerado(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaautogenerado());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaidDetalleHistoriaClinica(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getIdDetalleHistoriaClinica());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaCitaactoMedico(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaactoMedico());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaCitaMedicocmp(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicocmp());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallehistoriaclinica detallehistoriaclinica = detallediagnostico.getDetallehistoriaclinica();
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica = em.getReference(detallehistoriaclinica.getClass(), detallehistoriaclinica.getDetallehistoriaclinicaPK());
                detallediagnostico.setDetallehistoriaclinica(detallehistoriaclinica);
            }
            Diagnostico diagnostico = detallediagnostico.getDiagnostico();
            if (diagnostico != null) {
                diagnostico = em.getReference(diagnostico.getClass(), diagnostico.getCodigo());
                detallediagnostico.setDiagnostico(diagnostico);
            }
            em.persist(detallediagnostico);
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica.getDetallediagnosticoList().add(detallediagnostico);
                detallehistoriaclinica = em.merge(detallehistoriaclinica);
            }
            if (diagnostico != null) {
                diagnostico.getDetallediagnosticoList().add(detallediagnostico);
                diagnostico = em.merge(diagnostico);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallediagnostico(detallediagnostico.getDetallediagnosticoPK()) != null) {
                throw new PreexistingEntityException("Detallediagnostico " + detallediagnostico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallediagnostico detallediagnostico) throws NonexistentEntityException, RollbackFailureException, Exception {
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaCitaMedicodni(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicodni());
        detallediagnostico.getDetallediagnosticoPK().setDiagnosticocodigo(detallediagnostico.getDiagnostico().getCodigo());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaHistoriaClinicanumeroRegistro(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicanumeroRegistro());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaHistoriaClinicaPacientedni(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaPacientedni());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaHistoriaClinicaautogenerado(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaautogenerado());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaidDetalleHistoriaClinica(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getIdDetalleHistoriaClinica());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaCitaactoMedico(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaactoMedico());
        detallediagnostico.getDetallediagnosticoPK().setDetalleHistoriaClinicaCitaMedicocmp(detallediagnostico.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicocmp());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallediagnostico persistentDetallediagnostico = em.find(Detallediagnostico.class, detallediagnostico.getDetallediagnosticoPK());
            Detallehistoriaclinica detallehistoriaclinicaOld = persistentDetallediagnostico.getDetallehistoriaclinica();
            Detallehistoriaclinica detallehistoriaclinicaNew = detallediagnostico.getDetallehistoriaclinica();
            Diagnostico diagnosticoOld = persistentDetallediagnostico.getDiagnostico();
            Diagnostico diagnosticoNew = detallediagnostico.getDiagnostico();
            if (detallehistoriaclinicaNew != null) {
                detallehistoriaclinicaNew = em.getReference(detallehistoriaclinicaNew.getClass(), detallehistoriaclinicaNew.getDetallehistoriaclinicaPK());
                detallediagnostico.setDetallehistoriaclinica(detallehistoriaclinicaNew);
            }
            if (diagnosticoNew != null) {
                diagnosticoNew = em.getReference(diagnosticoNew.getClass(), diagnosticoNew.getCodigo());
                detallediagnostico.setDiagnostico(diagnosticoNew);
            }
            detallediagnostico = em.merge(detallediagnostico);
            if (detallehistoriaclinicaOld != null && !detallehistoriaclinicaOld.equals(detallehistoriaclinicaNew)) {
                detallehistoriaclinicaOld.getDetallediagnosticoList().remove(detallediagnostico);
                detallehistoriaclinicaOld = em.merge(detallehistoriaclinicaOld);
            }
            if (detallehistoriaclinicaNew != null && !detallehistoriaclinicaNew.equals(detallehistoriaclinicaOld)) {
                detallehistoriaclinicaNew.getDetallediagnosticoList().add(detallediagnostico);
                detallehistoriaclinicaNew = em.merge(detallehistoriaclinicaNew);
            }
            if (diagnosticoOld != null && !diagnosticoOld.equals(diagnosticoNew)) {
                diagnosticoOld.getDetallediagnosticoList().remove(detallediagnostico);
                diagnosticoOld = em.merge(diagnosticoOld);
            }
            if (diagnosticoNew != null && !diagnosticoNew.equals(diagnosticoOld)) {
                diagnosticoNew.getDetallediagnosticoList().add(detallediagnostico);
                diagnosticoNew = em.merge(diagnosticoNew);
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
                DetallediagnosticoPK id = detallediagnostico.getDetallediagnosticoPK();
                if (findDetallediagnostico(id) == null) {
                    throw new NonexistentEntityException("The detallediagnostico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetallediagnosticoPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallediagnostico detallediagnostico;
            try {
                detallediagnostico = em.getReference(Detallediagnostico.class, id);
                detallediagnostico.getDetallediagnosticoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallediagnostico with id " + id + " no longer exists.", enfe);
            }
            Detallehistoriaclinica detallehistoriaclinica = detallediagnostico.getDetallehistoriaclinica();
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica.getDetallediagnosticoList().remove(detallediagnostico);
                detallehistoriaclinica = em.merge(detallehistoriaclinica);
            }
            Diagnostico diagnostico = detallediagnostico.getDiagnostico();
            if (diagnostico != null) {
                diagnostico.getDetallediagnosticoList().remove(detallediagnostico);
                diagnostico = em.merge(diagnostico);
            }
            em.remove(detallediagnostico);
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

    public List<Detallediagnostico> findDetallediagnosticoEntities() {
        return findDetallediagnosticoEntities(true, -1, -1);
    }

    public List<Detallediagnostico> findDetallediagnosticoEntities(int maxResults, int firstResult) {
        return findDetallediagnosticoEntities(false, maxResults, firstResult);
    }

    private List<Detallediagnostico> findDetallediagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallediagnostico.class));
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

    public Detallediagnostico findDetallediagnostico(DetallediagnosticoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallediagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallediagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallediagnostico> rt = cq.from(Detallediagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
