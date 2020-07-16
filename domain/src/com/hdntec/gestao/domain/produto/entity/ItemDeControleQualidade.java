package com.hdntec.gestao.domain.produto.entity;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.annotations.ForeignKey;

/**
 * Itens de controle são as propriedades físicas, químicas e metalúrgicas que compõem a {@link Qualidade} de um {@link Produto}. *
 * <p>
 * Esses principais valores são:
 * <ul>
 * <li>meta - a meta do item de controle, determinado pela carga a ser atendida
 * <li>embarcado - o valor do item de controle medido no embarque
 * <li>estimado - o valor estimado do item de controle, calculado através de coeficientes empíricos
 * <li>blend - o valor da média ponderada da blendagem de diferentes produtos
 * </ul>
 * 
 * @author andre
 */
@Entity
@Table(name = "ItemCtrQualidade")
public class ItemDeControleQualidade extends ItemDeControle 
{

   public ItemDeControleQualidade()
   {
      super();
   }
   private Qualidade qualidade;
   
   private String usinas; 

   @ManyToOne
   @ForeignKey(name = "fk_qualidade_item")
   @JoinColumn(name = "id_Qualidade")
   public Qualidade getQualidade()
   {
      return qualidade;
   }

   public void setQualidade(Qualidade qualidade)
   {
      this.qualidade = qualidade;
   }

      @Column(name="usinas",length=255,nullable=true)
   public String getUsinas() {
	   return usinas;
   }

   public void setUsinas(String usinas) {
	   this.usinas = usinas;
   }
   @Override
   public ItemDeControle copiarStatus() {
       BeanUtilsBean copyBean = new BeanUtilsBean();
       ItemDeControleQualidade newStatus = null;
       try {           
           newStatus = (ItemDeControleQualidade) copyBean.cloneBean(this);
           newStatus.setIdItemDeControle(null);
           newStatus.setDtInicio(this.getDtInicio());
           newStatus.setDtFim(this.getDtFim());           
           newStatus.setTipoItemControle(this.getTipoItemControle());
           newStatus.setIdUser(this.cloneLong(this.getIdUser()));
           newStatus.setLimInfMetaOrientacaoEmb(this.cloneDouble(this.getLimInfMetaOrientacaoEmb()));
           newStatus.setLimSupMetaOrientacaoEmb(this.cloneDouble(this.getLimSupMetaOrientacaoEmb()));
           newStatus.setEmbarcado(this.cloneDouble(this.getEmbarcado()));
           newStatus.setValor(this.cloneDouble(this.getValor()));
           
       } catch (IllegalAccessException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (InstantiationException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           // TODO Auto-generated catch block
           e.printStackTrace(); 
       } catch (NoSuchMethodException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       return newStatus;             
   }
       
}
