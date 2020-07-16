package br.com.cflex.supervision.stockyard.util;

import java.util.HashMap;
import java.util.Map;

import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoProduto;

/**
 * <P><B>Description :</B><BR>
 * General TipoProdutoUtil
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 07/07/2009
 * @version $Revision: 1.1 $
 */
public class TipoProdutoUtil {

    public static final String NOVO_TIPO_PRODUTO = "novoTipoProduto";

    public static final String ATUALIZAR_TIPO_PRODUTO = "atualizarTipoProduto";

    public static final String CANCELAR_TIPO_PRODUTO = "cancelarTipoProduto";
    
    private static Map<Long,TipoProduto> mapTipoProduto = new HashMap<Long,TipoProduto>();

    

    /**
     * 
     * getMapTipoProduto
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 14/07/2009
     * @see
     * @return
     * @return Returns the Map<Long,TipoProduto>.
     */
    public static Map<Long, TipoProduto> getMapTipoProduto() {
		return mapTipoProduto;
	}



    /**
     * 
     * setMapTipoProduto
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 14/07/2009
     * @see
     * @param mapTipoProduto
     * @return Returns the void.
     */
	public static void setMapTipoProduto(Map<Long, TipoProduto> mapTipoProduto) {
		TipoProdutoUtil.mapTipoProduto = mapTipoProduto;
	}



	/**
	 * 
	 * addTipoProduto
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 14/07/2009
	 * @see
	 * @param tipoProduto
	 * @return Returns the void.
	 */
	public static void addTipoProduto(TipoProduto tipoProduto) {
    	if ( mapTipoProduto.containsKey(tipoProduto.getIdTipoProduto()) ) {
    		mapTipoProduto.remove(tipoProduto.getIdTipoProduto());
    	}
    	mapTipoProduto.put(tipoProduto.getIdTipoProduto(), tipoProduto);
    }
	
	/**
	 * 
	 * getTipoProduto
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 15/07/2009
	 * @see
	 * @param id
	 * @return
	 * @return Returns the TipoProduto.
	 */
	public static TipoProduto getTipoProduto(Long id) {
		return mapTipoProduto.get(id);
	}
}