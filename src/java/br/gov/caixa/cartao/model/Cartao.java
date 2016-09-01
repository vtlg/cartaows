/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.cartao.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author c105118
 */
@Entity
@Table(name = "tb_cartao", catalog = "postgres", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "tbCartao.findAll", query = "SELECT t FROM Cartao t"),
    @NamedQuery(name = "tbCartao.findByDtValidade", query = "SELECT t FROM Cartao t WHERE t.dtValidade = :dtValidade"),
    @NamedQuery(name = "tbCartao.findByIdCliente", query = "SELECT t FROM Cartao t WHERE t.idCliente.idCliente = :id"),
    @NamedQuery(name = "tbCartao.findByNumero", query = "SELECT t FROM Cartao t WHERE t.numero = :numero")})
public class Cartao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "dt_validade")
    @Temporal(TemporalType.DATE)
    private Date dtValidade;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "numero")
    private String numero;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    @ManyToOne
    private Cliente idCliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nome")
    private String nome;

    public Cartao() {
    }

    public Cartao(String numero) {
        this.numero = numero;
    }

    public Date getDtValidade(){
        /*
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dtValidade = df.parse(dtValidade.toString());
                    } catch (ParseException ex) {
            Logger.getLogger(Cartao.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        return dtValidade;
    }

    public void setDtValidade(Date dtValidade) {
        this.dtValidade = dtValidade;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }
    
    public String setNome(String nome) {
        return this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cartao)) {
            return false;
        }
        Cartao other = (Cartao) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.caixa.cartao.tb_cartao[ numero=" + numero + " ]";
    }
    
}
