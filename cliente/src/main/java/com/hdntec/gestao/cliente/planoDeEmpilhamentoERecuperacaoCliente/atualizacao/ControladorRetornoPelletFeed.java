package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao;

import com.hdntec.gestao.domain.plano.entity.Atividade;
/**
 * Controlador para atualizacao da recuperacao
 */
public class ControladorRetornoPelletFeed  { 

   // atividade a qual este controlador pertence.
   private Atividade atividadeRetornoPelletFeed;
   
   public ControladorRetornoPelletFeed(Atividade atividade)
   {
      this.atividadeRetornoPelletFeed = atividade;
   }
/*   
   @Override
   public SituacaoPatio executar(SituacaoPatio situacaoDePatio, Date inicioExecucao, Date fimExecucao)
   {
   	// executa a atividade de retorno de pellet feed para filtragem
      return executarAtividadeRetornoPelletFeed(situacaoDePatio, inicioExecucao, fimExecucao);
   }

   private SituacaoPatio executarAtividadeRetornoPelletFeed(SituacaoPatio situacaoDePatio, Date inicioExecucao, Date fimExecucao) {

   	// pega os objetos equivalentes na nova situacao
      // na atualizacao sempre a lista de lugares tem apenas um elemento
      LugarEmpilhamentoRecuperacao lugarEmpRecAtual = atividadeRetornoPelletFeed.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
      List<Baliza> listaBalizasEquivalentes = obterBalizasEquivalentes(lugarEmpRecAtual.getListaDeBalizas(), situacaoDePatio);
      MaquinaDoPatio maquinaEquivalente = ControladorMaquinas.buscaMaquinaEquivalente(lugarEmpRecAtual.getMaquinaDoPatio(), situacaoDePatio);

      // pilha utilizada se for necessario excluir balizas
      Pilha pilhaDasBalizas = new LinkedList<Baliza>(listaBalizasEquivalentes).getFirst().getPilha();

      // seta o horario da situacao
      situacaoDePatio.setDataHora(fimExecucao);

      Date horaTermino = atividadeRetornoPelletFeed.getDtFim();
      Date horaInicio = atividadeRetornoPelletFeed.getDtInicio();

      // 1) seta a maquina como parada
      maquinaEquivalente.setEstado(EstadoMaquinaEnum.OCIOSA);

      // 4) atualiza a quantidade recuperada das balizas (retira a quantidade igualmente de todas as balizas selecionadas)
      int sizeBalizas = listaBalizasEquivalentes.size();

      // calcula a quantidade que deve ser retirada de cada baliza
      double quantidadeRetirarPorBaliza = lugarEmpRecAtual.getQuantidade() / sizeBalizas;
      
      // lista com as balizas que ainda tem produto. Essa lista eh utilizada como auxiliar para verificar se existe alguma
      // baliza que nao contem a quantidade que deve ser subtraida. Neste caso distribui a retirada pelas balizas restantes
      List<Baliza> listaBalizasComProduto = new ArrayList<Baliza>();
      listaBalizasComProduto.addAll(listaBalizasEquivalentes);

      // mapa para apontar realmente quanto foi retirado de cada baliza
      HashMap<Integer, Double> mapaQuantidadeRetiradaDeBaliza = new HashMap<Integer, Double>();
      boolean distribuiuRetirada = false;
      while (!distribuiuRetirada)
      {
         distribuiuRetirada = true;
         for (Baliza bal : listaBalizasComProduto)
         {
            // verifica se pode retirar toda a quantidade desta baliza
            double saldoDaPilha = bal.getProduto().getQuantidade() - quantidadeRetirarPorBaliza;
            if (saldoDaPilha <= 0)
            {
               mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), new Double(bal.getProduto().getQuantidade()));
               distribuiuRetirada = false;
               bal.getProduto().setQuantidade(0.0);
               quantidadeRetirarPorBaliza += Math.abs(saldoDaPilha);
               listaBalizasComProduto.remove(bal);
               break;
            }
         }
      }
      // distribui as retiradas pelas pilhas restantes
      for (Baliza bal : listaBalizasComProduto)
      {
         bal.getProduto().setQuantidade(bal.getProduto().getQuantidade() - quantidadeRetirarPorBaliza);
         mapaQuantidadeRetiradaDeBaliza.put(bal.getNumero(), quantidadeRetirarPorBaliza);
      }

      for (Baliza bal : listaBalizasEquivalentes)
      {
         // 8) verifica se finalizou a baliza da pilha
         if (bal.getProduto().getQuantidade() <= 0)
         {
            bal.setProduto(null);
            bal.setHorarioFimFormacao(null);
            bal.setHorarioInicioFormacao(null);
         }
      }

      // ... verifica se a pilha que esta sendo recuperada ainda possui alguma baliza com produto
      if (pilhaDasBalizas.getListaDeBalizas() == null || pilhaDasBalizas.getListaDeBalizas().isEmpty())
      {
         // ... remove a pilha da lista de pilhas dos patios pois a mesma foi retornada completamente
         situacaoDePatio.getListaDePilhasNosPatios().remove(pilhaDasBalizas);
      } 
      else 
      {
         List<Pilha> listaNovasPilhas = decompoePilhaEditada(pilhaDasBalizas);
         if (listaNovasPilhas != null && listaNovasPilhas.size() > 0)
         {
            situacaoDePatio.getListaDePilhasNosPatios().addAll(listaNovasPilhas);
            situacaoDePatio.getListaDePilhasNosPatios().remove(pilhaDasBalizas);
         }
         else
         {
            situacaoDePatio.getListaDePilhasNosPatios().remove(pilhaDasBalizas);
         }
      }

      // 13) seta a posicao da maquina
      if (listaBalizasEquivalentes != null && listaBalizasEquivalentes.size() > 0 && lugarEmpRecAtual.getSentido() == null){
         maquinaEquivalente.setPosicao(listaBalizasEquivalentes.get(listaBalizasEquivalentes.size()-1));
      }else if(lugarEmpRecAtual.getSentido() == SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL){
          maquinaEquivalente.setPosicao(listaBalizasEquivalentes.get(listaBalizasEquivalentes.size()-1));
      }else{
          maquinaEquivalente.setPosicao(listaBalizasEquivalentes.get(0));
      }

      // 14) seta a atividade atual
      maquinaEquivalente.setAtividade(atividadeRetornoPelletFeed);

      situacaoDePatio.setAtividade(atividadeRetornoPelletFeed);

      return situacaoDePatio;

   }

   *//**
    * Retorna lista de balizas equivalentes da situacao de patio passada por parametro.
    * @param listaDeBalizas Lista de balizas de uma situacao qualquer.
    * @param situacaoDoPatio Situacao de patio de onde vao sair as balizas equivalentes
    * @return Lista de balizas equivalentes.
    *//*
   public static List<Baliza> obterBalizasEquivalentes(List<Baliza> listaDeBalizas, SituacaoPatio situacaoDoPatio)
   {
      List<Baliza> balizasEquivalentes = new ArrayList<Baliza>();
      for(Baliza baliza : listaDeBalizas)
      {
         Baliza balizaEquivalente = ControladorMaquinas.buscaBalizaEquivalente(baliza, situacaoDoPatio);
         balizasEquivalentes.add(balizaEquivalente);
      }
      return balizasEquivalentes;
   }

   *//**
    * Metodo que decompoe a pilha editada em diversas pilhas se necessario
    * @param pilhaRecuperada
    * @return
    *//*
   private List<Pilha> decompoePilhaEditada(Pilha pilhaEditada)
   {
      List<Pilha> listaPilhaAtualizada = new ArrayList<Pilha>();
      Pilha novaPilha = null;

      List<Baliza> novaListaBalizas = new ArrayList<Baliza>();

      int i = 0;
      Collections.sort(pilhaEditada.getListaDeBalizas(), new ComparadorBalizas());
      for (Baliza balizaPilha : pilhaEditada.getListaDeBalizas())
      {

         // pega o tipo de produto da baliza anterior para verificar se eh o mesmo da baliza atual
         Baliza balizaAnterior = (i > 0) ? pilhaEditada.getListaDeBalizas().get(i - 1) : balizaPilha;

         // verifica se o produto da baliza e a pilha da baliza possui objetos
         if (balizaPilha.getPilha() != null && balizaPilha.getProduto() != null)
         {
            if (balizaAnterior.getProduto() == null && balizaPilha.getProduto() != null)
            {
               novaListaBalizas.add(balizaPilha);
            }
            else if (balizaPilha.getProduto().getTipoProduto().equals(balizaAnterior.getProduto().getTipoProduto()))
            {
               novaListaBalizas.add(balizaPilha);
            }
            else
            {
               
               // limpando o objeto pilha da baliza
               balizaPilha.setPilha(null);

               // cria uma nova pilha qdo encontrado elementos nulos na lista de balizas da pilha
               novaPilha = new Pilha();

               // adiciona a nova pilha na lista
               listaPilhaAtualizada.add(novaPilha);

               novaPilha.setCliente(pilhaEditada.getCliente());
               novaPilha.setNomePilha(pilhaEditada.getNomePilha()+"_"+listaPilhaAtualizada.size());
               for (Baliza novaBaliza : novaListaBalizas)
               {
                  novaBaliza.setPilha(novaPilha);
               }
               Collections.sort(novaListaBalizas, new ComparadorBalizas());
               novaPilha.setListaDeBalizas(novaListaBalizas);

               novaListaBalizas = new ArrayList<Baliza>();
               novaListaBalizas.add(balizaPilha);
            }
         }
         else if (balizaPilha.getPilha() != null && balizaPilha.getProduto() == null)
         {

            // limpando o objeto pilha da baliza
            balizaPilha.setPilha(null);

            // verifica se a baliza anterior possui produto para a criacao da nova pilha
            if (balizaAnterior.getPilha() != null && balizaAnterior.getProduto() != null)
            {

               // cria uma nova pilha qdo encontrado elementos nulos na lista de balizas da pilha
               novaPilha = new Pilha();

               // adiciona a nova pilha na lista
               listaPilhaAtualizada.add(novaPilha);

               novaPilha.setCliente(pilhaEditada.getCliente());
               novaPilha.setNomePilha(pilhaEditada.getNomePilha()+"_"+listaPilhaAtualizada.size());
               for (Baliza novaBaliza : novaListaBalizas)
               {
                  novaBaliza.setPilha(novaPilha);
               }
               Collections.sort(novaListaBalizas, new ComparadorBalizas());
               novaPilha.setListaDeBalizas(novaListaBalizas);

               novaListaBalizas = new ArrayList<Baliza>();
            }
         }
         i++;
      }

      if (!novaListaBalizas.isEmpty())
      {
         // cria uma nova pilha qdo encontrado elementos nulos na lista de balizas da pilha
         novaPilha = new Pilha();

         // adiciona a nova pilha na lista
         listaPilhaAtualizada.add(novaPilha);

         novaPilha.setCliente(pilhaEditada.getCliente());
         novaPilha.setNomePilha(pilhaEditada.getNomePilha()+"_"+listaPilhaAtualizada.size());
         for (Baliza novaBaliza : novaListaBalizas)
         {
            novaBaliza.setPilha(novaPilha);
         }
         Collections.sort(novaListaBalizas, new ComparadorBalizas());
         novaPilha.setListaDeBalizas(novaListaBalizas);

      }

      return listaPilhaAtualizada;
   }

   public Atividade getAtividade()
   {
      return this.atividadeRetornoPelletFeed;
   }
*/
}
