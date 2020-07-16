
package com.hdntec.gestao.services.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.hdntec.gestao.domain.navios.dao.ClienteDAO;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.dao.AtividadeDAO;
import com.hdntec.gestao.domain.plano.dao.LugarDeEmpilhamentoOuRecuperacaoDAO;
import com.hdntec.gestao.domain.plano.dao.PlanoEmpilhamentoRecuperacaoDAO;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.dao.PlantaDAO;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.services.controladores.IControladorFilaDeNavios;
import com.hdntec.gestao.services.controladores.impl.ControladorFilaDeNavios;

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
public class TestServicesNavio extends TestCase {

    private static PlantaDAO plantaDAO = new PlantaDAO();
    private static MetaBaliza metaBaliza;
    private static MetaBaliza metaBalizaProxima;
    private static List<Planta> plantas;
    private static List<Cliente> clientes;
    public static long timeDefault = System.currentTimeMillis();
    private static LugarDeEmpilhamentoOuRecuperacaoDAO daoL = new LugarDeEmpilhamentoOuRecuperacaoDAO();
    private static ClienteDAO dao = new ClienteDAO();    
    private static AtividadeDAO atividadeDAO = new AtividadeDAO();
    private static List<Berco> bercos;
    private static Date inicioExecucao;
    private static MetaNavio metaNavio;
    @Override
    
    
    public void setUp() {
    
       
            inicioExecucao = new Date(System.currentTimeMillis());
            plantas = plantaDAO.findAll(Planta.class);
            clientes =  dao.findAll(Cliente.class);
            // recupera patio        
             bercos = plantas.get(0).getListaBercos(inicioExecucao);
             
             
       
    }
    
    public void testCriarAtividadeAtracar() {
        try {
            ControladorFilaDeNavios controlador = new ControladorFilaDeNavios();
            
            
            FilaDeNavios fila = controlador.recuperaFila(inicioExecucao);
            List<Navio> navios = fila.getListaDeNaviosNaFila();
            
            AtividadeAtracarDesAtracarNavioVO movimentarNavioVO = new AtividadeAtracarDesAtracarNavioVO(); 

            metaNavio = navios.get(0).getMetaNavio();
            
            movimentarNavioVO.setTipoAtividade(TipoAtividadeEnum.CHEGADA_DE_NAVIO);
            movimentarNavioVO.setDataInicio(getHoraAtividade(0));
            movimentarNavioVO.setMetaNavio(metaNavio);            
            movimentarNavioVO.setMetaBercoDestino(bercos.get(0).getMetaBerco());
            IControladorFilaDeNavios service = new ControladorFilaDeNavios();
            Atividade atividade =  service.movimentarNavio(movimentarNavioVO);
            
            // atualizar dados do estado anterior
            /**
             * TODO ALTERAR PRA SALVAR APENAS OS OBJETOS ALTERADOS PELA ATIVIDADE E N�O A PLANTA TODA 
             * 
             */            
            //plantaDAO.atualizar(situacao.getPlanta());
            
            atividade = atividadeDAO.salvaAtividade(atividade);
            
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


    /*public void testCriarAtividadeDesatracar() {
        try {            
            AtividadeAtracarDesAtracarNavioVO movimentarNavioVO = new AtividadeAtracarDesAtracarNavioVO(); 
            movimentarNavioVO.setTipoAtividade(TipoAtividadeEnum.SAIDA_DE_NAVIO);
            movimentarNavioVO.setDataInicio(getHoraAtividade(5));
            movimentarNavioVO.setMetaNavio(metaNavio);            
            movimentarNavioVO.setMetaBercoDestino(bercos.get(0).getMetaBerco());
            testAtracarNavio service = new testAtracarNavio();
            Atividade atividade =  service.movimentarNavio(movimentarNavioVO);
            
            // atualizar dados do estado anterior
            *//**
             * TODO ALTERAR PRA SALVAR APENAS OS OBJETOS ALTERADOS PELA ATIVIDADE E N�O A PLANTA TODA 
             * 
             *//*            
            //plantaDAO.atualizar(situacao.getPlanta());
            
            atividade = atividadeDAO.salvaAtividade(atividade);
            
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
*/
    

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
