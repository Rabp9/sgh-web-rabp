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
import org.sghweb.jpa.Medico;
import org.sghweb.jpa.Detallehistoriaclinica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Cita;
import org.sghweb.jpa.CitaPK;

/**
 *
 * @author essalud
 */
public class CitaJpaController implements Serializable {

    public CitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cita cita) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cita.getCitaPK() == null) {
            cita.setCitaPK(new CitaPK());
        }
        if (cita.getDetallehistoriaclinicaList() == null) {
            cita.setDetallehistoriaclinicaList(new ArrayList<Detallehistoriaclinica>());
        }
        cita.getCitaPK().setMedicocmp(cita.getMedico().getMedicoPK().getCmp());
        cita.getCitaPK().setMedicodni(cita.getMedico().getMedicoPK().getDni());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medico medico = cita.getMedico();
            if (medico != null) {
                medico = em.getReference(medico.getClass(), medico.getMedicoPK());
                cita.setMedico(medico);
            }
            List<Detallehistoriaclinica> attachedDetallehistoriaclinicaList = new ArrayList<Detallehistoriaclinica>();
            for (Detallehistoriaclinica detallehistoriaclinicaListDetallehistoriaclinicaToAttach : cita.getDetallehistoriaclinicaList()) {
                detallehistoriaclinicaListDetallehistoriaclinicaToAttach = em.getReference(detallehistoriaclinicaListDetallehistoriaclinicaToAttach.getClass(), detallehistoriaclinicaListDetallehistoriaclinicaToAttach.getDetallehistoriaclinicaPK());
                attachedDetallehistoriaclinicaList.add(detallehistoriaclinicaListDetallehistoriaclinicaToAttach);
            }
            cita.setDetallehistoriaclinicaList(attachedDetallehistoriaclinicaList);
            em.persist(cita);
            if (medico != null) {
                medico.getCitaList().add(cita);
                medico = em.merge(medico);
            }
            for (Detallehistoriaclinica detallehistoriaclinicaListDetallehistoriaclinica : cita.getDetallehistoriaclinicaList()) {
                Cita oldCitaOfDetallehistoriaclinicaListDetallehistoriaclinica = detallehistoriaclinicaListDetallehistoriaclinica.getCita();
                detallehistoriaclinicaListDetallehistoriaclinica.setCita(cita);
                detallehistoriaclinicaListDetallehistoriaclinica = em.merge(detallehistoriaclinicaListDetallehistoriaclinica);
                if (oldCitaOfDetallehistoriaclinicaListDetallehistoriaclinica != null) {
                    oldCitaOfDetallehistoriaclinicaListDetallehistoriaclinica.getDetallehistoriaclinicaList().remove(detallehistoriaclinicaListDetallehistoriaclinica);
                    oldCitaOfDetallehistoriaclinicaListDetallehistoriaclinica = em.merge(oldCitaOfDetallehistoriaclinicaListDetallehistoriaclinica);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCita(cita.getCitaPK()) != null) {
                throw new PreexistingEntityException("Cita " + cita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cita cita) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        cita.getCitaPK().setMedicocmp(cita.getMedico().getMedicoPK().getCmp());
        cita.getCitaPK().setMedicodni(cita.getMedico().getMedicoPK().getDni());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita persistentCita = em.find(Cita.class, cita.getCitaPK());
            Medico medicoOld = persistentCita.getMedico();
            Medico medicoNew = cita.getMedico();
            List<Detallehistoriaclinica> detallehistoriaclinicaListOld = persistentCita.getDetallehistoriaclinicaList();
            List<Detallehistoriaclinica> detallehistoriaclinicaListNew = cita.getDetallehistoriaclinicaList();
            List<String> illegalOrphanMessages = null;
            for (Detallehistoriaclinica detallehistoriaclinicaListOldDetallehistoriaclinica : detallehistoriaclinicaListOld) {
                if (!detallehistoriaclinicaListNew.contains(detallehistoriaclinicaListOldDetallehistoriaclinica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallehistoriaclinica " + detallehistoriaclinicaListOldDetallehistoriaclinica + " since its cita field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (medicoNew != null) {
                medicoNew = em.getReference(medicoNew.getClass(), medicoNew.getMedicoPK());
                cita.setMedico(medicoNew);
            }
            List<Detallehistoriaclinica> attachedDetallehistoriaclinicaListNew = new ArrayList<Detallehistoriaclinica>();
            for (Detallehistoriaclinica detallehistoriaclinicaListNewDetallehistoriaclinicaToAttach : detallehistoriaclinicaListNew) {
                detallehistoriaclinicaListNewDetallehistoriaclinicaToAttach = em.getReference(detallehistoriaclinicaListNewDetallehistoriaclinicaToAttach.getClass(), detallehistoriaclinicaListNewDetallehistoriaclinicaToAttach.getDetallehistoriaclinicaPK());
                attachedDetallehistoriaclinicaListNew.add(detallehistoriaclinicaListNewDetallehistoriaclinicaToAttach);
            }
            detallehistoriaclinicaListNew = attachedDetallehistoriaclinicaListNew;
            cita.setDetallehistoriaclinicaList(detallehistoriaclinicaListNew);
            cita = em.merge(cita);
            if (medicoOld != null && !medicoOld.equals(medicoNew)) {
                medicoOld.getCitaList().remove(cita);
                medicoOld = em.merge(medicoOld);
            }
            if (medicoNew != null && !medicoNew.equals(medicoOld)) {
                medicoNew.getCitaList().add(cita);
                medicoNew = em.merge(medicoNew);
            }
            for (Detallehistoriaclinica detallehistoriaclinicaListNewDetallehistoriaclinica : detallehistoriaclinicaListNew) {
                if (!detallehistoriaclinicaListOld.contains(detallehistoriaclinicaListNewDetallehistoriaclinica)) {
                    Cita oldCitaOfDetallehistoriaclinicaListNewDetallehistoriaclinica = detallehistoriaclinicaListNewDetallehistoriaclinica.getCita();
                    detallehistoriaclinicaListNewDetallehistoriaclinica.setCita(cita);
                    detallehistoriaclinicaListNewDetallehistoriaclinica = em.merge(detallehistoriaclinicaListNewDetallehistoriaclinica);
                    if (oldCitaOfDetallehistoriaclinicaListNewDetallehistoriaclinica != null && !oldCitaOfDetallehistoriaclinicaListNewDetallehistoriaclinica.equals(cita)) {
                        oldCitaOfDetallehistoriaclinicaListNewDetallehistoriaclinica.getDetallehistoriaclinicaList().remove(detallehistoriaclinicaListNewDetallehistoriaclinica);
                        oldCitaOfDetallehistoriaclinicaListNewDetallehistoriaclinica = em.merge(oldCitaOfDetallehistoriaclinicaListNewDetallehistoriaclinica);
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
                CitaPK id = cita.getCitaPK();
                if (findCita(id) == null) {
                    throw new NonexistentEntityException("The cita with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CitaPK id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita cita;
            try {
                cita = em.getReference(Cita.class, id);
                cita.getCitaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cita with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallehistoriaclinica> detallehistoriaclinicaListOrphanCheck = cita.getDetallehistoriaclinicaList();
            for (Detallehistoriaclinica detallehistoriaclinicaListOrphanCheckDetallehistoriaclinica : detallehistoriaclinicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cita (" + cita + ") cannot be destroyed since the Detallehistoriaclinica " + detallehistoriaclinicaListOrphanCheckDetallehistoriaclinica + " in its detallehistoriaclinicaList field has a non-nullable cita field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medico medico = cita.getMedico();
            if (medico != null) {
                medico.getCitaList().remove(cita);
                medico = em.merge(medico);
            }
            em.remove(cita);
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

    public List<Cita> findCitaEntities() {
        return findCitaEntities(true, -1, -1);
    }

    public List<Cita> findCitaEntities(int maxResults, int firstResult) {
        return findCitaEntities(false, maxResults, firstResult);
    }

    private List<Cita> findCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cita.class));
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

    public Cita findCita(CitaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cita> rt = cq.from(Cita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
