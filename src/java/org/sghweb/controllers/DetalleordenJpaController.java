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
import org.sghweb.jpa.Detallehistoriaclinica;
import org.sghweb.jpa.Detalleorden;
import org.sghweb.jpa.DetalleordenPK;

/**
 *
 * @author Roberto
 */
public class DetalleordenJpaController implements Serializable {

    public DetalleordenJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleorden detalleorden) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (detalleorden.getDetalleordenPK() == null) {
            detalleorden.setDetalleordenPK(new DetalleordenPK());
        }
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaCitaactoMedico(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaactoMedico());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaHistoriaClinicanumeroRegistro(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicanumeroRegistro());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaHistoriaClinicaPacientedni(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaPacientedni());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaCitaMedicocmp(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicocmp());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaHistoriaClinicaautogenerado(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaautogenerado());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaCitaMedicodni(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicodni());
        detalleorden.getDetalleordenPK().setOrdennroOrden(detalleorden.getOrden().getNroOrden());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaidDetalleHistoriaClinica(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getIdDetalleHistoriaClinica());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orden orden = detalleorden.getOrden();
            if (orden != null) {
                orden = em.getReference(orden.getClass(), orden.getNroOrden());
                detalleorden.setOrden(orden);
            }
            Detallehistoriaclinica detallehistoriaclinica = detalleorden.getDetallehistoriaclinica();
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica = em.getReference(detallehistoriaclinica.getClass(), detallehistoriaclinica.getDetallehistoriaclinicaPK());
                detalleorden.setDetallehistoriaclinica(detallehistoriaclinica);
            }
            em.persist(detalleorden);
            if (orden != null) {
                orden.getDetalleordenList().add(detalleorden);
                orden = em.merge(orden);
            }
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica.getDetalleordenList().add(detalleorden);
                detallehistoriaclinica = em.merge(detallehistoriaclinica);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleorden(detalleorden.getDetalleordenPK()) != null) {
                throw new PreexistingEntityException("Detalleorden " + detalleorden + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleorden detalleorden) throws NonexistentEntityException, RollbackFailureException, Exception {
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaCitaactoMedico(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaactoMedico());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaHistoriaClinicanumeroRegistro(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicanumeroRegistro());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaHistoriaClinicaPacientedni(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaPacientedni());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaCitaMedicocmp(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicocmp());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaHistoriaClinicaautogenerado(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getHistoriaClinicaautogenerado());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaCitaMedicodni(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getCitaMedicodni());
        detalleorden.getDetalleordenPK().setOrdennroOrden(detalleorden.getOrden().getNroOrden());
        detalleorden.getDetalleordenPK().setDetalleHistoriaClinicaidDetalleHistoriaClinica(detalleorden.getDetallehistoriaclinica().getDetallehistoriaclinicaPK().getIdDetalleHistoriaClinica());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleorden persistentDetalleorden = em.find(Detalleorden.class, detalleorden.getDetalleordenPK());
            Orden ordenOld = persistentDetalleorden.getOrden();
            Orden ordenNew = detalleorden.getOrden();
            Detallehistoriaclinica detallehistoriaclinicaOld = persistentDetalleorden.getDetallehistoriaclinica();
            Detallehistoriaclinica detallehistoriaclinicaNew = detalleorden.getDetallehistoriaclinica();
            if (ordenNew != null) {
                ordenNew = em.getReference(ordenNew.getClass(), ordenNew.getNroOrden());
                detalleorden.setOrden(ordenNew);
            }
            if (detallehistoriaclinicaNew != null) {
                detallehistoriaclinicaNew = em.getReference(detallehistoriaclinicaNew.getClass(), detallehistoriaclinicaNew.getDetallehistoriaclinicaPK());
                detalleorden.setDetallehistoriaclinica(detallehistoriaclinicaNew);
            }
            detalleorden = em.merge(detalleorden);
            if (ordenOld != null && !ordenOld.equals(ordenNew)) {
                ordenOld.getDetalleordenList().remove(detalleorden);
                ordenOld = em.merge(ordenOld);
            }
            if (ordenNew != null && !ordenNew.equals(ordenOld)) {
                ordenNew.getDetalleordenList().add(detalleorden);
                ordenNew = em.merge(ordenNew);
            }
            if (detallehistoriaclinicaOld != null && !detallehistoriaclinicaOld.equals(detallehistoriaclinicaNew)) {
                detallehistoriaclinicaOld.getDetalleordenList().remove(detalleorden);
                detallehistoriaclinicaOld = em.merge(detallehistoriaclinicaOld);
            }
            if (detallehistoriaclinicaNew != null && !detallehistoriaclinicaNew.equals(detallehistoriaclinicaOld)) {
                detallehistoriaclinicaNew.getDetalleordenList().add(detalleorden);
                detallehistoriaclinicaNew = em.merge(detallehistoriaclinicaNew);
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
                DetalleordenPK id = detalleorden.getDetalleordenPK();
                if (findDetalleorden(id) == null) {
                    throw new NonexistentEntityException("The detalleorden with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetalleordenPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleorden detalleorden;
            try {
                detalleorden = em.getReference(Detalleorden.class, id);
                detalleorden.getDetalleordenPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleorden with id " + id + " no longer exists.", enfe);
            }
            Orden orden = detalleorden.getOrden();
            if (orden != null) {
                orden.getDetalleordenList().remove(detalleorden);
                orden = em.merge(orden);
            }
            Detallehistoriaclinica detallehistoriaclinica = detalleorden.getDetallehistoriaclinica();
            if (detallehistoriaclinica != null) {
                detallehistoriaclinica.getDetalleordenList().remove(detalleorden);
                detallehistoriaclinica = em.merge(detallehistoriaclinica);
            }
            em.remove(detalleorden);
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

    public List<Detalleorden> findDetalleordenEntities() {
        return findDetalleordenEntities(true, -1, -1);
    }

    public List<Detalleorden> findDetalleordenEntities(int maxResults, int firstResult) {
        return findDetalleordenEntities(false, maxResults, firstResult);
    }

    private List<Detalleorden> findDetalleordenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleorden.class));
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

    public Detalleorden findDetalleorden(DetalleordenPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleorden.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleordenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleorden> rt = cq.from(Detalleorden.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
