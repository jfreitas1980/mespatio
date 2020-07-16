
package com.hdntec.gestao.services.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.hdntec.gestao.domain.navios.StatusNavioEnum;
import com.hdntec.gestao.domain.navios.dao.ClienteDAO;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.navios.dao.NavioDAO;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.dao.AtividadeDAO;
import com.hdntec.gestao.domain.plano.dao.LugarDeEmpilhamentoOuRecuperacaoDAO;
import com.hdntec.gestao.domain.plano.dao.PlanoEmpilhamentoRecuperacaoDAO;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.dao.PlantaDAO;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.vo.atividades.AtualizarRecuperacaoVO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeRecuperacao;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeRecuperacao;
import com.hdntec.gestao.util.GeradorModeloUtilCenarioCompleto;

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
public class TestServicesRecuperar extends TestCase {

	private static PlantaDAO plantaDAO = new PlantaDAO();
	private static MetaNavioDAO navioDAO = new MetaNavioDAO();
	private static LugarDeEmpilhamentoOuRecuperacaoDAO daoL = new LugarDeEmpilhamentoOuRecuperacaoDAO();
    private static ClienteDAO dao = new ClienteDAO();    
    private static AtividadeDAO atividadeDAO = new AtividadeDAO();
	
    private static MetaBaliza metaBaliza;
    private static MetaBaliza metaBalizaProxima;
    private static List<Planta> plantas;
    private static List<Cliente> clientes;
    public static long timeDefault = System.currentTimeMillis();
    
    private static List<Patio> patios;
    private static MetaPatio metaPatio;
    private static List<MetaUsina> usinas;
    private static List<Correia> correias;
    private static Correia correiaDestino;
    private static List<MetaMaquinaDoPatio> maquinas;
    private static MaquinaDoPatio maquina;
    private static MetaMaquinaDoPatio metaMaquina;
    private static List<MetaBaliza> metaBalizas;
    private static MetaNavio metaNavio;
    private static Date inicioExecucao = new Date(System.currentTimeMillis());
    
    @Override
    public void setUp() {
        plantas = plantaDAO.findAll(Planta.class);
        clientes =  dao.findAll(Cliente.class);
        NavioDAO navioDaoLocal = new NavioDAO();
        Navio navio = new Navio();
        navio.setStatusEmbarque(StatusNavioEnum.ATRACADO);
    	List<Navio> result = null;
        try {
			result = navioDaoLocal.buscaPorExemploNavio(navio);
		} catch (ErroSistemicoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		metaNavio = result.get(0).getMetaNavio();
        // recupera patio        
         patios = plantas.get(0).getListaPatios(inicioExecucao);
      // define meta patio
                
        usinas = plantas.get(0).getListaMetaUsinas();
        
        
      //recupera correias
        correias = plantas.get(0).getListaCorreias(inicioExecucao);
        // recupera correia no momento         
        correiaDestino = null;
        MetaCorreia metaCorreia = null;
        
        for (Patio patio : patios) {            
            MetaPatio metaPatioLocal = patio.getMetaPatio();
            for (Correia item : correias) {
                correiaDestino = GeradorModeloUtilCenarioCompleto.recuperarCorreiaRecuperacaoPatio(metaPatioLocal, item, inicioExecucao);
                if (correiaDestino != null) {
                    metaCorreia = correiaDestino.getMetaCorreia();
                    metaPatio = metaPatioLocal;
                    break;
                }
            }
            if (metaCorreia != null) {
                break;
            }
        }
            // recupera meta maquinas 
        maquinas = correiaDestino.getMetaCorreia().getListaDeMetaMaquinas();
      
        maquina = GeradorModeloUtilCenarioCompleto.recuperarMaquinaRecuperadorCorreia(inicioExecucao, maquinas);

        metaMaquina = maquina.getMetaMaquina();
        
        // recurpar as meta balizas do patio
        metaBalizas = metaPatio.getListaDeMetaBalizas();

    }
    
    public void testCriarAtividadeRecuperacaoBaliza() {
        try {
            
            AtualizarRecuperacaoVO empilhamentoVO = new AtualizarRecuperacaoVO(); 

            
            
            
            /**
             * procura baliza sem produto 
             */
            for (MetaBaliza baliza : metaBalizas) {
            	Baliza balizaDestino = baliza.retornaStatusHorario(inicioExecucao);
                if (balizaDestino.getProduto() != null ) {// && balizaDestino.getProduto().getQuantidade() > 0) {
                    metaBaliza = baliza;                
                    break;
                }
            }
            List<Baliza> balizasPilha = metaBaliza.retornaStatusHorario(inicioExecucao).retornaStatusHorario(inicioExecucao).getListaDeBalizas();
            Collections.sort(balizasPilha,new ComparadorBalizas());
            metaBalizaProxima =  balizasPilha.get(balizasPilha.size() -1).getMetaBaliza();
            empilhamentoVO.setDataInicio(getHoraAtividade(0));
            empilhamentoVO.setNomePilha("RECUPERACAO");
            empilhamentoVO.getListaBalizas().add(metaBaliza);
            //empilhamentoVO.getListaUsinas().addAll(usinas);
            empilhamentoVO.getListaMaquinas().add(metaMaquina);
            empilhamentoVO.setTipoProduto(usinas.get(0).getCampanhaAtual(getHoraAtividade(0)).getTipoProduto());
            empilhamentoVO.setCliente(clientes.get(0));            
            empilhamentoVO.setMetaCarga(metaNavio.getListaMetaCargas().get(0));
            IControladorExecutarAtividadeRecuperacao service = new ControladorExecutarAtividadeRecuperacao();
            List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
            Atividade atividade =  service.recuperar(empilhamentoVO,lugaresAnteriores);
            
            // atualizar dados do estado anterior
            /**
             * TODO ALTERAR PRA SALVAR APENAS OS OBJETOS ALTERADOS PELA ATIVIDADE E N�O A PLANTA TODA 
             * 
             */            
            //plantaDAO.atualizar(situacao.getPlanta());
            
            atividadeDAO.salvaAtividade(atividade);
            if (lugaresAnteriores!= null && lugaresAnteriores.size() > 0) {
                daoL.atualizar(lugaresAnteriores); 
            }
            
            gerarSituacao(atividade);
            //navioDAO.encerrarSessao();
            //daoL.encerrarSessao();
            //atividadeDAO.encerrarSessao();
            //dao.encerrarSessao();
            //plantaDAO.encerrarSessao();                      
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    public void testFinalizarAtividadeRecuperacaoBaliza() {
        try {
            
            AtualizarRecuperacaoVO empilhamentoVO = new AtualizarRecuperacaoVO(); 
            
              
            empilhamentoVO.setDataInicio(inicioExecucao);
            empilhamentoVO.setDataFim(getHoraAtividade(2));
            //empilhamentoVO.setFinalizando(Boolean.TRUE);
            empilhamentoVO.setNomePilha("RECUPERACAO");            
            empilhamentoVO.getListaBalizas().add(metaBalizaProxima);
            empilhamentoVO.getListaMaquinas().add(metaMaquina);
            empilhamentoVO.setTipoProduto(usinas.get(0).getCampanhaAtual(getHoraAtividade(2)).getTipoProduto());
            empilhamentoVO.setCliente(clientes.get(0));            
            empilhamentoVO.setMetaCarga(metaNavio.getListaMetaCargas().get(0));
            IControladorExecutarAtividadeRecuperacao service = new ControladorExecutarAtividadeRecuperacao();
            List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
            Atividade atividade =  service.recuperar(empilhamentoVO,lugaresAnteriores);
            
            // atualizar dados do estado anterior
            /**
             * TODO ALTERAR PRA SALVAR APENAS OS OBJETOS ALTERADOS PELA ATIVIDADE E N�O A PLANTA TODA 
             * 
             */            
            //plantaDAO.atualizar(situacao.getPlanta());
            
            atividadeDAO.salvaAtividade(atividade);
            if (lugaresAnteriores!= null && lugaresAnteriores.size() > 0) {
                daoL.atualizar(lugaresAnteriores); 
            }
            
            gerarSituacao(atividade);
            
            //daoL.encerrarSessao();
            //atividadeDAO.encerrarSessao();
            //dao.encerrarSessao();
            //plantaDAO.encerrarSessao();                      
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private Date getHoraAtividade(int minutos) {
        Calendar calEvento = Calendar.getInstance();
        calEvento.setTimeInMillis(inicioExecucao.getTime());
        calEvento.add(Calendar.MINUTE, minutos);           
        return calEvento.getTime();           
    }

    public void gerarSituacao(Atividade atividade) {
        Planta planta = plantas.get(0);
        PlanoEmpilhamentoRecuperacaoDAO planoDao = new PlanoEmpilhamentoRecuperacaoDAO();
        List<PlanoEmpilhamentoRecuperacao> planos = planoDao.findAll(PlanoEmpilhamentoRecuperacao.class);
        PlanoEmpilhamentoRecuperacao plano = planos.get(0); 
        
        SituacaoPatio situacao = plano.gerarSituacao(atividade);
        
        plano.addSituacaoPatio(situacao);
         
        try {
            planoDao.salvaPlanoEmpilhamentoRecuperacao(plano);
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        planoDao.encerrarSessao();
        
    }
    
}
