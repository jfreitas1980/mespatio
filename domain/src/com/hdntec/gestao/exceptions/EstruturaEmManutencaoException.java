/**
 * 
 */
package com.hdntec.gestao.exceptions;

import com.hdntec.gestao.util.PropertiesUtil;



/**
 * 
 * Exception que é lançada quando uma estrutura que está em manutenção é selecionada 
 * 
 * @author andre
 *
 */
public class EstruturaEmManutencaoException extends Exception
{

   /**
    * Serialização
    */
   private static final long serialVersionUID = -8865039101741612668L;

   /**
    * 
    */
   public EstruturaEmManutencaoException()
   {
      super(PropertiesUtil.getMessage("exception.estrutura.manutencao"));
   }

   /**
    * @param arg0
    * @param arg1
    */
   public EstruturaEmManutencaoException(String arg0, Throwable arg1)
   {
      super(arg0, arg1);
   }

   /**
    * @param arg0
    */
   public EstruturaEmManutencaoException(String arg0)
   {
      super(arg0);
   }

   /**
    * @param arg0
    */
   public EstruturaEmManutencaoException(Throwable arg0)
   {
      super(arg0);
   }
   
   
}
