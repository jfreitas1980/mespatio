package com.hdntec.gestao.cliente.interfaceProgramacao;

/**
 * Interface criada para a inicializacao dos objetos que sao adicionados aos paineis
 * do desenho da planta
 * 
 * @author hdn.luchetta
 */
public interface InterfaceInicializacao {

    public void inicializaInterface();

    public void defineDimensoesFixas();

    /**
     * Desabilita os menus do objeto caso existam para que os mesmos não possam
     * serem utilizandos quando o sistema estiver executando uma rotira de processamento
     */
    public void desabilitarMenus();

    /**
     * Habilita os menus do objeto caso existam para que os mesmos possam
     * serem utilizandos quando o sistema estiver concluído a execução uma rotira de processamento
     */
    public void habilitarMenus();

}
