package com.hdntec.gestao.domain.blendagem;

import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CargaSelecionadaException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;


/**
 * Controla as operações do subsistema de cálculo de qualidade. Esse subsistema é responsável por calcular a blendagem de produtos de campanhas de usinas e de balizas para atender determinadas cargas.
 * 
 * 
 * @author andre
 * 
 */
public class ControladorCalculoQualidade implements IControladorCalculoQualidade {

    /** referência à  classe que controla as operações do modelo de domínio */    

    /** a {@link Blendagem} atualmente selecionada */
    private Blendagem blendagem;

    /**
     * Metodo auxiliar que verifica se existe uma blendagem criada para a realizacao do calculo
     */
    @Override
    public void instanciaBlendagem() {
        if (blendagem == null) {
            blendagem = Blendagem.getInstance();
            blendagem.setControladorCalculoQualidade(this);
        }
    }
    
    public void instanciaNovaBlendagem() 
    {    
    	blendagem = Blendagem.getNewInstance();
        blendagem.setControladorCalculoQualidade(this);
    }

    @Override
    public Blendagem blendarCampanhaDeUsina(Campanha campanha, Double quantidade, TipoDeProdutoEnum tipoDeProdutoSelecionado) throws BlendagemInvalidaException, CampanhaIncompativelException, ExcessoDeMaterialParaEmbarqueException, ProdutoIncompativelException {
        instanciaBlendagem();
        
        Produto produtoDaCampanha = pegaOProdutoSelecionadoNaUsina(campanha, tipoDeProdutoSelecionado, quantidade);
        
        //...verifica se os produtos são compativeis para se realizar a blendagem ..
        if(produtoDaCampanha != null)
        {
            blendagem.verificaPossibilidadeDeBlendagem(blendagem.getProdutoResultante(), produtoDaCampanha);
        }
        
        blendagem.adicionarCampanhaBlendada(campanha, quantidade);
        
        if(produtoDaCampanha!=null)
        {
           blendagem.inserirProdutoNaBlendagem(produtoDaCampanha, produtoDaCampanha.getQuantidade());
        }
           
        return blendagem;
    }

    /**
     *  Método que retorna o produto selecionado na usina e seta este produto na campanha
     * @param campanha a campanha da usina selecionada
     * @param tipoDeProdutoSelecionadNaUsina o tipo de produto selecionado
     * @param quantidade a quantidade de material selecionada
     * @return o produto selecionado
     */
    private Produto pegaOProdutoSelecionadoNaUsina(Campanha campanha, TipoDeProdutoEnum tipoDeProdutoSelecionadNaUsina, Double quantidade)
    {
       //Achamos qual foi o tipo de produto selecionado na usina...
       TipoProduto tipoProdutoSelecionadoNaCampanha = null;
       if(tipoDeProdutoSelecionadNaUsina.equals(TipoDeProdutoEnum.PELOTA))
          tipoProdutoSelecionadoNaCampanha = campanha.getTipoProduto();
       else if (tipoDeProdutoSelecionadNaUsina.equals(TipoDeProdutoEnum.PELLET_FEED))
          tipoProdutoSelecionadoNaCampanha = campanha.getTipoPellet();
       else
          tipoProdutoSelecionadoNaCampanha = campanha.getTipoScreening();

       Produto novoProdutoParaBlendagem = new Produto((new Date()).getTime(),campanha.getQualidadeEstimada(),quantidade, tipoProdutoSelecionadoNaCampanha);
       //...setamos o produto selecionado na campanha ...
       //campanha.setProdutoSelecionado(novoProdutoParaBlendagem);

       return novoProdutoParaBlendagem;
    }

   @Override
    public Blendagem blendarBaliza(Baliza baliza) throws BlendagemInvalidaException, ProdutoIncompativelException, ExcessoDeMaterialParaEmbarqueException {
        instanciaBlendagem();
        //...verifica se os produtos sao compativeis para se realizar a blendagem ..
        if(baliza.getProduto() != null){
            blendagem.verificaPossibilidadeDeBlendagem(blendagem.getProdutoResultante(), baliza.getProduto());
        }

        Produto produtoDaBaliza = blendagem.adicionaBalizaBlendada(baliza);
        if(produtoDaBaliza!=null)
        {
           blendagem.inserirProdutoNaBlendagem(produtoDaBaliza, produtoDaBaliza.getQuantidade());
        }
           
        return blendagem;
    }

    @Override
    public Blendagem removerBlendagemBaliza(Baliza baliza) throws BlendagemInvalidaException, ProdutoIncompativelException
    {
       instanciaBlendagem();
       if (baliza !=null)
       {
          blendagem.removeBalizaBlendada(baliza);
       }
       return blendagem;
    }

    @Override
    public Blendagem atenderCarga(Carga carga) throws BlendagemInvalidaException, ProdutoIncompativelException, CargaSelecionadaException, ExcessoDeMaterialParaEmbarqueException {
        instanciaNovaBlendagem();
        //...verifica se os produtos sao compativeis para se realizar a blendagem ..
        if(carga.getProduto() != null){
            blendagem.verificaPossibilidadeDeBlendagem(blendagem.getProdutoResultante(), carga.getProduto());
        }
        Produto produtoDaCarga = blendagem.atualizarCargaSelecionada(carga);
        if(produtoDaCarga!=null)
        {
           blendagem.inserirProdutoNaBlendagem(produtoDaCarga, produtoDaCarga.getQuantidade());
        }          
        return blendagem;
    }

    @Override
   public Blendagem removerCarga(Carga carga) throws BlendagemInvalidaException, ProdutoIncompativelException
   {
      instanciaBlendagem();
      if (carga != null)
      {
         blendagem.retirarCarga(carga);
      }
      return blendagem;
   }

    @Override
    public Blendagem removerBlendagemCampanhaDeUsina(Campanha campanha) throws BlendagemInvalidaException, ProdutoIncompativelException {
        instanciaBlendagem();
        if(campanha!=null)
        {
           blendagem.retirarCampanha(campanha);
        }
        return blendagem;
    }

    @Override
    public Blendagem getBlendagem() {
        return blendagem;
    }

    @Override
    public void setBlendagem(Blendagem blendagem) {
        this.blendagem = blendagem;
    }

   /* public IControladorModelo getModelo() {
        return modelo;
    }

    public void setModelo(IControladorModelo modelo) {
        this.modelo = modelo;
    }*/

    @Override
    public List<Baliza> obterListaBalizasBlendada() {
        return (blendagem != null) ? blendagem.getListaBalizasBlendadas() : null;
    }

    @Override
    public List<Campanha> obterListaCampanhasBlendadas() {
        return (blendagem != null) ? blendagem.getListaDeCampanhas() : null;
    }

    @Override
    public void limparBlendagem() {
        setBlendagem(null);
    }

   @Override
   public Produto getProdutoResultanteDaBlendagem()
   {
      return blendagem.getProdutoResultante();
   }

   @Override
   public void inserirProdutoNaBlendagem(Produto produto) throws ProdutoIncompativelException
   {
      blendagem.inserirProdutoNaBlendagem(produto, produto.getQuantidade());
   }

   @Override
   public void retirarProdutoDaBlendagem(Produto produto) throws ProdutoIncompativelException
   {
      blendagem.retirarProdutoDaBlendagem(produto);
      
   }
}
