package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

/**
 * Classe que controla os eventos de seleção das balizas com click-arraste do mouse
 * @author Bruno Gomes
 */
public class ControladorEventoMouseBaliza {
    
    private boolean mousePressedFoiAtivado = Boolean.FALSE;

    /**  Construtor padrão*/
    public ControladorEventoMouseBaliza(){
    }
    
    public boolean isMousePressedFoiAtivado() {
        return mousePressedFoiAtivado;
    }

    public void setMousePressedFoiAtivado(boolean mousePressedFoiAtivado) {
        this.mousePressedFoiAtivado = mousePressedFoiAtivado;
    }


}
