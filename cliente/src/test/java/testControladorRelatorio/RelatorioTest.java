package testControladorRelatorio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.hdntec.gestao.domain.relatorio.CampoRelatorio;
import com.hdntec.gestao.domain.relatorio.PadraoCampo;
import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.domain.relatorio.enums.EnumTipoRelatorio;
import com.hdntec.gestao.integracao.relatorio.controladores.IControladorGestaoRelatorio;

public class RelatorioTest extends TestCase {

	IControladorGestaoRelatorio controladorGestaoRelatorio;

	
	public RelatorioTest(String testName) {
		super(testName);
		controladorGestaoRelatorio = lookUp();
	}
	
	/**
	 * 
	 * testSalvaCampoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 * @see
	 * @throws Exception
	 * @return Returns the void.
	 */
	public void testSalvarCampoRelatorio() {
		try {
			CampoRelatorio campo = new CampoRelatorio();
			campo.setNomeCampo("CampoTeste");
			CampoRelatorio campoSalvo = controladorGestaoRelatorio.salvarCampoRelatorio(campo);
			CampoRelatorio campoRecuperado = controladorGestaoRelatorio.buscarCampoRelatorioPorId(
					campoSalvo.getIdCampo());
			
			assertNotNull(campoRecuperado);
			assertTrue(campoRecuperado.getIdCampo()>0);
			assertEquals("CampoTeste", campoRecuperado.getNomeCampo());
			
			controladorGestaoRelatorio.removerCampoRelatorio(campoRecuperado);
			
		} catch (Exception e) {
			fail("Ocorreu exceção :  " + e.getMessage() + "\n");
            e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * testAlterarCampoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 * @see
	 * @throws Exception
	 * @return Returns the void.
	 */
	public void testAlterarCampoRelatorio() {
		try {
			CampoRelatorio campo = new CampoRelatorio();
			campo.setNomeCampo("CampoTeste");
			CampoRelatorio campoSalvo = controladorGestaoRelatorio.salvarCampoRelatorio(campo);
			
			campo = controladorGestaoRelatorio.buscarCampoRelatorioPorId(campoSalvo.getIdCampo());
			
			String nomeCampoNovo = "Nome alterado";
			campo.setNomeCampo(nomeCampoNovo);
			campo = controladorGestaoRelatorio.salvarCampoRelatorio(campo);

			CampoRelatorio campoRecuperado = controladorGestaoRelatorio.buscarCampoRelatorioPorId(campo.getIdCampo());
			assertNotNull(campoRecuperado);
			assertEquals(nomeCampoNovo, campoRecuperado.getNomeCampo());

			controladorGestaoRelatorio.removerCampoRelatorio(campoRecuperado);
			
		} catch (Exception e) {
			fail("Ocorreu exceção :  " + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * testSalvarLista
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @return Returns the void.
	 */
    public void testSalvarLista() {
    	try {
    		List<CampoRelatorio> campos = new ArrayList<CampoRelatorio>();
    		CampoRelatorio campoRelatorio = new CampoRelatorio();
    		campoRelatorio.setNomeCampo("Campo1");
    		campos.add(campoRelatorio);
    		campoRelatorio = new CampoRelatorio();
    		campoRelatorio.setNomeCampo("Campo2");
    		campos.add(campoRelatorio);
    		campoRelatorio = new CampoRelatorio();
    		campoRelatorio.setNomeCampo("Campo3");
    		campos.add(campoRelatorio);
    		
    		int totalCampos = campos.size();

    		List<CampoRelatorio> camposSalvo = controladorGestaoRelatorio.salvarListaDeCampoRelatorio(campos);
    		assertEquals(totalCampos, camposSalvo.size());
    		
    		for (CampoRelatorio campo : camposSalvo) {
    			assertNotNull(campo.getIdCampo());
    		}
    		
    		for (CampoRelatorio campo : camposSalvo) {
    			controladorGestaoRelatorio.removerCampoRelatorio(campo);
    		}
    		List<CampoRelatorio> camposRecuperado = controladorGestaoRelatorio.buscarCampos();
    		
    		assertTrue(camposRecuperado.isEmpty());
    		
    	} catch (Exception e) {
    		fail("Ocorreu exceção :  " + e.getMessage() + "\n");
    		e.printStackTrace();
    	}
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
			padrao.setNomePadraoRelatorio("PadraoRelatorio01");
			 
			for (int i=0; i<5; i++) {
				PadraoCampo padraoCampo = new PadraoCampo();
				padraoCampo.setCampoRelatorio(camposRelatorio.get(i));
				padraoCampo.setPadraoRelatorio(padrao);
				padraoCampo.setOrdem(i);
				padrao.addPadraoCampo(padraoCampo);
			}			
			
			PadraoRelatorio padraoSalvo = controladorGestaoRelatorio.salvarPadraoRelatorio(padrao);
			assertNotNull(padraoSalvo.getIdPadraoRelatorio());
			
			PadraoRelatorio padraoRecuperado = controladorGestaoRelatorio.buscarPadraoRelatorioPorId(padraoSalvo.getIdPadraoRelatorio());
			
			assertNotNull(padraoRecuperado);
			assertEquals(5,padraoRecuperado.getListaDeCampos().size());
			
			controladorGestaoRelatorio.removerPadraoRelatorio(padraoRecuperado);
			
		} catch (Exception e) {
			fail("Ocorreu exceção :  " + e.getMessage() + "\n");
            e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * testAlterarCampoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 * @see
	 * @throws Exception
	 * @return Returns the void.
	 */
	public void testAlterarCamposPadraoRelatorio() {
		try {
			List<CampoRelatorio> camposRelatorio = controladorGestaoRelatorio.buscarCampos(); 
			assertFalse(camposRelatorio.isEmpty());
			int totalCampos = camposRelatorio.size();
			assertTrue(totalCampos>5);
			
			PadraoRelatorio padrao = new PadraoRelatorio();
			padrao.setNomePadraoRelatorio("PadraoRelatorio01");
			 
			for (int i=0; i<5; i++) {
				PadraoCampo padraoCampo = new PadraoCampo();
				padraoCampo.setCampoRelatorio(camposRelatorio.get(i));
				padraoCampo.setPadraoRelatorio(padrao);
				padraoCampo.setOrdem(i);
				padrao.addPadraoCampo(padraoCampo);
			}			
			
			PadraoRelatorio padraoSalvo = controladorGestaoRelatorio.salvarPadraoRelatorio(padrao);
			assertNotNull(padraoSalvo.getIdPadraoRelatorio());
			
			
			PadraoRelatorio padraoRelatorio = controladorGestaoRelatorio.buscarPadraoRelatorioPorId(padraoSalvo.getIdPadraoRelatorio());
			assertEquals(5,padraoRelatorio.getListaDeCampos().size());
			
			PadraoCampo campoRemovido = padraoRelatorio.getListaDeCampos().remove(0);
			assertNotNull(campoRemovido);
			
			int i = 1;
			PadraoCampo padraoCampo = new PadraoCampo();
			padraoCampo.setCampoRelatorio(camposRelatorio.get(6));
			padraoCampo.setOrdem(i++);
			padraoCampo.setPadraoRelatorio(padraoRelatorio);
			
			for(PadraoCampo campo : padraoRelatorio.getListaDeCampos()) {
				campo.setOrdem(i++);
			}
			padraoRelatorio.addPadraoCampo(padraoCampo);
			
			padraoCampo = new PadraoCampo();
			padraoCampo.setCampoRelatorio(camposRelatorio.get(7));
			padraoCampo.setOrdem(i);
			padraoCampo.setPadraoRelatorio(padraoRelatorio);
			padraoRelatorio.addPadraoCampo(padraoCampo);
			
			padraoRelatorio = controladorGestaoRelatorio.salvarPadraoRelatorio(padraoRelatorio);
			
			PadraoRelatorio padraoRecuperado = controladorGestaoRelatorio.buscarPadraoRelatorioPorId(padraoRelatorio.getIdPadraoRelatorio());
			
			assertNotNull(padraoRecuperado);
			assertEquals(6,padraoRecuperado.getListaDeCampos().size());
			
			controladorGestaoRelatorio.removerPadraoRelatorio(padraoRecuperado);
			
		} catch (Exception e) {
			fail("Ocorreu exceção :  " + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * testSalvarRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @return Returns the void.
	 */
	public void testSalvarRelatorio() {
		
		try {
			List<CampoRelatorio> camposRelatorio = controladorGestaoRelatorio.buscarCampos(); 
			assertFalse(camposRelatorio.isEmpty());
			int totalCampos = camposRelatorio.size();
			assertTrue(totalCampos>5);
			
			PadraoRelatorio padrao = new PadraoRelatorio();
			padrao.setNomePadraoRelatorio("PadraoRelatorio01");
			 
			for (int i=0; i<5; i++) {
				PadraoCampo padraoCampo = new PadraoCampo();
				padraoCampo.setCampoRelatorio(camposRelatorio.get(i));
				padraoCampo.setPadraoRelatorio(padrao);
				padraoCampo.setOrdem(i);
				padrao.addPadraoCampo(padraoCampo);
			}			
			
			PadraoRelatorio padraoSalvo = controladorGestaoRelatorio.salvarPadraoRelatorio(padrao);
			assertNotNull(padraoSalvo.getIdPadraoRelatorio());
			
			PadraoRelatorio padraoRecuperado = controladorGestaoRelatorio.buscarPadraoRelatorioPorId(padraoSalvo.getIdPadraoRelatorio());
			
			assertNotNull(padraoRecuperado);
			assertEquals(5,padraoRecuperado.getListaDeCampos().size());

			
			Relatorio relatorio = new Relatorio();
			relatorio.setNomeRelatorio("RelatorioOficial01");
			relatorio.setTipoRelatorio(EnumTipoRelatorio.OFICIAL);
			relatorio.setIdUsuario(1L);
			relatorio.setHorarioInicioRelatorio(new Date());
			relatorio.setHorarioFimRelatorio(new Date());
			relatorio.setPadraoRelatorio(padraoRecuperado);
			
			Relatorio relatorioSalvo = controladorGestaoRelatorio.salvarRelatorio(relatorio);
			
			assertNotNull(relatorioSalvo);
			assertNotNull(relatorioSalvo.getIdRelatorio());
			
			Relatorio relatorioRecuperado = controladorGestaoRelatorio.buscarRelatorioPorId(relatorioSalvo.getIdRelatorio());
			
			assertNotNull(relatorioRecuperado);
			assertNotNull(relatorioRecuperado.getIdRelatorio());
			assertEquals(padraoRecuperado, relatorioRecuperado.getPadraoRelatorio());
			
			controladorGestaoRelatorio.removerRelatorio(relatorioRecuperado);
			relatorioRecuperado = controladorGestaoRelatorio.buscarRelatorioPorId(relatorioRecuperado.getIdRelatorio());
			assertNull(relatorioRecuperado);
			
		} catch (Exception e) {
			fail("Ocorreu exceção :  " + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * testSalvarRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @return Returns the void.
	 */
	public void testBuscarRelatorio() {
		
		try {
			List<PadraoRelatorio> padraoRelatorioList = controladorGestaoRelatorio.buscarPadroesRelatorio();
			assertFalse(padraoRelatorioList.isEmpty());
			
			Relatorio relatorio = new Relatorio();
			relatorio.setNomeRelatorio("RelatorioOficial01");
			relatorio.setTipoRelatorio(EnumTipoRelatorio.OFICIAL);
			relatorio.setIdUsuario(1L);
			relatorio.setHorarioInicioRelatorio(new Date());
			relatorio.setHorarioFimRelatorio(new Date());
			relatorio.setPadraoRelatorio(padraoRelatorioList.get(0));
			
			Relatorio relatorioASalvo = controladorGestaoRelatorio.salvarRelatorio(relatorio);
			
			relatorio = new Relatorio();
			relatorio.setNomeRelatorio("RelatorioLocal01");
			relatorio.setTipoRelatorio(EnumTipoRelatorio.INSTANTANEO);
			relatorio.setIdUsuario(1L);
			relatorio.setHorarioInicioRelatorio(new Date());
			relatorio.setHorarioFimRelatorio(new Date());
			relatorio.setPadraoRelatorio(padraoRelatorioList.get(0));
			
			Relatorio relatorioBSalvo = controladorGestaoRelatorio.salvarRelatorio(relatorio);
			
			List<Relatorio> relatorios = controladorGestaoRelatorio.buscarRelatorios();
			assertTrue(relatorios.size()>=2);
			
			
			Relatorio filtro = new Relatorio();
			filtro.setNomeRelatorio(relatorioBSalvo.getNomeRelatorio());
			relatorios = controladorGestaoRelatorio.buscarRelatoriosPorFiltro(filtro);
			assertEquals(1, relatorios.size());
			assertEquals(relatorio.getNomeRelatorio(),relatorios.get(0).getNomeRelatorio());
			
			controladorGestaoRelatorio.removerRelatorio(relatorioASalvo);
			controladorGestaoRelatorio.removerRelatorio(relatorioBSalvo);
			
		} catch (Exception e) {
			fail("Ocorreu exceção :  " + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}
	
    
    
    // método para realizar lookup dos serviços remotamente
    private IControladorGestaoRelatorio lookUp() {
        try {
            return null;//(IControladorGestaoRelatorio) ServiceLocator.getRemote("bs/ControladorGestaoRelatorio", IControladorGestaoRelatorio.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
