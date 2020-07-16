package com.hdntec.gestao.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtilsBean;


@MappedSuperclass
public class StatusEntity<T> extends  AuditTrail implements IStatusEntity<T> {

	/** Serializacao do objeto */
	private static final long serialVersionUID = 1L;

	/** a data de criacao do obejto */
	private Long inicio;

	/** a data de criacao do obejto */
	private Long fim;

	@Transient
	public Date getDtInicio() {
	   Date result = null;
		if (inicio != null) {
			result = new Date(inicio);
	   }
    	return result;			
	}
	@Transient
	public void setDtInicio(Date dtInicio) 
	{		
		setInicio(dtInicio.getTime());
		this.setDtInsert(dtInicio);
		this.setIdUser(1l);
	}
	@Transient
	public Date getDtFim() {
		Date result = null;
		if (fim != null) {
			result = new Date(fim);
	   }
    	return result;	
	}
	@Transient
	public void setDtFim(Date dtFim) {
		if (dtFim != null)
		setFim(dtFim.getTime());
	}


    @SuppressWarnings("unchecked")
    @Override
    public T copiarStatus() {
        BeanUtilsBean copyBean = new BeanUtilsBean();
        T newStatus = null;
        try {
            newStatus = (T) copyBean.cloneBean(this);
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

        
    @Column(name = "DT_INICIO", nullable = false, insertable = true, updatable = true)
	public Long getInicio() {
		return inicio;
	}

	public void setInicio(Long inicio) {
		this.inicio = inicio;
	}
	
	@Column(name = "DT_FIM", nullable = true, insertable = true, updatable = true)
	public Long getFim() {
		return fim;
	}

	public void setFim(Long fim) {
		this.fim = fim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fim == null) ? 0 : fim.hashCode());
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;		
		if (obj ==null)
		    return false;
		if (getClass() != obj.getClass())
			return false;
		StatusEntity other = (StatusEntity) obj;
		if (fim == null) {
			if (other.fim != null)
				return false;
		} else if (!fim.equals(other.fim))
			return false;
		if (inicio == null) {
			if (other.inicio != null)
				return false;
		} else if (!inicio.equals(other.inicio))
			return false;
		return true;
	}	
}
