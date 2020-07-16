package com.hdntec.gestao.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtilsBean;



public class AbstractMetaEntity<T> extends AuditTrail implements GenericMetaEntity<T>  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected List<T> listaStatus;

	private  ComparadorStatusEntity<T> comparadorStatus = new ComparadorStatusEntity<T>(); 
	protected List<Long> listaHoraInicioStatus;
	
	@Override
	public void incluirNovoStatus(T novoStatus, Date horaStatus){
		try { 
		// encontra o indice correspondente ao novo status
	      int indice = getIndiceIntervaloCorrespondente(horaStatus);
	      
	      // inclui o novo status na lista
	      ((StatusEntity)novoStatus).setDtInicio(horaStatus);
	      getListaStatus().add(indice, novoStatus);	      
	      if  (indice > 0) {
	    	  T statusAnterior =  getListaStatus().get(indice-1);
	    	  ((StatusEntity)statusAnterior).setDtFim(horaStatus);	    	  
	      }	      
	      getListaHoraInicioStatus();//.add(indice, horaStatus.getTime());
	      ordernar();
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}

	@Override
	public void removeStatusAPartirDeHorario(Date horario) {
		// TODO Auto-generated method stub
		  // pega o indice correspondente ao horario
		try { 
			
		int indice = getIndiceIntervaloCorrespondente(horario);
	      
	      // remove todos os elementos a partir do indice atual do status
	      int sizeLista = getListaHoraInicioStatus().size();
	      for (int i = indice; i < sizeLista; i++)
	      {
	    	 getListaHoraInicioStatus().remove(getListaHoraInicioStatus().size()-1);
	         getListaStatus().remove(getListaStatus().size()-1);
	      }
	      ordernar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public T retornaStatusHorario(Date horario)  {
		try { 
		    ordernar();	
		// TODO Auto-generated method stub
		 // pega o indice correspondente ao horario
	      int indice = getIndiceIntervaloCorrespondente(horario);
	      // se for o primeiro indice, verifica se o objeto ja tinha sido criado neste horario
	      if (getListaHoraInicioStatus().size() == 0) return null;
	      
	      if (indice == 0   &&  getListaHoraInicioStatus().get(indice) > horario.getTime())
	    	  return getListaStatus().get(indice);
	         //return null;
	      
	      // se for o ultimo indice, entao retorna o indice anterior
	      if (indice == getListaHoraInicioStatus().size())
	         return getListaStatus().get(indice-1);
	      
	      // se o horario encontrado corresponde exatamente ao horario passado como parametro entao devolve o status deste horario...
	      if (getListaHoraInicioStatus().get(indice) == horario.getTime())
	         return  getListaStatus().get(indice);
	      // ...senao retorna o status anterior ao indice encontrado
	      else
	         return  getListaStatus().get(indice-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	     return null;		    
	}
    @Override
    public void ordernar() {
        Collections.sort(this.getListaStatus(), comparadorStatus);
    }

	/** 
	    * Encontra o indice correspondente ao intervalo passado como parametro
	    */
	public int getIndiceIntervaloCorrespondente(Date horario)
	   {
	      int indice = Collections.binarySearch(getListaHoraInicioStatus(), horario.getTime());
	      
	      if (indice >= 0)
	         return indice;
	      else
	         return Math.abs(indice+1);
	   }
	@OrderBy("dtInicio")
	public List<T> getListaStatus() {
		if (listaStatus == null) {
			listaStatus = new ArrayList<T>();
		 }		
		return listaStatus;
	}

	public void setListaStatus(List<T> listaStatus) {
		this.listaStatus = listaStatus;
	}
    
	@Transient 
	public List<Long> getListaHoraInicioStatus() {
		 	  listaHoraInicioStatus = new ArrayList<Long>();		 
    		  for (T t : getListaStatus()) {		
    			  listaHoraInicioStatus.add(((StatusEntity)t).getDtInicio().getTime());
    		  }
    		  
    		  ordernar();
              
		return listaHoraInicioStatus;
	}

	public void setListaHoraInicioStatus(List<Long> listaHoraInicioStatus) {
		this.listaHoraInicioStatus = listaHoraInicioStatus;
	}

    @Override
    public T copiarStatus(T oldValue) {
        BeanUtilsBean copyBean = new BeanUtilsBean();
        T newStatus = null;
        try {
            newStatus = (T) copyBean.cloneBean(oldValue);
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

    @Override
    public T clonarStatus(Date horario) {
        // TODO Auto-generated method stub        
        T oldValue = this.retornaStatusHorario(horario);         
        T newValue = this.copiarStatus(oldValue);
        // inclui o estado
        this.incluirNovoStatus(newValue, horario);
        ((StatusEntity)oldValue).setDtFim(new Date(horario.getTime()));
        ((StatusEntity)newValue).setDtInicio(new Date(horario.getTime()));
        ((StatusEntity)newValue).setDtFim(null);
        ordernar();
        
        return newValue;
    }

    
    
    
	/** Retorna o tamanho da lista de status */
    public int getSizeListaStatus()
    {
    	return listaStatus.size();
    }
    
    /** Altera o valor do status posicionado no indice passado como parametro */
    public void alteraValorStatus(int indice, T novoValor)
    {
    	listaStatus.set(indice, novoValor);
    }
    
    @Override
    public Boolean removerStatus(T status)  {
		Boolean result = Boolean.FALSE;
    	try { 
    		result = getListaStatus().remove(status);	    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		    
	}
    
   
}
