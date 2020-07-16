package com.hdntec.gestao.cliente.interfaceDeDialogo;

import java.util.Date;
import java.util.List;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos.IControladorInterfaceComandos;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceBaliza;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento.IControladorPlanejamento;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CargaSelecionadaException;
import com.hdntec.gestao.exceptions.CarregarOficialNaoNecessariaException;
import com.hdntec.gestao.exceptions.ConsolidacaoNaoNecessariaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.PilhaExistenteException;
import com.hdntec.gestao.exceptions.PlanosOficiaisNaoLocalizadosException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.exceptions.RemocaoDePlanosNaoPermitidaException;
import com.hdntec.gestao.exceptions.TempoInsuficienteException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;


/**
 * Interface de acesso ao subsistema de interface de diálogo com o usuário.
 *
 * @author andre
 *
 */
public interface IControladorInterfaceInicial {

   /**
    * Verifica se a campanha da usina pode ser blendada
    * @param campanhaSelecionada a campanha selecionada para blendagem
    * @param quantidade a quantidade de produto da campanha selecionada para blendagem
    * @param tipoDeProdutoSelecionado o tipo de produto selecionado na usina para blendagem
    * @throws BlendagemInvalidaException
    * @throws CampanhaIncompativelException caso o produto da campanha selecionada seja incompatível com os previamente selecionados
    * @throws ExcessoDeMaterialParaEmbarqueException caso a quantidadde de material selecionado exceda a quantidade necessária para embarcar a carga
    */
    public void verificaBlendagemCampanhaDeUsina(Campanha campanhaSelecionada, Double quantidade, TipoDeProdutoEnum tipoDeProdutoSelecionado) throws BlendagemInvalidaException, CampanhaIncompativelException,  ExcessoDeMaterialParaEmbarqueException, ProdutoIncompativelException;

    /**
     * Verifica a remoção da camapanha da blendagem atual
     * @param campanhaSelecionada a campanha selecionada para remoção
     * @throws BlendagemInvalidaException
     */
    public void verificaBlendagemRemocaoCampanhaDeUsina(Campanha campanhaSelecionada) throws BlendagemInvalidaException, ProdutoIncompativelException;

    /**
     * Verifrica se a baliza selecionada pode ser blendada
     * @param balizaSelecionada a baliza selecionada para blendagem
     * @throws BlendagemInvalidaException
     * @throws CampanhaIncompativelException caso o produto da campanha selecionada seja incompatível com os previamente selecionados
     * @throws ProdutoIncompativelException caso o produto da baliza selecionada seja incompatível com os produtos previamente selecionados para blendagem
     * @throws ExcessoDeMaterialParaEmbarqueException caso a quantidade de material selecionado para blendagem exceda a quantidade necessária para atender a carga
     */
    public void verificaBlendagemBaliza(Baliza balizaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException,  ExcessoDeMaterialParaEmbarqueException;

    /**
     *  Verifica a remoção da baliza da blendagem atual
     * @param balizaDeselecionada a baliza a ser removida da blendagem
     * @throws BlendagemInvalidaException
     * @throws CampanhaIncompativelException
     * @throws ProdutoIncompativelException caso o produto da baliza selecionada seja incompatível com os produtos previamente selecionados para blendagem
     */
    public void verificaRemocaoBlendagemBaliza(Baliza balizaDeselecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException;

    /**
     * Verifrica se a baliza selecionada pode ser blendada
     * @param balizaSelecionada a baliza selecionada para blendagem
     * @throws BlendagemInvalidaException
     * @throws CampanhaIncompativelException caso o produto da campanha selecionada seja incompatível com os previamente selecionados
     * @throws ProdutoIncompativelException caso o produto da baliza selecionada seja incompatível com os produtos previamente selecionados para blendagem
     * @throws ExcessoDeMaterialParaEmbarqueException caso a quantidade de material selecionado para blendagem exceda a quantidade necessária para atender a carga
     */
    public void verificaBlendagemBaliza(List<InterfaceBaliza> listaBalizaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException,  ExcessoDeMaterialParaEmbarqueException;

    /**
     *  Verifica a remoção da baliza da blendagem atual
     * @param balizaDeselecionada a baliza a ser removida da blendagem
     * @throws BlendagemInvalidaException
     * @throws CampanhaIncompativelException
     * @throws ProdutoIncompativelException caso o produto da baliza selecionada seja incompatível com os produtos previamente selecionados para blendagem
     */
    public void verificaRemocaoBlendagemBaliza(List<InterfaceBaliza> listaBalizaDeselecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException;

    /**
     * Verifica se a carga selecionada poder ser atendida pela blendagem
     * @param cargaSelecionada a carga selecionada
     * @throws BlendagemInvalidaException
     * @throws CampanhaIncompativelException caso o produto da campanha selecionada seja incompatível com os previamente selecionados
     * @throws ProdutoIncompativelException caso o produto da baliza selecionada seja incompatível com os produtos previamente selecionados para blendagem
     * @throws CargaSelecionadaException
     * @throws ExcessoDeMaterialParaEmbarqueException caso haja mais material selecionado na blendagem do que a carga comporta
     */
    public void verificaBlendagemCarga(Carga cargaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException, CargaSelecionadaException, ExcessoDeMaterialParaEmbarqueException;

    /**
     * Verifica a remoção do produto da carga na blendagem atual
     * @param cargaSelecionada a carga selecionada para ter o produto removido
     * @throws BlendagemInvalidaException
     * @throws ProdutoIncompativelException caso o produto da baliza selecionada seja incompatível com os produtos previamente selecionados para blendagem
     */
    public void verificaRemocaoBlendagemCarga(Carga cargaSelecionada) throws BlendagemInvalidaException, ProdutoIncompativelException;

    public void empilhar(Atividade atividadeEmpilhamento) throws BlendagemInvalidaException, TempoInsuficienteException, CampanhaIncompativelException, ValidacaoObjetosOperacaoException;

    public void recuperar(Atividade atividade) throws ValidacaoObjetosOperacaoException;

    public void chavearAtualizacaoAutomatica(boolean atualizacaoAtivada);

    public void apagarSituacaoPatio(Integer indiceDaSituacaoDePatioExibida) throws ErroSistemicoException;

    public void consolidarPlano() throws ErroSistemicoException;

    public List<Campanha> buscarCampanhasSelecionadas();

    public List<Baliza> buscarBalizasSelecionadas();

    public void ativarMensagem(InterfaceMensagem interfaceMensagem);

    public ModoDeOperacaoEnum obterModoOperacao();

    public IControladorPlanejamento getPlanejamento();

    /**
     * cria nova situacao com uma campanha diferente
     * @param atividades
     * @throws AtividadeException
     */
    public void mudarCampanha(List<Atividade> atividades) throws AtividadeException;

    /**
     * Posiciona o slider no indice da situacao passada como parametro
     * @param situacaoPatio
     */
    public void posicionaSituacaoPatioSlider(SituacaoPatio situacaoPatio);

    /**
     * Soma as atividades do plano oficial para geracoes de situacoes intermediarias
     */
    public void atualizarPlanoOficialComAtividades() throws BlendagemInvalidaException, CampanhaIncompativelException, TempoInsuficienteException;

    /**
     * Busca o plano de empilhamento e recuperacao nao oficial do usuario
     */
    public void buscarPlanoEmpilhamentoRecuperacaoPorUsuario(Long idUser) throws ErroSistemicoException;

    /**
     * Busca o planoDeEmpilhamentoRecuperacao oficial
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void buscarPlanoEmpilhamentoRecuperacaoOficial(Long qtdeHorasSituacoesAnteriores) throws PlanosOficiaisNaoLocalizadosException, ErroSistemicoException;

    /**
     * Busca novas situações realizadas do plano de empilhamento e recuperacao oficial
     *
     */
    public void buscarNovasSituacoesRealizadasDoPlanoEmpilhamentoRecuperacaoOficial();

    /**
     * Exclui a blendagem atual.
     */
    public void limparBlendagem();

    /**
     * Apresenta a Situação de Pátio cujo índice é passado como parâmetro
     * @param indiceSituacaoDePatio indice da Situação De Pátio a ser apresentada
     */
    void apresentarSituacaoDePatio(int indiceSituacaoDePatio);

    /**
     * Salva e atualiza a situacao de patio visualizada atualmente
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void atualizarSituacaoDePatio() throws ErroSistemicoException;

    /**
     * Salva e atualiza a situacao de patio visualizada atualmente apos uma edicao que que não for considerada como atividade
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void atualizarSituacaoDePatioAposEdicao() throws ErroSistemicoException;

    /**
     * Retorna toda a interfaceInicial do controlador
     * @return - A interface inicial manipulada
     */
    public InterfaceInicial getInterfaceInicial();

    /**
     * Passa a interfaceInicial para o controlador manipular
     * @param interfaceInicial
     */
    public void setInterfaceInicial(InterfaceInicial interfaceInicial);

    /**
     * Remove todas as seleções realizadas na interface do sistema
     */
    public void removeSelecoes();

    /**
     * Remove todas as seleções exceto material de usina, pilhas, balizas e cargas, ou seja, tudo que não faça oarte da blendagem
     */
    public void removeSelecoesQueNaoFacamParteDaBlendagem();

    /**
     * Remove todas as seleções de Balizas
     */
    public void removeSelecoesBalizasCheias();

    /**
     * Executa a integracao com o sistema SAP
     */
    public void executarIntegracaoSistemaSAP(Date dataExecucaoIntegracao) throws ErroSistemicoException;

    /**
     * Executa a integracao com o sistema CRM
     */
    public PlanoEmpilhamentoRecuperacao executarIntegracaoSistemaCRM(Date dataExecucaoIntegracao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao, Date dataSituacao) throws ErroSistemicoException;

    /**
     * Executa a integracao com o sistema MES
     */
    public PlanoEmpilhamentoRecuperacao executarIntegracaoSistemaMES(Date dataExecucaoIntegracao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao) throws ErroSistemicoException;

    
    public void removeSelecoesBalizasVazias();

    /** sera o modo de operacao na interface inicial */
    public void setModoOperacao(ModoDeOperacaoEnum modoOperacao);

    /** acesso ao subsistema da interface de comandos */
    public IControladorInterfaceComandos getInterfaceComandos();

    /** Ativa o modo de edicao */
    public void ativaModoEdicao();

    /** Desativa o modo de edicao */
    public void desativarModoEdicao();

   /* *//** retorna o controlador de acesso a base de dados *//*
    public IControladorModelo getModelo();*/

    public List<Usina> buscarUsinasSelecionadas();

    /**
     * Obtém o tipo de produto selecionado nos itens de blendagem
     * @return o tipo de produto selecionado nos itens de blendagem
     */
    public TipoProduto getTipoDeProdutoSelecionado();

    /**
     * desativa o processamento da mensagem de processamento
     */
    public void desativarMensagemProcessamento();

    /**
     * move o slider para situacao de patio anterior
     */
    public void exibirSituacaoPatioAnterior();

    /**
     * move o slider para situacao de patio posterior
     */
    public void exibirSituacaoPatioPosterior();

    /**
     * move o slider para última situacao de patio
     */
    public void exibirUltimaSituacaoPatio();

    /**
     * move o slider para primeira situacao de patio
     */
    public void exibirPrimeiraSituacaoPatio();

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

    public void recolherResultadoAmostragem(Atividade atividade) throws AtividadeException;

    public void atualizarEmpilhamento(Atividade atividadeEmpilhamento) throws AtividadeException, ValidacaoObjetosOperacaoException;

    public void atualizarEmpilhamentoEmergencia(Atividade atividadeEmpilhamentoEmergencia) throws AtividadeException, ValidacaoObjetosOperacaoException;
    
    public void atualizarRecuperacao(Atividade atividadeRecuperacao) throws ValidacaoObjetosOperacaoException;

    public void movimentar(Atividade atividade) throws AtividadeException, ValidacaoObjetosOperacaoException;
    
    public void editarBalizas(Atividade atividadeEdicao) throws AtividadeException;

    public void tratamentoPSM(Atividade atividade) throws AtividadeException;

    public List<Baliza> getBalizasComProdutoNoPatio(Patio patio,Date hora);

    public List<Baliza> getBalizasProdutoPatio(Patio patio, TipoDeProdutoEnum tipoProduto, Date hora);

    public List<Baliza> getBalizasDoTipo(Patio patio, EnumTipoBaliza tipoBaliza,Date hora);

    /**
     * Metodo que verifica se existe para as usinas um ritimo de producao direfente
     * do atual para atualizar a taxa de operacao das usinas e a mesma trabalhar
     * com a nova taxa nas proximas atividades.
     * Este metodo deve ser chamado antes de cada atividade que utiliza campanha de producao
     * das usinas
     *
     * @param dataHoraSituacao
     */
    public void verificarNovoRitimoProducaoUsinas(Date dataHoraSituacao) throws ErroSistemicoException;
    
    /**
     * Metodo que realiza a propagacao da fila de navios qdo a mesma for editada no passado
     * @param filaNavioPropagacao
     */
    public void propagarFilaDeNavios(FilaDeNavios filaNavioPropagacao);

    /**
     * Metodo que transforma o plano do usuario em um plano oficial
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    /**
     * Metodo que transforma o plano do usuario em um plano oficial
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void oficializarPlano() throws ErroSistemicoException;

    /**
     * Carrega o plano do usuario a partir de um oficial
     * @param idUser
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.PlanosOficiaisNaoLocalizadosException
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void carregarPlanoEmpilhamentoRecuperacaoOficialToUsuario(Long idUser) throws PlanosOficiaisNaoLocalizadosException, ErroSistemicoException;

    /**
     * Metodo que verifica se é necessario o carregamento do plano oficial
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.CarregarOficialNaoNecessariaException
     */
    public void verificarPossibilidadeCarregarOficial() throws CarregarOficialNaoNecessariaException;

    /**
     * Metodo que executa a atividade de retorno de pellet feed para filtragem
     * @param atividade
     */
    public void retornarPelletFeed(Atividade atividadeRetornoPelletFeed);

    public void verificarPossibilidadeCarregarPlanoUsuario() throws CarregarOficialNaoNecessariaException;

    /**
     * Executa a integracao com o PIMS
     * @param dataExecucaoIntegracao
     * @param planoEmpilhamentoRecuperacao
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public PlanoEmpilhamentoRecuperacao executarIntegracaoSistemaPIMS(Date dataExecucaoIntegracao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao) throws ErroSistemicoException;

    /**
     * Executa a atividade de pilha de emergencia
     * @param atividadePilhaEmergencia
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.AtividadeException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoObjetosOperacaoException
     */
    public void atualizarPilhaEmergencia(Atividade atividadePilhaEmergencia) throws AtividadeException, ValidacaoObjetosOperacaoException;

    /**
     * Executa a atividade de transporte de pilha de emergencia
     * @param atividadeTransportePilhaEmergencia
     * @throws AtividadeException
     * @throws ValidacaoObjetosOperacaoException
     */
    public void transportarPilhaEmergencia(Atividade atividadeTransportePilhaEmergencia) throws AtividadeException, ValidacaoObjetosOperacaoException;

    public void verificaMovimentacaoNavio(AtividadeAtracarDesAtracarNavioVO movimentarVO) throws AtividadeException;
    
    /**
     * Metodo que realiza a movimentacao da pilha de emergencia
     * @param atividadeMovimentarPilhaEmergencia
     * @throws AtividadeException
     * @throws ValidacaoObjetosOperacaoException
     */
    public void movimentarPilhaEmergencia(Atividade atividadeMovimentarPilhaEmergencia) throws AtividadeException, ValidacaoObjetosOperacaoException;

    /**
     * Metodo que realiza a movimentacao da pilha de PSM ou pellet feed
     * @param atividadeMovimentarPilhaEmergencia
     * @throws AtividadeException
     * @throws ValidacaoObjetosOperacaoException
     */
    public void movimentarPilhaPSMPelletFeed(Atividade atividadeMovimentarPilhaEmergencia) throws AtividadeException, ValidacaoObjetosOperacaoException;

    
    /**
     * Metodo que verifica se ja existe uma pilha no patio com o nome
     * passado como parametro
     * @param nomePilha
     * @throws ValidacaoCampoException
     */
    public void validarNomeDaPilha(String nomePilha) throws PilhaExistenteException;

    public boolean verificaPermissaoAtualizacaoProducao();

    public void validarAtividadeDeRecuperacaoEmExecucao(Navio navio) throws AtividadeException;
    
    /**
     * Realiza a atualizacao de recuperacao de uma usina
     * @param carga
     * @param usinaEditada
     * @param tipoProduto
     * @param numeroPorao
     * @param dataFinalAtividade
     * @param dataInicioAtividade
     * @throws ValidacaoObjetosOperacaoException
     * @throws AtividadeException 
     */
    public void atualizaRecuperacaoUsina(Usina usinaEditada,Filtragem filtragemEditada, Carga carga,  
 		   TipoProduto tipoProduto, String numeroPorao, Date dataFinalAtividade,
 		   Date dataInicioAtividade,Atividade atividadeExecutada,Campanha campanha) throws ValidacaoObjetosOperacaoException, AtividadeException;

    
    /**
     * Metodo que verifica se existe alguma atividade de atualizacao de empilhamento em 
     * aberto para a usina informada.
     * @param usina
     * @return lista de maquinas que estao realizando o(s) empilhamento(s)
     */
    public List<MaquinaDoPatio> verificarEmpilhamentoEmAndamentoNaUsina(Usina usina,Date data);
    
    /** Retorna true se a situacao exibida é a ultima situacao do planejamento */
    public boolean estaNaUltimaSituacao();

    /**
     * finalizarAtualizacaoEmpilhamento
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 07/07/2010
     * @see
     * @param 
     * @return void
     */
    public void finalizarAtualizacaoEmpilhamento(Atividade atividade, Date dataFim)
    throws ValidacaoObjetosOperacaoException, AtividadeException;

    public List<MaquinaDoPatio> verificarEmpilhamentoEmAndamentoNaFiltragem(Filtragem filtragemEditada,
                    Date dataInicioAtividade);

    void executarIntegracaoSistemaNavioCRM(MetaNavio metaNavio, Date dataSituacao) throws ErroSistemicoException;
    
    public void editarMaquinas(Atividade atividadeEdicao) throws AtividadeException;
}
