package com.hdntec.gestao.domain.integracao.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para a entidade forte IntegracaoMES
 */
public class IntegracaoMESDAO extends AbstractGenericDAO<IntegracaoMES> {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
    /**
     * salva entidade IntegracaoMES
     *
     * @param IntegracaoMES
     */
    public void salvaIntegracaoMES(IntegracaoMES integracaoMES) throws ErroSistemicoException {
        super.salvar(integracaoMES);
//        super.encerrarSessao();
    }

    /**
     * altera entidade IntegracaoMES
     *
     * @param IntegracaoMES
     */
    public void alteraIntegracaoMES(IntegracaoMES integracaoMES) throws ErroSistemicoException {
        super.atualizar(integracaoMES);
//        super.encerrarSessao();
    }

    /**
     * remove entidade IntegracaoMES
     *
     * @param IntegracaoMES
     */
    public void removeIntegracaoMES(IntegracaoMES integracaoMES) throws ErroSistemicoException {
        super.deletar(integracaoMES);
//        super.encerrarSessao();
    }

    /**
     * busca entidade carga por exemplo
     *
     * @param carga
     */
    public List<IntegracaoMES> buscaPorExemploIntegracaoMES(IntegracaoMES integracaoMES) throws ErroSistemicoException {
       List<IntegracaoMES> lista = super.buscarListaDeObjetos(integracaoMES);
//       super.encerrarSessao();
       return lista;
    }

    /**
     * Metodo que busca os dados lidos do mes
     * @param integracaoMES
     * @param dataLeituraInicial
     * @param dataLeituraFinal
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public List<IntegracaoMES> buscarDadosLidosPorData(IntegracaoMES integracaoMES, Date dataLeituraInicial, Date dataLeituraFinal) throws ErroSistemicoException {
       List<IntegracaoMES> lista = super.buscarListaDeObjetosPorDatas(integracaoMES, "dataLeitura", dataLeituraInicial, dataLeituraFinal);
//       super.encerrarSessao();
       return lista;
    }
    
    public Date buscarUltimaDataAntesDaDataInicial(IntegracaoMES integracaoMES, Date dataLeituraInicial) throws ErroSistemicoException
    {
   	 Session session =  HibernateUtil.getSession();
       StringBuilder sSql = new StringBuilder();
   	 
       sSql.append("select max(dataLeitura) as max from IntegracaoMES where cdAreaRespEd = :param1 AND "); 
       //sSql.append("select dataLeitura from IntegracaoMES where cdAreaRespEd = :param1 AND "); 
       sSql.append("cdFaseProcesso = :param2 AND "); 
       sSql.append("cdItemControle = :param3 AND "); 
       sSql.append("cdTipoProcesso = :param4 AND "); 
       sSql.append("dataLeitura < :param5"); 

       Query q = session.createQuery(sSql.toString());
       q.setParameter("param1", integracaoMES.getCdAreaRespEd());
       q.setParameter("param2", integracaoMES.getCdFaseProcesso());
       q.setParameter("param3", integracaoMES.getCdItemControle());
       q.setParameter("param4", integracaoMES.getCdTipoProcesso());
       q.setParameter("param5", dataLeituraInicial);
       
       return (Date) q.uniqueResult();
    }

    public List<IntegracaoMES> buscarMESbyPerido(IntegracaoMES integracaoMES, Date dataLeituraInicial) throws ErroSistemicoException
    {
    	List<IntegracaoMES> result = null; 
    	Session session =  HibernateUtil.getSession();
       StringBuilder sSql = new StringBuilder();
   	 
      // sSql.append("select * ");
       sSql.append("from IntegracaoMES where cdAreaRespEd = :param1 AND "); 
       sSql.append("cdFaseProcesso = :param2 AND "); 
       sSql.append("cdItemControle = :param3 AND "); 
       sSql.append("cdTipoProcesso = :param4 AND "); 
       sSql.append("dataLeitura >= :param5 ");
       sSql.append(" order by cdFaseProcesso,cdItemControle,cdTipoProcesso,cdAreaRespEd,dataLeitura"); 
       Query q = session.createQuery(sSql.toString());       
       q.setParameter("param1", integracaoMES.getCdAreaRespEd());
       q.setParameter("param2", integracaoMES.getCdFaseProcesso());
       q.setParameter("param3", integracaoMES.getCdItemControle());
       q.setParameter("param4", integracaoMES.getCdTipoProcesso());
       q.setParameter("param5", dataLeituraInicial);       
       //q.setParameter("param6", "N");              
       result = q.list();
       return result;
    }    
}
