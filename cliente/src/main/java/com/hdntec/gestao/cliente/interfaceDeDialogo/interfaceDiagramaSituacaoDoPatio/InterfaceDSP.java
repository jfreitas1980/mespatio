package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.util.List;

import javax.swing.JPanel;

import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;


/**
 * Interface gráfica que representa o DSP. Esta interface é uma agregação das seguintes interfaces:
 * <ul>
 * <li>interfaces de pátio;
 * <li>interfaces de correia;
 * <li>interfaces de usinas.
 * </ul>
 * 
 * @author andre
 * 
 */
public class InterfaceDSP {

    /** serial gerado */
    private static final long serialVersionUID = -4409073873490134410L;

    /** acesso às operações do subsistema de interface gráfica DSP */
    
    //TODO Darley: Obs. para gerar o relatorio do DSP é criado uma nova InterfaceDSP
    //que deve possuir um controlador unico para ela também. Por isso o controlador
    //não pode ser estatico.
    //private static ControladorDSP controladorDSP = null;
    private ControladorDSP controladorDSP = null;

    /** a interface gráfica principal */
    private InterfaceInicial interfaceInicial;

    /** a lista de interfaces gráficas de pátios */
    private List<InterfacePatio> listaDePatios;

    /** a lista de interfaces gráficas de correias */
    private List<InterfaceCorreia> listaCorreias;

    /** a lista de interfaces gráficas de correias */
    private List<InterfaceFiltragem> listaFiltragem;

    
    /** a lista de interfaces gráficas de usinas */
    private List<InterfaceUsina> listaUsinas;

    /** Painel inicial correspondente a interface DSP*/
    private JPanel componenteInterfaceDSP;

    /** Painel inicial que corresponde a interface de usinas */
    private JPanel componenteInterfaceUsina;

     /** a lista de itnerfaces graficas de anotações*/
    private List<InterfaceAnotacao> listaAnotacoes;

    public InterfaceDSP() {
        
    	if (controladorDSP == null) {
    		controladorDSP = new ControladorDSP();
    	}
        controladorDSP.setInterfaceDSP(this);
    }

    public ControladorDSP getControladorDSP() {
        return controladorDSP;
    }

    public void setControladorDSP(ControladorDSP controladorDSP) {
        this.controladorDSP = controladorDSP;
    }

    public InterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    public void setInterfaceInicial(InterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;
    }

    public List<InterfaceCorreia> getListaCorreias() {
        return listaCorreias;
    }

    public void setListaCorreias(List<InterfaceCorreia> listaCorreias) {
        this.listaCorreias = listaCorreias;
    }

    public List<InterfacePatio> getListaDePatios() {
        return listaDePatios;
    }

    public void setListaDePatios(List<InterfacePatio> listaDePatios) {
        this.listaDePatios = listaDePatios;
    }

    public List<InterfaceUsina> getListaUsinas() {
        return listaUsinas;
    }

    public void setListaUsinas(List<InterfaceUsina> listaUsinas) {
        this.listaUsinas = listaUsinas;
    }

    public JPanel getComponenteInterfaceDSP() {
        return componenteInterfaceDSP;
    }

    public void setComponenteInterfaceDSP(JPanel componenteInterfaceDSP) {
        this.componenteInterfaceDSP = componenteInterfaceDSP;
    }

    public JPanel getComponenteInterfaceUsina() {
        return componenteInterfaceUsina;
    }

    public void setComponenteInterfaceUsina(JPanel componenteInterfaceUsina) {
        this.componenteInterfaceUsina = componenteInterfaceUsina;
    }

    public List<InterfaceAnotacao> getListaAnotacoes() {
        return listaAnotacoes;
    }

    public void setListaAnotacoes(List<InterfaceAnotacao> listaAnotacoes) {
        this.listaAnotacoes = listaAnotacoes;
    }

    public List<InterfaceFiltragem> getListaFiltragem() {
        return listaFiltragem;
    }

    public void setListaFiltragem(List<InterfaceFiltragem> listaFiltragem) {
        this.listaFiltragem = listaFiltragem;
    }

    
}