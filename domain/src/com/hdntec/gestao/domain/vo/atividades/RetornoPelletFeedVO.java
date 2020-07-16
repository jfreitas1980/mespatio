package com.hdntec.gestao.domain.vo.atividades;

public class RetornoPelletFeedVO extends AtividadeRecuperarEmpilharVO
{ 
   
   /* quantidade de material movimentado */
   private double quantidadeMovimentacao;
   
   public RetornoPelletFeedVO() 
   {   
   }

   public double getQuantidadeMovimentacao()
   {
      return quantidadeMovimentacao;
   }

   public void setQuantidadeMovimentacao(double quantidadeMovimentacao)
   {
      this.quantidadeMovimentacao = quantidadeMovimentacao;
   }

}