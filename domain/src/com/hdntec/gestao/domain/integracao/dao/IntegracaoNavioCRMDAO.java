package com.hdntec.gestao.domain.integracao.dao;

import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.integracao.IntegracaoCargaCRM;
import com.hdntec.gestao.domain.integracao.IntegracaoNavioCRM;
import com.hdntec.gestao.domain.integracao.IntegracaoOrientEmbarqueCRM;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para a entidade forte IntegracaoMES
 */
public class IntegracaoNavioCRMDAO extends AbstractGenericDAO<IntegracaoNavioCRM> {


    /**
     * Atualiza a lista de navios que esta sendo integrado
     * @param listaIntegracaoNavioCRM
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public void atualizarNaviosIntegrados(List<IntegracaoNavioCRM> listaIntegracaoNavioCRM) throws ErroSistemicoException {
        super.salvar(listaIntegracaoNavioCRM);
//        super.encerrarSessao();
    }

    /**
     * salva entidade IntegracaoNavioCRM
     * @param IntegracaoNavioCRM
     */
    public void salvaIntegracaoCRM(IntegracaoNavioCRM integracaoNavio) throws ErroSistemicoException {
        super.salvar(integracaoNavio);
//        super.encerrarSessao();
    }

    /**
     * altera entidade IntegracaoNavioCRM
     * @param IntegracaoNavioCRM
     */
    public void alteraIntegracaoCRM(IntegracaoNavioCRM integracaoNavio) throws ErroSistemicoException {
        super.atualizar(integracaoNavio);
//        super.encerrarSessao();
    }

    /**
     * remove entidade IntegracaoNavioCRM
     * @param IntegracaoNavioCRM
     */
    public void removeIntegracaoCRM(IntegracaoNavioCRM integracaoNavio) throws ErroSistemicoException {
        super.deletar(integracaoNavio);
//        super.encerrarSessao();
    }

    /**
     * busca entidade carga por exemplo
     * @param List<IntegracaoNavioCRM>
     */
    public List<IntegracaoNavioCRM> buscaPorExemploIntegracaoCRM(IntegracaoNavioCRM integracaoNavio) throws ErroSistemicoException {
        List<IntegracaoNavioCRM> listaPesquisada = super.buscarListaDeObjetos(integracaoNavio);
        acessaListasIntegracaoNavioCRM(listaPesquisada);
//        super.encerrarSessao();
        return listaPesquisada;
    }

    /**
     * Metodo que busca os dados lidos do crm
     * @param integracaoCRM
     * @param dataLeituraInicial
     * @param dataLeituraFinal
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public List<IntegracaoNavioCRM> buscarNovosNaviosParaFila(IntegracaoNavioCRM integracaoNavio, Date dataLeituraInicial, Date dataLeituraFinal) throws ErroSistemicoException {
//        List<IntegracaoNavioCRM> listaPesquisada = super.buscarListaDeObjetosPorDatas(integracaoNavio, "dataETA", dataLeituraInicial, dataLeituraFinal);
    	List<IntegracaoNavioCRM> listaPesquisada = super.buscarListaDeObjetosPorCondicaoDiferenteDe(integracaoNavio, "statusEmbarque", "E");
//        acessaListasIntegracaoNavioCRM(listaPesquisada);
//        super.encerrarSessao();
        return listaPesquisada;
    }

    /**
     * Metodo auxiliar que acessa as sublistas do objeto integracaoNavioCRM
     * @param listaPesquisada
     */
    private void acessaListasIntegracaoNavioCRM(List<IntegracaoNavioCRM> listaPesquisada) {
        // acessando a listas e os objetos dos navios
        for (IntegracaoNavioCRM navio : listaPesquisada) {
            for (IntegracaoCargaCRM carga : navio.getListaCargasNavio()) {
                IntegracaoOrientEmbarqueCRM orientacaoEmbarque = carga.getOrientacaoEmbarque();
                if (orientacaoEmbarque != null) {
                    if (orientacaoEmbarque.getListaItensControleCRM() != null) {
                        carga.getOrientacaoEmbarque().getListaItensControleCRM().size();
                    }
                }
            }
        }
    }

}
