package br.com.cflex.samarco.supervision.stockyard.relatorio.enums;

import com.lowagie.text.Font;

/**
 * 
 * <P><B>Description :</B><BR>
 * General EstiloFonteEnum
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 26/08/2009
 * @version $Revision: 1.1 $
 */
public enum EstiloFonteEnum {
	NEGRITO,
	NORMAL,
	ITALICO;
	
	/**
	 * 
	 * getEstilo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 26/08/2009
	 * @see
	 * @param estilo
	 * @return
	 * @return Returns the int.
	 */
	public int getEstilo() {
		int valor = Font.NORMAL;
		if (NEGRITO.equals(this)) {
			valor = Font.BOLD;
		}
		else if (ITALICO.equals(this)) {
			valor = Font.ITALIC;
		}
		return valor;
	}
}
