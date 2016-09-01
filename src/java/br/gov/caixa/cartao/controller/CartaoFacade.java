/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.cartao.controller;

import br.gov.caixa.cartao.model.Cartao;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author c105118
 */
@Stateless
public class CartaoFacade extends AbstractFacade<Cartao> {

    @PersistenceContext(unitName = "cartaoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CartaoFacade() {
        super(Cartao.class);
    }
    
}
