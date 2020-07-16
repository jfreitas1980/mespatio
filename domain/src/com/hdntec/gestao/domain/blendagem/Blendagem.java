package com.hdntec.gestao.domain.blendagem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.CargaSelecionadaException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.util.PropertiesUtil;


/**
 * Blendagem é a mistura de um {@link Produto} de uma ou mais balizas e/ou campanhas de usinas de quantidades diferentes para atingir uma qualidade determinada. A blendagem normalmente é realizada para elevar a qualidade de determinados grupos de baliza de baixa qualidade com grupos de baliza de qualidade superior, fazendo a média ponderada. Sobre esse conceito, deve-se fazer a aná¡lise de qualidade pelas caracterí­sticas dos itens de controle.
 * 
 * @author andre
 * 
 */
public class Blendagem 
{
    private static Blendagem instance = null;
    
    private Blendagem(){
        
    }

    
    public static Blendagem getInstance() {
        if (instance == null) {
            instance = new Blendagem();
        }
        return instance;
    }

    public static Blendagem getNewInstance()
    {
    	instance = new Blendagem();
    	return instance;
    }
   
    
    /** o produto resultante da blendagem selecionada atualmente*/
   private Produto produtoResultante;

   /** a lista de produtos selecionados atualmente para blendagem */
   private List<Produto> listaDeProdutosSelecionados;

   /** lista de campanhas das usinas para blendagem */
   private List<Campanha> listaDeCampanhas;

   /** a lista de balizas que fora usada para calculo da blendagem */
   private List<Baliza> listaBalizasBlendadas;

   /** a carga selecionada para blendagem */
   private Carga cargaSelecionada;

   /** referência ao controlador das operações do subsistema de cálculo de qualidade*/
   private ControladorCalculoQualidade controladorCalculoQualidade;
   
   /** mapa com as quantidades de cada tipo de produto presente na blendagem*/
   private HashMap<TipoProduto, Double> mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem;

   /**
    * Adiciona o produto passado como parâmetro para a lista de produtos selecionados para blendagem. Assim que o produto é adicionado à  lista, deve-se calcular a blendagem incorporando seus valores nas médias. Caso algum erro ocorra no cálculo, deve-se retirar o produto a lista.
    * 
    * @param produto
    *           o produto a ser inserido na lista de produtos blendados
    */
   public void inserirProdutoNaBlendagem(Produto produto, double quantidadeBlendada)
   {
      instanciaListaProdutosSelecionados();

      //Caso exista algum produto resultante de blendagem atualmente ...
      if (produtoResultante != null)
      {
         // ... adicionamos o produto na lista ...
         listaDeProdutosSelecionados.add(produto);

        //... atualizamos o mapa de quantidades de tipos de produto da blendagem ...
         atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposInsercaoDoProduto(produto, quantidadeBlendada);
         
         //... e calculamos a blendagem, dando origem a uma nova qualidade e quantidade ...
         produtoResultante = calculaProdutoResultanteDaInsercaoDeProdutoNaBlendagem(produtoResultante, produto, quantidadeBlendada);
         
      } else
      {
         //... caso ainda não exista nenhum produto resultante da blendagem, o produto resultante será o próprio produto selecionado ...     
         produtoResultante = produto.copiarStatus();
         listaDeProdutosSelecionados.add(produto);
         
         //... zeramos as amostras se a qualidade medida para o produto é real. Se não for, não podemos dizer nada sobre ela
         Qualidade qualidadeResultante = produtoResultante.getQualidade().copiarStatus();
         if(qualidadeResultante.getEhReal())
         {
            qualidadeResultante.setListaDeAmostras(new ArrayList<Amostra>());
         }else
         {
            qualidadeResultante.setListaDeAmostras(new ArrayList<Amostra>());
            
            // Guarda uma copia da qualidade para gerar a nova lista
            //Qualidade qualidadeCopia = qualidadeResultante.copiarStatus();
            
            List<ItemDeControleQualidade> itensControle = new ArrayList<ItemDeControleQualidade>();
            for (ItemDeControleQualidade item : qualidadeResultante.getListaDeItensDeControleQualidade()) {
                ItemDeControleQualidade novoItem =(ItemDeControleQualidade)item.copiarStatus();   
                novoItem.setIdItemDeControle(null);            
                novoItem.setQualidade(qualidadeResultante);             
                itensControle.add(novoItem);
            }
            
            // limpa lista de itens de controle
            //qualidadeCopia.getListaDeItensDeControleQualidade().clear();
            
            //qualidadeCopia.setListaDeItensDeControle(criaListaDeItensDeControleComValoresVazios(qualidadeCopia.getListaDeItensDeControle()));
            
            produtoResultante.setQualidade(qualidadeResultante);
         }
         
         //... e precisaremos montar o mapa de quantidades de tipos de produto da blendagem.
         montaMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem();
      }
   }

   /**
    * Cria uma lista de itens de controle com os mesmo tipos de itens de controle da lista passada com valores vazios
    * @param listaItensControle lista de itens de controle a ser copiada com valores vazios
    * @return
    */
   public static List<ItemDeControle> criaListaDeItensDeControleComValoresVazios(List<ItemDeControle> listaItensControle)
   {
      List<ItemDeControle> listaDeItensDeControleComValoresVazio = new ArrayList<ItemDeControle>();
      for (int i=0; i<listaItensControle.size();i++)
      {
         ItemDeControle itemDeControle = listaItensControle.get(i);
         listaDeItensDeControleComValoresVazio.add(criaItemDeControleComValorVazio(itemDeControle.getTipoItemControle(),i));    
      }
      return listaDeItensDeControleComValoresVazio;
   }
   
   /**
    * Cria um item de controle com o tipo de item de controle passado e valores vazios
    * @param tipoItemDeControle o tipo de item de controle do item de controle a ser criado
    * @return
    */
   private static ItemDeControle criaItemDeControleComValorVazio(TipoItemDeControle tipoItemDeControle, int incrementoParaDiferenciarOID)
   {
	   ItemDeControle itemControle = new ItemDeControleQualidade();
       itemControle.setTipoItemControle(tipoItemDeControle);
       itemControle.setInicio(System.currentTimeMillis());
       itemControle.setIdUser(1L);
       return itemControle;
   }

   /**
    * Calcula o produto resultante da blendagem de um produto adicionado a um produto base
    *    
    * @param produtoBase
    * @param produtoAdicionado
    * @return o produto resultante da blendagem
    */
   public Produto calculaProdutoResultanteDaInsercaoDeProdutoNaBlendagem(Produto produtoBase, Produto produtoAdicionado, double quantidadeBlendada)
   {
      Produto produtoResultante =  Produto.criaProduto(buscaOProdutoEmQuantidadePredominante(), System.currentTimeMillis());
      
      /*
       * Precisamos setar:
       * 1 - tipoProduto
       * 2 - quantidade
       * 3 - qualidade
       * 4 - lista de rastreabilidades
       */
      
      //1) Assumiremos o tipoDeProduto predominante, ou seja, o que estiver em maior quantidade
      //produtoResultante.setTipoProduto(buscaOProdutoEmQuantidadePredominante());

      //2) A quantidade é simplesmente a soma das quantidades dos produtos adicionados
      produtoResultante.setQuantidade(produtoBase.getQuantidade()+quantidadeBlendada);
      
      //3) A qualidade será calculada através de média ponderada das qualidades dos produtos
      produtoResultante.setQualidade(calculaQualidadeMediaPonderadaPelaQuantidadeNaInsercaoDeProdutoNaBlendagem(produtoBase.getQualidade(),produtoBase.getQuantidade(),produtoAdicionado.getQualidade(),quantidadeBlendada));
      produtoResultante.getQualidade().setProduto(produtoResultante);
      return produtoResultante;
   }

   /**
    * Metodo que verifica se dois tipos de Produto podem ser blendados
    * @param produtoOriginal - produto ja existente na selecao de blendagem
    * @param produtoAdicionado - produto que se deseja adicionar a blendagem, e que sera verificada a compatibilidade
    * @return - true caso possam ser blendados <br> do contrario sobe exceção de ProdutoIncompativel
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ProdutoIncompativelException
    */
   public void verificaPossibilidadeDeBlendagem(Produto produtoOriginal, Produto produtoAdicionado) throws ProdutoIncompativelException
   {
       boolean resultado = Boolean.FALSE;
       if(produtoOriginal != null){
           //tipos de produto -> pelota,somente podem ser blendados entre si...
           //ou seja, Pelota: tipos de pelota só podem ser blendadas com outros tipos de pelota. Isso significa que não deve ser possível blendar
           //pelota com algum tipo de Pellet Feed ou Sinter Feed.
           if (produtoOriginal.getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA) && produtoAdicionado.getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA)) {
               resultado = Boolean.TRUE;
           } //Pellet Feed: os tipos de pellet feed podem ser blendados entre si e podem ser blendados com os Pellet Screening também.
           //Pellet Screening: os tipos de Pellet Screening podem ser blendados
           else if (produtoOriginal.getTipoProduto().getTipoDeProduto() != (TipoDeProdutoEnum.PELOTA) && produtoAdicionado.getTipoProduto().getTipoDeProduto() != (TipoDeProdutoEnum.PELOTA)) {
               resultado = Boolean.TRUE;
           }

           //verifica se a blendagem foi possivel, do contrario retornara um erro
           if (!resultado) {
               throw new ProdutoIncompativelException(PropertiesUtil.buscarPropriedade("exception.adicionarProduto"));
           }
       }
       
   }
   
   /**
    * Calcula a qualidade média de dois produtos, ponderada pelas suas quantidades
    * @param qualidadeProdutoBase a qualidade do produto base
    * @param quantidadeProdutoBase a quantidade do produto base
    * @param qualidadeProdutoAdicionado a qualidade do produto adicionado
    * @param quantidadeProdutoAdicionado a quantidade do produto adicionado
    * @return
    */
   private Qualidade calculaQualidadeMediaPonderadaPelaQuantidadeNaInsercaoDeProdutoNaBlendagem(Qualidade qualidadeProdutoBase, Double quantidadeProdutoBase, Qualidade qualidadeProdutoAdicionado, Double quantidadeProdutoAdicionado)
   {   
      Qualidade qualidadeResultante = null;
      qualidadeResultante = new Qualidade();
      qualidadeResultante.setDtInicio(new Date(System.currentTimeMillis()));
      qualidadeResultante.setIdUser(1L);
      qualidadeResultante.setDtInsert(new Date(System.currentTimeMillis()));
      /* Pseudocódigo
       * A - Se uma das qualidades não é real 
       * B - retorna uma qualidade não real 
       * C - caso contrário, calcula a qualidade média ponderada pelas quantidades dos produtos, deixando a lista de amostras vazias
       */      
       //if(qualidadeProdutoBase.getEhReal()&&qualidadeProdutoAdicionado.getEhReal())
       //{     
          /*
           * Precisamos setar: 
           * 1 - listaDeAmostras; 
           * 2 - listaDeItensDeControle;
           * 3 - ehReal
           */
          // 1)
          qualidadeResultante.setListaDeAmostras(new ArrayList<Amostra>());
          // 2)
          qualidadeResultante.setListaDeItensDeControle(
                          calculaValoresMediosItensDeControlePonderadosPelaQuantidadeNaInsercaoDeProdutoNaBlendagem(
                                          qualidadeProdutoBase.getListaDeItensDeControle(),
                                          quantidadeProdutoBase, 
                                          qualidadeProdutoAdicionado.getListaDeItensDeControle(),
                                          quantidadeProdutoAdicionado));
          // 3)
          qualidadeResultante.setEhReal(Boolean.TRUE);
       //}
//       else
//       {
//          // 1)
//          qualidadeResultante.setListaDeAmostras(new ArrayList<Amostra>());
//          // 2)
//          qualidadeResultante.setListaDeItensDeControle(criaListaDeItensDeControleComValoresVazios(qualidadeProdutoBase.getListaDeItensDeControle()));
//          // 3)
//          qualidadeResultante.setEhReal(Boolean.FALSE);
//          
//       }
      return qualidadeResultante;
   }

   /**
    * Calcula os valores médios de cada item de controle ponderados pela quantidade dos produtos blendados
    * @param listaDeItensDeControleProdutoBase a lista de itens de controle do produto base da blendagem
    * @param quantidadeProdutoBase a quantidade do produto base da blendagem
    * @param listaDeItensDeControleProdutoAdicionado a lista de itens de controle do produto adicionado na blendagem
    * @param quantidadeProdutoAdicionado a quantidade do produto adicionado na blendagem
    * @return a lista de itens de controle com os valores médios
    */
   private List<ItemDeControle> calculaValoresMediosItensDeControlePonderadosPelaQuantidadeNaInsercaoDeProdutoNaBlendagem(List<ItemDeControle> listaDeItensDeControleProdutoBase, Double quantidadeProdutoBase, List<ItemDeControle> listaDeItensDeControleProdutoAdicionado, Double quantidadeProdutoAdicionado)
   {
      List<ItemDeControle> listaDeItensDeControleResultante = new ArrayList<ItemDeControle>();

      // mapa auxiliar para fazer a busca dos itens de controle
      HashMap<TipoItemDeControle, ItemDeControle> mapaAux = new HashMap<TipoItemDeControle, ItemDeControle>();
      for (ItemDeControle ic : listaDeItensDeControleProdutoAdicionado)
      	mapaAux.put(ic.getTipoItemControle(), ic);
      
      // Para cada item de controle do produto base ...
      for(ItemDeControle itemDeControleDoProdutoBase : listaDeItensDeControleProdutoBase)
      {
         //... encontraremos os itens de controle equivalentes no produto adicionado...
         //ItemDeControle itemDeControleDoProdutoAdicionado = ControladorQualidade.buscaSeHaItemDeControleEquivalenteNaLista(itemDeControleDoProdutoBase, listaDeItensDeControleProdutoAdicionado);
      	ItemDeControle itemDeControleDoProdutoAdicionado = mapaAux.get(itemDeControleDoProdutoBase.getTipoItemControle());
         //... somente os itens de controle comuns aos dois produtos serão relevantes para o produto resultante da blendagem ...
         if(itemDeControleDoProdutoAdicionado!=null)
         {
            //ItemDeControle novoItemDeControleEquivalenteComValorMedio = new ItemDeControle();
        	 /**JESSÉ 18/6*/ 
        	 ItemDeControle novoItemDeControleEquivalenteComValorMedio = new ItemDeControleQualidade();
        	 novoItemDeControleEquivalenteComValorMedio.setDtInicio(new Date(System.currentTimeMillis()));
        	 novoItemDeControleEquivalenteComValorMedio.setDtInsert(new Date(System.currentTimeMillis()));
        	 novoItemDeControleEquivalenteComValorMedio.setIdUser(1L);
        	 /*
             * Precisamos setar:
             * 1 - tipoItemControle
             * 2 - valor
             * 3 - desvioPadraoValor
             * 4 - embarcado
             * 5 - desvioPadraoEmbarcado
             * 6 - adicionar o item de controle na lista
             */

            //1) - basta setar o tipo igual ao de qualquer um dos outros itens de controle equivalentes
            novoItemDeControleEquivalenteComValorMedio.setTipoItemControle(itemDeControleDoProdutoBase.getTipoItemControle());

            //2) - o valor médio será a média ponderada dos valores dos itens equivalentes
            if(itemDeControleDoProdutoAdicionado.getValor()!=null)
            {
               if(itemDeControleDoProdutoBase.getValor()==null)
                  itemDeControleDoProdutoBase.setValor(itemDeControleDoProdutoAdicionado.getValor());
               else
                  novoItemDeControleEquivalenteComValorMedio.setValor(ControladorQualidade.calculaValorMedioPonderadoNaInsercao(itemDeControleDoProdutoBase.getValor(),quantidadeProdutoBase,itemDeControleDoProdutoAdicionado.getValor(),quantidadeProdutoAdicionado));
            }
            else
            {
            	novoItemDeControleEquivalenteComValorMedio.setValor(itemDeControleDoProdutoBase.getValor());
            }

            //3) - o desvio padrão médio será a média ponderada dos desvios padrões, levando em consideração as regras de propagação de erro
//            if(itemDeControleDoProdutoAdicionado.getDesvioPadraoValor()!=null)
//            {
//               if(itemDeControleDoProdutoBase.getDesvioPadraoValor()==null)
//                  itemDeControleDoProdutoBase.setDesvioPadraoValor(itemDeControleDoProdutoAdicionado.getDesvioPadraoValor());
//               else
//                  novoItemDeControleEquivalenteComValorMedio.setDesvioPadraoValor(ControladorQualidade.calculaDesvioPadraoMedioPonderadoNaInsercao(itemDeControleDoProdutoBase.getDesvioPadraoValor(),quantidadeProdutoBase,itemDeControleDoProdutoAdicionado.getDesvioPadraoValor(),quantidadeProdutoAdicionado));
//            }
//            else
//            {
//            	novoItemDeControleEquivalenteComValorMedio.setDesvioPadraoValor(itemDeControleDoProdutoBase.getDesvioPadraoValor());
//            }

            //4)o valor médio será a média dos valores dos itens equivalentes
            if(itemDeControleDoProdutoAdicionado.getEmbarcado()!=null)
            {
               //... caso a qualidade ainda não possua nenhum valor, o valor será dado pelo valor da amostra ...
               if(itemDeControleDoProdutoBase.getEmbarcado()==null)
                  itemDeControleDoProdutoBase.setEmbarcado(itemDeControleDoProdutoAdicionado.getEmbarcado());
               else
                  itemDeControleDoProdutoBase.setEmbarcado(ControladorQualidade.calculaValorMedioPonderadoNaInsercao(itemDeControleDoProdutoBase.getEmbarcado(), quantidadeProdutoBase, itemDeControleDoProdutoAdicionado.getEmbarcado(), quantidadeProdutoAdicionado));
            }
            else
            {
            	novoItemDeControleEquivalenteComValorMedio.setEmbarcado(itemDeControleDoProdutoBase.getEmbarcado());
            }


            //5)- o desvio padrão médio será a média dos desvios padrões, levando em consideração as regras de propagação de erro
//            if(itemDeControleDoProdutoAdicionado.getDesvioPadraoEmbarcado()!=null)
//            {
//               //... caso a qualidade ainda não possua nenhum desvio padrão do valor, o valor do desvio padrãoserá dado pelo valor da amostra ...
//               if(itemDeControleDoProdutoBase.getDesvioPadraoEmbarcado()==null)
//                  itemDeControleDoProdutoBase.setDesvioPadraoEmbarcado(itemDeControleDoProdutoAdicionado.getDesvioPadraoEmbarcado());
//               else
//                  itemDeControleDoProdutoBase.setDesvioPadraoEmbarcado(ControladorQualidade.calculaDesvioPadraoMedioPonderadoNaInsercao(itemDeControleDoProdutoBase.getDesvioPadraoEmbarcado(), quantidadeProdutoBase, itemDeControleDoProdutoAdicionado.getDesvioPadraoEmbarcado(), quantidadeProdutoAdicionado));
//            }
//            else
//            {
//            	novoItemDeControleEquivalenteComValorMedio.setDesvioPadraoEmbarcado(itemDeControleDoProdutoBase.getDesvioPadraoEmbarcado());
//            }

            //6)
            listaDeItensDeControleResultante.add(novoItemDeControleEquivalenteComValorMedio);
            
         }
      }

      return listaDeItensDeControleResultante;
   }

   /**
    * Retira baliza da blendagem passando o produto da baliza como parametro.
    * 
    * @param produto
    *            Produto da baliza a ser retirada da blendagem.
    * @return
    * @throws ProdutoIncompativelException
    */
   public void retirarProdutoDaBlendagem(Produto produto) throws ProdutoIncompativelException
   {
      instanciaListaProdutosSelecionados();
      
      if (produto != null)
      {
    	  listaDeProdutosSelecionados.remove(produto);
    	  
            //... caso a lista fique vazia ...
            if (listaDeProdutosSelecionados.isEmpty())
            {
               //... não temos mais produto na blendagem ...
               produtoResultante = null;
               //... e montamos novamente o mapa de quantidades de tipos de produto da blendagem ...
               montaMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem();
               //... caso sobre um único produto ...
            }else if(listaDeProdutosSelecionados.size()==1)
            {
               //... o produto resultante da blendagem será ele mesmo ...
            	atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposRemocaoDoProduto(produto);
                  
            	produtoResultante = calculaProdutoResultanteDaRemocaoDoProdutoNaBlendagem(produtoResultante, produto);
            	
               //... após copiar, zeramos as amostras se a qualidade medida para o produto é real. Se não for, não podemos dizer nada sobre ela
               /*Qualidade qualidadeResultante = produtoResultante.getQualidade();
               if(qualidadeResultante.getEhReal())
               {
                  qualidadeResultante.setListaDeAmostras(new ArrayList<Amostra>());
               }
               else
               {
                  qualidadeResultante.setListaDeAmostras(new ArrayList<Amostra>());                  
                  qualidadeResultante.setListaDeItensDeControle(criaListaDeItensDeControleComValoresVazios(qualidadeResultante.getListaDeItensDeControle()));
               }*/
               
               //... e montamos novamente o mapa de quantidades de tipos de produto da blendagem ...
               montaMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem();
            }else //... caso sobre mais de um produto, precisamos calcular o produto resultante da retirada do produto ...
            {
               //... atualizamos o mapa de quantidades de tipos de produto da blendagem ...
               atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposRemocaoDoProduto(produto);
               
               produtoResultante = calculaProdutoResultanteDaRemocaoDoProdutoNaBlendagem(produtoResultante, produto);               
            }
         } 
   }

   /**
    * Calcula o produto resultante da blendagem após remover um produto da blendagem atual
    * @param produtoBase o produto base da blendagem de onde se retira um produto 
    * @param produtoRetirado o produto retirado da blendagem
    * @return o produto resultante da remoção de um produto da blendagem
    */
   private Produto calculaProdutoResultanteDaRemocaoDoProdutoNaBlendagem(Produto produtoBase, Produto produtoRetirado)
   {
      Produto produtoResultante =  Produto.criaProduto(buscaOProdutoEmQuantidadePredominante(), System.currentTimeMillis());
      
      /*
       * Precisamos setar:
       * 1 - tipoProduto
       * 2 - quantidade
       * 3 - qualidade
       * 4 - lista de rastreabilidades
       */
      
      //1) Assumiremos o tipoDeProduto predominante, ou seja, o que estiver em maior quantidade
      produtoResultante.setTipoProduto(buscaOProdutoEmQuantidadePredominante());

      //2) A quantidade é simplesmente a subtração da quantidade anterior com a do produto removido
      produtoResultante.setQuantidade(produtoBase.getQuantidade()-produtoRetirado.getQuantidade());
      
      //3) A qualidade será calculada através da retirada do produto da média ponderada das qualidades dos produtos
      produtoResultante.setQualidade(calculaQualidadeMediaPonderadaPelaQuantidadeNaRemocaoDeProdutoNaBlendagem(produtoBase.getQualidade(),produtoBase.getQuantidade(),produtoRetirado.getQualidade(),produtoRetirado.getQuantidade()));
      produtoResultante.getQualidade().setProduto(produtoResultante);
      return produtoResultante;
   }

   /**
    * Calcula a qualidade média ponderada pela quantidade após remover um produto da blendagem
    * 
    * @param qualidadeProdutoBase a qualidade do produto base resultante da blendagem de onde será retirado um produto
    * @param quantidadeProdutoBase a quantidade do produto base resultante da blendagem de onde será retirado um produto
    * @param qualidadeProdutoRetirado a qualidade do produto retirado da blendagem
    * @param quantidadeProdutoRetirado a quantidade do produto retirado da blendagem
    * @return a qualidade do produto resultante da blendagem após a remoção de um produto da blendagem
    */
   private Qualidade calculaQualidadeMediaPonderadaPelaQuantidadeNaRemocaoDeProdutoNaBlendagem(Qualidade qualidadeProdutoBase, Double quantidadeProdutoBase, Qualidade qualidadeProdutoRetirado, Double quantidadeProdutoRetirado)
   {
      instanciaListaProdutosSelecionados();

      Qualidade qualidadeResultante = null;
      qualidadeResultante = new Qualidade();
      
      /* Pseudocódigo
       * A - Se uma das qualidades não é real
       * B - retorna uma qualidade não real
       * C - caso contrário, calcula a qualidade média ponderada pelas quantidades dos produtos após a remoção do produto e deixa a lista de amostras vazia 
       */  
      
      if(isTodasAsQualidadesDosProdutosAdicionadosReais())
      {         
         /*
          * Precisamos setar:
          * 1 - listaDeAmostras;
          * 2 - listaDeItensDeControle;
          * 3 - ehReal
          */

         //1) 
         qualidadeResultante.setListaDeAmostras(new ArrayList<Amostra>());
         // ... no caso estarmos retirando um produto com qualidade conhecida de uma blendagem com qualidade conhecida, basta calcular a média ponderada baseada nos itens de controle conhecidos...
         //if(qualidadeProdutoBase.getEhReal()&&qualidadeProdutoRetirado.getEhReal())
         //{
            //2)
            qualidadeResultante.setListaDeItensDeControle(calculaValoresMediosItensDeControlePonderadosPelaQuantidadeNaRemocaoDeProdutoNaBlendagem(qualidadeProdutoBase.getListaDeItensDeControle(), quantidadeProdutoBase, qualidadeProdutoRetirado.getListaDeItensDeControle(), quantidadeProdutoRetirado));            
         //}
         //else //...no caso de estarmos retirando um produto de blendagem de qualidade desconhecida, e considerando que todos os produtos que sobraram lá dentro tem qualidade conhecida, isto significa que estamos retirando o último produto sem qualidade conhecida, e portanto precisaremos refazer a lista de itens de controle baseado nos produtos selecionados restantes ...
         //{
         //   qualidadeResultante.setListaDeItensDeControle(refazAListaDeItensDeControleDaQualidadeResultanteDaBlendagemBaseadoNosProdutosQueContinuamSelecionados());
         //}
         //3)
         qualidadeResultante.setEhReal(Boolean.TRUE);
         
      }else
      {
         /*
          * Precisamos setar:
          * 1 - listaDeAmostras;
          * 2 - listaDeItensDeControle;
          * 3 - ehReal
          */
      	
         //1) 
         qualidadeResultante.setListaDeAmostras(new ArrayList<Amostra>());
         //2)
         qualidadeResultante.setListaDeItensDeControle(criaListaDeItensDeControleComValoresVazios(qualidadeProdutoBase.getListaDeItensDeControle()));
         //3)
         qualidadeResultante.setEhReal(Boolean.FALSE);

      }

      return qualidadeResultante;
   }

  /* *//**
    * Refaz a lista de itens de controle da qualidade resultante começando do zero, considerando os produtos que continuam selecionados para a blendagem
    * @return a lista de itens de controle da qualidade do produto resultante da blendagem atual
    *//*
   private List<ItemDeControle> refazAListaDeItensDeControleDaQualidadeResultanteDaBlendagemBaseadoNosProdutosQueContinuamSelecionados()
   {
      instanciaListaProdutosSelecionados();
      
      List<ItemDeControle> listaDeItensDeControleResultante = null;
      Double quantidadeProdutoResultante = 0.0;
      
      // Caso haja produto selecionado ...
      if(!listaDeProdutosSelecionados.isEmpty())
      {
         //... setamos inicialmente a lista de itens de controle para ser um clone do primeiro produto da lista...
         CloneHelper clonador = new CloneHelper();
         
         listaDeItensDeControleResultante = clonador.verifyListaDeItensDeControle(listaDeProdutosSelecionados.get(0).getQualidade().getListaDeItensDeControle());
         
         quantidadeProdutoResultante = listaDeProdutosSelecionados.get(0).getQuantidade();
         
         //... caso a lista possua outros produtos adicionados na blendagem, blendamos um por um até o fim da lista.
         if(listaDeProdutosSelecionados.size()>1)
         {
            for(int i=1;i<listaDeProdutosSelecionados.size();i++)
            {
               Produto produtoAnalisado = listaDeProdutosSelecionados.get(i);
               listaDeItensDeControleResultante = calculaValoresMediosItensDeControlePonderadosPelaQuantidadeNaInsercaoDeProdutoNaBlendagem(listaDeItensDeControleResultante, quantidadeProdutoResultante, produtoAnalisado.getQualidade().getListaDeItensDeControle(), produtoAnalisado.getQuantidade());
               quantidadeProdutoResultante+=produtoAnalisado.getQuantidade();
            }
         }
      }
           
      return listaDeItensDeControleResultante;
   }
*/
   /**
    * Calcula os valores médios de cada item de controle relevante e comum dos produtos blendados após a remoção de um produto da blendagem
    * 
    * @param listaDeItensDeControleProdutoBase a lista de itens de controle de um produto base da blendagem do qual será removido o produto
    * @param quantidadeProdutoBase a quantidade deste produto base da blendagem
    * @param listaDeItensDeControleProdutoRetirado a lista de itens de controle do produto removido
    * @param quantidadeProdutoRetirado a quantidade do produto retirado
    * @return a lista de itens de controle com os valores médios e os desvio padrões médios a pós a remoção de um produto da blendagem
    */
   private List<ItemDeControle> calculaValoresMediosItensDeControlePonderadosPelaQuantidadeNaRemocaoDeProdutoNaBlendagem(List<ItemDeControle> listaDeItensDeControleProdutoBase, Double quantidadeProdutoBase, List<ItemDeControle> listaDeItensDeControleProdutoRetirado, Double quantidadeProdutoRetirado)
   {
      List<ItemDeControle> listaDeItensDeControleResultante = new ArrayList<ItemDeControle>();

      // Para cada item de controle do produto base ...
      for(ItemDeControle itemDeControleDoProdutoBase : listaDeItensDeControleProdutoBase)
      {
         //... encontraremos os itens de controle equivalentes no produto removido...
         ItemDeControle itemDeControleDoProdutoRetirado = ControladorQualidade.buscaSeHaItemDeControleEquivalenteNaLista(itemDeControleDoProdutoBase, listaDeItensDeControleProdutoRetirado);
         //... somente os itens de controle comuns aos dois produtos serão relevantes para o produto resultante da blendagem ...
         if(itemDeControleDoProdutoRetirado!=null)
         {
        	 	ItemDeControle novoItemDeControleEquivalenteComValorMedio = new ItemDeControleQualidade();
            /*
             * Precisamos setar:
             * 1 - tipoItemControle
             * 2 - valor
             * 3 - desvioPadraoValor
             * 4 - embarcado
             * 5 - desvioPadraoEmbarcado
             * 6 - adicionar o item de controle na lista
             */

            //1) - basta setar o tipo igual ao de qualquer um dos outros itens de controle equivalentes
            novoItemDeControleEquivalenteComValorMedio.setTipoItemControle(itemDeControleDoProdutoBase.getTipoItemControle());

            //2) - o valor médio será a média ponderada dos valores dos itens equivalentes após a remoção do produto 
            if(itemDeControleDoProdutoBase.getValor()!=null)
            {
               if(itemDeControleDoProdutoRetirado.getValor()!=null)
                  novoItemDeControleEquivalenteComValorMedio.setValor(ControladorQualidade.calculaValorMedioPonderadoNaRemocao(itemDeControleDoProdutoBase.getValor(),quantidadeProdutoBase,itemDeControleDoProdutoRetirado.getValor(),quantidadeProdutoRetirado));
               else
                  novoItemDeControleEquivalenteComValorMedio.setValor(itemDeControleDoProdutoBase.getValor());
            }
               
            //3) - o desvio padrão médio será a média ponderada dos desvios padrões, levando em consideração as regras de propagação de erro, após a remoção do produto
//            if(itemDeControleDoProdutoBase.getDesvioPadraoValor()!=null)
//            {
//               if(itemDeControleDoProdutoRetirado.getDesvioPadraoValor()!=null)
//                  novoItemDeControleEquivalenteComValorMedio.setDesvioPadraoValor(ControladorQualidade.calculaDesvioPadraoMedioPonderadoNaRemocao(itemDeControleDoProdutoBase.getDesvioPadraoValor(),quantidadeProdutoBase,itemDeControleDoProdutoRetirado.getDesvioPadraoValor(),quantidadeProdutoRetirado));
//               else
//                  novoItemDeControleEquivalenteComValorMedio.setDesvioPadraoValor(itemDeControleDoProdutoBase.getDesvioPadraoValor());
//            }
               
            //4)
            if(itemDeControleDoProdutoBase.getEmbarcado()!=null)
            {
               if(itemDeControleDoProdutoRetirado.getEmbarcado()!=null)
                  novoItemDeControleEquivalenteComValorMedio.setEmbarcado(ControladorQualidade.calculaValorMedioPonderadoNaRemocao(itemDeControleDoProdutoBase.getEmbarcado(),quantidadeProdutoBase,itemDeControleDoProdutoRetirado.getEmbarcado(),quantidadeProdutoRetirado));
               else
                  novoItemDeControleEquivalenteComValorMedio.setEmbarcado(itemDeControleDoProdutoBase.getEmbarcado());
            }
            
          //5)
//            if(itemDeControleDoProdutoBase.getDesvioPadraoEmbarcado()!=null)
//            {
//               if(itemDeControleDoProdutoRetirado.getDesvioPadraoEmbarcado()!=null)
//                  novoItemDeControleEquivalenteComValorMedio.setDesvioPadraoEmbarcado(ControladorQualidade.calculaDesvioPadraoMedioPonderadoNaRemocao(itemDeControleDoProdutoBase.getDesvioPadraoEmbarcado(),quantidadeProdutoBase,itemDeControleDoProdutoRetirado.getDesvioPadraoEmbarcado(),quantidadeProdutoRetirado));
//               else
//                  novoItemDeControleEquivalenteComValorMedio.setDesvioPadraoEmbarcado(itemDeControleDoProdutoBase.getDesvioPadraoEmbarcado());
//            }
            
            //6)
            listaDeItensDeControleResultante.add(novoItemDeControleEquivalenteComValorMedio);
         }
      }

      return listaDeItensDeControleResultante;
   }

   /**
    * Verifica se todas as qualidades dos produtos selecionados na blendagem são reais
    * @return verdadeiro se todas as qualidades dos produtos adicionados forem reais
    */
   public boolean isTodasAsQualidadesDosProdutosAdicionadosReais()
   {
      boolean resultado = true;
      
      /*for(Produto produtoAnalisado : listaDeProdutosSelecionados)
      {
         if(!produtoAnalisado.getQualidade().getEhReal())
         {
          resultado = false;
          break;
         }
      }*/
      
      return resultado;
   }

   /**
    * Busca entre os produtos selecionados da blendagem aquele que possui a maior quantidade selecionada
    * @return o tipo de produto de quantidade predominante na blendagem
    */
   private TipoProduto buscaOProdutoEmQuantidadePredominante()
   {
      TipoProduto tipoProdutoEmMaiorQuantidade = null;
      
      tipoProdutoEmMaiorQuantidade = encontraMaiorQuantidadeNoMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem();
      
      return tipoProdutoEmMaiorQuantidade;
   }

   /**
    * Encontra o produto de maior quantidade incluído na blendagem
    * @return o produto de maior quantidade incluído na blendagem
    */
   private TipoProduto encontraMaiorQuantidadeNoMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem()
   {
      TipoProduto tipoProdutoEmMaiorQuantidade = null;
      Double maiorQuantidade = -Double.MAX_VALUE;
      
      if(mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem!=null)
      {
         Iterator it = mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.keySet().iterator();
      
         // Iteramos nos tipos de produtos da blendagem para encontrar aquele que tem a maior quantidade
         while(it.hasNext())
         {
            TipoProduto tipoProdutoAnalisado = (TipoProduto) it.next();
            Double quantidadeDoProdutoAnalisado = mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.get(tipoProdutoAnalisado);
            if(quantidadeDoProdutoAnalisado>maiorQuantidade)
            {
               maiorQuantidade = quantidadeDoProdutoAnalisado;
               tipoProdutoEmMaiorQuantidade = tipoProdutoAnalisado;
            }
         }
      }
     
      return tipoProdutoEmMaiorQuantidade;
   }

   /**
    * Monta o mapa de quantidades de cada tipo de produto da blendagem
    */
   private void montaMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem()
   {
      mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem = new HashMap<TipoProduto, Double>();
      
      instanciaListaProdutosSelecionados();
      
      //Para cada tipo de produto dos produtos selecionados...
      for(Produto produtoAnalisado: listaDeProdutosSelecionados)
      {
         //... analisamos se já existe o tipo de produto em questão ...
         if(mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.get(produtoAnalisado.getTipoProduto())==null)
         {
            // ... caso não exista, incluimos o mesmo com a respectiva quantidade ...
            mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.put(produtoAnalisado.getTipoProduto(),produtoAnalisado.getQuantidade());
         }else
         {
            //... caso já exista, substituímos a sua entrada no mapa adicionando a nova quantidade
            mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.put(produtoAnalisado.getTipoProduto(),(mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.get(produtoAnalisado.getTipoProduto())+produtoAnalisado.getQuantidade()));
         }      
      }     
   }
   
   /**
    * Atualiza o mapa de quantidades de tipo de produto escolhidos na blendagem após a inserção de um produto
    * 
    * @param produtoInserido o produto a ser inserido
    */
   public void atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposInsercaoDoProduto(Produto produtoInserido, double quantidadeBlendada)
   {
      instanciaMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem();
      
      //... analisamos se já existe o tipo de produto em questão ...
      if(mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.get(produtoInserido.getTipoProduto())==null)
      {
         // ... caso não exista, incluimos o mesmo com a respectiva quantidade ...
         mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.put(produtoInserido.getTipoProduto(), quantidadeBlendada);
      }else
      {
         //... caso já exista, substituímos a sua entrada no mapa adicionando a nova quantidade
         mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.put(produtoInserido.getTipoProduto(),(mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.get(produtoInserido.getTipoProduto())+quantidadeBlendada));
      }      
   }
   
   /**
    * Atualiza o mapa de quantidades de tipo de produto escolhidos na blendagem após a remoção de um produto
    * 
    * @param produtoRemovido o produto removido
    */
   private void atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposRemocaoDoProduto(Produto produtoRemovido)
   {
      instanciaMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem();
      
      //... analisamos se só existe o  produto em questão com seu tipo...
      Double value = mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.get(produtoRemovido.getTipoProduto());
      if (value != null) { 
    	  if(value.equals(produtoRemovido.getQuantidade()))
    	  {
    		  //... caso ele seja o único, removemos a entrada deste tipo de produto do mapa
    		  mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.remove(produtoRemovido.getTipoProduto());
    	  }else 
    	  {
    		  //... caso não seja o único, substituímos a sua entrada no mapa removendo a sua quantidade
    		  mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.put(produtoRemovido.getTipoProduto(),(mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem.get(produtoRemovido.getTipoProduto())-produtoRemovido.getQuantidade()));
    	  }
      }	  
   }

   /**
    * Atualiza a carga para atendimento para ser a carga selecionada. Logo após essa atualização, deve-se calcular o atendimento da carga. 
    * Caso aconteça algum erro nesse cálculo, essa carga deve ser deselecionada para atendimento. Somente cargas cujo produto seja do mesmo tipo da blendagem selecionada podem ser atendidas.
    * 
    * @param cargaSelecionada
    *           a carga a ser atendida
    * @return <code>true</code> se a operação for bem sucedida
    * @throws ProdutoIncompativelException
    * @throws ExcessoDeMaterialParaEmbarqueException
    */
   public Produto atualizarCargaSelecionada(Carga cargaSelecionada) throws ProdutoIncompativelException, CargaSelecionadaException, ExcessoDeMaterialParaEmbarqueException
   {
	  // setamos a carga selecionada 
      this.setCargaSelecionada(cargaSelecionada);

      if (produtoResultante != null)
      {
        Double novaQuantidadeBlendada = null;
        if (cargaSelecionada.getProduto()!= null)
           novaQuantidadeBlendada = produtoResultante.getQuantidade() + cargaSelecionada.getProduto().getQuantidade();
        else
           novaQuantidadeBlendada = produtoResultante.getQuantidade();
        if (novaQuantidadeBlendada <= cargaSelecionada.getOrientacaoDeEmbarque().getQuantidadeNecessaria())
        {
           this.setCargaSelecionada(cargaSelecionada);               
           return(cargaSelecionada.getProduto());
        } else
        {
           throw new ExcessoDeMaterialParaEmbarqueException();                  
        }
      } else
      {
         //... se a carga possuir produto, adicionamos na blendagem.
         return(cargaSelecionada.getProduto());
      }
   }

   /**
    * Retira uma carga selecionada da blendagem.
    * 
    * @param carga
    *           Carga a ser retirada da blendagem.
    * @return retorna a propria blendagem.
    * @throws ProdutoIncompativelException
    */
   public void retirarCarga(Carga carga) throws ProdutoIncompativelException
   {
      instanciaListaProdutosSelecionados();
      
      this.setCargaSelecionada(null);
      
      Produto produtoDaCarga = carga.getProduto();
      if(produtoDaCarga!=null)
         this.retirarProdutoDaBlendagem(produtoDaCarga);
      
      if (listaDeProdutosSelecionados.isEmpty())
      {
         produtoResultante = null;
      } 
   }

   /**
    * Adiciona o material da campanha selecionada na blendagem após realizar todas as verificações para garantir que o mesmo pode ser adicionado
    * @param campanha a campanha cujo produto será adicionado na blendagem
    * @param quantidade a quantidade de material a ser adicionado na blendagem
    * @throws ExcessoDeMaterialParaEmbarqueException caso seja selecionado mais material do que o necessário para embarcar a carga
    */
   public void adicionarCampanhaBlendada(Campanha campanha, Double quantidade) throws ExcessoDeMaterialParaEmbarqueException
   {
      instanciaListaCampanha();

      if (produtoResultante != null && cargaSelecionada!=null)
      {
         Double novaQuantidadeBlendada = produtoResultante.getQuantidade() + quantidade;

         //... verificamos se com a seleção desta carga iremos exceder a capacidade da carga ...
         if (novaQuantidadeBlendada <= cargaSelecionada.getOrientacaoDeEmbarque().getQuantidadeNecessaria())
         {
            //... blendamos o novo produto selecionado ...
            listaDeCampanhas.add(campanha);                                                   
         }else
         {
            throw new ExcessoDeMaterialParaEmbarqueException();
         }      
      } else
      {        
         //... colocamos a campanha na lista de campanhas blendadas.
         listaDeCampanhas.add(campanha);
      }
   }

   public void retirarCampanha(Campanha campanha) throws ProdutoIncompativelException
   {
      instanciaListaCampanha();
      instanciaListaProdutosSelecionados();

      listaDeCampanhas.remove(campanha);
      
      Produto produtoSelecionadoDaCampanha = null; //campanha.getProdutoSelecionado();
          
      if (produtoSelecionadoDaCampanha != null)
      {
         this.retirarProdutoDaBlendagem(produtoSelecionadoDaCampanha);

      }
         
      if (listaDeProdutosSelecionados.isEmpty())
      {
         produtoResultante = null;
      }       
   }

   /**
    * @return the cargaSelecionada
    */
   public Carga getCargaSelecionada() {
      return cargaSelecionada;
   }

   /**
    * @param cargaSelecionada
    *            the cargaSelecionada to set
    */
   public void setCargaSelecionada(Carga cargaSelecionada) {
      this.cargaSelecionada = cargaSelecionada;
   }

   /**
    * @param listaDeProdutosSelecionados the listaDeProdutosSelecionados to set
    */
   public void setListaDeProdutosSelecionados(List<Produto> listaDeProdutosSelecionados)
   {
      this.listaDeProdutosSelecionados = listaDeProdutosSelecionados;
   }

   /**
    * @return the controladorCalculoQualidade
    */
   public ControladorCalculoQualidade getControladorCalculoQualidade() {
      return controladorCalculoQualidade;
   }

   /**
    * @param controladorCalculoQualidade
    *            the controladorCalculoQualidade to set
    */
   public void setControladorCalculoQualidade(ControladorCalculoQualidade controladorCalculoQualidade) {
      this.controladorCalculoQualidade = controladorCalculoQualidade;
   }

   /**
    * @return the listaDeProdutosSelecionados
    */
   public List<Produto> getListaDeProdutosSelecionados() {
      if (listaDeProdutosSelecionados == null) {
         listaDeProdutosSelecionados = new ArrayList<Produto>();
      }
      return listaDeProdutosSelecionados;
   }

   public List<Campanha> getListaDeCampanhas() {
      return listaDeCampanhas;
   }

   public void setListaDeCampanhas(List<Campanha> listaDeCampanhas) {
      this.listaDeCampanhas = listaDeCampanhas;
   }

   public List<Baliza> getListaBalizasBlendadas() {
      return listaBalizasBlendadas;
   }

   public void setListaBalizasBlendadas(List<Baliza> listaBalizasBlendadas) {
      this.listaBalizasBlendadas = listaBalizasBlendadas;
   }

   /** 
    * Adiciona a baliza com o produto blendado a lista de balizas blendadas
    * @param balizaBlendada
    * @throws ProdutoIncompativelException 
    * @throws ExcessoDeMaterialParaEmbarqueException 
    */
   public Produto adicionaBalizaBlendada(Baliza balizaBlendada) throws ProdutoIncompativelException, ExcessoDeMaterialParaEmbarqueException {

      instanciaListaBalizasBlendadas();

      if (produtoResultante != null)
      {
         Double novaQuantidadeBlendada = null;
         novaQuantidadeBlendada = produtoResultante.getQuantidade() + balizaBlendada.getProduto().getQuantidade();
         if(cargaSelecionada!=null)
         {
            if (novaQuantidadeBlendada <= cargaSelecionada.getOrientacaoDeEmbarque().getQuantidadeNecessaria())
            {
               listaBalizasBlendadas.add(balizaBlendada);
               return(balizaBlendada.getProduto());
            }else
            {
               throw new ExcessoDeMaterialParaEmbarqueException();
            }
         }else
         {
            listaBalizasBlendadas.add(balizaBlendada);
            return(balizaBlendada.getProduto());
         }          
      }else
      {
         listaBalizasBlendadas.add(balizaBlendada);
         return(balizaBlendada.getProduto());
      }     
   }

   /**
    * Remove a baliza com o produto desblendado da lista de balizas blendadas
    * 
    * @param balizaBlendada
    * @throws ProdutoIncompativelException 
    */
   public void removeBalizaBlendada(Baliza balizaBlendada) throws ProdutoIncompativelException
   {
      instanciaListaBalizasBlendadas();
      instanciaListaProdutosSelecionados();
      
      listaBalizasBlendadas.remove(balizaBlendada);
      
      Produto produtoDaBaliza = balizaBlendada.getProduto();
      
      if(produtoDaBaliza!=null)
      {
         this.retirarProdutoDaBlendagem(produtoDaBaliza);

      }
      
      
      if (listaDeProdutosSelecionados.isEmpty())
      {
         produtoResultante = null;
      } 
   }

   public void instanciaListaCampanha() {
      if (this.listaDeCampanhas == null) {
         listaDeCampanhas = new ArrayList<Campanha>();
      }
   }
   
   public void instanciaListaBalizasBlendadas()
   {
      if (this.listaBalizasBlendadas == null)
      {
         listaBalizasBlendadas = new ArrayList<Baliza>();
      }
   }

   public void instanciaListaProdutosSelecionados()
   {
      if (this.listaDeProdutosSelecionados == null)
      {
         listaDeProdutosSelecionados = new ArrayList<Produto>();
      }
   }
   
   public void instanciaMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem()
   {
      if (this.mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem == null)
      {
         mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem = new HashMap<TipoProduto, Double>();
      }
   }
   
   /**
    * @return the produtoResultante
    */
   public Produto getProdutoResultante()
   {
      return produtoResultante;
   }

   /**
    * @param produtoResultante the produtoResultante to set
    */
   public void setProdutoResultante(Produto produtoResultante)
   {
      this.produtoResultante = produtoResultante;
   }

   /**
    * @return the mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem
    */
   public HashMap<TipoProduto, Double> getMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem()
   {
      return mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem;
   }

   /**
    * @param mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem the mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem to set
    */
   public void setMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem(HashMap<TipoProduto, Double> mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem)
   {
      this.mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem = mapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem;
   }
}
