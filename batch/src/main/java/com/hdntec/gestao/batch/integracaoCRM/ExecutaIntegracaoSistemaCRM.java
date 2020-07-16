package com.hdntec.gestao.batch.integracaoCRM;

import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;


/**
 *
 * @author Bruno Gomes
 */
public class ExecutaIntegracaoSistemaCRM extends TimerTask{

    private ControladorIntegracaoSistemasExternos controlador;

    public ExecutaIntegracaoSistemaCRM(ControladorIntegracaoSistemasExternos controlador){
        this.controlador = controlador;
    }
    @Override
    public void run() {
        try {
            controlador.executaIntegracaoSistemaCRM(new Date(super.scheduledExecutionTime()));
        } catch (IntegracaoNaoRealizadaException ex) {
            Logger.getLogger(ExecutaIntegracaoSistemaCRM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
