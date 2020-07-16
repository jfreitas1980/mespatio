/**
 * 
 */
package com.hdntec.gestao.exceptions;

import com.hdntec.gestao.util.PropertiesUtil;




/**
 * @author andre
 *
 */
public class ExcessoDeMaterialParaEmbarqueException extends Exception
{

   /**
    * Serialização
    */
   private static final long serialVersionUID = 1292733742484116364L;

   /**
    * 
    */
   public ExcessoDeMaterialParaEmbarqueException()
   {
      super(PropertiesUtil.getMessage("exception.excesso.material.reservado"));
   }

   /**
    * @param arg0
    * @param arg1
    */
   public ExcessoDeMaterialParaEmbarqueException(String arg0, Throwable arg1)
   {
      super(arg0, arg1);
   }

   /**
    * @param arg0
    */
   public ExcessoDeMaterialParaEmbarqueException(String arg0)
   {
      super(arg0);
   }

   /**
    * @param arg0
    */
   public ExcessoDeMaterialParaEmbarqueException(Throwable arg0)
   {
      super(arg0);
   }
   
}
