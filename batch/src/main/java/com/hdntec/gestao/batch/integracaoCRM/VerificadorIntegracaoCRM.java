
package com.hdntec.gestao.batch.integracaoCRM;

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
public class VerificadorIntegracaoCRM {

    /**
     * construtor padrao
     */
    public VerificadorIntegracaoCRM() {
    }

    public void configuraIntegracaoSistemaCRM(ControladorIntegracaoSistemasExternos controlador){
        try {
            // cria uma classe calendário com a data corrente
            Calendar dataIniAtualizacaoCRM = Calendar.getInstance();
            //Seta a data inicial para o dia seguinte
            Date dataInicializacaoCRM = DSSStockyardTimeUtil.criaDataComString(PropertiesUtil.buscarPropriedade("data.inicio.atualizacao.sistema.crm"),
                    PropertiesUtil.buscarPropriedade("formato.campo.data"));
            dataIniAtualizacaoCRM.setTime(dataInicializacaoCRM);
            
            // Seta a hora da inicializacao na data
            String horaInicializacaoCRM = PropertiesUtil.buscarPropriedade("hora.inicio.atualizacao.sistema.crm");
            dataIniAtualizacaoCRM.set(Calendar.HOUR, Integer.parseInt(horaInicializacaoCRM.split(":")[0]));
            dataIniAtualizacaoCRM.set(Calendar.MINUTE, Integer.parseInt(horaInicializacaoCRM.split(":")[1]));
            dataIniAtualizacaoCRM.set(Calendar.SECOND, Integer.parseInt(horaInicializacaoCRM.split(":")[2]));
            
            //Instancia o timer
            Timer timerCRM = new Timer();

            //Intervalo para executar a classe novamente # 60 segundos = 1 minuto
            Double periodicidadeMinutosCRM = Double.parseDouble(PropertiesUtil.buscarPropriedade("periodicidade.atualizacao.sistema.crm"));
            long periodoExecucaoCRM = Math.round(60 * periodicidadeMinutosCRM * 1000);

             //Agenda a tarefa para atualizacao do CRM
            timerCRM.schedule(new ExecutaIntegracaoSistemaCRM(controlador), dataIniAtualizacaoCRM.getTime(), periodoExecucaoCRM);

        } catch (ValidacaoCampoException ex) {
            Logger.getLogger(VerificadorIntegracaoCRM.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
