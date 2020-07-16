package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.navios.comparadores.ComparadorNavio;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * Interface gráfica que representa a {@link FilaDeNavios}.
 * 
 * @author andre
 * 
 */
public class InterfaceFilaDeNavios extends JPanel {

	/** acesso às operações do subsistema de interface gráfica de fila de navios */
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;

	/** serial gerado */
    private static final long serialVersionUID = 5226859927447562502L;

    /** a {@link FilaDeNavios} visualizada */
    private FilaDeNavios filaDeNaviosVisualizada;

    /** a interface principal do sistema */
    private InterfaceInicial interfaceInicial;

    /** a lista de interfaces de pier */
    private List<InterfacePier> listaDePiers;

    /** O painel que corresponde a fila de navios que sera desenhada */
    private JTabbedPane componenteInterfaceFilaNavios;

    /** O painel que corresponde a fila de navios que sera desenhada */
    private JPanel componenteInterfacePier;

    /** A lista de interface navios da fila de navios */
    private List<NavioNaFilaPnl> listaInterfaceNavio;

    /** Determina se a fila de navios foi editada para necessidade de consolidacao */
    private Boolean filaDeNaviosEditada;

    private JPopupMenu popMenuNavio;
    private JMenuItem mnuFilaDeNavio;
    private InterfaceMensagem interfaceMensagem;
    private Date horaSituacao;

    public InterfaceFilaDeNavios(Date horaSituacao)
    {
        this.horaSituacao = horaSituacao;
        instanciaMenus();
        //this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
        filaDeNaviosEditada = Boolean.FALSE;
        
        desabilitaMenusPermissaoUsuario();
    }

    private void instanciaMenus(){
        popMenuNavio = new JPopupMenu();
        mnuFilaDeNavio = new JMenuItem();
        mnuFilaDeNavio.setText(PropertiesUtil.getMessage("mensagem.inserir.navio.filanavio"));
        mnuFilaDeNavio.setToolTipText("");
        popMenuNavio.add(mnuFilaDeNavio);
    }

    /**
     * Metodo que instancia a acao de popMenu para inserir um navio na fila
     * Este metodo so pode ser chamado apos instanciar o controladorFilaDeNavios e a filaDeNaviosVisualisada desta classe
     */
    public void defineAcoesParaFila()
    {
        //retorna o component da interface inicial
        this.getInterfaceInicial().getTbFilaDeNavios().setComponentPopupMenu(popMenuNavio);
        if(filaDeNaviosVisualizada.getListaDeNaviosNaFila().isEmpty())
        {//se a fila de navios estiver vazia apresentar este componente de menu
            this.getInterfaceInicial().getTbFilaDeNavios().getComponentPopupMenu().getComponents()[0].setVisible(true);
        }else{//..caso contrario não apresentar, pois no panel que contem algum navio da fila tambem tem este component
            this.getInterfaceInicial().getTbFilaDeNavios().getComponentPopupMenu().getComponents()[0].setVisible(false);
        }

        mnuFilaDeNavio.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
               try {
                    //muda o estado interno do sistema para o MODO_EDICAO
                    controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getControladorInterfaceInicial().ativaModoEdicao();
                    //chama metodo que apresenta a tela de edicao dos dados do Navio
                    controladorInterfaceFilaDeNavios.inserirNavioFilaDeNavios(null);
                } catch (OperacaoCanceladaPeloUsuarioException ex) {
                	//controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    //controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
                } 
            }
        });
    }

  
    public FilaDeNavios getFilaDeNaviosVisualizada() {
        return filaDeNaviosVisualizada;
    }

    public void setFilaDeNaviosVisualizada(FilaDeNavios filaDeNaviosVisualizada) {
        this.filaDeNaviosVisualizada = filaDeNaviosVisualizada;
    }

    public InterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    public void setInterfaceInicial(InterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;
    }

    public List<InterfacePier> getListaDePiers() {
        return listaDePiers;
    }

    public void setListaDePiers(List<InterfacePier> listaDePiers) {
        this.listaDePiers = listaDePiers;
    }

    public JTabbedPane getComponenteInterfaceFilaNavios() {
        return componenteInterfaceFilaNavios;
    }

    public void setComponenteInterfaceFilaNavios(JTabbedPane tbPaneFilaDeNavios) {
        this.componenteInterfaceFilaNavios = tbPaneFilaDeNavios;
    }

    public JPanel getComponenteInterfacePier() {
        return componenteInterfacePier;
    }

    public void setComponenteInterfacePier(JPanel componenteInterfacePier) {
        this.componenteInterfacePier = componenteInterfacePier;
    }

    public List<NavioNaFilaPnl> getListaInterfaceNavio() {
        if (listaInterfaceNavio == null) {
            listaInterfaceNavio = new ArrayList<NavioNaFilaPnl>();
        }
        return listaInterfaceNavio;
    }

    public void setListaInterfaceNavio(List<NavioNaFilaPnl> listaInterfaceNavio) {
        this.listaInterfaceNavio = listaInterfaceNavio;
    }

    public void desenhaFilaDeNavios() {
        componenteInterfaceFilaNavios.removeAll();
        List<Navio> filaNavio = filaDeNaviosVisualizada.getListaDeNaviosNaFila();
        Collections.sort(filaNavio, new ComparadorNavio());
        for (Navio navio : filaNavio) {
            InterfaceNavio interfaceNavio = new InterfaceNavio(getHoraSituacao());
            interfaceNavio.setNavioVisualizado(navio);
            interfaceNavio.setComponenteInterfaceFilaNavios(this);
            controladorInterfaceFilaDeNavios.getListaInterfacesNavios().add(interfaceNavio);
            interfaceNavio.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaDeNavios);
            NavioNaFilaPnl navioNaFila = new NavioNaFilaPnl(interfaceNavio);
            componenteInterfaceFilaNavios.addTab(navio.getNomeNavio() + " - " + DSSStockyardTimeUtil.formatarData(navio.getEta(), "dd/MM/yyyy"), navioNaFila);
            this.getListaInterfaceNavio().add(navioNaFila);
        }
    }
    
    /**
     * Metodo que desabilita os menus existentes neste objeto em caso de processamento
     */
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
    public void habilitarMenus() {
        if (this.getComponentPopupMenu() != null) {
            for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                this.getComponentPopupMenu().getComponent(i).setEnabled(true);
            }
        }
    }

   public Boolean getFilaDeNaviosEditada()
   {
      return filaDeNaviosEditada;
   }

   public void setFilaDeNaviosEditada(Boolean filaDeNaviosEditada)
   {
      this.filaDeNaviosEditada = filaDeNaviosEditada;
   }

   private void desabilitaMenusPermissaoUsuario(){
      /* if(controladorInterfaceFilaDeNavios.getInterfaceInicial().verificaPermissaoAtualizacaoProducao()){
           *///mnuFilaDeNavio.setEnabled(false);
       //}
   }

   public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios() {
       return controladorInterfaceFilaDeNavios;
   }

   public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) {
       this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
   }

public Date getHoraSituacao() {
    return horaSituacao;
}

public void setHoraSituacao(Date horaSituacao) {
    this.horaSituacao = horaSituacao;
}

}
