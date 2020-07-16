package com.hdntec.gestao.domain.produto.factory;


import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleAmostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleOrientacaoEmbarque;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;

/**
 * Factory responsável por gerar implementações de  SvAnotacaoDAO.
 * @author <a href="mailto:hdn.oliveira@cflex.com.br">Fabio Oliveira Silva</a>
 *
 */
public class ItemDeControleFactory
{

   /** Instância singleton da fábrica. */
   private static ItemDeControleFactory instance = null;

   /**
    * Construtor privado.
    */
   private ItemDeControleFactory()
   {
   }

   /**
    * Retorna a instancia singleton da fábrica.
    * @return TransLogicDAOFactory
    */
   public static ItemDeControleFactory getInstance()
   {
      if (instance == null)
      {
         instance = new ItemDeControleFactory();
      }
      return instance;
   }

   public ItemDeControle create(final String who)
   {
      ItemDeControle result = null;

      try
      {
         Class<?> clazz = Class.forName(who);
         result = (ItemDeControle) clazz.newInstance();
      }
      catch (ClassCastException exc)
      {
         throw new RuntimeException("A classe " + who + " nao eh filha da classe ItemDeControle");
      }
      catch (ClassNotFoundException exc)
      {
         throw new RuntimeException("Nao foi possivel encontrar a classe " + who + " no seu CLASSPATH");
      }
      catch (InstantiationException exc)
      {
         throw new RuntimeException("Erro ao tentar instanciar a classe " + who);
      }
      catch (IllegalAccessException exc)
      {
         throw new RuntimeException("Acesso ilegal a classe  " + who);
      }
      return result;
   }

   public ItemDeControle create(ItemDeControle source)
   {
      ItemDeControle result = null;

      try
      {
         if (source.getClass().getName().equals(ItemDeControleAmostra.class.getName()))
         {
            result = new ItemDeControleAmostra();                       
         }
         else if (source.getClass().getName().equals(ItemDeControleQualidade.class.getName()))
         {
            result = new ItemDeControleQualidade();
          
         }
         else if (source.getClass().getName().equals(ItemDeControleOrientacaoEmbarque.class.getName()))
         {
            result = new ItemDeControleOrientacaoEmbarque();                      
         }
      }
      catch (ClassCastException exc)
      {
         throw new RuntimeException("A classe " + source.getClass().getName() + " nao eh filha da classe ItemDeControle");
      }
      return result;
   }
}
