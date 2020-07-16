package com.hdntec.gestao.integracao.integracaoPIMS;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.hdntec.gestao.domain.integracao.IControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;

/**
 * Controlador das operações do subsistema de integração com o sistema externo PIMS.
 * 
 * @author andre
 * 
 */
@Stateless(name = "bs/ControladorPIMS", mappedName = "bs/ControladorPIMS")
public class ControladorPIMS implements IControladorPIMS {

    @EJB(name = "bs/ControladorIntegracao/local")
    private IControladorIntegracao controladorIntegracao;

    @EJB(name = "bs/ControladorPlanoDeSituacoesDoPatio/local")
    private IControladorModelo controladorModelo;

    /** a interface com o sistema externo PIMS */
    private InterfacePIMS interfacePIMS;

    @Override
    public PlanoEmpilhamentoRecuperacao atualizarDados(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Map<String,List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException {
        try {
            interfacePIMS = new InterfacePIMS();
            interfacePIMS.setControladorModelo(controladorModelo);
            interfacePIMS.setControladorIntegracao(controladorIntegracao);
            
            // atualiza os dados e retorna o plano do cliente atualizado
            PlanoEmpilhamentoRecuperacao planoAtualizado = interfacePIMS.atualizarDados(dataExecucaoAtualizacao, planoEmpilhamentoRecuperacao,map);
            interfacePIMS = null;
            //controladorModelo = null;
            return planoAtualizado;
            
        } catch (IntegracaoNaoRealizadaException ex) {
            Logger.getLogger(ControladorPIMS.class.getName()).log(Level.SEVERE, null, ex);
            throw(ex);
        } catch (ErroSistemicoException ex) {
            Logger.getLogger(ControladorPIMS.class.getName()).log(Level.SEVERE, null, ex);
            throw(ex);
        }
    }

    public InterfacePIMS getInterfacePIMS() {
        return interfacePIMS;
    }

    public void setInterfacePIMS(InterfacePIMS interfacePIMS) {
        this.interfacePIMS = interfacePIMS;
    }
}