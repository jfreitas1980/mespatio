package com.hdntec.gestao.services.controladores.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;
import com.hdntec.gestao.services.controladores.IControladorExecutarPerdaPellet;

public class ControladorExecutarPerdaPellet implements IControladorExecutarPerdaPellet
{
	private static ControladorExecutarPerdaPellet instance = null;

	/**
	 * Construtor privado.
	 */
	public ControladorExecutarPerdaPellet() {
	}

	/**
	 * Retorna a instancia singleton da fAbrica.
	 * 
	 * @return TransLogicDAOFactory
	 */
	public static ControladorExecutarPerdaPellet getInstance() {
		if (instance == null) {
			instance = new ControladorExecutarPerdaPellet();
		}
		return instance;
	}

    
	
    @Override
	public void empilharPerdaPellet(Atividade atividade,Date horaEmpilhamento, List<AtividadeCampanha> atividadesCampanhas, 
			HashMap<AtividadeCampanha, Double> mapaPerdaPelletFeedPorCampanha) 
    {
    	LugarEmpilhamentoRecuperacao lugarEmpRecAtual = null;       	    
    	if (!mapaPerdaPelletFeedPorCampanha.isEmpty()) {
    		lugarEmpRecAtual = new LugarEmpilhamentoRecuperacao();     
    		lugarEmpRecAtual.setOrdem(1);
	        lugarEmpRecAtual.setDtInsert(new Date(System.currentTimeMillis()));
	        lugarEmpRecAtual.setIdUser(1L);
	        lugarEmpRecAtual.setDtInicio(horaEmpilhamento);
	        lugarEmpRecAtual.setQuantidade(0D);
	        lugarEmpRecAtual.setTipoProduto(atividadesCampanhas.get(0).getCampanha().getTipoScreening());
	        // seta os objetos no lugar de empilhamento                    
	        lugarEmpRecAtual.setNomeDoLugarEmpRec("PELLET_SCREENING");
	        
	        atividade.addLugarEmpilhamento(lugarEmpRecAtual);
	        atividade.setUpdated(Boolean.TRUE);
    	}  
        // mapa guardando a quantidade que deve ser empilhada por baliza em cada patio
      HashMap<Baliza, Double> mapaQuantidadeEmpilharBaliza = new HashMap<Baliza,Double>();
      
      // mapa auxiliar para armazenar as rastreabilidades
      HashMap<MetaBaliza, HashMap<AtividadeCampanha, Double>> mapaRastreabilidades = new HashMap<MetaBaliza, HashMap<AtividadeCampanha,Double>>();
      
    	for (AtividadeCampanha ac : atividadesCampanhas)
    	{
         double quantidadeTotalEmpilhar = 0;
         List<MetaBaliza> listaBalizas = new ArrayList<MetaBaliza>();
    		
         if (mapaPerdaPelletFeedPorCampanha.get(ac) != null) 
         {
            quantidadeTotalEmpilhar += mapaPerdaPelletFeedPorCampanha.get(ac).doubleValue();
       		
       		// verifica qual eh o patio onde eh empilhada a perda de pellet para as usinas 
       		MetaPatio mp = ac.getCampanha().getMetaUsina().getMetaPatioExpurgoPellet();
      	   // monta a lista de balizas onde sera empilhada a perda de pellet
      		for (MetaBaliza mb : mp.getListaDeMetaBalizas())
      		{
      		   Baliza baliza = mb.retornaStatusHorario(horaEmpilhamento);
      			
      		   if (baliza.getTipoBaliza().equals(EnumTipoBaliza.PELLET_SCREENING))
      			{
      			   // a baliza do tipo pellet so esta disponivel se nao tem produto ou se o produto eh pellet feed
      		      if (baliza.getProduto() == null || baliza.getProduto().getTipoProduto().equals(ac.getCampanha().getTipoScreening()))
      		      {
      			     listaBalizas.add(mb);
      			   }
      			}
      		}
      
          	int numeroBalizas = listaBalizas.size();
          	
          	// verifica se o espa�o que ainda existe para empilhar � suficiente para receber todo o material
          	double espacoRestante = 0;
          	HashMap<MetaBaliza, Double> mapaEspacoRestanteNaBaliza = new HashMap<MetaBaliza, Double>();
          	for (MetaBaliza metaBaliza : listaBalizas)
          	{
          		Baliza baliza = metaBaliza.retornaStatusHorario(horaEmpilhamento);
          		double espacoRestanteBaliza;
          		Double quantidadeJaReservadaParaEmpilharNaBaliza = mapaQuantidadeEmpilharBaliza.get(baliza);
          		if (quantidadeJaReservadaParaEmpilharNaBaliza == null)
          		   quantidadeJaReservadaParaEmpilharNaBaliza = new Double(0);
          		if (baliza.getProduto() != null)
          		  espacoRestanteBaliza = Math.max(0d, baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade() - quantidadeJaReservadaParaEmpilharNaBaliza);
          		else
          		  espacoRestanteBaliza = baliza.getCapacidadeMaxima() - quantidadeJaReservadaParaEmpilharNaBaliza;
          		espacoRestante += espacoRestanteBaliza;
          		mapaEspacoRestanteNaBaliza.put(metaBaliza, new Double(espacoRestanteBaliza));
          	}
          	
          	// se a capacidade restante for menor que a necessaria entao distribui o material igualmente por todas as balizas
          	double quantidadeAdicionalNaBaliza = 0;
          	if (espacoRestante < 0)
          	   espacoRestante = 0;
          	if (espacoRestante < quantidadeTotalEmpilhar)
          		quantidadeAdicionalNaBaliza = (quantidadeTotalEmpilhar - espacoRestante) / numeroBalizas;
          	
          	// distribui o material empilhado nas balizas disponiveis
          	double quantidadeRestante = quantidadeTotalEmpilhar;    	
          	
          	int sizeMetaBalizas = listaBalizas.size();
          	for (int i = 0; i < sizeMetaBalizas && quantidadeRestante > 0; i++)
          	{
          	   MetaBaliza metaBaliza = listaBalizas.get(i);
          	   Baliza balizaAtual = metaBaliza.retornaStatusHorario(horaEmpilhamento);
          	   
          		double quantidadeEmpilharBaliza = Math.min(mapaEspacoRestanteNaBaliza.get(metaBaliza), quantidadeRestante) + quantidadeAdicionalNaBaliza;
          		HashMap<AtividadeCampanha, Double> mapaQuantidadeEmpilhadaCampanha = mapaRastreabilidades.get(metaBaliza);
          		if (mapaQuantidadeEmpilhadaCampanha == null)
          		   mapaQuantidadeEmpilhadaCampanha = new HashMap<AtividadeCampanha, Double>();
          		mapaQuantidadeEmpilhadaCampanha.put(ac, quantidadeEmpilharBaliza);
          		mapaRastreabilidades.put(metaBaliza, mapaQuantidadeEmpilhadaCampanha);
          		
          		Double quantidadeQueDeveSerEmpilhada = mapaQuantidadeEmpilharBaliza.get(balizaAtual);
          		if (quantidadeQueDeveSerEmpilhada == null)
          		   quantidadeQueDeveSerEmpilhada = new Double(0);
          		quantidadeQueDeveSerEmpilhada += quantidadeEmpilharBaliza;
          		mapaQuantidadeEmpilharBaliza.put(balizaAtual, quantidadeQueDeveSerEmpilhada);
          		quantidadeRestante -= quantidadeEmpilharBaliza;
          	}
       	}
    	}
    	
    	HashMap<MetaPatio, ArrayList<Baliza>> mapaBalizasPorPatio = new HashMap<MetaPatio, ArrayList<Baliza>>();

 	   Set<Baliza> balizasUtilizadas = mapaQuantidadeEmpilharBaliza.keySet();
 	   for (Baliza baliza : balizasUtilizadas)
 	   {     
 	      // recupera a quantidade total que deve ser empilhada na baliza
 	      Double quantidadeEmpilharBaliza = mapaQuantidadeEmpilharBaliza.get(baliza);
 	      
    		// cria um novo status da baliza para setar o produto
    		Baliza novoStatusBaliza = baliza.getMetaBaliza().clonarStatus(horaEmpilhamento);
    		lugarEmpRecAtual.addBaliza(novoStatusBaliza);
    		if (baliza.getProduto() == null) 
         {
             Produto produto = new Produto();
             produto.setDtInicio(horaEmpilhamento);
             produto.addRastreabilidade(new ArrayList<Rastreabilidade>());
             Qualidade qualidade = new Qualidade();
             qualidade.setEhReal(false);
             qualidade.setListaDeAmostras(new ArrayList<Amostra>());
             qualidade.setDtInicio(horaEmpilhamento);
             List<ItemDeControle> listaItensDeControle = atividadesCampanhas.get(0).getCampanha().getQualidadeEstimada().getListaDeItensDeControle();
             qualidade.setListaDeItensDeControle(Blendagem.criaListaDeItensDeControleComValoresVazios(listaItensDeControle));
             for (ItemDeControle ic : qualidade.getListaDeItensDeControle())
                ic.setDtInicio(horaEmpilhamento);
             produto.setQualidade(qualidade);             
             qualidade.setProduto(produto);
            
             produto.setQuantidade(0.0);
             produto.setTipoProduto(atividadesCampanhas.get(0).getCampanha().getTipoScreening());
             novoStatusBaliza.setProduto(produto);
         }
    		
    		// atualiza a quantidade na baliza
         novoStatusBaliza.getProduto().setQuantidade(novoStatusBaliza.getProduto().getQuantidade() + quantidadeEmpilharBaliza);
         
         // acrescenta os registros de rastreabilidade no produto
         HashMap<AtividadeCampanha, Double> mapaQuantidadeEmpilhadaCampanha = mapaRastreabilidades.get(novoStatusBaliza.getMetaBaliza());
         Set<AtividadeCampanha> atividadesCampanhaRastreabilidade = mapaQuantidadeEmpilhadaCampanha.keySet();
         long chave = 1l;
         for (AtividadeCampanha ac1 : atividadesCampanhaRastreabilidade)
         {            
            double quantidadeRastreabilidade = mapaQuantidadeEmpilhadaCampanha.get(ac1);
            acrescentaRastreabilidade(horaEmpilhamento, horaEmpilhamento, ac1.getCampanha(), quantidadeRastreabilidade, novoStatusBaliza, novoStatusBaliza.getProduto(),chave);
            chave++;
         }
         
         // insere a baliza na lista de balizas utilizadas para perda de pellet no patio
         ArrayList<Baliza> listaBalizasNoPatio = mapaBalizasPorPatio.get(novoStatusBaliza.getMetaBaliza().getMetaPatio());
         if (listaBalizasNoPatio == null)
            listaBalizasNoPatio = new ArrayList<Baliza>();
         listaBalizasNoPatio.add(novoStatusBaliza);
         mapaBalizasPorPatio.put(novoStatusBaliza.getMetaBaliza().getMetaPatio(), listaBalizasNoPatio);
 	   }
    	
    	// cria as novas pilhas resultantes
      Set<MetaPatio> listaPatios = mapaBalizasPorPatio.keySet();
      for (MetaPatio mp : listaPatios)
      {
    	  lugarEmpRecAtual.addPilhaEditada(Pilha.criaPilhasDescontinuas(mapaBalizasPorPatio.get(mp), null, horaEmpilhamento, "pel",null,null));
      }
     
      
      

      
      
  	}
    
    /* Acrescenta os registros de rastreabilidade no produto */
    private void acrescentaRastreabilidade(Date horaInicio, Date horaTermino, Campanha campanha, double quantidadeEmpilhada, Baliza balizaAnterior, Produto produto,long chave)
    {
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
}