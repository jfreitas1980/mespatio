package com.hdntec.gestao.domain.vo.atividades;

import java.util.ArrayList;
import java.util.List;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;


public class MovimentacaoVO extends AtividadeRecuperarEmpilharVO
{ 
   private TipoAtividadeEnum tipoAtividade;
  
   private Atividade atividadeAnterior;
   
   /* numero do porao na movimentacao pilha para navio */
   private String txtNumeroPorao;
   
   /* grupo de balizas destino. As balizas origem estao na classe AtividadeRecuperarEmpilharVO */
   private List<MetaBaliza> listaBalizasDestino;
   
   /* nome da pilha destino */
   private String nomePilhaDestino;
   
   /* quantidade de material movimentado */
   private double quantidadeMovimentacao;
   
   public MovimentacaoVO() 
   {   
   }

   public String getTxtNumeroPorao()
   {
      return txtNumeroPorao;
   }

   public void setTxtNumeroPorao(String txtNumeroPorao)
   {
      this.txtNumeroPorao = txtNumeroPorao;
   }

   public List<MetaBaliza> getListaBalizasDestino()
   {
       if (listaBalizasDestino == null)
           listaBalizasDestino = new ArrayList<MetaBaliza>();
       return listaBalizasDestino;
   }

   public void setListaBalizasDestino(List<MetaBaliza> listaBalizasDestino)
   {
      this.listaBalizasDestino = listaBalizasDestino;
   }

   public double getQuantidadeMovimentacao()
   {
      return quantidadeMovimentacao;
   }

   public void setQuantidadeMovimentacao(double quantidadeMovimentacao)
   {
      this.quantidadeMovimentacao = quantidadeMovimentacao;
   }

public String getNomePilhaDestino() {
	return nomePilhaDestino;
}

public void setNomePilhaDestino(String nomePilhaDestino) {
	this.nomePilhaDestino = nomePilhaDestino;
}

public TipoAtividadeEnum getTipoAtividade() {
    return tipoAtividade;
}

public void setTipoAtividade(TipoAtividadeEnum tipoAtividade) {
    this.tipoAtividade = tipoAtividade;
}

@Override
public Atividade getAtividadeAnterior() {
    return atividadeAnterior;
}

@Override
public void setAtividadeAnterior(Atividade atividadeAnterior) {
    this.atividadeAnterior = atividadeAnterior;
}

}