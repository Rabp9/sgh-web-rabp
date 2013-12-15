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
import org.sghweb.jpa.Detalleserviciomedico;
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
import org.sghweb.jpa.Cita;
import org.sghweb.jpa.Medico;
import org.sghweb.jpa.MedicoPK;

/**
 *
 * @author Roberto
 */
public class MedicoJpaController implements Serializable {

    public MedicoJpaController(UserTransaction utx, EntityManagerFactory emf) {
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

    public void create(Medico medico) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (medico.getMedicoPK() == null) {
            medico.setMedicoPK(new MedicoPK());
        }
        if (medico.getDetalleserviciomedicoList() == null) {
            medico.setDetalleserviciomedicoList(new ArrayList<Detalleserviciomedico>());
        }
        if (medico.getCitaList() == null) {
            medico.setCitaList(new ArrayList<Cita>());
        }
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            List<Detalleserviciomedico> attachedDetalleserviciomedicoList = new ArrayList<Detalleserviciomedico>();
            for (Detalleserviciomedico detalleserviciomedicoListDetalleserviciomedicoToAttach : medico.getDetalleserviciomedicoList()) {
                detalleserviciomedicoListDetalleserviciomedicoToAttach = em.getReference(detalleserviciomedicoListDetalleserviciomedicoToAttach.getClass(), detalleserviciomedicoListDetalleserviciomedicoToAttach.getDetalleserviciomedicoPK());
                attachedDetalleserviciomedicoList.add(detalleserviciomedicoListDetalleserviciomedicoToAttach);
            }
            medico.setDetalleserviciomedicoList(attachedDetalleserviciomedicoList);
            List<Cita> attachedCitaList = new ArrayList<Cita>();
            for (Cita citaListCitaToAttach : medico.getCitaList()) {
                citaListCitaToAttach = em.getReference(citaListCitaToAttach.getClass(), citaListCitaToAttach.getCitaPK());
                attachedCitaList.add(citaListCitaToAttach);
            }
            medico.setCitaList(attachedCitaList);
            em.persist(medico);
            for (Detalleserviciomedico detalleserviciomedicoListDetalleserviciomedico : medico.getDetalleserviciomedicoList()) {
                Medico oldMedicoOfDetalleserviciomedicoListDetalleserviciomedico = detalleserviciomedicoListDetalleserviciomedico.getMedico();
                detalleserviciomedicoListDetalleserviciomedico.setMedico(medico);
                detalleserviciomedicoListDetalleserviciomedico = em.merge(detalleserviciomedicoListDetalleserviciomedico);
                if (oldMedicoOfDetalleserviciomedicoListDetalleserviciomedico != null) {
                    oldMedicoOfDetalleserviciomedicoListDetalleserviciomedico.getDetalleserviciomedicoList().remove(detalleserviciomedicoListDetalleserviciomedico);
                    oldMedicoOfDetalleserviciomedicoListDetalleserviciomedico = em.merge(oldMedicoOfDetalleserviciomedicoListDetalleserviciomedico);
                }
            }
            for (Cita citaListCita : medico.getCitaList()) {
                Medico oldMedicoOfCitaListCita = citaListCita.getMedico();
                citaListCita.setMedico(medico);
                citaListCita = em.merge(citaListCita);
                if (oldMedicoOfCitaListCita != null) {
                    oldMedicoOfCitaListCita.getCitaList().remove(citaListCita);
                    oldMedicoOfCitaListCita = em.merge(oldMedicoOfCitaListCita);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMedico(medico.getMedicoPK()) != null) {
                throw new PreexistingEntityException("Medico " + medico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medico medico) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Medico persistentMedico = em.find(Medico.class, medico.getMedicoPK());
            List<Detalleserviciomedico> detalleserviciomedicoListOld = persistentMedico.getDetalleserviciomedicoList();
            List<Detalleserviciomedico> detalleserviciomedicoListNew = medico.getDetalleserviciomedicoList();
            List<Cita> citaListOld = persistentMedico.getCitaList();
            List<Cita> citaListNew = medico.getCitaList();
            List<String> illegalOrphanMessages = null;
            for (Detalleserviciomedico detalleserviciomedicoListOldDetalleserviciomedico : detalleserviciomedicoListOld) {
                if (!detalleserviciomedicoListNew.contains(detalleserviciomedicoListOldDetalleserviciomedico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleserviciomedico " + detalleserviciomedicoListOldDetalleserviciomedico + " since its medico field is not nullable.");
                }
            }
            for (Cita citaListOldCita : citaListOld) {
                if (!citaListNew.contains(citaListOldCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cita " + citaListOldCita + " since its medico field is not nullable.");
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
            medico.setDetalleserviciomedicoList(detalleserviciomedicoListNew);
            List<Cita> attachedCitaListNew = new ArrayList<Cita>();
            for (Cita citaListNewCitaToAttach : citaListNew) {
                citaListNewCitaToAttach = em.getReference(citaListNewCitaToAttach.getClass(), citaListNewCitaToAttach.getCitaPK());
                attachedCitaListNew.add(citaListNewCitaToAttach);
            }
            citaListNew = attachedCitaListNew;
            medico.setCitaList(citaListNew);
            medico = em.merge(medico);
            for (Detalleserviciomedico detalleserviciomedicoListNewDetalleserviciomedico : detalleserviciomedicoListNew) {
                if (!detalleserviciomedicoListOld.contains(detalleserviciomedicoListNewDetalleserviciomedico)) {
                    Medico oldMedicoOfDetalleserviciomedicoListNewDetalleserviciomedico = detalleserviciomedicoListNewDetalleserviciomedico.getMedico();
                    detalleserviciomedicoListNewDetalleserviciomedico.setMedico(medico);
                    detalleserviciomedicoListNewDetalleserviciomedico = em.merge(detalleserviciomedicoListNewDetalleserviciomedico);
                    if (oldMedicoOfDetalleserviciomedicoListNewDetalleserviciomedico != null && !oldMedicoOfDetalleserviciomedicoListNewDetalleserviciomedico.equals(medico)) {
                        oldMedicoOfDetalleserviciomedicoListNewDetalleserviciomedico.getDetalleserviciomedicoList().remove(detalleserviciomedicoListNewDetalleserviciomedico);
                        oldMedicoOfDetalleserviciomedicoListNewDetalleserviciomedico = em.merge(oldMedicoOfDetalleserviciomedicoListNewDetalleserviciomedico);
                    }
                }
            }
            for (Cita citaListNewCita : citaListNew) {
                if (!citaListOld.contains(citaListNewCita)) {
                    Medico oldMedicoOfCitaListNewCita = citaListNewCita.getMedico();
                    citaListNewCita.setMedico(medico);
                    citaListNewCita = em.merge(citaListNewCita);
                    if (oldMedicoOfCitaListNewCita != null && !oldMedicoOfCitaListNewCita.equals(medico)) {
                        oldMedicoOfCitaListNewCita.getCitaList().remove(citaListNewCita);
                        oldMedicoOfCitaListNewCita = em.merge(oldMedicoOfCitaListNewCita);
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
                MedicoPK id = medico.getMedicoPK();
                if (findMedico(id) == null) {
                    throw new NonexistentEntityException("The medico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MedicoPK id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Medico medico;
            try {
                medico = em.getReference(Medico.class, id);
                medico.getMedicoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleserviciomedico> detalleserviciomedicoListOrphanCheck = medico.getDetalleserviciomedicoList();
            for (Detalleserviciomedico detalleserviciomedicoListOrphanCheckDetalleserviciomedico : detalleserviciomedicoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medico (" + medico + ") cannot be destroyed since the Detalleserviciomedico " + detalleserviciomedicoListOrphanCheckDetalleserviciomedico + " in its detalleserviciomedicoList field has a non-nullable medico field.");
            }
            List<Cita> citaListOrphanCheck = medico.getCitaList();
            for (Cita citaListOrphanCheckCita : citaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medico (" + medico + ") cannot be destroyed since the Cita " + citaListOrphanCheckCita + " in its citaList field has a non-nullable medico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(medico);
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

    public List<Medico> findMedicoEntities() {
        return findMedicoEntities(true, -1, -1);
    }

    public List<Medico> findMedicoEntities(int maxResults, int firstResult) {
        return findMedicoEntities(false, maxResults, firstResult);
    }

    private List<Medico> findMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medico.class));
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

    public Medico findMedico(MedicoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medico.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medico> rt = cq.from(Medico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
