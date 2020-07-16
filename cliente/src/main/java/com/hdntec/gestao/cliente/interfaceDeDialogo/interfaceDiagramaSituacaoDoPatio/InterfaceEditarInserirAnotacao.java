/*
 * InterfaceEditarInserirAnotacao.java
 *
 * Created on 20/05/2009
 */

package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable;
import com.hdntec.gestao.cliente.util.telas.DSSStockyardTelaUtil;
import com.hdntec.gestao.domain.planta.dao.PlantaDAO;
import com.hdntec.gestao.domain.planta.dao.RegistroDaAnotacaoDAO;
import com.hdntec.gestao.domain.planta.entity.status.Anotacao;
import com.hdntec.gestao.domain.planta.entity.status.RegistroDaAnotacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * Interface de cadastro das anotacoes inseridas no DSP
 * @author Bruno Gomes
 */
public abstract class InterfaceEditarInserirAnotacao extends javax.swing.JPanel {

    private List<ColunaTabela> listaColunas;
    private List<RegistroDaAnotacao> itensRemovidos = new ArrayList<RegistroDaAnotacao>();
    private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;
    // Indica se a esta inserindo ou editando uma anotacao
    private boolean novaAnotacao;
    //informa se esta editando ou adicionando uma linha na Anotacao
    private boolean modoEditar;
    //lista de registro para guardar os dados da anotacao
    private List<RegistroDaAnotacao> listaRegistro;

    private InterfaceAnotacao interfaceAnotacao;
    private EditarInserirAnotacao editarInserirAnotacao;
    //posicao do mouse no eixo XY para desenhar a Anotacao sempre que esta for criada
    private int eixoX;
    private int eixoY;
    //data/hora em que a anotacao foi confirmada
    private Date dataDaAnotacao;
    private ControladorDSP controladorDSP;
    /** interface mensagem*/
    InterfaceMensagem interfaceMensagem;

    /**guarda temporariamente o comentario do usuario e persiste no objeto ao confirmar edicao */
    HashMap<Long, String> mapaDeComentario;


    /** Creates new form InterfaceEditarInserirAnotacao */
    public InterfaceEditarInserirAnotacao(InterfaceAnotacao interfaceAnotacao) {
        initComponents();

        this.interfaceAnotacao = interfaceAnotacao;
        this.editarInserirAnotacao = new EditarInserirAnotacao(this);
        criaColunas();
        novaAnotacao = Boolean.FALSE;
        //seta o controladorDSP
        this.controladorDSP = interfaceAnotacao.getControladorDSP();
        //passa os dados da lista de anotacao, para a lista de registros para caso desista da edicao em memoria esta alteracao não afete a
        //lista de registros da anotacao
        listaRegistro = new ArrayList<RegistroDaAnotacao>();
        listaRegistro.addAll(interfaceAnotacao.getAnotacaoVisualizada().getListaRegistrosDaAnotacao());
        //apresenta o usuario logado
        jlUsuario.setText(InterfaceInicial.getUsuarioLogado().getName());

        //atualiza interface
         carregaDadosInterface();

         defineAcaoDePermissaoDoUsuario();

         mapaDeComentario = new HashMap<Long, String>();
    }

    public InterfaceEditarInserirAnotacao(ControladorDSP controladorDSP, int posicaoX, int posicaoY) {
        initComponents();

        this.editarInserirAnotacao = new EditarInserirAnotacao(this);
        criaColunas();
        novaAnotacao = Boolean.TRUE;
         //posicao da imagem (anotacao) na tela
        this.eixoX = posicaoX;
        this.eixoY = posicaoY;
        //seta o controladorDSP
        this.controladorDSP = controladorDSP;
        //passa os dados da lista de anotacao, para a lista de registros para caso desista da edicao em memoria esta alteracao não afete a
        //lista de registros da anotacao
        listaRegistro = new ArrayList<RegistroDaAnotacao>();
        defineAcaoDePermissaoDoUsuario();
        //apresenta o usuario logado
        jlUsuario.setText(InterfaceInicial.getUsuarioLogado().getName());

        mapaDeComentario = new HashMap<Long, String>();
    }

    private void defineAcaoDePermissaoDoUsuario(){
        //somente o administrador pode editar uma anotacao no passado e excluir uma anotacao
        if(controladorDSP.verificaPermissaoParaExcluirAnotacao() && cFlexStockyardJTable1.getRowCount() > 0){
            jbEditar.setEnabled(Boolean.TRUE);
        }
        else{
            jbEditar.setEnabled(Boolean.FALSE);
        }
    }

    /**
     * Carrega a tabela com os dados dos registros da Anotacao
     */
    private void carregaDadosInterface(){
        if(interfaceAnotacao != null){
            try {
                this.dtIniCalendarioHoraCFlex.setDataHora(interfaceAnotacao.getAnotacaoVisualizada().getDtInicio());
                this.dtFinCalendarioHoraCFlex.setDataHora(interfaceAnotacao.getAnotacaoVisualizada().getDtFim());                
                editarInserirAnotacao.carregaTabelaAnotacoes(listaRegistro, cFlexStockyardJTable1, listaColunas);
            } catch (ErroSistemicoException ex) {
                Logger.getLogger(InterfaceEditarInserirAnotacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

     private void criaColunas() {
        listaColunas = new ArrayList<ColunaTabela>();
        ColunaTabela coluna;

        // coluna com o o radiobutton para selecionar a linha desejada da Anotação
        coluna = new ColunaTabela();
        coluna.setAlinhamento(SwingConstants.CENTER);
        coluna.setEditar(Boolean.TRUE);
        coluna.setLargura(40);
        coluna.setRedimensionar(Boolean.FALSE);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.sel"));
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_RADIOBUTTON);
        listaColunas.add(coluna);

        //coluna que contem o id de cada item(registro) da anotacao
        coluna = new ColunaTabela();
        coluna.setAlinhamento(SwingConstants.LEADING);
        coluna.setEditar(Boolean.FALSE);
        coluna.setLargura(40);
        coluna.setRedimensionar(Boolean.FALSE);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.id"));
        listaColunas.add(coluna);

        //Coluna nome do usuario que inseriu a anotacao
        coluna = new ColunaTabela();
        coluna.setLargura(100);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.usuario"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(Boolean.TRUE);
        coluna.setEditar(Boolean.FALSE);
        listaColunas.add(coluna);

        //Coluna data/hora em que a anotacao foi inserida
        coluna = new ColunaTabela();
        coluna.setLargura(150);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.data.hora"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(Boolean.TRUE);
        coluna.setEditar(Boolean.FALSE);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        listaColunas.add(coluna);

        //Coluna descrição da anotacao
        coluna = new ColunaTabela();
        coluna.setLargura(530);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.descricao"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(Boolean.TRUE);
        coluna.setEditar(Boolean.FALSE);
        listaColunas.add(coluna);

        //seta tipo de selecao da tabela
        modoSelecaoTabela();
        // habilita campo de inserir dados
        habilitaFuncoes(Boolean.FALSE);

    }

     /**
      * 
      */
     private void modoSelecaoTabela(){
         cFlexStockyardJTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         cFlexStockyardJTable1.setRowSelectionAllowed(Boolean.TRUE);
     }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAnotacao = new javax.swing.JPanel();
        jbFechar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        cFlexStockyardJTable1 = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        jLabel2 = new javax.swing.JLabel();
        jlUsuario = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jtNovaDescricao = new javax.swing.JTextField();
        jbOkbutton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbConfirmar = new javax.swing.JButton();
        jbAdicionar = new javax.swing.JButton();
        jbEditar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        dtIniCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        jLabel9 = new javax.swing.JLabel();
        dtFinCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        jbExcluir = new javax.swing.JButton();

        pnlAnotacao.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("titulo.mensagem.anotacao")));

        //jbFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        jbFechar.setText(PropertiesUtil.getMessage("botao.fechar"));
        jbFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFecharActionPerformed(evt);
            }
        });

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        cFlexStockyardJTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        cFlexStockyardJTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cFlexStockyardJTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(cFlexStockyardJTable1);

        jLabel2.setText(PropertiesUtil.getMessage("label.usuario"));

        jLabel1.setText(PropertiesUtil.getMessage("label.descricao"));

        jbOkbutton.setText(PropertiesUtil.getMessage("botao.ok"));
        jbOkbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOkbuttonActionPerformed(evt);
            }
        });

  //      jbConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/confirmar.png"))); // NOI18N
        jbConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
        //jbConfirmar.setEnabled(false);
        jbConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConfirmarActionPerformed(evt);
            }
        });

        jbAdicionar.setText(PropertiesUtil.getMessage("botao.adicionar"));
        jbAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAdicionarActionPerformed(evt);
            }
        });

        jbEditar.setText(PropertiesUtil.getMessage("botao.editar"));
        jbEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEditarActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel5.setText("Data de início:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel9.setText("Data final:");

        jbExcluir.setText("Excluir");
        jbExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAnotacaoLayout = new javax.swing.GroupLayout(pnlAnotacao);
        pnlAnotacao.setLayout(pnlAnotacaoLayout);
        pnlAnotacaoLayout.setHorizontalGroup(
            pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAnotacaoLayout.createSequentialGroup()
                .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAnotacaoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(14, 14, 14)
                        .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAnotacaoLayout.createSequentialGroup()
                                .addComponent(jtNovaDescricao, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbOkbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jlUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAnotacaoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jbFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAnotacaoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAnotacaoLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 460, Short.MAX_VALUE)
                                .addComponent(jbExcluir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbEditar)
                                .addGap(18, 18, 18)
                                .addComponent(jbAdicionar)))))
                .addContainerGap())
            .addGroup(pnlAnotacaoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtIniCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(dtFinCalendarioHoraCFlex, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addGap(275, 275, 275))
        );
        pnlAnotacaoLayout.setVerticalGroup(
            pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAnotacaoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(dtIniCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(dtFinCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAdicionar)
                    .addComponent(jbEditar)
                    .addComponent(jbExcluir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jlUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtNovaDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbOkbutton)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAnotacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbConfirmar)
                    .addComponent(jbFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlAnotacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(pnlAnotacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Metodo criado abstrato, pois sua implementacao sera na classe que o instanciou
     */
    public abstract void fecharJanela();

    /**
     * limpa o campo onde se insere o texto inserido pelo usuario
     */
    private void limpaCampoTexto(){
        jtNovaDescricao.setText("");
    }

    /**
     * acao do botao de adicionar anotacao
     * @param evt
     */
    private void jbOkbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOkbuttonActionPerformed
        if (editarInserirAnotacao.validaDados())
        {
            if (novaAnotacao) {
                //dentro deste bloco esta se criando uma nova anotacao... primero sera criada a InterfaceAnotacao que representa a anotacao
                if (interfaceAnotacao == null) {
                    interfaceAnotacao = new InterfaceAnotacao(eixoX, eixoY);
                }
                //...Cria o objeto anotacao
                Anotacao anotacao = editarInserirAnotacao.criaObjetoAnotacao(eixoX, eixoY);
                                
                montarAnotacao(anotacao);
                editarInserirAnotacao.verificaListaAnotacaoNula(anotacao);
                //guarda o momento em que a anotacao foi criada
                dataDaAnotacao = Calendar.getInstance().getTime();

                //cria e insere um registroDeAnotação na Anotacao
                String user = InterfaceInicial.getUsuarioLogado().getName();
                editarInserirAnotacao.acrescentaNaListaAnotacao(anotacao, user, dataDaAnotacao, jtNovaDescricao.getText());
                
                //adiciona a anotacao criada a interfaceAnotacao
                interfaceAnotacao.setAnotacaoVisualizada(anotacao);
                interfaceAnotacao.setControladorDSP(controladorDSP);
                //atualiza a lista de anotacao
                listaRegistro.addAll(interfaceAnotacao.getAnotacaoVisualizada().getListaRegistrosDaAnotacao());
                //apresenta os dados na interface
                carregaDadosInterface();

            } else {
                //dentro deste condicional edita-se uma anotacao ja existente
                if (modoEditar) {
                    try {
                        //guarda o momento em que a anotacao foi criada
                        dataDaAnotacao = Calendar.getInstance().getTime();
                        Long idAnotacao = editarInserirAnotacao.retornaLinhaSelecionadaTabela(cFlexStockyardJTable1);
                        if (idAnotacao != null) {
                            //se for diferente de null esta editando
                        
                            RegistroDaAnotacao registro = editarInserirAnotacao.obterRegistroAnotacaoSelecionado(interfaceAnotacao.getAnotacaoVisualizada(), idAnotacao);
                            
                            montarAnotacao(interfaceAnotacao.getAnotacaoVisualizada());
                           
                            mapaDeComentario.put(registro.getIdRegistroAnotacao(), jtNovaDescricao.getText());

                            //edita o registro da anotacao
//                            apresentaDadosInformacaoTabela();

                        } else {
                            criaAtualizaRegistroTabela();
                            montarAnotacao(interfaceAnotacao.getAnotacaoVisualizada());
                        }
                    } catch (ValidacaoCampoException ex) {
                    	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorDSP.ativarMensagem(interfaceMensagem);
                    }

                } else {
                    montarAnotacao(interfaceAnotacao.getAnotacaoVisualizada());
                    criaAtualizaRegistroTabela();                    
                }
            }

            //limpa o campo onde a descricao da anotacao foi escrita
            limpaCampoTexto();
            //jbConfirmar.setEnabled(Boolean.TRUE);

        }else{
            apresentaMensagem();
        }

         //edita o registro da anotacao
        apresentaDadosInformacaoTabela();
        //volta ao estado de inicializacao de tela
        habilitaFuncoes(Boolean.FALSE);
        jbAdicionar.setEnabled(Boolean.TRUE);
        
}//GEN-LAST:event_jbOkbuttonActionPerformed

    /**
     * montarAnotacao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 08/12/2010
     * @see
     * @param 
     * @return void
     */
    private void montarAnotacao(Anotacao anotacao) {
        try {
            DSSStockyardTimeUtil.validarDataHora(dtIniCalendarioHoraCFlex.getDataHora(), PropertiesUtil.getMessage("aviso.data.hora.inicio.campanha"));
        
            DSSStockyardTimeUtil.validarDataHora(dtFinCalendarioHoraCFlex.getDataHora(), PropertiesUtil.getMessage("aviso.data.hora.fim.campanha"));
        
        Date dataInicialCampanha = DSSStockyardTimeUtil.criaDataComString(dtIniCalendarioHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        Date dataFinalCampanha = DSSStockyardTimeUtil.criaDataComString(dtFinCalendarioHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));

        if (dataFinalCampanha.compareTo(dataInicialCampanha) < 0) {
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.datafinal.maior.datainicial"));
        }
        
        anotacao.setIdUser(1L);
        anotacao.setDtInicio(dataInicialCampanha);
        anotacao.setDtFim(dataFinalCampanha);
        
        } catch (ValidacaoCampoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void criaAtualizaRegistroTabela(){
        //guarda o momento em que a anotacao foi criada
        dataDaAnotacao = Calendar.getInstance().getTime();
        //cria e adiciona um novo registro para a anotacao
        String user = InterfaceInicial.getUsuarioLogado().getName();
        editarInserirAnotacao.adicionaRegistroAListaDeRegistroAnotacao(listaRegistro, user, dataDaAnotacao, jtNovaDescricao.getText(),interfaceAnotacao.getAnotacaoVisualizada());
       
    }

    private void jbFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFecharActionPerformed
        
        this.operacaoCanceladaPeloUsuario = Boolean.TRUE;
        fecharJanela();
        // chegando ate o JDialog
        DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
    }//GEN-LAST:event_jbFecharActionPerformed

    private void jbConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConfirmarActionPerformed
             
        //varifica se algum dado foi inserido
        if (!listaRegistro.isEmpty()) {
            // atualiza lista de registro
            atualizaListaRegistro();

            //passa os dados da lista de anotacao, para a lista de registros para caso desista da edicao em memoria esta alteracao não afete a
            //lista de registros da anotacao
            editarInserirAnotacao.atualizaListaAnotacao(listaRegistro, interfaceAnotacao.getAnotacaoVisualizada());
            // Insere ou atualiza um objeto Anotacao na lista de anotacoes do objeto Planta
            editarInserirAnotacao.insereAnotacaoListaPlata(controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida(), interfaceAnotacao.getAnotacaoVisualizada(), novaAnotacao);
            //salva a insercao ou edicao da anotacao         
            controladorDSP.getInterfaceInicial().getInterfaceComandos().finalizarEdicoes();
        }
        montarAnotacao(interfaceAnotacao.getAnotacaoVisualizada());    
        
        

        //registro.setAnotacao(null);
        RegistroDaAnotacaoDAO daoReg = new RegistroDaAnotacaoDAO();
        daoReg.remover(itensRemovidos);
        
        
        PlantaDAO plantaDAO = new PlantaDAO();
        plantaDAO.salvar(controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getPlanta());
   
        
        operacaoCanceladaPeloUsuario = Boolean.FALSE;
        // chegando ate o JDialog
        DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
        
    }//GEN-LAST:event_jbConfirmarActionPerformed

    private void cFlexStockyardJTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cFlexStockyardJTable1MouseClicked
        try {
            if(modoEditar){
                habilitaFuncoes(Boolean.TRUE);
                exibeDadosTabelaAnotacaoSelecionada();
            }
        } catch (ValidacaoCampoException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }


    }//GEN-LAST:event_cFlexStockyardJTable1MouseClicked

    private void jbAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAdicionarActionPerformed
        modoEditar = Boolean.FALSE;
        jbEditar.setEnabled(Boolean.FALSE);
        jbAdicionar.setEnabled(Boolean.FALSE);
        habilitaFuncoes(Boolean.TRUE);


}//GEN-LAST:event_jbAdicionarActionPerformed

    private void jbEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditarActionPerformed
        try
        { //habilita modo de edi??o
        modoEditar = Boolean.TRUE;
        jbAdicionar.setEnabled(Boolean.FALSE);
        //exibe o dado selecionado para edicao
        exibeDadosTabelaAnotacaoSelecionada();
        //habilita campos para edicao
        habilitaFuncoes(Boolean.TRUE);
        } catch (ValidacaoCampoException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }

    }//GEN-LAST:event_jbEditarActionPerformed

    private void jbExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExcluirActionPerformed
        // TODO add your handling code here:
        
            
            if(cFlexStockyardJTable1.getSelectedRow() != -1){
             RegistroDaAnotacao registro = interfaceAnotacao.getAnotacaoVisualizada().getListaRegistrosDaAnotacao().get(cFlexStockyardJTable1.getSelectedRow());
            
            
            
            JLabel pergunta = new JLabel("Deseja excluir o registro da anotação ? \n " + registro);
            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
            int confirm = JOptionPane.showOptionDialog(
                    controladorDSP.getInterfaceDSP().getInterfaceInicial(),
                    pergunta,
                    PropertiesUtil.getMessage("mensagem.option.pane.alerta"),
                    JOptionPane.YES_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null);

            if (confirm == JOptionPane.YES_OPTION) {
               //seta o patio da anotacao para null
//                  interfaceAnotacao.getAnotacaoVisualizada().setPatio(null);
               //remove a Anotacao da lista de anotacoes da Plantas                
               
                
               interfaceAnotacao.getAnotacaoVisualizada().getListaRegistrosDaAnotacao().remove(registro);

               itensRemovidos.add(registro);
               
               listaRegistro = new ArrayList<RegistroDaAnotacao>();
               listaRegistro.addAll(interfaceAnotacao.getAnotacaoVisualizada().getListaRegistrosDaAnotacao());
               
               try {
                editarInserirAnotacao.carregaTabelaAnotacoes(listaRegistro, cFlexStockyardJTable1, listaColunas);
            } catch (ErroSistemicoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
               
               
            }
            
            }             
        
    }//GEN-LAST:event_jbExcluirActionPerformed

    private void habilitaFuncoes(boolean funcao)
    {
        jtNovaDescricao.setEnabled(funcao);
        jbOkbutton.setEnabled(funcao);
    }
    private void exibeDadosTabelaAnotacaoSelecionada() throws ValidacaoCampoException{
        CFlexStockyardFuncoesTabela.validaSelecaoTabela(cFlexStockyardJTable1);
        RegistroDaAnotacao registro = selecionarRegistroAnotacao();   
        
        if (registro != null) {
        //apresenta a anotacao selecionada na caixa de texto
            jtNovaDescricao.setText(registro.getDescricao());
        }else{
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("exception.editar.item.na.memoria"));
        }
    }

    /**
     * selecionarRegistroAnotacao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 08/12/2010
     * @see
     * @param 
     * @return RegistroDaAnotacao
     */
    private RegistroDaAnotacao selecionarRegistroAnotacao() throws ValidacaoCampoException {
        Long idAnotacao = editarInserirAnotacao.retornaLinhaSelecionadaTabela(cFlexStockyardJTable1);
        RegistroDaAnotacao registro = null;
        if(idAnotacao != null) {
            registro = editarInserirAnotacao.obterRegistroAnotacaoSelecionado(interfaceAnotacao.getAnotacaoVisualizada(), idAnotacao);
        }
        return registro;
    }

    /**
     * Apresenta mensagem que o campo texto de descricao esta vazio
     */
    private void apresentaMensagem(){
        JLabel descricao = new JLabel(PropertiesUtil.getMessage("mensagem.option.pane.campo.descricao.anotacao.vazio"));
        JOptionPane.showMessageDialog(this,
                descricao,
                PropertiesUtil.getMessage("mensagem.option.pane.aviso"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void apresentaDadosInformacaoTabela()
    {
        try {
            editarInserirAnotacao.carregaTabelaAnotacoes(listaRegistro, mapaDeComentario, cFlexStockyardJTable1, listaColunas);
        } catch (ErroSistemicoException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("aviso.mensagem.erro.ao.carregar.tabela.informacoes"));
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
    }
    private void atualizaListaRegistro()
    {
        if(!mapaDeComentario.isEmpty()){
            String mensagem;
            for(RegistroDaAnotacao registro : listaRegistro){
                mensagem = mapaDeComentario.get(registro.getIdRegistroAnotacao());
                if(mensagem != null){
                    registro.setDescricao(mensagem);                    
                }
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable cFlexStockyardJTable1;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtFinCalendarioHoraCFlex;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtIniCalendarioHoraCFlex;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbAdicionar;
    private javax.swing.JButton jbConfirmar;
    private javax.swing.JButton jbEditar;
    private javax.swing.JButton jbExcluir;
    private javax.swing.JButton jbFechar;
    private javax.swing.JButton jbOkbutton;
    private javax.swing.JLabel jlUsuario;
    private javax.swing.JTextField jtNovaDescricao;
    private javax.swing.JPanel pnlAnotacao;
    // End of variables declaration//GEN-END:variables

    public CFlexStockyardJTable getCFlexStockyardJTable1() {
        return cFlexStockyardJTable1;
    }

    public void setCFlexStockyardJTable1(CFlexStockyardJTable cFlexStockyardJTable1) {
        this.cFlexStockyardJTable1 = cFlexStockyardJTable1;
    }

    public InterfaceAnotacao getInterfaceAnotacao() {
        return interfaceAnotacao;
    }

    public void setInterfaceAnotacao(InterfaceAnotacao interfaceAnotacao) {
        this.interfaceAnotacao = interfaceAnotacao;
    }

    public Boolean getOperacaoCanceladaPeloUsuario() {
        return operacaoCanceladaPeloUsuario;
    }

    public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario) {
        this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
    }

    public JTextField getJtNovaDescricao() {
        return jtNovaDescricao;
    }

}
