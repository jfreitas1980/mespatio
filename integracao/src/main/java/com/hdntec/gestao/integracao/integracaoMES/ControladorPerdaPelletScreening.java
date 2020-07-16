package com.hdntec.gestao.integracao.integracaoMES;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;


public class ControladorPerdaPelletScreening {

    private SituacaoPatio situacaoPatio;
    private List<Usina> usinasEmpilhamento;
    private Double quantidadePerda;
    private List<Baliza> listaBalizasPerdas;

    public ControladorPerdaPelletScreening(SituacaoPatio situacaoPatio,
            List<Usina> usinasEmpilhamento, Double quantidadePerda) {
        this.situacaoPatio = situacaoPatio;
        this.usinasEmpilhamento = retornaUsinaEquivalente(usinasEmpilhamento);
        this.quantidadePerda = quantidadePerda;
        this.listaBalizasPerdas = null;        
    }

    public SituacaoPatio getSituacaoPatioAtualizado() {
        Double qtdMaterialPerdidoComoPerda;
        for (Usina usina : usinasEmpilhamento) {
        	if (usina.getTaxaGeracaoPellet() > 0) {
	        		/*if (usina.getAtividade() != null) {
	        			qtdMaterialPerdidoComoPerda = this.retornarValorProduzidoPelaUsina(usina) * usina.getTaxaGeracaoPellet();
	                                distribuiMaterialPerdaEntreBalizasDePelletScrrening(qtdMaterialPerdidoComoPerda, usina);
	        		} else {
	        			qtdMaterialPerdidoComoPerda = quantidade * usina.getTaxaGeracaoPellet();
	                                distribuiMaterialPerdaEntreBalizasDePelletScrrening(qtdMaterialPerdidoComoPerda, usina);
	        		}  */              
            } else {
                qtdMaterialPerdidoComoPerda = 0.0;
                distribuiMaterialPerdaEntreBalizasDePelletScrrening(qtdMaterialPerdidoComoPerda, usina);
            }
		}
        
        
        return situacaoPatio;
    }
    
    public void atualizarPerdaUsina() {
        /*Double qtdMaterialPerdidoComoPerda;
        for (Usina usina : usinasEmpilhamento) {
        	if (usina.getTaxaGeracaoPellet() > 0) {
        			qtdMaterialPerdidoComoPerda = quantidade * usina.getTaxaGeracaoPellet();
                    distribuiMaterialPerdaEntreBalizasDePelletScrrening(qtdMaterialPerdidoComoPerda, usina);        		
            } else {
                qtdMaterialPerdidoComoPerda = 0.0;
                distribuiMaterialPerdaEntreBalizasDePelletScrrening(qtdMaterialPerdidoComoPerda, usina);
            }
		}*/
    }    
    
    
    private Double retornarValorProduzidoPelaUsina(Usina usina) {
        double qtdeProduzida = 0d;
        /* TODO REFAZER METODO 
        
        for (AtividadeCampanha atividadeCampanha : usina.getAtividade().getListaDeAtividadesCampanha()) {
           * 
           * if (atividadeCampanha.getCampanha().getUsina().getNomeUsina().equals(usina.getNomeUsina())) {
                LugarEmpilhamentoRecuperacao lugarEmpRec = null;
                if (usina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao() != null && !usina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().isEmpty()) {
                    lugarEmpRec = usina.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
                    qtdeProduzida = lugarEmpRec.getQuantidade();
                    break;
                }
            }*/
        //}
        return qtdeProduzida;
    }

    public List<ItemDeControle> buscaListaDeItensDeControle() {
    	/*
    	 * 	TODO REFAZER METODO
    	 * for (Patio patio : situacaoPatio.getPlanta().getListaPatios()) {
            for (Baliza baliza : patio.getListaDeBalizas()) {
                if (baliza.getProduto() != null) {
                    return baliza.getProduto().getQualidade().getListaDeItensDeControle();
                }
            }
        }*/
    	return null;
    }
    
    private void distribuiMaterialPerdaEntreBalizasDePelletScrrening(Double qtdMaterial, Usina usina) {
        //prepara balizas para receber material e conta número de balizas do tipo.
    	this.listaBalizasPerdas  = new ArrayList<Baliza>();
        int qtdBalizas = 0;
        qtdBalizas = preparaBalizasParaMaterial(usina, listaBalizasPerdas);
        //enquanto tiver algum espaço livre e o material não foi todo distribuido
        while (qtdMaterial > 0.1 && qtdBalizas > 0) {
            Double porcaoIndividual = qtdMaterial / qtdBalizas;
            //porcaoIndividual = DSSStockyardFuncoesNumeros.arredondaValor(porcaoIndividual,2);
            BigDecimal bd = new BigDecimal(porcaoIndividual);
            bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
            porcaoIndividual = bd.doubleValue();

            //Divide a quantidade remanescente entre todas as balizas que tem material
            for (Baliza baliza : listaBalizasPerdas) {
                if (baliza.getProduto() == null) {
                    Produto produto = new Produto();
                    produto.addRastreabilidade(new ArrayList<Rastreabilidade>());
                    Qualidade qualidade = new Qualidade();
                    qualidade.setEhReal(false);
                    qualidade.setListaDeAmostras(new ArrayList<Amostra>());
                   /**
                    * TODO REFAZER
                    *  qualidade.setListaDeItensDeControle(Blendagem.criaListaDeItensDeControleComValoresVazios(this.buscaListaDeItensDeControle()));
                    */
                    produto.setQualidade(qualidade);
                    qualidade.setProduto(produto);
                    produto.setQuantidade(0.0);
                   // produto.setTipoProduto(usina.getCampanhaAtual().getTipoScreening());
                    baliza.setProduto(produto);                    
                }
                //adiciona o material considerando o limite das balizas
                Double volumeDisponivelNaPilha = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();
                if (porcaoIndividual > volumeDisponivelNaPilha) {
                    qtdMaterial -= volumeDisponivelNaPilha;
                    baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + volumeDisponivelNaPilha);
                } else {
                    // ajuste para arrendondamento estava caidno em loop infinito
                	if (qtdMaterial.doubleValue() < porcaoIndividual.doubleValue()) {                		
                		baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + qtdMaterial);
                		qtdMaterial = 0D;
                	} else {
                		qtdMaterial -= porcaoIndividual;
                	    bd = new BigDecimal(qtdMaterial);
                        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
                        qtdMaterial = bd.doubleValue();

                		baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + porcaoIndividual);
                	}
                	
                    //baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + porcaoIndividual);
                }
            }
            if (qtdMaterial > 0) {
                listaBalizasPerdas = new ArrayList<Baliza>();
                qtdBalizas = preparaBalizasParaMaterial(usina, listaBalizasPerdas);
            }
        }
    }

    private int preparaBalizasParaMaterial(Usina usina,
            List<Baliza> listaBalizas) {
        Pilha lastPilha = null;
        int qtdValida = 0;
/*        for (Baliza baliza : usina.getPatioExpurgoPellet().getListaDeBalizas()) {
            if (baliza.getTipoBaliza().equals(EnumTipoBaliza.PELLET_SCREENING)) {
                if (baliza.getProduto() == null || baliza.getProduto().getQuantidade() < baliza.getCapacidadeMaxima() && baliza.getProduto().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_SCREENING)) {
                    qtdValida++;
                    listaBalizas.add(baliza);
                }
                if (baliza.getPilha() != null) {
                    lastPilha = baliza.getPilha();
                } else {
                    if (lastPilha != null) {
                        baliza.setPilha(lastPilha);
                        lastPilha.getListaDeBalizas().add(baliza);
                    } else {
                        baliza.setPilha(getPilhaAdequadaParaBaliza(baliza));
                        lastPilha = baliza.getPilha();
                        lastPilha.getListaDeBalizas().add(baliza);
                    }
                }
            } else {
                lastPilha = null;
            }
        }*/
        return qtdValida;
    }

  

    private Boolean temNomeCompativel(String nome, Baliza baliza) {
      /*  Iterator<Baliza> it = baliza.getPatio().getListaDeBalizas().iterator();
        while (it.hasNext()) {
            Baliza baliza1 = it.next();
            if (baliza1.getPilha() != null) {
                if (baliza1.getPilha().getNomePilha().equals(nome)) {
                    return Boolean.FALSE;
                }
            }
        }*/
        return Boolean.TRUE;
    }

    private List<Usina> retornaUsinaEquivalente(List<Usina> usinasProcuradas) {
        List<Usina> usinasEncontradasEquivalentes = new ArrayList<Usina>();
       /* 
        * TODO REFAZER METODO
        * for (Usina usinaAtual : this.situacaoPatio.getPlanta().getListaUsinas()) {
        	for (Usina usina : usinasProcuradas) {
        		if (usinaAtual.getNomeUsina().equals(usina.getNomeUsina())) {
        			usinasEncontradasEquivalentes.add(usinaAtual);
                }
			}
            
        }*/
        return usinasEncontradasEquivalentes;
    }

	public List<Baliza> getListaBalizasPerdas() {
		return listaBalizasPerdas;
	}

	public void setListaBalizasPerdas(List<Baliza> listaBalizasPerdas) {
		this.listaBalizasPerdas = listaBalizasPerdas;
	}
}


