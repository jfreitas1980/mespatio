package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JPanel;

import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.domain.planta.entity.status.Berco;


/**
 * Interface gráfica que representa o {@link Berco}.
 * 
 * @author andre
 * 
 */
public class InterfaceBerco extends JPanel implements InterfaceInicializacao {

    /** serial gerado */
    private static final long serialVersionUID = -2635756877350650989L;

    /** o berço visualizado */
    private Berco bercoVisualizada;

    /** acesso às operações do subsistema de interface gráfica de fila de navios */
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;

    /** o píer ao qual este berço pertence */
    private InterfacePier pier;

    /** o navio atendido por este berço */
    private InterfaceNavio navioAtendido;

    private JPanel painelInformacoesNavioAtracado;

    /** Constante que refere-se a quantidade de objetos da fila de navios. Nesta caso possui 3 : Navio1, Navio2 e Pier */
    private final int QTDE_OBJETOS_PIER = 2;

    public InterfaceBerco() {
        defineEventosParaInterfaceBerco();
    }

    /**
     * Metodo auxiliar que cria e define os eventos dos paineis principais da interface uinicial
     */
    private void defineEventosParaInterfaceBerco() {
        // Eventos acionado qdo algum componente for adicionado ao painel do DSP
        this.addContainerListener(new java.awt.event.ContainerAdapter() {

            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                ((InterfaceInicializacao) evt.getChild()).inicializaInterface();
            }
        });
    }

    public Berco getBercoVisualizada() {
        return bercoVisualizada;
    }

    public void setBercoVisualizada(Berco bercoVisualizada) {
        this.bercoVisualizada = bercoVisualizada;
    }

    public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios() {
        return controladorInterfaceFilaDeNavios;
    }

    public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) {
        this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
    }

    public InterfaceNavio getNavioAtendido() {
        return navioAtendido;
    }

    public void setNavioAtendido(InterfaceNavio navioAtendido) {
        this.navioAtendido = navioAtendido;
    }

    public InterfacePier getPier() {
        return pier;
    }

    public void setPier(InterfacePier pier) {
        this.pier = pier;
    }

    @Override
    public void inicializaInterface() {
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setToolTipText(bercoVisualizada.getNomeBerco());
        calculaPosicaoBerco();
        desenhaNavioNoBerco();
    }

    private void desenhaNavioNoBerco() {
        if (bercoVisualizada.getNavioAtracado() != null) {
            navioAtendido = new InterfaceNavio(pier.getHoraSituacao());
            navioAtendido.setBercoDeAtracacao(this);
            navioAtendido.setComponenteInterfaceFilaNavios(pier.getPnlInterfacePier());
            navioAtendido.setNavioVisualizado(bercoVisualizada.getNavioAtracado());
            navioAtendido.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaDeNavios);
            this.add(navioAtendido);

            InformacoesNavioAtracadoPnl pnlInfoNavioAtracado = new InformacoesNavioAtracadoPnl(bercoVisualizada.getNavioAtracado(),pier.getHoraSituacao());
            pnlInfoNavioAtracado.atualizaInformacoesNavioAtracado();
            painelInformacoesNavioAtracado.add(pnlInfoNavioAtracado);
        }
    }

    @Override
    public void defineDimensoesFixas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void calculaPosicaoBerco() {

        Rectangle rectParent = pier.getPnlInterfacePier().getBounds();
        int qtdeComponentesInseridos = pier.getPnlInterfacePier().getComponentCount() - 2;
        int larguraComponenteBerco = 0;
        int posicaoX = 0;

        //calcula largura do componente berco
        if (qtdeComponentesInseridos == 0) {
            larguraComponenteBerco = (int) (rectParent.getBounds().getWidth() / QTDE_OBJETOS_PIER) - (pier.getVariacaoLarguraDesenhoPier());
        } else {
            larguraComponenteBerco = (int) (rectParent.getBounds().getWidth() / QTDE_OBJETOS_PIER) - (pier.getVariacaoLarguraDesenhoPier());
            posicaoX = ((larguraComponenteBerco + pier.getWidth() - pier.getVariacaoLarguraDesenhoPier()) * qtdeComponentesInseridos);
        }
    
        int posicaoY = (int) ((rectParent.getHeight() / 2)  - 70 ) ;
//        int posicaoY = 0;

        this.setBounds(posicaoX, posicaoY, larguraComponenteBerco, pier.getAlturaDesenhoPier() /*- 30*/);

    }

    public JPanel getPainelInformacoesNavioAtracado() {
        return painelInformacoesNavioAtracado;
    }

    public void setPainelInformacoesNavioAtracado(JPanel painelInformacoesNavioAtracado) {
        this.painelInformacoesNavioAtracado = painelInformacoesNavioAtracado;
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

}
