package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * 
 * <P><B>Description :</B><BR>
 * General InterfaceMenuRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 18/08/2009
 * @version $Revision: 1.1 $
 */
public class InterfaceMenuRelatorio extends MouseAdapter  {

	private JPopupMenu popup;

	public InterfaceMenuRelatorio() {
		popup = new JPopupMenu();
	}
	
	/**
	 * Adiciona os itens do menu de relatorio
	 * addItemRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/08/2009
	 * @see
	 * @param menuItem
	 * @return Returns the void.
	 */
	public void addItemRelatorio(JMenuItem menuItem) {
		popup.add(menuItem);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//showPopup(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}
	
	/**
	 * 
	 * showPopup
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/08/2009
	 * @see
	 * @param e
	 * @return Returns the void.
	 */
	private void showPopup(MouseEvent e) {
		int posY = e.getComponent().getY();
		popup.show(e.getComponent(), 0, posY);
	}



}
