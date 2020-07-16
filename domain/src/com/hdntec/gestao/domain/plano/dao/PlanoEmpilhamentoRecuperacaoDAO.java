package com.hdntec.gestao.domain.plano.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;


public class PlanoEmpilhamentoRecuperacaoDAO extends AbstractGenericDAO<PlanoEmpilhamentoRecuperacao>
{

   public PlanoEmpilhamentoRecuperacao salvaPlanoEmpilhamentoRecuperacao(PlanoEmpilhamentoRecuperacao plano) throws ErroSistemicoException
   {
      try {
         
          PlanoEmpilhamentoRecuperacao planoSalvo = super.salvar(plano);
         
         //super.encerrarSessao();
         return planoSalvo;
      }
      catch (Exception hbEx) {
         hbEx.printStackTrace();
         throw new ErroSistemicoException(hbEx.getMessage());
      }
   }
   
   public void atualizarPlanoEmpilhamentoRecuperacao(PlanoEmpilhamentoRecuperacao plano) throws ErroSistemicoException
   {
	   super.atualizar(plano);
   }

   public void alteraPlanoEmpilhamentoRecuperacao(PlanoEmpilhamentoRecuperacao plano) throws ErroSistemicoException
   {
      try {
         super.atualizar(plano);
//         super.encerrarSessao();
      }
      catch (HibernateException hbex) {
         hbex.printStackTrace();
         throw new ErroSistemicoException(hbex.getMessage());
      }
   }

   public void removePlanoEmpilhamentoRecuperacao(PlanoEmpilhamentoRecuperacao plano) throws ErroSistemicoException
   {
      try {
         super.deletar(plano);
//         super.encerrarSessao();
      }
      catch (HibernateException hbex) {
         hbex.printStackTrace();
         throw new ErroSistemicoException(hbex.getMessage());
      }
   }

   public void removeListaDePlanoEmpilhamentoRecuperacao(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException
   {
      try {
         super.remover(listaPlanos);
//         super.encerrarSessao();
      }
      catch (HibernateException hbex) {
         hbex.printStackTrace();
         throw new ErroSistemicoException(hbex.getMessage());
      }
   }

   public List<PlanoEmpilhamentoRecuperacao> buscaPorExemploMudancaDeCampanha(PlanoEmpilhamentoRecuperacao plano) throws ErroSistemicoException
   {
      try {
         List<PlanoEmpilhamentoRecuperacao> listaPesquisada = super.buscarListaDeObjetos(plano);
//         super.encerrarSessao();
         return listaPesquisada;
      }
      catch (HibernateException hbex) {
         hbex.printStackTrace();
         throw new ErroSistemicoException(hbex.getMessage());
      }
   }

   public List<PlanoEmpilhamentoRecuperacao> buscarListaDePlanosPorDatas(PlanoEmpilhamentoRecuperacao objeto, String nomeCampo, Date dataInicial, Date dataFinal) throws ErroSistemicoException
   {
      try {
         List<PlanoEmpilhamentoRecuperacao> listaPesquisada =
                                            super.buscarListaDeObjetosPorDatas(objeto, nomeCampo, dataInicial, dataFinal);

         // carrega todas as listas dos planos encontrados para nao ocorrer erro EAGERInitializationException
         for (PlanoEmpilhamentoRecuperacao plano : listaPesquisada) {
            this.carregarListasDoPlano(plano);
         }
//         super.encerrarSessao();
         return listaPesquisada;
      }
      catch (HibernateException hbex) {
         hbex.printStackTrace();
         throw new ErroSistemicoException(hbex.getMessage());
      }
   }

   @Deprecated
   public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacao(PlanoEmpilhamentoRecuperacao plano) throws ErroSistemicoException
   {
      try {
         PlanoEmpilhamentoRecuperacao planoEmpRecuper = super.buscarPorObjeto(plano);

         /*TipoItemDeControleDAO dao = new TipoItemDeControleDAO();
         List<TipoItemDeControle> result = dao.findAll(TipoItemDeControle.class);*/


         if (planoEmpRecuper != null) {
            carregarSituacoesDePatioByPlano(planoEmpRecuper);
            carregarListasDoPlano(planoEmpRecuper);
         }
         return planoEmpRecuper;
         //super.encerrarSessao();
      }
      catch (HibernateException hbex) {
         hbex.printStackTrace();
         throw new ErroSistemicoException(hbex.getMessage());
      }

   }

   public List<PlanoEmpilhamentoRecuperacao> salvarOuAtualizarPlanosDaLista(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException
   {
      try {
         List<PlanoEmpilhamentoRecuperacao> listaPlanosSalvos = new ArrayList<PlanoEmpilhamentoRecuperacao>();
         listaPlanosSalvos = super.salvar(listaPlanos);
//         super.encerrarSessao();
         return listaPlanosSalvos;
      }
      catch (HibernateException hbex) {
         hbex.printStackTrace();
         throw new ErroSistemicoException(hbex.getMessage());
      }
   }

   /**
    * Metodo auxiliar que acessa as listas do objeto PlanoEmpilhamentoRecuperacao
    * para que as listas EAGER sejam carregadas e retornadas para interface jah preenchidas
    * evitando o erro EAGERInitializationException.
    *
    * @param plano
    */
   private void carregarListasDoPlano(PlanoEmpilhamentoRecuperacao plano)
   {
      if (plano != null) {
         for (SituacaoPatio situacaoPatio : plano.getListaSituacoesPatio()) {
           // situacaoPatio.setSituacaoPatioPersistida(Boolean.TRUE);
         }
      }
   }
   
   private void carregarListasPatios(PlanoEmpilhamentoRecuperacao plano) {
	   if (plano != null) {
		   for (SituacaoPatio situacaoPatio : plano.getListaSituacoesPatio()) {
			/*   for (Patio patio : situacaoPatio.getPlanta().getListaPatios()) {
				   System.out.println(patio);
			   }*/
		   }
	   }
   }

   @SuppressWarnings("unchecked")
   public List<SituacaoPatio> carregarSituacoesDePatioByPlano(PlanoEmpilhamentoRecuperacao plano)
   {
      Session session = HibernateUtil.getSession();
      Criteria criteria = session.createCriteria(SituacaoPatio.class, "sp");
      criteria.add(Restrictions.eq("sp.planoEmpilhamento", plano));
      criteria.add(Restrictions.between("sp.dataHora", new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 12)), new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 72))));
      List<SituacaoPatio> result = criteria.list();
      plano.setListaSituacoesPatio(result);
      HibernateUtil.closeSession();
      return result;
   }

   public PlanoEmpilhamentoRecuperacao carregarPlanoUsuario(Long idUser)
   {
      Session session = HibernateUtil.getSession();


      StringBuilder sSql = new StringBuilder();
      sSql.append("select distinct(t) from PlanoEmpilhamentoRecuperacao ");
      sSql.append("t join fetch t.listaSituacoesPatio as situacoes ");
      sSql.append(" where t.idUser =:param3 and  t.ehOficial = 0");
      Query q = session.createQuery(sSql.toString());
      q.setParameter("param3", idUser);
      PlanoEmpilhamentoRecuperacao result = (PlanoEmpilhamentoRecuperacao) q.uniqueResult();
//      super.encerrarSessao();
      carregarListasDoPlano(result);
      return result;

   }

   public PlanoEmpilhamentoRecuperacao carregarPlanoOficial(Long qtdeHorasSituacoesAnteriores)
   {	   
	   Session session = HibernateUtil.getSession();
	   StringBuilder sSql = new StringBuilder();

      sSql.append("select max(DT_INICIO) as ultimaData from SituacaoPatio where idplanoEmpRec is not null");
      Query qDate = session.createSQLQuery(sSql.toString());
      BigDecimal strUltimaDataHora = (BigDecimal) qDate.uniqueResult();
      
      Date dateFiltro = DSSStockyardTimeUtil.somarHoraNaData(new Date(strUltimaDataHora.longValue()), qtdeHorasSituacoesAnteriores.intValue() * -1, Calendar.HOUR);
      
      sSql = new StringBuilder();
      sSql.append("select distinct(t) from PlanoEmpilhamentoRecuperacao ");
      sSql.append("t join fetch t.listaSituacoesPatio as situacoes ");
      //sSql.append(" where t.ehOficial = 1 order by situacoes.inicio");
      sSql.append(" where situacoes.inicio >= :param1 and t.ehOficial = 1 order by situacoes.inicio");
      Query q = session.createQuery(sSql.toString());
      q.setParameter("param1", dateFiltro.getTime());
      PlanoEmpilhamentoRecuperacao result = (PlanoEmpilhamentoRecuperacao) q.uniqueResult();

      carregarListasDoPlano(result);
      
//      super.encerrarSessao();
      
      return result;
   }
   


   
   public PlanoEmpilhamentoRecuperacao carregarPlanoOficial()
   {
      Session session = HibernateUtil.getSession();

      StringBuilder sSql = new StringBuilder();
      sSql.append("select distinct(t) from PlanoEmpilhamentoRecuperacao ");
      sSql.append("t join fetch t.listaSituacoesPatio as situacoes ");
      sSql.append(" where t.ehOficial = 1");
      Query q = session.createQuery(sSql.toString());
      PlanoEmpilhamentoRecuperacao result = (PlanoEmpilhamentoRecuperacao) q.uniqueResult();
//      super.encerrarSessao();
      carregarListasDoPlano(result);
      return result;

   }

   public PlanoEmpilhamentoRecuperacao carregarPlanoUsuarioPorPeriodo(Long idUser, Date dataInicial, Date dataFinal)
   {
      Session session = HibernateUtil.getSession();
      StringBuilder sSql = new StringBuilder();
      sSql.append("select distinct(t) from PlanoEmpilhamentoRecuperacao t ");
      sSql.append(" join fetch t.listaSituacoesPatio as situacoes ");
      sSql.append(" where t.idUser =:idUser and  t.ehOficial = 0 ");
      sSql.append(" and situacoes.dataHora between :dataInicial and :dataFinal");
      Query q = session.createQuery(sSql.toString());
      q.setParameter("dataInicial", dataInicial);
      q.setParameter("dataFinal", dataFinal);
      q.setParameter("idUser", idUser);
      PlanoEmpilhamentoRecuperacao result = (PlanoEmpilhamentoRecuperacao) q.uniqueResult();
//      super.encerrarSessao();
      carregarListasDoPlano(result);
      return result;
   }

   public PlanoEmpilhamentoRecuperacao carregarPlanoOficialPorPeriodo(Date dataInicial, Date dataFinal)
   {
      Session session = HibernateUtil.getSession();
      StringBuilder sSql = new StringBuilder();
      sSql.append("select distinct(t) from PlanoEmpilhamentoRecuperacao ");
      sSql.append("t join fetch t.listaSituacoesPatio as situacoes ");
      sSql.append(" where t.ehOficial = 1 and ");
      sSql.append(" situacoes.dataHora between :dataInicial and :dataFinal");
      Query q = session.createQuery(sSql.toString());
      q.setParameter("dataInicial", dataInicial);
      q.setParameter("dataFinal", dataFinal);
      q.setMaxResults(1);
      PlanoEmpilhamentoRecuperacao result = (PlanoEmpilhamentoRecuperacao) q.uniqueResult();
//      super.encerrarSessao();
      carregarListasDoPlano(result);
      return result;
   }
   
   /**
    * 
    * @param listaPatios
    * @return
    */
   //TODO Darley SA11079
   public List<Patio> atualizarListaBalizasPorPatios(List<Patio> listaPatios) {
	   Session session = HibernateUtil.getSession();
	   StringBuilder sSql = new StringBuilder();
	   sSql.append("select distinct(p) from Patio ");
	   sSql.append("p join fetch p.listaDeBalizas ");
	   sSql.append("where p in ( :patios )");

	   Query q = session.createQuery(sSql.toString());
	   q.setParameterList("patios", listaPatios);

	   List<Patio> lista = q.list();

//	   super.encerrarSessao();
	   
	   return lista;
   }

   /**
    * 
    * @param listaPatios
    * @return
    */
   public List<Patio> atualizarMaquinasDoPatio(List<Patio> listaPatios) {
	   Session session = HibernateUtil.getSession();
	   StringBuilder sSql = new StringBuilder();
	   sSql.append("select distinct(mp) from MaquinaDoPatio mp ");
	   sSql.append("where mp.patio in ( :patios ) ");
	   sSql.append("and mp.correia is null");

	   Query q = session.createQuery(sSql.toString());
	   q.setParameterList("patios", listaPatios);

	   List<MaquinaDoPatio> lista = q.list();

//	   super.encerrarSessao();
	   
	 /*  for (Patio patio : listaPatios) {
		   patio.setListaDeMaquinasDoPatio(new ArrayList<MaquinaDoPatio>());
		   for (MaquinaDoPatio maquina : lista) {
			   if (maquina.getPatio().getIdPatio().equals(patio.getIdPatio())) {
				   patio.addMaquina(maquina);
			   }
		   }
	   }*/
	   
	   return listaPatios;
   }
   
   /**
    * 
    * @param correias
    * @return
    */
   public List<Correia> atualizarListaMaquinasCorreias(List<Correia> correias) {
	   Session session = HibernateUtil.getSession();
	   StringBuilder sSql = new StringBuilder();
	   sSql.append("select distinct(mp) from MaquinaDoPatio mp ");
	   sSql.append("where mp.correia in ( :correias )");

	   Query q = session.createQuery(sSql.toString());
	   q.setParameterList("correias", correias);

	   List<MaquinaDoPatio> lista = q.list();

//	   super.encerrarSessao();
	   
	   for (Correia correia : correias) {
		   correia.setListaDeMaquinas(new ArrayList<MaquinaDoPatio>());
		   for (MaquinaDoPatio maquina : lista) {
			   //if (correia.getIdCorreia().equals(maquina.getCorreia().getIdCorreia())) {
				   //correia.addMaquina(maquina);
			   //}
		   }
	   }
	   
	   return correias;
   }
   
      
   public List<Pier> atualizarListasDosPiers(List<Pier> listaPier) {
	   Session session = HibernateUtil.getSession();
	   StringBuilder sSql = new StringBuilder();
	   sSql.append("select distinct(p) from Pier p ");
	   sSql.append("join fetch p.listaDeCarregadoraDeNavios ");
	   sSql.append("join fetch p.listaDeBercosDeAtracacao ");
	   sSql.append("where p in ( :piers )");

	   Query q = session.createQuery(sSql.toString());
	   q.setParameterList("piers", listaPier);

	   return listaPier;
   }
   

}
