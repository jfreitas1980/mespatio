package com.hdntec.gestao.batch.integracaoSAP;

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
public class ExecutaIntegracaoSistemaSAP extends TimerTask{

    private ControladorIntegracaoSistemasExternos controlador;

    public ExecutaIntegracaoSistemaSAP(ControladorIntegracaoSistemasExternos controlador){
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try {
            controlador.executaIntegracaoSistemaSAP(new Date(super.scheduledExecutionTime()));
        } catch (IntegracaoNaoRealizadaException ex) {
            Logger.getLogger(ExecutaIntegracaoSistemaSAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
