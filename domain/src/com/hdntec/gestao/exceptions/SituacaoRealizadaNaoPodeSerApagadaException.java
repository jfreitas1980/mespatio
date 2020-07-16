/**
 * 
 */
package com.hdntec.gestao.exceptions;

import com.hdntec.gestao.util.PropertiesUtil;


/**
 * @author andre
 *
 */
public class SituacaoRealizadaNaoPodeSerApagadaException extends Exception
{

   /**
    * serial gerado
    */
   private static final long serialVersionUID = 3726733951989291468L;

   /**
    * 
    */
   public SituacaoRealizadaNaoPodeSerApagadaException()
   {
      super(PropertiesUtil.getMessage("exception.situacao.realizada.naopode.ser.apagada"));
   }

   /**
    * @param message
    * @param cause
    */
   public SituacaoRealizadaNaoPodeSerApagadaException(String message, Throwable cause)
   {
      super(message, cause);
   }

   /**
    * @param message
    */
   public SituacaoRealizadaNaoPodeSerApagadaException(String message)
   {
      super(message);
   }

   /**
    * @param cause
    */
   public SituacaoRealizadaNaoPodeSerApagadaException(Throwable cause)
   {
      super(cause);
   }   
}
