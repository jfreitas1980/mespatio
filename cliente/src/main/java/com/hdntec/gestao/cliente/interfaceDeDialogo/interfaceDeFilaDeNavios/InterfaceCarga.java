package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jxl.read.biff.BiffException;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceRastreabilidade;
import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceSelecionavelParaAtividade;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CargaSelecionadaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.exceptions.RastreabilidadeException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoEmpilhamentoException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoNuloException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gráfica que representa a {@link Carga}.
 *
 * @author andre
 *
 */
public class InterfaceCarga extends RepresentacaoGrafica implements InterfaceInicializacao, InterfaceSelecionavelParaAtividade {

    /** serial gerado */
    private static final long serialVersionUID = 974372259025689992L;
    /** o status de atendimento da carga */
    private Integer statusAtendimento;
    /** a {@link Carga} visulizada */
    private Carga cargaVisualizada;
    /** a interface gráfica do navio ao qual esta carga pertence */
    private InterfaceNavio navio;
    /** acesso às operações do subsistema de interface gráfica de fila de navios */
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;
    /** flag que indica se a baliza estao ou nao selecionada */
    private Boolean cargaSelecionada;
    /** a interface das mensagens exibidas pela interface carga */
    private InterfaceMensagem interfaceMensagem;
    /** identifica se o desenha a carga do navio sera na fila ou no pier */
    private Boolean desenharCargaNavioFila;
    /**caminho de onde se encontra a planilha de atualizacao da Qualidade da Amostra do Produto da Carga */
    private String pathFileName;
    /** mapa que guarda o tamanho (dimensao) das images das cargas < integer(numero de cargas), dimension(dimensao da imagem)>*/
    private HashMap<Integer, Dimension> hashDeImagens;
    /** hash com o nome das diferentes imagens para o numero de cargas do navio */
    private HashMap<Integer, String> hashNomeImagemSelecao;

    //menus
    private JMenuItem mnuExibirRastreabilidade;
    private JMenuItem mnuItemAtualizar;
    //private JMenuItem mnuCaminhoArquivo;


    public InterfaceCarga() {
        defineEventosParaCarga();
        criarPopMenuCarga();        
        this.cargaSelecionada = Boolean.FALSE;
        this.desenharCargaNavioFila = Boolean.FALSE;
        preencheHashMapImagem();
        defineImagemParaSelecaoDeCarga();
    }

    private void defineEventosParaCarga() {
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    try {
                        selecionaDeselecionaCarga(evt);
                    } catch (SelecaoObjetoModoNuloException sm) {
                    	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                        interfaceMensagem.setDescricaoMensagem(sm.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (SelecaoObjetoModoEmpilhamentoException semr) {
                    	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                        interfaceMensagem.setDescricaoMensagem(semr.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (CargaSelecionadaException ex) {
                    	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    }
                }
            }
        });
    }

    /**Retorna a Interface carga que foi clicada */
    private InterfaceCarga obterCargaClicada(ActionEvent evt) {
        JMenuItem mnuItem = (JMenuItem) evt.getSource();
        JPopupMenu popupMenu = (JPopupMenu) mnuItem.getParent();
        return (InterfaceCarga) popupMenu.getInvoker();
    }

    private void selecionaDeselecionaCarga(MouseEvent evt) throws SelecaoObjetoModoNuloException, SelecaoObjetoModoEmpilhamentoException, CargaSelecionadaException {
        if (cargaVisualizada.getOrientacaoDeEmbarque() == null) {
            throw new CargaSelecionadaException(PropertiesUtil.buscarPropriedade("exception.carga.OrientacaoEmbarque.nulo"));
        }
        if (cargaSelecionada) {
            deselecionar();
        } else {
            selecionar();
        }
        this.repaint();
        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().repaint();
    }

    private void criarPopMenuCarga() {
        JPopupMenu popMnuParaCarga = new JPopupMenu();

//        JMenuItem
        mnuExibirRastreabilidade = new JMenuItem();
        mnuExibirRastreabilidade.setText(PropertiesUtil.buscarPropriedade("menu.carga.rastreabilidade"));
        mnuExibirRastreabilidade.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                exibirRastreabilidadeCarga(evt);
            }
        });
        popMnuParaCarga.add(mnuExibirRastreabilidade);

        //menu atualização
//        JMenuItem
        mnuItemAtualizar = new JMenuItem();
        mnuItemAtualizar.setText(PropertiesUtil.buscarPropriedade("menu.carga.atualizar"));
        mnuItemAtualizar.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                atualizacaoAmostraCarga(evt);
            }
        });
        popMnuParaCarga.add(mnuItemAtualizar);

        //menu para especificar o caminho de importacao de planilha para esta carga
//        JMenuItem
/*        mnuCaminhoArquivo = new JMenuItem();
        mnuCaminhoArquivo.setText(PropertiesUtil.buscarPropriedade("menu.carga.caminhoPlanilha"));
        mnuCaminhoArquivo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                editarCaminhoDaPlanilhaAmostra(event);
            }
        });
        popMnuParaCarga.add(mnuCaminhoArquivo);
*/
        this.setComponentPopupMenu(popMnuParaCarga);
        //atualiza visibilidade dos menus
        visibilidadeMenusCarga(popMnuParaCarga);
    }

    private void visibilidadeMenusCarga(JPopupMenu popMenu) {
        if (this.getCargaVisualizada() != null && this.getCargaVisualizada().getCaminhoCompletoPlanilha() != null) {
            if (this.getComponentPopupMenu() != null) {
                for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                    JMenuItem menu = (JMenuItem) this.getComponentPopupMenu().getComponent(i);
                    if (menu.getText().equalsIgnoreCase(PropertiesUtil.buscarPropriedade("menu.carga.atualizar"))) {
                        menu.setEnabled(Boolean.TRUE);
                        break;
                    }
                }
            }
        } else {
            if (this.getComponentPopupMenu() != null) {
                for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                    JMenuItem menu = (JMenuItem) this.getComponentPopupMenu().getComponent(i);
                    if (menu.getText().equalsIgnoreCase("Atualizar")) {
                        //menu.setEnabled(Boolean.FALSE);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Edita o caminho da planilha de importacao da Amostra de embarque e cria Amostra para a Carga e atualiza os itensDeControle do produto da Carga
     * @param evt
     */
    /*private void editarCaminhoDaPlanilhaAmostra(ActionEvent evt) {

        final InterfaceCarga interfaceCarga = obterCargaClicada(evt);
        final InterfaceCarga interfaceCargaThread = this;
        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceComandos().desabilitarTodasFuncoes();

        final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
        interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
        interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
        interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.processamento.importar.planilha"));

        new Thread("Thread edita caminho planilha amostra") {

            @Override
            public void run() {
                if (interfaceCarga.getCargaVisualizada().equals(cargaVisualizada)) {
                    try {//verifica se o navio esta atracado ou na filaDeNavio
                        if (interfaceCarga.getCargaVisualizada().getNavio().getBercoDeAtracacao() == null) { //!= null) {
                            CriaAmostraParaCarga criaAmostraParaCarga = new CriaAmostraParaCarga(interfaceCargaThread);
                            //abre janela para escolha do caminho da planilha de importacao da amostra de embarque
                            criaAmostraParaCarga.abrePlanilha();

                            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagemProcessamento);
                            //grava o caminho na interfaceCarga
                            cargaVisualizada.getMetaCarga().setCaminhoCompletoPlanilha(criaAmostraParaCarga.getCflexStockyardLeitorPlanilha().getFileCaminhoCompleto());
                            //cria atividade de resultado de amostragem
                            criaAtividadeResultadoAmostragem(criaAmostraParaCarga);
                            //atualiza visibilidade dos itens de menu do popMenu
                            visibilidadeMenusCarga(interfaceCargaThread.getComponentPopupMenu());
                            controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        } else {
                            throw new CargaSelecionadaException(PropertiesUtil.buscarPropriedade("exception.carga.para.amostra.invalida"));
                        }

                    } catch (CargaSelecionadaException ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (AtividadeException ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (BiffException ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.importacao.excel"));
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (IOException ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (OperacaoCanceladaPeloUsuarioException ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (Exception ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                        interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.planilha.incompativel"));
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } finally {
                    	controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        // habilita todas as funcoes
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceComandos().habilitarTodasFuncoes();
                    }
                }
            }
        }.start();
    }*/

    /**
     * cria a Atividade ResultadoAmostragem
     * @param criaAmostraParaCarga
     */
    /*private void criaAtividadeResultadoAmostragem(CriaAmostraParaCarga criaAmostraParaCarga) throws AtividadeException {//cria atividade Resultado Amostragem
        Atividade atividade = criaAmostraParaCarga.criaAtividadeResultadoAmostragem();
        //adiciona atividade pontual no plano
        this.getControladorInterfaceFilaDeNavios().getInterfaceInicial().recolherResultadoAmostragem(atividade);

        //atualiza interface
        this.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().atualizarDSP();
    }*/

    private void atualizacaoAmostraCarga(ActionEvent evt) {

        final InterfaceCarga interfaceCarga = obterCargaClicada(evt);
        final InterfaceCarga interfaceCargaThread = this;
        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceComandos().desabilitarTodasFuncoes();

        final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
        interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
        interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
        interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.processamento.importar.planilha"));

        new Thread("Thread atualiza amostra da carga") {

            @Override
            public void run() {
                if (interfaceCarga.getCargaVisualizada().equals(cargaVisualizada)) {
                    try {
                        //verifica se o navio esta atracado ou na filaDeNavio
                        if (cargaVisualizada.getNavio(navio.getHoraSituacao()).getBercoDeAtracacao() != null) { //!= null) {
                            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagemProcessamento);
                            //classe que cria a amostra
                            CriaAmostraParaCarga criaAmostraParaCarga = new CriaAmostraParaCarga(interfaceCargaThread);
                            //instacia classe de leitura de planilha
                            //criaAmostraParaCarga.criaPlanilhaLeitura(cargaVisualizada.getCaminhoCompletoPlanilha());
                            criaAmostraParaCarga.abrePlanilha();
                            // efetua a leitura dos itens de controle embarcados
                            criaAmostraParaCarga.efetuaLeituraItensDeControleEmbarcado();
                            controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().getControladorInterfaceInicial().getPlanejamento().setConsolidacaoRealizada(Boolean.FALSE);
                            controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        } else {
                            throw new CargaSelecionadaException(PropertiesUtil.buscarPropriedade("exception.carga.para.amostra.invalida"));
                        }
                    } catch (CargaSelecionadaException ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (IOException ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (BiffException ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.importacao.excel"));
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } catch (Exception ex) {
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                        interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.planilha.incompativel"));
                        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                    } finally {
                    	controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                        // habilita todas as funcoes
                        controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceComandos().habilitarTodasFuncoes();
                    }
                }
            }
        }.start();
    }

    private void exibirRastreabilidadeCarga(ActionEvent evt) {
        try {
            if (cargaVisualizada.getProduto() != null && cargaVisualizada.getProduto().getListaDeRastreabilidades() != null && !cargaVisualizada.getProduto().getListaDeRastreabilidades().isEmpty()) {
                InterfaceRastreabilidade interfaceRastreabilidade = new InterfaceRastreabilidade(controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial(), true, cargaVisualizada.getProduto().getListaDeRastreabilidades());
                interfaceRastreabilidade.setControladorInterfaceInicial(controladorInterfaceFilaDeNavios.getInterfaceInicial());
                interfaceRastreabilidade.setLocationRelativeTo(null);
                interfaceRastreabilidade.setVisible(true);
            } else {
                throw new RastreabilidadeException(PropertiesUtil.getMessage("mensagem.produto.rastreabilidade.nao.definida"));
            }
        } catch (RastreabilidadeException rastEx) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(rastEx.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        } catch (ErroSistemicoException errSis) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        }
    }

    private void definePropriedadesParaCarga() {
        this.setLayout(null);
        //tooltip com os dados da carga

        List<String> listaParam = new ArrayList<String>();
        if (cargaVisualizada != null) {
        listaParam.add(cargaVisualizada.getIdentificadorCarga());
        listaParam.add(cargaVisualizada.getOrientacaoDeEmbarque() != null ? cargaVisualizada.getOrientacaoDeEmbarque().getQuantidadeNecessaria().toString() : "0");
        if (cargaVisualizada.getProduto() != null)
            listaParam.add(cargaVisualizada.getProduto().getQuantidade().toString());
        }
        visibilidadeMenusCarga(this.getComponentPopupMenu());
    }

    private void calculaPosicaoCarga() {
        Rectangle dimensaoNavio = navio.getBounds();
        int qtdeCargasInseridas = navio.getComponentCount() - 1;

        int alturaCarga = 0;
        int larguraCarga = 0;
        int posicaoX = 0;
        int posicaoY = 0;
        Integer nroDeCargasDoNavio = navio.getListaDecarga().size();

        alturaCarga = (int) hashDeImagens.get(nroDeCargasDoNavio).getHeight();
        larguraCarga = (int) hashDeImagens.get(nroDeCargasDoNavio).getWidth();
        posicaoX = ((larguraCarga * qtdeCargasInseridas) + 37);
        posicaoY = (int) ((dimensaoNavio.getHeight() / 2) - (alturaCarga / 2));

        this.setBounds(posicaoX, posicaoY, larguraCarga, alturaCarga);
    }

    @Override
    public void deselecionar() {
        try {
            controladorInterfaceFilaDeNavios.deselecionaCarga(cargaVisualizada);
            this.cargaSelecionada = Boolean.FALSE;
            this.setImagemDSP("");
        } catch (BlendagemInvalidaException e) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        } catch (ProdutoIncompativelException po) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(po.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        }
    }

    @Override
    public void selecionar() {
        try {
            controladorInterfaceFilaDeNavios.selecionaCarga(cargaVisualizada);
            this.cargaSelecionada = Boolean.TRUE;

            this.setImagemDSP(hashNomeImagemSelecao.get(navio.getListaDecarga().size()));
            this.setDimensaoImagem(this.getBounds());


        } catch (BlendagemInvalidaException e) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        } catch (CampanhaIncompativelException ca) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ca.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        } catch (ProdutoIncompativelException po) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(po.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        } catch (CargaSelecionadaException cs) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(cs.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        } catch (ExcessoDeMaterialParaEmbarqueException empe) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(empe.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        }
    }

    public Carga getCargaVisualizada() {
        return cargaVisualizada;
    }

    public void setCargaVisualizada(Carga cargaVisualizada) {
        this.cargaVisualizada = cargaVisualizada;
    }

    public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios() {
        return controladorInterfaceFilaDeNavios;
    }

    public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) {
        this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
    }

    public InterfaceNavio getNavio() {
        return navio;
    }

    public void setNavio(InterfaceNavio navio) {
        this.navio = navio;
    }

    public int getStatusAtendimento() {
        return statusAtendimento;
    }

    public void setStatusAtendimento(int statusAtendimento) {
        this.statusAtendimento = statusAtendimento;
    }

    @Override
    public Boolean isSelecionada() {
        return cargaSelecionada;
    }

    @Override
    public void setSelecionada(Boolean selecionada) {
        this.cargaSelecionada = selecionada;
    }

    @Override
    public void inicializaInterface() {
        definePropriedadesParaCarga();
        criarPopMenuCarga();
        desabilitaMenusPermissaoUsuario();
        calculaPosicaoCarga();
        // obtendo e pintando o objeto a cor do produto
        if(cargaVisualizada != null && cargaVisualizada.getOrientacaoDeEmbarque()!=null && cargaVisualizada.getOrientacaoDeEmbarque().getTipoProduto() != null)
        {
           String[] rgb = cargaVisualizada.getOrientacaoDeEmbarque().getTipoProduto().getCorIdentificacao().split(",");
           Color corProdutoCarga = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
           this.setBackground(corProdutoCarga);
        }
    }

    @Override
    public void defineDimensoesFixas() {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    public void setDesenharCargaNavioFila(Boolean desenharCargaNavioFila) {
        this.desenharCargaNavioFila = desenharCargaNavioFila;
    }

    public String getPathFileName() {
        return pathFileName;
    }

    public void setPathFileName(String pathFileName) {
        this.pathFileName = pathFileName;
    }

    /**
     * Metodo que desabilita os menus existentes neste objeto em caso de processamento
     */
    @Override
    public void desabilitarMenus() {
        if (this.getComponentPopupMenu() != null) {
            for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                this.getComponentPopupMenu().getComponent(i).setEnabled(false);
            }
        }
    }

    /**
     * Metodo que habilita os menus existentes neste objeto em caso de finalizacao processamento
     */
    @Override
    public void habilitarMenus() {
        if (this.getComponentPopupMenu() != null) {
            for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                this.getComponentPopupMenu().getComponent(i).setEnabled(true);
            }
        }
    }

    /**
     * popula a o hash com as dimensoes das cargas
     */
    private void preencheHashMapImagem() {
        hashDeImagens = new HashMap<Integer, Dimension>();
        hashDeImagens.put(1, new Dimension(216, 50));
        hashDeImagens.put(2, new Dimension(108, 50));
        hashDeImagens.put(3, new Dimension(72, 50));
        hashDeImagens.put(4, new Dimension(54, 50));
        hashDeImagens.put(5, new Dimension(43, 50));
        hashDeImagens.put(6, new Dimension(36, 50));
        hashDeImagens.put(7, new Dimension(31, 50));
        hashDeImagens.put(8, new Dimension(27, 50));
        hashDeImagens.put(9, new Dimension(24, 50));
        hashDeImagens.put(10, new Dimension(22, 50));
    }

    private void defineImagemParaSelecaoDeCarga() {
        hashNomeImagemSelecao = new HashMap<Integer, String>();
        hashNomeImagemSelecao.put(1, "selecionar_carga_navio-01.png");
        hashNomeImagemSelecao.put(2, "selecionar_carga_navio-02.png");
        hashNomeImagemSelecao.put(3, "selecionar_carga_navio-03.png");
        hashNomeImagemSelecao.put(4, "selecionar_carga_navio-04.png");
        hashNomeImagemSelecao.put(5, "selecionar_carga_navio-05.png");
        hashNomeImagemSelecao.put(6, "selecionar_carga_navio-06.png");
        hashNomeImagemSelecao.put(7, "selecionar_carga_navio-07.png");
        hashNomeImagemSelecao.put(8, "selecionar_carga_navio-08.png");
        hashNomeImagemSelecao.put(9, "selecionar_carga_navio-09.png");
        hashNomeImagemSelecao.put(10, "selecionar_carga_navio-10.png");
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (cargaVisualizada.getProduto() != null) {
            buffer.append(cargaVisualizada.getProduto().getTipoProduto().toString()).append(" - ");
            buffer.append(cargaVisualizada.getProduto().getQuantidade()).append(" ton - ");
            if (cargaVisualizada.getProduto().getQualidade() != null) {
                buffer.append(cargaVisualizada.getProduto().getQualidade().toString());
            }else {
                buffer.append("Qualidade indefinida");
            }
        }   
        return buffer.toString();
    }

    private void desabilitaMenusPermissaoUsuario(){
       /* if(controladorInterfaceFilaDeNavios.getInterfaceInicial().verificaPermissaoAtualizacaoProducao()){
            mnuCaminhoArquivo.setEnabled(false);
            mnuItemAtualizar.setEnabled(false);
        }*/
    }
}
