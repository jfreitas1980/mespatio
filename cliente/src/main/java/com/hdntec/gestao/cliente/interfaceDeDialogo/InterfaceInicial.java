
package com.hdntec.gestao.cliente.interfaceDeDialogo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.hdntec.gestao.cliente.atualizacao.sistemasexternos.sap.AgendamentoIntegracaoSAP;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceBlendagem.ControladorInterfaceBlendagem;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceBlendagem.InterfaceBlendagem;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos.InterfaceComandos;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos.InterfaceComandosSlider;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.ControladorInterfaceFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceBerco;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfacePier;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InformacoesCampanhaPnl;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceAnotacao;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceBaliza;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceCorreia;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceDSP;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceDadosEdicao;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceFiltragem;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceMaquinaDoPatio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfacePatio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfacePilha;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceUsina;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceNavegacao.ControladorInterfaceNavegacaoEventos;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceNavegacao.InterfaceNavegacaoEventos;
import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceDeDialogo.uteis.DefinePropriedadesFixas;
import com.hdntec.gestao.cliente.interfaceDeDialogo.uteis.ImageHandler;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.messagens.ThreadMensagem;
import com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento.IControladorPlanejamento;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.domain.blendagem.ControladorCalculoQualidade;
import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.integracao.IntegracaoParametros;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorCorreias;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorPatios;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorUsinas;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.status.Anotacao;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.TempoInsuficienteException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.hibernate.HibernateUtil;
import com.hdntec.gestao.integracao.IControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.login.entity.User;
import com.hdntec.gestao.login.ws.service.LogonPromptDialog;
import com.hdntec.gestao.services.controladores.impl.ControladorFilaDeNavios;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * Interface principal do sistema. Essa interface Ã© uma agregaÃ§Ã£o das interfaces:
 * <ul>
 * <li>interface de navegacao de eventos;
 * <li>interface de blendagem;
 * <li>interface de comandos;
 * <li>interface do DSP;
 * <li>interface de fila de navios.
 * </ul>
 *
 * @author andre
 *
 */
@SuppressWarnings("serial")
public class InterfaceInicial extends javax.swing.JFrame {

    public static User getUsuarioLogado() {
        return usuarioLogado;
    }

    // método para realizar lookup dos serviços de integracao com sistemas externos
    public static IControladorIntegracaoSistemasExternos lookUpIntegracaoSistemasExternos() {
        try {
            return null;//(IControladorIntegracaoSistemasExternos) ServiceLocator.getRemote("bs/ControladorIntegracaoSistemasExternos", IControladorIntegracaoSistemasExternos.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // método para realizar lookup dos serviços remotamente
    public static IControladorModelo lookUpModelo() {
        try {
            return new ControladorModelo();// (IControladorModelo) ServiceLocator.getRemote("bs/ControladorModelo", IControladorModelo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                dialog.setVisible(true);
                User user = dialog.getUser();
                if (user != null) {
                    usuarioLogado = user;

                    InterfaceInicial init = new InterfaceInicial();
                    init.setVisible(true);
                    InterfaceInicial.montaLogLogin();
                }
            }
        });
    }

    public static void montaLogLogin() {
        StringBuilder mensagem = new StringBuilder();
        String branco = " ";
        mensagem.append(PropertiesUtil.getMessage("log.mensagem.usuario"));
        mensagem.append(branco);
        if (usuarioLogado != null)
            mensagem.append(usuarioLogado.getName());
        mensagem.append(branco);
        mensagem.append(PropertiesUtil.getMessage("log.mesagem.entrou.no.sistema"));

        logger.info(mensagem);
    }

    public static void montaLogLogout() {
        StringBuilder mensagem = new StringBuilder();
        String branco = " ";
        mensagem.append(PropertiesUtil.getMessage("log.mensagem.usuario"));
        mensagem.append(branco);
        mensagem.append(usuarioLogado.getName());
        mensagem.append(branco);
        mensagem.append(PropertiesUtil.getMessage("log.mesagem.saiu.no.sistema"));

        logger.info(mensagem);
    }

    public static void setUsuarioLogado(User usuarioLogado) {
        InterfaceInicial.usuarioLogado = usuarioLogado;
    }

    private static User usuarioLogado;

    private Boolean executandoIntegracao = Boolean.FALSE;

    /** a lista de intervalos de execucao */
    private static Map<String, List<IntegracaoMES>> mapUsinaProducao;

    /** acesso ao controlador integracao sistemas externos do servidor */
    //private IControladorIntegracaoSistemasExternos controladorIntegracaoSistemasExternos;

    private static ComparadorPatios comparadorPatio = new ComparadorPatios();

    private static ComparadorCorreias comparadorCorreia = new ComparadorCorreias();

    private static LogonPromptDialog dialog = new LogonPromptDialog(null, true, PropertiesUtil
                    .buscarPropriedade("login.webservice.endpoint"), PropertiesUtil
                    .buscarPropriedade("login.webservice.changepass"));

    /** acesso ao controlador modelo do servidor */
    // private IControladorModelo controladorModelo;

    public static Map<String, List<IntegracaoMES>> getMapUsinaProducao() {
        if (mapUsinaProducao == null) {
            mapUsinaProducao = new HashMap<String, List<IntegracaoMES>>();
        }
        return mapUsinaProducao;
    }

    public static void makeMapIntegracaoMES(List<IntegracaoMES> listaDeQuantidadesUsina) {
        List<IntegracaoMES> itens = null;
        for (IntegracaoMES integraMES : listaDeQuantidadesUsina) {
            StringBuilder currKey = new StringBuilder();
            currKey.append(integraMES.getCdFaseProcesso().toString().trim());
            currKey.append(integraMES.getCdItemControle().toString().trim());
            currKey.append(integraMES.getCdTipoProcesso().trim());
            currKey.append(integraMES.getCdAreaRespEd().trim());
            if (!getMapUsinaProducao().containsKey(currKey.toString())) {
                itens = new ArrayList<IntegracaoMES>();
                itens.add(integraMES);
                getMapUsinaProducao().put(currKey.toString(), itens);
            } else {
                itens = getMapUsinaProducao().get(currKey.toString());
                if (!itens.contains(integraMES)) {
                    itens.add(integraMES);
                    Collections.sort(itens);
                }
            }
        }
    }

    /** acesso ao controlador das operações do subsistema de interface principal */
    private ControladorInterfaceInicial controladorInterfaceInicial;

    /** interface de navegação de eventos, onde pode-se navegar pelas diversas situações do plano */
    private static InterfaceNavegacaoEventos interfaceNavegacaoEventos = null;

    /** interface de blendagem, onde pode-se enxergar o atendimento das cargas pela blendagem */
    private InterfaceBlendagem interfaceBlendagem;

    /** interface de comandos, onde pode-se executar comandos no sistema **/
    private InterfaceComandos interfaceComandos = null;

    /** interface do DSP, onde estão representados os itens presentes no pátio */
    private static InterfaceDSP interfaceDSP = null;

    /** interface de fila de navios, onde estão representados os navios e suas cargas na fila */
    private InterfaceFilaDeNavios interfaceFilaDeNavios = null;

    /** Indice da situacao de patio do plano oficial que esta selecionada no momento */
    private Integer indiceSituacaoPatioSelecionada;

    /** Modo de operação do sistema */
    private ModoDeOperacaoEnum modoDeOperacao;

    /** Controlador da Interface Fila de Navios */
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaNavios;

    /** lista de piers desenhados no DSP */
    private static List<InterfacePier> listaInterfacePiers = new ArrayList<InterfacePier>();

    /** o processo da interfaceMensagem */
    private ThreadMensagem processamentoMensagens;

    /** a Situacao de patio que esta exibida no momento */
    private SituacaoPatio situacaoPatioExibida;
    /** flag que verifica se o slider esta sendo movimenta atraves dos botoes de navegacao */
    private Boolean movimentacaoViaBotoesDeNavegacao;
    /** interface de comandos, onde pode-se executar comandos no sistema **/
    private InterfaceComandosSlider interfaceComandosSlider;

    /** caminho do diretorio de imagens*/
    private static String diretorioImagem = "tela.diretorio.imagens";

    /** imagem de background da interfaceInicial, do novo design*/
    private BufferedImage imagemFundoInterface;

    /** popmenu para inserir anotação*/
    private static JPopupMenu popMnuPatio = null;

    //posicao onde sera inserida a anotacao
    private Integer anotacaoEixoX;

    private Integer anotacaoEixoY;

    /** Log para Login e Logout*/
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("loginLogout");

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanelMetaDeEmbarque;

    private javax.swing.JPanel jPanelMetaDeProducao;

    private javax.swing.JPanel jPanelOrientacaoDeEmbarque;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTabbedPane jTabbedPaneInterfaceBlendagem;

    private javax.swing.JLabel labelLogoSamarco;

    private javax.swing.JLabel lbNomeUsuario;

    private javax.swing.JLabel lbTextoUsuario;

    private javax.swing.JLabel lblDataSituacaoExibida;

    private javax.swing.JLabel lblGrpBalizaCliente;

    private javax.swing.JLabel lblHoraSituacaoExibida;

    private javax.swing.JLabel lblInformacaoQualidadeMetaProducao;

    private javax.swing.JLabel lblInformacaoQualidadeOrientacaoEmbarque;

    private javax.swing.JLabel lblInformacaoQualidadePnlMetaEmbarque;

    private javax.swing.JLabel lblMensagem;

    private javax.swing.JLabel lblMensagemSitPatio;

    private javax.swing.JPanel pnlCoresProdutos;

    private javax.swing.JPanel pnlDataHoraSituacao;

    private javax.swing.JPanel pnlInfoGrupoBalizas;

    private javax.swing.JPanel pnlInformacoesBercoOeste;

    private javax.swing.JPanel pnlInterfaceBlendagem;

    private javax.swing.JPanel pnlInterfaceComandos;

    private javax.swing.JPanel pnlInterfaceComandosSlider;

    private javax.swing.JPanel pnlInterfaceDSP;

    private javax.swing.JPanel pnlInterfaceFilaNavios;

    private javax.swing.JPanel pnlInterfaceInformacaoBercoLeste;

    private javax.swing.JPanel pnlInterfacePierOeste;

    private javax.swing.JPanel pnlInterfaceUsinas;

    private javax.swing.JPanel pnlItensControleMetaEmbarque;

    private javax.swing.JPanel pnlItensControleMetaProducao;

    private javax.swing.JPanel pnlItensControleOrientacaoEmbarque;

    private javax.swing.JPanel pnlListaCampanhas;

    private javax.swing.JPanel pnlLogoSamarco;

    private javax.swing.JPanel pnlMensagens;

    private javax.swing.JPanel pnlNaviosFila;

    private com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica pnlPrincipal;

    private javax.swing.JPanel pnlusuario;

    private javax.swing.JScrollPane scrBlendagemMetaProducao;

    private javax.swing.JScrollPane scrBlendagemPnlMetaEmbarque;

    private javax.swing.JScrollPane scrBlendagemPnlOrientacaoEmbarque;

    private javax.swing.JScrollPane scrCoresProdutos;

    private javax.swing.JScrollPane scrFilaNavios;

    private javax.swing.JScrollPane scrFramePrincipal;

    private javax.swing.JSlider sliderNavegacao;

    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tabelaProdutoSelecionadoQuantidade;

    private javax.swing.JTabbedPane tbFilaDeNavios;
    // End of variables declaration//GEN-END:variables

    /** Constantes da tabela de produtos selecionados */
    private final Integer COL_PRODUTO_SELECIONADO = 0;

    private final Integer COL_QUANTIDADE_SELECIONADA = 1;

    /* public void montaPatio()
     {
        montaInterfaceDSP();
     }
    */
    /* public IControladorModelo getControladorModelo()
     {
      return controladorModelo;
     }*/

    private final Integer COL_PORCENTAGEM = 2;

    /** Lista de colunas da tabela de produtos selecionados */
    private List<ColunaTabela> listaColunaInformacoes;

    public InterfaceInicial() {

        //imagem de background
        imagemFundoInterface = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem),
                        "layout_background.png");
        initComponents();
        //    SplashWindow splash = new SplashWindow();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Modo de operacao inicial nulo
        setModoDeOperacao(ModoDeOperacaoEnum.OPERACAONULA);
        movimentacaoViaBotoesDeNavegacao = Boolean.FALSE;
        defineControladores();

        //imagem de background
        //      imagemFundoInterface = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem), "layout_background.png");
        pnlPrincipal.setImagemDSP(imagemFundoInterface);
        pnlPrincipal.setDimensaoImagem(pnlPrincipal.getBounds());

        defineEventosParaInterfaceInicial();

        montaInterfaceNavegacao();

        //TODO Darley trocando a ordem de exibicao
        controladorInterfaceInicial.exibirUltimaSituacaoPatio();

        montaInterfaceBlendagem();
        montaInterfaceCoresProdutos();
        montaInterfaceComandos();
         montaInterfaceComandosDoSlider();
        System.out.println("Desabilitei leitor de bit de integração");
        //configuraAtualizacaoSistemaSAP();

        //thread que verifica se existe atualizacao nos sistemas externos para que possam ser gravados para a baseDeDados do stockyard
        //observadorIntegracaoParametros();

        if (this.getSituacaoPatioExibida() != null && this.getSituacaoPatioExibida().getPlanoEmpilhamento().getEhOficial()) {
            this.setTitle(this.getTitle() + " - " + PropertiesUtil.getMessage("titulo.plano.oficial"));
        }

        montaEscalaMeta();

        try {
            criaColunasInformacoesBalizasSelecionadas();
            controladorInterfaceInicial.atualizarPlanoOficialComAtividades();
        } catch (BlendagemInvalidaException ex) {
            Logger.getLogger(InterfaceInicial.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CampanhaIncompativelException ex) {
            Logger.getLogger(InterfaceInicial.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TempoInsuficienteException ex) {
            Logger.getLogger(InterfaceInicial.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErroSistemicoException ex) {
            Logger.getLogger(InterfaceInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
        UIManager.put("OptionPane.yesButtonText", PropertiesUtil.getMessage("mensagem.condicional.sim"));
        UIManager.put("OptionPane.noButtonText", PropertiesUtil.getMessage("mensagem.condicional.nao"));
        UIManager.put("OptionPane.cancelButtonText", PropertiesUtil.getMessage("mensagem.condicional.cancelar"));

    }

    private void adicionaEventoMouse() {
        pnlInterfaceDSP.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    anotacaoEixoX = evt.getX();
                    anotacaoEixoY = evt.getY();
                    popMnuPatio.show(pnlInterfaceDSP, anotacaoEixoX, anotacaoEixoY);
                }
            }
        });
    }

    private void apresentaUsuarioLogado() {
        lbNomeUsuario.setText(usuarioLogado.getName());
    }

    /**
     * Metodo auxiliar que configura o inicio da atualização automatica para o sistema SAP
     * de acordo com propriedades setadas no properties do sistema
     */
    private void configuraAtualizacaoSistemaSAP() {

        try {
            // cria uma classe calendário com a data corrente
            Calendar dataIniAtualizacaoSAP = Calendar.getInstance();

            //Seta a data inicial para o dia seguinte, ou seja, a próxima 00hs
            Date dataInicializacaoSAP = DSSStockyardTimeUtil.criaDataComString(PropertiesUtil
                            .buscarPropriedade("data.inicio.atualizacao.sistema.sap"), PropertiesUtil
                            .buscarPropriedade("formato.campo.data"));
            dataIniAtualizacaoSAP.setTime(dataInicializacaoSAP);

            // Seta a hora da inicializacao na data
            String horaInicializacaoSAP = PropertiesUtil.buscarPropriedade("hora.inicio.atualizacao.sistema.sap");
            dataIniAtualizacaoSAP.set(Calendar.HOUR, Integer.parseInt(horaInicializacaoSAP.split(":")[0]));
            dataIniAtualizacaoSAP.set(Calendar.MINUTE, Integer.parseInt(horaInicializacaoSAP.split(":")[1]));
            dataIniAtualizacaoSAP.set(Calendar.SECOND, Integer.parseInt(horaInicializacaoSAP.split(":")[2]));

            //Instancia o timer
            Timer timerSAP = new Timer();

            //Intervalo para executar a classe novamente # 3600 segundo = 1 hora
            Integer periodicidadeHorasSAP = Integer.parseInt(PropertiesUtil
                            .buscarPropriedade("periodicidade.atualizacao.sistema.sap"));
            long periodoExecucaoSAP = 3600 * periodicidadeHorasSAP * 1000;

            //Agenda a tarefa para atualizacao do SAP
            timerSAP.scheduleAtFixedRate(new AgendamentoIntegracaoSAP(this.controladorInterfaceInicial),
                            dataIniAtualizacaoSAP.getTime(), periodoExecucaoSAP);

        } catch (ValidacaoCampoException vcEx) {
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            ativarMensagem(interfaceMensagem);
        }
    }

    /**
    * cria popMenu Inserir e Excluir anotacao do patio
    */
    private void criaPopMenuPatio() {

        if (popMnuPatio == null) {
            popMnuPatio = new JPopupMenu();
            // menu de inserir anotação
            JMenuItem mnuInserirAnotacao = new JMenuItem();
            mnuInserirAnotacao.setText(PropertiesUtil.getMessage("titulo.mensagem.inserir.anotacao"));
            mnuInserirAnotacao.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    // metodo de INSERIR anotacao - InterfacePAtio
                    eventoInserirAnotacao(evt, anotacaoEixoX, anotacaoEixoY);
                }
            });
            popMnuPatio.add(mnuInserirAnotacao);
        }
    }

    /**
     * Cria os controladores dos subsistemas que sera acessados pela interface inicial
     */
    private void defineControladores() {

        try {
            controladorInterfaceInicial = new ControladorInterfaceInicial();

        } catch (ErroSistemicoException ex) {
            Logger.getLogger(InterfaceInicial.class.getName()).log(Level.SEVERE, null, ex);
        }

        //controladorIntegracaoSistemasExternos = lookUpIntegracaoSistemasExternos();
        //controladorInterfaceInicial.setControladorIntegracaoSistemasExternos(controladorIntegracaoSistemasExternos);

        controladorInterfaceInicial.setInterfaceInicial(this);

        // cria o controlador para fila de navios
        controladorInterfaceFilaNavios = new ControladorInterfaceFilaDeNavios();
        controladorInterfaceFilaNavios.setInterfaceInicial(controladorInterfaceInicial);
        controladorInterfaceFilaNavios.setInterfaceDadosEdicao(new InterfaceDadosEdicao());
        // adiciona o controlador da fila de navios no controlador inicial
        controladorInterfaceInicial.setInterfaceFilaDeNavios(controladorInterfaceFilaNavios);

        // cria o controlador para o calculo da qualidade
        ControladorCalculoQualidade controladorCalculoQualidade = new ControladorCalculoQualidade();
        // adiciona o calculo da qualidade no controlador inicial
        controladorInterfaceInicial.setCalculoQualidade(controladorCalculoQualidade);

    }

    /**
     * Metodo auxiliar que cria e define os eventos dos paineis principais da interface uinicial
     */
    private void defineEventosParaInterfaceInicial() {
        // Eventos acionado qdo algum componente for adicionado ao painel do DSP
        pnlInterfaceDSP.addContainerListener(new java.awt.event.ContainerAdapter() {

            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                ((InterfaceInicializacao) evt.getChild()).inicializaInterface();
            }
        });

        // Eventos acionado qdo algum componente for adicionado ao painel de Usinas
        pnlInterfaceUsinas.addContainerListener(new java.awt.event.ContainerAdapter() {

            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                ((InterfaceInicializacao) evt.getChild()).inicializaInterface();
            }
        });

        // Eventos acionado qdo algum componente for adicionado ao painel de fila de navios
        pnlInterfacePierOeste.addContainerListener(new java.awt.event.ContainerAdapter() {

            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                ((InterfaceInicializacao) evt.getChild()).inicializaInterface();
            }
        });

    }

    private void desenhaDataHoraSituacaoPatioExibida(SituacaoPatio situacaoPatioExibida) {

        try {
            lblHoraSituacaoExibida.setText("");
            lblDataSituacaoExibida.setText("");
            lblMensagemSitPatio.setText("");

            String mensagemHora = PropertiesUtil.getMessage("texto.abre.html.desenha.situacao.patio");
            String mensagemData = PropertiesUtil.getMessage("texto.abre.html.desenha.situacao.patio");
            String mensagemSituacaoPatio = PropertiesUtil.getMessage("texto.abre.html.desenha.situacao.patio");

            // cria uma string com a data atual no formato DD/MM/YYYY HH:MM:SS para ignorar os miliseguindos do new Date()
            String dataAtualFormatada = DSSStockyardTimeUtil.formatarData(new Date(), PropertiesUtil
                            .buscarPropriedade("formato.campo.datahora"));

            // converte a string para um campo date
            Date dataAtual = DSSStockyardTimeUtil.criaDataComString(dataAtualFormatada, PropertiesUtil
                            .buscarPropriedade("formato.campo.datahora"));

            String formatoHora = PropertiesUtil.buscarPropriedade("formato.campo.hora");
            String formatoData = PropertiesUtil.buscarPropriedade("formato.campo.data");
            SimpleDateFormat formatoHoraLongo = new SimpleDateFormat(formatoHora, new Locale("pt", "BR"));
            SimpleDateFormat formatoDataLongo = new SimpleDateFormat(formatoData, new Locale("pt", "BR"));

            if (dataAtual.compareTo(situacaoPatioExibida.getDtInicio()) > 0) {
                // se a data atual comparada com a data da situacao exibida for maior que zero
                // significa que a situacao esta no passado e ja foi realizada
                mensagemSituacaoPatio += PropertiesUtil.getMessage("mensagem.situacao.patio.realizada");
                mensagemHora += formatoHoraLongo.format(situacaoPatioExibida.getDtInicio());
                mensagemData += formatoDataLongo.format(situacaoPatioExibida.getDtInicio());
            } else {
                // se a data atual comparada com a data da situacao exibida for menor que zero
                // significa que a situacao esta no futuro e ainda nao foi realizada
                mensagemSituacaoPatio += PropertiesUtil.getMessage("mensagem.situacao.patio.planejado");
                mensagemHora += formatoHoraLongo.format(situacaoPatioExibida.getDtInicio());
                mensagemData += formatoDataLongo.format(situacaoPatioExibida.getDtInicio());
            }

            mensagemSituacaoPatio += PropertiesUtil.getMessage("texto.fecha.html.desenha.situacao.patio");
            mensagemData += PropertiesUtil.getMessage("texto.fecha.html.desenha.situacao.patio");
            mensagemHora += PropertiesUtil.getMessage("texto.fecha.html.desenha.situacao.patio");

            lblMensagemSitPatio.setText(mensagemSituacaoPatio);
            lblHoraSituacaoExibida.setText(mensagemHora);
            lblDataSituacaoExibida.setText(mensagemData);

        } catch (ValidacaoCampoException vcEx) {
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            this.ativarMensagem(interfaceMensagem);
        }
    }

    private void eventoInserirAnotacao(ActionEvent evt, int eixoX, int eixoY) {
        try {
            inserirAnotacao(eixoX, eixoY);

        } catch (OperacaoCanceladaPeloUsuarioException ex) {
            interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing

        int confirm = JOptionPane.showOptionDialog(this, PropertiesUtil.getMessage("mensagem.titulo.sair.mensagem"),
                        PropertiesUtil.getMessage("mensagem.titulo.sair"), JOptionPane.YES_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, null, null);

        if (confirm == JOptionPane.YES_OPTION) {
            HibernateUtil.closeSession();
            String loginSTR = null;
            if (InterfaceInicial.usuarioLogado != null)
                loginSTR = InterfaceInicial.usuarioLogado.getLogin();
            try {
                dialog.executarLogout(loginSTR);
            } catch (Exception e) {
                e.printStackTrace();
            }

            InterfaceInicial.montaLogLogout();
            System.exit(EXIT_ON_CLOSE);
        } else {
            return;
        }

    }//GEN-LAST:event_formWindowClosing 

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrFramePrincipal = new javax.swing.JScrollPane();
        pnlPrincipal = new RepresentacaoGrafica(imagemFundoInterface);
        sliderNavegacao = new javax.swing.JSlider();
        pnlInterfacePierOeste = new javax.swing.JPanel();
        pnlInterfaceDSP = new javax.swing.JPanel();
        pnlInterfaceUsinas = new javax.swing.JPanel();
        pnlInterfaceComandos = new javax.swing.JPanel();
        pnlInterfaceBlendagem = new javax.swing.JPanel();
        jTabbedPaneInterfaceBlendagem = new javax.swing.JTabbedPane();
        jPanelOrientacaoDeEmbarque = new javax.swing.JPanel();
        scrBlendagemPnlOrientacaoEmbarque = new javax.swing.JScrollPane();
        pnlItensControleOrientacaoEmbarque = new javax.swing.JPanel();
        lblInformacaoQualidadeOrientacaoEmbarque = new javax.swing.JLabel();
        jPanelMetaDeEmbarque = new javax.swing.JPanel();
        scrBlendagemPnlMetaEmbarque = new javax.swing.JScrollPane();
        pnlItensControleMetaEmbarque = new javax.swing.JPanel();
        lblInformacaoQualidadePnlMetaEmbarque = new javax.swing.JLabel();
        jPanelMetaDeProducao = new javax.swing.JPanel();
        scrBlendagemMetaProducao = new javax.swing.JScrollPane();
        pnlItensControleMetaProducao = new javax.swing.JPanel();
        lblInformacaoQualidadeMetaProducao = new javax.swing.JLabel();
        pnlInfoGrupoBalizas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblGrpBalizaCliente = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaProdutoSelecionadoQuantidade = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        pnlInterfaceFilaNavios = new javax.swing.JPanel();
        scrFilaNavios = new javax.swing.JScrollPane();
        pnlNaviosFila = new javax.swing.JPanel();
        tbFilaDeNavios = new javax.swing.JTabbedPane();
        pnlInformacoesBercoOeste = new javax.swing.JPanel();
        pnlMensagens = new javax.swing.JPanel();
        lblMensagem = new javax.swing.JLabel();
        pnlListaCampanhas = new javax.swing.JPanel();
        pnlDataHoraSituacao = new javax.swing.JPanel();
        lblHoraSituacaoExibida = new javax.swing.JLabel();
        lblDataSituacaoExibida = new javax.swing.JLabel();
        lblMensagemSitPatio = new javax.swing.JLabel();
        scrCoresProdutos = new javax.swing.JScrollPane();
        pnlCoresProdutos = new javax.swing.JPanel();
        pnlInterfaceInformacaoBercoLeste = new javax.swing.JPanel();
        pnlLogoSamarco = new javax.swing.JPanel();
        labelLogoSamarco = new javax.swing.JLabel();
        pnlInterfaceComandosSlider = new javax.swing.JPanel();
        pnlusuario = new javax.swing.JPanel();
        lbTextoUsuario = new javax.swing.JLabel();
        lbNomeUsuario = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(PropertiesUtil.getMessage("titulo.interfaceinicial.mensagem.mespatio"));
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        
        Insets in = java.awt.Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
        
        
        int width = screenSize.width-(in.left + in.top);
        int height = screenSize.height-(in.top + in.bottom);
        //setSize(width,height);
        //setLocation(in.left,in.top);
        
        //set
        //this.setBounds (in.left,in.top, width, height);
        
        setBounds(new java.awt.Rectangle(0, 0, width, height));
        setIconImage(ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem), "logo_boneco.png"));
        setMinimumSize(new java.awt.Dimension(width, height));
        setName("frmPrincipal"); // NOI18N
        //set
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        scrFramePrincipal.setPreferredSize(new java.awt.Dimension(1900, 1198));

        pnlPrincipal.setAutoscrolls(true);
        pnlPrincipal.setMaximumSize(new java.awt.Dimension(1950, 1250));
        pnlPrincipal.setMinimumSize(new java.awt.Dimension(1900, 1100));
        pnlPrincipal.setOpaque(false);
        pnlPrincipal.setPreferredSize(new java.awt.Dimension(1900, 1140));
        pnlPrincipal.setLayout(null);

        sliderNavegacao.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        sliderNavegacao.setBounds(2, 850, 1760, 1920);
        sliderNavegacao.setMajorTickSpacing(1);
        sliderNavegacao.setMinimum(1);
        sliderNavegacao.setPaintLabels(true);
        sliderNavegacao.setPaintTicks(true);
        sliderNavegacao.setMaximumSize(new java.awt.Dimension(600, 41));
        sliderNavegacao.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderNavegacaoStateChanged(evt);
            }
        });
        pnlPrincipal.add(sliderNavegacao);
        sliderNavegacao.setBounds(840, 830, 1060, 60);

        pnlInterfacePierOeste.setMaximumSize(new java.awt.Dimension(120, 120));
        pnlInterfacePierOeste.setMinimumSize(new java.awt.Dimension(100, 100));
        pnlInterfacePierOeste.setLayout(null);
        pnlInterfacePierOeste.setBounds(1490, 261, 291, 310);
        pnlPrincipal.add(pnlInterfacePierOeste);
        pnlInterfacePierOeste.setBounds(900, 900, 670, 180);

        pnlInterfaceDSP.setBackground(new java.awt.Color(255, 255, 255));
        pnlInterfaceDSP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInterfaceDSP.setMinimumSize(new java.awt.Dimension(500, 500));
        pnlInterfaceDSP.setLayout(null);
        pnlPrincipal.add(pnlInterfaceDSP);
        pnlInterfaceDSP.setBounds(590, 81, 1320, 730);

        pnlInterfaceUsinas.setBackground(java.awt.SystemColor.window);
        pnlInterfaceUsinas.setMaximumSize(new java.awt.Dimension(327, 327));
        pnlInterfaceUsinas.setMinimumSize(new java.awt.Dimension(97, 200));
        pnlInterfaceUsinas.setOpaque(false);
        pnlInterfaceUsinas.setLayout(null);
        pnlInterfaceUsinas.setBounds(430, 1, 97, 400);
        pnlPrincipal.add(pnlInterfaceUsinas);
        pnlInterfaceUsinas.setBounds(490, 81, 97, 400);

        pnlInterfaceComandos.setMaximumSize(new java.awt.Dimension(214, 214));
        pnlInterfaceComandos.setMinimumSize(new java.awt.Dimension(150, 100));
        pnlInterfaceComandos.setPreferredSize(new java.awt.Dimension(20, 20));
        pnlInterfaceComandos.setLayout(new java.awt.BorderLayout());
        pnlInterfaceComandos.setBounds(0, 0, 880, 97);
        pnlPrincipal.add(pnlInterfaceComandos);
        pnlInterfaceComandos.setBounds(0, 0, 1240, 80);

        pnlInterfaceBlendagem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PropertiesUtil
                        .getMessage("mensagem.qualidade.media"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11),
                        new java.awt.Color(59, 58, 58))); // NOI18N
        pnlInterfaceBlendagem.setMaximumSize(new java.awt.Dimension(600, 600));
        pnlInterfaceBlendagem.setMinimumSize(new java.awt.Dimension(466, 500));
        pnlInterfaceBlendagem.setPreferredSize(new java.awt.Dimension(466, 500));

        jTabbedPaneInterfaceBlendagem.setPreferredSize(new java.awt.Dimension(466, 460));

        jPanelOrientacaoDeEmbarque.setMinimumSize(new java.awt.Dimension(50, 50));
        jPanelOrientacaoDeEmbarque.setPreferredSize(new java.awt.Dimension(460, 400));

        pnlItensControleOrientacaoEmbarque.setLayout(new java.awt.GridLayout(0, 1));
        scrBlendagemPnlOrientacaoEmbarque.setViewportView(pnlItensControleOrientacaoEmbarque);

        lblInformacaoQualidadeOrientacaoEmbarque.setForeground(java.awt.Color.blue);
        lblInformacaoQualidadeOrientacaoEmbarque.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInformacaoQualidadeOrientacaoEmbarque.setText("Esse produto está aguardando resultado bi-horário");

        javax.swing.GroupLayout jPanelOrientacaoDeEmbarqueLayout = new javax.swing.GroupLayout(jPanelOrientacaoDeEmbarque);
        jPanelOrientacaoDeEmbarque.setLayout(jPanelOrientacaoDeEmbarqueLayout);
        jPanelOrientacaoDeEmbarqueLayout
                        .setHorizontalGroup(jPanelOrientacaoDeEmbarqueLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrBlendagemPnlOrientacaoEmbarque,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                                        .addGroup(
                                                        jPanelOrientacaoDeEmbarqueLayout
                                                                        .createParallelGroup(
                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(
                                                                                        jPanelOrientacaoDeEmbarqueLayout
                                                                                                        .createSequentialGroup()
                                                                                                        .addContainerGap()
                                                                                                        .addComponent(
                                                                                                                        lblInformacaoQualidadeOrientacaoEmbarque,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        439,
                                                                                                                        Short.MAX_VALUE)
                                                                                                        .addContainerGap())));
        jPanelOrientacaoDeEmbarqueLayout
                        .setVerticalGroup(jPanelOrientacaoDeEmbarqueLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        jPanelOrientacaoDeEmbarqueLayout
                                                                        .createSequentialGroup()
                                                                        .addGap(39, 39, 39)
                                                                        .addComponent(
                                                                                        scrBlendagemPnlOrientacaoEmbarque,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        394,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addContainerGap(
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE))
                                        .addGroup(
                                                        jPanelOrientacaoDeEmbarqueLayout
                                                                        .createParallelGroup(
                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(
                                                                                        jPanelOrientacaoDeEmbarqueLayout
                                                                                                        .createSequentialGroup()
                                                                                                        .addContainerGap()
                                                                                                        .addComponent(
                                                                                                                        lblInformacaoQualidadeOrientacaoEmbarque,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        22,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addGap(512, 512,
                                                                                                                        512))));

        jTabbedPaneInterfaceBlendagem.addTab(PropertiesUtil.getMessage("mensagem.interface.inicial.orientacao.embarque"),
                        jPanelOrientacaoDeEmbarque);

        pnlItensControleMetaEmbarque.setLayout(new java.awt.GridLayout(0, 1));
        scrBlendagemPnlMetaEmbarque.setViewportView(pnlItensControleMetaEmbarque);

        lblInformacaoQualidadePnlMetaEmbarque.setForeground(java.awt.Color.blue);
        lblInformacaoQualidadePnlMetaEmbarque.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInformacaoQualidadePnlMetaEmbarque.setText("Esse produto está aguardando resultado bi-horário");

        javax.swing.GroupLayout jPanelMetaDeEmbarqueLayout = new javax.swing.GroupLayout(jPanelMetaDeEmbarque);
        jPanelMetaDeEmbarque.setLayout(jPanelMetaDeEmbarqueLayout);
        jPanelMetaDeEmbarqueLayout.setHorizontalGroup(jPanelMetaDeEmbarqueLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addComponent(scrBlendagemPnlMetaEmbarque,
                        javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE).addGroup(
                        jPanelMetaDeEmbarqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                                        jPanelMetaDeEmbarqueLayout.createSequentialGroup().addContainerGap().addComponent(
                                                        lblInformacaoQualidadePnlMetaEmbarque,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                                                        .addContainerGap())));
        jPanelMetaDeEmbarqueLayout.setVerticalGroup(jPanelMetaDeEmbarqueLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        jPanelMetaDeEmbarqueLayout.createSequentialGroup().addGap(43, 43, 43).addComponent(
                                        scrBlendagemPnlMetaEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, 389,
                                        javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
                        jPanelMetaDeEmbarqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                                        jPanelMetaDeEmbarqueLayout.createSequentialGroup().addContainerGap().addComponent(
                                                        lblInformacaoQualidadePnlMetaEmbarque,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 22,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(399,
                                                        Short.MAX_VALUE))));

        jTabbedPaneInterfaceBlendagem.addTab(PropertiesUtil.getMessage("mensagem.interface.inicial.meta.embarque"),
                        jPanelMetaDeEmbarque);

        pnlItensControleMetaProducao.setLayout(new java.awt.GridLayout(0, 1));
        scrBlendagemMetaProducao.setViewportView(pnlItensControleMetaProducao);

        lblInformacaoQualidadeMetaProducao.setForeground(java.awt.Color.blue);
        lblInformacaoQualidadeMetaProducao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInformacaoQualidadeMetaProducao.setText("Esse produto está aguardando resultado bi-horário");

        javax.swing.GroupLayout jPanelMetaDeProducaoLayout = new javax.swing.GroupLayout(jPanelMetaDeProducao);
        jPanelMetaDeProducao.setLayout(jPanelMetaDeProducaoLayout);
        jPanelMetaDeProducaoLayout.setHorizontalGroup(jPanelMetaDeProducaoLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addComponent(scrBlendagemMetaProducao,
                        javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE).addComponent(scrBlendagemMetaProducao,
                        javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE).addGroup(
                        jPanelMetaDeProducaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                        jPanelMetaDeProducaoLayout.createSequentialGroup().addContainerGap().addComponent(
                                                        lblInformacaoQualidadeMetaProducao,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                                                        .addContainerGap())));
        jPanelMetaDeProducaoLayout.setVerticalGroup(jPanelMetaDeProducaoLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        jPanelMetaDeProducaoLayout.createSequentialGroup().addComponent(lblInformacaoQualidadeMetaProducao,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                                        scrBlendagemMetaProducao, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        412, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
                        jPanelMetaDeProducaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,
                                        440, Short.MAX_VALUE)));

        jTabbedPaneInterfaceBlendagem.addTab(PropertiesUtil.getMessage("mensagem.interface.inicial.meta.producao"),
                        jPanelMetaDeProducao);

        pnlInterfaceBlendagem.setBounds(1, 131, 428, 560);

        javax.swing.GroupLayout pnlInterfaceBlendagemLayout = new javax.swing.GroupLayout(pnlInterfaceBlendagem);
        pnlInterfaceBlendagem.setLayout(pnlInterfaceBlendagemLayout);
        pnlInterfaceBlendagemLayout.setHorizontalGroup(pnlInterfaceBlendagemLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        pnlInterfaceBlendagemLayout.createSequentialGroup().addContainerGap().addComponent(
                                        jTabbedPaneInterfaceBlendagem, javax.swing.GroupLayout.DEFAULT_SIZE, 464,
                                        Short.MAX_VALUE)));
        pnlInterfaceBlendagemLayout.setVerticalGroup(pnlInterfaceBlendagemLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        pnlInterfaceBlendagemLayout.createSequentialGroup().addComponent(jTabbedPaneInterfaceBlendagem,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE).addContainerGap(12,
                                        Short.MAX_VALUE)));

        pnlPrincipal.add(pnlInterfaceBlendagem);
        pnlInterfaceBlendagem.setBounds(0, 200, 486, 500);

        pnlInfoGrupoBalizas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produtos selecionados",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11),
                        new java.awt.Color(59, 58, 58))); // NOI18N
        pnlInfoGrupoBalizas.setMaximumSize(new java.awt.Dimension(500, 500));
        pnlInfoGrupoBalizas.setMinimumSize(new java.awt.Dimension(466, 115));
        pnlInfoGrupoBalizas.setBounds(1, 1, 428, 130);

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 10));
        jLabel1.setForeground(new java.awt.Color(59, 58, 58));
        jLabel1.setText("Cliente:");

        lblGrpBalizaCliente.setFont(new java.awt.Font("Verdana", 0, 10));
        lblGrpBalizaCliente.setForeground(new java.awt.Color(59, 58, 58));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tabelaProdutoSelecionadoQuantidade.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{

        }, new String[]{

        }));
        jScrollPane1.setViewportView(tabelaProdutoSelecionadoQuantidade);

        javax.swing.GroupLayout pnlInfoGrupoBalizasLayout = new javax.swing.GroupLayout(pnlInfoGrupoBalizas);
        pnlInfoGrupoBalizas.setLayout(pnlInfoGrupoBalizasLayout);
        pnlInfoGrupoBalizasLayout
                        .setHorizontalGroup(pnlInfoGrupoBalizasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        pnlInfoGrupoBalizasLayout
                                                                        .createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(
                                                                                        pnlInfoGrupoBalizasLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                        .addComponent(
                                                                                                                        jScrollPane1,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        454,
                                                                                                                        Short.MAX_VALUE)
                                                                                                        .addGroup(
                                                                                                                        pnlInfoGrupoBalizasLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel1)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        lblGrpBalizaCliente,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        131,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                        .addContainerGap()));
        pnlInfoGrupoBalizasLayout.setVerticalGroup(pnlInfoGrupoBalizasLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        pnlInfoGrupoBalizasLayout.createSequentialGroup().addGroup(
                                        pnlInfoGrupoBalizasLayout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1)
                                                        .addComponent(lblGrpBalizaCliente,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 15,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                                        jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 53,
                                                        Short.MAX_VALUE).addContainerGap()));

        pnlPrincipal.add(pnlInfoGrupoBalizas);
        pnlInfoGrupoBalizas.setBounds(0, 81, 486, 113);

        pnlInterfaceFilaNavios.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fila de Navios",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11),
                        new java.awt.Color(59, 58, 58))); // NOI18N
        pnlInterfaceFilaNavios.setLayout(new java.awt.BorderLayout());

        pnlNaviosFila.setLayout(new java.awt.BorderLayout());

        tbFilaDeNavios.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        pnlNaviosFila.add(tbFilaDeNavios, java.awt.BorderLayout.CENTER);

        scrFilaNavios.setViewportView(pnlNaviosFila);

        pnlInterfaceFilaNavios.add(scrFilaNavios, java.awt.BorderLayout.CENTER);

        pnlPrincipal.add(pnlInterfaceFilaNavios);
        pnlInterfaceFilaNavios.setBounds(0, 895, 590, 186);

        pnlInformacoesBercoOeste.setBackground(new java.awt.Color(255, 255, 255));
        pnlInformacoesBercoOeste.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PropertiesUtil
                        .getMessage("mensagem.interface.inicial.berco.oeste"),
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 11))); // NOI18N
        pnlInformacoesBercoOeste
                        .setLayout(new javax.swing.BoxLayout(pnlInformacoesBercoOeste, javax.swing.BoxLayout.Y_AXIS));
        pnlPrincipal.add(pnlInformacoesBercoOeste);
        pnlInformacoesBercoOeste.setBounds(590, 895, 300, 186);

        pnlMensagens.setMaximumSize(new java.awt.Dimension(2147, 100));
        pnlMensagens.setMinimumSize(new java.awt.Dimension(500, 35));
        pnlMensagens.setLayout(new java.awt.BorderLayout());

        lblMensagem.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblMensagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMensagem.setMinimumSize(new java.awt.Dimension(200, 20));
        pnlMensagens.add(lblMensagem, java.awt.BorderLayout.CENTER);

        pnlPrincipal.add(pnlMensagens);
        pnlMensagens.setBounds(10, 1085, 1900, 30);

        pnlListaCampanhas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de Campanhas",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11),
                        new java.awt.Color(59, 58, 58))); // NOI18N
        pnlListaCampanhas.setLayout(new java.awt.BorderLayout());
        pnlPrincipal.add(pnlListaCampanhas);
        pnlListaCampanhas.setBounds(0, 705, 486, 188);

        pnlDataHoraSituacao.setMaximumSize(new java.awt.Dimension(214, 214));

        lblHoraSituacaoExibida.setFont(new java.awt.Font("Verdana", 1, 11));
        lblHoraSituacaoExibida.setForeground(new java.awt.Color(104, 104, 104));
        lblHoraSituacaoExibida.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblHoraSituacaoExibida.setMaximumSize(new java.awt.Dimension(30, 30));
        lblHoraSituacaoExibida.setMinimumSize(new java.awt.Dimension(20, 20));
        lblHoraSituacaoExibida.setPreferredSize(new java.awt.Dimension(20, 20));
        lblHoraSituacaoExibida.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")
                        + "/relogio.png"));

        lblDataSituacaoExibida.setFont(new java.awt.Font("Verdana", 1, 11));
        lblDataSituacaoExibida.setForeground(new java.awt.Color(104, 104, 104));
        lblDataSituacaoExibida.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDataSituacaoExibida.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")
                        + "/calendario.png"));

        lblMensagemSitPatio.setFont(new java.awt.Font("Verdana", 0, 11));
        lblMensagemSitPatio.setForeground(new java.awt.Color(104, 104, 104));

        javax.swing.GroupLayout pnlDataHoraSituacaoLayout = new javax.swing.GroupLayout(pnlDataHoraSituacao);
        pnlDataHoraSituacao.setLayout(pnlDataHoraSituacaoLayout);
        pnlDataHoraSituacaoLayout
                        .setHorizontalGroup(pnlDataHoraSituacaoLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        pnlDataHoraSituacaoLayout
                                                                        .createSequentialGroup()
                                                                        .addGroup(
                                                                                        pnlDataHoraSituacaoLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                        .addGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                        pnlDataHoraSituacaoLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        lblDataSituacaoExibida,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        116,
                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        lblHoraSituacaoExibida,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        108,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                        .addComponent(
                                                                                                                        lblMensagemSitPatio,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        230,
                                                                                                                        Short.MAX_VALUE))
                                                                        .addContainerGap()));
        pnlDataHoraSituacaoLayout
                        .setVerticalGroup(pnlDataHoraSituacaoLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                        pnlDataHoraSituacaoLayout
                                                                        .createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addComponent(
                                                                                        lblMensagemSitPatio,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        23, Short.MAX_VALUE)
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDataHoraSituacaoLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                        .addComponent(
                                                                                                                        lblDataSituacaoExibida,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        19,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        lblHoraSituacaoExibida,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))));

        pnlPrincipal.add(pnlDataHoraSituacao);
        pnlDataHoraSituacao.setBounds(1250, 10, 240, 60);

        scrCoresProdutos.setBounds(1490, 1, 269, 260);
        scrCoresProdutos.setBorder(null);

        pnlCoresProdutos.setMaximumSize(new java.awt.Dimension(300, 300));
        pnlCoresProdutos.setMinimumSize(new java.awt.Dimension(97, 200));
        pnlCoresProdutos.setLayout(new java.awt.GridLayout(0, 1, 0, 3));
        scrCoresProdutos.setViewportView(pnlCoresProdutos);

        pnlPrincipal.add(scrCoresProdutos);
        scrCoresProdutos.setBounds(490, 490, 97, 400);

        pnlInterfaceInformacaoBercoLeste.setBackground(new java.awt.Color(255, 255, 255));
        pnlInterfaceInformacaoBercoLeste.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PropertiesUtil
                        .getMessage("mensagem.interface.inicial.berco.leste"),
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 11))); // NOI18N
        pnlInterfaceInformacaoBercoLeste.setMaximumSize(new java.awt.Dimension(327, 327));
        pnlInterfaceInformacaoBercoLeste.setMinimumSize(new java.awt.Dimension(100, 100));
        pnlInterfaceInformacaoBercoLeste.setLayout(new javax.swing.BoxLayout(pnlInterfaceInformacaoBercoLeste,
                        javax.swing.BoxLayout.Y_AXIS));
        pnlPrincipal.add(pnlInterfaceInformacaoBercoLeste);
        pnlInterfaceInformacaoBercoLeste.setBounds(1580, 895, 300, 186);

        labelLogoSamarco.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLogoSamarco.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")
                        + "/logo_samarco.png"));

        javax.swing.GroupLayout pnlLogoSamarcoLayout = new javax.swing.GroupLayout(pnlLogoSamarco);
        pnlLogoSamarco.setLayout(pnlLogoSamarcoLayout);
        pnlLogoSamarcoLayout.setHorizontalGroup(pnlLogoSamarcoLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        pnlLogoSamarcoLayout.createSequentialGroup().addContainerGap().addComponent(labelLogoSamarco)
                                        .addContainerGap(200, Short.MAX_VALUE)));
        pnlLogoSamarcoLayout.setVerticalGroup(pnlLogoSamarcoLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        pnlLogoSamarcoLayout.createSequentialGroup().addComponent(labelLogoSamarco,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(20, Short.MAX_VALUE)));

        pnlPrincipal.add(pnlLogoSamarco);
        pnlLogoSamarco.setBounds(1640, 10, 210, 60);

        pnlInterfaceComandosSlider.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pnlInterfaceComandosSliderLayout = new javax.swing.GroupLayout(pnlInterfaceComandosSlider);
        pnlInterfaceComandosSlider.setLayout(pnlInterfaceComandosSliderLayout);
        pnlInterfaceComandosSliderLayout.setHorizontalGroup(pnlInterfaceComandosSliderLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 196, Short.MAX_VALUE));
        pnlInterfaceComandosSliderLayout.setVerticalGroup(pnlInterfaceComandosSliderLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 59, Short.MAX_VALUE));

        pnlPrincipal.add(pnlInterfaceComandosSlider);
        pnlInterfaceComandosSlider.setBounds(620, 830, 200, 63);

        pnlusuario.setLayout(null);

        lbTextoUsuario.setFont(new java.awt.Font("Verdana", 0, 11));
        lbTextoUsuario.setForeground(new java.awt.Color(104, 104, 104));
        lbTextoUsuario.setText(PropertiesUtil.getMessage("label.usuario.logado"));
        pnlusuario.add(lbTextoUsuario);
        lbTextoUsuario.setBounds(0, 20, 122, 15);

        lbNomeUsuario.setFont(new java.awt.Font("Verdana", 1, 11));
        lbNomeUsuario.setForeground(new java.awt.Color(104, 104, 104));
        pnlusuario.add(lbNomeUsuario);
        lbNomeUsuario.setBounds(0, 40, 110, 20);

        pnlPrincipal.add(pnlusuario);
        pnlusuario.setBounds(1500, 10, 130, 60);

        scrFramePrincipal.setViewportView(pnlPrincipal);

        getContentPane().add(scrFramePrincipal, java.awt.BorderLayout.CENTER);

       
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Metodo auxiliar que busca dentro das interfaces de patio a interfaceBaliza de
     * acordo com a baliza e o patio passado por parametros
     *
     * @param baliza
     * @param patio
     * @return InterfaceBaliza - a interfaceBaliza localizada
     */
    public InterfaceBaliza localizarInterfaceBalizaDoPatio(Baliza baliza, Patio patio) {
        InterfaceBaliza interfaceBalizaEncontrada = null;
        for (InterfacePatio interfacePatio : interfaceDSP.getListaDePatios()) {
            if (interfacePatio.getPatioVisualizado().getMetaPatio().equals(patio.getMetaPatio())) {
                for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas()) {
                    if (interfaceBaliza.getBalizaVisualizada().getMetaBaliza().equals(baliza.getMetaBaliza())) {
                        interfaceBalizaEncontrada = interfaceBaliza;
                        break;
                    }
                }
                if (interfaceBalizaEncontrada != null) {
                    break;
                }
            }
        }
        return interfaceBalizaEncontrada;
    }

    /** monta uma tabela com os indicadores de situacao planejada ou realizada para cada ponto do slider */
    private Hashtable<Integer, Component> montaDicionarioSlider() throws ValidacaoCampoException {

        Hashtable<Integer, Component> mapaDicionario = new Hashtable<Integer, Component>();
        int i = 1;
        for (SituacaoPatio sitPatio : this.controladorInterfaceInicial.getPlanejamento().obterSituacoesDePatio()) {
            JLabel labelExibicao = new JLabel();
            labelExibicao.setText("  ");
            labelExibicao.setOpaque(true);

            // cria uma string com a data atual no formato DD/MM/YYYY HH:MM:SS para ignorar os miliseguindos do new Date()
            String dataAtualFormatada = DSSStockyardTimeUtil.formatarData(new Date(), PropertiesUtil
                            .buscarPropriedade("formato.campo.datahora"));

            // converte a string para um campo date
            Date dataAtual = null;
            try {
                dataAtual = DSSStockyardTimeUtil.criaDataComString(dataAtualFormatada, PropertiesUtil
                                .buscarPropriedade("formato.campo.datahora"));
            } catch (ValidacaoCampoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (dataAtual.compareTo(sitPatio.getDtInicio()) > 0) {
                // se a data atual comparada com a data da situacao exibida for maior que zero
                // significa que a situacao esta no passado e ja foi realizada
                labelExibicao.setBackground(Color.GREEN);
            } else {
                // se a data atual comparada com a data da situacao exibida for menor que zero
                // significa que a situacao esta no futuro e ainda nao foi realizada
                labelExibicao.setBackground(Color.BLUE);
            }

            labelExibicao.setText("  ");
            labelExibicao.setFont(new Font("Dialog", 1, 9));

            mapaDicionario.put(i, labelExibicao);

            //para atualizar e buscar as situcoes pelo indice
            sitPatio.setIndiceSituacaoPatio(i);

            i++;
        }
        return mapaDicionario;
    }

    /**
    * monta desenho da escala para cada tipo de item de controle
    */
    private void montaEscalaMeta() {
        IControladorModelo controladorModelo = null;
        try {
            //Darley RMI OFF
            //             controladorModelo = lookUpModelo();
            controladorModelo = new ControladorModelo();
            List<TipoItemDeControle> listaTipoItemControle = controladorModelo.buscarTiposItemControle();
            DefinePropriedadesFixas definePropriedadesFixas = new DefinePropriedadesFixas();
            definePropriedadesFixas.criaPainelEscalaMetas(listaTipoItemControle);

        } catch (ErroSistemicoException ex) {
            Logger.getLogger(InterfaceInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            controladorModelo = null;
        }
    }

    /**
      * @param situacaoPatioExibida
      */
    private void montaInterfaceAnotacao(SituacaoPatio situacaoPatioExibida) {
        List<InterfaceAnotacao> listaAnotacao = new ArrayList<InterfaceAnotacao>();
        interfaceDSP.setListaAnotacoes(null);
        if (situacaoPatioExibida.getPlanta().getAnotacoes(situacaoPatioExibida.getDtInicio()) != null) {
            for (Anotacao anotacao : situacaoPatioExibida.getPlanta().getAnotacoes(situacaoPatioExibida.getDtInicio())) {
                InterfaceAnotacao interfaceAnotacao = new InterfaceAnotacao(anotacao.getEixoX(), anotacao.getEixoY());
                interfaceAnotacao.setAnotacaoVisualizada(anotacao);
                interfaceAnotacao.setControladorDSP(interfaceDSP.getControladorDSP());

                listaAnotacao.add(interfaceAnotacao);
            }
            // adiciona a lista de interfaces de anotacao na interface DSP
            interfaceDSP.setListaAnotacoes(listaAnotacao);

            //adicionando as interfacesAnotacao no DSP
            for (InterfaceAnotacao interfaceAnotacao : listaAnotacao) {
                pnlInterfaceDSP.add(interfaceAnotacao);
            }
        }
    }

    /**
     * Metodo auxiliar que monta a interface de Blendagem da tela principal
     */
    private void montaInterfaceBlendagem() {
        interfaceBlendagem = new InterfaceBlendagem(controladorInterfaceInicial);
        interfaceBlendagem.setInterfaceInicial(this);
        interfaceBlendagem.limparVisualizacao();
        ((ControladorInterfaceBlendagem) interfaceBlendagem.getIControladorInterfaceBlendagem())
                        .setInterfaceBlendagem(interfaceBlendagem);
        controladorInterfaceInicial.setInterfaceBlendagem(interfaceBlendagem.getIControladorInterfaceBlendagem());
    }

    /**
     * Método auxiliar que montará os itens existentes na interface de comandos.
     */
    private void montaInterfaceComandos() {
        interfaceComandos = new InterfaceComandos(this, controladorInterfaceInicial);
        //  interfaceComandos.getControladorInterfaceComandos().setInterfaceInicial(controladorInterfaceInicial);
        controladorInterfaceInicial.setInterfaceComandos(interfaceComandos.getControladorInterfaceComandos());
        pnlInterfaceComandos.add(interfaceComandos);
    }

    /**
     * Método auxiliar que montará os itens existentes na interface de comandos, para a navegacao no slider.ai
     */
    private void montaInterfaceComandosDoSlider() {
        interfaceComandosSlider = new InterfaceComandosSlider();
        interfaceComandosSlider.setInterfaceInicial(this);
        interfaceComandosSlider.setControladorInterfaceComandos(this.getInterfaceComandos()
                        .getControladorInterfaceComandos());

        pnlInterfaceComandosSlider.add(interfaceComandosSlider);
    }

    /**
     * Metodo auxiliar que monta a interface que identifica os produtos das pilhas pelas cores
     */
    private void montaInterfaceCoresProdutos() {
        IControladorModelo controladorModelo = null;
        try {
            //      controladorModelo = lookUpModelo();
            controladorModelo = new ControladorModelo();
            List<TipoProduto> listaTiposProduto = controladorModelo.buscarTiposProduto(new TipoProduto());
            BufferedImage imagemProduto = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem),
                            "bkg_produtos.png");
            //         BufferedImage imagemTriangulo = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem), "triangulo-legenda-produto.png");
            Color cor;

            for (TipoProduto tipoProduto : listaTiposProduto) {
                BufferedImage imagemTriangulo = ImageHandler.carregarImagem(PropertiesUtil
                                .buscarPropriedade(diretorioImagem), "triangulo-legenda-produto.png");
                RepresentacaoGrafica pnlIdentificacaoTipoProduto = new RepresentacaoGrafica(imagemProduto);
                pnlIdentificacaoTipoProduto.setDimensaoImagem(new Rectangle(75, 33));
                pnlIdentificacaoTipoProduto.setBounds(new Rectangle(75, 33));
                pnlIdentificacaoTipoProduto.setMinimumSize(new Dimension(75, 33));
                pnlIdentificacaoTipoProduto.setPreferredSize(new Dimension(75, 33));

                pnlIdentificacaoTipoProduto.setLayout(null);

                //label com a descricao do tipo de produto
                JLabel lblDescricao = new JLabel();
                if (!tipoProduto.getCodigoFamiliaTipoProduto().equalsIgnoreCase("-")) {
                    lblDescricao.setText(tipoProduto.getCodigoFamiliaTipoProduto() + " - "
                                    + tipoProduto.getCodigoTipoProduto());
                } else {
                    lblDescricao.setText(tipoProduto.getCodigoTipoProduto());
                }
                lblDescricao.setToolTipText(tipoProduto.getDescricaoTipoProduto());
                lblDescricao.setFont(new Font("Arial", Font.PLAIN, 9));
                lblDescricao.setBounds(5, 5, 70, 9);

                pnlIdentificacaoTipoProduto.add(lblDescricao);

                String[] rgb = tipoProduto.getCorIdentificacao().split(",");
                RepresentacaoGrafica pnlCorIndentificacao = new RepresentacaoGrafica(imagemTriangulo);
                cor = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                pnlCorIndentificacao.setBackground(Color.WHITE);
                pnlCorIndentificacao.setDimensaoImagem(new Rectangle(25, 13));

                pintaFundoImagemLegendaProduto(pnlCorIndentificacao.getImagemDSP(), cor.getRGB());

                pnlIdentificacaoTipoProduto.add(pnlCorIndentificacao);
                pnlCorIndentificacao.setBounds((pnlIdentificacaoTipoProduto.getWidth() / 2)
                                - pnlCorIndentificacao.getImagemDSP().getWidth() + 10, 15, 26, 14);

                //adicionando o painel de cores de cada tipo de produto
                pnlCoresProdutos.add(pnlIdentificacaoTipoProduto);
            }
        } catch (ErroSistemicoException erroSis) {
            erroSis.printStackTrace();
        }
        finally {
            controladorModelo = null;
        }
    }

    /**
     * Metodo auxiliar que monta a interface de correias do DSP de acordo com a
     * situacao de patio recebida como parametro.
     * @param situacaoPatioSelecionada
     */
    private void montaInterfaceCorreia(SituacaoPatio situacaoPatioSelecionada) {

        List<InterfaceCorreia> listaInterfacesCorreia = new ArrayList<InterfaceCorreia>();

        interfaceDSP.setListaCorreias(null);
        // percorre a lista de correias da planta
        for (Correia correiaPatio : situacaoPatioSelecionada.getPlanta().getListaCorreias(
                        situacaoPatioSelecionada.getDtInicio())) {
            // cria uma nova interface de correia
            InterfaceCorreia interfaceCorreia = new InterfaceCorreia();
            interfaceCorreia.setHoraSituacao(situacaoPatioSelecionada.getDtInicio());
            interfaceCorreia.setCorreiaVisualizada(correiaPatio);
            interfaceCorreia.setInterfaceDSP(interfaceDSP);
            interfaceCorreia.setControladorDSP(interfaceDSP.getControladorDSP());

            // monta a interface das maquinas que pertencem a correia
            montaInterfaceMaquinasDaCorreia(interfaceCorreia);
            // adiciona a interfaceCorreia na lista
            listaInterfacesCorreia.add(interfaceCorreia);
        }

        // adiciona as correias da planta no DSP
        interfaceDSP.setListaCorreias(listaInterfacesCorreia);
        for (InterfaceCorreia iCorreia : listaInterfacesCorreia) {
            pnlInterfaceDSP.add(iCorreia);
            pnlInterfaceDSP.setComponentZOrder(iCorreia, 1);
        }

        // adiciona as maquinas das correias da planta no DSP
        for (InterfaceCorreia iCorreia : listaInterfacesCorreia) {
            // adiciona a lista de máquinas
            if (iCorreia.getListaDeMaquinas() != null) {
                for (InterfaceMaquinaDoPatio itMaq : iCorreia.getListaDeMaquinas()) {
                    pnlInterfaceDSP.add(itMaq);
                    pnlInterfaceDSP.setComponentZOrder(itMaq, 0);
                }
            }
        }
    }

    /**
     * monta a interface com as informacoes da campanha
     */
    private void montaInterfaceExibicaoCampanhas(SituacaoPatio situacaoPatioExibida) {
        pnlListaCampanhas.removeAll();
        InformacoesCampanhaPnl informacoesCampanha = new InformacoesCampanhaPnl(this.controladorInterfaceInicial
                        .getPlanejamento().obterSituacoesDePatio(), situacaoPatioExibida, interfaceDSP);
        pnlListaCampanhas.add(informacoesCampanha);
    }

    /**
     * Metodo auxiliar que monta as interfaces das maquinas que pertencem a interfaceCorreia
     * recebida como parametros
     *
     * @param interfaceCorreia
     */
    private void montaInterfaceMaquinasDaCorreia(InterfaceCorreia interfaceCorreia) {

        // verifica se existe alguma máquina na correia
        if (interfaceCorreia.getCorreiaVisualizada().getListaDeMaquinas(interfaceCorreia.getHoraSituacao()) != null) {
            // percorre a lista de máquinas da correia
            List<InterfaceMaquinaDoPatio> listaInterfaceMaquina = new ArrayList<InterfaceMaquinaDoPatio>();
            for (MaquinaDoPatio maquina : interfaceCorreia.getCorreiaVisualizada().getListaDeMaquinas(
                            interfaceCorreia.getHoraSituacao())) {
                // cria uma interface máquina
                InterfaceMaquinaDoPatio interfaceMaquina = new InterfaceMaquinaDoPatio();
                interfaceMaquina.setMaquinaDoPatioVisualizada(maquina);
                interfaceMaquina.setCorreia(interfaceCorreia);
                interfaceMaquina.setPosicao(localizarInterfaceBalizaDoPatio(maquina.getPosicao(), maquina.getPosicao()
                                .getPatio()));
                interfaceMaquina.setControladorDSP(interfaceDSP.getControladorDSP());
                listaInterfaceMaquina.add(interfaceMaquina);
            }
            // adiciona a lista de interfaceMaquina na interface correia
            interfaceCorreia.setListaDeInterfaceMaquinas(listaInterfaceMaquina);
        }
    }

    /**
     * Metodo auxiliar que monta a interface de Navegacao da tela principal
     */
    private void montaInterfaceNavegacao() {
        try {
            IControladorPlanejamento controlador = this.controladorInterfaceInicial.getPlanejamento();
            if (interfaceNavegacaoEventos == null) {
                interfaceNavegacaoEventos = new InterfaceNavegacaoEventos();
                interfaceNavegacaoEventos
                                .setControladorInterfaceNavegacaoEventos(new ControladorInterfaceNavegacaoEventos());
                interfaceNavegacaoEventos.getControladorInterfaceNavegacaoEventos().setInterfaceInicial(
                                controladorInterfaceInicial);
                interfaceNavegacaoEventos.getControladorInterfaceNavegacaoEventos().setInterfaceNavegacaoEventos(
                                interfaceNavegacaoEventos);
                interfaceNavegacaoEventos.setInterfaceInicial(this);
            }
            sliderNavegacao.setMaximum(controlador.obterNumeroDeSituacoes());

            sliderNavegacao.setLabelTable(montaDicionarioSlider());

            // se o maximo for 0 significa que soh existe 1 situacao de patio para ser exibida,
            // entao o indice deve ser 0
            int indice = indiceSituacaoPatioSelecionada == null ? 0 : indiceSituacaoPatioSelecionada;
            if (sliderNavegacao.getMaximum() == 0) {
                if (indice > 0) {
                    interfaceNavegacaoEventos.setIndiceSituacaoSelecionada(indice - 1);
                } else {
                    interfaceNavegacaoEventos.setIndiceSituacaoSelecionada(indice);
                }
            } else {
                interfaceNavegacaoEventos.setIndiceSituacaoSelecionada(indice);
            }

            controladorInterfaceInicial.setInterfaceNavegacaoEventos(interfaceNavegacaoEventos
                            .getControladorInterfaceNavegacaoEventos());
        } catch (ValidacaoCampoException vcEx) {
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            this.ativarMensagem(interfaceMensagem);
        }
    }

    /**
     * Metodo auxiliar que monta a interface de patio do DSP de acordo com a
     * situacao de patio recebida como parametro.
     * @param situacaoPatioSelecionada
     */
    private void montaInterfacePatio(SituacaoPatio situacaoPatioSelecionada) {

        // cria uma lista de interfaces de patio
        List<InterfacePatio> listaInterfacePatio = new ArrayList<InterfacePatio>();
        interfaceDSP.setListaDePatios(null);
        interfaceDSP.getControladorDSP().setListaDePatios(null);
        InterfacePatio interfacePatio;
        List<Pilha> pilhas = situacaoPatioSelecionada.getListaDePilhasNosPatios(situacaoPatioSelecionada.getDtInicio());
        for (Patio patio : situacaoPatioSelecionada.getPlanta().getListaPatios(situacaoPatioSelecionada.getDtInicio())) {

            interfacePatio = new InterfacePatio(situacaoPatioSelecionada.getDtInicio());

            interfacePatio.setInterfaceDSP(interfaceDSP);
            interfacePatio.setPatioVisualizado(patio);
            interfacePatio.setControladorDSP(interfaceDSP.getControladorDSP());
            interfacePatio.montaListaInterfacesBaliza();
            interfaceDSP.getControladorDSP().setListaDePatios(null);
            List<InterfacePilha> listaInterfacePilhaDoPatio = new ArrayList<InterfacePilha>();
            for (Pilha pilha : pilhas) {
                if (pilha.verificarPatioDaPilha(patio)) {

                    InterfacePilha interfacePilha = new InterfacePilha();
                    interfacePilha.setPilhaVisualizada(pilha);

                    List<InterfaceBaliza> listaInterfaceBaliza = new ArrayList<InterfaceBaliza>();
                    for (Baliza baliza : pilha.getListaDeBalizas()) {
                        InterfaceBaliza interfaceBaliza = interfacePatio.getMapaInterfaceBaliza().get(baliza.getNumero());
                        interfaceBaliza.setControladorEventoMouseBaliza(interfacePatio.getControladorEventoMouseBaliza());
                        listaInterfaceBaliza.add(interfaceBaliza);
                    }

                    interfacePilha.setListaDeBalizas(listaInterfaceBaliza);
                    interfacePilha.setInterfaceDSP(interfaceDSP);

                    listaInterfacePilhaDoPatio.add(interfacePilha);
                }
            }

            interfacePatio.setListaDePilhas(listaInterfacePilhaDoPatio);
            listaInterfacePatio.add(interfacePatio);

        }

        // seta a lista de patios no controlador do DSP
        interfaceDSP.getControladorDSP().setListaDePatios(listaInterfacePatio);

        // adiciona a lista de interfaces de patio na interface DSP
        interfaceDSP.setListaDePatios(listaInterfacePatio);
        for (InterfacePatio iPatio : interfaceDSP.getListaDePatios()) {
            // adiciona as interfaces de patio na tela
            pnlInterfaceDSP.add(iPatio);
        }

    }

    private void montaInterfacePier(SituacaoPatio situacaoPatioSelecionada) {
        pnlInterfacePierOeste.removeAll();

        listaInterfacePiers.clear();
        for (Pier pier : situacaoPatioSelecionada.getPlanta().getListaPiers(situacaoPatioSelecionada.getDtInicio())) {

            InterfacePier interfacePier = new InterfacePier(interfaceDSP.getControladorDSP(), situacaoPatioSelecionada
                            .getDtInicio());

            interfacePier.setPierVisualizado(pier);
            interfacePier.setPnlInterfacePier(pnlInterfacePierOeste);
            interfacePier.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaNavios);

            listaInterfacePiers.add(interfacePier);
        }
        interfaceFilaDeNavios.setListaDePiers(listaInterfacePiers);

        for (InterfacePier iPier : listaInterfacePiers) {
            // adiciona as interfaces de pier na tela
            pnlInterfacePierOeste.add(iPier);
        }

        //darley
        pnlInformacoesBercoOeste.repaint();
        pnlInterfaceInformacaoBercoLeste.repaint();
        pnlInterfacePierOeste.repaint();
    }

    /**
     * Metodo auxiliar que monta a interface de usina do DSP de acordo com a
     * situacao de patio recebida como parametro.
     * @param situacaoPatioSelecionada
     */
    private void montaInterfaceUsinas(SituacaoPatio situacaoPatioSelecionada) {

        List<InterfaceUsina> listaInterfaceUsina = new ArrayList<InterfaceUsina>();

        Map<MetaFiltragem, Usina> mapFiltragensSelecionaveis = new HashMap<MetaFiltragem, Usina>();
        List<Usina> usinaList = situacaoPatioSelecionada.getPlanta().getListaUsinas(situacaoPatioSelecionada.getDtInicio());
        Collections.sort(usinaList, new ComparadorUsinas());
        for (Usina usina : usinaList) {
            // cria uma nova interface usina
            InterfaceUsina interfaceUsina = new InterfaceUsina(situacaoPatioSelecionada.getDtInicio());

            interfaceUsina.setInterfaceDSP(interfaceDSP);
            interfaceUsina.setUsinaVisualizada(usina);
            interfaceUsina.setControladorDSP(interfaceDSP.getControladorDSP());
            // adiciona a interface usina na lista
            listaInterfaceUsina.add(interfaceUsina);
            MetaFiltragem filtragem = interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem();
            if (filtragem != null) {
                if (mapFiltragensSelecionaveis.get(filtragem) == null) {
                    mapFiltragensSelecionaveis.put(filtragem, usina);
                }
            }
        }

        Set<MetaFiltragem> s = mapFiltragensSelecionaveis.keySet();
        for (Iterator<MetaFiltragem> it = s.iterator(); it.hasNext();) {
            MetaFiltragem meta = (MetaFiltragem) it.next();
            Usina usina = mapFiltragensSelecionaveis.get(meta);
            InterfaceFiltragem interfaceUsina = new InterfaceFiltragem(situacaoPatioSelecionada.getDtInicio());

            interfaceUsina.setInterfaceDSP(interfaceDSP);
            interfaceUsina.setUsinaVisualizada(usina);
            interfaceUsina.setFiltragemVisualizada(meta.retornaStatusHorario(situacaoPatioSelecionada.getDtInicio()));
            interfaceUsina.setControladorDSP(interfaceDSP.getControladorDSP());
            // adiciona a interface usina na lista
            listaInterfaceUsina.add(interfaceUsina);
        }

        interfaceDSP.setListaUsinas(listaInterfaceUsina);

        for (InterfaceUsina interfaceUsina : interfaceDSP.getListaUsinas()) {
            pnlInterfaceUsinas.add(interfaceUsina);
        }

    }

    /**
     * pinta a imagem do triangulo da legenda com as cores dos produtos da carga
     * @param imagemTriangulo
     *
     *    .       .    .
     *   ..       .    .
     *  ....  =  ..  + ..
     * ......   ...    ...
     */
    private void pintaFundoImagemLegendaProduto(BufferedImage imagemTriangulo, int rgb) {
        int largura = imagemTriangulo.getWidth();
        int altura = imagemTriangulo.getHeight();
        int meioTriangulo = largura / 2;
        int colunas = 0;

        //pinta a primeira metado do triandulo de baixo para cima, e da direita para esquerda
        for (int x = altura; x > 0; x--) {//movimenta sobre as linhas
            for (int y = meioTriangulo; y > colunas; y--) {//movimento sobre as colunas de cada linha
                imagemTriangulo.setRGB(x, y, rgb);
            }
            colunas += 1;
        }

        //pinta a segunda metade do triangulo de baixo para cima, e da esquerda paa direita
        for (int y = altura - 1; y > 0; y--) {
            for (int x = meioTriangulo; x < largura - 1; x++) {
                imagemTriangulo.setRGB(x, y, rgb);
            }
            largura -= 1;
        }
    }

    private void sliderNavegacaoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderNavegacaoStateChanged
        JSlider source = (JSlider) evt.getSource();
        if (!source.getValueIsAdjusting()) {
            //Subtrai-se 1 da situacao selecionada para começar do 0, pois o slider inicia-se em 1 e a lista inicia-se em 0
            Integer novaSituacaoPatio = new Integer((int) source.getValue() - 1);
            // verifica se a nova situacao selecionada eh diferente da situacao atual
            if (indiceSituacaoPatioSelecionada == null) {
                if (source.getMaximum() == source.getValue()) {
                    this.indiceSituacaoPatioSelecionada = novaSituacaoPatio;
                    
                    montaInterfaceDSP();
                }
            } else {
                if (!novaSituacaoPatio.equals(indiceSituacaoPatioSelecionada) || movimentacaoViaBotoesDeNavegacao) {
                    this.indiceSituacaoPatioSelecionada = novaSituacaoPatio;
                    montaInterfaceDSP();
                    movimentacaoViaBotoesDeNavegacao = Boolean.FALSE;
                }
            }
        }
    }//GEN-LAST:event_sliderNavegacaoStateChanged

    /**
     * apaga situacoes do plano a partir da selecionda
     */
    public void apagaSituacoesDoPlanoEmpilhamentoRecuperacaoAPartirDaSelecionda() {
        throw new UnsupportedOperationException();
    }

    /** Ativa a exibicao da mensagem em uma nova tarefa para não interromper
     * o processamento.
     * @param interfaceMensagem
     */
    public void ativarMensagem(InterfaceMensagem interfaceMensagem) {
        if (processamentoMensagens == null) {
            processamentoMensagens = new ThreadMensagem(this, interfaceMensagem);
            processamentoMensagens.start();
        } else {
            if (!processamentoMensagens.isAlive()) {
                processamentoMensagens = new ThreadMensagem(this, interfaceMensagem);
                processamentoMensagens.start();
            }
        }
    }

    /**
     * Modifica o status da coluna atializacaoCampoIntegracao, da tabela integracaoParametros, modificando seu status para FALSE
     */
    public void atualizaDadosIntegracaoParametrosCRM() {
        IControladorModelo controladorModelo = null;
        try {
            //atualiza coluna dados MES
            controladorModelo = lookUpModelo();

            //atualiza coluna dados CRM
            IntegracaoParametros integracao = controladorModelo.buscarParametroSistema(Long.parseLong(PropertiesUtil
                            .buscarPropriedade("codigo.sistema.externo.CRM")));
            if (integracao != null && integracao.getAtualizacaoCampoIntegracao().equalsIgnoreCase("TRUE")) {
                integracao.setAtualizacaoCampoIntegracao("FALSE");
                //atualiza o dado na base
                controladorModelo.atualizaParametrosSistema(integracao);
            }

            //atualiza coluna dados SAP
            integracao = controladorModelo.buscarParametroSistema(Long.parseLong(PropertiesUtil
                            .buscarPropriedade("codigo.sistema.externo.SAP")));
            if (integracao != null && integracao.getAtualizacaoCampoIntegracao().equalsIgnoreCase("TRUE")) {
                integracao.setAtualizacaoCampoIntegracao("FALSE");
                //atualiza o dado na base
                controladorModelo.atualizaParametrosSistema(integracao);
            }

        } catch (ErroSistemicoException ex) {
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            this.ativarMensagem(interfaceMensagem);
        }
        finally {
            controladorModelo = null;
        }

    }

    public void atualizaDadosIntegracaoParametrosMES() {
        IControladorModelo controladorModelo = null;
        try {
            //atualiza coluna dados MES
            controladorModelo = lookUpModelo();
            //controladorModelo = new ControladorModelo();
            IntegracaoParametros integracao = controladorModelo.buscarParametroSistema(Long.parseLong(PropertiesUtil
                            .buscarPropriedade("codigo.sistema.externo.MES")));
            if (integracao != null && integracao.getAtualizacaoCampoIntegracao().equalsIgnoreCase("TRUE")) {
                integracao.setAtualizacaoCampoIntegracao("FALSE");
                //atualiza o dado na base
               //controladorModelo.atualizaParametrosSistema(integracao);
            }
            //atualiza coluna dados CRM

            integracao = controladorModelo.buscarParametroSistema(Long.parseLong(PropertiesUtil
                            .buscarPropriedade("codigo.sistema.externo.MES_RITMO")));
            if (integracao != null && integracao.getAtualizacaoCampoIntegracao().equalsIgnoreCase("TRUE")) {
                integracao.setAtualizacaoCampoIntegracao("FALSE");
                //atualiza o dado na base
                //controladorModelo.atualizaParametrosSistema(integracao);
            }
        } catch (ErroSistemicoException ex) {
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            this.ativarMensagem(interfaceMensagem);
        }
        finally {
            controladorModelo = null;
        }
    }

    public void atualizarDSP() {
        montaInterfaceNavegacao();
    }

    /**
     * atualizar o plano visualizado com o novo plano oficial pegado do servidor
     * @param novoPlanoOficialVisualizado
     */
    public void atualizarPlanoVisualizado(PlanoEmpilhamentoRecuperacao novoPlanoOficialVisualizado) {
        throw new UnsupportedOperationException();
    }

    /**
     * Atualiza as informacoes na tabela de lista de produtos selecionados
     * @param mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem
     * @throws ErroSistemicoException
     */
    public void atualizaTabelaInformacoesBalizasSelecionadas(
                    HashMap<TipoProduto, Double> mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem)
                    throws ErroSistemicoException {
        if (mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem != null) {
            Vector vInformacoesProdutosSelecionados = new Vector();

            Iterator it = mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.keySet().iterator();

            // Iteramos nos tipos de produtos da blendagem para encontrar aquele que tem a maior quantidade
            Double totalSelecionado = 0.0d;
            while (it.hasNext()) {
                TipoProduto tipoProdutoAnalisado = (TipoProduto) it.next();
                totalSelecionado += mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.get(tipoProdutoAnalisado);
            }
            Iterator it2 = mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.keySet().iterator();

            while (it2.hasNext()) {
                TipoProduto tipoProdutoAnalisado = (TipoProduto) it2.next();
                Double quantidadeDoProdutoAnalisado = mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem
                                .get(tipoProdutoAnalisado);

                Object[] dados = new Object[3];

                dados[COL_PRODUTO_SELECIONADO] = tipoProdutoAnalisado.toString();
                dados[COL_QUANTIDADE_SELECIONADA] = DSSStockyardFuncoesNumeros.getQtdeFormatada(
                                quantidadeDoProdutoAnalisado, 2);
                if (quantidadeDoProdutoAnalisado == 0 && totalSelecionado == 0) {
                    dados[COL_PORCENTAGEM] = "0,0%";
                } else {
                    dados[COL_PORCENTAGEM] = DSSStockyardFuncoesNumeros.getValorFormatado(quantidadeDoProdutoAnalisado
                                    / totalSelecionado * 100, 2)
                                    + "%";
                }

                vInformacoesProdutosSelecionados.add(new Vector(Arrays.asList(dados)));

            }
            CFlexStockyardFuncoesTabela.setInformacoesTabela(tabelaProdutoSelecionadoQuantidade,
                            vInformacoesProdutosSelecionados, listaColunaInformacoes);
        }
    }

    /**
     * Cria a lista de colunas para exibicao das informacoes dos produtos selecionadas
     * @throws ErroSistemicoException
     */
    public void criaColunasInformacoesBalizasSelecionadas() throws ErroSistemicoException {
        listaColunaInformacoes = new ArrayList<ColunaTabela>();

        ColunaTabela colInfo;
        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(213);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo(PropertiesUtil.getMessage("mensagem.produto.tipo"));
        listaColunaInformacoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(150);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.quantidade"));
        listaColunaInformacoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(70);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("%");
        listaColunaInformacoes.add(colInfo);

        limpaInformacoesDaTabela();

    }

    /** Desativa a exibicao da mensagem de processamento
     * @param interfaceMensagem
     */
    public void desativarMensagem() {
        pnlMensagens.setVisible(false);
        if (processamentoMensagens != null) {
            processamentoMensagens.interrupt();
            processamentoMensagens = null;
        }
    }

    public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaNavios() {
        return controladorInterfaceFilaNavios;
    }

    public ControladorInterfaceInicial getControladorInterfaceInicial() {
        return controladorInterfaceInicial;
    }

    public Boolean getExecutandoIntegracao() {
        return executandoIntegracao;
    }

    /**
     * @return the indiceSituacaoPatioSelecionada
     */
    public Integer getIndiceSituacaoPatioSelecionada() {
        return indiceSituacaoPatioSelecionada;
    }

    public InterfaceBlendagem getInterfaceBlendagem() {
        return interfaceBlendagem;
    }

    public InterfaceComandos getInterfaceComandos() {
        return interfaceComandos;
    }

    public InterfaceDSP getInterfaceDSP() {
        return interfaceDSP;
    }

    public InterfaceFilaDeNavios getInterfaceFilaDeNavios() {
        return interfaceFilaDeNavios;
    }

    public InterfaceNavegacaoEventos getInterfaceNavegacaoEventos() {
        return interfaceNavegacaoEventos;
    }

    public JLabel getLblGrpBalizaCliente() {
        return lblGrpBalizaCliente;
    }

    /**
     * @return the lblInformacaoQualidadeMetaProducao
     */
    public javax.swing.JLabel getLblInformacaoQualidadeMetaProducao() {
        return lblInformacaoQualidadeMetaProducao;
    }

    /**
     * @return the lblInformacaoQualidade
     */
    public javax.swing.JLabel getLblInformacaoQualidadeOrientacaoEmbarque() {
        return lblInformacaoQualidadeOrientacaoEmbarque;
    }

    /**
     * @return the lblInformacaoQualidadePnlMetaEmbarque
     */
    public javax.swing.JLabel getLblInformacaoQualidadePnlMetaEmbarque() {
        return lblInformacaoQualidadePnlMetaEmbarque;
    }

    public JLabel getLblMensagem() {
        return lblMensagem;
    }

    public List<Cliente> getListaDeClientes() throws ErroSistemicoException {
        //     IControladorModelo controladorModelo = lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        List<Cliente> result = controladorModelo.buscaClientes(new Cliente());
        controladorModelo = null;
        return result;
    }

    /**
     * @return the listaInterfacePiers
     */
    public List<InterfacePier> getListaInterfacePiers() {
        return listaInterfacePiers;
    }

    public List<TipoItemDeControle> getListaTiposItemDeControle() throws ErroSistemicoException {
        //Darley retirando as chamadas remotas
        //     IControladorModelo controladorModelo = lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        List<TipoItemDeControle> result = controladorModelo.buscarTiposItemDeControle(new TipoItemDeControle());
        controladorModelo = null;
        return result;
    }

    public List<TipoProduto> getListaTiposProduto() throws ErroSistemicoException {
        //Darley retirando as chamadas remotas
        //     IControladorModelo controladorModelo = lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        List<TipoProduto> result = controladorModelo.buscarTiposProduto(new TipoProduto());
        controladorModelo = null;
        return result;
    }

    /**
     *
     * @return o modo de operação atual do sistema
     */
    public ModoDeOperacaoEnum getModoDeOperacao() {
        return modoDeOperacao;
    }

    public Boolean getMovimentacaoViaBotoesDeNavegacao() {
        return movimentacaoViaBotoesDeNavegacao;
    }

    public JPanel getPnlInformacoesBercoOeste() {
        return pnlInformacoesBercoOeste;
    }

    public JPanel getPnlInterfaceInformacaoBercoLeste() {
        return pnlInterfaceInformacaoBercoLeste;
    }

    /**
     * @return the pnlItensControleMetaEmbarque
     */
    public javax.swing.JPanel getPnlItensControleMetaEmbarque() {
        return pnlItensControleMetaEmbarque;
    }

    /**
     * @return the pnlItensControleMetaProducao
     */
    public javax.swing.JPanel getPnlItensControleMetaProducao() {
        return pnlItensControleMetaProducao;
    }

    /**
     * @return the pnlItensControle
     */
    public javax.swing.JPanel getPnlItensControleOrientacaoEmbarque() {
        return pnlItensControleOrientacaoEmbarque;
    }

    public JPanel getPnlMensagens() {
        return pnlMensagens;
    }

    public JPopupMenu getPopMnuPatio() {
        return popMnuPatio;
    }

    /**
     * @return the scrBlendagem
     */
    public javax.swing.JScrollPane getScrBlendagem() {
        return scrBlendagemPnlOrientacaoEmbarque;
    }

    public SituacaoPatio getSituacaoPatioExibida() {
        return situacaoPatioExibida;
    }

    public JSlider getSliderNavegacao() {
        return sliderNavegacao;
    }

    /**
     * @return the tabelaProdutoSelecionadoQuantidade
     */
    public com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable getTabelaProdutoSelecionadoQuantidade() {
        return tabelaProdutoSelecionadoQuantidade;

    }

    public JTabbedPane getTbFilaDeNavios() {
        return tbFilaDeNavios;
    }

    /**
    * Insere uma anotação no patio
    */
    public void inserirAnotacao(int eixoX, int eixoY) throws OperacaoCanceladaPeloUsuarioException {
        interfaceDSP.getControladorDSP().inserirAnotacao(eixoX, eixoY, null);
    }

    /**
     * Limpa as informaÃ§Ãµes da tabela
     * @throws ErroSistemicoException
     */
    public void limpaInformacoesDaTabela() throws ErroSistemicoException {
        Vector vInformacoesProdutosSelecionados = new Vector();

        CFlexStockyardFuncoesTabela.setInformacoesTabela(tabelaProdutoSelecionadoQuantidade,
                        vInformacoesProdutosSelecionados, listaColunaInformacoes);
    }

    /**
     * Método auxiliar que montará os itens existentes em um DSP
     */
    public void montaInterfaceDSP() {
        IControladorPlanejamento controlador = this.controladorInterfaceInicial.getPlanejamento();
        
        // remove todos os componentes inseridos no DSP
        pnlInterfaceDSP.removeAll();
        pnlInterfaceUsinas.removeAll();
        pnlItensControleOrientacaoEmbarque.removeAll();
        pnlItensControleMetaEmbarque.removeAll();
        pnlItensControleMetaProducao.removeAll();
        pnlInterfacePierOeste.removeAll();
        pnlInformacoesBercoOeste.removeAll();
        pnlInterfaceInformacaoBercoLeste.removeAll();

        // atualiza o indice da situacao de patio selecionada na interface de navegacao.
        interfaceNavegacaoEventos.setIndiceSituacaoSelecionada(indiceSituacaoPatioSelecionada);
        // cria uma nova interfaceDSP e adiciona os componentes necessarios para sua exibicao
        if (interfaceDSP == null) {
            interfaceDSP = new InterfaceDSP();
            interfaceDSP.setInterfaceInicial(this);
        }

        interfaceDSP.setComponenteInterfaceDSP(pnlInterfaceDSP);
        interfaceDSP.setComponenteInterfaceUsina(pnlInterfaceUsinas);

        interfaceDSP.getControladorDSP().setInterfaceInicial(controladorInterfaceInicial);
        interfaceDSP.getControladorDSP().setInterfaceDadosEdicao(new InterfaceDadosEdicao());

        // adiciona o controlador do DSP ao controlador da interface inicial
        controladorInterfaceInicial.setDsp(interfaceDSP.getControladorDSP());

        // obtem a situacao de patio selecionada do plano oficial        
        this.situacaoPatioExibida = controlador.buscarSituacaoPatio(indiceSituacaoPatioSelecionada);
        // monta a interface com os patios de acordo com a situacao selecionada

        criaPopMenuPatio();

        //atualiza estados das maquinas
        //interfaceDSP.getControladorDSP().atualizaEstadoMaquinas(situacaoPatioExibida.getDtInicio(), situacaoPatioExibida.getPlanta().getListaMaquinasDoPatio());
        //atualiza os estados das correias
        interfaceDSP.getControladorDSP().atualizaEstadoCorreias(situacaoPatioExibida.getDtInicio(),
                        situacaoPatioExibida.getPlanta().getListaCorreias(situacaoPatioExibida.getDtInicio()));

        //ordena patios para evitar problemas futuros
        List<Patio> patios = situacaoPatioExibida.getPlanta().getListaPatios(situacaoPatioExibida.getDtInicio());

        Collections.sort(patios, comparadorPatio);
        montaInterfacePatio(situacaoPatioExibida);

        //ordena as correias pelos seus numeros antes de inserir as maquinas nelas para não haver problemas de exibição
        List<Correia> correias = situacaoPatioExibida.getPlanta().getListaCorreias(situacaoPatioExibida.getDtInicio());
        Collections.sort(correias, comparadorCorreia);
        // monta a interface com as correias existentes no plano oficial
        montaInterfaceCorreia(situacaoPatioExibida);

        // monta a interface com as usinas existentes no plano oficial
        montaInterfaceUsinas(situacaoPatioExibida);

        //monta interfacefila de navios
        montaInterfaceFilaNavios(true);

        // monta a interface do pier
        montaInterfacePier(situacaoPatioExibida);

        // monta a inteface com a lista de campanhas
        montaInterfaceExibicaoCampanhas(situacaoPatioExibida);

        // remove a blendagem devido a mudanca de situacao de patio
        controladorInterfaceInicial.limparBlendagem();
        //monta anotacao
        montaInterfaceAnotacao(situacaoPatioExibida);

        // desenha o painel com a data e hora da situação de patio exibida
        desenhaDataHoraSituacaoPatioExibida(situacaoPatioExibida);
        //apresenta o usuario logado no sistema
        apresentaUsuarioLogado();

        adicionaEventoMouse();
        this.setModoDeOperacao(ModoDeOperacaoEnum.OPERACAONULA);
        this.getControladorInterfaceInicial().removeSelecoes();

        System.gc();
    }

    /**
     * Metodo auxiliar que monta a interface de fila de navios da planta
     *
     */
    public void montaInterfaceFilaNavios(Boolean buscarFilaNaBase) {
        //listaInterfacePiers.clear();
        interfaceFilaDeNavios = null;
        controladorInterfaceFilaNavios.setInterfaceDadosEdicao(new InterfaceDadosEdicao());
        if (interfaceFilaDeNavios == null) {
            interfaceFilaDeNavios = new InterfaceFilaDeNavios(this.getSituacaoPatioExibida().getDtInicio());
            interfaceFilaDeNavios.setInterfaceInicial(this);
        }

        interfaceFilaDeNavios.setComponenteInterfaceFilaNavios(tbFilaDeNavios);
        interfaceFilaDeNavios.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaNavios);
        interfaceFilaDeNavios.setComponenteInterfacePier(pnlInterfacePierOeste);
        interfaceFilaDeNavios.setListaDePiers(listaInterfacePiers);

        controladorInterfaceFilaNavios.setInterfaceFilaDeNavios(interfaceFilaDeNavios);

        if (interfaceFilaDeNavios.getFilaDeNaviosVisualizada() == null) {
            FilaDeNavios filaAtual = ControladorFilaDeNavios.getInstance().recuperaFila(
                            this.getSituacaoPatioExibida().getDtInicio());
            interfaceFilaDeNavios.setFilaDeNaviosVisualizada(filaAtual);
        }
        interfaceFilaDeNavios.defineAcoesParaFila();
        interfaceFilaDeNavios.desenhaFilaDeNavios();
    }

    /**
     *verifica se na tabela de integracaoParamentros houve alguma atualizacao
     */
    public void observadorIntegracaoParametros() {

        new Thread("Thread verificar status tabela Integracao Parametros") {

            @Override
            public void run() {
                long periodoMinutos = Long.parseLong(PropertiesUtil
                                .buscarPropriedade("valor.periodo.minutos.atualizacao.mespatio"));
                //este processo deve ficar rodando em background e a cada um minuto devera repetir a verificacao

                while (true) {
                    try {
                        Thread.sleep(periodoMinutos * 60 * 1000);
                        //espera por aproximandamente N minutos(entrada de dados) e repete a verificacao
                        //converte periodo passado por parametro, para milisegundos
                        if (executandoIntegracao) {
                            Thread.sleep(periodoMinutos * 60 * 1000);
                            return;
                        }
                        IControladorModelo controladorModelo = lookUpModelo();
                        //busca e verifica informações do sistema MES

                        IntegracaoParametros integracao = controladorModelo.buscarParametroSistema(Long
                                        .parseLong(PropertiesUtil.buscarPropriedade("codigo.sistema.externo.MES")));
                        if (integracao != null && integracao.getAtualizacaoCampoIntegracao().equalsIgnoreCase("TRUE")) {
                            if (getSituacaoPatioExibida() != null
                                            && getSituacaoPatioExibida().getPlanoEmpilhamento() != null
                                            && getSituacaoPatioExibida().getPlanoEmpilhamento().getEhOficial()) {//se o plano não for o Oficial, significa que o plano atual é de usuario e neste caso deve-se bloquear as atualizacoes com os sistemas externos
                                interfaceComandos.habilitarIconeAtualizacaoMES();
                            }
                        }

                        //busca e verifica informações do sistema CRM
                        integracao = controladorModelo.buscarParametroSistema(Long.parseLong(PropertiesUtil
                                        .buscarPropriedade("codigo.sistema.externo.CRM")));
                        if (integracao != null && integracao.getAtualizacaoCampoIntegracao().equalsIgnoreCase("TRUE")) {
                            if (getSituacaoPatioExibida() != null
                                            && getSituacaoPatioExibida().getPlanoEmpilhamento() != null
                                            && getSituacaoPatioExibida().getPlanoEmpilhamento().getEhOficial()) {
                                interfaceComandos.habilitarIconeAtualizacaoCRM();
                            }
                        }
                    } catch (ErroSistemicoException ex) {
                        controladorInterfaceInicial.desativarMensagemProcessamento();
                        InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        controladorInterfaceInicial.ativarMensagem(interfaceMensagem);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(InterfaceInicial.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {

                    }
                }
            }
        }.start();
    }

    /**
     * Pega o timestamp do plano visualizado para saber quais situações são necessárias na atualização
     * @return
     */
    public Date pegarTimeStampPlanoOficialVisualizado() {
        throw new UnsupportedOperationException();
    }

    /**
     * Usado para atualizar a posicao das maquinas quando editadas, pois o repaint() não funciona
     * @param maquina
     */
    public void removeAdicionaComponentInterfaceDSP(Component maquina) {
        pnlInterfaceDSP.remove(maquina);
        pnlInterfaceDSP.add(maquina);
    }

    public void setControladorInterfaceInicial(ControladorInterfaceInicial controladorInterfaceInicial) {
        this.controladorInterfaceInicial = controladorInterfaceInicial;
    }

    public void setExecutandoIntegracao(Boolean executandoIntegracao) {
        this.executandoIntegracao = executandoIntegracao;
    }

    /**
     * @param indiceSituacaoPatioSelecionada the indiceSituacaoPatioSelecionada to set
     */
    public void setIndiceSituacaoPatioSelecionada(Integer indiceSituacaoPatioSelecionada) {
        this.indiceSituacaoPatioSelecionada = indiceSituacaoPatioSelecionada;
    }

    public void setInterfaceBlendagem(InterfaceBlendagem interfaceBlendagem) {
        this.interfaceBlendagem = interfaceBlendagem;
    }

    public void setInterfaceComandos(InterfaceComandos interfaceComandos) {
        this.interfaceComandos = interfaceComandos;
    }

    public void setInterfaceFilaDeNavios(InterfaceFilaDeNavios interfaceFilaDeNavios) {
        this.interfaceFilaDeNavios = interfaceFilaDeNavios;
    }

    /**
     * @param lblInformacaoQualidadeMetaProducao the lblInformacaoQualidadeMetaProducao to set
     */
    public void setLblInformacaoQualidadeMetaProducao(javax.swing.JLabel lblInformacaoQualidadeMetaProducao) {
        this.lblInformacaoQualidadeMetaProducao = lblInformacaoQualidadeMetaProducao;
    }

    /**
     * @param lblInformacaoQualidade the lblInformacaoQualidade to set
     */
    public void setLblInformacaoQualidadeOrientacaoEmbarque(javax.swing.JLabel lblInformacaoQualidade) {
        this.lblInformacaoQualidadeOrientacaoEmbarque = lblInformacaoQualidade;
    }

    /**
     * @param lblInformacaoQualidadePnlMetaEmbarque the lblInformacaoQualidadePnlMetaEmbarque to set
     */
    public void setLblInformacaoQualidadePnlMetaEmbarque(javax.swing.JLabel lblInformacaoQualidadePnlMetaEmbarque) {
        this.lblInformacaoQualidadePnlMetaEmbarque = lblInformacaoQualidadePnlMetaEmbarque;
    }

    public void setModoDeOperacao(ModoDeOperacaoEnum modoDeOperacao) {
        this.modoDeOperacao = modoDeOperacao;
    }

    public void setMovimentacaoViaBotoesDeNavegacao(Boolean movimentacaoViaBotoesDeNavegacao) {
        this.movimentacaoViaBotoesDeNavegacao = movimentacaoViaBotoesDeNavegacao;
    }

    public void setPnlInformacoesBercoOeste(JPanel pnlInformacoesBercoOeste) {
        this.pnlInformacoesBercoOeste = pnlInformacoesBercoOeste;
    }

    public void setPnlInterfaceInformacaoBercoLeste(JPanel pnlInterfaceInformacaoBercoLeste) {
        this.pnlInterfaceInformacaoBercoLeste = pnlInterfaceInformacaoBercoLeste;
    }

    /**
     * @param pnlItensControleMetaEmbarque the pnlItensControleMetaEmbarque to set
     */
    public void setPnlItensControleMetaEmbarque(javax.swing.JPanel pnlItensControleMetaEmbarque) {
        this.pnlItensControleMetaEmbarque = pnlItensControleMetaEmbarque;
    }

    /**
        * @param pnlItensControleMetaProducao the pnlItensControleMetaProducao to set
        */
    public void setPnlItensControleMetaProducao(javax.swing.JPanel pnlItensControleMetaProducao) {
        this.pnlItensControleMetaProducao = pnlItensControleMetaProducao;
    }

    /**
        * @param pnlItensControle the pnlItensControle to set
        */
    public void setPnlItensControleOrientacaoEmbarque(javax.swing.JPanel pnlItensControle) {
        this.pnlItensControleOrientacaoEmbarque = pnlItensControle;
    }

    /**
     * @param scrBlendagem the scrBlendagem to set
     */
    public void setScrBlendagem(javax.swing.JScrollPane scrBlendagem) {
        this.scrBlendagemPnlOrientacaoEmbarque = scrBlendagem;
    }

    public void setSituacaoPatioExibida(SituacaoPatio situacaoPatioExibida) {
        this.situacaoPatioExibida = situacaoPatioExibida;
    }

    /**
        * @param tabelaProdutoSelecionadoQuantidade the tabelaProdutoSelecionadoQuantidade to set
        */
    public void setTabelaProdutoSelecionadoQuantidade(
                    com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tabelaProdutoSelecionadoQuantidade) {
        this.tabelaProdutoSelecionadoQuantidade = tabelaProdutoSelecionadoQuantidade;
    }

    public void verificaGeracaoDeLogBlend(Carga carga) {
        List<String> listaTipoProduto = new ArrayList<String>();
        //monta lista com tipos de produto
        for (int i = 0; i < tabelaProdutoSelecionadoQuantidade.getRowCount(); i++) {
            listaTipoProduto.add(tabelaProdutoSelecionadoQuantidade.getValueAt(i, COL_PRODUTO_SELECIONADO).toString());
        }

        interfaceDSP.getControladorDSP().verificaGerarLogBlend(listaTipoProduto, carga);
    }

    /**
     * verifica se as situacoes do plano  partir da escolhida podem ser apagadas
     * @param indiceInicial
     */
    public void verificaSeSituacoesPodemSerApagadas(Integer indiceInicial) {
        throw new UnsupportedOperationException();
    }

    /**
     * bercosPier
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 28/10/2010
     * @see
     * @param 
     * @return List<MetaBerco>
     */
    public List<MetaBerco> bercosPier() {
        
    	List<MetaBerco> result = new ArrayList<MetaBerco>();
        for (InterfacePier interfacePier : getListaInterfacePiers()) {
            if (interfacePier.getListaDeBercos() != null) {
                for (InterfaceBerco interfaceBerco : interfacePier.getListaDeBercos()) {
                    result.add(interfaceBerco.getBercoVisualizada().getMetaBerco());
                }
            }
        }
        return result;
    }

}
