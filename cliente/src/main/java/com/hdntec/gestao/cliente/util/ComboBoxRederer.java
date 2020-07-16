package com.hdntec.gestao.cliente.util;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * Classe que adiciona um tooltip a um comboBox;<p>
 * ie: String [] itens = {"por", "esp", "ing" };<br>
 * String [] tooltip = {"portugues, "espanhol", "ingles" };<br>
 * ComboBoxRederer renderer = new ComboBoxRederer();<br>
 * <b>renderer.setTooltips(tooltip);</b><br>
 * JComboBox combo = new JComboBox(itens);<br>
 * combo.setRenderer(renderer);
 *
 * @author Bruno Gomes
 */
public class ComboBoxRederer extends BasicComboBoxRenderer {
    /**lista que contem os dados do tooltip */
    private String[] tooltips;

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      if (isSelected) {
        setBackground(list.getSelectionBackground());
        setForeground(list.getSelectionForeground());
        if (-1 < index) {
          list.setToolTipText(tooltips[index]);
        }
      } else {
        setBackground(list.getBackground());
        setForeground(list.getForeground());
      }
      setFont(list.getFont());
      setText((value == null) ? "" : value.toString());
      return this;
    }

    public void setTooltips(String[] tooltips) {
        this.tooltips = tooltips;
    }

}
