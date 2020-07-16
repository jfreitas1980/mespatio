
package com.hdntec.gestao.integracao.integracaoCRM;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.hdntec.gestao.domain.integracao.IControladorIntegracao;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.navios.IControladorNavios;
import com.hdntec.gestao.integracao.plano.controladores.IControladorPlanoDeSituacoesDoPatio;
import com.hdntec.gestao.integracao.produto.IControladorProduto;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;


/**
 * Controlador das operações do subsistema de integração com o sistema externo CRM.
 * 
 * @author andre
 * 
 */
@Stateless(name = "bs/ControladorCRM", mappedName = "bs/ControladorCRM")
public class ControladorCRM implements IControladorCRM {

    /** a interface com o sistema externo CRM */
    private InterfaceCRM interfaceCRM;

    @EJB(name = "bs/ControladorIntegracao/local")
    private IControladorIntegracao controladorIntegracao;

    @EJB(name = "bs/ControladorPlanoDeSituacoesDoPatio/local")
    private IControladorPlanoDeSituacoesDoPatio controladorPlano;

    @EJB(name = "bs/ControladorNavios/local")
    private IControladorNavios controladorNavios;

    @EJB(name = "bs/ControladorProduto/local")
    private IControladorProduto controladorProduto;
    
    @Override
    public PlanoEmpilhamentoRecuperacao atualizarDados(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Date dataSitucao) throws IntegracaoNaoRealizadaException, ErroSistemicoException {

        // cria uma nova interface com o sistema MES
        interfaceCRM = new InterfaceCRM();

        // carrega os controladores para a interface mes
        interfaceCRM.setControladorPlano(controladorPlano);
        interfaceCRM.setControladorIntegracao(controladorIntegracao);
        interfaceCRM.setControladorNavios(controladorNavios);
        interfaceCRM.setControladorProduto(controladorProduto);

        // atualiza os dados
        return interfaceCRM.atualizarDados(dataExecucaoAtualizacao, planoEmpilhamentoRecuperacao,dataSitucao);

    }

    
    @Override
    public void atualizarDados(MetaNavio metaNavio,long timeDefault) throws IntegracaoNaoRealizadaException, ErroSistemicoException {

        // cria uma nova interface com o sistema MES
        interfaceCRM = new InterfaceCRM();

        // carrega os controladores para a interface mes
        interfaceCRM.setControladorPlano(controladorPlano);
        interfaceCRM.setControladorIntegracao(controladorIntegracao);
        interfaceCRM.setControladorNavios(controladorNavios);
        interfaceCRM.setControladorProduto(controladorProduto);

        // atualiza os dados
        interfaceCRM.atualizarNavio(metaNavio,timeDefault);

    }
    
    public InterfaceCRM getInterfaceCRM() {
        return interfaceCRM;
    }

    public void setInterfaceCRM(InterfaceCRM interfaceCRM) {
        this.interfaceCRM = interfaceCRM;
    }
}