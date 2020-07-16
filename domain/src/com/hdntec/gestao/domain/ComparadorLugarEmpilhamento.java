package com.hdntec.gestao.domain;

import java.util.Comparator;

import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;



/**
 * <P><B>Description :</B><BR>
 * General ComparadorStatusEntity
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 17/06/2010
 * @version $Revision: 1.1 $
 * @param <T>
 */
public class ComparadorLugarEmpilhamento<T> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        
    	  Integer order = ((LugarEmpilhamentoRecuperacao)o1).getOrdem();        
    	  Integer order1 = ((LugarEmpilhamentoRecuperacao)o2).getOrdem();
         
          if(order > order1)
              return 1;
          else if(order < order1)
              return -1;
          else
              return 0;      	
    }
}
