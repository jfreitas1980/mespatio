/**
 * 
 */
package com.hdntec.gestao.exceptions;

import com.hdntec.gestao.util.PropertiesUtil;



/**
 * @author andre
 *
 */
public class MaquinaEstaIndisponivelException extends Exception
{

   /**
    * Serialização
    */
   private static final long serialVersionUID = 4417922383426586570L;

   /**
    * 
    */
   public MaquinaEstaIndisponivelException()
   {
      super(PropertiesUtil.getMessage("exception.maquina.indisponivel.naopode.ser.selecionada"));
   }

   /**
    * @param arg0
    * @param arg1
    */
   public MaquinaEstaIndisponivelException(String arg0, Throwable arg1)
   {
      super(arg0, arg1);
   }

   /**
    * @param arg0
    */
   public MaquinaEstaIndisponivelException(String arg0)
   {
      super(arg0);
   }

   /**
    * @param arg0
    */
   public MaquinaEstaIndisponivelException(Throwable arg0)
   {
      super(arg0);
   }
   
   
}
