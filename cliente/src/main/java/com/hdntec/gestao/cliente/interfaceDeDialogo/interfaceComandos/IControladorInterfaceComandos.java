package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.exceptions.BalizasSelecionadasException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CarregarOficialNaoNecessariaException;
import com.hdntec.gestao.exceptions.ConsolidacaoNaoNecessariaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.PermissaoDeUsuarioException;
import com.hdntec.gestao.exceptions.PilhaParaRecuperacaoSemMaquinaCapazException;
import com.hdntec.gestao.exceptions.PlanosOficiaisNaoLocalizadosException;
import com.hdntec.gestao.exceptions.RemocaoDePlanosNaoPermitidaException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoEmpilhamentoException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoRecuperacaoException;
import com.hdntec.gestao.exceptions.TempoInsuficienteException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;



/**
 * Interface de acesso às operações do subsistema de interface gráfica de comandos do usuário
 *
 * @author andre
 *
 */
public interface IControladorInterfaceComandos {

   /**
    *      * Solicita que seja planejado o empilhamento. Esse empilhamento será planejado de acordo com
     * as seleções realizadas pelo usuário na interface.
    * @param carga
    * @throws OperacaoCanceladaPeloUsuarioException
    * @throws ValidacaoObjetosOperacaoException
    * @throws BlendagemInvalidaException
    * @throws TempoInsuficienteException
    * @throws CampanhaIncompativelException
    * @throws BalizasSelecionadasException
    * @throws SelecaoObjetoModoEmpilhamentoException
    */
    public void empilhar(Carga carga) throws OperacaoCanceladaPeloUsuarioException, ValidacaoObjetosOperacaoException, BlendagemInvalidaException, TempoInsuficienteException, CampanhaIncompativelException, BalizasSelecionadasException, SelecaoObjetoModoEmpilhamentoException;

    /**
     * Muda a situação de pátio para a situação do presente e habilita o modo de edição
     */
    public void editar();

    /**
     * Verifica os dados inseridos pelo usuário para especificar a atividade de empilhamento a ser planejada.
     * Verifica se os dados são coerentes para originar uma atividade de empilhamento factível.
     *
     * @param atividadeEmpilhamento
     *           a atividade de empilhamento a ser verificada
     */
    public void verificaDadosEmpilhamento(Atividade atividadeEmpilhamento) throws ValidacaoObjetosOperacaoException, BalizasSelecionadasException;

    /**
     * Solicita que seja planejada a recuperação.
     * A recuperação será planejada de acordo com as seleções do usuário na interface.
     *
     * @param carga a carga selecionada para atendimento na recuperação
     * @throws OperacaoCanceladaPeloUsuarioException caso a operação seja cancelada pelo usuário
     * @throws ValidacaoObjetosOperacaoException
     * @throws PilhaParaRecuperacaoSemMaquinaCapazException caso não seja selecionada nenhuma máquina capaz de recuperar uma determinada pilha para recuperação
     */
    public void recuperar(Carga carga) throws OperacaoCanceladaPeloUsuarioException, ValidacaoObjetosOperacaoException, PilhaParaRecuperacaoSemMaquinaCapazException, SelecaoObjetoModoRecuperacaoException;

    /**
     * Verifica se os dados inseridos pelo usuário para especificar a atividade de recuperação estão corretos. Verifica se os dados são coerentes para dar origem a uma atividade de recuperação que seja factível.
     *
     * @param atividadeRecuperacao
     *           a atividade de recuperação a ser verificada
     */
    public void verficaDadosRecuperacao(Atividade atividadeRecuperacao) throws ValidacaoObjetosOperacaoException;

    /**
     * Solicita que a atualização de informações dos sistemas externos seja chaveada. Caso a atividade esteja ativada, ela será desativada, e vice-versa.
     *
     * @param atualizacaoAtivada
     *           o status da atualização automática
     */
    public void chavearAtualizacaoAutomatica(Boolean atualizacaoAtivada);

    /**
     * Solicita que todas as situações planejadas, a partir da situação visualizada,
     * sejam apagadas do plano. Situações realizadas não podem ser apagadas.
     *
     */
    public void apagarSituacaoPatio(Integer indiceDaSituacaoDePatioExibida) throws ErroSistemicoException;

    /**
     * Pega o plano oficial do servidor e substitui pelo plano atualmente visualizado.
     *
     */
    public void pegarPlanoOficial();

    /**
     * Consolida o plano atual do cliente como oficial, enviando-o para substituir o plano oficial no servidor.
     *
     */
    public void consolidarPlano() throws ErroSistemicoException;

    /**
     * Ativa a exibição da mensagem na tela inicial
     *
     * @param interfaceMensagem
     */
    public void ativarMensagem(InterfaceMensagem interfaceMensagem);

    /**
     * Metodo que cancela o modo de operacao escolhido pelo usuário
     */
    public void cancelarModoOperacao();


    /**
     * Desabilita as funcoes dos botoes quando algum mod
     * de operacao foi acionado
     */
    public void desabilitaFuncoes();

    /**
     * Habilita as funcoes dos botoes quando algum modo
     * de operacao foi confirmado ou cancelado
     */
    public void habilitaFuncoes();

    /** finaliza uma edicao especifica */
    public void finalizarEdicoes();

    /** desabilita os menus de edicao dos objetos */
    public void desabilitarMenusDeEdicao();

    /** desabilita os menus de edicao dos objetos */
    public void habilitarMenusDeEdicao();

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

    public void habilitarTodasFuncoes();

    public void desabilitarTodasFuncoes();

     /**
     * verifica se o usuario logado no sistema tem permissao para apagar um plano
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.PermissaoDeUsuarioException
     */
    public void verificarPermissaoUsuarioApagarPlano() throws PermissaoDeUsuarioException;
    public void oficializarPlano() throws ErroSistemicoException;
    public void buscarPlanoEmpilhamentoRecuperacaoDoUsuario(Long idUser) throws PlanosOficiaisNaoLocalizadosException, ErroSistemicoException;
    public void verificarPossibilidadeCarregarPlanoUsuario() throws CarregarOficialNaoNecessariaException;
    public void verificarPossibilidadeCarregarOficial() throws CarregarOficialNaoNecessariaException;

    public boolean verificaPermissaoAtualizacaoProducao();
    
    /**
     * 
     * @return
     */
    public boolean verificaNecessidadeDeIntegrarComMES();

}
