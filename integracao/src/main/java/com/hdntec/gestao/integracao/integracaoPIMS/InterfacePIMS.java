package com.hdntec.gestao.integracao.integracaoPIMS;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.numeros.DSSStockyardFuncoesNumeros;

import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.blendagem.ControladorCalculoQualidade;
import com.hdntec.gestao.domain.integracao.IControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.plano.comparadores.ComparadorSituacoesPatio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.integracao.facade.mes.MESFacade;
import com.hdntec.gestao.integracao.facade.mes.PIMSFacade;
import com.hdntec.gestao.integracao.facade.mes.impl.MESFacadeImpl;
import com.hdntec.gestao.integracao.facade.mes.impl.PIMSFacadeImpl;
import com.hdntec.gestao.integracao.plano.controladores.ControladorMaquinas;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarPerdaPellet;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;
/**
 * Interface com o sistema externo PIMS.
 * PIMS ÃƒÂ© o sistema externo que armazena e envia para o sistema os dados de estado e posicionamento das mÃƒÂ¡quinas do pÃƒÂ¡tio.
 * 
 * @author andre
 */
public class InterfacePIMS
{
   /** Log para Login e Logout*/
   private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("InterfacePims");

   
   /** Acesso ao subsistema Controlador do Plano de Empilhamento e Recuperacao */
   private IControladorModelo controladorModelo;

   /** Acesso ao subsistema Controlador das Integracoes */
   private IControladorIntegracao controladorIntegracao;

   /** indica se houve atualizacao de dados. Neste caso o plano deve ser gravado na base */
   private boolean atualizouDados;

   PIMSFacade pimsFacade = new PIMSFacadeImpl();

   private static Date lastUpdateTime;

   public static Date getLastUpdateTime()
   {
      return lastUpdateTime;
   }

   public static Date defineCurrTime(Date data)
   {
      if (lastUpdateTime == null) {
         lastUpdateTime = new Date();
      }
      lastUpdateTime.setTime(data.getTime());
      return lastUpdateTime;
   }

   /**
    * Atualiza os dados do sistema externo PIMS e MES, a partir da ultimaAtualizacao.
    * 
    * @return a lista de atividades que aconteceram desde a ÃƒÂºltima atualizaÃƒÂ§ÃƒÂ£o
    */
   public PlanoEmpilhamentoRecuperacao atualizarDados(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao, Map<String, List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException
   {
      try {
         MESFacade mesFacade = new MESFacadeImpl();

         atualizouDados = false;

         // o plano a ser atualizado
         PlanoEmpilhamentoRecuperacao planoAtualizacaoAtual = planoEmpilhamentoRecuperacao;
         
         Collections.sort(planoAtualizacaoAtual.getListaSituacoesPatio(), new ComparadorSituacoesPatio());

         //TODO para atualizar os dados de quantidade na recuperacao temos que fazer as seguintes tarefas:
         // para todas as atividades que ainda nao foram atualizadas:

         // 1) verificar se a atividade atual usa a recuperadora
         //    1.1) verificar se existem atividades de usina para o navio durante o periodo da atividade
         //       1.1.1) se existir, entao so continua a atualizacao se todas as informacoes de quantidade das usinas estiverem 
         //       disponiveis. Se as informacoes nao estiverem disponiveis entao essa atualizacao fica pendente.
         //       retirar da pilha Q = C03 - Retomadora - Usinas
         //       acrescentar na carga Q = C03 - Retomadora - Usinas
         //       1.1.2) se nao existir entao
         //       retirar da pilha Q = C03 - Retomadora
         //       acrescentar na carga Q = C03 - Retomadora
         //    1.2) indica que a atividade foi atualizada com sucesso

         // 2) verificar se a atividade atual usa a retomadora. Neste caso
         //       retirar da pilha Q = Retomadora
         //       acrescentar na carga Q = Retomadora
         // 2.1) indica que a atividade foi atualizada com sucesso

         // 3) verificar se a atividade atual eh de uma usina direto para o navio. Neste caso
         //       acrescentar na carga Q = Usina
         // 3.1) indica que a atividade foi atualizada com sucesso
         
         int sizeSituacoes = planoAtualizacaoAtual.getListaSituacoesPatio().size();
         
         int qtdSituacoesAtualizadas = 0;
         
         for (int indexSituacao = 1; indexSituacao < sizeSituacoes; indexSituacao++)
         {
            SituacaoPatio situacaoAtual = planoAtualizacaoAtual.getListaSituacoesPatio().get(indexSituacao);
            System.out.println("Atualizando PIMS situacao " + situacaoAtual);
            Atividade atividade = situacaoAtual.getAtividade();
            
            if (atividade.getFinalizada() && atividade.getTipoAtividade() == TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO && !atividade.getQuantidadeAtualizadaPeloMES())
            {
               System.out.println("Validando Recuperacao situacao " + atividade);
                              
               // 3) verificar se a atividade atual eh de uma usina direto para o navio
               if (atividade.getListaDeAtividadesCampanha() != null && !atividade.getListaDeAtividadesCampanha().isEmpty()) 
               {
                 System.out.println("Inicio Atualizando Quantidade Campanha " +  atividade + "-" + atividade.getDtInicio()  + "-" + atividade.getDtFim());
                 atualizouDados = atualizaQuantidadesCampanha(map, mesFacade, atividade, atividade.getListaDeAtividadesCampanha().get(0));
                 atividade.setUpdated(atualizouDados);
                 System.out.println("Fim Atualizando Quantidade Campanha " + atividade + "-" + new Date(System.currentTimeMillis()));
                 if (atualizouDados)
                    qtdSituacoesAtualizadas++;
                  // se atualizou campanha, atualiza pellet screening
                  //TODO
               }
               else 
               {
                  // a atividade de atualizacao tem apenas um lugar de empilhamento e recuperacao
                  LugarEmpilhamentoRecuperacao lugarEmpRec = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);

                  // maquina que realiza a atividade atual
                  MaquinaDoPatio maquinaAtividade = lugarEmpRec.getMaquinaDoPatio();

                  // 1) verificar se a atividade atual usa a recuperadora
                  if (maquinaAtividade.getNomeMaquina().trim().equalsIgnoreCase("Empilhadeira-Recuperadora 1")) 
                  {
                     System.out.println("Inicio Atualizando Quantidade Empilhadeira-Recuperadora 1 " + atividade + "-" + atividade.getDtInicio()  + "-" + atividade.getDtFim());
                     
                     atualizouDados = atualizaQuantidadesRecuperadora(map, mesFacade, planoAtualizacaoAtual, atividade, situacaoAtual);
                     atividade.setUpdated(atualizouDados);
                     if (atualizouDados)qtdSituacoesAtualizadas++;
                     System.out.println("Fim Atualizando Quantidade Empilhadeira-Recuperadora 1 " + atividade + "-" + new Date(System.currentTimeMillis()));
                  }
                  // 2) verificar se a atividade atual usa a retomadora
                  else if (maquinaAtividade.getNomeMaquina().trim().equalsIgnoreCase("Retomadora")) 
                  {
                     System.out.println("Inicio Atualizando Quantidade Retomadora " +  atividade + "-" + atividade.getDtInicio()  + "-" + atividade.getDtFim());
                     atualizouDados = atualizaQuantidadesRetomadora(atividade, situacaoAtual);
                     atividade.setUpdated(atualizouDados);
                     if (atualizouDados)qtdSituacoesAtualizadas++;
                     System.out.println("Fim Atualizando Quantidade Retomadora " + atividade + "-" + new Date(System.currentTimeMillis()));
                  }
               }
            }
         }

        
         System.out.println("SituaÃ§Ãµes de PÃ¡tio atualizadas " + qtdSituacoesAtualizadas);   
         return planoAtualizacaoAtual;

      }
      catch (Exception ex) {
         ex.printStackTrace();
         throw new ErroSistemicoException(ex.getMessage());
      }
   }

   private boolean atualizaQuantidadesCampanha(Map<String, List<IntegracaoMES>> map, MESFacade mesFacade, 
            Atividade atividade, AtividadeCampanha atividadeCampanha)
   {      
      Carga cargaAtividade = atividade.getCarga();
      // mapa contendo a quantidade de perda de pellet feed para cada campanha
      HashMap<AtividadeCampanha, Double> mapaPerdaSinterFeedPorCampanha = new HashMap<AtividadeCampanha, Double>();

      
      // verificar se os dados de quantidade para esta campanha ja estao disponiveis
      Double quantidadeCampanha = mesFacade.calcular(atividade.getDtInicio(), atividade.getDtFim(), atividade.getListaDeAtividadesCampanha().get(0).getCampanha(), map, atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getTipoProduto());
      if (quantidadeCampanha == null) {         
        return false; // ainda nao pode atualizar essa atividade. Tem que esperar ate que o resultado da usina esteja disponivel
      }
      
      // calcula a quantidade de perda de pellet feed para esta campanha
      double taxaDeGeracaoPellet = atividadeCampanha.getCampanha().getMetaUsina().retornaStatusHorario(atividade.getDtFim()).getTaxaGeracaoPellet();
      
      // só deve gerar sinter feed para pelota      
      if (taxaDeGeracaoPellet > 0 && atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA))
      {
         double quantidadePerdaPellet = quantidadeCampanha * taxaDeGeracaoPellet;
         mapaPerdaSinterFeedPorCampanha.put(atividadeCampanha, new Double(quantidadePerdaPellet));
      }
      
      
      // indice do status a partir do qual deve propagar a modificacao na carga
      int indiceInicioAtualizacao = cargaAtividade.getMetaCarga().getIndiceIntervaloCorrespondente(atividade.getDtFim());
      int sizeListaStatusCarga = cargaAtividade.getMetaCarga().getSizeListaStatus();
      long chave = 1l;
      for (int j = indiceInicioAtualizacao; j < sizeListaStatusCarga; j++)
      {
         Carga proximoStatusCarga = cargaAtividade.getMetaCarga().getListaStatus().get(j);
         List<Rastreabilidade> listaRastreabilidadesJaExistentes = proximoStatusCarga.getProduto().getListaDeRastreabilidades();
         if (proximoStatusCarga != null) 
         {
            Campanha campanha = atividade.getListaDeAtividadesCampanha().get(0).getCampanha();
            ControladorCalculoQualidade controladorCQ = new ControladorCalculoQualidade();
            
            // atualiza o produto da carga
            Blendagem blendagemResultante;
            Produto produtoCarga = proximoStatusCarga.getProduto();
            
            try 
            {
               blendagemResultante = controladorCQ.atenderCarga(proximoStatusCarga);
               blendagemResultante.inserirProdutoNaBlendagem(proximoStatusCarga.getProduto(), quantidadeCampanha);
               produtoCarga.setTipoProduto(blendagemResultante.getProdutoResultante().getTipoProduto());
               produtoCarga.setQuantidade(blendagemResultante.getProdutoResultante().getQuantidade());
               Qualidade qualidadeOriginal = produtoCarga.getQualidade();
               List<ItemDeControleQualidade> listaDeItensDeControleQualidadeOriginal = qualidadeOriginal.getListaDeItensDeControleQualidade();
               for (ItemDeControleQualidade icq : listaDeItensDeControleQualidadeOriginal) {
            	   icq.setQualidade(null);
               }
               List<ItemDeControleQualidade> listaDeItensDeControleNaBaliza = blendagemResultante.getProdutoResultante().getQualidade().getListaDeItensDeControleQualidade();
               for (ItemDeControleQualidade ic : listaDeItensDeControleNaBaliza)
               {
            	   ic.setQualidade(qualidadeOriginal);
            	   
               }
               qualidadeOriginal.getListaDeItensDeControleQualidade().addAll(listaDeItensDeControleNaBaliza);
               
               
               //proximoStatusCarga.setProduto(blendagemResultante.getProdutoResultante());         
            } 
            catch (Exception e) 
            {
               // TODO Auto-generated catch block
               logger.error("Erro ao gerar blendagem na recuperacao direto da usina",e);
            }
            String nomePorao = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getNomePorao();
            proximoStatusCarga.getProduto().addRastreabilidade(listaRastreabilidadesJaExistentes);
            Rastreabilidade rastreabilidade = acrescentaRastreabilidade(atividade.getDtInicio(), atividade.getDtFim(), null, campanha,
                  quantidadeCampanha, nomePorao, proximoStatusCarga.getProduto(),chave);
            chave++;
            if (proximoStatusCarga.getProduto().getListaDeRastreabilidades() == null) {
               proximoStatusCarga.getProduto().addRastreabilidade(new ArrayList<Rastreabilidade>());
            }
            proximoStatusCarga.getProduto().addRastreabilidade(rastreabilidade);
            
            //proximoStatusCarga.getProduto().addRastreabilidade(rastreabilidade);
            
            long chaveR = 1l;
            for (Rastreabilidade rast : proximoStatusCarga.getProduto().getListaDeRastreabilidades()) {
                rast.setNumeroRastreabilidade(chave);
                chaveR++;
            }
            
         }
      }

      if (atividade.getListaDeAtividadesCampanha() != null && atividade.getListaDeAtividadesCampanha().size() > 0) {
        atividade.getListaDeAtividadesCampanha().get(0).setQuantidade(quantidadeCampanha);
      }
      
      /** atualiza a quantidade da perda de sinter feed */
      ControladorExecutarPerdaPellet controladorPerdaPellet = ControladorExecutarPerdaPellet.getInstance();
      controladorPerdaPellet.empilharPerdaPellet(atividade,atividade.getDtFim(), atividade.getListaDeAtividadesCampanha(), mapaPerdaSinterFeedPorCampanha);
      
      atividade.setQuantidadeAtualizadaPeloMES(Boolean.TRUE);
      return true;
   }

   private boolean atualizaQuantidadesRecuperadora(Map<String, List<IntegracaoMES>> map, MESFacade mesFacade, 
            PlanoEmpilhamentoRecuperacao planoAtualizacaoAtual, Atividade atividade, SituacaoPatio situacaoPatioAtual) throws ErroSistemicoException
   {
      // 1.1) verificar se existem atividades de usina para o navio durante o periodo da atividade
      List<Atividade> listaAtividadesUsinaParalelas = new ArrayList<Atividade>();
      for (SituacaoPatio sit : planoAtualizacaoAtual.getListaSituacoesPatio()) 
      {
        if (sit.getAtividade() != null && sit.getAtividade().getFinalizada() && sit.getAtividade().getTipoAtividade() == TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO) 
         {
            if (sit.getAtividade().getListaDeAtividadesCampanha() != null && sit.getAtividade().getListaDeAtividadesCampanha().size() > 0) 
            {
               Date horaInicioUsina = sit.getAtividade().getDtInicio();
               Date horaTerminoUsina = sit.getAtividade().getDtFim();
               
               if (horaInicioUsina.getTime() >= atividade.getDtInicio().getTime() && horaInicioUsina.getTime() <= atividade.getDtFim().getTime()) {
                  listaAtividadesUsinaParalelas.add(sit.getAtividade());
               }
               else if (horaTerminoUsina.getTime() >= atividade.getDtInicio().getTime() && horaTerminoUsina.getTime() <= atividade.getDtFim().getTime()) {
                  listaAtividadesUsinaParalelas.add(sit.getAtividade());
               }
            }
         }
      }
      
      // para cada atividade de usina em paralelo deve verificar se os dados de quantidade ja estao disponiveis
      Double quantidadeTotalUsinas = new Double(0);
      for (Atividade atividadeUsina : listaAtividadesUsinaParalelas) 
      {  
         Double result = mesFacade.calcular(atividade.getDtInicio(), atividade.getDtFim(), atividadeUsina.getListaDeAtividadesCampanha().get(0).getCampanha(), map, atividade.getCarga().getProduto().getTipoProduto());
         if (result == null) 
         {
            return false; // ainda nao pode atualizar essa atividade. Tem que esperar atÃƒÂ© que o resultado da usina esteja disponivel
         }
         else 
         {
            quantidadeTotalUsinas += result;
         }
      }

      // efetua a leitura do PIMS para as quantidades da C03 e da retomadora
      String tagBalancaBerco = obterTagBalancaC3DoBerco(atividade);
      if (tagBalancaBerco.isEmpty()) {
         return false; // o berco do navio atracado esta sem a tag da balanca relacionada
      }
      System.out.println("Tag balanca C3 = " + tagBalancaBerco);
      Double quantidadeC3 = pimsFacade.calcular(atividade.getDtInicio(), atividade.getDtFim(), tagBalancaBerco);
      if (quantidadeC3 == null) 
      {
         return false; // quantidade da C03 nao esta disponivel
      }
      System.out.println("Quantidade C3 = " + quantidadeC3);
      
      /*TODO
      TAG DA Recuperadadora eh o mesmo da Retomadora
      RETIRAR QUANDO ELA TIVER BALANCA
      MUDARA REGRA ABAIXO NO CASO DA BALANCA SER DA RECUPERADORA
      IDEM atualizaQuantidadesRetomadora
       */
      String tagBalancaMaquina = obterTagBalancaMaquina(atividade);
      System.out.println("Tag Retomadora  = " + tagBalancaMaquina);
      if (tagBalancaMaquina.isEmpty()) 
      {
         return false; // tag da balanca da maquina nao cadastrada
      }
      Double quantidadeRetomadora = pimsFacade.calcular(atividade.getDtInicio(), atividade.getDtFim(), tagBalancaMaquina);
      if (quantidadeRetomadora == null) 
      {
         return false; // quantidade da retomadora nao esta disponivel
      }
      System.out.println("Quantidade Retomadora = " + quantidadeRetomadora);
      
      System.out.println("Valor Recuperadora = quantidadeC3 - quantidadeRetomadora - quantidadeTotalUsinas = " + quantidadeC3 + " - " + quantidadeRetomadora + " - " + quantidadeTotalUsinas);
      // calcula a quantidade que foi recuperada pela recuperadora
      Double quantidadeRecuperadora = quantidadeC3 - quantidadeRetomadora - quantidadeTotalUsinas;
      System.out.println(quantidadeRecuperadora);
      
      if (quantidadeRecuperadora < 0) 
      {
        return false; // quantidade da Recuperadora deu negativo nao esta disponivel
      }

      atualizaQuantidadesBalizas(atividade, quantidadeRecuperadora, situacaoPatioAtual);

      if (atividade.getListaDeLugaresDeEmpilhamentoRecuperacao() != null && atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().size() > 0) 
      {
         atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).setQuantidade(quantidadeRecuperadora);
      }
      atividade.setQuantidadeAtualizadaPeloMES(Boolean.TRUE);
      
      return true;
   }

   private boolean atualizaQuantidadesRetomadora(Atividade atividade, SituacaoPatio situacaoPatioAtual) throws ErroSistemicoException
   {
      // efetua a leitura do PIMS para a quantidade da retomadora
      String tagBalancaMaquina = obterTagBalancaMaquina(atividade);
      if (tagBalancaMaquina.isEmpty()) 
      {
         return false; // tag da balanca da maquina nao cadastrada
      }
      Double quantidadeRetomadora = pimsFacade.calcular(atividade.getDtInicio(), atividade.getDtFim(), tagBalancaMaquina);
      if (quantidadeRetomadora == null) 
      {
         return false; // quantidade da retomadora nao esta disponivel
      }
      
      atualizaQuantidadesBalizas(atividade, quantidadeRetomadora, situacaoPatioAtual);
     
      if (atividade.getListaDeLugaresDeEmpilhamentoRecuperacao() != null && atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().size() > 0) {
         atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).setQuantidade(quantidadeRetomadora);
      }
      atividade.setQuantidadeAtualizadaPeloMES(Boolean.TRUE);
      return true;
   }

   private void atualizaQuantidadesBalizas(Atividade atividade, Double quantidade, SituacaoPatio situacaoPatioAtual)
   {
      // quantidade inicial da carga antes da recuperacao
      double quantidadeInicialCarga = atividade.getCarga().getProduto().getQuantidade();
      
      List<Baliza> listaBalizasAtividade = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas();
      int sizeBalizas = listaBalizasAtividade.size();
      
      // RETIRADA DO MATERIAL DAS BALIZAS
      // define como vai ser a retirada do material recuperado das balizas:
      // 1) para material do tipo pelota recupera baliza a baliza no sentido da recuperacao ate atingir a quantidade estipulada
      // 2) para material do tipo pellet feed ou sinter feed distribui a retirada por todas as balizas

      // mapa para apontar realmente quanto foi retirado de cada baliza (e necessario para calculo de qualidade resultante da carga)
      HashMap<Integer, Double> mapaQuantidadeRetiradaDeBaliza = new HashMap<Integer, Double>();
      DecimalFormat twoDForm = new DecimalFormat("#.##");
      TipoProduto tipoProduto = atividade.getCarga().getProduto().getTipoProduto();
      
      if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA) || tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA_PSM)) 
      {
         // 1) PELOTA
         // verifica se tem material suficiente nas balizas para atender a quantidade especificada
         double quantidadeTotalBalizas = 0;
         
         for (Baliza bal : listaBalizasAtividade) 
         {             
            if (bal.getProduto() != null && bal.getRetirandoMaterial() == 1) 
            {
               quantidadeTotalBalizas += bal.getProduto().getQuantidade();
            }  
         }
         // se a quantidade recuperada for maior ou igual a quantidade total nas balizas entao zera todas as balizas
         if (quantidade >= quantidadeTotalBalizas) 
         {
            for (Baliza bal : listaBalizasAtividade) 
            {
               if (bal.getProduto() != null && bal.getRetirandoMaterial() == 1) 
               {
                  double quantidadeRetiradaBaliza = bal.getProduto().getQuantidade();
                  mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), new Double(bal.getProduto().getQuantidade()));
                  // atualiza os status da baliza retirando a quantidade
                  int indiceInicioAtualizacao = bal.getMetaBaliza().getIndiceIntervaloCorrespondente(atividade.getDtFim());
                  int sizeListaStatusBaliza = bal.getMetaBaliza().getSizeListaStatus();
                  for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++)
                  {
                     Baliza proximoStatusBaliza = bal.getMetaBaliza().getListaStatus().get(j);
                     // atualiza a quantidade no status da baliza
                     if (proximoStatusBaliza.getProduto() != null)
                     {
                        double quantidadeResultanteBaliza = Math.max(0d, proximoStatusBaliza.getProduto().getQuantidade() - quantidadeRetiradaBaliza);
                        proximoStatusBaliza.getProduto().setQuantidade(quantidadeResultanteBaliza);
                     }
                  }
               }
               else 
               {
                  mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), new Double(0));
               }
            }
         }
         // se tem mais material nas balizas do que o recuperado entao retira o material baliza a baliza
         else 
         {
            // verifica a ordem em que as balizas sao recuperadas
            List<Baliza> balizasOrdenadas = listaBalizasAtividade;
            Collections.sort(balizasOrdenadas,Baliza.comparadorBaliza);
            if (atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getSentido() == SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL) 
            {
               Collections.reverse(balizasOrdenadas);
            }
            double quantidadePendente = quantidade;
            while (quantidadePendente > 0) 
            {
               for (Baliza bal : balizasOrdenadas) 
               {
                  if (bal.getProduto() != null && bal.getRetirandoMaterial() == 1) 
                  {
                     // determina quanto vai retirar da baliza
                     double quantidadeRecuperadaBaliza = Math.min(bal.getProduto().getQuantidade(), quantidadePendente);
                     quantidadePendente -= quantidadeRecuperadaBaliza;
                     mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), new Double(quantidadeRecuperadaBaliza));
                     
                     // atualiza os status da baliza retirando a quantidade
                     int indiceInicioAtualizacao = bal.getMetaBaliza().getIndiceIntervaloCorrespondente(atividade.getDtFim());
                     int sizeListaStatusBaliza = bal.getMetaBaliza().getSizeListaStatus();
                     for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++)
                     {
                        Baliza proximoStatusBaliza = bal.getMetaBaliza().getListaStatus().get(j);
                        double quantidadeResultanteBaliza = Math.max(0d, proximoStatusBaliza.getProduto().getQuantidade() - quantidadeRecuperadaBaliza);
                        proximoStatusBaliza.getProduto().setQuantidade(quantidadeResultanteBaliza);
                     }
                  }  
                  else 
                  {
                     mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), new Double(0));
                  }
               }
            }
         }
      }
      else 
      {
         // 2) PELLET FEED / SINTER FEED      
         // calcula a quantidade que deve ser retirada de cada baliza
         double quantidadeRetirarPorBaliza = quantidade / sizeBalizas;

         // lista com as balizas que ainda tem produto. Essa lista eh utilizada como auxiliar para verificar se existe alguma
         // baliza que nao contem a quantidade que deve ser subtraida. Neste caso distribui a retirada pelas balizas restantes
         List<Baliza> listaBalizasComProduto = new ArrayList<Baliza>();
         listaBalizasComProduto.addAll(listaBalizasAtividade);

         boolean distribuiuRetirada = false;
         
         while (!distribuiuRetirada) 
         {
            distribuiuRetirada = true;
            for (Baliza bal : listaBalizasComProduto) 
            {
               if (bal.getProduto() != null && bal.getRetirandoMaterial() == 1) 
               {                 
                  // verifica se pode retirar toda a quantidade desta baliza                  
            	   Double saldoDaPilha = bal.getProduto().getQuantidade() - quantidadeRetirarPorBaliza;                                        
                   saldoDaPilha = DSSStockyardFuncoesNumeros.arredondarDoubleParaCimaAPartirDeNumCasasDecimais(saldoDaPilha,1);
                  if (saldoDaPilha  <= 0) {
                     double quantidadeRecuperadaBaliza = bal.getProduto().getQuantidade();
                     mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), new Double(quantidadeRecuperadaBaliza));
                     distribuiuRetirada = false;
                     listaBalizasComProduto.remove(bal);
                     quantidadeRetirarPorBaliza += Math.abs(saldoDaPilha / listaBalizasComProduto.size());
                     
                     // atualiza os status da baliza retirando a quantidade
                     int indiceInicioAtualizacao = bal.getMetaBaliza().getIndiceIntervaloCorrespondente(atividade.getDtFim());
                     int sizeListaStatusBaliza = bal.getMetaBaliza().getSizeListaStatus();
                     // verifica se é necessario criar um novo status
                     if (indiceInicioAtualizacao == sizeListaStatusBaliza)
                     {
                        Baliza novoStatusBaliza = bal.getMetaBaliza().clonarStatus(atividade.getDtFim());
                        sizeListaStatusBaliza++;
                     }
                     for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++)
                     {
                        Baliza proximoStatusBaliza = bal.getMetaBaliza().getListaStatus().get(j);
                        double quantidadeResultanteBaliza = Math.max(0d, proximoStatusBaliza.getProduto().getQuantidade() - quantidadeRecuperadaBaliza);
                        proximoStatusBaliza.getProduto().setQuantidade(quantidadeResultanteBaliza);
                     }
                     break;
                  }
               } 
               else 
               {
                  mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), new Double(0)); 
               }
            }   
         }

         // distribui as retiradas pelas pilhas restantes
         for (Baliza bal : listaBalizasComProduto) 
         {
            if (bal.getProduto() != null && bal.getRetirandoMaterial() == 1) 
            {                 
               mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), quantidadeRetirarPorBaliza);
               
               // atualiza os status da baliza retirando a quantidade
               int indiceInicioAtualizacao = bal.getMetaBaliza().getIndiceIntervaloCorrespondente(atividade.getDtFim());
               int sizeListaStatusBaliza = bal.getMetaBaliza().getSizeListaStatus();
               for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++)
               {
                  Baliza proximoStatusBaliza = bal.getMetaBaliza().getListaStatus().get(j);
                  double quantidadeResultanteBaliza = Math.max(0d, proximoStatusBaliza.getProduto().getQuantidade() - quantidadeRetirarPorBaliza);
                  proximoStatusBaliza.getProduto().setQuantidade(quantidadeResultanteBaliza);
               }      
            }   
         }
      }

      // 5) atualiza a qualidade da carga
      String nomePorao = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getNomePorao();
      HashMap<Carga, List<Rastreabilidade>> mapaNovasRastreabilidades = new HashMap<Carga, List<Rastreabilidade>>();
      HashMap<Carga, List<Rastreabilidade>> mapaRastreabilidadesJaExistentes = new HashMap<Carga, List<Rastreabilidade>>();

      // indice do status a partir do qual deve propagar a modificacao na carga
      try 
      {
         boolean precisaDecomporPilha = false;
         Pilha pilhaAtividade = listaBalizasAtividade.get(0).retornaStatusHorario(atividade.getDtFim());
         atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDePilhasEditadas().add(pilhaAtividade);
         int indiceInicioAtualizacao = atividade.getCarga().getMetaCarga().getIndiceIntervaloCorrespondente(atividade.getDtFim());
         int sizeListaStatusCarga = atividade.getCarga().getMetaCarga().getSizeListaStatus();
         for (int j = indiceInicioAtualizacao; j < sizeListaStatusCarga; j++)
         {
            Carga proximoStatusCarga = atividade.getCarga().getMetaCarga().getListaStatus().get(j);
            List<Rastreabilidade> listaRastreabilidadesJaExistentes = mapaRastreabilidadesJaExistentes.get(proximoStatusCarga);
            if (listaRastreabilidadesJaExistentes == null)
               listaRastreabilidadesJaExistentes = new ArrayList<Rastreabilidade>();
            listaRastreabilidadesJaExistentes.addAll(proximoStatusCarga.getProduto().getListaDeRastreabilidades());
            mapaRastreabilidadesJaExistentes.put(proximoStatusCarga,listaRastreabilidadesJaExistentes);
            ControladorCalculoQualidade controladorCQ = new ControladorCalculoQualidade();
            Blendagem blendagemResultante = controladorCQ.atenderCarga(proximoStatusCarga);
            long chave = 1l;
            for (Baliza bal : listaBalizasAtividade) 
            {               
               Double quantidadeRecuperada = mapaQuantidadeRetiradaDeBaliza.get(bal.getNumero());
               if (quantidadeRecuperada != null && quantidadeRecuperada.doubleValue() > 0) 
               {
                  blendagemResultante.inserirProdutoNaBlendagem(bal.getProduto(), quantidadeRecuperada);
                  
                  
                  proximoStatusCarga.getProduto().setTipoProduto(blendagemResultante.getProdutoResultante().getTipoProduto());
                  proximoStatusCarga.getProduto().setQuantidade(blendagemResultante.getProdutoResultante().getQuantidade());

                  
                  /**
                   * TODO
                   * 
                   * ATUALIZA OS DADOS DA QUALIDADE
                   * 
                   */
                  
                  /*if (proximoStatusCarga.getProduto() != null)
                  {
                     listaDeItensDeControleNaBaliza = proximoStatusCarga.getProduto().getQualidade().getListaDeItensDeControle();
                     for (ItemDeControle ic : listaDeItensDeControleNaBaliza)
                     {
                        ItemDeControleQualidade icq = (ItemDeControleQualidade) ic;
                        // verifica se o item ja foi atualizado para esta usina
                        if (!listaStringEntityContemString(icq.getUsinas(), campanha.getMetaUsina().getNomeUsina()))
                        {
                           String chaveFaseItem = montaChaveFaseItem(campanha, icq, tipoProdutoBaliza);
                           // para cada item de controle do produto da baliza:
                           // i) verifica se ja sairam todas as amostras relativas ao periodo considerado da atividade
                           List<IntegracaoMES> listaQualidades = mapaDataIntegracaoMESAmostras.get(chaveFaseItem);
                           List<Double> listaQuantidades = mapaQuantidades.get(chaveFaseItem);
                           if (listaQualidades != null && listaQuantidades != null)
                           {
                              int sizeListaQuantidades = listaQuantidades.size();
                              int sizeListaQualidades = listaQualidades.size();
                              if (sizeListaQualidades > 0 && sizeListaQualidades == sizeListaQuantidades) // as
                              // duas listas devem ter o mesmo tamanho pois para cada bi-horaria de qualidade tem que ter uma
                              // quantidade equivalente
                              {
                                 precisaConsolidarDados = true;
                                 StringBuffer usinas = new StringBuffer();
                                 if (icq.getUsinas() != null && icq.getUsinas().length() > 0)
                                 {
                                    usinas.append(icq.getUsinas()).append(campanha.getMetaUsina().getNomeUsina()).append(";");
                                    icq.setUsinas(usinas.toString());
                                 } else
                                 {
                                    usinas.append(campanha.getMetaUsina().getNomeUsina()).append(";");
                                    icq.setUsinas(usinas.toString());
                                 }
                                 double denominador = 0;
                                 double numerador = 0;
                                 if (icq.getValor() != null && icq.getValor().doubleValue() > 0)
                                 {
                                    denominador = baliza.getProduto().getQuantidade();
                                    numerador = baliza.getProduto().getQuantidade() * icq.getValor();
                                 }
                                 for (int i = 0; i < sizeListaQualidades; i++)
                                 {
                                    double valorQualidade = listaQualidades.get(i).getValorLeitura();
                                    double valorQuantidade = listaQuantidades.get(i);
                                    denominador += valorQuantidade;
                                    numerador += valorQualidade * valorQuantidade;
                                 }
                                 if (denominador > 0)
                                 {
                                    icq.setValor(numerador / denominador);
                                 } else
                                 {
                                    icq.setValor(null);
                                 }
                              }
                           }
                        }
                     }
                     proximoStatusCarga.getProduto().getQualidade().setEhReal(true);
   */               
                  
                  
                  //proximoStatusCarga.setProduto(blendagemResultante.getProdutoResultante());
   
                  // 6) acrescenta os registros de rastreabilidade no produto
                  Rastreabilidade rastreabilidade = acrescentaRastreabilidade(atividade.getDtInicio(), atividade.getDtFim(), bal, null, quantidadeRecuperada, nomePorao, proximoStatusCarga.getProduto(),chave);
                  chave++; 
                  List<Rastreabilidade> listaNovasRastreabilidades = mapaNovasRastreabilidades.get(proximoStatusCarga);
                  if (listaNovasRastreabilidades == null)
                     listaNovasRastreabilidades = new ArrayList<Rastreabilidade>();
                  listaNovasRastreabilidades.add(rastreabilidade);
                  mapaNovasRastreabilidades.put(proximoStatusCarga, listaNovasRastreabilidades);
                }
          
               // 8) verifica se finalizou a baliza da pilha
               /// verificar se existe alguma maquina fazendo um empilhamento 
               /*
                * TODO 11079 TESTAR SE EXISTE MAQUINA EMPILHANDO NA MESMA BALIZA
                */
               Boolean matarBaliza = ControladorMaquinas.existeEmpilhamentoParalelo(bal, situacaoPatioAtual);
               System.out.println("Existe empilhamento paralelo  = " + matarBaliza);
              
                                                       
               Double saldoDaPilha = DSSStockyardFuncoesNumeros.arredondarDoubleParaCimaAPartirDeNumCasasDecimais(bal.getProduto().getQuantidade(),1);
               if (bal.getProduto() != null && saldoDaPilha <= 0 && !matarBaliza) 
               {
                  bal.setProduto(null);
                  bal.setHorarioFimFormacao(null);
                  bal.setHorarioInicioFormacao(null);
                  precisaDecomporPilha = true;
               }
            }
         }
         if (precisaDecomporPilha)
         {
            this.decompoePilhaEditada(pilhaAtividade, situacaoPatioAtual.getDtInicio());
         }
      }
      catch (Exception ex) 
      {
         // neste ponto nao precisa mais verificar a compatibilidade dos produtos. Isso ja foi feito anteriormente.
         // desta forma, nao eh preciso tratar as excecoes de compatilidade aqui
         logger.error("Erro ao gerar blendagem na recuperacao das balizas",ex);
         ex.printStackTrace();
      }
      
      // Atualizacao de rastreabilidades
      // indice do status a partir do qual deve propagar a modificacao na carga
      int indiceInicioAtualizacao = atividade.getCarga().getMetaCarga().getIndiceIntervaloCorrespondente(atividade.getDtFim());
      int sizeListaStatusCarga = atividade.getCarga().getMetaCarga().getSizeListaStatus();
      
      
   
      for (int j = indiceInicioAtualizacao; j < sizeListaStatusCarga; j++)
      {
         Carga proximoStatusCarga = atividade.getCarga().getMetaCarga().getListaStatus().get(j);
         proximoStatusCarga.getProduto().addRastreabilidade(mapaRastreabilidadesJaExistentes.get(proximoStatusCarga));
         proximoStatusCarga.getProduto().addRastreabilidade(mapaNovasRastreabilidades.get(proximoStatusCarga));         
         proximoStatusCarga.getProduto().setQuantidade(quantidadeInicialCarga + quantidade);
         
      }
   }



   /* Acrescenta os registros de rastreabilidade no produto */
   private Rastreabilidade acrescentaRastreabilidade(Date horaInicio, Date horaTermino, Baliza balizaOrigem, Campanha campanha,
                                                     double quantidadeEmpilhada, String nomePorao, Produto produto, long chave)
   {
      // para as atividades pontuais nao eh necessario incluir a rastreabilidade
      if (horaInicio.getTime() == horaTermino.getTime()) {
         return null;
      }

      Rastreabilidade rastreabilidade = new Rastreabilidade();
      rastreabilidade.setHorarioInicioEntradaDeMaterial(horaInicio);
      rastreabilidade.setHorarioFimEntradaDeMaterial(horaTermino);
      rastreabilidade.setDtInicio(horaInicio);
      rastreabilidade.setDtFim(horaTermino);
      rastreabilidade.setDtInsert(horaInicio);
      rastreabilidade.setIdUser(1l);
      rastreabilidade.setNumeroRastreabilidade(chave);
      if (campanha != null) {
         
         rastreabilidade.setNomeUsina(campanha.getMetaUsina().getNomeUsina());
         rastreabilidade.setTaxaDeOperacaoUsina(campanha.getMetaUsina().retornaStatusHorario(horaInicio).getTaxaDeOperacao());
         rastreabilidade.setDataInicioUsina(campanha.getDataInicial());
         rastreabilidade.setDataFimUsina(campanha.getDataFinal());
         rastreabilidade.setNomeCampanhaUsina(campanha.getNomeCampanha());
         rastreabilidade.setTipoProdutoUsina(campanha.getTipoProduto());
         rastreabilidade.setTipoPelletUsina(campanha.getTipoPellet());
         rastreabilidade.setTipoScreeningUsina(campanha.getTipoScreening());            
         //rastreabilidade.setUsinaDeOrigem(campanha.getUsina());
      }
      rastreabilidade.setTipoAtividade(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO);
      rastreabilidade.setQuantidade(quantidadeEmpilhada);
      if (balizaOrigem != null) {
         rastreabilidade.setNomeBaliza(balizaOrigem.getNomeBaliza());
         rastreabilidade.setNumeroBaliza(balizaOrigem.getNumero());
         rastreabilidade.setLarguraBaliza(balizaOrigem.getLargura());
         rastreabilidade.setDtHoraIniFormBaliza(balizaOrigem.getHorarioInicioFormacao());
         rastreabilidade.setDtHoraFimFormBaliza(balizaOrigem.getHorarioFimFormacao());
         rastreabilidade.setCapacMaxBaliza(balizaOrigem.getCapacidadeMaxima());
         rastreabilidade.setNomePatioBaliza(balizaOrigem.getPatio().getNomePatio());
         rastreabilidade.setNumPatioBaliza(balizaOrigem.getPatio().getNumero());
         rastreabilidade.setNomePilhaBaliza(balizaOrigem.retornaStatusHorario(horaTermino).getNomePilha());
      }
      rastreabilidade.setNomePorao(nomePorao);
      
      produto.addRastreabilidade(rastreabilidade);

      return rastreabilidade;
   }

   public IControladorIntegracao getControladorIntegracao()
   {
      return controladorIntegracao;
   }

   /**
    * @return the controladorModelo
    */
   public IControladorModelo getControladorModelo()
   {
      return controladorModelo;
   }

   /**
    * @param controladorModelo the controladorModelo to set
    */
   public void setControladorModelo(IControladorModelo controladorModelo)
   {
      this.controladorModelo = controladorModelo;
   }

   public void setControladorIntegracao(IControladorIntegracao controladorIntegracao)
   {
      this.controladorIntegracao = controladorIntegracao;
   }

   private String obterTagBalancaC3DoBerco(Atividade atividadeRecuperacao)
   {
      String tagBalancaC3 = "";
      if (atividadeRecuperacao.getCarga() != null) {
         tagBalancaC3 = atividadeRecuperacao.getCarga().getNavio(atividadeRecuperacao.getDtFim()).getBercoDeAtracacao().getTagPims();
      }

      return tagBalancaC3;
   }

   private String obterTagBalancaMaquina(Atividade atividadeRecuperacao)
   {
      String tagBalancaMaquina = "";
      tagBalancaMaquina = atividadeRecuperacao.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getMaquinaDoPatio().getTagPimsBalanca();
      if (tagBalancaMaquina == null) {
         tagBalancaMaquina = "";
      }
      return tagBalancaMaquina;
   }

   /**
 * decompoePilhaEditada - decomposição de pilha do pims pode gerar novo status
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 21/07/2010
 * @see
 * @param 
 * @return void
 */
private void decompoePilhaEditada(Pilha pilhaEditada, Date horarioAtividade)
   {
      List<Baliza> novaListaBalizas = new ArrayList<Baliza>();      
      int i = 0;
      Collections.sort(pilhaEditada.getListaDeBalizas(), Baliza.comparadorBaliza);
      StringBuffer value = new StringBuffer();
      int contadorPilhas = 0;
      
      for ( Baliza balizaPilha : pilhaEditada.getListaDeBalizas()) 
      {
    	  
    	  Baliza balizaAnterior = (i > 0) ? pilhaEditada.getListaDeBalizas().get(i - 1) : balizaPilha;
    	  int idx = balizaAnterior.getNumero() - balizaPilha.getNumero();
          // verifica se o produto da baliza e a pilha da baliza possui objetos
          if (pilhaEditada != null && balizaPilha.getProduto() != null) 
          {
        	if (balizaAnterior.getProduto() == null
						&& balizaPilha.getProduto() != null &&  (idx  == 0 || idx == 1|| idx == -1)) {
					novaListaBalizas.add(balizaPilha);
				} else if (balizaPilha.getProduto().getTipoProduto()
						.equals(balizaAnterior.getProduto().getTipoProduto()) && (idx  == 0 || idx == 1|| idx == -1)) {
					novaListaBalizas.add(balizaPilha);               
				}
            else 
            {
               // limpando o objeto pilha da baliza
               int indiceInicioAtualizacao = balizaPilha.getMetaBaliza().getIndiceIntervaloCorrespondente(horarioAtividade);
               int sizeListaStatusBaliza = balizaPilha.getMetaBaliza().getSizeListaStatus();
               // verifica se é necessario criar um novo status
               if (indiceInicioAtualizacao == sizeListaStatusBaliza)
               {
                  Baliza novoStatusBaliza = balizaPilha.getMetaBaliza().clonarStatus(horarioAtividade);
                  sizeListaStatusBaliza++;
               }
               for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++)
               {
                  Baliza proximoStatusBaliza = balizaPilha.getMetaBaliza().getListaStatus().get(j);                                                      
                  //pilhaEditada.getListaDeBalizas().remove(proximoStatusBaliza);
                  //proximoStatusBaliza.getPilhas().remove(pilhaEditada);
                  //proximoStatusBaliza.setPilhas(null);
                  proximoStatusBaliza.setHorarioInicioFormacao(null);                    
                  proximoStatusBaliza.setHorarioFimFormacao(null);
               }

               // cria uma nova pilha qdo encontrado elementos nulos na lista de balizas da pilha
               contadorPilhas++;           
               value = new StringBuffer();
               value.append(pilhaEditada.getNomePilha()).append("_").append(contadorPilhas);
               Collections.sort(novaListaBalizas, new ComparadorBalizas());
               pilhaEditada.setDtFim(horarioAtividade);
               Pilha.criaPilha(value.toString(), novaListaBalizas, pilhaEditada.getCliente(), horarioAtividade.getTime(),pilhaEditada);
               
               novaListaBalizas = new ArrayList<Baliza>();
               novaListaBalizas.add(balizaPilha);
            }
         }
         else if (pilhaEditada != null && balizaPilha.getProduto() == null) 
         {
            // limpando o objeto pilha da baliza
            int indiceInicioAtualizacao = balizaPilha.getMetaBaliza().getIndiceIntervaloCorrespondente(horarioAtividade);
            int sizeListaStatusBaliza = balizaPilha.getMetaBaliza().getSizeListaStatus();
            // verifica se é necessario criar um novo status
            if (indiceInicioAtualizacao == sizeListaStatusBaliza)
            {
               Baliza novoStatusBaliza = balizaPilha.getMetaBaliza().clonarStatus(horarioAtividade);
               sizeListaStatusBaliza++;
            }
            for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++)
            {
               Baliza proximoStatusBaliza = balizaPilha.getMetaBaliza().getListaStatus().get(j);
               //pilhaEditada.removeBaliza(proximoStatusBaliza);
               //proximoStatusBaliza.getPilhas().remove(pilhaEditada);
               //proximoStatusBaliza.setPilhas(null);
               proximoStatusBaliza.setHorarioInicioFormacao(null);                    
               proximoStatusBaliza.setHorarioFimFormacao(null);
            }

            // verifica se a baliza anterior possui produto para a criacao da nova pilha
            if (balizaAnterior.retornaStatusHorario(horarioAtividade) != null && balizaAnterior.getProduto() != null) 
            {
               // cria uma nova pilha qdo encontrado elementos nulos na lista de balizas da pilha
               contadorPilhas++;
               value = new StringBuffer();
               value.append(pilhaEditada.getNomePilha()).append("_").append(contadorPilhas);
               Collections.sort(novaListaBalizas, new ComparadorBalizas());               
               pilhaEditada.setDtFim(horarioAtividade);
               Pilha.criaPilha(value.toString(), novaListaBalizas, pilhaEditada.getCliente(), horarioAtividade.getTime(),pilhaEditada);             
               novaListaBalizas = new ArrayList<Baliza>();
            }
         }
	         //balizaAnterior =  balizaPilha;
          i++;
      }

      if (!novaListaBalizas.isEmpty()) 
      {
         // cria uma nova pilha qdo encontrado elementos nulos na lista de balizas da pilha
         contadorPilhas++;
         value = new StringBuffer();
         value.append(pilhaEditada.getNomePilha()).append("_").append(contadorPilhas);
         Collections.sort(novaListaBalizas, new ComparadorBalizas());
         pilhaEditada.setListaDeBalizas(new ArrayList<Baliza>());
         pilhaEditada.addBaliza(novaListaBalizas);
         //Pilha novaPilha = Pilha.criaPilha(value.toString(), novaListaBalizas, pilhaEditada.getCliente(), horarioAtividade.getTime());
      }
   }

}
