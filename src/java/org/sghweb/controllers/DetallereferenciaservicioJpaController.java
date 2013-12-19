/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sghweb.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
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
import org.sghweb.jpa.Detallereferenciaservicio;
import org.sghweb.jpa.DetallereferenciaservicioPK;
import org.sghweb.jpa.Servicio;
import org.sghweb.jpa.Referencia;

/**
 *
 * @author Roberto
 */
public class DetallereferenciaservicioJpaController implements Serializable {

    public DetallereferenciaservicioJpaController(UserTransaction utx, EntityManagerFactory emf) {
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

    public void create(Detallereferenciaservicio detallereferenciaservicio) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (detallereferenciaservicio.getDetallereferenciaservicioPK() == null) {
            detallereferenciaservicio.setDetallereferenciaservicioPK(new DetallereferenciaservicioPK());
        }
        detallereferenciaservicio.getDetallereferenciaservicioPK().setReferencianumeroRegistro(detallereferenciaservicio.getReferencia().getReferenciaPK().getNumeroRegistro());
        detallereferenciaservicio.getDetallereferenciaservicioPK().setReferenciaPacientedni(detallereferenciaservicio.getReferencia().getReferenciaPK().getPacientedni());
        detallereferenciaservicio.getDetallereferenciaservicioPK().setServiciocodigo(detallereferenciaservicio.getServicio().getCodigo());
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Servicio servicio = detallereferenciaservicio.getServicio();
            if (servicio != null) {
                servicio = em.getReference(servicio.getClass(), servicio.getCodigo());
                detallereferenciaservicio.setServicio(servicio);
            }
            Referencia referencia = detallereferenciaservicio.getReferencia();
            if (referencia != null) {
                referencia = em.getReference(referencia.getClass(), referencia.getReferenciaPK());
                detallereferenciaservicio.setReferencia(referencia);
            }
            em.persist(detallereferenciaservicio);
            if (servicio != null) {
                servicio.getDetallereferenciaservicioList().add(detallereferenciaservicio);
                servicio = em.merge(servicio);
            }
            if (referencia != null) {
                referencia.getDetallereferenciaservicioList().add(detallereferenciaservicio);
                referencia = em.merge(referencia);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallereferenciaservicio(detallereferenciaservicio.getDetallereferenciaservicioPK()) != null) {
                throw new PreexistingEntityException("Detallereferenciaservicio " + detallereferenciaservicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallereferenciaservicio detallereferenciaservicio) throws NonexistentEntityException, RollbackFailureException, Exception {
        detallereferenciaservicio.getDetallereferenciaservicioPK().setReferencianumeroRegistro(detallereferenciaservicio.getReferencia().getReferenciaPK().getNumeroRegistro());
        detallereferenciaservicio.getDetallereferenciaservicioPK().setReferenciaPacientedni(detallereferenciaservicio.getReferencia().getReferenciaPK().getPacientedni());
        detallereferenciaservicio.getDetallereferenciaservicioPK().setServiciocodigo(detallereferenciaservicio.getServicio().getCodigo());
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Detallereferenciaservicio persistentDetallereferenciaservicio = em.find(Detallereferenciaservicio.class, detallereferenciaservicio.getDetallereferenciaservicioPK());
            Servicio servicioOld = persistentDetallereferenciaservicio.getServicio();
            Servicio servicioNew = detallereferenciaservicio.getServicio();
            Referencia referenciaOld = persistentDetallereferenciaservicio.getReferencia();
            Referencia referenciaNew = detallereferenciaservicio.getReferencia();
            if (servicioNew != null) {
                servicioNew = em.getReference(servicioNew.getClass(), servicioNew.getCodigo());
                detallereferenciaservicio.setServicio(servicioNew);
            }
            if (referenciaNew != null) {
                referenciaNew = em.getReference(referenciaNew.getClass(), referenciaNew.getReferenciaPK());
                detallereferenciaservicio.setReferencia(referenciaNew);
            }
            detallereferenciaservicio = em.merge(detallereferenciaservicio);
            if (servicioOld != null && !servicioOld.equals(servicioNew)) {
                servicioOld.getDetallereferenciaservicioList().remove(detallereferenciaservicio);
                servicioOld = em.merge(servicioOld);
            }
            if (servicioNew != null && !servicioNew.equals(servicioOld)) {
                servicioNew.getDetallereferenciaservicioList().add(detallereferenciaservicio);
                servicioNew = em.merge(servicioNew);
            }
            if (referenciaOld != null && !referenciaOld.equals(referenciaNew)) {
                referenciaOld.getDetallereferenciaservicioList().remove(detallereferenciaservicio);
                referenciaOld = em.merge(referenciaOld);
            }
            if (referenciaNew != null && !referenciaNew.equals(referenciaOld)) {
                referenciaNew.getDetallereferenciaservicioList().add(detallereferenciaservicio);
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
                DetallereferenciaservicioPK id = detallereferenciaservicio.getDetallereferenciaservicioPK();
                if (findDetallereferenciaservicio(id) == null) {
                    throw new NonexistentEntityException("The detallereferenciaservicio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetallereferenciaservicioPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Detallereferenciaservicio detallereferenciaservicio;
            try {
                detallereferenciaservicio = em.getReference(Detallereferenciaservicio.class, id);
                detallereferenciaservicio.getDetallereferenciaservicioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallereferenciaservicio with id " + id + " no longer exists.", enfe);
            }
            Servicio servicio = detallereferenciaservicio.getServicio();
            if (servicio != null) {
                servicio.getDetallereferenciaservicioList().remove(detallereferenciaservicio);
                servicio = em.merge(servicio);
            }
            Referencia referencia = detallereferenciaservicio.getReferencia();
            if (referencia != null) {
                referencia.getDetallereferenciaservicioList().remove(detallereferenciaservicio);
                referencia = em.merge(referencia);
            }
            em.remove(detallereferenciaservicio);
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

    public List<Detallereferenciaservicio> findDetallereferenciaservicioEntities() {
        return findDetallereferenciaservicioEntities(true, -1, -1);
    }

    public List<Detallereferenciaservicio> findDetallereferenciaservicioEntities(int maxResults, int firstResult) {
        return findDetallereferenciaservicioEntities(false, maxResults, firstResult);
    }

    private List<Detallereferenciaservicio> findDetallereferenciaservicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallereferenciaservicio.class));
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

    public Detallereferenciaservicio findDetallereferenciaservicio(DetallereferenciaservicioPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallereferenciaservicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallereferenciaservicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallereferenciaservicio> rt = cq.from(Detallereferenciaservicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
