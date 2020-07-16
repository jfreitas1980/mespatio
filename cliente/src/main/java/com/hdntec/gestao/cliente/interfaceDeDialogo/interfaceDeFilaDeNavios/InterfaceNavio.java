package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gráfica que representa o {@link Navio}.
 * 
 * @author andre
 * 
 */
public class InterfaceNavio extends RepresentacaoGrafica implements InterfaceInicializacao {

    /** serial gerado */
    private static final long serialVersionUID = 7015299626540323108L;

    private Date horaSituacao;
    
    /** o navio visualizado nesta interface */
    private Navio navioVisualizado;

    /** a interface gráfico do berço onde este navio atraca */
    private InterfaceBerco bercoDeAtracacao;

    /** acesso às operações do subsistema de interface gráfica de fila de navios */
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;

    /** a lista das interfaces das carga deste navio */
    private List<InterfaceCarga> listaDecarga;

    /** Painel inicia que corresponde a interface fila de navios */
    private JPanel componenteInterfaceFilaNavios;

    /** a interface de mensagem disparada para aplicacao */
    private InterfaceMensagem interfaceMensagem;

    /** identifica se o desenha do navio sera na fila ou no pier */
    private Boolean desenharNavioFila;


    public InterfaceNavio(Date horaSituacao) {
        super("navio.png");
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.desenharNavioFila = Boolean.FALSE;
        this.horaSituacao = horaSituacao;
        defineEventosParaNavio();
    }

    private void defineEventosParaNavio() {
        this.addContainerListener(new java.awt.event.ContainerAdapter() {
            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                ((InterfaceInicializacao) evt.getChild()).inicializaInterface();
            }
        });
    }

    private void criarPopMenuParaNavio() {
        
        
        JPopupMenu popMnuNavio = new JPopupMenu();


        JMenuItem mnuAtualizarCRM = new JMenuItem();
        mnuAtualizarCRM.setText("Atualizar Dados CRM");
        mnuAtualizarCRM.setToolTipText("");

        mnuAtualizarCRM.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atualizarNavioCRMActionPerformed(evt);
            }
        });
        popMnuNavio.add(mnuAtualizarCRM);
          
        //menu para editar navio
        JMenuItem mnuEditarMavio = new JMenuItem();
        mnuEditarMavio.setText(PropertiesUtil.getMessage("mensagem.menu.interface.navio.editar"));
        mnuEditarMavio.setToolTipText("");
        mnuEditarMavio.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarNavioActionPerformed(evt);
            }
        });
        popMnuNavio.add(mnuEditarMavio);

        //menu para movimentar o navio (atividade desatracar ou retornar navio a fila)
        JMenuItem mnuMovimentarNavio = new JMenuItem();
        mnuMovimentarNavio.setText(PropertiesUtil.getMessage("menu.movimentar.navio"));
        mnuMovimentarNavio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try{
                InterfaceMovimentarNavio interfaceMovimentarNavio = new InterfaceMovimentarNavio(null, true, InterfaceNavio.this);
                interfaceMovimentarNavio.setVisible(true);

                if(interfaceMovimentarNavio.getOperacaoCanceladaPeloUsuario()){
                        throw new OperacaoCanceladaPeloUsuarioException();
                }
                //verificar necessidade de atualizar DSP
                getControladorInterfaceFilaDeNavios().getInterfaceInicial().getInterfaceInicial().atualizarDSP();
                getControladorInterfaceFilaDeNavios().getInterfaceInicial().exibirUltimaSituacaoPatio();

                }catch (OperacaoCanceladaPeloUsuarioException ex) {
                	getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    InterfaceNavio.this.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                }

            }
        });
        popMnuNavio.add(mnuMovimentarNavio);

        this.setComponentPopupMenu(popMnuNavio);

    }

    private void editarNavioActionPerformed(ActionEvent evt) {
        try {
            InterfaceNavio navioClicado = obterNavioClicado(evt);
            if (navioClicado.getNavioVisualizado().equals(getNavioVisualizado())) {
                //chama metodo que apresenta a tela de edicao dos dados do Navio
                controladorInterfaceFilaDeNavios.editarFilaDeNavios(this, InterfaceEditaNavioNaFilaDeNavios.MODO_EDITAR_ATRACADO);
            }
        } catch (OperacaoCanceladaPeloUsuarioException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        }
    }

    private void atualizarNavioCRMActionPerformed(ActionEvent evt) {
        InterfaceNavio navioClicado = null;
        try {            
             navioClicado = obterNavioClicado(evt);
                if (navioClicado.getNavioVisualizado().equals(getNavioVisualizado())) {
                        //verifica se a filaDeNavios pode ser edita... ou se houve alguma alteracao na situacaoDePatio
                        navioClicado.getControladorInterfaceFilaDeNavios().verificarModoDeEdicao();
                        //muda o estado interno do sistema para o MODO_EDICAO
                        navioClicado.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().getControladorInterfaceInicial().ativaModoEdicao();
                        //chama metodo que apresenta a tela de edicao dos dados do Navio
                        navioClicado.getControladorInterfaceFilaDeNavios().atualizarNavioFilaDeNavios(navioClicado);
                } 
            } catch (OperacaoCanceladaPeloUsuarioException ex) {
                    navioClicado.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    navioClicado.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                } catch (ModoDeEdicaoException mdex) {
                    navioClicado.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
                    navioClicado.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                } catch (ErroSistemicoException e) {
                    // TODO Auto-generated catch block
                    navioClicado.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(e.getMessage());
                    navioClicado.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                    e.printStackTrace();
                }        
    }
    
    private InterfaceNavio obterNavioClicado(ActionEvent evt) {
        JMenuItem mnuItem = (JMenuItem) evt.getSource();
        JPopupMenu popupMenu = (JPopupMenu) mnuItem.getParent();
        return (InterfaceNavio) popupMenu.getInvoker();
    }

    private void calculaPosicaoNavio() {
        if (!this.desenharNavioFila) {
            //desenhando navio na horizontal, atracado no pier
            int posicaoX = (bercoDeAtracacao.getPier().getVariacaoLarguraDesenhoPier() / 2) + 15 ;
            int posicaoY = (bercoDeAtracacao.getPier().getVariacaoLarguraDesenhoPier() * 5 );
            int largura = (int) (bercoDeAtracacao.getBounds().getWidth() - (bercoDeAtracacao.getPier().getVariacaoLarguraDesenhoPier() * 2));
            int altura = (int) (bercoDeAtracacao.getBounds().getHeight() - bercoDeAtracacao.getPier().getVariacaoLarguraDesenhoPier());

            this.setDimensaoImagem(posicaoX, posicaoY, largura - 15, this.getImagemDSP().getHeight());
            this.setBounds(posicaoX, posicaoY,  largura - 15, this.getImagemDSP().getHeight());
        } else {
            //desenha navio na horizontal na fila da navios
            Dimension dimensaoImagem = componenteInterfaceFilaNavios.getPreferredSize();
            //este calculo esta apenas para ajustar o tamanho do navio dentro do componete (jpanel) da fila de navios para ficar com as dimensoes (largura=307, altura=85)
            this.setDimensaoImagem((int) componenteInterfaceFilaNavios.getAlignmentX(), (int) componenteInterfaceFilaNavios.getAlignmentY(), (dimensaoImagem.width - 116), dimensaoImagem.height - 8 );
            this.setBounds((int) componenteInterfaceFilaNavios.getAlignmentX(), (int) componenteInterfaceFilaNavios.getAlignmentY(), (dimensaoImagem.width - 116), dimensaoImagem.height - 7 );
        }
    }

    private void desenhaCargasDoNavio() {
        this.removeAll();
        listaDecarga = new ArrayList<InterfaceCarga>();
    
        for (Carga carga : navioVisualizado.getListaDeCargasDoNavio(horaSituacao)) {

            InterfaceCarga interfaceCarga = new InterfaceCarga();

            interfaceCarga.setCargaVisualizada(carga);
            interfaceCarga.setNavio(this);
            interfaceCarga.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaDeNavios);
            interfaceCarga.setDesenharCargaNavioFila(desenharNavioFila);

            listaDecarga.add(interfaceCarga);

        }

        for (InterfaceCarga interfaceCarga : listaDecarga) {
            this.add(interfaceCarga);
        }
    }

    @Override
    public void inicializaInterface() {
        calculaPosicaoNavio();
        desenhaCargasDoNavio();
        if (navioVisualizado.getBercoDeAtracacao() != null) {
            criarPopMenuParaNavio();
            desabilitaMenusPermissaoUsuario();
        }
    }

    @Override
    public void defineDimensoesFixas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InterfaceBerco getBercoDeAtracacao() {
        return bercoDeAtracacao;
    }

    public void setBercoDeAtracacao(InterfaceBerco bercoDeAtracacao) {
        this.bercoDeAtracacao = bercoDeAtracacao;
    }

    public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios() {
        return controladorInterfaceFilaDeNavios;
    }

    public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) {
        this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
    }

    public List<InterfaceCarga> getListaDecarga() {
        return listaDecarga;
    }

    public void setListaDecarga(List<InterfaceCarga> listaDecarga) {
        this.listaDecarga = listaDecarga;
    }

    public Navio getNavioVisualizado() {
        return navioVisualizado;
    }

    public void setNavioVisualizado(Navio navioVisualizado) {
        this.navioVisualizado = navioVisualizado;
    }

    public JPanel getComponenteInterfaceFilaNavios() {
        return componenteInterfaceFilaNavios;
    }

    public void setComponenteInterfaceFilaNavios(JPanel componenteInterfaceFilaNavios) {
        this.componenteInterfaceFilaNavios = componenteInterfaceFilaNavios;
    }

    public void setDesenharNavioFila(Boolean desenharNavioFila) {
        this.desenharNavioFila = desenharNavioFila;
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

   @Override
   public String toString()
   {
      return navioVisualizado.getNomeNavio();
   }

   private void desabilitaMenusPermissaoUsuario()
   {
       if (controladorInterfaceFilaDeNavios.getInterfaceInicial().verificaPermissaoAtualizacaoProducao())
       {
           if (this.getComponentPopupMenu() != null)
           {
               for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                   this.getComponentPopupMenu().getComponent(i).setEnabled(false);
               }
           }
       }
   }

public Date getHoraSituacao() {
    return horaSituacao;
}

public void setHoraSituacao(Date horaSituacao) {
    this.horaSituacao = horaSituacao;
}

}
