/**
 * 
 */
package com.hdntec.gestao.domain.produto.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Rastreabilidade
 * @author andre
 *
 */
public class RastreabilidadeDAO extends AbstractGenericDAO<Rastreabilidade>
{
   
   /**
    * Salva objeto rastreabilidade na entidade
    * @param {@link RastreabilidadeDAO}
    */
   public Rastreabilidade salvarRastreabilidade(Rastreabilidade rastreabilidade) throws ErroSistemicoException
   {
      try
      {
         Rastreabilidade rastreabilidadeSalva = super.salvar(rastreabilidade);
//         super.encerrarSessao();
         return rastreabilidadeSalva;
      } catch (HibernateException hbEx)
      {
         hbEx.printStackTrace();
         throw new ErroSistemicoException(hbEx.getMessage());
      }
   }

   /**
    * Altera o objeto rastreabilidade na entidade
    * @param {@link RastreabilidadeDAO}
    */
   public void alterarRastreabilidade(Rastreabilidade rastreabilidade) throws ErroSistemicoException {
       try {
           super.atualizar(rastreabilidade);
//           super.encerrarSessao();
       } catch (HibernateException hbEx) 
       {
           hbEx.printStackTrace();
           throw new ErroSistemicoException(hbEx.getMessage());
       }
   }

   /**
    * Remove o objeto rastreabilidade da entidade
    * @param {@link RastreabilidadeDAO}
    */
   public void removerRastreabilidade(Rastreabilidade rastreabilidade) throws ErroSistemicoException {
       try {
           super.deletar(rastreabilidade);
       } catch (HibernateException hbEx) {
           hbEx.printStackTrace();
           throw new ErroSistemicoException(hbEx.getMessage());
       }
   }

   /**
    * Busca a rastreabilidade da entidade Rastreabilidade por exemplo
    *
    * @param {@link RastreabilidadeDAO}
    * @return link List<Rastreabilidade>}
    */
   public List<Rastreabilidade> buscaPorExemploRastreabilidade(Rastreabilidade rastreabilidade) throws ErroSistemicoException {
       try {
           List<Rastreabilidade> listaPesquisada = super.buscarListaDeObjetos(rastreabilidade);
//           super.encerrarSessao();
           return listaPesquisada;
       } catch (HibernateException hbEx) {
           hbEx.printStackTrace();
           throw new ErroSistemicoException(hbEx.getMessage());
       }
   }
   
}
