package com.hdntec.gestao.domain.produto.entity;

import java.lang.reflect.InvocationTargetException;

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
@Table(name = "ItemCtrAmostra")
public class ItemDeControleAmostra extends ItemDeControle
{

   public ItemDeControleAmostra()
   {
      super();
      // TODO Auto-generated constructor stub
   }
   private Amostra amostra;

   @ManyToOne
   @ForeignKey(name = "fk_amostra_item")
   @JoinColumn(name = "id_Amostra")
   public Amostra getAmostra()
   {
      return amostra;
   }

   public void setAmostra(Amostra amostra)
   {
      this.amostra = amostra;
   }
   
   @Override
   public ItemDeControle copiarStatus() {
       BeanUtilsBean copyBean = new BeanUtilsBean();
       ItemDeControleAmostra newStatus = null;
       try {
           newStatus = (ItemDeControleAmostra) copyBean.cloneBean(this);
           newStatus.setDtInicio(super.getDtInicio());
           newStatus.setDtFim(super.getDtFim());           
           newStatus.setTipoItemControle(super.getTipoItemControle());
           newStatus.setIdUser(super.cloneLong(super.getIdUser()));
           newStatus.setLimInfMetaOrientacaoEmb(super.cloneDouble(super.getLimInfMetaOrientacaoEmb()));
           newStatus.setLimSupMetaOrientacaoEmb(super.cloneDouble(super.getLimSupMetaOrientacaoEmb()));
           newStatus.setEmbarcado(super.cloneDouble(super.getEmbarcado()));
           newStatus.setValor(super.cloneDouble(super.getValor()));
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
