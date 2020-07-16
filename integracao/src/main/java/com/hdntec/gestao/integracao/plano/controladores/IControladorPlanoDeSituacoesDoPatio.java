package com.hdntec.gestao.integracao.plano.controladores;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Interface de acesso às operações do subsistema de plano de situações do pátio.
 * 
 * @author andre
 * 
 */
@Local
public interface IControladorPlanoDeSituacoesDoPatio {

    /**
     * Busca todos os planos de empilhamento e recuperação da base de dados
     * @return List<PlanoEmpilhamentoRecuperacao>
     */
   public List<PlanoEmpilhamentoRecuperacao> buscarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException;

    /**
     * salva um plano de empilhamento e recuperação
     *
     * @param planoDeEmpilhamentoERecuperacao
     */
    public PlanoEmpilhamentoRecuperacao salvarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException;

    /**
     * atualiza um plano de empilhamento e recuperação
     *
     * @param planoDeEmpilhamentoERecuperacao
     */
    public void atualizarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException;

    /**
     * Remove um plano de empilhamento e recuperacao
     * @param planoDeEmpilhamentoERecuperacao
     */
    public void removerPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException;

    /**
     * Salva a entidade situação de patio
     *
     * @param situacaoPatio
     */
    public SituacaoPatio salvaSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException;

     /**
     * Busca o plano de empilhamento e recuperação que é oficial no momento
     *
     * @return {@link PlanoEmpilhamentoRecuperacao}
     */
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficial() throws ErroSistemicoException;

    /**
     * Busca o plano de empilhamento e recuperação que é oficial no momento
     *
     * @return {@link PlanoEmpilhamentoRecuperacao}
     */
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficial(Long qtdeHorasSituacoesAnteriores) throws ErroSistemicoException;

    /**
     * adiciona as novas situações geradas na atualização dos sistemas externos
     * @param listaDeNovasSituacoesDePatio
     */
    public void adicionaEOrdenaNovasSituacoesNoPlanoOficial(List<SituacaoPatio> listaDeNovasSituacoesDePatio) throws ErroSistemicoException;

    /**
     * Busca novas situações realizadas do plano de empilhamento e recuperacao oficial
     *
     */
    public List<SituacaoPatio> buscarNovasSituacoesRealizadasDoPlanoEmpilhamentoRecuperacaoOficial(Date timestamp) throws ErroSistemicoException;

    /**
     * salva uma atividade
     *
     * @param Atividade
     */
    public Atividade salvarAtividade(Atividade recuperacao) throws ErroSistemicoException;

    /**
     * atualiza uma atividade
     *
     * @param Atividade
     */
    public void atualizarAtividade(Atividade recuperacao) throws ErroSistemicoException;

    /**
     * Busca todos os planos de empilhamento e recuperação da base de dados
     * de um determinado periodo que estão oficionalizados
     * @return List<PlanoEmpilhamentoRecuperacao>
     */
    public List<PlanoEmpilhamentoRecuperacao> buscarListaPlanosOficiaisPorData(Date dataInicial, Date dataFinal) throws ErroSistemicoException;

    /**
     * Atualiza uma lista de planos de empilhamento e recuperação
     * @param listaPlanos
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void atualizarListaDePlanos(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException;

    /**
     * Salvar o cliente no banco
     * @param cliente
     * @return o cliente persistido
     */
   public Cliente salvarCliente(Cliente cliente) throws ErroSistemicoException;

   

   /**
    * Salva uma lista de planos de empilhamento e recuperacao 
    * @param listaPlanos
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    */
   public List<PlanoEmpilhamentoRecuperacao> salvarOuAtualizarPlanosDaLista(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException;


   /**
    * Remove uma lista de situacoes de patio 
    * @param listaPlanos
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    */
   public void removeListaDePlanoEmpilhamentoRecuperacao(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException;

   /**
    * Atualiza a situação de patio
    * @param situacaoPatio
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    */
   public void atualizaSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException;

   /**
    * Atualiza os dados do cliente
    * @param cliente
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    */
   public void atualizaCliente(Cliente cliente) throws ErroSistemicoException;

   /**
    * Busca o plano de empilhamento e recuperacao do usuario logado no sistema
    * @return
    */
   public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoPorUsuario(Long idUser) throws ErroSistemicoException;
   
   /**
    * Salva uma lista de situaçõe de pátio 
    * @param listaPlanos
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    */
   public List<SituacaoPatio> salvaSituacaoPatio(List<SituacaoPatio> lstPatio) throws ErroSistemicoException;

   /**
    * Busca um plano e uma lista de situacoes com base no periodo informado para a situacao
    * @param dataInicial
    * @param dataFinal
    * @return PlanoEmpilhamento
    * @throws ErroSistemicoException
    */
   public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficialPorPeriodo(Date dataInicial, Date dataFinal) throws ErroSistemicoException;

   /**
    * Busca um plano de um usuario com uma lista de situacoes filtradas pelo periodo informado
    * @param idUser
    * @param dataInicial
    * @param dataFinal
    * @return
    * @throws ErroSistemicoException
    */
   public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoUsuarioPorPeriodo(Long idUser, Date dataInicial, Date dataFinal) throws ErroSistemicoException;


   /**
    * Metodo que retorna a ultima situacao de patio realizada para o dia recebido como parametro
    * @param dataPesquisa
    * @return - SituacaoPatio
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    */
   public SituacaoPatio buscarUltimaSituacaoPatioDoDia(Date dataPesquisa) throws ErroSistemicoException;

   /**
    * Busca a situacao do patio oficial mais atual dentro do periodo informado
    * @param dataInicial
    * @param dataFinal
    * @return
    * @throws ErroSistemicoException
    */
   public SituacaoPatio buscarSituacaoPatioOficialAtualPorPeriodo(Date dataInicial, Date dataFinal) throws ErroSistemicoException;
   
   /**
    * Busca as situacoes de patio oficiais por um periodo e por um tipo de 
    * atividade. 
    * @return
    * @throws ErroSistemicoException
    */
   public List<SituacaoPatio> buscarListaSPOficialPorPeriodoEAtividade(
		   Date dataInicial, Date dataFinal, 
		   TipoAtividadeEnum tipoAtividade) throws ErroSistemicoException;
   
   /**
    * Busca as situacoes de patio por periodo e carrega também a lista de 
    * relatorios (Deslocamento de maquina) da atividade de cada situacao.  
    * buscarSituacaoPatioRelAtividade
    * 
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 13/08/2009
    * @see
    * @param dataInicial
    * @param dataFinal
    * @param ehOficial
    * @return
    * @throws ErroSistemicoException
    * @return Returns the List<SituacaoPatio>.
    */
   public List<SituacaoPatio> buscarSituacaoPatioRelAtividade(Date dataInicial,
   		Date dataFinal, boolean ehOficial) throws ErroSistemicoException;
   
   public void removerSituacaoPatio(List<SituacaoPatio> lstSituacaoPatio) throws ErroSistemicoException;

   public void removerSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException;
   
   /**
    * Atualiza a lista de balizas de cada patio informado
    * @param listaPatios
    * @throws ErroSistemicoException
    */
   public List<Patio> atualizarListaBalizasPorPatios(List<Patio> listaPatios)
	throws ErroSistemicoException;
   
   /**
    * 
    * @param listaPatios
    * @return
    * @throws ErroSistemicoException
    */
   public List<Patio> atualizarMaquinasDoPatio(List<Patio> listaPatios)
	throws ErroSistemicoException;
   
   /**
    * 
    * @param correias
    * @return
    * @throws ErroSistemicoException
    */
   public List<Correia> atualizarListaMaquinasCorreias(List<Correia> correias);
   
   /**
    * 
    * @param usinas
    * @return
    */
   public List<Usina> atualizarListaEmergenciaUsinas(List<Usina> usinas);

   /**
    * 
    * @return
    */
   public void encerrarSessao();
}
