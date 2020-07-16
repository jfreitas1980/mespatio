package com.hdntec.gestao.batch.integracaoSAP;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.batch.utilitarios.DSSStockyardTimeUtil;
import com.hdntec.gestao.batch.utilitarios.arquivo.PropertiesUtil;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;


/**
 *
 * @author Bruno Gomes
 */
public class VerificadorIntegracaoSAP {

    /**
     * construtor padrão
     */
    public VerificadorIntegracaoSAP(){
    }

    public void configuraIntegracaoSistemaSAP(ControladorIntegracaoSistemasExternos controlador){
        try {
            // cria uma classe calendário com a data corrente
            Calendar dataInitAtualizacaoSAP = Calendar.getInstance();
            //Seta a data inicial para o dia seguinte
            Date dataInicializacaoSAP = DSSStockyardTimeUtil.criaDataComString(PropertiesUtil.buscarPropriedade("data.inicio.atualizacao.sistema.sap"), PropertiesUtil.buscarPropriedade("formato.campo.data"));
            dataInitAtualizacaoSAP.setTime(dataInicializacaoSAP);

            // Seta a hora da inicializacao na data
            String horaInicializacaoSAP = PropertiesUtil.buscarPropriedade("hora.inicio.atualizacao.sistema.sap");
            dataInitAtualizacaoSAP.set(Calendar.HOUR, Integer.parseInt(horaInicializacaoSAP.split(":")[0]));
            dataInitAtualizacaoSAP.set(Calendar.MINUTE, Integer.parseInt(horaInicializacaoSAP.split(":")[1]));
            dataInitAtualizacaoSAP.set(Calendar.SECOND, Integer.parseInt(horaInicializacaoSAP.split(":")[2]));

            //instancia o timer
            Timer timerSAP = new Timer();

            //Intervalo para executar a classe novamente # 3600 segundo = 1 hora
            Double periodicidadeHorasSAP = Double.parseDouble(PropertiesUtil.buscarPropriedade("periodicidade.atualizacao.sistema.sap"));
            long periodoExecucaoSAP = Math.round(3600 * periodicidadeHorasSAP * 1000);

            // Agenda a tarefa para atualizacao do MES
            timerSAP.schedule(new ExecutaIntegracaoSistemaSAP(controlador), dataInicializacaoSAP, periodoExecucaoSAP);


        } catch (ValidacaoCampoException ex) {
            Logger.getLogger(VerificadorIntegracaoSAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
