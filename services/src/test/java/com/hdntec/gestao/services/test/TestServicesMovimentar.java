
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
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.dao.MetaMaquinaDoPatioDAO;
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
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeMovimentacaoPilha;
import com.hdntec.gestao.util.GeradorModeloUtilCenarioCompleto;

public class TestServicesMovimentar extends TestCase 
{
    PlantaDAO plantaDAO = new PlantaDAO();
    private static List<MetaBaliza> metaBalizasOrigem;
    private static List<MetaBaliza> metaBalizasDestino;
    
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
    private static MaquinaDoPatio maquinaOrigem;
    private static MaquinaDoPatio maquinaDestino;
    private static MetaMaquinaDoPatio metaMaquinaOrigem;
    private static MetaMaquinaDoPatio metaMaquinaDestino;
    private static List<MetaBaliza> metaBalizas;
    private static Date inicioExecucao = new Date(System.currentTimeMillis());
    private static Double quantidadeMovimentada = null;
    @Override
    public void setUp() {
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
       
        MetaMaquinaDoPatioDAO dao = new MetaMaquinaDoPatioDAO();
        MetaMaquinaDoPatio meta = new MetaMaquinaDoPatio();
        meta.setTipoDaMaquina(TipoMaquinaEnum.PA_CARREGADEIRA);
        List<MetaMaquinaDoPatio> result = dao.buscarListaDeObjetos(meta);
        
     
        // maquina origem da movimentacao
        maquinaOrigem = GeradorModeloUtilCenarioCompleto.recuperarPaCarregadeiraCorreia(inicioExecucao, result);
        metaMaquinaOrigem = maquinaOrigem.getMetaMaquina();

        maquinas = correiaDestino.getMetaCorreia().getListaDeMetaMaquinas();
        
        
        // maquina destino da movimentacao
        maquinaDestino = GeradorModeloUtilCenarioCompleto.recuperarMaquinaEmpilhamentoCorreia(inicioExecucao, maquinas);
        metaMaquinaDestino = maquinaDestino.getMetaMaquina();
        
        // recurpar as meta balizas do patio
        metaBalizas = metaPatio.getListaDeMetaBalizas();

    }
    
    public void testMovimentacaoIniciarPilhaParaPilha()
    {
       try
       {
          MovimentacaoVO movimentacaoVO = new MovimentacaoVO();
          movimentacaoVO.setTipoAtividade(TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM);
          TipoProduto tipo = null;
          // procura duas balizas consecutivas com produto para servir de origem na movimentacao          
          metaBalizasOrigem = new ArrayList<MetaBaliza>();
          
          int indiceOrigem;
          for (indiceOrigem = 0; indiceOrigem < 129; indiceOrigem++)
          {
             Patio patio = metaPatio.retornaStatusHorario(getHoraAtividade(0));
             Baliza baliza = patio.getListaDeBalizas(getHoraAtividade(0)).get(indiceOrigem);
             if (baliza.getProduto() != null)
             {
                 metaBalizasOrigem.add(baliza.getMetaBaliza());
                 tipo = baliza.getProduto().getTipoProduto();
                 quantidadeMovimentada = new Double(baliza.getProduto().getQuantidade().doubleValue());
                 Pilha pilha = baliza.retornaStatusHorario(getHoraAtividade(0));
                 for (Baliza n : pilha.getListaDeBalizas())
                     metaBalizasOrigem.add(n.getMetaBaliza());                 
                 break;
             }
          }          
          
          metaBalizasDestino = new ArrayList<MetaBaliza>();
          boolean adicionouBaliza = false;
          for (int indiceDestino = indiceOrigem; indiceDestino < 129; indiceDestino++)
          {
             
             Patio patio = metaPatio.retornaStatusHorario(getHoraAtividade(0));
             Baliza baliza = patio.getListaDeBalizas(getHoraAtividade(0)).get(indiceDestino);
             if (baliza.getProduto() == null)
             {
                 metaBalizasDestino.add(baliza.getMetaBaliza());
                 adicionouBaliza = true;
             } else {
               if (adicionouBaliza) break;
             }
          }
          
          movimentacaoVO.setDataInicio(getHoraAtividade(0));
          movimentacaoVO.setQuantidadeMovimentacao(quantidadeMovimentada);
          movimentacaoVO.setListaBalizas(metaBalizasOrigem);
          movimentacaoVO.setListaBalizasDestino(metaBalizasDestino);
          List<MetaMaquinaDoPatio> listaMaquinas = new ArrayList<MetaMaquinaDoPatio>();
          listaMaquinas.add(metaMaquinaOrigem);
          listaMaquinas.add(metaMaquinaDestino);
          movimentacaoVO.setListaMaquinas(listaMaquinas);
          movimentacaoVO.setSentidoEmpilhamento(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
          movimentacaoVO.setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
          movimentacaoVO.setTipoProduto(tipo);
          movimentacaoVO.setNomePilha("pilha alterada movi");
          movimentacaoVO.setNomePilhaDestino("pilha gerada movimentacao");
          
          ControladorExecutarAtividadeMovimentacaoPilha service = ControladorExecutarAtividadeMovimentacaoPilha.getInstance();
          Atividade atividade = service.movimentar(movimentacaoVO);
                 
         // atualizar dados do estado anterior
         /**
          * TODO ALTERAR PRA SALVAR APENAS OS OBJETOS ALTERADOS PELA ATIVIDADE E N�O A PLANTA TODA 
          * 
          */            
         //plantaDAO.atualizar(situacao.getPlanta());
               
         atividadeDAO.salvaAtividade(atividade);
         
         gerarSituacao(atividade);
         
         daoL.encerrarSessao();
         atividadeDAO.encerrarSessao();
         dao.encerrarSessao();
         plantaDAO.encerrarSessao();                      
       } 
       catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public void testMovimentacaoFinalizarPilhaParaPilha()
    {
       try
       {
          MovimentacaoVO movimentacaoVO = new MovimentacaoVO();
          movimentacaoVO.setTipoAtividade(TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM); 
          movimentacaoVO.setDataInicio(getHoraAtividade(0));
          movimentacaoVO.setDataFim(getHoraAtividade(600));
          movimentacaoVO.setListaBalizas(metaBalizasOrigem);
          movimentacaoVO.setListaBalizasDestino(metaBalizasDestino);
          movimentacaoVO.setQuantidadeMovimentacao(quantidadeMovimentada);
          List<MetaMaquinaDoPatio> listaMaquinas = new ArrayList<MetaMaquinaDoPatio>();
          listaMaquinas.add(metaMaquinaOrigem);
          listaMaquinas.add(metaMaquinaDestino);
          movimentacaoVO.setNomePilha("pilha alterada movi");
          movimentacaoVO.setNomePilhaDestino("pilha gerada movimentacao");
          movimentacaoVO.setListaMaquinas(listaMaquinas);
          movimentacaoVO.setSentidoEmpilhamento(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
          movimentacaoVO.setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
          movimentacaoVO.setTipoProduto(metaBalizasOrigem.get(0).retornaStatusHorario(getHoraAtividade(0)).getProduto().getTipoProduto());
          
          
          
          ControladorExecutarAtividadeMovimentacaoPilha service = ControladorExecutarAtividadeMovimentacaoPilha.getInstance();
          Atividade atividade = service.movimentar(movimentacaoVO);
                 
         // atualizar dados do estado anterior
         /**
          * TODO ALTERAR PRA SALVAR APENAS OS OBJETOS ALTERADOS PELA ATIVIDADE E N�O A PLANTA TODA 
          * 
          */            
         //plantaDAO.atualizar(situacao.getPlanta());
               
         atividadeDAO.salvaAtividade(atividade);
         
         gerarSituacao(atividade);
         
         daoL.encerrarSessao();
         atividadeDAO.encerrarSessao();
         dao.encerrarSessao();
         plantaDAO.encerrarSessao();                      
       } 
       catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    private Date getHoraAtividade(int minutos) {
        Calendar calEvento = Calendar.getInstance();
        calEvento.setTimeInMillis(inicioExecucao.getTime());
        calEvento.add(Calendar.MINUTE, minutos);           
        return calEvento.getTime();           
    }

    
}
