package com.hdntec.gestao.domain.produto.entity;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;


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
@Table(name = "ItemCtrOrientacao")
public class ItemDeControleOrientacaoEmbarque extends ItemDeControle
{

   public ItemDeControleOrientacaoEmbarque()
   {
      super();
   }
   private OrientacaoDeEmbarque orientacao;

   @ManyToOne
   @ForeignKey(name = "fk_orientacao_item")
   @JoinColumn(name = "id_Orientacao")
   public OrientacaoDeEmbarque getOrientacao()
   {
      return orientacao;
   }

   public void setOrientacao(OrientacaoDeEmbarque orientacao)
   {
      this.orientacao = orientacao;
   }
   
   @Override
   public ItemDeControle copiarStatus() {
       BeanUtilsBean copyBean = new BeanUtilsBean();
       ItemDeControleOrientacaoEmbarque newStatus = null;
       try {
           newStatus = (ItemDeControleOrientacaoEmbarque) copyBean.cloneBean(this);
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
