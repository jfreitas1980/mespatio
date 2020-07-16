/**
 * A fila de navios representa uma sequência de atendimento dos navios que chegarão para ser embarcados na Samarco.
 *
 * @author andre
 */
package com.hdntec.gestao.domain.navios.entity.status;

import java.io.Serializable;
import java.util.List;


public class FilaDeNavios implements Serializable
{

   /** Serialização do objeto */
   private static final long serialVersionUID = -5523535940225589983L;

   /** a lista de navios na fila */
   private List<Navio> listaDeNaviosNaFila;

      /**
    * Construtor Padrao.
    */
   public FilaDeNavios()
   {
   }

   public List<Navio> getListaDeNaviosNaFila()
   {
      return listaDeNaviosNaFila;
   }

   
   public void setListaDeNaviosNaFila(List<Navio> listaDeNaviosNaFila)
   {
      this.listaDeNaviosNaFila = listaDeNaviosNaFila;
   }
   
}
