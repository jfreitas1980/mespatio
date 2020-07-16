package com.hdntec.gestao.batch.integracaoRPUSINAS;

import java.util.Date;
import java.util.TimerTask;

import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;


/**
 *
 * @author Bruno Gomes
 */
public class ExecutaIntegracaoSistemaRPUSINAS extends TimerTask{

    private ControladorIntegracaoSistemasExternos controlador;

    public ExecutaIntegracaoSistemaRPUSINAS(ControladorIntegracaoSistemasExternos controlador){
        this.controlador = controlador;
    }

    @Override
    public void run(){
        try {
            controlador.executaIntegracaoSistemaRPUSINAS(new Date(super.scheduledExecutionTime()));
        } catch (IntegracaoNaoRealizadaException ex) {
            System.out.println(ex.getMessage());
        }
        
    }

}
