package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceBlendagem;


import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.hdntec.gestao.cliente.interfaceDeDialogo.ControladorInterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.IControladorDSP;
import com.hdntec.gestao.cliente.interfaceDeDialogo.uteis.ImageHandler;
import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.produto.comparadores.ComparadorItemControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.produto.ControladorItemDeControle;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gráfica que apresenta o atendimento de determinada carga pela blendagem de produtos de balizas e/ou de campanhas de usinas.
 * 
 * @author andre
 * 
 */
public class InterfaceBlendagem extends JPanel {

    /** Serialização*/
    private static final long serialVersionUID = -4991689327202322646L;

    /** a blendagem apresentada na tela */
    private Blendagem blendagemVisualizada;

    /** acesso as operações do subsistema de blendagem */

    private IControladorInterfaceBlendagem controladorInterfaceBlendagem;

    /** interface gráfica principal do sistema */
    private InterfaceInicial interfaceInicial;

    /** acesso às operações do DSP */
    private IControladorDSP dsp;

    /*controlador de item de controle */
    private ControladorItemDeControle controladorItemDeControle = new ControladorItemDeControle();

    /** imagens dos farois para os itens de controle*/
    public static ImageIcon imgSemaforoVerde = new ImageIcon(ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens"), "farol_qualidade-02.png"));
    public static ImageIcon imgSemaforoVermelho = new ImageIcon(ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens"), "farol_qualidade-03.png"));
    public static ImageIcon imgSemaforoAzul = new ImageIcon(ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens"), "farol_qualidade-01.png"));
    public static BufferedImage imagemFundoBlend = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens"), "bkg_qualidade.png");

    public InterfaceBlendagem(ControladorInterfaceInicial cii) {
        this.dsp = cii.getDsp();
        controladorInterfaceBlendagem = new ControladorInterfaceBlendagem();
    }

    /**
     * Metodo que exibira os itens de controle com as informacoes calculadas de Blend, Meta, Estimativa, Desvio Padrao ...
     *
     * @param blendagem
     */
    public void apresentarBlendagem(Blendagem blendagem)
   {
      this.blendagemVisualizada = blendagem;
      // primeiramente, limpa-se a visualização
      
      limparVisualizacao();
      
      if (blendagem.getProdutoResultante() != null)
      {
         Boolean apresentarBlendagem = Boolean.FALSE;
         // caso o produto selecionado seja pellet feed ou pellet screening, não é necessário apresentar a blendagem. Caso contrário, apresenta-se a blendagem
         apresentarBlendagem = ((blendagem.getProdutoResultante().getTipoProduto() != null && blendagem.getProdutoResultante().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA)) || blendagem.getProdutoResultante().getTipoProduto() == null);
         // ... caso o produto selecionado seja pelota ...
         if (apresentarBlendagem)
         {
            if(!blendagemVisualizada.isTodasAsQualidadesDosProdutosAdicionadosReais())
            {
               interfaceInicial.getLblInformacaoQualidadeOrientacaoEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.bihoraria"));
               interfaceInicial.getLblInformacaoQualidadePnlMetaEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.bihoraria"));
               interfaceInicial.getLblInformacaoQualidadeMetaProducao().setText(PropertiesUtil.getMessage("mensagem.blendagem.bihoraria"));

            }else
            {
               interfaceInicial.getLblInformacaoQualidadeOrientacaoEmbarque().setText("");
               interfaceInicial.getLblInformacaoQualidadePnlMetaEmbarque().setText("");
               interfaceInicial.getLblInformacaoQualidadeMetaProducao().setText("");
            }

            //Para saber as meta internas, eh preciso saber a carga
            Carga cargaSelecionada = blendagemVisualizada.getCargaSelecionada();

            Collections.sort(blendagemVisualizada.getProdutoResultante().getQualidade().getListaDeItensDeControle(), new ComparadorItemControle());
            
            for (ItemDeControle itemBlend : blendagemVisualizada.getProdutoResultante().getQualidade().getListaDeItensDeControle())
            {
                controladorItemDeControle.setItemDeControle(itemBlend);

               //tela de item de controle com meta da orientação de embarque
               ItemControleBlendadoPnl pnlItemBlendagemOrientacaoEmbarque = null;
               if(cargaSelecionada!=null){
                  pnlItemBlendagemOrientacaoEmbarque = new ItemControleBlendadoPnl(itemBlend, buscarItemDeControleEquivalenteNaCarga(cargaSelecionada, itemBlend), cargaSelecionada.getOrientacaoDeEmbarque().getTipoProduto(), interfaceInicial.getSituacaoPatioExibida().getDtInicio());
                  pnlItemBlendagemOrientacaoEmbarque.setInterfaceBlendagem(this);
                  pnlItemBlendagemOrientacaoEmbarque.montaDesenhoItemBlend();
               }else{
                    pnlItemBlendagemOrientacaoEmbarque = new ItemControleBlendadoPnl(itemBlend, buscarItemDeControleEquivalenteNaCarga(cargaSelecionada, itemBlend), null, interfaceInicial.getSituacaoPatioExibida().getDtInicio());
                    pnlItemBlendagemOrientacaoEmbarque.setInterfaceBlendagem(this);
                    pnlItemBlendagemOrientacaoEmbarque.montaDesenhoItemBlend();
                }
               interfaceInicial.getPnlItensControleOrientacaoEmbarque().add(pnlItemBlendagemOrientacaoEmbarque);

               //tela de item de controle com meta interna de embarque
               ItemControleBlendadoMetaEmbarque pnlItemBlendagemMetaEmbarque = null;
               if(cargaSelecionada!=null){
                  pnlItemBlendagemMetaEmbarque = new ItemControleBlendadoMetaEmbarque(itemBlend, cargaSelecionada.getOrientacaoDeEmbarque().getTipoProduto(), interfaceInicial.getSituacaoPatioExibida().getDtInicio());
                  pnlItemBlendagemMetaEmbarque.setInterfaceBlendagem(this);
                  pnlItemBlendagemMetaEmbarque.montaDesenhoItemBlend();
               }else{
                  pnlItemBlendagemMetaEmbarque = new ItemControleBlendadoMetaEmbarque(itemBlend, null, interfaceInicial.getSituacaoPatioExibida().getDtInicio());
                  pnlItemBlendagemMetaEmbarque.setInterfaceBlendagem(this);
                  pnlItemBlendagemMetaEmbarque.montaDesenhoItemBlend();
               }
               interfaceInicial.getPnlItensControleMetaEmbarque().add(pnlItemBlendagemMetaEmbarque);

               //tela de item de controle com meta interna de produção
               ItemControleBlendadoMetaProducao pnlItemBlendagemMetaProducao = null;
               if(cargaSelecionada!=null){
                  pnlItemBlendagemMetaProducao = new ItemControleBlendadoMetaProducao(itemBlend,cargaSelecionada.getOrientacaoDeEmbarque().getTipoProduto(), interfaceInicial.getSituacaoPatioExibida().getDtInicio());
                  pnlItemBlendagemMetaProducao.setInterfaceBlendagem(this);
                  pnlItemBlendagemMetaProducao.montaDesenhoItemBlend();
               }else{
                  pnlItemBlendagemMetaProducao = new ItemControleBlendadoMetaProducao(itemBlend,null, interfaceInicial.getSituacaoPatioExibida().getDtInicio());
                  pnlItemBlendagemMetaProducao.setInterfaceBlendagem(this);
                  pnlItemBlendagemMetaProducao.montaDesenhoItemBlend();
               }
               interfaceInicial.getPnlItensControleMetaProducao().add(pnlItemBlendagemMetaProducao);
            }
         }else
         {
            if(blendagem.getProdutoResultante().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_FEED))
            {
               interfaceInicial.getLblInformacaoQualidadeOrientacaoEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.pelletfeed"));
               interfaceInicial.getLblInformacaoQualidadePnlMetaEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.pelletfeed"));
               interfaceInicial.getLblInformacaoQualidadeMetaProducao().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.pelletfeed"));

            }               
            else if(blendagem.getProdutoResultante().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_SCREENING))
            {
               interfaceInicial.getLblInformacaoQualidadeOrientacaoEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.screening"));  
               interfaceInicial.getLblInformacaoQualidadePnlMetaEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.screening"));
               interfaceInicial.getLblInformacaoQualidadeMetaProducao().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.screening"));

            }             
        }
         
         if (blendagem.getProdutoResultante().getTipoProduto() != null)
         {
        		 try {
						interfaceInicial
								.atualizaTabelaInformacoesBalizasSelecionadas(blendagem
										.getMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem());
					} catch (ErroSistemicoException ex) {
						Logger.getLogger(InterfaceInicial.class.getName()).log(
								Level.SEVERE, null, ex);
					}
         	/*TODO VALIDAR NECESSIDADE DO CODIGO ABAIXO ESATVA DANDO NULL POINTER 
         	 * 
         	 * }else
         	{         
        	 if(blendagem.getProdutoResultante().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_FEED))
            {

               interfaceInicial.getLblInformacaoQualidadeOrientacaoEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.pelletfeed"));
               interfaceInicial.getLblInformacaoQualidadePnlMetaEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.pelletfeed"));
               interfaceInicial.getLblInformacaoQualidadeMetaProducao().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.pelletfeed"));

            }               
            else if(blendagem.getProdutoResultante().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_SCREENING))
            {
               interfaceInicial.getLblInformacaoQualidadeOrientacaoEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.screening"));  
               interfaceInicial.getLblInformacaoQualidadePnlMetaEmbarque().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.screening"));
               interfaceInicial.getLblInformacaoQualidadeMetaProducao().setText(PropertiesUtil.getMessage("mensagem.blendagem.qualidade.nao.relevante.screening"));
*/
           // }             
         }

      }
      
      if (blendagem.getCargaSelecionada() != null)
      {
         interfaceInicial.getLblGrpBalizaCliente().setText(blendagem.getCargaSelecionada().getNavio(interfaceInicial.getSituacaoPatioExibida().getDtInicio()).getCliente().getNomeCliente());
      }
      
   }

    /**
     * Metodo que limpara a exibicao os itens de controle com as informacoes calculadas de Blend, Meta, Estimativa, Desvio Padrao ...
     */
    public void limparVisualizacao() {
        //limpando os itens de controle apresentados
        interfaceInicial.getPnlItensControleOrientacaoEmbarque().removeAll();
        interfaceInicial.getPnlItensControleMetaEmbarque().removeAll();
        interfaceInicial.getPnlItensControleMetaProducao().removeAll();

        //limpando os textos informativos de cliente, produto e quantidade
        interfaceInicial.getLblGrpBalizaCliente().setText("");
        interfaceInicial.getLblInformacaoQualidadeOrientacaoEmbarque().setText(""); 
        interfaceInicial.getLblInformacaoQualidadePnlMetaEmbarque().setText("");
        interfaceInicial.getLblInformacaoQualidadeMetaProducao().setText("");
        
        //limpar a tabela
        try
      {
         interfaceInicial.criaColunasInformacoesBalizasSelecionadas();
      } catch (ErroSistemicoException ex)
      {
         Logger.getLogger(InterfaceInicial.class.getName()).log(Level.SEVERE, null, ex);
      }
        
    }

    public Blendagem getBlendagemVisualizada() {
        return blendagemVisualizada;
    }

    public void setBlendagemVisualizada(Blendagem blendagemVisualizada) {
        this.blendagemVisualizada = blendagemVisualizada;
    }

    public IControladorInterfaceBlendagem getIControladorInterfaceBlendagem() {
        return controladorInterfaceBlendagem;
    }

    public void setIControladorInterfaceBlendagem(IControladorInterfaceBlendagem controladorInterfaceBlendagem) {
        this.controladorInterfaceBlendagem = controladorInterfaceBlendagem;
    }

    public InterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    public void setInterfaceInicial(InterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;
    }

    /**
     * Metodo que busca um item de controle equivalente na carga selecionada. Caso a carga selecionada
     * for nula o item de controle equivalente tambem será.
     * @param cargaSelecionada
     * @param itemBlend
     * @return
     */
    private ItemDeControle buscarItemDeControleEquivalenteNaCarga(Carga cargaSelecionada, ItemDeControle itemBlend) {
        ItemDeControle itemControleEquivalente = null;
        if (cargaSelecionada != null) {
            // percorrer a lista de item de controle da orientacao de embarque da carga selecionada
            for (ItemDeControle itemControleCargaSelecionada : cargaSelecionada.getOrientacaoDeEmbarque().getListaItemDeControle()) {
                if (itemControleCargaSelecionada.getTipoItemControle().equals(itemBlend.getTipoItemControle())) {
                    itemControleEquivalente = itemControleCargaSelecionada;
                    break;
                }
            }
        }
        return itemControleEquivalente;
    }

    public ControladorItemDeControle getControladorItemDeControle() {
        return controladorItemDeControle;
    }

    
}
