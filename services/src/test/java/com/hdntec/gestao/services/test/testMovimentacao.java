package com.hdntec.gestao.services.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO;

public class testMovimentacao
{
    private Pilha pilhaAtualOrigem = null;
    private Pilha pilhaAtualDestino = null;
    public Atividade movimentar(MovimentacaoVO movimentacaoVO)
   {
      // TODO Auto-generated method stub
      List<Baliza> balizasOrigem = new ArrayList<Baliza>();
      List<Baliza> balizasDestino = new ArrayList<Baliza>();
      List<MaquinaDoPatio> maquinas = new ArrayList<MaquinaDoPatio>();
      List<Correia> correias = new ArrayList<Correia>();
      Boolean finalizarAtividade = Boolean.FALSE;
      Atividade atividade = new Atividade();
      atividade.setTipoAtividade(TipoAtividadeEnum.MOVIMENTAR_PILHA_PELLET_FEED);
      
      LugarEmpilhamentoRecuperacao lugarEmpRecOrigem = new LugarEmpilhamentoRecuperacao();
      

      try
      {
         /* verifica se deve finalizar a operacao */
         if (movimentacaoVO.getDataFim() != null)
         {
             pilhaAtualOrigem = movimentacaoVO.getListaBalizas().get(0).retornaStatusHorario(movimentacaoVO.getDataFim()).retornaStatusHorario(movimentacaoVO.getDataFim());
             pilhaAtualDestino = movimentacaoVO.getListaBalizasDestino().get(0).retornaStatusHorario(movimentacaoVO.getDataFim()).retornaStatusHorario(movimentacaoVO.getDataFim());
             finalizarAtividade = Boolean.TRUE;
         } else {
             pilhaAtualOrigem = movimentacaoVO.getListaBalizas().get(0).retornaStatusHorario(movimentacaoVO.getDataInicio()).retornaStatusHorario(movimentacaoVO.getDataInicio());
             pilhaAtualDestino = movimentacaoVO.getListaBalizasDestino().get(0).retornaStatusHorario(movimentacaoVO.getDataInicio()).retornaStatusHorario(movimentacaoVO.getDataInicio());

         
         }
         
         if (finalizarAtividade)
         {
            atividade.setDtInicio(movimentacaoVO.getDataInicio());
            atividade.setDtFim(movimentacaoVO.getDataFim());
            atividade.setDtInsert(new Date(System.currentTimeMillis()));
            atividade.setIdUser(1L);
            lugarEmpRecOrigem.setDtFim(movimentacaoVO.getDataFim());
            lugarEmpRecOrigem.setExecutado(Boolean.TRUE);            
         } else
         {
            atividade.setDtInicio(movimentacaoVO.getDataInicio());
            atividade.setDtInsert(new Date(System.currentTimeMillis()));
            atividade.setIdUser(1L);
         }
         
         /**
          1- aplica atividade nas maquinas
         */
         List<SentidoEmpilhamentoRecuperacaoEnum> sentidos = new ArrayList<SentidoEmpilhamentoRecuperacaoEnum>();
         sentidos.add(movimentacaoVO.getSentidoRecuperacao());
         sentidos.add(movimentacaoVO.getSentidoEmpilhamento());
         
      // aplica atividade nas balizas e retorna  a lista de novos estados
         balizasOrigem = MetaBaliza.gerarAtividadeMovimentacaoRecuperacaoBalizas(movimentacaoVO);
         balizasDestino = MetaBaliza.gerarAtividadeMovimentacaoEmpilhamentoBalizas(movimentacaoVO);
          
         
         List<LugarEmpilhamentoRecuperacao> lugarAnterior = new ArrayList<LugarEmpilhamentoRecuperacao>();
         MetaMaquinaDoPatio.gerarAtividadeMaquina(movimentacaoVO, lugarAnterior, balizasOrigem, balizasDestino,   maquinas,
                         correias, atividade, finalizarAtividade);
         
         
         if (movimentacaoVO.getDataFim() == null) {
                 // cria a pilha destino
             // cria a pilha destino
             
             
             Pilha novaPilhaOrigem = Pilha.criaPilha(movimentacaoVO.getNomePilhaDestino(), balizasOrigem,
                             movimentacaoVO.getCliente(), movimentacaoVO.getDataInicio().getTime(),pilhaAtualOrigem);
             if (pilhaAtualOrigem != null)
                 
             novaPilhaOrigem.addBaliza(pilhaAtualOrigem.getListaDeBalizas());
             
             Pilha novaPilhaDestino = Pilha.criaPilha(movimentacaoVO.getNomePilhaDestino(), balizasDestino, 
                   movimentacaoVO.getCliente(), movimentacaoVO.getDataInicio().getTime(),pilhaAtualDestino);
             if (pilhaAtualDestino != null)
                 
             novaPilhaDestino.addBaliza(pilhaAtualDestino.getListaDeBalizas());

         }
         
         /**
          * 2 - efetua a movimentacao do material 
          */
         realizarMovimentacaoPilhaPilha(movimentacaoVO,balizasOrigem,balizasDestino);

         /**
          * 3 - cria os lugares de empilhamento e recuperacao
          */
         lugarEmpRecOrigem.setDtFim(new Date(System.currentTimeMillis()));
         lugarEmpRecOrigem.setIdUser(1L);
         lugarEmpRecOrigem.setDtInicio(movimentacaoVO.getDataInicio());
         lugarEmpRecOrigem.setQuantidade(movimentacaoVO.getQuantidadeMovimentacao());
         lugarEmpRecOrigem.addMaquinaDoPatio(maquinas);
         lugarEmpRecOrigem.addBaliza(balizasOrigem);
         lugarEmpRecOrigem.addBaliza(balizasDestino);
         lugarEmpRecOrigem.addCorreia(correias);
         lugarEmpRecOrigem.setTipoProduto(movimentacaoVO.getTipoProduto());
         atividade.addLugarEmpilhamento(lugarEmpRecOrigem);
         
                  
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      return atividade;

   }
   
   private void realizarMovimentacaoPilhaPilha(MovimentacaoVO movimentacaoVO,List<Baliza> balizasOrigem,List<Baliza> balizasDestino) 
   {
	   
        if (movimentacaoVO.getDataFim() != null) {

            // pega o produto da primeira baliza da pilha origem
            Produto produtoTransporte = balizasOrigem.get(0).getProduto();

            // duracao da atividade
            double horasDuracaoAtividade = ((((movimentacaoVO.getDataFim().getTime() - movimentacaoVO.getDataInicio()
                            .getTime()) / 1000)) / 60.0) / 60.0;

            // quantidade maxima que a maquina pode ter movimentado de acordo com sua taxa de operacao
            MaquinaDoPatio maquinaOrigem = movimentacaoVO.getListaMaquinas().get(0).retornaStatusHorario(
                            movimentacaoVO.getDataFim());
            double capacidadeMaquinaTempo = maquinaOrigem.quantidadePorTempo(horasDuracaoAtividade);

            // limita a quantidade movimentada se ultrapassar a capacidade da maquina
            movimentacaoVO.setQuantidadeMovimentacao(Math.min(capacidadeMaquinaTempo, movimentacaoVO
                            .getQuantidadeMovimentacao()));

            // quantidade de produto empilhado em cada baliza destino
            double quantidadeProdutoPorBaliza = (movimentacaoVO.getQuantidadeMovimentacao() / balizasDestino.size());
                    
            //TODO atualizar qualidade e fazer blendagem

            for (Baliza balizaDestino : balizasDestino) {
                Produto produtoNovo = Produto.criaProduto(movimentacaoVO.getTipoProduto(), movimentacaoVO.getDataFim()
                                .getTime());
                produtoNovo.setQuantidade(quantidadeProdutoPorBaliza);

                Qualidade novaQualidade = new Qualidade();
                novaQualidade.setListaDeAmostras(new ArrayList<Amostra>());
                //novaQualidade.setListaDeItensDeControle(Blendagem.criaListaDeItensDeControleComValoresVazios(produtoTransporte.getQualidade().getListaDeItensDeControle()));
                novaQualidade.setEhReal(Boolean.FALSE);

                produtoNovo.setQualidade(novaQualidade);
                novaQualidade.setProduto(produtoNovo);

                if (balizaDestino.getProduto() != null) {
                    Blendagem blend = Blendagem.getInstance();
                    Produto produtobaliza = balizaDestino.getProduto().copiarStatus();
                    blend.atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposInsercaoDoProduto(produtobaliza,
                                    produtobaliza.getQuantidade());
                    blend.atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposInsercaoDoProduto(produtoNovo,
                                    quantidadeProdutoPorBaliza);
                    produtoNovo = blend.calculaProdutoResultanteDaInsercaoDeProdutoNaBlendagem(produtobaliza, produtoNovo,
                                    quantidadeProdutoPorBaliza);
                } else {
                    produtoNovo.setQuantidade(quantidadeProdutoPorBaliza);
                }

                /*  List<Rastreabilidade> listaRastreabilidades = new ArrayList<Rastreabilidade>();

                  listaRastreabilidades.add(acrescentaRastreabilidade(dataInicio,
                        dataFinal, null, null, quantidadeProdutoPorBaliza, "",
                        produtoNovo));
                  produtoNovo.addRastreabilidade(listaRastreabilidades);
                */
                balizaDestino.setProduto(produtoNovo);
                /*  balizaDestino.setPilha(novaPilhaDestino);
                  novaPilhaDestino.addBaliza(balizaDestino);
                  balizaDestino.setHorarioInicioFormacao(dataInicio);
                  balizaDestino.setHorarioFimFormacao(dataFinal);*/
            }


            
            retiraMaterialDaPilhaOrigem(movimentacaoVO,balizasOrigem, movimentacaoVO.getQuantidadeMovimentacao());

            // ordena a lista de balizas
            Collections.sort(balizasDestino, new ComparadorBalizas());
            // novaPilhaDestino.setListaDeBalizas(listaBalizasDestino);
                      
            // cria a pilha destino
            Pilha novaPilhaDestino = Pilha.criaPilha(movimentacaoVO.getNomePilhaDestino(), balizasDestino,
                            movimentacaoVO.getCliente(), movimentacaoVO.getDataFim().getTime(),pilhaAtualDestino);
            
            novaPilhaDestino.addBaliza(pilhaAtualDestino.getListaDeBalizas());

        }
   }

	private void retiraMaterialDaPilhaOrigem(MovimentacaoVO vo, List<Baliza> listaBalizasOrigem, double quantidadeMovimentada) 
	{
		double quantidadePendente = quantidadeMovimentada;
		while (quantidadePendente > 0) 
		{
		   for (Baliza bal : listaBalizasOrigem) 
		   {
			   // determina quanto vai retirar da baliza
			   double quantidadeRecuperadaBaliza = Math.min(bal.getProduto().getQuantidade(), quantidadePendente);
				quantidadePendente -= quantidadeRecuperadaBaliza;
				bal.getProduto().setQuantidade(bal.getProduto().getQuantidade() - quantidadeRecuperadaBaliza);
			}
		}
	   List<Baliza> balizaFinais = new ArrayList<Baliza>();
		// verifica se � necessario ajustar a pilha
		for (Baliza baliza : listaBalizasOrigem) 
		{
			if (baliza.getProduto().getQuantidade().doubleValue() <= 0.0d) 
			{				
			    baliza.setProduto(null);
				baliza.setHorarioFimFormacao(null);
				baliza.setHorarioInicioFormacao(null);
            } else {
                balizaFinais.add(baliza);
            }
		}
		
		  // cria a pilha destino
        Pilha novaPilhaOrigem = Pilha.criaPilha(vo.getNomePilhaDestino(), balizaFinais,
                        vo.getCliente(), vo.getDataFim().getTime(),pilhaAtualOrigem);

        novaPilhaOrigem.addBaliza(pilhaAtualOrigem.getListaDeBalizas());

		
	}
}
