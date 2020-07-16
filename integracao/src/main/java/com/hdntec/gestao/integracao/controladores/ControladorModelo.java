package com.hdntec.gestao.integracao.controladores;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.hdntec.gestao.domain.integracao.ControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IntegracaoParametros;
import com.hdntec.gestao.domain.integracao.IntegracaoSAP;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.relatorio.FaixaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.TabelaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.dao.FaixaAmostragemFrequenciaDAO;
import com.hdntec.gestao.domain.relatorio.dao.IFaixaAmostragemFrequenciaDAO;
import com.hdntec.gestao.domain.relatorio.dao.ITabelaAmostragemFrequenciaDAO;
import com.hdntec.gestao.domain.relatorio.dao.TabelaAmostragemFrequenciaDAO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.navios.ControladorNavios;
import com.hdntec.gestao.integracao.navios.IControladorNavios;
import com.hdntec.gestao.integracao.pilhas.ControladorPilhas;
import com.hdntec.gestao.integracao.pilhas.IControladorPilha;
import com.hdntec.gestao.integracao.plano.controladores.ControladorPlanoDeSituacoesDoPatio;
import com.hdntec.gestao.integracao.plano.controladores.IControladorPlanoDeSituacoesDoPatio;
import com.hdntec.gestao.integracao.planta.ControladorPlanta;
import com.hdntec.gestao.integracao.planta.IControladorPlanta;
import com.hdntec.gestao.integracao.produto.ControladorProduto;
import com.hdntec.gestao.integracao.produto.IControladorProduto;


/**
 * Controlador das operações do subsistema de modelo de domínio.
 * 
 * @author andre
 * 
 */
//, mappedName = "bs/ControladorModelo"
@Stateless(mappedName = "bs/ControladorModelo")
public class ControladorModelo implements IControladorModelo {

    //@EJB(name = "bs/ControladorProduto/local")
    @EJB
    private IControladorProduto controladorProduto;

    //@EJB(name = "bs/ControladorPilha/local")    
    @EJB
    private IControladorPilha controladorPilha;

    @EJB//(name = "bs/ControladorPlanoDeSituacoesDoPatio/local")
    private IControladorPlanoDeSituacoesDoPatio controladorPlano;

    @EJB//(name = "bs/ControladorPlanta/local")
    private IControladorPlanta controladorPlanta;

    
    @EJB//(name = "bs/ControladorNavio/local")
    private IControladorNavios controladorNavio;

    @EJB//(name = "bs/ControladorIntegracao/local")
    private IControladorIntegracao controladorIntegracao;

    @Override
    public PlanoEmpilhamentoRecuperacao salvarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException {
   	  if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }

        return controladorPlano.salvarPlanoDeEmpilhamentoERecuperacao(planoDeEmpilhamentoERecuperacao);
    }

    @Override
    public void atualizarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException {
   	  if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }

        controladorPlano.atualizarPlanoDeEmpilhamentoERecuperacao(planoDeEmpilhamentoERecuperacao);
    }

    @Override
    public List<PlanoEmpilhamentoRecuperacao> buscarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException {
//    	controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
   	  if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }

    	return controladorPlano.buscarPlanoDeEmpilhamentoERecuperacao(planoDeEmpilhamentoERecuperacao);
    }

    @Override
    public void removerPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException {
//    	controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
    	controladorPlano.removerPlanoDeEmpilhamentoERecuperacao(planoDeEmpilhamentoERecuperacao);
    }

    @Override
    public void salvaPlanta(Planta planta) throws ErroSistemicoException {
 	   if (	controladorPlanta == null) {
 	       	controladorPlanta = new ControladorPlanta();
 	       }
    	controladorPlanta.salvaPlanta(planta);
    }

    @Override
    public SituacaoPatio salvaSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
    	 if (controladorPlano == null) {
		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	 }    	
        return controladorPlano.salvaSituacaoPatio(situacaoPatio);
    }

    @Override
    public List<SituacaoPatio> salvaSituacaoPatio(List<SituacaoPatio> lstSituacaoPatio) throws ErroSistemicoException {
//    	controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
   	 if (controladorPlano == null) {
 		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
     	 }    	

    	return controladorPlano.salvaSituacaoPatio(lstSituacaoPatio);
    }
    
    @Override
    public void removerSituacaoPatio(List<SituacaoPatio> lstSituacaoPatio) throws ErroSistemicoException {
    	if (controladorPlano == null) {
    		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	}
        controladorPlano.removerSituacaoPatio(lstSituacaoPatio);
    }

    @Override
    public void removerSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
    	//Darley removendo a chamada remota se necessario
    	if (controladorPlano == null) {
    		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	}
        controladorPlano.removerSituacaoPatio(situacaoPatio);
    }

    @Override
    public void salvaProduto(Produto produto) throws ErroSistemicoException {
//    	controladorProduto = new ControladorProduto();
    	if (controladorProduto == null) {
    		controladorProduto = new ControladorProduto();
    	}        
    	controladorProduto.salvaProduto(produto);
    }

    @Override
    public TipoProduto salvaTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
//    	controladorProduto = new ControladorProduto();
    	if (controladorProduto == null) {
    		controladorProduto = new ControladorProduto();
    	}
    	return controladorProduto.salvaTipoProduto(tipoProduto);
    }

     @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficial() throws ErroSistemicoException {
    	//Jessé RMI OFF
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
        return controladorPlano.buscarPlanoEmpilhamentoRecuperacaoOficial();
    }

    @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficial(Long qtdeHorasSituacoesAnteriores) throws ErroSistemicoException {
    	System.out.println("Inicio Leitura servidor" +new Date(System.currentTimeMillis()));
    	//Jessé RMI OFF
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
    	PlanoEmpilhamentoRecuperacao result = controladorPlano.buscarPlanoEmpilhamentoRecuperacaoOficial(qtdeHorasSituacoesAnteriores);
    	System.out.println("Fim Leitura servidor" +new Date(System.currentTimeMillis()));
    	return result;
    }

    @Override
    public List<TipoProduto> buscarTiposProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
    	//Darley RMI OFF
    	if (controladorProduto == null) {
    		controladorProduto = new ControladorProduto();
    	}
        return controladorProduto.buscarTiposProduto(tipoProduto);
    }

    @Override
    public List<TipoItemDeControle> buscarTiposItemDeControle(TipoItemDeControle tipoItemDeControle) throws ErroSistemicoException {
    	//Darley RMI OFF
    	if (controladorProduto == null) {
    		controladorProduto = new ControladorProduto();
    	}
        return controladorProduto.buscarTiposItemControle();
    }

    @Override
    public void adicionaEOrdenaNovasSituacoesNoPlanoOficial(List<SituacaoPatio> listaDeNovasSituacoesDePatio) throws ErroSistemicoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SituacaoPatio> buscarNovasSituacoesRealizadasDoPlanoEmpilhamentoRecuperacaoOficial(Date timestamp) throws ErroSistemicoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Atividade salvarAtividade(Atividade atividade) throws ErroSistemicoException {
    	//Darley RMI OFF
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
        return controladorPlano.salvarAtividade(atividade);
    }

    @Override
    public void atualizarAtividade(Atividade atividade) throws ErroSistemicoException {
    	//Darley RMI OFF
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
        controladorPlano.atualizarAtividade(atividade);
    }

    @Override
    public void salvaListaBalizas(List<Baliza> listaBalizas) throws ErroSistemicoException {
    	if (controladorPilha == null) {
    		controladorPilha = new ControladorPilhas();
    	}
    	controladorPilha.salvaListaBalizas(listaBalizas);
    }

    @Override
    public List<PlanoEmpilhamentoRecuperacao> buscarListaPlanosOficiaisPorData(Date dataInicial, Date dataFinal) throws ErroSistemicoException {
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
        return controladorPlano.buscarListaPlanosOficiaisPorData(dataInicial, dataFinal);
    }

    @Override
    public void salvaListaTipoItemControle(List<TipoItemDeControle> listaItensControle) throws ErroSistemicoException {
    	//Darley RMI OFF
    	if (controladorProduto == null) {
    		controladorProduto = new ControladorProduto();
    	}

        controladorProduto.salvarTiposItemControle(listaItensControle);
    }

    @Override
    public List<TipoItemDeControle> buscarTiposItemControle() throws ErroSistemicoException {
    	//Darley RMI OFF
    	if (controladorProduto == null) {
    		controladorProduto = new ControladorProduto();
    	}
        return controladorProduto.buscarTiposItemControle();
    }

    @Override
    public FilaDeNavios salvarFilaDeNavios(FilaDeNavios filaNavio) throws ErroSistemicoException {
      	if (controladorNavio == null) {
    		controladorNavio = new ControladorNavios();
    	}

        return controladorNavio.salvarFilaDeNavios(filaNavio);
    }

    @Override
    public FilaDeNavios buscarFilaDeNavios(FilaDeNavios filaNavio) throws ErroSistemicoException {
      	if (controladorNavio == null) {
    		controladorNavio = new ControladorNavios();
    	}

        return controladorNavio.buscarFilaDeNavios(filaNavio);
    }

    @Override
    public List<Cliente> buscaClientes(Cliente cliente) throws ErroSistemicoException {
      	if (controladorNavio == null) {
    		controladorNavio = new ControladorNavios();
    	}

        return controladorNavio.buscarCliente(cliente);
    }

    @Override
    public List<Cliente> buscaClientes() throws ErroSistemicoException {
      	if (controladorNavio == null) {
    		controladorNavio = new ControladorNavios();
    	}

        return controladorNavio.buscaListaClientes();
    }

    
    @Override
    public List<CarregadoraDeNavios> buscarListaCarregadorasNavio() throws ErroSistemicoException {
 	   if (	controladorPlanta == null) {
 	       	controladorPlanta = new ControladorPlanta();
 	       }
    	return controladorPlanta.buscarListaCarregadorasNavio();
    }

    @Override
    public List<IntegracaoSAP> buscarListaDadosTotalizadosSAP(Date dataInicial, Date dataFinal) throws ErroSistemicoException {

    	if (controladorIntegracao == null) {
  		  	controladorIntegracao = new ControladorIntegracao();
  	   	   }

    	return controladorIntegracao.buscarListaDadosTotalizadosSAP(dataInicial, dataFinal);
    }

    @Override
    public IntegracaoParametros buscarParametroSistema(Long idSistema) throws ErroSistemicoException {
    	if (controladorIntegracao == null) {
  		  	controladorIntegracao = new ControladorIntegracao();
  	   	   }
    	IntegracaoParametros integracao = controladorIntegracao.buscarParametroSistema(idSistema);
    	encerrarSessao();
        return integracao;
    }

    @Override
    public void atualizaParametrosSistema(IntegracaoParametros integracaoParametros) throws ErroSistemicoException {
    	if (controladorIntegracao == null) {
  		  	controladorIntegracao = new ControladorIntegracao();
  	   	   }
    	controladorIntegracao.atualizarParametroSistema(integracaoParametros);
        encerrarSessao();
    }

    @Override
     public void salvarParametrosSistemas(IntegracaoParametros integracaoParametros) throws ErroSistemicoException{
    	if (controladorIntegracao == null) {
  		  	controladorIntegracao = new ControladorIntegracao();
  	   	   }
 
    	controladorIntegracao.salvarParametroSistema(integracaoParametros);
         encerrarSessao();
     }


    @Override
    public void atualizaFilaDeNavios(FilaDeNavios filaNavios) throws ErroSistemicoException {
//    	controladorNavio = new ControladorNavios();
      	if (controladorNavio == null) {
    		controladorNavio = new ControladorNavios();
    	}
    	controladorNavio.atualizarFilaDeNavios(filaNavios);
    }

    @Override
    public List<MaquinaDoPatio> buscarListaMaquinas() throws ErroSistemicoException {
    	//Darley removendo chamada remota
        if (	controladorPlanta == null) {
        	controladorPlanta = new ControladorPlanta();
        }
     
    	return controladorPlanta.buscarListaMaquinas();
    }

    @Override
    public Cliente salvarCliente(Cliente cliente) throws ErroSistemicoException {

    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
    	return controladorPlano.salvarCliente(cliente);
    }

    @Override
    public Navio salvarNavio(Navio navio) throws ErroSistemicoException {
    	if (controladorNavio == null) {
    		controladorNavio = new ControladorNavios();
    	}
        return controladorNavio.salvarNavio(navio);
    }

    @Override
    public void removerNavio(Navio navio) throws ErroSistemicoException
    {
    	if (controladorNavio == null) {
    		controladorNavio = new ControladorNavios();
    	}
        controladorNavio.removerNavio(navio);
    }

    @Override
    public void salvaListaTiposProduto(List<TipoProduto> listaTiposProduto) throws ErroSistemicoException {
    	  if (controladorProduto == null) {
    		  controladorProduto = new ControladorProduto();
  	  }
    	controladorProduto.salvaListaTiposProduto(listaTiposProduto);
    }

    
    @Override
    public List<PlanoEmpilhamentoRecuperacao> salvarOuAtualizarPlanosDaLista(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException {
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	  }
    	return controladorPlano.salvarOuAtualizarPlanosDaLista(listaPlanos);
    }

    @Override
    public void removeListaDePlanoEmpilhamentoRecuperacao(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException {
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	  }
    	controladorPlano.removeListaDePlanoEmpilhamentoRecuperacao(listaPlanos);
    }

    @Override
    public void atualizaCliente(Cliente cliente) throws ErroSistemicoException {
    	if (controladorNavio == null) {
    		controladorNavio = new ControladorNavios();
    	}
        controladorNavio.atualizaCliente(cliente);
    }

    @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoPorUsuario(Long idUser) throws ErroSistemicoException {
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	  }
    	return controladorPlano.buscarPlanoEmpilhamentoRecuperacaoPorUsuario(idUser);
    }
    
    @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficialPorPeriodo(Date dataInicial, Date dataFinal) throws ErroSistemicoException {
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
    	return controladorPlano.buscarPlanoEmpilhamentoRecuperacaoOficialPorPeriodo(dataInicial, dataFinal);
    }
    
    @Override
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoUsuarioPorPeriodo(Long idUser, Date dataInicial, Date dataFinal) throws ErroSistemicoException {
    	  if (controladorPlano == null) {
    	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
    	   	   }
    	return controladorPlano.buscarPlanoEmpilhamentoRecuperacaoUsuarioPorPeriodo(idUser, dataInicial, dataFinal);
    }

   @Override
   public SituacaoPatio buscarUltimaSituacaoPatioDoDia(Date dataPesquisa) throws ErroSistemicoException
   {
		  if (controladorPlano == null) {
  	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
  	   	   }
  
      return controladorPlano.buscarUltimaSituacaoPatioDoDia(dataPesquisa);
   }

   @Override
   public Pier salvaPier(Pier pier) throws ErroSistemicoException {
	   if (	controladorPlanta == null) {
       	controladorPlanta = new ControladorPlanta();
       }
    
	   return controladorPlanta.salvaPier(pier);
   }


   @Override
   public void removeManutencao(Manutencao manutencao) throws ErroSistemicoException {
	   if (	controladorPlanta == null) {
       	controladorPlanta = new ControladorPlanta();
       }
    
	   controladorPlanta.removeManutencao(manutencao);
   }

   @Override
   public MaquinaDoPatio salvaMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException {
	   if (controladorPlanta == null) {
		   controladorPlanta = new ControladorPlanta();
	   }
         return controladorPlanta.salvaMaquinaDoPatio(maquina);
   }

   @Override
   public MaquinaDoPatio buscarMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException {
	   if (	controladorPlanta == null) {
       	controladorPlanta = new ControladorPlanta();
       }
    
	   return controladorPlanta.buscarMaquinaDoPatio(maquina);
   }

   @Override
   public List<MetaMaquinaDoPatio> buscarMaquinaDoPatio(MetaMaquinaDoPatio maquina) throws ErroSistemicoException {
       if ( controladorPlanta == null) {
        controladorPlanta = new ControladorPlanta();
       }
    
       return controladorPlanta.buscarMaquinaDoPatio(maquina);
   }

   
   @Override
   public Correia buscarCorreia(Correia correia) throws ErroSistemicoException {
	   if (	controladorPlanta == null) {
       	controladorPlanta = new ControladorPlanta();
       }
    
	   return controladorPlanta.buscarCorreia(correia);
   }

   @Override
   public Correia salvaCorreia(Correia correia) throws ErroSistemicoException {
	   if (	controladorPlanta == null) {
       	controladorPlanta = new ControladorPlanta();
       }
    
	   return controladorPlanta.salvaCorreia(correia);
   }

   @Override
   public List<MaquinaDoPatio> buscaListaDeMaquinasDoPatio() throws ErroSistemicoException {
	   if (	controladorPlanta == null) {
       	controladorPlanta = new ControladorPlanta();
       }
    
	   return controladorPlanta.buscaListaDeMaquinasDoPatio();
   }

   @Override
   public List<Manutencao> salvarOuAtualizarListaDeManutencoes(List<Manutencao> listaManutencoes) throws ErroSistemicoException
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public SituacaoPatio buscarSituacaoPatioOficialAtualPorPeriodo(Date dataInicial, Date dataFinal) throws ErroSistemicoException {
   	   if (controladorPlano == null) {
   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
   	   }
	   return controladorPlano.buscarSituacaoPatioOficialAtualPorPeriodo(dataInicial, dataFinal);
   }
   
   @Override
   public List<SituacaoPatio> buscarListaSPOficialPorPeriodoEAtividade(
   		Date dataInicial, Date dataFinal, TipoAtividadeEnum tipoAtividade)
   		throws ErroSistemicoException {
	   if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }
	   return controladorPlano.buscarListaSPOficialPorPeriodoEAtividade(
			   dataInicial, dataFinal, tipoAtividade);
   }
   
   @Override
   public List<FaixaAmostragemFrequencia> buscarFaixaAmostragem(
   		FaixaAmostragemFrequencia faixa) throws ErroSistemicoException {
   	
   	IFaixaAmostragemFrequenciaDAO dao = new FaixaAmostragemFrequenciaDAO();
   	return dao.buscarPorExemplo(faixa);
   }
   
   @Override
   public void removerFaixaAmostragemFrequencia(FaixaAmostragemFrequencia faixa)
   		throws ErroSistemicoException {
   	IFaixaAmostragemFrequenciaDAO dao = new FaixaAmostragemFrequenciaDAO();
   	dao.removerFaixaAmostragemFrequencia(faixa);
   }
   
   @Override
   public FaixaAmostragemFrequencia salvarFaixaAmostragemFrequencia(
   		FaixaAmostragemFrequencia faixa) throws ErroSistemicoException {
   	IFaixaAmostragemFrequenciaDAO dao = new FaixaAmostragemFrequenciaDAO();
   	return dao.salvarFaixaAmostragemFrequencia(faixa);
   }
   
   @Override
   public List<TabelaAmostragemFrequencia> buscarTabelaAmostragem(
   		TabelaAmostragemFrequencia tabela) throws ErroSistemicoException {
   	
   	ITabelaAmostragemFrequenciaDAO dao = new TabelaAmostragemFrequenciaDAO();
   	return dao.buscarPorExemplo(tabela);
   }
   
   @Override
   public void removerTabelaAmostragemFrequencia(
   		TabelaAmostragemFrequencia tabela) throws ErroSistemicoException {
   	
   	ITabelaAmostragemFrequenciaDAO dao = new TabelaAmostragemFrequenciaDAO();
   	dao.removerTabelaAmostragemFrequencia(tabela);
   }
   
   @Override
   public TabelaAmostragemFrequencia salvarTabelaAmostragemFrequencia(
   		TabelaAmostragemFrequencia tabela) throws ErroSistemicoException {

   	ITabelaAmostragemFrequenciaDAO dao = new TabelaAmostragemFrequenciaDAO();
   	return dao.salvarTabelaAmostragemFrequencia(tabela);
   }
   
   @Override
   public TabelaAmostragemFrequencia recuperarTabelaAmostragemAtual() {
   	ITabelaAmostragemFrequenciaDAO dao = new TabelaAmostragemFrequenciaDAO();
   	return dao.buscarTabelaAtual();
   }
   
   @Override
   public List<SituacaoPatio> buscarSituacaoPatioRelAtividade(
   		Date dataInicial, Date dataFinal, boolean ehOficial)
   		throws ErroSistemicoException {
	   if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }
   	return controladorPlano.buscarSituacaoPatioRelAtividade(dataInicial, 
   			dataFinal, ehOficial);
   }
   
   @Override
	public List<Patio> atualizarListaBalizasPorPatios(List<Patio> listaPatios)
			throws ErroSistemicoException {
	   if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }
		return controladorPlano.atualizarListaBalizasPorPatios(listaPatios);
	}
   
   @Override
   public List<Patio> atualizarMaquinasDoPatio(List<Patio> listaPatios)
		throws ErroSistemicoException {
	   if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }
	   return controladorPlano.atualizarMaquinasDoPatio(listaPatios);
   }
   
   @Override
   public List<Correia> atualizarListaMaquinasCorreias(List<Correia> correias) {
	   if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }
	   return controladorPlano.atualizarListaMaquinasCorreias(correias);
   }
   
   @Override
   public List<Usina> atualizarListaEmergenciaUsinas(List<Usina> usinas) {
	   if (controladorPlano == null) {
	   		controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   	   }
	   return controladorPlano.atualizarListaEmergenciaUsinas(usinas);
   }
     
   @Override
   public void encerrarSessao() {
	   if (controladorPlano != null) {
		   controladorPlano.encerrarSessao();
	   }
   }
   
   @Override
   public void atualizarSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException {
	   //Darley removendo a chamada remota se necessario
	   if (controladorPlano == null) {
		   controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
	   }
	   controladorPlano.atualizaSituacaoPatio(situacaoPatio);
   }
   
   @Override
   public List<MaquinaDoPatio> buscaListaMaquinaDoPatioPorManutencao(
		   MaquinaDoPatio maquina, Date dataInicial, Date dataFinal) {
	   if (controladorPlanta == null) {
		   controladorPlanta = new ControladorPlanta();
	   }
	   return controladorPlanta.buscaListaMaquinaDoPatioPorManutencao(maquina,dataInicial,dataFinal);
   }

   @Override
   public List<Correia> buscaListaCorreiaPorManutencao(Correia correia,
		   Date dataInicial, Date dataFinal) {
	   if (controladorPlanta == null) {
		   controladorPlanta = new ControladorPlanta();
	   }
	   return controladorPlanta.buscaListaCorreiaPorManutencao(correia,dataInicial,dataFinal);
   }
}
