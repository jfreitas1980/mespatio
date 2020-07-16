package com.hdntec.gestao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class AbstractGenericDAO<T> implements GenericDAO<T> {

    
    @Override
    public T salvar(T clazz) throws HibernateException {
        try {
            HibernateUtil.beginTransaction();            
            HibernateUtil.getSession().saveOrUpdate(clazz);
            HibernateUtil.commitTransaction();
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            HibernateUtil.rollbackTransaction();
            throw hbex;
        } finally {
      //  	 encerrarSessao();
        }
        return clazz;
    }

    @Override
    public void atualizar(T clazz) throws HibernateException {
        try {
            HibernateUtil.beginTransaction();
            HibernateUtil.getSession().update(clazz);            
            HibernateUtil.commitTransaction();
        } catch (HibernateException hbex) {
        	HibernateUtil.rollbackTransaction();
            hbex.printStackTrace();
        } finally {
//        	encerrarSessao();
       }
    }

    @Override
    public void deletar(T clazz) throws HibernateException {
        try {
            HibernateUtil.beginTransaction();
            HibernateUtil.getSession().delete(clazz);            
            HibernateUtil.commitTransaction();
        } catch (HibernateException hbex) {
        	HibernateUtil.rollbackTransaction();
        	hbex.printStackTrace();
        	throw hbex;
        } finally {
//        	encerrarSessao();
       }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> buscarListaDeObjetos(final T objeto) throws HibernateException {
    	List<T> listaPesquisada = null;
    	try {
        	final Criteria c = HibernateUtil.getSession().createCriteria(objeto.getClass());
        	c.add(Example.create(objeto).enableLike(MatchMode.ANYWHERE).ignoreCase());
        	listaPesquisada = c.list();
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw hbex;
         } finally {
//       	 encerrarSessao();
       }
        	return listaPesquisada;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T buscarPorObjeto(final T objeto) throws HibernateException {
    	T objPesquisado = null;
    	try {
    	final Criteria c = HibernateUtil.getSession().createCriteria(objeto.getClass());
        c.add(Example.create(objeto).enableLike(MatchMode.ANYWHERE).ignoreCase());
        objPesquisado = (T) c.uniqueResult();        
    	 } catch (HibernateException hbex) {
             hbex.printStackTrace();
             throw hbex;
          } finally {
//        	 encerrarSessao();
        }
        return objPesquisado;
    }

    @Override
    public void encerrarSessao() throws HibernateException {
        HibernateUtil.closeSession();
    }

    @Override
    public List<T> buscarListaDeObjetosPorDatas(T objeto, String nomeCampo, Date dataInicial, Date dataFinal) {
    	List<T> listaPesquisada = null;
    	try {
    		Criteria crit = HibernateUtil.getSession().createCriteria(objeto.getClass());
    		crit.add(Restrictions.between(nomeCampo, dataInicial, dataFinal));
    		crit.add(Example.create(objeto).enableLike(MatchMode.ANYWHERE).ignoreCase());
    		crit.addOrder(Order.asc(nomeCampo));
    		listaPesquisada = crit.list();
    	} catch (HibernateException hbex) {
    		hbex.printStackTrace();
    		throw hbex;
    	} finally {
    		//        	 encerrarSessao();
    	}
        return listaPesquisada;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(Class<T> clazz) throws HibernateException {
        List<T> result = null;
        try {            
            Criteria crit = HibernateUtil.getSession().createCriteria(clazz); 
            crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            crit.setCacheable(true);            
            result = crit.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new HibernateException(e);
        }
        finally {
//       	 	encerrarSessao();
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAllDistinctyOff(Class<T> clazz) throws HibernateException {
        List<T> result = null;
        try {            
            Criteria crit = HibernateUtil.getSession().createCriteria(clazz);             
            crit.setCacheable(true);            
            result = crit.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new HibernateException(e);
        }
        finally {
//              encerrarSessao();
        }
        return result;
    }
    
    
    @Override
    public List<T> salvar(List<T> listaObjetos) throws HibernateException {
        List<T> listaObjetosSalvo = new ArrayList<T>();
        try {
            HibernateUtil.beginTransaction();
            for (T objeto : listaObjetos) {
                HibernateUtil.getSession().saveOrUpdate(objeto);                
                listaObjetosSalvo.add(objeto);
            }
            HibernateUtil.commitTransaction();
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            HibernateUtil.rollbackTransaction();
            throw hbex;
        }    
    finally {
//   	 	encerrarSessao();
    }
        return listaObjetosSalvo;
    }

    @Override
    public void remover(List<T> listaObjetos) throws HibernateException {
        try {
            HibernateUtil.beginTransaction();
            for (T objeto : listaObjetos) {
                HibernateUtil.getSession().delete(objeto);
                HibernateUtil.commitTransaction();
            }            
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            HibernateUtil.rollbackTransaction();
            throw hbex;
        }    
        finally {
//   	 		encerrarSessao();
    	}
    }

    @Override
    public void atualizar(List<T> listaObjetos) throws HibernateException {
        try {
            HibernateUtil.beginTransaction();
            for (T objeto : listaObjetos) {
                HibernateUtil.getSession().persist(objeto);
            }
            HibernateUtil.commitTransaction();
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            HibernateUtil.rollbackTransaction();
            throw hbex;
        }    
        finally {
//   	 		encerrarSessao();
    	}
    }
       
    @Override
    public T getObjeto(Class<T> clazz, Serializable id) throws HibernateException {
    	T objPesquisado = null;
    	try {
    	objPesquisado = (T)HibernateUtil.getSession().get(clazz, id);
      } catch (HibernateException hbex) {
    	  hbex.printStackTrace(); 
    	  throw hbex;
      }    
      	finally {
//	 		encerrarSessao();
      	}
    	return objPesquisado;
    }

	@Override
	public List<T> buscarListaDeObjetosPorCondicaoDiferenteDe(T objeto, String nomeCampo, String condicao) {
		List<T> listaPesquisada = null;
    	try {
    		 Criteria crit = HibernateUtil.getSession().createCriteria(objeto.getClass());
            crit.add(Restrictions.ne(nomeCampo , condicao));
            crit.add(Example.create(objeto).enableLike(MatchMode.ANYWHERE).ignoreCase());
//            crit.addOrder(Order.asc(nomeCampo));
            listaPesquisada = crit.list();
    		
    	}catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw hbex;
         } finally {
//       	 encerrarSessao();
       }
       return listaPesquisada;
	}

}
