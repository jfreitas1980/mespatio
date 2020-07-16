package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceSelecionavelParaAtividade;
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;


/**
 * Interface gráfica que representa a {@link CarregadoraDeNavios}.
 * 
 * @author andre
 * 
 */
public class InterfaceCarregadoraDeNavios extends RepresentacaoGrafica implements InterfaceSelecionavelParaAtividade
{

    /** serial gerado */
    private static final long serialVersionUID = 4904531550970883563L;

    /** a carregadora de navios visualizada */
    private CarregadoraDeNavios carregadoraDeNaviosVisualizada;

    /** a interface de píer ao qual esta carregadora de navios pertence */
    private InterfacePier pier;

    /** acesso às operações do subsistema de interface de fila de navios */
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;

    /**
     * Mostra a {@link CarregadoraDeNavios} na interface gráfica com o usuário.
     * 
     * @param carregadoraDeNavios
     *           a carregadora de navios a ser apresentada
     * @return <code>true</code> caso a operação tenha sucesso
     */
    public boolean mostrarCarregadoraDeNavios(CarregadoraDeNavios carregadoraDeNavios) {
        throw new UnsupportedOperationException();
    }

    public void atualizarApresentacao() {
        throw new UnsupportedOperationException();
    }

    public void deselecionar() {
        throw new UnsupportedOperationException();
    }

    public void selecionar() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the carregadoraDeNaviosVisualizada
     */
    public CarregadoraDeNavios getCarregadoraDeNaviosVisualizada() {
        return carregadoraDeNaviosVisualizada;
    }

    /**
     * @param carregadoraDeNaviosVisualizada
     *           the carregadoraDeNaviosVisualizada to set
     */
    public void setCarregadoraDeNaviosVisualizada(CarregadoraDeNavios carregadoraDeNaviosVisualizada) {
        this.carregadoraDeNaviosVisualizada = carregadoraDeNaviosVisualizada;
    }

    /**
     * @return the controladorInterfaceFilaDeNavios
     */
    public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios() {
        return controladorInterfaceFilaDeNavios;
    }

    /**
     * @param controladorInterfaceFilaDeNavios
     *           the controladorInterfaceFilaDeNavios to set
     */
    public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) {
        this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
    }

    /**
     * @return the pier
     */
    public InterfacePier getPier() {
        return pier;
    }

    /**
     * @param pier
     *           the pier to set
     */
    public void setPier(InterfacePier pier) {
        this.pier = pier;
    }

    public Boolean isSelecionada() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setSelecionada(Boolean selecionada) {
        // TODO Auto-generated method stub
    }

    public void atualiza() {
        // TODO Auto-generated method stub
    }
}
