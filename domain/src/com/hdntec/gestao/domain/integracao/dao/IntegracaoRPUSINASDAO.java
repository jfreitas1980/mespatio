package com.hdntec.gestao.domain.integracao.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Expression;

import com.hdntec.gestao.domain.integracao.IntegracaoRPUSINAS;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para a entidade forte IntegracaoRPUSINAS
 */
public class IntegracaoRPUSINASDAO extends AbstractGenericDAO<IntegracaoRPUSINAS> {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
    /**
     * salva entidade IntegracaoRPUSINAS
     *
     * @param IntegracaoRPUSINAS
     */
    public void salvaIntegracaoMES(IntegracaoRPUSINAS integracaoRPUsinas) throws ErroSistemicoException {
        super.salvar(integracaoRPUsinas);
//        super.encerrarSessao();
    }

    /**
     * altera entidade IntegracaoRPUSINAS
     *
     * @param IntegracaoRPUSINAS
     */
    public void alteraIntegracaoMES(IntegracaoRPUSINAS integracaoRPUsinas) throws ErroSistemicoException {
        super.atualizar(integracaoRPUsinas);
//        super.encerrarSessao();
    }

    /**
     * remove entidade IntegracaoRPUSINAS
     *
     * @param IntegracaoRPUSINAS
     */
    public void removeIntegracaoMES(IntegracaoRPUSINAS integracaoRPUsinas) throws ErroSistemicoException {
        super.deletar(integracaoRPUsinas);
//        super.encerrarSessao();
    }

    /**
     * busca entidade carga por exemplo
     *
     * @param IntegracaoRPUSINAS
     */
    public List<IntegracaoRPUSINAS> buscaPorExemploIntegracaoMES(IntegracaoRPUSINAS integracaoRPUsinas) throws ErroSistemicoException {
       List<IntegracaoRPUSINAS> lista = super.buscarListaDeObjetos(integracaoRPUsinas);
//       super.encerrarSessao();
       return lista;
    }

    /**
     * Metodo que busca os dados lidos do mes
     * @param integracaoRPUsinas
     * @param dataLeituraInicial
     * @param dataLeituraFinal
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public List<IntegracaoRPUSINAS> buscarDadosLidosPorData(IntegracaoRPUSINAS integracaoRPUsinas, Date dataLeituraInicial, Date dataLeituraFinal) throws ErroSistemicoException {
       List<IntegracaoRPUSINAS> lista = super.buscarListaDeObjetosPorDatas(integracaoRPUsinas, "dataLeitura", dataLeituraInicial, dataLeituraFinal);
//       super.encerrarSessao();
       return lista;
    }
    
    /**
     * Metodo que busca os novos ritimos de producao das usinas do MES de acordo
     * com a data da situacao
     * 
     * @param dataSituacaoPatioBase
     * @return
     * @throws ErroSistemicoException
     */
    public List<IntegracaoRPUSINAS> buscarNovoRitimoProducaoUsinas(Date dataSituacaoPatioBase) throws ErroSistemicoException
    {
       List<IntegracaoRPUSINAS> listaRetorno = new ArrayList<IntegracaoRPUSINAS>();
       final Criteria crit = HibernateUtil.getSession().createCriteria(IntegracaoRPUSINAS.class);
       
       crit.add( Expression.sql("dataleitura >= ?", dataSituacaoPatioBase, Hibernate.TIMESTAMP));
       
       listaRetorno = crit.list();
       
       return listaRetorno;
    }
    
    
}
