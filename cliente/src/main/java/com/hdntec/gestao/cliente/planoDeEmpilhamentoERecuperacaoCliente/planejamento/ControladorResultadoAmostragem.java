package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento;

import com.hdntec.gestao.domain.plano.entity.Atividade;


/**
 * Classe que implementa o controle de edição da Amostra do Produto embarcado da Carga via importacao de dados de uma planilha
 * @author Bruno Gomes
 */
public class ControladorResultadoAmostragem {

    Atividade atividade;

    public ControladorResultadoAmostragem(Atividade atividade){
        this.atividade = atividade;
    }

/*    @Override
    public SituacaoPatio executar(SituacaoPatio situacaoDePatio, Date inicioExecucao, Date fimExecucao) {
        //edita carga que teve uma amostra importada
        for(Pier pier : situacaoDePatio.getPlanta().getListaPiers())
        {
            for(Berco berco : pier.getListaDeBercosDeAtracacao())
            {//encontrando o navio onde se encontra a Carga que teve a amostra importada
                if(berco.getNavioAtracado() != null && berco.getNavioAtracado().getNomeNavio().equalsIgnoreCase(atividade.getCarga().getNavio().getNomeNavio()))
                {//percorre a lista de Carga para encontrar a carga editada
                    for(Carga cargaEditada : berco.getNavioAtracado().getListaDeCargasDoNavio())
                    {
                        if(cargaEditada.getIdentificadorCarga().equalsIgnoreCase(atividade.getCarga().getIdentificadorCarga()))
                        {
                            //cria um clone da amostra e seta na carga editada
                            Amostra amostraComIdFalso = clonaAmostraComIdFalso(atividade.getListaResultadoAmostras().get(0));
                            List<Amostra> listaAmostras = new ArrayList<Amostra>();
                            listaAmostras.add(amostraComIdFalso);
                            cargaEditada.getProduto().getQualidade().setListaDeAmostras(listaAmostras);
                            
                            //atualiza o valor do embarcado e desvio padrão dos ItensDeControle da carga
                            atualizaItemDeControleCarga(amostraComIdFalso.getListaDeItensDeControle(), cargaEditada);
                            //seta o caminho da planilha de importacao
                            cargaEditada.setCaminhoCompletoPlanilha(atividade.getCarga().getCaminhoCompletoPlanilha());
                        }
                    }                    
                }
            }
        }
        return situacaoDePatio;
    }

    *//**
     * atualiza os valores dos itensDeControle da Qualidade da Carga com os itensDeControle da Amostra (real embarcado)
     * @param listaItensDaAmostra
     * @param cargaEditada
     *//*
    public void atualizaItemDeControleCarga(List<ItemDeControle> listaItensDaAmostra, Carga cargaEditada)
    {//lista os itensDeControle do produto da carga
        for(ItemDeControle itemDaCarga : cargaEditada.getProduto().getQualidade().getListaDeItensDeControle())
        {//lista os itensDeControle da Amostra
            for(ItemDeControle itemDaAmostra : listaItensDaAmostra)
            {//verifica se os itens são compativeis
                if(itemDaCarga.getTipoItemControle().getDescricaoTipoItemControle().equalsIgnoreCase(itemDaAmostra.getTipoItemControle().getDescricaoTipoItemControle()))
                {//... em caso positivo atualiza o valor (embarcado e desvioPadrao) do item da Carga com o respectivo valor do item da Amostra
                    itemDaCarga.setEmbarcado(itemDaAmostra.getValor());
//                    itemDaCarga.setDesvioPadraoEmbarcado(itemDaAmostra.getDesvioPadraoValor());
                }
            }
        }
    }

    *//**
     * Clona a amostra da Atividade atribuindo um Id falso para o clone
     * @param amostra
     * @return
     *//*
    private Amostra clonaAmostraComIdFalso(Amostra amostra)
    {
        Amostra amostraClone = new Amostra();
        amostraClone.setNomeAmostra(amostra.getNomeAmostra());
//        amostraClone.setQuantidade(clone.cloneDouble(amostra.getQuantidade()));
        amostraClone.setListaDeItensDeControle(clonarItensDeControle(amostra.getListaDeItensDeControle()));

        return amostraClone;
    }

    *//**
     * Clona a lista de itensDeControle (da Amostra) colocando id falso para cada item de clontrole clonado.
     * @param listaDeItensDeControle
     * @return
     *//*
    private List<ItemDeControle> clonarItensDeControle(List<ItemDeControle> listaDeItensDeControle) {
        List<ItemDeControle> listaDeItensDeControleClonados = new ArrayList<ItemDeControle>();
        for(ItemDeControle itemDeControle : listaDeItensDeControle) {
        	ItemDeControle itemDeControleClonado = new ItemDeControleAmostra();
            itemDeControleClonado.setTipoItemControle(itemDeControle.getTipoItemControle());
            //valor e desvio padrao do valor
            //itemDeControleClonado.setValor(clone.cloneDouble(itemDeControle.getValor()));
//            itemDeControleClonado.setDesvioPadraoValor(clone.cloneDouble(itemDeControle.getDesvioPadraoValor()));

            listaDeItensDeControleClonados.add(itemDeControleClonado);
        }

        return listaDeItensDeControleClonados;
    }
*/
}//end of class
