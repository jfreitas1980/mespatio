package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.RetornaObjetosDaInterfaceFilaDeNavios;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ObjetoPesquisadoNaoEncontradoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InterfacePesquisa extends javax.swing.JDialog {

    /**
     * serial UID 
     */
    private static final long serialVersionUID = 4478157970032187323L;
    
    /** interface com as informações de mensagem para exibição na tela */
    private InterfaceMensagem interfaceMensagem;
    /** Acesso ao controlador do subsistema de interface de comandos*/
    ControladorInterfaceComandos controladorInterfaceComandos;
    /** lista com os tipos de produto usados na pesquisa de campanha de produção */
    List<TipoProduto> listaTiposProduto;
    //formatar o campo dataHora
    private SimpleDateFormat formatter = new SimpleDateFormat(PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
    private Navio navioProcurado;    
    //lista de maquinas
    List<MaquinaDoPatio> listaMaquinasCadastradas;
    
    private final Integer COL_EQUIPAMENTO = 0;
    private final Integer COL_STATUS = 1;
    private final Integer COL_HORARIO_INICIAL = 2;
    private final Integer COL_HORARIO_FINAL = 3;
    private final Integer COL_PATIO = 4;
    private final Integer COL_BALIZA = 5;
    private List<ColunaTabela> listaColunaInformacoes;
    private Vector vInformacoesResultadoManutencao;
    private List<MaquinaDoPatio> listaMaquinasCadastradasManutencao;
    private boolean pesquisaManutencaoMaquina = Boolean.FALSE;
    private boolean pesquisaManutencaoCorreia = Boolean.FALSE;


    @SuppressWarnings("unchecked")
    public InterfacePesquisa(java.awt.Frame parent, boolean modal) throws ErroSistemicoException {
        super(parent, modal);
        initComponents();
        
        vInformacoesResultadoManutencao = new Vector();
        criaColunasInformacoesBalizasSelecionadas();
        carregaDadosManutencaoPesquisa(new ArrayList());
        rbTodosEquipamentosActionPerformed(null);
    }

    /**
     * verifica se o ETA do navio pesquisado é igual ao ETA procurado
     * @param navio
     * @param etaProcurado
     * @return verdadeiro se encontrou, false caso contrario
     */
    private boolean buscaNavioPorData(Navio navio, Date intervaloEtaInicial, Date intervaloEtaFinal)
    {//a procura esta sendo por data
        boolean resultado;

        if(navio.getEta().compareTo(intervaloEtaInicial) >= 0
                && navio.getEta().compareTo(intervaloEtaFinal) <= 0)
        {//verifica se a data do ETA do navio esta dentro do intervalo desejado
            resultado = true;
        }
        else{
            resultado = false;
        }
        return resultado;
    }

    /**
     * Inicializa a pesquisa de maquinas carregando a lista de maquinas
     *
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void carregarFiltroPesquisarMaquinas() throws ErroSistemicoException {
        inicializaPesquisaMaquinas();

        listaMaquinasCadastradas = new ArrayList<MaquinaDoPatio>();

        // adiciona a lista de maquinas todas as empilhadeiras cadastradas
        /* carrega todas as maquinas registradas no banco*/
        //listaMaquinasCadastradas =  controladorInterfaceComandos.getInterfaceInicial().getInterfaceInicial().getControladorModelo().buscarListaMaquinas();
        SituacaoPatio situacaoLocal = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal();
        for(MaquinaDoPatio maquina : situacaoLocal.getPlanta().getListaMaquinasDoPatio(situacaoLocal.getDtInicio()))
        {//carrega apenas as maquinas da memoria 
            listaMaquinasCadastradas.add(maquina);
        }
        // adiciona o elemento 0 no combo
        cmbMaquinas.addItem(PropertiesUtil.getMessage("combo.selecionar"));

        // carrega o combo com as maquinas
        HashMap<String, MaquinaDoPatio> listaCombo = new HashMap<String, MaquinaDoPatio>();

        // Busca apenas as maquinas unicas.
      for (MaquinaDoPatio maquina : listaMaquinasCadastradas)
      {
         listaCombo.put(maquina.getNomeMaquina(), maquina);
      }
      //Popula o combobox apenas com 1 maquina de cada tipo.
      for (MaquinaDoPatio maquina : listaCombo.values())
      {
         cmbMaquinas.addItem(maquina);
      }
   }
    
    public void carregarFiltroPesquisarManutencaoMaquina() throws ErroSistemicoException {
//        inicializaPesquisaMaquinas();
        listaMaquinasCadastradasManutencao = new ArrayList<MaquinaDoPatio>();

        // adiciona a lista de maquinas todas as empilhadeiras cadastradas
        /* carrega todas as maquinas registradas no banco*/
        SituacaoPatio situacaoLocal = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal(); 
        for(MaquinaDoPatio maquina : situacaoLocal.getPlanta().getListaMaquinasDoPatio(situacaoLocal.getDtInicio()))
        {//carrega apenas as maquinas da memoria
            if (!TipoMaquinaEnum.PA_CARREGADEIRA.equals(maquina.getTipoDaMaquina())) {
                listaMaquinasCadastradasManutencao.add(maquina);
            }
        }
        // adiciona o elemento 0 no combo
        cmbEquipamentos.addItem(PropertiesUtil.getMessage("combo.maquinas"));

        // carrega o combo com as maquinas
        HashMap<String, MaquinaDoPatio> listaCombo = new HashMap<String, MaquinaDoPatio>();

        // Busca apenas as maquinas unicas.
      for (MaquinaDoPatio maquina : listaMaquinasCadastradasManutencao)
      {
         listaCombo.put(maquina.getNomeMaquina(), maquina);
      }
      //Popula o combobox apenas com 1 maquina de cada tipo.
      for (MaquinaDoPatio maquina : listaCombo.values())
      {
         cmbEquipamentos.addItem(maquina);
      }
      //adiciona as correias para pesquisa
      cmbCorreiasManutencao.addItem(PropertiesUtil.getMessage("combo.correias"));
      SituacaoPatio situacaoPatio = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal();
      try {
        for(Correia correia : situacaoPatio.getPlanta().getListaCorreias(situacaoPatio.getDtInicio())){
              cmbCorreiasManutencao.addItem(correia);
          }
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
      
   }

    /**
     * carrega os dados para a pesquisa de navios
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void carregarFiltroPesquisaNavios() throws ErroSistemicoException{
        // A pesquisa de Navio foi desenvolvida na SA8552, UC114
        /** De acordo com o Marcelo (Samarco) a pesquisa de navio deve conter apenas o ETA (data/hora) como campo de entrada para a pesquisa de
         navio, e apresentar todos os navios que cheguem naquela data
         SA9148*/
        limpaPesquisaNavio();
      
    }

    /**
     * Inicializa a pesquisa de campanha de produção carregando os tipos de produto e limpando 
     * as datas.
     * 
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void carregarFiltroPesquisarCampanha() throws ErroSistemicoException {
        perIniCalendarioCFlex.limpaData();
        perFimCalendarioCFlex.limpaData();
        pnlResultadoPesquisa.setVisible(false);
        //Darley Retirando chamada remota
//        IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
        IControladorModelo controladorModelo = new ControladorModelo();
        listaTiposProduto = controladorModelo.buscarTiposProduto(new TipoProduto());
        controladorModelo = null;
        cmbProdutoProduzido.addItem(PropertiesUtil.getMessage("combo.selecionar"));
        for (TipoProduto tipoProduto : listaTiposProduto) {
            cmbProdutoProduzido.addItem(tipoProduto);
        }
        perIniCalendarioCFlex.requestFocus();
    }

    /**
     * Pesquisar um item nas Situações de Pátio apresentadas
     * @return List<Integer> - indices das situacoes encontradas
     */
    public List<Integer> pesquisar() {
        throw new UnsupportedOperationException();
    }

    /**
     * Busca a campanha especificada nas Situações de Pátio disponíveis
     * @param campanhaProcurada a Campanha a ser pesquisada
     * @return List<Integer> - indices das situacoes encontradas
     */
    public List<SituacaoPatio> buscarCampanha(Date dataInicial, Date dataFinal, TipoProduto produtoProduzido) {
        List<SituacaoPatio> listaSituacoesEncontradas = new ArrayList<SituacaoPatio>();

        // Obtem todas as situacoes de patio que estao no slider que serao usadas para pesquisa
        for (SituacaoPatio situacaoPatio : controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().obterSituacoesDePatio()) {

            // vefifica se a situacao esta dentro do periodo digitado
            if (situacaoPatio.getDtInicio().getTime() >= dataInicial.getTime()
                    && situacaoPatio.getDtInicio().getTime() <= dataFinal.getTime()) {

                // percorre as usinas da situacao para verificar a campanha e o produto
                for (Usina usinaSituacao : situacaoPatio.getPlanta().getListaUsinas(situacaoPatio.getDtInicio())) {
                    // varifica se o produto produzido na campanha da usina é igual ao produto selecinado.
                    if (usinaSituacao.getMetaUsina().getCampanhaAtual(situacaoPatio.getDtInicio()).getTipoProduto().getIdTipoProduto().equals(produtoProduzido.getIdTipoProduto())) {
                        listaSituacoesEncontradas.add(situacaoPatio);
                        break;
                    }
                }
            }
        }

        return listaSituacoesEncontradas;
    }
   
   /**
    * busca uma determinada máquina baseado em suas características e/ou informações de estado e posição
    * 
    * @param maquinaPesquisada
    *           a máquina buscada
    * @throws ErroSistemicoException 
    * @throws ValidacaoCampoException 
    */
    public List<MaquinaDoPatio> buscarMaquina(Date dataInicial, Date dataFinal) throws ErroSistemicoException, ValidacaoCampoException
    {
       //Procura a maquina cadastrada nas diversas situacoes gravadas.
        //HashMap<Long, MaquinaDoPatio> maquinasPesquisadas = new HashMap<Long, MaquinaDoPatio>();
        List<MaquinaDoPatio> result = new ArrayList<MaquinaDoPatio>();
        MaquinaDoPatio maquinaItem = (MaquinaDoPatio)cmbMaquinas.getSelectedItem();
        /*for(MaquinaDoPatio maquinaDoPatio : listaMaquinasCadastradas){
            if(maquinaDoPatio.getMetaMaquina().equals(maquinaItem.getMetaMaquina())){
                    maquinasPesquisadas.put(maquinaDoPatio.getIdMaquina(), maquinaDoPatio);
            }
        }*/
     //  Date dataPesquisa = DSSStockyardTimeUtil.criaDataComString(dtInicioMaquinaDataHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
       
       
       
       // Obtem todas as situacoes de patio que estao no slider que serao usadas para pesquisa
       for (SituacaoPatio situacaoPatio : controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().obterSituacoesDePatio()) {

           // vefifica se a situacao esta dentro do periodo digitado
           if (situacaoPatio.getDtInicio().getTime() >= dataInicial.getTime()
                   && situacaoPatio.getDtInicio().getTime() <= dataFinal.getTime()) {               
               for (MaquinaDoPatio maquina : situacaoPatio.getPlanta().getListaMaquinasDoPatio(situacaoPatio.getDtInicio()))
               {
                   if(maquina.getMetaMaquina().equals(maquinaItem.getMetaMaquina())){                           
                       if (!result.contains(maquina))  result.add(maquina);
                   }
               }              
           }
       }
/*
       
       
       // TODO - LUCHETTA
       
       // Fazer com que o gerador de meta situações retorne a situacao na data informada
       SituacaoPatio situacaoPatio = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().obterSituacaoDePatio(dataPesquisa);
       
       MaquinaDoPatio maquinaRetorno=null;
       MaquinaDoPatio maquinaSelecionada = (MaquinaDoPatio)cmbMaquinas.getSelectedItem();
       for (MaquinaDoPatio maquina : situacaoPatio.getPlanta().getListaMaquinasDoPatio(situacaoPatio.getDtInicio()))
       {
           // De acordo com a situacao buscada, procura a maquina correspondente a essa situacao
//          if (maquinasPesquisadas.containsKey(maquina.getIdMaquina()))
//          {
//             maquinaRetorno = maquina;
//          }
           if(maquina.getNomeMaquina().equalsIgnoreCase(maquinaSelecionada.getNomeMaquina())
                   && maquina.getTipoDaMaquina().equals(maquinaSelecionada.getTipoDaMaquina())){
               maquinaRetorno = maquina;
           }
       }
       return maquinaRetorno;
*/
    return result;    
    
    }

    /**
     * busca pelo navio na fila de navios e apresenta os dados do mesmo
     * @return
     */
    public List<Navio> buscarNavio() throws ValidacaoCampoException {
        RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();
        Date intervaloEtaInicio = roifn.setObjetoData(etaCalendarioInicio.getDataFormatada());
        Date intervaloEtaFinal = roifn.setObjetoData(etaCalendarioFinal.getDataFormatada());
        
        //lista dos navios retornados que satisfazem o criterio de pesquisa (ETA)
        List<Navio> listaNaviosEncontrados = new ArrayList<Navio>();

        for(Navio navio: controladorInterfaceComandos.getInterfaceComandos().getInterfaceInicial().getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila()){
                if (buscaNavioPorData(navio, intervaloEtaInicio, intervaloEtaFinal)) {
                    listaNaviosEncontrados.add(navio);
                }
        }
        return listaNaviosEncontrados;
    }


    public ControladorInterfaceComandos getControladorInterfaceComandos() {
        return controladorInterfaceComandos;
    }

    public void setControladorInterfaceComandos(ControladorInterfaceComandos controladorInterfaceComandos) {
        this.controladorInterfaceComandos = controladorInterfaceComandos;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        pnlInterfacePesquisa = new javax.swing.JPanel();
        tbpInterfacePesquisa = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        pnlFiltroPesquisa = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbProdutoProduzido = new javax.swing.JComboBox();
        cmdLimparDados = new javax.swing.JButton();
        cmdPesquisar = new javax.swing.JButton();
        cmdEncerrarPesquisa = new javax.swing.JButton();
        pnlResultadoPesquisa = new javax.swing.JPanel();
        scrArvoreResultado = new javax.swing.JScrollPane();
        arvoreResultado = new javax.swing.JTree();
        
        scrArvoreResultadoMaquina = new javax.swing.JScrollPane();
        arvoreResultadoMaquina = new javax.swing.JTree();
        
        cmdExibirSituacao = new javax.swing.JButton();
        pnlPesquisarMaquinas = new javax.swing.JPanel();
        pnlFiltro = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbMaquinas = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmdLimparPesquisaMaquina = new javax.swing.JButton();
        cmdPesquisarMaquina = new javax.swing.JButton();
        cmdEncerrarPesquisaMaquina = new javax.swing.JButton();
        pnlResultadoPesquisaMaquinas = new javax.swing.JPanel();
        //jLabel6 = new javax.swing.JLabel();
        //jLabel7 = new javax.swing.JLabel();
        //lblResultadoPosicionamento = new javax.swing.JLabel();
        //lblResultadoEstadoMaquina = new javax.swing.JLabel();
        pnlPesquisarNavios = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jbEncerrar = new javax.swing.JButton();
        jbPesquisar = new javax.swing.JButton();
        jbLimpar = new javax.swing.JButton();
        pnlResultadoNavio = new javax.swing.JPanel();
        jlNomeNavio = new javax.swing.JLabel();
        jlEtaResultado = new javax.swing.JLabel();
        jlQuantidadeProgEmbarque = new javax.swing.JLabel();
        jlTipoProduto = new javax.swing.JLabel();
        jlCliente = new javax.swing.JLabel();
        jtNome = new javax.swing.JTextField();
        jtquantidadeEmbarcada = new javax.swing.JTextField();
        jftEta = new javax.swing.JFormattedTextField();
        jtTipoProduto = new javax.swing.JTextField();
        jtNomecliente = new javax.swing.JTextField();
        pnlArvoreResultadoNavio = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtResultadoNavios = new javax.swing.JTree();
        jcTipoProduto = new javax.swing.JComboBox();
      //datas
        etaCalendarioInicio = new com.hdntec.gestao.cliente.util.datahora.CalendarioCFlex();
        etaCalendarioFinal = new com.hdntec.gestao.cliente.util.datahora.CalendarioCFlex();
        perIniCalendarioCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioCFlex();
        perFimCalendarioCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioCFlex();
        dtInicioMaquinaDataHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        dtFinalMaquinaDataHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        //painel de pesquisa de manutencoes
        pnlPesquisarManutencao = new javax.swing.JPanel();
        pnlFiltroManutencao = new javax.swing.JPanel();
        jlEquipamento = new javax.swing.JLabel();
        jlCorreiaManutencao = new javax.swing.JLabel();
        cmbEquipamentos = new javax.swing.JComboBox();
        cmbCorreiasManutencao = new javax.swing.JComboBox();
        jlDataInicio = new javax.swing.JLabel();
        jldataFinal = new javax.swing.JLabel();
        dtInicioEquipamentoManutencao = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        dtFinalManutencaoEquipamento = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        pnlInfEquipamentoPesquisado = new javax.swing.JPanel();
        tblInformacoesEquipamentoPesquisado = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        scrInfEquipamentoPesquisado = new javax.swing.JScrollPane();
        cmdEncerrarPesquisaManutencao = new javax.swing.JButton();
        cmdLimparPesquisaManutencao = new javax.swing.JButton();
        cmdPesquisarManutencao = new javax.swing.JButton();
        rbCorreia = new javax.swing.JRadioButton();
        rbMaquina = new javax.swing.JRadioButton();
        rbTodosEquipamentos = new javax.swing.JRadioButton();
        grupoBotoes = new javax.swing.ButtonGroup();        
        
        jToggleButton1.setText("jToggleButton1");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Interface de Pesquisa");

        pnlInterfacePesquisa.setLayout(new java.awt.BorderLayout());

        tbpInterfacePesquisa.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tbpInterfacePesquisa.setPreferredSize(new java.awt.Dimension(367, 563));

        jPanel1.setMaximumSize(new java.awt.Dimension(327, 327));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel1.setLayout(null);

        pnlFiltroPesquisa.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro para Pesquisa de Campanha"));
        pnlFiltroPesquisa.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setText("Período Inicial:");
        pnlFiltroPesquisa.add(jLabel1);
        jLabel1.setBounds(20, 20, 102, 17);
        
        pnlFiltroPesquisa.add(perIniCalendarioCFlex);
        perIniCalendarioCFlex.setBounds(170, 20, 100, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel2.setText("Período Final:");
        pnlFiltroPesquisa.add(jLabel2);
        jLabel2.setBounds(20, 50, 93, 17);
        
        pnlFiltroPesquisa.add(perFimCalendarioCFlex);
        perFimCalendarioCFlex.setBounds(170, 50, 100, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel3.setText("Produto Produzido:");
        pnlFiltroPesquisa.add(jLabel3);
        jLabel3.setBounds(20, 80, 136, 17);

        cmbProdutoProduzido.setFont(new java.awt.Font("Tahoma", 0, 14));
        pnlFiltroPesquisa.add(cmbProdutoProduzido);
        cmbProdutoProduzido.setBounds(170, 80, 130, 23);

        jPanel1.add(pnlFiltroPesquisa);
        pnlFiltroPesquisa.setBounds(20, 10, 580, 110);

        cmdLimparDados.setFont(new java.awt.Font("Tahoma", 1, 12));
        StringBuffer value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/eraser.png");
        cmdLimparDados.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        cmdLimparDados.setText("Limpar Dados");
        cmdLimparDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLimparDadosActionPerformed(evt);
            }
        });
        jPanel1.add(cmdLimparDados);
        cmdLimparDados.setBounds(353, 150, 137, 25);

        cmdPesquisar.setFont(new java.awt.Font("Tahoma", 1, 12));
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/find.png");
        cmdPesquisar.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N        
        cmdPesquisar.setText("Buscar");
        cmdPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPesquisarActionPerformed(evt);
            }
        });
        jPanel1.add(cmdPesquisar);
        cmdPesquisar.setBounds(500, 150, 95, 25);

        cmdEncerrarPesquisa.setFont(new java.awt.Font("Tahoma", 1, 12));
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
        cmdEncerrarPesquisa.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        cmdEncerrarPesquisa.setText("Encerrar");
        cmdEncerrarPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEncerrarPesquisaActionPerformed(evt);
            }
        });
        jPanel1.add(cmdEncerrarPesquisa);
        cmdEncerrarPesquisa.setBounds(240, 150, 105, 25);

        pnlResultadoPesquisa.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultado da Pesquisa"));
        pnlResultadoPesquisa.setLayout(null);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        arvoreResultado.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        scrArvoreResultado.setViewportView(arvoreResultado);

        pnlResultadoPesquisa.add(scrArvoreResultado);
        scrArvoreResultado.setBounds(10, 20, 560, 130);

        cmdExibirSituacao.setFont(new java.awt.Font("Tahoma", 1, 12));
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/xeyes.png");
        cmdExibirSituacao.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        cmdExibirSituacao.setText("Ir Para Situação");
        cmdExibirSituacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExibirSituacaoActionPerformed(evt);
            }
        });
        pnlResultadoPesquisa.add(cmdExibirSituacao);
        cmdExibirSituacao.setBounds(415, 160, 152, 25);

        jPanel1.add(pnlResultadoPesquisa);
        pnlResultadoPesquisa.setBounds(20, 210, 580, 190);

        tbpInterfacePesquisa.addTab("Campanhas de Produção", jPanel1);

        pnlPesquisarMaquinas.setLayout(null);

        pnlFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro para Pesquisa de Máquinas"));
        pnlFiltro.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel4.setText("Máquina:");
        pnlFiltro.add(jLabel4);
        jLabel4.setBounds(20, 50, 63, 17);

        cmbMaquinas.setFont(new java.awt.Font("Tahoma", 0, 14));
        pnlFiltro.add(cmbMaquinas);
        cmbMaquinas.setBounds(93, 48, 180, 23);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel5.setText("Data inicial:");
        pnlFiltro.add(jLabel5);
        jLabel5.setBounds(280, 20, 79, 17);
        
        pnlFiltro.add(dtInicioMaquinaDataHoraCFlex);
        dtInicioMaquinaDataHoraCFlex.setBounds(380, 20, 200, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel11.setText("Data final:");
        pnlFiltro.add(jLabel11);
        jLabel11.setBounds(280, 50, 69, 17);
        
        pnlFiltro.add(dtFinalMaquinaDataHoraCFlex);
        dtFinalMaquinaDataHoraCFlex.setBounds(380, 50, 200, 20);

        pnlPesquisarMaquinas.add(pnlFiltro);
        pnlFiltro.setBounds(10, 30, 590, 80);

        cmdLimparPesquisaMaquina.setFont(new java.awt.Font("Tahoma", 1, 12));
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/eraser.png");
        cmdLimparPesquisaMaquina.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N

        
        cmdLimparPesquisaMaquina.setText("Limpar dados");
        cmdLimparPesquisaMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLimparPesquisaMaquinaActionPerformed(evt);
            }
        });
        pnlPesquisarMaquinas.add(cmdLimparPesquisaMaquina);
        cmdLimparPesquisaMaquina.setBounds(190, 130, 137, 25);

        cmdPesquisarMaquina.setFont(new java.awt.Font("Tahoma", 1, 12));
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/find.png");
        cmdPesquisarMaquina.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N

        cmdPesquisarMaquina.setText("Pesquisar");
        cmdPesquisarMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPesquisarMaquinaActionPerformed(evt);
            }
        });
        pnlPesquisarMaquinas.add(cmdPesquisarMaquina);
        cmdPesquisarMaquina.setBounds(480, 130, 115, 25);

        cmdEncerrarPesquisaMaquina.setFont(new java.awt.Font("Tahoma", 1, 12));
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
        cmdEncerrarPesquisaMaquina.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N        
        cmdEncerrarPesquisaMaquina.setText("Encerrar");
        cmdEncerrarPesquisaMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEncerrarPesquisaMaquinaActionPerformed(evt);
            }
        });
        pnlPesquisarMaquinas.add(cmdEncerrarPesquisaMaquina);
        cmdEncerrarPesquisaMaquina.setBounds(350, 130, 106, 25);

        pnlResultadoPesquisaMaquinas.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultado da Pesquisa"));
        pnlResultadoPesquisaMaquinas.setLayout(null);

        //jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14));
        //jLabel6.setText("Posicionamento da Máquina:");
        //pnlResultadoPesquisaMaquinas.add(jLabel6);
        //jLabel6.setBounds(10, 80, 197, 17);

        //jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14));
        //jLabel7.setText("Estado da Máquina:");
        //pnlResultadoPesquisaMaquinas.add(jLabel7);
        //jLabel7.setBounds(10, 50, 136, 17);

        //lblResultadoPosicionamento.setFont(new java.awt.Font("Tahoma", 1, 14));
        //lblResultadoPosicionamento.setForeground(java.awt.Color.blue);
        //lblResultadoPosicionamento.setText("tes");

        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("root");
        arvoreResultadoMaquina.setModel(new javax.swing.tree.DefaultTreeModel(treeNode3));
        scrArvoreResultadoMaquina.setViewportView(arvoreResultadoMaquina);
        pnlResultadoPesquisaMaquinas.add(scrArvoreResultadoMaquina);
        scrArvoreResultadoMaquina.setBounds(10, 20, 560, 130);

        
        
        
        
        
        //pnlResultadoPesquisaMaquinas.add(lblResultadoPosicionamento);
       // lblResultadoPosicionamento.setBounds(230, 50, 100, 17);

       // lblResultadoEstadoMaquina.setFont(new java.awt.Font("Tahoma", 1, 14));
       // lblResultadoEstadoMaquina.setForeground(java.awt.Color.blue);
       // lblResultadoEstadoMaquina.setText("est");
       // pnlResultadoPesquisaMaquinas.add(lblResultadoEstadoMaquina);
       // lblResultadoEstadoMaquina.setBounds(230, 80, 100, 17);

        pnlPesquisarMaquinas.add(pnlResultadoPesquisaMaquinas);
        pnlResultadoPesquisaMaquinas.setBounds(10, 190, 590, 130);

        tbpInterfacePesquisa.addTab("Pesquisar Máquinas", pnlPesquisarMaquinas);
        
        /** PESQUISA DE MANUTENCOES*/
        pnlPesquisarManutencao.setLayout(null);
        
        grupoBotoes.add(rbCorreia);
        grupoBotoes.add(rbMaquina);
        grupoBotoes.add(rbTodosEquipamentos);
        
        rbMaquina.setText(PropertiesUtil.getMessage("radio.maquinas"));
        rbMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMaquinaActionPerformed(evt);
            }
        });
        pnlPesquisarManutencao.add(rbMaquina);
        rbMaquina.setBounds(10, 10, 100, 12);
        
        rbCorreia.setText(PropertiesUtil.getMessage("radio.correias"));
        rbCorreia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCorreiaActionPerformed(evt);
            }
        });
        pnlPesquisarManutencao.add(rbCorreia);
        rbCorreia.setBounds(150, 10, 80, 12);
        
        rbTodosEquipamentos.setText(PropertiesUtil.getMessage("combo.todos.equipamentos"));
        rbTodosEquipamentos.setSelected(true);
        rbTodosEquipamentos.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTodosEquipamentosActionPerformed(evt);
            }
        });
        pnlPesquisarManutencao.add(rbTodosEquipamentos);
        rbTodosEquipamentos.setBounds(300, 10, 170, 12);
        
        pnlFiltroManutencao.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro para Pesquisa de Manutenções"));
        pnlFiltroManutencao.setLayout(null);

        jlEquipamento.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlEquipamento.setText("Máquina:");
        pnlFiltroManutencao.add(jlEquipamento);
        jlEquipamento.setBounds(15, 50, 70, 23);

        cmbEquipamentos.setFont(new java.awt.Font("Tahoma", 0, 12));
        pnlFiltroManutencao.add(cmbEquipamentos);
        cmbEquipamentos.setBounds(90, 48, 180, 23);
        
        jlCorreiaManutencao.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlCorreiaManutencao.setText("Correia:");
        pnlFiltroManutencao.add(jlCorreiaManutencao);
        jlCorreiaManutencao.setBounds(15, 22, 70, 23);
        
        cmbCorreiasManutencao.setFont(new java.awt.Font("Tahoma", 0, 12));
        pnlFiltroManutencao.add(cmbCorreiasManutencao);
        cmbCorreiasManutencao.setBounds(90, 20, 150, 23);

        jlDataInicio.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlDataInicio.setText("Data inicial:");
        pnlFiltroManutencao.add(jlDataInicio);
        jlDataInicio.setBounds(280, 20, 79, 17);
        
        pnlFiltroManutencao.add(dtInicioEquipamentoManutencao);
        dtInicioEquipamentoManutencao.setBounds(380, 20, 200, 20);

        jldataFinal.setFont(new java.awt.Font("Tahoma", 1, 12));
        jldataFinal.setText("Data final:");
        pnlFiltroManutencao.add(jldataFinal);
        jldataFinal.setBounds(280, 50, 69, 17);
        
        pnlFiltroManutencao.add(dtFinalManutencaoEquipamento);
        dtFinalManutencaoEquipamento.setBounds(380, 50, 200, 20);

        pnlPesquisarManutencao.add(pnlFiltroManutencao);
        pnlFiltroManutencao.setBounds(10, 40, 590, 80);
        
        cmdLimparPesquisaManutencao.setFont(new java.awt.Font("Tahoma", 1, 12));
        //cmdLimparPesquisaManutencao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/eraser.png"))); // NOI18N
        cmdLimparPesquisaManutencao.setText("Limpar dados");
        cmdLimparPesquisaManutencao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLimparPesquisaManutencaoActionPerformed(evt);
            }
        });
        pnlPesquisarManutencao.add(cmdLimparPesquisaManutencao);
        cmdLimparPesquisaManutencao.setBounds(350, 130, 137, 25);

        cmdPesquisarManutencao.setFont(new java.awt.Font("Tahoma", 1, 12));
        //cmdPesquisarManutencao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/find.png"))); // NOI18N
        cmdPesquisarManutencao.setText("Pesquisar");
        cmdPesquisarManutencao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPesquisarManutencaoActionPerformed(evt);
            }
        });
        pnlPesquisarManutencao.add(cmdPesquisarManutencao);
        cmdPesquisarManutencao.setBounds(480, 130, 115, 25);

        cmdEncerrarPesquisaManutencao.setFont(new java.awt.Font("Tahoma", 1, 12));
        //cmdEncerrarPesquisaManutencao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        cmdEncerrarPesquisaManutencao.setText("Encerrar");
        cmdEncerrarPesquisaManutencao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEncerrarPesquisaManutencaoActionPerformed(evt);
            }
        });
        pnlPesquisarManutencao.add(cmdEncerrarPesquisaManutencao);
        cmdEncerrarPesquisaManutencao.setBounds(190, 130, 106, 25);
        
        //tabela resultados pesquisa manutencoes de equipamentos
        pnlInfEquipamentoPesquisado.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações equipamentos pesquisados"));
        pnlInfEquipamentoPesquisado.setLayout(new java.awt.BorderLayout());
        tblInformacoesEquipamentoPesquisado.setModel(new javax.swing.table.DefaultTableModel(new Object [][] {},new String [] {}));
        scrInfEquipamentoPesquisado.setViewportView(tblInformacoesEquipamentoPesquisado);
        pnlInfEquipamentoPesquisado.add(scrInfEquipamentoPesquisado, java.awt.BorderLayout.CENTER);
        
        pnlPesquisarManutencao.add(pnlInfEquipamentoPesquisado);
        pnlInfEquipamentoPesquisado.setBounds(10, 200, 590, 200);
        
        tbpInterfacePesquisa.addTab("Pesquisar Manutenções", pnlPesquisarManutencao);

        /** PESQUISA DE NAVIOS */
        pnlPesquisarNavios.setLayout(null);
        
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro para Pesquisa de Navios"));
        jPanel2.setLayout(null);

        jLabel10.setText("ETA");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(30, 30, 30, 14);

        jLabel8.setText("Intervalo final");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(270, 50, 90, 14);
        
        jPanel2.add(etaCalendarioFinal);
        etaCalendarioFinal.setBounds(350, 45, 90, 20);

        jLabel9.setText("Intervalo inicial");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(30, 50, 90, 14);
        
        jPanel2.add(etaCalendarioInicio);
        etaCalendarioInicio.setBounds(120, 45, 90, 20);

        pnlPesquisarNavios.add(jPanel2);
        jPanel2.setBounds(10, 30, 580, 70);

        jbEncerrar.setFont(new java.awt.Font("Tahoma", 1, 12));
        //jbEncerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        jbEncerrar.setText("Encerrar");
        jbEncerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEncerrarActionPerformed(evt);
            }
        });
        pnlPesquisarNavios.add(jbEncerrar);
        jbEncerrar.setBounds(220, 110, 110, 25);

        jbPesquisar.setFont(new java.awt.Font("Tahoma", 1, 12));
        //jbPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/find.png"))); // NOI18N
        jbPesquisar.setText("Pesquisar");
        jbPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPesquisarActionPerformed(evt);
            }
        });
        pnlPesquisarNavios.add(jbPesquisar);
        jbPesquisar.setBounds(480, 110, 113, 25);

        jbLimpar.setFont(new java.awt.Font("Tahoma", 1, 12));
        //jbLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/eraser.png"))); // NOI18N
        jbLimpar.setText("Limpar dados");
        jbLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLimparActionPerformed(evt);
            }
        });
        pnlPesquisarNavios.add(jbLimpar);
        jbLimpar.setBounds(336, 110, 136, 25);

        pnlResultadoNavio.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados do Navio Selecionado"));
        pnlResultadoNavio.setLayout(null);

        jlNomeNavio.setText("Nome do navio");
        pnlResultadoNavio.add(jlNomeNavio);
        jlNomeNavio.setBounds(20, 65, 100, 14);

        jlEtaResultado.setText("ETA");
        pnlResultadoNavio.add(jlEtaResultado);
        jlEtaResultado.setBounds(20, 42, 30, 14);

        jlQuantidadeProgEmbarque.setText("Quantidade programada embarque");
        pnlResultadoNavio.add(jlQuantidadeProgEmbarque);
        jlQuantidadeProgEmbarque.setBounds(20, 87, 200, 14);

        jlTipoProduto.setText("Tipo de produto");
        pnlResultadoNavio.add(jlTipoProduto);
        jlTipoProduto.setBounds(20, 110, 100, 14);

        jlCliente.setText("Cliente");
        pnlResultadoNavio.add(jlCliente);
        jlCliente.setBounds(20, 135, 50, 14);

        jtNome.setEnabled(false);
        pnlResultadoNavio.add(jtNome);
        jtNome.setBounds(230, 60, 120, 20);

        jtquantidadeEmbarcada.setEnabled(false);
        pnlResultadoNavio.add(jtquantidadeEmbarcada);
        jtquantidadeEmbarcada.setBounds(230, 85, 120, 20);

        jftEta.setEnabled(false);
        pnlResultadoNavio.add(jftEta);
        jftEta.setBounds(230, 35, 120, 20);

        jtTipoProduto.setEnabled(false);
        pnlResultadoNavio.add(jtTipoProduto);
        jtTipoProduto.setBounds(230, 110, 120, 20);

        jtNomecliente.setEnabled(false);
        pnlResultadoNavio.add(jtNomecliente);
        jtNomecliente.setBounds(230, 135, 120, 20);

        pnlPesquisarNavios.add(pnlResultadoNavio);
        pnlResultadoNavio.setBounds(30, 310, 570, 160);

        pnlArvoreResultadoNavio.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jtResultadoNavios.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultado da Pesquisa"));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jtResultadoNavios.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jtResultadoNavios.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jtResultadoNaviosValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jtResultadoNavios);

        javax.swing.GroupLayout pnlArvoreResultadoNavioLayout = new javax.swing.GroupLayout(pnlArvoreResultadoNavio);
        pnlArvoreResultadoNavio.setLayout(pnlArvoreResultadoNavioLayout);
        pnlArvoreResultadoNavioLayout.setHorizontalGroup(
            pnlArvoreResultadoNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlArvoreResultadoNavioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlArvoreResultadoNavioLayout.setVerticalGroup(
            pnlArvoreResultadoNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArvoreResultadoNavioLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlPesquisarNavios.add(pnlArvoreResultadoNavio);
        pnlArvoreResultadoNavio.setBounds(30, 170, 566, 131);

        tbpInterfacePesquisa.addTab("Pesquisar Navio", pnlPesquisarNavios);

        pnlInterfacePesquisa.add(tbpInterfacePesquisa, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(pnlInterfacePesquisa, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-633)/2, (screenSize.height-540)/2, 633, 540);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdLimparDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLimparDadosActionPerformed
        inicializaPesquisaCampanha();
    }//GEN-LAST:event_cmdLimparDadosActionPerformed

    private void cmdPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPesquisarActionPerformed
        try {
            pnlResultadoPesquisa.setVisible(false);

            validarFiltroPesquisaCampanha();

            Date dataPeriodoInicial = DSSStockyardTimeUtil.criaDataComString(perIniCalendarioCFlex.getDataFormatada(), PropertiesUtil.buscarPropriedade("formato.campo.data"));
            Date dataPeriodoFinal = DSSStockyardTimeUtil.criaDataComString(perFimCalendarioCFlex.getDataFormatada(), PropertiesUtil.buscarPropriedade("formato.campo.data"));

            List<SituacaoPatio> listaSituacoesEncontradas =
                    this.buscarCampanha(dataPeriodoInicial, dataPeriodoFinal, (TipoProduto)cmbProdutoProduzido.getSelectedItem());
            
            if (!listaSituacoesEncontradas.isEmpty()) {
                exibirResultaPesquisaCampanhaProducao(listaSituacoesEncontradas);
            } else {
                JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("mensagem.option.pane.nao.foram.encontrada.campanha.de.producao.para.exibicao"), PropertiesUtil.getMessage("mensagem.option.pane.alerta"), JOptionPane.WARNING_MESSAGE);
            }
        } catch (ValidacaoCampoException vcEx) {
            JOptionPane.showMessageDialog(this, vcEx.getMessage(), PropertiesUtil.getMessage("mensagem.option.pane.alerta"), JOptionPane.WARNING_MESSAGE);
        }
}//GEN-LAST:event_cmdPesquisarActionPerformed

    /**
     * Metodo que mostra a arvore com o resultado da pesquisa das campanhas de producao
     * encontrada.
     * @param listaSituacoesEncontradas
     */
    private void exibirResultaPesquisaCampanhaProducao(List<SituacaoPatio> listaSituacoesEncontradas) {

        pnlResultadoPesquisa.setVisible(true);

        // cria o root da arvore
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(PropertiesUtil.getMessage("titulo.situacoes.de.patio.encontradas"));

        // cria os filhos do root da arvore
        for (SituacaoPatio situacaoPatio : listaSituacoesEncontradas) {
            DefaultMutableTreeNode folhaArvore = new DefaultMutableTreeNode(situacaoPatio);
            root.add(folhaArvore);
        }
        arvoreResultado.setModel(new DefaultTreeModel(root));
    }

    private void cmdEncerrarPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEncerrarPesquisaActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cmdEncerrarPesquisaActionPerformed

    private void cmdExibirSituacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExibirSituacaoActionPerformed
        try {
            exibirSituacaoPesquisaCampanhaSelecionada();
        } catch (ValidacaoCampoException vcEx) {
            controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        }
    }//GEN-LAST:event_cmdExibirSituacaoActionPerformed

    private void cmdLimparPesquisaMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLimparPesquisaMaquinaActionPerformed
        inicializaPesquisaMaquinas();
    }//GEN-LAST:event_cmdLimparPesquisaMaquinaActionPerformed

    private void cmdEncerrarPesquisaMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEncerrarPesquisaMaquinaActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cmdEncerrarPesquisaMaquinaActionPerformed

    private void jbEncerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEncerrarActionPerformed
        // add your handling code here:
         this.setVisible(false);
    }//GEN-LAST:event_jbEncerrarActionPerformed

    private void jbPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPesquisarActionPerformed
        try {
            validaDadosNavio();
            List<Navio> listaDeNavios = this.buscarNavio();
            exibirResultadoDaPesquisaNavio(listaDeNavios);

        } catch (ObjetoPesquisadoNaoEncontradoException opex) {
            controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(opex.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);            
        } catch (ValidacaoCampoException ex) {
            controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);            
        }
    }//GEN-LAST:event_jbPesquisarActionPerformed

    /**
     *
     * @param listaDeNavios
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ObjetoPesquisadoNaoEncontradoException
     */
    private void exibirResultadoDaPesquisaNavio(List<Navio> listaDeNavios) throws ObjetoPesquisadoNaoEncontradoException{
        if(listaDeNavios == null || listaDeNavios.isEmpty()){
            List<String> paramMensagem = new ArrayList<String>();
            paramMensagem.add(PropertiesUtil.getMessage("aviso.navio.pesquisado"));
            throw new ObjetoPesquisadoNaoEncontradoException("exception.validacao.objeto.pesquisado.nao.encontrado", paramMensagem);
        }
        //seta painel que contem o Jtree para visible>TRUE
        pnlArvoreResultadoNavio.setVisible(Boolean.TRUE);

        //no raiz da arvore
        DefaultMutableTreeNode noRaiz = new DefaultMutableTreeNode(PropertiesUtil.getMessage("titulo.navio.encontrado"));
        //cria os nós folhas da arvore, filho do root(raiz)
        for(Navio navio : listaDeNavios){
            DefaultMutableTreeNode noFolha = new DefaultMutableTreeNode(navio);
            noRaiz.add(noFolha);
        }
        jtResultadoNavios.setModel(new DefaultTreeModel(noRaiz));

    }
    private void jbLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLimparActionPerformed
        limpaPesquisaNavio();
    }//GEN-LAST:event_jbLimparActionPerformed

    private void cmdPesquisarMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPesquisarMaquinaActionPerformed
        try {
            validarFiltroPesquisaMaquinas();
            Date dataInicio = DSSStockyardTimeUtil.criaDataComString(dtInicioMaquinaDataHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            Date dataFim = DSSStockyardTimeUtil.criaDataComString(dtFinalMaquinaDataHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            List<MaquinaDoPatio> maquinaPesquisada = buscarMaquina(dataInicio,dataFim);
            exibirResultadoPesquisaMaquina(maquinaPesquisada);
        } catch (ObjetoPesquisadoNaoEncontradoException obEx) {
            controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(obEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        } catch (ValidacaoCampoException vcEx) {
            controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        } catch (ErroSistemicoException errSis) {
            controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        }
    }//GEN-LAST:event_cmdPesquisarMaquinaActionPerformed

    private void jtResultadoNaviosValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jtResultadoNaviosValueChanged
        // TODO ação ao selecionar um item da arvore
        //retorna o objeto selecionado na arvore de resultados
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jtResultadoNavios.getLastSelectedPathComponent();
        //verifica se este objeto não esta nulo
        if(node == null){
            return; //em caso positivo significa que nenhum objeto foi selecionado
        }
        Object nodeInfo = node.getUserObject();
        //verifica se o no selecionado é um no folha
        if(node.isLeaf()){
            Navio navioSelecionado = (Navio) nodeInfo;
            limpaDadosTrocaNavio();
            apresentaDadosNavioSelecionado(navioSelecionado);
        }

    }//GEN-LAST:event_jtResultadoNaviosValueChanged

    /**
     * mostra os dados do navio selecionado no painel de dados do navio
     * @param navioSelecionado
     */
    private void apresentaDadosNavioSelecionado(Navio navioSelecionado){
        pnlResultadoNavio.setVisible(true);
        //exibe os dados na tela        
        jftEta.setText(formatter.format(navioSelecionado.getEta()));
        jtNome.setText(navioSelecionado.getNomeNavio());        
        jtquantidadeEmbarcada.setText(apresentaQuantidadeProgramadaEmbarque(navioSelecionado));
        if(navioSelecionado.getListaDeCargasDoNavio(navioSelecionado.getDtInicio()) != null){           
            for(Carga carga : navioSelecionado.getListaDeCargasDoNavio(navioSelecionado.getDtInicio())){
                jcTipoProduto.addItem(carga.getProduto().getTipoProduto());
            }           
        }
        if(navioSelecionado.getCliente() != null){
            jtNomecliente.setText(navioSelecionado.getCliente().getNomeCliente());
        }else{
            jtNomecliente.setText("");
        }
        
        
      
        //seleciona a aba na fila de navios onde o navio selecionado se encontra
        navioProcurado = navioSelecionado;
        exibirNaFilaNavioPesquisado();
        controladorInterfaceComandos.getInterfaceComandos().getInterfaceInicial().getInterfaceFilaDeNavios().getComponenteInterfaceFilaNavios().repaint();

    }
    
    private void exibirNaFilaNavioPesquisado(){
        if(navioProcurado != null){
            Integer index;
            Navio navio;
            for (index = 0; index <  controladorInterfaceComandos.getInterfaceComandos().getInterfaceInicial().getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila().size(); index++) {
                navio = controladorInterfaceComandos.getInterfaceComandos().getInterfaceInicial().getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila().get(index);
                if (navio.getNomeNavio().equalsIgnoreCase(navioProcurado.getNomeNavio()) && navio.getEta().equals(navioProcurado.getEta()) && navio.getCliente().getNomeCliente().equalsIgnoreCase(navioProcurado.getCliente().getNomeCliente())) {
                    controladorInterfaceComandos.getInterfaceComandos().getInterfaceInicial().getInterfaceFilaDeNavios().getComponenteInterfaceFilaNavios().setSelectedIndex(index);
                }
            }
        }
    }

    /**
     * Metodo auxiliar para o botao de limpar tela
     */
    private void limpaPesquisaNavio(){
        //dados entrada
        etaCalendarioInicio.limpaData();
        etaCalendarioFinal.limpaData();
        pnlResultadoNavio.setVisible(false);
        //dados saida
        jtquantidadeEmbarcada.setText("");
        jftEta.setText("");
        jcTipoProduto.removeAllItems();
        jtNomecliente.setText("");
        jtNome.setText("");
   
        pnlArvoreResultadoNavio.setVisible(Boolean.FALSE);

    }
    /**
     * Metodo utilizado quando selecionado outro navio na arvore de resultados
     */
    private void limpaDadosTrocaNavio(){
        //dados saida
        jtquantidadeEmbarcada.setText("");
        jftEta.setText("");
        jcTipoProduto.removeAllItems();
        jtNomecliente.setText("");
        jtNome.setText("");
    }
    /**
     * valida o campo de data do eta inserido na tela
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private void validaDadosNavio() throws ValidacaoCampoException{
        //valida campo eta
        DSSStockyardTimeUtil.validarData(etaCalendarioInicio.getDataFormatada(), PropertiesUtil.getMessage("label.eta.inicial"));
        DSSStockyardTimeUtil.validarData(etaCalendarioFinal.getDataFormatada(), PropertiesUtil.getMessage("label.eta.final"));
        //verifica se a data inicial não é menor que a data final de intevalo de busca
        RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();
        Date dataInicio = roifn.setObjetoData(etaCalendarioInicio.getDataFormatada());
        Date dataFinal = roifn.setObjetoData(etaCalendarioFinal.getDataFormatada());        
        if(dataFinal.before(dataInicio)){
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("valida.datas.interdicao"));
        }
        
    }
    /**
     * Metodo que mostra a arvore com o resultado da pesquisa da maquina
     */
    private void exibirResultadoPesquisaMaquina(List<MaquinaDoPatio> maquinasPesquisadas) throws ObjetoPesquisadoNaoEncontradoException {
        if (maquinasPesquisadas == null) {
            List<String> paramMensagem = new ArrayList<String>();
            paramMensagem.add(PropertiesUtil.getMessage("aviso.maquina.pesquisada"));
            throw new ObjetoPesquisadoNaoEncontradoException("exception.validacao.objeto.pesquisado.nao.encontrado", paramMensagem);
        }
        pnlResultadoPesquisaMaquinas.setVisible(true);
        
        //no raiz da arvore
        DefaultMutableTreeNode noRaiz = new DefaultMutableTreeNode("Maquina Encontrada");
        //cria os nós folhas da arvore, filho do root(raiz)
        for(MaquinaDoPatio maquina : maquinasPesquisadas){
         
            
            
            String dados = maquina.getMetaMaquina().getNomeMaquina() + " - " + maquina.getEstado() + " - Baliza : " + maquina.getPosicao().getNumero() + " - " +  maquina.getPosicao().getPatio().getNomePatio() + " - " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(maquina.getDtInicio());
            DefaultMutableTreeNode noFolha = new DefaultMutableTreeNode(dados);
            noRaiz.add(noFolha);
        }
        arvoreResultadoMaquina.setModel(new DefaultTreeModel(noRaiz));

        
        
        //lblResultadoPosicionamento.setSize(340, lblResultadoPosicionamento.getHeight());
        //lblResultadoPosicionamento.setText( PropertiesUtil.getMessage("label.maquina.localizada.esta.com.estado") + maquinaPesquisada.getEstado());
        //lblResultadoEstadoMaquina.setSize(340, lblResultadoEstadoMaquina.getHeight());
        //lblResultadoEstadoMaquina.setText(PropertiesUtil.getMessage("label.maquina.na.baliza.numero") + maquinaPesquisada.getPosicao().getNumero() + " " + PropertiesUtil.getMessage("label.mensagem.do.patio") + maquinaPesquisada.getPosicao().getPatio().getNomePatio());
    }

    /**
     * Metodo que valida as informações digitadas antes da realização da pesquisa de maquinas
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private void validarFiltroPesquisaMaquinas() throws ValidacaoCampoException {
        // verifica se algum produto foi selecionado na tela
        if (cmbMaquinas.getSelectedIndex() == 0) {
            List<String> paramMensagens = new ArrayList<String>();
            paramMensagens.add(PropertiesUtil.getMessage("aviso.maquina"));
            throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
        }

        // valida campo data de final do periodo da pesquisa
        DSSStockyardTimeUtil.validarDataHora(dtInicioMaquinaDataHoraCFlex.getDataHora(), PropertiesUtil.getMessage("aviso.data.hora.para.pesquisa.informado.invalido"));
    }

    /**
     * Metodo que inicializa a pesquisa de maquinas limpando as informações do 
     * filtro e esconde o painel com o resultado da pesquisa
     */
    private void inicializaPesquisaMaquinas() {
        dtInicioMaquinaDataHoraCFlex.limpaDataHora();
        if (cmbMaquinas.getItemCount() > 0) {
            cmbMaquinas.setSelectedIndex(0);
        }
        cmbMaquinas.requestFocus();
        pnlResultadoPesquisaMaquinas.setVisible(false);
        //lblResultadoEstadoMaquina.setText("");
        //lblResultadoPosicionamento.setText("");
    }

    /**
     * Metodo que monta a arvore com o resultado da pesquisa de campnha de produção
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private void exibirSituacaoPesquisaCampanhaSelecionada() throws ValidacaoCampoException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)arvoreResultado.getLastSelectedPathComponent();

        if (node == null) {
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.nao.foi.selecionada.nenhuma.situacao.para.exibicao"));
        }

        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            SituacaoPatio situacaoSelecionada = (SituacaoPatio)nodeInfo;
            controladorInterfaceComandos.getInterfaceInicial().posicionaSituacaoPatioSlider(situacaoSelecionada);
        }
        this.setVisible(false);
    }

    /**
     * Metodo que valida as informações digitadas no filtro da pesquisa de campnha de producao
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private void validarFiltroPesquisaCampanha() throws ValidacaoCampoException {

        // valida campo data de inicio do periodo da pesquisa
        DSSStockyardTimeUtil.validarData(perIniCalendarioCFlex.getDataFormatada(), PropertiesUtil.getMessage("aviso.periodo.inicial.informado.invalido"));

        if (perIniCalendarioCFlex.getDataFormatada().equals(DSSStockyardTimeUtil.DATA_EM_BRANCO)) {
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.periodo.final.nao.informando"));
        }

        // valida campo data de final do periodo da pesquisa
        DSSStockyardTimeUtil.validarData(perFimCalendarioCFlex.getDataFormatada(), PropertiesUtil.getMessage("aviso.data.periodo.final.nao.informando"));

        Date dataPeriodoInicial = DSSStockyardTimeUtil.criaDataComString(perIniCalendarioCFlex.getDataFormatada(), PropertiesUtil.buscarPropriedade("formato.campo.data"));
        Date dataPeriodoFinal = DSSStockyardTimeUtil.criaDataComString(perFimCalendarioCFlex.getDataFormatada(), PropertiesUtil.buscarPropriedade("formato.campo.data"));

        if (dataPeriodoFinal.compareTo(dataPeriodoInicial) < 0) {
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.periodo.final.deve.ser.maior.que.periodo.inicial"));
        }

        // verifica se algum produto foi selecionado na tela
        if (cmbProdutoProduzido.getSelectedIndex() == 0) {
            List<String> paramMensagens = new ArrayList<String>();
            paramMensagens.add(PropertiesUtil.getMessage("aviso.produto.produzido"));
            throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
        }
    }

    /**
     * Metodo que inicializa as informações do filtro da pesquisa de campanha de produção
     */
    private void inicializaPesquisaCampanha() {
        perIniCalendarioCFlex.limpaData();
        perFimCalendarioCFlex.limpaData();
        pnlResultadoPesquisa.setVisible(false);
        perIniCalendarioCFlex.requestFocus();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree arvoreResultado;
    private javax.swing.JTree arvoreResultadoMaquina;
    private javax.swing.JComboBox cmbMaquinas;
    private javax.swing.JComboBox cmbProdutoProduzido;
    private javax.swing.JButton cmdEncerrarPesquisa;
    private javax.swing.JButton cmdEncerrarPesquisaMaquina;
    private javax.swing.JButton cmdExibirSituacao;
    private javax.swing.JButton cmdLimparDados;
    private javax.swing.JButton cmdLimparPesquisaMaquina;
    private javax.swing.JButton cmdPesquisar;
    private javax.swing.JButton cmdPesquisarMaquina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    //private javax.swing.JLabel jLabel6;
    //private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton jbEncerrar;
    private javax.swing.JButton jbLimpar;
    private javax.swing.JButton jbPesquisar;
    private javax.swing.JFormattedTextField jftEta;
    private javax.swing.JLabel jlCliente;
    private javax.swing.JLabel jlEtaResultado;
    private javax.swing.JLabel jlNomeNavio;
    private javax.swing.JLabel jlQuantidadeProgEmbarque;
    private javax.swing.JLabel jlTipoProduto;
    private javax.swing.JTextField jtNome;
    private javax.swing.JTextField jtNomecliente;
    private javax.swing.JTree jtResultadoNavios;
    private javax.swing.JTextField jtTipoProduto;
    private javax.swing.JTextField jtquantidadeEmbarcada;
    //private javax.swing.JLabel lblResultadoEstadoMaquina;
    //private javax.swing.JLabel lblResultadoPosicionamento;
    private javax.swing.JPanel pnlArvoreResultadoNavio;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JPanel pnlFiltroPesquisa;
    private javax.swing.JPanel pnlInterfacePesquisa;
    private javax.swing.JPanel pnlPesquisarMaquinas;
    private javax.swing.JPanel pnlPesquisarNavios;
    private javax.swing.JPanel pnlResultadoNavio;
    private javax.swing.JPanel pnlResultadoPesquisa;
    private javax.swing.JPanel pnlResultadoPesquisaMaquinas;
    private javax.swing.JPanel pnlPesquisarManutencao;
    private javax.swing.JScrollPane scrArvoreResultado;
    private javax.swing.JScrollPane scrArvoreResultadoMaquina;
    private javax.swing.JTabbedPane tbpInterfacePesquisa;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioCFlex etaCalendarioInicio;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioCFlex etaCalendarioFinal;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioCFlex perIniCalendarioCFlex;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioCFlex perFimCalendarioCFlex;    
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtInicioMaquinaDataHoraCFlex;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtFinalMaquinaDataHoraCFlex;
    private javax.swing.JComboBox jcTipoProduto;
    private javax.swing.JPanel pnlFiltroManutencao;
    private javax.swing.JLabel jlEquipamento;
    private javax.swing.JLabel jlCorreiaManutencao;
    private javax.swing.JComboBox cmbEquipamentos;
    private javax.swing.JComboBox cmbCorreiasManutencao;
    private javax.swing.JLabel jlDataInicio;
    private javax.swing.JLabel jldataFinal;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtInicioEquipamentoManutencao;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtFinalManutencaoEquipamento;
    private javax.swing.JPanel pnlInfEquipamentoPesquisado;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblInformacoesEquipamentoPesquisado;
    private javax.swing.JScrollPane scrInfEquipamentoPesquisado;
    private javax.swing.JButton cmdEncerrarPesquisaManutencao;
    private javax.swing.JButton cmdLimparPesquisaManutencao;
    private javax.swing.JButton cmdPesquisarManutencao;
    private javax.swing.JRadioButton rbMaquina;
    private javax.swing.JRadioButton rbCorreia;
    private javax.swing.JRadioButton rbTodosEquipamentos;
    private javax.swing.ButtonGroup grupoBotoes;
    
    // End of variables declaration//GEN-END:variables
    
    
    public static void main(String args[])
    {
        try {
            InterfacePesquisa pesquisa = new InterfacePesquisa(new JFrame(), false);
            pesquisa.setVisible(true);
            
        } catch (HeadlessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
    }
    
    private String apresentaQuantidadeProgramadaEmbarque(Navio navio){
        String result = "";
        double valor = 0;
        //verifica se o navio possui carga e orientacao de embarque
        if(navio.getListaDeCargasDoNavio(navio.getDtInicio()) != null && !navio.getListaDeCargasDoNavio(navio.getDtInicio()).isEmpty()){
            for(Carga carga : navio.getListaDeCargasDoNavio(navio.getDtInicio())){
                if(carga.getOrientacaoDeEmbarque() != null){
                    valor += carga.getOrientacaoDeEmbarque().getQuantidadeNecessaria();                 
                }
            }
            result = String.valueOf(valor);
        }
            
        return result;
    }
    
    private void cmdLimparPesquisaManutencaoActionPerformed(ActionEvent evt){
        try {
            carregaDadosManutencaoPesquisa(new ArrayList());
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * Metodo para pesquisa de Manutencoes de Maquinas e Correias
     * @param evt
     */
    private void cmdPesquisarManutencaoActionPerformed(ActionEvent evt){
        List listaResultadoPesquisa;
        try {
            validarFiltroPesquisaManutencoes();
            listaResultadoPesquisa = buscarManutencaoMaquina();
            if(!listaResultadoPesquisa.isEmpty()){//encontrou dados
                carregaDadosManutencaoPesquisa(listaResultadoPesquisa);
            }else{//não foi encontrado resultado para pesquisa
                throw new ObjetoPesquisadoNaoEncontradoException(PropertiesUtil.getMessage("mensagem.objeto.pesquisado.nao.encontrado"));
            }
            
        } catch (ErroSistemicoException ex) {
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
//          ex.printStackTrace();
        } catch (ValidacaoCampoException ex) {
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
            ex.printStackTrace();
        } catch (ObjetoPesquisadoNaoEncontradoException ex) {
            // TODO Auto-generated catch block
//          ex.printStackTrace();
        }
        
        
    }
    private void cmdEncerrarPesquisaManutencaoActionPerformed(ActionEvent evt){
        this.setVisible(false);
    }

    /**
     * 
     * @return
     * @throws ErroSistemicoException
     * @throws ValidacaoCampoException
     */
    @SuppressWarnings("unchecked")
    private List buscarManutencaoMaquina() throws ErroSistemicoException, ValidacaoCampoException
    {
       Date dataPesquisaInicial = DSSStockyardTimeUtil.criaDataComString(dtInicioEquipamentoManutencao.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
       Date dataPesquisaFinal = DSSStockyardTimeUtil.criaDataComString(dtFinalManutencaoEquipamento.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
       List listaManutencao = null;
       
       //pesquisa de maquina
       if(pesquisaManutencaoMaquina){
           MaquinaDoPatio maquinaSelecionada = null;
           if (cmbEquipamentos.getSelectedItem() instanceof MaquinaDoPatio) {
               maquinaSelecionada = (MaquinaDoPatio)cmbEquipamentos.getSelectedItem();
           }
           listaManutencao = pesquisaMaquinasManutencao(maquinaSelecionada, dataPesquisaInicial, dataPesquisaFinal);
       }
       //pesquisa de correia
       else if(pesquisaManutencaoCorreia){
           Correia correiaSelecionada = null;
           if (cmbCorreiasManutencao.getSelectedItem() instanceof Correia) {
               correiaSelecionada = (Correia)cmbCorreiasManutencao.getSelectedItem();
           }
           listaManutencao = pesquisaCorreiasManutencao(correiaSelecionada, dataPesquisaInicial, dataPesquisaFinal);
       }
       //pesquisar a manutencao de todos os equipamentos que possuem lista de manutencao
       else {
           //busca nas correias
           List listCorreia = pesquisaCorreiasManutencao(null, dataPesquisaInicial, dataPesquisaFinal);
           //busca nas maquinas
           listaManutencao = pesquisaMaquinasManutencao(null, dataPesquisaInicial, dataPesquisaFinal);
           //Junta as manutencoes das maquinas e das correias
           listaManutencao.addAll(listCorreia);
       }
       return listaManutencao;
    }

    /**
     * 
     * @param maquinaPesquisa
     * @param dataInicial
     * @param dataFinal
     * @return
     */
    @SuppressWarnings("unchecked")
    private List pesquisaMaquinasManutencao(MaquinaDoPatio maquinaPesquisa, Date dataInicial, Date dataFinal) {

        List listaManutencao = new ArrayList();

        SituacaoPatio situacao = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal();

        for (MaquinaDoPatio maquina : situacao.getPlanta().getListaMaquinasDoPatio(situacao.getDtInicio()))
        {
            //verificando se a maquina da situacao é a maquina informada no filtro
            boolean filtroMaquinaOk = true;
            if (maquinaPesquisa != null && !maquina.getNomeMaquina().equalsIgnoreCase(maquinaPesquisa.getNomeMaquina())) {
                filtroMaquinaOk = false;
            }

            if (filtroMaquinaOk) {
            /*  if(maquina.getListaManutencao() != null && !maquina.getListaManutencao().isEmpty()){

                    for(Manutencao manutencao : maquina.getListaManutencao()){
                        if (manutencao.getDataInicial().compareTo(dataInicial)>=0 &&
                                manutencao.getDataFinal().compareTo(dataFinal)<=0) {

                            Object[] dados = new Object[6];
                            dados[COL_EQUIPAMENTO] = maquina.getNomeMaquina();
                            dados[COL_STATUS] = maquina.getEstado();
                            dados[COL_HORARIO_INICIAL] = DSSStockyardTimeUtil.formatarData(manutencao.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
                            dados[COL_HORARIO_FINAL] = DSSStockyardTimeUtil.formatarData(manutencao.getDtFim(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
                            dados[COL_PATIO] = maquina.getPosicao().getPatio();
                            dados[COL_BALIZA] = maquina.getPosicao();
                            listaManutencao.add(dados);
                        }
                    }                         
                }                 */ 
            }

        }



        
//      List<MaquinaDoPatio> maquinaList = controladorModelo.buscaListaMaquinaDoPatioPorManutencao(maquinaPesquisa, dataInicial, dataFinal);
//      for (MaquinaDoPatio maquina : maquinaList) {
//          for (Manutencao manutencao : maquina.getListaManutencao()) {
//
//              if (manutencao.getDataInicial().compareTo(dataInicial)>=0 &&
//                      manutencao.getDataFinal().compareTo(dataFinal)<=0) {
//                  Object[] dados = new Object[6];
//                  dados[COL_EQUIPAMENTO] = maquina.getNomeMaquina();
//                  dados[COL_STATUS] = maquina.getEstado();
//                  dados[COL_HORARIO_INICIAL] = DSSStockyardTimeUtil.formatarData(manutencao.getDataInicial(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
//                  dados[COL_HORARIO_FINAL] = DSSStockyardTimeUtil.formatarData(manutencao.getDataFinal(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
//                  dados[COL_PATIO] = maquina.getPosicao().getPatio();
//                  dados[COL_BALIZA] = maquina.getPosicao();
//                  if (!dadosJaInseridos(dados,listaManutencao)) {
//                      listaManutencao.add(dados);
//                  }
//              }
//          }
//      }
        return listaManutencao;
    }
    

    
    /**
     * 
     * @param correiaPesquisa
     * @param dataInicial
     * @param dataFinal
     * @return
     */
    @SuppressWarnings("unchecked")
    private List pesquisaCorreiasManutencao(Correia correiaPesquisa, Date dataInicial, Date dataFinal) {
        
        List listaManutencao = new ArrayList();

        SituacaoPatio situacao = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal();
        for (Correia correia : situacao.getPlanta().getListaCorreias(situacao.getDtInicio()))
        {
            //verificando se a correia da situacao é a correia informada no filtro
            //caso a correiaPesquisa for nula quer dizer para buscar todas
            boolean filtroCorreiaOk = true;
            if (correiaPesquisa != null && 
                    (!correia.getNomeCorreia().equalsIgnoreCase(correiaPesquisa.getNomeCorreia())
                            || !correia.getNumero().equals(correiaPesquisa.getNumero()))) {
                filtroCorreiaOk = false;
            }

            if (filtroCorreiaOk) {

                if(correia.getMetaCorreia().getListaManutencao() != null && !correia.getMetaCorreia().getListaManutencao().isEmpty()){
                    for(Manutencao manutencao : correia.getMetaCorreia().getListaManutencao()){

                        if (manutencao.getDataInicial().compareTo(dataInicial)>=0 &&
                                manutencao.getDataFinal().compareTo(dataFinal)<=0) {
                            Object[] dados = new Object[6];
                            dados[COL_EQUIPAMENTO] = correia.getNomeCorreia();
                            dados[COL_STATUS] = correia.getEstado();
                            dados[COL_HORARIO_INICIAL] = DSSStockyardTimeUtil.formatarData(manutencao.getDataInicial(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
                            dados[COL_HORARIO_FINAL] = DSSStockyardTimeUtil.formatarData(manutencao.getDataFinal(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
                            dados[COL_PATIO] = "";
                            dados[COL_BALIZA] = "";
                            listaManutencao.add(dados);
                        }
                    }                         
                }                  
            }

        }

//      for (Correia correia : correiaList) {
//          for (Manutencao manutencao : correia.getListaManutencao()) {
//              
//              if (manutencao.getDataInicial().compareTo(dataInicial)>=0 &&
//                      manutencao.getDataFinal().compareTo(dataFinal)<=0) {
//                  Object[] dados = new Object[6];
//                  dados[COL_EQUIPAMENTO] = correia.getNomeCorreia();
//                  dados[COL_STATUS] = correia.getEstado();
//                  dados[COL_HORARIO_INICIAL] = DSSStockyardTimeUtil.formatarData(manutencao.getDataInicial(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
//                  dados[COL_HORARIO_FINAL] = DSSStockyardTimeUtil.formatarData(manutencao.getDataFinal(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
//                  dados[COL_PATIO] = "";
//                  dados[COL_BALIZA] = "";
//                  if (!dadosJaInseridos(dados,listaManutencao)) {
//                      listaManutencao.add(dados);
//                  }
//              }
//          }
//      }
        return listaManutencao;
    }
    
//    /**
//     * 
//     * @param dados
//     * @param manutencoes
//     * @return
//     */
//    private boolean dadosJaInseridos(Object[] dados, List manutencoes) {
//      boolean encontrou = false;
//      for (int i = 0; i < manutencoes.size(); i++) {
//          Object[] valor = (Object[])manutencoes.get(i);
//          String nomeEquipamentoA = (String)valor[COL_EQUIPAMENTO];
//          String nomeEquipamentoB = (String)dados[COL_EQUIPAMENTO];
//          String horaInicialA = (String)valor[COL_HORARIO_INICIAL];
//          String horaInicialB = (String)dados[COL_HORARIO_INICIAL];
//          String horaFinalA = (String)valor[COL_HORARIO_FINAL];
//          String horaFinalB = (String)dados[COL_HORARIO_FINAL];
//          
//          if (nomeEquipamentoA.equals(nomeEquipamentoB) &&
//             horaInicialA.equals(horaInicialB) &&
//             horaFinalA.equals(horaFinalB)) {
//              encontrou = true;
//          }
//      }
//      return encontrou;
//    }
    
    
    private void criaColunasInformacoesBalizasSelecionadas()
    {
       listaColunaInformacoes = new ArrayList<ColunaTabela>();
       ColunaTabela colInfo;

       colInfo = new ColunaTabela();
       colInfo.setAlinhamento(SwingConstants.CENTER);
       colInfo.setEditar(Boolean.FALSE);
       colInfo.setLargura(150);
       colInfo.setRedimensionar(Boolean.FALSE);
       colInfo.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.maquina"));
       listaColunaInformacoes.add(colInfo);
       
       colInfo = new ColunaTabela();
       colInfo.setAlinhamento(SwingConstants.CENTER);
       colInfo.setEditar(Boolean.FALSE);
       colInfo.setLargura(100);
       colInfo.setRedimensionar(Boolean.FALSE);
       colInfo.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.status"));
       listaColunaInformacoes.add(colInfo);
       
       colInfo = new ColunaTabela();
       colInfo.setAlinhamento(SwingConstants.CENTER);
       colInfo.setEditar(Boolean.FALSE);
       colInfo.setLargura(180);
       colInfo.setRedimensionar(Boolean.FALSE);
       colInfo.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.data.inicial.manutencao"));
       listaColunaInformacoes.add(colInfo);
       
       colInfo = new ColunaTabela();
       colInfo.setAlinhamento(SwingConstants.CENTER);
       colInfo.setEditar(Boolean.FALSE);
       colInfo.setLargura(180);
       colInfo.setRedimensionar(Boolean.FALSE);
       colInfo.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.data.final.manutencao"));
       listaColunaInformacoes.add(colInfo);
       
       colInfo = new ColunaTabela();
       colInfo.setAlinhamento(SwingConstants.CENTER);
       colInfo.setEditar(Boolean.FALSE);
       colInfo.setLargura(100);
       colInfo.setRedimensionar(Boolean.FALSE);
       colInfo.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.nome.patio"));
       listaColunaInformacoes.add(colInfo);
       
       colInfo = new ColunaTabela();
       colInfo.setAlinhamento(SwingConstants.CENTER);
       colInfo.setEditar(Boolean.FALSE);
       colInfo.setLargura(100);
       colInfo.setRedimensionar(Boolean.FALSE);
       colInfo.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.baliza"));
       listaColunaInformacoes.add(colInfo);
    }
    

    
    private void carregaDadosManutencaoPesquisa(List listaManutencao) throws ErroSistemicoException{
        vInformacoesResultadoManutencao.removeAllElements();

        for (int i = 0; i<listaManutencao.size();i++)
        {
            Object[] dados = (Object[])listaManutencao.get(i);
            vInformacoesResultadoManutencao.add(new Vector(Arrays.asList(dados)));
        }
        CFlexStockyardFuncoesTabela.setInformacoesTabela(tblInformacoesEquipamentoPesquisado, vInformacoesResultadoManutencao, listaColunaInformacoes);
    }
    
    private void rbMaquinaActionPerformed(ActionEvent evt){
        cmbCorreiasManutencao.setEnabled(false);
        cmbEquipamentos.setEnabled(true);
        pesquisaManutencaoMaquina = true;
        pesquisaManutencaoCorreia = false;
    }
    
    private void rbCorreiaActionPerformed(ActionEvent evt){
        cmbCorreiasManutencao.setEnabled(true);
        cmbEquipamentos.setEnabled(false);
        pesquisaManutencaoMaquina = false;
        pesquisaManutencaoCorreia = true;
    }
    private void rbTodosEquipamentosActionPerformed(ActionEvent evt){
        cmbCorreiasManutencao.setEnabled(false);
        cmbEquipamentos.setEnabled(false);
        pesquisaManutencaoMaquina = false;
        pesquisaManutencaoCorreia = false;
    }
    /**
     * Metodo que valida as informações digitadas antes da realização da pesquisa de manutencoes
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private void validarFiltroPesquisaManutencoes() throws ValidacaoCampoException {
        //verifica se algum botao de radio foi selecionado
        if(!rbCorreia.isSelected() && !rbMaquina.isSelected() && !rbTodosEquipamentos.isSelected()){
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.tipo.equipamento.pesquisa.nao.selecionado"));
        }       
        //verifica dados para manutencao de maquina
        if (pesquisaManutencaoMaquina && cmbEquipamentos.getSelectedIndex() < 0) {
            List<String> paramMensagens = new ArrayList<String>();
            paramMensagens.add(PropertiesUtil.getMessage("aviso.maquina"));
            throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
        }//verifica dados para manutencao de correia
        else if (pesquisaManutencaoCorreia && cmbCorreiasManutencao.getSelectedIndex() < 0) {
            List<String> paramMensagens = new ArrayList<String>();
            paramMensagens.add(PropertiesUtil.getMessage("combo.correias"));
            throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
        }

        // valida campo data de final do periodo da pesquisa
        DSSStockyardTimeUtil.validarDataHora(dtInicioEquipamentoManutencao.getDataHora(), PropertiesUtil.getMessage("aviso.data.hora.para.pesquisa.informado.invalido"));
        DSSStockyardTimeUtil.validarDataHora(dtFinalManutencaoEquipamento.getDataHora(), PropertiesUtil.getMessage("aviso.data.hora.para.pesquisa.informado.invalido"));
    }
    
//    /**
//     * Classe responsavel para armazenar o resultado das pesquisas de manutencao
//     * @author Darley
//     *
//     */
//    class DadosPesquisaManutencao {
//      String nomeEquipamento;
//      String statusEquipamento;
//      Date dataInicial;
//      Date dataFinal;
//      String patioEquipamento;
//      String balizaEquipamento;
//    }
}

