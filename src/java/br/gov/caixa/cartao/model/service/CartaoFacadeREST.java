/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.cartao.model.service;

import br.gov.caixa.cartao.model.Cartao;
import com.google.gson.Gson;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author c105118
 */
@Stateless
@Path("cartao")
public class CartaoFacadeREST extends AbstractFacade<Cartao> {

    @PersistenceContext(unitName = "cartaoPU")
    private EntityManager em;

    public CartaoFacadeREST() {
        super(Cartao.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Cartao entity) {
        super.create(entity);
    }
    
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void createT(String entity) {
        Gson g = new Gson();
        Cartao c = g.fromJson(entity, Cartao.class);
        super.create(c);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, String entity) {
        Gson g = new Gson();
        Cartao c = g.fromJson(entity, Cartao.class);
        super.edit(c);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Cartao find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Path("cliente/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Cartao> findAllCliente(@PathParam("id") Integer id) {
        
        Query q = getEntityManager().createNamedQuery("tbCartao.findByIdCliente");
        q.setParameter("id", id);
        
        
        return q.getResultList();
        
    }
    
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Cartao> getAll() {
        
        Query q = getEntityManager().createNamedQuery("tbCartao.findAll");

        return q.getResultList();
        
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
