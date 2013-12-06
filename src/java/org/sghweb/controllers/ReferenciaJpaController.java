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
import org.sghweb.jpa.Paciente;
import org.sghweb.jpa.Detallereferenciadiagnostico;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.sghweb.controllers.exceptions.IllegalOrphanException;
import org.sghweb.controllers.exceptions.NonexistentEntityException;
import org.sghweb.controllers.exceptions.PreexistingEntityException;
import org.sghweb.controllers.exceptions.RollbackFailureException;
import org.sghweb.jpa.Referencia;
import org.sghweb.jpa.ReferenciaPK;

/**
 *
 * @author Roberto
 */
public class ReferenciaJpaController implements Serializable {

    public ReferenciaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Referencia referencia) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (referencia.getReferenciaPK() == null) {
            referencia.setReferenciaPK(new ReferenciaPK());
        }
        if (referencia.getDetallereferenciadiagnosticoList() == null) {
            referencia.setDetallereferenciadiagnosticoList(new ArrayList<Detallereferenciadiagnostico>());
        }
        referencia.getReferenciaPK().setPacientedni(referencia.getPaciente().getDni());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente paciente = referencia.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getDni());
                referencia.setPaciente(paciente);
            }
            List<Detallereferenciadiagnostico> attachedDetallereferenciadiagnosticoList = new ArrayList<Detallereferenciadiagnostico>();
            for (Detallereferenciadiagnostico detallereferenciadiagnosticoListDetallereferenciadiagnosticoToAttach : referencia.getDetallereferenciadiagnosticoList()) {
                detallereferenciadiagnosticoListDetallereferenciadiagnosticoToAttach = em.getReference(detallereferenciadiagnosticoListDetallereferenciadiagnosticoToAttach.getClass(), detallereferenciadiagnosticoListDetallereferenciadiagnosticoToAttach.getDetallereferenciadiagnosticoPK());
                attachedDetallereferenciadiagnosticoList.add(detallereferenciadiagnosticoListDetallereferenciadiagnosticoToAttach);
            }
            referencia.setDetallereferenciadiagnosticoList(attachedDetallereferenciadiagnosticoList);
            em.persist(referencia);
            if (paciente != null) {
                paciente.getReferenciaList().add(referencia);
                paciente = em.merge(paciente);
            }
            for (Detallereferenciadiagnostico detallereferenciadiagnosticoListDetallereferenciadiagnostico : referencia.getDetallereferenciadiagnosticoList()) {
                Referencia oldReferenciaOfDetallereferenciadiagnosticoListDetallereferenciadiagnostico = detallereferenciadiagnosticoListDetallereferenciadiagnostico.getReferencia();
                detallereferenciadiagnosticoListDetallereferenciadiagnostico.setReferencia(referencia);
                detallereferenciadiagnosticoListDetallereferenciadiagnostico = em.merge(detallereferenciadiagnosticoListDetallereferenciadiagnostico);
                if (oldReferenciaOfDetallereferenciadiagnosticoListDetallereferenciadiagnostico != null) {
                    oldReferenciaOfDetallereferenciadiagnosticoListDetallereferenciadiagnostico.getDetallereferenciadiagnosticoList().remove(detallereferenciadiagnosticoListDetallereferenciadiagnostico);
                    oldReferenciaOfDetallereferenciadiagnosticoListDetallereferenciadiagnostico = em.merge(oldReferenciaOfDetallereferenciadiagnosticoListDetallereferenciadiagnostico);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReferencia(referencia.getReferenciaPK()) != null) {
                throw new PreexistingEntityException("Referencia " + referencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Referencia referencia) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        referencia.getReferenciaPK().setPacientedni(referencia.getPaciente().getDni());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Referencia persistentReferencia = em.find(Referencia.class, referencia.getReferenciaPK());
            Paciente pacienteOld = persistentReferencia.getPaciente();
            Paciente pacienteNew = referencia.getPaciente();
            List<Detallereferenciadiagnostico> detallereferenciadiagnosticoListOld = persistentReferencia.getDetallereferenciadiagnosticoList();
            List<Detallereferenciadiagnostico> detallereferenciadiagnosticoListNew = referencia.getDetallereferenciadiagnosticoList();
            List<String> illegalOrphanMessages = null;
            for (Detallereferenciadiagnostico detallereferenciadiagnosticoListOldDetallereferenciadiagnostico : detallereferenciadiagnosticoListOld) {
                if (!detallereferenciadiagnosticoListNew.contains(detallereferenciadiagnosticoListOldDetallereferenciadiagnostico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallereferenciadiagnostico " + detallereferenciadiagnosticoListOldDetallereferenciadiagnostico + " since its referencia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getDni());
                referencia.setPaciente(pacienteNew);
            }
            List<Detallereferenciadiagnostico> attachedDetallereferenciadiagnosticoListNew = new ArrayList<Detallereferenciadiagnostico>();
            for (Detallereferenciadiagnostico detallereferenciadiagnosticoListNewDetallereferenciadiagnosticoToAttach : detallereferenciadiagnosticoListNew) {
                detallereferenciadiagnosticoListNewDetallereferenciadiagnosticoToAttach = em.getReference(detallereferenciadiagnosticoListNewDetallereferenciadiagnosticoToAttach.getClass(), detallereferenciadiagnosticoListNewDetallereferenciadiagnosticoToAttach.getDetallereferenciadiagnosticoPK());
                attachedDetallereferenciadiagnosticoListNew.add(detallereferenciadiagnosticoListNewDetallereferenciadiagnosticoToAttach);
            }
            detallereferenciadiagnosticoListNew = attachedDetallereferenciadiagnosticoListNew;
            referencia.setDetallereferenciadiagnosticoList(detallereferenciadiagnosticoListNew);
            referencia = em.merge(referencia);
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.getReferenciaList().remove(referencia);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.getReferenciaList().add(referencia);
                pacienteNew = em.merge(pacienteNew);
            }
            for (Detallereferenciadiagnostico detallereferenciadiagnosticoListNewDetallereferenciadiagnostico : detallereferenciadiagnosticoListNew) {
                if (!detallereferenciadiagnosticoListOld.contains(detallereferenciadiagnosticoListNewDetallereferenciadiagnostico)) {
                    Referencia oldReferenciaOfDetallereferenciadiagnosticoListNewDetallereferenciadiagnostico = detallereferenciadiagnosticoListNewDetallereferenciadiagnostico.getReferencia();
                    detallereferenciadiagnosticoListNewDetallereferenciadiagnostico.setReferencia(referencia);
                    detallereferenciadiagnosticoListNewDetallereferenciadiagnostico = em.merge(detallereferenciadiagnosticoListNewDetallereferenciadiagnostico);
                    if (oldReferenciaOfDetallereferenciadiagnosticoListNewDetallereferenciadiagnostico != null && !oldReferenciaOfDetallereferenciadiagnosticoListNewDetallereferenciadiagnostico.equals(referencia)) {
                        oldReferenciaOfDetallereferenciadiagnosticoListNewDetallereferenciadiagnostico.getDetallereferenciadiagnosticoList().remove(detallereferenciadiagnosticoListNewDetallereferenciadiagnostico);
                        oldReferenciaOfDetallereferenciadiagnosticoListNewDetallereferenciadiagnostico = em.merge(oldReferenciaOfDetallereferenciadiagnosticoListNewDetallereferenciadiagnostico);
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
                ReferenciaPK id = referencia.getReferenciaPK();
                if (findReferencia(id) == null) {
                    throw new NonexistentEntityException("The referencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ReferenciaPK id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Referencia referencia;
            try {
                referencia = em.getReference(Referencia.class, id);
                referencia.getReferenciaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The referencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallereferenciadiagnostico> detallereferenciadiagnosticoListOrphanCheck = referencia.getDetallereferenciadiagnosticoList();
            for (Detallereferenciadiagnostico detallereferenciadiagnosticoListOrphanCheckDetallereferenciadiagnostico : detallereferenciadiagnosticoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Referencia (" + referencia + ") cannot be destroyed since the Detallereferenciadiagnostico " + detallereferenciadiagnosticoListOrphanCheckDetallereferenciadiagnostico + " in its detallereferenciadiagnosticoList field has a non-nullable referencia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paciente paciente = referencia.getPaciente();
            if (paciente != null) {
                paciente.getReferenciaList().remove(referencia);
                paciente = em.merge(paciente);
            }
            em.remove(referencia);
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

    public List<Referencia> findReferenciaEntities() {
        return findReferenciaEntities(true, -1, -1);
    }

    public List<Referencia> findReferenciaEntities(int maxResults, int firstResult) {
        return findReferenciaEntities(false, maxResults, firstResult);
    }

    private List<Referencia> findReferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Referencia.class));
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

    public Referencia findReferencia(ReferenciaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Referencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getReferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Referencia> rt = cq.from(Referencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
