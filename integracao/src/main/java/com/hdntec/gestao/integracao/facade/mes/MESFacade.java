package com.hdntec.gestao.integracao.facade.mes;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


public interface MESFacade {
	public Double calcular(Date inicio, Date fim,Campanha campanha,Map<String,List<IntegracaoMES>> map, TipoProduto tipoProduto);
	
}
