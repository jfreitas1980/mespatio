package com.hdntec.gestao.domain.integracao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.integracao.dao.IntegracaoMESDAO;
import com.hdntec.gestao.domain.integracao.dao.IntegracaoNavioCRMDAO;
import com.hdntec.gestao.domain.integracao.dao.IntegracaoParametrosDAO;
import com.hdntec.gestao.domain.integracao.dao.IntegracaoRPUSINASDAO;
import com.hdntec.gestao.domain.integracao.dao.IntegracaoSAPDAO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Controlador que implementa os metodos de integracao dos sistemas externos
 * @author Rodrigo Luchetta
 */
@Stateless(name = "bs/ControladorIntegracao", mappedName = "bs/ControladorIntegracao")
public class ControladorIntegracao implements IControladorIntegracao {

    @Override
    public List<IntegracaoMES> obterDadosIntegracao(IntegracaoMES integracaoMES) throws ErroSistemicoException {
        IntegracaoMESDAO daoMES = new IntegracaoMESDAO();
        List<IntegracaoMES> listaDadosIntegracao = daoMES.buscaPorExemploIntegracaoMES(integracaoMES);
        //Darley a sessao nao pode ser encerrada sempre 
//        daoMES.encerrarSessao();
        return listaDadosIntegracao;
    }

    @Override
    public void atualizarDadosIntegrados(List<IntegracaoMES> listaDadosIntegrados) throws ErroSistemicoException {
        IntegracaoMESDAO daoMES = new IntegracaoMESDAO();
        listaDadosIntegrados = daoMES.salvar(listaDadosIntegrados);
        //Darley a sessao nao pode ser encerrada sempre
//        daoMES.encerrarSessao();
    }

    @Override
    public List<IntegracaoMES> buscarDadosLidosPorData(IntegracaoMES integracaoMES, Date dataLeituraInicial, Date dataLeituraFinal) throws ErroSistemicoException {
        IntegracaoMESDAO daoMES = new IntegracaoMESDAO();
        List<IntegracaoMES> listaDadosIntegracao = daoMES.buscarMESbyPerido(integracaoMES, dataLeituraInicial);
        //Darley a sessao nao pode ser encerrada sempre
//        daoMES.encerrarSessao();
        return listaDadosIntegracao;
    }
    
    @Override
    public Date buscarUltimaDataAntesDaDataInicial(IntegracaoMES integracaoMES, Date dataLeituraInicial) throws ErroSistemicoException {
        IntegracaoMESDAO daoMES = new IntegracaoMESDAO();
        Date ultimaData = daoMES.buscarUltimaDataAntesDaDataInicial(integracaoMES, dataLeituraInicial);
        //Darley a sessao nao pode ser encerrada sempre
//        daoMES.encerrarSessao();
        return ultimaData;
    }

    @Override
    public IntegracaoParametros buscarParametroSistema(Long idSistema) throws ErroSistemicoException {
        IntegracaoParametrosDAO integracaoParamDAO = new IntegracaoParametrosDAO();
        IntegracaoParametros integracaoParametros = integracaoParamDAO.buscarParametroSistema(idSistema);
        //Darley a sessao nao pode ser encerrada sempre
//        integracaoParamDAO.encerrarSessao();
        return integracaoParametros;
    }

    @Override
    public void atualizarParametroSistema(IntegracaoParametros paramIntegracao) throws ErroSistemicoException {
        IntegracaoParametrosDAO integracaoParamDAO = new IntegracaoParametrosDAO();
        integracaoParamDAO.atualizaParametroIntegracao(paramIntegracao);
        //Darley a sessao nao pode ser encerrada sempre
//        integracaoParamDAO.encerrarSessao();
    }

    @Override
    public void salvarParametroSistema(IntegracaoParametros integracaoParametros) throws ErroSistemicoException{
        IntegracaoParametrosDAO integracaoParametrosDAO = new IntegracaoParametrosDAO();
        integracaoParametrosDAO.salvaIntegracaoParametroSistema(integracaoParametros);
        //Darley a sessao nao pode ser encerrada sempre
//        integracaoParametrosDAO.encerrarSessao();
    }

    @Override
    public List<IntegracaoNavioCRM> buscarNovosNaviosParaFila(Date dataLeituraEmbarqueInicial, Date dataLeituraEmbarqueFinal) throws ErroSistemicoException {
        IntegracaoNavioCRMDAO integracaoNavioDAO = new IntegracaoNavioCRMDAO();

	    IntegracaoNavioCRM integracaoNavioCRM = new IntegracaoNavioCRM();
        integracaoNavioCRM.setProcessado(Boolean.FALSE);
	  
        List<IntegracaoNavioCRM> listaPesquisada = integracaoNavioDAO.buscarNovosNaviosParaFila(integracaoNavioCRM, dataLeituraEmbarqueInicial, dataLeituraEmbarqueFinal);
        return listaPesquisada;
    }


    @Override
    public List<IntegracaoNavioCRM> buscarAtualizacaoNavio(String chaveSAP) throws ErroSistemicoException {
        IntegracaoNavioCRMDAO integracaoNavioDAO = new IntegracaoNavioCRMDAO();

      IntegracaoNavioCRM integracaoNavioCRM = new IntegracaoNavioCRM();
      integracaoNavioCRM.setProcessado(Boolean.FALSE);
      integracaoNavioCRM.setSap_vbap_vbeln(chaveSAP);
      List<IntegracaoNavioCRM> listaPesquisada = new ArrayList<IntegracaoNavioCRM>();
      listaPesquisada.add(integracaoNavioDAO.buscarPorObjeto(integracaoNavioCRM));
      return listaPesquisada;
    }

    
    @Override
    public void atualizarNaviosIntegrados(List<IntegracaoNavioCRM> listaNaviosIntegrados) throws ErroSistemicoException {
        IntegracaoNavioCRMDAO daoNavioCRM = new IntegracaoNavioCRMDAO();
        daoNavioCRM.atualizarNaviosIntegrados(listaNaviosIntegrados);
    }

    @Override
    public List<IntegracaoSAP> buscarListaDadosTotalizadosSAP(Date dataInicial, Date dataFinal) throws ErroSistemicoException {
        IntegracaoSAPDAO sapDAO = new IntegracaoSAPDAO();
        
        List<IntegracaoSAP> listaPesquisada = sapDAO.buscarDadosLidosPorData(new IntegracaoSAP(), dataInicial, dataFinal);
        //Darley a sessao nao pode ser encerrada sempre
//        sapDAO.encerrarSessao();
        
        return listaPesquisada;
    }

   @Override
   public List<IntegracaoRPUSINAS> buscarNovoRitimoProducaoUsinas(Date dataHoraSituacao) throws ErroSistemicoException
   {
      List<IntegracaoRPUSINAS> listaRetorno = new ArrayList<IntegracaoRPUSINAS>();
      IntegracaoRPUSINASDAO rpUsinasDAO = new IntegracaoRPUSINASDAO();
      
      listaRetorno = rpUsinasDAO.buscarNovoRitimoProducaoUsinas(dataHoraSituacao);
      
      return listaRetorno;
   }

   @Override
   public List<IntegracaoMES> buscarDadosLidosPorData(IntegracaoMES integracaoMES,
	   Date dataLeituraFinal) throws ErroSistemicoException {
	   IntegracaoMESDAO daoMES = new IntegracaoMESDAO();
	   List<IntegracaoMES> listaDadosIntegracao = daoMES.buscarMESbyPerido(integracaoMES, dataLeituraFinal);
	   //Darley a sessao nao pode ser encerrada sempre
//	   daoMES.encerrarSessao();
	   return listaDadosIntegracao;
   }
   
   
}
