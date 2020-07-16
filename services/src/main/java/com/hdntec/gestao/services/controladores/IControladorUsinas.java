/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.services.controladores;

import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * <P><B>Description :</B><BR>
 * General IControladorUsinas
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 02/09/2010
 * @version $Revision: 1.1 $
 */
public interface IControladorUsinas {
  
    public void salvarCampanha(Campanha campanha) throws ErroSistemicoException;
    
    public void removerCampanha(Campanha campanha) throws ErroSistemicoException;
}
