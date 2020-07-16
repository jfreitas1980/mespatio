package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceBerco;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceCarga;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfacePier;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.NavioNaFilaPnl;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceBaliza;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceCorreia;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceMaquinaDoPatio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfacePatio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceUsina;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
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
import com.hdntec.gestao.login.entity.Permission;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Controlador das operaÃ§Ãµes do subsistema de interface grÃ¡fica de comandos do usuÃ¡rio. Este subsistema possibilita ao usuÃ¡rio emitir uma sÃ©rie de comandos ao sistema.
 * <p>
 * Estes comandos sÃ£o representados pelas operaÃ§Ãµes disponÃ­veis.
 *
 * @author andre
 *
 */
public class ControladorInterfaceComandos implements IControladorInterfaceComandos {

    /** interface grÃ¡fica de comandos do usuÃ¡rio */
    private InterfaceComandos interfaceComandos;

    /** interface grafica de comandos de botoes de navegacao do slider */
    private InterfaceComandosSlider interfaceComandosSlider;

    
    /** acesso Ã s operaÃ§Ãµes do subsistema da interface grÃ¡fica principal */
    private IControladorInterfaceInicial interfaceInicial;

    private InterfaceMensagem interfaceMensagem;

    public ControladorInterfaceComandos() {
    }

    @Override
    public void empilhar(Carga carga) throws OperacaoCanceladaPeloUsuarioException, ValidacaoObjetosOperacaoException, BlendagemInvalidaException, TempoInsuficienteException, CampanhaIncompativelException, BalizasSelecionadasException, SelecaoObjetoModoEmpilhamentoException
    {
       try
       {
          // verificando se exite alguma nova taxa de operacao para as usinas que foram lidas do MES
          interfaceInicial.verificarNovoRitimoProducaoUsinas(interfaceInicial.getInterfaceInicial().getSituacaoPatioExibida().getDtInicio());
       }
       catch (ErroSistemicoException errEx)
       {
          throw new OperacaoCanceladaPeloUsuarioException();
       }

       //Carregando as baliza selecionadas...
       List<InterfaceBaliza> listaDeInterfacesDeBaliza = interfaceInicial.getInterfaceInicial().getInterfaceDSP().getControladorDSP().getListaDeBalizas();
       List<Baliza> listaDeBalizasSelecionadas = new ArrayList<Baliza>();
       for(InterfaceBaliza interfaceBalizaAnalisada : listaDeInterfacesDeBaliza)
       {
          if(interfaceBalizaAnalisada.isSelecionada())
             listaDeBalizasSelecionadas.add(interfaceBalizaAnalisada.getBalizaVisualizada());
       }


       interfaceInicial.getInterfaceInicial().atualizarDSP();
    }

    @Override
    public void verificaDadosEmpilhamento(Atividade atividadeEmpilhamento) throws ValidacaoObjetosOperacaoException, BalizasSelecionadasException {
        //TODO - AndrÃ© - Verificar se os fluxos escolhidos para cada mÃ¡quina excedem o fluxo nominal. Verificar tambÃ©m se as mÃ¡quinas selecionadas jÃ¡ estÃ£o em operaÃ§Ã£o
        LugarEmpilhamentoRecuperacao lugarEmpilhamentoRecuperacao = atividadeEmpilhamento.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);

        for (Iterator<Baliza> it2 = lugarEmpilhamentoRecuperacao.getListaDeBalizas().iterator(); it2.hasNext();) {
           Baliza baliza = it2.next();
           if (!baliza.getTipoBaliza().equals(EnumTipoBaliza.NORMAL)) {
              throw new BalizasSelecionadasException(PropertiesUtil.getMessage("aviso.nao.eh.permitido.empilhar.na.baliza") + baliza.getNumero() + PropertiesUtil.getMessage("aviso.mensagem.do.tipo") + baliza.getTipoBaliza());
           }  
        }
    }

    @Override
    public void recuperar(Carga carga) throws OperacaoCanceladaPeloUsuarioException, ValidacaoObjetosOperacaoException, PilhaParaRecuperacaoSemMaquinaCapazException, SelecaoObjetoModoRecuperacaoException
    {
       try
       {
          // verificando se exite alguma nova taxa de operacao para as usinas que foram lidas do MES
          interfaceInicial.verificarNovoRitimoProducaoUsinas(interfaceInicial.getInterfaceInicial().getSituacaoPatioExibida().getDtInicio());
       }
       catch (ErroSistemicoException errEx)
       {
          throw new OperacaoCanceladaPeloUsuarioException();
       }

       interfaceInicial.getInterfaceInicial().atualizarDSP();
    }

    @Override
    public void verficaDadosRecuperacao(Atividade atividadeRecuperacao) throws ValidacaoObjetosOperacaoException
    {
       List<String> paramMensagens = new ArrayList<String>();
       if (atividadeRecuperacao.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0) == null)
       {
          paramMensagens.add(PropertiesUtil.getMessage("aviso.pilhas.a.serem.recuperadas"));
          paramMensagens.add(PropertiesUtil.getMessage("aviso.recuperacao"));
          throw new ValidacaoObjetosOperacaoException("exception.validacao.selecao.objetos.operacao", paramMensagens);
       }
       // verifica capacidade da empilhadeiraRecuperadora com o da correia
       LugarEmpilhamentoRecuperacao listaDePilhasASeremRecuperadas = atividadeRecuperacao.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
       if (listaDePilhasASeremRecuperadas != null)
       {
//          if (listaDePilhasASeremRecuperadas.getTaxaDeOperacaoNaPilha() > listaDePilhasASeremRecuperadas.getMaquinaDoPatio().getCorreia().getTaxaDeOperacao())
  //        {
           //  paramMensagens.add(listaDePilhasASeremRecuperadas.getMaquinaDoPatio().getNomeMaquina());
             //paramMensagens.add(listaDePilhasASeremRecuperadas.getMaquinaDoPatio().getCorreia().getNomeCorreia());
    //         throw new ValidacaoObjetosOperacaoException("exception.validacao.taxaoperacao.objetos.operacao", paramMensagens);
      //    }
       }
    }

    @Override
    public void chavearAtualizacaoAutomatica(Boolean atualizacaoAtivada) {
        interfaceInicial.chavearAtualizacaoAutomatica(atualizacaoAtivada);
    }

    @Override
    public void apagarSituacaoPatio(Integer indiceDaSituacaoDePatioExibida) throws ErroSistemicoException {
        interfaceInicial.apagarSituacaoPatio(indiceDaSituacaoDePatioExibida);
    }

    @Override
    public void pegarPlanoOficial() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void consolidarPlano() throws ErroSistemicoException {
        interfaceInicial.consolidarPlano();
    }

    @Override
    public void oficializarPlano() throws ErroSistemicoException {
        interfaceInicial.oficializarPlano();
    }

    @Override
    public void ativarMensagem(InterfaceMensagem interfaceMensagem) {
        interfaceInicial.ativarMensagem(interfaceMensagem);
    }

    @Override
    public void cancelarModoOperacao() {
        interfaceComandos.cancelarModoOperacao();
    }

    public InterfaceComandos getInterfaceComandos() {
        return interfaceComandos;
    }

    public void setInterfaceComandos(InterfaceComandos interfaceComandos) {
        this.interfaceComandos = interfaceComandos;
    }

    
    public IControladorInterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    public void setInterfaceInicial(IControladorInterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;
    }


    /**
     * Busca o plano de empilhamento e recuperacao oficial e seta como sendo do usuario
     * @param dataInicial
     * @param dataFinal
     * @throws ErroSistemicoException
     * @throws PlanosOficiaisNaoLocalizadosException
     */
    @Override
    public void buscarPlanoEmpilhamentoRecuperacaoDoUsuario(Long idUser) throws PlanosOficiaisNaoLocalizadosException, ErroSistemicoException{
       interfaceInicial.carregarPlanoEmpilhamentoRecuperacaoOficialToUsuario(idUser);
    }

    /**
     * Busca novas situaÃ§Ãµes realizadas do plano de empilhamento e recuperacao oficial
     *
     */
    public void buscarNovasSituacoesRealizadasDoPlanoEmpilhamentoRecuperacaoOficial() {
        throw new UnsupportedOperationException();
    }

    /**
     * Muda a situaÃ§Ã£o de pÃ¡tio para a situaÃ§Ã£o do presente e habilita o modo de ediÃ§Ã£o
     */
    @Override
    public void editar() {
        this.getInterfaceInicial().getInterfaceInicial().getInterfaceNavegacaoEventos().irParaOPresente();
    }

    /**
     * Solicita pesquisar um determinado item qualquer nas diversas situacoes de Patio apresentadas
     */
    public void pesquisaItem() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void desabilitaFuncoes() {
        interfaceComandos.desabilitaFuncoes();
    }

    @Override
    public void habilitaFuncoes() {
        interfaceComandos.habilitaFuncoes();
    }

    @Override
    public void finalizarEdicoes() {
        interfaceComandos.finalizarEdicoes();
    }

    @Override
    public void desabilitarMenusDeEdicao() {
        // desabilitando menus das usinas
        for (InterfaceUsina interfaceUsina : interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaUsinas()) {
            interfaceUsina.desabilitarMenus();
        }

        // desabilitando menus das correias e das maquinas existentes nela
        for (InterfaceCorreia interfaceCorreia : interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaCorreias()) {
            interfaceCorreia.desabilitarMenus();
            for (InterfaceMaquinaDoPatio interfaceMaquinaDoPatio : interfaceCorreia.getListaDeInterfaceMaquinas()) {
                interfaceMaquinaDoPatio.desabilitarMenus();
            }
        }

        // desabilitando os menus das balizas do patio
        for (InterfacePatio interfacePatio : interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaDePatios()) {
            for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas()) {
                interfaceBaliza.desabilitarMenus();
            }
            for (InterfaceMaquinaDoPatio interfaceMaquinaDoPatio : interfacePatio.getListaDeMaquinas()) {
               interfaceMaquinaDoPatio.desabilitarMenus();
           }
        }

        // desabilitando os menus dos navios atracados
        if (interfaceInicial.getInterfaceInicial().getListaInterfacePiers() != null) {
            for (InterfacePier interfacePier : interfaceInicial.getInterfaceInicial().getListaInterfacePiers()) {
                for (InterfaceBerco interfaceBerco : interfacePier.getListaDeBercos()) {
                    if (interfaceBerco.getNavioAtendido() != null) {
                        interfaceBerco.getNavioAtendido().desabilitarMenus();
                        // desabilitando os menus de edicao da carga do navio
                        for (InterfaceCarga interfaceCarga : interfaceBerco.getNavioAtendido().getListaDecarga()) {
                            interfaceCarga.desabilitarMenus();
                        }
                    }
                }
            }
        }

        // desabilitando os menus dos navios da fila
        for (NavioNaFilaPnl interfaceNavioPNL : interfaceInicial.getInterfaceInicial().getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getListaInterfaceNavio()) {
           interfaceNavioPNL.desabilitarMenus();
           interfaceNavioPNL.getInterfaceNavio().desabilitarMenus();
            // desabilitando os menus de edicao da carga do navio
            for (InterfaceCarga interfaceCarga : interfaceNavioPNL.getInterfaceNavio().getListaDecarga()) {
                interfaceCarga.desabilitarMenus();
            }
        }
    }

    @Override
    public void habilitarMenusDeEdicao() {
        // habilitando menus das usinas
        if (interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaUsinas() != null) {
            for (InterfaceUsina interfaceUsina : interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaUsinas()) {
                interfaceUsina.habilitarMenus();
            }
        }

        // habilitando menus das correias e das maquinas existentes nela
        if (interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaCorreias() != null) {
            for (InterfaceCorreia interfaceCorreia : interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaCorreias()) {
                interfaceCorreia.habilitarMenus();
                for (InterfaceMaquinaDoPatio interfaceMaquinaDoPatio : interfaceCorreia.getListaDeInterfaceMaquinas()) {
                    interfaceMaquinaDoPatio.habilitarMenus();
                }
            }
        }

        // habilitando os menus das balizas do patio
        if (interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaDePatios() != null) {
            for (InterfacePatio interfacePatio : interfaceInicial.getInterfaceInicial().getInterfaceDSP().getListaDePatios()) {
                for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas()) {
                    interfaceBaliza.habilitarMenus();
                }
                if (interfacePatio.getListaDeMaquinas() != null) {
	                for (InterfaceMaquinaDoPatio interfaceMaquinaDoPatio : interfacePatio.getListaDeMaquinas()) {
	                    interfaceMaquinaDoPatio.habilitarMenus();
	                }
                }
            }
        }

        // habilitando os menus dos navios atracados
        if (interfaceInicial.getInterfaceInicial().getListaInterfacePiers() != null) {
            for (InterfacePier interfacePier : interfaceInicial.getInterfaceInicial().getListaInterfacePiers()) {
                if (interfacePier.getListaDeBercos() != null) {
                    for (InterfaceBerco interfaceBerco : interfacePier.getListaDeBercos()) {
                        if (interfaceBerco.getNavioAtendido() != null) {
                            interfaceBerco.getNavioAtendido().habilitarMenus();
                            // habilitando os menus de edicao da carga do navio
                            if (interfaceBerco.getNavioAtendido().getListaDecarga() != null) {
                                for (InterfaceCarga interfaceCarga : interfaceBerco.getNavioAtendido().getListaDecarga()) {
                                    interfaceCarga.habilitarMenus();
                                }
                            }
                        }
                    }
                }
            }
        }

        //habilitar o menu da fila de navios
        interfaceInicial.getInterfaceInicial().getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().habilitarMenus();

        // habilitando os menus dos navios da fila
        for (NavioNaFilaPnl interfaceNavio : interfaceInicial.getInterfaceInicial().getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getListaInterfaceNavio()) {
            interfaceNavio.habilitarMenus();
            interfaceNavio.getInterfaceNavio().habilitarMenus();
            // habilitando os menus de edicao da carga do navio
            for (InterfaceCarga interfaceCarga : interfaceNavio.getInterfaceNavio().getListaDecarga()) {
                interfaceCarga.habilitarMenus();
            }
        }
    }

    @Override
    public void verificarPossibilidadeDeConsolidacao() throws ConsolidacaoNaoNecessariaException {
        interfaceInicial.verificarPossibilidadeDeConsolidacao();
    }

    @Override
    public void verificarPossibilidadeCarregarPlanoUsuario() throws CarregarOficialNaoNecessariaException {
        interfaceInicial.verificarPossibilidadeCarregarPlanoUsuario();
    }
    
    @Override
    public void verificarPossibilidadeDeRemocaoDePlanos() throws RemocaoDePlanosNaoPermitidaException {
        interfaceInicial.verificarPossibilidadeDeRemocaoDePlanos();
    }

    @Override
    public void habilitarTodasFuncoes() {
        interfaceComandos.habilitarTodasFuncoes();
    }

    @Override
    public void desabilitarTodasFuncoes() {
        interfaceComandos.desabilitarTodasFuncoes();
    }

 @Override
 public void verificarPermissaoUsuarioApagarPlano() throws PermissaoDeUsuarioException    {
     for (Permission permission : InterfaceInicial.getUsuarioLogado().getProfile().getPermissions()) {
         if ((permission.getName().equalsIgnoreCase(PropertiesUtil.buscarPropriedade("nivel.acesso.nao.pode.apagar.plano")))) {
             throw new PermissaoDeUsuarioException(PropertiesUtil.buscarPropriedade("exception.permissao.apagar.plano.negada"));
         }
     }
 }
    public InterfaceComandosSlider getInterfaceComandosSlider() {
        return interfaceComandosSlider;
    }

    public void setInterfaceComandosSlider(InterfaceComandosSlider interfaceComandosSlider) {
        this.interfaceComandosSlider = interfaceComandosSlider;
    }

    @Override
    public void verificarPossibilidadeCarregarOficial() throws CarregarOficialNaoNecessariaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
     * Se o usuario logado tiver associado a ele a permissao "nao pode executar atualizacoes de producao" os seguintes botoes devem ser desabilitados:
     * consolidar, apagar, IntegrarMES, IntegrarCRM
      * @return true: se o usuario tiver esta permissao associada a ele; <br>
      * false caso contrario
     */
    public boolean verificaPermissaoAtualizacaoProducao()
    {
        return getInterfaceInicial().verificaPermissaoAtualizacaoProducao();
    }

    /**
     * se existir necessidade de integrar com o MES retorna true
     * @return
     */
    public boolean verificaNecessidadeDeIntegrarComMES() {
        boolean result;
        if(interfaceComandos.isIntegracaoMesAtivada()){
            result = true;
        }else{
            result = false;
        }
        return result;
    }

}
