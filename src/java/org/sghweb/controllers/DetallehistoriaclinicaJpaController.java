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
import org.sghweb.jpa.Cita;
import org.sghweb.jpa.Historiaclinica;
import org.sghweb.jpa.Citt;
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
import org.sghweb.jpa.Detalleorden;
import org.sghweb.jpa.Detallediagnostico;
import org.sghweb.jpa.Detallehistoriaclinica;
import org.sghweb.jpa.DetallehistoriaclinicaPK;

/**
 *
 * @author Roberto
 */
public class DetallehistoriaclinicaJpaController implements Serializable {

    public DetallehistoriaclinicaJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    
    public void create(Detallehistoriaclinica detallehistoriaclinica) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (detallehistoriaclinica.getDetallehistoriaclinicaPK() == null) {
            detallehistoriaclinica.setDetallehistoriaclinicaPK(new DetallehistoriaclinicaPK());
        }
        if (detallehistoriaclinica.getCittList() == null) {
            detallehistoriaclinica.setCittList(new ArrayList<Citt>());
        }
        if (detallehistoriaclinica.getDetalleordenList() == null) {
            detallehistoriaclinica.setDetalleordenList(new ArrayList<Detalleorden>());
        }
        if (detallehistoriaclinica.getDetallediagnosticoList() == null) {
            detallehistoriaclinica.setDetallediagnosticoList(new ArrayList<Detallediagnostico>());
        }
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setHistoriaClinicaautogenerado(detallehistoriaclinica.getHistoriaclinica().getHistoriaclinicaPK().getAutogenerado());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setCitaactoMedico(detallehistoriaclinica.getCita().getCitaPK().getActoMedico());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setHistoriaClinicanumeroRegistro(detallehistoriaclinica.getHistoriaclinica().getHistoriaclinicaPK().getNumeroRegistro());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setHistoriaClinicaPacientedni(detallehistoriaclinica.getHistoriaclinica().getHistoriaclinicaPK().getPacientedni());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setCitaMedicodni(detallehistoriaclinica.getCita().getCitaPK().getMedicodni());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setCitaMedicocmp(detallehistoriaclinica.getCita().getCitaPK().getMedicocmp());
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Cita cita = detallehistoriaclinica.getCita();
            if (cita != null) {
                cita = em.getReference(cita.getClass(), cita.getCitaPK());
                detallehistoriaclinica.setCita(cita);
            }
            Historiaclinica historiaclinica = detallehistoriaclinica.getHistoriaclinica();
            if (historiaclinica != null) {
                historiaclinica = em.getReference(historiaclinica.getClass(), historiaclinica.getHistoriaclinicaPK());
                detallehistoriaclinica.setHistoriaclinica(historiaclinica);
            }
            List<Citt> attachedCittList = new ArrayList<Citt>();
            for (Citt cittListCittToAttach : detallehistoriaclinica.getCittList()) {
                cittListCittToAttach = em.getReference(cittListCittToAttach.getClass(), cittListCittToAttach.getCittPK());
                attachedCittList.add(cittListCittToAttach);
            }
            detallehistoriaclinica.setCittList(attachedCittList);
            List<Detalleorden> attachedDetalleordenList = new ArrayList<Detalleorden>();
            for (Detalleorden detalleordenListDetalleordenToAttach : detallehistoriaclinica.getDetalleordenList()) {
                detalleordenListDetalleordenToAttach = em.getReference(detalleordenListDetalleordenToAttach.getClass(), detalleordenListDetalleordenToAttach.getDetalleordenPK());
                attachedDetalleordenList.add(detalleordenListDetalleordenToAttach);
            }
            detallehistoriaclinica.setDetalleordenList(attachedDetalleordenList);
            List<Detallediagnostico> attachedDetallediagnosticoList = new ArrayList<Detallediagnostico>();
            for (Detallediagnostico detallediagnosticoListDetallediagnosticoToAttach : detallehistoriaclinica.getDetallediagnosticoList()) {
                detallediagnosticoListDetallediagnosticoToAttach = em.getReference(detallediagnosticoListDetallediagnosticoToAttach.getClass(), detallediagnosticoListDetallediagnosticoToAttach.getDetallediagnosticoPK());
                attachedDetallediagnosticoList.add(detallediagnosticoListDetallediagnosticoToAttach);
            }
            detallehistoriaclinica.setDetallediagnosticoList(attachedDetallediagnosticoList);
            em.persist(detallehistoriaclinica);
            if (cita != null) {
                cita.getDetallehistoriaclinicaList().add(detallehistoriaclinica);
                cita = em.merge(cita);
            }
            if (historiaclinica != null) {
                historiaclinica.getDetallehistoriaclinicaList().add(detallehistoriaclinica);
                historiaclinica = em.merge(historiaclinica);
            }
            for (Citt cittListCitt : detallehistoriaclinica.getCittList()) {
                Detallehistoriaclinica oldDetallehistoriaclinicaOfCittListCitt = cittListCitt.getDetallehistoriaclinica();
                cittListCitt.setDetallehistoriaclinica(detallehistoriaclinica);
                cittListCitt = em.merge(cittListCitt);
                if (oldDetallehistoriaclinicaOfCittListCitt != null) {
                    oldDetallehistoriaclinicaOfCittListCitt.getCittList().remove(cittListCitt);
                    oldDetallehistoriaclinicaOfCittListCitt = em.merge(oldDetallehistoriaclinicaOfCittListCitt);
                }
            }
            for (Detalleorden detalleordenListDetalleorden : detallehistoriaclinica.getDetalleordenList()) {
                Detallehistoriaclinica oldDetallehistoriaclinicaOfDetalleordenListDetalleorden = detalleordenListDetalleorden.getDetallehistoriaclinica();
                detalleordenListDetalleorden.setDetallehistoriaclinica(detallehistoriaclinica);
                detalleordenListDetalleorden = em.merge(detalleordenListDetalleorden);
                if (oldDetallehistoriaclinicaOfDetalleordenListDetalleorden != null) {
                    oldDetallehistoriaclinicaOfDetalleordenListDetalleorden.getDetalleordenList().remove(detalleordenListDetalleorden);
                    oldDetallehistoriaclinicaOfDetalleordenListDetalleorden = em.merge(oldDetallehistoriaclinicaOfDetalleordenListDetalleorden);
                }
            }
            for (Detallediagnostico detallediagnosticoListDetallediagnostico : detallehistoriaclinica.getDetallediagnosticoList()) {
                Detallehistoriaclinica oldDetallehistoriaclinicaOfDetallediagnosticoListDetallediagnostico = detallediagnosticoListDetallediagnostico.getDetallehistoriaclinica();
                detallediagnosticoListDetallediagnostico.setDetallehistoriaclinica(detallehistoriaclinica);
                detallediagnosticoListDetallediagnostico = em.merge(detallediagnosticoListDetallediagnostico);
                if (oldDetallehistoriaclinicaOfDetallediagnosticoListDetallediagnostico != null) {
                    oldDetallehistoriaclinicaOfDetallediagnosticoListDetallediagnostico.getDetallediagnosticoList().remove(detallediagnosticoListDetallediagnostico);
                    oldDetallehistoriaclinicaOfDetallediagnosticoListDetallediagnostico = em.merge(oldDetallehistoriaclinicaOfDetallediagnosticoListDetallediagnostico);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallehistoriaclinica(detallehistoriaclinica.getDetallehistoriaclinicaPK()) != null) {
                throw new PreexistingEntityException("Detallehistoriaclinica " + detallehistoriaclinica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallehistoriaclinica detallehistoriaclinica) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setHistoriaClinicaautogenerado(detallehistoriaclinica.getHistoriaclinica().getHistoriaclinicaPK().getAutogenerado());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setCitaactoMedico(detallehistoriaclinica.getCita().getCitaPK().getActoMedico());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setHistoriaClinicanumeroRegistro(detallehistoriaclinica.getHistoriaclinica().getHistoriaclinicaPK().getNumeroRegistro());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setHistoriaClinicaPacientedni(detallehistoriaclinica.getHistoriaclinica().getHistoriaclinicaPK().getPacientedni());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setCitaMedicodni(detallehistoriaclinica.getCita().getCitaPK().getMedicodni());
        detallehistoriaclinica.getDetallehistoriaclinicaPK().setCitaMedicocmp(detallehistoriaclinica.getCita().getCitaPK().getMedicocmp());
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Detallehistoriaclinica persistentDetallehistoriaclinica = em.find(Detallehistoriaclinica.class, detallehistoriaclinica.getDetallehistoriaclinicaPK());
            Cita citaOld = persistentDetallehistoriaclinica.getCita();
            Cita citaNew = detallehistoriaclinica.getCita();
            Historiaclinica historiaclinicaOld = persistentDetallehistoriaclinica.getHistoriaclinica();
            Historiaclinica historiaclinicaNew = detallehistoriaclinica.getHistoriaclinica();
            List<Citt> cittListOld = persistentDetallehistoriaclinica.getCittList();
            List<Citt> cittListNew = detallehistoriaclinica.getCittList();
            List<Detalleorden> detalleordenListOld = persistentDetallehistoriaclinica.getDetalleordenList();
            List<Detalleorden> detalleordenListNew = detallehistoriaclinica.getDetalleordenList();
            List<Detallediagnostico> detallediagnosticoListOld = persistentDetallehistoriaclinica.getDetallediagnosticoList();
            List<Detallediagnostico> detallediagnosticoListNew = detallehistoriaclinica.getDetallediagnosticoList();
            List<String> illegalOrphanMessages = null;
            for (Citt cittListOldCitt : cittListOld) {
                if (!cittListNew.contains(cittListOldCitt)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citt " + cittListOldCitt + " since its detallehistoriaclinica field is not nullable.");
                }
            }
            for (Detalleorden detalleordenListOldDetalleorden : detalleordenListOld) {
                if (!detalleordenListNew.contains(detalleordenListOldDetalleorden)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleorden " + detalleordenListOldDetalleorden + " since its detallehistoriaclinica field is not nullable.");
                }
            }
            for (Detallediagnostico detallediagnosticoListOldDetallediagnostico : detallediagnosticoListOld) {
                if (!detallediagnosticoListNew.contains(detallediagnosticoListOldDetallediagnostico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallediagnostico " + detallediagnosticoListOldDetallediagnostico + " since its detallehistoriaclinica field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (citaNew != null) {
                citaNew = em.getReference(citaNew.getClass(), citaNew.getCitaPK());
                detallehistoriaclinica.setCita(citaNew);
            }
            if (historiaclinicaNew != null) {
                historiaclinicaNew = em.getReference(historiaclinicaNew.getClass(), historiaclinicaNew.getHistoriaclinicaPK());
                detallehistoriaclinica.setHistoriaclinica(historiaclinicaNew);
            }
            List<Citt> attachedCittListNew = new ArrayList<Citt>();
            for (Citt cittListNewCittToAttach : cittListNew) {
                cittListNewCittToAttach = em.getReference(cittListNewCittToAttach.getClass(), cittListNewCittToAttach.getCittPK());
                attachedCittListNew.add(cittListNewCittToAttach);
            }
            cittListNew = attachedCittListNew;
            detallehistoriaclinica.setCittList(cittListNew);
            List<Detalleorden> attachedDetalleordenListNew = new ArrayList<Detalleorden>();
            for (Detalleorden detalleordenListNewDetalleordenToAttach : detalleordenListNew) {
                detalleordenListNewDetalleordenToAttach = em.getReference(detalleordenListNewDetalleordenToAttach.getClass(), detalleordenListNewDetalleordenToAttach.getDetalleordenPK());
                attachedDetalleordenListNew.add(detalleordenListNewDetalleordenToAttach);
            }
            detalleordenListNew = attachedDetalleordenListNew;
            detallehistoriaclinica.setDetalleordenList(detalleordenListNew);
            List<Detallediagnostico> attachedDetallediagnosticoListNew = new ArrayList<Detallediagnostico>();
            for (Detallediagnostico detallediagnosticoListNewDetallediagnosticoToAttach : detallediagnosticoListNew) {
                detallediagnosticoListNewDetallediagnosticoToAttach = em.getReference(detallediagnosticoListNewDetallediagnosticoToAttach.getClass(), detallediagnosticoListNewDetallediagnosticoToAttach.getDetallediagnosticoPK());
                attachedDetallediagnosticoListNew.add(detallediagnosticoListNewDetallediagnosticoToAttach);
            }
            detallediagnosticoListNew = attachedDetallediagnosticoListNew;
            detallehistoriaclinica.setDetallediagnosticoList(detallediagnosticoListNew);
            detallehistoriaclinica = em.merge(detallehistoriaclinica);
            if (citaOld != null && !citaOld.equals(citaNew)) {
                citaOld.getDetallehistoriaclinicaList().remove(detallehistoriaclinica);
                citaOld = em.merge(citaOld);
            }
            if (citaNew != null && !citaNew.equals(citaOld)) {
                citaNew.getDetallehistoriaclinicaList().add(detallehistoriaclinica);
                citaNew = em.merge(citaNew);
            }
            if (historiaclinicaOld != null && !historiaclinicaOld.equals(historiaclinicaNew)) {
                historiaclinicaOld.getDetallehistoriaclinicaList().remove(detallehistoriaclinica);
                historiaclinicaOld = em.merge(historiaclinicaOld);
            }
            if (historiaclinicaNew != null && !historiaclinicaNew.equals(historiaclinicaOld)) {
                historiaclinicaNew.getDetallehistoriaclinicaList().add(detallehistoriaclinica);
                historiaclinicaNew = em.merge(historiaclinicaNew);
            }
            for (Citt cittListNewCitt : cittListNew) {
                if (!cittListOld.contains(cittListNewCitt)) {
                    Detallehistoriaclinica oldDetallehistoriaclinicaOfCittListNewCitt = cittListNewCitt.getDetallehistoriaclinica();
                    cittListNewCitt.setDetallehistoriaclinica(detallehistoriaclinica);
                    cittListNewCitt = em.merge(cittListNewCitt);
                    if (oldDetallehistoriaclinicaOfCittListNewCitt != null && !oldDetallehistoriaclinicaOfCittListNewCitt.equals(detallehistoriaclinica)) {
                        oldDetallehistoriaclinicaOfCittListNewCitt.getCittList().remove(cittListNewCitt);
                        oldDetallehistoriaclinicaOfCittListNewCitt = em.merge(oldDetallehistoriaclinicaOfCittListNewCitt);
                    }
                }
            }
            for (Detalleorden detalleordenListNewDetalleorden : detalleordenListNew) {
                if (!detalleordenListOld.contains(detalleordenListNewDetalleorden)) {
                    Detallehistoriaclinica oldDetallehistoriaclinicaOfDetalleordenListNewDetalleorden = detalleordenListNewDetalleorden.getDetallehistoriaclinica();
                    detalleordenListNewDetalleorden.setDetallehistoriaclinica(detallehistoriaclinica);
                    detalleordenListNewDetalleorden = em.merge(detalleordenListNewDetalleorden);
                    if (oldDetallehistoriaclinicaOfDetalleordenListNewDetalleorden != null && !oldDetallehistoriaclinicaOfDetalleordenListNewDetalleorden.equals(detallehistoriaclinica)) {
                        oldDetallehistoriaclinicaOfDetalleordenListNewDetalleorden.getDetalleordenList().remove(detalleordenListNewDetalleorden);
                        oldDetallehistoriaclinicaOfDetalleordenListNewDetalleorden = em.merge(oldDetallehistoriaclinicaOfDetalleordenListNewDetalleorden);
                    }
                }
            }
            for (Detallediagnostico detallediagnosticoListNewDetallediagnostico : detallediagnosticoListNew) {
                if (!detallediagnosticoListOld.contains(detallediagnosticoListNewDetallediagnostico)) {
                    Detallehistoriaclinica oldDetallehistoriaclinicaOfDetallediagnosticoListNewDetallediagnostico = detallediagnosticoListNewDetallediagnostico.getDetallehistoriaclinica();
                    detallediagnosticoListNewDetallediagnostico.setDetallehistoriaclinica(detallehistoriaclinica);
                    detallediagnosticoListNewDetallediagnostico = em.merge(detallediagnosticoListNewDetallediagnostico);
                    if (oldDetallehistoriaclinicaOfDetallediagnosticoListNewDetallediagnostico != null && !oldDetallehistoriaclinicaOfDetallediagnosticoListNewDetallediagnostico.equals(detallehistoriaclinica)) {
                        oldDetallehistoriaclinicaOfDetallediagnosticoListNewDetallediagnostico.getDetallediagnosticoList().remove(detallediagnosticoListNewDetallediagnostico);
                        oldDetallehistoriaclinicaOfDetallediagnosticoListNewDetallediagnostico = em.merge(oldDetallehistoriaclinicaOfDetallediagnosticoListNewDetallediagnostico);
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
                DetallehistoriaclinicaPK id = detallehistoriaclinica.getDetallehistoriaclinicaPK();
                if (findDetallehistoriaclinica(id) == null) {
                    throw new NonexistentEntityException("The detallehistoriaclinica with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetallehistoriaclinicaPK id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context initCtx = new InitialContext(); 
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Detallehistoriaclinica detallehistoriaclinica;
            try {
                detallehistoriaclinica = em.getReference(Detallehistoriaclinica.class, id);
                detallehistoriaclinica.getDetallehistoriaclinicaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallehistoriaclinica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Citt> cittListOrphanCheck = detallehistoriaclinica.getCittList();
            for (Citt cittListOrphanCheckCitt : cittListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Detallehistoriaclinica (" + detallehistoriaclinica + ") cannot be destroyed since the Citt " + cittListOrphanCheckCitt + " in its cittList field has a non-nullable detallehistoriaclinica field.");
            }
            List<Detalleorden> detalleordenListOrphanCheck = detallehistoriaclinica.getDetalleordenList();
            for (Detalleorden detalleordenListOrphanCheckDetalleorden : detalleordenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Detallehistoriaclinica (" + detallehistoriaclinica + ") cannot be destroyed since the Detalleorden " + detalleordenListOrphanCheckDetalleorden + " in its detalleordenList field has a non-nullable detallehistoriaclinica field.");
            }
            List<Detallediagnostico> detallediagnosticoListOrphanCheck = detallehistoriaclinica.getDetallediagnosticoList();
            for (Detallediagnostico detallediagnosticoListOrphanCheckDetallediagnostico : detallediagnosticoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Detallehistoriaclinica (" + detallehistoriaclinica + ") cannot be destroyed since the Detallediagnostico " + detallediagnosticoListOrphanCheckDetallediagnostico + " in its detallediagnosticoList field has a non-nullable detallehistoriaclinica field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cita cita = detallehistoriaclinica.getCita();
            if (cita != null) {
                cita.getDetallehistoriaclinicaList().remove(detallehistoriaclinica);
                cita = em.merge(cita);
            }
            Historiaclinica historiaclinica = detallehistoriaclinica.getHistoriaclinica();
            if (historiaclinica != null) {
                historiaclinica.getDetallehistoriaclinicaList().remove(detallehistoriaclinica);
                historiaclinica = em.merge(historiaclinica);
            }
            em.remove(detallehistoriaclinica);
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

    public List<Detallehistoriaclinica> findDetallehistoriaclinicaEntities() {
        return findDetallehistoriaclinicaEntities(true, -1, -1);
    }

    public List<Detallehistoriaclinica> findDetallehistoriaclinicaEntities(int maxResults, int firstResult) {
        return findDetallehistoriaclinicaEntities(false, maxResults, firstResult);
    }

    private List<Detallehistoriaclinica> findDetallehistoriaclinicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallehistoriaclinica.class));
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

    public Detallehistoriaclinica findDetallehistoriaclinica(DetallehistoriaclinicaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallehistoriaclinica.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallehistoriaclinicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallehistoriaclinica> rt = cq.from(Detallehistoriaclinica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
