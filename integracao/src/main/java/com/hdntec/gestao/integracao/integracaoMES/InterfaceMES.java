package com.hdntec.gestao.integracao.integracaoMES;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.integracao.IControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.integracao.IntegracaoParametros;
import com.hdntec.gestao.domain.plano.comparadores.ComparadorSituacoesPatio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.produto.enums.TipoItemControleEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.integracao.facade.mes.MESFacade;
import com.hdntec.gestao.integracao.facade.mes.impl.MESFacadeImpl;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarPerdaPellet;

import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;

/**
 * Interface com o sistema externo MES.
 * 
 * Para atualizar as qualidades e quantidades percorre-se a lista de atividades aplicadas ao patio.
 * Quando uma atividade é de empilhamento e está finalizada deve-se atualizar a baliza correspondente e propagar a atualização para as
 * situações futuras.
 * A baliza que deve ser atualizada corresponde à baliza da atividade anterior, apontada pela atividade finalizada.
 * 
 * @author andre
 * 
 */
public class InterfaceMES
{
   private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("InterfacePims");
 
   /** Acesso ao subsistema Controlador do Plano de Empilhamento e Recuperacao */
   private IControladorModelo controladorModelo;
   
   /** Acesso ao subsistema Controlador das Integracoes */
   private IControladorIntegracao controladorIntegracao;
   
   /** Mapa com os dados ja adicionados na lista */
   private HashMap<Long, IntegracaoMES> mapaListaDadosProcessados;
   
   private static MESFacade mesFacade = new MESFacadeImpl();
   
   private static Date lastUpdateTime;
   
   public static Date getLastUpdateTime()
   {
      return lastUpdateTime;
   }
   
   public static Date defineCurrTime(Date data)
   {
      if (lastUpdateTime == null)
      {
         lastUpdateTime = new Date();
      }
      lastUpdateTime.setTime(data.getTime());
      return lastUpdateTime;
   }
   
   private boolean precisaConsolidarDados;
   
   /**
    * Atualiza os dados do sistema externo MES, a partir da ultimaAtualizacao.
    * 
    * @return a lista de atividades que aconteceram desde a ultima atualizacao
    */
   public PlanoEmpilhamentoRecuperacao atualizarDados(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao, Map<String, List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException
   {
      try
      {
         precisaConsolidarDados = false;
         
         // obtem os parametros do sistema MES
         IntegracaoParametros paramIntegracao = controladorIntegracao.buscarParametroSistema(this.getIdSistemaMES());
         
         // o plano a ser atualizado
         PlanoEmpilhamentoRecuperacao planoAtualizacaoAtual = planoEmpilhamentoRecuperacao;
         Collections.sort(planoAtualizacaoAtual.getListaSituacoesPatio(), new ComparadorSituacoesPatio());
         
         // para atualizar os dados vindos do MES temos que fazer as
         // seguintes tarefas:
         // 1) percorre a lista situacoes e analisa as atividades
         // 2) faz uma busca nos dados do MES utilizando como parametros a
         // usina, as datas de inicio e fim da atividade e os
         // itens de controle da baliza
         // 3) criar uma lista de amostras com os dados obtidos no MES para
         // qualidade e quantidade
         // 4) criar um ControladorQualidade passando a qualidade atual da
         // baliza como parametro
         // 5) insere a lista de amostras no ControladorQualidade
         // 6) seta a qualidade como real
         // 7) Gerar perda de pellet
         
         
         // 1)
         int sizeSituacoes = planoAtualizacaoAtual.getListaSituacoesPatio().size();
         /**
          * TODO REMOVER PARA PARAMETRO DO SISTEMA 11079
          * */
         int qtdSituacoesAtualizadas = 0;
         
         for (int indexSituacao = 1; indexSituacao < sizeSituacoes; indexSituacao++)
         {
            SituacaoPatio situacaoAtual = planoAtualizacaoAtual.getListaSituacoesPatio().get(indexSituacao);
            logger.info("Atualizando Mes situacao " + situacaoAtual);
            Atividade atividadePai = situacaoAtual.getAtividade();
            Atividade atividade = atividadePai.getAtividadeAnterior();
            
            if (atividadePai.getFinalizada() && atividade != null && 
                     atividade.getTipoAtividade() == TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO && !atividade.getQuantidadeAtualizadaPeloMES())
            {
               // 2) e 3)
               LugarEmpilhamentoRecuperacao lugarEmpRec = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
               // ATUALIZACAO DE QUANTIDADE
               if (!atividade.getQuantidadeAtualizadaPeloMES())
               {
                  logger.info("Inicio Atualizando Quantidade Atividade " +  atividade + "-" + atividade.getDtInicio()  + "-" + atividade.getDtFim());
                  atualizaQuantidadesEmpilhamentoUsinas(atividadePai, lugarEmpRec, map);
                  logger.info("Fim Atualizando Quantidade Atividade " + atividade + "-" + new Date(System.currentTimeMillis()));
                  qtdSituacoesAtualizadas++;
               }
               
               // ATUALIZACAO DE QUALIDADE
               logger.info("Inicio Atualizando Qualidades Atividade" + atividade + "-" + new Date(System.currentTimeMillis()));
               /*
                * TODO 11079 COMENTADO METODO QUE ATUALIZA QUALIDADES ANALISAR MANEIRA DE CONTROLAR QUALIDADES Q JÃ� FORAM ATULIZADAS E MECANISMO DE PROPAGAÃ‡ÃƒO
                */
               atualizaQualidadesEmpilhamentoUsinas(atividade, lugarEmpRec, map);
               logger.info("Fim Atualizando Qualidades Atividade" + atividade + "-" + new Date(System.currentTimeMillis()));
            }
         }
        
         logger.info("SituaÃ§Ãµes de PÃ¡tio atualizadas " + qtdSituacoesAtualizadas);
         return planoAtualizacaoAtual;
      } catch (Exception ex)
      {
         ex.printStackTrace();
         throw new ErroSistemicoException(ex.getMessage());
      }
   }
   
   private void atualizaQualidadesEmpilhamentoUsinas(Atividade atividade, LugarEmpilhamentoRecuperacao lugarEmpRec, Map<String, List<IntegracaoMES>> map) throws ErroSistemicoException
   {
	   Baliza baliza = lugarEmpRec.getListaDeBalizas().get(0);
       if (lugarEmpRec.getListaDeBalizas().size() > 1) {
           for (Baliza balizaI : lugarEmpRec.getListaDeBalizas()) {
               if (balizaI.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                   baliza = balizaI;
                   break;
               }    
           }    
       }
      
      // indice do status a partir do qual deve propagar a modificacao na baliza
      int indiceInicioAtualizacao = baliza.getMetaBaliza().getIndiceIntervaloCorrespondente(atividade.getDtFim());
      
      if (baliza.getProduto() != null && baliza.getProduto().getQualidade() != null)
      {
         List<ItemDeControle> listaDeItensDeControleNaBaliza = baliza.getProduto().getQualidade().getListaDeItensDeControle();
         TipoProduto tipoProdutoBaliza = baliza.getProduto().getTipoProduto();
         for (AtividadeCampanha atividadeCampanha : atividade.getListaDeAtividadesCampanha())
         {
            Campanha campanha = atividadeCampanha.getCampanha();
            // gera a lista de amostras de qualidade
            Map<String, List<IntegracaoMES>> mapaDataIntegracaoMESAmostras = geraListaDeIntegracaoMESAmostras(campanha, atividade.getDtInicio(), atividade.getDtFim(), listaDeItensDeControleNaBaliza, tipoProdutoBaliza);
            
            // gera a lista de quantidade empilhada no periodo
            // equivalente aos resultados da amostra do mapa acima
            Map<String, List<Double>> mapaQuantidades = geraMapaQuantidade(campanha, atividade.getDtInicio(), atividade.getDtFim(), mapaDataIntegracaoMESAmostras, map, tipoProdutoBaliza);
            
            // produtos sem qualidade
            if (mapaDataIntegracaoMESAmostras != null && mapaQuantidades != null)
            {
               // ajusta a qualidade do produto empilhado na baliza, propagando a mudança para os estados futuros
               int sizeListaStatusBaliza = baliza.getMetaBaliza().getSizeListaStatus();
               for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++)
               {
                  Baliza proximoStatusBaliza = baliza.getMetaBaliza().getListaStatus().get(j);
                  
                  if (proximoStatusBaliza.getProduto() != null)
                  {
                     listaDeItensDeControleNaBaliza = proximoStatusBaliza.getProduto().getQualidade().getListaDeItensDeControle();
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
                     proximoStatusBaliza.getProduto().getQualidade().setEhReal(true);
                  }
               }
            }
         }
      }
   }
   
   private Map<String, List<Double>> geraMapaQuantidade(Campanha campanha, Date horaInicio, Date horaTermino, Map<String, List<IntegracaoMES>> mapaQualidades, Map<String, List<IntegracaoMES>> mapaQuantidades, TipoProduto tipoProduto)
   {
      Map<String, List<Double>> mapaQuantidadeRelativaQualidade = new HashMap<String, List<Double>>();
      if (mapaQualidades != null)
      {
         for (String chave : mapaQualidades.keySet())
         {
            List<IntegracaoMES> listaIntegracaoMES = mapaQualidades.get(chave);
            List<Double> listaQuantidades = new ArrayList<Double>();
            if (listaIntegracaoMES != null && listaIntegracaoMES.size() > 0)
            {
               for (IntegracaoMES integracaoMES : listaIntegracaoMES)
               {
                  Date inicioIntervalo = horaInicio;
                  Date fimIntervalo = horaTermino;
                  if (inicioIntervalo.getTime() < integracaoMES.getDataLeituraInicio().getTime())
                     inicioIntervalo = integracaoMES.getDataLeituraInicio();
                  if (fimIntervalo.getTime() > integracaoMES.getDataLeitura().getTime())
                     fimIntervalo = integracaoMES.getDataLeitura();
                  Double quantidadeFacade = mesFacade.calcular(inicioIntervalo, fimIntervalo, campanha, mapaQuantidades, tipoProduto);
                  if (quantidadeFacade != null)
                     listaQuantidades.add(quantidadeFacade);
               }
            }
            mapaQuantidadeRelativaQualidade.put(chave, listaQuantidades);
         }
      }
      return mapaQuantidadeRelativaQualidade;
   }
   
   private void atualizaQuantidadesEmpilhamentoUsinas(Atividade atividadePai, LugarEmpilhamentoRecuperacao lugarEmpRec, Map<String, List<IntegracaoMES>> map) throws ErroSistemicoException
   {
      // pega a baliza para definir a lista de itens de controle
      //
       //Collections.sort(lugarEmpRec.getListaDeBalizas(),Baliza.comparadorBaliza);
	   Atividade atividade = atividadePai.getAtividadeAnterior();
	   
	   Baliza baliza = lugarEmpRec.getListaDeBalizas().get(0);
       if (lugarEmpRec.getListaDeBalizas().size() > 1) {
           for (Baliza balizaI : lugarEmpRec.getListaDeBalizas()) {
               if (balizaI.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                   baliza = balizaI;
                   break;
               }    
           }    
       }
       
       
       
      TipoProduto tipoProdutoBaliza = baliza.getProduto().getTipoProduto();
      Double quantidadeTotal = new Double(0);
      
      // indice do status a partir do qual deve propagar a modificacao na baliza
      int indiceInicioAtualizacao = baliza.getMetaBaliza().getIndiceIntervaloCorrespondente(atividade.getDtFim());
      
      // esse boolean é utilizado para indicar que conseguiu calcular as quantidades para todas as campanhas 
      // Se nao conseguir calcular para todas entao nao atualiza nenhuma
      boolean temCampanhaPendente = false;
      
      // mapa contendo a quantidade de perda de pellet feed para cada campanha
      HashMap<AtividadeCampanha, Double> mapaPerdaSinterFeedPorCampanha = new HashMap<AtividadeCampanha, Double>();
      
      // verifica a quantidade total empilhada
      // verifica tambem se consegue recuperar os dados para todas as campanhas
      for (AtividadeCampanha atividadeCampanha : atividade.getListaDeAtividadesCampanha())
      {
         // calcula a quantidade empilhada por cada campanha durante a duracao da atividade
         Campanha campanha = atividadeCampanha.getCampanha();
         
         Double quantidadeCampanha = null;
         // verifica se ja tinha calculado a quantidade para esta campanha anteriormente
         if (atividadeCampanha.getQuantidade() != null)
            quantidadeCampanha = atividadeCampanha.getQuantidade();
         else
            quantidadeCampanha = mesFacade.calcular(atividade.getDtInicio(), atividade.getDtFim(), campanha, map, tipoProdutoBaliza);
         
         if (quantidadeCampanha != null)
         {
            quantidadeTotal += quantidadeCampanha;
            atividadeCampanha.setQuantidade(quantidadeCampanha);
            
            // calcula a quantidade de perda de pellet feed para esta campanha
            double taxaDeGeracaoPellet = atividadeCampanha.getCampanha().getMetaUsina().retornaStatusHorario(atividade.getDtFim()).getTaxaGeracaoPellet();
            // só deve gerar sinter feed para pelota
            if (taxaDeGeracaoPellet > 0 && tipoProdutoBaliza.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA))
            {
               double quantidadePerdaPellet = quantidadeCampanha * taxaDeGeracaoPellet;
               mapaPerdaSinterFeedPorCampanha.put(atividadeCampanha, new Double(quantidadePerdaPellet));
            }
         }
         else
         {
            temCampanhaPendente = true;
         }
      }
      
      // verifica se conseguiu atualizar os dados de todas as campanhas
      if (!temCampanhaPendente)
      {
         atividade.setQuantidadeAtualizadaPeloMES(Boolean.TRUE);
         precisaConsolidarDados = true;
         atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).setQuantidade(quantidadeTotal);         
         atividade.setUpdated(Boolean.TRUE);
         // acrescenta a quantidade empilhada na baliza, propagando a mudança para os estados futuros
         int sizeListaStatusBaliza = baliza.getMetaBaliza().getSizeListaStatus();
         for (int j = indiceInicioAtualizacao; j < sizeListaStatusBaliza; j++)
         {
            Baliza proximoStatusBaliza = baliza.getMetaBaliza().getListaStatus().get(j);
            
            // atualiza a quantidade no status da baliza
            if (proximoStatusBaliza.getProduto() != null)
            {
               
                Blendagem blend = Blendagem.getInstance();                   
                blend.setMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem(null);   
                blend.setProdutoResultante(null);
                blend.setListaBalizasBlendadas(null);
                blend.setCargaSelecionada(null);
                blend.setListaDeCampanhas(null);
                blend.setListaDeProdutosSelecionados(null);

                
                Produto produtobaliza = proximoStatusBaliza.getProduto();                
                Produto produtoNovo = Produto.criaProduto(lugarEmpRec.getTipoProduto(), System.currentTimeMillis());                
                produtoNovo.setQuantidade(quantidadeTotal);
                
                Qualidade novaQualidade = new Qualidade();
                novaQualidade.setListaDeAmostras(new ArrayList<Amostra>());
                
                novaQualidade.setListaDeItensDeControle(Blendagem.criaListaDeItensDeControleComValoresVazios(atividade.getListaDeAtividadesCampanha().get(0).getCampanha().getQualidadeEstimada().getListaDeItensDeControle()));              
                
                for (ItemDeControle ic : novaQualidade.getListaDeItensDeControle())
                {
                    ic.setDtInicio(atividade.getDtInicio());
                    ic.setDtFim(atividade.getDtFim());
                    ic.setDtInsert(new Date(System.currentTimeMillis()));
                }
                novaQualidade.setEhReal(false);
                novaQualidade.setDtInicio(atividade.getDtInicio());
                novaQualidade.setDtFim(atividade.getDtFim());
                novaQualidade.setDtInsert(new Date(System.currentTimeMillis()));
                novaQualidade.setIdUser(1L);
                produtoNovo.setQualidade(novaQualidade);                
                novaQualidade.setProduto(produtoNovo);
                
                if (produtobaliza.getQuantidade() > 0) {
                	blend.atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposInsercaoDoProduto(produtobaliza,
                                produtobaliza.getQuantidade());
                	blend.atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposInsercaoDoProduto(produtoNovo,
                                quantidadeTotal);
                
                	produtoNovo = blend.calculaProdutoResultanteDaInsercaoDeProdutoNaBlendagem(produtobaliza, produtoNovo,
                                quantidadeTotal);               
                }  
                
                //atualiza dados do produto da baliza
               // produtobaliza.getQualidade().setEhReal(Boolean.TRUE);
               // produtobaliza.getQualidade().getListaDeItensDeControleQualidade().clear();
               // produtobaliza.getQualidade().getListaDeItensDeControleQualidade().addAll(produtoNovo.getQualidade().getListaDeItensDeControleQualidade());
                produtobaliza.setTipoProduto(produtoNovo.getTipoProduto());
                produtobaliza.setQuantidade(produtoNovo.getQuantidade());
                //produtoNovo.addRastreabilidade(produtobaliza.getListaDeRastreabilidades());
                
                //proximoStatusBaliza.setProduto(produtoNovo);
              
                if (atividade.getListaDeAtividadesCampanha() != null && !atividade.getListaDeAtividadesCampanha().isEmpty())
               {
                     long chave = 1;
                    for (AtividadeCampanha atividadeCampanha : atividade.getListaDeAtividadesCampanha())
                  {
                     Campanha campanha = atividadeCampanha.getCampanha();
                     // acrescenta os registros de rastreabilidade no produto
                     acrescentaRastreabilidade(atividade.getDtInicio(), atividade.getDtFim(), campanha, atividadeCampanha.getQuantidade(), proximoStatusBaliza, proximoStatusBaliza.getProduto(),chave);
                  }
               }
            }
         }
  
         /** atualiza a quantidade da perda de pellet feed */
         ControladorExecutarPerdaPellet controladorPerdaPellet = ControladorExecutarPerdaPellet.getInstance();
         controladorPerdaPellet.empilharPerdaPellet(atividadePai,atividade.getDtFim(), atividade.getListaDeAtividadesCampanha(), mapaPerdaSinterFeedPorCampanha);
      }
   }
   
   /* Acrescenta os registros de rastreabilidade no produto */
   private void acrescentaRastreabilidade(Date horaInicio, Date horaTermino, Campanha campanha, double quantidadeEmpilhada, Baliza balizaAnterior, Produto produto, long chave)
   {
      // para as atividades pontuais nao eh necessario incluir a
      // rastreabilidade
      if (horaInicio.getTime() == horaTermino.getTime())
         return;
      Rastreabilidade rastreabilidade = new Rastreabilidade();
      rastreabilidade.setHorarioInicioEntradaDeMaterial(horaInicio);
      rastreabilidade.setHorarioFimEntradaDeMaterial(horaTermino);
      rastreabilidade.setDtInicio(horaInicio);
      rastreabilidade.setDtFim(horaTermino);
      rastreabilidade.setDtInsert(new Date(System.currentTimeMillis()));
      if (campanha != null)
      {
         rastreabilidade.setNomeUsina(campanha.getMetaUsina().getNomeUsina());
         rastreabilidade.setTaxaDeOperacaoUsina(campanha.getMetaUsina().retornaStatusHorario(horaTermino).getTaxaDeOperacao());
         rastreabilidade.setDataInicioUsina(campanha.getDataInicial());
         rastreabilidade.setDataFimUsina(campanha.getDataFinal());
         rastreabilidade.setNomeCampanhaUsina(campanha.getNomeCampanha());
         rastreabilidade.setTipoProdutoUsina(campanha.getTipoProduto());
         rastreabilidade.setTipoPelletUsina(campanha.getTipoPellet());
         rastreabilidade.setTipoScreeningUsina(campanha.getTipoScreening());
      }
      rastreabilidade.setTipoAtividade(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO);
      rastreabilidade.setQuantidade(quantidadeEmpilhada);
      if (balizaAnterior.getProduto() != null)
      {
         if (balizaAnterior.getProduto().getListaDeRastreabilidades() == null)
         {
            balizaAnterior.getProduto().addRastreabilidade(new ArrayList<Rastreabilidade>());
         }
         rastreabilidade.setNumeroRastreabilidade(chave);
         balizaAnterior.getProduto().addRastreabilidade(rastreabilidade);      
         //rastreabilidade.setProduto(produto);
      }
   }
   
   /**
    * Gera a lista de integracaoMES equivalentes a usina, data de inicio e fim da atividade
    */
   private Map<String, List<IntegracaoMES>> geraListaDeIntegracaoMESAmostras(Campanha campanha, Date horaInicio, Date horaTermino, List<ItemDeControle> listaItensDeControle, TipoProduto tipoProduto) throws ErroSistemicoException
   {
      if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_SCREENING))
      {
         // o screening nao tem amostras de qualidade
         return null;
      }
      // mapa para armazenar os itens de controle por data, permitindo montar
      // as amostras depois
      // HashMap<Date, ArrayList<ItemDeControle>> mapaItemControleData = new
      // HashMap<Date, ArrayList<ItemDeControle>>();
      // clone helper para criar itens de controle para as amostras
      // procura as entradas para cada item de controle durante o periodo
      List<IntegracaoMES> listaIntegracoes = new ArrayList<IntegracaoMES>();
      Map<String, List<IntegracaoMES>> mapUsinaProducao = new HashMap<String, List<IntegracaoMES>>();
      for (ItemDeControle itemControle : listaItensDeControle)
      {
         IntegracaoMES filtroIntegracaoMES = new IntegracaoMES();         
         if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_FEED))
         {
            /*
              * 
              * */
            if (itemControle.getTipoItemControle().equals(TipoItemControleEnum.QUIMICO))
            {
               filtroIntegracaoMES.setCdFaseProcesso(campanha.getCodigoFaseProcessoPelletFeedQAQ());
            } else if (itemControle.getTipoItemControle().equals(TipoItemControleEnum.FISICO))
            {
               filtroIntegracaoMES.setCdFaseProcesso(campanha.getCodigoFaseProcessoPelletFeedQAF());
            } else
            {
               filtroIntegracaoMES.setCdFaseProcesso(itemControle.getTipoItemControle().getCdTipoItemControlePelletFeed());
            }
            // filtroIntegracaoMES.setCdFaseProcesso(campanha.getCodigoFaseProcessoPelletFeed());
            filtroIntegracaoMES.setCdItemControle(itemControle.getTipoItemControle().getCdTipoItemControlePelletFeed());
            filtroIntegracaoMES.setCdTipoProcesso(itemControle.getTipoItemControle().getTipoProcessoPelletFeed());
            filtroIntegracaoMES.setCdAreaRespEd(itemControle.getTipoItemControle().getAreaRespEDPelletFeed());
         } else if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA))
         {
            filtroIntegracaoMES.setCdFaseProcesso(campanha.getCodigoFaseProcessoPelota());
            filtroIntegracaoMES.setCdItemControle(itemControle.getTipoItemControle().getCdTipoItemControlePelota());
            filtroIntegracaoMES.setCdTipoProcesso(itemControle.getTipoItemControle().getTipoProcessoPelota());
            filtroIntegracaoMES.setCdAreaRespEd(itemControle.getTipoItemControle().getAreaRespEDPelota());
         }
         listaIntegracoes = controladorIntegracao.buscarDadosLidosPorData(filtroIntegracaoMES, horaInicio);
         if (listaIntegracoes != null && !listaIntegracoes.isEmpty())
         {
            listaIntegracoes = makeListItensValidos(listaIntegracoes, horaInicio, horaTermino);
            makeMapQualidadesMES(listaIntegracoes, mapUsinaProducao);
         }
      }
      return mapUsinaProducao;
   }
   

   
   public IControladorIntegracao getControladorIntegracao()
   {
      return controladorIntegracao;
   }
   
   public void setControladorIntegracao(IControladorIntegracao controladorIntegracao)
   {
      this.controladorIntegracao = controladorIntegracao;
   }
   
   public Long getIdSistemaMES()
   {
      return new Long(1);
   }
   
   public HashMap<Long, IntegracaoMES> getMapaListaDadosProcessados()
   {
      if (mapaListaDadosProcessados == null)
      {
         mapaListaDadosProcessados = new HashMap<Long, IntegracaoMES>();
      }
      return mapaListaDadosProcessados;
   }
   
   public void setMapaListaDadosProcessados(HashMap<Long, IntegracaoMES> mapaListaDadosProcessados)
   {
      this.mapaListaDadosProcessados = mapaListaDadosProcessados;
   }
   
   public List<IntegracaoMES> atualizarDadosUsina(IntegracaoMES integracaoMES, Date dataLeituraInicial) throws ErroSistemicoException
   {
      return controladorIntegracao.buscarDadosLidosPorData(integracaoMES, dataLeituraInicial);
   }
   
   /*
    * monta a lista de qualidades ordenadaTODO UNIFICAR METODOS EM CLASSE DE UTILITARIOS
    */
   private void makeMapQualidadesMES(List<IntegracaoMES> listaDeQualidadesUsina, Map<String, List<IntegracaoMES>> map)
   {
      List<IntegracaoMES> itens = null;
      if (listaDeQualidadesUsina == null || listaDeQualidadesUsina.isEmpty())
      {
         return;
      }
      for (IntegracaoMES integraMES : listaDeQualidadesUsina)
      {
         StringBuilder currKey = new StringBuilder();
         currKey.append(integraMES.getCdFaseProcesso().toString().trim());
         currKey.append(integraMES.getCdItemControle().toString().trim());
         currKey.append(integraMES.getCdTipoProcesso().trim());
         currKey.append(integraMES.getCdAreaRespEd().trim());
         if (!map.containsKey(currKey.toString()))
         {
            itens = new ArrayList<IntegracaoMES>();
            itens.add(integraMES);
            map.put(currKey.toString(), itens);
         } else
         {
            itens = map.get(currKey.toString());
            if (!itens.contains(integraMES))
            {
               itens.add(integraMES);
               Collections.sort(itens);
            }
         }
      }
   }
   
   private List<IntegracaoMES> makeListItensValidos(List<IntegracaoMES> itens, Date inicio, Date fim)
   {
      boolean achouFim = false;
      boolean achouPrimeiro = false;
      List<IntegracaoMES> itensValidos = new ArrayList<IntegracaoMES>();
      // recupera os itens q estao dentro do range
      for (IntegracaoMES item : itens)
      {
         if (item.getDataLeitura() != null && item.getDataLeituraInicio() != null)
         {
            if (fim.getTime() >= item.getDataLeituraInicio().getTime() && item.getDataLeitura().getTime() >= fim.getTime())
            {
               itensValidos.add(item);
               achouFim = true;
               break;
            }
            if (!achouPrimeiro && inicio.getTime() >= item.getDataLeituraInicio().getTime() && inicio.getTime() <= item.getDataLeitura().getTime())
            {
               itensValidos.add(item);
               achouPrimeiro = true;
            } else if (achouPrimeiro)
            {
               itensValidos.add(item);
            }
         }
      }
      if (!achouFim)
         itensValidos = null;
      return itensValidos;
   }
   
   private String montaChaveFaseItem(Campanha campanha, ItemDeControle itemControle, TipoProduto tipoProduto)
   {
      StringBuilder currKey = new StringBuilder();
      if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_FEED))
      {
         /*
          * 
          * */
         if (itemControle.getTipoItemControle().equals(TipoItemControleEnum.QUIMICO))
         {
            currKey.append(campanha.getCodigoFaseProcessoPelletFeedQAQ());
         } else if (itemControle.getTipoItemControle().equals(TipoItemControleEnum.FISICO))
         {
            currKey.append(campanha.getCodigoFaseProcessoPelletFeedQAF());
         } else
         {
            currKey.append(itemControle.getTipoItemControle().getCdTipoItemControlePelletFeed());
         }
         if (itemControle.getTipoItemControle().getTipoProcessoPelletFeed() != null && itemControle.getTipoItemControle().getAreaRespEDPelletFeed() != null)
         {
            currKey.append(itemControle.getTipoItemControle().getTipoProcessoPelletFeed().trim());
            currKey.append(itemControle.getTipoItemControle().getAreaRespEDPelletFeed().trim());
         }
      } else
      {
         currKey.append(campanha.getCodigoFaseProcessoPelota());
         currKey.append(itemControle.getTipoItemControle().getCdTipoItemControlePelota());
         if (itemControle.getTipoItemControle().getTipoProcessoPelota() != null && itemControle.getTipoItemControle().getAreaRespEDPelota() != null)
         {
            currKey.append(itemControle.getTipoItemControle().getTipoProcessoPelota().trim());
            currKey.append(itemControle.getTipoItemControle().getAreaRespEDPelota().trim());
         }
      }
      return currKey.toString();
   }
   
   private boolean listaStringEntityContemString(String lista, String str)
   {
      if (lista == null || lista.length() == 0)
         return false;
      StringTokenizer tk = new StringTokenizer(lista, ";");
      int tokencount = tk.countTokens();
      for (int i = 0; i < tokencount; i++)
      {
         if (tk.nextToken().trim().equalsIgnoreCase(str.trim()))
         {
            return true;
         }
      }
      return false;
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
}
