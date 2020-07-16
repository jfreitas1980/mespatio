/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.integracao.planoDeEmpilhamentoERecuperacao.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;


/**
 * Esta classe auxiliar fornece alguns metodos para tratamento de pilhas virtuais que são
 * usados em varias partes do código
 * @author Ricardo Trabalho
 */
public class ControladorLugaresDeEmpilhamentoOuRecuperacao {

    /**
     * este metodo estatico retorna uma lista de pilhas virtuais por maquina de trabalho.
     * @param pilhasVirtuais Lista de pilhas virtuais.
     * @return Lista de listas de pilhas virtuais separadas por maquina de execucao.
     */
    public static List<List<LugarEmpilhamentoRecuperacao>> pilhasVirtuaisPorMaquina(List<LugarEmpilhamentoRecuperacao> pilhasVirtuais){
        List<List<LugarEmpilhamentoRecuperacao>> pilhasVirtuaisPorMaquina = null;
        List<MaquinaDoPatio> listaDeMaquinas = obterListaDeTodasAsMaquinasDaAtividade(pilhasVirtuais);

        pilhasVirtuaisPorMaquina = obterMatrizDeTodasAsPilhasDaAtividadePorCadaMaquina(listaDeMaquinas,pilhasVirtuais);
        return pilhasVirtuaisPorMaquina;
    }

    private static List<MaquinaDoPatio> obterListaDeTodasAsMaquinasDaAtividade(List<LugarEmpilhamentoRecuperacao> listaDePilhasDaAtividadeNoFuturo)
    {
         List<MaquinaDoPatio> listaDeMaquinas = new ArrayList<MaquinaDoPatio>();
       /* for (int i = 0; i < listaDePilhasDaAtividadeNoFuturo.size(); i++)
        {
            LugarEmpilhamentoRecuperacao pilha = listaDePilhasDaAtividadeNoFuturo.get(i);
            MaquinaDoPatio maquina = pilha.getMaquinaDoPatio();
            if (!listaDeMaquinas.contains(maquina)) {
                listaDeMaquinas.add(maquina);
            }
        }*/

         return listaDeMaquinas;
    }

    private static List<List<LugarEmpilhamentoRecuperacao>> obterMatrizDeTodasAsPilhasDaAtividadePorCadaMaquina(List<MaquinaDoPatio> listaDeMaquinas, List<LugarEmpilhamentoRecuperacao> listaDePilhasRecuperadasNoFuturo)
    {
         List<List<LugarEmpilhamentoRecuperacao>> matrizDePilhasPorMaquinas = new ArrayList<List<LugarEmpilhamentoRecuperacao>>();
        /*for(int i=0; i<listaDeMaquinas.size();i++)
        {
         /*  MaquinaDoPatio maquina = listaDeMaquinas.get(i);
           List<LugarEmpilhamentoRecuperacao> listaDePilhasDaMaquina= new ArrayList<LugarEmpilhamentoRecuperacao>();
            for (int j = 0; j < listaDePilhasRecuperadasNoFuturo.size(); j++)
            {
                LugarEmpilhamentoRecuperacao pilha = listaDePilhasRecuperadasNoFuturo.get(j);
                if(maquina.equals(pilha.getMaquinaDoPatio()))
                    listaDePilhasDaMaquina.add(pilha);
            }
           matrizDePilhasPorMaquinas.add(listaDePilhasDaMaquina);
        }*/
        	return null;
         //return matrizDePilhasPorMaquinas;
    }

    /**
     * Retorna a pilha virtual que tem a baliza inicial mais proxima da maquina de execucao
     * e ainda nao foi executada.
     * @param listaPilhaVirtualMaquina Lista de pilhas virtuais.
     * @return pilha virtual.
     */
    public static LugarEmpilhamentoRecuperacao obterPilhaVirtualQueAindaNaoFoiExecutadaComBalizaInicialMaisProximaDaMaquinaEmExecucao(List<LugarEmpilhamentoRecuperacao> listaPilhaVirtualMaquina) {
        LugarEmpilhamentoRecuperacao lugarDeEmpilhamentoOuRecuperacao = null;
        Double menorTempoParaInicio=Double.MAX_VALUE;
        for(LugarEmpilhamentoRecuperacao pilha : listaPilhaVirtualMaquina)
        {
            Double tempoParaInicio = tempoDaPilhaAteSuaMaquina(pilha);
            if(tempoParaInicio<menorTempoParaInicio&&!pilha.getExecutado())
            {
                lugarDeEmpilhamentoOuRecuperacao = pilha;
                menorTempoParaInicio = tempoParaInicio;
            }
        }
        return lugarDeEmpilhamentoOuRecuperacao;
    }

    public static Double obterTempoAtePilhaRecuperadaVirtualMaisProximaDeSuaMaquina(List<LugarEmpilhamentoRecuperacao> listaDePilhasRecuperadasNoFuturo) {

        Double menorTempoParaInicio=Double.MAX_VALUE;
        for(LugarEmpilhamentoRecuperacao pilha : listaDePilhasRecuperadasNoFuturo)
        {
            Double tempoParaInicio = tempoDaPilhaAteSuaMaquina(pilha);
            if(tempoParaInicio<menorTempoParaInicio)
            {
                menorTempoParaInicio = tempoParaInicio;
            }
        }
        return menorTempoParaInicio;

    }

    public static Double tempoDaPilhaAteSuaMaquina(LugarEmpilhamentoRecuperacao pilha)
    {
        /* MaquinaDoPatio maquina = pilha.getMaquinaDoPatio();
         Baliza balizaInicial = null;
         if(pilha.getSentido().equals(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL)){
             balizaInicial = pilha.getListaDeBalizas().get(0);
         }
         else{
             balizaInicial = pilha.getListaDeBalizas().get(pilha.getListaDeBalizas().size()-1);
         }
         Double tempoParaPercorrerDistancia = obterTempoParaPercorrerDistancia(maquina.getPosicao().getNumero(),balizaInicial.getNumero(),maquina.getVelocidadeDeslocamento());
         return tempoParaPercorrerDistancia;*/
    	return null;
    }

    public static Double obterTempoParaPercorrerDistancia(Integer balizaInicial, Integer balizaFinal, Double velocidadeDeDeslocamento) {
        Integer distancia= Math.abs(balizaInicial - balizaFinal);
        Double tempoParaPercorrerDistancia = ((distancia/velocidadeDeDeslocamento));
        return tempoParaPercorrerDistancia;
    }
    
    /**
     * Calcula a diferenca em horas dessas duas datas passadas
     * @param inicioExecucao Data inicial com data antiga
     * @param fimExecucao Data final com valor mais atual
     * @return Retorna a diferenca em horas das duas datas.
     */
    public static Double obterTempoMaximoExecucao(Date inicioExecucao, Date fimExecucao) {
        return (double)((fimExecucao.getTime() - inicioExecucao.getTime()) / (1000d*60d*60d));
    }
    
    public static List<Baliza> inicializarBalizasPilha(LugarEmpilhamentoRecuperacao lugarDeEmpilhamentoOuRecuperacao, SituacaoPatio situacaoDoPatio)
    {
       List<Baliza> listaBalizas = obterBalizasEquivalentes(lugarDeEmpilhamentoOuRecuperacao.getListaDeBalizas(), situacaoDoPatio);
      /* for(Baliza baliza : listaBalizas){
           if(baliza.getProduto() == null){
               baliza.setUtilizada(new Boolean(true));
           }
           else{
               baliza.setUtilizada(new Boolean(false));
           }
       }*/
       return listaBalizas;
    }
    
    public static List<Baliza> inicializarBalizasPilhaEmpilhamento(LugarEmpilhamentoRecuperacao lugarDeEmpilhamentoOuRecuperacao, SituacaoPatio situacaoDoPatio)
    {
       List<Baliza> listaBalizas = obterBalizasEquivalentes(lugarDeEmpilhamentoOuRecuperacao.getListaDeBalizas(), situacaoDoPatio);
       
      /* for(Baliza baliza : listaBalizas){
           if(baliza.getProduto() == null||baliza.getProduto().getQuantidade()<baliza.getCapacidadeMaxima()){
               baliza.setUtilizada(new Boolean(false));
           }
           else{
               baliza.setUtilizada(new Boolean(true));
           }
       }*/
       return listaBalizas;
    }

    /**
     * Retorna lista de balizas equivalentes da situacao de patio passada por parametro.
     * @param listaDeBalizas Lista de balizas de uma situacao qualquer.
     * @param situacaoDoPatio Situacao de patio de onde vao sair as balizas equivalentes
     * @return Lista de balizas equivalentes.
     */
    public static List<Baliza> obterBalizasEquivalentes(List<Baliza> listaDeBalizas, SituacaoPatio situacaoDoPatio)
    {
       List<Baliza> balizasEquivalentes = new ArrayList<Baliza>();
      /* for(Baliza baliza : listaDeBalizas){
           Baliza balizaEquivalente = ControladorMaquinas.buscaBalizaEquivalente(baliza, situacaoDoPatio);
           balizasEquivalentes.add(balizaEquivalente);
       }*/
       return balizasEquivalentes;
    }


}
