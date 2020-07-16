package com.hdntec.gestao.domain.relatorio.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <P><B>Description :</B><BR>
 * General CamposRelatorioEnum
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 15/06/2009
 * @version $Revision: 1.1 $
 */
public enum CamposRelatorioEnum {

	nomePilha,
	nomePatio,
	horarioInicioFormacao,
	horarioFimFormacao,
	nomeCliente,
	codigoFamiliaTipoProduto,
	codigoTipoProduto,
	quantidade,
	tipoBaliza;
	
	/**
	 * 
	 * getValues
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 15/06/2009
	 * @see
	 * @return
	 * @return Returns the List<String>.
	 */
	public static List<String> getValues() {
		List<String> result = new ArrayList<String>(); 
		for( CamposRelatorioEnum value : CamposRelatorioEnum.values()) {
			result.add(value.toString());
		}
		return result;
	}
}
