/**
 * 
 */
package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

import java.util.Date;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceEditaDadosMaquina;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceMaquinaDoPatio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.vo.atividades.EdicaoMaquinaVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.services.controladores.IControladorExecutarEdicaoMaquinas;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarEdicaoMaquina;



/**
 * @author andre
 *
 */
public class AtualizacaoEdicaoMaquinaDoPatio implements ICommand
{

   private InterfaceMaquinaDoPatio interfaceMaquinaDoPatio;

   private Double taxaDeOperacao;

   private InterfaceEditaDadosMaquina interfaceEditaDadosMaquina;

   /**
    * Construtor da classe AtualizacaoEdicaoMaquina
    * @param interfaceMaquina tipo de maquina que esta sendo editada
    * @param posicao baliza onde a maquina esta
    * @param estado  estado atual da maquina
    * @param taxaOperacao taxa de operacao da maquina
    */
   public AtualizacaoEdicaoMaquinaDoPatio(InterfaceMaquinaDoPatio interfaceMaquinaDoPatio, InterfaceEditaDadosMaquina interfaceEditaDadosMaquina) {
       this.interfaceMaquinaDoPatio = interfaceMaquinaDoPatio;
       this.interfaceEditaDadosMaquina = interfaceEditaDadosMaquina;             
   }   
   
   @Override
   public void execute() {
       
	   
	   IControladorExecutarEdicaoMaquinas service = ControladorExecutarEdicaoMaquina.getInstance();
	   
	   EdicaoMaquinaVO vo = new EdicaoMaquinaVO();
	   
	   MetaMaquinaDoPatio metaMaquina = this.interfaceMaquinaDoPatio.getMaquinaVisualizada().getMetaMaquina();
	   Date dataSitucao =  interfaceMaquinaDoPatio.getControladorDSP().getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio();	   
	   dataSitucao = Atividade.verificaAtualizaDataAtividade(dataSitucao, dataSitucao);	  
	   
	   
	   vo.setEstado(interfaceEditaDadosMaquina.getEstado());
       vo.setPosicao(interfaceEditaDadosMaquina.getPosicaoEditada());
       vo.setPatio(interfaceEditaDadosMaquina.getPosicaoEditada().getMetaPatio());
       vo.setTaxaDeOperacaoNominal(interfaceEditaDadosMaquina.getTaxaOperacao());
       vo.setVelocidadeDeslocamento(interfaceEditaDadosMaquina.getVelocidadeDeslocamento());
       vo.setGirarLanca(interfaceEditaDadosMaquina.getGirarLanca());
       vo.setMaquina(metaMaquina);
       vo.setDataInicio(dataSitucao); 
       vo.setDataFim(dataSitucao);
	   Atividade atividade = null;
       try {
		atividade = service.editarMaquinas(vo);
		this.interfaceMaquinaDoPatio.getControladorDSP().getInterfaceInicial().editarMaquinas(atividade);
		//this.interfaceMaquinaDoPatio.getControladorDSP().getInterfaceInicial().getInterfaceInicial().atualizarDSP();
		
	} catch (AtividadeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();	
	}
	   
   }
}
