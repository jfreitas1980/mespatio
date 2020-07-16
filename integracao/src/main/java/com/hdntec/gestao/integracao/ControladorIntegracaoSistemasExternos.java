package com.hdntec.gestao.integracao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.hdntec.gestao.domain.integracao.ControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.integracao.IntegracaoRPUSINAS;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.integracaoCRM.ControladorCRM;
import com.hdntec.gestao.integracao.integracaoCRM.IControladorCRM;
import com.hdntec.gestao.integracao.integracaoMES.ControladorMES;
import com.hdntec.gestao.integracao.integracaoMES.IControladorMES;
import com.hdntec.gestao.integracao.integracaoPIMS.ControladorPIMS;
import com.hdntec.gestao.integracao.integracaoPIMS.IControladorPIMS;
import com.hdntec.gestao.integracao.planoDeEmpilhamentoERecuperacao.controladores.IControladorAtualizacao;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;

/**
 * Controlador das operações do subsistema de integração com sistemas externos.
 * <p>
 * Requisitos especiais: - Envio de mensagens
 *
 * @author andre
 *
 */
@Stateless(mappedName = "bs/ControladorIntegracaoSistemasExternos")
public class ControladorIntegracaoSistemasExternos implements IControladorIntegracaoSistemasExternos
{

   /** acesso às operações do subsistema de integração com MES */
   @EJB
   private IControladorMES controladorMES;

   /** acesso às operações do subsistema de integração com CRM */
   @EJB
   private IControladorCRM controladorCRM;
    
    /** acesso às operações do subsistema de integração com PIMS */
    @EJB
    private IControladorPIMS controladorPIMS;

   /** acesso às operações do subsistema de integração com MES para ritimo de producao das usinas */
   @EJB
   private IControladorIntegracao controladorRPUsinas;

   /** o status da atualização automática. <code>true</code> significa que está ativada */
   private Boolean atualizacaoAtivada;

   /** data-hora da última atualização. Essa data-hora pode ser usada para saber se é necessário pegar algum dado ou não */
   private Date ultimaAtualizacao;

   /** acesso às operações do subsistema de atualização */
   private IControladorAtualizacao atualizacao;

   @Override
   public void enviaMensagemClienteErroDeLeituraDeSistemasExternos()
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public void ativarAtualizacaoAutomatica()
   {
      this.atualizacaoAtivada = Boolean.TRUE;
   }

   @Override
   public void desativarAtualizacaoAutomatica()
   {
      this.atualizacaoAtivada = Boolean.FALSE;
   }

   @Override
   public void chavearAtualizacaoAutomatica(Boolean atualizacaoAtivada)
   {
      this.atualizacaoAtivada = atualizacaoAtivada;
      if (atualizacaoAtivada) {
         this.desativarAtualizacaoAutomatica();
      }
      else {
         this.ativarAtualizacaoAutomatica();
      }
   }

   @Override
   public Boolean getAtualizacaoAtivada()
   {
      return atualizacaoAtivada;
   }

   @Override
   public PlanoEmpilhamentoRecuperacao atualizarDadosCRM(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Date dataSituacao) throws IntegracaoNaoRealizadaException, ErroSistemicoException
   {
	   if (controladorCRM == null) {
		   controladorCRM = new ControladorCRM();
       }
	   return controladorCRM.atualizarDados(dataExecucaoAtualizacao, planoEmpilhamentoRecuperacao,dataSituacao);
   }
    
   
   
   @Override
   public void atualizarDadosNavioCRM(MetaNavio metaNavio,Date dataSituacao) throws IntegracaoNaoRealizadaException, ErroSistemicoException
   {
       if (controladorCRM == null) {
           controladorCRM = new ControladorCRM();
       }
       controladorCRM.atualizarDados(metaNavio,dataSituacao.getTime());
   }
   
    @Override
    public PlanoEmpilhamentoRecuperacao atualizarDadosPIMS(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Map<String,List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException {
        if (controladorPIMS == null) {
        	controladorPIMS = new ControladorPIMS();
        }
    	PlanoEmpilhamentoRecuperacao planoAtualizado = controladorPIMS.atualizarDados(dataExecucaoAtualizacao, planoEmpilhamentoRecuperacao,map);        
        return planoAtualizado;
    } 

    @Override
    public PlanoEmpilhamentoRecuperacao atualizarDadosMES(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Map<String,List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException {
    	 if (controladorMES == null) {
    		 controladorMES = new ControladorMES();
         }
    	PlanoEmpilhamentoRecuperacao planoAtualizado = controladorMES.atualizarDados(dataExecucaoAtualizacao, planoEmpilhamentoRecuperacao,map);        
        return planoAtualizado;
    }

   public IControladorAtualizacao getAtualizacao()
   {
      return atualizacao;
   }

  
   public void setAtualizacaoAtivada(Boolean atualizacaoAtivada)
   {
      this.atualizacaoAtivada = atualizacaoAtivada;
   }

   public Date getUltimaAtualizacao()
   {
      return ultimaAtualizacao;
   }

   public void setUltimaAtualizacao(Date ultimaAtualizacao)
   {
      this.ultimaAtualizacao = ultimaAtualizacao;
   }

    @Override
    public List<IntegracaoRPUSINAS> buscarNovoRitimoProducaoUsinas(Date dataHoraSituacao) throws ErroSistemicoException {
    	//Darley removendo chamada remota
    	//controladorRPUsinas = new ControladorIntegracao();
    	 if (controladorRPUsinas == null) {
    		 controladorRPUsinas = new ControladorIntegracao();
         }
    	return controladorRPUsinas.buscarNovoRitimoProducaoUsinas(dataHoraSituacao);
    }

	@Override
	public List<IntegracaoMES> atualizarDadosUsina(IntegracaoMES integracaoMES,
			Date dataLeituraInicial) throws IntegracaoNaoRealizadaException,
			ErroSistemicoException {
		//Darley removendo chamada remota
		 if (controladorMES == null) {
    		 controladorMES = new ControladorMES();
         }
		return controladorMES.atualizarDadosUsina(integracaoMES, dataLeituraInicial);
	}

   @Override
   public void atualizarTempoUltimaAtualizacao()
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

}
