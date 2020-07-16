package com.hdntec.gestao.cliente.atualizacao.sistemasexternos.sap;

import java.util.Date;

import com.hdntec.gestao.domain.produto.entity.TipoProduto;


public class TipoProdutoIntegracaoSAP
{

   /** Tipo de produto */
   private TipoProduto tipoProduto;

   /** Data do estoque */
   private Date dataEstoque;

   /** quantidade estoque do SAP */
   private Double quantidaeEstoqueSAP;

   /** quantidade estoque do mespatio */
   private Double quantidadeEstoqueMESPATIO;

   /** quantidade da diferenca entre SAP e MESPATIO */
   private Double quantidadeProdutoDivergente;

   /** situacao do estoque */
   private String situacao;

   public Double getQuantidadeEstoqueMESPATIO()
   {
      return quantidadeEstoqueMESPATIO;
   }

   public void setQuantidadeEstoqueMESPATIO(Double quantidadeEstoqueMESPATIO)
   {
      this.quantidadeEstoqueMESPATIO = quantidadeEstoqueMESPATIO;
   }

   public Double getQuantidaeEstoqueSAP()
   {
      return quantidaeEstoqueSAP;
   }

   public String getSituacao()
   {
      return situacao;
   }

   public void setSituacao(String situacao)
   {
      this.situacao = situacao;
   }

   public void setQuantidaeEstoqueSAP(Double quantidaeEstoqueSAP)
   {
      this.quantidaeEstoqueSAP = quantidaeEstoqueSAP;
   }

   public TipoProduto getTipoProduto()
   {
      return tipoProduto;
   }

   public void setTipoProduto(TipoProduto tipoProduto)
   {
      this.tipoProduto = tipoProduto;
   }

   public Double getQuantidadeProdutoDivergente()
   {
      return quantidadeProdutoDivergente;
   }

   public void setQuantidadeProdutoDivergente(Double quantidadeProdutoDivergente)
   {
      this.quantidadeProdutoDivergente = quantidadeProdutoDivergente;
   }

   public Date getDataEstoque()
   {
      return dataEstoque;
   }

   public void setDataEstoque(Date dataEstoque)
   {
      this.dataEstoque = dataEstoque;
   }

}
