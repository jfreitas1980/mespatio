package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.ControladorDSP;
import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBerco;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gráfica que representa o {@link Pier}.
 * 
 * @author andre
 * 
 */
public class InterfacePier extends RepresentacaoGrafica implements InterfaceInicializacao
{

   /** serial gerado */
   private static final long serialVersionUID = -734812151292622648L;

   private Date horaSituacao;
   /** acesso às operações do subsistema de interface gráfica DSP */
   private ControladorDSP controladorDSP;

   /** o píer visualizado nesta interface */
   private Pier pierVisualizado;

   /** a interface gráfica de fila de navios */
   private JPanel pnlInterfacePier;

   /** acesso às operações da interface gráfica de fila de navios */
   private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;

   /** a lista das interfaces dos berços deste píer */
   private List<InterfaceBerco> listaDeBercos;

   /** a lista das interfaces das carregadoras de navio deste píer */
   private List<InterfaceCarregadoraDeNavios> listaDeCarregadorasDeNavio;

   private int variacaoLarguraDesenhoPier;

   private int alturaDesenhoPier;

   /** a interface das mensagens exibidas pela interface baliza */
   private InterfaceMensagem interfaceMensagem;

   /** Constante que refere-se a quantidade de objetos da fila de navios
    * Nesta caso possui 3 : Navio1, Navio2 e Pier
    */
   private final int QTDE_OBJETOS_PIER = 3;

   public InterfacePier(ControladorDSP controladorDSP,Date horaSituacao)
   {
      super();
      this.controladorDSP = controladorDSP;
      this.horaSituacao = horaSituacao;
   }

   @Override
   public void inicializaInterface()
   {
      defineDimensoesFixas();
      calculaPosicaoPier();
      desenhaBercosDoPier();
      criaPopMenuPier();
      desabilitaMenusPermissaoUsuario();
   }

   private void criaPopMenuPier()
   {
      JPopupMenu popMnuPier = new JPopupMenu();

      JMenuItem mnuListaInterdicoes = new JMenuItem();
      mnuListaInterdicoes.setText(PropertiesUtil.getMessage("mensagem.titulo.janela.baliza.interdicao"));
      mnuListaInterdicoes.setToolTipText(PropertiesUtil.getMessage("mensagem.titulo.janela.baliza.interdicao"));
      mnuListaInterdicoes.addActionListener(new java.awt.event.ActionListener()
      {

         @Override
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            apresentaListaDeInterdicoes(evt);
         }
      });
      popMnuPier.add(mnuListaInterdicoes);

      this.setComponentPopupMenu(popMnuPier);
   }

   /**
    * Metodo que apresenta a interface de lista de manutencoes da maquina visualizada.
    * @param evt
    */
   private void apresentaListaDeInterdicoes(ActionEvent evt)
   {
      try {
         controladorDSP.interditaPier(this);
      }
      catch (OperacaoCanceladaPeloUsuarioException ex) {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(ex.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (ErroSistemicoException ex) {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(ex.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }

   @Override
   public void defineDimensoesFixas()
   {
      variacaoLarguraDesenhoPier = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.pier.variacao.largura").trim());
      alturaDesenhoPier = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.pier.altura").trim());
   }

   /**
    * Calcula a posicao do componente pier na interface fila de navios
    */
   private void calculaPosicaoPier()
   {

      Rectangle rectParent = this.pnlInterfacePier.getBounds();
      int larguraComponentes = (int) (rectParent.getBounds().getWidth() / QTDE_OBJETOS_PIER);
      int larguraComponentePier = larguraComponentes - variacaoLarguraDesenhoPier;
      int alturaPier = (int) rectParent.getHeight();

      int posicaoX = (int) (rectParent.getBounds().getWidth() / 2) - 5;
      int posicaoY = (int) ((rectParent.getHeight() / 2) - 70);

      this.setDimensaoImagem(posicaoX, posicaoY, 13, alturaDesenhoPier);
      this.setBounds(posicaoX, posicaoY, 13, alturaDesenhoPier);
      if (this.pierVisualizado.getMetaPier().pierInterditado(horaSituacao))
      {
         this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
         this.setToolTipText(pierVisualizado.getNomePier() + " - Interditado ");
      }
      else
      {
         this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
         this.setToolTipText(pierVisualizado.getNomePier() + " - Liberado ");
      }
   }

      /** Desenha os bercos de atracacao do pier */
    private void desenhaBercosDoPier()
    {
      listaDeBercos = montaListaInterfaceBercos();
      
      for (InterfaceBerco interfaceBerco : listaDeBercos) {
         this.pnlInterfacePier.add(interfaceBerco);
      }
    }

   /**
    * Cria uma lista de interfaces de berco para exibicao na interface
    * @return
    */
   private List<InterfaceBerco> montaListaInterfaceBercos()
   {
       /***
        * 
        * TODO ARRUMAR INTERFACE, CARREGA OS BERÇOS DESORDENADOS FIXOU O PAINEL DE INFORMACAO , MAS NA A ORDEM DOS BERCOS
        * 
        * 
        */
       
       InterfaceBerco bercoOeste = null;
       InterfaceBerco bercoLeste = null;
      List<InterfaceBerco> listaInterfaceBerco = new ArrayList<InterfaceBerco>();
      controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().getPnlInformacoesBercoOeste().removeAll();
      controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().getPnlInterfaceInformacaoBercoLeste().removeAll();
      Collections.sort(pierVisualizado.getListaDeBercosDeAtracacao(horaSituacao), new ComparadorBerco() );
      Collections.reverse(pierVisualizado.getListaDeBercosDeAtracacao(horaSituacao));
      for (Berco berco : pierVisualizado.getListaDeBercosDeAtracacao(horaSituacao)) {
         InterfaceBerco interfaceBerco = new InterfaceBerco();
         interfaceBerco.setBercoVisualizada(berco);
         interfaceBerco.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaDeNavios);
         if (berco.getNomeBerco().toUpperCase().endsWith("OESTE")) {
            interfaceBerco.setPainelInformacoesNavioAtracado(controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().getPnlInformacoesBercoOeste());
            bercoOeste = interfaceBerco;            
         }
         else /*if(berco.getNomeBerco().toUpperCase().endsWith("LESTE"))*/ {
            interfaceBerco.setPainelInformacoesNavioAtracado(controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().getPnlInterfaceInformacaoBercoLeste());
            bercoLeste = interfaceBerco;
         }
         interfaceBerco.setPier(this);
      }
      listaInterfaceBerco.add(0,bercoOeste);
      listaInterfaceBerco.add(1,bercoLeste);
      return listaInterfaceBerco;
   }

   public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios()
   {
      return controladorInterfaceFilaDeNavios;
   }

   public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios)
   {
      this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
   }

   public List<InterfaceBerco> getListaDeBercos()
   {
      return listaDeBercos;
   }

   public void setListaDeBercos(List<InterfaceBerco> listaDeBercos)
   {
      this.listaDeBercos = listaDeBercos;
   }

   public List<InterfaceCarregadoraDeNavios> getListaDeCarregadorasDeNavio()
   {
      return listaDeCarregadorasDeNavio;
   }

   public void setListaDeCarregadorasDeNavio(List<InterfaceCarregadoraDeNavios> listaDeCarregadorasDeNavio)
   {
      this.listaDeCarregadorasDeNavio = listaDeCarregadorasDeNavio;
   }

   public Pier getPierVisualizado()
   {
      return pierVisualizado;
   }

   public void setPierVisualizado(Pier pierVisualizado)
   {
      this.pierVisualizado = pierVisualizado;
   }

   public int getAlturaDesenhoPier()
   {
      return alturaDesenhoPier;
   }

   public void setAlturaDesenhoPier(int alturaDesenhoPier)
   {
      this.alturaDesenhoPier = alturaDesenhoPier;
   }

   public int getVariacaoLarguraDesenhoPier()
   {
      return variacaoLarguraDesenhoPier;
   }

   public void setVariacaoLarguraDesenhoPier(int variacaoLarguraDesenhoPier)
   {
      this.variacaoLarguraDesenhoPier = variacaoLarguraDesenhoPier;
   }

   public JPanel getPnlInterfacePier()
   {
      return pnlInterfacePier;
   }

   public void setPnlInterfacePier(JPanel pnlInterfacePier)
   {
      this.pnlInterfacePier = pnlInterfacePier;
   }

   /**
    * Metodo que desabilita os menus existentes neste objeto em caso de processamento
    */
   @Override
   public void desabilitarMenus()
   {
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
   public void habilitarMenus()
   {
      if (this.getComponentPopupMenu() != null) {
         for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
            this.getComponentPopupMenu().getComponent(i).setEnabled(true);
         }
      }
   }

   private void desabilitaMenusPermissaoUsuario()
   {
      if (controladorDSP.getInterfaceInicial().verificaPermissaoAtualizacaoProducao()) {
         desabilitarMenus();
      }
   }

public Date getHoraSituacao() {
    return horaSituacao;
}

public void setHoraSituacao(Date horaSituacao) {
    this.horaSituacao = horaSituacao;
}
}
