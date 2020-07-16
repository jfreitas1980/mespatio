/**
 * 
 */
package com.hdntec.gestao.integracao.facade.mes.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.integracao.facade.mes.MESFacade;


/**
 * @author jesse
 *
 */
public class MESFacadeImpl implements MESFacade {

	/* (non-Javadoc)
	 * @see com.hdntec.gestao.integracao.facade.mes.MESFacade#calcular(java.util.Date, java.util.Date, com.hdntec.gestao.domain.planta.Campanha)
	 */
	
	
	@Override
	public Double calcular(Date inicio, Date fim, Campanha campanha,Map<String,List<IntegracaoMES>> map,TipoProduto tipoProduto) {
		//valida se mudou a chave
		List<IntegracaoMES> itensValidos = makeListItensValidos(inicio, fim, campanha, map,tipoProduto);		
		if (itensValidos == null)
			return null;
		
		Double result = new Double(0);
		// verifica se tem apenas um item 
		if (itensValidos.size() == 1) {
			IntegracaoMES item = itensValidos.get(0);			
	        result = new Double(((double)(fim.getTime() - inicio.getTime()) / (double)calculateTimeDiff(item)) * item.getValorLeitura());
		} else {
		   int i = 0;
			for (IntegracaoMES item : itensValidos) {		   	  
				// primeiro item
				if (i == 0) {
					Double currValue = ( (double) (item.getDataLeitura().getTime() - inicio.getTime()) / (double) calculateTimeDiff(item)) * item.getValorLeitura(); 
					result += currValue;
		   	    } else if (i == itensValidos.size() - 1) {
		   	    	Double currValue = ((double) (fim.getTime() - item.getDataLeituraInicio().getTime()) / (double) calculateTimeDiff(item)) * item.getValorLeitura();
		   	    	result += currValue; 	
		   	    } else {
	   	    	Double currValue = item.getValorLeitura();
		   	    	result += currValue;	
		   	    }
			  i++;	
		   	}
		}
	  return result;	
	}
	/**
	 * @param item
	 * @return
	 */
	private Long calculateTimeDiff(IntegracaoMES item) {
		Long time = item.getDataLeitura().getTime() - item.getDataLeituraInicio().getTime();
		return time;
	}
	/**
	 * @param inicio
	 * @param fim
	 * @param campanha
	 * @param map
	 */
	public static List<IntegracaoMES> makeListItensValidos(Date inicio, Date fim, Campanha campanha,
			Map<String, List<IntegracaoMES>> map,TipoProduto tipoProduto) {
		boolean achouFim = false;
		boolean achouPrimeiro = false;
		List<IntegracaoMES> itensValidos = new ArrayList<IntegracaoMES>();
		// monta a chave para o MAP
		StringBuilder currKey = makeCurrentKey(campanha,tipoProduto);              		 
   		if (currKey != null) {
   			List<IntegracaoMES> itens = map.get(currKey.toString());
   			if (itens != null) {
	   			// recupera os itens q estão dentro do range
			   	for (IntegracaoMES item : itens) 
			   	{
			   		if (item.getDataLeitura() != null && item.getDataLeituraInicio() != null)
			   		{
			   			if (fim.getTime() >= item.getDataLeituraInicio().getTime() &&   item.getDataLeitura().getTime() >= fim.getTime()) {  
			   			    itensValidos.add(item);
			   				achouFim = true;
				       		break;	  
				       	 }			   			
			   			
			   			if (!achouPrimeiro && inicio.getTime() >= item.getDataLeituraInicio().getTime() &&  inicio.getTime() <= item.getDataLeitura().getTime()) {
			   			   itensValidos.add(item);
			   			   achouPrimeiro = true;
			   			} else if (achouPrimeiro) {
			  			   itensValidos.add(item);  
			  		    }			   			
			       	}				       	  
			   }
			}   			
   		}	
   	if (!achouFim)
   		itensValidos = null;
   	
   	return itensValidos;
	} 
	
	/**
	 * @param campanha
	 * @return
	 */
	private static StringBuilder makeCurrentKey(Campanha campanha,TipoProduto tipoProduto) {
		StringBuilder currKey = new StringBuilder(); 
		   if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_FEED))
		      {  
               currKey.append(campanha.getCodigoFaseProcessoPelletFeed().toString().trim());
               currKey.append(campanha.getCdTipoItemControlePelletFeed().toString().trim());
               currKey.append(campanha.getTipoProcessoPelletFeed().toString().trim());
               currKey.append(campanha.getAreaRespEDPelletFeed().toString().trim());
            } else if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA)){
               currKey.append(campanha.getCodigoFaseProcessoPelota().toString().trim());            
               currKey.append(campanha.getCdTipoItemControlePelota().toString().trim());
               currKey.append(campanha.getTipoProcessoPelota().toString().trim());
               currKey.append(campanha.getAreaRespEDPelota().toString().trim());
            } else {
              return null;
            }              		
		return currKey;
	}





}
