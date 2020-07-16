
package com.hdntec.gestao.services.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.hdntec.gestao.domain.navios.dao.ClienteDAO;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.plano.dao.AtividadeDAO;
import com.hdntec.gestao.domain.plano.dao.LugarDeEmpilhamentoOuRecuperacaoDAO;
import com.hdntec.gestao.domain.plano.dao.PlanoEmpilhamentoRecuperacaoDAO;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
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
import com.hdntec.gestao.domain.vo.atividades.AtualizarEmpilhamentoVO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeEmpilhamento;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeEmpilhamento;
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
public class TestServices extends TestCase {

    private static PlantaDAO plantaDAO = new PlantaDAO();
    private static MetaBaliza metaBaliza;
    private static MetaBaliza metaBalizaProxima;
    private static List<Planta> plantas;
    private static List<Cliente> clientes;
    public static long timeDefault = System.currentTimeMillis();
    private static LugarDeEmpilhamentoOuRecuperacaoDAO daoL = new LugarDeEmpilhamentoOuRecuperacaoDAO();
    private static ClienteDAO dao = new ClienteDAO();    
    private static AtividadeDAO atividadeDAO = new AtividadeDAO();
    private static List<Patio> patios;
    private static MetaPatio metaPatio;
    private static List<MetaUsina> usinas;
    private static List<Correia> correias;
    private static Correia correiaDestino;
    private static List<MetaMaquinaDoPatio> maquinas;
    private static MaquinaDoPatio maquina;
    private static MetaMaquinaDoPatio metaMaquina;
    private static List<MetaBaliza> metaBalizas;
    private static Date inicioExecucao;
    @Override
    
    
    public void setUp() {
    
       
            inicioExecucao = new Date(System.currentTimeMillis());
            plantas = plantaDAO.findAll(Planta.class);
            clientes =  dao.findAll(Cliente.class);
            // recupera patio        
             patios = plantas.get(0).getListaPatios(inicioExecucao);
          // define meta patio
            metaPatio = patios.get(0).getMetaPatio();        
            usinas = plantas.get(0).getListaMetaUsinas();
            
            
          //recupera correias
            correias = plantas.get(0).getListaCorreias(inicioExecucao);
            // recupera correia no momento         
            correiaDestino = null;
            MetaCorreia metaCorreia = null;
            for (Correia item : correias) {
                correiaDestino = GeradorModeloUtilCenarioCompleto.recuperarCorreiaEmpilhamentoPatio(metaPatio, item, inicioExecucao);
                if (correiaDestino != null) {
                    metaCorreia = correiaDestino.getMetaCorreia();
                    break;
                }
            }
            // recupera meta maquinas 
            maquinas = correiaDestino.getMetaCorreia().getListaDeMetaMaquinas();
          
            maquina = GeradorModeloUtilCenarioCompleto.recuperarMaquinaEmpilhamentoCorreia(inicioExecucao, maquinas);
    
            metaMaquina = maquina.getMetaMaquina();
            
            // recurpar as meta balizas do patio
            metaBalizas = metaPatio.getListaDeMetaBalizas();
       
    }
    
    public void testCriarAtividadeEmpilhamentoBalizaVazia() {
        try {
            
            AtualizarEmpilhamentoVO empilhamentoVO = new AtualizarEmpilhamentoVO(); 

            
            
            /**
             * procura baliza sem produto 
             */
            for (MetaBaliza baliza : metaBalizas) {
            	Baliza balizaDestino = baliza.retornaStatusHorario(getHoraAtividade(0));
                if (balizaDestino.getProduto() == null) {
                    metaBaliza = baliza;                
                    break;
                }
            }
              
            empilhamentoVO.setDataInicio(getHoraAtividade(0));
            empilhamentoVO.setNomePilha("PILHA TESTE BALIZA VAZIA");
            empilhamentoVO.getListaBalizas().add(metaBaliza);
            empilhamentoVO.getListaUsinas().addAll(usinas);
            empilhamentoVO.getListaMaquinas().add(metaMaquina);
            empilhamentoVO.setTipoProduto(usinas.get(0).getCampanhaAtual(getHoraAtividade(0)).getTipoProduto());
            empilhamentoVO.setCliente(clientes.get(0));            
            ControladorExecutarAtividadeEmpilhamento service = new ControladorExecutarAtividadeEmpilhamento();
            List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
            Atividade atividade =  service.empilhar(empilhamentoVO,lugaresAnteriores);
            
            // atualizar dados do estado anterior
            /**
             * TODO ALTERAR PRA SALVAR APENAS OS OBJETOS ALTERADOS PELA ATIVIDADE E N�O A PLANTA TODA 
             * 
             */            
            //plantaDAO.atualizar(situacao.getPlanta());
            
            atividade = atividadeDAO.salvaAtividade(atividade);
            if (lugaresAnteriores!= null && lugaresAnteriores.size() > 0) {
                daoL.atualizar(lugaresAnteriores); 
            }
            gerarSituacao(atividade);
            
            daoL.encerrarSessao();
            atividadeDAO.encerrarSessao();
            dao.encerrarSessao();
            plantaDAO.encerrarSessao();                      
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

      
    
    public void testCriarAtividadeEmpilhamentoBalizaComProduto() {
        try {
            
            
            AtualizarEmpilhamentoVO empilhamentoVO = new AtualizarEmpilhamentoVO(); 
                           
            empilhamentoVO.setDataInicio(getHoraAtividade(1));
            empilhamentoVO.setNomePilha("PILHA TESTE BALIZA COM PRODUTO");
            empilhamentoVO.getListaBalizas().add(metaBaliza);
            empilhamentoVO.getListaUsinas().addAll(usinas);
            empilhamentoVO.getListaMaquinas().add(metaMaquina);
            empilhamentoVO.setTipoProduto(usinas.get(0).getCampanhaAtual(getHoraAtividade(1)).getTipoProduto());
            empilhamentoVO.setCliente(clientes.get(0));            
            IControladorExecutarAtividadeEmpilhamento service = new ControladorExecutarAtividadeEmpilhamento();
            List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
            Atividade atividade =  service.empilhar(empilhamentoVO,lugaresAnteriores);
            
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
            
            daoL.encerrarSessao();
            atividadeDAO.encerrarSessao();
            dao.encerrarSessao();
            plantaDAO.encerrarSessao();                      
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void testCriarAtividadeEmpilhamentoProximaBaliza() {
        try {
           
            
            AtualizarEmpilhamentoVO empilhamentoVO = new AtualizarEmpilhamentoVO(); 
            /**
             * procura baliza sem produto 
             */
            for (MetaBaliza baliza : metaBalizas) {
                Baliza balizaDestino = baliza.retornaStatusHorario(getHoraAtividade(2));
                if (balizaDestino.getProduto() == null) {
                    metaBalizaProxima = baliza;                
                    break;
                }
            }
              
            empilhamentoVO.setDataInicio(getHoraAtividade(2));
            empilhamentoVO.setNomePilha("PILHA TESTE BALIZA COM PRODUTO 2 balizas inicio");
            empilhamentoVO.getListaBalizas().add(metaBalizaProxima);
            empilhamentoVO.getListaUsinas().addAll(usinas);
            empilhamentoVO.getListaMaquinas().add(metaMaquina);
            empilhamentoVO.setTipoProduto(usinas.get(0).getCampanhaAtual(getHoraAtividade(2)).getTipoProduto());
            empilhamentoVO.setCliente(clientes.get(0));            
            IControladorExecutarAtividadeEmpilhamento service = new ControladorExecutarAtividadeEmpilhamento();
            List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
            Atividade atividade =  service.empilhar(empilhamentoVO,lugaresAnteriores);
            
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
            
            daoL.encerrarSessao();
            atividadeDAO.encerrarSessao();
            dao.encerrarSessao();
            plantaDAO.encerrarSessao();                      
                      
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testCriarAtividadeEmpilhamentoFinalizar() {
        try {            
            
            AtualizarEmpilhamentoVO empilhamentoVO = new AtualizarEmpilhamentoVO(); 

                                       
            empilhamentoVO.setDataInicio(getHoraAtividade(3));
            empilhamentoVO.setDataFim(getHoraAtividade(3));
            empilhamentoVO.setNomePilha("PILHA TESTE BALIZA COM PRODUTO 2 balizas fim");
            empilhamentoVO.getListaBalizas().add(metaBalizaProxima);
            empilhamentoVO.getListaUsinas().addAll(usinas);
            empilhamentoVO.getListaMaquinas().add(metaMaquina);
            empilhamentoVO.setTipoProduto(usinas.get(0).getCampanhaAtual(getHoraAtividade(3)).getTipoProduto());
            empilhamentoVO.setCliente(clientes.get(0));            
            
            IControladorExecutarAtividadeEmpilhamento service = new ControladorExecutarAtividadeEmpilhamento();
            
            List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
            
            Atividade atividade =  service.empilhar(empilhamentoVO,lugaresAnteriores);
          
            atividadeDAO.salvaAtividade(atividade);
            
            if (lugaresAnteriores!= null && lugaresAnteriores.size() > 0) {
                daoL.atualizar(lugaresAnteriores); 
            }
            
            gerarSituacao(atividade);
            
            
            daoL.encerrarSessao();
            atividadeDAO.encerrarSessao();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    
    public void testApagarAtividadeEmpilhamento() {
        try {
            AtividadeDAO atividadeDAO = new AtividadeDAO();            
            List<Atividade> atividades = atividadeDAO.findAll(Atividade.class);            
            //atividadeDAO.deletar(atividades.get(0));
            //atividadeDAO.encerrarSessao();
                       
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

        
    }
    

}
