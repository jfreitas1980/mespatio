package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import jxl.read.biff.BiffException;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceEditarCampanha.ACAO;
import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceSelecionavelParaAtividade;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.EstruturaEmManutencaoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.LeituraPlanilhaException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoNuloException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gr�fica que representa a {@link Usina}.
 *
 * @author andre
 *
 */
public class InterfaceUsina extends RepresentacaoGrafica implements InterfaceInicializacao, InterfaceSelecionavelParaAtividade
{

    private Date horaSituacao;
    /** serial gerado */
    private static final long serialVersionUID = -5593851060963158566L;

    /** a {@link Usina} visualizada nesta interface gr�fica */
    private Usina usinaVisualizada;

    protected  JLabel labelNome;
    /** acesso �s opera s do subsistema de interface gr�fica DSP */
    protected ControladorDSP controladorDSP;

    /** interface gr�fica DSP */
    private InterfaceDSP interfaceDSP;

    /** interface gr�fica da {@link Campanha} que pertence a esta usina */
    private InterfaceCampanha interfaceCampanha;

    /** flag que indica se a usina esta ou nao selecionada */
    private Boolean usinaSelecionada;

    /** a interface das mensagens exibidas pela interface carga */
    protected InterfaceMensagem interfaceMensagem;

    /** o tempo informado da atividade de empilhamento */
    private Double tempoEmpilhamento;

    private int eixoXUsina;

    private int tamanhoBorda;

    //menus
    protected JMenuItem mnuCriarCampanha;
    private JMenuItem mnuEditarCampanha;    
    private JMenuItem mnuExcluirCampanha;
    private JMenuItem mnuImportarCampanha;
    protected JMenuItem mnuAtualizacaoRecuperacao;
    private JMenuItem mnuAtualizacaoPilhaEmergencia;

    public InterfaceUsina(Date horaSituacao) {
        super("usina-a.png");
        this.setOpaque(true);
        this.setHoraSituacao(horaSituacao);
        usinaSelecionada = Boolean.FALSE;
        this.setLayout(null);
    }

    
    private InterfaceUsina obterUsinaClicada(ActionEvent evt) {
        JMenuItem mnuItem = (JMenuItem) evt.getSource();
        JPopupMenu popupMenu = (JPopupMenu) mnuItem.getParent();
        return (InterfaceUsina) popupMenu.getInvoker();
    }

    protected void defineEventosParaUsina() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                selecionaDeselecionaUsina(evt);
            }
        });
    }

    private void editaDadosCampanha(ActionEvent evt) throws OperacaoCanceladaPeloUsuarioException {
        try {
            controladorDSP.verificarModoDeEdicao();
            this.getInterfaceDSP().getControladorDSP().editaCampanhaProducao(this, this.getInterfaceDSP().getInterfaceInicial().getListaTiposProduto(), this.getInterfaceDSP().getInterfaceInicial().getListaTiposItemDeControle(),ACAO.EDICAO,null);
        } catch (ErroSistemicoException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }catch(ModoDeEdicaoException mdex){
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
    }

    private void criarDadosCampanha(ActionEvent evt) throws OperacaoCanceladaPeloUsuarioException {
        try {
            controladorDSP.verificarModoDeEdicao();
            this.getInterfaceDSP().getControladorDSP().editaCampanhaProducao(this, this.getInterfaceDSP().getInterfaceInicial().getListaTiposProduto(), this.getInterfaceDSP().getInterfaceInicial().getListaTiposItemDeControle(),ACAO.CRIACAO,null);
        } catch (ErroSistemicoException ex) {
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }catch(ModoDeEdicaoException mdex){
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
    }

    
    
    private void selecionaDeselecionaUsina(MouseEvent evt) {
        /*if (evt.getButton() == MouseEvent.BUTTON1) {
            if (!controladorDSP.getInterfaceInicial().verificaPermissaoAtualizacaoProducao()) {
                if (usinaSelecionada) {
                    deselecionar();
                } else {
                    selecionar();
                }
            }
        }*/
    }

    @Override
    public void inicializaInterface() {
    	StringBuffer dados = new StringBuffer(); 
    	dados.append("<html>").append(PropertiesUtil.getMessage("label.usina")).append(usinaVisualizada.getNomeUsina()).append("<br>");
        
    	
    	
    	dados.append(PropertiesUtil.getMessage("label.taxa.operacao")).append(DSSStockyardFuncoesNumeros.getQtdeFormatada(usinaVisualizada.getTaxaDeOperacao(), 2));
        
    	if (usinaVisualizada.getAtividade() != null) {
    	    
    	    dados.append("<br>").append(PropertiesUtil.getMessage("label.atividade")).append(usinaVisualizada.getAtividade().getTipoAtividade().toString());
        
    	    if (usinaVisualizada.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO) && usinaVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
    	        dados.append("<br>").append(PropertiesUtil.getMessage("label.maquina"));        	        
    	       // dados.append(usinaVisualizada.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getMaquinaDoPatio().getNomeMaquina());
    	    }
    	    
    	    if (usinaVisualizada.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO) && usinaVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
    	        dados.append("<br>").append(PropertiesUtil.getMessage("label.carga.tooltip")); 
    	        dados.append(usinaVisualizada.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getIdentificadorCarga()).append("<br>").append(PropertiesUtil.getMessage("label.navio"));
    	       // dados.append(usinaVisualizada.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getNavio(usinaVisualizada.getAtividade().getDtInicio()).getNomeNavio());
    	    } 
    	}   	
    	
    	
        this.setToolTipText(dados.toString());

        defineDimensoesFixas();
        calculaPosicaoUsina();
        defineEventosParaUsina();
        criaPopMenuParaUsina();
        desabilitaMenusPermissaoUsuario();
        controladorDSP.setInterfaceDadosEdicao(new InterfaceDadosEdicao());
        if(usinaVisualizada.isUsinaSelecionada()) {
            usinaSelecionada = Boolean.TRUE;
            desenhaUsinaSelecionada();
        }

    }

    private void criaPopMenuParaUsina() {

        JPopupMenu popMnuEditarUsina = new JPopupMenu();


//      JMenuItem
        mnuCriarCampanha = new JMenuItem();
        mnuCriarCampanha.setText(PropertiesUtil.getMessage("menu.criar.campanha"));
        mnuCriarCampanha.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                criarCampanhaActionPerformed(evt);
            }
        });
        popMnuEditarUsina.add(mnuCriarCampanha);

        
/*        //JMenuItem
        mnuEditarCampanha = new JMenuItem();
        mnuEditarCampanha.setText(PropertiesUtil.getMessage("menu.editar.campanha"));
        mnuEditarCampanha.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                editarCampanhaActionPerformed(evt);
            }
        });
        popMnuEditarUsina.add(mnuEditarCampanha);

        if (usinaVisualizada.getCampanhaAtual(this.getHoraSituacao()) == null) {
            mnuEditarCampanha.setEnabled(false);
        }
*/        
        
//        JMenuItem
        /*mnuExcluirCampanha = new JMenuItem();
        mnuExcluirCampanha.setText(PropertiesUtil.getMessage("menu.excluir.campanha"));
        mnuExcluirCampanha.setEnabled(false);
        mnuExcluirCampanha.addActionListener(new java.awt.event.ActionListener() {
            
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirCampanhaActionPerformed(evt);
            }
        });
        popMnuEditarUsina.add(mnuExcluirCampanha);
*/
//        JMenuItem
        mnuImportarCampanha = new JMenuItem();
        mnuImportarCampanha.setText(PropertiesUtil.getMessage("menu.importar.ata.campanha"));
        mnuImportarCampanha.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importarAtaDeCampanhaActionPerformed(evt);
            }
        });
        popMnuEditarUsina.add(mnuImportarCampanha);

//        JMenuItem
        mnuAtualizacaoRecuperacao = new JMenuItem();
        mnuAtualizacaoRecuperacao.setText(PropertiesUtil.getMessage("menu.atualizacao.de.recuperacao"));
        mnuAtualizacaoRecuperacao.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atualizacaoRecuperacaoUsina(evt);
            }
        });
        popMnuEditarUsina.add(mnuAtualizacaoRecuperacao);

        if (usinaVisualizada.getMetaUsina().getListaMetaPatioEmergencia() != null && !usinaVisualizada.getMetaUsina().getListaMetaPatioEmergencia().isEmpty())
        {
//           JMenuItem
           mnuAtualizacaoPilhaEmergencia = new JMenuItem();
           mnuAtualizacaoPilhaEmergencia.setText(PropertiesUtil.getMessage("menu.atualizacao.pilha.emergencia"));
           mnuAtualizacaoPilhaEmergencia.addActionListener(new java.awt.event.ActionListener() {

               @Override
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                   atualizacaoPilhaEmergencia(evt);
               }
           });
           popMnuEditarUsina.add(mnuAtualizacaoPilhaEmergencia);
        }

        this.setComponentPopupMenu(popMnuEditarUsina);
    }

    private void excluirCampanhaActionPerformed(ActionEvent evt) {
        try{
            if (this.usinaVisualizada.getMetaUsina().getCampanhaAtual(this.getHoraSituacao()) != null) {
                controladorDSP.getInterfaceInicial().ativaModoEdicao();
                controladorDSP.verificarModoDeEdicao();

                JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.option.pane.deseja.realmente.excluir.campanha") + usinaVisualizada.getNomeUsina() + PropertiesUtil.getMessage("label.interrogacao"));
                pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                int confirm = JOptionPane.showOptionDialog(this, pergunta, PropertiesUtil.getMessage("mensagem.option.pane.tipo.atencao"), JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                /*if (confirm == JOptionPane.YES_OPTION) {                    
                    this.usinaVisualizada.setCampanhaAtual(null);                    
                    controladorDSP.getInterfaceInicial().getInterfaceComandos().finalizarEdicoes();
                }*/
            } else {
                throw new CampanhaIncompativelException("N�o existe campanha de produ  para exclus�o");
            }
        }catch(ModoDeEdicaoException mdex){
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        } catch (CampanhaIncompativelException campInexitEx) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(campInexitEx.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
    }

    private void criarCampanhaActionPerformed(ActionEvent evt) {
        InterfaceUsina usinaClicada = obterUsinaClicada(evt);
        if (usinaClicada.getUsinaVisualizada().equals(usinaVisualizada)) {
            try {
                controladorDSP.getInterfaceInicial().ativaModoEdicao();
                criarDadosCampanha(evt);
            } catch (OperacaoCanceladaPeloUsuarioException operCancelEx) {
                controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                interfaceMensagem = new InterfaceMensagem();
                interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                interfaceMensagem.setDescricaoMensagem(operCancelEx.getMessage());
                controladorDSP.ativarMensagem(interfaceMensagem);
            }
        }
    }
    
    private void editarCampanhaActionPerformed(ActionEvent evt) {
        InterfaceUsina usinaClicada = obterUsinaClicada(evt);
        if (usinaClicada.getUsinaVisualizada().equals(usinaVisualizada)) {
            try {
                controladorDSP.getInterfaceInicial().ativaModoEdicao();
                editaDadosCampanha(evt);
            } catch (OperacaoCanceladaPeloUsuarioException operCancelEx) {
            	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                interfaceMensagem = new InterfaceMensagem();
                interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                interfaceMensagem.setDescricaoMensagem(operCancelEx.getMessage());
                controladorDSP.ativarMensagem(interfaceMensagem);
            }
        }
    }

    protected void calculaPosicaoUsina() {
        Rectangle rectParent = interfaceDSP.getComponenteInterfaceUsina().getBounds();
        int eixoY = 0, comprimento = 0, largura = 0;
        int qtdeUsinasInseridas = 0, comprimentoRealUsina = 0;
        int diferencaPosicaoUsina = 0;
        qtdeUsinasInseridas = (interfaceDSP.getComponenteInterfaceUsina().getComponentCount() - 1);
        comprimentoRealUsina = (int) (rectParent.getHeight() / interfaceDSP.getListaUsinas().size());
        comprimentoRealUsina= comprimentoRealUsina - 5;
        comprimento = (comprimentoRealUsina  / 3) ;
        diferencaPosicaoUsina = comprimentoRealUsina - comprimento;
        largura = (int) rectParent.getWidth() - tamanhoBorda - 10 ;
        eixoY = ((qtdeUsinasInseridas * comprimentoRealUsina) + tamanhoBorda) + (diferencaPosicaoUsina / 2);
                
        //definindo as dimesoes da interfaceUsina
        this.setDimensaoImagem( eixoXUsina, eixoY, largura, comprimento+10);
        this.setBounds(eixoXUsina , eixoY, largura, comprimento + 30);
        adicionaNomeUsina(comprimento+10);           
    }

    @Override
    public void defineDimensoesFixas() {
        tamanhoBorda = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.usina.tamanho.borda").trim());
        eixoXUsina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.usina.eixo.x").trim());
    }

    public ControladorDSP getControladorDSP() {
        return controladorDSP;
    }

    public void setControladorDSP(ControladorDSP controladorDSP) {
        this.controladorDSP = controladorDSP;
    }

    public InterfaceCampanha getInterfaceCampanha() {
        return interfaceCampanha;
    }

    public void setInterfaceCampanha(InterfaceCampanha interfaceCampanha) {
        this.interfaceCampanha = interfaceCampanha;
    }

    public InterfaceDSP getInterfaceDSP() {
        return interfaceDSP;
    }

    public void setInterfaceDSP(InterfaceDSP interfaceDSP) {
        this.interfaceDSP = interfaceDSP;
    }

    public Usina getUsinaVisualizada() {
        return usinaVisualizada;
    }

    public void setUsinaVisualizada(Usina usinaVisualizada) {
        this.usinaVisualizada = usinaVisualizada;
    }

    @Override
    public void selecionar()
    {
       try
       {
           if (this.usinaVisualizada.getMetaUsina().getCampanhaAtual(this.getHoraSituacao()) == null) {
               throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.usina.nao.pode.ser.selecionada.por.nao.possuir.campanha"));
           }

           String qtdeCampanhaDigitada = null;
           TipoDeProdutoEnum tipoDeProdutoSelecionado = null;
           if (usinaVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
          {
             final InterfaceSelecaoUsina interfaceSelecaoUsina = new InterfaceSelecaoUsina(usinaVisualizada)
             {
                @Override
                public void fecharJanela()
                {
                   this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
                }
             };
             JDialog dialogInformacoesUsina = new JDialog(this.getInterfaceDSP().getInterfaceInicial(), true);
             dialogInformacoesUsina.addWindowListener(new java.awt.event.WindowAdapter()
             {
                @Override
                public void windowClosing(java.awt.event.WindowEvent evt)
                {
                   interfaceSelecaoUsina.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
                }
             });
             dialogInformacoesUsina.setLayout(new BorderLayout());
             dialogInformacoesUsina.setTitle(PropertiesUtil.getMessage("titulo.mensagem.produto.da") + usinaVisualizada.getNomeUsina());
             dialogInformacoesUsina.getContentPane().add(interfaceSelecaoUsina);
             dialogInformacoesUsina.pack();
             dialogInformacoesUsina.setLocationRelativeTo(null);
             dialogInformacoesUsina.setVisible(true);
             if (interfaceSelecaoUsina.getOperacaoCanceladaPeloUsuario())
             {
                dialogInformacoesUsina.setVisible(false);
                throw new OperacaoCanceladaPeloUsuarioException();
             }
             // Setar alteracoes
             qtdeCampanhaDigitada = interfaceSelecaoUsina.getJTextQuantidadeProduto().getText();
             tipoDeProdutoSelecionado = interfaceSelecaoUsina.getTipoDeProdutoSelecionado();
             dialogInformacoesUsina.setVisible(false);

             // verifica se a quantidade digitada eh valida
             if (qtdeCampanhaDigitada != null)
             {
                qtdeCampanhaDigitada = qtdeCampanhaDigitada.replace(".", "");
                qtdeCampanhaDigitada = qtdeCampanhaDigitada.replace(",", ".");
                Date dataAtual = controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio();
                Long tempoAtualMS = dataAtual.getTime();
                Double qtdeCampanhaBlendagem = Double.parseDouble(qtdeCampanhaDigitada);
                Long tempoDeProducaoDaUsina = ((usinaVisualizada.getMetaUsina().getCampanhaAtual(this.getHoraSituacao()).getDataFinal().getTime() - tempoAtualMS) / 1000) / 60 / 60;
                Double quantidadeASerProduzida = tempoDeProducaoDaUsina * usinaVisualizada.getTaxaDeOperacao();
                if (qtdeCampanhaBlendagem <= quantidadeASerProduzida)
                {
                   controladorDSP.selecionaCampanhaDeUsina(usinaVisualizada.getMetaUsina().getCampanhaAtual(this.getHoraSituacao()), qtdeCampanhaBlendagem, tipoDeProdutoSelecionado);
                   usinaVisualizada.getMetaUsina().getCampanhaAtual(this.getHoraSituacao()).setTipoDeProdutoTemporario(tipoDeProdutoSelecionado);
                   usinaVisualizada.getMetaUsina().getCampanhaAtual(this.getHoraSituacao()).setQuantidadeTemporaria(qtdeCampanhaBlendagem);
                   //desenhando a imagem da usina selecionada
                   desenhaUsinaSelecionada();
                   usinaSelecionada = Boolean.TRUE;
                } else
                {
                   throw new SelecaoObjetoModoNuloException(PropertiesUtil.getMessage("aviso.a.quantidade.de.produto.selecionado.da.usina.eh.superior.quantidade.produto.restante.que.campanha.atual.produzir"));
                }
             }
          } else
          {
             throw new EstruturaEmManutencaoException(PropertiesUtil.getMessage("aviso.esta.usina.esta.em.manutencao.neste.momento"));
          }

       } catch (BlendagemInvalidaException e)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
          interfaceMensagem.setDescricaoMensagem(e.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (CampanhaIncompativelException ca)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
          interfaceMensagem.setDescricaoMensagem(ca.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (NumberFormatException nbex)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.aviso.informar.numeros"));
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (EstruturaEmManutencaoException e)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
          interfaceMensagem.setDescricaoMensagem(e.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (ExcessoDeMaterialParaEmbarqueException e)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
          interfaceMensagem.setDescricaoMensagem(e.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (OperacaoCanceladaPeloUsuarioException e)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
          interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.operacao.cancelada.pelo.usuario"));
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (SelecaoObjetoModoNuloException e)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
          interfaceMensagem.setDescricaoMensagem(e.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (ProdutoIncompativelException e)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
          interfaceMensagem.setDescricaoMensagem(e.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (ValidacaoCampoException ex){
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
           interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(ex.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       }
    }

    /**
     * Verifica se a usina est� produzindo um tipo de produto compat�vel com o tipo de produto atualmente selecionado
     * @param tipoDeProdutoAtualmenteSelecionado o tipo de produto atualmente selecionado
     * @return verdadeiro caso a usina esteja produzindo um tipo de produto compat�vel com o tipo de produto atualmente selecionado
     */
    private boolean usinaEstahProduzindoTipoDeProdutoCompativel(TipoProduto tipoDeProdutoAtualmenteSelecionado)
    {
       boolean usinaEstahProduzindoTipoDeProdutoCompativel = false;

       //Se houver um tipo de produto selecionado...
       if(tipoDeProdutoAtualmenteSelecionado!=null)
       {
          //... separamos a campanha desta usina...
          Campanha campanhaAtual = usinaVisualizada.getMetaUsina().getCampanhaAtual(this.getHoraSituacao());

          //... e caso a sua pelota, pellet feed ou screening sejam compat�veis com o tipo de produto atualmente selecionados, retornamos verdadeiro
          if(campanhaAtual.getTipoProduto().equals(tipoDeProdutoAtualmenteSelecionado))
             usinaEstahProduzindoTipoDeProdutoCompativel=true;
          else if(campanhaAtual.getTipoPellet().equals(tipoDeProdutoAtualmenteSelecionado))
             usinaEstahProduzindoTipoDeProdutoCompativel=true;
          else if(campanhaAtual.getTipoScreening().equals(tipoDeProdutoAtualmenteSelecionado))
             usinaEstahProduzindoTipoDeProdutoCompativel=true;
       }else//... caso contr�rio, obviamente podemos selecionar a usina.
       {
          usinaEstahProduzindoTipoDeProdutoCompativel=true;
       }

       return usinaEstahProduzindoTipoDeProdutoCompativel;
    }

    @Override
    public void deselecionar()
    {
       try
       {
          controladorDSP.deselecionaCampanhaDeUsina(usinaVisualizada.getMetaUsina().getCampanhaAtual(this.getHoraSituacao()));
          desenhaUsina();
          usinaSelecionada = Boolean.FALSE;
       } catch (BlendagemInvalidaException e)
       {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(e.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (ProdutoIncompativelException e)
      {
    	   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(e.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
      }
    }

    public Double getTempoEmpilhamento() {
        return tempoEmpilhamento;
    }

    public void setTempoEmpilhamento(Double tempoEmpilhamento) {
        this.tempoEmpilhamento = tempoEmpilhamento;
    }

    @Override
    public Boolean isSelecionada() {
        return usinaSelecionada;
    }

    @Override
    public void setSelecionada(Boolean selecionada) {
        usinaSelecionada = selecionada;
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

    private void importarAtaDeCampanhaActionPerformed(ActionEvent evt) {

        try {

            final EditaMudancaDeCampanhaViaPlanilha edi = new EditaMudancaDeCampanhaViaPlanilha(controladorDSP);
            //abre opcao de escolher a planilha de importacao
            edi.abrePlanilha();

            controladorDSP.getInterfaceInicial().getInterfaceComandos().desabilitarTodasFuncoes();

            final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
            interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
            interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
            interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.processamento.importar.ata.campanha"));

            new Thread("thread importa o ta campanha") {
                @Override
                public void run() {
                    try {
                        controladorDSP.ativarMensagem(interfaceMensagemProcessamento);
                        CriaAtividadeMundacaDeCampanha criaAtividade = new CriaAtividadeMundacaDeCampanha(edi, controladorDSP);
                        //para cada atividade na lista de atividades criadas a partir da leitura da planilha, adicinar no ControladorPlanejamento
                        List<Atividade> listaAtividade = criaAtividade.criaAtividades(controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida());
                        controladorDSP.getInterfaceInicial().mudarCampanha(listaAtividade);
                        controladorDSP.getInterfaceDSP().getInterfaceInicial().atualizarDSP();
                        // limpando a mensagem de processamento
                        interfaceDSP.getInterfaceInicial().desativarMensagem();
                    } catch (ValidacaoCampoException ex) {
                        controladorDSP.getInterfaceDSP().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorDSP.ativarMensagem(interfaceMensagem);
                    } catch (Exception ex) {
                        controladorDSP.getInterfaceDSP().getInterfaceInicial().desativarMensagem();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorDSP.ativarMensagem(interfaceMensagem);
                    } finally {
                        // habilita todas as funcoes
                        controladorDSP.getInterfaceInicial().getInterfaceComandos().habilitarTodasFuncoes();
                    }
                }
            }.start();
        } catch (IOException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("aviso.mensagem.ocorreu.erro.durante.leitura.planilha"));
            controladorDSP.ativarMensagem(interfaceMensagem);
        } catch (BiffException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
           interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("aviso.mensagem.ocorreu.erro.durante.leitura.planilha"));
            controladorDSP.ativarMensagem(interfaceMensagem);
        } catch (OperacaoCanceladaPeloUsuarioException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        } catch (LeituraPlanilhaException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
    }

    private void desenhaUsina(){
        this.setImagemDSP("usina-a.png");
    }
    private void desenhaUsinaSelecionada()
    {
        this.setImagemDSP("usina-b.png");
    }

    private void atualizacaoRecuperacaoUsina(ActionEvent evt) {
      InterfaceUsina usinaClicada = obterUsinaClicada(evt);
      if (usinaClicada.getUsinaVisualizada().equals(getUsinaVisualizada()))
      {
         try
         {        	 
             if (this.getUsinaVisualizada().getMetaUsina().getCampanhaAtual(this.getHoraSituacao()) == null) {
                 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                 interfaceMensagem = new InterfaceMensagem();
                 interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                 interfaceMensagem.setDescricaoMensagem("Usina não possui campanha atual !");
                 controladorDSP.ativarMensagem(interfaceMensagem);             
                 return;
             }
             
             
         if (this.getUsinaVisualizada().getEstado().equals(EstadoMaquinaEnum.OPERACAO)
     			&& this.getUsinaVisualizada().getAtividade() != null && !this.getUsinaVisualizada().getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)  ) {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
        	 interfaceMensagem = new InterfaceMensagem();
        	 interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
        	 interfaceMensagem.setDescricaoMensagem("Usina executando atividade " + this.getUsinaVisualizada().getAtividade().getTipoAtividade().toString() + "!");
        	 controladorDSP.ativarMensagem(interfaceMensagem);             
        	 return;
         }
        	 controladorDSP.atualizaRecuperacaoUsina(usinaVisualizada, this);
             controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();          
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException erroSis)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
    }
  

    private void adicionaNomeUsina(int comprimento){        
        labelNome = new JLabel(this.getUsinaVisualizada().getNomeUsina());
        labelNome.setFont(new Font("Verdana", Font.PLAIN, 11));
        labelNome.setForeground(new Color(104, 104, 104));
        this.add(labelNome);
        //this.setBounds( , eixoY, largura, comprimento + 30);
        labelNome.setBounds(0, comprimento + 2, 80, 15);
    }

    private void atualizacaoPilhaEmergencia(ActionEvent evt) {
      InterfaceUsina usinaClicada = obterUsinaClicada(evt);
      if (usinaClicada.getUsinaVisualizada().equals(getUsinaVisualizada()))
      {
         try
         {
             if (this.getUsinaVisualizada().getEstado().equals(EstadoMaquinaEnum.OPERACAO)
                             && this.getUsinaVisualizada().getAtividade() != null && !this.getUsinaVisualizada().getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_PILHA_DE_EMERGENCIA)  ) {
                          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                          interfaceMensagem = new InterfaceMensagem();
                          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                          interfaceMensagem.setDescricaoMensagem("Usina executando atividade " + this.getUsinaVisualizada().getAtividade().getTipoAtividade().toString() + "!");
                          controladorDSP.ativarMensagem(interfaceMensagem);             
                          return;
                      }
                     
             
             
             controladorDSP.atualizaPilhaEmergencia(usinaVisualizada, this);
             controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException erroSis)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
    }

    protected void desabilitaMenusPermissaoUsuario()
    {
        if(controladorDSP.getInterfaceInicial().verificaPermissaoAtualizacaoProducao()){
            if (mnuAtualizacaoPilhaEmergencia != null)
            {//pois a usina 2 n�o possui este menu
                mnuAtualizacaoPilhaEmergencia.setEnabled(false);
            }
            mnuAtualizacaoRecuperacao.setEnabled(false);
            mnuEditarCampanha.setEnabled(false);
            mnuExcluirCampanha.setEnabled(false);
            mnuImportarCampanha.setEnabled(false);
        }
    }

    public Date getHoraSituacao() {
        return horaSituacao;
    }

    public void setHoraSituacao(Date horaSituacao) {
        this.horaSituacao = horaSituacao;
    }


    public JLabel getLabelNome() {
        return labelNome;
    }


    public void setLabelNome(JLabel labelNome) {
        this.labelNome = labelNome;
    }

}