package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.util.Date;
import java.util.List;

import com.hdntec.gestao.cliente.interfaceDeDialogo.ModoDeOperacaoEnum;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfacePier;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.PilhaPelletFeedNaoEncontradaException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;


/**
 * Interface de acesso às operações do subsistema de interface gráfica DSP.
 * 
 * @author andre
 * 
 */
public interface IControladorDSP {

    /**
     * Apresenta uma determinada {@link SituacaoPatio}. Essa situação de pátio é apresentada nos diversos contextos de cada uma das interfaces que representam itens de situação de pátio.
     * 
     * @param situacaoDePatio
     *           a situação de pátio a ser representada na interface
     */
    public void mostrarSituacaoDePatio(SituacaoPatio situacaoDePatio);

    /**
     * Busca a lista de campanhas de usinas selecionadas na interface
     * 
     * @return a lista de campanhas de usinas selecionadas na interface
     */
    public List<Campanha> buscarCampanhasSelecionadas();

    /**
     * Busca a lista de balizas selecionadas na interface
     * 
     * @return a lista de balzias selecionadas na interface
     */
    public List<Baliza> buscarBalizasSelecionadas();

    /**
     * Seleciona a baliza 
     * @throws BlendagemInvalidaException 
     * @throws ProdutoIncompativelException caso o produto selecionado seja incompatível com os produtos previamente selecionados para blendagem
     * @throws CampanhaIncompativelException
     * @throws ExcessoDeMaterialParaEmbarqueException caso a quantidade de material selecionado para blendagem exceda a quantidade necessária para atender a carga
     * @throws BlendagemIrrelevanteException
     */
    public void selecionaBaliza(Baliza balizaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException,  ExcessoDeMaterialParaEmbarqueException;

    /**
     * Deseleciona a baliza 
     * @throws BlendagemInvalidaException
     * @throws ProdutoIncompativelException
     * @throws CampanhaIncompativelException caso o produto da campanha selecionada seja incompatível com os previamente selecionados
     * @throws BlendagemIrrelevanteException
     */
    public void deselecionaBaliza(Baliza balizaDeselecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException ;

    /**
     * Seleciona a lista de balizas
     * @throws BlendagemInvalidaException
     * @throws ProdutoIncompativelException caso o produto selecionado seja incompatível com os produtos previamente selecionados para blendagem
     * @throws CampanhaIncompativelException
     * @throws ExcessoDeMaterialParaEmbarqueException caso a quantidade de material selecionado para blendagem exceda a quantidade necessária para atender a carga
     * @throws BlendagemIrrelevanteException
     */
    public void selecionaBaliza(List<InterfaceBaliza> listaBalizaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException,  ExcessoDeMaterialParaEmbarqueException;

    /**
     * Deseleciona a lista de balizas
     * @throws BlendagemInvalidaException
     * @throws ProdutoIncompativelException
     * @throws CampanhaIncompativelException caso o produto da campanha selecionada seja incompatível com os previamente selecionados
     * @throws BlendagemIrrelevanteException
     */
    public void deselecionaBaliza(List<InterfaceBaliza> listtaBalizaDeselecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException ;


    /**
     * Deseleciona todas as balizas. Implementado para empilhamento, nao funciona para recuperacao.*/
    public void deselecionaBalizas();

    public List<InterfaceBaliza> getListaDeBalizas();

    /**
     * Seleciona a campanha da usina para blendagem
     * @param campanhaSelecionada
     * @param quantidade a quantidade a ser blendada
     * @param tipoDeProdutoSelecionado o tipo de produto selecionado na usina
     * @throws BlendagemInvalidaException
     * @throws CampanhaIncompativelException caso o produto da campanha selecionada seja incompatível com os previamente selecionados
     * @throws ExcessoDeMaterialParaEmbarqueException caso a quantidade de material selecionado para blendagem exceda a quantidade necessária para atender a carga
     * @throws BlendagemIrrelevanteException
     */
    public void selecionaCampanhaDeUsina(Campanha campanhaSelecionada, Double quantidade, TipoDeProdutoEnum tipoDeProdutoSelecionado) throws BlendagemInvalidaException, CampanhaIncompativelException,  ExcessoDeMaterialParaEmbarqueException, ProdutoIncompativelException;

    /**
     * Deseleciona a campanha da usina para blendagem
     * @param campanhaSelecionada
     * @throws BlendagemInvalidaException
     * @throws BlendagemIrrelevanteException
     */
    public void deselecionaCampanhaDeUsina(Campanha campanhaSelecionada) throws BlendagemInvalidaException, ProdutoIncompativelException;

    /**
     * Ativa o objeto que exibirá uma mensagem na tela para o usuário
     * @param mensagem
     */
    public void ativarMensagem(InterfaceMensagem interfaceMensagem);

    /** Obtem o modo de operacao selecionado */
    public ModoDeOperacaoEnum obterModoOperacao();

    /**
     * Metodo que percorre a lista de máquinas adicionadas ao DSP e cria uma lista com
     * as que foram selecionadas pelo usuário
     * 
     * @return a lista de máquinas selecionadas
     */
    public List<MaquinaDoPatio> getListaMaquinasSelecionadas();

    /**
     * 
     * @return a lista de correias do DSP
     */
    public List<InterfaceCorreia> getListaDeCorreias();
    
    /**
     * 
     * @return a lista de pátios do DSP
     */
    public List<InterfacePatio> getListaDePatios();
    
    /**
     * 
     * @return a lista de usinas do DSP
     */
    public List<InterfaceUsina> getListaDeUsinas();

    /**
     * verifica se os componentes pertencentes ao DSP podem ser editados, caso contrario retorna uma Exception
     */
    public void verificarModoDeEdicao() throws ModoDeEdicaoException;

    /**
     * Verifica se alguma baliza foi selecionada para poder ser habilitado a edicao
     */
    public void verificarSelecaoDeBalizasParaEdicao() throws ModoDeEdicaoException;
    
        /**
     * Obtém as usinas selecionadas atualmente no DSP
     * @return as usinas selecionadas atualmente no DSP
     */
   public List<Usina> getUsinasSelecionadas();

   /**
    * Obtém o tipo de produto selecionado nos itens de blendagem
    * @return o tipo de produto selecionado nos itens de blendagem
    */
   public TipoProduto getTipoDeProdutoSelecionado();


   /**
    * Atualizacao de recuperacao com maquina
    * @param maquinaDoPatioVisualizada a maquina que esta recuperando
    * @param interfaceMaquinaDoPatio a interface gráfica representando a máquina
    * @throws ValidacaoCampoException
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   public void atualizaRecuperacaoMaquina(MaquinaDoPatio maquinaDoPatioVisualizada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;

   /**
    * Atualizacao de empilhamento com maquina
    * @param maquinaDoPatioVisualizada a maquina que esta empilhando
    * @param interfaceMaquinaDoPatio a interface gráfica representando a máquina
    * @throws ValidacaoCampoException
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   public void atualizaEmpilhamentoMaquina(MaquinaDoPatio maquinaDoPatioVisualizada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio, 
		   boolean finalizaAtualizao) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;
   
   
   /**
    * Retorna a interface que eh gerenciada pelo controlador DSP
    * @return
    */
   public InterfaceDSP getInterfaceDSP();


   /**
    * Atualizacao de empilhamento com usina
    * @param usinaVisualizada a usina que esta recuperando
    * @param interfaceUsina a interface gráfica representando a usina
    * @throws ValidacaoCampoException
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   public void atualizaRecuperacaoUsina(Usina usinaVisualizada, InterfaceUsina interfaceUsina) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;


   /**
    * Metodo que executa a operacao de retorno de pellet feed para filtragem
    * @param interfaceMaquinaDoPatio
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    */
   public void retornarPelletFeed(InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException, PilhaPelletFeedNaoEncontradaException;

   /**
    * Metodo que executa a operacao de atualizacao de empilhamento para pilha de emergencia
    * @param usinaVisualizada
    * @param interfaceUsina
    * @throws OperacaoCanceladaPeloUsuarioException
    * @throws ErroSistemicoException
    */
   public void atualizaPilhaEmergencia(Usina usinaVisualizada, InterfaceUsina interfaceUsina) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;
   
   /**
    * Metodo que executa a atividade de movimentacao de uma pilha de emergencia
    * @param maquinaSelecionada
    * @param interfaceMaquina
    * @throws OperacaoCanceladaPeloUsuarioException
    * @throws ErroSistemicoException
    */
   public void movimentarPilhaEmergencia(MaquinaDoPatio maquinaSelecionada, InterfaceMaquinaDoPatio interfaceMaquina)  throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;
   
   /**
    * Metodo que executa a atividade de movimentacao de uma pilha PSM ou pellet screening
    * @param maquinaSelecionada
    * @param interfaceMaquina
    * @throws OperacaoCanceladaPeloUsuarioException
    * @throws ErroSistemicoException
    */
   public void movimentarPilhaPellet(MaquinaDoPatio maquinaSelecionada, InterfaceMaquinaDoPatio interfaceMaquina, TipoDeProdutoEnum tipoProduto)  throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;
   
   /**
    * Metodo que unificar pilhas para realizacao de recuperacao
    * @throws OperacaoCanceladaPeloUsuarioException
    * @throws ErroSistemicoException
    */
   public void unificarPilhas() throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;

   /**
    * Metodo que verifica necessidade de consolidar o sistema antes de sincronizar com MES, para evitar perda de movimentações caso
    * a comunicação com o MES ou PIMS retorne erro
    * @return
    */
   public boolean verificaNecessidadeConsolidarParaIntegrar();
   /**
    * Metodo que verifica necessidade de integrar o MESPATIO com o MES/PIMS antes de finalizar uma recuperacao de maquina
    * @return
    */
   public boolean verificaNecessidadeIntegracaoAntesRecuperacao();
   
   public void atualizaEstadoMaquinas(Date data, List<MaquinaDoPatio> listaDeMaquinas);
   
   
   public void movimentarPilhaPSM(MaquinaDoPatio maquinaSelecionada, InterfaceMaquinaDoPatio interfaceMaquina, TipoDeProdutoEnum tipoProduto)  throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;

   public void atualizaRecuperacaoFiltragem(Filtragem filtragemSelecionada, Usina usinaVisualizada,InterfaceFiltragem interfaceFiltragem)
                throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;

   public void interditaPier(InterfacePier interfacePier) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;

   public void interditarBaliza(InterfacePatio interfacePatio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException;
   
   
}//fim de classe
