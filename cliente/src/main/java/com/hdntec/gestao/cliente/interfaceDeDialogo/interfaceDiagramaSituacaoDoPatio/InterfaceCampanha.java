package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceSelecionavelParaAtividade;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;


/**
 * Interface gráfica que representa a {@link Campanha}.
 * 
 * @author andre
 * 
 */
public class InterfaceCampanha extends RepresentacaoGrafica implements InterfaceSelecionavelParaAtividade{

    /** serial gerado */
    private static final long serialVersionUID = 2693061474352777391L;

    /** a campanha visualizada nesta interface */
    private Campanha campanhaVisualizada;

    /** acesso às operações do subsistema de interface gráfica DSP */
    private ControladorDSP controladorDSP;

    /** interface gráfica da {@link Usina} a qual esta campanha pertence */
    private InterfaceUsina interfaceUsina;

    /**
     * Mostra a {@link Campanha} na interface gráfica.
     * 
     * @param campanha
     *           a campanha a ser visualizada
     * @return <code>true</code> se a operação tiver sucesso
     */
    public boolean mostrarCampanha(Campanha campanha) {
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
     * @return the campanhaVisualizada
     */
    public Campanha getCampanhaVisualizada() {
        return campanhaVisualizada;
    }

    /**
     * @param campanhaVisualizada
     *           the campanhaVisualizada to set
     */
    public void setCampanhaVisualizada(Campanha campanhaVisualizada) {
        this.campanhaVisualizada = campanhaVisualizada;
    }

    /**
     * @return the controladorDSP
     */
    public ControladorDSP getControladorDSP() {
        return controladorDSP;
    }

    /**
     * @param controladorDSP
     *           the controladorDSP to set
     */
    public void setControladorDSP(ControladorDSP controladorDSP) {
        this.controladorDSP = controladorDSP;
    }

    /**
     * @return the interfaceUsina
     */
    public InterfaceUsina getInterfaceUsina() {
        return interfaceUsina;
    }

    /**
     * @param interfaceUsina
     *           the interfaceUsina to set
     */
    public void setInterfaceUsina(InterfaceUsina interfaceUsina) {
        this.interfaceUsina = interfaceUsina;
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
