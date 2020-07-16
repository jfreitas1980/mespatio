package com.hdntec.gestao.domain;

import java.util.Comparator;


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
public class ComparadorStatusEntity<T> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        return ((StatusEntity)o1).getDtInicio().compareTo(((StatusEntity)o2).getDtInicio());
    }
}
