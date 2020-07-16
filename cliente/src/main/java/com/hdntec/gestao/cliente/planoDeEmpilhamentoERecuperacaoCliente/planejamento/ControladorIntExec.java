package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento;

/**
 * Contem todas as funcionalidades de um intervalo de execucao.
 * 
 * @author Ricardo Trabalho
 */
public class ControladorIntExec
{

   /*private IntervaloDeExecucao intervalo = null;

   private CriaAtividadeMundacaDeCampanha criadorAtividadeCampanha;

   private ControladorPlanejamento controladorPlanejamento;

   *//**
    * Cria uma instancia de contralador de intervalo de execucao, carregando as
    * partes de atividade de todos os tipos em uma lista de partes unica ordenadas
    * por data de criacao.
    * @param intervalo
    *//*
   public ControladorIntExec(IntervaloDeExecucao intervalo)
   {
      this.intervalo = intervalo;
   }

   *//**
    * Executa todas as partes de atividades no intervalo de tempo definido nesse
    * intervalo de execucao, que pode ser um intervalo pontual ou um intervalo com um
    * inicio e fim.
    * @param situacaoPatio Situacao de patio original que Ã© clonada e altera de
    * acordo com todos as partes de atividade.
    * @return
    *//*
   public SituacaoPatio executar(SituacaoPatio situacaoPatio, ControladorPlanejamento controladorPlanejamento)
   {
      this.controladorPlanejamento = controladorPlanejamento;
      return executar(situacaoPatio, this.intervalo.getFim());
   }

   public void setCriadorAtividadeCampanha(CriaAtividadeMundacaDeCampanha criadorAtividadeCampanha)
   {
      this.criadorAtividadeCampanha = criadorAtividadeCampanha;
   }

   *//**
    *
    * @param situacaoPatio
    * @param data
    * @return
    *//*
   public SituacaoPatio executar(SituacaoPatio situacaoPatio, Date data)
   {
      SituacaoPatio situacaoPatioFinal = null;
      Boolean clonado = Boolean.FALSE;

      // faz todas as alteracoes que ocorrem em um intervalo de tempo de uma
      //situacao de patio para outra.
      for (Atividade atividade : this.intervalo.getListaDeAtividades())
      {
         IControladorAtividade controlador = null;
         switch (atividade.getTipoAtividade())
         {
            // Empilhamento realiza um intervalo de execucao e portanto
            // tem a situacao clonada para ser executado a atividade em um
            // intervalo de tempo definido.
            case EMPILHAMENTO:
            {
               // clone de situacao so deve ser feito uma unica vez para as atividades
               // do intervalo
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(data);
               situacaoPatioFinal.setAtividade(atividade);
               controlador = new ControladorEmpilhamento(atividade);
               break;
            }
            // Recuperacao realiza um intervalo de execucao e portanto
            // tem a situacao clonada para ser executado a atividade em um
            // intervalo de tempo definido.
            case RECUPERACAO:
            {
               // clone de situacao so deve ser feito uma unica vez para as atividades
               // do intervalo
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(data);
               //atualiza estados maquinas e correias
               atualizaEstadosSituacaoDePatio(situacaoPatioFinal, atividade);
               situacaoPatioFinal.setAtividade(atividade);
               controlador = new ControladorRecuperacao(atividade);
               break;
            }
            // Mudanca de campanha executa duas acoes mas em intervalos de
            // execucao pontuais diferentes, sendo um na criacao/alteracao da
            // campanha e outra na exclusao da mesma.
            case MUDANCA_DE_CAMPANHA:
            {
               // Se a data da situacao for menor que a data de inicio do intervalo,
               // entao Ã© gerada uma nova situacao de patio, caso contrario Ã© aproveitada
               // a mesma situacao.
               if (situacaoPatio.getDtInicio().compareTo(intervalo.getInicio()) == 0)
               {
                  situacaoPatioFinal = situacaoPatio;
               }
               else
               {
                  if (!clonado)
                  {
                     situacaoPatioFinal = clonarSituacao(situacaoPatio);
                     clonado = Boolean.TRUE;
                  }
                  situacaoPatioFinal.setDataHora(intervalo.getInicio());
               }
               controlador = new ControladorMudancaDeCampanhaEdicao(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            // SA9314
            case ATUALIZACAO_EMPILHAMENTO:
            {
               if (!clonado)
               {            	   
            	   situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               data = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoClone(data, situacaoPatio.getDtInicio());
               situacaoPatioFinal.setDataHora(data);
               
               controlador = new ControladorAtualizacaoEmpilhamento(atividade);
               situacaoPatioFinal.setAtividade(atividade);

//               String dataHoraSituacaoAtual = DSSStockyardTimeUtil.formatarData(situacaoPatio.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
//               String dataHoraSituacaoFinal = DSSStockyardTimeUtil.formatarData(situacaoPatioFinal.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
//
//               if (dataHoraSituacaoFinal.equals(dataHoraSituacaoAtual))
//               {
//                  data = DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(situacaoPatioFinal.getDtInicio());
//                  situacaoPatioFinal.setDataHora(data);
//                  this.intervalo.setInicio(data);
//                  this.intervalo.setFim(data);
//               }

               break;
            }
            //TODO SA9314
            case ATUALIZACAO_RECUPERACAO:
            {
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               data = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoClone(data, situacaoPatio.getDtInicio());
               situacaoPatioFinal.setDataHora(data);
               controlador = new ControladorAtualizacaoRecuperacao(atividade, controladorPlanejamento);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            //Resultado amostragem..... (atividade pontual)
            case RESULTADO_DE_AMOSTRAGEM:
            {
               // Se a data da situacao for menor que a data de inicio do intervalo,
               // entao Ã© gerada uma nova situacao de patio, caso contrario Ã© aproveitada
               // a mesma situacao.
               if (situacaoPatio.getDtInicio().compareTo(intervalo.getInicio()) == 0)
               {
                  situacaoPatioFinal = situacaoPatio;
               }
               else
               {
                  if (!clonado)
                  {
                     situacaoPatioFinal = clonarSituacao(situacaoPatio);
                     clonado = Boolean.TRUE;
                  }
                  situacaoPatioFinal.setDataHora(intervalo.getInicio());
               }
               //cria controlador de resultado amostragem
               controlador = new ControladorResultadoAmostragem(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            // Movimentacao de PSM e Pellet Screening (representa uma atividade pontual)
            case MOVIMENTACAO:
            {
               // clone de situacao so deve ser feito uma unica vez para as atividades
               // do intervalo
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(intervalo.getFim());
               controlador = new ControladorMovimentacao(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            case TRATAMENTO_PSM:
            {
               // clone de situacao so deve ser feito uma unica vez para as atividades
               // do intervalo
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(intervalo.getFim());
               controlador = new ControladorTratamentoPSM(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            //atividade para desatracar navio do pier(berco) //SA9999 e SA10004
            case SAIDA_DE_NAVIO:
            {
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(intervalo.getInicio());
               controlador = new ControladorAtividadeDesatracarNavio(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            //atividade para atracar navio no pier(berco) //SA9999 e SA10004
            case CHEGADA_DE_NAVIO:
            {
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(intervalo.getInicio());
               controlador = new ControladorAtividadeAtracarNavio(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            case RETORNO_PELLET_FEED:
            {
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(data);
               controlador = new ControladorRetornoPelletFeed(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            case PILHA_DE_EMERGENCIA:
            {
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(data);
               controlador = new ControladorPilhaEmergencia(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            case TRANSPORTAR_PILHA_EMERGENCIA:
            {
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(data);
               controlador = new ControladorTransportePilhaEmergencia(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            case MOVIMENTAR_PILHA_EMERGENCIA:
            {
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(data);
               controlador = new ControladorMovimentarPilhaEmergencia(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
            case MOVIMENTAR_PILHA_PSM_PELLET_FEED:
            {
               if (!clonado)
               {
                  situacaoPatioFinal = clonarSituacao(situacaoPatio);
                  clonado = Boolean.TRUE;
               }
               // Seta a hora inicial desta situacao utilizando a hora final
               situacaoPatioFinal.setDataHora(data);
               controlador = new ControladorMovimentacao(atividade);
               situacaoPatioFinal.setAtividade(atividade);
               break;
            }
         }

         // executa o que acontece na atividade neste intervalo, onde pode ser pontual ou um intervalo de tempo,
         // se for pontual o intervalo tera sua data fim como nula. Todos os intervalos sempre terao data de inicio.
         situacaoPatioFinal = controlador.executar(situacaoPatioFinal, this.intervalo.getInicio(), data);


         // implementar aqui atualização do screening das usinas.
         if (data != null && 
        		 ((atividade.getTipoAtividade().equals(TipoAtividadeEnum.EMPILHAMENTO) || atividade.getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO)) && 
        				 atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getTipoMovimento().equals(TipoMovimentacaoEnum.FINALIZADO)))
         {
            List<Usina> usinas = new ArrayList<Usina>();
            for (AtividadeCampanha atividadeCampanha : atividade.getListaDeAtividadesCampanha())
            {
               usinas.add(atividadeCampanha.getCampanha().getUsina());
            }
            Double qtdEmpilhar = 0.0d;
            for (LugarEmpilhamentoRecuperacao lugarEmpilhamentoRecuperacao : atividade.getListaDeLugaresDeEmpilhamentoRecuperacao())
            {
               qtdEmpilhar += lugarEmpilhamentoRecuperacao.getQuantidade();
            }
            ControladorPerdaPelletScreening controladorPerda = new ControladorPerdaPelletScreening(situacaoPatioFinal, usinas, qtdEmpilhar);
            situacaoPatioFinal = controladorPerda.getSituacaoPatioAtualizado();
         }

         if (data != null && (atividade.getTipoAtividade().equals(TipoAtividadeEnum.RECUPERACAO) && (atividade.getListaDeAtividadesCampanha() != null && atividade.getListaDeAtividadesCampanha().size() > 0)))
         {
            List<Usina> usinas = new ArrayList<Usina>();
            Double qtdEmpilhar = 0.0d;
            for (AtividadeCampanha atividadeCampanha : atividade.getListaDeAtividadesCampanha())
            {
               usinas.add(atividadeCampanha.getCampanha().getUsina());
               qtdEmpilhar += atividadeCampanha.getQtdTotalProduzida();
            }

            if (qtdEmpilhar > 0)
            {
               ControladorPerdaPelletScreening controladorPerda = new ControladorPerdaPelletScreening(situacaoPatioFinal, usinas, qtdEmpilhar);
               situacaoPatioFinal = controladorPerda.getSituacaoPatioAtualizado();
            }
         }

//         if (data != null && atividade.getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO))
//         {
//            for (AtividadeCampanha atividadeCampanha : atividade.getListaDeAtividadesCampanha())
//            {
////               if (atividadeCampanha.getCampanha().getUsina().getQtdProdutoAtualizacao() != null && atividadeCampanha.getCampanha().getUsina().getQtdProdutoAtualizacao() > 0.0d)
//               if(atividade.getQuantidadeAtualizadaPeloMES() && atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getQuantidade() != null && atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getQuantidade() > 0.0d)
//               {
//                  List<Usina> usinas = new ArrayList<Usina>();
//                  for (AtividadeCampanha atividadeCampanhaUsinas : atividade.getListaDeAtividadesCampanha())
//                  {
//                     usinas.add(atividadeCampanhaUsinas.getCampanha().getUsina());
//                  }
//
//                  ControladorPerdaPelletScreening controladorPerdaPelletScreening =
//                        new ControladorPerdaPelletScreening(situacaoPatioFinal, usinas, atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getQuantidade());
//                  situacaoPatioFinal = controladorPerdaPelletScreening.getSituacaoPatioAtualizado();
//               }
//            }
//         }
      }

      return situacaoPatioFinal;
   }

   private SituacaoPatio clonarSituacao(SituacaoPatio situacaoPatio) {
	// TODO Auto-generated method stub
	return null;
}

*//**
    * Atualiza os Estados (OCIOSO, OPERACAO) dos objetos envolvidos na Atividade da SituacaoDePatio
    * @param sitPatioFinal
    * @param atividade
    * SA9177
    *//*
   private void atualizaEstadosSituacaoDePatio(SituacaoPatio sitPatioFinal, Atividade atividade)
   {
      //ordena a lista de datas da atividade
      Collections.sort(atividade.getDatas());
      //guarda a ultima data da lista de datas da atividade
      Date dataFinal = atividade.getDatas().get(atividade.getDatas().size() - 1);
      //verifica se a situaÃ§Ã£o de patio Ã© a ultima situaÃ§Ã£o a ser gerada
      if (sitPatioFinal.getDtInicio().equals(dataFinal))
      {   //..caso positivo seta o estado das maquinas utilizadas e das correias para Ocioso
         for (LugarEmpilhamentoRecuperacao lugarER : atividade.getListaDeLugaresDeEmpilhamentoRecuperacao())
         {
            lugarER.getMaquinaDoPatio().setEstado(EstadoMaquinaEnum.OCIOSA);
            if (lugarER.getMaquinaDoPatio().getCorreia() != null)
            {//se a correia for igual a null, significa que a maquina Ã© uma Pa-Carregadeira
               lugarER.getMaquinaDoPatio().getCorreia().setEstado(EstadoMaquinaEnum.OCIOSA);
            }
            //muda o estado das maquinas e correias na situacao de patio
            buscaMaquinaEquivalenteDaAtividade(sitPatioFinal, lugarER.getMaquinaDoPatio());
         }
      }
      else
      {   //..caso contrario mantem o estado em Operacao
         for (LugarEmpilhamentoRecuperacao lugarER : atividade.getListaDeLugaresDeEmpilhamentoRecuperacao())
         {
            lugarER.getMaquinaDoPatio().setEstado(EstadoMaquinaEnum.OPERACAO);
            if (lugarER.getMaquinaDoPatio().getCorreia() != null)
            {//se a correia for igual a null, significa que a maquina Ã© uma Pa-Carregadeira
               lugarER.getMaquinaDoPatio().getCorreia().setEstado(EstadoMaquinaEnum.OPERACAO);
            }
            //muda o estado das maquinas e correias na situacao de patio
            buscaMaquinaEquivalenteDaAtividade(sitPatioFinal, lugarER.getMaquinaDoPatio());
         }
      }
   }

   *//**
    * metodo auxiliar do "atualizaEstadosSituacaoDePatio"
    * @param sitPatioFinal
    * @param maquinaDoPatio
    * @see SA9177
    *//*
   private void buscaMaquinaEquivalenteDaAtividade(SituacaoPatio sitPatioFinal, MaquinaDoPatio maquinaDoPatio)
   {
      for (MaquinaDoPatio maquinaDoPatio1 : sitPatioFinal.getPlanta().getListaMaquinasDoPatio())
      {
         if (maquinaDoPatio1.equals(maquinaDoPatio))
         {
            maquinaDoPatio1.setEstado(maquinaDoPatio.getEstado());
            if (maquinaDoPatio1.getCorreia() != null)
            {
               maquinaDoPatio1.getCorreia().setEstado(maquinaDoPatio.getCorreia().getEstado());
            }
         }
      }
   }
*/
   
  
}
