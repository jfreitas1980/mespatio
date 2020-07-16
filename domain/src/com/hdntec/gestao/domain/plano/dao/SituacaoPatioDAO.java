/**
 * 
 */

package com.hdntec.gestao.domain.plano.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.navios.dao.MetaCargaDAO;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.plano.entity.MovimentacaoNavio;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.dao.AtividadeCampanhaDAO;
import com.hdntec.gestao.domain.planta.dao.LugarEmpilhamentoRecuperacaoDAO;
import com.hdntec.gestao.domain.planta.dao.MetaBalizaDAO;
import com.hdntec.gestao.domain.planta.dao.MetaBercoDAO;
import com.hdntec.gestao.domain.planta.dao.MetaCorreiaDAO;
import com.hdntec.gestao.domain.planta.dao.MetaFiltragemDAO;
import com.hdntec.gestao.domain.planta.dao.MetaMaquinaDoPatioDAO;
import com.hdntec.gestao.domain.planta.dao.MetaUsinaDAO;
import com.hdntec.gestao.domain.planta.dao.MovimentacaoNavioDAO;
import com.hdntec.gestao.domain.produto.dao.ProdutoDAO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para persistir a entidade SituacaoPatio
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class SituacaoPatioDAO extends AbstractGenericDAO<SituacaoPatio> {

    private AtividadeDAO dao = new AtividadeDAO();
	/**
     * Salva objeto situacaoPatio na entidade
     * @param {@link SituacaoPatio}
     */
    public SituacaoPatio salvaSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
        try {
            
            if (situacaoPatio.getAtividade() != null && (situacaoPatio.getAtividade().getUpdated() || situacaoPatio.getAtividade().getId() == null)) {
            	situacaoPatio.setAtividade(dao.salvaAtividade(situacaoPatio.getAtividade()));
            	situacaoPatio.getAtividade().setUpdated(Boolean.FALSE);
            }	
                                                           
            return situacaoPatio;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto situacaoPatio na entidade
     * @param {@link SituacaoPatio}
     */
    public void alteraSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
        try {
            super.atualizar(situacaoPatio);
            //            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remover o objeto situacaoPatio na entidade
     * @param {@link SituacaoPatio}
     */
    public void removerSituacaoPatio(List<SituacaoPatio> situacaoPatio) throws ErroSistemicoException {
        try {
            List<Long> lstItens = new ArrayList<Long>();
            for (SituacaoPatio s : situacaoPatio) {
                lstItens.add(s.getIdSituacaoPatio());
            }
        } catch (HibernateException hbEx) {
            HibernateUtil.rollbackTransaction();
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto situacaoPatio da entidade
     * @param {@link SituacaoPatio}
     */
    public void removeSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
        try {

        	super.deletar(situacaoPatio);

            if (situacaoPatio.getAtividade() != null) {
            	dao.removeAtividade(situacaoPatio.getAtividade());
            }	
          
        	
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca situacaoPatio da entidade SituacaoPatio por exemplo
     * @param {@link SituacaoPatio}
     * @return link List<SituacaoPatio>}
     */
    public List<SituacaoPatio> buscaPorExemploSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
        try {
            List<SituacaoPatio> listaPesquisada = super.buscarListaDeObjetos(situacaoPatio);
            //            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<SituacaoPatio> salvaSituacaoPatio(List<SituacaoPatio> lstSituacaoPatio) throws ErroSistemicoException {
        try {
          
        	List<SituacaoPatio> situacaoSalva = new ArrayList<SituacaoPatio>();
        	for (SituacaoPatio s : lstSituacaoPatio) {
        		situacaoSalva.add(salvaSituacaoPatio(s));
        	}
        	
        	for (SituacaoPatio s : lstSituacaoPatio) {
        		if (s.getIdSituacaoPatio() == null) {
        			super.salvar(s);
        		}	
        	}

            return situacaoSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Metodo que retorna a ultima situacao de patio de um dia especifico
     * @param dataPesquisa
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public SituacaoPatio buscarUltimaSituacaoPatioDoDia(Date dataPesquisa) throws ErroSistemicoException {

        // criando a data inicial do filtro e setando a hora para 00:00:00
        Calendar dataInicial = Calendar.getInstance();
        dataInicial.setTime(dataPesquisa);
        dataInicial.set(Calendar.HOUR, 0);
        dataInicial.set(Calendar.MINUTE, 0);
        dataInicial.set(Calendar.SECOND, 0);

        // criando a data inicial do filtro e setando a hora para 23:59:59
        Calendar dataFinal = Calendar.getInstance();
        dataFinal.setTime(dataPesquisa);
        // Zerando a hora da data
        dataFinal.set(Calendar.HOUR, 23);
        dataFinal.set(Calendar.MINUTE, 59);
        dataFinal.set(Calendar.SECOND, 59);

        Session session = HibernateUtil.getSession();
        StringBuilder sSql = new StringBuilder();

        sSql.append("select distinct(t) from SituacaoPatio t");
        sSql.append(" where t.inicio between :param1 and :param2");
        sSql.append(" and rownum = 1");
        sSql.append(" order by inicio desc");

        Query q = session.createQuery(sSql.toString());
        q.setParameter("param1", dataInicial.getTime());
        q.setParameter("param2", dataFinal.getTime());

        SituacaoPatio result = (SituacaoPatio) q.uniqueResult();
        //      super.encerrarSessao();

        return result;
    }

    public SituacaoPatio buscarSituacaoPatioOficialPorPeriodo(Date dataInicial, Date dataFinal) {

        SituacaoPatio situacaoPatio = null;
        List<SituacaoPatio> situacaoLista = buscarSituacoesPatioPorPeriodo(dataInicial, dataFinal, true, true, null);
        if (situacaoLista.size() > 0) {
            situacaoPatio = situacaoLista.get(0);
        }
        return situacaoPatio;
    }

    public List<SituacaoPatio> buscarListaSPOficial(Date dataInicial, Date dataFinal, TipoAtividadeEnum tipoAtividade) {

        List<SituacaoPatio> result = buscarSituacoesPatioPorPeriodo(dataInicial, dataFinal, false, true, tipoAtividade);
        return result;
    }

    /**
     * 
     * @param dataInicial
     * @param dataFinal
     * @param isUnique
     * @param ehOficial
     * @param tipoAtividade
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<SituacaoPatio> buscarSituacoesPatioPorPeriodo(Date dataInicial, Date dataFinal, boolean isUnique,
                    boolean ehOficial, TipoAtividadeEnum tipoAtividade) {

        Session session = HibernateUtil.getSession();
        //Montando a consulta
        StringBuilder sSql = new StringBuilder();
        sSql.append("from " + SituacaoPatio.class.getName());
        sSql.append(" where planoEmpilhamento.ehOficial = :pOficial ");
        sSql.append(" and inicio between :dataInicial and :dataFinal ");
        if (tipoAtividade != null) {
            sSql.append(" and atividade.tipoAtividade = :tipoAtividade");
        }
        if (isUnique) {
            sSql.append("order by inicio desc");
        }
        //Criando a query e passando os parametros
        Query q = session.createQuery(sSql.toString());
        q.setParameter("dataInicial", dataInicial.getTime());
        q.setParameter("dataFinal", dataFinal.getTime());
        q.setBoolean("pOficial", ehOficial);
        if (tipoAtividade != null) {
            q.setParameter("tipoAtividade", tipoAtividade);
        }
        if (isUnique) {
            q.setMaxResults(1);
        }
        //Executando a consulta
        List<SituacaoPatio> situacaoLista = q.list();
        //		super.encerrarSessao();
        return situacaoLista;
    }

    public List<SituacaoPatio> buscarSituacaoPatioRelAtividade(Date dataInicial, Date dataFinal, boolean ehOficial) {

        Session session = HibernateUtil.getSession();
        //Montando a consulta
        StringBuilder sSql = new StringBuilder();
        sSql.append("from " + SituacaoPatio.class.getName());
        sSql.append(" where planoEmpilhamento.ehOficial = :pOficial ");
        sSql.append(" and inicio between :dataInicial and :dataFinal ");
        sSql.append(" order by inicio");
        //Criando a query e passando os parametros
        Query q = session.createQuery(sSql.toString());
        q.setParameter("dataInicial", dataInicial.getTime());
        q.setParameter("dataFinal", dataFinal.getTime());
        q.setBoolean("pOficial", ehOficial);
        //Executando a consulta
        List<SituacaoPatio> situacaoLista = q.list();
        inicializaAtividadeRelatorio(situacaoLista);
        //		super.encerrarSessao();
        return situacaoLista;
    }

    /**
     * 
     * inicializaAtividadeRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 13/08/2009
     * @see
     * @param situacaoList
     * @return Returns the void.
     */
    private void inicializaAtividadeRelatorio(List<SituacaoPatio> situacaoList) {
        for (SituacaoPatio situacao : situacaoList) {
            if (situacao.getAtividade() != null) {
                if (!Hibernate.isInitialized(situacao.getAtividade().getListaDeAtividadesRelatorio())) {
                    Hibernate.initialize(situacao.getAtividade().getListaDeAtividadesRelatorio());
                }
            }
        }
    }

    public void deletarSituacaoPatio(Long idSituacao) {

        Session session = HibernateUtil.getSession();
        CallableStatement st;
        try {
        	HibernateUtil.beginTransaction();                    	
        	st = session.connection().prepareCall("{call DELETARSITUACAO(?)}");
            //st.registerOutParameter(1, java.sql.Types.VARCHAR); 
            st.setLong(1, idSituacao);
            st.executeUpdate();
//            HibernateUtil.commitTransaction();
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPilhas() {

        Session session = HibernateUtil.getSession();
        Statement st;
        try {
            HibernateUtil.beginTransaction();                       
            st = session.connection().createStatement(); 
            st.execute("delete from BALIZA_PILHA p where p.LISTADEBALIZAS_IDBALIZA not in (select idbaliza from baliza)");
            st = session.connection().createStatement();
            st.execute("delete from BALIZA_PILHA p where p.LISTADEBALIZAS_IDBALIZA in (select idbaliza from baliza where id_produto is null)");
            st = session.connection().createStatement();
            st.execute(" delete from pilha p where p.idpilha not in (select PILHAS_idpilha from BALIZA_PILHA)");
            //st.registerOutParameter(1, java.sql.Types.VARCHAR); 
            //st.setLong(1, idSituacao);
            //st.executeUpdate();
           // HibernateUtil.commitTransaction();
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
       
    public void deletarPilhas1() {

        Session session = HibernateUtil.getSession();
        Statement st;
        try {
            HibernateUtil.beginTransaction();                       
            st = session.connection().createStatement(); 
            st.execute("delete from BALIZA_PILHA p where p.LISTADEBALIZAS_IDBALIZA not in (select idbaliza from baliza)");
            
            
            
            //st.registerOutParameter(1, java.sql.Types.VARCHAR); 
            //st.setLong(1, idSituacao);
            //st.executeUpdate();
            HibernateUtil.commitTransaction();
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
