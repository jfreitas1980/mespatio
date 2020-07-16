package com.hdntec.gestao.batch.integracaoMES;

import java.util.Date;
import java.util.TimerTask;

import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;


/**
 *
 * @author Bruno Gomes
 */
public class ExecutaIntegracaoSistemaMES extends TimerTask{

    private ControladorIntegracaoSistemasExternos controlador;

    public ExecutaIntegracaoSistemaMES(ControladorIntegracaoSistemasExternos controlador){
        this.controlador = controlador;
    }

    @Override
    public void run(){
        try {
            controlador.executaIntegracaoSistemaMES(new Date(super.scheduledExecutionTime()));            
        } catch (IntegracaoNaoRealizadaException ex) {
            System.out.println(ex.getMessage());
        }
        
    }

}
