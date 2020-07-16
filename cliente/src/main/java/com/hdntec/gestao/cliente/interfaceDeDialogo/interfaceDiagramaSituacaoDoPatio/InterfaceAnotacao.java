/**
 * 
 */
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.planta.entity.status.Anotacao;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface que representa a entidade {@link Anotacao}.
 * 
 * @author andre
 *
 */
public class InterfaceAnotacao extends RepresentacaoGrafica implements InterfaceInicializacao
{
   
   /** a {@link Anotacao} visualizada nesta interface */
   private Anotacao anotacaoVisualizada;

   /** a interface gráfica que representa o {@link Patio} no qual esta anotacao se encontra */
   private InterfacePatio interfacePatio;
   
   /** acesso às operações do subsistema de interface gráfica DSP */
   private ControladorDSP controladorDSP;

   /** interface mensagem de aviso ao usuario*/
   private InterfaceMensagem interfaceMensagem;

   /** posição da anotacao no DSP */
   private int eixoX = 0;
   private int eixoY = 0;
   private int largura;
   private int comprimento;

   /**
    * contrutor de classe
    */
   public InterfaceAnotacao(int eixoX, int eixoY){
       this.eixoX = eixoX;
       this.eixoY = eixoY;       
   }


   @Override
   public void defineDimensoesFixas()
   {
      comprimento = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.anotacao.comprimento").trim());
      largura = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.anotacao.largura").trim());
      
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

   private void criaPopmenuAnotacao(){
       JPopupMenu popMenuAnotacao = new JPopupMenu();
       //se o usuario logado tiver permissão para excluir anotacao habilita-se este menu
       if (controladorDSP.verificaPermissaoParaExcluirAnotacao()) {
           //menu de excluir anotacao
           JMenuItem mnuExcluirAnotacao = new JMenuItem();
           mnuExcluirAnotacao.setText(PropertiesUtil.getMessage("menu.excluir.anotacao"));
           mnuExcluirAnotacao.addActionListener(new ActionListener() {

               @Override
               public void actionPerformed(ActionEvent evt) {
                   excluirAnotacao();
               }
           });
           popMenuAnotacao.add(mnuExcluirAnotacao);
       }

        //menu de edicao da anotacao
        JMenuItem mnuEditarAnotacao = new JMenuItem();
        mnuEditarAnotacao.setText(PropertiesUtil.getMessage("menu.editar.anotacao"));
        mnuEditarAnotacao.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    editarAnotacao();
                } catch (OperacaoCanceladaPeloUsuarioException ex) {
                	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    controladorDSP.ativarMensagem(interfaceMensagem);
                }
            }
        });
        popMenuAnotacao.add(mnuEditarAnotacao);

        // Adiciona o popmenu neste component
        this.setComponentPopupMenu(popMenuAnotacao);
    }

   @Override
   public void inicializaInterface()
   {
      //define imagem
      this.setImagemDSP("anotacao1.gif");
      defineDimensoesFixas();
      calculaPosicaoAnotacao();
      criaPopmenuAnotacao();
      montaMensagemToolTip();
   }

   /**
    * apresenta o tooltip desta interface
    */
   private void montaMensagemToolTip(){
       String html = "<html>", html_end = "</html>";
       String texto = anotacaoVisualizada.toString();
       //monta inicio e fim das tag's em HTML
       texto = html.concat(texto);
       texto = texto.concat(html_end);

       //apresenta o tooltip
       this.setToolTipText(texto);
   }

   /**
    * calcula posicao da imagem no DSP
    */
   private void calculaPosicaoAnotacao(){
         int largura1 = 0, altura = 0;
         largura1 = this.largura;
         altura = this .comprimento;

         this.setDimensaoImagem(eixoX, eixoY, largura1, altura);
         //tambem eh necessario setar esplicitamento o setBounds para o Jpanel que a figura esta representando
         this.setBounds(eixoX, eixoY, largura1, altura);
   }
   
   /**
    * Edita uma anotação de um pátio
    */
   public void editarAnotacao() throws OperacaoCanceladaPeloUsuarioException
   {
      controladorDSP.editarAnotacao(this);
   }
   
   /**
    * Exclui uma anotação de um pátio
    */
   public void excluirAnotacao()
   {
      controladorDSP.excluirAnotacao(this);
   }

    public Anotacao getAnotacaoVisualizada() {
        return anotacaoVisualizada;
    }

    public void setAnotacaoVisualizada(Anotacao anotacaoVisualizada) {
        this.anotacaoVisualizada = anotacaoVisualizada;
    }

    public ControladorDSP getControladorDSP() {
        return controladorDSP;
    }

    public void setControladorDSP(ControladorDSP controladorDSP) {
        this.controladorDSP = controladorDSP;
    }

    public InterfacePatio getInterfacePatio() {
        return interfacePatio;
    }

    public void setInterfacePatio(InterfacePatio interfacePatio) {
        this.interfacePatio = interfacePatio;
    }

   
   
}
