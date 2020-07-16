/**
 * O produto é um bem comercializado.
 *
 * @author andre
 */
package com.hdntec.gestao.domain.produto.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;


@Entity
public class Produto extends StatusEntity<Produto>
{

   /** Serialização do objeto */
   private static final long serialVersionUID = -909625935654443214L;

   /** identificador de produto */
   private Long idProduto;

   /** a quantidade em toneladas deste produto */
   private Double quantidade;

   /** o tipo de produto */
   private TipoProduto tipoProduto;

   /** Qualidade do produto */
   private Qualidade qualidade;

   /** a lista de origens do produto */
   private List<Rastreabilidade> rastreabilidades;

   
   
     /**
    * Construtor Padrao.
    */
   public Produto()
   {
   }

   public Produto(Long idProduto, Qualidade qualidade, Double quantidade, TipoProduto tipoProduto)
   {
      this.idProduto = idProduto;
      this.qualidade = qualidade;
      this.quantidade = quantidade;
      this.tipoProduto = tipoProduto;
   }


   @Override
   public String toString()
   {
       StringBuffer value = new StringBuffer(); 
	   value.append(tipoProduto.toString()).append(" - ").append(quantidade).append(" ton - ").append(qualidade.toString());
	   return value.toString();
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "produto_seq")
   @SequenceGenerator(name = "produto_seq", sequenceName = "seqproduto")
   public Long getIdProduto()
   {
      return idProduto;
   }

   @Column(nullable = false)
   public Double getQuantidade()
   {
      return quantidade;
   }

   @OneToOne
   @ForeignKey(name = "fk_produto_tipoProduto")
   @JoinColumn(name = "id_TipoProduto")
   public TipoProduto getTipoProduto()
   {
      return tipoProduto;
   }

   public void setIdProduto(Long idProduto)
   {
      this.idProduto = idProduto;
   }

   public void setQuantidade(Double quantidade)
   {
      this.quantidade = quantidade;
      
   }

   public void setTipoProduto(TipoProduto tipoProduto)
   {
      this.tipoProduto = tipoProduto;
   }
   
  
/*   @OneToOne
   @Cascade(value = { CascadeType.SAVE_UPDATE })
   @ForeignKey(name = "fk_produto_qualidade")
   @JoinColumn(name = "id_qualidade",nullable=false,updatable=false)
   public Qualidade getQualidade()
   {
      return qualidade;
   }*/

   @OneToOne
   //@Cascade(value = { CascadeType.SAVE_UPDATE })
   @ForeignKey(name = "fk_produto_qualidade")
   @JoinColumn(name = "id_qualidade",nullable=true)
   public Qualidade getQualidade()
   {
      return qualidade;
   }

   public void setQualidade(Qualidade qualidade)
   {
      this.qualidade = qualidade;      
      if (this.qualidade != null) {
    	  this.qualidade.setProduto(this);
      }	  
   }

  @Transient                    
   public List<Rastreabilidade> getListaDeRastreabilidades()
   {
       return this.getRastreabilidades();
   }
   @Transient
   public void setListaDeRastreabilidades(List<Rastreabilidade> listaDeRastreabilidades)
   {
      this.setRastreabilidades(listaDeRastreabilidades);
   }


   public void addRastreabilidade(Rastreabilidade rastreabilidade)
   {
      if (getListaDeRastreabilidades() == null)
      {
         setListaDeRastreabilidades(new ArrayList<Rastreabilidade>());
      }
      if (!getListaDeRastreabilidades().contains(rastreabilidade))
      {
         rastreabilidade.addProduto(this);
         getListaDeRastreabilidades().add(rastreabilidade);
      }
   }

   public void addRastreabilidade(List<Rastreabilidade> listaRastreabilidades)
   {
      if (listaRastreabilidades != null)
      {
         for (Rastreabilidade item : listaRastreabilidades)
         {
            addRastreabilidade(item);
         }            
      }
   }

   public static Produto criaProduto(TipoProduto tipoProduto,long timeDefault) {
       // cria objeto produto para carga com dados reais
       Produto p = new Produto();
       p.setIdUser(1L);
       p.setDtInsert(new Date(timeDefault));
       p.setDtInicio(new Date(timeDefault));           
       p.setQuantidade(new Double(0));
       p.setTipoProduto(tipoProduto);
       return p;
   }
   
   
   
   
   @Override
   public Produto copiarStatus() {
     Produto result = super.copiarStatus();     
     result.setListaDeRastreabilidades(null);
     result.addRastreabilidade(this.rastreabilidades);
     result.setIdProduto(null);   
    /*if (result.getQualidade() != null)
     {    	
         Qualidade novaQualidade = result.getQualidade().copiarStatus();    	
         
         novaQualidade.setIdQualidade(null);                           
         result.setQualidade(novaQualidade);
         novaQualidade.setProduto(result);
    //	   AMOSTRAS
    	   
         List<Amostra> novasAmostras = new ArrayList<Amostra>();
         if (novaQualidade.getListaDeAmostras() != null) {
	         for (Amostra amostra : novaQualidade.getListaDeAmostras()) {
	            Amostra novaAmostra =  amostra.copiarStatus();   
	            novaAmostra.setIdAmostra(null);                        
	            List<ItemDeControleAmostra> itensControle = new ArrayList<ItemDeControleAmostra>();
	            for (ItemDeControleAmostra item : novaAmostra.getListaDeItensDeControleAmostra()) {
	                ItemDeControleAmostra novoItem =(ItemDeControleAmostra)item.copiarStatus();   
	                novoItem.setIdItemDeControle(null);
	                novoItem.setAmostra(novaAmostra);             
	                itensControle.add(novoItem);
	                
	            }
	            novaAmostra.setListaDeItensDeControleAmostra(itensControle);
	            novasAmostras.add(novaAmostra);                        
	         }
	         novaQualidade.setListaDeAmostras(novasAmostras);
         } else {
        	 novaQualidade.setListaDeAmostras(new ArrayList<Amostra>());
         }
         
      //     ITENS DE CONTROLE
           
         List<ItemDeControleQualidade> itensControle = new ArrayList<ItemDeControleQualidade>();
         for (ItemDeControleQualidade item : novaQualidade.getListaDeItensDeControleQualidade()) {
             ItemDeControleQualidade novoItem =(ItemDeControleQualidade)item.copiarStatus();   
             novoItem.setIdItemDeControle(null);            
             //novoItem.setQualidade(novaQualidade);             
             itensControle.add(novoItem);
         }         
         novaQualidade.setListaDeItensDeControleQualidade(null);
         novaQualidade.setListaDeItensDeControleQualidade(new ArrayList<ItemDeControleQualidade>());
         for(ItemDeControleQualidade item : itensControle) {
        	 item.setQualidade(novaQualidade);
        	 novaQualidade.getListaDeItensDeControleQualidade().add(item); 
        	 
         }
         itensControle = null;
         // novaQualidade.setListaDeItensDeControleQualidade(itensControle);
         result.setQualidade(novaQualidade);
     }*/
     return result;       
   }
   
   
   
   @ManyToMany(targetEntity=Rastreabilidade.class)
   @Cascade(value={CascadeType.SAVE_UPDATE})           
   @ForeignKey(name = "fk_rastr_prod", inverseName = "fk_rastr_prod_inv")
   @JoinTable(name = "Rast_Produto", joinColumns = @JoinColumn(name = "idProduto"), inverseJoinColumns = @JoinColumn(name = "idRast"))   
   public List<Rastreabilidade> getRastreabilidades() {
        if (rastreabilidades == null) {
            rastreabilidades = new ArrayList<Rastreabilidade>();
        }
       return rastreabilidades;
   }

public void setRastreabilidades(List<Rastreabilidade> rastreabilidades) {
    this.rastreabilidades = rastreabilidades;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((idProduto == null) ? 0 : idProduto.hashCode());
	result = prime * result
			+ ((quantidade == null) ? 0 : quantidade.hashCode());
	result = prime * result
			+ ((tipoProduto == null) ? 0 : tipoProduto.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (!super.equals(obj))
		return false;
	if (getClass() != obj.getClass())
		return false;
	Produto other = (Produto) obj;
	if (idProduto == null) {
		if (other.idProduto != null)
			return false;
	} else if (!idProduto.equals(other.idProduto))
		return false;
	if (quantidade == null) {
		if (other.quantidade != null)
			return false;
	} else if (!quantidade.equals(other.quantidade))
		return false;
	if (tipoProduto == null) {
		if (other.tipoProduto != null)
			return false;
	} else if (!tipoProduto.equals(other.tipoProduto))
		return false;
	return true;
}
}
 