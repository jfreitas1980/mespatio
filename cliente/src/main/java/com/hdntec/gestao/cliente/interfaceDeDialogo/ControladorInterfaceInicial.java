
package com.hdntec.gestao.cliente.interfaceDeDialogo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceBlendagem.IControladorInterfaceBlendagem;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceBlendagem.InterfaceBlendagem;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos.IControladorInterfaceComandos;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.ControladorInterfaceFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.IControladorInterfaceFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceBerco;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceCarga;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfacePier;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.NavioNaFilaPnl;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.IControladorDSP;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceBaliza;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceCorreia;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceMaquinaDoPatio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfacePatio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceUsina;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceNavegacao.IControladorInterfaceNavegacaoEventos;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento.ControladorPlanejamento;
import com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento.IControladorPlanejamento;
import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.blendagem.IControladorCalculoQualidade;
import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.integracao.IntegracaoRPUSINAS;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.domain.vo.atividades.AtualizarEmpilhamentoVO;
import com.hdntec.gestao.domain.vo.atividades.AtualizarRecuperacaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CargaSelecionadaException;
import com.hdntec.gestao.exceptions.CarregarOficialNaoNecessariaException;
import com.hdntec.gestao.exceptions.ConsolidacaoNaoNecessariaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;
import com.hdntec.gestao.exceptions.PilhaExistenteException;
import com.hdntec.gestao.exceptions.PlanosOficiaisNaoLocalizadosException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.exceptions.RemocaoDePlanosNaoPermitidaException;
import com.hdntec.gestao.exceptions.TempoInsuficienteException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.integracao.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.integracao.IControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.integracao.integracaoMES.InterfaceMES;
import com.hdntec.gestao.login.entity.Permission;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeEmpilhamento;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeRecuperacao;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeEmpilhamento;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeRecuperacao;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Controlador das operaÃ§Ãµes do subsistema de interface de diÃ¡logo.
 *
 * @author andre
 *
 */
public class ControladorInterfaceInicial implements IControladorInterfaceInicial {

    InterfaceMensagem interfaceMensagem;

    /** acesso ao subsistema de integração de sistemas externos */
    private IControladorIntegracaoSistemasExternos integracaoSistemasExternos;

    /** acesso ao subsistema de interface de comandos */
    private IControladorInterfaceComandos interfaceComandos;

    /** acesso ao subsistema de interface de blendagem */
    private IControladorInterfaceBlendagem controladorInterfaceBlendagem;

    /** acesso ao subsistema de interface de navegação dos eventos */
    private IControladorInterfaceNavegacaoEventos interfaceNavegacaoEventos;

    /** acesso ao subsistema de interface de fila de navios */
    private IControladorInterfaceFilaDeNavios interfaceFilaDeNavios;

    /** acesso ao subsistema da interface do dsp */
    private IControladorDSP dsp;

    /** acesso ao subsistema de cálculo da qualidade */
    private IControladorCalculoQualidade calculoQualidade;

    /** acesso ao subsistema de planejamento de atividades */
    private IControladorPlanejamento planejamento;

    /** acesso ao subsistema de atualizacao dos sistemas externos */
    //private IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos;

    /** interface principal do sistema */
    private InterfaceInicial interfaceInicial;

    private static IControladorModelo controladorModelo = null;

    public static IControladorModelo getControladorModelo() {
        if (controladorModelo == null) {
            controladorModelo = new ControladorModelo();
        }
        return controladorModelo;
    }

    public ControladorInterfaceInicial() throws ErroSistemicoException {
        try {
            //		   Session session = buscarSessionServer();

            buscarPlanoEmpilhamentoRecuperacaoOficial(Long.parseLong(PropertiesUtil
                            .buscarPropriedade("parametro.horas.planejado.oficial")));

            /*for(SituacaoPatio situacaoPatio : planejamento.getControladorDePlano().getPlanoEmpilhamentoRecuperacao().getListaSituacoesPatio()) {
               atualizarDadosSituacaoPatio(situacaoPatio);
            }*/

        } catch (PlanosOficiaisNaoLocalizadosException e) {
            JOptionPane.showMessageDialog(this.interfaceInicial, e.getMessage(), PropertiesUtil.getMessage("popup.atencao"),
                            JOptionPane.INFORMATION_MESSAGE);
            throw new ErroSistemicoException(e);
        }
    }

    public void atualizarDadosSituacaoPatio(SituacaoPatio situacaoPatio) throws Exception, ErroSistemicoException {

        List<Patio> listaPatios = atualizarListaBalizasPorPatios(situacaoPatio.getPlanta().getListaPatios(
                        situacaoPatio.getDtInicio()));

        //TODO Darley buscar as maquinas dos patios
        List<Patio> listaMaquinasDoPatio = atualizarMaquinasDoPatio(listaPatios);

        situacaoPatio.getPlanta().setListaPatios(listaMaquinasDoPatio, situacaoPatio.getDtInicio());

        //TODO Darley Saxxxx Buscar as Maquinas das correias
        List<Correia> listaCorreia = atualizarListaMaquinasCorreias(situacaoPatio.getPlanta().getListaCorreias(
                        situacaoPatio.getDtInicio()));
        situacaoPatio.getPlanta().setListaCorreias(listaCorreia, situacaoPatio.getDtInicio());

        //TODO Darley SAxxxx Fazendo a busca da lista de emergencias na Usina
        List<Usina> listaUsinas = atualizarListaEmergenciaUsinas(situacaoPatio.getPlanta().getListaUsinas(
                        situacaoPatio.getDtInicio()));
        situacaoPatio.getPlanta().setListaUsinas(listaUsinas, situacaoPatio.getDtInicio());

        //TODO Darley SA11079 Carregar a lista de Berco de Atracacao do Pier
        //	   List<Pier> listaPiers = atualizarListaBercoAtracacaoPiers()

    }

    @Override
    public void posicionaSituacaoPatioSlider(SituacaoPatio situacaoPatio) {
        interfaceInicial.getSliderNavegacao().setValue(situacaoPatio.getIndiceSituacaoPatio());
    }

    @Override
    public void verificaBlendagemRemocaoCampanhaDeUsina(Campanha campanhaSelecionada) throws BlendagemInvalidaException,
                    ProdutoIncompativelException {
        Blendagem blendCampanha = calculoQualidade.removerBlendagemCampanhaDeUsina(campanhaSelecionada);
        controladorInterfaceBlendagem.apresentarBlendagem(blendCampanha);
    }

    @Override
    public void verificaBlendagemCampanhaDeUsina(Campanha campanhaSelecionada, Double quantidade,
                    TipoDeProdutoEnum tipoDeProdutoSelecionado) throws BlendagemInvalidaException,
                    CampanhaIncompativelException, ExcessoDeMaterialParaEmbarqueException, ProdutoIncompativelException {
        Blendagem blendCampanha = calculoQualidade.blendarCampanhaDeUsina(campanhaSelecionada, quantidade,
                        tipoDeProdutoSelecionado);
        controladorInterfaceBlendagem.apresentarBlendagem(blendCampanha);
    }

    @Override
    public void verificaBlendagemBaliza(Baliza balizaSelecionada) throws BlendagemInvalidaException,
                    CampanhaIncompativelException, ProdutoIncompativelException, ExcessoDeMaterialParaEmbarqueException {
        Blendagem blendBaliza;
        blendBaliza = calculoQualidade.blendarBaliza(balizaSelecionada);
        controladorInterfaceBlendagem.apresentarBlendagem(blendBaliza);
    }

    @Override
    public void verificaRemocaoBlendagemBaliza(Baliza balizaDeselecionada) throws BlendagemInvalidaException,
                    CampanhaIncompativelException, ProdutoIncompativelException {
        Blendagem blendBaliza = calculoQualidade.removerBlendagemBaliza(balizaDeselecionada);
        controladorInterfaceBlendagem.apresentarBlendagem(blendBaliza);
    }

    @Override
    public void verificaBlendagemCarga(Carga cargaSelecionada) throws BlendagemInvalidaException,
                    CampanhaIncompativelException, ProdutoIncompativelException, CargaSelecionadaException,
                    ExcessoDeMaterialParaEmbarqueException {
        Blendagem blendCarga = calculoQualidade.atenderCarga(cargaSelecionada);
        controladorInterfaceBlendagem.apresentarBlendagem(blendCarga);
    }

    @Override
    public void verificaRemocaoBlendagemCarga(Carga cargaSelecionada) throws BlendagemInvalidaException,
                    ProdutoIncompativelException {
        Blendagem blendCarga = calculoQualidade.removerCarga(cargaSelecionada);
        controladorInterfaceBlendagem.apresentarBlendagem(blendCarga);
    }

    @Override
    public void empilhar(Atividade atividadeEmpilhamento) throws BlendagemInvalidaException, TempoInsuficienteException,
                    CampanhaIncompativelException, ValidacaoObjetosOperacaoException {
        planejamento.planejarEmpilhamento(atividadeEmpilhamento, interfaceInicial.getSituacaoPatioExibida());
    }

    @Override
    public void recuperar(Atividade atividade) throws ValidacaoObjetosOperacaoException {
        planejamento.planejarRecuperacao(atividade, interfaceInicial.getSituacaoPatioExibida());
    }

    @Override
    public void mudarCampanha(List<Atividade> atividades) throws AtividadeException {
        planejamento.planejarMudancaCampanha(atividades);
    }

    @Override
    public void recolherResultadoAmostragem(Atividade atividade) throws AtividadeException {
        planejamento.planejarResultadoAmostragem(atividade);
    }

    @Override
    public void atualizarPlanoOficialComAtividades() throws BlendagemInvalidaException, CampanhaIncompativelException,
                    TempoInsuficienteException {
    }

    @Override
    public void chavearAtualizacaoAutomatica(boolean atualizacaoAtivada) {
        //Darley removendo chamada remota
        IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos = InterfaceInicial
                        .lookUpIntegracaoSistemasExternos();
        // IControladorIntegracaoSistemasExternos  controladorIntegracaoSistemasExternos = new ControladorIntegracaoSistemasExternos();
        controladorIntegracaoSistemasExternos.chavearAtualizacaoAutomatica(atualizacaoAtivada);
        controladorIntegracaoSistemasExternos = null;
    }

    @Override
    public void apagarSituacaoPatio(Integer indiceDaSituacaoDePatioExibida) throws ErroSistemicoException {
        planejamento.removerPlano(indiceDaSituacaoDePatioExibida);
    }

    @Override
    public void consolidarPlano() throws ErroSistemicoException {
        planejamento.consolidarPlano();
    }

    @Override
    public void removeSelecoes() {
        //[SA8753] - Funcionalidade para remover todas as selecoes feitas na interface principal
        // percorre todas as correias do patio e suas respectivas mÃ¡quinas
        Blendagem.getInstance().setMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem(null);   
        Blendagem.getInstance().setProdutoResultante(null);
        Blendagem.getInstance().setListaBalizasBlendadas(null);
        Blendagem.getInstance().setCargaSelecionada(null);
        Blendagem.getInstance().setListaDeCampanhas(null);
        Blendagem.getInstance().setListaDeProdutosSelecionados(null);
        InterfaceBlendagem blend = dsp.getInterfaceDSP().getInterfaceInicial().getInterfaceBlendagem();        

        if (blend!= null) blend.apresentarBlendagem(Blendagem.getInstance());
        
        for (InterfaceCorreia interfaceCorreia : dsp.getListaDeCorreias()) {
            // Deseleciona as mÃ¡quinas
            for (InterfaceMaquinaDoPatio interfaceMaquina : interfaceCorreia.getListaDeMaquinas()) {
                if (interfaceMaquina.isSelecionada()) {
                    interfaceMaquina.deselecionarMaquina();
                }
            }
        }
        // percorre todos os patios
        for (InterfacePatio interfacePatio : dsp.getListaDePatios()) {
            // Deseleciona as  mÃ¡quinas do patio
            for (InterfaceMaquinaDoPatio interfaceMaquina : interfacePatio.getListaDeMaquinas()) {
                if (interfaceMaquina.isSelecionada()) {
                    interfaceMaquina.deselecionarMaquina();
                }
            }
            // Deseleciona as balizas selecionadas
            for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas()) {
                if (interfaceBaliza.isSelecionada()) {
                    interfaceBaliza.deselecionar();
                }
                //deselecionar uma eventual pilha que esteja selecionada e contendo esta baliza
                interfaceBaliza.deselecionarPilhaDestaBaliza();
            }
        }
/*        // Deseleciona os produtos vindos diretamente da usina
        for (InterfaceUsina interfaceUsina : dsp.getListaDeUsinas()) {
            if (interfaceUsina.isSelecionada()) {
                interfaceUsina.deselecionar();
            }
        }
*/        // percorre a lista de piers ate chegar a carga
        for (InterfacePier interfacePier : interfaceInicial.getInterfaceFilaDeNavios().getListaDePiers()) {
            // percorre a lista de bercos ate chegar aos navios
            for (InterfaceBerco interfaceBerco : interfacePier.getListaDeBercos()) {
                // Deseleciona as cargas
                if (interfaceBerco.getNavioAtendido() != null) {
                    for (InterfaceCarga interfaceCarga : interfaceBerco.getNavioAtendido().getListaDecarga()) {
                        if (interfaceCarga.isSelecionada()) {
                            interfaceCarga.deselecionar();
                        }
                    }
                }
            }
        }

        // percorre os navios da fila de navios para remover a selecoes da carga
        for (NavioNaFilaPnl navioFila : interfaceInicial.getInterfaceFilaDeNavios().getListaInterfaceNavio()) {
            for (InterfaceCarga interfaceCarga : navioFila.getInterfaceNavio().getListaDecarga()) {
                if (interfaceCarga.isSelecionada()) {
                    interfaceCarga.deselecionar();
                }
            }
        }

        dsp.getListaDeBalizas().clear();

        // repinta a interface inicial
        interfaceInicial.repaint();
    }

    @Override
    public void removeSelecoesQueNaoFacamParteDaBlendagem() {
        //[SA8753] - Funcionalidade para remover todas as seleÃ§Ãµes feitas na interface principal que nÃ£o faÃ§am parte da blendagem. Utilizada ao cancelar algum modo de operaÃ§Ã£o (empilhamento, recuperaÃ§Ã£o)
        // percorre todas as correias do patio e suas respectivas mÃ¡quinas
        for (InterfaceCorreia interfaceCorreia : dsp.getListaDeCorreias()) {
            //Deseleciona as mÃ¡quinas
            for (InterfaceMaquinaDoPatio interfaceMaquina : interfaceCorreia.getListaDeMaquinas()) {
                if (interfaceMaquina.isSelecionada()) {
                    interfaceMaquina.deselecionarMaquina();
                }
            }
        }
        // percorre todos os patios
        for (InterfacePatio interfacePatio : dsp.getListaDePatios()) {
            //Deseleciona pas-carregadeiras
            for (InterfaceMaquinaDoPatio interfaceMaquina : interfacePatio.getListaDeMaquinas()) {
                if (interfaceMaquina.isSelecionada()) {
                    interfaceMaquina.deselecionarMaquina();
                }
            }

            //Deseleciona as balizas vazias e selecionadas (no caso do empilhamento, pode haver uma baliza inicial vazia selecionada, Ã© importanted eseleciona-la!)
            for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas()) {
                if (interfaceBaliza.isSelecionada()) {
                    if (interfaceBaliza.getBalizaVisualizada().getProduto() == null) {
                        interfaceBaliza.deselecionar();
                    }
                }
            }
        }

        // repinta a interface inicial
        interfaceInicial.repaint();
    }

    @Override
    public void removeSelecoesBalizasCheias() {

        // percorre todos os patios
        for (InterfacePatio interfacePatio : dsp.getListaDePatios()) {
            // Deseleciona as balizas selecionadas
            for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas()) {
                if (interfaceBaliza.isSelecionada()) {
                    Produto produtoDaBaliza = interfaceBaliza.getBalizaVisualizada().getProduto();
                    if (produtoDaBaliza != null
                                    && (produtoDaBaliza.getQuantidade() >= interfaceBaliza.getBalizaVisualizada()
                                                    .getCapacidadeMaxima())) {
                        interfaceBaliza.deselecionar();
                    }
                }
                // deselecionar uma eventual pilha que contenha esta baliza
                interfaceBaliza.deselecionarPilhaDestaBaliza();
            }
        }
        // repinta a interface inicial
        interfaceInicial.repaint();
    }

    @Override
    public void removeSelecoesBalizasVazias() {

        // percorre todos os patios
        for (InterfacePatio interfacePatio : dsp.getListaDePatios()) {
            // Deseleciona as balizas selecionadas
            for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas()) {
                if (interfaceBaliza.isSelecionada()) {
                    if (interfaceBaliza.getBalizaVisualizada().getProduto() == null) {
                        interfaceBaliza.deselecionar();
                    }
                }
            }
        }
        // repinta a interface inicial
        interfaceInicial.repaint();
    }

    @Override
    public List<Campanha> buscarCampanhasSelecionadas() {
        return calculoQualidade.obterListaCampanhasBlendadas();
    }

    @Override
    public List<Baliza> buscarBalizasSelecionadas() {
        return calculoQualidade.obterListaBalizasBlendada();
    }

    @Override
    public void ativarMensagem(InterfaceMensagem interfaceMensagem) {
        interfaceInicial.ativarMensagem(interfaceMensagem);
    }

    @Override
    public ModoDeOperacaoEnum obterModoOperacao() {
        return interfaceInicial.getModoDeOperacao();
    }

    public IControladorCalculoQualidade getCalculoQualidade() {
        return calculoQualidade;
    }

    public void setCalculoQualidade(IControladorCalculoQualidade calculoQualidade) {
        this.calculoQualidade = calculoQualidade;
    }

    public IControladorDSP getDsp() {
        return dsp;
    }

    public void setDsp(IControladorDSP dsp) {
        this.dsp = dsp;
    }

    /*   public IControladorIntegracaoSistemasExternos getIntegracaoSistemasExternos()
       {
          return integracaoSistemasExternos;
       }

       public void setIntegracaoSistemasExternos(IControladorIntegracaoSistemasExternos integracaoSistemasExternos)
       {
          this.integracaoSistemasExternos = integracaoSistemasExternos;
       }
    */
    public IControladorInterfaceBlendagem getControladorInterfaceBlendagem() {
        return controladorInterfaceBlendagem;
    }

    public void setInterfaceBlendagem(IControladorInterfaceBlendagem controladorInterfaceBlendagem) {
        this.controladorInterfaceBlendagem = controladorInterfaceBlendagem;
    }

    @Override
    public IControladorInterfaceComandos getInterfaceComandos() {
        return interfaceComandos;
    }

    public void setInterfaceComandos(IControladorInterfaceComandos interfaceComandos) {
        this.interfaceComandos = interfaceComandos;
    }

    public ControladorInterfaceFilaDeNavios getInterfaceFilaDeNavios() {
        return (ControladorInterfaceFilaDeNavios) interfaceFilaDeNavios;
    }

    public void setInterfaceFilaDeNavios(IControladorInterfaceFilaDeNavios interfaceFilaDeNavios) {
        this.interfaceFilaDeNavios = interfaceFilaDeNavios;
    }

    @Override
    public InterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    @Override
    public void setInterfaceInicial(InterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;
    }

    /*  @Override
      public IControladorModelo getModelo()
      {
         return InterfaceInicial.lookUpModelo();
      }

      public void setModelo(IControladorModelo modelo)
      {
         this.modelo = modelo;
      }*/

    @Override
    public IControladorPlanejamento getPlanejamento() {
        return planejamento;
    }

    public void setPlanejamento(IControladorPlanejamento planejamento) {
        this.planejamento = planejamento;
    }

    /**
     * Limpa a blendagem quando ha uma mudanca de situacao apresentada
     */
    @Override
    public void limparBlendagem() {
        calculoQualidade.limparBlendagem();
    }

    @Override
    public void buscarPlanoEmpilhamentoRecuperacaoPorUsuario(Long idUser) throws ErroSistemicoException {
        //	   IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao = controladorModelo
                        .buscarPlanoEmpilhamentoRecuperacaoPorUsuario(idUser);

        /**TODO RECUEPRAR SITUA??ES DENTRO DO PERIODO*/
        if (planoEmpilhamentoRecuperacao == null) {
            planoEmpilhamentoRecuperacao = criarPlanoUsuario(idUser);
        }
        controladorModelo = null;
        this.planejamento = new ControladorPlanejamento(planoEmpilhamentoRecuperacao);
        this.planejamento.setControladorInterfaceInicial(this);

    }

    /**
     * Cria Plano para usuario e consolida
     * @param idUser
     * @return
     * @throws ErroSistemicoException
     */
    private PlanoEmpilhamentoRecuperacao criarPlanoUsuario(Long idUser) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao;
        PlanoEmpilhamentoRecuperacao planoUsuario = montarPlanoDoUsuario(idUser);

        //      IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        planoEmpilhamentoRecuperacao = controladorModelo.salvarPlanoDeEmpilhamentoERecuperacao(planoUsuario);
        controladorModelo = null;
        return planoEmpilhamentoRecuperacao;
    }

    /**
     * @param idUser
     * @return
     * @throws ErroSistemicoException
     */
    private PlanoEmpilhamentoRecuperacao montarPlanoDoUsuario(Long idUser) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao;
        List<SituacaoPatio> lstSitucaoClone = new ArrayList<SituacaoPatio>();
        /** RECUPERAR PLANO DO USUARIO , CLONAR E CONSOLIDAR COMO SENDO DO USUARIO*/
        //      IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        planoEmpilhamentoRecuperacao = controladorModelo.buscarPlanoEmpilhamentoRecuperacaoPorUsuario(idUser);
        PlanoEmpilhamentoRecuperacao planoUsuario;
        if (planoEmpilhamentoRecuperacao == null) {
            //busca o plano oficial
            planoEmpilhamentoRecuperacao = controladorModelo.buscarPlanoEmpilhamentoRecuperacaoOficial();
            /**Criando clone das situaçao para associar ao novo usuario*/
            planoUsuario = new PlanoEmpilhamentoRecuperacao();
            for (SituacaoPatio sit : planoEmpilhamentoRecuperacao.getListaSituacoesPatio()) {
                SituacaoPatio nova = new SituacaoPatio();
                nova.setPlanoEmpilhamento(planoUsuario);
                nova.setIdUser(idUser);
                lstSitucaoClone.add(nova);
            }

            planoUsuario.setIdUser(idUser);
            planoUsuario.setListaSituacoesPatio(lstSitucaoClone);
            planoUsuario.setDtInsert(new Date(System.currentTimeMillis()));
            planoUsuario.setEhOficial(Boolean.FALSE);
            //planoUsuario.setDataInicio(new Date(System.currentTimeMillis()));
            //salva plano criado
            planoUsuario = controladorModelo.salvarPlanoDeEmpilhamentoERecuperacao(planoUsuario);
            controladorModelo = null;
        } else {
            planoUsuario = planoEmpilhamentoRecuperacao;
        }
        return planoUsuario;
    }

    /**
     * Busca o plano de empilhamento e recuperacao oficial do sistema
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    @Override
    public void buscarPlanoEmpilhamentoRecuperacaoOficial(Long qtdeHorasSituacoesAnteriores)
                    throws PlanosOficiaisNaoLocalizadosException, ErroSistemicoException {
    	this.planejamento = null;
    	System.out.println("Inicio" + new Date(System.currentTimeMillis()));
        //IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao = getControladorModelo()
                        .buscarPlanoEmpilhamentoRecuperacaoOficial(qtdeHorasSituacoesAnteriores);
        controladorModelo = null;
        if (planoEmpilhamentoRecuperacao == null) {
            throw new PlanosOficiaisNaoLocalizadosException(PropertiesUtil
                            .buscarPropriedade("exception.carregar.planos.planejamentos.nao.encontrados"));
        }
        System.out.println("Fim leitura" + new Date(System.currentTimeMillis()));
        //planoEmpilhamentoRecuperacao.setIdUser(InterfaceInicial.getUsuarioLogado().getId());      
        this.planejamento = new ControladorPlanejamento(planoEmpilhamentoRecuperacao);
        this.planejamento.setControladorInterfaceInicial(this);
    }

    
    /**
     * Busca o plano de empilhamento e recuperacao oficial do sistema
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    @Override
    public void carregarPlanoEmpilhamentoRecuperacaoOficialToUsuario(Long idUser)
                    throws PlanosOficiaisNaoLocalizadosException, ErroSistemicoException {
        PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao = montarPlanoDoUsuario(idUser);
        if (planoEmpilhamentoRecuperacao == null) {
            throw new PlanosOficiaisNaoLocalizadosException(PropertiesUtil
                            .buscarPropriedade("exception.carregar.planos.planejamentos.nao.encontrados"));
        }
        planejamento.carregarPlanoEmpilhamentoRecuperacaoOficialToUsuario(planoEmpilhamentoRecuperacao);
    }

    @Override
    public void buscarNovasSituacoesRealizadasDoPlanoEmpilhamentoRecuperacaoOficial() {
        throw new UnsupportedOperationException();
    }

    /**
     * Verifica se o plano visualizado pode ser salvo como oficial
     */
    public void verificaSePlanoOficialVisualizadoPodeSerSalvo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void apresentarSituacaoDePatio(int indiceSituacaoDePatioASerApresentada) {
        throw new UnsupportedOperationException();
    }

    /*   @Override
       public IControladorIntegracaoSistemasExternos getControladorIntegracaoSistemasExternos()
       {
          return controladorIntegracaoSistemasExternos;
       }

       public void setControladorIntegracaoSistemasExternos(IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos)
       {
          this.controladorIntegracaoSistemasExternos = controladorIntegracaoSistemasExternos;
       }
    */
    @Override
    public void atualizarSituacaoDePatio() throws ErroSistemicoException {
      //  this.interfaceInicial.atualizarSituacaoDePatio();
    }

    @Override
    public void atualizarSituacaoDePatioAposEdicao() throws ErroSistemicoException {
       // this.interfaceInicial.atualizarSituacaoDePatioAposEdicao();
    }

    @Override
    public void executarIntegracaoSistemaSAP(Date dataExecucaoIntegracao) throws ErroSistemicoException {
        /**
              // TODO - Luchetta
              // Obter a situacao do patio no momento da atualizacao
              SituacaoPatio situacaoPatio = planejamento.getControladorDePlano().obterSituacaoPatioInicial();

              List<TipoProduto> listaTipoProdutoTotalizado = new ArrayList<TipoProduto>();

              // Percorre a lista de pilhas dos patios totalizando a quantidade dos tipos de produtos
              for (Pilha pilha : situacaoPatio.getListaDePilhasNosPatios())
              {
                 // percorre a lista de balizas da pilha para obter o produto e sua quantidade
                 for (Baliza baliza : pilha.getListaDeBalizas())
                 {
                    // localiza o tipo de produto da baliza na lista para totaliza-lo ou cria-lo
                    TipoProduto tipoProdutoBaliza =
                            localizarTipoProdutoListaTotalizador(baliza.getProduto().getTipoProduto().getIdTipoProduto(), listaTipoProdutoTotalizado);
                    if (tipoProdutoBaliza == null)
                    {
                       tipoProdutoBaliza = new TipoProduto();
                       tipoProdutoBaliza.setCodigoSAPDado(baliza.getProduto().getTipoProduto().getCodigoSAPDado());
                       tipoProdutoBaliza.setCodigoFamiliaTipoProduto(baliza.getProduto().getTipoProduto().getCodigoFamiliaTipoProduto());
                       tipoProdutoBaliza.setCodigoTipoProduto(baliza.getProduto().getTipoProduto().getCodigoTipoProduto());
                       tipoProdutoBaliza.setCorIdentificacao(baliza.getProduto().getTipoProduto().getCorIdentificacao());
                       tipoProdutoBaliza.setDescricaoTipoProduto(baliza.getProduto().getTipoProduto().getDescricaoTipoProduto());
                       tipoProdutoBaliza.setIdTipoProduto(baliza.getProduto().getTipoProduto().getIdTipoProduto());
                       tipoProdutoBaliza.setQuantidadeEstoquePatio(baliza.getProduto().getQuantidade());
                       listaTipoProdutoTotalizado.add(tipoProdutoBaliza);
                    } else
                    {
                       tipoProdutoBaliza.setQuantidadeEstoquePatio(tipoProdutoBaliza.getQuantidadeEstoquePatio() + baliza.getProduto().getQuantidade());
                    }
                 }
              }

              // obtem os parametros da ultima atualizacao do sistema SAP
              IntegracaoParametros integracaoParametros =
                      modelo.buscarParametroSistema(new Long(PropertiesUtil.buscarPropriedade("codigo.sistema.externo.SAP").trim()));

              List<TipoProdutoIntegracaoSAP> listaTiposProdutoIncompativeis = new ArrayList<TipoProdutoIntegracaoSAP>();

              List<IntegracaoSAP> listaTotalizadoresSAP = modelo.buscarListaDadosTotalizadosSAP(integracaoParametros.getDataUltimaLeitura());
              for (IntegracaoSAP dadoSAP : listaTotalizadoresSAP)
              {
                 // percorre a lista de tipos de produto totalizados e realiza a comparacao da quantidade
                 for (TipoProduto tipoProduto : listaTipoProdutoTotalizado)
                 {
                    if (tipoProduto.getCodigoSAPDado().equals(dadoSAP.getCd_SAP_Dado()))
                    {
                       if (!tipoProduto.getQuantidadeEstoquePatio().equals(dadoSAP.getValorEstoque()))
                       {
                          // cria um tipo de produto que contem as informacoes incompativeis de estoque
                          TipoProdutoIntegracaoSAP tipoProdutoSAP = new TipoProdutoIntegracaoSAP();
                          tipoProdutoSAP.setTipoProduto(tipoProduto);
                          tipoProdutoSAP.setQuantidadeEstoqueMESPATIO(tipoProduto.getQuantidadeEstoquePatio());
                          tipoProdutoSAP.setQuantidaeEstoqueSAP(dadoSAP.getValorEstoque());

                          // adiciona o produto com quantidade incompativel na lista de produtos incompativeis
                          listaTiposProdutoIncompativeis.add(tipoProdutoSAP);
                       }
                    }
                 }
              }
        */
    }

    /**
     * Metodo auxiliar que localiza um tipo de produto pelo idTipoProduto dentro de uma lista passada
     * como parametros
     *
     * @param idTipoProduto
     * @param listaPesquisa
     * @return
     */
    private TipoProduto localizarTipoProdutoListaTotalizador(Long idTipoProduto, List<TipoProduto> listaPesquisa) {
        TipoProduto tipoEncontrado = null;
        for (TipoProduto tipoProduto : listaPesquisa) {
            if (tipoProduto.getIdTipoProduto().equals(idTipoProduto)) {
                tipoEncontrado = tipoProduto;
                break;
            }
        }
        return tipoEncontrado;
    }

    @Override
    public PlanoEmpilhamentoRecuperacao executarIntegracaoSistemaCRM(Date dataExecucaoIntegracao,
                    PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao, Date dataSituacao) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacao planoAtualizado = null;
        try {
            Calendar dataIntegracao = Calendar.getInstance();
            dataIntegracao.setTime(dataExecucaoIntegracao);

            Integer nroDias = new Integer(PropertiesUtil.buscarPropriedade("quantidade.dias.a.frente.busca.navios.fila"));
            dataIntegracao.add(Calendar.DAY_OF_MONTH, nroDias);
            //Darley removendo chamada remota
            //          IControladorIntegracaoSistemasExternos  controladorIntegracaoSistemasExternos = InterfaceInicial.lookUpIntegracaoSistemasExternos();
            IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos = new ControladorIntegracaoSistemasExternos();
            planoAtualizado = controladorIntegracaoSistemasExternos.atualizarDadosCRM(dataIntegracao.getTime(),
                            planoEmpilhamentoRecuperacao,dataSituacao);
            
            
            
            //          atulizando a situacao apos salvar a tabela de navios na fila de navios
            planoEmpilhamentoRecuperacao = null;
            getPlanejamento().getControladorDePlano().setPlanoEmpilhamentoRecuperacao(planoAtualizado);
            //          getInterfaceInicial().atualizarDSP();
            controladorIntegracaoSistemasExternos = null;

            /**
             * TODO CRM ATUALIZACAO DE ATRACADOS
             */
            //executarAtracarEDesatracarNavio(dataExecucaoIntegracao);    

            interfaceInicial.getControladorInterfaceInicial().getPlanejamento().setConsolidacaoRealizada(Boolean.FALSE);

        } catch (IntegracaoNaoRealizadaException iEx) {
            desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(iEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            ativarMensagem(interfaceMensagem);
        }       
        return planoAtualizado;
    }


    @Override
    public void executarIntegracaoSistemaNavioCRM(MetaNavio metaNavio, Date dataSituacao) throws ErroSistemicoException {
        try {
    
            //Darley removendo chamada remota
            //          IControladorIntegracaoSistemasExternos  controladorIntegracaoSistemasExternos = InterfaceInicial.lookUpIntegracaoSistemasExternos();
            IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos = new ControladorIntegracaoSistemasExternos();
            controladorIntegracaoSistemasExternos.atualizarDadosNavioCRM(metaNavio, dataSituacao);
            controladorIntegracaoSistemasExternos = null;
           
            interfaceInicial.getControladorInterfaceInicial().getPlanejamento().setConsolidacaoRealizada(Boolean.FALSE);

        } catch (IntegracaoNaoRealizadaException iEx) {
            desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(iEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            ativarMensagem(interfaceMensagem);
        }
    }

    
    @Override
    public PlanoEmpilhamentoRecuperacao executarIntegracaoSistemaMES(Date dataExecucaoIntegracao,
                    PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacao planoAtualizadoAposMES = null;
        try {
            IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos = new ControladorIntegracaoSistemasExternos();
            List<IntegracaoMES> lstIntegracaoMes = recuperarCodigoUsinas(planoEmpilhamentoRecuperacao);

            // verifica se é primeira vez
            if (InterfaceMES.getLastUpdateTime() == null) {
                Date dataFirstSituacao = new Date(planoEmpilhamentoRecuperacao.getListaSituacoesPatio().get(0).getDtInicio()
                                .getTime()
                                - (1000 * 60 * 60 * 120));
                InterfaceMES.defineCurrTime(dataFirstSituacao);
            }
            /**TODO JESSÉ MES USINA
             * *rrecupera dados para cada usina  separado , melhorar para buscar de uma só vez*/

            if (lstIntegracaoMes.size() > 0) {
                for (IntegracaoMES it : lstIntegracaoMes) {
                    List<IntegracaoMES> itens = controladorIntegracaoSistemasExternos.atualizarDadosUsina(it, InterfaceMES
                                    .getLastUpdateTime());
                    // monta map de Usinas x Quantidades
                    InterfaceInicial.makeMapIntegracaoMES(itens);
                }
            }
            planoAtualizadoAposMES = controladorIntegracaoSistemasExternos.atualizarDadosMES(dataExecucaoIntegracao,
                            planoEmpilhamentoRecuperacao, InterfaceInicial.getMapUsinaProducao());
            
            planoAtualizadoAposMES = controladorIntegracaoSistemasExternos.atualizarDadosPIMS(dataExecucaoIntegracao,
                            planoAtualizadoAposMES, InterfaceInicial.getMapUsinaProducao());
            
            
            interfaceInicial.getControladorInterfaceInicial().getPlanejamento().setConsolidacaoRealizada(Boolean.FALSE);
            controladorIntegracaoSistemasExternos = null;
            planoEmpilhamentoRecuperacao = null;

        } catch (IntegracaoNaoRealizadaException iEx) {
            desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(iEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            ativarMensagem(interfaceMensagem);
        }
        return planoAtualizadoAposMES;
    }

    @Override
    public PlanoEmpilhamentoRecuperacao executarIntegracaoSistemaPIMS(Date dataExecucaoIntegracao,
                    PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao) throws ErroSistemicoException {
        PlanoEmpilhamentoRecuperacao planoAtualizadoAposPIMS = null;
        //Darley Removendo chamada remota
        //      IControladorIntegracaoSistemasExternos  controladorIntegracaoSistemasExternos = InterfaceInicial.lookUpIntegracaoSistemasExternos();
        IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos = new ControladorIntegracaoSistemasExternos();
        try {
            planoAtualizadoAposPIMS = controladorIntegracaoSistemasExternos.atualizarDadosPIMS(dataExecucaoIntegracao,
                            planoEmpilhamentoRecuperacao, InterfaceInicial.getMapUsinaProducao());
            interfaceInicial.getControladorInterfaceInicial().getPlanejamento().setConsolidacaoRealizada(Boolean.FALSE);            
            controladorIntegracaoSistemasExternos = null;
            planoEmpilhamentoRecuperacao = null;
        } catch (IntegracaoNaoRealizadaException iEx) {
            desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(iEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            ativarMensagem(interfaceMensagem);
        }
        return planoAtualizadoAposPIMS;
    }

    /**
     * @param planoEmpilhamentoRecuperacao
     * @return
     */
    private List<IntegracaoMES> recuperarCodigoUsinas(PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao) {
        SituacaoPatio situacao = planoEmpilhamentoRecuperacao.getListaSituacoesPatio().get(
                        planoEmpilhamentoRecuperacao.getListaSituacoesPatio().size() - 1);
        List<Usina> usinas = situacao.getPlanta().getListaUsinas(situacao.getDtInicio());
        List<IntegracaoMES> lstIntegracaoMes = new ArrayList<IntegracaoMES>();
        for (Usina usina : usinas) {
            List<Campanha> campanhas = usina.getMetaUsina().getCampanhas(situacao.getDtInicio()); 
            for (Campanha campanha : campanhas) {
                IntegracaoMES it = new IntegracaoMES();
                it.setCdFaseProcesso(campanha.getCodigoFaseProcessoPelota());            
                it.setCdAreaRespEd(campanha.getAreaRespEDPelota());
                it.setCdItemControle(campanha.getCdTipoItemControlePelota());
                it.setCdTipoProcesso(campanha.getTipoProcessoPelota());
                lstIntegracaoMes.add(it);
                
                
                it = new IntegracaoMES();
                it.setCdFaseProcesso(campanha.getCodigoFaseProcessoPelletFeed());            
                it.setCdAreaRespEd(campanha.getAreaRespEDPelletFeed());
                it.setCdItemControle(campanha.getCdTipoItemControlePelletFeed());
                it.setCdTipoProcesso(campanha.getTipoProcessoPelletFeed());
                lstIntegracaoMes.add(it);
            }                
        }
        
        return lstIntegracaoMes;
    }

    /**
     * @return the interfaceNavegacaoEventos
     */
    public IControladorInterfaceNavegacaoEventos getInterfaceNavegacaoEventos() {
        return interfaceNavegacaoEventos;
    }

    /**
     * @param interfaceNavegacaoEventos the interfaceNavegacaoEventos to set
     */
    public void setInterfaceNavegacaoEventos(IControladorInterfaceNavegacaoEventos interfaceNavegacaoEventos) {
        this.interfaceNavegacaoEventos = interfaceNavegacaoEventos;
    }

    @Override
    public void setModoOperacao(ModoDeOperacaoEnum modoOperacao) {
        this.interfaceInicial.setModoDeOperacao(modoOperacao);
    }

    @Override
    public void ativaModoEdicao() {
        this.setModoOperacao(ModoDeOperacaoEnum.EDITAR);
    }

    @Override
    public void desativarModoEdicao() {
        this.setModoOperacao(ModoDeOperacaoEnum.OPERACAONULA);
        interfaceInicial.getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
    }

    @Override
    public List<Usina> buscarUsinasSelecionadas() {
        return dsp.getUsinasSelecionadas();
    }

    @Override
    public TipoProduto getTipoDeProdutoSelecionado() {
        Blendagem blendagemAtual = calculoQualidade.getBlendagem();
        if (blendagemAtual != null) {
            Produto produtoResultante = blendagemAtual.getProdutoResultante();
            if (produtoResultante != null) {
                return produtoResultante.getTipoProduto();
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    @Override
    public void desativarMensagemProcessamento() {
        interfaceInicial.desativarMensagem();
    }

    @Override
    public void exibirSituacaoPatioAnterior() {
        int situacaoPatioExibida = interfaceInicial.getIndiceSituacaoPatioSelecionada();
        if (situacaoPatioExibida == 0) {
            desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil
                            .buscarPropriedade("mensagem.navegacao.ir.para.situacao.anterior"));
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            ativarMensagem(interfaceMensagem);
        } else {
            interfaceInicial.setMovimentacaoViaBotoesDeNavegacao(Boolean.TRUE);
            situacaoPatioExibida = situacaoPatioExibida - 1;
            interfaceInicial.setIndiceSituacaoPatioSelecionada(situacaoPatioExibida);
            //Adiciona-se 1 da situacao pois o slider inicia-se em 1 e a lista inicia-se em 0
            interfaceInicial.getSliderNavegacao().setValue(situacaoPatioExibida + 1);
        }
    }

    @Override
    public void exibirSituacaoPatioPosterior() {
        Integer situacaoPatioExibida = interfaceInicial.getIndiceSituacaoPatioSelecionada();
        if (situacaoPatioExibida.equals(planejamento.obterNumeroDeSituacoes())) {
            desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil
                            .buscarPropriedade("mensagem.navegacao.ir.para.proxima.situacao"));
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            ativarMensagem(interfaceMensagem);
        } else {
            interfaceInicial.setMovimentacaoViaBotoesDeNavegacao(Boolean.TRUE);
            situacaoPatioExibida = situacaoPatioExibida + 1;
            interfaceInicial.setIndiceSituacaoPatioSelecionada(situacaoPatioExibida);
            //Adiciona-se 1 da situacao pois o slider inicia-se em 1 e a lista inicia-se em 0
            interfaceInicial.getSliderNavegacao().setValue(situacaoPatioExibida + 1);
        }
    }

    @Override
    public void exibirUltimaSituacaoPatio() {
        Integer situacaoPatioExibida = planejamento.obterNumeroDeSituacoes();
        interfaceInicial.setMovimentacaoViaBotoesDeNavegacao(Boolean.TRUE);
        interfaceInicial.setIndiceSituacaoPatioSelecionada(situacaoPatioExibida);
        //Adiciona-se 1 da situacao pois o slider inicia-se em 1 e a lista inicia-se em 0
        interfaceInicial.getSliderNavegacao().setValue(situacaoPatioExibida + 1);
    }
    
    /** Retorna true se a situacao exibida é a ultima situacao do planejamento */
    public boolean estaNaUltimaSituacao()
    {
    	Integer indiceUltimaSituacao = planejamento.obterNumeroDeSituacoes();
    	if (interfaceInicial.getSliderNavegacao().getValue() < indiceUltimaSituacao)
    		return false;
    	
    	return true;
    }

    @Override
    public void exibirPrimeiraSituacaoPatio() {
        Integer situacaoPatioExibida = 0;
        interfaceInicial.setMovimentacaoViaBotoesDeNavegacao(Boolean.TRUE);
        interfaceInicial.setIndiceSituacaoPatioSelecionada(situacaoPatioExibida);
        //Adiciona-se 1 da situacao pois o slider inicia-se em 1 e a lista inicia-se em 0
        interfaceInicial.getSliderNavegacao().setValue(situacaoPatioExibida + 1);
    }

    @Override
    public void atualizarEmpilhamento(Atividade atividadeEmpilhamento) throws AtividadeException,
                    ValidacaoObjetosOperacaoException {
        planejamento.atualizacaoEmpilhamento(atividadeEmpilhamento, interfaceInicial.getSituacaoPatioExibida());        
        /*try {
                this.consolidarPlano();
            } catch (ErroSistemicoException e) {
                // TODO Auto-generated catch block
                throw new AtividadeException(e);
            }  */         
    }
    
    @Override
    public void atualizarEmpilhamentoEmergencia(Atividade atividadeEmpilhamentoEmergencia) throws AtividadeException,
                    ValidacaoObjetosOperacaoException {
        planejamento.atualizacaoEmpilhamentoEmergencia(atividadeEmpilhamentoEmergencia, interfaceInicial.getSituacaoPatioExibida());
        try {
            this.consolidarPlano();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            throw new AtividadeException(e);
        }

    }

    @Override
    public void atualizarRecuperacao(Atividade atividadeRecuperacao) throws ValidacaoObjetosOperacaoException {
        planejamento.atualizacaoRecuperacao(atividadeRecuperacao, interfaceInicial.getSituacaoPatioExibida());
        try {
            this.consolidarPlano();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block            
                throw new ValidacaoObjetosOperacaoException(e);
        }
    }

    @Override
    public void verificarPossibilidadeCarregarPlanoUsuario() throws CarregarOficialNaoNecessariaException {
        planejamento.verificarPossibilidadeDeCarregarPlanoUsuario();
    }

    @Override
    public void verificarPossibilidadeDeConsolidacao() throws ConsolidacaoNaoNecessariaException {
        planejamento.verificarPossibilidadeDeConsolidacao();
    }

    @Override
    public void verificarPossibilidadeDeRemocaoDePlanos() throws RemocaoDePlanosNaoPermitidaException {
        planejamento.verificarPossibilidadeDeRemocaoDePlanos();
    }

    @Override
    public List<Baliza> getBalizasDoTipo(Patio patio, EnumTipoBaliza tipoBaliza, Date hora) {
        List<Baliza> listBalizas = new ArrayList<Baliza>();
        for (Baliza baliza : patio.getListaDeBalizas(hora)) {
            if (baliza.getEstado().equals(EstadoMaquinaEnum.OCIOSA) && baliza.getTipoBaliza().equals(tipoBaliza)) {
                listBalizas.add(baliza);
            }
        }
        return listBalizas;
    }

    @Override
    public List<Baliza> getBalizasComProdutoNoPatio(Patio patio, Date hora) {
        List<Baliza> listBalizas = new ArrayList<Baliza>();
        for (Baliza baliza : patio.getListaDeBalizas(hora)) {
            if (baliza.getEstado().equals(EstadoMaquinaEnum.OCIOSA) && baliza.getProduto() != null
                            && baliza.getProduto().getTipoProduto() != null
                            && baliza.getProduto().getTipoProduto().getTipoDeProduto() != null) {
                listBalizas.add(baliza);
            }
        }
        return listBalizas;
    }

    @Override
    public void movimentar(Atividade atividade) throws AtividadeException, ValidacaoObjetosOperacaoException {
        planejamento.planejarMovimentacao(atividade, interfaceInicial.getSituacaoPatioExibida());
    }
    
    
    @Override
    public void editarBalizas(Atividade atividadeEdicao) throws AtividadeException
    {
        planejamento.atualizacaoEdicaoBalizas(atividadeEdicao, interfaceInicial.getSituacaoPatioExibida());
        try 
        {
            this.consolidarPlano();
        } 
        catch (ErroSistemicoException e) 
        {
            // TODO Auto-generated catch block
            throw new AtividadeException(e);
        }

    }


    public void editarMaquinas(Atividade atividadeEdicao) throws AtividadeException
    {
        planejamento.atualizacaoEdicaoMaquinas(atividadeEdicao, interfaceInicial.getSituacaoPatioExibida());
        try 
        {
            this.consolidarPlano();
        } 
        catch (ErroSistemicoException e) 
        {
            // TODO Auto-generated catch block
            throw new AtividadeException(e);
        }

    }

    
    
    @Override
    public void tratamentoPSM(Atividade atividade) throws AtividadeException {
        planejamento.planejarTratamentoPSM(atividade, interfaceInicial.getSituacaoPatioExibida());
    }

    @Override
    public List<Baliza> getBalizasProdutoPatio(Patio patio, TipoDeProdutoEnum tipoProduto, Date hora) {
        List<Baliza> listBalizas = new ArrayList<Baliza>();
        for (Baliza baliza : patio.getListaDeBalizas(hora)) {
            if (baliza.getEstado().equals(EstadoMaquinaEnum.OCIOSA) && baliza.getProduto() != null
                            && baliza.getProduto().getTipoProduto() != null
                            && baliza.getProduto().getTipoProduto().getTipoDeProduto() != null
                            && baliza.getProduto().getTipoProduto().getTipoDeProduto().equals(tipoProduto)) {
                listBalizas.add(baliza);
            }
        }
        return listBalizas;
    }

    @Override
    public void verificaMovimentacaoNavio(AtividadeAtracarDesAtracarNavioVO movimentarVO) throws AtividadeException {

        SituacaoPatio situacaoAtual = this.planejamento.getControladorDePlano().obterSituacaoPatioFinal();
        boolean continuarAtracacao = true;
        //verifica se o pier esta sob interdicao
        /*if (existePierSobInterdicao(data)) {
           JLabel pergunta = new JLabel("Pier sob interdição! Deseja continuar?");//PropertiesUtil.getMessage("mensagem.option.pane.confirma.consolidacao.plano"));
        	 pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
        	 int confirm = JOptionPane.showOptionDialog(
        			 interfaceInicial,
        			 pergunta,
        			 PropertiesUtil.getMessage("mensagem.option.pane.tipo.atencao") ,
        			 JOptionPane.YES_OPTION,
        			 JOptionPane.INFORMATION_MESSAGE,
        			 null,
        			 null,
        			 null);

        	 if (confirm != JOptionPane.YES_OPTION) {
        		 continuarAtracacao = false;
        	 }
        }*/
        if (continuarAtracacao) {
            //cria atividade para atracar o navio
            Atividade atividade = this.interfaceFilaDeNavios.criaAtividadeAtracarNavio(movimentarVO, situacaoAtual
                            .getDtInicio());
            this.planejamento.planejaAtracacaoDeNavio(atividade);
            //propagar navio atracado para situacoes futuras
            //this.planejamento.getControladorDePlano().propagaAtividadeAtracarParaFuturo(data, navioAtualizado);			  
            //}
        }
    }

  


    @Override
    public void verificarNovoRitimoProducaoUsinas(Date dataHoraSituacao) throws ErroSistemicoException {
        //Darley removendo chamada remota
        IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos = InterfaceInicial
                        .lookUpIntegracaoSistemasExternos();
        //IControladorIntegracaoSistemasExternos  controladorIntegracaoSistemasExternos = new ControladorIntegracaoSistemasExternos();
        List<IntegracaoRPUSINAS> listaNovosRitimosProducao = controladorIntegracaoSistemasExternos
                        .buscarNovoRitimoProducaoUsinas(dataHoraSituacao);
        if (listaNovosRitimosProducao != null && !listaNovosRitimosProducao.isEmpty()) {
            for (IntegracaoRPUSINAS integracaoUsina : listaNovosRitimosProducao) {
                // busca a usina na lista de usinas da situacao pela chave do MES
                Usina usina = buscarUsinaPorFaseItem(integracaoUsina.getCdFaseProcesso(), integracaoUsina
                                .getCdItemControle(), integracaoUsina.getCdTipoProcesso(), integracaoUsina.getCdAreaRespEd());
                if (usina != null) {
                    // atualizando a taxa de operacao da usina
                    usina.setTaxaDeOperacao(integracaoUsina.getValorLeitura());
                }
            }
        }
        controladorIntegracaoSistemasExternos = null;
    }

    /**
     * Metodo auxiliar que busca na lista de usinas do DSP a usina correspondente a usina cadastrada
     * no MES atraves da fase item tipo area
     * @param codigoFaseProcesso
     * @param cdItemControle
     * @param cdTipoProcesso
     * @param cdAreaRespEd
     * @return
     */
    private Usina buscarUsinaPorFaseItem(Long codigoFaseProcesso, Long cdItemControle, String cdTipoProcesso,
                    String cdAreaRespEd) {

        Usina usina = null;

        for (InterfaceUsina usinaDaLista : dsp.getInterfaceDSP().getListaUsinas()) {
            if (usinaDaLista.getUsinaVisualizada().getCodigoFaseProcessoUsina().equals(codigoFaseProcesso)
                            && usinaDaLista.getUsinaVisualizada().getCdItemControleUsina().equals(cdItemControle)
                            && usinaDaLista.getUsinaVisualizada().getCdTipoProcessoUsina().equals(cdTipoProcesso)
                            && usinaDaLista.getUsinaVisualizada().getCdAreaRespEdUsina().equals(cdAreaRespEd)) {
                usina = usinaDaLista.getUsinaVisualizada();
                break;
            }
        }

        return usina;
    }

    /**
     * Metodo auxiliar que busca em uma lista de item de controle um item de controle 
     * atraves de um tipo de item de controle
     * @param listaItemDeControle
     * @param tipoItemControle
     * @return
     */
    private ItemDeControle buscarItemDeControlePorTipoProduto(List<ItemDeControle> listaItemDeControle,
                    TipoItemDeControle tipoItemControle) {
        ItemDeControle itemRetorno = null;
        for (ItemDeControle itemControle : listaItemDeControle) {
            if (itemControle.getTipoItemControle().equals(tipoItemControle)) {
                itemRetorno = itemControle;
                break;
            }
        }
        return itemRetorno;
    }

    @Override
    public void oficializarPlano() throws ErroSistemicoException {
        planejamento.oficializaPlano();
    }

    @Override
    public void propagarFilaDeNavios(FilaDeNavios filaNavioPropagacao) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void verificaBlendagemBaliza(List<InterfaceBaliza> listaBalizaSelecionada) throws BlendagemInvalidaException,
                    CampanhaIncompativelException, ProdutoIncompativelException, ExcessoDeMaterialParaEmbarqueException {
        Blendagem blendBaliza = null;
        for (InterfaceBaliza iBaliza : listaBalizaSelecionada) {
            blendBaliza = calculoQualidade.blendarBaliza(iBaliza.getBalizaVisualizada());
        }
        controladorInterfaceBlendagem.apresentarBlendagem(blendBaliza);
    }

    @Override
    public void verificaRemocaoBlendagemBaliza(List<InterfaceBaliza> listaBalizaDeselecionada)
                    throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException {
        Blendagem blendBaliza = null;
        for (InterfaceBaliza iBaliza : listaBalizaDeselecionada) {
            blendBaliza = calculoQualidade.removerBlendagemBaliza(iBaliza.getBalizaVisualizada());
        }
        controladorInterfaceBlendagem.apresentarBlendagem(blendBaliza);
    }

    @Override
    public void retornarPelletFeed(Atividade atividadeRetornoPelletFeed) {
        planejamento.retornarPelletFeed(atividadeRetornoPelletFeed, interfaceInicial.getSituacaoPatioExibida());
    }

    @Override
    public void verificarPossibilidadeCarregarOficial() throws CarregarOficialNaoNecessariaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void atualizarPilhaEmergencia(Atividade atividadePilhaEmergencia) throws AtividadeException,
                    ValidacaoObjetosOperacaoException {
        planejamento.atualizacaoPilhaEmergencia(atividadePilhaEmergencia, interfaceInicial.getSituacaoPatioExibida());
    }

    @Override
    public void transportarPilhaEmergencia(Atividade atividadeTransportePilhaEmergencia) throws AtividadeException,
                    ValidacaoObjetosOperacaoException {
        planejamento.transportarPilhaEmergencia(atividadeTransportePilhaEmergencia, interfaceInicial
                        .getSituacaoPatioExibida());
    }

    @Override
    public void movimentarPilhaEmergencia(Atividade atividadeMovimentarPilhaEmergencia) throws AtividadeException,
                    ValidacaoObjetosOperacaoException {
        planejamento.movimentarPilhaEmergencia(atividadeMovimentarPilhaEmergencia, interfaceInicial
                        .getSituacaoPatioExibida());
    }

    @Override
    public void movimentarPilhaPSMPelletFeed(Atividade atividadeMovimentar) throws AtividadeException,
                    ValidacaoObjetosOperacaoException {
        planejamento.movimentarPilhaPSMPelletFeed(atividadeMovimentar, interfaceInicial.getSituacaoPatioExibida());
    }

    @Override
    public void validarNomeDaPilha(String nomePilha) throws PilhaExistenteException {
        if (interfaceInicial.getSituacaoPatioExibida().getListaDePilhasNosPatios(interfaceInicial.getSituacaoPatioExibida().getDtInicio()) != null) {
            for (Pilha pilhaPatio : interfaceInicial.getSituacaoPatioExibida().getListaDePilhasNosPatios(interfaceInicial.getSituacaoPatioExibida().getDtInicio())) {
                if (pilhaPatio.getNomePilha().trim().toLowerCase().equals(nomePilha.trim().toLowerCase())) {
                    throw new PilhaExistenteException(PropertiesUtil.getMessage("aviso.pilha.ja.existente"));
                }
            }
        }
    }

    @Override
    public boolean verificaPermissaoAtualizacaoProducao() {
        boolean result = false;
        for (Permission permission : InterfaceInicial.getUsuarioLogado().getProfile().getPermissions()) {
            if ((permission.getName().equalsIgnoreCase(PropertiesUtil.buscarPropriedade("nivel.acesso.nao.pode.executar.atuializacoes.producao").trim()))) {
                result = true;
            }
        }

        return result;
    }

    //TODO Darley SAxxxx criando um metodo para buscar as balizas dos patios
    public List<Patio> atualizarListaBalizasPorPatios(List<Patio> listaPatios) throws ErroSistemicoException {
        //	   IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        List<Patio> result = controladorModelo.atualizarListaBalizasPorPatios(listaPatios);
        controladorModelo = null;
        return result;
    }

    public List<Patio> atualizarMaquinasDoPatio(List<Patio> patios) throws ErroSistemicoException {
        //	   IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        List<Patio> result = controladorModelo.atualizarMaquinasDoPatio(patios);
        controladorModelo = null;
        return result;
    }

    public List<Correia> atualizarListaMaquinasCorreias(List<Correia> correias) {
        //	   IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        List<Correia> result = controladorModelo.atualizarListaMaquinasCorreias(correias);
        controladorModelo = null;
        return result;
    }

    public List<Usina> atualizarListaEmergenciaUsinas(List<Usina> usinas) {
        //	   IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        List<Usina> result = controladorModelo.atualizarListaEmergenciaUsinas(usinas);
        controladorModelo = null;
        return result;
    }

    /**
     * Realiza a atualizacao de recuperacao da usina
     * @param carga
     * @param usinaEditada
     * @param tipoProduto
     * @param numeroPorao
     * @param dataFinalAtividade
     * @param dataInicioAtividade
     * @throws ValidacaoObjetosOperacaoException
     * @throws AtividadeException 
     */
    public void atualizaRecuperacaoUsina(Usina usinaEditada,Filtragem filtragemEditada, Carga carga, TipoProduto tipoProduto, String numeroPorao,
                    Date dataFinalAtividade, Date dataInicioAtividade,Atividade atividadeExecutada,Campanha campanha) throws ValidacaoObjetosOperacaoException, AtividadeException {
        Atividade atividadeAtualizacaoRecuperacao = null;
        List<MetaUsina> listaMetaUsinas = new ArrayList<MetaUsina>();
        List<MetaFiltragem> listaMetaFiltragem = new ArrayList<MetaFiltragem>();
        AtualizarRecuperacaoVO empilhamentoVO = new AtualizarRecuperacaoVO();
        empilhamentoVO.setDataInicio(dataInicioAtividade);
        empilhamentoVO.setDataFim(dataFinalAtividade);
        empilhamentoVO.setAtividadeAnterior(atividadeExecutada);        
        // Carga
        // seta a carga       
        MetaCarga metaCarga = carga.getMetaCarga();

        List<Campanha> campanhas = new ArrayList<Campanha>();
        campanhas.add(campanha);
        empilhamentoVO.setCampanhas(campanhas);
        
        if (usinaEditada != null) {           
            listaMetaUsinas.add(usinaEditada.getMetaUsina());
            empilhamentoVO.getListaUsinas().addAll(listaMetaUsinas);
        }  else  if (filtragemEditada != null) {            
            listaMetaFiltragem.add(filtragemEditada.getMetaFiltragem());
            empilhamentoVO.getListaFiltragens().addAll(listaMetaFiltragem);
            
            for (MetaFiltragem filtragem : listaMetaFiltragem) {
                for (InterfaceUsina interfaceUsina : dsp.getInterfaceDSP().getListaUsinas())
                {
                    if (interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem().equals(filtragem)) 
                    {
                        empilhamentoVO.setTipoProduto(campanha.getTipoPellet());                                                          
                        break;
                    }    
               }
            }   
        }
        empilhamentoVO.setMetaCarga(metaCarga);
        if (dataFinalAtividade != null) {
            empilhamentoVO.setCliente(carga.getCliente(dataFinalAtividade));
        } else {
            empilhamentoVO.setCliente(carga.getCliente(dataInicioAtividade));
        }
        empilhamentoVO.setTipoProduto(tipoProduto);
        empilhamentoVO.setNomePorao(numeroPorao);
        
        empilhamentoVO.setNomePilha(numeroPorao);
        
        IControladorExecutarAtividadeRecuperacao service = ControladorExecutarAtividadeRecuperacao.getInstance();
        List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
        try {

            atividadeAtualizacaoRecuperacao = service.recuperar(empilhamentoVO, lugaresAnteriores);
        } catch (AtividadeException e) {
            // TODO Auto-generated catch block
           throw e;
        }
        atualizarRecuperacao(atividadeAtualizacaoRecuperacao);

    }

    /**
     * Metodo que verifica se existe alguma atividade de atualizacao de empilhamento em 
     * aberto para a usina informada.
     * @param usina
     * @return lista de maquinas que estao realizando o(s) empilhamento(s)
     */
    public List<MaquinaDoPatio> verificarEmpilhamentoEmAndamentoNaUsina(Usina usina,Date data) {


        List<MaquinaDoPatio> maquinasEmExecucao = new ArrayList<MaquinaDoPatio>();
        MetaUsina metaUsina= usina.getMetaUsina();
        Usina usinaAtual = metaUsina.retornaStatusHorario(data);
         if (usinaAtual.getAtividade() != null && usinaAtual.getEstado().equals(EstadoMaquinaEnum.OPERACAO)
                         && usinaAtual.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO)) {
                    maquinasEmExecucao.addAll(usinaAtual.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                            .getListaMaquinaDoPatio());
        }
        return maquinasEmExecucao;
    }

    
    /**
     * Metodo que verifica se existe alguma atividade de atualizacao de empilhamento em 
     * aberto para a usina informada.
     * @param usina
     * @return lista de maquinas que estao realizando o(s) empilhamento(s)
     */
    public List<MaquinaDoPatio> verificarEmpilhamentoEmAndamentoNaFiltragem(Filtragem usina,Date data) {


        List<MaquinaDoPatio> maquinasEmExecucao = new ArrayList<MaquinaDoPatio>();
        MetaFiltragem metaUsina= usina.getMetaFiltragem();
        Filtragem usinaAtual = metaUsina.retornaStatusHorario(data);
         if (usinaAtual.getAtividade() != null && usinaAtual.getEstado().equals(EstadoMaquinaEnum.OPERACAO)
                         && usinaAtual.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO)) {
                    maquinasEmExecucao.addAll(usinaAtual.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                            .getListaMaquinaDoPatio());
        }
        return maquinasEmExecucao;
    }


    public void finalizarAtualizacaoEmpilhamento(Atividade atividade, Date dataFim)
                    throws ValidacaoObjetosOperacaoException, AtividadeException {
        AtualizarEmpilhamentoVO empilhamentoVO = new AtualizarEmpilhamentoVO();
        empilhamentoVO.setDataInicio(atividade.getDtInicio());
        Baliza balizaEmpilhamento = null;
        for (Baliza baliza : atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas()) {
            balizaEmpilhamento = baliza;
            MetaBaliza metaBaliza = baliza.getMetaBaliza();
            empilhamentoVO.getListaBalizas().add(metaBaliza);
        }

        for (Usina usina : atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas()) {
            MetaUsina metaUsina = usina.getMetaUsina();
            empilhamentoVO.getListaUsinas().add(metaUsina);
        }

        for (MaquinaDoPatio maquina : atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaMaquinaDoPatio()) {
            MetaMaquinaDoPatio metaMaquina = maquina.getMetaMaquina();
            empilhamentoVO.getListaMaquinas().add(metaMaquina);
        }
        // finalizacao de empilhamento, pega o tipo de produto armazenado na pilha
        empilhamentoVO.setTipoProduto(balizaEmpilhamento.getProduto().getTipoProduto());
        // seta a data final da atividade
        empilhamentoVO.setDataFim(dataFim);

        //TODO ver de onde pega o cliente
        empilhamentoVO.setCliente(null);
        IControladorExecutarAtividadeEmpilhamento service = ControladorExecutarAtividadeEmpilhamento.getInstance();
        List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
        Atividade atividadeAtualizacaoEmpilhamento = service.empilhar(empilhamentoVO, lugaresAnteriores);

        atualizarEmpilhamento(atividadeAtualizacaoEmpilhamento);
    }

    @Override
    public void validarAtividadeDeRecuperacaoEmExecucao(Navio navio) throws AtividadeException {
        // TODO Auto-generated method stub
        SituacaoPatio situacaoPatio = interfaceInicial.getSituacaoPatioExibida();
        boolean atividadeRecuperacaoIniciada = false;
        try {
            for (Correia correia : situacaoPatio.getPlanta().getListaCorreias(situacaoPatio.getDtInicio())) {//verifica se existe alguma maquina em recuperacao para o navio
                for (MaquinaDoPatio maquina : correia.getListaDeMaquinas(situacaoPatio.getDtInicio())) {
                    if (maquina.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA)
                                    || maquina.getTipoDaMaquina().equals(TipoMaquinaEnum.RECUPERADORA)) {
                        if (maquina.getAtividade() != null
                                        && maquina.getAtividade().getTipoAtividade().equals(
                                                        TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)
                                        && maquina.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                            MetaNavio metaNavio = maquina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getNavio(situacaoPatio.getDtInicio()).getMetaNavio();                            
                            if (metaNavio.equals(navio.getMetaNavio())) {                                                        
                                atividadeRecuperacaoIniciada = true;
                            }    
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         if (!atividadeRecuperacaoIniciada)
         {//se não existir recuperacao de maquina, verificar se existe recuperacao direto das usinas para o navio
           for(Usina usina : situacaoPatio.getPlanta().getListaUsinas(situacaoPatio.getDtInicio())){
               if(usina.getAtividade() != null && usina.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO) && usina.getEstado().equals(EstadoMaquinaEnum.OPERACAO)){
                   MetaNavio metaNavio = usina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getNavio(situacaoPatio.getDtInicio()).getMetaNavio();                   
                   if (metaNavio.equals(navio.getMetaNavio())) {                                                        
                       atividadeRecuperacaoIniciada = true;
                   }
                   break;
               }
           }
         }
         if (atividadeRecuperacaoIniciada)
         {
            throw new AtividadeException(PropertiesUtil.getMessage("aviso.nao.foi.possivel.desatracar.navio.recuperacao.nao.finalizada"));
         }
        
    }

}
