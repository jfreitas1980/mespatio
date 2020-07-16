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
public enum EnumCamposRelatorio {
	
	/*Imagem dos patios e pilhas*/
	IMAGEM_SITUACAO(0d),
	/*nomePilha*/
	PILHA(1.5d),
	/*nomePatio*/
	PATIO(1.5d),
	/*horarioInicioFormacao*/
	INICIO(2.5d),
	/*horarioFimFormacao*/
	FIM(2.5d),
	/*nomeCliente*/
	CLIENTE(3d),
	/*codigoFamiliaTipoProduto*/
	/*codigoTipoProduto*/
	PRODUTO(3d),
	/*quantidade*/
	TON(2d),
	/*tipoBaliza*/
	EMERG(2d),
	/*Baliza*/
	BALIZA(1.5d);
	
	
	private final double peso;
	EnumCamposRelatorio(double valor) {
		this.peso = valor;
	}
	
	
	public double getPeso() {
		return this.peso;
	}
	
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
		for( EnumCamposRelatorio value : EnumCamposRelatorio.values()) {
			result.add(value.toString());
		}
		return result;
	}
	
	/**
	 * 
	 * getNaoDefinidas
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 * @see
	 * @return
	 * @return Returns the double.
	 */
	public static double getPesoNaoDefinido() {
		return 1.5d;
	}
}
