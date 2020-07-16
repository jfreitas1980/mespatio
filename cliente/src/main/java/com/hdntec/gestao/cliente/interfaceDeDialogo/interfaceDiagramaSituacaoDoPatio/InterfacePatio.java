package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores.ComparadorInterfaceBaliza;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gráfica que representa o {@link Patio}.
 *
 * @author andre
 *
 */
public class InterfacePatio extends JPanel implements InterfaceInicializacao {

    
	/** serial gerado */
    private static final long serialVersionUID = 2608398755652208034L;

    private Date horaSituacao;
    /** o {@link Patio} visualizado na interface gráfica */
    private Patio patioVisualizado;

    /** acesso às operações do subsistema de interface gráfica DSP */
    private ControladorDSP controladorDSP;

    /** a interface gráfica DSP */
    private InterfaceDSP interfaceDSP;

    /** a lista de interfaces gráficas de balizas que pertencem a este pátio */
    private List<InterfaceBaliza> listaDeBalizas;

    /** a lista de pilhas que exite no patio */
    private List<InterfacePilha> listaDePilhas;

    /** a lista de interfaces gráficas de máquinas que pertencem a este pátio*/
    private List<InterfaceMaquinaDoPatio> listaDeMaquinas;
    
    private int eixoXPatio, comprimentoCorreiaPatio, tamanhoBorda;

    private HashMap<Integer, InterfaceBaliza> mapaInterfaceBaliza;

      /**controla a ação de seleção da baliza com arraste do mouse */
    private ControladorEventoMouseBaliza controladorEventoMouseBaliza;

    /**
     * Contrutor da interface de patio
     */
    public InterfacePatio(Date horaSituacao) {
        super();
        this.horaSituacao = horaSituacao;
        controladorEventoMouseBaliza = new ControladorEventoMouseBaliza();
        defineEventosParaPatio();
        // seta a cor do desenho patio
        this.setBackground(new Color(255, 255, 255));
    }

    /**
     * Metodo que defene algumas propriedade de inicializacao da interface de patio
     */
    private void definePropriedadesInterfacePatio() {
        // seta o desenho da borda do patio
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        // seta o layout do desenho do patio
        this.setLayout(null);
        this.setOpaque(false);
    }

    /**
     * Metodo que inicializa a interface quando algum objeto a adiciona em seu conteiner
     */
    @Override
    public void inicializaInterface() {
        definePropriedadesInterfacePatio();
        defineDimensoesFixas();
        calculaPosicaoPatio();
        desenhaBalizasNoPatio();
        desenhaNumeroBalizas();
        desenhaPilhasNoPatio();
        desenhaMaquinaNoPatio();
        desenhaIdentidadePatio();
    }

    /**
     * Metodo que define algumas dimensoes fixas para desenho do patio
     * Essas dimensoes estao no properties do projeto
     */
    @Override
    public void defineDimensoesFixas() {
        eixoXPatio = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.patio.eixo.x").trim());
        comprimentoCorreiaPatio = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.patio.comprimento.correia").trim());
        tamanhoBorda = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.patio.tamanho.borda").trim());
    }

    /**
     * Metodo que desenha as maquinas existentes no patio
     */
    private void desenhaMaquinaNoPatio() {
        listaDeMaquinas = montaListaMaquinas();
        for (InterfaceMaquinaDoPatio im : listaDeMaquinas) {
        		this.add(im); 
        }
    }

    /**
     * Metodo que desenha as balizas existentes no patio
     */
    private void desenhaBalizasNoPatio() {
        if (listaDeBalizas == null)
        {
           montaListaInterfacesBaliza();
        }
        for (InterfaceBaliza itb : listaDeBalizas) {
            this.add(itb);
        }
        desenhaBordaFimBaliza();
    }

    /**
     * Metodo que desenha as pilhas existentes no patio
     */
    private void desenhaPilhasNoPatio() {
        atualizaListaDePilhasDoPatio();
        for (InterfacePilha itp : listaDePilhas) {
            this.add(itp);
        }

        desenhaNomePilhasDoPatio();
    }

    /**
     * Metodo auxiliar que atualiza a lista de pilhas do patio da seguinte forma:
     *   - Para cada pilha o metodo percorre a lista de balizas buscando a posicao da mesma no patio
     *     adicionando a baliza do patio na lista de balizas da pilha
     */
    private void atualizaListaDePilhasDoPatio() {

        // percorre a lista de pilhas do patio
        for (InterfacePilha interfacePilha : listaDePilhas) {
            List<InterfaceBaliza> listaInterfaceBalizaDaPilha = new ArrayList<InterfaceBaliza>();
            // percorre a lista de balizas da pilha
            for (Baliza balizaDaPilha : interfacePilha.getPilhaVisualizada().getListaDeBalizas()) {
                // percorre a lista de balizas do patio
                for (InterfaceBaliza interfaceBalizaDoPatio : listaDeBalizas) {
                    Baliza balizaDoPatio = interfaceBalizaDoPatio.getBalizaVisualizada();
                    // verifica se a baliza do patio eh igual a baliza da pilha
                    if (balizaDoPatio.getMetaBaliza().equals(balizaDaPilha.getMetaBaliza())) {
                        listaInterfaceBalizaDaPilha.add(interfaceBalizaDoPatio);
                    }
                }
            }
            Collections.sort(listaInterfaceBalizaDaPilha, new ComparadorInterfaceBaliza());
            interfacePilha.setListaDeBalizas(listaInterfaceBalizaDaPilha);
            interfacePilha.setControladorDSP(controladorDSP);
        }
    }

    /**
     * Metodo que adiciona alguns eventos necessarios para utilizacao da interface de patio
     */
    private void defineEventosParaPatio() {

        this.addContainerListener(new java.awt.event.ContainerAdapter() {
            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                ((InterfaceInicializacao) evt.getChild()).inicializaInterface();
            }
        });
    }

    /**
     * Metodo que calcula a posicao em que o patio irá ser desenhado na tela
     */
    private void calculaPosicaoPatio() {
        Rectangle rectParent = interfaceDSP.getComponenteInterfaceDSP().getBounds();
        int eixoX = 0, eixoY = 0, comprimento = 0, largura = 0, comprimentoPatioReal = 0;
        int qtdePatiosInseridos = 0;

        qtdePatiosInseridos = (interfaceDSP.getComponenteInterfaceDSP().getComponentCount() - 1);
        comprimentoPatioReal = (int) (rectParent.getHeight() / interfaceDSP.getListaDePatios().size());

        // subitrai-se 1 da lista pois a qtde de patios inseridos inicia-se em zero
        if (qtdePatiosInseridos == interfaceDSP.getListaDePatios().size() - 1) {
            // multiplica-se o comprimento da correia por 2 pois o ultimo patio tem 2 correias entre eles
            comprimento = comprimentoPatioReal - (comprimentoCorreiaPatio * 2) - tamanhoBorda;
        } else {
            comprimento = comprimentoPatioReal - comprimentoCorreiaPatio - tamanhoBorda;
        }

        largura = (int) rectParent.getWidth();
        eixoX = eixoXPatio;
        eixoY = ((qtdePatiosInseridos * comprimentoPatioReal) + tamanhoBorda + comprimentoCorreiaPatio);

        this.setBounds(eixoX, eixoY, largura, comprimento);
    }

    /**
     * Metodo auxliar que monta uma lista de interfaces de balizas de acordo
     * com a lista de balizas do patio.
     * @return
     */
    public void montaListaInterfacesBaliza() {

        listaDeBalizas = new ArrayList<InterfaceBaliza>();
        mapaInterfaceBaliza = null;
        mapaInterfaceBaliza = new HashMap<Integer, InterfaceBaliza>();
        InterfaceBaliza interfaceBaliza;

        Collections.sort(patioVisualizado.getListaDeBalizas(this.horaSituacao), Baliza.comparadorBaliza);

        for (Baliza baliza : patioVisualizado.getListaDeBalizas(this.horaSituacao)) {
            interfaceBaliza = new InterfaceBaliza();

            interfaceBaliza.setBalizaVisualizada(baliza);
            interfaceBaliza.setControladorDSP(controladorDSP);
            interfaceBaliza.setInterfacePatio(this);
            interfaceBaliza.setControladorEventoMouseBaliza(controladorEventoMouseBaliza);

            listaDeBalizas.add(interfaceBaliza);
            mapaInterfaceBaliza.put(baliza.getNumero(), interfaceBaliza);
        }
    }

    /**
     * Metodo auxiliar que monta uma lista de interfaces de maquinas
     *
     * @return
     */
    private List<InterfaceMaquinaDoPatio> montaListaMaquinas() {
        List<InterfaceMaquinaDoPatio> listaMaquinas = new ArrayList<InterfaceMaquinaDoPatio>();
        InterfaceMaquinaDoPatio interfaceMaquina;
        for (MaquinaDoPatio maquina : patioVisualizado.getListaDeMaquinasDoPatio(this.getHoraSituacao())) {
            interfaceMaquina = new InterfaceMaquinaDoPatio();
            interfaceMaquina.setPatio(this);
            interfaceMaquina.setMaquinaDoPatioVisualizada(maquina);
            interfaceMaquina.setControladorDSP(controladorDSP);
            interfaceMaquina.setPosicao(buscaBalizaDaPaCarregadeia(maquina.getPosicao(), listaDeBalizas));
            listaMaquinas.add(interfaceMaquina);
        }
        return listaMaquinas;
    }

    /**
     * sub metodo utilizado pelo metodo montaInterfacePaCarregadeira, para encontrar a interfaceBaliza
     * pertencente a este objeto
     * @return
     */
    private InterfaceBaliza buscaBalizaDaPaCarregadeia(Baliza baliza, List<InterfaceBaliza> listaInterfaceBalizas) {

        for (InterfaceBaliza interfaceBaliza : listaInterfaceBalizas) {
            if (interfaceBaliza.getBalizaVisualizada().getMetaBaliza().equals(baliza.getMetaBaliza())) {
                return interfaceBaliza;
            }
        }
        return null;
    }

    public boolean mostrarPatio(Patio patio) {
        throw new UnsupportedOperationException();
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

    public Patio getPatioVisualizado() {
        return patioVisualizado;
    }

    public void setPatioVisualizado(Patio patioVisualizado) {
        this.patioVisualizado = patioVisualizado;
    }

    public List<InterfacePilha> getListaDePilhas() {
        return listaDePilhas;
    }

    public void setListaDePilhas(List<InterfacePilha> listaDePilhas) {
        this.listaDePilhas = listaDePilhas;
    }

    /**
     * @return the listaDeMaquinas
     */
    public List<InterfaceMaquinaDoPatio> getListaDeMaquinas() {
        return listaDeMaquinas;
    }

    /**
     * @param listaDeMaquinas the listaDeMaquinas to set
     */
    public void setListaDeMaquinas(List<InterfaceMaquinaDoPatio> listaDeMaquinas) {
        this.listaDeMaquinas = listaDeMaquinas;
    }

    public ControladorEventoMouseBaliza getControladorEventoMouseBaliza() {
        return controladorEventoMouseBaliza;
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

   /**
    * Insere uma anotação no pátio
    */
   public void inserirAnotacao(Dimension coordenadasDeInsercaoDaAnotacao)
   {
      controladorDSP.inserirAnotacao(coordenadasDeInsercaoDaAnotacao, this);
   }

   /**
    * insere o efeito de borda no final da baliza (embaixo), para que a pilha desenhada fica entre as bordas
    */
   private void desenhaBordaFimBaliza()
   {
       InterfaceFinalBaliza label;
       for(InterfaceBaliza baliza : listaDeBalizas){
           label = new InterfaceFinalBaliza(baliza);
           this.add(label);
       }
   }

   /**
    * desenha o nome das pilhas do patio
    */
   private void desenhaNomePilhasDoPatio(){
       InterfaceNomeDaPilha nomePilha;
       for(InterfacePilha iPilha : listaDePilhas){
           nomePilha = new InterfaceNomeDaPilha(iPilha);
           this.add(nomePilha);
       }
   }

   @Override
   public String toString()
   {
       return getPatioVisualizado().getNomePatio();
   }

   private void desenhaIdentidadePatio(){
     InterfaceIdentificadorPatio label = new InterfaceIdentificadorPatio(this);
     this.add(label);
   }

   private void desenhaNumeroBalizas(){
        InterfaceIdentificadorBaliza label;
       for(InterfaceBaliza baliza : listaDeBalizas) {
           if ((baliza.getBalizaVisualizada().getNumero() % 10) == 0) {
               label = new InterfaceIdentificadorBaliza(baliza);
               this.add(label);
           }
       }
   }

   public HashMap<Integer, InterfaceBaliza> getMapaInterfaceBaliza()
   {
      return mapaInterfaceBaliza;
   }

   public void setMapaInterfaceBaliza(HashMap<Integer, InterfaceBaliza> mapaInterfaceBaliza)
   {
      this.mapaInterfaceBaliza = mapaInterfaceBaliza;
   }

   public InterfaceBaliza obterBalizaPeloNumero(Integer numeroBaliza)
   {
      return getMapaInterfaceBaliza().get(numeroBaliza);
   }

/**
 * Get horaSituacao
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @return Returns the Date.
 * @since 24/06/2010
 */
public Date getHoraSituacao() {
    return horaSituacao;
}

/**
 * Change horaSituacao	
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @param horaSituacao 
 * @since 24/06/2010
 */
public void setHoraSituacao(Date horaSituacao) {
    this.horaSituacao = horaSituacao;
}

}
