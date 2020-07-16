package testControladorRelatorio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.relatorio.FaixaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.ItemAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.TabelaAmostragemFrequencia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;

/**
 * 
 * <P><B>Description :</B><BR>
 * General TabelaAmostragemFrequenciaTest
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 28/08/2009
 * @version $Revision: 1.1 $
 */
public class TabelaAmostragemFrequenciaTest extends TestCase{
	
	   //Constantes que define a Norma de Amostragem e frequencia cadastrada
	public static final String CODIGO_NORMA = "U-DEO - 0 - Q 05";
	public static final int REVISAO_NORMA = 1;
	public static final String CLASSIFICACAO_NORMA = "Reservada";
	public static final String DATA_NORMA = "12/11/2008";
	private IControladorModelo controladorModelo;
	
	/**
	 * 
	 * @param testName
	 */
	public TabelaAmostragemFrequenciaTest(String testName) {
		super(testName);
		controladorModelo = lookUp();
	}
	
	public void testGravarAmostragemFaixa() {
		try {

			gerarTabelaAmostragemFrequencia();
			TabelaAmostragemFrequencia tabelaRec = controladorModelo.recuperarTabelaAmostragemAtual();
			
			assertNotNull(tabelaRec);
			 
			assertFalse(tabelaRec.getListaItens().isEmpty());

		}
		catch (Exception e) {
 			e.printStackTrace();
			fail("Ocorreu exceção :  " + e.getMessage() + "\n");
		}
	}
	
	   /**
	    * 
	    * gerarTabelaAmostragemFrequencia
	    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	    * @since 28/08/2009
	    * @see
	    * @param controladorModelo
	    * @throws ErroSistemicoException
	    * @throws ValidacaoCampoException
	    * @return Returns the void.
	    */
	   private void gerarTabelaAmostragemFrequencia()
	   throws ErroSistemicoException, ValidacaoCampoException {

		   //Gravando as Faixas
		   List<FaixaAmostragemFrequencia> listaFaixaPelota = new ArrayList<FaixaAmostragemFrequencia>();
		   List<FaixaAmostragemFrequencia> listaFaixaPellet = new ArrayList<FaixaAmostragemFrequencia>();
		   FaixaAmostragemFrequencia faixaFinal;

		   gravaListaFaixaPelota(listaFaixaPelota);
		   faixaFinal = listaFaixaPelota.get(listaFaixaPelota.size()-1);
		   gravaListaFaixaPellet(listaFaixaPellet);
		   listaFaixaPellet.add(faixaFinal);

		   //Gravando a tabela
		   TabelaAmostragemFrequencia tabela = new TabelaAmostragemFrequencia();
		   tabela.setDtInsert(new Date());
		   Calendar calendar = Calendar.getInstance();
		   calendar.set(2008, 11, 12, 0, 0, 0);
		   tabela.setData(calendar.getTime());
		   tabela.setCodigo(CODIGO_NORMA);
		   tabela.setRevisao(REVISAO_NORMA);
		   tabela.setClassificacao(CLASSIFICACAO_NORMA);
		   ItemAmostragemFrequencia itemA = popularItemPelotaPDR(listaFaixaPelota);
		   itemA.setTabela(tabela);
		   tabela.addItemTabela(itemA);
		   ItemAmostragemFrequencia itemB = popularItemPelotaPBF(listaFaixaPelota);
		   itemB.setTabela(tabela);
		   tabela.addItemTabela(itemB);
		   ItemAmostragemFrequencia itemC = popularItemPelletFeed(listaFaixaPellet);
		   itemC.setTabela(tabela);
		   tabela.addItemTabela(itemC);
		   ItemAmostragemFrequencia itemD = popularItemSinterFeed(listaFaixaPellet);
		   itemD.setTabela(tabela);
		   tabela.addItemTabela(itemD);

		   controladorModelo.salvarTabelaAmostragemFrequencia(tabela);
	   }
	   /**
	    * 
	    * gravaListaFaixaPelota
	    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	    * @since 28/08/2009
	    * @see
	    * @param listaFaixaPelota
	    * @param controladorModelo
	    * @throws ErroSistemicoException
	    * @return Returns the void.
	    */
	   private void gravaListaFaixaPelota(List<FaixaAmostragemFrequencia> listaFaixaPelota) throws ErroSistemicoException {

		   //Primeira Faixa
		   FaixaAmostragemFrequencia faixa = new FaixaAmostragemFrequencia();
		   faixa.setDtInsert(new Date());
		   faixa.setFaixaTonelagemInicial(0.00);
		   faixa.setFaixaTonelagemFinal(29999.99);
		   faixa.setIncremento(400);
		   faixa.setGranulometria(1600);
		   faixa.setTamboramento(6400);
		   faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		   listaFaixaPelota.add(faixa);

		   //Segunda Faixa
		   faixa = new FaixaAmostragemFrequencia();
		   faixa.setDtInsert(new Date());
		   faixa.setFaixaTonelagemInicial(30000.00);
		   faixa.setFaixaTonelagemFinal(49999.99);
		   faixa.setIncremento(500);
		   faixa.setGranulometria(2000);
		   faixa.setTamboramento(8000);
		   faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		   listaFaixaPelota.add(faixa);

		   //Terceira Faixa
		   faixa = new FaixaAmostragemFrequencia();
		   faixa.setDtInsert(new Date());
		   faixa.setFaixaTonelagemInicial(50000.00);
		   faixa.setFaixaTonelagemFinal(100000.00);
		   faixa.setIncremento(750);
		   faixa.setGranulometria(3000);
		   faixa.setTamboramento(12000);
		   faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		   listaFaixaPelota.add(faixa);

		   //Quarta Faixa
		   faixa = new FaixaAmostragemFrequencia();
		   faixa.setDtInsert(new Date());
		   faixa.setFaixaTonelagemInicial(100000.01);
		   faixa.setFaixaTonelagemFinal(999999999999.99);
		   faixa.setIncremento(1000);
		   faixa.setGranulometria(4000);
		   faixa.setTamboramento(16000);
		   faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		   listaFaixaPelota.add(faixa);
	   }

	   /**
	    * 
	    * gravaListaFaixaPellet
	    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	    * @since 28/08/2009
	    * @see
	    * @param listaFaixaPellet
	    * @param controladorModelo
	    * @throws ErroSistemicoException
	    * @return Returns the void.
	    */
	   private void gravaListaFaixaPellet(List<FaixaAmostragemFrequencia> listaFaixaPellet) throws ErroSistemicoException {

		   //Primeira Faixa
		   FaixaAmostragemFrequencia faixa = new FaixaAmostragemFrequencia();
		   faixa.setDtInsert(new Date());
		   faixa.setFaixaTonelagemInicial(2000.00);
		   faixa.setFaixaTonelagemFinal(4999.99);
		   faixa.setIncremento(125);
		   faixa.setGranulometria(500);
		   faixa.setTamboramento(2000);
		   faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		   listaFaixaPellet.add(faixa);

		   //Segunda Faixa
		   faixa = new FaixaAmostragemFrequencia();
		   faixa.setDtInsert(new Date());
		   faixa.setFaixaTonelagemInicial(5000.00);
		   faixa.setFaixaTonelagemFinal(29999.99);
		   faixa.setIncremento(250);
		   faixa.setGranulometria(1000);
		   faixa.setTamboramento(4000);
		   faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		   listaFaixaPellet.add(faixa);

		   //Terceira Faixa
		   faixa = new FaixaAmostragemFrequencia();
		   faixa.setDtInsert(new Date());
		   faixa.setFaixaTonelagemInicial(30000.00);
		   faixa.setFaixaTonelagemFinal(44999.99);
		   faixa.setIncremento(500);
		   faixa.setGranulometria(2000);
		   faixa.setTamboramento(8000);
		   faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		   listaFaixaPellet.add(faixa);

		   //Quarta Faixa
		   faixa = new FaixaAmostragemFrequencia();
		   faixa.setDtInsert(new Date());
		   faixa.setFaixaTonelagemInicial(45000.00);
		   faixa.setFaixaTonelagemFinal(100000.00);
		   faixa.setIncremento(750);
		   faixa.setGranulometria(3000);
		   faixa.setTamboramento(12000);
		   faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		   listaFaixaPellet.add(faixa);

	   }

	   /**
	    * 
	    * popularItemPelotaPDR
	    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	    * @since 28/08/2009
	    * @see
	    * @param listaFaixaPelota
	    * @return
	    * @return Returns the ItemAmostragemFrequencia.
	    */
	   private ItemAmostragemFrequencia popularItemPelotaPDR(List<FaixaAmostragemFrequencia> listaFaixaPelota) {

		   ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
		   item.setDtInsert(new Date());
		   item.setCodigoFamiliaTipoProduto("PDR");
		   item.setTipoProduto(TipoDeProdutoEnum.PELOTA);
		   item.setListaFaixas(listaFaixaPelota);

		   return item;
	   }
	   /**
	    * 
	    * popularItemPelotaPBF
	    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	    * @since 28/08/2009
	    * @see
	    * @param listaFaixaPelota
	    * @return
	    * @return Returns the ItemAmostragemFrequencia.
	    */
	   private ItemAmostragemFrequencia popularItemPelotaPBF(List<FaixaAmostragemFrequencia> listaFaixaPelota) {

		   ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
		   item.setDtInsert(new Date());
		   item.setCodigoFamiliaTipoProduto("PBF");
		   item.setTipoProduto(TipoDeProdutoEnum.PELOTA);
		   item.setListaFaixas(listaFaixaPelota);

		   return item;
	   }
	   /**
	    * 
	    * popularItemPelletFeed
	    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	    * @since 28/08/2009
	    * @see
	    * @param listaFaixaPellet
	    * @return
	    * @return Returns the ItemAmostragemFrequencia.
	    */
	   private ItemAmostragemFrequencia popularItemPelletFeed(List<FaixaAmostragemFrequencia> listaFaixaPellet) {

		   ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
		   item.setDtInsert(new Date());
		   item.setCodigoFamiliaTipoProduto("");
		   item.setTipoProduto(TipoDeProdutoEnum.PELLET_FEED);
		   item.setListaFaixas(listaFaixaPellet);

		   return item;
	   }
	   /**
	    * 
	    * popularItemSinterFeed
	    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	    * @since 28/08/2009
	    * @see
	    * @param listaFaixaPellet
	    * @return
	    * @return Returns the ItemAmostragemFrequencia.
	    */
	   private ItemAmostragemFrequencia popularItemSinterFeed(List<FaixaAmostragemFrequencia> listaFaixaPellet) {

		   ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
		   item.setDtInsert(new Date());
		   item.setCodigoFamiliaTipoProduto("");
		   item.setTipoProduto(TipoDeProdutoEnum.SINTER_FEED);
		   item.setListaFaixas(listaFaixaPellet);

		   return item;
	   }	
    // método para realizar lookup dos serviços remotamente
    private IControladorModelo lookUp() {
        try {
            return null;//(IControladorModelo) ServiceLocator.getRemote("bs/ControladorModelo", IControladorModelo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
