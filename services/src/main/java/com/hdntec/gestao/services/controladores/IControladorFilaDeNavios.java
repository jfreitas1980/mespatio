/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.services.controladores;

import java.util.Date;

import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.AtividadeException;

/**
 * <P><B>Description :</B><BR>
 * General IControladorFilaDeNavios
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 24/06/2010
 * @version $Revision: 1.1 $
 */
public interface IControladorFilaDeNavios {

    
    /**
     * recuperaFila
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 24/06/2010
     * @see
     * @param 
     * @return FilaDeNavios
     */
    public FilaDeNavios recuperaFila(Date hora);
    
    /**
     * @param movimentarNavioVO
     * @return
     * @throws AtividadeException
     */
    public Atividade movimentarNavio(AtividadeAtracarDesAtracarNavioVO movimentarNavioVO) throws AtividadeException;
}
