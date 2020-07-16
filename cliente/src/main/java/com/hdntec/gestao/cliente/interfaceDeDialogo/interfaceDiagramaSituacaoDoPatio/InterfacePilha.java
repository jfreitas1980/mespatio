package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores.ComparadorInterfaceBaliza;
import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceDeDialogo.uteis.ImageHandler;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gráfica que representa o {@link Patio}.
 * 
 * @author andre
 * 
 */
public class InterfacePilha extends RepresentacaoGrafica implements InterfaceInicializacao {

    /** serial gerado */
    private static final long serialVersionUID = 2608398755652208034L;
    
    /** o {@link Pilha} visualizado na interface gráfica */
    private Pilha pilhaVisualizada;

    /** acesso às operações do subsistema de interface gráfica DSP */
    private ControladorDSP controladorDSP;

    /** a interface gráfica DSP */
    private InterfaceDSP interfaceDSP;

    /** a lista de balizas que compoem esta pilha */
    private List<InterfaceBaliza> listaDeBalizas;

    private int alturaPilha;

    /** identifica se todas as balizas da pilha estao selecionadas */
    private Boolean pilhaSelecionada;

    public InterfacePilha(String nomeImagem) {
        super(nomeImagem);
        pilhaSelecionada = Boolean.FALSE;        
    }

    public InterfacePilha() {
        super();
        this.setLayout(null);
        pilhaSelecionada = Boolean.FALSE;        
    }

    @Override
    public void inicializaInterface() {
        this.setToolTipText(pilhaVisualizada.getNomePilha());
        defineDimensoesFixas();
        definePropriedadesPilha();
        calculaPosicaoPilha();
    }

    private void definePropriedadesPilha() {
        
        this.setToolTipText(pilhaVisualizada.getNomePilha());
        // setando a pilha na lista de balizas da pilha
        for (InterfaceBaliza interfaceBaliza : listaDeBalizas) {
            interfaceBaliza.setInterfacePilha(this);
        }
    }

    public InterfaceBaliza obterBalizaClicada(ActionEvent evt) {
        JMenuItem mnuItem = (JMenuItem) evt.getSource();
        JPopupMenu popupMenu = (JPopupMenu) mnuItem.getParent();
        return (InterfaceBaliza) popupMenu.getInvoker();
    }

    public void selecionarPilhaActionPerformed(ActionEvent evt) {
        try {
            Date horaSituacao =  listaDeBalizas.get(0).getInterfacePatio().getHoraSituacao();
            // [SA8753] - Se estiver no modo de empilhar, não deve ser poossível selecionar a pilha
            //if (!controladorDSP.getInterfaceDSP().getInterfaceInicial().getModoDeOperacao().equals(ModoDeOperacaoEnum.EMPILHAR)) {
                InterfaceBaliza balizaClicada = obterBalizaClicada(evt);
                //if (balizaClicada.getBalizaVisualizada().getPilha() != null) {
                    //if (balizaClicada.getBalizaVisualizada().getPilha().equals(pilhaVisualizada)) {
                        controladorDSP.selecionaBaliza(listaDeBalizas);
                        controladorDSP.getListaDeBalizas().addAll(listaDeBalizas);

                        selecionaAsBalizasDaPilha();
//                        for (InterfaceBaliza interfaceBaliza : listaDeBalizas) {
//                			interfaceBaliza.selecionar();
//                        }
                    //}
                //} else {
                    //throw new AtividadeException(PropertiesUtil.getMessage("aviso.baliza.selecionada.nao.possui.pilha.relacionada"));
                //}
            //} else {
                //throw new AtividadeException(PropertiesUtil.getMessage("avido.nao.eh.possivel.empilhar.numa.baliza.que.esteja.preenchida"));
            //}
        } catch (BlendagemInvalidaException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
            Logger.getLogger(InterfacePilha.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CampanhaIncompativelException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
            Logger.getLogger(InterfacePilha.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProdutoIncompativelException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
            Logger.getLogger(InterfacePilha.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExcessoDeMaterialParaEmbarqueException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
            Logger.getLogger(InterfacePilha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deselecionarPilhaActionPerformed(ActionEvent evt) {
                try {
                    controladorDSP.deselecionaBaliza(listaDeBalizas);
                    controladorDSP.getListaDeBalizas().removeAll(listaDeBalizas);
                    deselecionaAsbalizasDaPilha();
                } catch (BlendagemInvalidaException ex) {
                    Logger.getLogger(InterfacePilha.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CampanhaIncompativelException ex) {
                    Logger.getLogger(InterfacePilha.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ProdutoIncompativelException ex) {
                    Logger.getLogger(InterfacePilha.class.getName()).log(Level.SEVERE, null, ex);
                }
    }

    @Override
    public void defineDimensoesFixas() {
        alturaPilha = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.pilha.comprimento").trim());
    }

    private void calculaPosicaoPilha() {

        InterfaceBaliza balizaInicial = new LinkedList<InterfaceBaliza>(listaDeBalizas).getFirst();
        InterfaceBaliza balizaFinal = new LinkedList<InterfaceBaliza>(listaDeBalizas).getLast();

        int eixoX = 0, eixoY = 0, largura = 0;

        if (balizaFinal.getX() > balizaInicial.getX()) {
            largura = balizaFinal.getX() - balizaInicial.getX() + balizaFinal.getWidth();
            eixoX = balizaInicial.getX();
        } else {
            largura = balizaInicial.getX() - balizaFinal.getX() + balizaInicial.getWidth();
            eixoX = balizaFinal.getX();
        }

        eixoY = (balizaInicial.getY() + 5);

        //definindo imagem
       if  (desenhaImagemPilha() ) {
    	   this.setBounds( eixoX, eixoY, largura, alturaPilha);   
       }
   }

    public ControladorDSP getControladorDSP() {
        return controladorDSP;
    }

    public void setControladorDSP(ControladorDSP controladorDSP) {
        this.controladorDSP = controladorDSP;
    }

    public InterfaceDSP getInterfaceDSP() {
        return interfaceDSP;
    }

    public void setInterfaceDSP(InterfaceDSP interfaceDSP) {
        this.interfaceDSP = interfaceDSP;
    }

    public List<InterfaceBaliza> getListaDeBalizas() {
        return listaDeBalizas;
    }

    public void setListaDeBalizas(List<InterfaceBaliza> listaDeBalizas) {
        this.listaDeBalizas = listaDeBalizas;
    }

    public Pilha getPilhaVisualizada() {
        return pilhaVisualizada;
    }

    public void setPilhaVisualizada(Pilha pilhaVisualizada) {
        this.pilhaVisualizada = pilhaVisualizada;
    }

    public Boolean getPilhaSelecionada() {
        return pilhaSelecionada;
    }

    public void setPilhaSelecionada(Boolean pilhaSelecionada) {
        this.pilhaSelecionada = pilhaSelecionada;
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

    private Boolean desenhaImagemPilha()
    {
        //guarantindo que a lista de balizas estara ordenada antes de montar o desenho da pilha
    	Collections.sort(listaDeBalizas, new ComparadorInterfaceBaliza());
        List<InterfaceBaliza> itens = new ArrayList<InterfaceBaliza>();
        for (InterfaceBaliza baliza : listaDeBalizas) {
        	if (baliza.getBalizaVisualizada().getProduto() == null) {
        		itens.add(baliza)	;
        	}
        }
        
        listaDeBalizas.removeAll(itens);        
        Collections.sort(listaDeBalizas, new ComparadorInterfaceBaliza());
        if (listaDeBalizas.size() == 0 ) return Boolean.FALSE;
        InterfaceBaliza balizaInicial = new LinkedList<InterfaceBaliza>(listaDeBalizas).getFirst();
        InterfaceBaliza balizaFinal = new LinkedList<InterfaceBaliza>(listaDeBalizas).getLast();
        InterfaceBaliza baliza;
        
        
        
        //if (balizaInicial.getBalizaVisualizada().getProduto() != null) {
            String diretorioImagem = "tela.diretorio.imagens";
            BufferedImage imagemInicio = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem), "gradiente-pilha-inico.png");
            BufferedImage imagemFim = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem), "gradiente-pilha-fim.png");
            BufferedImage imagemMiolo = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem), "gradiente-pilha-miolo.png");

            TipoProduto tipoProdutoPilha = balizaInicial.getBalizaVisualizada().getProduto().getTipoProduto();
            String[] rgb = tipoProdutoPilha.getCorIdentificacao().split(",");

            //primeira imagem
            RepresentacaoGrafica grafico = new RepresentacaoGrafica(imagemInicio);
            grafico.setBounds(0, 0, balizaInicial.getWidth(), alturaPilha);
            grafico.setDimensaoImagem(grafico.getBounds());
            grafico.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
            grafico.setOpaque(true);

            this.add(grafico);

            
            
            //...imagem do miolo da pilha
            for (int i = 1; i < listaDeBalizas.size() - 1; i++) {
                baliza = listaDeBalizas.get(i);
                grafico = new RepresentacaoGrafica(imagemMiolo);
                grafico.setBounds(baliza.getWidth() * i, 0, baliza.getWidth(), alturaPilha);
                grafico.setDimensaoImagem(grafico.getBounds());
             //   if (baliza.getBalizaVisualizada().getProduto() != null && baliza.getBalizaVisualizada().getProduto().getTipoProduto() != null) {
                    rgb = baliza.getBalizaVisualizada().getProduto().getTipoProduto().getCorIdentificacao().split(",");
              //  }    
                grafico.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
                grafico.setOpaque(true);

                this.add(grafico);
            }

            //... imagem final da pilha
            grafico = new RepresentacaoGrafica(imagemFim);
            grafico.setBounds(balizaFinal.getWidth() * (listaDeBalizas.size() - 1), 0, balizaFinal.getWidth(), alturaPilha);
            grafico.setDimensaoImagem(grafico.getBounds());
            rgb = balizaFinal.getBalizaVisualizada().getProduto().getTipoProduto().getCorIdentificacao().split(",");
            grafico.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
            grafico.setOpaque(true);

            //finalmente adiciona esta representacaoGrafica com a imagem de pilha de cada baliza que a mesma ocupa
            this.add(grafico);
            return Boolean.TRUE;            
        //}
        //return Boolean.TRUE;
    }

   @Override
   public String toString()
   {
	return pilhaVisualizada.getNomePilha();
   }

   private void selecionaAsBalizasDaPilha(){
       ImageIcon icone = new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/selecionar_pilha.png");
       for(InterfaceBaliza iBaliza : listaDeBalizas){
            iBaliza.setSelecionada( Boolean.TRUE );
            iBaliza.setIcon(icone);
       }
       this.setPilhaSelecionada(true);
   }

   private void deselecionaAsbalizasDaPilha(){
       for(InterfaceBaliza iBaliza : listaDeBalizas){
           iBaliza.setIcon(null);
           iBaliza.setSelecionada(Boolean.FALSE);
       }
       this.setPilhaSelecionada(false);
   }
    
}
