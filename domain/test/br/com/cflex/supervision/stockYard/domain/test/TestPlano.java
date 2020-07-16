
package br.com.cflex.supervision.stockYard.domain.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.integracao.IntegracaoParametros;
import com.hdntec.gestao.domain.integracao.dao.IntegracaoParametrosDAO;
import com.hdntec.gestao.domain.navios.dao.ClienteDAO;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.plano.dao.PlanoEmpilhamentoRecuperacaoDAO;
import com.hdntec.gestao.domain.plano.dao.SituacaoPatioDAO;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.dao.CampanhaDAO;
import com.hdntec.gestao.domain.planta.dao.MetaBalizaDAO;
import com.hdntec.gestao.domain.planta.dao.MetaBercoDAO;
import com.hdntec.gestao.domain.planta.dao.MetaCarregadoraDeNaviosDAO;
import com.hdntec.gestao.domain.planta.dao.MetaCorreiaDAO;
import com.hdntec.gestao.domain.planta.dao.MetaFiltragemDAO;
import com.hdntec.gestao.domain.planta.dao.MetaMaquinaDoPatioDAO;
import com.hdntec.gestao.domain.planta.dao.MetaPatioDAO;
import com.hdntec.gestao.domain.planta.dao.MetaPierDAO;
import com.hdntec.gestao.domain.planta.dao.MetaUsinaDAO;
import com.hdntec.gestao.domain.planta.dao.PlantaDAO;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.entity.MetaCarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.produto.dao.MetaInternaDAO;
import com.hdntec.gestao.domain.produto.dao.TipoItemDeControleDAO;
import com.hdntec.gestao.domain.produto.dao.TipoProdutoDAO;
import com.hdntec.gestao.domain.produto.entity.MetaInterna;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.produto.enums.TipoMetaInternaEnum;
import com.hdntec.gestao.domain.relatorio.FaixaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.TabelaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.dao.FaixaAmostragemFrequenciaDAO;
import com.hdntec.gestao.domain.relatorio.dao.TabelaAmostragemFrequenciaDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;
import com.hdntec.gestao.util.GeradorModeloUtilCenarioCompleto;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * <P><B>Description :</B><BR>
 * General TestPlano
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 09/06/2010
 * @version $Revision: 1.1 $
 */
public class TestPlano extends TestCase {

    PlanoEmpilhamentoRecuperacao plano;
    Planta planta;
    MetaPier pier;
    PlantaDAO plantaDAO = new PlantaDAO();
    MetaPierDAO metaPierDAO = new MetaPierDAO();
    MetaPatioDAO metaPatioDAO = new MetaPatioDAO();
    MetaBalizaDAO metaBalizaDAO = new MetaBalizaDAO();
    MetaCorreiaDAO metaCorreiaDAO = new MetaCorreiaDAO();
    MetaMaquinaDoPatioDAO metaMaquinaDAO = new MetaMaquinaDoPatioDAO();
    MetaCarregadoraDeNaviosDAO metaCarregadoraDeNaviosDAO = new MetaCarregadoraDeNaviosDAO();
    MetaBercoDAO metaBercoDAO = new MetaBercoDAO();
    ClienteDAO clienteDAO = new ClienteDAO();
    MetaNavioDAO metaNavioDAO = new MetaNavioDAO();
    MetaUsinaDAO metaUsinaDAO = new MetaUsinaDAO();
    MetaFiltragemDAO metaFiltragemDAO = new MetaFiltragemDAO();
    private static List<TipoProduto> produtos = new ArrayList<TipoProduto>(); 
    private static List<TipoProduto> produtosCampanhas = new ArrayList<TipoProduto>();
    private static List<TipoItemDeControle> itensControle = new ArrayList<TipoItemDeControle>();    
    private static List<MetaFiltragem> listaDeFiltragens = new ArrayList<MetaFiltragem>();
     static {
         HibernateUtil.beginTransaction();         
    	 String str_date="01/01/2011";
         DateFormat formatter ;          
         formatter = new SimpleDateFormat("MM/dd/yyyy");
         Date data;
        try {
            data = (Date)formatter.parse(str_date);
            timeDefault= data.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
         
         
     }
    
    public static long timeDefault;//System.currentTimeMillis();
    
    
    
    /**
     * testCriarTipoProduto
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarTipoProduto() {
        try {
            TipoProdutoDAO dao = new TipoProdutoDAO();
            produtos.addAll(GeradorModeloUtilCenarioCompleto.criarTipoProduto());
            produtos = dao.salvar(produtos);
            dao.encerrarSessao();
            Assert.assertNotNull(produtos.get(0).getIdTipoProduto());            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * testCriarTipoProduto
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarTipoItemControle() {
        try {
            TipoItemDeControleDAO dao = new TipoItemDeControleDAO();
            itensControle.addAll(GeradorModeloUtilCenarioCompleto.criaListaTipoItemControle(timeDefault));
            dao.salvar(itensControle);
            GeradorModeloUtilCenarioCompleto.getListaTiposItensControle().addAll(itensControle);
            dao.encerrarSessao();
            Assert.assertNotNull(itensControle.get(0).getIdTipoItemDeControle());            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    
    
  
    
    
    /**
     * testCriarMetaEmbarque
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 23/08/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarMetaEmbarque() {
        try {
            
            TipoProdutoDAO daoTipoProduto = new TipoProdutoDAO();
            MetaInternaDAO  dao = new MetaInternaDAO();                 	
            List<TipoProduto> tipoProdutos = daoTipoProduto.buscarListaDeObjetos(new TipoProduto());
            List<MetaInterna> metas = new ArrayList<MetaInterna>();
            
            for (TipoProduto tp : tipoProdutos) {
                if (tp.getDescricaoTipoProduto().equals("PBF-STD")) metasPBF_STD(metas, tp);
                if (tp.getDescricaoTipoProduto().equals("PBF-MB45")) metasPBF_MB45(metas, tp);
                if (tp.getDescricaoTipoProduto().equals("PBF-HB")) metasPBF_HB(metas, tp);
                if (tp.getDescricaoTipoProduto().equals("PDR-MG")) metasPDR_MG(metas, tp);
                if (tp.getDescricaoTipoProduto().equals("PDR_MX")) metasPDR_MX(metas, tp);
                if (tp.getDescricaoTipoProduto().equals("PDR-HY")) metasPDR_HY(metas, tp);
            }
            
            dao.salvar(metas);
            dao.encerrarSessao();
            Assert.assertNotNull(produtos.get(0).getIdTipoProduto());            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void metasPBF_STD(List<MetaInterna> metas, TipoProduto tp) {        
            for (TipoItemDeControle tipo : itensControle) {
                if (tipo.getDescricaoTipoItemControle().equals("-6,3mm")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(1.8d);
                    meta.setLimiteSuperiorValorMetaInterna(1.8d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);       
                    
                    meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(1.3d);
                    meta.setLimiteSuperiorValorMetaInterna(1.3d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);       
                }
                
                if (tipo.getDescricaoTipoItemControle().equals("Relação 12,5/9")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(0.5d);
                    meta.setLimiteSuperiorValorMetaInterna(0.5d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        
                }
                
                if (tipo.getDescricaoTipoItemControle().equals("TB+6,3")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(93.4d);
                    meta.setLimiteSuperiorValorMetaInterna(93.4d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);               
                    
                    meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(93.6d);
                    meta.setLimiteSuperiorValorMetaInterna(93.6d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);               
                }
                
                if (tipo.getDescricaoTipoItemControle().equals("C 16,0")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(283d);
                    meta.setLimiteSuperiorValorMetaInterna(283d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);
                    
                    meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(290d);
                    meta.setLimiteSuperiorValorMetaInterna(290d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        

                }
                if (tipo.getDescricaoTipoItemControle().equals("-200c16")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(20d);
                    meta.setLimiteSuperiorValorMetaInterna(20d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        
                }
                if (tipo.getDescricaoTipoItemControle().equals("H20")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(2.12d);
                    meta.setLimiteSuperiorValorMetaInterna(2.12d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        
                    
                    meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(1d);
                    meta.setLimiteSuperiorValorMetaInterna(1d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);
                }
                if (tipo.getDescricaoTipoItemControle().equals("Fe")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(66.34d);
                    meta.setLimiteSuperiorValorMetaInterna(66.34d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        
                }
                if (tipo.getDescricaoTipoItemControle().equals("SiO2")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(2.4d);
                    meta.setLimiteSuperiorValorMetaInterna(2.4d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        
                }
                if (tipo.getDescricaoTipoItemControle().equals("CaO")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(1.7d);
                    meta.setLimiteSuperiorValorMetaInterna(1.7d);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        
                }
                if (tipo.getDescricaoTipoItemControle().equals("B2")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(0.8D);
                    meta.setLimiteSuperiorValorMetaInterna(0.8D);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        
                }
                if (tipo.getDescricaoTipoItemControle().equals("P")) {
                    MetaInterna meta = new MetaInterna();   
                    meta.setDtInicio(new Date(timeDefault));
                    meta.setDtFim((new Date(System.currentTimeMillis())));
                    meta.setLimiteInferiorValorMetaInterna(0.05D);
                    meta.setLimiteSuperiorValorMetaInterna(0.05D);
                    meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                    meta.setTipoItemDeControle(tipo);
                    meta.setTipoPelota(tp);
                    metas.add(meta);                        
                }
            }    
        
    }

    private void metasPBF_MB45(List<MetaInterna> metas, TipoProduto tp) {        
        for (TipoItemDeControle tipo : itensControle) {
            if (tipo.getDescricaoTipoItemControle().equals("-6,3mm")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(2d);
                meta.setLimiteSuperiorValorMetaInterna(2d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.3d);
                meta.setLimiteSuperiorValorMetaInterna(1.3d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("Relação 12,5/9")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.5d);
                meta.setLimiteSuperiorValorMetaInterna(0.5d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("TB+6,3")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.4d);
                meta.setLimiteSuperiorValorMetaInterna(93.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.6d);
                meta.setLimiteSuperiorValorMetaInterna(93.6d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("C 16,0")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(279d);
                meta.setLimiteSuperiorValorMetaInterna(279d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(288d);
                meta.setLimiteSuperiorValorMetaInterna(288d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        

            }
            if (tipo.getDescricaoTipoItemControle().equals("-200c16")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(20d);
                meta.setLimiteSuperiorValorMetaInterna(20d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("H20")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(2.43d);
                meta.setLimiteSuperiorValorMetaInterna(2.43d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1d);
                meta.setLimiteSuperiorValorMetaInterna(1d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
            }
            if (tipo.getDescricaoTipoItemControle().equals("Fe")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(67.08d);
                meta.setLimiteSuperiorValorMetaInterna(67.08d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("SiO2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(2.4d);
                meta.setLimiteSuperiorValorMetaInterna(2.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("B2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.45D);
                meta.setLimiteSuperiorValorMetaInterna(0.45D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("P")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.05D);
                meta.setLimiteSuperiorValorMetaInterna(0.05D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
        }    
    
}
    
    
    private void metasPBF_HB(List<MetaInterna> metas, TipoProduto tp) {        
        for (TipoItemDeControle tipo : itensControle) {
            if (tipo.getDescricaoTipoItemControle().equals("-6,3mm")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(2d);
                meta.setLimiteSuperiorValorMetaInterna(2d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.3d);
                meta.setLimiteSuperiorValorMetaInterna(1.3d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("Relação 12,5/9")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.5d);
                meta.setLimiteSuperiorValorMetaInterna(0.5d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("TB+6,3")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.3d);
                meta.setLimiteSuperiorValorMetaInterna(93.3d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.6d);
                meta.setLimiteSuperiorValorMetaInterna(93.6d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("C 16,0")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(264d);
                meta.setLimiteSuperiorValorMetaInterna(264d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(285d);
                meta.setLimiteSuperiorValorMetaInterna(285d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        

            }
            if (tipo.getDescricaoTipoItemControle().equals("-200c16")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(20d);
                meta.setLimiteSuperiorValorMetaInterna(20d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("H20")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(2.44d);
                meta.setLimiteSuperiorValorMetaInterna(2.44d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1d);
                meta.setLimiteSuperiorValorMetaInterna(1d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
            }
            if (tipo.getDescricaoTipoItemControle().equals("Fe")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(66.04d);
                meta.setLimiteSuperiorValorMetaInterna(66.04d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("SiO2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(2.4d);
                meta.setLimiteSuperiorValorMetaInterna(2.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("CaO")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(2D);
                meta.setLimiteSuperiorValorMetaInterna(2D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("B2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1D);
                meta.setLimiteSuperiorValorMetaInterna(1D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("P")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.05D);
                meta.setLimiteSuperiorValorMetaInterna(0.05D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
        }    
    
}
    
    private void metasPDR_MG(List<MetaInterna> metas, TipoProduto tp) {        
        for (TipoItemDeControle tipo : itensControle) {
            if (tipo.getDescricaoTipoItemControle().equals("-6,3mm")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.6d);
                meta.setLimiteSuperiorValorMetaInterna(1.6d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.2d);
                meta.setLimiteSuperiorValorMetaInterna(1.2d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("Relação 12,5/9")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.8d);
                meta.setLimiteSuperiorValorMetaInterna(0.8d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("TB+6,3")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.4d);
                meta.setLimiteSuperiorValorMetaInterna(93.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.6d);
                meta.setLimiteSuperiorValorMetaInterna(93.6d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("C 16,0")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(292d);
                meta.setLimiteSuperiorValorMetaInterna(292d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(290d);
                meta.setLimiteSuperiorValorMetaInterna(290d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        

            }
            if (tipo.getDescricaoTipoItemControle().equals("-200c16")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(20d);
                meta.setLimiteSuperiorValorMetaInterna(20d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("H20")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.79d);
                meta.setLimiteSuperiorValorMetaInterna(1.79d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.7d);
                meta.setLimiteSuperiorValorMetaInterna(0.7d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
            }
            if (tipo.getDescricaoTipoItemControle().equals("Fe")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(64.54d);
                meta.setLimiteSuperiorValorMetaInterna(64.54d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("SiO2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.4d);
                meta.setLimiteSuperiorValorMetaInterna(1.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("CaO")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.7D);
                meta.setLimiteSuperiorValorMetaInterna(0.7D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("MgO")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.2D);
                meta.setLimiteSuperiorValorMetaInterna(0.2D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("B2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.5D);
                meta.setLimiteSuperiorValorMetaInterna(0.5D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("P")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.05D);
                meta.setLimiteSuperiorValorMetaInterna(0.05D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
        }    
    
}
 
    private void metasPDR_MX(List<MetaInterna> metas, TipoProduto tp) {        
        for (TipoItemDeControle tipo : itensControle) {
            if (tipo.getDescricaoTipoItemControle().equals("-6,3mm")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.7d);
                meta.setLimiteSuperiorValorMetaInterna(1.7d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.2d);
                meta.setLimiteSuperiorValorMetaInterna(1.2d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("Relação 12,5/9")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.8d);
                meta.setLimiteSuperiorValorMetaInterna(0.8d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("TB+6,3")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.4d);
                meta.setLimiteSuperiorValorMetaInterna(93.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.6d);
                meta.setLimiteSuperiorValorMetaInterna(93.6d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("C 16,0")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(296d);
                meta.setLimiteSuperiorValorMetaInterna(296d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(300d);
                meta.setLimiteSuperiorValorMetaInterna(300d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        

            }
            if (tipo.getDescricaoTipoItemControle().equals("-200c16")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(20d);
                meta.setLimiteSuperiorValorMetaInterna(20d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("H20")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.79d);
                meta.setLimiteSuperiorValorMetaInterna(1.79d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.7d);
                meta.setLimiteSuperiorValorMetaInterna(0.7d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
            }
            if (tipo.getDescricaoTipoItemControle().equals("Fe")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(67.84d);
                meta.setLimiteSuperiorValorMetaInterna(67.84d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("SiO2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.4d);
                meta.setLimiteSuperiorValorMetaInterna(1.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("CaO")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.7D);
                meta.setLimiteSuperiorValorMetaInterna(0.7D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            
            if (tipo.getDescricaoTipoItemControle().equals("B2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.5D);
                meta.setLimiteSuperiorValorMetaInterna(0.5D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("P")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.05D);
                meta.setLimiteSuperiorValorMetaInterna(0.05D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
        }    
    
}
 
    private void metasPDR_HY(List<MetaInterna> metas, TipoProduto tp) {        
        for (TipoItemDeControle tipo : itensControle) {
            if (tipo.getDescricaoTipoItemControle().equals("-6,3mm")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(2.1d);
                meta.setLimiteSuperiorValorMetaInterna(2.1d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.2d);
                meta.setLimiteSuperiorValorMetaInterna(1.2d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);       
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("Relação 12,5/9")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.8d);
                meta.setLimiteSuperiorValorMetaInterna(0.8d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("TB+6,3")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.4d);
                meta.setLimiteSuperiorValorMetaInterna(93.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(93.6d);
                meta.setLimiteSuperiorValorMetaInterna(93.6d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);               
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("C 16,0")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(277d);
                meta.setLimiteSuperiorValorMetaInterna(277d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(300d);
                meta.setLimiteSuperiorValorMetaInterna(300d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        

            }
            if (tipo.getDescricaoTipoItemControle().equals("-200c16")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(20d);
                meta.setLimiteSuperiorValorMetaInterna(20d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("H20")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.79d);
                meta.setLimiteSuperiorValorMetaInterna(1.79d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
                
                meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.7d);
                meta.setLimiteSuperiorValorMetaInterna(0.7d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_PRODUCAO);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);
            }
            if (tipo.getDescricaoTipoItemControle().equals("Fe")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(67.77d);
                meta.setLimiteSuperiorValorMetaInterna(67.77d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("SiO2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(1.4d);
                meta.setLimiteSuperiorValorMetaInterna(1.4d);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            if (tipo.getDescricaoTipoItemControle().equals("CaO")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.7D);
                meta.setLimiteSuperiorValorMetaInterna(0.7D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            
            
            if (tipo.getDescricaoTipoItemControle().equals("B2")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.5D);
                meta.setLimiteSuperiorValorMetaInterna(0.5D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
            if (tipo.getDescricaoTipoItemControle().equals("P")) {
                MetaInterna meta = new MetaInterna();   
                meta.setDtInicio(new Date(timeDefault));
                meta.setDtFim((new Date(System.currentTimeMillis())));
                meta.setLimiteInferiorValorMetaInterna(0.05D);
                meta.setLimiteSuperiorValorMetaInterna(0.05D);
                meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
                meta.setTipoItemDeControle(tipo);
                meta.setTipoPelota(tp);
                metas.add(meta);                        
            }
        }    
    
}
    /**
     * testCriarPlanta
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarPlanta() {
        planta = new Planta();
        planta.setIdUser(1l);
        planta.setNomePlanta("PLANTA VERSÃO BETA");
        planta.setDtInicio(new Date(timeDefault));
        planta.setDtInsert(new Date(timeDefault));
        try {
            planta = plantaDAO.salvaPlanta(planta);
            plantaDAO.encerrarSessao();
            Assert.assertNotNull(planta.getIdPlanta());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    

    public void testCriarSituacaoPatio() {
       planta = new Planta();
       planta.setNomePlanta("PLANTA VERSÃO BETA");
       planta = plantaDAO.buscarPorObjeto(planta);
        
       plano = new PlanoEmpilhamentoRecuperacao();
       plano.setDtInicio(new Date(timeDefault));
       plano.setDtInsert(new Date(timeDefault));
       plano.setEhOficial(Boolean.TRUE);
       plano.setIdUser(1l);
        
       SituacaoPatio situacao = new SituacaoPatio();       
       situacao.setPlanta(planta);       
       situacao.setDtInicio(new Date(timeDefault));
       situacao.setDtInsert(new Date(timeDefault));
       situacao.setIdUser(1l);
        
        
       PlanoEmpilhamentoRecuperacaoDAO dao = new PlanoEmpilhamentoRecuperacaoDAO();
       plano = dao.salvar(plano);
       
       plano.addSituacaoPatio(situacao);
        
       SituacaoPatioDAO daoS = new SituacaoPatioDAO();
       daoS.salvar(situacao);
        
    }

    
    /**
     * testCriarPier
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarPier() {
        try {
            planta = new Planta();
            planta.setNomePlanta("PLANTA VERSÃO BETA");
            planta = plantaDAO.buscarPorObjeto(planta);
            
            pier = GeradorModeloUtilCenarioCompleto.criarPier(timeDefault);
            
            planta.addMetaPier(pier);
            
            metaPierDAO.salvaMetaPier(pier);
            metaPierDAO.encerrarSessao();
            plantaDAO.encerrarSessao();
            
            Assert.assertNotNull(pier.getIdPier());
            Assert.assertNotNull(pier.retornaStatusHorario(new Date(timeDefault)).getIdPier());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * testCriarCarregadoraPier
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarCarregadoraPier() {
        try {
            pier = new MetaPier();
            pier.setNomePier(GeradorModeloUtilCenarioCompleto.NOME_PIER);
            pier = metaPierDAO.buscarPorObjeto(pier);

            List<MetaCarregadoraDeNavios> listaDeCarregadoraDeNavios = GeradorModeloUtilCenarioCompleto
                            .criarCarregadoraNavio(timeDefault);

            pier.addMetaCarregadoraNavio(listaDeCarregadoraDeNavios);

            metaCarregadoraDeNaviosDAO.salvaMetaCarregadoraDeNavios(listaDeCarregadoraDeNavios);
            metaCarregadoraDeNaviosDAO.encerrarSessao();
            Assert.assertNotNull(pier.getIdPier());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * testCriarBercos
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarBercos() {
        try {
            pier = new MetaPier();
            pier.setNomePier(GeradorModeloUtilCenarioCompleto.NOME_PIER);
            pier = metaPierDAO.buscarPorObjeto(pier);

            List<MetaBerco> listaBercos = GeradorModeloUtilCenarioCompleto.criarBercos(timeDefault);

            pier.addMetaBerco(listaBercos);

            metaBercoDAO.salvaMetaBerco(listaBercos);
            metaBercoDAO.encerrarSessao();
            Assert.assertNotNull(pier.getIdPier());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * testCriarPatios
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarPatios() {
        try {
            planta = new Planta();
            planta.setNomePlanta("PLANTA VERSÃO BETA");
            planta = plantaDAO.buscarPorObjeto(planta);
            
            List<MetaPatio> result = GeradorModeloUtilCenarioCompleto.criaPatios(timeDefault);
            
            planta.addMetaPatio(result);
            
            result = metaPatioDAO.salvaMetaPatio(result);
            metaPatioDAO.encerrarSessao();
            plantaDAO.encerrarSessao();
            
            Assert.assertNotNull(result.get(0).getIdPatio());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * testCriarBalizas
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarBalizas() {
        try {
            List<MetaPatio> result = metaPatioDAO.findAll(MetaPatio.class);
            List<MetaBaliza> balizas = new ArrayList<MetaBaliza>();
            for (MetaPatio patio : result) {
            	balizas.addAll(GeradorModeloUtilCenarioCompleto.criaListaDeBalizas(130, patio, timeDefault));                                              
            }   
            metaBalizaDAO.salvaListaMetaBalizas(balizas);
            metaBalizaDAO.encerrarSessao();            
            Assert.assertNotNull(result.get(0).getIdPatio());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * testCriarMaquinaDoPatios
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarMaquinaDoPatios() {
        try {
            List<MetaPatio> result = metaPatioDAO.findAll(MetaPatio.class);
            for (MetaPatio patio : result) {
              MetaBaliza baliza = new MetaBaliza();
              baliza.setMetaPatio(patio);
              List<MetaBaliza> itens = metaBalizaDAO.buscarListaDeObjetos(baliza);
              patio.addBaliza(itens);
            }            
            //metaBalizaDAO.encerrarSessao();
            List<MetaMaquinaDoPatio> maquinas = new ArrayList<MetaMaquinaDoPatio>();
            maquinas.add(GeradorModeloUtilCenarioCompleto
                            .criaPaCarregadeira("Pá Carregadeira 1", result.get(0), timeDefault));

            maquinas.add(GeradorModeloUtilCenarioCompleto
                            .criaPaCarregadeira("Pá Carregadeira 2", result.get(1), timeDefault));
            
            metaPatioDAO.encerrarSessao();
            metaBalizaDAO.encerrarSessao();
            maquinas = metaMaquinaDAO.salvaMetaMaquinaDoPatio(maquinas);
            metaMaquinaDAO.encerrarSessao();            
            Assert.assertNotNull(result.get(0).getIdPatio());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * testCriarCorreias
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarCorreias() {
        try {
            planta = new Planta();
            planta.setNomePlanta("PLANTA VERSÃO BETA");
            planta = plantaDAO.buscarPorObjeto(planta);
            
            
            List<MetaPatio> result = metaPatioDAO.findAll(MetaPatio.class);
            for (MetaPatio patio : result) {
                MetaBaliza baliza = new MetaBaliza();
                baliza.setMetaPatio(patio);
                List<MetaBaliza> itens = metaBalizaDAO.buscarListaDeObjetos(baliza);
                patio.addBaliza(itens);
              }
            List<MetaCorreia> listaDeCorreias = GeradorModeloUtilCenarioCompleto.criaListaCorreias(result, timeDefault);
            
            planta.addMetaCorreia(listaDeCorreias);
            metaPatioDAO.encerrarSessao();
            metaBalizaDAO.encerrarSessao();      
            listaDeCorreias = metaCorreiaDAO.salvaMetaCorreia(listaDeCorreias);            
            metaCorreiaDAO.encerrarSessao();            
            plantaDAO.encerrarSessao();
            
            Assert.assertNotNull(listaDeCorreias.get(0).getIdCorreia());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * testCriarMaquinasCorreias
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarMaquinasCorreias() {
        try {
            List<MetaCorreia> listaDeCorreias = metaCorreiaDAO.findAll(MetaCorreia.class);
            
            for (MetaCorreia correia : listaDeCorreias) {
                
            	MetaBaliza baliza = new MetaBaliza();
                if (correia.getMetaPatioInferior() != null) {
                	baliza.setMetaPatio(correia.getMetaPatioInferior());
                	List<MetaBaliza> itens = metaBalizaDAO.buscarListaDeObjetos(baliza);
                	correia.getMetaPatioInferior().addBaliza(itens);
                }	
                if (correia.getMetaPatioSuperior() != null) {
                	baliza.setMetaPatio(correia.getMetaPatioSuperior());
                	List<MetaBaliza> itens = metaBalizaDAO.buscarListaDeObjetos(baliza);
                	correia.getMetaPatioSuperior().addBaliza(itens);
                }
              }
            
            List<MetaMaquinaDoPatio> maquinas = GeradorModeloUtilCenarioCompleto.criarListaMaquinasCorrerias(timeDefault,
                            listaDeCorreias);            
            metaBalizaDAO.encerrarSessao();
            maquinas = metaMaquinaDAO.salvaMetaMaquinaDoPatio(maquinas);
            metaMaquinaDAO.encerrarSessao();
            metaCorreiaDAO.encerrarSessao();
            Assert.assertNotNull(maquinas.get(0).getIdMaquina());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    
    public void testCriarFiltragem() {
        try {
            planta = new Planta();
            planta.setNomePlanta("PLANTA VERSÃO BETA");
            planta = plantaDAO.buscarPorObjeto(planta);
            
            
            List<MetaCorreia> listaDeCorreias = metaCorreiaDAO.findAll(MetaCorreia.class);
            
            MetaCorreia metaCorreia = null;
            for (MetaCorreia correia : listaDeCorreias) {
                if (correia.getNomeCorreia().equals(GeradorModeloUtilCenarioCompleto.NOME_CORREIA_1)) {
                    metaCorreia = correia;
                    break;
                }
            }    
            
            
            MetaFiltragem filtragem = GeradorModeloUtilCenarioCompleto.criaFiltragem("Filtragem 1", timeDefault);
            listaDeFiltragens.add(filtragem);
            metaCorreia.addMetaFiltragem(filtragem);
            planta.addMetaFiltragem(filtragem);
            
            filtragem = GeradorModeloUtilCenarioCompleto.criaFiltragem("Filtragem 2", timeDefault);
            listaDeFiltragens.add(filtragem);
            metaCorreia.addMetaFiltragem(filtragem);
            planta.addMetaFiltragem(filtragem);
            
            listaDeFiltragens = metaFiltragemDAO.salvaMetaFiltragem(listaDeFiltragens);
            
            metaFiltragemDAO.encerrarSessao();            
            plantaDAO.encerrarSessao();
            
            Assert.assertNotNull(listaDeFiltragens.get(0).getIdFiltragem());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    
    /**
     * testCriarUsinas
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     */
    public void testCriarUsinas() {
        try {
            planta = new Planta();
            planta.setNomePlanta("PLANTA VERSÃO BETA");
            planta = plantaDAO.buscarPorObjeto(planta);
            
            
            List<MetaCorreia> listaDeCorreias = metaCorreiaDAO.findAll(MetaCorreia.class);
            
            List<MetaPatio> result = metaPatioDAO.findAll(MetaPatio.class);
            List<MetaUsina> listaDeUsinas = GeradorModeloUtilCenarioCompleto.criaListaUsinas(timeDefault, result,listaDeFiltragens,listaDeCorreias);
            
            planta.addMetaUsina(listaDeUsinas);
            
            listaDeUsinas = metaUsinaDAO.salvaMetaUsina(listaDeUsinas);
            
            metaUsinaDAO.encerrarSessao();
            metaPatioDAO.encerrarSessao();
            plantaDAO.encerrarSessao();
            
            Assert.assertNotNull(listaDeUsinas.get(0).getIdUsina());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    
    public void testCriarCampanhas() {
        try {
            CampanhaDAO dao = new CampanhaDAO();
            TipoProduto tipoDeProdutoDaUsina = null;
            TipoProduto tipoDePelletPFL = null;
            TipoProduto tipoDeScreening = null;
            for (TipoProduto tipo : produtos) {
                if (tipo.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA) && tipo.getCodigoInsumoTipoProduto() != null) {
                    tipoDeProdutoDaUsina = tipo;
                    tipoDePelletPFL  = tipo.getCodigoInsumoTipoProduto(); 
                    produtosCampanhas.add(tipoDeProdutoDaUsina);                     
                } else if (tipo.getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_SCREENING)) {
                    tipoDeScreening = tipo;
                    produtosCampanhas.add(tipoDeScreening);
                }
            }            
            List<Campanha> listaCampanhas = new ArrayList<Campanha>();
            List<MetaUsina> listaDeUsinas = metaUsinaDAO.findAll(MetaUsina.class);

            
            for (MetaUsina metaUsina : listaDeUsinas) {
            	Campanha campanha = null;
               if (metaUsina.getNomeUsina().equals(GeradorModeloUtilCenarioCompleto.NOME_USINA_3)) {
            	   campanha = GeradorModeloUtilCenarioCompleto.criaCampanha(tipoDeProdutoDaUsina, tipoDePelletPFL, tipoDeScreening, metaUsina,
            	                   timeDefault,metaUsina.getCodigoFaseProcessoUsina(),650030020L,650030010L,650060010L,"DD","HH","CC","CC",150L,150L);
            	   campanha.setFinalizada(Boolean.FALSE);
               } else {
            	   campanha = GeradorModeloUtilCenarioCompleto.criaCampanha(tipoDeProdutoDaUsina, tipoDePelletPFL,
            	                   tipoDeScreening, metaUsina,timeDefault,metaUsina.getCodigoFaseProcessoUsina(),420030020L,420030010L,420060000L,"DD","HH","CC","CC",150L,150L);
            	   campanha.setFinalizada(Boolean.FALSE);
               }
                metaUsina.addCampanha(campanha);
                listaCampanhas.add(campanha);
            }
            listaCampanhas = dao.salvaCampanha(listaCampanhas);
            dao.encerrarSessao();
            metaUsinaDAO.encerrarSessao();
            Assert.assertNotNull(listaCampanhas.get(0).getIdCampanha());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   
   /* *//**
     * testCriarClientes
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     *//*
    public void testCriarClientes() {
        try {
            List<Cliente> result = GeradorModeloUtilCenarioCompleto.criaCliente();            
            result = clienteDAO.salvar(result);
            clienteDAO.encerrarSessao();            
            Assert.assertNotNull(result.get(0).getIdCliente());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

    
    /**
     * testCriarNavios
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 09/06/2010
     * @see
     * @param 
     * @return void
     *//*
    public void testCriarNavios() {
        try {
            List<Cliente> result = clienteDAO.findAll(Cliente.class);                        
            List<MetaNavio> navios = GeradorModeloUtilCenarioCompleto.criaListaDeNavios(result, timeDefault,produtosCampanhas);            
            
            navios = metaNavioDAO.salvar(navios);
            clienteDAO.encerrarSessao();
            metaNavioDAO.encerrarSessao();
            Assert.assertNotNull(navios.get(0).getIdMetaNavio());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

    /**
     * testCriarPilhas
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 10/06/2010
     * @see
     * @param 
     * @return void
     *//*
    public void testCriarPilhas() {
        try {
            PilhaDAO dao = new PilhaDAO();
            List<MetaPatio> result = metaPatioDAO.findAll(MetaPatio.class);                        
            List<Cliente> result2 = clienteDAO.findAll(Cliente.class);
                            
            List<Pilha>  pilhas = GeradorModeloUtilCenarioCompleto.criaListaDePilhas(result, result2.get(0), produtos, timeDefault);            
            pilhas = dao.salvar(pilhas);
            metaPatioDAO.encerrarSessao();
            clienteDAO.encerrarSessao();
            dao.encerrarSessao();
            Assert.assertNotNull(pilhas.get(0).getIdPilha());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }*/
    
    public void testCriarIntegracao() {
        try {
            IntegracaoParametrosDAO dao = new IntegracaoParametrosDAO();            
            for (int i = 1; i < 10 ; i ++) {
                IntegracaoParametros param = new IntegracaoParametros();
                param.setDataUltimaLeitura(new Date(timeDefault));
                param.setDataUltimaIntegracao(new Date(timeDefault));
                param.setAtualizacaoCampoIntegracao("FALSE");
                param.setIdSistema(new Long(i));
                dao.salvar(param);                
            }               
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void testCriarFaixaAmostragem() {
        try {
            FaixaAmostragemFrequenciaDAO dao = new FaixaAmostragemFrequenciaDAO();            
            TabelaAmostragemFrequenciaDAO dao1 = new TabelaAmostragemFrequenciaDAO(); 
            List<FaixaAmostragemFrequencia> listaFaixaPellet = new ArrayList<FaixaAmostragemFrequencia>();
            List<FaixaAmostragemFrequencia> listaFaixaPelota = new ArrayList<FaixaAmostragemFrequencia>();
            GeradorModeloUtilCenarioCompleto.gerarListaFaixaAmostragem(listaFaixaPelota, listaFaixaPellet);
            listaFaixaPelota = dao.salvar(listaFaixaPelota);
            listaFaixaPellet = dao.salvar(listaFaixaPellet);
            
            TabelaAmostragemFrequencia tabela =   GeradorModeloUtilCenarioCompleto.gerarTabelaAmostragemFrequencia(listaFaixaPelota, listaFaixaPellet);
            dao1.salvar(tabela);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    @Override
    public void tearDown() {
    	HibernateUtil.commitTransaction();
    }
    
    
    
}
