
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceEditarCampanha.ACAO;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardTableModelCustom;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.services.controladores.impl.ControladorUsinas;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InformacoesCampanhaPnl extends javax.swing.JPanel {

    /** Lista de Situacoes de Patio onde existem as campanhas */
    private List<SituacaoPatio> listaSituacaoPatio;
    /** Constantes referente as colunas da tabela */
    private final Integer COL_USINA = 0;
    private final Integer COL_DATA_INICIAL = 1;
    private final Integer COL_DATA_FINAL = 2;
    private final Integer COL_TIPO_PRODUTO = 3;
    private InterfaceDSP interfaceDSP;
    private Campanha campanha;
    /* private final Integer COL_QTDE_PRODUTO_PRODUZIDO = 4;
     private final Integer COL_QTDE_PRODUTO_A_PRODUZIR = 5;*/
    /** Vetor com os dados da tabela */
    private Vector vInformacoesCampanha;
    /** Lista com as informacoes das colunas da tabela */
    private List<ColunaTabela> listaColunas;
    /** Lista de Campanhas jah incluidas na tabela */
    private Map<Integer, Campanha> listaCampanhasIncluidas;
    /** A situacao de patio exibida no slider */
    private SituacaoPatio situacaoPatioExibida;
    private InterfaceMensagem interfaceMensagem;
    private JPopupMenu popMnuEditarUsina;
    public InformacoesCampanhaPnl(List<SituacaoPatio> listaSituacaoPatio, SituacaoPatio situacaoPatioExibida,InterfaceDSP interfaceDSP) {
        try {
            initComponents();
            this.listaSituacaoPatio = listaSituacaoPatio;
            this.situacaoPatioExibida = situacaoPatioExibida;
            this.interfaceDSP = interfaceDSP;
            vInformacoesCampanha = new Vector();
            listaCampanhasIncluidas = new HashMap<Integer, Campanha>();
            criaColunasTabela();
            limpaInformacoesCampanha();

            tblInformacoesCampanha.setModel(new CFlexStockyardTableModelCustom());
            tblInformacoesCampanha.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tblInformacoesCampanhaMouseClicked(evt);
                }

            });

            montaTabelaInformacoesCampanha();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Monta a tabela com as informacoes das campanhas existentes nas situacoes em exibi 
     * @throws java.lang.Exception
     */
    private void montaTabelaInformacoesCampanha() throws Exception {
        listaCampanhasIncluidas.clear();
        Object[] dadoCampanha = new Object[4];
        int index = 0;
        for (Usina usina : situacaoPatioExibida.getPlanta().getListaUsinas(situacaoPatioExibida.getDtInicio())) {            
            for (Campanha campanha : usina.getMetaUsina().getCampanhasFuturas(situacaoPatioExibida.getDtInicio())) {
                dadoCampanha[COL_USINA] = usina.getNomeUsina();
                dadoCampanha[COL_DATA_INICIAL] = DSSStockyardTimeUtil.formatarData(campanha.getDataInicial(), PropertiesUtil
                                .buscarPropriedade("formato.campo.datahora"));
                dadoCampanha[COL_DATA_FINAL] = DSSStockyardTimeUtil.formatarData(campanha.getDataFinal(), PropertiesUtil
                                .buscarPropriedade("formato.campo.datahora"));
                dadoCampanha[COL_TIPO_PRODUTO] = campanha.getTipoProduto().getCodigoFamiliaTipoProduto() + " - "
                                + campanha.getTipoProduto().getCodigoTipoProduto();
                /* dadoCampanha[COL_QTDE_PRODUTO_PRODUZIDO] = DSSStockyardFuncoesNumeros.getQtdeFormatada(calculaQuantidadeProdutoProduzido(usina,campanha), 2);
                 dadoCampanha[COL_QTDE_PRODUTO_A_PRODUZIR] = DSSStockyardFuncoesNumeros.getQtdeFormatada(calculaQuantidadeProdutoParaProduzir(usina,campanha), 2);
                */
                vInformacoesCampanha.add(new Vector(Arrays.asList(dadoCampanha)));
                listaCampanhasIncluidas.put(index, campanha);
                index++;
            }
        }
        // atualizando a tabela com suas campanhas
        CFlexStockyardFuncoesTabela.setInformacoesTabela(tblInformacoesCampanha, vInformacoesCampanha, listaColunas);
        criaPopMenuParaUsina();
    }


    /**
     * Limpa a tabela que contem as informacoes das campanhas
     * @throws java.lang.Exception
     */
    private void limpaInformacoesCampanha() throws Exception {
        vInformacoesCampanha.removeAllElements();
        CFlexStockyardFuncoesTabela.setInformacoesTabela(tblInformacoesCampanha, vInformacoesCampanha, listaColunas);
    }

    /**
     * Metodo que cria a lista de colunas para exibicao na tabela
     */

    private void criaColunasTabela() {
        listaColunas = new ArrayList<ColunaTabela>();
        ColunaTabela col;

        col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(83);
        col.setRedimensionar(Boolean.FALSE);
        col.setTitulo("Usina");
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(133);
        col.setRedimensionar(Boolean.FALSE);
        col.setTitulo("Data inicial");
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(133);
        col.setRedimensionar(Boolean.FALSE);
        col.setTitulo("Data final");
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(105);
        col.setRedimensionar(Boolean.FALSE);
        col.setTitulo("Produto");
        listaColunas.add(col);

        /*col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(70);
        col.setRedimensionar(Boolean.TRUE);
        col.setTitulo("Produzido");
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(65);
        col.setRedimensionar(Boolean.FALSE);
        col.setTitulo("A produzir");
        listaColunas.add(col);*/

    }

    private void tblInformacoesCampanhaMouseClicked(MouseEvent evt) {
        // TODO Auto-generated method stub
        
            if(evt.getButton() == MouseEvent.BUTTON3) {
                int linhaSelecionada = tblInformacoesCampanha.rowAtPoint(evt.getPoint());
                tblInformacoesCampanha.setRowSelectionInterval(linhaSelecionada, linhaSelecionada);
                this.campanha = listaCampanhasIncluidas.get(linhaSelecionada);
                popMnuEditarUsina.show(tblInformacoesCampanha, evt.getX(), evt.getY());
                //editaDadosCampanha(evt);
           }                      
    }

    
    private void criaPopMenuParaUsina() {

        popMnuEditarUsina = new JPopupMenu();
//    
        //JMenuItem
        JMenuItem mnuEditarCampanha = new JMenuItem();
        mnuEditarCampanha.setText(PropertiesUtil.getMessage("menu.editar.campanha"));
        mnuEditarCampanha.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
               editaDadosCampanha();
            }
        });
        popMnuEditarUsina.add(mnuEditarCampanha);

      //JMenuItem
        JMenuItem mnuExcluirCampanha = new JMenuItem();
        mnuExcluirCampanha.setText(PropertiesUtil.getMessage("menu.excluir.campanha"));
        mnuExcluirCampanha.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
               excluiDadosCampanha();
            }
        });
        popMnuEditarUsina.add(mnuExcluirCampanha);

        
        //this.tblInformacoesCampanha.setComponentPopupMenu(popMnuEditarUsina);
    }

    private void editaDadosCampanha()  {
        try {
            if (this.campanha != null) {
            this.interfaceDSP.getControladorDSP().verificarModoDeEdicao();
            InterfaceUsina interfaceUsina = null;
            for (InterfaceUsina interfaceU : this.interfaceDSP.getListaUsinas()) {
                if (interfaceU.getUsinaVisualizada().getMetaUsina().equals(campanha.getMetaUsina())) {
                    interfaceUsina = interfaceU;
                    break;
                }
            }
            this.interfaceDSP.getControladorDSP().editaCampanhaProducao(interfaceUsina, this.interfaceDSP.getInterfaceInicial().getListaTiposProduto(), this.interfaceDSP.getInterfaceInicial().getListaTiposItemDeControle(),ACAO.EDICAO,campanha);
            }   
        } catch (ErroSistemicoException ex) {
            this.interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            this.interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        }catch(ModoDeEdicaoException mdex){
            this.interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
            this.interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        } catch (OperacaoCanceladaPeloUsuarioException e) {
            // TODO Auto-generated catch block
            this.interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            this.interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        }
    }


    private void excluiDadosCampanha()  {
        try {
            if (this.campanha != null) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(campanha.getNomeCampanha());
                buffer.append(" - ");
                buffer.append(campanha.getDataInicial());
                buffer.append(" - ");
                buffer.append(campanha.getDataFinal());
                
                JLabel pergunta = new JLabel("Deseja excluir a campanha " + buffer.toString());
                
                pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                int confirm = JOptionPane.showOptionDialog(null, pergunta, PropertiesUtil.getMessage("popup.atencao"),
                                JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    
                if (confirm == JOptionPane.NO_OPTION) {
                    return;
    
                } else {
                        ControladorUsinas.getInstance().removerCampanha(this.campanha);
                        this.limpaInformacoesCampanha();
                        this.montaTabelaInformacoesCampanha();
                    
                }
    
            }   
        } catch (ErroSistemicoException ex) {
            this.interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            this.interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        }catch(ModoDeEdicaoException mdex){
            this.interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
            this.interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        } catch (OperacaoCanceladaPeloUsuarioException e) {
            // TODO Auto-generated catch block
            this.interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            this.interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        } catch (ValidacaoCampoException e) {
            // TODO Auto-generated catch block
            this.interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            this.interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.interfaceDSP.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            this.interfaceDSP.getControladorDSP().ativarMensagem(interfaceMensagem);
        }
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrInformacoesCampnha = new javax.swing.JScrollPane();
        tblInformacoesCampanha = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();

        setLayout(new java.awt.BorderLayout());

        scrInformacoesCampnha.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblInformacoesCampanha.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{

        }, new String[]{

        }));
        tblInformacoesCampanha.setFont(new java.awt.Font("Tahoma", 0, 9));
        scrInformacoesCampnha.setViewportView(tblInformacoesCampanha);

        add(scrInformacoesCampnha, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrInformacoesCampnha;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblInformacoesCampanha;
    // End of variables declaration//GEN-END:variables
}