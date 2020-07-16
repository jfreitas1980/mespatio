package com.hdntec.gestao.integracao.integracaoMES;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.hdntec.gestao.domain.integracao.ControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;

/**
 * Controlador das operações do subsistema de integração com o sistema externo MES.
 * 
 * @author andre
 * 
 */
@Stateless(name = "bs/ControladorMES", mappedName = "bs/ControladorMES")
public class ControladorMES implements IControladorMES {

    /** a interface com o sistema externo MES */
    private InterfaceMES interfaceMES;

    @EJB(name = "bs/ControladorIntegracao/local")
    private IControladorIntegracao controladorIntegracao;

    @EJB(name = "bs/ControladorPlanoDeSituacoesDoPatio/local")
    private IControladorModelo controladorModelo;

    @Override
    public PlanoEmpilhamentoRecuperacao atualizarDados(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Map<String,List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException {

        // cria uma nova interface com o sistema MES
        interfaceMES = getInterfaceMES();

        initServices();

        
        // carrega os controladores para a interface mes
        interfaceMES.setControladorModelo(controladorModelo);
        interfaceMES.setControladorIntegracao(controladorIntegracao);

        // atualiza os dados e retorna o plano do cliente atualizado
        PlanoEmpilhamentoRecuperacao planoAtualizado = interfaceMES.atualizarDados(dataExecucaoAtualizacao, planoEmpilhamentoRecuperacao,map);
       /* controladorModelo = null;*/
        interfaceMES = null;
        return planoAtualizado;
    }



	private void initServices() {
		if (controladorIntegracao == null) {
        	controladorIntegracao = new ControladorIntegracao();
        }


        if (controladorModelo == null) {
        	controladorModelo = new ControladorModelo();
        }
	}

    
     
    public InterfaceMES getInterfaceMES() {
        //if  (interfaceMES == null) {
        	 interfaceMES = new InterfaceMES();
        //}
    	return interfaceMES;
    }

    public void setInterfaceMES(InterfaceMES interfaceMES) {
        this.interfaceMES = interfaceMES;
    }



	@Override
	public List<IntegracaoMES> atualizarDadosUsina(IntegracaoMES integracaoMES,
			Date dataLeituraInicial) throws ErroSistemicoException {
		interfaceMES = getInterfaceMES();
		
		  initServices();

		
		// carrega os controladores para a interface mes
		  interfaceMES.setControladorModelo(controladorModelo);
        interfaceMES.setControladorIntegracao(controladorIntegracao);
  
		// TODO Auto-generated method stub
	
		List<IntegracaoMES>  result = interfaceMES.atualizarDadosUsina(integracaoMES, dataLeituraInicial);
		interfaceMES = null;
		//controladorModelo = null;
		//controladorIntegracao = null;
		// atualiza a data da interfaceMES para agora devido a leitura dos dados da usina
		//if (result != null && result.size() != 0) {
		   // IntegracaoMES lastItem = result.get(result.size() -1);
			//InterfaceMES.defineCurrTime(lastItem.getDataLeitura());	
		//}
		return result;
	}

}
