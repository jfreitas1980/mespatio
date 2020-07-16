package com.hdntec.gestao.cliente.util.tabela;

import java.util.Vector;

public class ColunaTabela {
    
    /** Constantes referente ao tipo de editor de uma coluna */
    public static final Integer COL_TIPO_CHECKBOX = 1;
    public static final Integer COL_TIPO_RADIOBUTTON = 2;
    public static final Integer COL_TIPO_COMBOBOX = 3;
    public static final Integer COL_TIPO_TEXT = 4;
    public static final Integer COL_TIPO_COMPONENTE = 5;
    public static final Integer COL_TIPO_TEXT_LOST_FOCUS = 6;
    
    /** titulo da coluna da tabela */
    private String titulo;
    
    /** largura da coluna da tabela */
    private Integer largura;

    /** se a coluna vai ser editável ou não editável */
    private Boolean editar;
    
    /** se a coluna poderá ser redimensionada */
    private Boolean redimensionar;
    
    /** alinhamento da coluna: Utilizar SwingContants */
    private Integer alinhamento;
    
    /** se a coluna terá um tipo de editor. Ex: CheckBox, ComboBox, RadioButton */
    private Integer tipoEditor;

    /** dados usados para carregar os combos da coluna */
    private Vector vItensCombo;

    public Integer getAlinhamento() {
        return alinhamento;
    }

    public void setAlinhamento(Integer alinhamento) {
        this.alinhamento = alinhamento;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public Integer getLargura() {
        return largura;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }

    public Boolean getRedimensionar() {
        return redimensionar;
    }

    public void setRedimensionar(Boolean redimensionar) {
        this.redimensionar = redimensionar;
    }

    public Integer getTipoEditor() {
        return tipoEditor;
    }

    public void setTipoEditor(Integer tipoEditor) {
        this.tipoEditor = tipoEditor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Vector getVItensCombo() {
        return vItensCombo;
    }

    public void setVItensCombo(Vector vItensCombo) {
        this.vItensCombo = vItensCombo;
    }

}

