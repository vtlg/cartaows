/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.cartao.repository;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.gov.caixa.cartao.model.Cartao;
import br.gov.caixa.cartao.model.Cliente;
import br.gov.caixa.cartao.repository.exceptions.NonexistentEntityException;
import br.gov.caixa.cartao.repository.exceptions.PreexistingEntityException;
import br.gov.caixa.cartao.repository.exceptions.RollbackFailureException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author c105118
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cliente.getTbCartaoSet() == null) {
            cliente.setTbCartaoSet(new HashSet<Cartao>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Set<Cartao> attachedTbCartaoSet = new HashSet<Cartao>();
            for (Cartao tbCartaoSetCartaoToAttach : cliente.getTbCartaoSet()) {
                tbCartaoSetCartaoToAttach = em.getReference(tbCartaoSetCartaoToAttach.getClass(), tbCartaoSetCartaoToAttach.getNumero());
                attachedTbCartaoSet.add(tbCartaoSetCartaoToAttach);
            }
            cliente.setTbCartaoSet(attachedTbCartaoSet);
            em.persist(cliente);
            for (Cartao tbCartaoSetCartao : cliente.getTbCartaoSet()) {
                Cliente oldIdClienteOfTbCartaoSetCartao = tbCartaoSetCartao.getIdCliente();
                tbCartaoSetCartao.setIdCliente(cliente);
                tbCartaoSetCartao = em.merge(tbCartaoSetCartao);
                if (oldIdClienteOfTbCartaoSetCartao != null) {
                    oldIdClienteOfTbCartaoSetCartao.getTbCartaoSet().remove(tbCartaoSetCartao);
                    oldIdClienteOfTbCartaoSetCartao = em.merge(oldIdClienteOfTbCartaoSetCartao);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCliente(cliente.getIdCliente()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            Set<Cartao> tbCartaoSetOld = persistentCliente.getTbCartaoSet();
            Set<Cartao> tbCartaoSetNew = cliente.getTbCartaoSet();
            Set<Cartao> attachedTbCartaoSetNew = new HashSet<Cartao>();
            for (Cartao tbCartaoSetNewCartaoToAttach : tbCartaoSetNew) {
                tbCartaoSetNewCartaoToAttach = em.getReference(tbCartaoSetNewCartaoToAttach.getClass(), tbCartaoSetNewCartaoToAttach.getNumero());
                attachedTbCartaoSetNew.add(tbCartaoSetNewCartaoToAttach);
            }
            tbCartaoSetNew = attachedTbCartaoSetNew;
            cliente.setTbCartaoSet(tbCartaoSetNew);
            cliente = em.merge(cliente);
            for (Cartao tbCartaoSetOldCartao : tbCartaoSetOld) {
                if (!tbCartaoSetNew.contains(tbCartaoSetOldCartao)) {
                    tbCartaoSetOldCartao.setIdCliente(null);
                    tbCartaoSetOldCartao = em.merge(tbCartaoSetOldCartao);
                }
            }
            for (Cartao tbCartaoSetNewCartao : tbCartaoSetNew) {
                if (!tbCartaoSetOld.contains(tbCartaoSetNewCartao)) {
                    Cliente oldIdClienteOfTbCartaoSetNewCartao = tbCartaoSetNewCartao.getIdCliente();
                    tbCartaoSetNewCartao.setIdCliente(cliente);
                    tbCartaoSetNewCartao = em.merge(tbCartaoSetNewCartao);
                    if (oldIdClienteOfTbCartaoSetNewCartao != null && !oldIdClienteOfTbCartaoSetNewCartao.equals(cliente)) {
                        oldIdClienteOfTbCartaoSetNewCartao.getTbCartaoSet().remove(tbCartaoSetNewCartao);
                        oldIdClienteOfTbCartaoSetNewCartao = em.merge(oldIdClienteOfTbCartaoSetNewCartao);
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
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            Set<Cartao> tbCartaoSet = cliente.getTbCartaoSet();
            for (Cartao tbCartaoSetCartao : tbCartaoSet) {
                tbCartaoSetCartao.setIdCliente(null);
                tbCartaoSetCartao = em.merge(tbCartaoSetCartao);
            }
            em.remove(cliente);
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

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
