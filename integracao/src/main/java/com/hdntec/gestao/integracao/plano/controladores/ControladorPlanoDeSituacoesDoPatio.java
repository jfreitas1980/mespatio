package com.hdntec.gestao.integracao.plano.controladores;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.navios.dao.ClienteDAO;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.plano.dao.AtividadeDAO;
import com.hdntec.gestao.domain.plano.dao.PlanoEmpilhamentoRecuperacaoDAO;
import com.hdntec.gestao.domain.plano.dao.SituacaoPatioDAO;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Controlador de operações do subsistema de plano de situações do pátio.
 * 
 * @author andre
 * 
 */
@Stateless(name = "bs/ControladorPlanoDeSituacoesDoPatio", mappedName = "bs/ControladorPlanoDeSituacoesDoPatio")
public class ControladorPlanoDeSituacoesDoPatio implements IControladorPlanoDeSituacoesDoPatio {

    @Override
    public List<PlanoEmpilhamentoRecuperacao> buscarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        return dao.buscaPorExemploMudancaDeCampanha(planoDeEmpilhamentoERecuperacao);
    }

    @Override
    public void removerPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        dao.removePlanoEmpilhamentoRecuperacao(planoDeEmpilhamentoERecuperacao);
    }

    @Override
    public PlanoEmpilhamentoRecuperacao salvarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        PlanoEmpilhamentoRecuperacao result = dao.salvaPlanoEmpilhamentoRecuperacao(planoDeEmpilhamentoERecuperacao);        
        return result;
    }

    @Override
    public void atualizarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        //Darley Mudando o metodo de alterar para atualizar o plano na sessao.
//        dao.alteraPlanoEmpilhamentoRecuperacao(planoDeEmpilhamentoERecuperacao);
        dao.atualizarPlanoEmpilhamentoRecuperacao(planoDeEmpilhamentoERecuperacao);
    }

    @Override
    public SituacaoPatio salvaSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
        SituacaoPatioDAO situacaoPatioDAO = new SituacaoPatioDAO();
        return situacaoPatioDAO.salvaSituacaoPatio(situacaoPatio);
    }

     @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficial() throws ErroSistemicoException {
        //PlanoEmpilhamentoRecuperacao plano = new PlanoEmpilhamentoRecuperacao();
        //plano.setEhOficial(Boolean.TRUE);
        PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        return dao.carregarPlanoOficial();
    }

    @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficial(Long qtdeHorasSituacoesAnteriores) throws ErroSistemicoException {
        //PlanoEmpilhamentoRecuperacao plano = new PlanoEmpilhamentoRecuperacao();
        //plano.setEhOficial(Boolean.TRUE);
        PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        return dao.carregarPlanoOficial(qtdeHorasSituacoesAnteriores);
    }

    @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoPorUsuario(Long idUser) throws ErroSistemicoException {
        //PlanoEmpilhamentoRecuperacao plano = new PlanoEmpilhamentoRecuperacao();
        //plano.setIdUser(idUser);
        //plano.setEhOficial(Boolean.FALSE);
        PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        return dao.carregarPlanoUsuario(idUser);
    }
    
    @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficialPorPeriodo(Date dataInicial, Date dataFinal) throws ErroSistemicoException {
    	PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        return dao.carregarPlanoOficialPorPeriodo(dataInicial, dataFinal);
    }
    
    @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoUsuarioPorPeriodo(Long idUser, Date dataInicial, Date dataFinal) throws ErroSistemicoException {
    	PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
        return dao.carregarPlanoUsuarioPorPeriodo(idUser, dataInicial, dataFinal);
    }
    
 

    @Override
    public void adicionaEOrdenaNovasSituacoesNoPlanoOficial(List<SituacaoPatio> listaDeNovasSituacoesDePatio) throws ErroSistemicoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SituacaoPatio> buscarNovasSituacoesRealizadasDoPlanoEmpilhamentoRecuperacaoOficial(Date timestamp) throws ErroSistemicoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Atividade salvarAtividade(Atividade atividade) throws ErroSistemicoException {
        AtividadeDAO atividadeDAO = new AtividadeDAO();
        return atividadeDAO.salvaAtividade(atividade);
    }

    @Override
    public void atualizarAtividade(Atividade atividade) throws ErroSistemicoException {
        AtividadeDAO atividadeDAO = new AtividadeDAO();
        atividadeDAO.alteraAtividade(atividade);
    }

    @Override
    public List<PlanoEmpilhamentoRecuperacao> buscarListaPlanosOficiaisPorData(Date dataInicial, Date dataFinal) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacaoDAO planoDAO = new PlanoEmpilhamentoRecuperacaoDAO();

        PlanoEmpilhamentoRecuperacao filtroPlano = new PlanoEmpilhamentoRecuperacao();
        filtroPlano.setEhOficial(Boolean.TRUE);

        return planoDAO.buscarListaDePlanosPorDatas(filtroPlano, "dataInicio", dataInicial, dataFinal);
    }

    @Override
    public void atualizarListaDePlanos(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacaoDAO planoDAO = new PlanoEmpilhamentoRecuperacaoDAO();
        planoDAO.atualizar(listaPlanos);
    }

    @Override
    public Cliente salvarCliente(Cliente cliente) throws ErroSistemicoException {
        ClienteDAO clienteDAO = new ClienteDAO();
        return clienteDAO.salvaCliente(cliente);
    }

    
    @Override
    public List<PlanoEmpilhamentoRecuperacao> salvarOuAtualizarPlanosDaLista(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacaoDAO planoDAO = new PlanoEmpilhamentoRecuperacaoDAO();
        return planoDAO.salvarOuAtualizarPlanosDaLista(listaPlanos);
    }

    @Override
    public void removeListaDePlanoEmpilhamentoRecuperacao(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacaoDAO planoDAO = new PlanoEmpilhamentoRecuperacaoDAO();
        planoDAO.removeListaDePlanoEmpilhamentoRecuperacao(listaPlanos);
    }

    @Override
    public void atualizaSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
        SituacaoPatioDAO situacaoPatioDAO = new SituacaoPatioDAO();
        situacaoPatioDAO.alteraSituacaoPatio(situacaoPatio);
    }

    @Override
    public void atualizaCliente(Cliente cliente) throws ErroSistemicoException {
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.alteraCliente(cliente);
    }
    
    @Override
    public List<SituacaoPatio> salvaSituacaoPatio(List<SituacaoPatio> lstSituacaoPatio) throws ErroSistemicoException {
        SituacaoPatioDAO situacaoPatioDAO = new SituacaoPatioDAO();
        return situacaoPatioDAO.salvaSituacaoPatio(lstSituacaoPatio);
    }

   @Override
   public SituacaoPatio buscarUltimaSituacaoPatioDoDia(Date dataPesquisa) throws ErroSistemicoException
   {
      SituacaoPatioDAO situacaoPatioDAO = new SituacaoPatioDAO();
      return situacaoPatioDAO.buscarUltimaSituacaoPatioDoDia(dataPesquisa);
   }
   
   @Override
   public SituacaoPatio buscarSituacaoPatioOficialAtualPorPeriodo(Date dataInicial, Date dataFinal) throws ErroSistemicoException {
	   SituacaoPatioDAO dao = new SituacaoPatioDAO();
	   return dao.buscarSituacaoPatioOficialPorPeriodo(dataInicial, dataFinal);
   }
   
   @Override
   public List<SituacaoPatio> buscarListaSPOficialPorPeriodoEAtividade(
   		Date dataInicial, Date dataFinal, TipoAtividadeEnum tipoAtividade)
   		throws ErroSistemicoException {

	   SituacaoPatioDAO dao = new SituacaoPatioDAO();
	   return dao.buscarListaSPOficial(dataInicial, dataFinal, tipoAtividade);
   }
   
   @Override
   public List<SituacaoPatio> buscarSituacaoPatioRelAtividade(
   		Date dataInicial, Date dataFinal, boolean ehOficial)
   		throws ErroSistemicoException {

   	SituacaoPatioDAO dao = new SituacaoPatioDAO();
   	return dao.buscarSituacaoPatioRelAtividade(dataInicial, dataFinal, ehOficial);
   }
   
   @Override
   public void removerSituacaoPatio(List<SituacaoPatio> lstSituacaoPatio) throws ErroSistemicoException {
       SituacaoPatioDAO situacaoPatioDAO = new SituacaoPatioDAO();
        situacaoPatioDAO.removerSituacaoPatio(lstSituacaoPatio);
   }

   @Override
   public void removerSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
       SituacaoPatioDAO situacaoPatioDAO = new SituacaoPatioDAO();
       situacaoPatioDAO.removeSituacaoPatio(situacaoPatio);
   }
   
   @Override
   public List<Patio> atualizarListaBalizasPorPatios(List<Patio> listaPatios) throws ErroSistemicoException {
	   PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
	   return dao.atualizarListaBalizasPorPatios(listaPatios);
   }
   
   @Override
   public List<Patio> atualizarMaquinasDoPatio(List<Patio> patios)
		throws ErroSistemicoException {
	   
	   PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
	   return dao.atualizarMaquinasDoPatio(patios);
   }
   
   @Override
   public List<Correia> atualizarListaMaquinasCorreias(List<Correia> correias) {
	   
	   PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
	   return dao.atualizarListaMaquinasCorreias(correias);
   }
   
   @Override
   public List<Usina> atualizarListaEmergenciaUsinas(List<Usina> usinas) {
	   
	   PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
	   return null;//dao.atualizarListaEmergenciaUsinas(usinas);
   }
   
   @Override
   public void encerrarSessao() {
	   HibernateUtil.closeSession();
   }
}
