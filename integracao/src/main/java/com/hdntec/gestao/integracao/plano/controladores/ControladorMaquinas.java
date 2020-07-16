package com.hdntec.gestao.integracao.plano.controladores;

import java.util.List;

import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


/**
 * 
 * @author Ricardo Trabalho
 * @author andre (LT)
 */
public class ControladorMaquinas
{
   
   /**
    * Desloca uma maquina fora de operacao ate uma posicao desejada ou ate onde for possivel com o tempo maximo passado.
    * 
    * @param maquina
    *           a maquina a ser deslocada no patio a que pertence
    * @param balizaFinal
    *           a posicao final desejada da maquina.
    * @param duracaoMaxima
    *           a duracao maxima para ocorrer essa operacao de deslocamento.
    * @return o tempo em horas referente ao tempo gasto para deslocar
    */
   public static Double deslocaMaquinaNaoOperando(MaquinaDoPatio maquina, Baliza balizaFinal, Double duracaoMaxima)
   {
      Double tempoDeslocamento = null;
      tempoDeslocamento = calculaTempoDeslocamentoNaoOperando(maquina, balizaFinal);
      if (duracaoMaxima != null && duracaoMaxima.compareTo(tempoDeslocamento) >= 0)
      {
         maquina.setPosicao(balizaFinal);
      } else
      {
         Baliza baliza = obterBalizaFinal(maquina, balizaFinal, duracaoMaxima);
         maquina.setPosicao(baliza);
         tempoDeslocamento = new Double(duracaoMaxima);
      }
      return tempoDeslocamento;
   }
   
   /**
    * Desloca uma maquina em operacao ate a baliza final desejada.
    * 
    * @param maquina
    *           MaquinaDoPatio que realizara a operacao.
    * @param indexBalizaInicial
    *           Posicao no patio da baliza onde se encontra a maquina.
    * @param indexBalizaFinal
    *           Posicao final onde sera posicionada a maquina.
    * @param quantidadeTotal
    *           Quantidade total de produto a ser movimentado em toneladas
    * @return Tempo em horas necessario para realizar essa atividade.
    */
   public static Long deslocaMaquinaOperando(MaquinaDoPatio maquina, int indexBalizaInicial, int indexBalizaFinal, Double quantidadeTotal)
   {
      Long tempoDeslocamento = null;
      tempoDeslocamento = calculaTempoDeslocamentoOperando(maquina, quantidadeTotal);
     // maquina.setPosicao(maquina.getPosicao().getPatio().getListaDeBalizas().get(indexBalizaFinal));
      return tempoDeslocamento;
   }
   
   /** tempo de deslocamento ate uma baliza de uma maquina nao operando */
   private static Double calculaTempoDeslocamentoNaoOperando(MaquinaDoPatio maquina, Baliza balizaFinal)
   {
      Baliza balizaInicial = maquina.getPosicao();
      Double velocidadeDeslocamento = maquina.getVelocidadeDeslocamento();
      Integer distanciaEntreBalizas = Math.abs(balizaFinal.getNumero() - balizaInicial.getNumero());
      return distanciaEntreBalizas / velocidadeDeslocamento;
   }
   
   /** tempo de deslocamento ate uma baliza de uma maquina em operacao recolhendo todo conteudo das balizas */
   private static Long calculaTempoDeslocamentoOperando(MaquinaDoPatio maquina, Double quantidadeTotal)
   {
      // Taxa de operacao por tonelada.
      Long taxaDeOperacao = maquina.getTaxaDeOperacaoTemporaria().longValue();
      return quantidadeTotal.longValue() / taxaDeOperacao;
   }
   
   // obter baliza final dado um tempo maximo de deslocamento.
   private static Baliza obterBalizaFinal(MaquinaDoPatio maquina, Baliza balizaFinal, Double duracaoMaxima)
   {
      Double velocidadeDeslocamento = maquina.getVelocidadeDeslocamento();
      Baliza baliza = null;
      Integer numeroDeBalizasQueAMaquinaAndaNaDuracaoMaxima = new Double(Math.ceil(duracaoMaxima * velocidadeDeslocamento)).intValue();
      if (balizaFinal.getNumero().compareTo(maquina.getPosicao().getNumero()) > 0)
      {
         // verifica se consegue atingir a baliza final.
         Integer distanciaEmBalizas = balizaFinal.getNumero() - maquina.getPosicao().getNumero();
         if (distanciaEmBalizas <= numeroDeBalizasQueAMaquinaAndaNaDuracaoMaxima)
         {
            baliza = balizaFinal;
         } else
         {
            Integer posicaoFinal = numeroDeBalizasQueAMaquinaAndaNaDuracaoMaxima + maquina.getPosicao().getNumero();
         /*   for (Baliza balizaTemporaria : balizaFinal.getPatio().getListaDeBalizas())
            {
               if (balizaTemporaria.getNumero().equals(posicaoFinal))
               {
                  baliza = balizaTemporaria;
               }
            }*/
         }
      } else
      {
         // verifica se consegue atingir a baliza final.
         Integer distanciaEmBalizas = maquina.getPosicao().getNumero() - balizaFinal.getNumero();
         if (distanciaEmBalizas <= numeroDeBalizasQueAMaquinaAndaNaDuracaoMaxima)
         {
            baliza = balizaFinal;
         } else
         {
            Integer posicaoFinal = maquina.getPosicao().getNumero() - numeroDeBalizasQueAMaquinaAndaNaDuracaoMaxima;
           /* for (Baliza balizaTemporaria : balizaFinal.getPatio().getListaDeBalizas())
            {
               if (balizaTemporaria.getNumero().equals(posicaoFinal))
               {
                  baliza = balizaTemporaria;
               }
            }*/
         }
      }
      return baliza;
   }
   
   /**
    * Busca maquina equivalente a maquina passada por parametro na nova situacao de patio passada por parametro.
    * 
    * @param maquinaUtilizada
    *           Maquina da situacao de patio inicial
    * @param situacaoPatio
    *           Situacao de patio nova
    * @return a maquina equivalente
    */
   public static MaquinaDoPatio buscaMaquinaEquivalente(MaquinaDoPatio maquinaUtilizada, SituacaoPatio situacaoDePatio)
   {
      MaquinaDoPatio maquinaEquivalente = null;
      for (MaquinaDoPatio maq : situacaoDePatio.getPlanta().getListaMaquinasDoPatio(situacaoDePatio.getDtInicio()))
      {
         if (maquinaUtilizada.getNomeMaquina().equalsIgnoreCase(maq.getNomeMaquina()))
         {
            maquinaEquivalente = maq;
            break;
         }
      }
      return maquinaEquivalente;
   }
   
   /**
    * 
    * @param baliza
    *           Baliza da situacao inicial
    * @param sitPatio
    *           Situacao de patio de onde vai ser pega a baliza equivalente a baliza passada por parametro
    * @return Baliza equivalente na situacao de patio passada em relacao a baliza passada por parametro
    */
   public static Baliza buscaBalizaEquivalente(Baliza baliza, SituacaoPatio sitPatio)
   {
      Baliza balizaResultante = null;
      if (baliza != null)
      {
        /* for (Patio patio : sitPatio.getPlanta().getListaPatios())
         {
            if (patio.getNumero().equals(baliza.getPatio().getNumero()))
            {
               for (Baliza balizaEquivalente : patio.getListaDeBalizas())
               {
                  if (balizaEquivalente.getNumero().equals(baliza.getNumero()))
                  {
                     balizaResultante = balizaEquivalente;
                     break;
                  }
               }
            }
         }*/
      }
      return balizaResultante;
   }

   /**
    *
    * @param baliza
    *           Baliza da situacao inicial
    * @param sitPatio
    *           Situacao de patio de onde vai ser pega a baliza equivalente a baliza passada por parametro
    * @return Baliza equivalente na situacao de patio passada em relacao a baliza passada por parametro
    */
   public static Baliza buscaBalizaEquivalente(Baliza baliza, SituacaoPatio sitPatio, EnumTipoBaliza tipoBaliza)
   {
      Baliza balizaResultante = null;
      if (baliza != null)
      {
         /*for (Patio patio : sitPatio.getPlanta().getListaPatios())
         {
            if (patio.getNumero().equals(baliza.getPatio().getNumero()))
            {
               for (Baliza balizaEquivalente : patio.getListaDeBalizas())
               {
                  if (balizaEquivalente.getNumero().equals(baliza.getNumero()) && balizaEquivalente.getTipoBaliza().equals(tipoBaliza))
                  {
                     balizaResultante = balizaEquivalente;
                     break;
                  }
               }
            }
         }*/
      }
      return balizaResultante;
   }

   /**
    * Encontra a baliza inicial da parte da recuperação, baseado na lista de balizas recuperadas e no sentido
    * 
    * @param listaDeBalizasDaParteDarecuperacao
    *           a lista de balizas recuperadas nesta parte da recuperação
    * @param sentidoDaMaquinaRecuperacao
    *           o sentido da máquina na recuperacão
    * @return a baliza inicial da parte da recuperação
    */
   public static Baliza encontraBalizaInicialDaMaquinaNaParteDaRecuperacao(List<Baliza> listaDeBalizasDaParteDarecuperacao, SentidoEmpilhamentoRecuperacaoEnum sentidoDaMaquinaRecuperacao)
   {
      Baliza balizaInicialDaRecuperacao = null;
      // se o sentido da recuperação for da esquerda para a direita ...
      if (sentidoDaMaquinaRecuperacao.equals(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL))
      {
         Integer menorMarcacaoBaliza = Integer.MAX_VALUE;
         // ... a baliza inicial será aquela que tiver o menor número ...
         for (Baliza balizaAnalisada : listaDeBalizasDaParteDarecuperacao)
         {
            Integer numeroDaBalizaAnalisada = balizaAnalisada.getNumero();
            if (numeroDaBalizaAnalisada < menorMarcacaoBaliza)
            {
               menorMarcacaoBaliza = numeroDaBalizaAnalisada;
               balizaInicialDaRecuperacao = balizaAnalisada;
            }
         }
      } else
         // ... caso contrário ...
      {
         Integer maiorMarcacaoBaliza = Integer.MIN_VALUE;
         // ... a baliza inicial será aquela que tiver o maior número ...
         for (Baliza balizaAnalisada : listaDeBalizasDaParteDarecuperacao)
         {
            Integer numeroDaBalizaAnalisada = balizaAnalisada.getNumero();
            if (numeroDaBalizaAnalisada > maiorMarcacaoBaliza)
            {
               maiorMarcacaoBaliza = numeroDaBalizaAnalisada;
               balizaInicialDaRecuperacao = balizaAnalisada;
            }
         }
      }
      return balizaInicialDaRecuperacao;
   }
   
   public static void movimentaMaquina(MaquinaDoPatio maquinaUtilizada, SentidoEmpilhamentoRecuperacaoEnum sentidoDaMaquinaNaAtividade, List<Baliza> listaDeBalizasRecuperadas, SituacaoPatio situacaoDePatio, Double duracaoMaxima)
   {
      // encontrando a baliza onde a atividade irá começar
      Baliza balizaInicialDaMaquinaNaRecuperacao = encontraBalizaInicialDaMaquinaNaParteDaRecuperacao(listaDeBalizasRecuperadas, sentidoDaMaquinaNaAtividade);
      // clona a máquina
      MaquinaDoPatio maquinaDaNovaSituacao = buscaMaquinaEquivalente(maquinaUtilizada, situacaoDePatio);
      // clona a balizaInicial
      Baliza balizaDaNovaSituacao = buscaBalizaEquivalente(balizaInicialDaMaquinaNaRecuperacao, situacaoDePatio);
      // desloca a empilhadeira-recuperadora
      deslocaMaquinaNaoOperando(maquinaDaNovaSituacao, balizaDaNovaSituacao, duracaoMaxima);
   }
   
   /**
    * Verifica durante o calculo da quantidade de uma recuperação se a baliza que está sendo recuperada
    * tem um atividade de empilhamento em paralelo
    * @param baliza
    *           Baliza da situacao inicial
    * @param sitPatio
    *           Situacao de patio de onde vai ser pega a baliza equivalente a baliza passada por parametro
    * @return Baliza equivalente na situacao de patio passada em relacao a baliza passada por parametro
    */
   public static Boolean existeEmpilhamentoParalelo(Baliza baliza, SituacaoPatio sitPatio)
   {
      Boolean result = Boolean.FALSE;
      if (baliza != null)
      {
    	  for (MetaCorreia metaCorreia : sitPatio.getPlanta().getListaMetaCorreias())
        {
    	     Correia correia = metaCorreia.retornaStatusHorario(sitPatio.getDtInicio());
           if ( (correia.getPatioInferior() != null && correia.getPatioInferior().equals(baliza.getPatio()) ) ||
                (correia.getPatioSuperior() != null &&  correia.getPatioSuperior().equals(baliza.getPatio())) ) {
          	  result = validarAtividadeMaquina(baliza, correia);
          	 if (result) break;             	 
           }
          }
      }
      return result;
   
}

   
private static Boolean validarAtividadeMaquina(Baliza baliza, Correia correia) {
	Boolean result = Boolean.FALSE;
	for (MaquinaDoPatio maquina : correia.getListaDeMaquinas(correia.getDtInicio())) {
		 if (maquina.getPosicao().getMetaBaliza().equals(baliza.getMetaBaliza()) && maquina.getEstado().equals(EstadoMaquinaEnum.OPERACAO) && 
				 maquina.getAtividade() != null && maquina.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO))  {
			 result = Boolean.TRUE;
			 break;
		 }
	 }
	return result;
}




public static Boolean validarAtividadeMaquina(MaquinaDoPatio maquina) {
	Boolean result = Boolean.FALSE;
		 if (maquina.getEstado().equals(EstadoMaquinaEnum.OPERACAO) && 
				 maquina.getAtividade() != null && maquina.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO))  {
			 result = Boolean.TRUE;
		 }
	return result;
}
}



