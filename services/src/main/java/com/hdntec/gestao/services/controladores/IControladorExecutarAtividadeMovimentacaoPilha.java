/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.services.controladores;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;

/**
 * <P><B>Description :</B><BR>
 * General IControladorExecutarAtividadeRecuperacao
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:paulo@cflex.com.br">paulo</a>
 * @since 10/06/2010
 * @version $Revision: 1.1 $
 */
public interface IControladorExecutarAtividadeMovimentacaoPilha {

    public Atividade movimentar(MovimentacaoVO movimentacaoVO) throws AtividadeException;
}
