/*
 * InterfaceAtualizacaoMaquinaEmpilhamento.java
 *
 * Created on 31/03/2009, 18:12:07
 */

package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Font;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.texto.FixedLengthDocument;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.vo.atividades.AtualizarEmpilhamentoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.PilhaExistenteException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeEmpilhamento;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeEmpilhamento;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

@SuppressWarnings("serial")
public class InterfaceAtualizacaoMaquinaEmpilhamento extends javax.swing.JDialog {

    private Map<MetaFiltragem, JCheckBox> mapFiltragensSelecionaveis;
    private Map<MetaFiltragem, Boolean> mapFiltragensSelecionadas;
    private HashMap<MetaFiltragem, HashMap<JCheckBox, JComboBox>> mapFiltragemCombos;

    private HashMap<MetaUsina, HashMap<Integer, Campanha>> mapComboCampanhaUsina;
    private HashMap<MetaFiltragem, HashMap<Integer, Campanha>> mapComboCampanhaFiltragem;

    private HashMap<MetaUsina, JCheckBox> mapUsinasSelecionaveis;
    private HashMap<MetaUsina, HashMap<JCheckBox, JComboBox>> mapUsinasCombos;

    private Map<MetaUsina, Campanha> mapUsinaCampanha;
    private Map<MetaFiltragem, Campanha> mapFiltragemCampanha;

    ControladorDSP controladorDSP;

    Atividade atividadeExecutada;

    MaquinaDoPatio maquinaDoPatioEditada;

    InterfaceMaquinaDoPatio interfaceMaquinaDoPatio;

    /** Controla se a edicao foi cancelada ou nao */
    private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;

    /** Baliza em que a maquina se encontra atualmente */
    Baliza balizaAtual;

    /** Interface onde sao armazenadas as usinas selecionadas e o produto */
    //   ArrayList<InterfaceSelecaoProdutoUsina> listaInterfacesSelecaoProdutoUsina = new ArrayList<InterfaceSelecaoProdutoUsina>();

    /** a interface que contem a informacao da mensagem que sera exibida na tela */
    private InterfaceMensagem interfaceMensagem;

    /** A atividade de atualizacao de empilhamento que esta sendo realizada */
    Atividade atividadeAtualizacaoEmpilhamento;

    private final int INDICE_COMPONENTE_NOMESUINA = 1;

    //   private final int INDICE_COMPONENTE_TAXAOPERACAO = 2;

    private final int INDICE_COMPONENTE_TIPOPRODUTO = 2;

    /** a lista de interface usina selecionada para recuperacao */
    private List<Usina> listaUsinasSelecionada;

    private List<MetaFiltragem> listaFiltragemSelecionada;

    /** guarda informacao se o comportamento da interface sera para finalizacao de atividade*/
    private boolean finalizaAtividade;

    /** data da atividade */
    Date dataHoraOcorrenciaEvento;

    /** Creates new form InterfaceAtualizacaoMaquinaEmpilhamento */
    public InterfaceAtualizacaoMaquinaEmpilhamento(java.awt.Frame parent, boolean modal, ControladorDSP controladorDSP,
                    MaquinaDoPatio maquinaDoPatioEditada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio,
                    boolean finalizaAtividade) throws ErroSistemicoException {
        super(parent, modal);
        initComponents();
        this.controladorDSP = controladorDSP;
        this.maquinaDoPatioEditada = maquinaDoPatioEditada;
        this.interfaceMaquinaDoPatio = interfaceMaquinaDoPatio;
        this.finalizaAtividade = finalizaAtividade;

        //TODO verificar data
        dataHoraOcorrenciaEvento = new Date(System.currentTimeMillis());

        if (finalizaAtividade) {
            habilitaModoFinalizaAtualizacao();
        } else {
            habilitaTodasFuncoes();
        }
        prepaparPainelUsinas();
        prepararPainelFiltragem();
        montaPainelUsinas(interfaceMaquinaDoPatio.getControladorDSP().getInterfaceDSP().getInterfaceInicial()
                        .getSituacaoPatioExibida().getDtInicio());
        montaPainelFiltragem(interfaceMaquinaDoPatio.getControladorDSP().getInterfaceDSP().getInterfaceInicial()
                        .getSituacaoPatioExibida().getDtInicio());
        inicializarAtualizacaoEmpilhamento();
    }

    private void inicializarAtualizacaoEmpilhamento() {
        atividadeExecutada = maquinaDoPatioEditada.getMetaMaquina().existeEmpilhamentoPendente();
        if (atividadeExecutada != null) {
            carregarComboTipoProdutoMudarBaliza();
            SituacaoPatio situacao = controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano()
                            .obterSituacaoPatioFinal();
            // ... caso a lista de atividades campanha nao for vazia trazer as campanhas usadas na recuperacao como selecionadas
            if (atividadeExecutada.getListaDeAtividadesCampanha() != null
                            && !atividadeExecutada.getListaDeAtividadesCampanha().isEmpty()) {
                if (atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas() != null
                                && atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas()
                                                .size() > 0) {

                    for (AtividadeCampanha atividadeCampanha : atividadeExecutada.getListaDeAtividadesCampanha()) {
                        selecionarUsinaPelaAtividade(atividadeCampanha.getCampanha().getUsina(situacao.getDtInicio()),
                                        atividadeCampanha.getTipoProduto());
                    }
                } else if (atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaFiltragens() != null
                                && atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                                                .getListaFiltragens().size() > 0) {
                    recuperaFiltragemSelecionada(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                                    .getListaFiltragens(), atividadeExecutada);
                }
            }
        } else {
            carregaTodasBalizas(dataHoraOcorrenciaEvento);
        }
    }

    /**
     * Metodo que monta o painel das usinas que estao disponiveis para
     * atividade de recuperacao
     */
    private void montaPainelUsinas(Date data) {
        mapComboCampanhaUsina = new HashMap<MetaUsina, HashMap<Integer, Campanha>>();
        if (mapUsinasCombos == null || mapUsinasCombos.isEmpty())
            return;
        for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
            if (!(interfaceUsina instanceof InterfaceFiltragem)) {
                Map<JCheckBox, JComboBox> mapchkCombo = mapUsinasCombos.get(interfaceUsina.getUsinaVisualizada()
                                .getMetaUsina());
                if (mapchkCombo == null)
                    return;
                if (interfaceUsina.getUsinaVisualizada().getMetaUsina().getCampanhaAtual(data) != null) {
                    Set s = mapchkCombo.keySet();
                    for (Iterator it = s.iterator(); it.hasNext();) {
                        JCheckBox chk = (JCheckBox) it.next();
                        chk.setEnabled(true);
                        JComboBox result = mapchkCombo.get(chk);
                        HashMap<Integer, Campanha> mapComboCampanha = new HashMap<Integer, Campanha>();
                        mapComboCampanhaUsina.put(interfaceUsina.getUsinaVisualizada().getMetaUsina(), mapComboCampanha);
                        if (result != null) {
                            result.removeAllItems();
                            List<Campanha> campanhas = interfaceUsina.getUsinaVisualizada().getMetaUsina()
                                            .getCampanhas(data);
                            result.setEnabled(true);
                            Integer index = 0;
                            for (Campanha c : campanhas) {
                                result.addItem(c.getTipoProduto());
                                mapComboCampanha.put(index, c);
                                index++;
                            }
                            if (finalizaAtividade) {
                                chk.setEnabled(false);
                                result.setEnabled(false);
                            }
                        }
                    }
                } else {
                    Set s = mapchkCombo.keySet();
                    for (Iterator it = s.iterator(); it.hasNext();) {
                        JCheckBox chk = (JCheckBox) it.next();
                        JComboBox result = mapchkCombo.get(chk);
                        if (result != null) {
                            result.removeAllItems();
                            chk.setEnabled(false);
                            result.setEnabled(false);
                            if (finalizaAtividade) {
                                chk.setEnabled(false);
                                result.setEnabled(false);
                            }
                        }
                    }
                }
            }

        }
    }

    private void prepaparPainelUsinas() {
        mapUsinasSelecionaveis = new HashMap<MetaUsina, JCheckBox>();
        mapUsinasCombos = new HashMap<MetaUsina, HashMap<JCheckBox, JComboBox>>();

        for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
            if (!mapUsinasCombos.containsKey(interfaceUsina.getUsinaVisualizada().getMetaUsina())) {

                JCheckBox chkUsina = new JCheckBox();

                chkUsina.setName("chkUsina_" + interfaceUsina.getUsinaVisualizada().getNomeUsina().toLowerCase());
                JComboBox cmbTipoProdutoUsina = new javax.swing.JComboBox();
                HashMap<JCheckBox, JComboBox> mapchkCombo = new HashMap<JCheckBox, JComboBox>();
                mapchkCombo.put(chkUsina, cmbTipoProdutoUsina);
                mapUsinasCombos.put(interfaceUsina.getUsinaVisualizada().getMetaUsina(), mapchkCombo);
                mapUsinasSelecionaveis.put(interfaceUsina.getUsinaVisualizada().getMetaUsina(), chkUsina);
                pnlUsinasOperacao.add(chkUsina);
                pnlUsinasOperacao.add(new JLabel(interfaceUsina.getUsinaVisualizada().getNomeUsina()));
                pnlUsinasOperacao.add(cmbTipoProdutoUsina);
            }
        }
    }

    private void prepararPainelFiltragem() {
        mapFiltragensSelecionaveis = new HashMap<MetaFiltragem, JCheckBox>();

        mapFiltragemCombos = new HashMap<MetaFiltragem, HashMap<JCheckBox, JComboBox>>();

        for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
            MetaFiltragem filtragem = interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem();
            if (filtragem != null) {

                if (!mapFiltragemCombos.containsKey(filtragem)) {
                    JCheckBox chkfiltragem = new JCheckBox();
                    chkfiltragem.setName("chkfiltragem_"
                                    + interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem()
                                                    .getNomeFiltragem().toLowerCase());
                    JComboBox cmbTipoProdutoUsina = new javax.swing.JComboBox();
                    HashMap<JCheckBox, JComboBox> mapchkCombo = new HashMap<JCheckBox, JComboBox>();
                    mapchkCombo.put(chkfiltragem, cmbTipoProdutoUsina);
                    mapFiltragemCombos.put(filtragem, mapchkCombo);
                    mapFiltragensSelecionaveis.put(filtragem, chkfiltragem);
                    pnlFiltragemOperacao.add(chkfiltragem);
                    pnlFiltragemOperacao.add(new JLabel(filtragem.getNomeFiltragem()));
                    pnlFiltragemOperacao.add(cmbTipoProdutoUsina);
                }
            }
        }
    }

    /**
     * Metodo que monta o painel das usinas que estao disponiveis para
     * atividade de recuperacao
     */
    private void montaPainelFiltragem(Date data) {
        mapComboCampanhaFiltragem = new HashMap<MetaFiltragem, HashMap<Integer, Campanha>>();
        if (mapFiltragemCombos == null || mapFiltragemCombos.isEmpty())
            return;
        for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
            if (interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem() != null 
                            && interfaceUsina.getUsinaVisualizada().getMetaUsina().getCampanhaAtual(data) != null) {
                Map<JCheckBox, JComboBox> mapchkCombo = mapFiltragemCombos.get(interfaceUsina.getUsinaVisualizada()
                                .getMetaUsina().getFiltragemOrigem());
                if (mapchkCombo == null)
                    return;
                if (interfaceUsina.getUsinaVisualizada().getMetaUsina().getCampanhaAtual(data) != null) {
                    Set s = mapchkCombo.keySet();
                    for (Iterator it = s.iterator(); it.hasNext();) {
                        JCheckBox chk = (JCheckBox) it.next();
                        chk.setEnabled(true);
                        JComboBox result = mapchkCombo.get(chk);
                        HashMap<Integer, Campanha> mapComboCampanha = new HashMap<Integer, Campanha>();
                        mapComboCampanhaFiltragem.put(interfaceUsina.getUsinaVisualizada().getMetaUsina()
                                        .getFiltragemOrigem(), mapComboCampanha);

                        if (result != null) {
                            result.removeAllItems();
                            List<Campanha> campanhas = interfaceUsina.getUsinaVisualizada().getMetaUsina()
                                            .getCampanhas(data);
                            result.setEnabled(true);
                            Integer index = 0;
                            for (Campanha c : campanhas) {
                                result.addItem(c.getTipoPellet());
                                mapComboCampanha.put(index, c);
                                index++;
                            }
                            if (finalizaAtividade) {
                                chk.setEnabled(false);
                                result.setEnabled(false);
                            }
                        }
                    }
                } else {
                    Set s = mapchkCombo.keySet();
                    for (Iterator it = s.iterator(); it.hasNext();) {
                        JCheckBox chk = (JCheckBox) it.next();
                        JComboBox result = mapchkCombo.get(chk);
                        if (result != null) {
                            result.removeAllItems();
                            chk.setEnabled(false);
                            result.setEnabled(false);
                            if (finalizaAtividade) {
                                chk.setEnabled(false);
                                result.setEnabled(false);
                            }
                        }
                    }
                }
            }

        }
    }

    /** Carrega o nome da pilha */
    private void carregarNomePilha() {
        //   	nomePilhaJTF.setEnabled(true);
        // se a maquina ja esta em operacao usa o nome da pilha da baliza atual
        if (maquinaDoPatioEditada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
            nomePilhaJTF.setText(maquinaDoPatioEditada.getPosicao().retornaStatusHorario(dataHoraOcorrenciaEvento)
                            .getNomePilha());
        } else {
            Baliza proximaBaliza = (Baliza) proximaBalizaJCB.getSelectedItem();
            Pilha pilhaDaBaliza = null;
            if (proximaBaliza != null)
                pilhaDaBaliza = proximaBaliza.retornaStatusHorario(dataHoraOcorrenciaEvento);
            if (proximaBaliza != null && pilhaDaBaliza != null && pilhaDaBaliza.getNomePilha() != null) {
                nomePilhaJTF.setText(pilhaDaBaliza.getNomePilha());
            } else {
                nomePilhaJTF.setText("");
            }
        }
    }

    private void habilitaDesabilitaCampos(boolean value) {
        horaEventoJTF.setEnabled(value);
        nomePilhaJTF.setEnabled(value);
        proximaBalizaJCB.setEnabled(value);
        confirmarJB.setEnabled(value);
        desistirJB.setEnabled(value);

        for (int i = 0; i < pnlUsinasOperacao.getComponents().length; i++) {
            if (pnlUsinasOperacao.getComponent(i) instanceof JCheckBox) {
                JCheckBox chkUsina = (JCheckBox) pnlUsinasOperacao.getComponent(i);
                chkUsina.setEnabled(chkUsina.isEnabled());
            } else if (pnlUsinasOperacao.getComponent(i) instanceof JComboBox) {
                JComboBox cmbTipoProduto = (JComboBox) pnlUsinasOperacao.getComponent(i);
                cmbTipoProduto.setEnabled(cmbTipoProduto.isEnabled());
            }
        }

    }

    private void habilitaTodasFuncoes() throws ErroSistemicoException {
        horaEventoJTF.setEnabled(true);
        nomePilhaJTF.setEnabled(true);
        proximaBalizaJCB.setEnabled(true);
        confirmarJB.setEnabled(true);
        desistirJB.setEnabled(true);

        carregarBalizaAtual();
        setaTipoOperacao();
        carregarDataHoraAtual();
        carregarNomePilha();
        carregarNomePatio();

    }

    /** Carrega o nome do patio */
    public void carregarNomePatio() {
        patioJTF.setText(maquinaDoPatioEditada.getPosicao().getPatio().getNomePatio());
    }

    /** Seta a data hora atual */
    private void carregarDataHoraAtual() {
        horaEventoJTF.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil
                        .obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora")));
        horaEventoJTF.setSelectionStart(11); // comeca a selecao no inicio da hora
        horaEventoJTF.setSelectionEnd(horaEventoJTF.getText().length());
        horaEventoJTF.requestFocus();
    }

    /** Seta o tipo de operacao padrao conforme o estado da maquina */
    private void setaTipoOperacao() {
        // se a maquina ja esta operando o tipo padrao sera "mudanca de baliza"
        if (maquinaDoPatioEditada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
            carregarComboTipoProdutoMudarBaliza();
        } // se a maquina nao esta operando o tipo padrao sera "iniciar"
    }

    /** Carrega a baliza em que a maquina esta atualmente */
    private void carregarBalizaAtual() {
        balizaAtual = maquinaDoPatioEditada.getPosicao();
        balizaAtualJTF.setText(balizaAtual.getNomeBaliza());

        //if (atividadeExecutada != null)
        //{
        proximaBalizaJL.setText(PropertiesUtil.getMessage("mensagem.baliza.proxima"));

        int numeroBaliza = balizaAtual.getNumero();
        // o combo box de proxima baliza eh um anti-bobeira pra obrigar o usuario a cadastrar todas as mudancas de baliza
        proximaBalizaJCB.removeAllItems();
        // inclue a baliza anterior
        if (numeroBaliza >= 1) {
            proximaBalizaJCB.addItem(retornaBaliza(maquinaDoPatioEditada.getPosicao().getPatio(), numeroBaliza - 1));
        }
        // inclue a baliza atual
        proximaBalizaJCB.addItem(balizaAtual);
        // inclue a baliza posterior
        if (numeroBaliza < maquinaDoPatioEditada.getPosicao().getPatio().getListaDeBalizas(dataHoraOcorrenciaEvento).size()) {
            proximaBalizaJCB.addItem(retornaBaliza(maquinaDoPatioEditada.getPosicao().getPatio(), numeroBaliza + 1));
        }

        // for (Baliza b : proximaBalizaJCB.getItemCount())
        // deixa selecionada a baliza atual
        proximaBalizaJCB.setSelectedItem(balizaAtual);
        //}

        // se a maquina estiver trabalhando em mais de uma atividade ao mesmo tempo entao nao deixa mudar a baliza
        Atividade movimentarPsm = maquinaDoPatioEditada.getMetaMaquina().existeMovimentacaoPSMPendente();
        Atividade movimentarEmergencia = maquinaDoPatioEditada.getMetaMaquina().existeMovimentacaoEmergenciaPendente();
        Atividade movimentarPellet = maquinaDoPatioEditada.getMetaMaquina().existeMovimentacaoPelletPendente();
        if (movimentarPsm != null || movimentarEmergencia != null || movimentarPellet != null) {
            proximaBalizaJCB.setEnabled(false);
        }
    }

    /** Retorna uma baliza dado o patio */
    private Baliza retornaBaliza(Patio patio, int numeroBaliza) {
        for (Baliza baliza : patio.getListaDeBalizas(dataHoraOcorrenciaEvento)) {
            if (baliza.getPatio().equals(patio) && baliza.getNumero().equals(numeroBaliza)) {
                return baliza;
            }
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        horarioJP = new javax.swing.JPanel();
        horaEventoJTF = new javax.swing.JFormattedTextField();
        MaskFormatter fmtHoraInicioFormacao = new MaskFormatter();
        try {
            fmtHoraInicioFormacao.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
            fmtHoraInicioFormacao.setPlaceholderCharacter(' ');
            fmtHoraInicioFormacao.install(horaEventoJTF);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        
        
        PlainDocument doc = (PlainDocument) horaEventoJTF.getDocument();   
        doc.addDocumentListener(new DocumentListener() {   
          public void changedUpdate(DocumentEvent e) {   
              horaEventoJTFTextChanged();
          }   
          public void insertUpdate(DocumentEvent e) {   
              horaEventoJTFTextChanged();
          }   
          public void removeUpdate(DocumentEvent e) {   
              //txtDataHoraInicioRecuperacaoTextChanged();
          }   
        });   

        horaEventoJL = new javax.swing.JLabel();
        balizasJP = new javax.swing.JPanel();
        balizaAtualJL = new javax.swing.JLabel();
        proximaBalizaJL = new javax.swing.JLabel();
        balizaAtualJTF = new javax.swing.JTextField();
        nomePilhaJL = new javax.swing.JLabel();
        nomePilhaJTF = new javax.swing.JTextField();
        proximaBalizaJCB = new javax.swing.JComboBox();
        patioJL = new javax.swing.JLabel();
        patioJTF = new javax.swing.JTextField();
        pnlUsinasOperacao = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        desistirJB = new javax.swing.JButton();
        confirmarJB = new javax.swing.JButton();
        pnlFiltragemOperacao = new javax.swing.JPanel();
        jLabelChkFiltragem = new javax.swing.JLabel();
        jLabelNomeFiltragem = new javax.swing.JLabel();
        jLabelProdutoFiltragem = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(PropertiesUtil.getMessage("mensagem.atualizacao.empilhamento"));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        horarioJP.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.horario")));

        horaEventoJL.setText(PropertiesUtil.getMessage("mensagem.ocorrencia.eventos"));

        javax.swing.GroupLayout horarioJPLayout = new javax.swing.GroupLayout(horarioJP);
        horarioJP.setLayout(horarioJPLayout);
        horarioJPLayout.setHorizontalGroup(
            horarioJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(horarioJPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(horaEventoJL, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(horaEventoJTF, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );
        horarioJPLayout.setVerticalGroup(
            horarioJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(horarioJPLayout.createSequentialGroup()
                .addGroup(horarioJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(horaEventoJL, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(horaEventoJTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        balizasJP.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.pilha")));

        balizaAtualJL.setText(PropertiesUtil.getMessage("mensagem.baliza.atual"));

        proximaBalizaJL.setText(PropertiesUtil.getMessage("mensagem.baliza.proxima"));

        balizaAtualJTF.setDocument(new FixedLengthDocument(60));
        balizaAtualJTF.setEditable(false);

        nomePilhaJL.setText(PropertiesUtil.getMessage("mensagem.pilha.nome"));

        proximaBalizaJCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                proximaBalizaJCBItemStateChanged(evt);
            }
        });

        patioJL.setText(PropertiesUtil.getMessage("mensagem.patio.nome"));

        patioJTF.setEditable(false);

        javax.swing.GroupLayout balizasJPLayout = new javax.swing.GroupLayout(balizasJP);
        balizasJP.setLayout(balizasJPLayout);
        balizasJPLayout.setHorizontalGroup(
            balizasJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, balizasJPLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(balizasJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(balizaAtualJL, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(patioJL, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(balizasJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(patioJTF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(balizaAtualJTF, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(balizasJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(balizasJPLayout.createSequentialGroup()
                        .addComponent(proximaBalizaJL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(proximaBalizaJCB, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(balizasJPLayout.createSequentialGroup()
                        .addComponent(nomePilhaJL)
                        .addGap(10, 10, 10)
                        .addComponent(nomePilhaJTF, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        balizasJPLayout.setVerticalGroup(
            balizasJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(balizasJPLayout.createSequentialGroup()
                .addGroup(balizasJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proximaBalizaJL, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(proximaBalizaJCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(balizaAtualJL, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(balizaAtualJTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(balizasJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patioJL)
                    .addComponent(patioJTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomePilhaJTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomePilhaJL))
                .addContainerGap())
        );

        pnlUsinasOperacao.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.usinas")));
        pnlUsinasOperacao.setLayout(new java.awt.GridLayout(0, 3, 10, 5));

        jLabel7.setText(PropertiesUtil.getMessage("mensagem.selecao"));
        jLabel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pnlUsinasOperacao.add(jLabel7);

        jLabel8.setText(PropertiesUtil.getMessage("mensagem.usina.nome"));
        jLabel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pnlUsinasOperacao.add(jLabel8);

        jLabel10.setText(PropertiesUtil.getMessage("mensagem.produto.tipo"));
        jLabel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pnlUsinasOperacao.add(jLabel10);

        desistirJB.setFont(new java.awt.Font("Tahoma", 1, 12));
        //desistirJB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        desistirJB.setText(PropertiesUtil.getMessage("botao.desistir"));
        desistirJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desistirJBActionPerformed(evt);
            }
        });

        confirmarJB.setFont(new java.awt.Font("Tahoma", 1, 12));
//        confirmarJB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/confirmar.png"))); // NOI18N
        confirmarJB.setText(PropertiesUtil.getMessage("botao.confirmar"));
        confirmarJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmarJBActionPerformed(evt);
            }
        });

        pnlFiltragemOperacao.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.usinas")));
        pnlFiltragemOperacao.setLayout(new java.awt.GridLayout(0, 3, 10, 5));

        jLabelChkFiltragem.setText(PropertiesUtil.getMessage("mensagem.selecao"));
        jLabelChkFiltragem.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pnlFiltragemOperacao.add(jLabelChkFiltragem);

        jLabelNomeFiltragem.setText(PropertiesUtil.getMessage("mensagem.usina.nome"));
        jLabelNomeFiltragem.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pnlFiltragemOperacao.add(jLabelNomeFiltragem);

        jLabelProdutoFiltragem.setText(PropertiesUtil.getMessage("mensagem.produto.tipo"));
        jLabelProdutoFiltragem.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pnlFiltragemOperacao.add(jLabelProdutoFiltragem);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(balizasJP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(pnlUsinasOperacao, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pnlFiltragemOperacao, 0, 0, Short.MAX_VALUE)
                        .addGap(4, 4, 4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(desistirJB, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(confirmarJB, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(horarioJP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(horarioJP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(balizasJP, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlUsinasOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlFiltragemOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmarJB, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(desistirJB))
                .addContainerGap(176, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void validarInformacoesAtualizacaoEmpilhamento() throws ValidacaoCampoException, PilhaExistenteException {

        if (horaEventoJTF.getText().equals(DSSStockyardTimeUtil.DATAHORA_EM_BRANCO)) {
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.datahora.nao.preenchido"));
        }

        DSSStockyardTimeUtil.validarDataHora(horaEventoJTF.getText(), PropertiesUtil.getMessage("mensagem.datahora.evento"));
        dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(horaEventoJTF.getText(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));

        SituacaoPatio situacaoPatioRealizada = controladorDSP.getInterfaceInicial().getPlanejamento()
                        .getControladorDePlano().obterSituacaoPatioFinal();
        dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoClone(dataHoraOcorrenciaEvento,
                        situacaoPatioRealizada.getDtInicio());
        if (situacaoPatioRealizada != null) {
            if (dataHoraOcorrenciaEvento.compareTo(situacaoPatioRealizada.getDtInicio()) < 0) {
                throw new ValidacaoCampoException(PropertiesUtil
                                .getMessage("aviso.mensagem.data.inicial.menor.que.data.situacao.patio"));
            }
        }

        if (nomePilhaJTF.getText().trim().equals("")) {
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.pilha.nome.nao.preenchido"));
        }

        if (atividadeExecutada == null) {
            if (listaUsinasSelecionada.isEmpty() && listaFiltragemSelecionada.isEmpty()) {
                throw new ValidacaoCampoException("Selecione usinas ou filtragens para executar a atividade !");
            }

            if (!listaUsinasSelecionada.isEmpty() && !listaFiltragemSelecionada.isEmpty()) {
                throw new ValidacaoCampoException("Não é permitido operações com Usinas e Filtragens ao mesmo tempo !");
            }

           // controladorDSP.getInterfaceInicial().validarNomeDaPilha(nomePilhaJTF.getText());
        } 
    }

    private void confirmarJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmarJBActionPerformed

        try {

            Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(horaEventoJTF.getText(), PropertiesUtil
                            .buscarPropriedade("formato.campo.datahora"));
            double diferencaHoras = DSSStockyardTimeUtil.diferencaEmHoras(controladorDSP.getInterfaceInicial()
                            .getInterfaceInicial().getSituacaoPatioExibida().getDtInicio(), dataHoraOcorrenciaEvento);
            double tempoValidacaoHorasFutura = new Double(PropertiesUtil
                            .buscarPropriedade("quantidade.horas.aviso.data.futura.atividades"));

            if (diferencaHoras >= tempoValidacaoHorasFutura) {
                JLabel pergunta = new JLabel(PropertiesUtil.getMessage("aviso.hora.atividade.superior.ao.parametro.futuro"));
                pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                int confirm = JOptionPane.showOptionDialog(this, pergunta, PropertiesUtil.getMessage("popup.atencao"),
                                JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

                if (confirm == JOptionPane.NO_OPTION) {
                    horaEventoJTF.requestFocus();
                    return;
                }
            }

            final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
            interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
            interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
            interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil
                            .buscarPropriedade("mensagem.processamento.atualizacao.empilhamento"));

            new Thread("Thread atualização maquina empilhamento") {
                @Override
                public void run() {
                    try {
                        Date dataAtividade = DSSStockyardTimeUtil.criaDataComString(horaEventoJTF.getText(), PropertiesUtil
                                        .buscarPropriedade("formato.campo.datahora"));
                        boolean encerrar = false;
                        carregarListaUsinasSelecionadas(dataAtividade);
                        carregarListaFiltragensSelecionadas(dataAtividade);
                        validarInformacoesAtualizacaoEmpilhamento();
                        //Precisa verificar se as usinas selecionadas estao com um atualizacao de recuperacao aberta

                        dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP
                                        .getInterfaceInicial().getPlanejamento().getControladorDePlano()
                                        .obterSituacaoPatioFinal().getDtInicio());


                        
                        
                        List<MaquinaDoPatio> maquinasAndamento = verificarEmpilhamentoEmAndamentoNasUsinas(dataAtividade);
                        
                        if (maquinasAndamento.size() > 0) {
                            StringBuilder usinas = new StringBuilder("Empilhamento Máquinas em andamento\n");
                            for (MaquinaDoPatio maquina : maquinasAndamento) {
                               // usinas.append(maquina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas().get(0).getNomeUsina());
                                usinas.append(maquina.getAtividade().getTipoAtividade().toString());
                                usinas.append(" - ");
                                usinas.append(maquina.getNomeMaquina());
                                usinas.append("\n");
                            }

                            JLabel pergunta = new JLabel(usinas.toString());
                            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                            JOptionPane.showOptionDialog(null,
                                            //pergunta,
                                            usinas.toString(), PropertiesUtil.getMessage("popup.atencao"),
                                            JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                            return;
                        }
                        
                        maquinasAndamento = verificarEmpilhamentoEmAndamentoNasFiltragens(dataAtividade);
                        
                        if (maquinasAndamento.size() > 0) {
                            StringBuilder usinas = new StringBuilder("Empilhamento Máquinas em andamento\n");
                            for (MaquinaDoPatio maquina : maquinasAndamento) {
                               // usinas.append(maquina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas().get(0).getNomeUsina());
                                usinas.append(maquina.getAtividade().getTipoAtividade().toString());
                                usinas.append(" - ");
                                usinas.append(maquina.getNomeMaquina());
                                usinas.append("\n");
                            }

                            JLabel pergunta = new JLabel(usinas.toString());
                            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                            JOptionPane.showOptionDialog(null,
                                            //pergunta,
                                            usinas.toString(), PropertiesUtil.getMessage("popup.atencao"),
                                            JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                            return;
                        }
                        
                        List<Usina> usinasComAtividadeEmAndamento = verificarEmpilhamentoEmergenciaEmAndamentoNasUsinas(dataAtividade);
                        if (usinasComAtividadeEmAndamento.size() > 0) {
                            StringBuilder usinas = new StringBuilder(PropertiesUtil
                                            .getMessage("aviso.empilhamento.emergencia.usina")
                                            + "\n");
                            for (Usina usina : usinasComAtividadeEmAndamento) {
                                usinas.append(usina.getAtividade().getTipoAtividade().toString());
                                usinas.append(" - ");
                                usinas.append(usina.getNomeUsina());
                                usinas.append("\n");
                            }

                            JLabel pergunta = new JLabel(usinas.toString());
                            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                            JOptionPane.showOptionDialog(null,
                                            //pergunta,
                                            usinas.toString(), PropertiesUtil.getMessage("popup.atencao"),
                                            JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                            return;
                        }

                        usinasComAtividadeEmAndamento = verificarRecuperacaoEmAndamentoNasUsinas(dataAtividade);

                        if (usinasComAtividadeEmAndamento.size() > 0) {
                            StringBuilder usinas = new StringBuilder(PropertiesUtil
                                            .getMessage("aviso.encerrar.recuperacao.usina")
                                            + "\n");
                            for (Usina usina : usinasComAtividadeEmAndamento) {
                                usinas.append(usina.getAtividade().getTipoAtividade().toString());
                                usinas.append(" - ");
                                usinas.append(usina.getNomeUsina());
                                usinas.append("\n");
                            }
                            usinas.append(PropertiesUtil.getMessage("aviso.encerrar.recuperacao.usina.continuar"));

                            JLabel pergunta = new JLabel(usinas.toString());
                            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                            int confirm = JOptionPane.showOptionDialog(null,
                                            //pergunta,
                                            usinas.toString(), PropertiesUtil.getMessage("popup.atencao"),
                                            JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

                            if (confirm == JOptionPane.NO_OPTION) {
                                return;

                            } else {
                                encerraRecuperacaoNasUsinas(usinasComAtividadeEmAndamento, dataAtividade);
                                dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP
                                                .getInterfaceInicial().getPlanejamento().getControladorDePlano()
                                                .obterSituacaoPatioFinal().getDtInicio());
                            }
                        }

                        List<Filtragem> filtragemAndamento = verificarRecuperacaoEmAndamentoNasFiltragem(dataAtividade);

                        if (filtragemAndamento.size() > 0) {
                            StringBuilder usinas = new StringBuilder(PropertiesUtil
                                            .getMessage("aviso.encerrar.recuperacao.usina")
                                            + "\n");
                            for (Filtragem usina : filtragemAndamento) {
                                usinas.append(usina.getAtividade().getTipoAtividade().toString());
                                usinas.append(" - ");
                                usinas.append(usina.getMetaFiltragem().getNomeFiltragem());
                                usinas.append("\n");
                            }
                            usinas.append(PropertiesUtil.getMessage("aviso.encerrar.recuperacao.usina.continuar"));

                            JLabel pergunta = new JLabel(usinas.toString());
                            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                            int confirm = JOptionPane.showOptionDialog(null,
                                            //pergunta,
                                            usinas.toString(), PropertiesUtil.getMessage("popup.atencao"),
                                            JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

                            if (confirm == JOptionPane.NO_OPTION) {
                                return;

                            } else {
                                encerraRecuperacaoNasFiltragem(filtragemAndamento, dataAtividade);
                                dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP
                                                .getInterfaceInicial().getPlanejamento().getControladorDePlano()
                                                .obterSituacaoPatioFinal().getDtInicio());
                            }
                        }

                        habilitaDesabilitaCampos(false);

                        controladorDSP.ativarMensagem(interfaceMensagemProcessamento);

                        String nomePilha = nomePilhaJTF.getText();

                        /** cria o VO para o empilhamento */
                        AtualizarEmpilhamentoVO empilhamentoVO = new AtualizarEmpilhamentoVO();

                        empilhamentoVO.setDataInicio(dataAtividade);
                        empilhamentoVO.setNomePilha(nomePilha);
                        Baliza balizaEmpilhamento;

                        balizaEmpilhamento = (Baliza) proximaBalizaJCB.getSelectedItem();

                        empilhamentoVO.getListaBalizas().add(balizaEmpilhamento.getMetaBaliza());
                        List<MetaUsina> listaMetaUsinas = new ArrayList<MetaUsina>();

                        for (Usina usinaSelecionada : listaUsinasSelecionada) {
                            listaMetaUsinas.add(usinaSelecionada.getMetaUsina());
                        }
                        empilhamentoVO.getListaUsinas().addAll(listaMetaUsinas);
                        empilhamentoVO.setMapUsinaCampanha(mapUsinaCampanha);
                        List<MetaFiltragem> listaMetaFiltragem = new ArrayList<MetaFiltragem>();

                        for (MetaFiltragem usinaSelecionada : listaFiltragemSelecionada) {
                            listaMetaFiltragem.add(usinaSelecionada);
                        }

                    
                        empilhamentoVO.getListaFiltragens().addAll(listaMetaFiltragem);
                        empilhamentoVO.setMapFiltragemCampanha(mapFiltragemCampanha);

                        List<MetaMaquinaDoPatio> listaMetaMaquinas = new ArrayList<MetaMaquinaDoPatio>();
                        listaMetaMaquinas.add(maquinaDoPatioEditada.getMetaMaquina());
                        empilhamentoVO.setListaMaquinas(listaMetaMaquinas);
                        if (listaMetaUsinas != null && listaMetaUsinas.size() > 0) {
                            empilhamentoVO.setTipoProduto(null);
                        } else if (listaMetaFiltragem != null && listaMetaFiltragem.size() > 0) {
                            empilhamentoVO.setTipoProduto(null);
                        } else {
                            // finalizacao de empilhamento, pega o tipo de produto armazenado na pilha
                            empilhamentoVO.setTipoProduto(balizaEmpilhamento.getProduto().getTipoProduto());
                            // seta a data final da atividade
                            empilhamentoVO.setDataFim(dataAtividade);
                        }

                        //TODO ver de onde pega o cliente
                        empilhamentoVO.setCliente(null);
                        IControladorExecutarAtividadeEmpilhamento service = ControladorExecutarAtividadeEmpilhamento
                                        .getInstance();
                        List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();

                        
                        if (validarCampanhaDiferente()) {
                            return;   
                        }
                            
                            
                        
                        atividadeAtualizacaoEmpilhamento = service.empilhar(empilhamentoVO, lugaresAnteriores);

                        controladorDSP.getInterfaceInicial().atualizarEmpilhamento(atividadeAtualizacaoEmpilhamento);
                        controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();

                        operacaoCanceladaPeloUsuario = Boolean.FALSE;

                        // chegando ate o JDialog
                        setVisible(false);

                    } catch (AtividadeException ex) {
                        controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorDSP.ativarMensagem(interfaceMensagem);
                    } catch (ValidacaoCampoException vcEx) {
                        controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                        interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
                        controladorDSP.ativarMensagem(interfaceMensagem);
                    } catch (ValidacaoObjetosOperacaoException ex) {
                        controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorDSP.ativarMensagem(interfaceMensagem);
                    } catch (PilhaExistenteException ex) {
                        controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorDSP.ativarMensagem(interfaceMensagem);
                    }
                    finally {
                        controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                        //             	  confirmarJB.setEnabled(true);
                        //             	  desistirJB.setEnabled(true);
                        habilitaDesabilitaCampos(true);
                    }
                }
            }.start();
        } catch (ValidacaoCampoException vcEx) {
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
    }//GEN-LAST:event_confirmarJBActionPerformed

    /**
     * encerraRecuperacaoNasUsinas
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return void
     * @throws AtividadeException 
     */
    private void encerraRecuperacaoNasUsinas(List<Usina> usinas, Date dataAtividade)
                    throws ValidacaoObjetosOperacaoException, AtividadeException {

        for (Usina usina : usinas) {

            Date dataIni = usina.getAtividade().getDtInicio();
            dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP.getInterfaceInicial()
                            .getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
            controladorDSP.getInterfaceInicial()
                            .atualizaRecuperacaoUsina(
                                            usina,
                                            null,
                                            usina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                                                            .getListaCargas().get(0),
                                            usina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                                                            .getTipoProduto(),
                                            usina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                                                            .getNomePorao(), dataAtividade, dataIni, atividadeExecutada,
                                            atividadeExecutada.getListaDeAtividadesCampanha().get(0).getCampanha());

        }
    }

    /**
     * encerraRecuperacaoNasFiltragem
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return void
     * @throws AtividadeException 
     */
    private void encerraRecuperacaoNasFiltragem(List<Filtragem> usinas, Date dataAtividade)
                    throws ValidacaoObjetosOperacaoException, AtividadeException {

        for (Filtragem filtragem : usinas) {

            Date dataIni = filtragem.getAtividade().getDtInicio();
            dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP.getInterfaceInicial()
                            .getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
            controladorDSP.getInterfaceInicial().atualizaRecuperacaoUsina(
                            null,
                            filtragem,
                            filtragem.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas()
                                            .get(0),
                            filtragem.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getTipoProduto(),
                            filtragem.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getNomePorao(),
                            dataAtividade, dataIni, atividadeExecutada,
                            atividadeExecutada.getListaDeAtividadesCampanha().get(0).getCampanha());

        }
    }

    /**
     * verificarRecuperacaoEmAndamentoNasUsinas
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return List<Usina>
     */
    private List<Usina> verificarRecuperacaoEmAndamentoNasUsinas(Date dataAtividade) {
        List<Usina> usinasComAtividadeEmAndamento = new ArrayList<Usina>();

        for (Usina usina : listaUsinasSelecionada) {
            MetaUsina metaUsina = usina.getMetaUsina();
            Usina usinaCorrente = metaUsina.retornaStatusHorario(dataAtividade);
            //Verificando se a usina tem uma atividade de Atualizacao de Recuperacao		   
            if (usinaCorrente.getAtividade() != null
                            && usinaCorrente.getAtividade().getTipoAtividade().equals(
                                            TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)
                            && usinaCorrente.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                usinasComAtividadeEmAndamento.add(usinaCorrente);
            }
        }
        return usinasComAtividadeEmAndamento;
    }

    /**
     * verificarEmpilhamentoEmAndamentoNasUsinas
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return List<MaquinaDoPatio>
     */
    private List<MaquinaDoPatio> verificarEmpilhamentoEmAndamentoNasUsinas(Date dataAtividade) {
        List<MaquinaDoPatio> usinasComAtividadeEmAndamento = new ArrayList<MaquinaDoPatio>();

        for (Usina usina : listaUsinasSelecionada) {
            MetaUsina metaUsina = usina.getMetaUsina();
            Usina usinaCorrente = metaUsina.retornaStatusHorario(dataAtividade);

            if (usinaCorrente.getAtividade() != null
                            && usinaCorrente.getAtividade().getTipoAtividade().equals(
                                            TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO)
                            && usinaCorrente.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {

                List<LugarEmpilhamentoRecuperacao> lstLugar = usinaCorrente.getAtividade()
                                .getListaDeLugaresDeEmpilhamentoRecuperacao();
                for (LugarEmpilhamentoRecuperacao lugar : lstLugar) {
                    List<MaquinaDoPatio> lstMaquina = lugar.getListaMaquinaDoPatio();
                    if (lstMaquina != null) {
                    	for (MaquinaDoPatio maquina : lstMaquina) {
                    		if (!maquina.getMetaMaquina().equals(this.maquinaDoPatioEditada.getMetaMaquina())) {
                    			usinasComAtividadeEmAndamento.add(maquina);
                    		}
                    	}
                    }	
                }
            }
        }
        return usinasComAtividadeEmAndamento;
    }

    
    /**
     * verificarEmpilhamentoEmAndamentoNasFiltragens
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return List<MaquinaDoPatio>
     */
    private List<MaquinaDoPatio> verificarEmpilhamentoEmAndamentoNasFiltragens(Date dataAtividade) {
        List<MaquinaDoPatio> usinasComAtividadeEmAndamento = new ArrayList<MaquinaDoPatio>();


        
        for (MetaFiltragem metaFiltragem : listaFiltragemSelecionada) {
            Filtragem usinaCorrente = metaFiltragem.retornaStatusHorario(dataAtividade);
            //Verificando se a usina tem uma atividade de Atualizacao de Recuperacao        
            if (usinaCorrente.getAtividade() != null
                            && usinaCorrente.getAtividade().getTipoAtividade().equals(
                                            TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO)
                            && usinaCorrente.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {

                List<LugarEmpilhamentoRecuperacao> lstLugar = usinaCorrente.getAtividade()
                .getListaDeLugaresDeEmpilhamentoRecuperacao();
                for (LugarEmpilhamentoRecuperacao lugar : lstLugar) {
                    List<MaquinaDoPatio> lstMaquina = lugar.getListaMaquinaDoPatio();
                    if (lstMaquina != null) {
                    	for (MaquinaDoPatio maquina : lstMaquina) {
                    		if (!maquina.getMetaMaquina().equals(this.maquinaDoPatioEditada.getMetaMaquina())) {
                    			usinasComAtividadeEmAndamento.add(maquina);
                    		}
                    	}
                    }	
                }
            }
        }        
         return usinasComAtividadeEmAndamento;
    }
    
    /**
     * verificarEmpilhamentoEmergenciaEmAndamentoNasUsinas
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return List<Usina>
     */
    private List<Usina> verificarEmpilhamentoEmergenciaEmAndamentoNasUsinas(Date dataAtividade) {
        List<Usina> usinasComAtividadeEmAndamento = new ArrayList<Usina>();

        for (Usina usina : listaUsinasSelecionada) {
            MetaUsina metaUsina = usina.getMetaUsina();
            Usina usinaCorrente = metaUsina.retornaStatusHorario(dataAtividade);
            //Verificando se a usina tem uma atividade de Atualizacao de Recuperacao        
            if (usinaCorrente.getAtividade() != null
                            && usinaCorrente.getAtividade().getTipoAtividade().equals(
                                            TipoAtividadeEnum.ATUALIZACAO_PILHA_DE_EMERGENCIA)
                            && usinaCorrente.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                usinasComAtividadeEmAndamento.add(usinaCorrente);
            }
        }
        return usinasComAtividadeEmAndamento;
    }

    /**
     * verificarRecuperacaoEmAndamentoNasFiltragem
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return List<Filtragem>
     */
    private List<Filtragem> verificarRecuperacaoEmAndamentoNasFiltragem(Date dataAtividade) {
        
        List<Filtragem> listaFiltragem = new ArrayList<Filtragem>();
        

        for (MetaFiltragem metaFiltragem : listaFiltragemSelecionada) {
            Filtragem usinaCorrente = metaFiltragem.retornaStatusHorario(dataAtividade);
            //Verificando se a usina tem uma atividade de Atualizacao de Recuperacao        
            if (usinaCorrente.getAtividade() != null
                            && usinaCorrente.getAtividade().getTipoAtividade().equals(
                                            TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)
                            && usinaCorrente.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                listaFiltragem.add(usinaCorrente);
            }
        }
        return listaFiltragem;
    }

    /**
     * Carrega as balizas anterior e posterior a baliza atual quando, tambem apresenta a baliza atual
     * o tipo de operacao for mudar baliza
     */
    private void carregarComboTipoProdutoMudarBaliza() {
        proximaBalizaJCB.removeAllItems();
        //      proximaBalizaJCB.setEnabled(true);
        Baliza balizaFinal = new LinkedList<Baliza>(balizaAtual.getPatio().getListaDeBalizas(dataHoraOcorrenciaEvento))
                        .getLast();
        if (balizaAtual.getNumero() > 1 && balizaAtual.getNumero() != balizaFinal.getNumero()) {
            Baliza balizaAnterior = retornaBaliza(balizaAtual.getPatio(), balizaAtual.getNumero() - 1);
            if (balizaAnterior != null) {
                proximaBalizaJCB.addItem(balizaAnterior);
            }

            Baliza balizaAtualCombo = retornaBaliza(balizaAtual.getPatio(), balizaAtual.getNumero());
            if (balizaAtualCombo != null) {
                proximaBalizaJCB.addItem(balizaAtualCombo);
            }

            Baliza balizaFinalCombo = retornaBaliza(balizaAtual.getPatio(), balizaAtual.getNumero() + 1);
            if (balizaFinalCombo != null) {
                proximaBalizaJCB.addItem(balizaFinalCombo);
            }
        } else if (balizaAtual.getNumero() == 1) {
            Baliza balizaAtualCombo = retornaBaliza(balizaAtual.getPatio(), balizaAtual.getNumero());
            if (balizaAtualCombo != null) {
                proximaBalizaJCB.addItem(balizaAtualCombo);
            }

            Baliza balizaFinalCombo = retornaBaliza(balizaAtual.getPatio(), balizaAtual.getNumero() + 1);
            if (balizaFinalCombo != null) {
                proximaBalizaJCB.addItem(balizaFinalCombo);
            }
        } else {
            Baliza balizaAnterior = retornaBaliza(balizaAtual.getPatio(), balizaAtual.getNumero() - 1);
            if (balizaAnterior != null) {
                proximaBalizaJCB.addItem(balizaAnterior);
            }

            Baliza balizaAtualCombo = retornaBaliza(balizaAtual.getPatio(), balizaAtual.getNumero());
            if (balizaAtualCombo != null) {
                proximaBalizaJCB.addItem(balizaAtualCombo);
            }
        }

        proximaBalizaJCB.setSelectedItem(retornaBaliza(balizaAtual.getPatio(), balizaAtual.getNumero()));
    }

    private void desistirJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desistirJBActionPerformed
        operacaoCanceladaPeloUsuario = Boolean.TRUE;
        this.dispose();
    }//GEN-LAST:event_desistirJBActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        operacaoCanceladaPeloUsuario = Boolean.TRUE;
    }//GEN-LAST:event_formWindowClosed

    private void proximaBalizaJCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_proximaBalizaJCBItemStateChanged
        carregarNomePilha();
    }//GEN-LAST:event_proximaBalizaJCBItemStateChanged

    public Boolean getOperacaoCanceladaPeloUsuario() {
        return operacaoCanceladaPeloUsuario;
    }

    public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario) {
        this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
    }

    public Atividade getAtividadeAtualizacaoEmpilhamento() {
        return atividadeAtualizacaoEmpilhamento;
    }

    public void setAtividadeAtualizacaoEmpilhamento(Atividade atividadeAtualizacaoEmpilhamento) {
        this.atividadeAtualizacaoEmpilhamento = atividadeAtualizacaoEmpilhamento;
    }

    /**
     * Metodo que carrega uma lista com a lista de usinas selecionadas
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private void carregarListaUsinasSelecionadas(Date data) throws ValidacaoCampoException {
        listaUsinasSelecionada = new ArrayList<Usina>();
        mapUsinaCampanha = new HashMap<MetaUsina, Campanha>();
        for (int i = 0; i < pnlUsinasOperacao.getComponents().length; i++) {
            if (pnlUsinasOperacao.getComponent(i) instanceof JCheckBox) {
                JCheckBox chkUsina = (JCheckBox) pnlUsinasOperacao.getComponent(i);
                if (chkUsina.isSelected()) {
                    JLabel lblNomeUsina = (JLabel) pnlUsinasOperacao.getComponent(i + INDICE_COMPONENTE_NOMESUINA);
                    //               JTextField txtTaxaOperacao = (JTextField) pnlUsinasOperacao.getComponent(i + INDICE_COMPONENTE_TAXAOPERACAO);

                    JComboBox cmbTipoProduto = (JComboBox) pnlUsinasOperacao.getComponent(i + INDICE_COMPONENTE_TIPOPRODUTO);
                    TipoProduto tipoProdutoSelecionado = (TipoProduto) cmbTipoProduto.getSelectedItem();

                    Usina usinaSelecionada = buscarUsinaNaListaPeloNome(lblNomeUsina.getText());
                    //               usinaSelecionada.setTaxaDeOperacao(DSSStockyardFuncoesNumeros.getStringToDouble(txtTaxaOperacao.getText()));

                    HashMap<Integer, Campanha> itemCampanha = mapComboCampanhaUsina.get(usinaSelecionada.getMetaUsina());
                    Campanha campanhaUsina = itemCampanha.get(cmbTipoProduto.getSelectedIndex());

                    mapUsinaCampanha.put(usinaSelecionada.getMetaUsina(), campanhaUsina);

                    
                    listaUsinasSelecionada.add(usinaSelecionada);
                }
            }
        }
    }

    /**
     * Metodo que carrega uma lista com a lista de usinas selecionadas
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private void carregarListaFiltragensSelecionadas(Date data) throws ValidacaoCampoException {
        listaFiltragemSelecionada = new ArrayList<MetaFiltragem>();
        mapFiltragemCampanha = new HashMap<MetaFiltragem, Campanha>();
        for (int i = 0; i < pnlFiltragemOperacao.getComponents().length; i++) {
            if (pnlFiltragemOperacao.getComponent(i) instanceof JCheckBox) {
                JCheckBox chkUsina = (JCheckBox) pnlFiltragemOperacao.getComponent(i);
                if (chkUsina.isSelected()) {
                    JLabel lblNomeUsina = (JLabel) pnlFiltragemOperacao.getComponent(i + INDICE_COMPONENTE_NOMESUINA);
                    //               JTextField txtTaxaOperacao = (JTextField) pnlUsinasOperacao.getComponent(i + INDICE_COMPONENTE_TAXAOPERACAO);

                    JComboBox cmbTipoProduto = (JComboBox) pnlFiltragemOperacao.getComponent(i
                                    + INDICE_COMPONENTE_TIPOPRODUTO);
                    TipoProduto tipoProdutoSelecionado = (TipoProduto) cmbTipoProduto.getSelectedItem();

                    MetaFiltragem usinaSelecionada = buscarFiltragemNaListaPeloNome(lblNomeUsina.getText());
                    //               usinaSelecionada.setTaxaDeOperacao(DSSStockyardFuncoesNumeros.getStringToDouble(txtTaxaOperacao.getText()));

                    HashMap<Integer, Campanha> itemCampanha = mapComboCampanhaFiltragem.get(usinaSelecionada);
                    Campanha campanhaUsina = itemCampanha.get(cmbTipoProduto.getSelectedIndex());
                    mapFiltragemCampanha.put(usinaSelecionada, campanhaUsina);

                    listaFiltragemSelecionada.add(usinaSelecionada);
                }
            }
        }
    }

   
    /**
     * Pesquisa uma usina na lista de usinas do DSP pelo nome
     * @param nomeUsina
     * @return
     */
    private Usina buscarUsinaNaListaPeloNome(String nomeUsina) {
        Usina usina = null;
        for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
            if (interfaceUsina.getUsinaVisualizada().getNomeUsina().toUpperCase().equals(nomeUsina.toUpperCase())) {
                usina = interfaceUsina.getUsinaVisualizada();
                break;
            }
        }
        return usina;
    }

    /**
     * Pesquisa uma usina na lista de usinas do DSP pelo nome
     * @param nomeUsina
     * @return
     */
    private MetaFiltragem buscarFiltragemNaListaPeloNome(String nomeUsina) {
        MetaFiltragem usina = null;
        for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
            if (interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem().getNomeFiltragem().toUpperCase()
                            .equals(nomeUsina.toUpperCase())) {
                usina = interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem();
                break;
            }
        }
        return usina;
    }

    /**
     * Seleciona no painel de usinas as atividades campanhas
     * @param usina
     */
    private void selecionarUsinaPelaAtividade(Usina usina, TipoProduto tipoProduto) {
        for (int i = 0; i < pnlUsinasOperacao.getComponents().length; i++) {
            if (pnlUsinasOperacao.getComponent(i) instanceof JCheckBox) {
                JCheckBox chkUsina = (JCheckBox) pnlUsinasOperacao.getComponent(i);
                //            JTextField txtTaxaOperacao = (JTextField) pnlUsinasOperacao.getComponent(i + INDICE_COMPONENTE_TAXAOPERACAO);
                JComboBox cmbTipoProduto = (JComboBox) pnlUsinasOperacao.getComponent(i + INDICE_COMPONENTE_TIPOPRODUTO);

                String nomeComponente = "chkUsina_" + usina.getNomeUsina();
                if (chkUsina.getName().toLowerCase().equals(nomeComponente.toLowerCase())) {
                    if (finalizaAtividade) {
                        chkUsina.setSelected(false);
                        chkUsina.setEnabled(false);
                        //                  txtTaxaOperacao.setEditable(false);
                        cmbTipoProduto.setEnabled(false);
                    } else {
                        chkUsina.setSelected(true);
                    }

                    cmbTipoProduto.setSelectedItem(tipoProduto);
                    /*
                    
                    if (usina.getMetaUsina().getCampanhaAtual(atividadeExecutada.getDtInicio()).getTipoDeProdutoTemporario() != null
                                    && usina.getMetaUsina().getCampanhaAtual(atividadeExecutada.getDtInicio())
                                                    .getTipoDeProdutoTemporario().equals(TipoDeProdutoEnum.PELLET_FEED)) {
                        cmbTipoProduto.setSelectedItem(usina.getMetaUsina().getCampanhaAtual(
                                        atividadeExecutada.getDtInicio()).getTipoPellet());
                    } else if (usina.getMetaUsina().getCampanhaAtual(atividadeExecutada.getDtInicio())
                                    .getTipoDeProdutoTemporario() != null
                                    && usina.getMetaUsina().getCampanhaAtual(atividadeExecutada.getDtInicio())
                                                    .getTipoDeProdutoTemporario().equals(TipoDeProdutoEnum.PELLET_SCREENING)) {
                        cmbTipoProduto.setSelectedItem(usina.getMetaUsina().getCampanhaAtual(
                                        atividadeExecutada.getDtInicio()).getTipoScreening());
                    } else {
                        cmbTipoProduto.setSelectedItem(usina.getMetaUsina().getCampanhaAtual(
                                        atividadeExecutada.getDtInicio()).getTipoProduto());
                    }
                    *///               txtTaxaOperacao.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(usina.getTaxaDeOperacao(), 2));
                }
            }
        }
    }

    /**
     * carrega o combo proximaBalizaJCB com a lista de todas as balizas do patio que a maquina se encontra
     */
    private void carregaTodasBalizas(Date horaAtividade) {
        proximaBalizaJCB.removeAllItems();
        proximaBalizaJL.setText(PropertiesUtil.getMessage("label.inicio.empilhamento"));

        for (Baliza baliza : maquinaDoPatioEditada.getPosicao().getPatio().getListaDeBalizas(horaAtividade)) {           
                proximaBalizaJCB.addItem(baliza);
                if (baliza.getMetaBaliza().equals(balizaAtual.getMetaBaliza())) {
                    proximaBalizaJCB.setSelectedItem(baliza);

                }           
        }
        //muda o nome do label

        /*      if (balizaAtual != null) {
                  proximaBalizaJCB.setSelectedItem(balizaAtual);
              }*/
    }

    private void recuperaFiltragemSelecionada(List<Filtragem> filtragensAtividade, Atividade atividade) {

        mapFiltragensSelecionadas = new HashMap<MetaFiltragem, Boolean>();
        if (filtragensAtividade != null) {
            for (Filtragem f : filtragensAtividade) {
                if (f.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                    JCheckBox chkFiltragem = mapFiltragensSelecionaveis.get(f.getMetaFiltragem());
                    if (chkFiltragem != null) {
                        if (finalizaAtividade) {
                            chkFiltragem.setSelected(false);
                            chkFiltragem.setEnabled(false);
                            //                       txtTaxaOperacao.setEditable(false);                       
                        } else {
                            chkFiltragem.setSelected(true);
                            mapFiltragensSelecionadas.put(f.getMetaFiltragem(), Boolean.TRUE);
                        }
                    }
                }
            }
        }
    }

    private void habilitaModoFinalizaAtualizacao() {
        nomePilhaJTF.setEnabled(false);
        proximaBalizaJCB.setEnabled(false);

        carregarBalizaAtual();
        setaTipoOperacao();
        carregarDataHoraAtual();
        carregarNomePilha();
        carregarNomePatio();
    }

    private void horaEventoJTFTextChanged() {//GEN-FIRST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged
        try {
            if (horaEventoJTF.getText() != "") {
                Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(horaEventoJTF.getText(),
                                PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
                montaPainelUsinas(dataHoraOcorrenciaEvento);
                montaPainelFiltragem(dataHoraOcorrenciaEvento);
                //montaComboProdutoProduzido(dataHoraOcorrenciaEvento);            
            }
        } catch (ValidacaoCampoException e) {
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged

    /**
     * validarCampanhaDiferente
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return void
     */
    private Boolean validarCampanhaDiferente() {
        Boolean campanhaDiferente = Boolean.FALSE;
        StringBuilder campanhasUsinas = new StringBuilder("Campanhas diferentes !!\n");
        if (atividadeExecutada != null) {
            List<AtividadeCampanha> atvCampanha = atividadeExecutada.getListaDeAtividadesCampanha(); 
            List<Usina> usinas = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas();
            List<Filtragem> filtragens = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaFiltragens();

         
           if (usinas != null) {
                for (Usina usina : usinas) {
                    Campanha campanhaAtual = mapUsinaCampanha.get(usina.getMetaUsina());                                
                    if (campanhaAtual != null) {                                    
                        for(AtividadeCampanha atvCmp : atvCampanha) {
                            if (atvCmp.getNomeUsina().equals(usina.getNomeUsina()) && !atvCmp.getCampanha().equals(campanhaAtual)) {
                                campanhasUsinas.append("A Campanha selecionada : " + campanhaAtual.getNomeCampanha() + " para a Usina : " + usina.getNomeUsina() + " é diferente da campanha " + atvCmp.getCampanha().getNomeCampanha() + " selecionada no inicio da atividade ! \n" );
                                campanhaDiferente = Boolean.TRUE;
                            } 
                        }    
                    } else {						
						try {
							Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil
									.criaDataComString(
											horaEventoJTF.getText(),
											PropertiesUtil
													.buscarPropriedade("formato.campo.datahora"));
							campanhaAtual = usina.getMetaUsina()
									.getCampanhaAtual(dataHoraOcorrenciaEvento);
							for (AtividadeCampanha atvCmp : atvCampanha) {
								if (atvCmp.getNomeUsina().equals(
										usina.getNomeUsina())
										&& !atvCmp.getCampanha().equals(
												campanhaAtual)) {
									campanhasUsinas
											.append("A Campanha selecionada : "
													+ campanhaAtual
															.getNomeCampanha()
													+ " para a Usina : "
													+ usina.getNomeUsina()
													+ " é diferente da campanha "
													+ atvCmp.getCampanha()
															.getNomeCampanha()
													+ " selecionada no inicio da atividade ! \n");
									campanhaDiferente = Boolean.TRUE;
								}
							}
						} catch (ValidacaoCampoException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
           }
            if (filtragens != null) {
                for (Filtragem filtragem : filtragens) {                    
                	Campanha campanhaAtual = mapFiltragemCampanha.get(filtragem.getMetaFiltragem());                                
                    if (campanhaAtual != null) {                                    
                        for(AtividadeCampanha atvCmp : atvCampanha) {
                            if (atvCmp.getNomeUsina().equals(campanhaAtual.getMetaUsina().getNomeUsina()) && !atvCmp.getCampanha().equals(campanhaAtual)) {
                                campanhasUsinas.append("A Campanha selecionada : " + campanhaAtual.getNomeCampanha() + " para a Filtragem : " + filtragem.getMetaFiltragem().getNomeFiltragem() + " é diferente da campanha " + atvCmp.getCampanha().getNomeCampanha() + " selecionada no inicio da atividade ! \n" );
                                campanhaDiferente = Boolean.TRUE;
                            } 
                        }    
                    } else {
                    	Date dataHoraOcorrenciaEvento;
						try {
							dataHoraOcorrenciaEvento = DSSStockyardTimeUtil
							.criaDataComString(
									horaEventoJTF.getText(),
									PropertiesUtil
											.buscarPropriedade("formato.campo.datahora"));
							
							for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
	                            if (interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem().equals(filtragem.getMetaFiltragem())) {                            	
	                            	campanhaAtual =interfaceUsina.getUsinaVisualizada().getMetaUsina()
									.getCampanhaAtual(dataHoraOcorrenciaEvento);                            
	                            	break;
	                            }
	                    	}
							
							if (campanhaAtual != null) {
	                    		for(AtividadeCampanha atvCmp : atvCampanha) {
	                                if (atvCmp.getNomeUsina().equals(campanhaAtual.getMetaUsina().getNomeUsina()) && !atvCmp.getCampanha().equals(campanhaAtual)) {
	                                    campanhasUsinas.append("A Campanha selecionada : " + campanhaAtual.getNomeCampanha() + " para a Filtragem : " + filtragem.getMetaFiltragem().getNomeFiltragem() + " é diferente da campanha " + atvCmp.getCampanha().getNomeCampanha() + " selecionada no inicio da atividade ! \n" );
	                                    campanhaDiferente = Boolean.TRUE;
	                                } 
	                            }
	                    	}
						} catch (ValidacaoCampoException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}                    
                   }                
               }
            }
        }   
        if (campanhaDiferente) {
            JLabel pergunta = new JLabel(campanhasUsinas.toString());
            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
            JOptionPane.showOptionDialog(null,
                            //pergunta,
                            campanhasUsinas.toString(), PropertiesUtil.getMessage("popup.atencao"),
                            JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        }
    
        return campanhaDiferente;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balizaAtualJL;
    private javax.swing.JTextField balizaAtualJTF;
    private javax.swing.JPanel balizasJP;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton confirmarJB;
    private javax.swing.JButton desistirJB;
    private javax.swing.JLabel horaEventoJL;
    private javax.swing.JFormattedTextField horaEventoJTF;
    private javax.swing.JPanel horarioJP;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelChkFiltragem;
    private javax.swing.JLabel jLabelNomeFiltragem;
    private javax.swing.JLabel jLabelProdutoFiltragem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nomePilhaJL;
    private javax.swing.JTextField nomePilhaJTF;
    private javax.swing.JLabel patioJL;
    private javax.swing.JTextField patioJTF;
    private javax.swing.JPanel pnlFiltragemOperacao;
    private javax.swing.JPanel pnlUsinasOperacao;
    private javax.swing.JComboBox proximaBalizaJCB;
    private javax.swing.JLabel proximaBalizaJL;
    // End of variables declaration//GEN-END:variables
}