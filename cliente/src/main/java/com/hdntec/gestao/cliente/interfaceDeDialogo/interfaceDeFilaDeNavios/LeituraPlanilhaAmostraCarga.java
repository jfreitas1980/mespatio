package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.planilha.CflexStockyardLeitorPlanilha;
import com.hdntec.gestao.cliente.util.planilha.PlanilhaUtil;
import com.hdntec.gestao.cliente.util.texto.DSSStockyardFuncoesTexto;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.LeituraPlanilhaException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 *
 * @author Bruno Gomes
 */
public class LeituraPlanilhaAmostraCarga {

    private static final int NUMERO_ITENS_DE_CONTROLE = 15;
    private InterfaceCarga interfaceCarga;
    private CflexStockyardLeitorPlanilha cflexStockyardLeitorPlanilha;

    /**
     * contrutor de classe
     * @param interfaceCarga - interfaceCarga que se deseja importar a amostra de produto
     * @param cflex - instancia da classe CflexStockyardLeitorPlanilha
     */
    public LeituraPlanilhaAmostraCarga(InterfaceCarga interfaceCarga, CflexStockyardLeitorPlanilha cflex) {
        this.interfaceCarga = interfaceCarga;
        this.cflexStockyardLeitorPlanilha = cflex;
    }

    /**
     * cria uma lista de itensDeControle a partir da leitura da planilha de importacao de qualidade de embarque
     * @param sheet - aba onde se encontram os valores dos itensDeControle que devem ser lidos
     * @return
     */
    public void efetuaLeituraItensEmbarcado(int sheet)
    {
        try {
            Double embarcado;
            Integer linhaValorEmbarcado = 106; // a linha da planilha onde começam os valores (embarcado) de cada ItemDeControle
            
            Integer index = 0; //usado para controlar o laço de repetição sobre o numero de ItensDeControle
            Integer colunaValorItens = 2;
            Integer linhaNomeItens = 3;

            String espaco = " ";
            /**nome do tipo de ItemDeControle lido a partir da planilha de importacao */
            String nomeTipoItemPlanilha;
            /** nome do tipo de ItemDeControle gravado na base de dados*/
            String nomeItemGravadoBase;

            for (ItemDeControleQualidade ic : interfaceCarga.getCargaVisualizada().getProduto().getQualidade().getListaDeItensDeControleQualidade())
            {
            	TipoItemDeControle tipoItem = ic.getTipoItemControle();
            
                colunaValorItens = 2; //atualiza o indice da linha, para procurar o proximo item
                index = 0; //atualiza o somador, para procurar o proximo item

                //remove os espaços em branco
                nomeItemGravadoBase = DSSStockyardFuncoesTexto.removeCaracterEspecificado(tipoItem.getDescricaoTipoItemControle() , espaco);

                while (index < NUMERO_ITENS_DE_CONTROLE)
                {//primeiro while para varer os itens da planilha com valores (propriedades) fisicas
                    nomeTipoItemPlanilha = DSSStockyardFuncoesTexto.removeCaracterEspecificado( cflexStockyardLeitorPlanilha.leCelula(linhaNomeItens, colunaValorItens, sheet), espaco ).replace(',','.');
                    if (nomeItemGravadoBase.equalsIgnoreCase(nomeTipoItemPlanilha))
                    {
                        //valor da amostra embarcada
                        embarcado = DSSStockyardFuncoesNumeros.getStringToDouble( cflexStockyardLeitorPlanilha.leCelula(linhaValorEmbarcado, colunaValorItens, sheet) );
                        
/*                        //cria tipo item controle
                        tipoItemDeControle = new TipoItemDeControle();
                        tipoItemDeControle = tipoItem;
                        //cria item controle
                        *//**JESSÉ 18/6*//*
                        item = new ItemDeControleOrientacaoEmbarque();
                        //item = new ItemDeControle();
                        item.setTipoItemControle(tipoItemDeControle); //adiciona o TipoItemDeControle ao itemDeControle
                        item.setValor(embarcado);*/
                        
                        // seta o item de controle embarcado
                        ic.setEmbarcado(embarcado);
                        break;
                    }
                    //somando 3 (tres), pois a leitura não esta considerando celulas mescladas como sendo apenas 1(uma), então deve-se pular ate o proximo item
                    colunaValorItens+=3;
                    index++;
                }

                //segundo while para varer os itens da planilha com valores (propriedades) quimicas
               int linhaValorEmbarcado2 = 142; //linha onde estão descritos os valores (embarcado) dos tipo de itemDeControle com propriedades quimicas na planilha
               int linhaNomeItem2 = 115; // linha onde encontra-se o nome dos itensDeControle de propriedades quimicas
                colunaValorItens = 2; //atualiza o indice da linha, para procurar o proximo item
                index = 0;//atualiza o valor do contador para o novo laço de repaticao
                while (index < NUMERO_ITENS_DE_CONTROLE)
                {
                    nomeTipoItemPlanilha = DSSStockyardFuncoesTexto.removeCaracterEspecificado( cflexStockyardLeitorPlanilha.leCelula(linhaNomeItem2, colunaValorItens, sheet), espaco ).replace(',','.');
                    if (nomeItemGravadoBase.equalsIgnoreCase(nomeTipoItemPlanilha)
                            || (nomeItemGravadoBase.toUpperCase().startsWith("ABRAS") && nomeTipoItemPlanilha.toUpperCase().startsWith("ABRAS"))
                            || (nomeItemGravadoBase.toUpperCase().startsWith("TAMB") && nomeTipoItemPlanilha.toUpperCase().startsWith("TAMB")))
                    {
                        //valor da amostra embarcada
                        embarcado = DSSStockyardFuncoesNumeros.getStringToDouble( cflexStockyardLeitorPlanilha.leCelula(linhaValorEmbarcado2, colunaValorItens, sheet) );
                        
/*                        //cria tipo item controle
                        tipoItemDeControle = new TipoItemDeControle();
                        tipoItemDeControle = tipoItem;
                        //cria item controle
                        *//**JESSÉ 18/6*//*
                        item = new ItemDeControleOrientacaoEmbarque();
                        //item = new ItemDeControle();
                        item.setTipoItemControle(tipoItemDeControle); //adiciona o TipoItemDeControle ao itemDeControle
                        item.setValor(embarcado);*/
                     
                        // seta o item de controle embarcado
                        ic.setEmbarcado(embarcado);
                        break;
                    }
                    //somando 3 (tres), pois a leitura não esta considerando celulas mescladas como sendo apenas 1(uma), então deve-se pular ate o proximo item
                    colunaValorItens+=3;
                    index++;
                }
           }
        }catch (LeituraPlanilhaException ex) {
            ativaMensagem(ex.getMessage());
        }catch (ErroSistemicoException ex) {
            ativaMensagem(ex.getMessage());
        }catch (Exception ex) {
            ativaMensagem(PropertiesUtil.buscarPropriedade("exception.erro.ao.ler.planilha"));
        }
    }

    /**
     * Le a quantidade total de amostragem da carga
     * @param sheet
     * @return
     */
    public Double getQuantidadeAmostra(int sheet){
        Double quantidadeTotalAmostra = 0.d;
        try {
            // linha da planilha de importacao da amostra onde se encontra a quantidade total da amostra da carga
            int linha = 0;
            //apos ler da planilha remove o caracter de ponto, caso exista, e apos converte virgula para ponto, pois variavel Double separara casas decimais por ponto "."
            quantidadeTotalAmostra = DSSStockyardFuncoesNumeros.getStringToDouble( DSSStockyardFuncoesTexto.removeCaracteresEspeciais( cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_R, sheet)) );
        } catch (LeituraPlanilhaException ex) {
            ativaMensagem(ex.getMessage());
        } catch (Exception ex) {
            ativaMensagem(PropertiesUtil.buscarPropriedade("exception.erro.ao.ler.planilha"));
        }

        return quantidadeTotalAmostra;
    }

    /**
     * Cria um nome para a amostra a partir da data que esta cadastrada na planilha
     * @param sheet
     * @return
     */
    public String criaNomeDaAmostra(int sheet){
         String nome = "Amostra ";
        try {
            int linha = 0; //linha onde se encontra a data cadastrada da amostra
            //o campo data para formar o nome da amostra se encontra na coluna AA da planilha, por isso estou somando o valor da colunaZ + colunaB
            nome = nome.concat(cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_Z + PlanilhaUtil.COLUNA_B, sheet));
        } catch (LeituraPlanilhaException ex) {
            ativaMensagem(ex.getMessage());
        } catch (Exception ex) {
            ativaMensagem(PropertiesUtil.buscarPropriedade("exception.erro.ao.ler.planilha"));
        }
         return nome;
    }

    /**
     * retorna uma String do campo de data que foi lido da planilha
     * @param sheet
     * @return
     */
    public String retornaDataDaAmostra(int sheet){
        String data = "";
        try {
            int linha = 0; //linha onde se encontra a data cadastrada da amostra
            //o campo data se encontra na coluna AA da planilha, por isso estou somando o valor da colunaZ + colunaB
            data = cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_Z + PlanilhaUtil.COLUNA_B, sheet);
        } catch (LeituraPlanilhaException ex) {
            ativaMensagem(ex.getMessage());
        } catch (Exception ex) {
            ativaMensagem(PropertiesUtil.buscarPropriedade("exception.erro.ao.ler.planilha"));
        }
        return data;
    }

    public void ativaMensagem(String aviso){
    	interfaceCarga.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
      InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
      interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
      interfaceMensagem.setDescricaoMensagem(aviso);
      interfaceCarga.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
    }

}
