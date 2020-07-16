package com.hdntec.gestao.domain.navios.entity.status;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.hdntec.gestao.domain.AuditTrail;


/**
 * O cliente é o elemento que é dono de uma demanda, ou seja, de uma {@link Carga}. 
 * As pilhas são construídas com objetivo de atender a uma dessas cargas e, por isso, são geralmente associadas a um cliente.
 * Os navios embarcam cargas de clientes.
 * 
 * @author andre
 * 
 */
@Entity
public class Cliente extends AuditTrail
{

   /** Serialização do objeto */
   private static final long serialVersionUID = -1055576442458617035L;

   private Long idCliente;

   /** Nome do cliente */
   private String nomeCliente;

   /** a taxa de estadia combinada com o cliente para cálculo de Demurrage */
   private Double taxaDeEstadia;

   /** o codigo do cliente no sistema externo */
   private String codigoCliente;

 
   public Cliente()
   {
   }

 
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "carga_seq")
   @SequenceGenerator(name = "carga_seq", sequenceName = "seqcarga")
   public Long getIdCliente()
   {
      return idCliente;
   }

   @Column(length = 60, nullable = true)
   public String getNomeCliente()
   {
      return nomeCliente;
   }

   @Column(nullable = true)
   public Double getTaxaDeEstadia()
   {
      return taxaDeEstadia;
   }

   @Column(nullable = false, length = 4)
   public String getCodigoCliente()
   {
      return codigoCliente;
   }

   public void setCodigoCliente(String codigoCliente)
   {
      this.codigoCliente = codigoCliente;
   }

   public void setIdCliente(Long idCliente)
   {
      this.idCliente = idCliente;
   }

   public void setNomeCliente(String nomeCliente)
   {
      this.nomeCliente = nomeCliente;
   }

   public void setTaxaDeEstadia(Double taxaDeEstadia)
   {
      this.taxaDeEstadia = taxaDeEstadia;
   }
   
   @Override
public String toString() {
       return getNomeCliente();
   }
   
   public static Cliente criarCliente(String nome,String codigoCliente ) {
       Cliente cliente1 = new Cliente();
       cliente1.setNomeCliente(nome);
       cliente1.setTaxaDeEstadia(0D);
       cliente1.setCodigoCliente(codigoCliente);
       cliente1.setDtInsert(new Date(System.currentTimeMillis()));
       cliente1.setIdUser(1L);
       return cliente1;
   }
}
