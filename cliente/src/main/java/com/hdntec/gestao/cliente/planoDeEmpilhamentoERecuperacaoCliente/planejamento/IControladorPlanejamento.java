package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento;

import java.util.Date;
import java.util.List;

import com.hdntec.gestao.cliente.interfaceDeDialogo.ControladorInterfaceInicial;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CarregarOficialNaoNecessariaException;
import com.hdntec.gestao.exceptions.ConsolidacaoNaoNecessariaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.RemocaoDePlanosNaoPermitidaException;
import com.hdntec.gestao.exceptions.TempoInsuficienteException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;


/**
 * Interface de acesso às operações do subsistema de planejamento.
 * 
 * @author andre
 * 
 */
public interface IControladorPlanejamento {

    /**
     * Planeja o {@link Empilhamento}.
     *
     * @param atividadeEmpilhamento
     *           a atividade de empilhamento a ser planejada
     * @return flag indicando sucesso ou fracasso
     * @throws ValidacaoObjetosOperacaoException
     */
    public boolean planejarEmpilhamento(Atividade atividadeEmpilhamento, SituacaoPatio situacaoPatioAtual) throws BlendagemInvalidaException, CampanhaIncompativelException, TempoInsuficienteException, ValidacaoObjetosOperacaoException;

    /**
     * Planeja a {@link Recuperacao}.
     *
     * @param atividadeRecuperacao
     *           a atividade de recuperação a ser planejada
     * @return flag indicando sucesso ou fracasso
     * @throws ValidacaoObjetosOperacaoException
     */
    public boolean planejarRecuperacao(Atividade atividadeRecuperacao, SituacaoPatio situacaoPatioAtual) throws ValidacaoObjetosOperacaoException;

    /**
     * Planeja a mudança de campanha
     * @param atividadesMudancaCampanha
     * @throws AtividadeException
     */
    public void planejarMudancaCampanha(List<Atividade> atividadesMudancaCampanha) throws AtividadeException;

	/**
     * planeja o resultado de amostragem do produto da carga
     * @param atividade
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.AtividadeException
     */
    public void planejarResultadoAmostragem(Atividade atividade) throws AtividadeException;
	

    /**
     * Retorna as situacoes de patio do plano virtual.
     * @return
     */
    public List<SituacaoPatio> obterSituacoesDePatio();

    /**
     * Numero de situacoes de patio do plano virtual.
     * @return
     */
    public Integer obterNumeroDeSituacoes();

    /**
     * Retorna situacao de patio referente a uma posicao do slider.
     * @param posicaoSituacao
     * @return
     */
    public SituacaoPatio buscarSituacaoPatio(Integer posicaoSituacao);
    

    /**
     * Retorna o controlador do plano de empilhamento e recuperacao real
     * @return
     */
    public ControladorDePlano getControladorDePlano();

    
    /**
     * Consolida o plano desenhado no DSP
     * @throws ErroSistemicoException
     */
    public void consolidarPlano() throws ErroSistemicoException;

    /** Carrega do controlador planejamento o controlador da interface inicial
     * @return
     */
    public ControladorInterfaceInicial getControladorInterfaceInicial();

    /** Carrega para o controlador planejamento o controlador da interface inicial
     * @param controladorInterfaceInicial
     */
    public void setControladorInterfaceInicial(ControladorInterfaceInicial controladorInterfaceInicial);

    /**
     * Remove um plano desenhado no DSP
     * @param indiceDaSituacaoDePatioExibida
     * @throws ErroSistemicoException
     */
    public void removerPlano(Integer indiceDaSituacaoDePatioExibida) throws ErroSistemicoException;

    /**
     * Retorna uma situacao de patio na data informada, se a situacao de patio sera visualizada
     * posteriormente, entao ela é colocada no plano virtual e podera ser consolidada.
     * @param data
     * @param visualizada
     * @return
     */
    public SituacaoPatio obterSituacaoDePatio(Date data);


    public void atualizacaoEmpilhamento(Atividade atividadeAtualizacaoEmpilhamento, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException;

    public void atualizacaoEmpilhamentoEmergencia(Atividade atividadeAtualizacaoEmpilhamentoEmergencia, SituacaoPatio situacaoPatioAtual) throws AtividadeException;
    
    public void atualizacaoRecuperacao(Atividade atividadeAtualizacaoRecuperacao, SituacaoPatio situacaoPatioAtual) throws ValidacaoObjetosOperacaoException;

    public void atualizacaoEdicaoBalizas(Atividade atividadeEdicaoBalizas, SituacaoPatio situacaoPatioAtual) throws AtividadeException;
    
    /**
     * Verifica a possibilidade de consolidacao para que o usuario não tente consolidadar 2
     * vezes o mesmo plano sem necessidade
     */
    public void verificarPossibilidadeDeConsolidacao() throws ConsolidacaoNaoNecessariaException;
    
    /** Verifica a possibilidade de remocao de um plano para que o usuario nao tente remover um plano
     * que nao esteja consolidade
     * @throws RemocaoDePlanosNaoPermitidaException
     */
    public void verificarPossibilidadeDeRemocaoDePlanos() throws RemocaoDePlanosNaoPermitidaException;

    public void planejarMovimentacao(Atividade atividade, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException;

    /**
     * Metodo que ativa a necessidade de uma consilidacao
     *
     */
    public void ativarNecessidadeDeConsolidacao();

    /**
     * Metodo que desativa a necessidade de uma consilidacao
     *
     */
    public void desativarNecessidadeDeConsolidacao();

    
    /**
     * planejamento da atividade de atracar navio
     * @param atividade
     */
    public void planejaAtracacaoDeNavio(Atividade atividade);

    public void planejarTratamentoPSM(Atividade atividade, SituacaoPatio situacaoPatioAtual) throws AtividadeException;
    public void oficializaPlano() throws ErroSistemicoException;
    public void carregarPlanoEmpilhamentoRecuperacaoOficialToUsuario(PlanoEmpilhamentoRecuperacao plano) throws ErroSistemicoException;
    public void verificarPossibilidadeDeCarregarPlanoUsuario() throws CarregarOficialNaoNecessariaException;

    public void retornarPelletFeed(Atividade atividadeRetornoPelletFeed, SituacaoPatio situacaoPatioAtual);

    /**
     * Executa a atualizacao de empilhamento para pilha de emergencia
     * @param atividadeAtualizacaoPilhaEmergencia
     * @param situacaoPatioAtual
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.AtividadeException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoObjetosOperacaoException
     */
    public void atualizacaoPilhaEmergencia(Atividade atividadeAtualizacaoPilhaEmergencia, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException;

    /**
     * Executa atividade que transporta uma pilha de emergencia
     * @param atividadeTransportarPilhaEmergencia
     * @param situacaoPatioAtual
     * @throws AtividadeException
     * @throws ValidacaoObjetosOperacaoException
     */
    public void transportarPilhaEmergencia(Atividade atividadeTransportarPilhaEmergencia, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException;

    /**
     * Metodo que executa a movimentacao de uma pilha de emergencia
     * @param atividadeMovimentarPilhaEmergencia
     * @param situacaoPatioAtual
     * @throws AtividadeException
     * @throws ValidacaoObjetosOperacaoException
     */
    public void movimentarPilhaEmergencia(Atividade atividadeMovimentarPilhaEmergencia, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException;
    
    /**
     * Metodo que executa a movimentacao de uma pilha de PSM ou pellet feed
     * @param atividadeMovimentarPilhaEmergencia
     * @param situacaoPatioAtual
     * @throws AtividadeException
     * @throws ValidacaoObjetosOperacaoException
     */
    public void movimentarPilhaPSMPelletFeed(Atividade atividadeMovimentar, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException;   
    
    public Boolean getConsolidacaoRealizada();
		
	public void setConsolidacaoRealizada(Boolean consolidacaoRealizada);

	public void atualizacaoEdicaoMaquinas(Atividade atividadeEdicaoBalizas,
			SituacaoPatio situacaoPatioAtual) throws AtividadeException;			
}    
