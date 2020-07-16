package testControladorRelatorio;

import java.util.List;

import junit.framework.TestCase;

import com.hdntec.gestao.domain.relatorio.CampoRelatorio;
import com.hdntec.gestao.domain.relatorio.PadraoCampo;
import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.enums.EnumCamposRelatorio;
import com.hdntec.gestao.integracao.relatorio.controladores.ControladorGestaoRelatorio;
import com.hdntec.gestao.integracao.relatorio.controladores.IControladorGestaoRelatorio;

/**
 * 
 * <P><B>Description :</B><BR>
 * General PopularDadosRelatorioTest
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 24/06/2009
 * @version $Revision: 1.1 $
 */
public class PopularDadosRelatorioTest extends TestCase{
	
	IControladorGestaoRelatorio controladorGestaoRelatorio;

	
	public PopularDadosRelatorioTest(String testName) {
		super(testName);
		controladorGestaoRelatorio = lookUp();
	}
    
    /**
     * 
     * testAtualizarCamposRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/06/2009
     * @see
     * @return Returns the void.
     */
    public void testAtualizarCamposRelatorio() {
    	try {
    		controladorGestaoRelatorio.atualizarCamposRelatorio();
    		
    		List<CampoRelatorio> camposRecuperado = controladorGestaoRelatorio.buscarCampos();
    		
    		assertFalse(camposRecuperado.isEmpty());
    		
    	} catch (Exception e) {
    		fail("Ocorreu exceção :  " + e.getMessage() + "\n");
    		e.printStackTrace();
    	}
    }
    
    
	/**
	 * 
	 * testSalvarPadraoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @return Returns the void.
	 */
	public void testSalvarPadraoRelatorio() {
		try {
			List<CampoRelatorio> camposRelatorio = controladorGestaoRelatorio.buscarCampos(); 
			assertFalse(camposRelatorio.isEmpty());
			int totalCampos = camposRelatorio.size();
			assertTrue(totalCampos>5);
			
			PadraoRelatorio padrao = new PadraoRelatorio();
			padrao.setNomePadraoRelatorio("PadraoRelatorioGraficoEDados");
			 
			int i = 1;
			for (CampoRelatorio campo : camposRelatorio) {
				if (i<15) {
					PadraoCampo padraoCampo = new PadraoCampo();
					padraoCampo.setCampoRelatorio(campo);
					padraoCampo.setPadraoRelatorio(padrao);
	
					if (campo.getNomeCampo().equals(EnumCamposRelatorio.IMAGEM_SITUACAO.toString()) ) {
						padraoCampo.setOrdem(0);
					}
					else {
						padraoCampo.setOrdem(i++);
					}
					padrao.addPadraoCampo(padraoCampo);
				}
			}
			
			PadraoRelatorio padraoSalvo = controladorGestaoRelatorio.salvarPadraoRelatorio(padrao);
			assertNotNull(padraoSalvo.getIdPadraoRelatorio());
			
			PadraoRelatorio padraoRecuperado = controladorGestaoRelatorio.buscarPadraoRelatorioPorId(padraoSalvo.getIdPadraoRelatorio());
			
			assertNotNull(padraoRecuperado);
			assertEquals(15,padraoRecuperado.getListaDeCampos().size());
			
			
			padrao = new PadraoRelatorio();
			padrao.setNomePadraoRelatorio("PadraoSemGrafico");
			 
			i = 1;
			for (CampoRelatorio campo : camposRelatorio) {
				if (i<=10) {
					if (!campo.getNomeCampo().equals(EnumCamposRelatorio.IMAGEM_SITUACAO.toString()) ) {
						PadraoCampo padraoCampo = new PadraoCampo();
						padraoCampo.setCampoRelatorio(campo);
						padraoCampo.setPadraoRelatorio(padrao);
						padraoCampo.setOrdem(i++);
						padrao.addPadraoCampo(padraoCampo);
					}
				}
			}
			
			padraoSalvo = controladorGestaoRelatorio.salvarPadraoRelatorio(padrao);
			assertNotNull(padraoSalvo.getIdPadraoRelatorio());
			
			padraoRecuperado = controladorGestaoRelatorio.buscarPadraoRelatorioPorId(padraoSalvo.getIdPadraoRelatorio());
			assertNotNull(padraoRecuperado);
			assertEquals(10,padraoRecuperado.getListaDeCampos().size());
			
			padrao = new PadraoRelatorio();
			padrao.setNomePadraoRelatorio("PadraoSoGrafico");
			
			for (CampoRelatorio campo : camposRelatorio) {
				if (campo.getNomeCampo().equals(EnumCamposRelatorio.IMAGEM_SITUACAO.toString()) ) {
					PadraoCampo padraoCampo = new PadraoCampo();
					padraoCampo.setCampoRelatorio(campo);
					padraoCampo.setPadraoRelatorio(padrao);
					padraoCampo.setOrdem(1);
					padrao.addPadraoCampo(padraoCampo);
					break;
				}
			}
			
			padraoSalvo = controladorGestaoRelatorio.salvarPadraoRelatorio(padrao);
			assertNotNull(padraoSalvo.getIdPadraoRelatorio());
			
		} catch (Exception e) {
			fail("Ocorreu exceção :  " + e.getMessage() + "\n");
            e.printStackTrace();
		}
	}
    
    // método para realizar lookup dos serviços remotamente
    private IControladorGestaoRelatorio lookUp() {
        try {
            return  new ControladorGestaoRelatorio(); //(IControladorGestaoRelatorio) ServiceLocator.getRemote("bs/ControladorGestaoRelatorio", IControladorGestaoRelatorio.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
