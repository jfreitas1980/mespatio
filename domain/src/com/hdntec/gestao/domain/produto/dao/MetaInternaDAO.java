/**
 * 
 */
package com.hdntec.gestao.domain.produto.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.produto.entity.MetaInterna;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para persistir a entidade MetaInterna
 * 
 * @author andre
 *
 */
public class MetaInternaDAO extends AbstractGenericDAO<MetaInterna>
{
   /**
    * Salva objeto meta interna na entidade
    * @param {@link MetaInternaDAO}
    */
   public MetaInterna salvarMetaInterna(MetaInterna metaInterna) throws ErroSistemicoException
   {
      try
      {
         MetaInterna metaInternaSalva = super.salvar(metaInterna);
//         super.encerrarSessao();
         return metaInternaSalva;
      } catch (HibernateException hbEx)
      {
         hbEx.printStackTrace();
         throw new ErroSistemicoException(hbEx.getMessage());
      }
   }

   /**
    * Altera o objeto meta interna na entidade
    * @param {@link MetaInternaDAO}
    */
   public void alterarMetaInterna(MetaInterna metaInterna) throws ErroSistemicoException {
       try {
           super.atualizar(metaInterna);
//           super.encerrarSessao();
       } catch (HibernateException hbEx) 
       {
           hbEx.printStackTrace();
           throw new ErroSistemicoException(hbEx.getMessage());
       }
   }

   /**
    * Remove o objeto meta interna da entidade
    * @param {@link MetaInternaDAO}
    */
   public void removerMetaInterna(MetaInterna metaInterna) throws ErroSistemicoException {
       try {
           super.deletar(metaInterna);
       } catch (HibernateException hbEx) {
           hbEx.printStackTrace();
           throw new ErroSistemicoException(hbEx.getMessage());
       }
   }

   /**
    * Busca meta interna da entidade MetaInterna por exemplo
    *
    * @param {@link MetaInternaDAO}
    * @return link List<MetaInterna>}
    */
   public List<MetaInterna> buscaPorExemploMetaInterna(MetaInterna metaInterna) throws ErroSistemicoException {
       try {
           List<MetaInterna> listaPesquisada = super.buscarListaDeObjetos(metaInterna);
//           super.encerrarSessao();
           return listaPesquisada;
       } catch (HibernateException hbEx) {
           hbEx.printStackTrace();
           throw new ErroSistemicoException(hbEx.getMessage());
       }
   }

   /**
    * Busca meta interna da entidade MetaInterna por exemplo
    *
    * @param {@link MetaInternaDAO}
    * @return link List<MetaInterna>}
    */
   public List<MetaInterna> buscaMetaInternaPorFiltro(MetaInterna metaInterna) throws ErroSistemicoException {
	   List<MetaInterna> result = new ArrayList<MetaInterna>();
	   Session session = HibernateUtil.getSession();
	   StringBuilder sSql = new StringBuilder();
	   sSql.append("select distinct(mi) from MetaInterna mi ");
	   sSql.append("join fetch mi.tipoPelota as tp ");
	   sSql.append("where upper(tp.descricaoTipoProduto) like upper(:param)");
	   Query q = session.createQuery(sSql.toString());
	   q.setParameter("param", "%"+metaInterna.getTipoPelota().getDescricaoTipoProduto()+"%");
	   result = q.list();
//	   super.encerrarSessao();

	   return result;
   }
   
   
   public MetaInterna buscarPorPeriodo(MetaInterna meta, Date dataFinal)
   {
      Session session = HibernateUtil.getSession();
      StringBuilder sSql = new StringBuilder();
      sSql.append("select distinct(t) from MetaInterna t ");
      sSql.append(" where t.tipoPelota =:tipoPelota and  t.tipoItemDeControle =:tipoItemDeControle and t.tipoDaMetaInterna =:tipoDaMetaInterna");
      sSql.append(" and  t.inicio <=:param1 and  t.fim >=:param1 ");
         
      Query q = session.createQuery(sSql.toString());
      q.setParameter("param1", dataFinal.getTime());
      q.setParameter("tipoPelota", meta.getTipoPelota());
      q.setParameter("tipoItemDeControle", meta.getTipoItemDeControle());
      q.setParameter("tipoDaMetaInterna", meta.getTipoDaMetaInterna());
      //q.setParameter("param2", dataInicial);
      MetaInterna result = (MetaInterna) q.uniqueResult();
//      super.encerrarSessao();
      return result;
   }
   
}
