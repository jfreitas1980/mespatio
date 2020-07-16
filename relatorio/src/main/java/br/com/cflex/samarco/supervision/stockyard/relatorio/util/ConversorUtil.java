package br.com.cflex.samarco.supervision.stockyard.relatorio.util;

public class ConversorUtil {

	/**
	 * 
	 * convert
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param obj
	 * @return
	 * @return Returns the Double.
	 */
	public static Double convert(Object obj) {
		if (obj instanceof Double) {
			return (Double)obj;
		}
		return new Double(0);
	}
	
}
